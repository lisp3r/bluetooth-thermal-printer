# Bluetooth Termal Printer

I love BLE stuff. It's awesome eseptially in reverse engenering context. In this repo I do reverse engineering of the [cute catish termal printer from Aliexpress](https://aliexpress.ru/item/1005002722811359.html). This includes transmission protocol and it's Android app because of images encoding.

Goals:

-[ ] Be able to pring from laptop
  -[x] Reverse the transmission protocol
  -[ ] Reverse the image's processing mechanism
- [ ] Printer autodefinifg
    - [ ] How iPring automatically defines which device is a printer?
    - [ ] Which information the printer advertizes?


- [Bluetooth Termal Printer](#bluetooth-termal-printer)
  - [The Cat Printer](#the-cat-printer)
    - [Bluetooth part](#bluetooth-part)
      - [Connect with bluetoothctl](#connect-with-bluetoothctl)
    - [Services](#services)
    - [Communication process](#communication-process)
    - [Connect with Python](#connect-with-python)
  - [Image](#image)
  - [iPrint reverse engenering](#iprint-reverse-engenering)


## The Cat Printer

 [ pic ]

It is supposed to be that the printer works with iPrint application. You connect your printer to the app, choose a picture to print, then iPrint process the image in some way and send it to a printer. Voila!

How to connect to the printer:

1. Long press to power on.
2. Green led starts to blink (it means the printer is not connected).
3. Connect to the printer (using `bluetoothctl` or just open iPrint).
4. Green led lights green - the printer is ready.

### Bluetooth part

#### Connect with bluetoothctl

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

### Services

The printer has two services:

    0xAE30
      Characterustics:
        UUID: 0xAE01 (Unknown, WRITE NO RESPONSE)
        UUID: 0xAE02 (Unknown, NOTIFY)
            Descriptors:
                UUID: 0x2902
        UUID: 0xAE03 (Unknown, WRITE NO RESPONSE)
        UUID: 0xAE04 (Unknown, NOTIFY)
            Descriptors:
                UUID: 0x2902
        UUID: 0xAE05 (Unknown, INDICATE)
            Descriptors:
                UUID: 0x2902
        UUID: 0xAE10 (Unknown, READ, WRITE)

    0xAE3A
      Characterustics:
        UUID: 0xAE3B (Unknown, WRITE NO RESPONSE)
        UUID: 0xAE3C (Unknown, NOTIFY)
            Descriptors:
                UUID: 0x2902

### Communication process

At this point I suppose only WRITE requests matters.

The following shows the communication process between iPrint and printer. Long story short, to print an image you should send three packages:

- change MTU to 123
- send `5178a80001000000ff5178a30001000000ff` on `0xae01`
- send `5178bb0001000107ff` on `0xae01`

You can find the pre-printing strings in `iPrint/classes/com/blueUtils/BluetoothUtils.java` if you ever dare to inspect this shitty code by yourself.

<details><summary>In details</summary>
<p>

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

7. Service: `0xae30`, Characteristic: `0xae01` (**WRITE NO RESPONSE**)

    *Start image transmission*. There is a chain of packets of 132 bytes each.

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
</p>
</details>

### Connect with Python

```py
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
```

## Image

How iPring encodes an image to print? Idk actually. Even now after reverse of iPrint and faking the entire process.

What do we see?

<details><summary>How to dump from wireshark</summary>
<p>

1. Export image's packages as JSON (Good filter: `bluetooth.addr==b7:26:a2:0d:ca:66`)

2. Extract hex data with Python

    ```py
    with open('reverse_stuff/rfid-metka-picture.json') as j_file:
        pic_json = json.load(j_file)

    pic_hex = [val['_source']['layers']['btatt']['btatt.value'] for val in pic_json]
    pic_hex = [val.replace(':', '') for val in pic_hex]

    # from arry to str
    pic_hex = ''.join(pic_hex)

    with open('reverse_stuff/rfid-metka-picture.pichex', 'w') as h_file:
        h_file.write(pic_hex)
    ```

</p>
</details>


- A printed image looks like Floyd–Steinberg dithering algorithm was used.
- No standard printer ESP/bla-bla codes.
- Image starts with 'headers':

      5178a30001000000ff
      5178a40001003399ff
      5178a6000b00aa551738445f5f5f44382ca1ff
      5178af000200e02e89ff
      5178be0001000000ff
      5178bd0001001e5aff
      5178bf0004007f7f7f03a8ff

- Image ends with 'footers':

      5178bf0004007f7f7f03a8ff
      5178bd000100194fff
      5178a10002003000f9ff
      5178a10002003000f9ff
      5178bd000100194fff
      5178a6000b00aa5517000000000000001711ff
      5178a30001000000ff

- Each line starts with `5178a2003000`.

## iPrint reverse engenering

To get the information about an image encoding I had reversed iPring app.

How to decompile:

    $ mkdir iPrint && cp iPrint\(com.frogtosea.iprint\)-1.1.0\(18\)-base.apk iPrint/ && cd iPrint
    $ mv iPrint\(com.frogtosea.iprint\)-1.1.0\(18\)-base.apk iPrint.zip && unzip iPrint.zip
    $ jadx classes.dex
