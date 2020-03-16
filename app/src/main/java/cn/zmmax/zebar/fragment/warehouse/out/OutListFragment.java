package cn.zmmax.zebar.fragment.warehouse.out;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseLabelFragment;
import cn.zmmax.zebar.bean.biz.LogiStoreOutD;


public class OutListFragment extends BaseLabelFragment {

    @BindView(R.id.out_no)
    EditText outNo;
    @BindView(R.id.out_list)
    RecyclerView outList;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    OutListAdapter outListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_out_list;
    }

    @Override
    protected void initViews() {
        outListAdapter = new OutListAdapter(R.layout.fragment_receipt_item);
        outList.setAdapter(outListAdapter);
        statusLayout.showEmpty();
    }

    @Override
    public String getTitle() {
        return "出货列表";
    }


    class OutListAdapter extends BaseQuickAdapter<LogiStoreOutD, BaseViewHolder> {

        public OutListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, LogiStoreOutD item) {

        }
    }
}
