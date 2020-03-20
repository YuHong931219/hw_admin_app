package cn.zmmax.zebar.fragment.purchase.cuttingEntry;

import android.view.View;
import android.widget.EditText;

import com.xuexiang.xui.widget.textview.autofit.AutoFitTextView;
import com.xuexiang.xutil.common.ClickUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseLabelFragment;

public class CuttingEntryDetailFragment extends BaseLabelFragment {

    @BindView(R.id.material_code)
    AutoFitTextView materialCode;
    @BindView(R.id.material_name)
    AutoFitTextView materialName;
    @BindView(R.id.spec)
    AutoFitTextView spec;
    @BindView(R.id.unit)
    AutoFitTextView unit;
    @BindView(R.id.batch_no)
    AutoFitTextView batchNo;
    @BindView(R.id.need_amount)
    AutoFitTextView needAmount;
    @BindView(R.id.work_code)
    EditText workCode;
    @BindView(R.id.location_code)
    EditText locationCode;
    @BindView(R.id.piece_width)
    AutoFitTextView pieceWidth;
    @BindView(R.id.piece_amount)
    AutoFitTextView pieceAmount;
    @BindView(R.id.volume)
    EditText volume;

    @Override
    public String getTitle() {
        return "物料扫描";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cutting_entry_detail;
    }

    @Override
    protected void initViews() {

    }

    void clear() {
        materialCode.setText("");
        materialName.setText("");
        spec.setText("");
        unit.setText("");
        batchNo.setText("");
        needAmount.setText("");
        workCode.setText("");
        pieceWidth.setText("");
        pieceAmount.setText("");
        volume.setText("");
        locationCode.setText("");
    }

    @OnClick({R.id.btn_cancel, R.id.btn_sure, R.id.btn_select_work_code, R.id.btn_select_location})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                ClickUtils.exitBy2Click();
                break;
            case R.id.btn_sure:
//                View linearLayout = getLayoutInflater().inflate(R.layout.print_cutting_entry, null);
//                QRCodeUtils.layoutView(mActivity, linearLayout);
//                AppCompatImageView qrCode = linearLayout.findViewById(R.id.qrCode);
//                Bitmap bitmap = QRCodeUtils.createQRCodeBitmap("1#AA00011221", 100, 100, "UTF-8", ErrorCorrectionLevel.L, Color.BLACK, Color.WHITE);
//                qrCode.setImageBitmap(bitmap);
//                Bitmap loadBitmapFromView = QRCodeUtils.loadBitmapFromView(linearLayout);
//                try {
//                    Printer printer = new Printer(getContext());
//                    printer.printBitmap(50, 0, false, loadBitmapFromView);
//                    printer.startPrint();
//                } catch (Exception e) {
//                    showErrorDialog(getContext(), "打印错误：" + e.getMessage());
//                }
                ((CuttingEntryFragment) Objects.requireNonNull(getParentFragment())).sure();
                break;
            case R.id.btn_select_work_code:
                ((CuttingEntryFragment) Objects.requireNonNull(getParentFragment())).chooseWorkList();
                break;
            case R.id.btn_select_location:
                ((CuttingEntryFragment) Objects.requireNonNull(getParentFragment())).chooseLocationList();
                break;
            default:
                break;
        }
    }
}
