package com.example.bitmaptest;

import android.graphics.Bitmap;
import java.io.Serializable;


public class PrintBean implements Serializable {
    private String HtmlContent;
    private boolean addWhite = false;
    private Bitmap bitmap;
    private boolean canText;
    private boolean crop;
    private boolean istext;
    private int printType;
    private boolean show = true;
    private String tag;
    private String title;
    private int type = 0;

    public boolean isAddWhite() {
        return this.addWhite;
    }

    public void setAddWhite(boolean z) {
        this.addWhite = z;
    }

    public boolean isCanText() {
        return this.canText;
    }

    public void setCanText(boolean z) {
        this.canText = z;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public int getPrintType() {
        return this.printType;
    }

    public void setPrintType(int i) {
        this.printType = i;
    }

    public String getHtmlContent() {
        return this.HtmlContent;
    }

    public void setHtmlContent(String str) {
        this.HtmlContent = str;
    }

    public boolean isCrop() {
        return this.crop;
    }

    public void setCrop(boolean z) {
        this.crop = z;
    }

    public boolean isIstext() {
        return this.istext;
    }

    public void setIstext(boolean z) {
        this.istext = z;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public boolean isShow() {
        return this.show;
    }

    public void setShow(boolean z) {
        this.show = z;
    }
}

