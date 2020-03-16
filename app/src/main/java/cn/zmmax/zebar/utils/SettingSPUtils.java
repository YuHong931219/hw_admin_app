package cn.zmmax.zebar.utils;

import android.content.Context;

import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.data.BaseSPUtil;

public class SettingSPUtils extends BaseSPUtil {

    private static volatile SettingSPUtils sInstance = null;

    private SettingSPUtils(Context context) {
        super(context);
    }

    /**
     * 获取单例
     * @return
     */
    public static SettingSPUtils getInstance() {
        if (sInstance == null) {
            synchronized (SettingSPUtils.class) {
                if (sInstance == null) {
                    sInstance = new SettingSPUtils(XUtil.getContext());
                }
            }
        }
        return sInstance;
    }

    private final String IS_FIRST_OPEN_KEY = "is_first_open_key";

    private final String IP_INFO = "ip_info";

    private final String TOKEN = "TOKEN";

    /**
     * 是否是第一次启动
     */
    public boolean isFirstOpen() {
        return getBoolean(IS_FIRST_OPEN_KEY, true);
    }

    /**
     * 设置是否是第一次启动
     */
    public void setIsFirstOpen(boolean isFirstOpen) {
        putBoolean(IS_FIRST_OPEN_KEY, isFirstOpen);
    }

    public String getIPList() {
        return getString(IP_INFO, "");
    }

    public void setIPList(String json) {
        putString(IP_INFO, json);
    }

    public String getToken() {
        return getString(TOKEN, "");
    }
}
