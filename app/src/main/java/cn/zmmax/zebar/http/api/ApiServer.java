package cn.zmmax.zebar.http.api;

import cn.zmmax.zebar.base.BaseRequest;
import cn.zmmax.zebar.bean.sys.SysGroup;
import cn.zmmax.zebar.bean.sys.SysUser;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiServer {

    @POST("/sys/company/findGroupSelectForApp")
    Observable<ApiResponse> findGroupSelectForApp(@Body SysGroup sysGroup);

    @POST("/appLogin")
    Observable<ApiResponse> appLogin(@Body SysUser sysUser);

    @POST
    Observable<ApiResponse> postHttp(@Url String url, @Body BaseRequest baseRequest);
}
