package com.rckd.inter;

import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by LiZheng on 2017/3/17 0017.
 * 接受回调结果
 */

public interface HttpListener<T>{
    void onSucceed(int what, Response<T> response);
    void onFailed(int what, Response<T> response);

}
