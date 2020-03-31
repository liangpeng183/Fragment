package com.smart.fragment.person;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseFragment;
import com.smart.fragment.person.activity.MyInfoActivity;
import com.smart.fragment.utils.ExitApplication;
import com.smart.fragment.utils.ImageLoader;
import com.smart.fragment.utils.OkHttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout user_info,exit;
    private TextView tv_name;
    private ImageView user_image;

    /*
     *   创建实例
     * */
    public static CFragment createInstance() {
        return new CFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_c, null);

        // 初始化控件
        initUi(view);

        /*
        *   所有点击监听事件
        * */
        setClick();

        /*
        *  访问服务器
        * */
        getData();

        /*
        *  设置圆形头像
        * */
        setImageCircle();


        /*
        *  取出name (当前登录用户)
        * */
        Bundle bundle = getArguments();
        String name = bundle.getString("name");
        tv_name.setText(name);

        return view;
    }

    // 设置圆形头像
    private void setImageCircle() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.head);
        Bitmap bm = ImageLoader.getRoundBitmap(bitmap);
        user_image.setImageBitmap(bm);
    }

    // 访问
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtil.sendOkHttpRequest("http://192.168.1.103:8001/getAll", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("","请求错误13");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        Log.i("","请求成功！"+data);
                    }
                });
            }

        }).start();
    }

    // 初始化控件
    private void initUi(View view) {
        user_info = view.findViewById(R.id.user_info);
        tv_name = view.findViewById(R.id.tv_name);
        user_image = view.findViewById(R.id.user_image);
        exit = view.findViewById(R.id.exit);
    }
    // 设置 点击监听事件
    public void setClick(){
        user_info.setOnClickListener(this);  // 查看用户信息
        exit.setOnClickListener(this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.user_info:
                intent =  new Intent(context, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                exit();
                break;
            default:
                break;
        }

    }

    /*
    *  退出 按钮
    * */
    private void exit() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("是否退出程序")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        getActivity().finish();
//                        System.exit(0);
                        ExitApplication.getInstance().exit(context);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).create();
        alertDialog.show();
    }




}
