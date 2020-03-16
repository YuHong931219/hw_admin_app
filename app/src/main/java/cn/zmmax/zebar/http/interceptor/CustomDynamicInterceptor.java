package cn.zmmax.zebar.http.interceptor;

import com.xuexiang.xhttp2.interceptor.BaseDynamicInterceptor;
import com.xuexiang.xutil.data.DateUtils;

import java.util.TreeMap;

import cn.zmmax.zebar.http.TokenManager;

/**
 * 自定义动态添加请求参数的拦截器
 */
public class CustomDynamicInterceptor extends BaseDynamicInterceptor<CustomDynamicInterceptor> {

    @Override
    protected TreeMap<String, Object> updateDynamicParams(TreeMap<String, Object> dynamicMap) {
        if (isAccessToken()) {//是否添加token
            dynamicMap.put("token", TokenManager.getInstance().getToken());
        }
        if (isSign()) {//是否添加签名
            dynamicMap.put("sign", TokenManager.getInstance().getSign());
        }
        if (isTimeStamp()) {//是否添加请求时间戳
            dynamicMap.put("timeStamp", DateUtils.getNowMills());
        }
        return dynamicMap;
    }
}
