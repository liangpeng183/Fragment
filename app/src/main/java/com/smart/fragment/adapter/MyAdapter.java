package com.smart.fragment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.bean.JD;

import java.util.List;

public class MyAdapter extends ArrayAdapter {

    private  int resource;

    private TextView jd_id;
    private TextView jd_name;
    private TextView jd_desc;

    public MyAdapter(@NonNull Context context, int view, List<JD> data) {
        super(context, view,data);
        resource = view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // Log.i("sfs","sfdsff");
        //获取当前项的Weather实例
        JD jd = (JD) getItem(position);
        //LayoutInflater的inflate()方法接收3个参数：需要实例化布局资源的id、ViewGroup类型视图组对象、false
        //false表示只让父布局中声明的layout属性生效，但不会为这个view添加父布局
        View view  = LayoutInflater.from(getContext()).inflate(resource,parent,false);
        jd_name = view.findViewById(R.id.jd_name);
        jd_id = view.findViewById(R.id.jd_id);
        jd_desc = view.findViewById(R.id.jd_desc);
        jd_name.setText(jd.getName());
        jd_id.setText(jd.getId());
        jd_desc.setText(jd.getDesc());
        return view;
    }


}
