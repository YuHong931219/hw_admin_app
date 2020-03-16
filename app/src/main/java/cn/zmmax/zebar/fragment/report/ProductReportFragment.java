package cn.zmmax.zebar.fragment.report;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;

import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseBusinessFragment;

@Page(name = "生产报工", anim = CoreAnim.none, category = PageCategory.report, extra = R.drawable.icon_work_report)
public class ProductReportFragment extends BaseBusinessFragment {

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
