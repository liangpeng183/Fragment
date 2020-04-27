package com.smart.fragment.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/*
 *   okhttp3
 * */
public class OkHttpUtil {


    // get 请求
    /*
     *  url  请求地址
     *  callback  回调
     * */
    public static void sendOkHttpRequest(final String url,
                                         final Callback callback){
        /*
         *  设置响应时间
         * */
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000,TimeUnit.SECONDS)
                .readTimeout(2000, TimeUnit.SECONDS)
                .build();
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        //enqueue方法内部已经帮助我们开启好了线程，最终的结果会回调到callback中
        client.newCall(request).enqueue(callback);

    }


    // post 请求 提交数据到服务器
    public static void sendOkHttpResponse(final String url, final RequestBody requestBody,
                                          final Callback callback){
        /*
         *  设置响应时间
         * */
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000,TimeUnit.SECONDS)
                .readTimeout(2000, TimeUnit.SECONDS)
                .build();
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //enqueue方法内部已经帮助我们开启好了线程，最终的结果会回调到callback中
        client.newCall(request).enqueue(callback);
    }



}
