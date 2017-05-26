package com.rckd.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.litesuits.common.utils.AppUtil;
import com.rckd.R;
import com.rckd.application.AppConfig;
import com.rckd.base.BaseActivity;
import com.rckd.utils.X5WebView;
import com.rckd.view.DownloadingDialog;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.bingoogolapple.update.BGADownloadProgressEvent;
import cn.bingoogolapple.update.BGAUpgradeUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.functions.Action1;
import timber.log.Timber;

import static cn.bingoogolapple.update.BGAUpgradeUtil.installApk;


/**
 * Created by LiZheng on 2017/3/15 0015.
 */
//PermissionListener权限监听接口
public class BrowserActivity extends com.rckd.base.BaseActivity implements com.yanzhenjie.permission.PermissionListener, EasyPermissions.PermissionCallbacks {
//    private static final int REQUEST_CODE_SETTING = 300;
    //    private static final int REQUEST_CODE_PERMISSION_LOCATION = 100;
    private String mCheckUrl = "http://client.waimai.baidu.com/message/updatetag";
    private String mUpdateUrl = "http://mobile.ac.qq.com/qqcomic_android.apk";
    /**
     * 下载文件权限请求码
     */
    private static final int RC_PERMISSION_DOWNLOAD = 1;
    /**
     * 删除文件权限请求码
     */
    private static final int RC_PERMISSION_DELETE = 2;
    private DownloadingDialog mDownloadingDialog;
    private String mNewVersion = "1.0.1";
    private String mApkUrl = "http://7xk9dj.com1.z0.glb.clouddn.com/BGAUpdateSample_v1.0.1_debug.apk";

    private static boolean flag = false;
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;

    private ProgressBar mPageLoadingProgressBar = null;
    private ValueCallback<Uri> uploadFile;
    private URL mIntentUrl;
    private String tag = BrowserActivity.class.getName().toString();
//    private String[] strPression = {
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
////            Manifest.permission.INTERNET,
////            Manifest.permission.ACCESS_WIFI_STATE,
////            Manifest.permission.CHANGE_WIFI_STATE,
////            Manifest.permission.ACCESS_NETWORK_STATE,et
////            Manifest.permission.CHANGE_NETWORK_STATE,
//            Manifest.permission.READ_PHONE_STATE
//    };

//    protected  boolean isHaveAndPermission(String... permissions) {
//        Timber.e(tag + "  isFlag , String... permissions = " + permissions, tag);
//        if (AndPermission.hasPermission(BrowserActivity.this, permissions)) {
//            Timber.e(tag + " isFlag return  true ", tag);
//            return true;
//        }
//        return false;
//    }

//    private void getPression(String... permissions) {
//        Timber.e(tag + " getPression , String... permissions = " + permissions, tag);
//        AndPermission.with(BrowserActivity.this)
//                .requestCode(REQUEST_CODE_PERMISSION_LOCATION)
//                .permission(permissions
//                )
//                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
////                .rationale(rationaleListener)
//                .rationale(new RationaleListener() {
//                    @Override
//                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
//                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
//                        AndPermission.rationaleDialog(BrowserActivity.this, rationale).show();
//                    }
//                })
//                .send();
//        Timber.e(tag + " requestPression over", tag);
//    }


    @Override
    public void initView() {
        Timber.tag(tag);
        Timber.e(tag + " initView start", tag);
        //Activity在onCreate时需要设置   getWindow().setFormat(PixelFormat.TRANSLUCENT);,避免闪频
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        Intent intent = getIntent();
        if (intent != null) {
            try {
                Timber.e(tag + " intent != null", tag);
                mIntentUrl = new URL(intent.getData().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*getWindow().addFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.tbs_brower);
        mViewParent = (ViewGroup) findViewById(R.id.webView1);
//        handler.sendEmptyMessage(MessageHandler.MSG_INIT_UI);
        Timber.e(tag + " initView over", tag);
        if (isHaveAndPermission(strPression)) {
            Timber.e(tag + " finish", tag);
            initWeb();
            flag = true;
        } else {
            Timber.e(tag + " 没有权限！！！  ");
            getPression(BrowserActivity.this, REQUEST_CODE_PERMISSION_LOCATION, strPression);
        }
        // 监听下载进度
        // 监听下载进度
        BGAUpgradeUtil.getDownloadProgressEventObservable().compose(this.<BGADownloadProgressEvent>bindToLifecycle()).subscribe(new Action1<BGADownloadProgressEvent>() {
            @Override
            public void call(BGADownloadProgressEvent downloadProgressEvent) {
                if (mDownloadingDialog != null && mDownloadingDialog.isShowing() && downloadProgressEvent.isNotDownloadFinished()) {
                    mDownloadingDialog.setProgress(downloadProgressEvent.getProgress(), downloadProgressEvent.getTotal());
                }
            }


        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            Timber.e(tag + " OnResume ", tag);
            initWeb();
        }
    }

    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(com.rckd.R.id.progressBar1);// new
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(com.rckd.R.drawable.color_progress_bar));
    }


    private void initWeb() {
        mWebView = new X5WebView(this, null);
        Timber.e(tag + " Current SDK_INT: " + Build.VERSION.SDK_INT, tag);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));
        initProgressBar();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // TODO Auto-generated method stub
                Log.e(tag, "request.getUrl().toString() is " + request.getUrl().toString());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            /**
             * 全屏播放配置
             */
            //Android 4.4以上手机，由于厂商原因大部分不会进入该回调方法
            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
                FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView arg0,
                                             ValueCallback<Uri[]> arg1, FileChooserParams arg2) {
                // TODO Auto-generated method stub
                Log.e(tag, tag + " onShowFileChooser");
                return super.onShowFileChooser(arg0, arg1, arg2);
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String captureType) {

                BrowserActivity.this.uploadFile = uploadFile;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "test"), 0);
            }


            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                Log.e(tag, tag + " setX5webview = null");
                return super.onJsAlert(null, "www.baidu.com", "aa", arg3);
            }

            /**
             * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
             */


            @Override
            public void onReceivedTitle(WebView arg0, final String arg1) {
                super.onReceivedTitle(arg0, arg1);
                Log.e(tag, tag + " webpage title is " + arg1);

            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(final String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.e(tag, "url: " + arg0);


                new AlertDialog.Builder(BrowserActivity.this)
                        .setTitle("即将下载")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //自动升级下载
                                        makeText("fake message: i'll download...");
                                        downloadApkFile(arg0);
                                    }
                                })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        makeText("fake message: refuse download...");
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {

                                        makeText("fake message: refuse download...");
                                    }
                                }).show();
            }
        });


        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);

        //地理位置权限，此处需要申请地理位置权限
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            mWebView.loadUrl(AppConfig.HOME_URL);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }
        TbsLog.e(tag, "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Integer.parseInt(Build.VERSION.SDK) >= 16)
//                    changGoForwardButton(mWebView);
                    return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.e(tag + "  onDestroy ", tag);
        //for bug:Receiver not registered: android.widget.ZoomButtonsController crash android
        if (mWebView != null && mWebView.getParent() != null) {
            Timber.e(tag + "  mWebView != null ", tag);
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
//            flag = false;
            Timber.e(tag + " flag= " + flag, tag);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //int requestCode, int resultCode, Intent data 注意三个参数
        TbsLog.e(tag, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);

        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
//                Toast.makeText(this, "欢迎回来", Toast.LENGTH_LONG).show();
                makeText("欢迎回来");
                Timber.e(tag + " REQUEST_CODE_SETTING =" + REQUEST_CODE_SETTING, tag);
                initWeb();
                flag = true;
                break;
            }
            default:
                Timber.e(tag + " 即将退出  =" + REQUEST_CODE_SETTING, tag);
                defaultFinish();
                break;
        }


    }


    //----------------------------------权限回调处理,因Android 6.0 系统  权限问题,因此,只有在某个界面需要使用时才能申请----------------------------------//
    //权限申请如果你继承的是android.app.Activity、android.app.Fragment、在6.0以下的手机是没有onRequestPermissionsResult()方法的，
    // 所以需要在申请权限前判断,手机会默认直接拥有各种权限,只需要在mf文件中申请即可,
    // 但对于国产自制权限手机,可能会出现bug
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // listener方式，最后一个参数是PermissionListener。
        /**
         * 转给AndPermission分析结果。
         *
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         * @param listener PermissionListener 对象。
         */
        //this实际上是 PermissionListener接口
        Timber.e(tag + " onRequestPermissionsResult ", tag);
        if (requestCode == 1 || requestCode == 2) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
        if (!flag) {
            AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }


    }
    //----------------------------------权限回调处理,因Android 6.0 系统  权限问题,因此,只有在某个界面需要使用时才能申请----------------------------------//


    //----------------------------------设置回调监听接口前谈对话框提醒用户----------------------------------//
//    private RationaleListener rationaleListener = new RationaleListener() {
//        // 这里使用自定义对话框，如果不想自定义，用AndPermission默认对话框：
//        // AndPermission.rationaleDialog(Context, Rationale).show();
//        @Override
//        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
//            com.yanzhenjie.alertdialog.AlertDialog.build(BrowserActivity.this)
//                    .setTitle("友情提示")
//                    .setMessage("本app没有相关权限,可能不能给您提供更好的用户体验,是否立即去查看权限问题!!!")
//                    .setPositiveButton("好的,我同意", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Timber.e(tag + " setPositiveButton  onclik", tag);
//                            dialog.cancel();
//                            rationale.resume();
//                        }
//                    })
//                    .setNegativeButton("不,我拒绝!", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Timber.e(tag + " setNegativeButton  onclik", tag);
//                            dialog.cancel();
//                            rationale.cancel();
//                        }
//                    })
//                    .show();
//        }
//    };


    //----------------------------------回掉后返回结果，在结果中进行具体分析----------------------------------//
    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        switch (requestCode) {
//            case AppPressionCode.TenCentTbs:
//                makeText("权限申请成功");
//                Timber.e(tag + " onSucceed " + AppPressionCode.TenCentTbs, tag);
////                flag=true;
//                break;

            case REQUEST_CODE_PERMISSION_LOCATION:
                makeText(tag + " 开始申请权限");
                Timber.e(tag + " onSucceed " + " AppPressionCode.TenCentMap", tag);
                initWeb();
                flag = true;

        }
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {
//            case AppPressionCode.TenCentTbs:
//                makeText("您没有给相应的权限");
//                Timber.e(tag + " onFailed " + AppPressionCode.TenCentTbs, tag);
////                flag=false;
//                break;
            case REQUEST_CODE_PERMISSION_LOCATION:
                Timber.e(tag + " onFailed " + " AppPressionCode.TenCentMap ", tag);
                makeText(tag + " 您没有给相应的权限！！！我们可能无法提供更好的服务给你");
                flag = false;
                break;
        }
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(BrowserActivity.this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
            flag = false;

            // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingHandle = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingHandle.execute();
//            你的dialog点击了取消调用：
//            settingHandle.cancel();
        }
    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }


    /**
     * 显示下载对话框
     */
    private void showDownloadingDialog() {
        if (mDownloadingDialog == null) {
            mDownloadingDialog = new DownloadingDialog(this);
        }
        mDownloadingDialog.show();
    }


    /**
     * 隐藏下载对话框
     */
    private void dismissDownloadingDialog() {
        if (mDownloadingDialog != null) {
            mDownloadingDialog.dismiss();
        }
    }


    private void downloadApkFile(String apkUrl) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 如果新版 apk 文件已经下载过了，直接 return，此时不需要开发者调用安装 apk 文件的方法，在 isApkFileDownloaded 里已经调用了安装」
            //需要从远程上获取版本信息
            if (BGAUpgradeUtil.isApkFileDownloaded(mNewVersion)) {
                return;
            }
            // 下载新版 apk 文件
            BGAUpgradeUtil.downloadApkFile(apkUrl, mNewVersion)
                    .subscribe(new Subscriber<File>() {
                        @Override
                        public void onStart() {
                            showDownloadingDialog();
                        }

                        @Override
                        public void onCompleted() {
                            dismissDownloadingDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissDownloadingDialog();
                        }

                        @Override
                        public void onNext(File apkFile) {
                            if (apkFile != null) {
                                BGAUpgradeUtil.installApk(apkFile);
//                                installApk(apkFile);
                            }
                        }
                    });
        } else {
            EasyPermissions.requestPermissions(this, " 使用 BGAUpdateDemo 需要授权读写外部存储权限!", RC_PERMISSION_DOWNLOAD, perms);
        }
    }

//    /**
//     * 安装 apk 文件
//     *
//     * @param apkFile
//     */
//    public static void installApk(File apkFile) {
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(BaseActivity.mContext, "com.rckd.fileprovider", apkFile);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        if (BaseActivity.mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
//            BaseActivity.mContext.startActivity(intent);
//        }
//
//
//    }



    /**
     * 删除之前升级时下载的老的 apk 文件
     */
    @AfterPermissionGranted(RC_PERMISSION_DELETE)
    public void deleteApkFile() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 删除之前升级时下载的老的 apk 文件
            BGAUpgradeUtil.deleteOldApk();
        } else {
            EasyPermissions.requestPermissions(this, " 使用 BGAUpdateDemo 需要授权读写外部存储权限!", RC_PERMISSION_DELETE, perms);
        }
    }




}





