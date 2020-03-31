package com.smart.fragment.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.utils.OkHttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateSignature extends BaseActivity implements View.OnClickListener {

    private EditText my_Signature;
    private ImageView pre_back;
    private TextView text_num; // 显示剩余字数

    private Button save_signature;

    int num = 30;//限制的最大字数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_signature);

        /*
         *  透明状态栏 沉浸式
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initUi();

        Intent intent = getIntent();
        String name = getIntent().getStringExtra("name");
        my_Signature.setText(name);  // 这里开始变化   调用 变化监听事件
        my_Signature.setSelection(name.length());
        int number = num - my_Signature.length();
        //Log.e("","   afasff    "+number);
        text_num.setText("" + number);  //  "" 转成字符串
        /*
         *   监听 edittext 变化
         * */
        my_Signature.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = num - editable.length();
                text_num.setText("" + number);  //  "" 转成字符串
                selectionStart = my_Signature.getSelectionStart();
                selectionEnd = my_Signature.getSelectionEnd();
                if (temp.length() > num) {
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    my_Signature.setText(editable);
                    my_Signature.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

    }

    private void initUi() {

        my_Signature = findViewById(R.id.my_Signature);

        save_signature = findViewById(R.id.save_signature);
        save_signature.setOnClickListener(this);

        pre_back = findViewById(R.id.back_pre);
        pre_back.setOnClickListener(this);

        text_num = findViewById(R.id.text_num);
        text_num.setText(""+num);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_pre:
                finish();
                break;
            case R.id.save_signature:
                updateOrSaveSign();
                break;
            default:
                break;
        }
    }

    /*
     *  修改 保存 个性签名
     * */
    public void updateOrSaveSign() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mySignature = my_Signature.getText().toString().trim();
                String url = "http://192.168.1.103:8001/user/updateOrSaveSign/" + mySignature;
                OkHttpUtil.sendOkHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                    }
                });
            }
        }).start();
    }


}


