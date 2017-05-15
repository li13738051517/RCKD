package com.rckd.utils;

import android.util.Log;

import timber.log.Timber;

/**
 * Created by LiZheng on 2017/3/15 0015.
 * 此处是奔溃报告，后期可结合此处以电子邮件形式发送或者上传到服务器
 */

public class CrashReportingTree  extends Timber.Tree {
    private String tag=CrashReportingTree.class.getName();
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }
        FakeCrashLibrary.log(priority, tag, message);
        if (t != null) {
            if (priority == Log.ERROR) {
                FakeCrashLibrary.logError(t);
            } else if (priority == Log.WARN) {
                FakeCrashLibrary.logWarning(t);
            }
        }
    }
}
