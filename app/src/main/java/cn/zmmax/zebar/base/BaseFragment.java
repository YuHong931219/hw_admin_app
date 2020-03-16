package cn.zmmax.zebar.base;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.actionbar.TitleUtils;
import com.xuexiang.xutil.net.JsonUtil;

import cn.zmmax.zebar.http.subscriber.HttpLoadingDialog;

public abstract class BaseFragment extends XPageFragment {

    static final int CAMERA_REQUEST = 1;
    static final int REQUEST_IMAGE = 2;
    public IProgressLoader iProgressLoader;

    @Override
    protected void initPage() {
        initTitle();
        initViews();
        initListeners();
        iProgressLoader = new HttpLoadingDialog(getContext(), "加载中...");
    }

    protected TitleBar initTitle() {
        return TitleUtils.addTitleBarDynamic((ViewGroup) getRootView(), getPageTitle(), v -> popToBack());
    }

    @Override
    protected void initListeners() {

    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openNewPage(Class<T> clazz) {
        return new PageOption(clazz)
                .setNewActivity(true)
                .open(this);
    }

    /**
     * 打开一个新的页面
     *
     * @param name
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openNewPage(String name) {
        return new PageOption(name)
                .setAnim(CoreAnim.slide)
                .setNewActivity(true)
                .setAddToBackStack(true)
                .open(this);
    }

    /**
     * 打开一个新的页面
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openNewPage(Class<T> clazz, String key, Object value) {
        return new PageOption(clazz)
                .setNewActivity(true)
                .putString(key, JsonUtil.toJson(value))
                .open(this);
    }

    /**
     * 打开页面,需要结果返回
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends XPageFragment> Fragment openPageForResult(Class<T> clazz, String key, Object value, int requestCode) {
        return new PageOption(clazz)
                .setRequestCode(requestCode)
                .putString(key, JsonUtil.toJson(value))
                .open(this);
    }
}
