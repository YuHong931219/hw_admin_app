package cn.zmmax.zebar.http.interceptor;

import com.xuexiang.xhttp2.interceptor.HttpLoggingInterceptor;
import com.xuexiang.xhttp2.utils.HttpUtils;

import java.io.IOException;

import okhttp3.Connection;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;


public class CustomLoggingInterceptor extends HttpLoggingInterceptor {
    public CustomLoggingInterceptor() {
        super("custom");
        setLevel(Level.PARAM);
    }

    @Override
    protected void logForRequest(Request request, Connection connection) throws IOException {
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        StringBuilder logBuilder = new StringBuilder();
        try {
            logBuilder.append("--> ")
                    .append(request.method())
                    .append(' ')
                    .append(request.url())
                    .append(' ')
                    .append(protocol)
                    .append("\r\n");
            if (hasRequestBody) {
                logBuilder.append("入参:");
                if (HttpUtils.isPlaintext(requestBody.contentType())) {
                    logBuilder.append(bodyToString(request));
                } else {
                    logBuilder.append("maybe [file part] , too large too print , ignored!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log(logBuilder.toString());
    }

    @Override
    protected Response logForResponse(Response response, long tookMs) {
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();

        log("<-- " + clone.code() + ' ' + clone.message() + ' ' + clone.request().url() + " (" + tookMs + "ms）");
        try {
            if (HttpHeaders.hasBody(clone) && responseBody != null) {
                if (HttpUtils.isPlaintext(responseBody.contentType())) {
                    String body = responseBody.string();
                    log("\t出参:" + body);
                    responseBody = ResponseBody.create(responseBody.contentType(), body);
                    return response.newBuilder().body(responseBody).build();
                } else {
                    log("\t出参: maybe [file part] , too large too print , ignored!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
