package com.smart.fragment.person.activity;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.utils.Const;
import com.smart.fragment.utils.OkHttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateNickName extends BaseActivity implements View.OnClickListener {

    private ImageView back_pre;
    private Button save_update;
    private EditText nickName_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatenick);

        /*
         *  透明状态栏 沉浸式
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        /*
         *  初始化控件
         * */
        initUi();
        Log.i("CharSequence------>", "333333333");

        Intent intent = getIntent();
        String name = getIntent().getStringExtra("name");
        nickName_update.setText(name);  // 这里开始变化   调用 变化监听事件
        Log.i("CharSequence------>", "444444");


    }

    private void initUi() {
        back_pre = findViewById(R.id.back_pre);
        back_pre.setOnClickListener(this);

        save_update = findViewById(R.id.save_nickUpdate);
        save_update.setOnClickListener(this);
        save_update.setEnabled(false);

        nickName_update = findViewById(R.id.nickName_update);
        nickName_update.addTextChangedListener(textWatcher);
    }

    /*
     *  edittext 变化监听事件
     * */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i("CharSequence------>", "11111111111");
            save_update.setEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i("CharSequence------>", charSequence.toString());

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.i("CharSequence------>", "222222222");
            save_update.setEnabled(true);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_pre:
                finish();
                break;
            case R.id.save_nickUpdate:
                updateNickName();
                break;
            default:
                break;
        }
    }

    /*
     *   修改昵称   接口
     * */
    private void updateNickName() {

        String nick = nickName_update.getText().toString().trim();
        String url = Const.URL.URL_SUFFIX +  "user/updateNickName/" + nick;
        OkHttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        });

    }


    // handler 处理
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.equals("ok")) {
                        finish();
                    } else {
                        Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };


}
