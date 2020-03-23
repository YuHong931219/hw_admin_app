package cn.zmmax.zebar;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.kongzue.debugsdk.DebugSDK;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.XHttpSDK;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.system.DeviceUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import org.litepal.LitePalApplication;

import cn.zmmax.scm.page.AppPageConfig;
import cn.zmmax.zebar.broadcast.ButtonBroadcastReceiver;
import cn.zmmax.zebar.broadcast.ScanBroadcastReceiver;
import cn.zmmax.zebar.http.converter.MyGsonConverterFactory;
import cn.zmmax.zebar.http.entity.AppUpdateEntityParser;
import cn.zmmax.zebar.http.interceptor.CustomDynamicInterceptor;
import cn.zmmax.zebar.http.interceptor.CustomExpiredInterceptor;
import cn.zmmax.zebar.http.interceptor.CustomLoggingInterceptor;
import cn.zmmax.zebar.http.interceptor.CustomTokenInterceptor;
import cn.zmmax.zebar.http.interceptor.OKHttpUpdateHttpService;
import cn.zmmax.zebar.utils.SettingSPUtils;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

public class MyApplication extends Application {
    private static MyApplication instance;
    private static PosApi mPosSDK = null;
    private static PrintQueue mPrintQueue = null;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init() {
        instance = this;
        LitePalApplication.initialize(this);
        TypefaceProvider.registerDefaultIconSets();
        initXUI();
        initXUtil();
        initHttp();
        initXPage();
        initAOP();
        initQRCode();
        initXCrash();
        initXUpdate();
        String deviceModel = DeviceUtils.getDeviceModel();
        if (deviceModel.contains("5501")) {
            initXPrint();
        }
    }

    private void initXUpdate() {
        //设置版本更新出错的监听
        XUpdate.get()
                .debug(true)
                .isWifiOnly(false)
                .isGet(true)
                .isAutoMode(false)
                .param("versionCode", UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                .setOnUpdateFailureListener(error -> {
                    if (error.getCode() != CHECK_NO_NEW_VERSION) {
                        ToastUtils.toast(error.toString());
                    }
                })
                .supportSilentInstall(true)
                .setIUpdateParser(new AppUpdateEntityParser())
                .setIUpdateHttpService(new OKHttpUpdateHttpService())
                .init(this);
    }


    private void initXPrint() {
        mPosSDK = PosApi.getInstance(this);
        // 根据型号进行初始化mPosApi类
        if (Build.MODEL.contains("LTE")
                || android.os.Build.DISPLAY.contains("3508")
                || android.os.Build.DISPLAY.contains("403")
                || android.os.Build.DISPLAY.contains("35S09")) {
            mPosSDK.initPosDev("ima35s09");
        } else if (Build.MODEL.contains("5501")) {
            mPosSDK.initPosDev("ima35s12");
        } else {
            mPosSDK.initPosDev(PosApi.PRODUCT_MODEL_IMA80M01);
        }
        new Handler().postDelayed(() -> {
            if (mPosSDK != null) {
                mPosSDK.gpioControl((byte) 0x1E, 0, 1);
                mPosSDK.extendSerialInit(3, 4, 1, 1, 1, 1);
            }
        }, 1000);
        ButtonBroadcastReceiver buttonBroadcastReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ismart.intent.scandown");
        registerReceiver(buttonBroadcastReceiver, intentFilter);
        ScanBroadcastReceiver scanBroadcastReceiver = new ScanBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PosApi.ACTION_POS_COMM_STATUS);
        registerReceiver(scanBroadcastReceiver, filter);

        //监听初始化回调结果
        mPosSDK.setOnComEventListener(mCommEventListener);
        // 打印队列初始化
        mPrintQueue = new PrintQueue(getInstance(), mPosSDK);
//        // 打印队列初始化
        mPrintQueue.init();
        // 打印结果监听
        mPrintQueue.setOnPrintListener(new PrintQueue.OnPrintListener() {
            @Override
            public void onFinish() { }

            @Override
            public void onFailed(int state) {
                switch (state) {
                    case PosApi.ERR_POS_PRINT_NO_PAPER:
                        // 打印缺纸
                        XLogger.d("打印缺纸");
                        break;
                    case PosApi.ERR_POS_PRINT_FAILED:
                        // 打印失败
                        XLogger.d("打印失败");
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_LOW:
                        // 电压过低
                        XLogger.d("电压过低");
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_HIGH:
                        // 电压过高
//                        showTip("电压过高");
                        break;
                }
            }

            @Override
            public void onGetState(int arg0) { }

            @Override
            public void onPrinterSetting(int state) {
                switch (state) {
                    case 0:
                        Toast.makeText(getInstance(), "持续有纸", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getInstance(), "缺纸", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getInstance(), "检测到黑标", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }



    private void initXCrash() {
        DebugSDK.initSDK(this)
                .setOnBugReportListener((exceptionMessage, phoneInfo) -> {
                    //此处处理崩溃信息，例如上传服务器。
                    Log.d("<<<", "exceptionMessage: " + exceptionMessage + "\nphoneInfo: " + phoneInfo);
//                    Intent data = new Intent(Intent.ACTION_SENDTO);
//                    data.setData(Uri.parse("mailto:18779861289@163.com"));
//                    data.putExtra(Intent.EXTRA_SUBJECT, "移动条码崩溃日志");
//                    data.putExtra(Intent.EXTRA_TEXT, "exceptionMessage: " + exceptionMessage + "\nphoneInfo: " + phoneInfo);
//                    startActivity(data);
                });
    }

    private void initQRCode() {
        XQRCode.debug(true);
    }

    private void initAOP() {
        //初始化插件
        XAOP.init(this);
        //日志打印切片开启
        XAOP.debug(BuildConfig.DEBUG);
        //设置动态申请权限切片 申请权限被拒绝的事件响应监听
        XAOP.setOnPermissionDeniedListener(permissionsDenied -> ToastUtils.toast("权限申请被拒绝:" + StringUtils.listToString(permissionsDenied, ",")));
    }

    private void initHttp() {
        XHttpSDK.init(this);   //初始化网络请求框架，必须首先执行
        XHttpSDK.debug("XHttp");
        XHttpSDK.setBaseUrl(SettingSPUtils.getInstance().getString("HTTP_TYPE", "") + SettingSPUtils.getInstance().getString("IP", "") + ":" + SettingSPUtils.getInstance().getString("PORT", "") + "/");
        CustomDynamicInterceptor dynamicInterceptor = new CustomDynamicInterceptor();
        dynamicInterceptor.accessToken(true);
        XHttpSDK.addInterceptor(dynamicInterceptor); //设置动态参数添加拦截器
        XHttpSDK.addInterceptor(new CustomExpiredInterceptor()); //请求失效校验拦截器
        XHttpSDK.addInterceptor(new CustomLoggingInterceptor()); //请求日志拦截器
        XHttpSDK.addInterceptor(new CustomTokenInterceptor());
        XHttp.getInstance()
                .addConverterFactory(MyGsonConverterFactory.create())
                .setRetryDelay(3)
                .setReadTimeOut(100000)
                .setWriteTimeOut(100000)
                .setTimeout(100000)
                .setConnectTimeout(100000);
    }

    private void initXPage() {
        //页面注册
        PageConfig.getInstance()
                .setPageConfiguration(context -> {
                    //自动注册页面,是编译时自动生成的，build一下就出来了。如果你还没使用@Page的话，暂时是不会生成的。
                    return AppPageConfig.getInstance().getPages(); //自动注册页面
                })
                .debug("PageLog")       //开启调试
                .setContainActivityClazz(XPageActivity.class) //设置默认的容器Activity
                .enableWatcher(false)   //设置是否开启内存泄露监测
                .init(this);            //初始化页面配置
    }

    private void initXUtil() {
        XUtil.init(this);
        XUtil.debug(true);
    }


    private void initXUI() {
        XUI.init(this);
        XUI.debug(true);
        XToast.Config.get().setGravity(Gravity.CENTER);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * Called when the overall system is running low on memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w("MyApplication", "System is running low on memory");
    }

    // 其他地方引用mPosApi变量
    public static PosApi getPosApi() {
        return mPosSDK;
    }

    public static PrintQueue getPrintQueue() {
        return mPrintQueue;
    }

    /**
     * 初始化设备
     */
    static PosApi.OnCommEventListener mCommEventListener = (cmdFlag, state, resp, respLen) -> {
        if (cmdFlag == PosApi.POS_INIT) {
            if (state == PosApi.COMM_STATUS_SUCCESS) {
                Toast.makeText(getInstance(), "设备初始化成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getInstance(), "设备初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
