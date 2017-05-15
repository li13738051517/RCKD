package com.rckd.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;

import com.rckd.anim.FragmentAnimator;
import com.rckd.helper.AnimatorHelper;
import com.rckd.helper.FragmentationDelegate;
import com.rckd.helper.LifecycleHelper;
import com.rckd.helper.ResultRecord;
import com.rckd.helper.SupportTransaction;
import com.rckd.helper.TransactionRecord;
import com.rckd.inter.ISupportFragment;
import com.rckd.inter.OnFragmentDestoryViewListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;


/**
 * Created by LiZheng on 2017/3/18 0018.
 */

public class BaseFragment extends Fragment implements ISupportFragment  {
    // LaunchMode ，可以选择启动模式
    public static final int STANDARD = 0;
    public static final int SINGLETOP = 1;
    public static final int SINGLETASK = 2;
    // ResultCode ，根据请求吗
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    private static final int REQUEST_CODE_INVALID = BaseActivity.REQUEST_CODE_INVALID;

    //
    private static final long SHOW_SPACE = 200L;
    private static final long DEFAULT_ANIM_DURATION = 300L;
    //
    private Bundle mNewBundle;
    private boolean mIsRoot, mIsSharedElement;
    private boolean mIsHidden = true;   // 用于记录Fragment show/hide 状态
    private static final String tag = BaseFragment.class.getName();
    /**
     * Toolbar.
     */
    private Toolbar mToolbar;

    // SupportVisible相关
    private boolean mIsSupportVisible;
    private boolean mNeedDispatch = true;
    private boolean mInvisibleWhenLeave;
    private boolean mIsFirstVisible = true;
    private boolean mFixStatePagerAdapter;
    private Bundle mSaveInstanceState;

    //软键盘支持度
    private InputMethodManager mIMM;
    private boolean mNeedHideSoft;  // 隐藏软键盘
    protected BaseActivity baseActivity;//
    protected FragmentationDelegate mFragmentationDelegate;

    private int mContainerId;   // 该Fragment所处的Container的id

    private FragmentAnimator mFragmentAnimator;
    private AnimatorHelper mAnimHelper;
    public boolean mLocking; // 是否加锁 用于Fragmentation-SwipeBack库

    private OnFragmentDestoryViewListener mOnDestoryViewListener;

    private TransactionRecord mTransactionRecord;

    @IntDef({STANDARD, SINGLETOP, SINGLETASK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LaunchMode {
    }


    /**
     * @param context
     * @param fragmentClass
     * @param <T>
     * @return
     */
    public static <T extends BaseFragment> T instantiate(Context context, Class<T> fragmentClass) {
        //noinspection unchecked

        return (T) instantiate(context, fragmentClass.getName(), null);
    }

    /**
     * @param context
     * @param fragmentClass
     * @param bundle
     * @param <T>
     * @return
     */
    public static <T extends BaseFragment> T instantiate(Context context, Class<T> fragmentClass, Bundle bundle) {
        //noinspection unchecked
        return (T) instantiate(context, fragmentClass.getName(), bundle);
    }

    /**
     * Get BaseActivity.
     *
     * @return
     */
    protected final BaseActivity getBaseActivity() {
        return baseActivity;
    }

    /**
     * @param tClass clazz class for activity.
     * @param <T>    {@link Activity}.
     */
    protected final <T extends Activity> void startActivity(Class<T> tClass) {
        startActivity(new Intent(baseActivity, tClass));
    }

    /**
     * Start activity and finish my parent.
     *
     * @param clazz class for activity.
     * @param <T>   {@link Activity}.
     */
    protected final <T extends Activity> void startActivityFinish(Class<T> clazz) {
        startActivity(new Intent(baseActivity, clazz));
        baseActivity.finish();
    }

    //优先获取一个实例化
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    //此处可能还需要修改
    public void finish() {
        baseActivity.onBackPressed();
    }

    /**
     * Set Toolbar.
     *
     * @param toolbar {@link Toolbar}.
     */
    public final void setToolbar(@NonNull Toolbar toolbar) {
        this.mToolbar = toolbar;
        onCreateOptionsMenu(mToolbar.getMenu(), new SupportMenuInflater(baseActivity));
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
    }


    /**
     * Display home up button.
     *
     * @param drawableId drawable id.
     */
    public final void displayHomeAsUpEnabled(@DrawableRes int drawableId) {
        displayHomeAsUpEnabled(ContextCompat.getDrawable(baseActivity, drawableId));
    }

    /**
     * Display home up button.
     *
     * @param drawable {@link Drawable}.
     */
    public final void displayHomeAsUpEnabled(Drawable drawable) {
        mToolbar.setNavigationIcon(drawable);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onInterceptToolbarBack())
                    finish();
            }
        });
    }

    /**
     * Override this method, intercept backPressed of ToolBar.
     *
     * @return true, other wise false.
     */
    public boolean onInterceptToolbarBack() {
        return false;
    }

    /**
     * @return
     */
    protected final Toolbar getmToolbar() {
        return mToolbar;
    }

    /**
     * Set title.
     *
     * @param titleId string resource id from {@code string.xml}.
     */
    protected void setTitle(int titleId) {
        if (mToolbar != null)
            mToolbar.setTitle(titleId);
    }


    /**
     * Set sub title.
     *
     * @param title sub title.
     */
    protected void setSubtitle(CharSequence title) {
        if (mToolbar != null)
            mToolbar.setSubtitle(title);
    }

    /**
     * Set sub title.
     *
     * @param titleId string resource id from {@code string.xml}.
     */
    protected void setSubtitle(int titleId) {
        if (mToolbar != null)
            mToolbar.setSubtitle(titleId);
    }

    // ------------------------- Stack  以下部分为堆栈  以及悬浮穿做准备------------------------- //
    /**
     * Stack info.
     */
    private BaseActivity.FragmentStackEntity mStackEntity;

    /**
     * Set result.
     *
     * @param resultCode result code, one of {@link BaseFragment#RESULT_OK}, {@link BaseFragment#RESULT_CANCELED}.
     */
    protected final void setResult(int resultCode) {
        mStackEntity.resultCode = resultCode;
    }

    /**
     * Set result.
     *
     * @param resultCode resultCode, use {@link }.
     * @param result     {@link Bundle}.
     */
    protected final void setResult(int resultCode, @NonNull Bundle result) {
        mStackEntity.resultCode = resultCode;
        mStackEntity.result = result;
    }

    /**
     * Get the resultCode for requestCode.
     */
    final void setStackEntity(@NonNull BaseActivity.FragmentStackEntity stackEntity) {
        this.mStackEntity = stackEntity;
    }


    /**
     * Show a fragment.
     *
     * @param clazz fragment class.
     * @param <T>   {@link BaseFragment}.
     */
    public final <T extends BaseFragment> void startFragment(Class<T> clazz) {
        try {
            BaseFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, true, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param clazz       fragment class.
     * @param stickyStack sticky to back stack.
     * @param <T>         {@link BaseFragment}.
     */
    public final <T extends BaseFragment> void startFragment(Class<T> clazz, boolean stickyStack) {
        try {
            BaseFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, stickyStack, REQUEST_CODE_INVALID);
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
        startFragment(targetFragment, true, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param <T>            {@link BaseFragment}.
     */
    public final <T extends BaseFragment> void startFragment(T targetFragment, boolean stickyStack) {
        startFragment(targetFragment, stickyStack, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
     * @param <T>         {@link BaseFragment}.
     */
    public final <T extends BaseFragment> void startFragmentForResquest(Class<T> clazz, int requestCode) {
        try {
            BaseFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, true, requestCode);
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
    public final <T extends BaseFragment> void startFragmentForResquest(T targetFragment, int requestCode) {
        startFragment(targetFragment, true, requestCode);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param requestCode    requestCode.
     * @param <T>            {@link BaseFragment}.
     */
    private <T extends BaseFragment> void startFragment(T targetFragment, boolean stickyStack, int requestCode) {
        baseActivity.startFragment(this, targetFragment, stickyStack, requestCode);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.baseActivity = (BaseActivity) activity;
            mFragmentationDelegate = baseActivity.getFragmentationDelegate();
        } else {
            throw new RuntimeException(activity.toString() + "must extends BaseActivity!");
        }

        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONATTACH, null, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIsRoot = bundle.getBoolean(FragmentationDelegate.FRAGMENTATION_ARG_IS_ROOT, false);
            mIsSharedElement = bundle.getBoolean(FragmentationDelegate.FRAGMENTATION_ARG_IS_SHARED_ELEMENT, false);
            mContainerId = bundle.getInt(FragmentationDelegate.FRAGMENTATION_ARG_CONTAINER);
        }

        if (savedInstanceState == null) {
            mFragmentAnimator = onCreateFragmentAnimator();
            if (mFragmentAnimator == null) {
                mFragmentAnimator = baseActivity.getFragmentAnimator();
            }
        } else {
            mSaveInstanceState = savedInstanceState;
            mFragmentAnimator = savedInstanceState.getParcelable(FragmentationDelegate.FRAGMENTATION_STATE_SAVE_ANIMATOR);
            mIsHidden = savedInstanceState.getBoolean(FragmentationDelegate.FRAGMENTATION_STATE_SAVE_IS_HIDDEN);
            if (!mFixStatePagerAdapter) { // setUserVisibleHint() may be called before onCreate()
                mInvisibleWhenLeave = savedInstanceState.getBoolean(FragmentationDelegate.FRAGMENTATION_STATE_SAVE_IS_INVISIBLE_WHEN_LEAVE);
            }
            if (mContainerId == 0) { // After strong kill, mContianerId may not be correct restored.
                mIsRoot = savedInstanceState.getBoolean(FragmentationDelegate.FRAGMENTATION_ARG_IS_ROOT, false);
                mIsSharedElement = savedInstanceState.getBoolean(FragmentationDelegate.FRAGMENTATION_ARG_IS_SHARED_ELEMENT, false);
                mContainerId = savedInstanceState.getInt(FragmentationDelegate.FRAGMENTATION_ARG_CONTAINER);
            }
        }

        if (restoreInstanceState()) {
            // 解决重叠问题
            processRestoreInstanceState(savedInstanceState);
        }

        initAnim();

        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONCREATE, savedInstanceState, false);
    }


    private void processRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden()) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    /**
     * 内存重启后,是否让Fragmentation帮你恢复子Fragment状态
     */
    protected boolean restoreInstanceState() {
        return true;
    }

    private void initAnim() {
        mAnimHelper = new AnimatorHelper(baseActivity.getApplicationContext(), mFragmentAnimator);
        // 监听入栈动画结束(1.为了防抖动; 2.为了Fragmentation的回调所用)
        mAnimHelper.enterAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                baseActivity.setFragmentClickable(false);  // 开启防抖动
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                notifyEnterAnimEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (baseActivity.mPopMultipleNoAnim || mLocking) {
            if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE && enter) {
                return mAnimHelper.getNoneAnimFixed();
            }
            return mAnimHelper.getNoneAnim();
        }
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
            if (enter) {
                if (mIsRoot) return mAnimHelper.getNoneAnim();
                return mAnimHelper.enterAnim;
            } else {
                return mAnimHelper.popExitAnim;
            }
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
            return enter ? mAnimHelper.popEnterAnim : mAnimHelper.exitAnim;
        } else {
            if (mIsSharedElement && enter) notifyNoAnim();
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mIsRoot) {
            outState.putBoolean(FragmentationDelegate.FRAGMENTATION_ARG_IS_ROOT, true);
        }
        if (mIsSharedElement) {
            outState.putBoolean(FragmentationDelegate.FRAGMENTATION_ARG_IS_SHARED_ELEMENT, true);
        }
        outState.putInt(FragmentationDelegate.FRAGMENTATION_ARG_CONTAINER, mContainerId);
        outState.putParcelable(FragmentationDelegate.FRAGMENTATION_STATE_SAVE_ANIMATOR, mFragmentAnimator);
        outState.putBoolean(FragmentationDelegate.FRAGMENTATION_STATE_SAVE_IS_HIDDEN, isHidden());
        outState.putBoolean(FragmentationDelegate.FRAGMENTATION_STATE_SAVE_IS_INVISIBLE_WHEN_LEAVE, mInvisibleWhenLeave);

        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONSAVEINSTANCESTATE, outState, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        initFragmentBackground(view);
        // 防止某种情况 上一个Fragment仍可点击问题
        if (view != null) {
            view.setClickable(true);
        }

        if (savedInstanceState != null || mIsRoot || (getTag() != null && getTag().startsWith("android:switcher:"))) {
            notifyNoAnim();
        }

        if (!mInvisibleWhenLeave && !isHidden() && (getUserVisibleHint() || mFixStatePagerAdapter)) {
            if ((getParentFragment() != null && !getParentFragment().isHidden()) || getParentFragment() == null) {
                mNeedDispatch = false;
                dispatchSupportVisible(true);
            }
        }

        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONACTIVITYCREATED, savedInstanceState, false);
    }

    private void notifyNoAnim() {
        notifyEnterAnimationEnd(mSaveInstanceState);
        baseActivity.setFragmentClickable(true);
    }

    protected void initFragmentBackground(View view) {
        setBackground(view);
    }

    protected void setBackground(View view) {
        if (view != null && view.getBackground() == null) {
            int defaultBg = baseActivity.getDefaultFragmentBackground();
            if (defaultBg == 0) {
                int background = getWindowBackground();
                view.setBackgroundResource(background);
            } else {
                view.setBackgroundResource(defaultBg);
            }
        }
    }

    protected int getWindowBackground() {
        TypedArray a = baseActivity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();
        return background;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mIsFirstVisible) {
            if (!mIsSupportVisible && !mInvisibleWhenLeave && !isHidden() && getUserVisibleHint()) {
                mNeedDispatch = false;
                dispatchSupportVisible(true);
            }
        }

        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONRESUME, null, false);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mIsSupportVisible && !isHidden() && getUserVisibleHint()) {
            mNeedDispatch = false;
            mInvisibleWhenLeave = false;
            dispatchSupportVisible(false);
        } else {
            mInvisibleWhenLeave = true;
        }

        if (mNeedHideSoft) {
            hideSoftInput();
        }

        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONPAUSE, null, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isResumed()) {
            dispatchSupportVisible(!hidden);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            if (!mIsSupportVisible && isVisibleToUser) {
                dispatchSupportVisible(true);
            } else if (mIsSupportVisible && !isVisibleToUser) {
                dispatchSupportVisible(false);
            }
        } else if (isVisibleToUser) {
            mInvisibleWhenLeave = false;
            mFixStatePagerAdapter = true;
        }
    }

    /**
     * Called when the fragment is vivible.
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    public void onSupportVisible() {
    }

    /**
     * Called when the fragment is invivible.
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    public void onSupportInvisible() {
    }

    /**
     * Return true if the fragment has been supportVisible.
     */
    final public boolean isSupportVisible() {
        return mIsSupportVisible;
    }

    /**
     * Lazy initial，Called when fragment is first called.
     * <p>
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 入栈动画 结束时,回调
     */
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
    }

    private void dispatchSupportVisible(boolean visible) {
        mIsSupportVisible = visible;

        if (!mNeedDispatch) {
            mNeedDispatch = true;
        } else {
            FragmentManager fragmentManager = getChildFragmentManager();
            if (fragmentManager != null) {
                List<Fragment> childFragments = fragmentManager.getFragments();
                if (childFragments != null) {
                    for (Fragment child : childFragments) {
                        if (child instanceof BaseFragment && !child.isHidden() && child.getUserVisibleHint()) {
                            ((BaseFragment) child).dispatchSupportVisible(visible);
                        }
                    }
                }
            }
        }

        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onLazyInitView(mSaveInstanceState);
                dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONLAZYINITVIEW, null, false);
            }

            onSupportVisible();
            if (baseActivity != null) {
                baseActivity.setFragmentClickable(true);
            }
            dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONSUPPORTVISIBLE, null, true);
        } else {
            onSupportInvisible();
            dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONSUPPORTINVISIBLE, null, false);
        }
    }

    boolean isSupportHidden() {
        return mIsHidden;
    }

    /**
     * 获取该Fragment所在的容器id
     */
    public int getContainerId() {
        return mContainerId;
    }

    public long getEnterAnimDuration() {
        if (mIsRoot) {
            return 0;
        }
        if (mAnimHelper == null) {
            return DEFAULT_ANIM_DURATION;
        }
        return mAnimHelper.enterAnim.getDuration();
    }

    public long getExitAnimDuration() {
        if (mAnimHelper == null) {
            return DEFAULT_ANIM_DURATION;
        }
        return mAnimHelper.exitAnim.getDuration();
    }

    long getPopEnterAnimDuration() {
        if (mAnimHelper == null) {
            return DEFAULT_ANIM_DURATION;
        }
        return mAnimHelper.popEnterAnim.getDuration();
    }

    public long getPopExitAnimDuration() {
        if (mAnimHelper == null) {
            return DEFAULT_ANIM_DURATION;
        }
        return mAnimHelper.popExitAnim.getDuration();
    }

    private void notifyEnterAnimationEnd(final Bundle savedInstanceState) {
        baseActivity.getHandler().post(new Runnable() {
            @Override
            public void run() {
                onEnterAnimationEnd(savedInstanceState);
                dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONENTERANIMATIONEND, savedInstanceState, false);
            }
        });
    }

    /**
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    protected FragmentAnimator onCreateFragmentAnimator() {
        return baseActivity.getFragmentAnimator();
    }

    /**
     * (因为事务异步的原因) 如果你想在onCreateView/onActivityCreated中使用 start/pop 方法,请使用该方法把你的任务入队
     *
     * @param runnable 需要执行的任务
     */
    protected void enqueueAction(Runnable runnable) {
        baseActivity.getHandler().postDelayed(runnable, getEnterAnimDuration());
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        if (getView() != null) {
            initImm();
            mIMM.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected void showSoftInput(final View view) {
        if (view == null) return;
        initImm();
        view.requestFocus();
        mNeedHideSoft = true;
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIMM.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, SHOW_SPACE);
    }

    private void initImm() {
        if (mIMM == null) {
            mIMM = (InputMethodManager) baseActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    public boolean onBackPressedSupport() {
        return false;
    }

    /**
     * Add some action when calling start()/startXX()
     */
    public SupportTransaction transaction() {
        return new SupportTransaction.SupportTransactionImpl<>(this);
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    @Override
    public void loadRootFragment(int containerId, BaseFragment toFragment) {
        mFragmentationDelegate.loadRootTransaction(getChildFragmentManager(), containerId, toFragment);
    }

    /**
     * 以replace方式加载根Fragment
     */
    @Override
    public void replaceLoadRootFragment(int containerId, BaseFragment toFragment, boolean addToBack) {
        mFragmentationDelegate.replaceLoadRootTransaction(getChildFragmentManager(), containerId, toFragment, addToBack);
    }

    /**
     * 加载多个同级根Fragment
     *
     * @param containerId 容器id
     * @param toFragments 目标Fragments
     */
    @Override
    public void loadMultipleRootFragment(int containerId, int showPosition, BaseFragment... toFragments) {
        mFragmentationDelegate.loadMultipleRootTransaction(getChildFragmentManager(), containerId, showPosition, toFragments);
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
        mFragmentationDelegate.showHideFragment(getChildFragmentManager(), showFragment, hideFragment);
    }

    /**
     * 启动目标Fragment
     *
     * @param toFragment 目标Fragment
     */
    @Override
    public void start(BaseFragment toFragment) {
        start(toFragment, STANDARD);
    }

    @Override
    public void start(final BaseFragment toFragment, @LaunchMode final int launchMode) {
        mFragmentationDelegate.dispatchStartTransaction(getFragmentManager(), this, toFragment, 0, launchMode, FragmentationDelegate.TYPE_ADD);
    }

    @Override
    public void startForResult(BaseFragment toFragment, int requestCode) {
        mFragmentationDelegate.dispatchStartTransaction(getFragmentManager(), this, toFragment, requestCode, STANDARD, FragmentationDelegate.TYPE_ADD_RESULT);
    }

    @Override
    public void startWithPop(BaseFragment toFragment) {
        mFragmentationDelegate.dispatchStartTransaction(getFragmentManager(), this, toFragment, 0, STANDARD, FragmentationDelegate.TYPE_ADD_WITH_POP);
    }

    @Override
    public void replaceFragment(BaseFragment toFragment, boolean addToBack) {
        mFragmentationDelegate.replaceTransaction(this, toFragment, addToBack);
    }

    /**
     * @return 位于栈顶的Fragment
     */
    @Override
    public BaseFragment getTopFragment() {
        return mFragmentationDelegate.getTopFragment(getFragmentManager());
    }

    /**
     * @return 位于栈顶的子Fragment
     */
    @Override
    public BaseFragment getTopChildFragment() {
        return mFragmentationDelegate.getTopFragment(getChildFragmentManager());
    }

    /**
     * @return 位于当前Fragment的前一个Fragment
     */
    @Override
    public BaseFragment getPreFragment() {
        return mFragmentationDelegate.getPreFragment(this);
    }

    /**
     * @return 栈内fragmentClass的fragment对象
     */
    @Override
    public <T extends BaseFragment> T findFragment(Class<T> fragmentClass) {
        return mFragmentationDelegate.findStackFragment(fragmentClass, null, getFragmentManager());
    }

    @Override
    public <T extends BaseFragment> T findFragment(String fragmentTag) {
        FragmentationDelegate.checkNotNull(fragmentTag, "tag == null");
        return mFragmentationDelegate.findStackFragment(null, fragmentTag, getFragmentManager());
    }

    /**
     * @return 栈内fragmentClass的子fragment对象
     */
    @Override
    public <T extends BaseFragment> T findChildFragment(Class<T> fragmentClass) {
        return mFragmentationDelegate.findStackFragment(fragmentClass, null, getChildFragmentManager());
    }

    @Override
    public <T extends BaseFragment> T findChildFragment(String fragmentTag) {
        FragmentationDelegate.checkNotNull(fragmentTag, "tag == null");
        return mFragmentationDelegate.findStackFragment(null, fragmentTag, getChildFragmentManager());
    }

    /**
     * 出栈
     */
    @Override
    public void pop() {
        mFragmentationDelegate.back(getFragmentManager());
    }

    /**
     * 子栈内 出栈
     */
    @Override
    public void popChild() {
        mFragmentationDelegate.back(getChildFragmentManager());
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
        mFragmentationDelegate.popTo(targetFragmentTag, includeTargetFragment, afterPopTransactionRunnable, getFragmentManager());
    }

    /**
     * 子栈内
     */
    @Override
    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        popToChild(targetFragmentClass.getName(), includeTargetFragment);
    }

    @Override
    public void popToChild(String targetFragmentTag, boolean includeTargetFragment) {
        popToChild(targetFragmentTag, includeTargetFragment, null);
    }

    @Override
    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        popTo(targetFragmentClass.getName(), includeTargetFragment, afterPopTransactionRunnable);
    }

    @Override
    public void popToChild(String targetFragmentTag, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mFragmentationDelegate.popTo(targetFragmentTag, includeTargetFragment, afterPopTransactionRunnable, getChildFragmentManager());
    }

    public void popForSwipeBack() {
        mLocking = true;
        mFragmentationDelegate.back(getFragmentManager());
        mLocking = false;
    }

    /**
     * 设置Result数据 (通过startForResult)
     *
     * @param resultCode resultCode
     * @param bundle     设置Result数据
     */
    public void setFragmentResult(int resultCode, Bundle bundle) {
        Bundle args = getArguments();
        if (args == null || !args.containsKey(FragmentationDelegate.FRAGMENTATION_ARG_RESULT_RECORD)) {
            return;
        }

        ResultRecord resultRecord = args.getParcelable(FragmentationDelegate.FRAGMENTATION_ARG_RESULT_RECORD);
        if (resultRecord != null) {
            resultRecord.resultCode = resultCode;
            resultRecord.resultBundle = bundle;
        }
    }

    /**
     * 接受Result数据 (通过startForResult的返回数据)
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        Result数据
     */
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     *
     * @param args 通过上个Fragment的putNewBundle(Bundle newBundle)时传递的数据
     */
    public void onNewBundle(Bundle args) {
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     */
    public void putNewBundle(Bundle newBundle) {
        this.mNewBundle = newBundle;
    }

    public Bundle getNewBundle() {
        return mNewBundle;
    }

    /**
     * 入场动画结束时,回调
     */
    void notifyEnterAnimEnd() {
        notifyEnterAnimationEnd(null);
        baseActivity.setFragmentClickable(true);
    }

    public void setTransactionRecord(TransactionRecord record) {
        this.mTransactionRecord = record;
    }

    public TransactionRecord getTransactionRecord() {
        return mTransactionRecord;
    }

    public Bundle getSaveInstanceState() {
        return mSaveInstanceState;
    }

    /**
     * @see OnFragmentDestoryViewListener
     */
    public void setOnFragmentDestoryViewListener(OnFragmentDestoryViewListener listener) {
        this.mOnDestoryViewListener = listener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONVIEWCREATED, savedInstanceState, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONSTART, null, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONSTOP, null, false);
    }

    @Override
    public void onDestroyView() {
        baseActivity.setFragmentClickable(true);
        super.onDestroyView();
        if (mOnDestoryViewListener != null) {
            mOnDestoryViewListener.onDestoryView();
            mOnDestoryViewListener = null;
        }
        mIsFirstVisible = true;
        mFixStatePagerAdapter = false;
        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONDESTROYVIEW, null, false);
    }

    @Override
    public void onDestroy() {
        mFragmentationDelegate.handleResultRecord(this);
        super.onDestroy();
        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONDESTROY, null, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dispatchFragmentLifecycle(LifecycleHelper.LIFECYLCE_ONDETACH, null, false);
    }

    private void dispatchFragmentLifecycle(int lifecycle, Bundle bundle, boolean visible) {
        if (baseActivity == null) {
            return;
        }
        baseActivity.dispatchFragmentLifecycle(lifecycle, BaseFragment.this, bundle, visible);
    }
}
