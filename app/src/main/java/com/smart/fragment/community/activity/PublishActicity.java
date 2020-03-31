package com.smart.fragment.community.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;

public class PublishActicity extends BaseActivity implements View.OnClickListener {

    private ImageView back_pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        /*
         *  透明状态栏 沉浸式
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        back_pre = findViewById(R.id.back_pre);
        back_pre.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_pre:
                //Intent intent = new Intent(this,BFragment.class);
               // startActivity(intent);
                this.finish();
                break;
        }
    }
}
