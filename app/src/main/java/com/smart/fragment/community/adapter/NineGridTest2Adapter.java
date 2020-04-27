package com.smart.fragment.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.bean.Info;
import com.smart.fragment.community.model.NineGridTestModel;
import com.smart.fragment.community.view.NineGridTestLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HMY on 2016/8/6
 */
public class NineGridTest2Adapter extends RecyclerView.Adapter<NineGridTest2Adapter.ViewHolder> {

    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;

    private List<Info> infoList = new ArrayList<>();

    public NineGridTest2Adapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<Info> infoList,List<NineGridTestModel> list) {
        this.mList = list;
        this.infoList = infoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.item_bbs_nine_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    // 数据填充
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.layout.setIsShowAll(mList.get(position).isShowAll);
        holder.layout.setUrlList(mList.get(position).urlList);

        holder.avater.setImageResource(Integer.parseInt(infoList.get(position).getHeadImg()));
        holder.user_nick.setText(infoList.get(position).getAuthor());
        holder.content.setText(infoList.get(position).getContent());
        holder.showTime.setText(infoList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    /*
    *  绑定控件
    * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;  // 展示图片
        ImageView avater; // 头像
        TextView user_nick, content, showTime;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);

            avater = itemView.findViewById(R.id.avatar);
            user_nick = itemView.findViewById(R.id.user_nick);
            content = itemView.findViewById(R.id.pub_content);
            showTime = itemView.findViewById(R.id.showTime);

        }
    }

    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
