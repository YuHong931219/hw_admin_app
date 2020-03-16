package cn.zmmax.zebar.fragment.warehouse.moveStore;

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

@Page(name = "移库", anim = CoreAnim.fade, category = PageCategory.warehouse, extra = R.drawable.icon_move_store)
public class MoveStoreFragment extends BaseBusinessFragment {


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    MoveStoreDetailFragment moveStoreDetailFragment;
    MoveStoreListFragment moveStoreListFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    protected void initViews() {
        moveStoreDetailFragment = new MoveStoreDetailFragment();
        moveStoreListFragment = new MoveStoreListFragment();
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(moveStoreDetailFragment.getTitle()));
        adapter.addFragment(moveStoreDetailFragment, moveStoreDetailFragment.getTitle());
        tabLayout.addTab(tabLayout.newTab().setText(moveStoreListFragment.getTitle()));
        adapter.addFragment(moveStoreListFragment, moveStoreListFragment.getTitle());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void getScanResult(String result) {

    }
}
