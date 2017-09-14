package com.jusenr.opensource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.jusenr.opensourcelibrary.ToastUtils;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showAlertToast(getApplicationContext(), "Hello See!");
            }
        });

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        TextView tv1 = (TextView) findViewById(R.id.sample_text1);
        tv1.setText(stringFromJNI2());

        mUsername = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = mUsername.getText().toString().trim();
                String passwordStr = mPassword.getText().toString().trim();

                //注册
                register(usernameStr, passwordStr);
            }
        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = mUsername.getText().toString().trim();
                String passwordStr = mPassword.getText().toString().trim();

                //登录
                login(usernameStr, passwordStr);
            }
        });
        findViewById(R.id.tv_loginout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登出
                loginout();
            }
        });

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    private void login(String username, String password) {
        EMClient.getInstance().login(username, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                Toast.makeText(getApplicationContext(), "登录聊天服务器成功!", Toast.LENGTH_SHORT).show();
                mUsername.setText("");
                mPassword.setText("");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
                Toast.makeText(getApplicationContext(), "登录聊天服务器失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String username, String password) {
        //注册
        //同步方法
        try {
            EMClient.getInstance().createAccount(username, password);
            Log.d("main", "注册成功！");
            Toast.makeText(getApplicationContext(), "注册成功!", Toast.LENGTH_SHORT).show();
            mUsername.setText("");
            mPassword.setText("");
        } catch (HyphenateException e) {
            e.printStackTrace();
            Log.d("main", "注册失败！");
            Toast.makeText(getApplicationContext(), "注册失败!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginout() {
        //同步方法
//        EMClient.getInstance().logout(true);

        //异步方法
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                TipsUtils.apply(getApplicationContext(), code);
            }
        });
    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                        } else {
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String stringFromJNI2();
}
