package cn.zmmax.zebar.fragment.other;


import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.model.PageInfo;

import java.util.List;

import cn.zmmax.scm.page.AppPageConfig;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseHomeFragment;

/**
 * 原材料收货
 */
@Page(name = "原料管理", anim = CoreAnim.none)
public class PurchaseFragment extends BaseHomeFragment {


    public PurchaseFragment() {
        setPageName("原料管理");
    }

    @Override
    protected List<PageInfo> getPageContents() {
        return AppPageConfig.getInstance().getPurchaseList();
    }

    @Override
    public int getMenuIcon() {
        return R.drawable.select_icon_tabbar_purchase;
    }
}
