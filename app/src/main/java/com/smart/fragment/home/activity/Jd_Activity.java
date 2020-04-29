package com.smart.fragment.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseActivity;
import com.smart.fragment.bean.JD;
import com.smart.fragment.home.adapter.JdAdapter_Class;
import com.smart.fragment.home.adapter.JdAdapter_Right;

import java.util.ArrayList;
import java.util.List;

public class Jd_Activity extends BaseActivity {

    private RecyclerView recyclerView, recyclerView_jdClass;
    private TextView tv_category;  // 类目控件
    private List<JD> jdList = new ArrayList<>();
    private List<JD> jdList1 = new ArrayList<>();

    private String category;

    JdAdapter_Right jdAdapter_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jd);


        initUi();
        initList();
        Log.i("景点list集合：", jdList.toString());

        //创建LinearLayoutManager，用于决定RecyclerView的布局方式
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        recyclerView_jdClass.setLayoutManager(linearLayoutManager1);

        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL)); // 分割线
        recyclerView_jdClass.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL)); // 分割线
/*        //创建适配器  景点信息展示
        JdAdapter_Right jdAdapter = new JdAdapter_Right(context, jdList1);
        recyclerView.setAdapter(jdAdapter);
        jdAdapter.setOnclick(new JdAdapter_Right.Onclick() {
            @Override
            public void click(int position) {
                Log.i("", "当前景点：" + jdList.get(position));
                Toast.makeText(context, "当前：" + jdList.get(position).getJdName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShowJdDetail.class);
                intent.putExtra("name", jdList.get(position).getJdName());
                intent.putExtra("jd_data", jdList.get(position));
                startActivity(intent);
            }
        });*/

        // 默认选中第一个
        List<JD> l = new ArrayList<>();
        JD j = new JD();
        j = jdList.get(0);  //默认显示第一个
        l.add(j);
        tv_category.setText(j.getJdName());
        jdAdapter_right = new JdAdapter_Right();  // 空的构造函数
        recyclerView.setAdapter(jdAdapter_right);  // 设置适配器
        jdAdapter_right.setFirst(l); // 设置数据



        //创建适配器   类别
        JdAdapter_Class jdAdapter1 = new JdAdapter_Class(context, jdList);
        recyclerView_jdClass.setAdapter(jdAdapter1);
        jdAdapter1.setFirst(l);
        jdAdapter1.setOnclick(new JdAdapter_Class.Onclick() {
            @Override
            public void click(int position) {
                Log.i("", "当前景点：" + jdList.get(position));
                category = jdList.get(position).getJdName();
                tv_category.setText(category);
                Toast.makeText(context, "当前：" + jdList.get(position).getJdName(), Toast.LENGTH_SHORT).show();
                // 通过name  去查询相关
                if (!jdList.get(position).getJdName().equals("")) {
                    jdList1.clear();
                    for (int i = 0; i < 8; i++) {
                        JD jd = new JD();
                        jd.setJdDesc("自行车v不能那么add给去微软推哦VS哥哥更为合适的公司的双方当事人基督教零三分纪录飞机的数量附件");
                        jd.setJdName("桂林 " + position + ":" + i);
                        jd.setImage(String.valueOf(R.drawable.guilin));
                        jdList1.add(jd);
                    }
                }

                //创建适配器  景点信息展示
                jdAdapter_right = new JdAdapter_Right(context, jdList1);
                recyclerView.setAdapter(jdAdapter_right);
                jdAdapter_right.setOnclick(new JdAdapter_Right.Onclick() {
                    @Override
                    public void click(int position) {
                        Log.i("", "当前景点：" + jdList.get(position));
                        Toast.makeText(context, "当前：" + jdList.get(position).getJdName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ShowJdDetail.class);
                        intent.putExtra("name", jdList.get(position).getJdName());
                        intent.putExtra("jd_data", jdList.get(position));
                        startActivity(intent);
                    }
                });
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
            jd.setJdName("桂林 " + i);
            jd.setImage(String.valueOf(R.drawable.guilin));
            jdList.add(jd);
        }
    }

    /*
     *  初始化 UI控件
     * */
    private void initUi() {
        recyclerView = findViewById(R.id.recycleview_jd_right);
        recyclerView_jdClass = findViewById(R.id.recycleview_jd_class);

        tv_category = findViewById(R.id.jd_category);
    }

}
