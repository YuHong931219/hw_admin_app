package cn.zmmax.zebar.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.posapi.PosApi;

import cn.zmmax.zebar.MyApplication;

/**
 * 扫描按钮广播
 */
public class ButtonBroadcastReceiver extends BroadcastReceiver {

    private static PosApi mPosSDK = MyApplication.getPosApi();
    private static byte gpioTrig = 0x29;// PC9
    private static Handler handler = new Handler();
    private static Runnable run = () -> {
        mPosSDK.gpioControl(gpioTrig, 0, 1);
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        mPosSDK.gpioControl(gpioTrig, 0, 1);
        mPosSDK.gpioControl(gpioTrig, 0, 0);
        handler.removeCallbacks(run);
        handler.postDelayed(run, 3000);
    }
}
