package com.smart.fragment.person.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;

import java.io.FileNotFoundException;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back_pre;
    private ImageView head;
    private LinearLayout pick_head, line_nick, line_signature;
    private TextView nickName, signature;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;


    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_RESULT_REQUEST = 0xa2;


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

        line_signature = findViewById(R.id.line_signature);
        line_signature.setOnClickListener(this);
    }

    /*
     *  打开相册  选择头像
     * */
    private void selectPic() {
      /*  //intent可以应用于广播和发起意图，其中属性有：ComponentName,action,data等
        Intent intent = new Intent();
        intent.setType("image/*");
        //action表示intent的类型，可以是查看、删除、发布或其他情况；我们选择ACTION_GET_CONTENT，系统可以根据Type类型来调用系统程序选择Type
        //类型的内容给你选择
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //如果第二个参数大于或等于0，那么当用户操作完成后会返回到本程序的onActivityResult方法
        startActivityForResult(intent, 1);  // 打开相册*/

        // 直接打开 手机图库 （相册）
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, 1);
    }

    /**
     * 把用户选择的图片显示在imageview中
     * 返回的方法  对应 startActivityForResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //用户操作完成，结果码返回是-1，即RESULT_OK

        if (resultCode == RESULT_OK) {
            //获取选中文件的定位符
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            //使用content的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //获取图片
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                head.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        } else {
            //操作错误或没有选择图片
            Log.i("MainActivtiy", "operation error");
        }

    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            head.setImageBitmap(photo);
        }
    }


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
            default:
                break;
        }
    }


}
