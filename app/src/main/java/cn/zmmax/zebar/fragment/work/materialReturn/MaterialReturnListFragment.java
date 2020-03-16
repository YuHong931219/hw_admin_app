package cn.zmmax.zebar.fragment.work.materialReturn;


import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseLabelFragment;
import cn.zmmax.zebar.bean.pro.ProMaterialRequireN;

public class MaterialReturnListFragment extends BaseLabelFragment {


    @BindView(R.id.material_return_code)
    EditText materialReturnCode;
    @BindView(R.id.material_return_list)
    RecyclerView materialReturnList;
    @BindView(R.id.btn_cancel)
    SuperButton btnCancel;
    @BindView(R.id.btn_submit)
    SuperButton btnSubmit;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    MaterialReturnListAdapter materialReturnListAdapter;

    @Override
    public String getTitle() {
        return "退料列表";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material_return_list;
    }

    @Override
    protected void initViews() {
        materialReturnListAdapter = new MaterialReturnListAdapter(R.layout.fragment_receipt_item);
        materialReturnList.setAdapter(materialReturnListAdapter);
        statusLayout.showEmpty();
    }

    class MaterialReturnListAdapter extends BaseQuickAdapter<ProMaterialRequireN, BaseViewHolder> {

        MaterialReturnListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ProMaterialRequireN item) {

        }
    }
}
