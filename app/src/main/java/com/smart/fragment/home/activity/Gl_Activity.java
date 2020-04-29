package com.smart.fragment.home.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.bean.GongLue;
import com.smart.fragment.home.adapter.GlAdapter;

import java.util.ArrayList;
import java.util.List;

public class Gl_Activity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<GongLue>  glList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl);

        initUi();
        initList();
        Log.i("攻略list集合：", glList.toString());

        //创建LinearLayoutManager，用于决定RecyclerView的布局方式
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        //创建适配器
        GlAdapter glAdapter = new GlAdapter(context,glList);
        recyclerView.setAdapter(glAdapter);
       /* glAdapter.setOnclick(new GlAdapter.Onclick() {
            @Override
            public void click(int position) {
                Log.i("","当前景点："+glList.get(position));
                Toast.makeText(context, "当前："+jdList.get(position).getJdName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,ShowJdDetail.class);
                intent.putExtra("name",jdList.get(position).getJdName());
                intent.putExtra("jd_data",jdList.get(position));
                startActivity(intent);
            }
        });*/

    }


    /*
     *  获取数据
     * */
    private void initList() {

    }

    /*
     *  初始化 UI控件
     * */
    private void initUi() {
        recyclerView = findViewById(R.id.recycleview_gl);
    }


}
