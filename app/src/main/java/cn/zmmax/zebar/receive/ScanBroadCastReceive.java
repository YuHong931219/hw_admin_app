package cn.zmmax.zebar.receive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.zmmax.zebar.interfice.ScanInterface;

/**
 * Created by wenchuan on 2017/2/25.
 * com.symbol.datawedge.data_string （斑马新机型）
 * com.symbol.scanconfig.decode_data
 */

public class ScanBroadCastReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        try {
            String scanstr = null;
            if (b != null) {
                scanstr = (String) b.get("com.symbol.datawedge.data_string");
                if (scanstr == null) {
                    scanstr = (String) b.get("com.symbol.scanconfig.decode_data");
                }
            }
            Log.e("ScanBroadCastReceive", ".....扫描结果." + scanstr);
            // 后进先出
            if (((Activity) context).hasWindowFocus()) {
                if (scanstr != null) {
                    ((ScanInterface) context).ScanHandle(scanstr.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
