package cn.zmmax.zebar.webView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.xuexiang.xrouter.facade.Postcard;
import com.xuexiang.xrouter.facade.callback.NavCallback;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.tip.ToastUtils;

import cn.zmmax.zebar.R;

import static cn.zmmax.zebar.webView.AgentWebFragment.KEY_URL;

/**
 * 壳浏览器
 */
public class AgentWebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web);

        Uri uri = getIntent().getData();
        if (uri != null) {
            XRouter.getInstance().build(uri).navigation(this, new NavCallback() {
                @Override
                public void onArrival(Postcard postcard) {
                    finish();
                }
            });
        } else {
            setContentView(R.layout.activity_agent_web);
            String url = getIntent().getStringExtra(KEY_URL);
            if (url != null) {
                openFragment(url);
            } else {
                ToastUtils.toast("数据出错！");
                finish();
            }
        }
    }

    private AgentWebFragment mAgentWebFragment;

    private void openFragment(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_frame_layout, mAgentWebFragment = AgentWebFragment.getInstance(url));
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
//		mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AgentWebFragment agentWebFragment = mAgentWebFragment;
        if (agentWebFragment != null) {
            if (((FragmentKeyDown) agentWebFragment).onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
