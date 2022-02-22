Bluetooth Termal Printer
=========================================
Project diary


My printer: https://aliexpress.ru/item/1005002722811359.html


- [Bluetooth Termal Printer](#bluetooth-termal-printer)
  - [Connect with bluetoothctl](#connect-with-bluetoothctl)
  - [Printer's bluetooth part](#printers-bluetooth-part)
    - [Services](#services)
    - [Network communication](#network-communication)
      - [Before a print](#before-a-print)
        - [About pre-printing write requests](#about-pre-printing-write-requests)
        - [About HCI package structure](#about-hci-package-structure)
      - [Print](#print)
  - [Write with Python and bluepy](#write-with-python-and-bluepy)
  - [Image](#image)
    - [How to comtile an image from .pcap](#how-to-comtile-an-image-from-pcap)
  - [TODO](#todo)


The printer work stages:

1. Long press to power on
2. Green led started to blink - the printer is not connected
3. Connect to the printer (using `bluetoothctl` or just open iPrint)
4. Green led lights green - the printer is ready

## Connect with bluetoothctl

    [bluetooth]# menu scan
    [bluetooth]# clear
    [bluetooth]# back
    [bluetooth]# scan on
    ...
    [NEW] Device B7:26:A2:0D:CA:66 GT01
    ...
    [bluetooth]# pair B7:26:A2:0D:CA:66
    Attempting to pair with B7:26:A2:0D:CA:66
    [CHG] Device B7:26:A2:0D:CA:66 Connected: yes
    [NEW] Primary Service (Handle 0xcad1)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004
        0000ae30-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Characteristic (Handle 0x2ace)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char0005
        0000ae01-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Characteristic (Handle 0x2ace)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char0007
        0000ae02-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Descriptor (Handle 0x2040)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char0007/desc0009
        00002902-0000-1000-8000-00805f9b34fb
        Client Characteristic Configuration
    [NEW] Characteristic (Handle 0x2ace)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char000a
        0000ae03-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Characteristic (Handle 0x2ace)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char000c
        0000ae04-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Descriptor (Handle 0x1c00)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char000c/desc000e
        00002902-0000-1000-8000-00805f9b34fb
        Client Characteristic Configuration
    [NEW] Characteristic (Handle 0x2ace)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char000f
        0000ae05-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Descriptor (Handle 0x1940)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char000f/desc0011
        00002902-0000-1000-8000-00805f9b34fb
        Client Characteristic Configuration
    [NEW] Characteristic (Handle 0x2ace)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0004/char0012
        0000ae10-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Primary Service (Handle 0xcad1)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0040
        0000ae3a-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Characteristic (Handle 0x2ace)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0040/char0041
        0000ae3b-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Characteristic (Handle 0x2ace)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0040/char0043
        0000ae3c-0000-1000-8000-00805f9b34fb
        Unknown
    [NEW] Descriptor (Handle 0xc360)
        /org/bluez/hci0/dev_B7_26_A2_0D_CA_66/service0040/char0043/desc0045
        00002902-0000-1000-8000-00805f9b34fb
        Client Characteristic Configuration
    [CHG] Device B7:26:A2:0D:CA:66 UUIDs: 00001800-0000-1000-8000-00805f9b34fb
    [CHG] Device B7:26:A2:0D:CA:66 UUIDs: 0000ae30-0000-1000-8000-00805f9b34fb
    [CHG] Device B7:26:A2:0D:CA:66 UUIDs: 0000ae3a-0000-1000-8000-00805f9b34fb
    [CHG] Device B7:26:A2:0D:CA:66 ServicesResolved: yes
    [CHG] Device B7:26:A2:0D:CA:66 Paired: yes

## Printer's bluetooth part

### Services

It has two services:

1. 0xAE30
   Characterustics:
   1. UUID: 0xAE01 (Unknown, WRITE NO RESPONSE)
   2. UUID: 0xAE02 (Unknown, NOTIFY)
      Descriptors:
      1. UUID: 0x2902
   3. UUID: 0xAE03 (Unknown, WRITE NO RESPONSE)
   4. UUID: 0xAE04 (Unknown, NOTIFY)
      Descriptors:
      1. UUID: 0x2902
   5. UUID: 0xAE05 (Unknown, INDICATE)
      Descriptors:
      1. UUID: 0x2902
   6. UUID: 0xAE10 (Unknown, READ, WRITE)
2. 0xAE3A
   Characterustics:
   1. UUID: 0xAE3B (Unknown, WRITE NO RESPONSE)
   2. UUID: 0xAE3C (Unknown, NOTIFY)
      Descriptors:
      1. UUID: 0x2902

### Network communication

At this point I suppose only WRITE requests matters.

#### Before a print

1. Service: `0xae30`, Characteristic: `0xae02` (**NOTIFY**), Desc: `0x2902`

    *Enables notification*

   1. HEX:

          0000   02 0e 00 09 00 05 00 04 00 12 09 00 01 00
          0000   01 00

   2. Plain Text:

          localhost ()	b7:26:a2:0d:ca:66 ()	ATT	14	Sent Write Request, Handle: 0x0009 (Unknown: Unknown: Client Characteristic Configuration)

          Frame 70: 14 bytes on wire (112 bits), 14 bytes captured (112 bits)
          ...
          Bluetooth Attribute Protocol
              Opcode: Write Request (0x12)
                  0... .... = Authentication Signature: False
                  .0.. .... = Command: False
                  ..01 0010 = Method: Write Request (0x12)
              Handle: 0x0009 (Unknown: Unknown: Client Characteristic Configuration)
                  [Service UUID: Unknown (0xae30)]
                  [Characteristic UUID: Unknown (0xae02)]
                  [UUID: Client Characteristic Configuration (0x2902)]
              Characteristic Configuration Client: 0x0001, Notification
                  0000 0000 0000 00.. = Reseved: 0x0000
                  .... .... .... ..0. = Indication: False
                  .... .... .... ...1 = Notification: True

2. Service: `0xae30`, Characteristic: `0xae04` (**NOTIFY**), Desc: `0x2902`

   *also enables notifications ...*

3. Service: `0xae30`, Characteristic: `0xae05` (**INDICATE**), Desc: `0x2902`

    *manipulations with led?*

   1. HEX:

          0000   02 0e 00 09 00 05 00 04 00 12 11 00 02 00
          0000   02 00

   2. Plain Text:

          Frame 78: 14 bytes on wire (112 bits), 14 bytes captured (112 bits)
          ...
          Bluetooth Attribute Protocol
              Opcode: Write Request (0x12)
                  0... .... = Authentication Signature: False
                  .0.. .... = Command: False
                  ..01 0010 = Method: Write Request (0x12)
              Handle: 0x0011 (Unknown: Unknown: Client Characteristic Configuration)
                  [Service UUID: Unknown (0xae30)]
                  [Characteristic UUID: Unknown (0xae05)]
                  [UUID: Client Characteristic Configuration (0x2902)]
              Characteristic Configuration Client: 0x0002, Indication
                  0000 0000 0000 00.. = Reseved: 0x0000
                  .... .... .... ..1. = Indication: True
                  .... .... .... ...0 = Notification: False

4. Exchange MTU Request, Client Rx MTU: 123

5. Service: `0xae30`, Characteristic: `0xae01` (**WRITE NO RESPONSE**)

    *First pre-printing write request.*

   1. HEX:

          0000   02 0e 00 19 00 15 00 04 00 52 06 00 51 78 a8 00
          0010   01 00 00 00 ff 51 78 a3 00 01 00 00 00 ff

          0000   51 78 a8 00 01 00 00 00 ff 51 78 a3 00 01 00 00
          0010   00 ff

   2. Plain Text:

          Frame 89: 30 bytes on wire (240 bits), 30 bytes captured (240 bits)
          ...
          Bluetooth Attribute Protocol
              Opcode: Write Command (0x52)
                  0... .... = Authentication Signature: False
                  .1.. .... = Command: True
                  ..01 0010 = Method: Write Request (0x12)
              Handle: 0x0006 (Unknown: Unknown)
                  [Service UUID: Unknown (0xae30)]
                  [UUID: Unknown (0xae01)]
              Value: 5178a80001000000ff5178a30001000000ff

    In HEX translation the value comes after `02 0e 00 19 00 15 00 04 00 52 06 00` unknown header.

6. Service: `0xae30`, Characteristic: `0xae01` (**WRITE NO RESPONSE**)

    *Second pre-printing write request*

   1. HEX:

          0000   02 0e 00 10 00 0c 00 04 00 52 06 00 51 78 bb 00
          0010   01 00 01 07 ff

        The value also starts with `'51 78'`.

   2. Plain Text:

          Frame 93: 21 bytes on wire (168 bits), 21 bytes captured (168 bits)
          ...
          Bluetooth Attribute Protocol
              Opcode: Write Command (0x52)
                  0... .... = Authentication Signature: False
                  .1.. .... = Command: True
                  ..01 0010 = Method: Write Request (0x12)
              Handle: 0x0006 (Unknown: Unknown)
                  [Service UUID: Unknown (0xae30)]
                  [UUID: Unknown (0xae01)]
              Value: 5178bb0001000107ff

##### About pre-printing write requests

You can find the pre-printing headers in `iPrint/classes/com/blueUtils/BluetoothUtils.java` if you ever dare to inspect this shitty code.



##### About HCI package structure

Full package structure from Wireshark:

    Frame 94: 21 bytes on wire (168 bits), 21 bytes captured (168 bits) on interface /tmp/wireshark_extcap_android, id 0
        Interface id: 0 (/tmp/wireshark_extcap_android)
        Encapsulation type: Bluetooth H4 with linux header (99)
        Arrival Time: Sep  4, 2021 19:54:59.742056000 MSK
        [Time shift for this packet: 0.000000000 seconds]
        Epoch Time: 1630774499.742056000 seconds
        [Time delta from previous captured frame: 0.007720000 seconds]
        [Time delta from previous displayed frame: 0.007720000 seconds]
        [Time since reference or first frame: 1.776077000 seconds]
        Frame Number: 94
        Frame Length: 21 bytes (168 bits)
        Capture Length: 21 bytes (168 bits)
        [Frame is marked: False]
        [Frame is ignored: False]
        Point-to-Point Direction: Sent (0)
        [Protocols in frame: bluetooth:hci_h4:bthci_acl:btl2cap:btatt]
    Bluetooth
        [Source: 00:00:00_00:00:00 (00:00:00:00:00:00)]
        [Destination: b7:26:a2:0d:ca:66 (b7:26:a2:0d:ca:66)]
    Bluetooth HCI H4
        [Direction: Sent (0x00)]
        HCI Packet Type: ACL Data (0x02)
    Bluetooth HCI ACL Packet
        .... 0000 0000 0101 = Connection Handle: 0x005
        ..00 .... .... .... = PB Flag: First Non-automatically Flushable Packet (0)
        00.. .... .... .... = BC Flag: Point-To-Point (0)
        Data Total Length: 16
        Data
        [Connect in frame: 3]
        [Source BD_ADDR: 00:00:00_00:00:00 (00:00:00:00:00:00)]
        [Source Device Name: ]
        [Source Role: Unknown (0)]
        [Destination BD_ADDR: b7:26:a2:0d:ca:66 (b7:26:a2:0d:ca:66)]
        [Destination Device Name: ]
        [Destination Role: Unknown (0)]
        [Current Mode: Unknown (-1)]
    Bluetooth L2CAP Protocol
        Length: 12
        CID: Attribute Protocol (0x0004)
    Bluetooth Attribute Protocol
        Opcode: Write Command (0x52)
            0... .... = Authentication Signature: False
            .1.. .... = Command: True
            ..01 0010 = Method: Write Request (0x12)
        Handle: 0x0006 (Unknown: Unknown)
            [Service UUID: Unknown (0xae30)]
            [UUID: Unknown (0xae01)]
        Value: 5178bb0001000107ff

HEX translation of the package above:

    0000   02 05 00 10 00 0c 00 04 00 52 06 00 51 78 bb 00   .........R..Qx..
    0010   01 00 01 07 ff                                    .....

As HCI Packet:

            02            05 00 10 00     0c 00 04 00     52 06 00   51 78 bb 00 01 00 01 07 ff
    | HCI package type | HCI ACL header | L2CAP header | ATT header |          value           |

HCI Packet Types:

| packet            | type |
| ----------------- | ---- |
| Command           | 0x01 |
| Asynchronous Data | 0x02 |
| Synchronous Data  | 0x03 |
| Event             | 0x04 |
| Extended Command  | 0x09 |

HCI ACL (0x02) package structute:

- Handle
- Flags
  - BP Flag: ...
  - BC Flag: ...
- Data total length (16 bytes)
- Data (Incapsulates Bluetooth L2CAP package)

L2CAP package structute:

- Length (0-16 bits)
- CID (17-32 bits)
- Information payload (Incapsulates ATT package)

CID: 0x0004 means there is L2CAP data (ATT) frame.

ATT package structure:

- ATT opcode
- Handle
- Value

#### Print

Packets each of 132 bytes.

Service: `0xae30`, Characteristic: `0xae01` (**WRITE NO RESPONSE**)

    Bluetooth Attribute Protocol
        Opcode: Write Command (0x52)
            0... .... = Authentication Signature: False
            .1.. .... = Command: True
            ..01 0010 = Method: Write Request (0x12)
        Handle: 0x0006 (Unknown: Unknown)
            [Service UUID: Unknown (0xae30)]
            [UUID: Unknown (0xae01)]
        Value: 5178a30001000000ff5178a40001003399ff5178a6000b00aa551738445f5f5f44382ca1…

HEX:

`Value` starts with `'51 78'`

    0000   02 05 00 7f 00 7b 00 04  00 52 06 00 51 78 a3 00
    0010   01 00 00 00 ff 51 78 a4  00 01 00 33 99 ff 51 78
    0020   a6 00 0b 00 aa 55 17 38  44 5f 5f 5f 44 38 2c a1
    0030   ff 51 78 af 00 02 00 e0  2e 89 ff 51 78 be 00 01
    0040   00 00 00 ff 51 78 bd 00  01 00 1e 5a ff 51 78 bf
    0050   00 04 00 7f 7f 7f 03 a8  ff 51 78 bf 00 04 00 7f
    0060   7f 7f 03 a8 ff 51 78 bf  00 04 00 7f 7f 7f 03 a8
    0070   ff 51 78 bf 00 04 00 7f  7f 7f 03 a8 ff 51 78 bf
    0080   00 04 00 7f

## Write with Python and bluepy

    import codecs, json, six
    from bluepy import btle


    printer = btle.Peripheral(deviceAddr='B7:26:A2:0D:CA:66')

    # Set MTU
    resp = printer.setMTU(123)

    # Write some params
    printer.writeCharacteristic(6, codecs.decode('5178a80001000000ff5178a30001000000ff', 'hex'))
    printer.writeCharacteristic(6, codecs.decode('5178bb0001000107ff', 'hex'))

    # Print image by 120 len parts

    for hex_str from hex_img:
        printer.writeCharacteristic(6, codecs.decode(hex_str, 'hex'))

    printer.disconnect()

## Image

How image is decoded? Who knows?

Reverse image:

    with open('rfid_metka.json') as j_file:
        rfid_metka_json = json.load(j_file)

    rfid_metka_hex = [val['_source']['layers']['btatt']['btatt.value'] for val in rfid_metka_json]
    rfid_metka_hex = [val.replace(':', '') for val in rfid_metka_hex]

    # from arry to str
    rfid_metka_hex = ''.join(rfid_metka_hex)
    with open('ble_printer/rfid-metka-picture.pichex', 'w') as h_file:
        h_file.write(rfid_metka_hex)

Image encoded to an array of dots with *Floyd–Steinberg dithering*.

Data stream starts and ands with `5178a30001000000ff`.

### How to comtile an image from .pcap

1. Export image's packages as JSON (Good filter: `bluetooth.addr==b7:26:a2:0d:ca:66`)

Image starts with 'headers':

        5178a30001000000ff
        5178a40001003399ff
        5178a6000b00aa551738445f5f5f44382ca1ff
        5178af000200e02e89ff
        5178be0001000000ff
        5178bd0001001e5aff
        5178bf0004007f7f7f03a8ff

Ends with 'footers':

        5178bd000100194fff
        5178a10002003000f9ff
        5178a10002003000f9ff
        5178bd000100194fff
        5178a6000b00aa5517000000000000001711ff
        5178a30001000000ff


## iPrint reverse engenering

To get the information about an image encoding I had reversed iPring app.

How to decompile:

    $ mkdir iPrint && cp iPrint\(com.frogtosea.iprint\)-1.1.0\(18\)-base.apk iPrint/ && cd iPrint
    $ mv iPrint\(com.frogtosea.iprint\)-1.1.0\(18\)-base.apk iPrint.zip && unzip iPrint.zip
    $ jadx classes.dex



## TODO

- [ ] Printer autodefinifg
    - [ ] How iPring automatically defines which device is a printer?
    - [ ] Which information the printer advertizes?


p=$(pidof com.frogtosea.iprint); echo "Getting logs from $p"; logcat --pid=$p
