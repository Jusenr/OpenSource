package com.jusenr.opensource;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class MainActivity extends AppCompatActivity {

    private String mText;
    private String mText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mText = extras.getString("text");
            mText1 = extras.getString("text1");
        }
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(mText);

        TextView tv1 = (TextView) findViewById(R.id.sample_text1);
        tv1.setText(mText1);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登出
                loginout();
            }
        });
        findViewById(R.id.btn_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChatActivity.class).putExtra("userId", "zxr123"));
            }
        });
    }

    private void loginout() {
        //同步方法
//        EMClient.getInstance().logout(true);

        //异步方法
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d("main", "登出成功！");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "登出成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, String message) {
                Log.d("main", "login: onError: " + code);
                runOnUiThread(new Runnable() {
                    public void run() {
                        TipsUtils.apply(getApplicationContext(), code);
                    }
                });
            }
        });
    }

}
