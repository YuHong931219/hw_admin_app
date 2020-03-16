package cn.zmmax.zebar.fragment.other;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.model.PageInfo;

import java.util.List;

import cn.zmmax.scm.page.AppPageConfig;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseHomeFragment;

@Page(name = "仓库管理", anim = CoreAnim.none)
public class WarehouseFragment extends BaseHomeFragment {

    public WarehouseFragment() {
        setPageName("仓库管理");
    }

    @Override
    protected List<PageInfo> getPageContents() {
        return AppPageConfig.getInstance().getWarehouseList();
    }

    @Override
    public int getMenuIcon() {
        return R.drawable.select_icon_tabbar_warehouse;
    }
}
