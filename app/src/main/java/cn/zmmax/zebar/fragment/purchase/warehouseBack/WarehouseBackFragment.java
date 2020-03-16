package cn.zmmax.zebar.fragment.purchase.warehouseBack;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.request.CustomRequest;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.enums.PageCategory;
import com.xuexiang.xui.adapter.FragmentAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseBusinessFragment;
import cn.zmmax.zebar.base.BaseFragment;
import cn.zmmax.zebar.base.BaseRequest;
import cn.zmmax.zebar.bean.purchase.LogiMaterialReturnD;
import cn.zmmax.zebar.http.api.ApiResponse;
import cn.zmmax.zebar.http.api.ApiServer;
import cn.zmmax.zebar.utils.QRCodeUtils;

import static cn.zmmax.zebar.utils.UIUtils.showErrorDialog;
import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

@Page(name = "仓退", anim = CoreAnim.fade, category = PageCategory.purchase, extra = R.drawable.icon_come_back, sort = 3)
public class WarehouseBackFragment extends BaseBusinessFragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private WarehouseBackListFragment warehouseBackListFragment;
    private WarehouseBackDetailFragment warehouseBackDetailFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_page;
    }

    @Override
    protected void initViews() {
        warehouseBackListFragment = new WarehouseBackListFragment();
        warehouseBackDetailFragment = new WarehouseBackDetailFragment();
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getChildFragmentManager());
        tabLayout.setTabMode(MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(warehouseBackListFragment.getTitle()));
        adapter.addFragment(warehouseBackListFragment, warehouseBackListFragment.getTitle());
        tabLayout.addTab(tabLayout.newTab().setText(warehouseBackDetailFragment.getTitle()));
        adapter.addFragment(warehouseBackDetailFragment, warehouseBackDetailFragment.getTitle());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void getScanResult(String result) {
        QRCodeUtils qrCodeUtils = new QRCodeUtils(result);
        if (QRCodeUtils.TYPE_13.equals(qrCodeUtils.getContentByPosition(0))) {
            Map<String, Object> map = new HashMap<>();
            map.put("docCode", qrCodeUtils.getContentByPosition(1));
            getWarehouseBackData(map);
        } else if ("1".equals(qrCodeUtils.getContentByPosition(0))) {

        } else if ("2".equals(qrCodeUtils.getContentByPosition(0))) {
            warehouseBackDetailFragment.locationCode.setText(qrCodeUtils.getContentByPosition(1));
        }
    }

    @SuppressWarnings("unchecked")
    private void getWarehouseBackData(Map<String,Object> map) {
        CustomRequest request = XHttp.custom();
        ProgressLoadingSubscriber<ApiResponse> tipRequestSubscriber = request.call(request.create(ApiServer.class)
                .postHttp("/app/warehouseBack/queryWarehouseBack", new BaseRequest<>(map)))
                .subscribeWith(new ProgressLoadingSubscriber<ApiResponse>(iProgressLoader, true, true) {

                    @Override
                    protected void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            List<LogiMaterialReturnD> materialReturnDList = (List<LogiMaterialReturnD>) apiResponse.getBody();
                            warehouseBackListFragment.warehouseBackListAdapter.setNewData(materialReturnDList);
                            warehouseBackListFragment.statusLayout.showContent();
                        } else {
                            showErrorDialog(getContext(), apiResponse.getMsg());
                        }
                    }
                });
    }
}
