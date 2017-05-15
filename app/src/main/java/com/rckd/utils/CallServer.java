package com.rckd.utils;

import android.util.Log;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * Created by LiZheng on 2017/3/17 0017.
 */

public class CallServer {
//    private static CallServer instance;
    /**
     * 请求队列。
     */
    private RequestQueue requestQueue;
    private static String tag = CallServer.class.getName();

//    private int num;


    private CallServer() {
        // 默认并发值为3
        // RequestQueue reqQueue = NoHttp.newRequestQueue(10);
        Log.e(tag,tag+ " CallServer ");
        requestQueue = NoHttp.newRequestQueue();
    }


    /**
     * 请求队列。
     */
    public static CallServer getInstance() {

        Log.e(tag, tag+" CallServer getInstance() ");
        return CallServerHoler.INSTANCE;
    }

    private static class CallServerHoler {
        //默认三个
        private static final CallServer INSTANCE = new CallServer();
    }


    /**
     * 添加一个请求到请求队列。
     *
     * @param what     用来标志请求, 当多个请求使用同一个Listener时, 在回调方法中会返回这个what。
     * @param request  请求对象。
     * @param listener 结果回调对象。
     */
    public <T> void add(int what, Request<T> request, OnResponseListener listener) {
        Log.e(tag, tag+" add "+what);
        requestQueue.add(what, request, listener);
    }

    /**
     * 取消这个sign标记的所有请求。
     *
     * @param sign 请求的取消标志。
     */
    public void cancelBySign(Object sign) {
        Log.e(tag, tag+" cancelBySign ");
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求。
     */
    public void cancelAll() {
        Log.e(tag, tag+" cancelAll");
        requestQueue.cancelAll();
    }





}
