package com.example.bitmaptest;

import android.util.Log;

import java.util.ArrayList;

public class BitmapHelpers {

    public static int printSize = 384;
    public static int eneragy = 12000;

    public static final byte print_text[] = { 81, 120, -66, 0, 1, 0, 1, 7, -1};

    public static String printHex(byte[] bytesArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytesArr) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    public static int hex2Dec(char c) {
        if (c >= '0' && c <= '9') { return c - 48; }
        if (c >= 'A' && c <= 'F') { return (c - 65) + 10; }
        return -1;
    }

    public static byte[] hexString2Bytes(String s) {
        int i = 0;
        int str_len = s.length();
        int k = str_len;
        Object obj = s;
        if (str_len % 2 != 0) {
            obj = new StringBuilder();
            ((StringBuilder) (obj)).append("0");
            ((StringBuilder) (obj)).append(s);
            obj = ((StringBuilder) (obj)).toString();
            k = str_len + 1;
        }
        char[] ac = ((String) (obj)).toUpperCase().toCharArray();
        byte[] my_s = new byte[k >> 1];
        for (; i < k; i += 2)
            my_s[i >> 1] = (byte) (hex2Dec(ac[i]) << 4 | hex2Dec(ac[i + 1]));
        return my_s;
    }

    public static byte calcCrc8(byte[] bytes, int i, int j) {
        byte[] CHECKSUM_TABLE = { 0, 7, 14, 9, 28, 27, 18, 21, 56, 63, 54, 49, 36, 35, 42, 45, 112, 119, 126, 121, 108,
                107, 98, 101, 72, 79, 70, 65, 84, 83, 90, 93, -32, -25, -18, -23, -4, -5, -14, -11, -40, -33, -42, -47,
                -60, -61, -54, -51, -112, -105, -98, -103, -116, -117, -126, -123, -88, -81, -90, -95, -76, -77, -70,
                -67, -57, -64, -55, -50, -37, -36, -43, -46, -1, -8, -15, -10, -29, -28, -19, -22, -73, -80, -71, -66,
                -85, -84, -91, -94, -113, -120, -127, -122, -109, -108, -99, -102, 39, 32, 41, 46, 59, 60, 53, 50, 31,
                24, 17, 22, 3, 4, 13, 10, 87, 80, 89, 94, 75, 76, 69, 66, 111, 104, 97, 102, 115, 116, 125, 122, -119,
                -114, -121, -128, -107, -110, -101, -100, -79, -74, -65, -72, -83, -86, -93, -92, -7, -2, -9, -16, -27,
                -30, -21, -20, -63, -58, -49, -56, -35, -38, -45, -44, 105, 110, 103, 96, 117, 114, 123, 124, 81, 86,
                95, 88, 77, 74, 67, 68, 25, 30, 23, 16, 5, 2, 11, 12, 33, 38, 47, 40, 61, 58, 51, 52, 78, 73, 64, 71,
                82, 85, 92, 91, 118, 113, 120, 127, 106, 109, 100, 99, 62, 57, 48, 55, 34, 37, 44, 43, 6, 1, 8, 15, 26,
                29, 20, 19, -82, -87, -96, -89, -78, -75, -68, -69, -106, -111, -104, -97, -118, -115, -124, -125, -34,
                -39, -48, -41, -62, -59, -52, -53, -26, -31, -24, -17, -6, -3, -12, -13 };
        byte byte1 = (byte)0;
        int byte0 = i;
        byte byte2;
        for (byte2 = byte1; byte0 < i + j; byte2 = byte1) {
            byte1 = CHECKSUM_TABLE[(byte2 ^ bytes[byte0]) & 0xff];
            byte0++;
        }
        return byte2;
    }

    public static byte[] feedPaper(int printSpeed) {
        // printSpeed = 30
        byte[] bytesArr = new byte[9];
        bytesArr[0] = (byte) 81;
        bytesArr[1] = (byte) 120;
        bytesArr[2] = (byte) -67;
        bytesArr[3] = (byte) 0;
        bytesArr[4] = (byte) 1;
        bytesArr[5] = (byte) 0;
        bytesArr[6] = hexString2Bytes(Integer.toHexString(printSpeed))[0];
        bytesArr[7] = calcCrc8(bytesArr, 6, 1);
        bytesArr[8] = (byte) -1;
        return bytesArr;
    }

//    public void BitmapToData(ArrayList printList, int width, int i, int concentration, boolean flag) {
//        if (printList.size() == 0) {
//            EventBusUtils.getInstance().post("onWriteFailed");
//        } else {
//            final PrinterModel.DataBean model = AppApplication.getPrinterModel();
//            // model = DataBean("GT01", " ", 0, 2, 384, 384, 8, "GT01-", true, 200, 55, 45,
//            // 123, 83, true, 2, 4, false, 12000, 12000, 12000, true);
//            //
//            this.concentration = concentration; // 3
//            StringBuilder stringbuilder = new StringBuilder();
//            stringbuilder.append("concentration---");
//            stringbuilder.append(this.concentration);
//            LogUtils.e(new Object[] { stringbuilder.toString() });
//            newCompress = flag;
//            dataIdentify = new ArrayList();
//            (new Thread(new Runnable() {
//                public void run() {
//                    ArrayList arraylist = new ArrayList();
//                    Vector vector = new Vector();
//                    vector.addAll(printList);
//                    // printLattice.length = 19
//                    packageLength = BluetoothOrder.printLattice.length + 9; // 28
//                    if (!model.getModelNo().startsWith("LY") && !model.getModelNo().equals("P10")
//                            && !model.getModelNo().startsWith("GB") && !model.getModelNo().startsWith("GT")
//                            && !model.getModelNo().startsWith("PR")) {
//                        setEneragy(0);
//                    } else {
//                        // here
//
//                        SPUtils sputils = SPUtils.getInstance();
//                        // ?
//                        StringBuilder stringbuilder2 = new StringBuilder();
//                        stringbuilder2.append(Code.DEV_CONCENTRATION); // Code.DEV_CONCENTRATION="dev_con"
//                        stringbuilder2.append(SPUtils.getInstance().getString(Code.DEF_DEVICE_ADDRESS)); // Code.DEF_DEVICE_ADDRESS="defDeviceAddress"
//                        int j = sputils.getInt(stringbuilder2.toString(), 0);
//                        if (j > 0) {
//                            // suppose not here
//                            // getCustomEnergy: [7680, 7680, 9600, 9600, 12000, 12000, 14400, 14400, 17280,
//                            // 17280, 20000, 20000, 20000, 20000]
//                            j = ((Integer) PrinterModel.getCustomEnergy(model.getModelNo()).get((j - 1) * 2))
//                                    .intValue();
//                            setEneragy(j);
//                        } else if (model.getModelNo().equals("LY01")) {
//                            SPUtils sputils1 = SPUtils.getInstance();
//                            StringBuilder stringbuilder1 = new StringBuilder();
//                            stringbuilder1.append(Code.LY01_DEF_ENERAGY);
//                            stringbuilder1.append(SPUtils.getInstance().getString(Code.DEF_DEVICE_ADDRESS));
//                            if (sputils1.getBoolean(stringbuilder1.toString(), true))
//                                setEneragy(9500);
//                            else
//                                setEneragy(20000);
//                        } else {
//                            // here ?
//                            setEneragy(model.getEneragy(concentration)); // 12000
//                        }
//                    }
//                    int l = 0;
//                    int k;
//                    int i1;
//                    for (k = 0; l < vector.size(); k = i1) {
//                        PrintBean printbean = (PrintBean) vector.get(l);
//                        i1 = k;
//                        if (printbean.isShow()) {
//                            i1 = k;
//                            if (printbean.getBitmap() != null) {
//                                // here
//                                // soooooo say we have a bitmap with width=384, height=800
//                                byte abyte2[] = PrintDataUtils.bitmapToBWPix(printbean.getBitmap());
//                                if (eneragyList != null && eneragyList.size() > 0) {
//                                    i1 = ((Integer) eneragyList.get(l)).intValue();
//                                    setEneragy(i1);
//                                }
//                                // ?? , ?? , 0 or 1
//                                abyte2 = eachLinePixToCmdB(abyte2, width, printbean.getPrintType());
//                                i1 = k;
//                                if (printbean.isAddWhite()) {
//                                    i1 = k + BluetoothOrder.paper.length;
//                                    arraylist.add(BluetoothOrder.paper);
//                                }
//                                i1 += abyte2.length;
//                                arraylist.add(abyte2);
//                            }
//                        }
//                        l++;
//                    }
//
//                    if (eneragyList != null)
//                        eneragyList.clear();
//                    setEneragy(0);
//                    byte abyte1[] = new byte[(k + BluetoothOrder.paper.length * 2 + 9) * finalPrintNum + 9
//                            + BluetoothOrder.paper.length * (model.getPaperNum() - 2)
//                            + BluetoothOrder.printLattice.length + BluetoothOrder.finishLattice.length + 9
//                            + BluetoothOrder.getDevState.length * 2];
//                    System.arraycopy(BluetoothOrder.getDevState, 0, abyte1, 0, BluetoothOrder.getDevState.length);
//                    k = BluetoothOrder.getDevState.length + 0;
//                    byte abyte3[] = BluetoothOrder.getBlackening(concentration);
//                    System.arraycopy(abyte3, 0, abyte1, k, abyte3.length);
//                    k += abyte3.length;
//                    System.arraycopy(BluetoothOrder.printLattice, 0, abyte1, k, BluetoothOrder.printLattice.length);
//                    k += BluetoothOrder.printLattice.length;
//                    for (l = 0; l < finalPrintNum; l++) {
//                        for (Iterator iterator = arraylist.iterator(); iterator.hasNext();) {
//                            byte abyte4[] = (byte[]) iterator.next();
//                            System.arraycopy(abyte4, 0, abyte1, k, abyte4.length);
//                            k += abyte4.length;
//                        }
//
//                        byte abyte5[] = feedPaper(25);
//                        System.arraycopy(abyte5, 0, abyte1, k, abyte5.length);
//                        k += abyte5.length;
//                        if (model.getDevdpi() == 200)
//                            System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
//                        else if (model.getDevdpi() == 300)
//                            System.arraycopy(BluetoothOrder.paper_300dpi, 0, abyte1, k,
//                                    BluetoothOrder.paper_300dpi.length);
//                        else
//                            System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
//                        k += BluetoothOrder.paper.length;
//                        if (model.getDevdpi() == 200)
//                            System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
//                        else if (model.getDevdpi() == 300)
//                            System.arraycopy(BluetoothOrder.paper_300dpi, 0, abyte1, k,
//                                    BluetoothOrder.paper_300dpi.length);
//                        else
//                            System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
//                        k += BluetoothOrder.paper.length;
//                    }
//
//                    byte abyte0[] = feedPaper(25);
//                    System.arraycopy(abyte0, 0, abyte1, k, abyte0.length);
//                    k += abyte0.length;
//                    for (l = 0; l < model.getPaperNum() - 2; l++) {
//                        if (model.getDevdpi() == 200)
//                            System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
//                        else if (model.getDevdpi() == 300)
//                            System.arraycopy(BluetoothOrder.paper_300dpi, 0, abyte1, k,
//                                    BluetoothOrder.paper_300dpi.length);
//                        else
//                            System.arraycopy(BluetoothOrder.paper, 0, abyte1, k, BluetoothOrder.paper.length);
//                        k += BluetoothOrder.paper.length;
//                    }
//
//                    System.arraycopy(BluetoothOrder.finishLattice, 0, abyte1, k, BluetoothOrder.finishLattice.length);
//                    l = BluetoothOrder.finishLattice.length;
//                    System.arraycopy(BluetoothOrder.getDevState, 0, abyte1, k + l, BluetoothOrder.getDevState.length);
//                    EventBusUtils.getInstance().post(Code.STARTPRINT, abyte1);
//                }
//
//                final PrintDataUtils this$0;
//                final int val$concentration;
//                final int val$finalPrintNum;
//                final PrinterModel.DataBean val$model;
//                final ArrayList val$printList;
//                final int val$width;
//
//                {
//                    this$0 = PrintDataUtils.this;
//                    printList = arraylist;
//                    model = databean;
//                    concentration = i;
//                    width = j;
//                    finalPrintNum = k;
//                    super();
//                }
//            })).start();
//            return;
//        }
//    }

//    public byte[] eachLinePixToCmdB(byte[] inputBitmap, int width, int j_print_type) {
//        // j_print_type = 0
//        // inputBitmap = bitmap
//        // width = 384
//        byte tmpBitmap[];
//        ArrayList arraylist, arrayList2 = new ArrayList();
//
//        int height = inputBitmap.length / width; // 800, ex k
//        int i1 = width / 8; // 48
//        byte byte0 = 10;
//
//        if (eneragy != 0) {
//            // tmpBitmap = new byte[width * height + 28]
//            tmpBitmap = new byte[width * height + print_text.length + 9 + 10]; // 307228
//
//            byte[] abyte2 = new byte[10];
//            abyte2[0] = (byte) 81;
//            abyte2[1] = (byte) 120;
//            abyte2[2] = (byte) -81;
//            abyte2[3] = (byte) 0;
//            abyte2[4] = (byte) 2;
//            abyte2[5] = (byte) 0;
//            abyte2[6] = hexString2Bytes(Integer.toHexString(eneragy))[1]; // 46
//            abyte2[7] = hexString2Bytes(Integer.toHexString(eneragy))[0]; // 224
//            abyte2[8] = calcCrc8(abyte2, 6, 2); // -104
//            abyte2[9] = (byte) -1;
//            // abyte2[] = { 81, 120, -81, 0, 2, 0, 46, 224, -104, -1 };
//            // arraycopy(src, srcPos, dest, int, length)
//            System.arraycopy(abyte2, 0, tmpBitmap, 0, abyte2.length);
//            // tmpBitmap = { 81, 120, -81, 0, 2, 0, 46, 224, -104, -1, 00, 00, 00, ...}
//            packageLength = packageLength + 10; // 38
//        } else {
//            tmpBitmap = new byte[width * k + BluetoothOrder.print_text.length + 9];
//            byte0 = 0;
//        }
//        if (j_print_type == Code.TEXT_PRINT_TYPE) {
//            System.arraycopy(print_text, 0, tmpBitmap, byte0, print_text.length);
//            j1 = byte0 + BluetoothOrder.print_text.length;
//            packageLength = packageLength + BluetoothOrder.print_text.length;
//            if (getTextSpeed() == 0)
//                j_print_type = AppApplication.getTextPrintSpeed();
//            else
//                j_print_type = getTextSpeed();
//            StringBuilder stringbuilder = new StringBuilder();
//            stringbuilder.append("textSpeed-----");
//            stringbuilder.append(j);
//            LogUtils.e(new Object[] { stringbuilder.toString() });
//            byte abyte3[] = feedPaper(j);
//            System.arraycopy(abyte3, 0, tmpBitmap, j1, abyte3.length);
//            j_print_type = AppApplication.getPrintTextMtu();
//            if (j != 0)
//                AppApplication.getInstance().getBle().setPerLength(j);
//            else
//                AppApplication.getInstance().getBle().setPerLength(23);
//        } else if (j_print_type == Code.IMG_PRINT_TYPE) {
//            eneragy = byte0; // 10
//            AppApplication.getPrinterModel().getModelNo();
//            // byte print_img[] = { 81, 120, -66, 0, 1, 0, 0, 0, -1 };
//            System.arraycopy(BluetoothOrder.print_img, 0, tmpBitmap, byte0, BluetoothOrder.print_img.length);
//            // byte0[] = { 51 78 AF 00 02 00 2E E0 98 FF 51 78 BE 00 01 00 00 00 FF 00 ... }
//            eneragy = byte0 + BluetoothOrder.print_img.length; // 19
//            packageLength = packageLength + BluetoothOrder.print_img.length; // 38 + 9 = 47
//            if (getImgSpeed() == 0)
//                j_print_type = AppApplication.getImgPrintSpeed();
//            else
//                j_print_type = getImgSpeed();
//            // j_print_type = 30
//            StringBuilder stringbuilder1 = new StringBuilder();
//            stringbuilder1.append("imgSpeed-----");
//            stringbuilder1.append(j_print_type);
//            LogUtils.e(new Object[] { stringbuilder1.toString() });
//            byte abyte4[] = feedPaper(j_print_type); // 51 78 BD 00 01 00 1E 5A FF
//            System.arraycopy(abyte4, 0, tmpBitmap, j1, abyte4.length);
//            j_print_type = AppApplication.getPrinterMtu(); // 0 ???
//            if (j_print_type != 0)
//                AppApplication.getInstance().getBle().setPerLength(j_print_type);
//            else
//                AppApplication.getInstance().getBle().setPerLength(23); // guess this
//        }
//        int j2 = eneragy + 9; // 19 + 9 = 28
//        packageLength = packageLength + 9; // 47 + 9 = 56
//        // byte0 = 0;
//        int k2 = 0;
//        int l2 = 0;
//        // j_print_type = height;
//        for (int l = 0; l < height;) {
//            int i3 = k2 + 8;
//            arraylist.clear();
//            arraylist1.clear();
//            int k1;
//            int l3;
//            if (newCompress) {
//                int j3 = 0;
//                boolean flag = false;
//                k1 = 0;
//                k2 = 0;
//                l3 = 0;
//                do {
//                    if (j3 >= width)
//                        break;
//                    byte byte1 = inputBitmap[l2 + j3];
//                    int l1;
//                    if (j3 == 0 || l3 == byte1) {
//                        l1 = k1 + 1;
//                    } else {
//                        dataTrim(k1, l3, arraylist1);
//                        l1 = 1;
//                    }
//                    if (byte1 != 0 && k2 == 0)
//                        k2 = 1;
//                    if (arraylist1.size() > i1) {
//                        k1 = l1;
//                        break;
//                    }
//                    k1 = l1;
//                    if (j3 == i - 1) {
//                        k1 = l1;
//                        if (l1 != 0) {
//                            k1 = l1;
//                            if (k2 != 0) {
//                                dataTrim(l1, byte1, arraylist1);
//                                k1 = 0;
//                            }
//                        }
//                    }
//                    j3++;
//                    l1 = byte1;
//                    l3 = l1;
//                } while (true);
//            } else {
//                boolean flag1 = false;
//                k1 = 0;
//                k2 = 0;
//                l3 = 0;
//            }
//            int i2 = l2 + i;
//            if (arraylist1.size() <= i1 && newCompress) {
//                arraylist.addAll(arraylist1);
//            } else {
//                arraylist.clear();
//                l2 = i2 - i;
//                arraylist.add(Byte.valueOf((byte) -1));
//                int k3 = 0;
//                do {
//                    i2 = l2;
//                    if (k3 >= i1)
//                        break;
//                    byte byte2 = (byte) (p0[inputBitmap[l2 + 7]] + p1[inputBitmap[l2 + 6]] + p2[inputBitmap[l2 + 5]]
//                            + p3[inputBitmap[l2 + 4]] + p4[inputBitmap[l2 + 3]] + p5[inputBitmap[l2 + 2]] + p6[inputBitmap[l2 + 1]]
//                            + inputBitmap[l2 + 0]);
//                    l2 += 8;
//                    arraylist.add(Byte.valueOf(byte2));
//                    k3++;
//                } while (true);
//            }
//            if (k2 == 0 && arraylist.size() == 0)
//                dataTrim(k1, l3, arraylist);
//            if (arraylist.size() > 0) {
//                byte abyte5[] = new byte[arraylist.size()];
//                for (k1 = 0; k1 < arraylist.size(); k1++)
//                    abyte5[k1] = ((Byte) arraylist.get(k1)).byteValue();
//
//                k2 = i3 + arraylist.size();
//                String s = Integer.toHexString(abyte5.length);
//                tmpBitmap[j2 + 0] = (byte) 81;
//                tmpBitmap[j2 + 1] = (byte) 120;
//                if (abyte5[0] == -1 && abyte5.length == i1 + 1) {
//                    tmpBitmap[j2 + 2] = (byte) -94;
//                    k2--;
//                    s = Integer.toHexString(i1);
//                } else {
//                    tmpBitmap[j2 + 2] = (byte) -65;
//                }
//                tmpBitmap[j2 + 3] = (byte) 0;
//                tmpBitmap[j2 + 4] = ConvertUtils.hexString2Bytes(s)[0];
//                tmpBitmap[j2 + 5] = (byte) 0;
//                if (abyte5[0] == -1 && abyte5.length == i1 + 1)
//                    System.arraycopy(abyte5, 1, tmpBitmap, j2 + 6, abyte5.length - 1);
//                else
//                    System.arraycopy(abyte5, 0, tmpBitmap, j2 + 6, abyte5.length);
//                if (abyte5[0] == -1 && abyte5.length == i1 + 1) {
//                    k1 = j2 + 6;
//                    tmpBitmap[k1 + i1] = BluetoothOrder.calcCrc8(tmpBitmap, k1, i1);
//                    tmpBitmap[j2 + 7 + i1] = (byte) -1;
//                    k1 = j2 + 8 + i1;
//                    packageLength = packageLength + 8 + i1;
//                } else {
//                    k1 = j2 + 6;
//                    tmpBitmap[abyte5.length + k1] = BluetoothOrder.calcCrc8(tmpBitmap, k1, abyte5.length);
//                    tmpBitmap[j2 + 7 + abyte5.length] = (byte) -1;
//                    k1 = j2 + 8 + abyte5.length;
//                    packageLength = packageLength + 8 + abyte5.length;
//                }
//            } else {
//                k1 = j2;
//                k2 = i3;
//            }
//            l++;
//            l2 = i2;
//            j2 = k1;
//        }
//
//        if (getEneragy() != 0)
//            inputBitmap = new byte[k2 + BluetoothOrder.print_text.length + 19];
//        else
//            inputBitmap = new byte[k2 + BluetoothOrder.print_text.length + 9];
//        System.arraycopy(tmpBitmap, 0, inputBitmap, 0, inputBitmap.length);
//        return inputBitmap;
//    }

}
