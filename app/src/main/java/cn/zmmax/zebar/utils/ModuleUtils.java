package cn.zmmax.zebar.utils;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cn.zmmax.zebar.R;

/**
 * Created by L.W.C on 2017/4/27.
 */

public class ModuleUtils {

    public static void etChange(Activity activity, EditText edt, List<EditText> paramList) {
        for (int i = 0; i < paramList.size(); i++) {
            paramList.get(i).setTextColor(activity.getResources().getColor(R.color.txt_detail));
        }
        edt.setTextColor(activity.getResources().getColor(R.color.edt_focue_txt));
        edt.setSelectAllOnFocus(true);
    }

    public static void tvChange(Activity activity, TextView txt, List<TextView> paramList) {
        for (int i = 0; i < paramList.size(); i++) {
            paramList.get(i).setTextColor(activity.getResources().getColor(R.color.txt_detail));
        }
        txt.setTextColor(activity.getResources().getColor(R.color.edt_focue_txt));
    }

    public static void viewChange(View pView, List<View> paramList) {
        for (int i = 0; i < paramList.size(); i++) {
            paramList.get(i).setBackground(null);
        }
        pView.setBackgroundResource(R.drawable.view_focus_bg_ea8010);
    }
}
