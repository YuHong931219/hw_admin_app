package cn.zmmax.zebar.fragment.other;


import android.view.ViewGroup;
import android.widget.TextView;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.actionbar.TitleUtils;
import com.xuexiang.xui.widget.grouplist.XUIGroupListView;
import com.xuexiang.xutil.app.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseFragment;
import cn.zmmax.zebar.utils.ZmxUtil;

@Page(name = "关于我们")
public class AboutFragment extends BaseFragment {

    @BindView(R.id.version)
    TextView mVersionTextView;
    @BindView(R.id.about_list)
    XUIGroupListView mAboutGroupListView;
    @BindView(R.id.copyright)
    TextView mCopyrightTextView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected TitleBar initTitle() {
        return TitleUtils.addTitleBarDynamic((ViewGroup) getRootView(), getPageTitle(), v -> popToBack());
    }

    @Override
    protected void initViews() {
        mVersionTextView.setText(String.format("版本号：%s", AppUtils.getAppVersionName()));
        XUIGroupListView.newSection(getContext())
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_homepage)),
                        v -> ZmxUtil.goWeb(getContext(), "http://www.ucom.net.cn/"))
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_operation)),
                        v -> ZmxUtil.goWeb(getContext(), "https://github.com/xuexiangjys/XUI/"))
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_update)),
                        v -> ZmxUtil.checkUpdate(getContext(), true))
                .addTo(mAboutGroupListView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String currentYear = dateFormat.format(new java.util.Date());
        mCopyrightTextView.setText(String.format(getResources().getString(R.string.about_copyright), currentYear));
    }
}
