package cn.zmmax.zebar.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.posapi.PosApi;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class ScanBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //这里写需要的业务逻辑
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
                    Log.e("二维码", "onReceive: " + str);
                    Intent intentBroadcast = new Intent();
                    intentBroadcast.putExtra("code", str);
                    intentBroadcast.setAction("com.qs.scanCode");
                    context.sendBroadcast(intentBroadcast);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
