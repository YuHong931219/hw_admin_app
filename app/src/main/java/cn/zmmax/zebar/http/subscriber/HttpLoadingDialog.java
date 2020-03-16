package cn.zmmax.zebar.http.subscriber;


import android.content.Context;

import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;
import com.xuexiang.xhttp2.subsciber.impl.OnProgressCancelListener;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.dialog.LoadingDialog;


public class HttpLoadingDialog implements IProgressLoader {

    /**
     * 进度loading弹窗
     */
    private LoadingDialog mDialog;
    /**
     * 进度框取消监听
     */
    private OnProgressCancelListener mOnProgressCancelListener;


    public HttpLoadingDialog(Context context) {
        this(context, "加载中...");
    }

    public HttpLoadingDialog(Context context, String msg) {
        mDialog = WidgetUtils.getLoadingDialog(context, msg);
        updateMessage(msg);
    }

    @Override
    public boolean isLoading() {
        return mDialog != null && mDialog.isShowing();
    }

    @Override
    public void updateMessage(String msg) {
        if (mDialog != null) {
            mDialog.setTitle(msg);
        }
    }

    @Override
    public void showLoading() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void dismissLoading() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void setCancelable(boolean flag) {
        mDialog.setCancelable(flag);
        if (flag) {
            mDialog.setOnCancelListener(dialogInterface -> {
                if (mOnProgressCancelListener != null) {
                    mOnProgressCancelListener.onCancelProgress();
                }
            });
        }
    }

    @Override
    public void setOnProgressCancelListener(OnProgressCancelListener listener) {
        mOnProgressCancelListener = listener;
    }
}
