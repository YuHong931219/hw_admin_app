package cn.zmmax.zebar.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.just.agentweb.core.AgentWeb;
import com.just.agentweb.core.client.DefaultWebClient;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.xuexiang.xui.utils.DrawableUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xupdate.XUpdate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.zmmax.zebar.R;
import cn.zmmax.zebar.listener.CustomUpdateFailureListener;
import cn.zmmax.zebar.webView.AgentWebActivity;
import cn.zmmax.zebar.webView.MiddlewareWebViewClient;

import static android.widget.LinearLayout.VERTICAL;
import static cn.zmmax.zebar.webView.AgentWebFragment.KEY_URL;

/**
 * 工具类
 */
public final class ZmxUtil {

    public final static String mUpdateUrl = "https://raw.githubusercontent.com/xuexiangjys/XUI/master/jsonapi/update_api.json";

    private ZmxUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 请求浏览器
     *
     * @param url
     */
    public static void goWeb(Context context, final String url) {
        Intent intent = new Intent(context, AgentWebActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

    /**
     * 创建AgentWeb
     *
     * @param fragment
     * @param viewGroup
     * @param url
     * @return
     */
    public static AgentWeb createAgentWeb(Fragment fragment, ViewGroup viewGroup, String url) {
        return AgentWeb.with(fragment)
                .setAgentWebParent(viewGroup, -1, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(-1, 3)
                .useMiddlewareWebClient(new MiddlewareWebViewClient())
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .createAgentWeb()
                .ready()
                .go(url);
    }

    public static void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 进行版本更新检查
     *
     * @param context
     */
    public static void checkUpdate(Context context, boolean needErrorTip) {
        XUpdate.newBuild(context).updateUrl(mUpdateUrl).update();
        XUpdate.get().setOnUpdateFailureListener(new CustomUpdateFailureListener(needErrorTip));
    }

    /**
     * 获取图片选择的配置
     *
     * @param fragment
     * @return
     */
    public static PictureSelectionModel getPictureSelector(Fragment fragment) {
        return PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.XUIPictureStyle)
                .maxSelectNum(8)
                .minSelectNum(1)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .previewEggs(true);
    }

    /**
     * 显示截图结果
     *
     * @param view
     */
    public static void showCaptureBitmap(View view) {
        final MaterialDialog dialog = new MaterialDialog.Builder(view.getContext())
                .customView(R.layout.dialog_drawable_utils_createfromview, true)
                .title("截图结果")
                .build();
        ImageView displayImageView = dialog.findViewById(R.id.createFromViewDisplay);
        Bitmap createFromViewBitmap = DrawableUtils.createBitmapFromView(view);
        displayImageView.setImageBitmap(createFromViewBitmap);
        displayImageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * 显示截图结果
     */
    public static void showCaptureBitmap(Context context, Bitmap bitmap) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_drawable_utils_createfromview, true)
                .title("截图结果")
                .build();
        ImageView displayImageView = dialog.findViewById(R.id.createFromViewDisplay);
        displayImageView.setImageBitmap(bitmap);
        displayImageView.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public static Object jsonToJavaBean(Object linkedTreeMap, Class<?> clazz) {
        Gson gson = new Gson();
        String toJson = gson.toJson(linkedTreeMap);
        return gson.fromJson(toJson, clazz);
    }

    public static <T> List<T> jsonToArrayList(Object linkedTreeMap, Class<T> claszz) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        String toJson = gson.toJson(linkedTreeMap);
        try {
            JSONArray jsonArray = new JSONArray(toJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                T data = gson.fromJson(jsonArray.getJSONObject(i).toString(), claszz);
                list.add(data);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
