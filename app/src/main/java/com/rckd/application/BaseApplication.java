package com.rckd.application;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.rckd.BuildConfig;
import com.rckd.base.BaseActivity;
import com.rckd.db.DBHelper;
import com.rckd.helper.Fragmentation;
import com.rckd.inter.ExceptionHandler;
import com.rckd.utils.CrashHandler;
import com.rckd.utils.CrashReportingTree;
import com.rckd.utils.LocationService;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;


/**
 * Created by LiZheng on 2017/3/15 0015.
 */


//https://github.com/litesuits/android-common    base_utils

public class BaseApplication extends android.app.Application {

    private static String tag = BaseApplication.class.getName().toString();
    private static Context context;
    private static Thread mainThread;//主线程
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//循环队列
    private static Handler mHandler;//主线程Handler
    //将所有Activity放在此集合中
    public static List<BaseActivity> activityList = new ArrayList<>();
    public static Boolean isLogin = false;//默认未登录,全局设置,无需放在共享文档中 ,登陆成功后同步cookies

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(tag, tag + " onCreate start");
        context = this.getApplicationContext();
        initLogDebug();
        initThread();
        initNoHttp();
//initAppIcon();
        initPicture();
        initTBS();
        //正式版本将其注释掉
        getStackBuilder();
        Log.e(tag, tag + " onCreate over");
        //收集奔溃信息
//        initCrash();
        //Install CustomActivityOnCrash
        CustomActivityOnCrash.install(this);
        initDB();
        /***
         * 初始化定位sdk，建议在Application中创建如百度地图,腾讯地图,高德地图, 国内google 服务并不稳定
         */

    }

    /**
     * 初始化网络Nottp
     * 需要以下权限，android 6.0以上请注意<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.INTERNET" />
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     */
    private void initNoHttp() {
        Timber.e(tag + " initNoHttp() start", tag);
        Logger.setDebug(BuildConfig.DEBUG);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("RCKDNoHttp");// 设置NoHttp打印Log的tag。
        // 一般情况下你只需要这样初始化：
//        NoHttp.initialize(this);
        NoHttp.initialize(this, new NoHttp.Config()
                        // 设置全局连接超时时间，单位毫秒，默认10s。
                        .setConnectTimeout(30 * 1000)
                        // 设置全局服务器响应超时时间，单位毫秒，默认10s。
                        .setReadTimeout(30 * 1000)
                        // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                        .setCacheStore(
                                new DBCacheStore(this).setEnable(true) // 如果不使用缓存，设置setEnable(false)禁用。
                        )
                        //如果你想缓存数据到SD卡，那么你需要考虑6.0及以上系统的运行时权限https://github.com/yanzhenjie/AndPermission
//                .setCacheStore(
//                        new DiskCacheStore(this) // 配置缓存到SD卡。
//                )
                        // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
                        .setCookieStore(
                                new DBCookieStore(this).setEnable(true) // 如果不维护cookie，设置false禁用
                        )
                        //  .setCookieStore(new DBCookieStore(this).setCookieStoreListener(mListener))
                        // 配置网络层，默认使用URLConnection，URLConnection Android 4.4,如果想用 OkHttp：OkHttpNetworkExecutor。
                        .setNetworkExecutor(new OkHttpNetworkExecutor())
        );
        // 如果你需要用OkHttp，请依赖下面的项目，version表示版本号：
        // compile 'com.yanzhenjie.nohttp:okhttp:1.1.1'
        // NoHttp详细使用文档：http://doc.nohttp.net
        Timber.e(tag + " initNoHttp() over", tag);
    }


    /**
     * 初始化第三方app Incon
     */
    private void initAppIcon() {
        Timber.e(tag + " initAppIcon start", tag);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Timber.e(tag + " initAppIcon over", tag);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Timber.e(tag + " onTerminate start", tag);
        exit();
        Timber.e(tag + " onTerminate over", tag);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Timber.e(tag + " onLowMemory ", tag);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // HOME键退出应用程序、长按MENU键，打开Recent TASK都会执行
        Timber.e(tag + " onTrimMemory ", tag);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.e(tag + " onConfigurationChanged ", tag);
    }

    /**
     * 初始化Log日志信息
     * 请勿删除timber
     * 请勿删除timber-lint
     * 请勿删除checkstyle.xml
     * 请勿删除deploy_javadoc.sh
     * 请勿删除相关配置信息
     */
    private void initLogDebug() {
        Log.e(tag, tag + " initLogDebug start");
        if (BuildConfig.DEBUG) {
            Log.e(tag, tag + " initLogDebug DEBUG");
            Timber.plant(new DebugTree());
        } else {
            Log.e(tag, tag + " initLogDebug not  DEBUG");
            Timber.plant(new CrashReportingTree());//存贮在当前log日志中,不需要保存在文件中
        }
//        Log.e(tag, tag + " initLogDebug over");
        Timber.tag(tag);
        Timber.e(tag + " initLogDebug over", tag);
    }

    /**
     * 初始化病获得线程相关信息
     */
    private void initThread() {
        Timber.e(tag + " initThread start", tag);
        mainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mMainLooper = getMainLooper();
        mHandler = new Handler();//可能会内存泄漏
        Timber.e(tag + " initThread over", tag);
    }

    /**
     * 初始化图片加载
     */
    private void initPicture() {
        //加载图片之前，你必须初始化Fresco类，多次初始化无效，Fresco，网络图片
        Timber.e(tag + "  initPicture start", tag);
        Fresco.initialize(this);
        Timber.e(tag + "  initPicture over", tag);
    }


    /**
     * 初始化腾讯浏览相关信息
     * tbs_sdk_thirdapp_v3.1.0.1034_43100_sharewithdownload_obfs_20170301_182143.jar包切勿删除
     * 本项目使用tencent的相关的jar
     * 需要以下权限
     * Manifest.permission.WRITE_EXTERNAL_STORAGE,
     * Manifest.permission.ACCESS_NETWORK_STATE,
     * Manifest.permission.ACCESS_WIFI_STATE,
     * Manifest.permission.INTERNET,
     * Manifest.permission.READ_PHONE_STATE
     */
    private void initTBS() {
        Timber.e(tag + "  initTBS  start", tag);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        TbsDownloader.needDownload(getApplicationContext(), true);
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.e(tag, "onDownloadFinish" + i);
                Timber.e(tag + " onDownloadFinish", tag);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.e(tag, "nInstallFinish" + i);
                Timber.e(tag + "  onInstallFinish", tag);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.e(tag, "onDownloadProgress" + i);
                Timber.e(tag + "  onDownloadProgress", tag);
            }
        });
//        QbSdk.initX5Environment(getApplicationContext(), null);
        QbSdk.initX5Environment(getApplicationContext(), callbackb);
        Timber.e(tag + " initTBS over ", tag);
    }


    /**
     * tbs_sdk_thirdapp_v3.1.0.1034_43100_sharewithdownload_obfs_20170301_182143.jar包切勿删除
     */
    QbSdk.PreInitCallback callbackb = new QbSdk.PreInitCallback() {
        @Override
        public void onViewInitFinished(boolean b) {

            Log.e(tag, " onViewInitFinished is " + b);
            Timber.e(tag + " onViewInitFinished", tag);
        }

        @Override
        public void onCoreInitFinished() {

            Log.e(tag, "onCoreInitFinished ");
            Timber.e(tag + " onCoreInitFinished", tag);
        }
    };

    /**
     * 添加Activity到ArrayList<Activity>管理集合
     *
     * @param activity
     */
    public void addActivity(BaseActivity activity) {
        Timber.e(tag + " addActivity start ", tag);
        String className = activity.getClass().getName();
        Timber.e(tag + " addActivity start " + className, tag);
        for (Activity at : activityList) {
            if (className.equals(at.getClass().getName())) {
                activityList.remove(at);
                break;
            }
        }
        activityList.add(activity);
        Timber.e(tag + " addActivity over ", tag);
    }


    /**
     * 退出应用程序
     */
    public void exit() {
        Timber.e(tag + " exit startt", tag);
        for (BaseActivity activity : activityList) {
            Timber.e(String.format(tag + " exit " + activity.toString()), tag);
//            activity.finish();
            activity.defaultFinish();
        }
        Timber.e(tag + " exit over", tag);
    }

    /**
     * 重启当前应用
     */
    public static void reStart() {
        Timber.e(tag + " restart   start", tag);
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        Timber.e(tag + " restart   over", tag);
    }


    /**
     * @return
     */
    public static Context getmContext() {
        Timber.e(tag + " getmContext()", tag);
        return context;
    }

    /**
     * @param mContext
     */
    public static void setContext(Context mContext) {
        Timber.e(tag + " setContext", tag);
        BaseApplication.context = mContext;
    }

    /**
     * @return
     */
    public static Thread getMainThread() {
        Timber.e(tag + " getMainThread", tag);
        return mainThread;
    }

    /**
     * @param mMainThread
     */
    public static void setMainThread(Thread mMainThread) {
        Timber.e(tag + " setMainThread ", tag);
        BaseApplication.mainThread = mMainThread;
    }

    /**
     * @return
     */
    public static long getMainThreadId() {
        Timber.e(tag + " getMainThreadId ", tag);
        return mMainThreadId;
    }

    /**
     * @param mMainThreadId
     */
    public static void setMainThreadId(long mMainThreadId) {
        Timber.e(tag + " setMainThreadId ", tag);
        BaseApplication.mMainThreadId = mMainThreadId;
    }

    /**
     * @return
     */
    public static Looper getMainThreadLooper() {
        Timber.e(tag + " getMainThreadLooper ", tag);
        return mMainLooper;
    }

    /**
     * @param mMainLooper
     */
    public static void setMainLooper(Looper mMainLooper) {
        Timber.e(tag + " setMainLooper ", tag);
        BaseApplication.mMainLooper = mMainLooper;
    }

    /**
     * @return
     */
    public static Handler getMainHandler() {
        Timber.e(tag + " getMainHandler ", tag);
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    /**
     * @param mHandler
     */
    public static void setMainHandler(Handler mHandler) {
        Timber.e(tag + " setMainHandler ", tag);
        BaseApplication.mHandler = mHandler;
    }


    /**
     * 获取栈视图,测试版本可以这样写
     */
    protected void getStackBuilder() {
        Timber.e(tag + " getStackBuilder start ", tag);
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                // ture时，遇到异常："Can not perform this action after onSaveInstanceState!"时，会抛出
                // false时，不会抛出，会捕获，可以在handleException()里监听到
                .debug(BuildConfig.DEBUG)
                // 线上环境时，可能会遇到上述异常，此时debug=false，不会抛出该异常（避免crash），会捕获
                // 建议在回调处上传至我们的Crash检测服务器
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
//                        Bugtags.sendException(e);
                        Timber.e(tag + " getStackBuilder onException() ,you need send Bugtags!!! ");
                    }
                })
                .install();
        Timber.e(tag + " getStackBuilder over ", tag);
        // init EventBus Index  建议配合build.gradle里apt{}开启
//        EventBus.builder()
//                .addIndex(new EventBusIndex())
//                .logNoSubscriberMessages(false)
//                .installDefaultEventBus();
    }

//    private DBCookieStore.CookieStoreListener mListener = new DBCookieStore.CookieStoreListener() {
//        @Override
//        public void onSaveCookie(URI uri, HttpCookie cookie) {
//            // 1. 判断这个被保存的Cookie是我们服务器下发的Session。
//            // 2. 这里的JSessionId是Session的name，
//            //    比如java的是JSessionId，PHP的是PSessionId，
//            //    当然这里只是举例，实际java中和php不一定是这个，具体要咨询你们服务器开发人员。
//            if ("JSessionId".equals(cookie.getName())) {
//                // 设置有效期为最大。
//                cookie.setMaxAge(HeaderUtil.getMaxExpiryMillis());
//            }
//        }
//
//        @Override
//        public void onRemoveCookie(URI uri, HttpCookie cookie) {
//
//        }
//    };


    public void initCrash() {
        //当程序发生Uncaught异常的时候,由该类来接管程序,一定要在这里初始化
        CrashHandler.getInstance().init(this);
    }


    /**
     * 初始化本地数据库
     * 先行创建一次本地数据库文件
     */
    private void initDB() {
        Timber.e(tag + " initDB start  ", tag);
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dbHelper.close();
        Timber.e(tag + " initDB over  ", tag);
    }

}



