package cn.zmmax.zebar.utils;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class QRCodeUtils {

    /* 0#送货单 */
    public static final String TYPE_0 = "0";
    /* 1#物料#送货单明细ID/批号 */
    public static final String TYPE_1 = "1";
    /* 2#库位 */
    public static final String TYPE_2 = "2";
    /* 3#销退单 */
    public static final String TYPE_3 = "3";
    /* 4#工单 */
    public static final String TYPE_4 = "4";
    /* 暂定 */
    public static final String TYPE_5 = "5";
    /* 暂定 */
    public static final String TYPE_6 = "6";
    /* 7#杂收单/杂发单 */
    public static final String TYPE_7 = "7";
    /* 8#出货通知单 */
    public static final String TYPE_8 = "8";
    /* 9#调拨单 */
    public static final String TYPE_9 = "9";
    /* 暂定 */
    public static final String TYPE_10 = "10";
    /* 暂定 */
    public static final String TYPE_11 = "11";
    /* 暂定 */
    public static final String TYPE_12 = "12";
    /* 13#仓退单 */
    public static final String TYPE_13 = "13";
    /* 暂定 */
    public static final String TYPE_14 = "14";

    private String[] qr;

    public QRCodeUtils(String qrCode) {
        qr = qrCode.split("#");
    }


    public String getContentByPosition(int position) {
        return qr[position];
    }


    /**
     * 生成简单二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param color_black            黑色色块
     * @param color_white            白色色块
     * @return BitMap
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height, String character_set, ErrorCorrectionLevel error_correction_level, int color_black, int color_white) {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // 宽和高>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set);
            }
            // 容错率设置
            if (error_correction_level != null) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
            }
            /* 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            /* 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black;//黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white;// 白色色块像素设置
                    }
                }
            }
            /* 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void layoutView(Activity activity, View v) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        // 整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        c.drawBitmap(bmp, new Matrix(), textPaint);
        /* 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }
}
