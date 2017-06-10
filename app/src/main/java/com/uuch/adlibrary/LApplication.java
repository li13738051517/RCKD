package com.uuch.adlibrary;

import android.app.Application;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.rckd.activity.MainActivity;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.utils.DisplayUtil;

import java.util.ArrayList;

/**
 * Created by aaron on 16/8/2.
 * 自定义Application 此类并没有使用 ,可以让其他的application继承此类
 */
public class LApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        initDisplayOpinion();
        Fresco.initialize(this);
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }
}
/**
使用细则
https://github.com/yipianfengye/android-adDialog
 1.  one
初始化数据
private void initData() {
    advList = new ArrayList<>();
    AdInfo adInfo = new AdInfo();
    adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png");
    advList.add(adInfo);

    adInfo = new AdInfo();
    adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage2.png");
    advList.add(adInfo);
}

 2  two
// 创建广告活动管理对象
AdManager adManager = new AdManager(MainActivity.this, advList);
adManager.setOverScreen(true)
 .setPageTransformer(new DepthPageTransformer());

 //执行弹窗的显示操作

 adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);


 */