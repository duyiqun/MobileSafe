package com.qun.mobilesafe.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Qun on 2017/5/31.
 */

public class HttpUtil {

    /**
     * get请求
     * @param url
     * @return
     * @throws IOException
     */
    public static Response httpGet(String url) throws IOException {

        // 01. 定义okhttp
        OkHttpClient okHttpClient_get = new OkHttpClient();
        // 02.请求体
        Request request = new Request.Builder().get()//get请求方式
                .url(url)//网址
                .build();
        // 03.执行okhttp
        Response response = okHttpClient_get.newCall(request).execute();
        return response;
    }
}
