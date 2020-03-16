package cn.zmmax.zebar.http.api;

import com.xuexiang.xhttp2.model.ApiResult;


public class ApiResponse<T> extends ApiResult<T> {

    private boolean success;
    private int errorCode;
    private String msg;
    private T body;

    public int getErrorCode() {
        return errorCode;
    }

    public ApiResponse<T> setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public ApiResponse<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getBody() {
        return body;
    }

    public ApiResponse<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public ApiResponse<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public int getCode() {
        return errorCode;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public T getData() {
        return body;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", errorCode=" + errorCode +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }
}
