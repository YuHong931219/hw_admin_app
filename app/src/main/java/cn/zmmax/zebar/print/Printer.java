package cn.zmmax.zebar.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import cn.zmmax.zebar.MyApplication;

import static cn.zmmax.zebar.MyApplication.getInstance;

public class Printer {


    private static PosApi mPosSDK = MyApplication.getPosApi();

    private static PrintQueue mPrintQueue = null;

    private Printer() {
    }

    public Printer(Context context) {
        mPrintQueue = new PrintQueue(context, mPosSDK);
        mPrintQueue.init();
    }

    public PrintQueue.TextData getTextData() {
        return mPrintQueue.new TextData();
    }

    /**
     * 打印一维条码
     *
     * @param concentration:打印浓度    范围为25-65，超过范围后恢复至默认值25
     * @param left：左边距，一维条码与纸张左边的距离
     * @param width：一维条码的宽度         58纸张最大宽度为58
     * @param height：一维条码的高度
     * @param blackLabel:是否进行黑标检测
     * @param str2:                 一维条码对象(不能是中文，若为中文则会报错)
     */
    public static void printBarCode(int concentration, int left, int width, int height, boolean blackLabel, String str2) {

        if (str2 == null || str2.length() <= 0)
            return;

        // 判断当前字符能否生成条码
        if (str2.getBytes().length > str2.length()) {
            Toast.makeText(getInstance(), "当前数据不能生成一维码", Toast.LENGTH_SHORT).show();
            return;
        }
        // 生成条码图片
        Bitmap btMap = BarcodeCreater.creatBarcode(getInstance(), str2.trim(), width, height, true, 1);
        // 条码图片转成打印字节数组
        byte[] printData = BitmapTools.bitmap2PrinterBytes(btMap);

        mPosSDK.printImage(concentration, left, btMap.getWidth(), btMap.getHeight(), printData);

        if (blackLabel) {
            //黑标走纸指令
            mPosSDK.printerSetting(1, 0);
        }
    }

    /**
     * 打印二维码
     *
     * @param concentration:       打印浓度 范围为25-65，超过范围后恢复至默认值25
     * @param left：左边距，二维码与纸张左边的距离
     * @param width：二维码的宽度         58纸张最大宽度为58
     * @param height：二维码的高度
     * @param blackLabel:是否进行黑标检测
     * @param str2:                二维码对象
     */
    public void printQRCode(int concentration, int left, int width, int height, boolean blackLabel, String str2) {
        if (str2 == null || str2.length() <= 0)
            return;
        // 生成二维码图片
        Bitmap mBitmap = create2DCode(str2, width, height);
        if (mBitmap != null) {
            mBitmap = zoomImg(mBitmap, width, height);
            // 条码图片转成打印字节数组
            byte[] printData = BitmapTools.bitmap2PrinterBytes(mBitmap);
            //打印图片
            mPosSDK.printImage(concentration, left, mBitmap.getWidth(), mBitmap.getHeight(), printData);
            //黑标走纸
            if (blackLabel) {
                mPosSDK.printerSetting(1, 0);
            }
        }
    }

    /**
     * 打印bitmap
     */
    public static void printBitmap(int concentration, int left, boolean blackLabel, Bitmap btMap1) {
        // 图片获取并二值化
        Bitmap btMap = BitmapTools.gray2Binary(btMap1);

        // 把图片转成打印字节数组
        byte[] printData = BitmapTools.bitmap2PrinterBytes(btMap);

        // 打印
        mPosSDK.printImage(concentration, left, btMap.getWidth(), btMap.getHeight(), printData);

        if (blackLabel) {
            mPosSDK.printerSetting(1, 0);
        }
    }

    /**
     * 打印文字，文字宽度满一行自动换行排版，不满一整行不打印除非强制换行
     *
     * @param size:               打印字体大小，size为1时候为正常字体，size为2时候为双倍字体
     * @param concentration：打印浓度
     * @param align:              文字对齐方式 0 左对齐 1居中 2右对齐
     * @param blackLabel:是否进行黑标检测
     * @param str:                要打印的文字字符串
     */
    public static void printText(int size, int concentration, int align, boolean blackLabel, String str) {
        addPrintTextWithSize(size, concentration, align, str);
        if (blackLabel) {
            mPosSDK.printerSetting(1, 0);
        }
    }

    /*
     * 打印文字 size 1 --倍大小 2--2倍大小
     */
    private static void addPrintTextWithSize(int size, int concentration, int alignType, String sb) {
        if (sb.length() == 0)
            return;

        PrintQueue.TextData tData = mPrintQueue.new TextData();// 构造TextData实例
        if (size == 1) {
            tData.addParam(PrintQueue.PARAM_TEXTSIZE_1X);// 设置一倍字体大小
        } else {
            tData.addParam(PrintQueue.PARAM_TEXTSIZE_2X);// 设置双倍字体大小
        }
        if (alignType == 2) {
            tData.addParam(PrintQueue.PARAM_ALIGN_RIGHT);// 设置居左对齐
        } else if (alignType == 1) {
            tData.addParam(PrintQueue.PARAM_ALIGN_MIDDLE);// 设置居中对齐
        } else {
            tData.addParam(PrintQueue.PARAM_ALIGN_LEFT);// 设置居右对齐
        }
        tData.addText(sb);
        mPosSDK.printText(concentration, tData.getData(), tData.getData().length);
    }

    public static void sendCMD(byte[] bt1) {
        mPosSDK.printText(50, bt1, bt1.length);
    }


    /**
     * 文字转图片
     *
     * @param str
     * @return
     */
    public static Bitmap word2bitmap(String str, int width) {
        Bitmap bMap = Bitmap.createBitmap(260, 40, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bMap);
        canvas.drawColor(Color.WHITE);
        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(28.0F);
        StaticLayout layout = new StaticLayout(str, textPaint, bMap.getWidth(), Layout.Alignment.ALIGN_CENTER, (float) 1.0, (float) 0.0, true);
        layout.draw(canvas);
        return bMap;

    }

    /**
     * 文字转图片
     *
     * @param text     将要生成图片的内容
     * @param textSize 文字大小
     * @return
     */
    public Bitmap textAsBitmap(String text, float textSize, int width) {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        StaticLayout layout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_CENTER, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20, layout.getHeight() + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);
        layout.draw(canvas);
        Log.d("textAsBitmap", String.format("1:%d %d", layout.getWidth(), layout.getHeight()));
        return bitmap;
    }

    /**
     * 两张图片上下合并成一张
     *
     * @param bitmap1
     * @param bitmap2
     * @return
     */
    public static Bitmap twoBtmap2One(Bitmap bitmap1, Bitmap bitmap2) {
        Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight() + bitmap2.getHeight(), bitmap1.getConfig());
        Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, 0, bitmap1.getHeight(), null);
        return bitmap3;
    }

    /**
     * 生成去除白边的二维码
     *
     * @param str
     * @param width
     * @param height
     * @return
     */
    private Bitmap create2DCode(String str, int width, int height) {
        try {
            BitMatrix matrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, width, height);
            width = matrix.getWidth();
            height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = Color.BLACK;
                    } else {
                        pixels[y * width + x] = Color.WHITE;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    private static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public PrintQueue addText(int concentration, PrintQueue.TextData data) {
        mPrintQueue.addText(concentration, data);
        return mPrintQueue;
    }

    public PrintQueue addText(int concentration, byte[] data) {
        mPrintQueue.addText(concentration, data);
        return mPrintQueue;
    }

    public PrintQueue addQRCode(int concentration, int left, int width, int height, String data, Bitmap bitmap) {
        Bitmap qrCode = create2DCode(data, width, height);
        if (qrCode != null) {
            qrCode = zoomImg(qrCode, width, height);
            Bitmap twoBtmap2One = twoBtmap2One(qrCode, bitmap);
            byte[] printData = BitmapTools.bitmap2PrinterBytes(twoBtmap2One);
            mPrintQueue.addBmp(concentration, left, width, height, printData);
        }
        return mPrintQueue;
    }

    public PrintQueue startPrint() {
        mPrintQueue.addAction(PrintQueue.PRINTER_CMD_KEY_CHECKBLACK);
        mPrintQueue.printStart();
        return mPrintQueue;
    }
}
