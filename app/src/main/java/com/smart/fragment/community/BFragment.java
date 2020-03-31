package com.smart.fragment.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smart.fragment.R;
import com.smart.fragment.base.BaseFragment;
import com.smart.fragment.community.activity.PublishActicity;


public class BFragment extends BaseFragment implements View.OnClickListener {

    private ImageView add;

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

        setClick(view);


        return view;
    }

    /*
     *  点击事件方监听
     * */
    public void setClick(View view) {
        add = view.findViewById(R.id.add);
        add.setOnClickListener(this);

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
