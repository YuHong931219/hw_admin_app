package cn.zmmax.zebar.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.posapi.PosApi;

import java.io.UnsupportedEncodingException;

import cn.zmmax.zebar.MyApplication;

public class ScanBroadcastReceiver extends BroadcastReceiver {

    private static byte gpioTrig = 0x29;// PC9

    private static PosApi mPosSDK = MyApplication.getPosApi();

    @Override
    public void onReceive(Context context, Intent intent) {
        scanDown();
    }

    /**
     * 执行扫描，扫描后的结果会通过action为PosApi.ACTION_POS_COMM_STATUS的广播发回
     */
    public static void scanDown() {
        mPosSDK.gpioControl(gpioTrig, 0, 1);
        mPosSDK.gpioControl(gpioTrig, 0, 0);
        handler.removeCallbacks(run);
        // 3秒后还没有扫描到信息则强制关闭扫描头
        handler.postDelayed(run, 5000);
    }

    private static Handler handler = new Handler();
    private static Runnable run = () -> {
        // 强制关闭扫描头
        mPosSDK.gpioControl(gpioTrig, 0, 1);
    };

    /**
     * 扫描信息获取
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action != null && action.equalsIgnoreCase(PosApi.ACTION_POS_COMM_STATUS)) {
                // 串口标志判断
                int cmdFlag = intent.getIntExtra(PosApi.KEY_CMD_FLAG, -1);
                // 获取串口返回的字节数组
                byte[] buffer = intent.getByteArrayExtra(PosApi.KEY_CMD_DATA_BUFFER);

                // 如果为扫描数据返回串口
                if (cmdFlag == PosApi.POS_EXPAND_SERIAL3) {
                    if (buffer == null)
                        return;
                    try {
                        // 将字节数组转成字符串
                        String str = new String(buffer, "GBK");

                        // 开启提示音，提示客户条码或者二维码已经被扫到
                        //                            player.start();
                        //扫描信息通过广播发送出去
                        Intent intentBroadcast = new Intent();
                        intentBroadcast.putExtra("code", str);
                        intentBroadcast.setAction("com.qs.scancode");
                        //sendBroadcast(intentBroadcast);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}
