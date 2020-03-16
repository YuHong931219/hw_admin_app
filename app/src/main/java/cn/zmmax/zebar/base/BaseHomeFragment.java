package cn.zmmax.zebar.base;

import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.adapter.recyclerview.GridDividerItemDecoration;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.common.ClickUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.activity.MainActivity;
import cn.zmmax.zebar.adapter.BaseRecyclerAdapter;
import cn.zmmax.zebar.adapter.WidgetItemAdapter;
import cn.zmmax.zebar.fragment.other.AboutFragment;

/**
 * 基础菜单
 */
public abstract class BaseHomeFragment extends BaseFragment implements BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private WidgetItemAdapter mWidgetItemAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_container;
    }

    @Override
    protected void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        int spanCount = 3;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(Objects.requireNonNull(getContext()), spanCount));
        mWidgetItemAdapter = new WidgetItemAdapter(sortPageInfo(getPageContents()));
        mWidgetItemAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mWidgetItemAdapter);
    }

    protected abstract List<PageInfo> getPageContents();

    public abstract int getMenuIcon();

    @Override
    @SingleClick
    public void onItemClick(View itemView, int pos) {
        PageInfo widgetInfo = mWidgetItemAdapter.getItem(pos);
        if (widgetInfo != null) {
            openNewPage(widgetInfo.getName());
        }
    }

    private List<PageInfo> sortPageInfo(List<PageInfo> pageInfoList) {
        Collections.sort(pageInfoList, (o1, o2) -> o1.getSort().compareTo(o2.getSort()));
        return pageInfoList;
    }


    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.removeAllActions();
        titleBar.setLeftImageResource(R.drawable.ic_action_menu);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            @SingleClick
            public void onClick(View v) {
                getContainer().openMenu();
            }
        });
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_about) {
            @Override
            @SingleClick
            public void performAction(View view) {
                openNewPage(AboutFragment.class);
            }
        });
        return titleBar;
    }


    private MainActivity getContainer() {
        return (MainActivity) getActivity();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getContainer().isMenuOpen()) {
                getContainer().closeMenu();
            } else {
                ClickUtils.exitBy2Click();
            }
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        //屏幕旋转时刷新一下title
        super.onConfigurationChanged(newConfig);
        ViewGroup root = (ViewGroup) getRootView();
        if (root.getChildAt(0) instanceof TitleBar) {
            root.removeViewAt(0);
            initTitleBar();
        }
    }
}
