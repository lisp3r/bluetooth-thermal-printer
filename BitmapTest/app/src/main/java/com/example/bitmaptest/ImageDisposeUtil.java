package com.example.bitmaptest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ImageDisposeUtil {
    static int[] histogram = new int[256];
    static double ma = 140.0d;
    static double mi = 55.0d;
    static int[] pixels3;
    public CallBackListener mCallBackListener;
    String TAG = "ImageDisposalUtil";

    private final PrinterModel pm = new PrinterModel();
    final PrinterModel.DataBean model = pm.getModel("no");

    public interface CallBackListener {
        void onBitmap(Bitmap bitmap);

        void onCancel();
    }

    public Bitmap getPrintBitmap(Bitmap bitmap) {
        return floydSteinbergDithering(bitmap);
    }

    private boolean isEmptyBitmap(Bitmap bitmap) {
        return bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }

    public Bitmap toGray(Bitmap bitmap) {
        return toGray(bitmap, false);
    }

    public Bitmap toGray(Bitmap bitmap, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        if (z && !bitmap.isRecycled() && createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public Bitmap getShowBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return convertGreyImgByFloyd(bitmap);
    }

    public Bitmap clip(Bitmap bitmap, int i, int i2, int i3, int i4) {
        return clip(bitmap, i, i2, i3, i4, false);
    }

    public Bitmap clip(Bitmap bitmap, int i, int i2, int i3, int i4, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, i, i2, i3, i4);
        if (z && !bitmap.isRecycled() && createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public Bitmap clip(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return clip(bitmap, 1, 2, bitmap.getWidth() - 1, bitmap.getHeight() - 2);
    }

    public Bitmap toGrayscale(Bitmap bitmap) {
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

    private native void binaryBlackAndWhiteNative(Bitmap bitmap);

    public Bitmap binaryBlackAndWhite(Bitmap bitmap) {
        bitmap = toGrayscale(bitmap);
        binaryBlackAndWhiteNative(bitmap);
        return bitmap;
    }

    public Bitmap getBlackAndWhite(Bitmap bitmap) {
        return binaryBlackAndWhite(bitmap);
    }

    public Bitmap fitBitmap(Bitmap bitmap, int i) {
        return fitBitmap(bitmap, i, false, false);
    }

    public Bitmap fitBitmap(Bitmap bitmap, int i, boolean z) {
        return fitBitmap(bitmap, i, false, z);
    }

    public Bitmap fitBitmap(Bitmap bitmap, int i, boolean z, boolean z2) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        if ((this.model.getSize() == 8 || this.model.getSize() == 4) && !z2) {
            i = (int) (((double) i) * 0.7d);
        }
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float f = ((float) i) / ((float) width);
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public Bitmap fitBitmap1(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        if (this.model.getSize() == 8 || this.model.getSize() == 4) {
            i = (int) (((double) i) * 0.7d);
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float f = ((float) i) / ((float) width);
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public Bitmap floydSteinbergDithering(Bitmap bitmap) {
        bitmap = toGrayscale(bitmap);
        floydSteinbergNative(bitmap);
        return bitmap;
    }

    private native void floydSteinbergNative(Bitmap bitmap);

    public int getPrintHeight(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }

        if (bitmap.getWidth() != this.model.getPrintSize()) {
            bitmap = fitBitmap(bitmap, this.model.getPrintSize());
        }
        if (bitmap == null) {
            return 0;
        }
        return (int) Math.ceil((((double) bitmap.getHeight()) / ((double) this.model.getOneLength())) / 10.0d);
    }

    public int getPrintHeight(List<Bitmap> list) {
        int i = 0;
        if (!(list == null || list.size() == 0)) {
            for (Bitmap bitmap : list) {
                i += getPrintHeight(bitmap);
            }
        }
        return i;
    }

    public int getPrintHeight(double d) {
        return (int) Math.ceil((d / ((double) this.model.getOneLength())) / 10.0d);
    }

    public Bitmap rotate(Bitmap bitmap, int i, float f, float f2) {
        return rotate(bitmap, i, f, f2, false);
    }

    public Bitmap rotate(Bitmap bitmap, int i, float f, float f2, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        if (i == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate((float) i, f, f2);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (z && !bitmap.isRecycled() && createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public Bitmap rotate(Bitmap bitmap, int i) {
        if (bitmap != null) {
            return rotate(bitmap, i * 90, (float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2));
        }
        return null;
    }

//    public static Bitmap printText(Bitmap bitmap) {
//        return printText(bitmap, true);
//    }

//    public static Bitmap printText(Bitmap bitmap, boolean z) {
//        if (bitmap == null) {
//            return null;
//        }
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size(), options);
//            Mat mat = new Mat();
//            Mat mat2 = new Mat();
//            org.opencv.android.Utils.bitmapToMat(decodeByteArray, mat);
//            Imgproc.cvtColor(mat, mat2, 7);
//            Imgproc.adaptiveThreshold(mat2, mat2, 255.0d, 1, 0, 51, 18.0d);
//            Bitmap createBitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), Bitmap.Config.RGB_565);
//            org.opencv.android.Utils.matToBitmap(mat2, createBitmap);
//            return createBitmap;
//        } catch (UnsatisfiedLinkError e) {
//            return null;
//        } catch (NullPointerException unused) {
//            return null;
//        }
//    }

//    public static Bitmap clearRed(Bitmap bitmap) {
//        if (bitmap == null) {
//            return null;
//        }
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size(), options);
//            Mat mat = new Mat();
//            org.opencv.android.Utils.bitmapToMat(decodeByteArray, mat);
//            ArrayList arrayList = new ArrayList();
//            Core.split(mat, arrayList);
//            Mat mat2 = (Mat) arrayList.get(2);
//            Mat mat3 = (Mat) arrayList.get(1);
//            Mat mat4 = (Mat) arrayList.get(0);
//            Mat mat5 = new Mat();
//            Imgproc.threshold(mat4, mat5, 0.85d * Imgproc.threshold(mat4, mat5, 0.0d, 255.0d, 8), 255.0d, 0);
//            Imgproc.adaptiveThreshold(mat5, mat5, 255.0d, 1, 0, 51, 18.0d);
//            Bitmap createBitmap = Bitmap.createBitmap(mat5.cols(), mat5.rows(), Bitmap.Config.RGB_565);
//            org.opencv.android.Utils.matToBitmap(mat5, createBitmap);
//            return createBitmap;
//        } catch (UnsatisfiedLinkError e) {
//            return null;
//        } catch (NullPointerException unused) {
//            return null;
//        }
//    }

//    public static Bitmap clearblue(Bitmap bitmap) {
//        if (bitmap == null) {
//            return null;
//        }
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size(), options);
//            Mat mat = new Mat();
//            org.opencv.android.Utils.bitmapToMat(decodeByteArray, mat);
//            ArrayList arrayList = new ArrayList();
//            Core.split(mat, arrayList);
//            Mat mat2 = (Mat) arrayList.get(2);
//            Mat mat3 = new Mat();
//            double threshold = 0.85d * Imgproc.threshold(mat2, mat3, 0.0d, 255.0d, 8);
//            Imgproc.threshold(mat2, mat3, threshold, 255.0d, 0);
//            Imgproc.adaptiveThreshold(mat3, mat3, 255.0d, 1, 0, 51, 18.0d);
//            Bitmap createBitmap = Bitmap.createBitmap(mat3.cols(), mat3.rows(), Bitmap.Config.RGB_565);
//            org.opencv.android.Utils.matToBitmap(mat3, createBitmap);
//            return createBitmap;
//        } catch (UnsatisfiedLinkError e) {
//            return null;
//        } catch (NullPointerException unused) {
//            return null;
//        }
//    }

//    public static Bitmap clearRedAndBlue(Bitmap bitmap) {
//        if (bitmap == null) {
//            return null;
//        }
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size(), options);
//            Mat mat = new Mat();
//            org.opencv.android.Utils.bitmapToMat(decodeByteArray, mat);
//            ArrayList arrayList = new ArrayList();
//            Core.split(mat, arrayList);
//            Mat mat2 = (Mat) arrayList.get(0);
//            Mat mat3 = new Mat();
//            double threshold = Imgproc.threshold(mat2, mat3, 0.0d, 255.0d, 8) * 0.85d;
//            Imgproc.threshold(mat2, mat3, threshold, 255.0d, 0);
//            Mat mat4 = new Mat();
//            double threshold2 = Imgproc.threshold((Mat) arrayList.get(2), mat4, 0.0d, 255.0d, 8) * 0.85d;
//            Imgproc.threshold(mat3, mat4, threshold2, 255.0d, 0);
//            Bitmap createBitmap = Bitmap.createBitmap(mat4.cols(), mat4.rows(), Bitmap.Config.RGB_565);
//            org.opencv.android.Utils.matToBitmap(mat4, createBitmap);
//            return createBitmap;
//        } catch (UnsatisfiedLinkError e) {
//            return null;
//        } catch (NullPointerException unused) {
//            return null;
//        }
//    }

//    public static Bitmap clearRedHSV(Bitmap bitmap) {
//        Mat mat = new Mat();
//        Mat mat2 = new Mat();
//        org.opencv.android.Utils.bitmapToMat(bitmap, mat);
//        Imgproc.cvtColor(mat, mat2, 41);
//        Mat mat3 = new Mat(mat2.size(), mat2.type());
//        for (int i = 0; i < mat2.rows(); i++) {
//            for (int i2 = 0; i2 < mat2.cols(); i2++) {
//                double[] dArr = (double[]) mat2.get(i, i2).clone();
//                double d = dArr[0];
//                double d2 = dArr[1];
//                double d3 = dArr[2];
//                if (((d >= 0.0d && d <= 10.0d) || (d >= 125.0d && d <= 180.0d)) && d2 >= 43.0d && d3 >= 46.0d) {
//                    dArr[0] = 0.0d;
//                    dArr[1] = 0.0d;
//                    dArr[2] = 255.0d;
//                }
//                mat3.put(i, i2, dArr);
//            }
//        }
//        Mat mat4 = new Mat();
//        Imgproc.cvtColor(mat3, mat4, 55);
//        Bitmap createBitmap = Bitmap.createBitmap(mat4.cols(), mat4.rows(), Bitmap.Config.RGB_565);
//        org.opencv.android.Utils.matToBitmap(mat4, createBitmap);
//        return createBitmap;
//    }

    public Bitmap clearRedPiexl(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        new ArrayList();
        for (int i = 0; i < iArr.length; i++) {
            int i2 = iArr[i];
            int i3 = (16711680 & i2) >> 16;
            int i4 = (65280 & i2) >> 8;
            int i5 = i2 & 255;
            Log.d("TAG", "r=" + i3 + ",g=" + i4 + ",b=" + i5);
            if (i3 >= 120 && i3 <= 255 && i4 <= 50 && i4 >= 0 && i5 >= 0 && i5 <= 50) {
                iArr[i] = 0;
            }
        }
        Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).setPixels(iArr, 0, width, 0, 0, width, height);
        return bitmap;
    }

//    public static Bitmap printTextNone(Bitmap bitmap) {
//        try {
//            Mat mat = new Mat();
//            Mat mat2 = new Mat();
//            org.opencv.android.Utils.bitmapToMat(bitmap, mat);
//            Imgproc.cvtColor(mat, mat, 7);
//            Imgproc.adaptiveThreshold(mat, mat2, 255.0d, 0, 0, 9, 5.0d);
//            Bitmap createBitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), Bitmap.Config.RGB_565);
//            org.opencv.android.Utils.matToBitmap(mat2, createBitmap);
//            return createBitmap;
//        } catch (UnsatisfiedLinkError e) {
//            return null;
//        }
//    }

//    public static Bitmap printText(Bitmap bitmap, int i, int i2) {
//        if (bitmap == null) {
//            return null;
//        }
//        try {
//            Mat mat = new Mat();
//            Mat mat2 = new Mat();
//            org.opencv.android.Utils.bitmapToMat(bitmap, mat);
//            Imgproc.cvtColor(mat, mat, 7);
//            Imgproc.adaptiveThreshold(mat, mat2, 255.0d, 1, 0, i, (double) i2);
//            Bitmap createBitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), Bitmap.Config.RGB_565);
//            org.opencv.android.Utils.matToBitmap(mat2, createBitmap);
//            return createBitmap;
//        } catch (UnsatisfiedLinkError e) {
//            return null;
//        } catch (NullPointerException unused) {
//            return null;
//        }
//    }

    public Bitmap getBitmap(CharSequence charSequence, int i, boolean z) {
        return getBitmap(charSequence, i, z, true, false, false, false, false);
    }

    public Bitmap getBitmap(CharSequence charSequence, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        Layout.Alignment alignment;
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            textPaint.setStrokeWidth(1.0f);
            textPaint.setTextSize((float) (this.model.getPrintSize() / i));
            textPaint.setUnderlineText(z);
            textPaint.setFakeBoldText(z3);
            if (z4) {
                textPaint.setTextSkewX(-0.25f);
            } else {
                textPaint.setTextSkewX(0.0f);
            }
            if (z5) {
                alignment = Layout.Alignment.ALIGN_CENTER;
            } else if (z6) {
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
            } else {
                alignment = Layout.Alignment.ALIGN_NORMAL;
            }
            StaticLayout staticLayout = new StaticLayout(charSequence, textPaint, this.model.getPrintSize(), alignment, 1.0f, 0.0f, false);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            Bitmap createBitmap = Bitmap.createBitmap(
                    this.model.getPrintSize(),
                    (int) Math.ceil((double) (((float) Math.ceil((double) (fontMetrics.descent - fontMetrics.ascent))) * ((float) staticLayout.getLineCount()))),
                    Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            canvas.translate(1.0f, 3.0f);
            canvas.drawColor(-1);
            staticLayout.draw(canvas);
            canvas.save();
            canvas.restore();
            return z2 ? textBitmap(createBitmap) : createBitmap;
        } catch (RuntimeException unused) {
            return null;
        }
    }

    public ArrayList<Bitmap> getBitmaps(CharSequence charSequence) {
        return getBitmaps(charSequence, 17, false);
    }

    public ArrayList<Bitmap> getBitmaps(CharSequence charSequence, boolean z) {
        return getBitmaps(charSequence, 17, z);
    }

    public ArrayList<Bitmap> getBitmaps(CharSequence charSequence, int i, boolean z) {
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            textPaint.setStrokeWidth(1.0f);
            textPaint.setTextSize((float) (this.model.getPrintSize() / i));
            textPaint.setUnderlineText(z);
            //textPaint.setTypeface(Typeface.createFromAsset(AppApplication.getAppContext().getAssets(), "siyuansongti.otf"));
            StaticLayout staticLayout = new StaticLayout(charSequence, textPaint, this.model.getPrintSize(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            int ceil = (int) Math.ceil((double) (((float) Math.ceil((double) (fontMetrics.descent - fontMetrics.ascent))) * ((float) staticLayout.getLineCount())));
            Log.e("", String.valueOf(Integer.valueOf(ceil)));
            Bitmap createBitmap = Bitmap.createBitmap(this.model.getPrintSize(), ceil, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            canvas.translate(1.0f, 3.0f);
            canvas.drawColor(-1);
            staticLayout.draw(canvas);
            canvas.save();
            canvas.restore();
            Bitmap textBitmap = textBitmap(createBitmap);
            ArrayList<Bitmap> arrayList = new ArrayList<>();
            int height = textBitmap.getHeight();
            if (height > 9000) {
                int i2 = 0;
                while (height >= i2) {
                    Log.e("", String.valueOf(Integer.valueOf(i2)));
                    Bitmap cropBitmapCustom = cropBitmapCustom(textBitmap, 0, i2, textBitmap.getWidth(), 9000, false);
                    i2 += 9000;
                    arrayList.add(cropBitmapCustom);
                }
            } else {
                arrayList.add(textBitmap);
            }
            return arrayList;
        } catch (RuntimeException unused) {
            return null;
        }
    }

    public Bitmap cropBitmapCustom(Bitmap bitmap, int i, int i2, int i3, int i4, boolean z) {
        Log.d("danxx", "cropBitmapRight before w : " + bitmap.getWidth());
        Log.d("danxx", "cropBitmapRight before h : " + bitmap.getHeight());
        if (i + i3 > bitmap.getWidth()) {
            i3 = bitmap.getWidth() - i;
        }
        if (i2 + i4 > bitmap.getHeight()) {
            i4 = bitmap.getHeight() - i2;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, i, i2, i3, i4);
        Log.d("danxx", "cropBitmapRight after w : " + createBitmap.getWidth());
        Log.d("danxx", "cropBitmapRight after h : " + createBitmap.getHeight());
        return createBitmap;
    }

    public Bitmap newBitmap(Bitmap bitmap, Bitmap bitmap2) {
        int width = bitmap.getWidth();
        if (bitmap2.getWidth() != width) {
            int height = (bitmap2.getHeight() * width) / bitmap2.getWidth();
            Bitmap createBitmap = Bitmap.createBitmap(width, bitmap.getHeight() + height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Bitmap resizeBitmap = resizeBitmap(bitmap2, width, height);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas.drawBitmap(resizeBitmap, 0.0f, (float) bitmap.getHeight(), (Paint) null);
            return createBitmap;
        }
        Bitmap createBitmap2 = Bitmap.createBitmap(width, bitmap.getHeight() + bitmap2.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas2.drawBitmap(bitmap2, 0.0f, (float) bitmap.getHeight(), (Paint) null);
        return createBitmap2;
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {
        Matrix matrix = new Matrix();
        matrix.postScale(((float) i) / ((float) bitmap.getWidth()), ((float) i2) / ((float) bitmap.getHeight()));
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap textBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int pixel = bitmap.getPixel(i2, i);
                if (!(pixel == -1 || i2 == 0 || i == 0 || i2 == width - 1 || i == height - 1)) {
                    int i3 = i2 - 1;
                    int i4 = (i - 1) * width;
                    int i5 = i3 + i4;
                    int i6 = i * width;
                    int i7 = i3 + i6;
                    int i8 = (i + 1) * width;
                    if (((iArr[i3 + i8] + 255) & iArr[i5] & (iArr[i7] + 255) & 255) == 0) {
                        int i9 = iArr[i2 + 2 + i6] & 255;
                        int i10 = iArr[i2 + 1 + i6] & 255;
                        int i11 = iArr[i7] & 255;
                        int i12 = iArr[(i2 - 2) + i6] & 255;
                        if (i10 == 0 && i11 == 0 && !(i9 == 0 && i12 == 0)) {
                            int i13 = iArr[i2 + i6];
                            pixel = -1;
                        }
                    }
                    int i14 = i2 + i4;
                    if ((iArr[i5] & (iArr[i14] + 255) & (iArr[i2 + 1 + i4] + 255) & 255) == 0) {
                        int i15 = iArr[i8 + i2] & 255;
                        int i16 = iArr[i14] & 255;
                        if (i15 == 255 && i16 == 0) {
                            Log.e("", "tmppixel---" + -1 + "---pixel---" + iArr[i6 + i2]);
                            pixel = -1;
                            iArr[(i * width) + i2] = pixel;
                        }
                    }
                }
                iArr[(i * width) + i2] = pixel;
            }
        }
        return Bitmap.createBitmap(iArr, width, height, Bitmap.Config.RGB_565);
    }

    public Bitmap convertGreyImgByFloyd(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap gray = toGray(bitmap);
        int width = gray.getWidth();
        int height = gray.getHeight();
        int i = width * height;
        int[] iArr = new int[i];
        gray.getPixels(iArr, 0, width, 0, 0, width, height);
        int[] iArr2 = new int[i];
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int i4 = (width * i2) + i3;
                iArr2[i4] = Color.red(iArr[i4]);
            }
        }
        int i5 = 0;
        while (i5 < height) {
            boolean z = i5 < height + -1;
            int i6 = 0;
            while (i6 < width) {
                boolean z2 = i6 > 0;
                boolean z3 = i6 < width + -1;
                int i7 = width * i5;
                int i8 = i7 + i6;
                int i9 = iArr2[i8];
                if (i9 >= 128) {
                    iArr[i8] = -1;
                    i9 -= 255;
                } else {
                    iArr[i8] = -16777216;
                }
                if (z3) {
                    int i10 = i6 + 1 + i7;
                    iArr2[i10] = iArr2[i10] + ((i9 * 7) / 16);
                }
                if (z2 && z) {
                    int i11 = (i6 - 1) + ((i5 + 1) * width);
                    iArr2[i11] = iArr2[i11] + ((i9 * 3) / 16);
                }
                if (z) {
                    int i12 = ((i5 + 1) * width) + i6;
                    iArr2[i12] = iArr2[i12] + ((i9 * 5) / 16);
                }
                if (z3 && z) {
                    int i13 = i6 + 1 + ((i5 + 1) * width);
                    iArr2[i13] = iArr2[i13] + (i9 / 16);
                }
                i6++;
            }
            i5++;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public Bitmap toGrayScale(Bitmap bitmap) {
        int behavior_skipCollapsed = 150;
        int autoSizeMaxTextSize = 130;
        int i;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[width * height];
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int pixel = (int) ((((double) ((float) ((bitmap.getPixel(i3, i2) & 16711680) >> 16))) * 0.3d) +
                        (((double) ((float) ((bitmap.getPixel(i3, i2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8))) * 0.59d) +
                        (((double) ((float) (bitmap.getPixel(i3, i2) & 255))) * 0.11d));
                if (pixel > 0 && pixel < 130) {
                    i = (pixel * 85) / autoSizeMaxTextSize;
                } else if (pixel <= 130 || pixel >= 150) {
                    i = (((pixel - 150) * 105) / 105) + behavior_skipCollapsed;
                } else {
                    i = ((65 * (pixel - 130)) / 20) + 85;
                }
                iArr[(width * i2) + i3] = i;
            }
        }
        return convertGreyImgByFloyd(iArr, width, height);
    }

//    public static Bitmap toGrayScale(Bitmap bitmap) {
//        int width = bitmap.getWidth();
//        int heigh = bitmap.getHeight();
//        int[] ai = new int[width * heigh];
//
//        for(int k = 0; k < heigh; k++) {
//            for(int l = 0; l < width; l++) {
//                int i1 = bitmap.getPixel(l, k);
//                int j1 = bitmap.getPixel(l, k);
//                int k1 = bitmap.getPixel(l, k);
//                k1 = (int)((double)(float)((i1 & 0xff0000) >> 16) * 0.29999999999999999D +
//                        (double)(float)((j1 & 0xff00) >> 8) * 0.58999999999999997D +
//                        (double)(float)(k1 & 0xff) * 0.11D);
//                if(k1 > 0 && k1 < 130)
//                    k1 = (k1 * 85) / 130;
//                else
//                if(k1 > 130 && k1 < 150)
//                    k1 = (65 * (k1 - 130)) / 20 + 85;
//                else
//                    k1 = ((k1 - 150) * 105) / 105 + 150;
//                ai[width * k + l] = k1;
//            }
//        }
//        return convertGreyImgByFloyd(ai, width, heigh);
//    }

    public Bitmap convertGreyImgByFloyd(int[] iArr, int i, int i2) {
        int i3;
        int[] iArr2 = new int[(i * i2)];
        int i4 = 0;
        while (i4 < i2) {
            boolean z = i4 < i2 + -1;
            int i5 = 0;
            while (i5 < i) {
                boolean z2 = i5 > 0;
                boolean z3 = i5 < i + -1;
                int i6 = i * i4;
                int i7 = i6 + i5;
                int i8 = iArr[i7];
                if (i8 >= 128) {
                    iArr2[i7] = -1;
                    i3 = i8 - 255;
                } else {
                    iArr2[i7] = -16777216;
                    i3 = i8 + 0;
                }
                if (z3) {
                    int i9 = i5 + 1 + i6;
                    iArr[i9] = iArr[i9] + ((i3 * 7) / 16);
                }
                if (z2 && z) {
                    int i10 = (i5 - 1) + ((i4 + 1) * i);
                    iArr[i10] = iArr[i10] + ((i3 * 3) / 16);
                }
                if (z) {
                    int i11 = ((i4 + 1) * i) + i5;
                    iArr[i11] = iArr[i11] + ((i3 * 5) / 16);
                }
                if (z3 && z) {
                    int i12 = i5 + 1 + ((i4 + 1) * i);
                    iArr[i12] = iArr[i12] + (i3 / 16);
                }
                i5++;
            }
            i4++;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
        createBitmap.setPixels(iArr2, 0, i, 0, 0, i, i2);
        return createBitmap;
    }

//    public static Bitmap ImageEnhancement(Bitmap bitmap) {
//        Mat mat = new Mat();
//        Mat mat2 = new Mat();
//        org.opencv.android.Utils.bitmapToMat(bitmap, mat);
//        Imgproc.cvtColor(mat, mat, 7);
//        CLAHE createCLAHE = Imgproc.createCLAHE();
//        createCLAHE.setClipLimit(4.0d);
//        createCLAHE.setTilesGridSize(new Size(8.0d, 8.0d));
//        createCLAHE.apply(mat, mat2);
//        Bitmap createBitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), Bitmap.Config.RGB_565);
//        org.opencv.android.Utils.matToBitmap(mat2, createBitmap);
//        return createBitmap;
//    }

    public Bitmap view2Bitmap(View view) {
        Bitmap bitmap = null;
        if (view == null) {
            return null;
        }
        if (!(view.getWidth() == 0 || view.getHeight() == 0)) {
            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Drawable background = view.getBackground();
            if (background != null) {
                background.draw(canvas);
            } else {
                canvas.drawColor(-1);
            }
            view.draw(canvas);
        }
        return bitmap;
    }

    public Bitmap scale(Bitmap bitmap, int i, int i2) {
        return scale(bitmap, i, i2, false);
    }

    public Bitmap scale(Bitmap bitmap, int i, int i2, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i2, true);
        if (z && !bitmap.isRecycled() && createScaledBitmap != bitmap) {
            bitmap.recycle();
        }
        return createScaledBitmap;
    }

    public Bitmap scale(Bitmap bitmap, float f, float f2) {
        return scale(bitmap, f, f2, false);
    }

    public Bitmap scale(Bitmap bitmap, float f, float f2, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(f, f2);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (z && !bitmap.isRecycled() && createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public Bitmap addImageWatermark(Bitmap bitmap, Bitmap bitmap2, int i, int i2, int i3) {
        return addImageWatermark(bitmap, bitmap2, i, i2, i3, false);
    }

    public Bitmap addImageWatermark(Bitmap bitmap, Bitmap bitmap2, int i, int i2, int i3, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
        if (!isEmptyBitmap(bitmap2)) {
            Paint paint = new Paint(1);
            Canvas canvas = new Canvas(copy);
            paint.setAlpha(i3);
            canvas.drawBitmap(bitmap2, (float) i, (float) i2, paint);
        }
        if (z && !bitmap.isRecycled() && copy != bitmap) {
            bitmap.recycle();
        }
        return copy;
    }

    public float div(float f, float f1, int i) {
        if(i >= 0)
            return (new BigDecimal(Double.toString(f))).divide(new BigDecimal(Double.toString(f1)), i, 4).floatValue();
        else
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }

    public float div(int i, int j, int k) {
        if(k >= 0)
            return (new BigDecimal(Double.toString(i))).divide(new BigDecimal(Double.toString(j)), k, 4).floatValue();
        else
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }

    public Bitmap mergeBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        float div = div(div(bitmap.getWidth() * model.getOneLength() * 10 * 11, this.model.getPrintSize(), 5), (float) bitmap.getHeight(), 5);
        int width = (int) (((float) bitmap.getWidth()) * div);
        int height = (int) (((float) bitmap.getHeight()) * div);
        Bitmap scale = scale(bitmap, width, height);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        canvas.drawColor(-1);
        canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
        return addImageWatermark(createBitmap, scale, (bitmap.getWidth() - width) / 2, 0, 255);
    }

    public String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    public CallBackListener getmCallBackListener() {
        return this.mCallBackListener;
    }

    public void setmCallBackListener(CallBackListener callBackListener) {
        this.mCallBackListener = callBackListener;
    }

    public Bitmap mergeBitmap(Bitmap bitmap) {
        int printImgSize = this.model.getPrintSize();
        Bitmap createBitmap = Bitmap.createBitmap(printImgSize, bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        canvas.drawColor(-1);
        canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
        return addImageWatermark(createBitmap, bitmap, (printImgSize - bitmap.getWidth()) / 2, 0, 255);
    }

    public Bitmap pngToWhite(Bitmap bitmap) {
        Bitmap copy = bitmap.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(copy);
        canvas.drawColor(-1);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        return copy;
    }

//    public ArrayList corpLandImg(Bitmap bitmap) {
//        ArrayList arraylist = new ArrayList();
//
//        int oneLengthScaled = this.model.getOneLength() * 100; // 8 * 100
//        Bitmap bitmap1 = fitBitmap(bitmap, i);
//
//        Log.i(TAG, "getOneLength: " + oneLengthScaled);
//
//        //bitmap1 = fitBitmap(bitmap, i);
//
//        int paperSize = this.model.getPaperSize(); // 384
//        Log.i(TAG, "getPaperSize: " + paperSize);
//
////        bitmap = JVM INSTR new #237 <Class StringBuilder>;
////        bitmap.StringBuilder();
////        bitmap.append("landScape----");
////        bitmap.append(i);
////        bitmap.append("h----");
////        bitmap.append(paperSize);
////        LogUtils.e(new Object[] {
////                bitmap.toString()
////        });
//
//        oneLengthScaled = bitmap1.getWidth() - 15;
//
//        while (oneLengthScaled < bitmap1.getWidth()) {
//            int k = 0;
//            if(k >= bitmap1.getHeight()) {
//                break;
//            }
//            bitmap1.setPixel(oneLengthScaled, k, -1);
//            k++;
//            oneLengthScaled++;
//        }
//
//        int l = 1;
//        oneLengthScaled = paperSize;
//        int k = 0;
//
//        while (l != 0) { // L15
//
//        // if(k >= bitmap1.getHeight() - paperSize)
//        //    break MISSING_BLOCK_LABEL_359;
//
//            int i1 = 0;
//            while (i1 < bitmap.getWidth()) { // L5
//
//                // if(bitmap1.getPixel(i1, oneLengthScaled + k) == -1)
//                //     break MISSING_BLOCK_LABEL_199;
//
//                oneLengthScaled--;
//                if(oneLengthScaled < paperSize / 2) {
//                    oneLengthScaled = paperSize;
//                    break; /* Loop/switch isn't completed */
//                }
//
//            }
//
//
//        }
//
//        _L15:
//        if(l == 0)
//            break; /* Loop/switch isn't completed */
//
//        if(k >= bitmap1.getHeight() - paperSize)
//            break MISSING_BLOCK_LABEL_359;
//        int i1 = 0;
//
//        _L5:
//        if(i1 >= bitmap1.getWidth())
//            break; /* Loop/switch isn't completed */
//
//        if(bitmap1.getPixel(i1, oneLengthScaled + k) == -1)
//            break MISSING_BLOCK_LABEL_199;
//
//        oneLengthScaled--;
//        if(oneLengthScaled < paperSize / 2)
//        {
//            oneLengthScaled = paperSize;
//            break; /* Loop/switch isn't completed */
//        }
//        i1 = 0;
//        break MISSING_BLOCK_LABEL_208;
//        i1++;
//        if(true) goto _L5; else goto _L4
//        _L4:
//        i1 = 1;
//        if(i1 == 0)
//            continue; /* Loop/switch isn't completed */
//        bitmap = ImageUtils.clip(bitmap1, 0, k, bitmap1.getWidth(), i);
//        int j1;
//        j1 = 0;
//        i1 = 0;
//        _L9:
//        if(j1 >= bitmap.getWidth())
//            break; /* Loop/switch isn't completed */
//        int k1 = 0;
//        _L7:
//        int l1 = i1;
//        if(k1 >= bitmap.getHeight())
//            break; /* Loop/switch isn't completed */
//        if(bitmap.getPixel(j1, k1) != -1)
//        {
//            l1 = 1;
//            break; /* Loop/switch isn't completed */
//        }
//        k1++;
//        if(true) goto _L7; else goto _L6
//        _L6:
//        j1++;
//        i1 = l1;
//        if(true) goto _L9; else goto _L8
//        _L8:
//        if(!i1)
//            break MISSING_BLOCK_LABEL_344;
//        Bitmap bitmap2 = ImageUtils.rotate(bitmap, 90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
//        bitmap = bitmap2;
//        if(bitmap2.getWidth() != AppApplication.getPrintSize())
//            bitmap = mergeBitmap(bitmap2);
//        arraylist.add(bitmap);
//        k += oneLengthScaled;
//        oneLengthScaled = paperSize;
//        continue; /* Loop/switch isn't completed */
//        bitmap = ImageUtils.clip(bitmap1, 0, k, bitmap1.getWidth(), bitmap1.getHeight() - k);
//        l = 0;
//        i1 = 0;
//        _L13:
//        if(l >= bitmap.getWidth())
//            break; /* Loop/switch isn't completed */
//        l1 = 0;
//        _L11:
//        j1 = i1;
//        if(l1 >= bitmap.getHeight())
//            break; /* Loop/switch isn't completed */
//        if(bitmap.getPixel(l, l1) != -1)
//        {
//            j1 = 1;
//            break; /* Loop/switch isn't completed */
//        }
//        l1++;
//        if(true) goto _L11; else goto _L10
//        _L10:
//        l++;
//        i1 = j1;
//        if(true) goto _L13; else goto _L12
//        _L12:
//        if(!i1)
//            break MISSING_BLOCK_LABEL_496;
//        bitmap2 = ImageUtils.rotate(bitmap, 90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
//        bitmap = bitmap2;
//        if(bitmap2.getWidth() != AppApplication.getPrintSize())
//            bitmap = mergeBitmap(bitmap2);
//        arraylist.add(bitmap);
//        k += oneLengthScaled;
//        l = 0;
//        if(true) goto _L15; else goto _L14
//
//        _L14:
//        if(bitmap1 == null)
//            try
//            {
//                if(!bitmap1.isRecycled())
//                    bitmap1.recycle();
//            }
//            // Misplaced declaration of an exception variable
//            catch(Bitmap bitmap) { }
//        return arraylist;
//    }
}
