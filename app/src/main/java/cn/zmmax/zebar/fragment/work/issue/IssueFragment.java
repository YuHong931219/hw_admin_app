package cn.zmmax.zebar.fragment.work.issue;

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

@Page(name = "发料", anim = CoreAnim.fade, category = PageCategory.work, extra = R.drawable.icon_issue)
public class IssueFragment extends BaseFragment {


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private IssueListFragment issueListFragment;
    private IssueDetailFragment issueDetailFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    protected void initViews() {
        issueListFragment = new IssueListFragment();
        issueDetailFragment = new IssueDetailFragment();
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(issueListFragment.getTitle()));
        adapter.addFragment(issueListFragment, issueListFragment.getTitle());
        tabLayout.addTab(tabLayout.newTab().setText(issueDetailFragment.getTitle()));
        adapter.addFragment(issueDetailFragment, issueDetailFragment.getTitle());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
