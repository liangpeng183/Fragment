package com.smart.fragment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.utils.Const;
import com.smart.fragment.utils.ExitApplication;
import com.smart.fragment.utils.MobileFormatCheck;
import com.smart.fragment.utils.OkHttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private String appKey = "2c0cf4ee61a03";
    private String appSecrete = "0bf52f34ed301686aa83178bc4c223c1";

    private EditText phone;
    private EditText password;

    private Button login;
    private TextView regist;

    public String myPhone, myPhone1;
    private LinearLayout rl_lv_item_bg;

    private String URL = Const.URL.URL_SUFFIX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ExitApplication.getInstance().addActivity(this);

        /*
         *  透明状态栏 沉浸式
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        init();

        rl_lv_item_bg = findViewById(R.id.rl_lv_item_bg);
        rl_lv_item_bg.getBackground().setAlpha(100);


        onDestroy();

    }


    private void init() {
        /*
         *  绑定控件
         * */
        phone = findViewById(R.id.phone);
        phone.addTextChangedListener(this);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        regist = findViewById(R.id.regist);

        login.setOnClickListener(this);
        regist.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                //loginCheck();

                break;
            case R.id.regist:
                //  跳到注册界面
                intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
    }


    /*
     *  登录 验证
     * */
    private void loginCheck() {
        myPhone = phone.getText().toString().trim();
        String myPassword = password.getText().toString().trim();

        String url =  URL +"user/login/" + myPhone + "/" + myPassword;

        if (myPhone.isEmpty() || myPhone.equals("")) {
            Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
        } else if (myPassword.isEmpty() || myPassword.equals("")) {
            Toast.makeText(context, "请输入密码！", Toast.LENGTH_SHORT).show();
        } else {
            // get 请求
            OkHttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("异常：", e.toString());
                }

                // 请求网络成功时调用
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        Log.i("返回的消息：", responseData);
                        Message msg = Message.obtain();
                        msg.what = 1;  //登录请求   返回
                        msg.obj = responseData;
                        handler.sendMessage(msg);
                    } else {
                        Log.i("", "error。。。。。");
                    }

                }
            });
        }
    }

    /*
     *  handler 处理
     * */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.equals("ok")) {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", myPhone);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "手机号或密码错误！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (msg.obj.equals("ok")) {
                        Toast.makeText(LoginActivity.this, "该手机号已存在，可直接登录", Toast.LENGTH_SHORT).show();
                    } else if(msg.obj.equals("fail")){
                        Toast.makeText(LoginActivity.this, "该手机号不存在,请先注册", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // 验证该手机号是否已经被注册
    public  void checkPhoneIsExsit(final String phone_num) {
        //final String phone_num1 = phone.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String checkPhoneIsRegist_url = URL + "user/checkPhoneIsRegisted/" + phone_num;
                OkHttpUtil.sendOkHttpRequest(checkPhoneIsRegist_url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("", "网络连接超时");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        Log.i("","验证手机号返回："+data);
                        Message msg = Message.obtain();
                        msg.obj = data;
                        msg.what = 2;
                        handler.sendMessage(msg); // handler 发送
                    }
                });
            }
        }).start();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        myPhone1 = phone.getText().toString().trim();
        if (myPhone1.length() == 11) {
            boolean flag = MobileFormatCheck.mobileNumberFormatCheck(myPhone1);
            if (flag == true) {
                //Log.i("", "手机格式正确");
                //Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
                checkPhoneIsExsit(myPhone1);
            } else {
                Log.i("", "手机格式错误");
                Toast.makeText(context, "手机号格式错误", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
