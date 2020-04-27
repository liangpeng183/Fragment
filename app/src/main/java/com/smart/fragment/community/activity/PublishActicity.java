package com.smart.fragment.community.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;
import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.community.FullyGridLayoutManager;
import com.smart.fragment.community.adapter.GridImageAdapter;
import com.smart.fragment.utils.BitmapAndStringChange;
import com.smart.fragment.utils.Const;
import com.smart.fragment.utils.ImageCompress;
import com.smart.fragment.utils.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActicity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView back_pre;
    private Button publish;
    private EditText publish_content;

    private int maxSelectNum = 9; // 最大图片数量
    private RecyclerView mRecyclerView;
    private PopupWindow pop;
    private GridImageAdapter adapter;  // 适配器
    private List<LocalMedia> selectList = new ArrayList<>();  //集合  存放多张图片

    private Map<Integer, String> map = new HashMap<>();
    private boolean flag = false;

    // 用于保存图片资源文件
    private List<Bitmap> mList = new ArrayList<Bitmap>();
    // 用于保存图片路径
    private List<String> list_path = new ArrayList<>();

    private String imagePath;
    private Bitmap bm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        /*
         *  透明状态栏 沉浸式
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initUi();

        initWidget();

    }

    /*
     *  初始化ui
     * */
    private void initUi() {
        back_pre = findViewById(R.id.back_pre);
        back_pre.setOnClickListener(this);
        publish = findViewById(R.id.publish);
        publish.setOnClickListener(this);
        publish_content = findViewById(R.id.publish_content);

        mRecyclerView = findViewById(R.id.recycler);

    }


    private void initWidget() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);  // 设置数据
        adapter.setSelectMax(maxSelectNum);  // 图片最大数
        mRecyclerView.setAdapter(adapter);  // 设置适配器
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(PublishActicity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(PublishActicity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(PublishActicity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @SuppressLint("CheckResult")
        @Override
        public void onAddPicClick() {
            //获取写的权限
            RxPermissions rxPermission = new RxPermissions(PublishActicity.this);
            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.granted) {// 用户已经同意该权限
                                //第一种方式，弹出选择和拍照的dialog
                                showPop();

                                //第二种方式，直接进入相册，但是 是有拍照得按钮的
//                                showAlbum();
                            } else {
                                Toast.makeText(PublishActicity.this, "拒绝", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    private void showAlbum() {
        //参数很多，根据需要添加
        PictureSelector.create(PublishActicity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.selectionMedia(selectList)// 是否传入已选图片
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void showPop() {
        View bottomView = View.inflate(PublishActicity.this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
//                    case R.id.tv_album:
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(PublishActicity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)//最少选择
                                .imageSpanCount(4)//每行显示数量
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(PublishActicity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    /*
     *  回调
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;  // 图片集合
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调

                images = PictureSelector.obtainMultipleResult(data);
                selectList.addAll(images);

                //selectList = PictureSelector.obtainMultipleResult(data);

                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                adapter.setList(selectList);
                Log.i("", "集合：" + selectList);
                adapter.notifyDataSetChanged();  // 动态更新
            }
        }
    }


    /*
     *  点击监听
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_pre:
                this.finish();
                break;
            case R.id.publish:
                publish();
                break;
            default:
                break;
        }
    }

    /*
     *   封装 转换 数据（文件）格式
     * */
    private void publish() {
        //String jsonMap = doChanged();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path;
                String result = null; // 转成base64 结果
                if (selectList.size() != 0) {
                    for (int i = 0; i < selectList.size(); i++) {
                        list_path.add(selectList.get(i).getPath());
                        //Log.i("", "路径：" + selectList.get(i).getPath());
                    }
                }
                /*
                 *  list 集合 循环读取
                 * */
                if (list_path.size() != 0) {
                    for (int i = 0; i < list_path.size(); i++) {
                        path = list_path.get(i);
                        Bitmap bitmap = ImageCompress.getImage(path); // 按比列压缩  再进行质量压缩
                        result = BitmapAndStringChange.bitmapToString(bitmap);  // 转成base64 字符串
                        //result = BitmapAndStringChange.bitmapToString(path);
                        Log.i("", "base64：：" + i);
                        map.put(i, result);  // 添加到map 集合
                    }
                    upload(map);
                }
            }
        }).start();

    }
    //  上传  连接服务器
    private void upload(Map<Integer, String> map) {
        String content = publish_content.getText().toString().trim();
        String jsonMap = JSONObject.toJSONString(map);
        Log.i("", "图片map：" + jsonMap);
        // url
        String url = Const.URL.URL_SUFFIX + "info/save";
        RequestBody requestBody = new FormBody.Builder()
                .add("content", content)
                .add("imgMap", jsonMap)
                .build();
        OkHttpUtil.sendOkHttpResponse(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("", "上传失败" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 list_path = null;
                 String data = response.body().string();
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = data;

            }
        });
    }

    //  handler 处理
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                     if("ok".equals(msg.obj.toString())){  // 上传成功
                         PublishActicity.this.finish();
                     }
                     else {
                         Toast.makeText(context,"发布失败！",Toast.LENGTH_SHORT).show();
                     }
                    break;
            }

        }
    };

    /*
     *  图片转换
     * */
    public String doChanged() {

        String path;  // 路径url

        if (selectList.size() != 0) {
            for (int i = 0; i < selectList.size(); i++) {
                list_path.add(selectList.get(i).getPath());
                //Log.i("", "路径：" + selectList.get(i).getPath());
            }
        }
        /*
         *  list 集合 循环读取
         * */
        if (list_path.size() != 0) {
            for (int i = 0; i < list_path.size(); i++) {
                path = list_path.get(i);
                final String finalPath = path;

                final int finalI = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result;   // 转成base64 结果
                        result = BitmapAndStringChange.imageToBase64(finalPath);
                        Log.i("", "base64：：" + result);
                        map.put(finalI, result);  // 添加到map 集合
                    }
                }).start();
            }
        }

        return JSONObject.toJSONString(map);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }



}
