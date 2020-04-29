package com.smart.fragment.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/*
 *   退出系统  两次返回
 * */
public class ExitApplication extends Application {

    private List<Activity> list = new ArrayList<Activity>();

    private static ExitApplication ea;

    public ExitApplication() {

    }
    //单例模式获取唯一的 ExitApplication
    public static ExitApplication getInstance() {
        if (null == ea) {
            ea = new ExitApplication();
        }
        return ea;
    }

    //添加activity到容器中
    public void addActivity(Activity activity) {
        list.add(activity);
    }

    //遍历所有的Activiy并finish
    public void exit(Context context) {
        for (Activity activity : list) {
            activity.finish();
        }
        System.exit(0);
    }




}
