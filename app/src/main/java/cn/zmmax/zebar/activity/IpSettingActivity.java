package cn.zmmax.zebar.activity;


import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.google.gson.Gson;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xrouter.utils.TextUtils;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseActivity;
import cn.zmmax.zebar.http.entity.NetWorkInfo;
import cn.zmmax.zebar.utils.SettingSPUtils;

import static cn.zmmax.zebar.utils.UIUtils.showErrorDialog;


public class IpSettingActivity extends BaseActivity {

    @BindView(R.id.ip_address)
    AppCompatAutoCompleteTextView ipAddress;
    @BindView(R.id.port)
    AppCompatAutoCompleteTextView port;
    @BindView(R.id.select_http_spinner)
    MaterialSpinner selectHttpSpinner;
    private SettingSPUtils instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ip_setting);
        ButterKnife.bind(this);
        initSharedPreference();
        initData();
    }

    private void initSharedPreference() {
        this.instance = SettingSPUtils.getInstance();
    }

    private void initData() {
        Gson gson = new Gson();
        String instanceIPList = instance.getIPList();
        try {
            JSONArray jsonArray = new JSONArray(instanceIPList);
            List<String> ipList = new ArrayList<>();
            List<String> portList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                NetWorkInfo netWorkInfo = gson.fromJson(jsonArray.getJSONObject(i).toString(), NetWorkInfo.class);
                if (!ipList.contains(netWorkInfo.getAddress())) {
                    ipList.add(netWorkInfo.getAddress());
                }
                if (!portList.contains(netWorkInfo.getPort())) {
                    portList.add(netWorkInfo.getPort());
                }
            }
            ArrayAdapter<String> ipArray = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, ipList);
            ipAddress.setAdapter(ipArray);
            ArrayAdapter<String> portArray = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, portList);
            port.setAdapter(portArray);
            ipAddress.setText(instance.getString("IP", ""));
            port.setText(instance.getString("PORT", ""));
            selectHttpSpinner.setSelectedItem(instance.getString("HTTP_TYPE", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btn_save)
    void onClickSave() {
        if (TextUtils.isEmpty(ipAddress.getText().toString())) {
            showErrorDialog(getContext(), "请输入IP地址");
            return;
        }
        NetWorkInfo netWorkInfo = new NetWorkInfo(ipAddress.getText().toString(), port.getText().toString());
        instance.putString("IP", ipAddress.getText().toString());
        instance.putString("PORT", port.getText().toString());
        instance.putString("HTTP_TYPE", selectHttpSpinner.getSelectedItem().toString());
        Gson gson = new Gson();
        if (StringUtils.isEmptyTrim(instance.getIPList())) {
            List<NetWorkInfo> netWorkInfoList = new ArrayList<>();
            netWorkInfoList.add(netWorkInfo);
            instance.setIPList(gson.toJson(netWorkInfoList));
            XToast.success(getContext(), "保存成功").show();
            ActivityUtils.startActivity(LoginActivity.class);
        } else {
            try {
                JSONArray jsonArray = new JSONArray(instance.getIPList());
                List<NetWorkInfo> netWorkInfoList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    NetWorkInfo workInfo = gson.fromJson(jsonArray.getJSONObject(i).toString(), NetWorkInfo.class);
                    netWorkInfoList.add(workInfo);
                }
                netWorkInfoList.add(netWorkInfo);
                instance.setIPList(gson.toJson(netWorkInfoList));
                XToast.success(getContext(), "保存成功").show();
                ActivityUtils.startActivity(LoginActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        XHttpSDK.setBaseUrl(SettingSPUtils.getInstance().getString("HTTP_TYPE", "") + SettingSPUtils.getInstance().getString("IP", "") + ":" + SettingSPUtils.getInstance().getString("PORT", "") + "/");
    }

    @OnClick(R.id.btn_cancel)
    void onClickCancel() {
        instance.remove("IP");
        instance.remove("PORT");
        instance.setIPList("");
        XToast.success(getContext(), "清除缓存成功").show();
        ipAddress.setAdapter(null);
        port.setAdapter(null);
    }
}