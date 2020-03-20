package cn.zmmax.zebar.http.entity;

import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.net.JsonUtil;


public class AppUpdateEntityParser implements IUpdateParser {


    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        AppUpdateResponse appUpdateResponse = JsonUtil.fromJson(json, AppUpdateResponse.class);
        if (appUpdateResponse != null) {
            return new UpdateEntity()
                    .setHasUpdate(appUpdateResponse.getUpdateStatus() == 1)
                    .setIsIgnorable(true)
                    .setForce(false)
                    .setVersionCode(StringUtils.toInt(appUpdateResponse.getVersionCode()))
                    .setVersionName(appUpdateResponse.getVersionName())
                    .setUpdateContent(appUpdateResponse.getModifyContent())
                    .setIsAutoInstall(true)
                    .setDownloadUrl(appUpdateResponse.getDownloadUrl());
        }
        return null;
    }
}
