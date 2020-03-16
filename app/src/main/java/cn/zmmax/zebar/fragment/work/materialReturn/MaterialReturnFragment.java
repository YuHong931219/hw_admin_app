package cn.zmmax.zebar.fragment.work.materialReturn;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;
import com.xuexiang.xui.adapter.FragmentAdapter;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseBusinessFragment;
import cn.zmmax.zebar.base.BaseFragment;
import cn.zmmax.zebar.fragment.purchase.warehouseBack.WarehouseBackDetailFragment;
import cn.zmmax.zebar.fragment.purchase.warehouseBack.WarehouseBackListFragment;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

@Page(name = "退料", anim = CoreAnim.fade, category = PageCategory.work, extra = R.drawable.icon_come_back)
public class MaterialReturnFragment extends BaseBusinessFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    MaterialReturnListFragment materialReturnListFragment;
    MaterialReturnDetailFragment materialReturnDetailFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    protected void initViews() {
        materialReturnListFragment = new MaterialReturnListFragment();
        materialReturnDetailFragment = new MaterialReturnDetailFragment();
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(materialReturnListFragment.getTitle()));
        adapter.addFragment(materialReturnListFragment, materialReturnListFragment.getTitle());
        tabLayout.addTab(tabLayout.newTab().setText(materialReturnDetailFragment.getTitle()));
        adapter.addFragment(materialReturnDetailFragment, materialReturnDetailFragment.getTitle());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void getScanResult(String result) {

    }
}
