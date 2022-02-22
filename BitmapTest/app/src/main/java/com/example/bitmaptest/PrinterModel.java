package com.example.bitmaptest;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PrinterModel {
    public String deviceName = "GT01";

    public DataBean getModel(String flag) {
        if("jingzhunxuexi_wand".equals(flag)) {
            return new DataBean("GT01", " ", 0, 2, 384, 384, 8, "GT01-", true, 200, 55, 45, 123, 83, true, 2, 4, false, 12000, 12000, 12000, true);
        }
        return new DataBean("GT01", " ", 0, 2, 384, 384, 8, "GT01-", true, 200, 30, 25, 123, 83, true, 2, 4, false, 12000, 12000, 12000, true);
    }

    public class DataBean {
        private boolean canChangeMTU;
        private int deepenEneragy;
        private int devdpi;
        private boolean hasId;
        private String headName;
        private int imgMTU;
        private int imgPrintSpeed;
        private int interval;
        private int model;
        private String modelName;
        private String modelNo;
        private int moderationEneragy;
        private boolean newCompress;
        private int oneLength;
        private int paperNum;
        private int paperSize;
        private int printSize;
        private int size;
        private int textMTU;
        private int textPrintSpeed;
        private int thinEneragy;
        private boolean useSPP;

        public DataBean(String str, String str2, int i, int i2, int i3, int i4, int i5,
                        String str3, boolean z, int i6, int i7, int i8, int i9, int i10, boolean z2,
                        int i11, int i12, boolean z3, int i13, int i14, int i15, boolean z4) {
            this.modelNo = str;
            this.modelName = str2;
            this.model = i;
            this.size = i2;
            this.paperSize = i3;
            this.printSize = i4;
            this.oneLength = i5;
            this.headName = str3;
            this.canChangeMTU = z;
            this.devdpi = i6;
            this.imgPrintSpeed = i7;
            this.textPrintSpeed = i8;
            this.imgMTU = i9;
            this.textMTU = i10;
            this.newCompress = z2;
            this.paperNum = i11;
            this.interval = i12;
            this.useSPP = z3;
            this.thinEneragy = i13;
            this.moderationEneragy = i14;
            this.deepenEneragy = i15;
            this.hasId = z4;
        }

        public boolean isHasId() {
            return this.hasId;
        }
        public void setHasId(boolean z) {
            this.hasId = z;
        }
        public int getThinEneragy() {
            return this.thinEneragy;
        }
        public int getModerationEneragy() {
            return this.moderationEneragy;
        }
        public int getDeepenEneragy() {
            return this.deepenEneragy;
        }
        public int getEneragy(int i) {
            if (i == 2) {
                return getThinEneragy();
            }
            if (i == 3) {
                return getModerationEneragy();
            }
            if (i == 5) {
                return getDeepenEneragy();
            }
            return getModerationEneragy();
        }
        public int getInterval() {
            return this.interval;
        }
        public int getImgMTU() {
            return this.imgMTU;
        }
        public int getTextMTU() {
            return this.textMTU;
        }
        public void setTextMTU(int i) {
            this.textMTU = i;
        }
        public void setImgMTU(int i) {
            this.imgMTU = i;
        }
        public boolean isNewCompress() {
            return this.newCompress;
        }
        public boolean isUseSPP() {
            return this.useSPP;
        }
        public void setPaperNum(int i) {
            this.paperNum = i;
        }
        public int getPrintSize() {
            return this.printSize;
        }
        public void setPrintSize(int i) {
            this.printSize = i;
        }
        public int getPaperNum() {
            return this.paperNum;
        }
        public int getImgPrintSpeed() {
            return this.imgPrintSpeed;
        }
        public void setImgPrintSpeed(int i) {
            this.imgPrintSpeed = i;
        }
        public int getTextPrintSpeed() {
            return this.textPrintSpeed;
        }
        public void setTextPrintSpeed(int i) {
            this.textPrintSpeed = i;
        }
        public int getDevdpi() {
            return this.devdpi;
        }
        public void setDevdpi(int i) {
            this.devdpi = i;
        }
        public void setHeadName(String str) {
            this.headName = str;
        }
        public int getOneLength() {
            return this.oneLength;
        }
        public void setOneLength(int i) {
            this.oneLength = i;
        }
        public String getModelNo() {
            return this.modelNo;
        }
        public void setModelNo(String str) {
            this.modelNo = str;
        }
        public String getModelName() {
            return this.modelName;
        }
        public void setModelName(String str) {
            this.modelName = str;
        }
        public int getModel() {
            return this.model;
        }
        public void setModel(int i) {
            this.model = i;
        }
        public int getSize() {
            return this.size;
        }
        public void setSize(int i) {
            this.size = i;
        }
        public int getPaperSize() {
            return this.paperSize;
        }
        public void setPaperSize(int i) {
            this.paperSize = i;
        }
        public boolean isCanChangeMTU() {
            return this.canChangeMTU;
        }
        public void setCanChangeMTU(boolean z) {
            this.canChangeMTU = z;
        }

        @Override
        public String toString() {

            StringBuilder result = new StringBuilder();
            String newLine = System.getProperty("line.separator");

            result.append( this.getClass().getName() );
            result.append( " Object {" );
            result.append(newLine);

            //determine fields declared in this class only (no fields of superclass)
            Field[] fields = this.getClass().getDeclaredFields();

            //print field names paired with their values
            for ( Field field : fields  ) {
                result.append("  ");
                try {
                    result.append( field.getName() );
                    result.append(": ");
                    //requires access to private field:
                    result.append( field.get(this) );
                } catch ( IllegalAccessException ex ) {
                    System.out.println(ex);
                }
                result.append(newLine);
            }
            result.append("}");

            return result.toString();
        }
    }

    public ArrayList<Integer> getCustomEnergy(String flag) {
        // getCustomEnergy: [7680, 7680, 9600, 9600, 12000, 12000, 14400, 14400, 17280, 17280, 20000, 20000, 20000, 20000]
        int eneragy = getModel(flag).getEneragy(3);
        int i = (int) ((double) eneragy * 0.8d);
        int i2 = (int) (((double) i) * 0.8d);
        int i3 = (int) ((double) eneragy * 1.2d);
        int i4 = 20000;
        if (i3 > 20000) {
            i3 = 20000;
        }
        int i5 = (int) (((double) i3) * 1.2d);
        int i6 = (int) (((double) i5) * 1.2d);
        int i7 = (int) (((double) i6) * 1.2d);
        if (i5 > 20000) {
            i5 = 20000;
        }
        if (i6 > 20000) {
            i6 = 20000;
        }
        if (i7 <= 20000) {
            i4 = i7;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(i2);
        arrayList.add(i2);
        arrayList.add(i);
        arrayList.add(i);
        arrayList.add(eneragy);
        arrayList.add(eneragy);
        arrayList.add(i3);
        arrayList.add(i3);
        arrayList.add(i5);
        arrayList.add(i5);
        arrayList.add(i6);
        arrayList.add(i6);
        arrayList.add(i4);
        arrayList.add(i4);
        return arrayList;
    }
}