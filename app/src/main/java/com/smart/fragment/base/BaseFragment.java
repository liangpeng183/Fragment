package com.smart.fragment.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;


public class BaseFragment extends Fragment
{

    public  Context context;   // 上下文
    protected View view = null;

    private static final String TAG = "BaseFragment------>>>>";
    public BaseActivity activity;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        activity = (BaseActivity) getActivity();
        context = activity;
    }


    public void shortNumToast(int num) {
        shortToast(String.valueOf(num));
    }

    private void shortToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
