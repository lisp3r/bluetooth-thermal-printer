package com.example.bitmaptest;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;


public class PrintDataUtils {
    private static PrintDataUtils mSingleInstance;
    private static int[] p0 = {0, 128};
    private static int[] p1 = {0, 64};
    private static int[] p2 = {0, 32};
    private static int[] p3 = {0, 16};
    private static int[] p4 = {0, 8};
    private static int[] p5 = {0, 4};
    private static int[] p6 = {0, 2};
    private int ImgSpeed = 0;
    private int concentration = 0;
    private ArrayList<Integer> dataIdentify;
    private int eneragy = 0;
    private ArrayList<Integer> eneragyList;
    private boolean newCompress = true;
    private int packageLength = 0;
    private int textSpeed = 0;

    public static int TEXT_PRINT_TYPE = 0;
    public static int IMG_PRINT_TYPE = 1;

    private final PrinterModel pm = new PrinterModel();
    final PrinterModel.DataBean model = pm.getModel("no");

    public void printArray(byte[] bArr, boolean printAsHex){
        int chunk = 50; // chunk size to divide
        for(int i = 0; i < bArr.length; i += chunk){
            if (printAsHex) {
                Log.i("PrintDataUtils", BitmapHelpers.printHex(Arrays.copyOfRange(bArr, i, Math.min(bArr.length, i + chunk))));
            } else {
                Log.i("PrintDataUtils", Arrays.toString(Arrays.copyOfRange(bArr, i, Math.min(bArr.length, i + chunk))));
            }
        }
    }

    public static PrintDataUtils getInstance() {
        if (mSingleInstance == null) {
            synchronized (PrintDataUtils.class) {
                if (mSingleInstance == null) {
                    mSingleInstance = new PrintDataUtils();
                }
            }
        }
        return mSingleInstance;
    }

    public int getTextSpeed() {
        return this.textSpeed;
    }

    public void setTextSpeed(int i) {
        this.textSpeed = i;
    }

    public int getImgSpeed() {
        return this.ImgSpeed;
    }

    public int getEneragy() {
        if (this.eneragy != 0 || this.concentration == 0) {
            return this.eneragy;
        }
        return model.getEneragy(this.concentration);
    }

    public void setEneragy(int i) {
        if (i != 0 || this.concentration == 0) {
            this.eneragy = i;
        } else {
            this.eneragy = model.getEneragy(this.concentration);
        }
    }

    public ArrayList<Integer> getEneragyList() {
        return this.eneragyList;
    }

    public void setEneragyList(ArrayList<Integer> arrayList) {
        this.eneragyList = arrayList;
    }

    public void setImgSpeed(int i) {
        this.ImgSpeed = i;
    }

    // BitmapToData(list, AppApplication.getPrintImgSize(), printNum, concentrationType, flag);
    public void BitmapToData(final ArrayList<PrintBean> imageToPrint, int width, int concentration, boolean flag) {
        Log.i("PrintDataUtils", "concentration---" + this.concentration);

        this.concentration = concentration;
        this.newCompress = flag;
        this.dataIdentify = new ArrayList<>();

        ArrayList array_list = new ArrayList();
        Vector vector = new Vector(imageToPrint);

        this.packageLength = BluetoothOrder.printLattice.length + 9;
        Log.i("PrintDataUtils", "packageLength: " + this.packageLength);

        // int newEneragy = pm.getCustomEnergy(model.getModelNo()).get((printNum - 1) * 2);
        // Log.i("PrintDataUtils", "ONE: " + newEneragy);
        // PrintDataUtils.this.setEneragy(newEneragy);

        int newEneragy = model.getEneragy(this.concentration);
        Log.i("PrintDataUtils", "set custom energy: " + newEneragy);
        this.setEneragy(newEneragy); // 12000

        int i2 = 0;
        for (int i3 = 0; i3 < vector.size(); i3++) {
            PrintBean printBean = (PrintBean)vector.get(i3);
            if (printBean.isShow() && printBean.getBitmap() != null) {
                byte[] bitmapToBWPix = bitmapToBWPix(printBean.getBitmap());

                if (this.eneragyList != null && this.eneragyList.size() > 0) {
                    this.setEneragy(this.eneragyList.get(i3));
                }
                byte[] eachLinePixToCmdB = eachLinePixToCmdB(bitmapToBWPix, width, printBean.getPrintType());

//                int chunk = 50;
//                for(int i = 0; i < eachLinePixToCmdB.length; i += chunk){
//                    Log.i("eachLinePixToCmdB", String.valueOf(Arrays.copyOfRange(eachLinePixToCmdB, i, Math.min(eachLinePixToCmdB.length, i + chunk))));
//                }

                if (printBean.isAddWhite()) {
                    i2 += BluetoothOrder.paper.length;
                    array_list.add(BluetoothOrder.paper);
                }
                i2 += eachLinePixToCmdB.length;
                array_list.add(eachLinePixToCmdB);
            }
        }

        if (this.eneragyList != null) {
            this.eneragyList.clear();
        }

        this.setEneragy(0);
        byte[] bArr = new byte[(((i2 + (BluetoothOrder.paper.length * 2) + 9)) + 9 + (BluetoothOrder.paper.length * (model.getPaperNum() - 2)) + BluetoothOrder.printLattice.length + BluetoothOrder.finishLattice.length + 9 + (BluetoothOrder.getDevState.length * 2))];

        Log.i("PrintDataUtils", "Create tmp array with length = " + bArr.length);

        System.arraycopy(BluetoothOrder.getDevState, 0, bArr, 0, BluetoothOrder.getDevState.length);
        int length = BluetoothOrder.getDevState.length;

        Log.i("PrintDataUtils", "Copy getDevState to tmp arr, length = " + length);

        byte[] blackening = BluetoothOrder.getBlackening(concentration, model);
        System.arraycopy(blackening, 0, bArr, length, blackening.length);
        length = length + blackening.length;

        Log.i("PrintDataUtils", "Copy blackening to tmp arr, length = " + length);

        System.arraycopy(BluetoothOrder.printLattice, 0, bArr, length, BluetoothOrder.printLattice.length);
        length = length + BluetoothOrder.printLattice.length;

        Log.i("PrintDataUtils", "Copy printLattice to tmp arr, length = " + length);

        for (int i4 = 0; i4 < 1; i4++) {
            Iterator it = array_list.iterator();
            while (it.hasNext()) {
                byte[] bArr2 = (byte[]) it.next();
                System.arraycopy(bArr2, 0, bArr, length, bArr2.length);
                length += bArr2.length;
            }

            byte[] feedPaper = PrintDataUtils.this.feedPaper(25);
            System.arraycopy(feedPaper, 0, bArr, length, feedPaper.length);

            Log.i("PrintDataUtils", "Copy feedPaper to tmp arr, length = " + length);

            int length4 = length + feedPaper.length;

            if (model.getDevdpi() == 200) {
                System.arraycopy(BluetoothOrder.paper, 0, bArr, length4, BluetoothOrder.paper.length);
            } else if (model.getDevdpi() == 300) {
                System.arraycopy(BluetoothOrder.paper_300dpi, 0, bArr, length4, BluetoothOrder.paper_300dpi.length);
            } else {
                System.arraycopy(BluetoothOrder.paper, 0, bArr, length4, BluetoothOrder.paper.length);
            }
            int length5 = length4 + BluetoothOrder.paper.length;
            if (model.getDevdpi() == 200) {
                System.arraycopy(BluetoothOrder.paper, 0, bArr, length5, BluetoothOrder.paper.length);
            } else if (model.getDevdpi() == 300) {
                System.arraycopy(BluetoothOrder.paper_300dpi, 0, bArr, length5, BluetoothOrder.paper_300dpi.length);
            } else {
                System.arraycopy(BluetoothOrder.paper, 0, bArr, length5, BluetoothOrder.paper.length);
            }
            length = length5 + BluetoothOrder.paper.length;
        }

        byte[] feedPaper2 = PrintDataUtils.this.feedPaper(25);
        System.arraycopy(feedPaper2, 0, bArr, length, feedPaper2.length);

        length = length + feedPaper2.length;
        for (int i5 = 0; i5 < model.getPaperNum() - 2; i5++) {
            if (model.getDevdpi() == 200) {
                System.arraycopy(BluetoothOrder.paper, 0, bArr, length, BluetoothOrder.paper.length);
            } else if (model.getDevdpi() == 300) {
                System.arraycopy(BluetoothOrder.paper_300dpi, 0, bArr, length, BluetoothOrder.paper_300dpi.length);
            } else {
                System.arraycopy(BluetoothOrder.paper, 0, bArr, length, BluetoothOrder.paper.length);
            }
            length += BluetoothOrder.paper.length;
        }
        System.arraycopy(BluetoothOrder.finishLattice, 0, bArr, length, BluetoothOrder.finishLattice.length);
        System.arraycopy(BluetoothOrder.getDevState, 0, bArr, length + BluetoothOrder.finishLattice.length, BluetoothOrder.getDevState.length);
//               EventBusUtils.getInstance().post(Code.STARTPRINT, bArr);

        Log.i("PrintDataUtils", "Array length: " + bArr.length);
        Log.i("PrintDataUtils", "Result array:");

        printArray(bArr, true);
    }

    public static byte[] bitmapToBWPix(Bitmap bitmap) {
        // It gets bitmap, copy all the pixels to int array and
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bitmap_size = width * height;
        
        int[] iArr = new int[bitmap_size];
        byte[] bArr = new byte[bitmap_size];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        format_K_threshold(iArr, bitmap.getWidth(), bitmap.getHeight(), bArr);
        return bArr;
    }

    public static void format_K_threshold(int[] iArr, int width, int height, byte[] bArr) {
        int row_n = 0;
        int blu_ish = 0;
        int pixel_idx = 0;
        while (row_n < height) {
            for (int i8 = 0; i8 < width; i8++) {
                blu_ish += iArr[pixel_idx] & 255;
                pixel_idx++;
            }
            row_n++;
        }
        int avg_bluish = ((blu_ish / height) / width) - 13;
        row_n = 0;
        int pix_idx = 0;
        while (row_n < height) {
            for (int i13 = 0; i13 < width; i13++) {
                int pix_bluish = iArr[pix_idx] & 255;
                if (pix_bluish == 0) {
                    bArr[pix_idx] = 1;
                } else if (pix_bluish > avg_bluish) {
                    bArr[pix_idx] = 0;
                } else {
                    bArr[pix_idx] = 1;
                }
                pix_idx++;
            }
            row_n++;
        }
    }

    // {0, 0, 0, .... }, 384, 1,
    private byte[] eachLinePixToCmdB(byte[] bArr, int width, int printType) {
        String TAG = "eachLinePixToCmdB";
//        String logString = "Input array: ";
//        Log.i(TAG, logString);
//        for(int i = 0; i < bArr.length; i += 50){
//            byte[] tb = Arrays.copyOfRange(bArr, i, Math.min(bArr.length, i + 50));
//            logString = "" + Arrays.toString(tb);
//            Log.i(TAG, logString);
//        }

        String logString = "Input array's width: " + width + ", print type: " + printType;
        Log.i(TAG, logString);

        byte[] tmpArr;

        byte[] bArr3;
        int i3;
        boolean z;
        int i4;
        byte b;
        char c;
        //int i5;
        //int i6;
        int length = bArr.length / width;
        int i7 = width / 8;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        new ArrayList();
        int offset = 0;
        int eneragy = getEneragy();

        Log.i(TAG, "eneragy: " + eneragy);

        if (eneragy != 0) {
            tmpArr = new byte[((width * length) + BluetoothOrder.print_text.length + 9 + 10)];
            byte[] bArr4 = new byte[10];
            bArr4[0] = 81;
            bArr4[1] = 120;
            bArr4[2] = -81;
            bArr4[3] = 0;
            bArr4[4] = 2;
            bArr4[5] = 0;
            bArr4[6] = hexString2Bytes(Integer.toHexString(eneragy))[1];
            bArr4[7] = hexString2Bytes(Integer.toHexString(eneragy))[0];
            bArr4[8] = BluetoothOrder.calcCrc8(bArr4, 6, 2);
            bArr4[9] = -1;

            System.arraycopy(bArr4, 0, tmpArr, 0, bArr4.length);
            this.packageLength += 10;
            offset = 10;

            logString = "New 10 bytes: " + Arrays.toString(tmpArr)
                    .concat("package length: " + this.packageLength);
            Log.i(TAG, logString);

        } else {
            tmpArr = new byte[((width * length) + BluetoothOrder.print_text.length + 9)];

            logString = "No new bytes: " + Arrays.toString(tmpArr)
                    .concat("package length: " + this.packageLength);
            Log.i(TAG, logString);
        }

        if (printType == TEXT_PRINT_TYPE) {
            System.arraycopy(BluetoothOrder.print_text, 0, tmpArr, offset, BluetoothOrder.print_text.length);

            Log.i(TAG, "[TEXT PRINT TYPE] Copy print_text to tmpArr: " + Arrays.toString(BluetoothOrder.print_text));

            offset += BluetoothOrder.print_text.length;
            this.packageLength += BluetoothOrder.print_text.length;

            int textSpeed = getTextSpeed();
            if (textSpeed == 0) {
                textSpeed = model.getTextPrintSpeed();
            }

            Log.i(TAG, "textSpeed-----" + textSpeed);

            byte[] feedPaper = feedPaper(textSpeed);
            System.arraycopy(feedPaper, 0, tmpArr, offset, feedPaper.length);

            Log.i(TAG, "[TEXT PRINT TYPE] Copy feedPaper(textSpeed) to tmpArr: " + Arrays.toString(feedPaper));

        } else if (printType == IMG_PRINT_TYPE) {
            System.arraycopy(BluetoothOrder.print_img, 0, tmpArr, offset, BluetoothOrder.print_img.length);
            offset += BluetoothOrder.print_img.length;

            Log.i(TAG, "[IMG PRINT TYPE] Copy print_img to tmpArr: " + Arrays.toString(BluetoothOrder.print_img));

            this.packageLength += BluetoothOrder.print_img.length;

            int imgSpeed = getImgSpeed();
            if (imgSpeed == 0) {
                imgSpeed = model.getImgPrintSpeed();
            }

            Log.e("eachLinePixToCmdB", "imgSpeed-----" + imgSpeed);

            byte[] feedPaper = feedPaper(imgSpeed);
            System.arraycopy(feedPaper, 0, tmpArr, offset, feedPaper.length);

            Log.i(TAG, "[IMG PRINT TYPE] Copy feedPaper(imgSpeed) to tmpArr: " + Arrays.toString(feedPaper));
        }


        Log.i(TAG, "Some strange shit starting...");
        int i9 = offset + 9;

        this.packageLength += 9;

        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        while (i10 < length) {
            i11 += 8;
            arrayList.clear();
            arrayList2.clear();
            if (this.newCompress) {
                int i13 = 0;
                b = 0;
                i4 = 0;
                z = false;
                while (true) {
                    if (i13 >= width) {
                        i3 = length;
                        break;
                    }
                    byte b2 = bArr[i12 + i13];
                    if (i13 == 0 || b == b2) {
                        i4++;
                    } else {
                        dataTrim(i4, b, arrayList2);
                        i4 = 1;
                    }
                    if (b2 == 0 || z) {
                        i3 = length;
                    } else {
                        i3 = length;
                        z = true;
                    }
                    if (arrayList2.size() > i7) {
                        break;
                    }
                    if (i13 == width - 1 && i4 != 0 && z) {
                        dataTrim(i4, b2, arrayList2);
                        i4 = 0;
                    }
                    i13++;
                    b = b2;
                    length = i3;
                }
            } else {
                i3 = length;
                b = 0;
                i4 = 0;
                z = false;
            }
            i12 += width;
            if (arrayList2.size() > i7 || !this.newCompress) {
                arrayList.clear();
                i12 -= width;
                arrayList.add((byte) -1);
                for (int i14 = 0; i14 < i7; i14++) {
                    i12 += 8;
                    arrayList.add((byte) (p0[bArr[i12 + 7]] + p1[bArr[i12 + 6]] + p2[bArr[i12 + 5]] + p3[bArr[i12 + 4]] + p4[bArr[i12 + 3]] + p5[bArr[i12 + 2]] + p6[bArr[i12 + 1]] + bArr[i12]));
                }
            } else {
                arrayList.addAll(arrayList2);
            }
            if (!z && arrayList.size() == 0) {
                dataTrim(i4, b, arrayList);
            }
            if (arrayList.size() > 0) {
                byte[] bArr5 = new byte[arrayList.size()];
                for (int i15 = 0; i15 < arrayList.size(); i15++) {
                    bArr5[i15] = ((Byte) arrayList.get(i15)).byteValue();
                }
                i11 += arrayList.size();
                String hexString = Integer.toHexString(bArr5.length);
                tmpArr[i9] = 81;
                tmpArr[i9 + 1] = 120;
                if (bArr5[0] == -1 && bArr5.length == i7 + 1) {
                    tmpArr[i9 + 2] = -94;
                    i11--;
                    hexString = Integer.toHexString(i7);
                } else {
                    tmpArr[i9 + 2] = -65;
                }
                tmpArr[i9 + 3] = 0;
                tmpArr[i9 + 4] = hexString2Bytes(hexString)[0];
                tmpArr[i9 + 5] = 0;
                if (bArr5[0] == -1 && bArr5.length == i7 + 1) {
                    System.arraycopy(bArr5, 1, tmpArr, i9 + 6, bArr5.length - 1);
                    c = 0;
                } else {
                    c = 0;
                    System.arraycopy(bArr5, 0, tmpArr, i9 + 6, bArr5.length);
                }
                if (bArr5[c] == -1 && bArr5.length == i7 + 1) {
                    int i16 = i9 + 6;
                    tmpArr[i16 + i7] = BluetoothOrder.calcCrc8(tmpArr, i16, i7);
                    tmpArr[i9 + 7 + i7] = -1;
                    i9 = i9 + 8 + i7;
                    this.packageLength = this.packageLength + 8 + i7;
                } else {
                    int i17 = i9 + 6;
                    tmpArr[bArr5.length + i17] = BluetoothOrder.calcCrc8(tmpArr, i17, bArr5.length);
                    tmpArr[i9 + 7 + bArr5.length] = -1;
                    i9 = i9 + 8 + bArr5.length;
                    this.packageLength = this.packageLength + 8 + bArr5.length;
                }
            }
            i10++;
            length = i3;
        }

        if (getEneragy() != 0) {
            bArr3 = new byte[(i11 + BluetoothOrder.print_text.length + 19)];
        } else {
            bArr3 = new byte[(i11 + BluetoothOrder.print_text.length + 9)];
        }
        System.arraycopy(tmpArr, 0, bArr3, 0, bArr3.length);

        // Log.i(TAG, Arrays.toString(bArr3));

//        int chunk = 50; // chunk size to divide
//        for(int i = 0; i < bArr.length; i += chunk){
//            Log.i(TAG, Arrays.toString(Arrays.copyOfRange(bArr3, i, Math.min(bArr.length, i + chunk))));
//        }

        return bArr3;
    }

    public static byte[] conver2HexToByte(String str) {
        String[] split = str.split(",");
        byte[] bArr = new byte[split.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = Long.valueOf(split[i], 2).byteValue();
        }
        return bArr;
    }

    private static String toBinary(int i) {
        String str = "";
        while (i != 0) {
            str = (i % 2) + str;
            i /= 2;
        }
        return str;
    }

    private byte[] eachLinePixToCmdC(byte[] bArr, int i) {
        int i2;
        int length = bArr.length / i;
        int i3 = i / 8;
        int i4 = i3 + 8;
        byte[] bArr2 = new byte[((length * i4) + BluetoothOrder.print_text.length + 9)];
        System.arraycopy(BluetoothOrder.print_text, 0, bArr2, 0, BluetoothOrder.print_text.length);
        int length2 = BluetoothOrder.print_text.length;
        if (getImgSpeed() == 0) {
            i2 = model.getTextPrintSpeed();
        } else {
            i2 = getTextSpeed();
        }
        byte[] feedPaper = feedPaper(i2);
        System.arraycopy(feedPaper, 0, bArr2, length2, feedPaper.length);
        String hexString = Integer.toHexString(i3);
        int i5 = 0;
        int i6 = 0;
        while (i5 < length) {
            int length3 = (i5 * i4) + BluetoothOrder.print_text.length + 9;
            bArr2[length3 + 0] = 81;
            bArr2[length3 + 1] = 120;
            bArr2[length3 + 2] = -94;
            bArr2[length3 + 3] = 0;
            bArr2[length3 + 4] = hexString2Bytes(hexString)[0];
            bArr2[length3 + 5] = 0;
            int i7 = i6;
            for (int i8 = 0; i8 < i3; i8++) {
                bArr2[length3 + 6 + i8] = (byte) (p0[bArr[i7 + 7]] + p1[bArr[i7 + 6]] + p2[bArr[i7 + 5]] + p3[bArr[i7 + 4]] + p4[bArr[i7 + 3]] + p5[bArr[i7 + 2]] + p6[bArr[i7 + 1]] + bArr[i7 + 0]);
                i7 += 8;
            }
            int i9 = length3 + 6;
            bArr2[i9 + i3] = BluetoothOrder.calcCrc8(bArr2, i9, i3);
            bArr2[length3 + 7 + i3] = -1;
            i5++;
            i6 = i7;
        }
        return bArr2;
    }

    private static void dataTrim(int i, byte byte0, ArrayList<Byte> arrayList) {
        for (; i > 127; i -= 127) {
            arrayList.add(conver2HexToByte(byte0 + toBinary(127))[0]);
        }

        if (i > 0) {
            String binary = toBinary(i);
            String str = ((int) byte0) + "";
            if (binary.length() < 8) {
                String str2 = str;
                for (int i2 = 0; i2 < 7 - binary.length(); i2++) {
                    str2 = str2 + 0;
                }
                str = str2;
            }
            arrayList.add(conver2HexToByte(str + binary)[0]);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private byte[] feedPaper(int i) {
        byte[] bArr = new byte[9];
        bArr[0] = 81;
        bArr[1] = 120;
        bArr[2] = -67;
        bArr[3] = 0;
        bArr[4] = 1;
        bArr[5] = 0;
        bArr[6] = hexString2Bytes(Integer.toHexString(i))[0];
        bArr[7] = BluetoothOrder.calcCrc8(bArr, 6, 1);
        bArr[8] = -1;
        return bArr;
    }

    public byte[] filledByte() {
        int paperSize = this.model.getPaperSize();

        byte[] bArr = new byte[(paperSize + (BluetoothOrder.paper.length * this.model.getPaperNum()))];
        for (int i = 0; i < paperSize; i++) {
            bArr[i] = 0;
        }

        for (int i2 = 0; i2 < this.model.getPaperNum(); i2++) {
            if (this.model.getDevdpi() == 200) {
                System.arraycopy(BluetoothOrder.paper, 0, bArr, paperSize, BluetoothOrder.paper.length);
            } else if (this.model.getDevdpi() == 300) {
                System.arraycopy(BluetoothOrder.paper_300dpi, 0, bArr, paperSize, BluetoothOrder.paper_300dpi.length);
            } else {
                System.arraycopy(BluetoothOrder.paper, 0, bArr, paperSize, BluetoothOrder.paper.length);
            }
            paperSize += BluetoothOrder.paper.length;
        }
        return bArr;
    }

    public byte[] filledByte(byte[] bArr) {
        int printSize = this.model.getPrintSize();

        byte[] bArr2 = new byte[printSize];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        int length = bArr.length;
        for (int i = 0; i < printSize - length; i++) {
            bArr2[length + i] = 0;
        }
        return bArr2;
    }

    public static int hex2Dec(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'A' && c <= 'F') {
            return (c - 65) + 10;
        }
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
}
