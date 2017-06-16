package com.rckd.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.CheckResult;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.jaeger.library.StatusBarUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.rckd.R;
import com.rckd.anim.DefaultVerticalAnimator;
import com.rckd.anim.FragmentAnimator;
import com.rckd.application.BaseApplication;
import com.rckd.debug.StackViewTouchListener;
import com.rckd.helper.FragmentLifecycleCallbacks;
import com.rckd.helper.Fragmentation;
import com.rckd.helper.FragmentationDelegate;
import com.rckd.helper.LifecycleHelper;
import com.rckd.inter.HttpListener;
import com.rckd.inter.ISupport;
import com.rckd.utils.CallServer;
import com.rckd.utils.HttpResponseListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tencent.map.geolocation.TencentLocationManager;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Created by LiZheng on 2017/3/30 0055.
 *
 */
public abstract class BaseActivity extends AppCompatActivity implements com.rckd.inter.ISupport, SensorEventListener ,
        com.jph.takephoto.app.TakePhoto.TakeResultListener,
        com.jph.takephoto.permission.InvokeListener ,BDLocationListener {
    private Timer timer;
    private com.daimajia.numberprogressbar.NumberProgressBar bnp;
    protected FragmentationDelegate mFragmentationDelegate;
    protected LifecycleHelper mLifecycleHelper;
    protected ArrayList<FragmentLifecycleCallbacks> mFragmentLifecycleCallbacks;
    protected FragmentAnimator mFragmentAnimator;
    private int mDefaultFragmentBackground = 0;
    protected boolean mPopMultipleNoAnim = false;
    // 防抖动 是否可以点击
    protected boolean mFragmentClickable = true;
    protected Handler mHandler;
    protected SensorManager mSensorManager;
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    public static final int REQUEST_CODE_INVALID = -1;
    protected AtomicInteger mAtomicInteger = new AtomicInteger();
    protected List<BaseFragment> mFragmentStack = new ArrayList<>();
    private Map<BaseFragment, FragmentStackEntity> mFragmentEntityMap = new HashMap<>();
    private static String tag = BaseActivity.class.getName().toString();
    protected boolean interruptFlag = false;//应用开启后,判断基类onCreate是否中断
    /**
     * @link mContext = this.getApplicationContext();
     */
    protected Context mContext;
    protected View view;//此处应当是viewgroup
    //-------------- NoHttp -----------//
    /**
     * 用来标记取消。
     */
    private Object object = new Object(); //相当于Handler的msg.what
    protected CallServer callServer;


    /**
     * 请求队列。
     */
//    protected RequestQueue mQueue;

    protected static class FragmentStackEntity {
        private FragmentStackEntity() {
            Log.e(tag, tag + "class  FragmentStackEntity init ");
        }
        private boolean isSticky = false;
        private int requestCode = REQUEST_CODE_INVALID;
        int resultCode = RESULT_CANCELED;
        Bundle result = null;
    }
    public TencentLocationManager mLocationManager; //腾讯地图管理器
    public String[] strPression = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };


    public LocationClient mLocationClient; //百度地图


    //    protected abstract int getLayoutId();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//默认没有标题  Activity
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//默认没有标题  AppCompatActivity
        Timber.tag(tag);
        Timber.e(tag + " onCreate start", tag);
        if (interruptFlag) {
            Timber.e(tag + " onCreate interruptFlag ", tag);
            finish();
            return;
        }
        setContentView(R.layout.activity_base);

//        int layoutId = getLayoutId();
//        if (layoutId != 0) {
//            setContentView(layoutId);
//            // 删除窗口背景
//            getWindow().setBackgroundDrawable(null);
//        }
        //decorview实际上就是activity的外层容器，是一层framlayout
        mContext = this.getApplicationContext();
//        view = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
        //以下状态栏使用第三方
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#9900FF"));
// set a custom navigation bar resource
//        tintManager.setNavigationBarTintResource(R.drawable.my_tint);
// set a custom status bar drawable
//        tintManager.setStatusBarTintDrawable(MyDrawable);


        lifecycleSubject.onNext(ActivityEvent.CREATE);
        //添加actiity到集合中
        ((BaseApplication) this.getApplication()).addActivity(this);

        mFragmentationDelegate = getFragmentationDelegate();
        mFragmentAnimator = onCreateFragmentAnimator();
        initSensorManager();
        //设置
        setStatusBar();
        //设置

        bnp = (NumberProgressBar)findViewById(R.id.numberbar1);
        bnp.setOnProgressBarListener(new OnProgressBarListener() {
            @Override
            public void onProgressChange(int current, int max) {
                if(current == max) {
                    bnp.setProgress(0);
                }
            }
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bnp.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);
        //初始化相关资源
        initSDK();
        initData();
        initView();
        initListener();
        // 默认并发值为3
        // RequestQueue reqQueue = NoHttp.newRequestQueue();
        callServer = CallServer.getInstance();
        Timber.e(tag + " onCreate over", tag);
        //创建地图定位管理者,全局交由此管理
        mLocationManager = TencentLocationManager.getInstance(mContext);
        mLocationClient = new LocationClient(mContext);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    //懒人管理模式
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * https://github.com/laobie/StatusBarUtil
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
        Timber.e(tag + " onStart ", tag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        Timber.e(tag + " onResume ", tag);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.e(tag + " onPause", tag);
        lifecycleSubject.onNext(ActivityEvent.PAUSE);


    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.e(tag + " onStop", tag);
        lifecycleSubject.onNext(ActivityEvent.DESTROY);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Timber.e(tag + "  onRestart", tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.e(tag + " onDestroy start", tag);
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        // 在组件销毁的时候调用队列的按照sign取消的方法即可取消。
        callServer.cancelBySign(object);
        // 和声明周期绑定，退出时取消这个队列中的所有请求，当然可以在你想取消的时候取消也可以，不一定和声明周期绑定。
//        mQueue.cancelBySign(object);
//        // 因为回调函数持有了activity，所以退出activity时请停止队列。
//        mQueue.stop();
//        Timber.e(tag + "  onDestroy", tag);
        if (mFragmentLifecycleCallbacks != null) {
            mFragmentLifecycleCallbacks.clear();
        }
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        timer.cancel();
        mLocationClient.stop();

    }


//    //为手动调用做准备
//    protected void cancelAll() {
//        mQueue.cancelAll();
//    }

    //为手动调用做准备
//    protected void cancelBySign(Object object) {
//
//        mQueue.cancelBySign(object);
//    }


    /**
     * 获取全局的Context
     *
     * @return {@link #mContext = this.getApplicationContext();}
     */
    public Context getmContext() {
        Timber.e(tag + "getmContext()", tag);
        return mContext;
    }

    /**
     * 中断onCreate
     */
    public void interrupt() {
        Timber.e(tag + " interrupt", tag);
        this.interruptFlag = true;
    }

    /**
     * 初始化第三资源以及注册服务广播等
     */
    public void initSDK() {
        Timber.e(tag + " initSDK", tag);
    }


    /**
     * 初始化数据等
     */
    public void initData() {
        Timber.e(tag + " initData", tag);
    }


    /**
     * 默认退出
     */
    public void defaultFinish() {
        Timber.e(tag + " defaultFinish", tag);
        super.finish();
    }

    /**
     * 初始化布局等
     */
    public void initView() {
        Timber.e(tag + " initView ", tag);
    }


    ;;

    /**
     * 注册本地监听接口
     */
    public void initListener() {
        Timber.e(tag + "  initListener ", tag);
    }


    /**
     * 通过Class跳转界面,无需携带参数
     **/
    public void startActivity(Class<?> cls) {
        Timber.e(tag + " startActivity Class<?> cls", tag);
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Timber.e(tag + " startActivity Class<?> cls, Bundle bundle start", tag);
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            Timber.e(tag + " startActivity bundle != null", tag);
            intent.putExtras(bundle);
        }
        startActivity(intent);
        //跳转时的样式动画效果
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        Timber.e(tag + " startActivity Class<?> cls, Bundle bundle over", tag);
    }

    /**
     * 通过Action跳转界面
     **/
    public void startActivity(String action) {
        Timber.e(tag + " startActivity String action  start", tag);
        startActivity(action, null);
        Timber.e(tag + " startActivity String action  over", tag);
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    public void startActivity(String action, Bundle bundle) {
        Timber.e(tag + " startActivity String action, Bundle bundle start", tag);
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            Timber.e(tag + " startActivity String action, Bundle bundle  bundle != null", tag);
            intent.putExtras(bundle);
        }
        startActivity(intent);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        Timber.e(tag + " startActivity String action, Bundle bundle over", tag);
    }


    /**
     * 含有Bundle通过Class打开编辑界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Timber.e(tag + " startActivityForResult  Class<?> cls, Bundle bundle, int requestCode start", tag);
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            Timber.e(tag + " startActivityForResult  Class<?> cls, Bundle bundle, int requestCode bundle != null ", tag);
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        Timber.e(tag + " startActivityForResult  Class<?> cls, Bundle bundle, int requestCode over", tag);
    }

    /**
     * 带有右进右出动画的退出
     */
    @Override
    public void finish() {
        super.finish();
        Timber.e(tag + " finish ", tag);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//系统自带的淡入淡出特效,可以自行使用xml文件实现
    }

    /**
     * makeText ,在某些国产手机中可能有问题,可能需要重新改写
     *
     * @param text
     */
    public void makeText(String text) {
        Timber.e(tag + " makeText " + text, tag);
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }


    /**
     * 设置沉浸式状态栏
     *
     * @param linear_bar 自定义的状态栏
     */
    public void setStatusBar(Context mContext, final ViewGroup linear_bar) {
        Timber.e(tag + "  setStatusBar", tag);
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Timber.e(tag + "  Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ", tag);
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            final int statusHeight = getStatusBarHeight();
            linear_bar.post(new Runnable() {
                @Override
                public void run() {
                    Timber.e(tag + "  setStatusBar  run start ", tag);
                    int titleHeight = linear_bar.getHeight();
                    android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) linear_bar.getLayoutParams();
                    params.height = statusHeight + titleHeight;
                    linear_bar.setLayoutParams(params);
                    Timber.e(tag + "  setStatusBar  run over", tag);
                }
            });
        }
    }


    /**
     * 获取状态栏的高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        try {
            Timber.e(tag + " getStatusBarHeight", tag);
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 发起请求。
     *
     * @param what      what.
     * @param request   请求对象。
     * @param callback  回调函数。
     * @param canCancel 是否能被用户取消。
     * @param isLoading 实现显示加载框。
     * @param <T>       想请求到的数据类型。
     */
//    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean
//            isLoading) {
//        request.setCancelSign(object);
//        mQueue.add(what, request, new HttpResponseListener<>(this, request, callback, canCancel, isLoading));
//    }


    /**
     * 发起请求。
     *
     * @param what
     * @param request
     * @param listener
     * @param <T>
     * @see com.rckd.utils.CallServer
     */
    public <T> void request(int what, Request<T> request, OnResponseListener<T> listener) {
        // 这里设置一个sign给这个请求。
        request.setCancelSign(object);
        callServer.add(what, request, listener);
    }


    /**
     * 发起请求。
     *
     * @param what      what.
     * @param request   请求对象。
     * @param callback  回调函数。
     * @param canCancel 是否能被用户取消。
     * @param isLoading 实现显示加载框。
     * @param <T>       想请求到的数据类型。
     */
    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean
            isLoading) {
        request.setCancelSign(object);
//        mQueue.add(what, request, new HttpResponseListener<>(this, request, callback, canCancel, isLoading));
        callServer.add(what, request, new HttpResponseListener<>(this, request, callback, canCancel, isLoading));
    }


    //以下是  Fragment 开启窗

    /**
     * Show a fragment.
     *
     * @param clazz fragment class.
     */
    public final <T extends BaseFragment> void startFragment(Class<T> clazz) {
        try {
            BaseFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, true, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param clazz       fragment class.
     * @param stickyStack sticky to back stack.
     */
    public final <T extends BaseFragment> void startFragment(Class<T> clazz, boolean stickyStack) {
        try {
            BaseFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, stickyStack, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param <T>            {@link BaseFragment}.
     */
    public final <T extends BaseFragment> void startFragment(T targetFragment) {
        startFragment(null, targetFragment, true, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param <T>            {@link BaseFragment}.
     */
    public final <T extends BaseFragment> void startFragment(T targetFragment, boolean stickyStack) {
        startFragment(null, targetFragment, stickyStack, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
     * @param <T>         {@link BaseFragment}.
     */
    public <T extends BaseFragment> void startFragmentForResquest(Class<T> clazz, int requestCode) {
        if (requestCode == REQUEST_CODE_INVALID)
            throw new IllegalArgumentException("The requestCode must be positive integer.");
        try {
            BaseFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, true, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.
     * @param <T>            {@link BaseFragment}.
     */
    public <T extends BaseFragment> void startFragmentForResquest(T targetFragment, int requestCode) {
        if (requestCode == REQUEST_CODE_INVALID)
            throw new IllegalArgumentException("The requestCode must be positive integer.");
        startFragment(null, targetFragment, true, requestCode);
    }

    /**
     * Show a fragment.
     *
     * @param nowFragment    Now show fragment, can be null.
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param requestCode    requestCode.
     * @param <T>            {@link BaseFragment}.
     */
    public <T extends BaseFragment> void startFragment(T nowFragment, T targetFragment, boolean stickyStack, int
            requestCode) {
        FragmentStackEntity fragmentStackEntity = new FragmentStackEntity();
        fragmentStackEntity.isSticky = stickyStack;
        fragmentStackEntity.requestCode = requestCode;
        targetFragment.setStackEntity(fragmentStackEntity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (nowFragment != null) {
            if (mFragmentEntityMap.get(nowFragment).isSticky) {
                nowFragment.onPause();
                nowFragment.onStop();
                fragmentTransaction.hide(nowFragment);
            } else {
                fragmentTransaction.remove(nowFragment);
                fragmentTransaction.commit();
                mFragmentStack.remove(nowFragment);
                mFragmentEntityMap.remove(nowFragment);

                fragmentTransaction = fragmentManager.beginTransaction();
            }
        }

        String fragmentTag = targetFragment.getClass().getSimpleName() + mAtomicInteger.incrementAndGet();
        fragmentTransaction.add(fragmentLayoutId(), targetFragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commitAllowingStateLoss();

        mFragmentStack.add(targetFragment);
        mFragmentEntityMap.put(targetFragment, fragmentStackEntity);
    }

    /**
     * When the back off.
     */
    private boolean onBackStackFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mFragmentStack.size() > 1) {
            fragmentManager.popBackStack();
            BaseFragment inFragment = mFragmentStack.get(mFragmentStack.size() - 2);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.show(inFragment);
            fragmentTransaction.commit();

            BaseFragment outFragment = mFragmentStack.get(mFragmentStack.size() - 1);
            inFragment.onResume();

            FragmentStackEntity fragmentStackEntity = mFragmentEntityMap.get(outFragment);
            mFragmentStack.remove(outFragment);
            mFragmentEntityMap.remove(outFragment);

            if (fragmentStackEntity.requestCode != REQUEST_CODE_INVALID) {
                inFragment.onFragmentResult(fragmentStackEntity.requestCode, fragmentStackEntity.resultCode,
                        fragmentStackEntity.result);
            }
            return true;
        }
        return false;
    }


    /**
     * Should be returned to display fragments id of {@link ViewGroup}.
     *
     * @return resource id of {@link ViewGroup}.
     */
    protected abstract
    @IdRes
    int fragmentLayoutId();


    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }


    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }


    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback) {
        synchronized (this) {
            if (mFragmentLifecycleCallbacks == null) {
                mFragmentLifecycleCallbacks = new ArrayList<>();
                mLifecycleHelper = new LifecycleHelper(mFragmentLifecycleCallbacks);
            }
            mFragmentLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback) {
        synchronized (this) {
            if (mFragmentLifecycleCallbacks != null) {
                mFragmentLifecycleCallbacks.remove(callback);
            }
        }
    }

    FragmentationDelegate getFragmentationDelegate() {
        if (mFragmentationDelegate == null) {
            mFragmentationDelegate = new FragmentationDelegate(this);
        }
        return mFragmentationDelegate;
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();

        }
        return mHandler;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStackFloatingView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setStackFloatingView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setStackFloatingView();
    }



    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    public FragmentAnimator getFragmentAnimator() {
        return new FragmentAnimator(
                mFragmentAnimator.getEnter(), mFragmentAnimator.getExit(),
                mFragmentAnimator.getPopEnter(), mFragmentAnimator.getPopExit()
        );
    }

    /**
     * 设置全局动画, 一般情况建议复写onCreateFragmentAnimator()设置
     */
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        this.mFragmentAnimator = fragmentAnimator;
    }

    /**
     * 构建Fragment转场动画
     * <p/>
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultVerticalAnimator();
    }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * Fragmentation默认使用Theme的android:windowbackground作为Fragment的背景,
     * 可以通过该方法改变Fragment背景。
     */
    protected void setDefaultFragmentBackground(@DrawableRes int backgroundRes) {
        mDefaultFragmentBackground = backgroundRes;
    }

    /**
     * (因为事务异步的原因) 如果你想在onCreate()中使用start/pop等 Fragment事务方法, 请使用该方法把你的任务入队
     *
     * @param runnable 需要执行的任务
     */
    protected void enqueueAction(Runnable runnable) {
        getHandler().post(runnable);
    }

    /**
     * 不建议复写该方法,请使用 {@link #onBackPressedSupport} 代替
     */
    @Override
    final public void onBackPressed() {
        // 这里是防止动画过程中，按返回键取消加载Fragment
        if (!mFragmentClickable) {
            setFragmentClickable(true);
        }

        // 获取activeFragment:即从栈顶开始 状态为show的那个Fragment
        BaseFragment activeFragment = mFragmentationDelegate.getActiveFragment(null, getSupportFragmentManager());
        if (mFragmentationDelegate.dispatchBackPressedEvent(activeFragment)) return;

        onBackPressedSupport();

    }


    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    public void onBackPressedSupport() {
//        if (!onBackStackFragment()) {
//            finish();
//        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            finish();
        }
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    @Override
    public void loadRootFragment(int containerId, BaseFragment toFragment) {
        mFragmentationDelegate.loadRootTransaction(getSupportFragmentManager(), containerId, toFragment);
    }

    /**
     * 以replace方式加载根Fragment
     */
    @Override
    public void replaceLoadRootFragment(int containerId, BaseFragment toFragment, boolean addToBack) {
        mFragmentationDelegate.replaceLoadRootTransaction(getSupportFragmentManager(), containerId, toFragment, addToBack);
    }

    /**
     * 加载多个根Fragment
     *
     * @param containerId 容器id
     * @param toFragments 目标Fragments
     */
    @Override
    public void loadMultipleRootFragment(int containerId, int showPosition, BaseFragment... toFragments) {
        mFragmentationDelegate.loadMultipleRootTransaction(getSupportFragmentManager(), containerId, showPosition, toFragments);
    }

    /**
     * show一个Fragment,hide其他同栈所有Fragment
     * 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     * <p>
     * 建议使用更明确的{@link #showHideFragment(BaseFragment, BaseFragment)}
     *
     * @param showFragment 需要show的Fragment
     */
    @Override
    public void showHideFragment(BaseFragment showFragment) {
        showHideFragment(showFragment, null);
    }

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     *
     * @param showFragment 需要show的Fragment
     * @param hideFragment 需要hide的Fragment
     */
    @Override
    public void showHideFragment(BaseFragment showFragment, BaseFragment hideFragment) {
        mFragmentationDelegate.showHideFragment(getSupportFragmentManager(), showFragment, hideFragment);
    }

    /**
     * 启动目标Fragment
     *
     * @param toFragment 目标Fragment
     */
    @Override
    public void start(BaseFragment toFragment) {
        start(toFragment, BaseFragment.STANDARD);
    }

    @Override
    public void start(BaseFragment toFragment, @BaseFragment.LaunchMode int launchMode) {
        mFragmentationDelegate.dispatchStartTransaction(getSupportFragmentManager(), getTopFragment(), toFragment, 0, launchMode, FragmentationDelegate.TYPE_ADD);
    }

    @Override
    public void startForResult(BaseFragment toFragment, int requestCode) {
        mFragmentationDelegate.dispatchStartTransaction(getSupportFragmentManager(), getTopFragment(), toFragment, requestCode, BaseFragment.STANDARD, FragmentationDelegate.TYPE_ADD_RESULT);
    }

    @Override
    public void startWithPop(BaseFragment toFragment) {
        mFragmentationDelegate.dispatchStartTransaction(getSupportFragmentManager(), getTopFragment(), toFragment, 0, BaseFragment.STANDARD, FragmentationDelegate.TYPE_ADD_WITH_POP);
    }

    /**
     * 得到位于栈顶Fragment
     */
    @Override
    public BaseFragment getTopFragment() {
        return mFragmentationDelegate.getTopFragment(getSupportFragmentManager());
    }

    /**
     * 获取栈内的fragment对象
     */
    @Override
    public <T extends BaseFragment> T findFragment(Class<T> fragmentClass) {
        return mFragmentationDelegate.findStackFragment(fragmentClass, null, getSupportFragmentManager());
    }

    @Override
    public <T extends BaseFragment> T findFragment(String fragmentTag) {
        FragmentationDelegate.checkNotNull(fragmentTag, "tag == null");
        return mFragmentationDelegate.findStackFragment(null, fragmentTag, getSupportFragmentManager());
    }

    /**
     * 出栈
     */
    @Override
    public void pop() {
        mFragmentationDelegate.back(getSupportFragmentManager());
    }

    /**
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    @Override
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        popTo(targetFragmentClass.getName(), includeTargetFragment);
    }

    @Override
    public void popTo(String targetFragmentTag, boolean includeTargetFragment) {
        popTo(targetFragmentTag, includeTargetFragment, null);
    }

    /**
     * 用于出栈后,立刻进行FragmentTransaction操作
     */
    @Override
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        popTo(targetFragmentClass.getName(), includeTargetFragment, afterPopTransactionRunnable);
    }

    @Override
    public void popTo(String targetFragmentTag, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mFragmentationDelegate.popTo(targetFragmentTag, includeTargetFragment, afterPopTransactionRunnable, getSupportFragmentManager());
    }

    public void preparePopMultiple() {
        mPopMultipleNoAnim = true;
    }

    public void popFinish() {
        mPopMultipleNoAnim = false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // 这里是防止动画过程中，按返回键取消加载Fragment
            if (!mFragmentClickable) {
                setFragmentClickable(true);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 防抖动(防止点击速度过快)
        if (!mFragmentClickable) return true;

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 防抖动(防止点击速度过快)
     */
    void setFragmentClickable(boolean clickable) {
        mFragmentClickable = clickable;
    }

    public int getDefaultFragmentBackground() {
        return mDefaultFragmentBackground;
    }

    void dispatchFragmentLifecycle(int lifecycle, BaseFragment fragment, Bundle bundle, boolean visible) {
        if (mLifecycleHelper == null) return;
        mLifecycleHelper.dispatchLifecycle(lifecycle, fragment, bundle, visible);
    }

    /**
     * 显示栈视图dialog,调试时使用
     */
    public void showFragmentStackHierarchyView() {
        mFragmentationDelegate.showFragmentStackHierarchyView();
    }

    /**
     * 显示栈视图日志,调试时使用
     */
    public void logFragmentStackHierarchy(String TAG) {
        mFragmentationDelegate.logFragmentRecords(TAG);
    }

    private void setStackFloatingView() {
        if (Fragmentation.getDefault().getMode() != Fragmentation.BUBBLE) return;
        View root = findViewById(android.R.id.content);
        if (root instanceof FrameLayout) {
            FrameLayout content = (FrameLayout) root;
            final ImageView stackView = new ImageView(this);
            stackView.setImageResource(R.drawable.fragmentation_ic_stack);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            final int dp18 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
            params.topMargin = dp18 * 5;
            params.rightMargin = dp18;
            stackView.setLayoutParams(params);
            content.addView(stackView);
            stackView.setOnTouchListener(new StackViewTouchListener(stackView, dp18 / 4));
            stackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentStackHierarchyView();
                }
            });
        }
    }

    private void initSensorManager() {
        if (Fragmentation.getDefault().getMode() != Fragmentation.SHAKE) return;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            int value = 12;
            if ((Math.abs(values[0]) >= value || Math.abs(values[1]) >= value || Math.abs(values[2]) >= value)) {
                showFragmentStackHierarchyView();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    /**
     * 判断是否拥有特殊权限组,在Android6.0以下默认拥有
     *
     * @param permissions
     * @return
     */
   public boolean isHaveAndPermission(String... permissions) {
        Timber.e(tag + "  isFlag , String... permissions = " + permissions, tag);
        if (AndPermission.hasPermission(mContext, permissions)) {
            Timber.e(tag + " isFlag return  true ", tag);
            return true;
        }
        return false;
    }


    //警告在使用权限后,如果应用需要混淆,Listener接受回调结果，不用任何配置
    //注视方式,需要混淆混淆代码
    /**
     * -keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionYes <methods>;
    }
     -keepclassmembers class ** {
    @com.yanzhenjie.permission.PermissionNo <methods>;
    }
     */


    /**
     * @param activity
     * @param requestCode
     * @param permissions
     */
   public void getPression(Activity activity, int requestCode, String... permissions) {
        Timber.e(tag + " getPression , String... permissions = " + permissions, tag);
        AndPermission.with(activity)
                .requestCode(requestCode)
                .permission(permissions)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                .rationale(rationaleListener)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(mContext, rationale).show();
                    }
                })
                .send();
        Timber.e(tag + " requestPression over", tag);
    }



    //------------------------------------------------------------
    //以下是拍照相关内容 https://github.com/crazycodeboy/TakePhoto 根据这个提示进一步封装 ,改写,源代码可参考 lib
    public InvokeParam invokeParam;
    public TakePhoto takePhoto;
    @Override
    public void takeSuccess(TResult result) {
        Log.i(tag,"takeSuccess：" + result.getImage().getCompressPath());
    }
    @Override
    public void takeFail(TResult result,String msg) {
        Log.i(tag, "takeFail:" + msg);
    }
    @Override
    public void takeCancel() {
        Log.i(tag, getResources().getString(R.string.msg_operation_canceled));
    }
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }
    //权限问题,takephoto自行解决,无需关心内部实现问题
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        com.jph.takephoto.permission.PermissionManager.TPermissionType type= PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }


    //-----------------------------------------------------------


    public  static boolean flag=false;

//地理位置-------------------
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {
        Timber.e(tag + i + "  " + s, tag);
        String resText = "";

        if (i == 0) {
            resText = "不是wifi热点, wifi:" + s;
        } else if (i == 1) {
            resText = "是wifi热点, wifi:" + s;
        } else if (i == -1) {
            resText = "未连接wifi";
        }
        Timber.e(tag + " resText = ", tag);
    }
    //地理位置---------------------

    /**
     * 配置定位的参数
     */
    public void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);

    }


//    @Override
//    public void onClick(View v) {
//
//    }




    //---------------------------------请求吗

    public static final int REQUEST_CODE_PERMISSION_LOCATION = 100;//地图定位
    public static final int REQUEST_CODE_SETTING = 300;//系统设置权限码
    public static final int REQUEST_CODE_BAR_AD=301;//
    public static final int REQUEST_CODE_LOGIN=302;



    //--------------------------------------结果吗
    public  static  final int RESULT_CODE_CITY=800;
    public  static  final int RESULT_CODE_BAR_AD=801;
    public static final int  RESULT_CODE_LOGIN=802;



}