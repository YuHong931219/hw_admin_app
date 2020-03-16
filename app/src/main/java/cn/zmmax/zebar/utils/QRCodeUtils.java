package cn.zmmax.zebar.utils;


public class QRCodeUtils {

    /* 0#送货单 */
    public static final String TYPE_0 = "0";
    /* 1#物料#送货单明细ID/批号 */
    public static final String TYPE_1 = "1";
    /* 2#库位 */
    public static final String TYPE_2 = "2";
    /* 3#销退单 */
    public static final String TYPE_3 = "3";
    /* 4#工单 */
    public static final String TYPE_4 = "4";
    /* 暂定 */
    public static final String TYPE_5 = "5";
    /* 暂定 */
    public static final String TYPE_6 = "6";
    /* 7#杂收单/杂发单 */
    public static final String TYPE_7 = "7";
    /* 8#出货通知单 */
    public static final String TYPE_8 = "8";
    /* 9#调拨单 */
    public static final String TYPE_9 = "9";
    /* 暂定 */
    public static final String TYPE_10 = "10";
    /* 暂定 */
    public static final String TYPE_11 = "11";
    /* 暂定 */
    public static final String TYPE_12 = "12";
    /* 13#仓退单 */
    public static final String TYPE_13 = "13";
    /* 暂定 */
    public static final String TYPE_14 = "14";

    private String [] qr;

    public QRCodeUtils(String qrCode) {
        qr = qrCode.split("#");
    }


    public String getContentByPosition(int position) {
        return qr[position];
    }
}
