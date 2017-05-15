package com.rckd.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rckd.application.AppConfig;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import com.rckd.R;

/**
 * Created by LiZheng on 2017/3/15 0015.
 * 在此Activity前已经加动画和因素引起动
 */
public class LoadingActivity extends BaseActivity {
    private static final String tag = LoadingActivity.class.getName().toString();
    private Handler handler = new Handler();
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(tag);
        Timber.e(tag + " initView start", tag);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);
        /**
         * 请勿删除build相关complier内容
         */
        ButterKnife.bind(this);
        try {
            Timber.e(tag + "  try catch start", tag);
            version.setText("V "
                    + this.getPackageManager().getPackageInfo(LoadingActivity.this.getPackageName(), 0).versionName);
            imageView.setVisibility(View.VISIBLE);
            Timber.e(tag + "  try catch over", tag);
        } catch (Exception e) {
        } finally {
            //        myHandler.postDelayed(new UpdateRunnable(), TIME);
            Timber.e(tag + "  LoadingActivity initListener finally", tag);
//            BaseApplication.getMainHandler().postDelayed(new UpdateRunnable(), AppConfig.WAIT_TIME);
            //
            handler.postDelayed(new UpdateRunnable(), AppConfig.WAIT_TIME);
        }
        //
        //使用网络仅仅是需要   callServer.add(NOHTTP_WHAT_TEST, request, onResponseListener);
    }

//    @Override
//    public void initSDK() {
//        super.initSDK();
//        Timber.e(tag + " initSDK start", tag);
//    }


//    @Override
//    public void initData() {
//        super.initData();
//        Timber.e(tag + " initData start", tag);
//    }

//    @Override
//    public void initView() {
//        super.initView();
////        Timber.tag(tag);
////        Timber.e(tag + " initView start", tag);
//
//    }


    /**
     * 升级apk的线程
     *
     * @author LiZheng
     */
    class UpdateRunnable implements Runnable {
        @Override
        public void run() {
            try {

                Log.e(tag, "LoadingActivity  UpdateRunnable");
                Timber.e(tag + "  LoadingActivity  UpdateRunnable", tag);
                //此处可以加载升级相关内容
//                jumpToWeb();
                jumpToMain();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 跳转到app界面
     */
    private void jumpToWeb() {
        Timber.e(tag + " LoadingActivity jumpToWeb ", tag);
        startActivity(BrowserActivity.class);
        finish();
    }


    private void jumpToMain() {
        Timber.e(tag + " LoadingActivity jumpToMain", tag);
        startActivity(MainActivity.class);
//        startActivity(TestActivityForSign.class);
        finish();

    }


    //

}