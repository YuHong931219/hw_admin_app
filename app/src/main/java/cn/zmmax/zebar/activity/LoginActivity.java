package cn.zmmax.zebar.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.constant.PermissionConstants;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xhttp2.request.CustomRequest;
import com.xuexiang.xhttp2.subsciber.ProgressLoadingSubscriber;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.net.NetworkUtils;
import com.xuexiang.xutil.system.PermissionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zmmax.zebar.R;
import cn.zmmax.zebar.base.BaseActivity;
import cn.zmmax.zebar.bean.sys.SysGroup;
import cn.zmmax.zebar.bean.sys.SysUser;
import cn.zmmax.zebar.http.api.ApiResponse;
import cn.zmmax.zebar.http.api.ApiServer;
import cn.zmmax.zebar.utils.SettingSPUtils;
import cn.zmmax.zebar.utils.ZmxUtil;

import static cn.zmmax.zebar.utils.UIUtils.showErrorDialog;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.select_ip_spinner)
    Spinner selectIpSpinner;
    @BindView(R.id.user_name)
    AppCompatEditText userName;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.login_btn)
    AppCompatButton loginBtn;
    @BindView(R.id.version_info)
    AppCompatTextView versionInfo;
    private SettingSPUtils instance;
    String[] groupArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.instance = SettingSPUtils.getInstance();
        ButterKnife.bind(this);
        initPermission();
        initData();
    }

    private void initPermission() {
        if (!NetworkUtils.isAvailableByPing()) {
            showErrorDialog(getContext(),"没有可用的网络,请打开数据流量或WIFI");
            return;
        }
        if (!PermissionUtils.isGranted(PermissionConstants.CAMERA, PermissionConstants.STORAGE)) {
            PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE);
        }
        versionInfo.setText(String.format("当前版本号：V%s ", AppUtils.getAppVersionName()));
    }

    void initData() {
        if (!NetworkUtils.isHaveInternet()) {
            XToast.error(getContext(), "当前没有可用的网络，请检查").show();
            return;
        }
        if (instance == null) {
            instance = SettingSPUtils.getInstance();
        }
        String baseUrl = instance.getString("HTTP_TYPE", "") + instance.getString("IP", "") + ":" + instance.getString("PORT", "") + "/";

        if (!XHttpSDK.verifyBaseUrl(baseUrl)) {
            Toast error = XToast.error(getContext(), "您没有设置过通信地址,\n正在跳转到通信设置界面,请稍后。。。");
            error.setDuration(Toast.LENGTH_LONG);
            error.show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ActivityUtils.startActivity(IpSettingActivity.class);
                }
            }, 3000);
        } else {
            findGroupSelect();
            initSysUser();
            password.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    login();
                    return true;
                }
                return false;
            });
        }
    }

    private void initSysUser() {
        instance.getString("GROUP_CODE", "");
        userName.setText(instance.getString("USER_NAME", ""));
        password.setText(instance.getString("PASSWORD", ""));
        if (groupArray != null) {
            for (int i = 0; i < groupArray.length; i++) {
                if (groupArray[i].equals(instance.getString("GROUP_CODE", ""))) {
                    selectIpSpinner.setSelection(i);
                }
            }
        }
    }


    private void findGroupSelect() {
        CustomRequest request = XHttp.custom();
        ProgressLoadingSubscriber<ApiResponse> tipRequestSubscriber = request.call(request.create(ApiServer.class)
                .findGroupSelectForApp(new SysGroup()))
                .subscribeWith(new ProgressLoadingSubscriber<ApiResponse>(iProgressLoader, true, true) {

                    @Override
                    protected void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            String json = JsonUtil.toJson(apiResponse.getBody());
                            Gson gson = new Gson();
                            List<SysGroup> sysGroupList = gson.fromJson(json, new TypeToken<List<SysGroup>>() {}.getType());
                            groupArray = new String[sysGroupList.size()];
                            for (int i = 0; i < sysGroupList.size(); i++) {
                                groupArray[i] = sysGroupList.get(i).getGroupCode() + "--" + sysGroupList.get(i).getGroupName();
                            }
                            WidgetUtils.setSpinnerDropDownVerticalOffset(selectIpSpinner);
                            WidgetUtils.initSpinnerStyle(selectIpSpinner, groupArray);
                        } else {
                            showErrorDialog(getContext(), apiResponse.getMsg());
                        }
                    }
                });
    }

    @OnClick(R.id.login_btn)
    void onClickLogin() {
        login();
    }

    private void login() {
        if (selectIpSpinner.getCount() == 0) {
            return;
        }
        if (selectIpSpinner.getSelectedItem().toString().isEmpty() || "".equals(selectIpSpinner.getSelectedItem().toString())) {
            showErrorDialog(this, "请选择营运中心");
            return;
        }
        SysUser sysUser = new SysUser();
        sysUser.setUsername(Objects.requireNonNull(userName.getText()).toString());
        sysUser.setPassword(Objects.requireNonNull(password.getText()).toString());
        sysUser.setGroupCode(selectIpSpinner.getSelectedItem().toString().substring(0, selectIpSpinner.getSelectedItem().toString().indexOf("--")));
        CustomRequest request = XHttp.custom();
        ProgressLoadingSubscriber<ApiResponse> progressLoadingSubscriber = request.call(request.create(ApiServer.class)
                .appLogin(sysUser))
                .subscribeWith(new ProgressLoadingSubscriber<ApiResponse>(iProgressLoader) {
                    @Override
                    protected void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            SysUser user = (SysUser) ZmxUtil.jsonToJavaBean(apiResponse.getBody(), SysUser.class);
                            instance.putString("USER_NAME", user.getUsername());
                            instance.putString("PASSWORD", user.getPassword());
                            instance.putString("GROUP_CODE", user.getGroupCode());
                            instance.putString("TOKEN", user.getToken());
                            ActivityUtils.startActivity(MainActivity.class);
                            finish();
                        } else {
                            showErrorDialog(getContext(), apiResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        showErrorDialog(getContext(), e.getDetailMessage());
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (StringUtils.isEmptyTrim(SettingSPUtils.getInstance().getString("IP", ""))) {
            XToast.error(getContext(),"通信地址为空，无法请求营运中心").show();
        } else {
            findGroupSelect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @OnClick(R.id.btn_setip)
    public void onClick() {
        ActivityUtils.startActivity(IpSettingActivity.class);
    }
}
