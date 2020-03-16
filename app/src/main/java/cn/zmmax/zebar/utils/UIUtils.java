package cn.zmmax.zebar.utils;

import android.content.Context;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import cn.zmmax.zebar.R;

/**
 * 对话框封装
 */
public class UIUtils {

    /**
     * 错误提示框
     * @param context context
     * @param message 信息
     */
    public static void showErrorDialog(Context context, String message) {
        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.icon_warning)
                .title("错误")
                .content(message)
                .positiveText("确定")
                .show();
    }

    /**
     * 警告提示框
     * @param context context
     * @param message 信息
     */
    public static void showWaringDialog(Context context, String message) {
        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.icon_tip)
                .title("警告")
                .content(message)
                .positiveText("确定")
                .show();
    }

    /**
     * 成功提示框
     * @param context context
     * @param message 信息
     */
    public static void showSuccessDialog(Context context, String message) {
        new MaterialDialog.Builder(context)
                .iconRes(R.drawable.icon_success)
                .title("提示")
                .content(message)
                .positiveText("确定")
                .show();
    }
}
