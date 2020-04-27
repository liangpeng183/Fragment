package com.smart.fragment.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.smart.fragment.R;
import com.smart.fragment.base.BaseFragment;
import com.smart.fragment.bean.Info;
import com.smart.fragment.community.activity.PublishActicity;
import com.smart.fragment.community.adapter.NineGridInfoAdapter;
import com.smart.fragment.community.adapter.NineGridTest2Adapter;
import com.smart.fragment.community.model.NineGridTestModel;
import com.smart.fragment.utils.CalTimeFarNow;
import com.smart.fragment.utils.Const;
import com.smart.fragment.utils.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BFragment extends BaseFragment implements View.OnClickListener {

    private ImageView add;

    // 自己做的部分
    private RecyclerView recycleview_info;

    private List<Info> infoList = new ArrayList<Info>();

    // 后改 （参考网上资料）
    private static final String ARG_LIST = "list";

    private ListView mListView; // listview
    private NineGridInfoAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NineGridTest2Adapter mRecycleAdapter;

    private List<NineGridTestModel> mList = new ArrayList<>();

    /**
     * 创建一个Fragment实例
     *
     * @return
     */
    public static BFragment createInstance() {
        return new BFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_b, null);

        initUi(view);

        //  数据刷新  ？？ 问题待解决
       /* if (mList.size() == 0 && infoList.size() == 0) {
            getAllInfo();
        }*/
        getAllInfo();


     /*   // 创建适配器  listview
        mAdapter = new NineGridInfoAdapter(context);
        mAdapter.setList(infoList, mList);  //当前list 只有图片
        //设置适配器
        mListView.setAdapter(mAdapter);*/

        // recycleview 适配器
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecycleAdapter = new NineGridTest2Adapter(context);
        mRecycleAdapter.setList(infoList,mList);
        mRecyclerView.setAdapter(mRecycleAdapter);


        return view;
    }

    /*
     *  绑定控件  点击事件方监听
     * */
    public void initUi(View view) {
        add = view.findViewById(R.id.add);
        add.setOnClickListener(this);

        // 后改
        mListView = view.findViewById(R.id.lv_bbs);

        mRecyclerView =  view.findViewById(R.id.recyclerView);


    }


    /*
     *   信息表 数据初始化   请求接口
     * */
    public void getAllInfo() {
        String url = Const.URL.URL_SUFFIX + "info/getAllInfo";
        OkHttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.i("BFragment", "info信息：" + data);
                dataChange(data);
            }
        });
    }

    // 数据格式转换   json
    public void dataChange(String data) {

        // fastjson  解析 json 数组
        try {
            JSONArray jsonArray = JSONObject.parseArray(data);
            for (int i = 0; i < jsonArray.size(); i++) {  // 数组循环
                Info info = new Info();
                JSONObject jsonObject = jsonArray.getJSONObject(i);  //  循环 获取每一个对象
                //Log.i("BFragment","内容：："+ jsonObject.getString("content"));
                info.setAuthor(jsonObject.getString("author"));
                info.setContent(jsonObject.getString("content"));

                String timeDiff = CalTimeFarNow.calTime(jsonObject.getString("time"));
                info.setTime(timeDiff);
                info.setHeadImg(String.valueOf(R.drawable.head));
                infoList.add(info);

                String pic = jsonObject.getString("pic");
                JSONArray picArrayy = JSONObject.parseArray(pic);
                //Log.i("BBBB","pic图片："+picArrayy);
                // 创建实例，
                NineGridTestModel model = new NineGridTestModel();
                for (int j = 0; j < picArrayy.size(); j++) {
                    //Log.i("BBBB","每个图片："+picArrayy.get(j));
                    // 一张图片
                    model.urlList.add((String) picArrayy.get(j));
                    //Log.i("BBBB","url形式："+ model.urlList);
                }
                mList.add(model);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                startActivity(new Intent(context, PublishActicity.class));
                break;
            default:
                break;
        }
    }


}
