package cn.zmmax.zebar.bean.pro;

import java.util.List;

import lombok.Data;

@Data
public class CuttingEntryRequest {

    private String materialCode;

    private String materialName;

    private String spec;

    private String unit;

    private List<ProWorkMaterialResponse> workList;

    private List<String> locationList;
}
