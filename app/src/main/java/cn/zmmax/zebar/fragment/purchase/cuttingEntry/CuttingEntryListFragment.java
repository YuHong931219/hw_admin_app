package cn.zmmax.zebar.fragment.purchase.cuttingEntry;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;
import com.xuexiang.xutil.common.ClickUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseLabelFragment;
import cn.zmmax.zebar.bean.pro.ProWorkMaterialResponse;

public class CuttingEntryListFragment extends BaseLabelFragment {

    @BindView(R.id.entry_list)
    RecyclerView entryList;
    @BindView(R.id.status_layout)
    StatefulLayout statusLayout;
    MyAdapter myAdapter;

    private XUIListPopup xuiListPopup;

    @Override
    public String getTitle() {
        return "数据汇总";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cutting_entry_list;
    }

    @Override
    protected void initViews() {
        statusLayout.showEmpty();
        myAdapter = new MyAdapter(R.layout.fragment_cutting_entry_list_item);
        entryList.setAdapter(myAdapter);
        myAdapter.setOnItemChildClickListener((adapter, childView, position) -> {
            if (childView.getId() == R.id.card_entry) {
                ProWorkMaterialResponse proWorkMaterialResponse = myAdapter.getData().get(position);
                String[] strArray = new String[proWorkMaterialResponse.getMaps().size()];
                for (int i = 0; i < proWorkMaterialResponse.getMaps().size(); i++) {
                    strArray[i] = "片宽:" + proWorkMaterialResponse.getMaps().get(i).get("pieceWidth") + "       " +
                            "片数:" + proWorkMaterialResponse.getMaps().get(i).get("pieceAmount");
                }
                XUISimpleAdapter xuiSimpleAdapter = XUISimpleAdapter.create(getContext(), strArray);
                xuiListPopup = new XUIListPopup(Objects.requireNonNull(getContext()), xuiSimpleAdapter);
                xuiListPopup.create(DensityUtils.dp2px(200), DensityUtils.dp2px(150), (adapterView, view, i, l) -> {
                });
                xuiListPopup.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
                xuiListPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
                xuiListPopup.show(childView);
            }
        });
    }

    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                ClickUtils.exitBy2Click();
                break;
            case R.id.btn_submit:
                ((CuttingEntryFragment) Objects.requireNonNull(getParentFragment())).submitData();
                break;
        }
    }

    class MyAdapter extends BaseQuickAdapter<ProWorkMaterialResponse, BaseViewHolder> {

        MyAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ProWorkMaterialResponse item) {
            helper.setText(R.id.material_code, item.getMaterialCode());
            helper.setText(R.id.material_name, item.getMaterialName());
            helper.setText(R.id.spec, item.getSpec());
            helper.setText(R.id.unit, item.getUnit());
            helper.setText(R.id.work_code, item.getWorkCode());
//            helper.setText(R.id.location_code, item.getLocationCode());
//            helper.setText(R.id.need_amount, String.valueOf(item.getNeedAmount()));
            helper.setText(R.id.volume, item.getCuttingLength());
            helper.addOnClickListener(R.id.card_entry);
        }
    }
}
