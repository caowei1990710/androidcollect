package com.codeboy.qianghongbao;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import com.codeboy.qianghongbao.job.WechatAccessbilityJob;

/**
 * <p>Created 16/2/5 下午6:06.</p>
 * <p><a href="mailto:codeboy2013@gmail.com">Email:codeboy2013@gmail.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author LeonLee
 */
public abstract class BaseSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().add(R.id.container, getSettingsFragment()).commitAllowingStateLoss();
        ((Button)findViewById(R.id.test)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            startActivity(LaunchIntent);
//            WechatAccessbilityJob job = new WechatAccessbilityJob();
//            job.handlerClick("我");
            }
        });
        if(isShowBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected boolean isShowBack() {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public abstract Fragment getSettingsFragment();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
