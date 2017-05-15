package com.rckd.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.rckd.R;

/**
 * Created by LiZheng on 2017/3/27 0027.
 */

public class Info {
    protected Context context;
    protected PackageManager pm;

    public Info(Context context) {
        this.context = context;
        pm = context.getPackageManager();
    }

    /*
     * 获取程序 图标
     */
    @TargetApi(21)
    public Drawable getAppIcon(String packname) {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return context.getDrawable(R.mipmap.ic_launcher);
    }

    /*
     *获取程序的版本号
     */
    public String getAppVersion(String packname) {

        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0.0.1";
    }


    /*
     * 获取程序的名字
     */
    public String getAppName(String packname) {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return "";
    }

    /*
     * 获取程序的权限
     */
    public String[] getAppPremission(String packname) {
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            return packinfo.requestedPermissions;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return new String[0];
    }


    /*
     * 获取程序的签名
     */
    public String getAppSignature(String packname) {
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取到所有的权限
            return packinfo.signatures[0].toCharsString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * spp的信息
     * @return
     */
    public String getAppInfo() {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName + "   " + versionName + "  " + versionCode;
        } catch (Exception e) {
        }
        return null;
    }
    /**
     * 获取App安装包信息
     *
     * @return
     */
  public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

}
