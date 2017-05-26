package com.rckd.application;



import android.content.Context;
import android.content.SharedPreferences;

import com.rckd.utils.FileUtil;
import com.yanzhenjie.nohttp.tools.IOUtils;

import java.io.File;

/**
 * Created by LiZheng on 2017/3/16 0016.
 */
//AppConfig  code:1000-9999
public class AppConfig {
    public static final int WAIT_TIME = 1000;//请求等待时间
    public static final String HOME_URL = "http://www.rckd.net/";//本app的主页


    public  static final String HOME_MAIN="http://www.rckd.net/main.shtml"; //首页的链接地址
    private static AppConfig appConfig;
    private SharedPreferences preferences;
    /**
     * App根目录.
     */
    public String APP_PATH_ROOT;
    private String ShareFile="Rckd";
    private  String file_path="Nohttp_Rckd";

    private AppConfig() {
        preferences = BaseApplication.getmContext().getSharedPreferences(ShareFile, Context.MODE_PRIVATE);
        APP_PATH_ROOT = FileUtil.getRootPath(BaseApplication.getmContext()).getAbsolutePath() + File.separator +
                "NoHttpSample";
    }

    public static AppConfig getInstance() {
        if (appConfig == null)
            synchronized (AppConfig.class) {
                if (appConfig == null)
                    appConfig = new AppConfig();
            }
        return appConfig;
    }

    //import com.yanzhenjie.nohttp.tools.IOUtils;
    public void initialize() {
        IOUtils.createFolder(APP_PATH_ROOT);
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

}
