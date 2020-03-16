package cn.zmmax.zebar.fragment.purchase.warehouseBack;

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

public class WarehouseBackListFragment extends BaseLabelFragment {

    @BindView(R.id.dn_no)
    EditText dnNo;
    @BindView(R.id.receipt_list)
    RecyclerView receiptList;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    WarehouseBackListAdapter warehouseBackListAdapter;

    @Override
    public String getTitle() {
        return "仓退列表";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_warehouse_back_list;
    }

    @Override
    protected void initViews() {
        warehouseBackListAdapter = new WarehouseBackListAdapter(R.layout.fragment_warehouse_back_list_item);
        receiptList.setAdapter(warehouseBackListAdapter);
        statusLayout.showEmpty();
    }

    class WarehouseBackListAdapter extends BaseQuickAdapter<LogiMaterialReturnD, BaseViewHolder> {

        WarehouseBackListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, LogiMaterialReturnD item) {
            helper.setText(R.id.material_code, item.getMaterialCode());
            helper.setText(R.id.material_name, item.getMaterialName());
            helper.setText(R.id.spec, item.getSpec());
            helper.setText(R.id.batch_no, item.getBatchNo());
            helper.setText(R.id.need_amount, String.valueOf(item.getAmount()));
            helper.setText(R.id.send_amount, String.valueOf(item.getActualAmount()));
            helper.setText(R.id.location_code, item.getLocationCode());
        }
    }
}
