package cn.zmmax.zebar.base;

import android.content.Context;
import android.os.Bundle;

import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.CoreSwitchBean;

import cn.zmmax.zebar.http.subscriber.HttpLoadingDialog;

public class BaseActivity extends XPageActivity {

    public IProgressLoader iProgressLoader;

    public interface ScanCallback {
        void handle(String code);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}