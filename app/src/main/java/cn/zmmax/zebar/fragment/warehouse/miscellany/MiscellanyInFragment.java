package cn.zmmax.zebar.fragment.warehouse.miscellany;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseBusinessFragment;
import cn.zmmax.zebar.bean.biz.LogiStoreMicellanyD;

@Page(name = "杂收", anim = CoreAnim.fade, category = PageCategory.warehouse, extra = R.drawable.icon_entry)
public class MiscellanyInFragment extends BaseBusinessFragment {

    @BindView(R.id.miscellany_code)
    EditText miscellanyCode;
    @BindView(R.id.location_code)
    EditText locationCode;
    @BindView(R.id.miscellany_in_list)
    RecyclerView miscellanyInList;
    @BindView(R.id.btn_cancel)
    SuperButton btnCancel;
    @BindView(R.id.btn_submit)
    SuperButton btnSubmit;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    MiscellanyInAdapter miscellanyInAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_miscellany_in;
    }

    @Override
    protected void initViews() {
        miscellanyInAdapter = new MiscellanyInAdapter(R.layout.fragment_receipt_item);
        miscellanyInList.setAdapter(miscellanyInAdapter);
        statusLayout.showEmpty();
    }

    @Override
    public void getScanResult(String result) {

    }

    class MiscellanyInAdapter extends BaseQuickAdapter<LogiStoreMicellanyD, BaseViewHolder> {

        MiscellanyInAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, LogiStoreMicellanyD item) {

        }
    }
}
