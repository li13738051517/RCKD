package com.rckd.fragment.first.child;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.rckd.R;
import com.rckd.activity.MainActivity;
import com.rckd.activity.NewJobActivity;
import com.rckd.adpter.BaseAdapterQd;
import com.rckd.adpter.FirstHomeAdapter;
import com.rckd.bean.Article;
import com.rckd.bean.BaseIcon;
import com.rckd.event.TabSelectedEvent;
import com.rckd.helper.DetailTransition;
import com.rckd.inter.OnItemClickListener;
import com.rckd.loader.GlidImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.R.id.list;
import static com.baidu.location.h.j.G;
import static com.baidu.location.h.j.n;
import static com.rckd.R.id.grid_photo;
import static com.rckd.R.id.gv;

/**
 * Created by LiZheng on 16/6/5.
 */
public class FirstHomeFragment extends com.rckd.base.BaseFragment implements SwipeRefreshLayout.OnRefreshListener  /* , com.yanzhenjie.permission.PermissionListener  */{
    private static String tag= FirstHomeFragment.class.getName();//tag标记
    Banner banner;
    GridView gridView;//gv  1
    BaseAdapter mAdapterGv;//adpter 1
    ArrayList<BaseIcon> mData ;
    GridView gridView2;   // gv 2
    BaseAdapter  mAdapterGv2; // madapter 2
    ArrayList<BaseIcon> mData2 ;
//    private Toolbar mToolbar;
    private RecyclerView mRecy;
    private SwipeRefreshLayout mRefreshLayout;
//    private FloatingActionButton mFab;
    private FirstHomeAdapter mAdapter;
    private boolean mInAtTop = true;
    private int mScrollTotal;
    private String[] mTitles = new String[]{
            "Use imagery to express a distinctive voice and exemplify creative excellence.",
            "An image that tells a story is infinitely more interesting and informative.",
            "The most powerful iconic images consist of a few meaningful elements, with minimal distractions.",
            "Properly contextualized concepts convey your message and brand more effectively.",
            "Have an iconic point of focus in your imagery. Focus ranges from a single entity to an overarching composition."
    };

    private int[] mImgRes = new int[]{
            R.drawable.bg_first, R.drawable.bg_second, R.drawable.bg_third, R.drawable.bg_fourth, R.drawable.bg_fifth
    };

    public static FirstHomeFragment newInstance() {

        Bundle args = new Bundle();
        FirstHomeFragment fragment = new FirstHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    private static boolean flag = false; //flag标记
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
//   public X5WebView mWebView;
//    public ViewGroup mViewParent;
//    private URL mIntentUrl;
//    private ValueCallback<Uri> uploadFile;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //-----------------------------
        Timber.tag(tag);
        Timber.e(tag + " onCreateView start", tag);
        //Activity在onCreate时需要设置   getWindow().setFormat(PixelFormat.TRANSLUCENT);,避免闪频
//        baseActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        Intent intent = baseActivity.getIntent();
//        if (intent != null) {
//            try {
//                Timber.e(tag + " intent != null", tag);
//                mIntentUrl = new URL(intent.getData().toString());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    /*getWindow().addFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        View view = inflater.inflate(R.layout.zhihu_fragment_first_home_index, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }


    private void initView(View view) {
//        initImgData();
        banner = (Banner) view.findViewById(R.id.banner);
        //本地图片数据（资源文件）
        List<Integer> list=new ArrayList<>();
        list.add(R.mipmap.b1);
        list.add(R.mipmap.b2);
        list.add(R.mipmap.b3);
        list.add(R.mipmap.b1);
        list.add(R.mipmap.b2);
        list.add(R.mipmap.b3);
        banner.setImages(list)
                .setImageLoader(new GlidImageLoader())
                .start();


        gridView=(GridView)view.findViewById(R.id.gv);
        mData = new ArrayList<BaseIcon>();
        //此处添加数据 ,仅仅只是添加几张图片的视图,可以这样写
        mData.add(new BaseIcon(R.mipmap.zhaopin, "最新招聘"));
        mData.add(new BaseIcon(R.mipmap.qiuzhi, "最新求职"));
        mData.add(new BaseIcon(R.mipmap.fwzs, "我的匹配"));
        mData.add(new BaseIcon(R.mipmap.eszj, "本地客服"));
        mData.add(new BaseIcon(R.mipmap.sf, "扎工作"));
        mData.add(new BaseIcon(R.mipmap.jyzh, "找人才"));
        mData.add(new BaseIcon(R.mipmap.lsdg, "名企招聘"));
        mData.add(new BaseIcon(R.mipmap.jgyd, "置顶帖子"));

        mAdapterGv = new BaseAdapterQd<BaseIcon>(mData, R.layout.item_grid_icon) {
            @Override
            public void bindView(BaseAdapterQd.ViewHolder holder, BaseIcon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };
       gridView.setAdapter(mAdapterGv);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    //最新招聘
                    case 0:
                        startActivity(NewJobActivity.class);
                        break;


                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                }

            }
        });

        gridView2=(GridView)view.findViewById(R.id.gv2);
        mData2 = new ArrayList<BaseIcon>();
        //此处添加数据 ,仅仅只是添加几张图片的视图,可以这样写
        mData2.add(new BaseIcon(R.mipmap.jgyd, "匠工约定"));
        mData2.add(new BaseIcon(R.mipmap.sf, "顺风拼车"));
        mData2.add(new BaseIcon(R.mipmap.lsdg, "临时短工"));
        mData2.add(new BaseIcon(R.mipmap.jyzh, "交友征婚"));
        mData2.add(new BaseIcon(R.mipmap.dtqz, "打听求助"));
        mData2.add(new BaseIcon(R.mipmap.eszj, "二手之家"));
        mData2.add(new BaseIcon(R.mipmap.fwzs, "房屋出售"));
        mData2.add(new BaseIcon(R.mipmap.gegz, "广而告之"));

        mAdapterGv2 = new BaseAdapterQd<BaseIcon>(mData2, R.layout.item_grid_icon) {
            @Override
            public void bindView(BaseAdapterQd.ViewHolder holder, BaseIcon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };
        gridView2.setAdapter(mAdapterGv2);
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                }

            }
        });










//        mViewParent = (ViewGroup) view.findViewById(R.id.webView1);//去找这个
//        mViewParent.setVisibility(View.VISIBLE);
//        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_top);
        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
//        mFab = (FloatingActionButton) view.findViewById(R.id.fab);

//        mToolbar.setTitle("首页");
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setVisibility(View.GONE);

        mAdapter = new FirstHomeAdapter(baseActivity);
        final LinearLayoutManager manager = new LinearLayoutManager(baseActivity);
        mRecy.setLayoutManager(manager);
        mRecy.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                FirstDetailFragment fragment = FirstDetailFragment.newInstance(mAdapter.getItem(position));

                // 这里是使用SharedElement的用例
                // LOLLIPOP(5.0)系统的 SharedElement支持有 系统BUG， 这里判断大于 > LOLLIPOP
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    setExitTransition(new Fade());
                    fragment.setEnterTransition(new Fade());
                    fragment.setSharedElementReturnTransition(new DetailTransition());
                    fragment.setSharedElementEnterTransition(new DetailTransition());

                    // 25.1.0以下的support包,Material过渡动画只有在进栈时有,返回时没有;
                    // 25.1.0+的support包，SharedElement正常
                    fragment.transaction()
                            .addSharedElement(((FirstHomeAdapter.VH) vh).img, getString(R.string.image_transition))
                            .addSharedElement(((FirstHomeAdapter.VH) vh).tvTitle, "tv")
                            .commit();
                }
                start(fragment);
            }
        });





        // Init Datas
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int index = i % 5;
            Article article = new Article(mTitles[index], mImgRes[index]);
            articleList.add(article);
        }
        mAdapter.setDatas(articleList);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mInAtTop = true;
                } else {
                    mInAtTop = false;
                }
//                if (dy > 5) {
//                    mFab.hide();
//                } else if (dy < -5) {
////                    mFab.show();
//                    //因项目需要此处将其隐藏掉
//                    mFab.show();
//                }
            }
        });

//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(baseActivity, "Action", Toast.LENGTH_SHORT).show();
//
//            }
//        });

//        initWeb();

    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void scrollToTop() {
        mRecy.smoothScrollToPosition(0);
    }

    /**
     * 选择tab事件
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainActivity.FIRST) return;

        if (mInAtTop) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
        } else {
            scrollToTop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecy.setAdapter(null);
        EventBus.getDefault().unregister(this);
//        if (mWebView != null && mWebView.getParent() != null) {
//            Timber.e(tag + "  mWebView != null ", tag);
//            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
//            mWebView.destroy();
//            mWebView = null;
////            flag = false;
//            Timber.e(tag + " flag= " + baseActivity.flag, tag);
//        }
    }




//    private void initWeb() {
//        mWebView = new X5WebView(baseActivity, null);
//        Timber.e(tag + " Current SDK_INT: " + Build.VERSION.SDK_INT, tag);
//        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.FILL_PARENT,
//                FrameLayout.LayoutParams.FILL_PARENT));
////        initProgressBar();
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;
//            }
//
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                // TODO Auto-generated method stub
//                Log.e(tag, "request.getUrl().toString() is " + request.getUrl().toString());
//                return super.shouldInterceptRequest(view, request);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
//            }
//        });
//        mWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
//                return super.onJsConfirm(arg0, arg1, arg2, arg3);
//            }
//
//            View myVideoView;
//            View myNormalView;
//            IX5WebChromeClient.CustomViewCallback callback;
//
//            /**
//             * 全屏播放配置
//             */
//            //Android 4.4以上手机，由于厂商原因大部分不会进入该回调方法
//            @Override
//            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
//                FrameLayout normalView = (FrameLayout) view.findViewById(R.id.web_filechooser);
//                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
//                viewGroup.removeView(normalView);
//                viewGroup.addView(view);
//                myVideoView = view;
//                myNormalView = normalView;
//                callback = customViewCallback;
//            }
//
//            @Override
//            public void onHideCustomView() {
//                if (callback != null) {
//                    callback.onCustomViewHidden();
//                    callback = null;
//                }
//                if (myVideoView != null) {
//                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
//                    viewGroup.removeView(myVideoView);
//                    viewGroup.addView(myNormalView);
//                }
//            }
//
//            @Override
//            public boolean onShowFileChooser(WebView arg0,
//                                             ValueCallback<Uri[]> arg1, FileChooserParams arg2) {
//                // TODO Auto-generated method stub
//                Log.e(tag, tag + " onShowFileChooser");
//                return super.onShowFileChooser(arg0, arg1, arg2);
//            }
//
//            @Override
//            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String captureType) {
//
//                FirstHomeFragment.this.uploadFile = uploadFile;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                startActivityForResult(Intent.createChooser(i, "test"), 0);
//            }
//
//
//            @Override
//            public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
//                /**
//                 * 这里写入你自定义的window alert
//                 */
//                Log.e(tag, tag + " setX5webview = null");
//                return super.onJsAlert(null, "www.baidu.com", "aa", arg3);
//            }
//
//            /**
//             * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
//             */
//
//
//            @Override
//            public void onReceivedTitle(WebView arg0, final String arg1) {
//                super.onReceivedTitle(arg0, arg1);
//                Log.e(tag, tag + " webpage title is " + arg1);
//
//            }
//        });
//
//        mWebView.setDownloadListener(new DownloadListener() {
//
//            @Override
//            public void onDownloadStart(final String arg0, String arg1, String arg2,
//                                        String arg3, long arg4) {
//                TbsLog.e(tag, "url: " + arg0);
//
//
//                new AlertDialog.Builder(baseActivity)
//                        .setTitle("即将下载")
//                        .setPositiveButton("是",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        //自动升级下载
//                                      baseActivity.makeText("fake message: i'll download...");
////                                        downloadApkFile(arg0);
//                                    }
//                                })
//                        .setNegativeButton("否",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//
//                                        baseActivity.makeText("fake message: refuse download...");
//                                    }
//                                })
//                        .setOnCancelListener(
//                                new DialogInterface.OnCancelListener() {
//
//                                    @Override
//                                    public void onCancel(DialogInterface dialog) {
//
//                                        baseActivity.makeText("fake message: refuse download...");
//                                    }
//                                }).show();
//            }
//        });
//
//
////        if (baseActivity.isHaveAndPermission(baseActivity.strPression)) {
////            Timber.e(tag + " finish", tag);
////            initWeb();
////            flag = true;
//
//            WebSettings webSetting = mWebView.getSettings();
//            webSetting.setAllowFileAccess(true);
//            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//            webSetting.setSupportZoom(true);
//            webSetting.setBuiltInZoomControls(true);
//            webSetting.setUseWideViewPort(true);
//            webSetting.setSupportMultipleWindows(false);
//            //webSetting.setLoadWithOverviewMode(true);
//            webSetting.setAppCacheEnabled(true);
//            //webSetting.setDatabaseEnabled(true);
//
//            //地理位置权限，此处需要申请地理位置权限
//            webSetting.setDomStorageEnabled(true);
//            webSetting.setJavaScriptEnabled(true);
//            webSetting.setGeolocationEnabled(true);
//            webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//            webSetting.setAppCachePath(baseActivity.getDir("appcache", 0).getPath());
//            webSetting.setDatabasePath(baseActivity.getDir("databases", 0).getPath());
//            webSetting.setGeolocationDatabasePath(this.getBaseActivity().getDir("geolocation", 0).getPath());
//            // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
//            webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
//
//
////        } else {
////            Timber.e(tag + " 没有权限！！！  ");
////            baseActivity.getPression(baseActivity, REQUEST_CODE_PERMISSION_LOCATION, baseActivity.strPression);
////        }
//
//
//        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        // webSetting.setPreFectch(true);
//        long time = System.currentTimeMillis();
//        if (mIntentUrl == null) {
//            mWebView.loadUrl(AppConfig.HOME_MAIN);
//        } else {
//            mWebView.loadUrl(mIntentUrl.toString());
//        }
//        TbsLog.e(tag, "cost time: "
//                + (System.currentTimeMillis() - time));
//        CookieSyncManager.createInstance(baseActivity);
//        CookieSyncManager.getInstance().sync();
//    }



    //-----------------------------------------------权限问题




    //----------------------------------权限回调处理,因Android 6.0 系统  权限问题,因此,只有在某个界面需要使用时才能申请----------------------------------//
    //权限申请如果你继承的是android.app.Activity、android.app.Fragment、在6.0以下的手机是没有onRequestPermissionsResult()方法的，
    // 所以需要在申请权限前判断,手机会默认直接拥有各种权限,只需要在mf文件中申请即可,
    // 但对于国产自制权限手机,可能会出现bug
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // listener方式，最后一个参数是PermissionListener。
//        /**
//         * 转给AndPermission分析结果。
//         *
//         * @param requestCode  请求码。
//         * @param permissions  权限数组，一个或者多个。
//         * @param grantResults 请求结果。
//         * @param listener PermissionListener 对象。
//         */
//        //this实际上是 PermissionListener接口
//        Timber.e(tag + " onRequestPermissionsResult ", tag);
//        if (requestCode == 1 || requestCode == 2) {
//            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//        }
//        if (!baseActivity.flag) {
//            AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//        }
//
//
//    }
    //----------------------------------权限回调处理,因Android 6.0 系统  权限问题,因此,只有在某个界面需要使用时才能申请----------------------------------//


    //----------------------------------设置回调监听接口前谈对话框提醒用户----------------------------------//



    //----------------------------------回掉后返回结果，在结果中进行具体分析----------------------------------//
//    @Override
//    public void onSucceed(int requestCode, List<String> grantPermissions) {
//        switch (requestCode) {
////            case AppPressionCode.TenCentTbs:
////                makeText("权限申请成功");
////                Timber.e(tag + " onSucceed " + AppPressionCode.TenCentTbs, tag);
//////                flag=true;
////                break;
//
//            case REQUEST_CODE_PERMISSION_LOCATION:
//                baseActivity.makeText(tag + " 开始申请权限");
//                Timber.e(tag + " onSucceed " + " AppPressionCode.TenCentMap", tag);
////                initWeb();
//                baseActivity.flag = true;
//
//        }
//    }

//    @Override
//    public void onFailed(int requestCode, List<String> deniedPermissions) {
//        switch (requestCode) {
//            case REQUEST_CODE_PERMISSION_LOCATION:
//                Timber.e(tag + " onFailed " + " AppPressionCode.TenCentMap ", tag);
//                baseActivity.makeText(tag + " 您没有给相应的权限！！！我们可能无法提供更好的服务给你");
//                baseActivity.flag = false;
//                break;
//        }
//        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
//        if (AndPermission.hasAlwaysDeniedPermission(baseActivity, deniedPermissions)) {
//            // 第一种：用默认的提示语。
//            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
////            flag = false;
//        }
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //int requestCode, int resultCode, Intent data 注意三个参数
//        TbsLog.e(tag, "onActivityResult, requestCode:" + requestCode
//                + ",resultCode:" + resultCode);
//
//        switch (requestCode) {
//            case REQUEST_CODE_SETTING: {
////                Toast.makeText(this, "欢迎回来", Toast.LENGTH_LONG).show();
//                baseActivity.makeText("欢迎回来");
//                Timber.e(tag + " REQUEST_CODE_SETTING =" + REQUEST_CODE_SETTING, tag);
//                initWeb();
//                baseActivity.flag = true;
//                break;
//            }
//            default:
//                Timber.e(tag + " 即将退出  =" + REQUEST_CODE_SETTING, tag);
//                baseActivity.defaultFinish();
//                break;
//        }
//
//
//    }


    //增强体验感
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
}
