package com.smart.fragment.utils;

/*
*   系统 常量
* */
public  class Const {


    public static class URL{
         public static String URL_SUFFIX = "http://192.168.0.103:8001/";   // 请求 url 前缀
        //String URL_SUFFIX = "http://47.108.180.195:8001/";   // 请求 url 前缀


        String URL_LOGIN ="login";  // 登录
    }

    public static class Config{
        public static boolean debug = false;
        public static boolean is_login = true;


        public static String loginInterceptorInvoker = "hh";
    }

}
