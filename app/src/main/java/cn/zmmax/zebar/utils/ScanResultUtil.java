package cn.zmmax.zebar.utils;

/**
 * Created by wenchuan on 2017/2/25.
 *
 */
public class ScanResultUtil {

    public static final String CODE_0 = "0";//0#送货单号#批次
    public static final String CODE_1 = "1";//   1#料号#送货单明细ID
    public static final String CODE_2 = "2";//2#库位编号 [收货区\备料区\托盘]
    public static final String CODE_3 = "3";//3#销退单号
    public static final String CODE_4 = "4";//4#工单编号
    public static final String CODE_5 = "5";//5#备料单号
    public static final String CODE_6 = "6";//6#工单#料号#备料单号
    public static final String CODE_7 = "7";//7#杂收杂发单
    public static final String CODE_8 = "8";//8#成品出货单
    public static final String CODE_9 = "9";//9#调拨单号
    public static final String CODE_10 = "10";//10#员工工号
    public static final String CODE_11 = "11";//11#刀具
    public static final String CODE_12 = "12"; // 12#料号#杂发单明细ID    12#班组   GTC
    public static final String CODE_13 = "13"; // 13#料号#仓退单明细ID
    public static final String CODE_14 = "14"; // 14#料号#出货单ID
    public static final String CODE_00 = "00"; // 00#料号#批号#数量#供应商编号#箱号
    public static final String CODE_01 = "01"; // 01#料号#批号#数量#供应商编号#箱号#采购单号#采购项次
    public static final String CODE_BM = "BM"; // 部门

    private String splitChat = "#";
    private String[] r;
    private String code;

    public ScanResultUtil(String s) {
        s = s.trim();
        r = s.split(splitChat);
        code = s;
    }

    public String getCode() {
        return code;
    }

    public int getDataLength() {
        return r.length;
    }

    public String getString(int i) {
        return r[i].trim();
    }

    public int getInt(int i) {
        return Integer.valueOf(r[i].trim().equals("") ? "0" : r[i].trim());
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int _i) {
        StringBuilder s = new StringBuilder();
        for (int i = _i, l = getDataLength(); i <= l - 1; i++) {
            s.append(r[i]);
            if (i < l - 1) {
                s.append(splitChat);
            }
        }
        return s.toString();
    }


}
