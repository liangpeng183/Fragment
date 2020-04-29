package com.smart.fragment.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.bean.JD;

import java.util.ArrayList;
import java.util.List;

public class JdAdapter_Class extends RecyclerView.Adapter<JdAdapter_Class.ViewHolder>{

    private List<JD> jdList;
    private Context context;
    Onclick aclick;

    private List<JD> first = new ArrayList<>();


    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    public JdAdapter_Class(Context context, List<JD> jdList) {
        this.context = context;
        this.jdList = jdList;
        notifyDataSetChanged();

      /*  isClicks.set(0,true);
        isClicks.add(true);*/
        isClicks = new ArrayList<>();
        for(int i = 0;i<jdList.size();i++){
            isClicks.add(false);
        }
    }

    public void setOnclick(Onclick aclick) {
        this.aclick = aclick;
    }

    public void setFirst(List<JD> l) {
        this.first = l;
        Log.i("","第一个 左边："+l);
        isClicks.set(0,true);
    }

    public interface Onclick{
         void click(int position);
    }



    //在内部类中完成对控件的绑定
    public class ViewHolder extends RecyclerView.ViewHolder  {

        private TextView tv_name;
        private RelativeLayout relativeLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.layout);
            tv_name = itemView.findViewById(R.id.jd_class);

        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycleview_jd_class,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder ;
    }


     // 数据填充
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        viewHolder.tv_name.setText(jdList.get(position).getJdName());

        if(isClicks.get(0)){
            viewHolder.tv_name.setTextColor(Color.parseColor("#00a0e9"));
        }

        if(isClicks.get(position)){
            viewHolder.tv_name.setTextColor(Color.parseColor("#00a0e9"));
        }else{
            viewHolder.tv_name.setTextColor(Color.parseColor("#000000"));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aclick.click(position);
                //viewHolder.tv_name.setTextColor(Color.parseColor("#6699CC"));

                for(int i = 0; i <isClicks.size();i++){
                    isClicks.set(i,false);
                }
                isClicks.set(position,true);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i("","数据项："+jdList.size());
        return jdList.size();
    }


}
