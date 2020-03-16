package cn.zmmax.zebar.fragment.warehouse.miscellany;

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

@Page(name = "杂发", anim = CoreAnim.fade, category = PageCategory.warehouse, extra = R.drawable.icon_out)
public class MiscellanyOutFragment extends BaseBusinessFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    MiscellanyOutListFragment miscellanyOutListFragment;
    MiscellanyOutDetailFragment miscellanyOutDetailFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    protected void initViews() {
        miscellanyOutListFragment = new MiscellanyOutListFragment();
        miscellanyOutDetailFragment = new MiscellanyOutDetailFragment();
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(miscellanyOutListFragment.getTitle()));
        adapter.addFragment(miscellanyOutListFragment, miscellanyOutListFragment.getTitle());
        tabLayout.addTab(tabLayout.newTab().setText(miscellanyOutDetailFragment.getTitle()));
        adapter.addFragment(miscellanyOutDetailFragment, miscellanyOutDetailFragment.getTitle());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void getScanResult(String result) {

    }
}
