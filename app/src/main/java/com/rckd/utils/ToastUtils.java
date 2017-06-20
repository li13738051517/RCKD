package com.rckd.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by LiZheng on 2017/06/20.
 * 此类可以考虑扩展Toast相关问题!!!
 */
public class ToastUtils {

    public static void ToastMessage(Context context, String msg) {
       Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
