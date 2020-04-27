package com.smart.fragment.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.bean.JD;
import com.smart.fragment.home.adapter.JdAdapter;

import java.util.ArrayList;
import java.util.List;

public class Jd_Activity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<JD> jdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jd);


        initUi();
        initList();
        Log.i("list集合：", jdList.toString());

        //创建LinearLayoutManager，用于决定RecyclerView的布局方式
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        //创建适配器
        JdAdapter jdAdapter = new JdAdapter(context,jdList);
        recyclerView.setAdapter(jdAdapter);
        jdAdapter.setOnclick(new JdAdapter.Onclick() {
            @Override
            public void click(int position) {
                Log.i("","当前景点："+jdList.get(position));
                Toast.makeText(context, "当前："+jdList.get(position).getJdName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,ShowJdDetail.class);
                intent.putExtra("name",jdList.get(position).getJdName());
                intent.putExtra("jd_data",jdList.get(position));
                startActivity(intent);
            }
        });

    }


    /*
     *  获取数据
     * */
    private void initList() {
        for (int i = 0; i < 8; i++) {
            JD jd = new JD();
            jd.setJdDesc("自行车v不能那么add给去微软推哦VS哥哥更为合适的公司的双方当事人基督教零三分纪录飞机的数量附件");
            jd.setJdName("桂林 "+i);
            jd.setImage(String.valueOf(R.drawable.guilin));
            jdList.add(jd);
        }
    }

    /*
     *  初始化 UI控件
     * */
    private void initUi() {
        recyclerView = findViewById(R.id.recycleview);
    }
}
