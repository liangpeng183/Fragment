package com.smart.fragment.person.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.bean.Users;
import com.smart.fragment.utils.BitmapAndStringChange;
import com.smart.fragment.utils.Const;
import com.smart.fragment.utils.ImageCompress;
import com.smart.fragment.utils.ImageUtils;
import com.smart.fragment.utils.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back_pre;
    private ImageView head;
    private LinearLayout pick_head, line_nick, line_signature, switch_sex;
    private TextView nickName, signature, sex, phone;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;

    String file = "";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    private String[] sexArry = new String[]{"男", "女"};// 性别选择

     Users users = new Users();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        /*
         *  透明状态栏 沉浸式
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initUi();

        initData();


    }

    /*
     *  activity 生命周期  返回此界面时刷新修改内容
     * */
    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /*
     *  初始化控件
     * */
    private void initUi() {
        back_pre = findViewById(R.id.back_pre);
        back_pre.setOnClickListener(this);

        pick_head = findViewById(R.id.pick_head);
        pick_head.setOnClickListener(this);

        line_nick = findViewById(R.id.line_nick);
        line_nick.setOnClickListener(this);

        head = findViewById(R.id.head);
        nickName = findViewById(R.id.nickName);
        signature = findViewById(R.id.signature);
        phone = findViewById(R.id.phone);

        line_signature = findViewById(R.id.line_signature);
        line_signature.setOnClickListener(this);

        switch_sex = findViewById(R.id.switch_sex);
        switch_sex.setOnClickListener(this);
        sex = findViewById(R.id.sex);
    }

    /*
     *  初始化用户，请求用户表信息 接口
     * */
    public  void initData() {
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
    public  void changeWithJSONObject(String jsonData) {
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
                    //Log.i("", "用户："+users);
                    phone.setText(users.getPhone());
                    nickName.setText(users.getNickName());
                    signature.setText(users.getSignature());
                    sex.setText(users.getSex());
                  /*  Glide.with(context)
                        .load(users.getHeadPic())
                        .into(head);*/
                    if (!users.getHeadPic().equals("")) {
                        Bitmap bitmap = BitmapAndStringChange.urlToBitmap(users.getHeadPic());// url (路径转成bitmap)
                        Log.i("", "用户头像：" + bitmap);
                        head.setImageBitmap(bitmap);
                    }

                    break;
            }

        }
    };

    /*
     *  打开相册  选择头像
     * */
    private void selectPic() {
        // 直接打开 手机图库 （相册）
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, 1);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         */
        Log.i("", "裁剪---" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.i("", "图片：---" + photo);
            Drawable drawable = new BitmapDrawable(photo);
            head.setImageDrawable(drawable);
        }
    }


    /**
     * 把用户选择的图片显示在imageview中
     * 返回的方法  对应 startActivityForResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

  /*      //用户操作完成，结果码返回是-1，即RESULT_OK
        switch (requestCode) {
            case 1:   // 打开相册
                startPhotoZoom(data.getData());
                break;
            case 2:  // 裁剪返回
                *//**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，小马只
                 * 在这个地方加下，大家可以根据不同情况在合适的
                 * 地方做判断处理类似情况
                 *
                 *//*
                Log.i("", "数据---" + data);
                if (null != data) {

                    setPicToView(data);
                }
                break;
            default:
                break;

        }*/
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            //获取选中文件的定位符
            Uri uri = data.getData();
            //Log.e("uri", uri.toString());
            //使用content的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //获取图片
               Bitmap  bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //bitmap = ImageCompress.zoomImage(bitmap,100,100);

                String path = ImageUtils.getRealPathFromUri(this, uri);// 通过uri获得图片路径

                // 获取旋转角度
                int degree = getDegree(path);

                bitmap = ImageCompress.getImage(path);  // 压缩 （先按比列压缩，再进行质量压缩）
                head.setImageBitmap(bitmap);
                file = BitmapAndStringChange.bitmapToString(bitmap); // bitmap 转  base64 字符串
                Log.e("file Base64:", file.toString());
               // Log.i("", "路径：：" + path);
                upload(file);

            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        } else {
            //操作错误或没有选择图片
            Log.i("MainActivtiy", "operation error");
        }
    }

    /*
    *  根据 图片path 动态获取 旋转角度
    * */
    public static int getDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.i("","readPictureDegree : orientation = " + orientation);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                degree = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                degree = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;

    }

    /*
     *  上传图片
     * */
    private void upload(final String path) {

  /*      File file = new File(path);
        Log.i("", "文件：" + file);

        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", path, image)
                .build();*/
        String url = Const.URL.URL_SUFFIX + "user/saveHead";
        RequestBody requestBody = new FormBody.Builder()
                .add("path", path)
                .build();

        OkHttpUtil.sendOkHttpResponse(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("MyInfoActivity", "上传失败！" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i("", "上传头像结果：" + data);
            }
        });

    }

    /*
     *  点击监听事件
     * */
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_pre:
                finish();
                break;
            case R.id.pick_head:
                selectPic();
                break;

            case R.id.line_nick:
                String name = nickName.getText().toString().trim();
                //界面跳转
                intent = new Intent(context, UpdateNickName.class);
                intent.putExtra("name", name);
                startActivity(intent);
                break;

            case R.id.line_signature:
                String mySignature = signature.getText().toString().trim();
                //界面跳转
                intent = new Intent(context, UpdateSignature.class);
                intent.putExtra("name", mySignature);
                startActivity(intent);
                break;
            case R.id.switch_sex:
                showSexChooseDialog();
                break;
            default:
                break;
        }
    }

    /*
     *   性别选择   弹出框
     * */
    private void showSexChooseDialog() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
        //builder3.setTitle("性别");
        TextView title = new TextView(this);
        title.setText("性别");
        title.setPadding(50, 16, 0, 0);
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        builder3.setCustomTitle(title);
        String mySex = sex.getText().toString().trim();
        int checkedItem = 5;

        if (mySex.equals("男")) {
            checkedItem = 0;
        } else if (mySex.equals("女")) {
            checkedItem = 1;
        }
        builder3.setSingleChoiceItems(sexArry, checkedItem, new DialogInterface.OnClickListener() {// 2默认的选中
            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                // showToast(which+"");
                sex.setText(sexArry[which]);
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
                //  保存数据  接口
                saveSex(sexArry[which]);
            }
        });

        builder3.show();// 让弹出框显示
    }

    /*
     *  保存 选择的性别
     * */
    private void saveSex(final String sex) {

        String url = Const.URL.URL_SUFFIX + "user/saveSex/" + sex;
        OkHttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("", "请求失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i("", "保存状态：" + data);
            }
        });


    }


}
