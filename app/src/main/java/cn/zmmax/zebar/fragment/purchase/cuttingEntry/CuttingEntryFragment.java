package cn.zmmax.zebar.fragment.purchase.cuttingEntry;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.request.CustomRequest;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.internal.MDButton;
import com.xuexiang.xutil.common.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseBusinessFragment;
import cn.zmmax.zebar.base.BaseFragment;
import cn.zmmax.zebar.base.BaseRequest;
import cn.zmmax.zebar.bean.biz.LogiStoreActual;
import cn.zmmax.zebar.bean.pro.CuttingEntryRequest;
import cn.zmmax.zebar.bean.pro.ProWorkMaterialResponse;
import cn.zmmax.zebar.http.api.ApiResponse;
import cn.zmmax.zebar.http.api.ApiServer;
import cn.zmmax.zebar.print.Printer;
import cn.zmmax.zebar.utils.QRCodeUtils;
import cn.zmmax.zebar.utils.SettingSPUtils;
import cn.zmmax.zebar.utils.ZmxUtil;

import static cn.zmmax.zebar.utils.UIUtils.showErrorDialog;
import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

@Page(name = "裁切拆箱", anim = CoreAnim.fade, category = PageCategory.purchase, extra = R.drawable.icon_entry, sort = 4)
public class CuttingEntryFragment extends BaseBusinessFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private CuttingEntryDetailFragment cuttingEntryDetailFragment;
    private CuttingEntryListFragment cuttingEntryListFragment;
    private ProWorkMaterialResponse proWorkMaterialResponse;
    private List<ProWorkMaterialResponse> workList = new ArrayList<>();
    private List<LogiStoreActual> storeActualList = new ArrayList<>();
    private List<ProWorkMaterialResponse> proWorkMaterialResponseList = new ArrayList<>();
    private SettingSPUtils instance;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    public void getScanResult(String result) {
        try {
            QRCodeUtils qrCodeUtils = new QRCodeUtils(result);
            String supplierCode = qrCodeUtils.getContentByPosition(4);
            String caseNo = qrCodeUtils.getContentByPosition(5);
            // 对数据汇总的数据判断是否重复
            List<ProWorkMaterialResponse> dataList = cuttingEntryListFragment.myAdapter.getData();
            for (ProWorkMaterialResponse workMaterialResponse : dataList) {
                if(workMaterialResponse.getBatchNo().equals(StringUtils.concat(supplierCode + "-" + caseNo))){
                    showErrorDialog(getContext(), "当前二维码不能重复扫描");
                    return;
                }
            }
            if (QRCodeUtils.TYPE_1.equals(qrCodeUtils.getContentByPosition(0))) {
                // 料号 + 批号 + 数量 + 供应商编号 + 箱号
                String materialCode = qrCodeUtils.getContentByPosition(1);
                String supplierBatchNo = qrCodeUtils.getContentByPosition(2);
                String amount = qrCodeUtils.getContentByPosition(3);
                cuttingEntryDetailFragment.batchNo.setText(StringUtils.concat(supplierCode + "-" + caseNo));
                cuttingEntryDetailFragment.batchNo.setTag(supplierBatchNo);
                Map<String, Object> map = new HashMap<>();
                map.put("materialCode", materialCode);
                map.put("batchNo", cuttingEntryDetailFragment.batchNo.getText().toString());
                map.put("caseNo", caseNo);
                getScanData(map);
            } else if (QRCodeUtils.TYPE_2.equals(qrCodeUtils.getContentByPosition(0))) {
                // 料号 + 批号 + 数量 + 供应商编号 + 箱号 + 订单号-订单项次
                String materialCode = qrCodeUtils.getContentByPosition(1);
                String supplierBatchNo = qrCodeUtils.getContentByPosition(2);
                String amount = qrCodeUtils.getContentByPosition(3);
                cuttingEntryDetailFragment.batchNo.setText(StringUtils.concat(supplierCode + "-" + caseNo));
                cuttingEntryDetailFragment.batchNo.setTag(supplierBatchNo);
                Map<String, Object> map = new HashMap<>();
                map.put("materialCode", materialCode);
                map.put("batchNo", cuttingEntryDetailFragment.batchNo.getText().toString());
                map.put("caseNo", caseNo);
                getScanData(map);
            }
        } catch (Exception e) {
            showErrorDialog(getContext(), "条码解析错误：" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void getScanData(Map<String, Object> map) {
        CustomRequest request = XHttp.custom();
        ProgressLoadingSubscriber<ApiResponse> tipRequestSubscriber = request.call(request.create(ApiServer.class)
                .postHttp("/app/cutting/selectWorkMaterial", new BaseRequest<>(map)))
                .subscribeWith(new ProgressLoadingSubscriber<ApiResponse>(iProgressLoader, true, true) {

                    @Override
                    protected void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            storeActualList.clear();
                            proWorkMaterialResponseList.clear();
                            CuttingEntryRequest cuttingEntryRequest = (CuttingEntryRequest) ZmxUtil.jsonToJavaBean(apiResponse.getBody(), CuttingEntryRequest.class);
                            cuttingEntryDetailFragment.materialCode.setText(cuttingEntryRequest.getMaterialCode());
                            cuttingEntryDetailFragment.materialName.setText(cuttingEntryRequest.getMaterialName());
                            cuttingEntryDetailFragment.spec.setText(cuttingEntryRequest.getSpec());
                            cuttingEntryDetailFragment.unit.setText(cuttingEntryRequest.getUnit());
                            storeActualList.addAll(cuttingEntryRequest.getStoreActualList());
                            proWorkMaterialResponseList = cuttingEntryRequest.getWorkList();
                            if(null != storeActualList && storeActualList.size() == 1){
                                cuttingEntryDetailFragment.locationCode.setText(storeActualList.get(0).getLocationCode());
                                cuttingEntryDetailFragment.needAmount.setText(storeActualList.get(0).getAmount().toString());
                            }
                            if (proWorkMaterialResponseList.size() == 1) {
                                ProWorkMaterialResponse proWorkMaterialResponse = proWorkMaterialResponseList.get(0);
                                cuttingEntryDetailFragment.pieceWidth.setText(proWorkMaterialResponse.getPieceWidth());
                                cuttingEntryDetailFragment.workCode.setText(proWorkMaterialResponse.getWorkCode());
                                cuttingEntryDetailFragment.materialCode.setTag(proWorkMaterialResponse.getReplacedMaterial());
                            } else if (proWorkMaterialResponseList.size() > 1) {
                                workList.addAll(proWorkMaterialResponseList);
                                View dialogView = getLayoutInflater().inflate(R.layout.common_list, null);
                                RecyclerView list = dialogView.findViewById(R.id.list);
                                WorkAdapter workAdapter = new WorkAdapter(R.layout.dialog_select_work);
                                list.setAdapter(workAdapter);
                                workAdapter.setNewData(proWorkMaterialResponseList);
                                workAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                                    if (view.getId() == R.id.checkbox || view.getId() == R.id.card_layout) {
                                        boolean isCheck = workAdapter.getData().get(position).getChecked();
                                        for (ProWorkMaterialResponse proWorkMaterialResponse : workAdapter.getData()) {
                                            proWorkMaterialResponse.setChecked(false);
                                        }
                                        workAdapter.getData().get(position).setChecked(!isCheck);
                                        workAdapter.notifyDataSetChanged();
                                    }
                                });
                                new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                                        .title("选择工单")
                                        .customView(dialogView, false)
                                        .positiveText("确定")
                                        .onPositive((dialog, which) -> {
                                            for (ProWorkMaterialResponse proWorkMaterialResponse : workAdapter.getData()) {
                                                if (proWorkMaterialResponse.getChecked()) {
                                                    cuttingEntryDetailFragment.pieceWidth.setText(proWorkMaterialResponse.getPieceWidth());
                                                    cuttingEntryDetailFragment.workCode.setText(proWorkMaterialResponse.getWorkCode());
                                                    cuttingEntryDetailFragment.materialCode.setTag(proWorkMaterialResponse.getReplacedMaterial());
                                                }
                                            }
                                            dialog.dismiss();
                                        })
                                        .negativeText("取消")
                                        .onNegative((dialog, which) -> dialog.dismiss())
                                        .show();
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
                                        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                                                .title("提示")
                                                .iconRes(R.drawable.icon_success)
                                                .content("提交成功，是否打印数据？")
                                                .positiveText("打印")
                                                .cancelable(false)
                                                .negativeText("取消")
                                                .onPositive((dialog1, i) -> {
                                                    List<ProWorkMaterialResponse> data = cuttingEntryListFragment.myAdapter.getData();
                                                    for (int k = 0; k <  data.size(); k++) {
                                                        ProWorkMaterialResponse workMaterialResponse = data.get(k);
                                                        View linearLayout = getLayoutInflater().inflate(R.layout.print_cutting_entry, null);
                                                        TextView materialCode = linearLayout.findViewById(R.id.material_code);
                                                        TextView spec = linearLayout.findViewById(R.id.spec);
                                                        TextView pieceOne = linearLayout.findViewById(R.id.piece_one);
                                                        TextView pieceTwo = linearLayout.findViewById(R.id.piece_two);
                                                        TextView pieceThree = linearLayout.findViewById(R.id.piece_three);
                                                        TextView pieceFour = linearLayout.findViewById(R.id.piece_four);
                                                        materialCode.setText(StringUtils.concat("料号:" + workMaterialResponse.getMaterialCode()));
                                                        spec.setText(StringUtils.concat("规格:" + workMaterialResponse.getSpec()));
                                                        pieceOne.setText(StringUtils.concat("片宽:" + workMaterialResponse.getMaps().get(0).get("pieceWidth") + "   片数:" + workMaterialResponse.getMaps().get(0).get("pieceAmount")));
                                                        pieceTwo.setText(StringUtils.concat("片宽:" + workMaterialResponse.getMaps().get(1).get("pieceWidth") + "   片数:" + workMaterialResponse.getMaps().get(1).get("pieceAmount")));
                                                        pieceThree.setText(StringUtils.concat("片宽:" + workMaterialResponse.getMaps().get(2).get("pieceWidth") + "   片数:" + workMaterialResponse.getMaps().get(2).get("pieceAmount")));
                                                        pieceFour.setText(StringUtils.concat("片宽:" + workMaterialResponse.getMaps().get(3).get("pieceWidth") + "   片数:" + workMaterialResponse.getMaps().get(3).get("pieceAmount")));
                                                        QRCodeUtils.layoutView(mActivity, linearLayout);
                                                        AppCompatImageView qrCode = linearLayout.findViewById(R.id.qrCode);
                                                        String[] splitArr = apiResponse.getData().toString().split("#");
                                                        String group_code1 = instance.getString("GROUP_CODE", "");
                                                        String arr = splitArr[k];
                                                        String group_code = group_code1 + arr;
                                                        Bitmap bitmap = QRCodeUtils.createQRCodeBitmap(StringUtils.concat("G#" + StringUtils.toString(group_code)), 80, 80, "UTF-8", ErrorCorrectionLevel.L, Color.BLACK, Color.WHITE);
                                                        qrCode.setImageBitmap(bitmap);
                                                        Bitmap loadBitmapFromView = QRCodeUtils.loadBitmapFromView(linearLayout);
                                                        try {
                                                            Printer printer = new Printer(getContext());
                                                            printer.printBitmap(50, 50, false, loadBitmapFromView);
                                                            printer.startPrint();
                                                        } catch (Exception e) {
                                                            showErrorDialog(getContext(), "打印错误：" + e.getMessage());
                                                        }
                                                        dialog1.dismiss();
                                                        cuttingEntryListFragment.myAdapter.setNewData(new ArrayList<>());
                                                        viewPager.setCurrentItem(0);
                                                        cuttingEntryDetailFragment.clear();
                                                    }
                                                })
                                                .onNegative((childDialog, i) -> {
                                                    childDialog.dismiss();
                                                    cuttingEntryListFragment.myAdapter.setNewData(new ArrayList<>());
                                                    viewPager.setCurrentItem(0);
                                                    cuttingEntryDetailFragment.clear();
                                                }).show();
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
        instance = SettingSPUtils.getInstance();
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
        if (storeActualList == null || storeActualList.size() == 0) {
            return;
        }
        View dialogView = getLayoutInflater().inflate(R.layout.common_list, null);
        RecyclerView list = dialogView.findViewById(R.id.list);
        DialogAdapter dialogAdapter = new DialogAdapter(R.layout.dialog_select_actual_store);
        list.setAdapter(dialogAdapter);
        dialogAdapter.setNewData(storeActualList);
        dialogAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.checkbox || view.getId() == R.id.card_layout) {
               boolean isCheck = dialogAdapter.getData().get(position).isChecked();
                for (LogiStoreActual logiStoreActual : dialogAdapter.getData()) {
                    logiStoreActual.setChecked(false);
                }
                dialogAdapter.getData().get(position).setChecked(!isCheck);
                dialogAdapter.notifyDataSetChanged();
            }
        });
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("选择库位")
                .customView(dialogView, false)
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    for (LogiStoreActual logiStoreActual : dialogAdapter.getData()) {
                        if (logiStoreActual.isChecked()) {
                            cuttingEntryDetailFragment.locationCode.setText(logiStoreActual.getLocationCode());
                            cuttingEntryDetailFragment.needAmount.setText(StringUtils.toString(logiStoreActual.getAmount()));
                        }
                    }
                    dialog.dismiss();
                })
                .negativeText("取消")
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    void chooseWorkList() {
        View dialogView = getLayoutInflater().inflate(R.layout.common_list, null);
        RecyclerView list = dialogView.findViewById(R.id.list);
        WorkAdapter workAdapter = new WorkAdapter(R.layout.dialog_select_work);
        list.setAdapter(workAdapter);
        workAdapter.setNewData(workList);
        workAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.checkbox || view.getId() == R.id.card_layout) {
                boolean isCheck = workAdapter.getData().get(position).getChecked();
                for (ProWorkMaterialResponse proWorkMaterialResponse : workAdapter.getData()) {
                    proWorkMaterialResponse.setChecked(false);
                }
                workAdapter.getData().get(position).setChecked(!isCheck);
                workAdapter.notifyDataSetChanged();
            }
        });
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("选择工单")
                .customView(dialogView, false)
                .positiveText("确定")
                .onPositive((dialog, which) -> {
                    for (ProWorkMaterialResponse proWorkMaterialResponse : workAdapter.getData()) {
                        if (proWorkMaterialResponse.getChecked()) {
                            cuttingEntryDetailFragment.workCode.setText(proWorkMaterialResponse.getWorkCode());
                            cuttingEntryDetailFragment.pieceWidth.setText(proWorkMaterialResponse.getPieceWidth());
                            cuttingEntryDetailFragment.materialCode.setTag(proWorkMaterialResponse.getReplacedMaterial());
                        }
                    }
                    dialog.dismiss();
                })
                .negativeText("取消")
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    void sure() {
        if (TextUtils.isEmpty(cuttingEntryDetailFragment.volume.getText().toString())) {
            showErrorDialog(getContext(), "请填写本卷长度");
            return;
        }
        if (proWorkMaterialResponse == null) {
            proWorkMaterialResponse = new ProWorkMaterialResponse();
            return;
        }
       if (TextUtils.isEmpty(cuttingEntryDetailFragment.workCode.getText().toString())) {
            showErrorDialog(getContext(), "工单不能为空");
            return;
        }

        if (TextUtils.isEmpty(cuttingEntryDetailFragment.locationCode.getText().toString())) {
            showErrorDialog(getContext(), "库位不能为空");
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
            MaterialDialog materialDialog = new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                    .title("请输入片宽和片数")
                    .customView(dialogView, false)
                    .positiveText("确定")
                    .onPositive(null)
                    .negativeText("取消")
                    .onNegative((dialog, which) -> dialog.dismiss()).show();
            materialDialog.setOnShowListener(dialog -> {
                MDButton mdButton = materialDialog.getActionButton(DialogAction.POSITIVE);
                mdButton.setOnClickListener(v -> {
                    if (TextUtils.isEmpty(pieceAmountOne.getText().toString())) {
                        pieceAmountOne.setError("片数不能为空");
                        return;
                    }
                    if (!TextUtils.isEmpty(pieceWidthTwo.getText().toString()) && TextUtils.isEmpty(pieceAmountTwo.getText().toString())) {
                        pieceAmountTwo.setError("片数不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(pieceWidthTwo.getText().toString()) && !TextUtils.isEmpty(pieceAmountTwo.getText().toString())) {
                        pieceWidthTwo.setError("片宽不能为空");
                        return;
                    }
                    if (!TextUtils.isEmpty(pieceWidthThree.getText().toString()) && TextUtils.isEmpty(pieceAmountThree.getText().toString())) {
                        pieceAmountThree.setError("片数不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(pieceWidthThree.getText().toString()) && !TextUtils.isEmpty(pieceAmountThree.getText().toString())) {
                        pieceWidthThree.setError("片宽不能为空");
                        return;
                    }
                    if (!TextUtils.isEmpty(pieceWidthFour.getText().toString()) && TextUtils.isEmpty(pieceAmountFour.getText().toString())) {
                        pieceAmountFour.setError("片数不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(pieceWidthFour.getText().toString()) && !TextUtils.isEmpty(pieceAmountFour.getText().toString())) {
                        pieceWidthFour.setError("片宽不能为空");
                        return;
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("pieceWidth", pieceWidthOne.getText().toString());
                    map.put("pieceAmount", pieceAmountOne.getText().toString());
                    proWorkMaterialResponse.getMaps().add(map);
                    Map<String, Object> twoMap = new HashMap<>();
                    twoMap.put("pieceWidth", pieceWidthTwo.getText().toString());
                    twoMap.put("pieceAmount", pieceAmountTwo.getText().toString());
                    proWorkMaterialResponse.getMaps().add(twoMap);
                    Map<String, Object> threeMap = new HashMap<>();
                    threeMap.put("pieceWidth", pieceWidthThree.getText().toString());
                    threeMap.put("pieceAmount", pieceAmountThree.getText().toString());
                    proWorkMaterialResponse.getMaps().add(threeMap);
                    Map<String, Object> fourMap = new HashMap<>();
                    fourMap.put("pieceWidth", pieceWidthFour.getText().toString());
                    fourMap.put("pieceAmount", pieceAmountFour.getText().toString());
                    proWorkMaterialResponse.getMaps().add(fourMap);
                    viewPager.setCurrentItem(1);
                    proWorkMaterialResponse.setReplacedMaterial((String) cuttingEntryDetailFragment.materialCode.getTag());
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
                    storeActualList.clear();
                });
            });
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
            proWorkMaterialResponse.setReplacedMaterial((String) cuttingEntryDetailFragment.materialCode.getTag());
            cuttingEntryListFragment.myAdapter.addData(proWorkMaterialResponse);
            cuttingEntryListFragment.statusLayout.showContent();
            cuttingEntryDetailFragment.clear();
            proWorkMaterialResponse = new ProWorkMaterialResponse();
            storeActualList.clear();
        }
    }

    class DialogAdapter extends BaseQuickAdapter<LogiStoreActual, BaseViewHolder> {

        DialogAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, LogiStoreActual item) {
            helper.setChecked(R.id.checkbox, item.isChecked());
            helper.addOnClickListener(R.id.checkbox, R.id.card_layout);
            helper.setText(R.id.material_code, item.getMaterialCode());
            helper.setText(R.id.batch_no, item.getBatchNo());
            helper.setText(R.id.location_code, item.getLocationCode());
            helper.setText(R.id.amount, StringUtils.toString(item.getAmount()));
        }
    }

    class WorkAdapter extends BaseQuickAdapter<ProWorkMaterialResponse, BaseViewHolder> {

        WorkAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ProWorkMaterialResponse item) {
            helper.setChecked(R.id.checkbox, item.getChecked());
            helper.addOnClickListener(R.id.checkbox, R.id.card_layout);
            helper.setText(R.id.work_code, item.getWorkCode());
            helper.setText(R.id.piece_width, item.getPieceWidth());
            helper.setText(R.id.replace_material, item.getReplacedMaterial());
        }
    }
}
