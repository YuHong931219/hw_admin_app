package cn.zmmax.zebar.bean.purchase;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LogiMaterialReturnD {

    private String materialCode;

    private String materialName;

    private String spec;

    private String locationCode;

    private String batchNo;

    private String docCode;

    private BigDecimal amount;

    private BigDecimal actualAmount;
}
