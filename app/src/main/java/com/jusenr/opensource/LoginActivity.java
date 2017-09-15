package com.jusenr.opensource;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

public class LoginActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = mUsername.getText().toString().trim();
                String passwordStr = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(usernameStr)) {
                    Toast.makeText(getApplicationContext(), R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordStr)) {
                    Toast.makeText(getApplicationContext(), R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                //注册
                register(usernameStr, passwordStr);
            }
        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = mUsername.getText().toString().trim();
                String passwordStr = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(usernameStr)) {
                    Toast.makeText(getApplicationContext(), R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordStr)) {
                    Toast.makeText(getApplicationContext(), R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
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
                Log.d("main", "登录成功！");
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
                        mUsername.setText("");
                        mPassword.setText("");

                        Bundle bundle = new Bundle();
                        bundle.putString("text", stringFromJNI());
                        bundle.putString("text1", stringFromJNI2());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录失败！");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "登录失败!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void register(final String username, final String password) {
        new Thread(new Runnable() {
            public void run() {
                //注册
                //同步方法
                try {
                    EMClient.getInstance().createAccount(username, password);
                    Log.d("main", "注册成功！");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "注册成功!", Toast.LENGTH_SHORT).show();
                            mUsername.setText(username);
                            mPassword.setText(password);
                        }
                    });
                } catch (final HyphenateException e) {
                    Log.d("main", "注册失败！");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
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

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            Log.d("main", "onConnected");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    ToastUtils.showAlertToast(getApplicationContext(), "onDisconnected");
                }
            });
        }

        @Override
        public void onDisconnected(final int error) {
            Log.d("main", "onDisconnected");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    ToastUtils.showAlertToast(getApplicationContext(), "onDisconnected");
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(LoginActivity.this)) {
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
