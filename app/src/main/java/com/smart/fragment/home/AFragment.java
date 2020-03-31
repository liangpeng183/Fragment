package com.smart.fragment.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.smart.fragment.R;
import com.smart.fragment.adapter.MyAdapter;
import com.smart.fragment.base.BaseFragment;
import com.smart.fragment.bean.JD;
import com.smart.fragment.home.activity.Gl_Activity;
import com.smart.fragment.home.activity.Jd_Activity;
import com.smart.fragment.home.activity.Ms_Activity;
import com.smart.fragment.home.activity.One_Info;
import com.smart.fragment.home.activity.Zs_Activity;
import com.smart.fragment.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class AFragment extends BaseFragment implements View.OnClickListener {

    private SwipeRefreshLayout refreshLayout;
    private SliderLayout sliderShow;

    private TextView tv;

    private LinearLayout gl, jd, ms, zs;

    private ListView listView;
    private ImageView img_jd, img_gl, img_ms, img_zs;

    private List<JD> jdList = new ArrayList<>();


    /**
     * 创建一个Fragment对象
     *
     * @return
     */
    public static AFragment createInstance() {
        return new AFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a, null);

        // 初始化控件
        initView(view);

        // 初始化图片滚播
        initSlider();

        // 下拉刷新控件
        initRefreshLayout(view);

        initJd();

        initData();

        // 设置圆形图片
        setPicCircle();

        return view;
    }

    // 设置圆形图片
    private void setPicCircle() {
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.jingdian);
        Bitmap bm1 = ImageLoader.getRoundBitmap(bitmap1);
        img_jd.setImageBitmap(bm1);

        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.gonglue);
        Bitmap bm2 = ImageLoader.getRoundBitmap(bitmap2);
        img_gl.setImageBitmap(bm2);

        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),
                R.drawable.meishi);
        Bitmap bm3 = ImageLoader.getRoundBitmap(bitmap3);
        img_ms.setImageBitmap(bm3);

        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(),
                R.drawable.zhusu);
        Bitmap bm4 = ImageLoader.getRoundBitmap(bitmap4);
        img_zs.setImageBitmap(bm4);

    }


    /*
     *  初始化 轮播
     * */
    private void initSlider() {
        final TextSliderView textSliderView = new TextSliderView(context);
        textSliderView
                .description("漓江河畔")
                .image("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2546878355,943257225&fm=26&gp=0.jpg");

        final TextSliderView textSliderView1 = new TextSliderView(context);
        textSliderView1
                .description("阳朔风光")
                .image("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3274299380,3205064876&fm=26&gp=0.jpg");

        final TextSliderView textSliderView2 = new TextSliderView(context);
        textSliderView2
                .description("两江四湖")
                .image("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=225869449,358583187&fm=26&gp=0.jpg");

        final TextSliderView textSliderView3 = new TextSliderView(context);
        textSliderView3
                .description("象山景色")
                .image("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3559345599,885132600&fm=26&gp=0.jpg");

        sliderShow.addSlider(textSliderView);
        sliderShow.addSlider(textSliderView3);
        sliderShow.addSlider(textSliderView1);
        sliderShow.addSlider(textSliderView2);

        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(context, "" + textSliderView.getDescription(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, One_Info.class);
                intent.putExtra("desc", textSliderView.getDescription());
                intent.putExtra("img", textSliderView.getUrl());
                startActivity(intent);
            }
        });
        textSliderView1.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(context, "" + textSliderView1.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(context, "" + textSliderView2.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
        textSliderView3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(context, "" + textSliderView3.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });

        // 转场效果
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        // 3秒切换
        sliderShow.setDuration(3000);
    }

    private void initJd() {

        for (int i = 0; i < 15; i++) {
            JD jd = new JD();
            int n = i + 1;
            jd.setId(String.valueOf(n));
            jd.setName("桂林" + n);
            jd.setDesc("山水甲天下" + n);
            jdList.add(jd);
        }

    }

    private void initData() {

        Log.i("list集合：", jdList.toString());
        final MyAdapter myAdapter = new MyAdapter(context, R.layout.listview, jdList);
        // 设置适配器
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JD jd = jdList.get(position);  // 获取点击位置
//                Log.i("item:","位置："+position);
                Toast.makeText(context, "当前位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     *    初始化控件
     * */
    private void initView(View view) {
        tv = view.findViewById(R.id.tv1);
        listView = view.findViewById(R.id.list_view);
        sliderShow = view.findViewById(R.id.slider);
        img_jd = view.findViewById(R.id.img_1);
        img_gl = view.findViewById(R.id.img_2);
        img_ms = view.findViewById(R.id.img_3);
        img_zs = view.findViewById(R.id.img_4);

        jd = view.findViewById(R.id.line_jd);
        gl = view.findViewById(R.id.line_gl);
        ms = view.findViewById(R.id.line_ms);
        zs = view.findViewById(R.id.line_zs);
        jd.setOnClickListener(this);
        gl.setOnClickListener(this);
        ms.setOnClickListener(this);
        zs.setOnClickListener(this);
    }

    // 下拉刷新控件
    private void initRefreshLayout(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(android.R.color.holo_red_light);
        refreshLayout.setDistanceToTriggerSync(100);
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);

        /*
         *  刷新动作
         * */
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {
                        tv.setTextColor(R.color.colorPrimary);
                        tv.setTextSize(20);
                        // 刷新完成
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);   // 2秒 刷新

            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
     *  点击监听事件
     * */
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.line_jd:
                intent = new Intent(context, Jd_Activity.class);
                startActivity(intent);
                break;
            case R.id.line_gl:
                intent = new Intent(context, Gl_Activity.class);
                startActivity(intent);
                break;
            case R.id.line_ms:
                intent = new Intent(context, Ms_Activity.class);
                startActivity(intent);
                break;
            case R.id.line_zs:
                intent = new Intent(context, Zs_Activity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
