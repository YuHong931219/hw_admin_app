package cn.zmmax.zebar.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import cn.zmmax.zebar.base.BaseActivity;

/**
 * Created by wenchuan on 2017/2/19.
 */

public class BlueBoothPrintUtil {

    private static boolean isSend = false;
    private static Handler mHandler = new Handler();


    public static void print(Context context, BluetoothDevice selectDevice, String msg, final BaseActivity.ScanCallback callback) {
        if(isSend) return;
        isSend = true;
        OutputStream os = null;
        try {
            // 获取到客户端接口
            BluetoothSocket clientSocket = selectDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            // 向服务端发送连接
            clientSocket.connect();
            // 获取到输出流，向外写数据
            os = clientSocket.getOutputStream();
            if (os != null) {
                // 需要发送的信息
//                String text = "^XA^LL500^FO100,0^A@,20,20,R:901.ARF^FD料  号:100001^FS^FO100,30^A@,20,20,R:901.ARF^FD品  名:英特尔 CPU^FS^FO100,60^A@,20,20,R:901.ARF^FD规  格:9600K^FS^FO100,90^A@,20,20,R:901.ARF^FD识别号:^FS^FO100,120^BCN,100,Y,N,N^BY1^FD19210001098#8^FS^PQ1,0,1,Y^XZ";
//                "^XA "
//                "^LL500
//                \\r\\n^FO100,0^A@,20,20,R:901.ARF^FD料  号:100001^FS
//                \\r\\n^FO100,30^A@,20,20,R:901.ARF^FD品  名:英特尔 CPU^FS
//                \\r\\n^FO100,60^A@,20,20,R:901.ARF^FD规  格:9600K^FS
//                \\r\\n^FO100,90^A@,20,20,R:901.ARF^FD识别号:^FS
//                \\r\\n^FO100,120^BCN,100,Y,N,N^BY1^FD19210001098#8^FS
//                \\r\\n^PQ1,0,1,Y
//                \\r\\n^XZ\n"
                String text = "^XA"+msg+"^XZ";

                // 以gbk的格式发送出去
                os.write(text.getBytes("gbk"));
                os.flush();
            }
            Toast.makeText(context, "发送信息成功", Toast.LENGTH_SHORT).show();

            if (os != null){
                os.close();
                os = null;
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.handle("1");
                }
            },200);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "发送信息失败", Toast.LENGTH_SHORT).show();
            if (os != null){
                try{
                    os.close();
                    os = null;
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.handle("0");
                }
            },200);

        } finally {
            isSend = false;

        }
    }

}
