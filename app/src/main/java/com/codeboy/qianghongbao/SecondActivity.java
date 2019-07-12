package com.codeboy.qianghongbao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by snsoft on 18/1/2019.
 */

public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        if (null != intent) {
            Uri uri = intent.getData();
            if (uri == null) {
                return;
            }
            String acionData = uri.getQueryParameter("action");

            TextView tv = (TextView)findViewById(R.id.qijian_test_tv);
            tv.append("\n传过来的action值为:" + acionData);
        }
    }
}
