package cn.zmmax.zebar.fragment.work.issue;

import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;
import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseLabelFragment;
import cn.zmmax.zebar.bean.pro.ProMaterialRequireN;

public class IssueListFragment extends BaseLabelFragment {

    @BindView(R.id.issue_code)
    EditText issueCode;
    @BindView(R.id.issue_list)
    RecyclerView issueList;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    IssueListAdapter issueListAdapter;

    @Override
    public String getTitle() {
        return "发料列表";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_issue_list;
    }

    @Override
    protected void initViews() {
        issueListAdapter = new IssueListAdapter(R.layout.fragment_receipt_item);
        issueList.setAdapter(issueListAdapter);
        statusLayout.showEmpty();
    }

    class IssueListAdapter extends BaseQuickAdapter<ProMaterialRequireN, BaseViewHolder> {

        public IssueListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ProMaterialRequireN item) {

        }
    }
}
