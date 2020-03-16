package cn.zmmax.zebar.fragment.warehouse.allocation;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseLabelFragment;
import cn.zmmax.zebar.bean.purchase.LogiMaterialReturnD;


public class AllocationListFragment extends BaseLabelFragment {


    @BindView(R.id.allocation_list)
    RecyclerView allocationList;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    AllocationListAdapter allocationListAdapter;
    @BindView(R.id.allocation_no)
    EditText allocationNo;

    @Override
    public String getTitle() {
        return "调拨列表";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_allocation_list;
    }

    @Override
    protected void initViews() {
        allocationListAdapter = new AllocationListAdapter(R.layout.fragment_receipt_item);
        allocationList.setAdapter(allocationListAdapter);
        statusLayout.showEmpty();
    }

    class AllocationListAdapter extends BaseQuickAdapter<LogiMaterialReturnD, BaseViewHolder> {

        AllocationListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, LogiMaterialReturnD item) {

        }
    }
}
