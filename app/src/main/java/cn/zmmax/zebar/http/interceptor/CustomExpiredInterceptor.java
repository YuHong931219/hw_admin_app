package cn.zmmax.zebar.http.interceptor;

import com.xuexiang.xhttp2.interceptor.BaseExpiredInterceptor;
import com.xuexiang.xhttp2.model.ExpiredInfo;
import com.xuexiang.xhttp2.utils.HttpUtils;
import com.xuexiang.xutil.net.JSONUtils;

import cn.zmmax.zebar.bean.sys.SysUser;
import cn.zmmax.zebar.http.TokenManager;
import okhttp3.Response;

import static cn.zmmax.zebar.http.interceptor.CustomExpiredInterceptor.ExpiredType.KEY_TOKEN_EXPIRED;
import static cn.zmmax.zebar.http.interceptor.CustomExpiredInterceptor.ExpiredType.KEY_UNREGISTERED_USER;

/**
 * 处理各种失效响应处理：包括token过期、账号异地登录、时间戳过期、签名sign错误等
 */
public class CustomExpiredInterceptor extends BaseExpiredInterceptor {

    /**
     * Token失效，需要重新获取token的code码
     */
    public static final int TOKEN_INVALID = 100;
    /**
     * 缺少Token
     */
    public static final int TOKEN_MISSING = TOKEN_INVALID + 1;
    /**
     * 认证失败
     */
    public static final int AUTH_ERROR = TOKEN_MISSING + 1;

    @Override
    protected ExpiredInfo isResponseExpired(Response oldResponse, String bodyString) {
        int code = JSONUtils.getInt(bodyString, "errorCode", 0);
        ExpiredInfo expiredInfo = new ExpiredInfo(code);
        switch (code) {
            case TOKEN_INVALID:
            case TOKEN_MISSING:
                expiredInfo.setExpiredType(KEY_TOKEN_EXPIRED).setBodyString(bodyString);
                break;
            case AUTH_ERROR:
                expiredInfo.setExpiredType(KEY_UNREGISTERED_USER).setBodyString(bodyString);
                break;
            default:
                break;
        }
        return expiredInfo;
    }

    @Override
    protected Response responseExpired(Response oldResponse, Chain chain, ExpiredInfo expiredInfo) {
        switch(expiredInfo.getExpiredType()) {
            case KEY_TOKEN_EXPIRED:
                SysUser user = TokenManager.getInstance().getLoginUser();
                if (user == null) {
                    return HttpUtils.getErrorResponse(oldResponse, expiredInfo.getCode(), "请先进行登录！");
                }
                break;
            case KEY_UNREGISTERED_USER:
                return HttpUtils.getErrorResponse(oldResponse, expiredInfo.getCode(), "非法用户登录！");
            default:
                break;
        }
        return null;
    }

    /**
     * 失效类型
     */
    static final class ExpiredType {

        /**
         * token失效
         */
        static final int KEY_TOKEN_EXPIRED = 10;

        /**
         * 未注册的用户
         */
        static final int KEY_UNREGISTERED_USER = KEY_TOKEN_EXPIRED + 1;
    }
}
