package com.smart.fragment.person;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseFragment;
import com.smart.fragment.bean.Users;
import com.smart.fragment.person.activity.MyInfoActivity;
import com.smart.fragment.ui.LoginActivity;
import com.smart.fragment.utils.Const;
import com.smart.fragment.utils.ExitApplication;
import com.smart.fragment.utils.ImageLoader;
import com.smart.fragment.utils.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout user_info, exit;
    private TextView tv_name;
    private ImageView user_image;

    Users users = new Users();

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
         *  设置圆形头像
         * */
        setImageCircle();

        /*
         *  访问服务器
         * */
        getData();


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
        String phone = "18377858933";
        final String url = Const.URL.URL_SUFFIX + "user/getUserByphone/" + phone;
        OkHttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i("", "返回用户信息：" + data);
                changeWithJSONObject(data);
            }
        });

    }

    /*
     *   对象和json格式的转换
     * */
    public void changeWithJSONObject(String jsonData) {
        try {
            //这里只有一条json数据，座椅不需要使用jsonArray
            JSONObject jsonObj = new JSONObject(jsonData);
            String data = jsonObj.getString("data");
            JSONObject user = new JSONObject(data);
            String nickName = user.getString("nickName");
            String phone = user.getString("phone");
            String signature = user.getString("signature");
            String sex = user.getString("sex");
            String headPic = user.getString("headPic");
            Log.i("", "当前用户头像：" + headPic);


            users.setNickName(nickName);
            users.setPhone(phone);
            users.setSignature(signature);
            users.setSex(sex);
            users.setHeadPic(headPic);
            Message msg = Message.obtain();
            msg.what = 1;  // 查询user
            msg.obj = users;
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
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
                    Log.i("", "用户：" + users.getHeadPic());
                    tv_name.setText(users.getNickName());
                  /*  Glide.with(context)
                        .load(users.getHeadPic())
                        .into(head);*/
                   /* if (!users.getHeadPic().equals("")) {
                        Bitmap bitmap = BitmapAndStringChange.urlToBitmap(users.getHeadPic());// url (路径转成bitmap)

                        Log.i("", "用户头像：" + bitmap);
                        Bitmap bm = ImageLoader.getRoundBitmap(bitmap);
                        user_image.setImageBitmap(bm);

                    }*/

                    break;
            }

        }
    };


    // 初始化控件
    private void initUi(View view) {
        user_info = view.findViewById(R.id.user_info);
        tv_name = view.findViewById(R.id.tv_name);
        user_image = view.findViewById(R.id.user_image);
        exit = view.findViewById(R.id.exit);
    }

    // 设置 点击监听事件
    public void setClick() {
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
                intent = new Intent(context, MyInfoActivity.class);
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
                        //cleatSp();
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

    private void cleatSp() {
        //删除文件

        File file = new File("/data/data/" + context.getPackageName().toString() + "/shared_prefs", "user.xml");
        if (file.exists()) {
            file.delete();
            Log.i("","已删除");
            Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
        }

        //清空文件

        if (LoginActivity.sp != null) {
            LoginActivity.sp.edit().clear().commit();
            Toast.makeText(context, "数据已清空", Toast.LENGTH_LONG).show();
        }


    }



}
