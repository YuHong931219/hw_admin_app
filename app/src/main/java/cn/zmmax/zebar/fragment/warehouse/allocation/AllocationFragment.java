package cn.zmmax.zebar.fragment.warehouse.allocation;


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

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

@Page(name = "调拨", anim = CoreAnim.fade, extra = R.drawable.icon_allocation, category = PageCategory.warehouse)
public class AllocationFragment extends BaseBusinessFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    AllocationListFragment allocationListFragment;
    AllocationDetailFragment allocationDetailFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    protected void initViews() {
        allocationListFragment = new AllocationListFragment();
        allocationDetailFragment = new AllocationDetailFragment();
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(allocationListFragment.getTitle()));
        adapter.addFragment(allocationListFragment, allocationListFragment.getTitle());
        tabLayout.addTab(tabLayout.newTab().setText(allocationDetailFragment.getTitle()));
        adapter.addFragment(allocationDetailFragment, allocationDetailFragment.getTitle());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void getScanResult(String result) {

    }
}
