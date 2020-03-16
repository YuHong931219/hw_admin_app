package cn.zmmax.zebar.fragment.warehouse.moveStore;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.widget.statelayout.StatefulLayout;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseLabelFragment;


public class MoveStoreListFragment extends BaseLabelFragment {

    @BindView(R.id.move_store_list)
    RecyclerView moveStoreList;
    @BindView(R.id.btn_cancel)
    SuperButton btnCancel;
    @BindView(R.id.btn_submit)
    SuperButton btnSubmit;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;

    @Override
    public String getTitle() {
        return "移库汇总";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_move_store_list;
    }

    @Override
    protected void initViews() {
        statusLayout.showEmpty();
    }
}
