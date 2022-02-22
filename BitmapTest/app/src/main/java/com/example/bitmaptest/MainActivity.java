package com.example.bitmaptest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    PrinterModel pm = new PrinterModel();
    PrinterModel.DataBean model = pm.getModel("no");
    PrintDataUtils printDataUtils = new PrintDataUtils();
    private Button btn;
    private ImageView image1, image2;
    private String TAG = pm.deviceName;
    private String code = "idk some param";
    private String type;
    private int printImageUrl = PrintDataUtils.IMG_PRINT_TYPE;
    private boolean needCrop = false;
    private int rotateNum = 0;
    private int fontSize = 8;
    private List<Bitmap> originalBitmapList;

    private ArrayList<PrintBean> landprintList = new ArrayList<>();
    private boolean showLandScape = false;

    private ArrayList<PrintBean> list = new ArrayList<>();
    public Bitmap printBitmap = null;
    int label_thumbnail_view4 = 5000;
    private ArrayList<PrintBean> printList = new ArrayList<>();

    private ImageDisposeUtil imageDisposeUtil = new ImageDisposeUtil();


//    public void PrintBitmap(int flag) {
//        if (flag == 1) {
//        //if (Code.QUESTION_HTML.equals(this.type) || Code.XUEKEWANG_HTML.equals(this.type)) {
//            if (this.code.equals("idk some param")) {
//                Bitmap bitmap = imageDisposeUtil.getBitmap("猫屁股", fontSize, true);
//                if (bitmap.getWidth() < model.getPrintSize()) {
//                    bitmap = imageDisposeUtil.mergeBitmap(bitmap);
//                }
//                PrintBean printBean = new PrintBean();
//                printBean.setShow(true);
//                printBean.setBitmap(bitmap);
//                this.list.add(printBean);
//            }
////            // Can't decompile imageDisposeUtil.corpLandImg
//////            if (this.showLandScape) {
//////                if (this.landprintList.size() > 0) {
//////                    if (this.landprintList.get(0).getBitmap().getWidth() != model.getPaperSize()) {
//////                        // Can't decompile imageDisposeUtil.corpLandImg
//////                        ArrayList<Bitmap> corpLandImg = imageDisposeUtil.corpLandImg(ViewToBitmap.scrollViewScreenShot(this.webLandscapeScrollview));
//////                        this.landprintList.clear();
//////
//////                        Iterator<Bitmap> it = corpLandImg.iterator();
//////                        while (it.hasNext()) {
//////                            Bitmap next = it.next();
//////                            if (next.getWidth() < model.getPrintSize()) {
//////                                next = imageDisposeUtil.mergeBitmap(next);
//////                            }
//////                            PrintBean printBean2 = new PrintBean();
//////                            printBean2.setShow(true);
//////                            printBean2.setBitmap(next);
//////                            printBean2.setIstext(true);
//////                            this.landprintList.add(printBean2);
//////                        }
//////                    }
//////                    this.list.addAll(this.landprintList);
//////                }
//////                if (this.list.size() > 0) {
//////                    return;
//////                }
//////                return;
//////            }
//            this.printBitmap = ViewToBitmap.scrollViewScreenShot((NestedScrollView) this.webScrollview);
//            // this.printBitmap = imageDisposeUtil.printText(this.printBitmap);
//            this.printBitmap = imageDisposeUtil.fitBitmap(this.printBitmap, model.getPaperSize(), true, true);
//            if (this.printBitmap.getWidth() < model.getPrintSize()) {
//                this.printBitmap = imageDisposeUtil.mergeBitmap(this.printBitmap);
//            }
//            PrintBean printBean3 = new PrintBean();
//            printBean3.setPrintType(PrintDataUtils.TEXT_PRINT_TYPE);
//            printBean3.setBitmap(this.printBitmap);
//            this.list.add(printBean3);
//        } else if (flag == 2) {
//            if (this.code.equals("idk some param")) {
//                try {
//                    this.printBitmap = ViewToBitmap.scrollViewScreenShot((NestedScrollView) this.webScrollview);
//                    int height = this.printBitmap.getHeight();
//                    ArrayList arrayList = new ArrayList();
//                    // crop image height to 5000
//                    if (height > 5000) {
//                        int i = label_thumbnail_view4;
//                        int i2 = 0;
//                        while (height > i2) {
//                            int i3 = height - i2;
//                            int i4 = i3 < 5000 ? i3 : i;
//                            Bitmap cropBitmapCustom = imageDisposeUtil.cropBitmapCustom(this.printBitmap, 0, i2, this.printBitmap.getWidth(), i4, false);
//                            i2 += i4;
//                            arrayList.add(cropBitmapCustom);
//                            i = i4;
//                        }
//                    } else {
//                        arrayList.add(this.printBitmap);
//                    }
//                    Iterator it2 = arrayList.iterator();
//                    while (it2.hasNext()) {
//                        Bitmap fitBitmap = imageDisposeUtil.fitBitmap(imageDisposeUtil.printText((Bitmap) it2.next()), model.getPaperSize(), true, true);
//                        if (fitBitmap.getWidth() < model.getPrintSize()) {
//                            fitBitmap = imageDisposeUtil.mergeBitmap(fitBitmap);
//                        }
//                        PrintBean printBean4 = new PrintBean();
//                        printBean4.setPrintType(PrintDataUtils.TEXT_PRINT_TYPE);
//                        printBean4.setBitmap(fitBitmap);
//                        this.list.add(printBean4);
//                    }
//                } catch (OutOfMemoryError unused) { }
//            } else {
//                Iterator<PrintBean> it3 = this.printList.iterator();
//                while (it3.hasNext()) {
//                    PrintBean next2 = it3.next();
//                    Bitmap bitmap2 = next2.getBitmap();
//                    if (bitmap2.getWidth() < model.getPrintSize()) {
//                        next2.setBitmap(imageDisposeUtil.mergeBitmap(bitmap2));
//                    }
//                }
//                this.list.addAll(this.printList);
//            }
//        } else if (flag == 3) {
//            this.printBitmap = ViewToBitmap.scrollViewScreenShot((NestedScrollView) this.webScrollview);
//            if (this.code.equals("idk some param")) {
//                Bitmap bitmap = imageDisposeUtil.getBitmap("猫屁股", fontSize, true);
//                if (bitmap.getWidth() < model.getPrintSize()) {
//                    bitmap = imageDisposeUtil.mergeBitmap(bitmap);
//                }
//                PrintBean printBean = new PrintBean();
//                printBean.setShow(true);
//                printBean.setBitmap(bitmap);
//                this.list.add(0, printBean);
//            }
//            //this.printBitmap = imageDisposeUtil.printText(this.printBitmap);
//            this.printBitmap = imageDisposeUtil.fitBitmap(this.printBitmap, model.getPaperSize(), true, true);
//            if (this.printBitmap.getWidth() < model.getPrintSize()) {
//                this.printBitmap = imageDisposeUtil.mergeBitmap(this.printBitmap);
//            }
//            PrintBean printBean2 = new PrintBean();
//            printBean2.setPrintType(PrintDataUtils.TEXT_PRINT_TYPE);
//            printBean2.setBitmap(this.printBitmap);
//            this.list.add(printBean2);
//        } else {
//            Bitmap bitmap;
//            if (this.printList.size() > 0) {
//                Bitmap bitmap2 = null;
//                if (this.code.equals("idk some param")) {
//                    bitmap2 = imageDisposeUtil.getBitmap("猫屁股", fontSize, true);
//                    if (bitmap2.getWidth() < model.getPrintSize()) {
//                        bitmap2 = imageDisposeUtil.mergeBitmap(bitmap2);
//                    }
//                }
//                int i = 0;
//                while (i < this.printList.size()) {
//                    if (this.originalBitmapList.size() > i) {
//                        PrintBean printBean = (PrintBean)this.printList.get(i);
//                        PrintImageView childAt = PrintPreviewActivity.this.linearImg.getChildAt(i);
//                        Bitmap bitmap3 = (Bitmap) PrintPreviewActivity.this.originalBitmapList.get(i);
//                        if (bitmap3 == null) {
//                            break;
//                        }
//                        if (printBean.getPrintType() != PrintDataUtils.IMG_PRINT_TYPE || bitmap3 == null) {
//                            if (this.code.equals("idk some param") && !Code.PRINT_STREAMER.equals(PrintPreviewActivity.this.activityType)) {
//                                // bitmap3 = imageDisposeUtil.printText(bitmap3);
//                            }
//                            Bitmap fitBitmap1 = imageDisposeUtil.fitBitmap1(bitmap3, model.getPrintSize());
//                            bitmap = (childAt == null || "cut".equals(childAt.getImgTag())) ? fitBitmap1 : (i != 0 || bitmap2 == null) ? childAt.mergeBitmap(fitBitmap1) : childAt.mergeBitmap(fitBitmap1, bitmap2);
//                        } else {
//                            bitmap = imageDisposeUtil.getShowBitmap(imageDisposeUtil.fitBitmap1(childAt.mergeBitmap(bitmap3), model.getPrintSize()));
//                        }
//                        if (bitmap.getWidth() < model.getPrintSize()) {
//                            bitmap = imageDisposeUtil.mergeBitmap(bitmap);
//                        }
//                        printBean.setBitmap(bitmap);
//                    }
//                    i++;
//                }
//                this.list.addAll(this.printList);
//            }
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bitmap bitmap = Bitmap.createBitmap(384, 384, Bitmap.Config.RGB_565);
                Bitmap bitmap = ((BitmapDrawable)image1.getDrawable()).getBitmap();

                Log.i(TAG, "Bitmap created");
                // printDataUtils.printArray(bitmap, false);
                // pm.printArray(bArr, true);

                Log.i(TAG, "Image width: " + bitmap.getWidth());
                Log.i(TAG, "Image height: " + bitmap.getHeight());

                Log.i(TAG, "Model name: " + model.getModelName());
                Log.i(TAG, "Model no: " + model.getModelNo());

                Log.i(TAG, "Print size: " + model.getPrintSize());
                Log.i(TAG, "Devapi: " + model.getDevdpi());
                Log.i(TAG, "Image MTU: " + model.getImgMTU());
                Log.i(TAG, "Image Print Speed: " + model.getImgPrintSpeed());
                Log.i(TAG, "Interval: " + model.getInterval());
                Log.i(TAG, "Moderation Eneragy: " + model.getModerationEneragy());
                Log.i(TAG, "One Length: " + model.getOneLength());
                Log.i(TAG, "Size: " + model.getSize());

                Log.i(TAG, "Paper Num: " + model.getPaperNum());
                Log.i(TAG, "Paper Size: " + model.getPaperSize());

                bitmap = imageDisposeUtil.fitBitmap(bitmap, model.getPaperSize(), true, true);

                if (bitmap.getWidth() < model.getPrintSize()) {
                    bitmap = imageDisposeUtil.mergeBitmap(bitmap);
                }

                Log.i(TAG, "Button onClick: bitmap created");
                image2.setImageBitmap(bitmap);

                PrintBean printBean = new PrintBean();
                printBean.setPrintType(PrintDataUtils.IMG_PRINT_TYPE);
                //printBean.setShow(true);
                printBean.setBitmap(bitmap);
                printBean.setIstext(false);
                //printBean.setCrop(true);

                ArrayList<PrintBean> arrayList = new ArrayList<>();
                arrayList.add(printBean);

                printDataUtils.BitmapToData(arrayList, bitmap.getWidth(), 3, true);
            }
        });
    }
}