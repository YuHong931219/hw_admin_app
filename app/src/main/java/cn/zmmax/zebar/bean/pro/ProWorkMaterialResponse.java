package cn.zmmax.zebar.bean.pro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ProWorkMaterialResponse {

    private String materialCode;

    private String materialName;

    private String spec;

    private String unit;

    private String pieceWidth;

    private String pieceAmount;

    private String cuttingLength;

    private String workCode;

    private String batchNo;

    private String supplierBatchNo;

    private String locationCode;

    private BigDecimal needAmount;

    private List<Map<String,Object>> maps;

    private Boolean checked = false;

    private String replacedMaterial;

}
