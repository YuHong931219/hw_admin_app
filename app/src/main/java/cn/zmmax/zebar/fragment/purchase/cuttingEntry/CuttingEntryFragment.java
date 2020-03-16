package cn.zmmax.zebar.fragment.purchase.cuttingEntry;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.request.CustomRequest;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xutil.common.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseBusinessFragment;
import cn.zmmax.zebar.base.BaseFragment;
import cn.zmmax.zebar.base.BaseRequest;
import cn.zmmax.zebar.bean.pro.CuttingEntryRequest;
import cn.zmmax.zebar.bean.pro.ProWorkMaterialResponse;
import cn.zmmax.zebar.http.api.ApiResponse;
import cn.zmmax.zebar.http.api.ApiServer;
import cn.zmmax.zebar.utils.QRCodeUtils;
import cn.zmmax.zebar.utils.ZmxUtil;

import static cn.zmmax.zebar.utils.UIUtils.showErrorDialog;
import static cn.zmmax.zebar.utils.UIUtils.showSuccessDialog;
import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

@Page(name = "裁切入库", anim = CoreAnim.fade, category = PageCategory.purchase, extra = R.drawable.icon_entry, sort = 4)
public class CuttingEntryFragment extends BaseBusinessFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private CuttingEntryDetailFragment cuttingEntryDetailFragment;
    private CuttingEntryListFragment cuttingEntryListFragment;
    private ProWorkMaterialResponse proWorkMaterialResponse;
    private List<String> workList = new ArrayList<>();
    private List<String> locationList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    public void getScanResult(String result) {
        try {
            QRCodeUtils qrCodeUtils = new QRCodeUtils(result);
            if (QRCodeUtils.TYPE_1.equals(qrCodeUtils.getContentByPosition(0))) {
                // 料号 + 批号 + 数量 + 供应商编号 + 箱号
                String materialCode = qrCodeUtils.getContentByPosition(1);
                String supplierBatchNo = qrCodeUtils.getContentByPosition(2);
                String amount = qrCodeUtils.getContentByPosition(3);
                String supplierCode = qrCodeUtils.getContentByPosition(4);
                String caseNo = qrCodeUtils.getContentByPosition(5);
                cuttingEntryDetailFragment.batchNo.setText(StringUtils.concat(supplierCode + "-" + caseNo));
                cuttingEntryDetailFragment.batchNo.setTag(supplierBatchNo);
                cuttingEntryDetailFragment.needAmount.setText(amount);
                Map<String,Object> map = new HashMap<>();
                map.put("materialCode", materialCode);
                map.put("caseNo", caseNo);
                getScanData(map);
            } else if (QRCodeUtils.TYPE_2.equals(qrCodeUtils.getContentByPosition(0))) {
                // 料号 + 批号 + 数量 + 供应商编号 + 箱号 + 订单号-订单项次
                String materialCode = qrCodeUtils.getContentByPosition(1);
                String supplierBatchNo = qrCodeUtils.getContentByPosition(2);
                String amount = qrCodeUtils.getContentByPosition(3);
                String supplierCode = qrCodeUtils.getContentByPosition(4);
                String caseNo = qrCodeUtils.getContentByPosition(5);
                cuttingEntryDetailFragment.batchNo.setText(StringUtils.concat(supplierCode + "-" + caseNo));
                cuttingEntryDetailFragment.batchNo.setTag(supplierBatchNo);
                cuttingEntryDetailFragment.needAmount.setText(amount);
                Map<String,Object> map = new HashMap<>();
                map.put("materialCode", materialCode);
                map.put("caseNo", caseNo);
                getScanData(map);
            }
        } catch (Exception e) {
            showErrorDialog(getContext(), "条码解析错误：" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void getScanData(Map<String,Object> map) {
        CustomRequest request = XHttp.custom();
        ProgressLoadingSubscriber<ApiResponse> tipRequestSubscriber = request.call(request.create(ApiServer.class)
                .postHttp("/app/cutting/selectWorkMaterial", new BaseRequest<>(map)))
                .subscribeWith(new ProgressLoadingSubscriber<ApiResponse>(iProgressLoader, true, true) {

                    @Override
                    protected void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            CuttingEntryRequest cuttingEntryRequest = (CuttingEntryRequest) ZmxUtil.jsonToJavaBean(apiResponse.getBody(), CuttingEntryRequest.class);
                            cuttingEntryDetailFragment.materialCode.setText(cuttingEntryRequest.getMaterialCode());
                            cuttingEntryDetailFragment.materialName.setText(cuttingEntryRequest.getMaterialName());
                            cuttingEntryDetailFragment.spec.setText(cuttingEntryRequest.getSpec());
                            cuttingEntryDetailFragment.unit.setText(cuttingEntryRequest.getUnit());
                            locationList.addAll(cuttingEntryRequest.getLocationList());
                            if (locationList.size() == 1) {
                                cuttingEntryDetailFragment.locationCode.setText(locationList.get(0));
                            }
                            List<ProWorkMaterialResponse> proWorkMaterialResponseList = cuttingEntryRequest.getWorkList();
                            if (proWorkMaterialResponseList.size() == 1) {
                                ProWorkMaterialResponse proWorkMaterialResponse = proWorkMaterialResponseList.get(0);
                                cuttingEntryDetailFragment.materialCode.setText(proWorkMaterialResponse.getMaterialCode());
                                cuttingEntryDetailFragment.materialName.setText(proWorkMaterialResponse.getMaterialName());
                                cuttingEntryDetailFragment.spec.setText(proWorkMaterialResponse.getSpec());
                                cuttingEntryDetailFragment.unit.setText(proWorkMaterialResponse.getUnit());
                                cuttingEntryDetailFragment.workCode.setText(proWorkMaterialResponse.getWorkCode());
                                cuttingEntryDetailFragment.pieceWidth.setText(proWorkMaterialResponse.getPieceWidth());
                                cuttingEntryDetailFragment.pieceAmount.setText(proWorkMaterialResponse.getPieceAmount());
                            } else if (proWorkMaterialResponseList.size() > 1) {
                                for (int i = 0; i < proWorkMaterialResponseList.size(); i++) {
                                    workList.add(proWorkMaterialResponseList.get(i).getWorkCode() + "#" + proWorkMaterialResponseList.get(i).getPieceWidth());
                                }
                                int i = 0;
                                new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                                        .title("选择工单")
                                        .items(workList)
                                        .cancelable(false)
                                        .itemsCallbackSingleChoice(i, (dialog, itemView, which, text) -> {
                                            String workList = CuttingEntryFragment.this.workList.get(which);
                                            String[] split = workList.split("#");
                                            cuttingEntryDetailFragment.workCode.setText(split[0]);
                                            cuttingEntryDetailFragment.pieceWidth.setText(split[1]);
                                            return true;
                                        }).show();
                            }
                        } else {
                            showErrorDialog(getContext(), apiResponse.getMsg());
                        }
                    }
                });
    }

    void submitData() {
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("提示")
                .iconRes(R.drawable.icon_warning)
                .content("您确认提交数据么？")
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    CustomRequest request = XHttp.custom();
                    ProgressLoadingSubscriber<ApiResponse> tipRequestSubscriber = request.call(request.create(ApiServer.class)
                            .postHttp("/app/cutting/submitWorkMaterial", new BaseRequest<>(cuttingEntryListFragment.myAdapter.getData())))
                            .subscribeWith(new ProgressLoadingSubscriber<ApiResponse>(iProgressLoader, true, true) {

                                @Override
                                protected void onSuccess(ApiResponse apiResponse) {
                                    if (apiResponse.isSuccess()) {
                                        showSuccessDialog(getContext(), "提交成功");
                                        cuttingEntryListFragment.myAdapter.setNewData(new ArrayList<>());
                                        viewPager.setCurrentItem(0);
                                        cuttingEntryDetailFragment.clear();
                                    } else {
                                        showErrorDialog(getContext(), apiResponse.getMsg());
                                    }
                                }
                            });
                })
                .negativeText("取消")
                .onNegative((dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    protected void initViews() {
        cuttingEntryDetailFragment = new CuttingEntryDetailFragment();
        cuttingEntryListFragment = new CuttingEntryListFragment();
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(cuttingEntryDetailFragment.getTitle()));
        adapter.addFragment(cuttingEntryDetailFragment, cuttingEntryDetailFragment.getTitle());
        tabLayout.addTab(tabLayout.newTab().setText(cuttingEntryListFragment.getTitle()));
        adapter.addFragment(cuttingEntryListFragment, cuttingEntryListFragment.getTitle());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        proWorkMaterialResponse = new ProWorkMaterialResponse();
    }

    void chooseLocationList() {
        if (locationList == null || locationList.size() == 1) {
            return;
        }
        int i = 0;
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("选择库位")
                .items(locationList)
                .cancelable(false)
                .itemsCallbackSingleChoice(i, (dialog, itemView, which, text) -> {
                    cuttingEntryDetailFragment.locationCode.setText(locationList.get(which));
                    return true;
                }).show();
    }

    void chooseWorkList() {
        if (workList == null || workList.size() == 1) {
            return;
        }
        int i = 0;
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("选择工单")
                .items(workList)
                .itemsCallbackSingleChoice(i, (dialog, itemView, which, text) -> {
                    String workList = CuttingEntryFragment.this.workList.get(which);
                    String[] split = workList.split("#");
                    cuttingEntryDetailFragment.workCode.setText(split[0]);
                    cuttingEntryDetailFragment.pieceWidth.setText(split[1]);
                    return true;
                }).show();
    }

    void sure() {
        if (TextUtils.isEmpty(cuttingEntryDetailFragment.volume.getText().toString())) {
            showErrorDialog(getContext(), "请填写本卷长度");
            return;
        }
        if (proWorkMaterialResponse == null) {
            return;
        }
        if (TextUtils.isEmpty(cuttingEntryDetailFragment.workCode.getText().toString())) {
            showErrorDialog(getContext(),"工单不能为空");
            return;
        }
        if (TextUtils.isEmpty(cuttingEntryDetailFragment.locationCode.getText().toString())) {
            showErrorDialog(getContext(),"库位不能为空");
            return;
        }
        if (proWorkMaterialResponse.getMaps() == null) {
            proWorkMaterialResponse.setMaps(new ArrayList<>());
        }
        if (proWorkMaterialResponse.getMaps().size() != 4) {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_cutting_piece, null);
            EditText pieceWidthOne = dialogView.findViewById(R.id.piece_width_one);
            EditText pieceAmountOne = dialogView.findViewById(R.id.piece_amount_one);
            pieceAmountOne.setText(cuttingEntryDetailFragment.pieceAmount.getText().toString());
            pieceWidthOne.setText(cuttingEntryDetailFragment.pieceWidth.getText().toString());
            EditText pieceWidthTwo = dialogView.findViewById(R.id.piece_width_two);
            EditText pieceAmountTwo = dialogView.findViewById(R.id.piece_amount_two);
            EditText pieceWidthThree = dialogView.findViewById(R.id.piece_width_three);
            EditText pieceAmountThree = dialogView.findViewById(R.id.piece_amount_three);
            EditText pieceWidthFour = dialogView.findViewById(R.id.piece_width_four);
            EditText pieceAmountFour = dialogView.findViewById(R.id.piece_amount_four);
            new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                    .title("请输入片宽和片数")
                    .customView(dialogView, false)
                    .positiveText("确定")
                    .onPositive((dialog, which) -> {
                        if (TextUtils.isEmpty(pieceAmountOne.getText().toString())) {
                            pieceAmountOne.setError("片数不能为空");
                            return;
                        }
                        if (!TextUtils.isEmpty(pieceWidthTwo.getText().toString()) && TextUtils.isEmpty(pieceAmountTwo.getText().toString())) {
                            pieceAmountTwo.setError("片数不能为空");
                            return;
                        }
                        if (!TextUtils.isEmpty(pieceWidthThree.getText().toString()) && TextUtils.isEmpty(pieceAmountThree.getText().toString())) {
                            pieceAmountThree.setError("片数不能为空");
                            return;
                        }
                        if (!TextUtils.isEmpty(pieceWidthFour.getText().toString()) && TextUtils.isEmpty(pieceAmountFour.getText().toString())) {
                            pieceAmountFour.setError("片数不能为空");
                            return;
                        }
                        Map<String,Object> map = new HashMap<>();
                        map.put("pieceWidth", pieceAmountOne.getText().toString());
                        map.put("pieceAmount", pieceWidthOne.getText().toString());
                        proWorkMaterialResponse.getMaps().add(map);
                        Map<String,Object> twoMap = new HashMap<>();
                        twoMap.put("pieceWidth", pieceWidthTwo.getText().toString());
                        twoMap.put("pieceAmount", pieceAmountTwo.getText().toString());
                        proWorkMaterialResponse.getMaps().add(twoMap);
                        Map<String,Object> threeMap = new HashMap<>();
                        threeMap.put("pieceWidth", pieceWidthThree.getText().toString());
                        threeMap.put("pieceAmount", pieceAmountThree.getText().toString());
                        proWorkMaterialResponse.getMaps().add(threeMap);
                        Map<String,Object> fourMap = new HashMap<>();
                        fourMap.put("pieceWidth", pieceWidthFour.getText().toString());
                        fourMap.put("pieceAmount", pieceAmountFour.getText().toString());
                        proWorkMaterialResponse.getMaps().add(fourMap);
                        viewPager.setCurrentItem(1);
                        proWorkMaterialResponse.setMaterialCode(cuttingEntryDetailFragment.materialCode.getText().toString());
                        proWorkMaterialResponse.setMaterialName(cuttingEntryDetailFragment.materialName.getText().toString());
                        proWorkMaterialResponse.setSpec(cuttingEntryDetailFragment.spec.getText().toString());
                        proWorkMaterialResponse.setUnit(cuttingEntryDetailFragment.unit.getText().toString());
                        proWorkMaterialResponse.setBatchNo(cuttingEntryDetailFragment.batchNo.getText().toString());
                        proWorkMaterialResponse.setWorkCode(cuttingEntryDetailFragment.workCode.getText().toString());
                        proWorkMaterialResponse.setLocationCode(cuttingEntryDetailFragment.locationCode.getText().toString());
                        proWorkMaterialResponse.setCuttingLength(cuttingEntryDetailFragment.volume.getText().toString());
                        proWorkMaterialResponse.setNeedAmount(new BigDecimal(cuttingEntryDetailFragment.needAmount.getText().toString()));
                        proWorkMaterialResponse.setSupplierBatchNo((String) cuttingEntryDetailFragment.batchNo.getTag());
                        cuttingEntryListFragment.myAdapter.addData(proWorkMaterialResponse);
                        cuttingEntryListFragment.statusLayout.showContent();
                        cuttingEntryDetailFragment.clear();
                        dialog.dismiss();
                        proWorkMaterialResponse = new ProWorkMaterialResponse();
                        workList.clear();
                        locationList.clear();
                    })
                    .negativeText("取消")
                    .onNegative((dialog, which) -> dialog.dismiss()).show();
        } else {
            proWorkMaterialResponse.setMaterialCode(cuttingEntryDetailFragment.materialCode.getText().toString());
            proWorkMaterialResponse.setMaterialName(cuttingEntryDetailFragment.materialName.getText().toString());
            proWorkMaterialResponse.setSpec(cuttingEntryDetailFragment.spec.getText().toString());
            proWorkMaterialResponse.setUnit(cuttingEntryDetailFragment.unit.getText().toString());
            proWorkMaterialResponse.setBatchNo(cuttingEntryDetailFragment.batchNo.getText().toString());
            proWorkMaterialResponse.setWorkCode(cuttingEntryDetailFragment.workCode.getText().toString());
            proWorkMaterialResponse.setLocationCode(cuttingEntryDetailFragment.locationCode.getText().toString());
            proWorkMaterialResponse.setCuttingLength(cuttingEntryDetailFragment.volume.getText().toString());
            proWorkMaterialResponse.setNeedAmount(new BigDecimal(cuttingEntryDetailFragment.needAmount.getText().toString()));
            proWorkMaterialResponse.setSupplierBatchNo((String) cuttingEntryDetailFragment.batchNo.getTag());
            cuttingEntryListFragment.myAdapter.addData(proWorkMaterialResponse);
            cuttingEntryListFragment.statusLayout.showContent();
            cuttingEntryDetailFragment.clear();
            proWorkMaterialResponse = new ProWorkMaterialResponse();
            locationList.clear();
        }
    }
}
