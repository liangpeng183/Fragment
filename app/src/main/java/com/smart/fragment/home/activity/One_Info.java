package com.smart.fragment.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;

public class One_Info extends BaseActivity {

    private TextView textView1;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one__info);

        Intent intent = getIntent();
        String desc = intent.getStringExtra("desc");
        String img_url = intent.getStringExtra("img");
        Log.i("","图片地址：：："+img_url);

        textView1 = findViewById(R.id.tv_desc);
        textView1.setText(desc);

        img = findViewById(R.id.img);


    }
}
