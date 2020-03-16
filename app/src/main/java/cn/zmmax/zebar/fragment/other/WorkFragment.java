package cn.zmmax.zebar.fragment.other;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.model.PageInfo;

import java.util.List;

import cn.zmmax.scm.page.AppPageConfig;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseHomeFragment;

@Page(name = "生产管理", anim = CoreAnim.none)
public class WorkFragment extends BaseHomeFragment {

    public WorkFragment() {
        setPageName("生产管理");
    }

    @Override
    protected List<PageInfo> getPageContents() {
        return AppPageConfig.getInstance().getWorkList();
    }

    @Override
    public int getMenuIcon() {
        return R.drawable.select_icon_tabbar_work;
    }
}
