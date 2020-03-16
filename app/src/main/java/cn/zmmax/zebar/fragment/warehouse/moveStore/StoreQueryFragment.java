package cn.zmmax.zebar.fragment.warehouse.moveStore;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;

import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseBusinessFragment;

@Page(name = "库存查询", anim = CoreAnim.fade, category = PageCategory.warehouse, extra = R.drawable.icon_store_query)
public class StoreQueryFragment extends BaseBusinessFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    protected void initViews() {

    }

    @Override
    public void getScanResult(String result) {

    }
}
