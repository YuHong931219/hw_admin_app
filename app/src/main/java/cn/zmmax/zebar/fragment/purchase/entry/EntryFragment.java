package cn.zmmax.zebar.fragment.purchase.entry;

import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.request.CustomRequest;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseBusinessFragment;
import cn.zmmax.zebar.base.BaseRequest;
import cn.zmmax.zebar.bean.purchase.LogiReceiptNoteD;
import cn.zmmax.zebar.fragment.other.PurchaseFragment;
import cn.zmmax.zebar.http.api.ApiResponse;
import cn.zmmax.zebar.http.api.ApiServer;
import cn.zmmax.zebar.utils.QRCodeUtils;

import static cn.zmmax.zebar.utils.UIUtils.showErrorDialog;
import static cn.zmmax.zebar.utils.UIUtils.showSuccessDialog;

@Page(name = "入库", anim = CoreAnim.fade, category = PageCategory.purchase, extra = R.drawable.icon_entry, sort = 1)
public class EntryFragment extends BaseBusinessFragment {

    @BindView(R.id.dn_no)
    EditText dnNo;
    @BindView(R.id.location_code)
    EditText locationCode;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    @BindView(R.id.receipt_list)
    RecyclerView receiptList;
    private EntryAdapter entryAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_entry;
    }

    @Override
    protected void initViews() {
        entryAdapter = new EntryAdapter(R.layout.fragment_entry_item);
        receiptList.setAdapter(entryAdapter);
        statusLayout.showEmpty();
    }

    @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        openPage(PurchaseFragment.class, false);
    }

    @OnClick(R.id.btn_submit)
    void onClickSubmit() {
        Map<String,Object> map = new HashMap<>();
        map.put("data", entryAdapter.getData());
        CustomRequest request = XHttp.custom();
        ProgressLoadingSubscriber<ApiResponse> tipRequestSubscriber = request.call(request.create(ApiServer.class)
                .postHttp("/app/receipt/submitEntry", new BaseRequest<>(map)))
                .subscribeWith(new ProgressLoadingSubscriber<ApiResponse>(iProgressLoader, true, true) {

                    @Override
                    protected void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            showSuccessDialog(getContext(), "提交成功");
                            entryAdapter.setNewData(new ArrayList<>());
                            dnNo.setText("");
                            locationCode.setText("");
                            statusLayout.showEmpty();
                        } else {
                            showErrorDialog(getContext(), apiResponse.getMsg());
                        }
                    }
                });
    }

    @Override
    public void getScanResult(String result) {
        QRCodeUtils qrCodeUtils = new QRCodeUtils(result);
        if (QRCodeUtils.TYPE_0.equals(qrCodeUtils.getContentByPosition(0))) {
            Map<String, Object> map = new HashMap<>();
            map.put("dnNo", qrCodeUtils.getContentByPosition(1));
            getEntryData(map);
        } else if (QRCodeUtils.TYPE_1.equals(qrCodeUtils.getContentByPosition(0))) {
            Map<String,Object> map = new HashMap<>();
            map.put("materialCode", qrCodeUtils.getContentByPosition(1));
            map.put("batchNo", qrCodeUtils.getContentByPosition(2));
            getEntryData(map);
        } else if (QRCodeUtils.TYPE_2.equals(qrCodeUtils.getContentByPosition(0))) {
            locationCode.setText(qrCodeUtils.getContentByPosition(1));
        }
    }

    @SuppressWarnings("unchecked")
    private void getEntryData(Map<String,Object> map) {
        CustomRequest request = XHttp.custom();
        ProgressLoadingSubscriber<ApiResponse> tipRequestSubscriber = request.call(request.create(ApiServer.class)
                .postHttp("/app/receipt/queryEntry", new BaseRequest<>(map)))
                .subscribeWith(new ProgressLoadingSubscriber<ApiResponse>(iProgressLoader, true, true) {

                    @Override
                    protected void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            List<LogiReceiptNoteD> receiptNoteDS = (List<LogiReceiptNoteD>) apiResponse.getBody();
                            entryAdapter.setNewData(receiptNoteDS);
                            statusLayout.showContent();
                        } else {
                            showErrorDialog(getContext(), apiResponse.getMsg());
                        }
                    }
                });
    }

    class EntryAdapter extends BaseQuickAdapter<LogiReceiptNoteD, BaseViewHolder> {

        EntryAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, LogiReceiptNoteD item) {
            helper.setText(R.id.material_code, item.getMaterialCode());
            helper.setText(R.id.material_name, item.getMaterialName());
            helper.setText(R.id.spec, item.getSpec());
            helper.setText(R.id.batch_no, item.getBatchNo());
            helper.setText(R.id.dn_amount, String.valueOf(item.getAmount()));
            helper.setText(R.id.receive_amount, String.valueOf(item.getAmount()));
            helper.setText(R.id.location_code, item.getLocationCode());
        }
    }
}
