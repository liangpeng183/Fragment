package com.smart.fragment.ui;

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
import android.widget.Toast;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.utils.Const;
import com.smart.fragment.utils.MobileFormatCheck;
import com.smart.fragment.utils.OkHttpUtil;
import com.smart.fragment.utils.SendSmsTimerUtils;

import java.io.IOException;

import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private EditText nickName, password, phone, code;
    private Button getCode, regist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        /*
         *  透明状态栏 沉浸式
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initUi();

    }

    /*
     *  绑定控件
     * */
    private void initUi() {
        nickName = findViewById(R.id.nickName);
        phone = findViewById(R.id.account_input);
        phone.addTextChangedListener(this);


        password = findViewById(R.id.password_input);
        code = findViewById(R.id.code);

        getCode = findViewById(R.id.getCode_btn);
        getCode.setOnClickListener(this);
        regist = findViewById(R.id.regist_btn);
        regist.setOnClickListener(this);
    }


    // 使用完EventHandler需注销，否则可能出现内存泄漏
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        final String myPhone = phone.getText().toString().trim();
        final String name = nickName.getText().toString().trim();
        final String psw = password.getText().toString().trim();
        switch (view.getId()) {
            // 获取验证码
            case R.id.getCode_btn:
                /**
                  * 第一个参数：按钮控件(需要实现倒计时的Button)
                  * 第二个参数：倒计时总时间，以毫秒为单位；
                  * 第三个参数：渐变事件，最低1秒，也就是说设置0-1000都是以一秒渐变，设置1000以上改变渐变时间
                  * 第四个个参数：点击textview之前的背景
                  * 第五个参数：点击textview之后的背景
                  */
                SendSmsTimerUtils mCountDownTimerUtils = new SendSmsTimerUtils(getCode, 60000, 1000, R.color.colorAccent, R.color.red);
                mCountDownTimerUtils.start();
                //通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", myPhone);
                break;
            // 注册
            case R.id.regist_btn:
                saveRegist(myPhone, name, psw);
/*                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", myPhone, code.getText().toString().trim());
                // 启动短信验证sdk   
                EventHandler eh = new EventHandler() {
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        // TODO 此处不可直接处理UI线程，处理后续操作需传到主线程中操作
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //回调完成  -1   ①
                            Log.i("result 回调完成----->", String.valueOf(result));
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //提交验证码成功   3    ③
                                Log.i("event 提交成功----->", String.valueOf(event));
                                saveRegist(myPhone,name,psw);
                            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                //获取验证码成功   2     ②
                                Log.i("event 获取成功----->", String.valueOf(event));
                            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                                //返回支持发送验证码的国家列表
                            }
                        } else {
                            ((Throwable) data).printStackTrace();
                            //  回调失败  0
                            Log.i("result 回调失败----->", String.valueOf(result));
                        }
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.arg1 = event;
                        msg.arg2 = result;
                        msg.obj = data;
                        handler.sendMessage(msg);
                    }
                };
//注册一个事件回调监听，用于处理SMSSDK接口请求的结果
                SMSSDK.registerEventHandler(eh);*/
                break;
            default:
                break;
        }

    }


    /*
     * 保存注册用户
     * */
    private void saveRegist(String myPhone, String name, String psw) {
        final String url = Const.URL.URL_SUFFIX + "user/regist";
        /*Users users = new Users();
        users.setNickName(name);
        users.setPhone(myPhone);
        users.setPassword(psw);
        Gson gson = new Gson();
        String userJon = gson.toJson(users);*/
        //Log.i("","json格式：----》"+userJon);
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        //final RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), userJon);
        final RequestBody requestBody = new FormBody.Builder()
                .add("nickName", name)
                .add("phone", myPhone)
                .add("password", psw)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtil.sendOkHttpResponse(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("", "请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        Log.i("", "注册返回的对应消息：" + data);
                        Message msg = Message.obtain();
                        msg.what = 3; // 注册
                        msg.obj = data;
                        handler.sendMessage(msg);
                    }
                });

            }
        }).start();
    }

    /*
     *  handler 处理
     * */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.i("handler1----->", msg.obj.toString());
                Log.i("handler2----->", String.valueOf(msg.arg1));//event
                Log.i("handler3----->", String.valueOf(msg.arg2));//result
                if (msg.arg2 == 0) {
                    Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();

                } else if (msg.arg2 == -1) {
                    if (msg.arg1 == 3) {
                    }
                    Toast.makeText(RegistActivity.this, "验证成功！", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 2) {   //what 为 2  ，判断手机号是否已被注册
                if (msg.obj.equals("ok")) {
                    Toast.makeText(RegistActivity.this, "该手机号已被注册，请登录", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistActivity.this, "该手机号可用", Toast.LENGTH_SHORT).show();
                }
            }
            else if(msg.what == 3){

            }
        }
    };


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String phone_num = phone.getEditableText().toString().trim();
        if (phone_num.length() == 11) {
            boolean flag = MobileFormatCheck.mobileNumberFormatCheck(phone_num);
            if (flag == true) {
                //Log.i("", "手机格式正确");
                //Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
                // 验证该手机号是否已经被注册
                checkPhoneIsExsit(phone_num);

            } else {
                Log.i("", "手机格式错误");
                Toast.makeText(context, "手机号格式错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 验证该手机号是否已经被注册
    public  void checkPhoneIsExsit(final String phone_num) {
        //final String phone_num1 = phone.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String checkPhoneIsRegist_url = Const.URL.URL_SUFFIX + "user/checkPhoneIsRegisted/" + phone_num;
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


}
