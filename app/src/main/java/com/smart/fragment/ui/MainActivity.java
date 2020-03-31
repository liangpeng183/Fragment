package com.smart.fragment.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.community.BFragment;
import com.smart.fragment.home.AFragment;
import com.smart.fragment.person.CFragment;
import com.smart.fragment.utils.ExitApplication;

public class MainActivity extends BaseActivity
        implements View.OnClickListener {

    AFragment aFragment;
    BFragment bFragment;
    CFragment cFragment;
    private static final int NUM = 3;

    TextView[] tv = new TextView[NUM];

    private ImageView imageView_a;
    private ImageView imageView_b;
    private ImageView imageView_c;

    /**
     * 上次点击返回键的时间
     */
    private long lastBackPressed;
    /**
     * 两次点击的间隔时间
     */
    private static final int QUIT_INTERVAL = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExitApplication.getInstance().addActivity(this);

        /*
         *  透明状态栏 沉浸式
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        imageView_a = findViewById(R.id.iv_mainbottom_a);
        imageView_b = findViewById(R.id.iv_mainbottom_b);
        imageView_c = findViewById(R.id.iv_mainbottom_c);


        aFragment = AFragment.createInstance();
        bFragment = BFragment.createInstance();
        cFragment = CFragment.createInstance();

        tv[0] = findViewById(R.id.tv_mainbottom_a);
        tv[1] = findViewById(R.id.tv_mainbottom_b);
        tv[2] = findViewById(R.id.tv_mainbottom_c);
        // 默认 首页
        switchFragment(AFragment.createInstance());
        setBottomView(0);
        imageView_a.setColorFilter(Color.RED);

        /*
         *   intent 过来的用户名
         * */
        getUsername();

    }

    /*
     *   intent 取出登录过来的用户名
     *   bundle 传到个人主页面
     * */
    private void getUsername() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        cFragment.setArguments(bundle);
    }


    /*
     *   切换Fragment
     * */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            transaction.replace(R.id.fragment_panel, fragment).commit();
        }
    }

    /*
     *  点击监听事件
     * */
    @Override
    public void onClick(View v) {
        Fragment fragment;
        switch (v.getId()) {
            case R.id.ll_mainbottom_a:  // 首页
                fragment = aFragment;
                imageView_a.setColorFilter(Color.RED);
                imageView_b.setColorFilter(Color.GRAY);
                imageView_c.setColorFilter(Color.GRAY);
                setBottomView(0);
                break;
            case R.id.ll_mainbottom_b: // 社区
                fragment = bFragment;
                imageView_a.setColorFilter(Color.GRAY);
                imageView_b.setColorFilter(Color.RED);
                imageView_c.setColorFilter(Color.GRAY);
                setBottomView(1);
                break;
            case R.id.ll_mainbottom_c:  // 个人中心
                fragment = cFragment;
                imageView_c.setColorFilter(Color.RED);
                imageView_b.setColorFilter(Color.GRAY);
                imageView_a.setColorFilter(Color.GRAY);
                setBottomView(2);
                break;
            default:
                fragment = aFragment;
                setBottomView(0);
        }
        switchFragment(fragment);
    }

    private void setBottomView(int which) {
        for (int i = 0; i < NUM; i++) {
            tv[i].setSelected(i == which);
        }
    }

    /**
     * 重写onKeyDown()
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long backPressed = System.currentTimeMillis();
            if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                lastBackPressed = backPressed;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();

            } else {
//                finish();
//                System.exit(0);   // 此方法只结束当前活动，返回到上一个活动。
                //  退出   结束所有活动  ，返回界面
                ExitApplication.getInstance().exit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 重写onBackPressed()
     */
    @Override
    public void onBackPressed() {
        long backPressed = System.currentTimeMillis();
        super.onBackPressed();
        if (backPressed - lastBackPressed > QUIT_INTERVAL) {
            lastBackPressed = backPressed;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();

        } else {
            finish();
            System.exit(0);
        }
    }



}
