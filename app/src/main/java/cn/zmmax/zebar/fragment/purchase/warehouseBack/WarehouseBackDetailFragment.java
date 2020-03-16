package cn.zmmax.zebar.fragment.purchase.warehouseBack;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;
import com.xuexiang.xui.widget.textview.autofit.AutoFitTextView;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseLabelFragment;
import cn.zmmax.zebar.bean.biz.LogiStoreActual;

public class WarehouseBackDetailFragment extends BaseLabelFragment {

    @BindView(R.id.material_code)
    AutoFitTextView materialCode;
    @BindView(R.id.material_name)
    AutoFitTextView materialName;
    @BindView(R.id.spec)
    AutoFitTextView spec;
    @BindView(R.id.unit)
    AutoFitTextView unit;
    @BindView(R.id.location_code)
    AutoFitTextView locationCode;
    @BindView(R.id.need_amount)
    AutoFitTextView needAmount;
    @BindView(R.id.send_amount)
    AutoFitTextView sendAmount;
    @BindView(R.id.warehouse_back_detail_list)
    RecyclerView warehouseBackDetailList;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    WarehouseBackDetailAdapter warehouseBackDetailAdapter;

    @Override
    public String getTitle() {
        return "仓退详情";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_warehouse_back_detail;
    }

    @Override
    protected void initViews() {
        warehouseBackDetailAdapter = new WarehouseBackDetailAdapter(R.layout.fragment_receipt_item);
        warehouseBackDetailList.setAdapter(warehouseBackDetailAdapter);
        statusLayout.showEmpty();
    }

    class WarehouseBackDetailAdapter extends BaseQuickAdapter<LogiStoreActual, BaseViewHolder> {

        WarehouseBackDetailAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, LogiStoreActual item) {

        }
    }
}
