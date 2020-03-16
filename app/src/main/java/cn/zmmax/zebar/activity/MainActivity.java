package cn.zmmax.zebar.activity;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.ClickUtils;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseActivity;
import cn.zmmax.zebar.base.BaseHomeFragment;
import cn.zmmax.zebar.fragment.other.AboutFragment;
import cn.zmmax.zebar.fragment.other.EChartsFragment;
import cn.zmmax.zebar.fragment.other.PurchaseFragment;
import cn.zmmax.zebar.fragment.other.ReportFragment;
import cn.zmmax.zebar.fragment.other.UserFragment;
import cn.zmmax.zebar.fragment.other.WarehouseFragment;
import cn.zmmax.zebar.fragment.other.WorkFragment;
import cn.zmmax.zebar.menu.DrawerAdapter;
import cn.zmmax.zebar.menu.DrawerItem;
import cn.zmmax.zebar.menu.SimpleItem;
import cn.zmmax.zebar.menu.SpaceItem;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

/**
 * 主页盒子
 */
public class MainActivity extends BaseActivity implements DrawerAdapter.OnItemSelectedListener {

    private SlidingRootNav mSlidingRootNav;
    private String[] mMenuTitles;
    private Drawable[] mMenuIcons;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initSlidingMenu(savedInstanceState);
        initViews();
    }

    private void initViews() {
        openPage(PurchaseFragment.class);
        initTab();
    }

    private void initTab() {
        TabLayout.Tab purchase = tabLayout.newTab();
        purchase.setText("原料管理");
        purchase.setIcon(R.drawable.select_icon_tabbar_purchase);
        tabLayout.addTab(purchase);
        TabLayout.Tab warehouse = tabLayout.newTab();
        warehouse.setText("仓储管理");
        warehouse.setIcon(R.drawable.select_icon_tabbar_warehouse);
        tabLayout.addTab(warehouse);
        TabLayout.Tab worker = tabLayout.newTab();
        worker.setText("生产管理");
        worker.setIcon(R.drawable.select_icon_tabbar_work);
        tabLayout.addTab(worker);
        TabLayout.Tab report = tabLayout.newTab();
        report.setText("报工管理");
        report.setIcon(R.drawable.select_icon_tabbar_report);
        tabLayout.addTab(report);
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        switchPage(PurchaseFragment.class);
                        break;
                    case 1:
                        switchPage(WarehouseFragment.class);
                        break;
                    case 2:
                        switchPage(WorkFragment.class);
                        break;
                    case 3:
                        switchPage(ReportFragment.class);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        FragmentAdapter<BaseHomeFragment> adapter = new FragmentAdapter<>(getSupportFragmentManager());
        PurchaseFragment purchaseFragment = new PurchaseFragment();
        adapter.addFragment(purchaseFragment, purchaseFragment.getPageName());
        WarehouseFragment warehouseFragment = new WarehouseFragment();
        adapter.addFragment(warehouseFragment, warehouseFragment.getPageName());
        WorkFragment workFragment = new WorkFragment();
        adapter.addFragment(workFragment, workFragment.getPageName());
        ReportFragment reportFragment = new ReportFragment();
        adapter.addFragment(reportFragment, reportFragment.getPageName());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        mMenuTitles = loadMenuTitles();
        mMenuIcons = loadMenuIcons();
        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        DrawerAdapter mAdapter = new DrawerAdapter(Arrays.asList(
                createItemFor(0).setChecked(true),
                createItemFor(1),
                createItemFor(2),
                createItemFor(3),
                new SpaceItem(48),
                createItemFor(5)));
        mAdapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(mAdapter);

        mAdapter.setSelected(0);
        mSlidingRootNav.setMenuLocked(false);
        mSlidingRootNav.getLayout().addDragStateListener(new DragStateListener() {
            @Override
            public void onDragStart() {

            }
            @Override
            public void onDragEnd(boolean isMenuOpened) {

            }
        });
    }

    public void openMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.openMenu();
        }
    }

    public void closeMenu() {
        if (mSlidingRootNav != null) {
            mSlidingRootNav.closeMenu();
        }
    }

    public boolean isMenuOpen() {
        if (mSlidingRootNav != null) {
            return mSlidingRootNav.isMenuOpened();
        }
        return false;
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(mMenuIcons[position], mMenuTitles[position])
                .withIconTint(ResUtils.getColor(R.color.gray_icon))
                .withTextTint(ThemeUtils.resolveColor(this, R.attr.xui_config_color_content_text))
                .withSelectedIconTint(ThemeUtils.resolveColor(this, R.attr.colorAccent))
                .withSelectedTextTint(ThemeUtils.resolveColor(this, R.attr.colorAccent));
    }

    private String[] loadMenuTitles() {
        return getResources().getStringArray(R.array.menu_titles);
    }

    private Drawable[] loadMenuIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.menu_icons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemSelected(int position) {
        switch(position) {
            case 0:
                if (tabLayout != null) {
                    TabLayout.Tab tab = tabLayout.getTabAt(position);
                    if (tab != null) {
                        tab.select();
                    }
                }
                mSlidingRootNav.closeMenu();
                break;
            case 1:
                openNewPage(EChartsFragment.class);
                mSlidingRootNav.closeMenu();
                break;
            case 2:
                openNewPage(UserFragment.class);
                mSlidingRootNav.closeMenu();
                break;
            case 3:
                openNewPage(AboutFragment.class);
                mSlidingRootNav.closeMenu();
                break;
            case 5:
                ActivityUtils.startActivity(LoginActivity.class);
                finish();
                break;
            default:
                break;
        }
    }
}