package com.smart.fragment.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.bean.ZhuSu;

import java.util.List;

public class ZsAdapter extends RecyclerView.Adapter<ZsAdapter.ViewHolder>{

    private List<ZhuSu> zsList;
    private Context context;
    Onclick aclick;

    public ZsAdapter(Context context, List<ZhuSu> zsList) {
        this.context = context;
        this.zsList = zsList;
    }

    public void setOnclick(Onclick aclick) {
        this.aclick = aclick;
    }
    public interface Onclick{
         void click(int position);
    }



    //在内部类中完成对控件的绑定
    public class ViewHolder extends RecyclerView.ViewHolder  {

        private ImageView imageView;
        private TextView tv_desc;
        private TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.jdPic_image);
            tv_desc = itemView.findViewById(R.id.jdDesc_tv);
            tv_name = itemView.findViewById(R.id.jdName_tv);

        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycleview_zs,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder ;
    }


     // 数据填充
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        //viewHolder.imageView.setImageResource(Integer.parseInt(jdList.get(position).ge()));
        viewHolder.tv_desc.setText(zsList.get(position).getName());
        //viewHolder.tv_name.setText(jdList.get(position).getJdName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aclick.click(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i("","数据项："+zsList.size());
        return zsList.size();
    }


}
