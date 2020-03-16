package cn.zmmax.zebar.http.interceptor;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.zmmax.zebar.utils.SettingSPUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomTokenInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("token", SettingSPUtils.getInstance().getString("TOKEN", ""));
        return chain.proceed(builder.build());
    }
}
