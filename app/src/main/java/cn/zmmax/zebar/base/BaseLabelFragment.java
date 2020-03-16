package cn.zmmax.zebar.base;


import com.xuexiang.xui.widget.actionbar.TitleBar;

/**
 * 每个页签的基础Fragment
 */
public abstract class BaseLabelFragment extends BaseFragment {

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    public abstract String getTitle();

}
