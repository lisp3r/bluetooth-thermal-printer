package com.example.bitmaptest;

import java.util.UUID;


public class BluetoothOrder {
    private static final byte[] CHECKSUM_TABLE = {0, 7, 14, 9, 28, 27, 18, 21, 56, 63, 54, 49, 36, 35, 42, 45, 112, 119, 126, 121, 108, 107, 98, 101, 72, 79, 70, 65, 84, 83, 90, 93, -32, -25, -18, -23, -4, -5, -14, -11, -40, -33, -42, -47, -60, -61, -54, -51, -112, -105, -98, -103, -116, -117, -126, -123, -88, -81, -90, -95, -76, -77, -70, -67, -57, -64, -55, -50, -37, -36, -43, -46, -1, -8, -15, -10, -29, -28, -19, -22, -73, -80, -71, -66, -85, -84, -91, -94, -113, -120, -127, -122, -109, -108, -99, -102, 39, 32, 41, 46, 59, 60, 53, 50, 31, 24, 17, 22, 3, 4, 13, 10, 87, 80, 89, 94, 75, 76, 69, 66, 111, 104, 97, 102, 115, 116, 125, 122, -119, -114, -121, Byte.MIN_VALUE, -107, -110, -101, -100, -79, -74, -65, -72, -83, -86, -93, -92, -7, -2, -9, -16, -27, -30, -21, -20, -63, -58, -49, -56, -35, -38, -45, -44, 105, 110, 103, 96, 117, 114, 123, 124, 81, 86, 95, 88, 77, 74, 67, 68, 25, 30, 23, 16, 5, 2, 11, 12, 33, 38, 47, 40, 61, 58, 51, 52, 78, 73, 64, 71, 82, 85, 92, 91, 118, 113, 120, Byte.MAX_VALUE, 106, 109, 100, 99, 62, 57, 48, 55, 34, 37, 44, 43, 6, 1, 8, 15, 26, 29, 20, 19, -82, -87, -96, -89, -78, -75, -68, -69, -106, -111, -104, -97, -118, -115, -124, -125, -34, -39, -48, -41, -62, -59, -52, -53, -26, -31, -24, -17, -6, -3, -12, -13};
    public static final byte[] finishLattice = {81, 120, -90, 0, 11, 0, -86, 85, 23, 0, 0, 0, 0, 0, 0, 0, 23, 17, -1};
    public static final byte[] getDevId = {81, 120, -69, 0, 1, 0, 1, 7, -1};
    public static final byte[] getDevInfo = {81, 120, -88, 0, 1, 0, 0, 0, -1};
    public static final byte[] getDevState = {81, 120, -93, 0, 1, 0, 0, 0, -1};
    public static final byte[] paper = {81, 120, -95, 0, 2, 0, 48, 0, -7, -1};
    public static final byte[] paper_300dpi = {81, 120, -95, 0, 2, 0, 72, 0, -13, -1};
    public static final byte[] printLattice = {81, 120, -90, 0, 11, 0, -86, 85, 23, 56, 68, 95, 95, 95, 68, 56, 44, -95, -1};
    public static final byte[] print_img = {81, 120, -66, 0, 1, 0, 0, 0, -1};
    public static final byte[] print_text = {81, 120, -66, 0, 1, 0, 1, 7, -1};
    public static final byte[] quality1 = {81, 120, -92, 0, 1, 0, 49, -105, -1};
    public static final byte[] quality2 = {81, 120, -92, 0, 1, 0, 50, -98, -1};
    public static final byte[] quality3 = {81, 120, -92, 0, 1, 0, 51, -103, -1};
    public static final byte[] quality4 = {81, 120, -92, 0, 1, 0, 52, -116, -1};
    public static final byte[] quality5 = {81, 120, -92, 0, 1, 0, 53, -117, -1};
    public static final UUID[] serviceUUID = {UUID.fromString("0000AE00-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000FF00-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000AB00-0000-1000-8000-00805F9B34FB")};
    public static final byte[] speed_moderation = {81, 120, -92, 0, 1, 0, 35, -23, -1};
    public static final byte[] speed_thick = {81, 120, -92, 0, 1, 0, 37, -5, -1};
    public static final byte[] speed_thin = {81, 120, -92, 0, 1, 0, 34, -18, -1};
    public static final byte[] updateDev = {81, 120, -87, 0, 1, 0, 0, 0, -1};
    public static final byte[] wifiData = {81, 120, -86, 0, 10, 0, 51, 54, 48, 115, 117, 110, 112, 101, 110, 103, 1, -1, 81, 120, -85, 0, 8, 0, 51, 53, 56, 56, 55, 51, 53, 49, -87, -1};
    public static final UUID[] writeUUid = {UUID.fromString("0000AE01-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000FF02-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000AB01-0000-1000-8000-00805F9B34FB")};

    public static String getDevType(int i) {
        return "XW00" + i;
    }

    public static byte[] getBlackening(int i, PrinterModel.DataBean model)
    {
        String s = model.getModelNo();
        if(i == 2)
            if(!s.equals("M01") && !s.equals("XW003") && !s.equals("PR") && !s.equals("JX001"))
                return quality2;
            else
                return quality3;
        if(i == 3)
            if(!s.equals("M01") && !s.equals("XW003") && !s.equals("XW001") && !s.equals("PR") && !s.equals("JX001"))
                return quality3;
            else
                return quality4;
        if(i == 5) {
            if(s.equals("GB01"))
                return quality4;
            else
                return quality5;
        } else {
            return quality3;
        }
    }

    public static byte[] sendWifi(String str, String str2) {
        byte[] bytes = str.getBytes();
        byte[] bytes2 = str2.getBytes();
        byte[] bArr = new byte[(bytes.length + 8 + bytes2.length + 8)];
        byte[] int2byte = int2byte(bytes.length);
        byte[] int2byte2 = int2byte(str2.length());
        bArr[0] = 81;
        bArr[1] = 120;
        bArr[2] = -86;
        bArr[3] = 0;
        bArr[4] = int2byte[0];
        bArr[5] = 0;
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 6] = bytes[i];
        }
        bArr[bytes.length + 6] = calcCrc8(bytes);
        bArr[bytes.length + 7] = -1;
        bArr[bytes.length + 8] = 81;
        bArr[bytes.length + 9] = 120;
        bArr[bytes.length + 10] = -85;
        bArr[bytes.length + 11] = 0;
        bArr[bytes.length + 12] = int2byte2[0];
        bArr[bytes.length + 13] = 0;
        for (int i2 = 0; i2 < bytes2.length; i2++) {
            bArr[i2 + 14 + bytes.length] = bytes2[i2];
        }
        bArr[bytes.length + 14 + bytes2.length] = calcCrc8(bytes2);
        bArr[bytes.length + 15 + bytes2.length] = -1;
        return bArr;
    }

    public static byte[] sendDateToWifi(String str, String str2) {
        byte[] bytes = (str + str2).getBytes();
        byte[] bArr = new byte[(bytes.length + 10)];
        byte[] int2byte = int2byte(bytes.length + 2);
        bArr[0] = 81;
        bArr[1] = 120;
        bArr[2] = -91;
        bArr[3] = 0;
        bArr[4] = int2byte[0];
        bArr[5] = 0;
        bArr[6] = 0;
        bArr[7] = -96;
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 8] = bytes[i];
        }
        bArr[bytes.length + 8] = calcCrc8(bArr, 6, bytes.length + 2);
        bArr[bytes.length + 9] = -1;
        return bArr;
    }

    public static byte[] int2byte(int i) {
        return (new byte[] {
                (byte)(i & 0xff), (byte)(i >> 8 & 0xff), (byte)(i >> 16 & 0xff), (byte)(i >>> 24)
        });
    }

    public static byte[] sendWifiPw(String str) {
        byte[] bytes = str.getBytes();
        byte[] bArr = new byte[(bytes.length + 8)];
        byte[] int2byte = int2byte(bytes.length);
        bArr[0] = 81;
        bArr[1] = 120;
        bArr[2] = -85;
        bArr[3] = 0;
        bArr[4] = int2byte[0];
        bArr[5] = 0;
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 6] = bytes[i];
        }
        bArr[bArr.length - 2] = calcCrc8(bytes);
        bArr[bArr.length - 1] = -1;
        return bArr;
    }

    public static byte calcCrc8(byte[] bArr) {
        return calcCrc8(bArr, 0, bArr.length, (byte) 0);
    }

    public static byte calcCrc8(byte[] bArr, int i, int i2) {
        return calcCrc8(bArr, i, i2, (byte) 0);
    }

    public static byte calcCrc8(byte[] bArr, int i, int i2, byte b) {
        byte b2 = b;
        for (int i3 = i; i3 < i + i2; i3++) {
            b2 = CHECKSUM_TABLE[(b2 ^ bArr[i3]) & 255];
        }
        return b2;
    }

    public static byte[] writeDevId(String str) {
        if (str.length() != 12) {
            return null;
        }
        byte[] hexToByteArray = hexToByteArray("00" + str);
        byte[] bArr = new byte[(hexToByteArray.length + 8)];
        bArr[0] = 81;
        bArr[1] = 120;
        bArr[2] = -69;
        bArr[3] = 0;
        bArr[4] = 7;
        bArr[5] = 0;
        for (int i = 0; i < hexToByteArray.length; i++) {
            bArr[i + 6] = hexToByteArray[i];
        }
        bArr[bArr.length - 2] = calcCrc8(hexToByteArray);
        bArr[bArr.length - 1] = -1;
        return bArr;
    }

    public static byte[] hexToByteArray(String str) {
        byte[] bArr;
        int length = str.length();

        if (length % 2 == 1) {
            length++;
            bArr = new byte[(length / 2)];

            StringBuilder sbStr = new StringBuilder();
            sbStr.append("0");
            sbStr.append(str);
            str = sbStr.toString();
        } else {
            bArr = new byte[(length / 2)];
        }
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = i + 2;
            bArr[i2] = hexToByte(str.substring(i, i3));
            i2++;
            i = i3;
        }
        return bArr;
    }

    public static byte hexToByte(String str) {
        return (byte) Integer.parseInt(str, 16);
    }
}
