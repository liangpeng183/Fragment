package com.smart.fragment.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.bean.JD;

public class ShowJdDetail extends BaseActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_jd_detail);

        initUi();
        initData();
    }

    private void initUi() {
        tv = findViewById(R.id.tv_name);
    }

    private void initData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        JD jd = (JD) intent.getSerializableExtra("jd_data");
        Log.i("","对象：" + jd.getJdName()+"   "+jd.getJdDesc());
        tv.setText(name);
    }

}
