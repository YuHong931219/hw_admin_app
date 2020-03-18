package cn.zmmax.zebar.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.CoreSwitchBean;

import cn.zmmax.zebar.http.subscriber.HttpLoadingDialog;

public class BaseActivity extends XPageActivity {

    public static final int REQUEST_CODE = 10001;
//    private ScanBroadCastReceive receiver;
    public IProgressLoader iProgressLoader;

    public interface ScanCallback {
        void handle(String code);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScanReceiver();
        iProgressLoader = new HttpLoadingDialog(this, "加载中...");
    }

    public Context getContext() {
        return this;
    }


    /**
     * 打开fragment
     *
     * @param clazz          页面类
     * @param addToBackStack 是否添加到栈中
     * @return 打开的fragment对象
     */
    @SuppressWarnings("unchecked")
    public <T extends XPageFragment> T openPage(Class<T> clazz, boolean addToBackStack) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setAddToBackStack(addToBackStack);
        return (T) openPage(page);
    }

    /**
     * 打开fragment
     *
     * @return 打开的fragment对象
     */
    @SuppressWarnings("unchecked")
    public <T extends XPageFragment> T openNewPage(Class<T> clazz) {
        CoreSwitchBean page = new CoreSwitchBean(clazz)
                .setNewActivity(true);
        return (T) openPage(page);
    }

    /**
     * 切换fragment
     *
     * @param clazz 页面类
     * @return 打开的fragment对象
     */
    public <T extends XPageFragment> T switchPage(Class<T> clazz) {
        return openPage(clazz, false);
    }

    public void initScanReceiver() {
//        receiver = new ScanBroadCastReceive();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.startPrint("cn.zmmax.zebar.scan");
//        intentFilter.addCategory("cn.zmmax.zebar.category.DEFAULT");
//        registerReceiver(receiver, intentFilter);
    }

    public void mobileScanBack(String code){
        Log.e("........", code);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (receiver != null) {
//            unregisterReceiver(receiver);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
//                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                        mobileScanBack(bundle.getString(CodeUtils.RESULT_STRING));
//                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                        Toast.makeText(BaseActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
//                    }
                }
            }
        }
    }
}