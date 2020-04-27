package com.smart.fragment.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.fragment.R;
import com.smart.fragment.bean.Info;
import com.smart.fragment.community.model.NineGridTestModel;
import com.smart.fragment.community.view.NineGridTestLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/13
 */
public class NineGridInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;

    private List<Info> infoList = new ArrayList<>();


    public NineGridInfoAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<Info> infoList, List<NineGridTestModel> list) {
        mList = list;
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return getListSize(mList);
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.item_bbs_nine_grid, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.layout.setIsShowAll(mList.get(position).isShowAll);
        holder.layout.setUrlList(mList.get(position).urlList);

        holder.avater.setImageResource(Integer.parseInt(infoList.get(position).getHeadImg()));
        holder.user_nick.setText(infoList.get(position).getAuthor());
        holder.content.setText(infoList.get(position).getContent());
        holder.showTime.setText(infoList.get(position).getTime());

        return convertView;
    }

    // 绑定控件
    private class ViewHolder {
        NineGridTestLayout layout; //展示图片
        ImageView avater; // 头像
        TextView user_nick, content, showTime;

        public ViewHolder(View view) {
            layout = view.findViewById(R.id.layout_nine_grid);
            avater = view.findViewById(R.id.avatar);
            user_nick = view.findViewById(R.id.user_nick);
            content = view.findViewById(R.id.pub_content);
            showTime = view.findViewById(R.id.showTime);
        }
    }

    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
