package cn.zmmax.zebar.fragment.work.workEntry;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;
import com.xuexiang.xui.adapter.FragmentAdapter;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseFragment;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

@Page(name = "完工入库", anim = CoreAnim.fade, category = PageCategory.work, extra = R.drawable.icon_product_entry)
public class WorkEntryFragment extends BaseFragment {


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private WorkEntryListFragment workEntryListFragment;
    private WorkEntryDetailFragment workEntryDetailFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    protected void initViews() {
        workEntryListFragment = new WorkEntryListFragment();
        workEntryDetailFragment = new WorkEntryDetailFragment();
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(workEntryDetailFragment.getTitle()));
        adapter.addFragment(workEntryDetailFragment, workEntryDetailFragment.getTitle());
        tabLayout.addTab(tabLayout.newTab().setText(workEntryListFragment.getTitle()));
        adapter.addFragment(workEntryListFragment, workEntryListFragment.getTitle());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}