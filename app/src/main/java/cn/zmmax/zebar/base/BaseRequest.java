package cn.zmmax.zebar.base;


import java.util.HashMap;
import java.util.Map;

import cn.zmmax.zebar.utils.SettingSPUtils;

public class BaseRequest<T> {

    private T data;

    private String token;

    public BaseRequest(T data) {
        this.data = data;
        this.token = SettingSPUtils.getInstance().getToken();
    }

    @SuppressWarnings("unchecked")
    public BaseRequest(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        this.data = (T) map;
        this.token = SettingSPUtils.getInstance().getToken();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
