# Application

Device name: GT01
Paper size:  384 (dot) (57*30 мм)
Print size:  384 (dot)
DPI:         200 (точек/дюйм)
Img print speed:  55 or 30
Text print speed: 45 or 25
image mtu:   123
text mtu:    83 (or 123?)
interval:    4


oneLength:  8   // ?
size: 2         // ?

Log tag: AndroidBLE


## Classes

- [AppApplication.java](iPrint/classes/com/Application/AppApplication.java)

        private BluetoothUtils ble;

        public void onCreate() {
            ble = BluetoothUtils.getInstance(this);
            mBLESPPUtils = new BLESPPUtils(this);
            mBLESPPUtils.setStopString("\r\n");
            mBLESPPUtils.onCreate();

- [MainActivity.java](iPrint/classes/com/activity/MainActivity.java) - starts some activities

- [PrinterModel.java](iPrint/classes/com/blueUtils/PrinterModel.java)
    - `PrinterModel.DataBean()`
    - `PrinterModel.getModel("GT01")` -> `DataBean("GT01", ...)`

- [PrintBean.java](iPrint/classes/com/Beans/PrintBean.java) - useless

- [WriteRequest.java](iPrint/classes/cn/com/heaton/blelibrary/ble/request/WriteRequest.java) - thing that does print

- [ImageUtils](iPrint/classes/com/blankj/utilcode/util/ImageUtils.java) - some operations with bitmap

- [PrintPreviewActivity.java](iPrint/classes/com/activity/PrintPreviewActivity.java)
- [ImageDisposeUtil.java](iPrint/classes/com/Utils/ImageDisposeUtil.java)
- [ViewToBitmap.java](iPrint/classes/com/Utils/ViewToBitmap.java)
- [LocalMedia.java](iPrint/classes/com/luck/picture/lib/entity/LocalMedia.java)
- [Code.java](iPrint/classes/com/Utils/Code.java)

### PrinterModel.java

DataBean represents device's cherecteristics (name, MTU, dpi, paper size, etc).

```java
public DataBean(
        String modelNo,        // GT01
        String modelName,      // " "
        int model,             // 0
        int size,              // 2
        int paperSize,         // 384
        int printSize,         // 384
        int oneLength,         // 8
        String headName,       // GT01-
        boolean canChangeMTU,  // true
        int devdpi,            // 200
        int imgPrintSpeed,     // 55 or 30
        int textPrintSpeed,    // 45 or 25
        int imgMTU,            // 123
        int textMTU,           // 83
        boolean newCompress,   // true
        int paperNum,          // 2
        int interval,          // 4
        boolean useSPP,        // false
        int thinEneragy,       // 12000
        int moderationEneragy, // 12000
        int deepenEneragy,     // 12000
        boolean hasId          // true
)

public static DataBean getModel(String s) // s == "GT01"
    {
    ...
        if("GT01".equals(s))
            if("jingzhunxuexi_wand".equals(StringUtils.getString(0x7f0e0030)))
                return new DataBean("GT01", " ", 0, 2, 384, 384, 8, "GT01-", true, 200, 55, 45, 123, 83, true, 2, 4, false, 12000, 12000, 12000, true);
            else
                return new DataBean("GT01", " ", 0, 2, 384, 384, 8, "GT01-", true, 200, 30, 25, 123, 83, true, 2, 4, false, 12000, 12000, 12000, true);
    }
```

## Image

```java
package com.askjeffreyliu.floydsteinbergdithering;
import android.graphics.*;

public class Utils {

    public static Bitmap toGrayscale(Bitmap bitmap) {
        int i = bitmap.getHeight();
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), i, android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint();
        ColorMatrix colormatrix = new ColorMatrix();
        colormatrix.setSaturation(0.0F);
        paint.setColorFilter(new ColorMatrixColorFilter(colormatrix));
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        return bitmap1;
    }

    private static native void binaryBlackAndWhiteNative(Bitmap bitmap);
    private static native void floydSteinbergNative(Bitmap bitmap);

    public static Bitmap binaryBlackAndWhite(Bitmap bitmap) {
        bitmap = toGrayscale(bitmap);
        binaryBlackAndWhiteNative(bitmap);
        return bitmap;
    }

    public static Bitmap floydSteinbergDithering(Bitmap bitmap) {
        bitmap = toGrayscale(bitmap);
        floydSteinbergNative(bitmap);
        return bitmap;
    }

    static {
        System.loadLibrary("fsdither");
    }
}
```

fsdither - Floyd–Steinberg dithering is an image dithering algorithm

    public static Bitmap createBitmap (int width, int height, Bitmap.Config config)

`ARGB_8888` - Each pixel is stored on 4 bytes (RGBA).
Each channel (RGB and alpha for translucency) is stored with 8 bits of precision (256 possible values.)
This configuration is very flexible and offers the best quality. It should be used whenever possible.

Use this formula to pack into 32 bits:

    int color = (A & 0xff) << 24 | (B & 0xff) << 16 | (G & 0xff) << 8 | (R & 0xff);

In python to grayscale:

    img = Image.open(p).convert('LA')
    img.save('greyscale.png')

`RGB_565` - Each pixel is stored on 2 bytes (RGB): red is stored with 5 bits of precision (32 possible values), green is stored with 6 bits of precision (64 possible values) and blue is stored with 5 bits of precision.

```java
public static Bitmap textBitmap(Bitmap bitmap)
    {
        if(bitmap == null)
            return null;
        int i = bitmap.getWidth();
        int j = bitmap.getHeight();
        int ai[] = new int[i * j];
        int k = 0;
        do
        {
            if(k >= j)
                break;
            for(int l = 0; l < i; l++)
            {
                int k1;
label0:
                {
                    int i2;
                    int l2;
                    int l3;
                    int i4;
label1:
                    {
                        int i1 = bitmap.getPixel(l, k);
                        k1 = i1;
                        if(i1 == -1)
                            break label0;
                        k1 = i1;
                        if(l == 0)
                            break label0;
                        k1 = i1;
                        if(k == 0)
                            break label0;
                        k1 = i1;
                        if(l == i - 1)
                            break label0;
                        k1 = i1;
                        if(k == j - 1)
                            break label0;
                        int l1 = l - 1;
                        i2 = (k - 1) * i;
                        k1 = l1 + i2;
                        int k2 = ai[k1];
                        l2 = k * i;
                        int i3 = l1 + l2;
                        int k3 = ai[i3];
                        l3 = (k + 1) * i;
                        i4 = i1;
                        if((ai[l1 + l3] + 255 & (k2 & k3 + 255) & 0xff) != 0)
                            break label1;
                        k3 = ai[l + 2 + l2];
                        k2 = ai[l + 1 + l2];
                        l1 = ai[i3];
                        i3 = ai[(l - 2) + l2];
                        i4 = i1;
                        if((k2 & 0xff) != 0)
                            break label1;
                        i4 = i1;
                        if((l1 & 0xff) != 0)
                            break label1;
                        if((k3 & 0xff) == 0)
                        {
                            i4 = i1;
                            if((i3 & 0xff) == 0)
                                break label1;
                        }
                        i4 = ai[l + l2];
                        i4 = -1;
                    }
                    int j3 = ai[k1];
                    int j1 = l + i2;
                    k1 = i4;
                    if((j3 & ai[j1] + 255 & ai[l + 1 + i2] + 255 & 0xff) == 0)
                    {
                        int j2 = ai[l3 + l];
                        j1 = ai[j1];
                        k1 = i4;
                        if((j2 & 0xff) == 255)
                        {
                            k1 = i4;
                            if((j1 & 0xff) == 0)
                            {
                                int j4 = ai[l2 + l];
                                StringBuilder stringbuilder = new StringBuilder();
                                stringbuilder.append("tmppixel---");
                                stringbuilder.append(-1);
                                stringbuilder.append("---pixel---");
                                stringbuilder.append(j4);
                                LogUtils.e(new Object[] {
                                    stringbuilder.toString()
                                });
                                k1 = -1;
                            }
                        }
                    }
                }
                ai[k * i + l] = k1;
            }

            k++;
        } while(true);
        return Bitmap.createBitmap(ai, i, j, android.graphics.Bitmap.Config.RGB_565);
    }
```

hex(12000) // '0x2ee0'
bytearray.fromhex('2ee0') // b'.\xe0'
bytearray.fromhex('2ee0')[0] // 46
bytearray.fromhex('2ee0')[1] // 224

byte print_text[] = {
    81, 120, -66, 0, 1, 0, 1, 7, -1
}

byte print_img[] = {
    81, 120, -66, 0, 1, 0, 0, 0, -1
}

final byte paper_300dpi[] = {
    81, 120, -95, 0, 2, 0, 72, 0, -13, -1
}

abyte1 = new byte[width * k + print_text.length + 9 + 10];
byte abyte2[] = new byte[10];
abyte2[] = {
    81, 120, -81, 0, 2, 0, 46, 224, BluetoothOrder.calcCrc8(abyte2, 6, 2), -1
}
            abyte2[0] = (byte)81;
            abyte2[1] = (byte)120;
            abyte2[2] = (byte)-81;
            abyte2[3] = (byte)0;
            abyte2[4] = (byte)2;
            abyte2[5] = (byte)0;
            abyte2[6] = ConvertUtils.hexString2Bytes(Integer.toHexString(getEneragy()))[1];
            abyte2[7] = ConvertUtils.hexString2Bytes(Integer.toHexString(getEneragy()))[0];
            abyte2[8] = BluetoothOrder.calcCrc8(abyte2, 6, 2);
            abyte2[9] = (byte)-1;

BluetoothOrder.calcCrc8(abyte2, 6, 2);
public static byte calcCrc8(byte abyte0[], int i, int j, byte byte0)
    {
        byte byte1 = byte0; // 0
        byte0 = 6;
        byte byte2;
        for(byte2 = byte1; byte0 < 8; byte2 = byte1)
        {
            byte1 = CHECKSUM_TABLE[(byte2 ^ abyte0[byte0]) & 0xff];
            byte0++;
        }

        return byte2;
    }

```java

public void BitmapToData(final ArrayList printList, final int width, int i, final int concentration, boolean flag) {
        final PrinterModel.DataBean model = AppApplication.getPrinterModel();

        this.concentration = concentration; // 3

        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("concentration---");
        stringbuilder.append(3);

        newCompress = flag; // true
        dataIdentify = new ArrayList();
        (new Thread(new Runnable() {
            public void run() {
                ArrayList arraylist = new ArrayList();
                Vector vector = new Vector();
                vector.addAll(printList);
                packageLength = 28;

                SPUtils sputils = SPUtils.getInstance();
                StringBuilder stringbuilder2 = new StringBuilder();
                stringbuilder2.append("dev_con");
                stringbuilder2.append(SPUtils.getInstance().getString("defDeviceAddress"));
                int j = sputils.getInt(stringbuilder2.toString(), 0);
                if(j > 0) {
                    j = ((Integer)PrinterModel.getCustomEnergy(model.getModelNo()).get((j - 1) * 2)).intValue();
                    // PrinterModel.getCustomEnergy(model.getModelNo() = [7680, 7680, 9600, 9600, 12000, 12000, 14400, 14400, 17280, 17280, 20000, 20000, 20000, 20000]
                    setEneragy(j);
                } else {
                    setEneragy(model.getEneragy(3)); // 12000
                }

                int l = 0;
                int k;
                int i1;
                for(k = 0; l < vector.size(); k = i1)
                {
                    PrintBean printbean = (PrintBean)vector.get(l);
                    i1 = k;
                    if(printbean.isShow())
                    {
                        i1 = k;
                        if(printbean.getBitmap() != null)
                        {
                            byte abyte2[] = PrintDataUtils.bitmapToBWPix(printbean.getBitmap());
                            if(eneragyList != null && eneragyList.size() > 0)
                            {
                                i1 = ((Integer)eneragyList.get(l)).intValue();
                                setEneragy(i1);
                            }
                            abyte2 = eachLinePixToCmdB(abyte2, width, printbean.getPrintType());
                            i1 = k;
                            if(printbean.isAddWhite())
                            {
                                i1 = k + BluetoothOrder.paper.length;
                                arraylist.add(BluetoothOrder.paper);
                            }
                            i1 += abyte2.length;
                            arraylist.add(abyte2);
                        }
                    }
                    l++;
                }
                if(eneragyList != null)
                    eneragyList.clear();
                setEneragy(0);
                byte abyte1[] = new byte[(k + BluetoothOrder.paper.length * 2 + 9) * finalPrintNum + 9 + BluetoothOrder.paper.length * (model.getPaperNum() - 2) + BluetoothOrder.printLattice.length + BluetoothOrder.finishLattice.length + 9 + BluetoothOrder.getDevState.length * 2];
                System.arraycopy(BluetoothOrder.getDevState, 0, abyte1, 0, BluetoothOrder.getDevState.length);
                k = BluetoothOrder.getDevState.length + 0;
                byte abyte3[] = BluetoothOrder.getBlackening(concentration);
                System.arraycopy(abyte3, 0, abyte1, k, abyte3.length);
                k += abyte3.length;
                System.arraycopy(BluetoothOrder.printLattice, 0, abyte1, k, BluetoothOrder.printLattice.length);
                k += BluetoothOrder.printLattice.length;
                for(l = 0; l < finalPrintNum; l++)
                {
                    for(Iterator iterator = arraylist.iterator(); iterator.hasNext();)
                    {
                        byte abyte4[] = (byte[])iterator.next();
                        System.arraycopy(abyte4, 0, abyte1, k, abyte4.length);
                        k += abyte4.length;
                    }

                    byte abyte5[] = feedPaper(25);
                    System.arraycopy(abyte5, 0, abyte1, k, abyte5.length);
                    k += abyte5.length;
                    if(model.getDevdpi() == 200)
                        System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
                    else
                    if(model.getDevdpi() == 300)
                        System.arraycopy(BluetoothOrder.paper_300dpi, 0, abyte1, k, BluetoothOrder.paper_300dpi.length);
                    else
                        System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
                    k += BluetoothOrder.paper.length;
                    if(model.getDevdpi() == 200)
                        System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
                    else
                    if(model.getDevdpi() == 300)
                        System.arraycopy(BluetoothOrder.paper_300dpi, 0, abyte1, k, BluetoothOrder.paper_300dpi.length);
                    else
                        System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
                    k += BluetoothOrder.paper.length;
                }

                byte abyte0[] = feedPaper(25);
                System.arraycopy(abyte0, 0, abyte1, k, abyte0.length);
                k += abyte0.length;
                for(l = 0; l < model.getPaperNum() - 2; l++)
                {
                    if(model.getDevdpi() == 200)
                        System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
                    else
                    if(model.getDevdpi() == 300)
                        System.arraycopy(BluetoothOrder.paper_300dpi, 0, abyte1, k, BluetoothOrder.paper_300dpi.length);
                    else
                        System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
                    k += BluetoothOrder.paper.length;
                }

                System.arraycopy(BluetoothOrder.finishLattice, 0, abyte1, k, BluetoothOrder.finishLattice.length);
                l = BluetoothOrder.finishLattice.length;
                System.arraycopy(BluetoothOrder.getDevState, 0, abyte1, k + l, BluetoothOrder.getDevState.length);
                EventBusUtils.getInstance().post(Code.STARTPRINT, abyte1);
            }

            final PrintDataUtils this$0;
            final int val$concentration;
            final int val$finalPrintNum;
            final PrinterModel.DataBean val$model;
            final ArrayList val$printList;
            final int val$width;

            
        {
            this$0 = PrintDataUtils.this;
            printList = arraylist;
            model = databean;
            concentration = i;
            width = j;
            finalPrintNum = k;
            super();
        }
        })).start();
        return;
    }
```


## Pre-headers and strings

`iPrint/classes/com/blueUtils/BluetoothUtils.java`:

```java
public void onChanged(BleDevice bledevice, BluetoothGattCharacteristic bluetoothgattcharacteristic) {

    String s = ByteUtils.BinaryToHexString(bluetoothgattcharacteristic.getValue());

    StringBuilder stringbuilder = new StringBuilder();

    stringbuilder.append("К массиву данных уведомлений устройства: %s");
    stringbuilder.append(s);

    bluetoothgattcharacteristic = bluetoothgattcharacteristic.getValue();

        if("51 78 AE 01 01 00 10 70 FF ".equals(s))
        {
            LogUtils.e(new Object[] { "Она полна" });
            mBle.stopWrite();
            break MISSING_BLOCK_LABEL_806;
        }
        if("51 78 AE 01 01 00 00 00 FF ".equals(s))
        {
            // Управление потоком---- Данные пустые 
            LogUtils.e(new Object[] { "Управление потоком---- Данные пустые" });
            mBle.startWrite();
            break MISSING_BLOCK_LABEL_806;
        }
        if("51 78 A5 01 17 00 A1 01 00 00 00 00 00 00 00 00 00 00 00 00 ".equals(s))
        {
            EventBusUtils.getInstance().post("setting_wifi", "success");
            break MISSING_BLOCK_LABEL_806;
        }
        if("51 78 A5 01 17 00 A1 02 00 00 00 00 00 00 00 00 00 00 00 00 ".equals(s))
        {
            EventBusUtils.getInstance().post("setting_wifi", "failed");
            break MISSING_BLOCK_LABEL_806;
        }
        if(bluetoothgattcharacteristic[2] != -88)
            break MISSING_BLOCK_LABEL_608;
        if(AppApplication.getPrinterModel(bledevice.getBleName()).isHasId() && !hasGetInfo)
        {
            byte abyte0[] = BluetoothOrder.getDevId;
            write(abyte0);
        }
```

## Bluetooth

Where ATT serveces set up?

`iPrint/classes/com/blueUtils/BluetoothUtils.java`

```java
public void initBlue()
{
    mBle = Ble.options().setLogTAG("AndroidBLE").setUuidServicesExtra(
        new UUID[] {
            UUID.fromString("0000AE00-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000FF00-0000-1000-8000-00805F9B34FB"),
            UUID.fromString("0000AB00-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000AE80-0000-1000-8000-00805F9B34FB"),
            UUID.fromString("49535343-FE7D-4AE5-8FA9-9FAFD205E455"), UUID.fromString("0000AE30-0000-1000-8000-00805f9b34fb")
        }).setUuidWriteExtra(
        new UUID[] {
            UUID.fromString("0000AE01-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000FF02-0000-1000-8000-00805F9B34FB"),
            UUID.fromString("0000AB01-0000-1000-8000-00805F9B34FB"), UUID.fromString("49535343-8841-43F4-A8D4-ECBE34729BB3"),
            UUID.fromString("0000AE81-0000-1000-8000-00805F9B34FB")
        }).setUuidNotifyExtra(
        new UUID[] {
            UUID.fromString("0000AE04-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000AB03-0000-1000-8000-00805F9B34FB")
        }
    ).create(AppApplication.getAppContext());
}

```


Pay attension to *Utils.java classes (ImageUtils, ZipUtils)
Check all `iPrint/classes/com/blueUtils/` code


```java
public class Main {
  public static void main(String[] args) {
    byte abyte0[] = new byte[8];
    int i = 0;
    abyte0[0] = (byte)81;
    abyte0[1] = (byte)120;
    abyte0[2] = (byte)-69;
    abyte0[3] = (byte)0;
    abyte0[4] = (byte)7;
    abyte0[5] = (byte)0;
    
    StringBuilder sb = new StringBuilder();
    for (byte b : abyte0) {
        sb.append(String.format("%02X ", b));
    }
    System.out.println(sb.toString());
  }
}
```

Bytearrays from `iPrint/classes/com/blueUtils/BluetoothOrder.java`

```java
    // 51 78 A1 00 02 00 48 00 F3 FF
    byte paper_300dpi[] = {81, 120, -95, 0, 2, 0, 72, 0, -13, -1};

    // 51 78 A1 00 02 00 30 00 F9 FF 
    byte paper[] = { 81, 120, -95, 0, 2, 0, 48, 0, -7, -1 };

    // 51 78 A6 00 0B 00 AA 55 17 38 44 5F 5F 5F 44 38 2C A1 FF
    byte printLattice[] = {
        81, 120, -90, 0, 11, 0, -86, 85, 23, 56, 
        68, 95, 95, 95, 68, 56, 44, -95, -1
    };
    
    // 51 78 BE 00 01 00 00 00 FF 
    byte print_img[] = { 81, 120, -66, 0, 1, 0, 0, 0, -1 };
    
    // 51 78 BE 00 01 00 01 07 FF 
    byte print_text[] = { 81, 120, -66, 0, 1, 0, 1, 7, -1 };

    // 51 78 A4 00 01 00 31 97 FF 
    byte quality1[] = {
        81, 120, -92, 0, 1, 0, 49, -105, -1
    };
    
    // 51 78 A4 00 01 00 32 9E FF
    byte quality2[] = {
        81, 120, -92, 0, 1, 0, 50, -98, -1
    };
    
    // 51 78 A4 00 01 00 33 99 FF 
    byte quality3[] = {
        81, 120, -92, 0, 1, 0, 51, -103, -1
    };
    
    // 51 78 A4 00 01 00 34 8C FF 
    byte quality4[] = {
        81, 120, -92, 0, 1, 0, 52, -116, -1
    };
    
    // 51 78 A4 00 01 00 35 8B FF 
    byte quality5[] = {
        81, 120, -92, 0, 1, 0, 53, -117, -1
    };

    // 51 78 A4 00 01 00 23 E9 FF 
    byte speed_moderation[] = {
        81, 120, -92, 0, 1, 0, 35, -23, -1
    };
    
    // 51 78 A4 00 01 00 25 FB FF
    byte speed_thick[] = {
        81, 120, -92, 0, 1, 0, 37, -5, -1
    };
    
    // 51 78 A4 00 01 00 22 EE FF
    byte speed_thin[] = {
        81, 120, -92, 0, 1, 0, 34, -18, -1
    };
    
    // 51 78 A9 00 01 00 00 00 FF 
    byte updateDev[] = {
        81, 120, -87, 0, 1, 0, 0, 0, -1
    };

    // 00 07 0E 09 1C 1B 12 15 38 3F 36 31 24 23 2A 2D 70 77 7E 79 6C 6B 62 65 48 4F 46 41 54 53 5A 5D E0 E7 EE E9 FC FB F2 F5 D8 DF D6 D1 C4 C3 CA CD 90 97 9E 99 8C 8B 82 85 A8 AF A6 A1 B4 B3 BA BD C7 C0 C9 CE DB DC D5 D2 FF F8 F1 F6 E3 E4 ED EA B7 B0 B9 BE AB AC A5 A2 8F 88 81 86 93 94 9D 9A 27 20 29 2E 3B 3C 35 32 1F 18 11 16 03 04 0D 0A 57 50 59 5E 4B 4C 45 42 6F 68 61 66 73 74 7D 7A 89 8E 87 80 95 92 9B 9C B1 B6 BF B8 AD AA A3 A4 F9 FE F7 F0 E5 E2 EB EC C1 C6 CF C8 DD DA D3 D4 69 6E 67 60 75 72 7B 7C 51 56 5F 58 4D 4A 43 44 19 1E 17 10 05 02 0B 0C 21 26 2F 28 3D 3A 33 34 4E 49 40 47 52 55 5C 5B 76 71 78 7F 6A 6D 64 63 3E 39 30 37 22 25 2C 2B 06 01 08 0F 1A 1D 14 13 AE A9 A0 A7 B2 B5 BC BB 96 91 98 9F 8A 8D 84 83 DE D9 D0 D7 C2 C5 CC CB E6 E1 E8 EF FA FD F4 F3 
    byte CHECKSUM_TABLE[] = {
        0, 7, 14, 9, 28, 27, 18, 21, 56, 63, 
        54, 49, 36, 35, 42, 45, 112, 119, 126, 121, 
        108, 107, 98, 101, 72, 79, 70, 65, 84, 83, 
        90, 93, -32, -25, -18, -23, -4, -5, -14, -11, 
        -40, -33, -42, -47, -60, -61, -54, -51, -112, -105, 
        -98, -103, -116, -117, -126, -123, -88, -81, -90, -95, 
        -76, -77, -70, -67, -57, -64, -55, -50, -37, -36, 
        -43, -46, -1, -8, -15, -10, -29, -28, -19, -22, 
        -73, -80, -71, -66, -85, -84, -91, -94, -113, -120, 
        -127, -122, -109, -108, -99, -102, 39, 32, 41, 46, 
        59, 60, 53, 50, 31, 24, 17, 22, 3, 4, 
        13, 10, 87, 80, 89, 94, 75, 76, 69, 66, 
        111, 104, 97, 102, 115, 116, 125, 122, -119, -114, 
        -121, -128, -107, -110, -101, -100, -79, -74, -65, -72, 
        -83, -86, -93, -92, -7, -2, -9, -16, -27, -30, 
        -21, -20, -63, -58, -49, -56, -35, -38, -45, -44, 
        105, 110, 103, 96, 117, 114, 123, 124, 81, 86, 
        95, 88, 77, 74, 67, 68, 25, 30, 23, 16, 
        5, 2, 11, 12, 33, 38, 47, 40, 61, 58, 
        51, 52, 78, 73, 64, 71, 82, 85, 92, 91, 
        118, 113, 120, 127, 106, 109, 100, 99, 62, 57, 
        48, 55, 34, 37, 44, 43, 6, 1, 8, 15, 
        26, 29, 20, 19, -82, -87, -96, -89, -78, -75, 
        -68, -69, -106, -111, -104, -97, -118, -115, -124, -125, 
        -34, -39, -48, -41, -62, -59, -52, -53, -26, -31, 
        -24, -17, -6, -3, -12, -13
    };
```




09-04 00:25:48.070   586   586 E PrintDataUtils: │ concentration---3

09-04 00:25:48.073   586 19266 E PrintDataUtils: │ Thread-27, com.blueUtils.PrintDataUtils.eachLinePixToCmdB(PrintDataUtils.java:318)

09-04 00:25:48.073   586 19266 E PrintDataUtils: │ 12000

09-04 00:25:48.074   586 19266 E PrintDataUtils: │ imgSpeed-----30

09-04 00:25:48.114   586   586 E PrintPreviewActivity: │ main, com.activity.PrintPreviewActivity.EventBus(PrintPreviewActivity.java:2934)
09-04 00:25:48.114   586   586 E PrintPreviewActivity: │ args[0] = STARTPRINT
09-04 00:25:48.114   586   586 E PrintPreviewActivity: │ args[1] = true

09-04 00:25:48.114   586 19139 E interval----: 4



PrintPreviewActivity:
    private void PrintBitmap()



// byte abyte2 = { 81, 120, -81, 0, 2, 0, 46, 224, 0, 0, -104, -1 };
// 51 78 af 00 02 00 2e e0 98 ff
// 51 78 a2 00 30 000000000000007cfbfb080000eea984400000004000e0dfdf7b17a12ab40485a89600000800000000000000000000000064ff


    bitmap = Bitmap.createBitmap(bm_width, horizontalscrollview.getHeight(), android.graphics.Bitmap.Config.RGB_565);
    horizontalscrollview.draw(new Canvas(bitmap));