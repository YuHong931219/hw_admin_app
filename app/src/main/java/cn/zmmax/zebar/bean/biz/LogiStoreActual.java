package cn.zmmax.zebar.bean.biz;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LogiStoreActual {

    private String materialCode;

    private String materialName;

    private String spec;

    private String unit;

    private String batchNo;

    private String locationCode;

    private BigDecimal amount;
}
