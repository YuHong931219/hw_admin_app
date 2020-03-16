package cn.zmmax.zebar.http;

import cn.zmmax.zebar.bean.sys.SysUser;


public class TokenManager {

    private static TokenManager sInstance;

    private String mToken = "";

    private String mSign = "";

    /**
     * 当前登录的用户
     */
    private SysUser mLoginUser;

    private TokenManager() {

    }

    public static TokenManager getInstance() {
        if (sInstance == null) {
            synchronized (TokenManager.class) {
                if (sInstance == null) {
                    sInstance = new TokenManager();
                }
            }
        }
        return sInstance;
    }

    public TokenManager setToken(String token) {
        mToken = token;
        return this;
    }

    public String getToken() {
        return mToken;
    }

    public TokenManager setSign(String sign) {
        mSign = sign;
        return this;
    }

    public String getSign() {
        return mSign;
    }

    public SysUser getLoginUser() {
        return mLoginUser;
    }

    public boolean isUserLogined() {
        return mLoginUser != null;
    }

    public TokenManager setLoginUser(SysUser loginUser) {
        mLoginUser = loginUser;
        return this;
    }
}
