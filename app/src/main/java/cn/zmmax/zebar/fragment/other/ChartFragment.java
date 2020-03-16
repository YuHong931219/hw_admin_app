package cn.zmmax.zebar.fragment.other;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.model.PageInfo;

import java.util.List;

import cn.zmmax.zebar.base.BaseHomeFragment;

@Page(name = "统计", anim = CoreAnim.fade)
public class ChartFragment extends BaseHomeFragment {

    @Override
    protected List<PageInfo> getPageContents() {
        return null;
    }

    @Override
    public int getMenuIcon() {
        return 0;
    }

}
