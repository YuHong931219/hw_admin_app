package cn.zmmax.zebar.utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * 震动工具类
 */
public class VibrateUtil {

    public static void vibrate(final Activity activity) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        if (vib != null) {
            vib.vibrate(1000);
        }
    }
}
