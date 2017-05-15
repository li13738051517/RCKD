package com.rckd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.rckd.R;
import com.rckd.adpter.MyBaseAdapter;
import com.rckd.anim.FragmentAnimator;
import com.rckd.base.BaseActivity;
import com.rckd.base.BaseFragment;
import com.rckd.base.BaseMainFragment;
import com.rckd.bean.BaseIcon;
import com.rckd.event.TabSelectedEvent;
import com.rckd.fragment.first.HomeFragment;
import com.rckd.fragment.first.child.FirstHomeFragment;
import com.rckd.fragment.fourth.UserCenterFragment;
import com.rckd.fragment.fourth.child.MeFragment;
import com.rckd.fragment.second.MsgFragment;
import com.rckd.fragment.second.child.ViewPagerFragment;
import com.rckd.fragment.third.SendBarFragment;
import com.rckd.fragment.third.child.ShopFragment;
import com.rckd.helper.FragmentLifecycleCallbacks;
import com.rckd.view.BottomBar;
import com.rckd.view.BottomBarTab;
import com.rckd.view.SlideFromBottonPoup;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.smtt.utils.TbsLog;
import com.yanzhenjie.permission.AndPermission;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * 类知乎 复杂嵌套Demo tip: 多使用右上角的"查看栈视图"
 * Created by LiZheng on 16/6/2.
 */
public class MainActivity extends BaseActivity implements BaseMainFragment.OnBackToFirstListener, View.OnClickListener, com.yanzhenjie.permission.PermissionListener {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private BaseFragment[] mFragments = new BaseFragment[4];

    private BottomBar mBottomBar;
//    private Toolbar mToolbar;

    ImageView title_logo;
    Button cityBtn;
    private static String tag = MainActivity.class.getName();
    private String msg = null;//接受返回来的信息,并对其解析
    TencentLocationManager mLocationManager;

    // 定义PopupWindow
//    private PopupWindow popWindow;
    // 获取手机屏幕分辨率的类
//    private DisplayMetrics dm;

    SlideFromBottonPoup popup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.e(tag);
        Timber.e(tag + " onCreate ", tag);
        if (savedInstanceState == null) {
            Timber.e(tag + " onCreate  savedInstanceState == null ", tag);
            mFragments[FIRST] = HomeFragment.newInstance();
            mFragments[SECOND] = MsgFragment.newInstance();
            mFragments[THIRD] = SendBarFragment.newInstance();
            mFragments[FOURTH] = UserCenterFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            Timber.e(tag + " onCreate  savedInstanceState == null else ", tag);
            mFragments[FIRST] = findFragment(HomeFragment.class);
            mFragments[SECOND] = findFragment(MsgFragment.class);
            mFragments[THIRD] = findFragment(SendBarFragment.class);
            mFragments[FOURTH] = findFragment(UserCenterFragment.class);
        }
        //先初始化poup
        popup = new SlideFromBottonPoup(this);
        //先初始化poup
        initPoupListener();

        initTab();
        // 可以监听该Activity下的所有Fragment的18个 生命周期方法
        startLocation(cityBtn);
        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentSupportVisible(BaseFragment fragment) {
                Log.e("MainActivity", "onFragmentSupportVisible--->" + fragment.getClass().getSimpleName());
            }
        });

    }


    private GridView grid_photo;
    private BaseAdapter mAdapter = null;
    private ArrayList<BaseIcon> mData = null;

    public void initPoupListener() {
        grid_photo = (GridView) popup.getView().findViewById(R.id.grid_photo);
        mData = new ArrayList<BaseIcon>();
        //此处添加数据 ,仅仅只是添加几张图片的视图,可以这样写
        mData.add(new BaseIcon(R.mipmap.zhaopin, "发招聘贴"));
        mData.add(new BaseIcon(R.mipmap.qiuzhi, "发求职贴"));
        mData.add(new BaseIcon(R.mipmap.fwzs, "房屋出售"));
        mData.add(new BaseIcon(R.mipmap.eszj, "二手之家"));
        mData.add(new BaseIcon(R.mipmap.sf, "顺风拼车"));
        mData.add(new BaseIcon(R.mipmap.jyzh, "交友征婚"));
        mData.add(new BaseIcon(R.mipmap.lsdg, "临时短工"));
        mData.add(new BaseIcon(R.mipmap.jgyd, "匠工约定"));
        mData.add(new BaseIcon(R.mipmap.dtqz, "打听求助"));
        mData.add(new BaseIcon(R.mipmap.gegz, "广而告之"));
        mAdapter = new MyBaseAdapter<BaseIcon>(mData, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, BaseIcon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };
        grid_photo.setAdapter(mAdapter);
        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "你点击了~" + position + "~项", Toast.LENGTH_SHORT).show();
                //此处编写position
                switch (position) {

                }
            }
        });


//        popup.getView().findViewById(R.id.text1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarAdActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarHelpActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarArtCratfsActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text4).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarTempJobActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text5).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarFriendActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text6).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarCarActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text7).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarOldHomeActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text8).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarHouseSaleActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text9).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarPasteJobActivity.class);
//            }
//        });
//        popup.getView().findViewById(R.id.text10).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(SendBarJobWantActivity.class);
//            }
//        });


    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return super.onCreateFragmentAnimator();
    }

    private void initTab() {
        Timber.e(tag + " initTab ", tag);
        title_logo = (ImageView) findViewById(R.id.title_logo);
        cityBtn = (Button) findViewById(R.id.city_text);
        cityBtn.setClickable(true);
        cityBtn.setOnClickListener(this);//执行切换城市的功能

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.addItem(new BottomBarTab(this, R.drawable.tab_home_selector))
                .addItem(new BottomBarTab(this, R.drawable.tab_msg_selector))
                .addItem(new BottomBarTab(this, R.drawable.tab_ft_selector))
                .addItem(new BottomBarTab(this, R.drawable.tab_person_selector));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                Timber.e(tag + " onTabSelected " + position + "\t" + prePosition, tag);
                if (position != 2 & prePosition != 2) {
                    showHideFragment(mFragments[position], mFragments[prePosition]);
                } else if (position != 2 & prePosition == 2) {
                    Timber.e(tag + " onTabSelected   prePosition ==2");
                    showHideFragment(mFragments[position], null);
                } else if (position == 2 & prePosition != 2) {
                    showHideFragment(mFragments[prePosition], null);
                    popup.showPopupWindow();
                }

            }

            @Override
            public void onTabUnselected(int position) {
                Timber.e(tag + " onTabUnselected " + position, tag);
            }

            @Override
            public void onTabReselected(int position) {
                Timber.e(tag + " onTabReselected " + position, tag);
                BaseFragment currentFragment = null;
                if (position != 2) {
                    currentFragment = mFragments[position];
                    int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();
                    // 如果不在该类别Fragment的主页,则回到主页;
                    if (count > 1) {
                        if (currentFragment instanceof HomeFragment) {
                            currentFragment.popToChild(FirstHomeFragment.class, false);
                        } else if (currentFragment instanceof MsgFragment) {
                            currentFragment.popToChild(ViewPagerFragment.class, false);
                        } else if (currentFragment instanceof SendBarFragment) {
                            currentFragment.popToChild(ShopFragment.class, false);
                        } else if (currentFragment instanceof UserCenterFragment) {
                            currentFragment.popToChild(MeFragment.class, false);
                        }
                        return;
                    }

                    // 这里推荐使用EventBus来实现 -> 解耦
                    if (count == 1) {
                        // 在FirstPagerFragment中接收, 因为是嵌套的Fragment 所以用EventBus比较方便
                        // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                        EventBus.getDefault().post(new TabSelectedEvent(position));
                    }

                } else {
                    Timber.e(tag + "  onTabReselected position==2 ", tag);
                    //如果postion
//                    showPopupWindow();
                    popup.showPopupWindow();
                }

            }
        });
    }


    /**
     * 显示PopupWindow弹出菜单
     */
//    protected void showPopupWindow(View parent) {
//        View view;
//        if (popWindow == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            view = layoutInflater.inflate(R.layout.ft_pop_win, null);
//            dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            // 创建一个PopuWidow对象
//            popWindow = new PopupWindow(view, dm.widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
//        }
//        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
//        popWindow.setFocusable(true);
//        // 设置允许在外点击消失
//        popWindow.setOutsideTouchable(true);
//        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
//        popWindow.setBackgroundDrawable(new BitmapDrawable());
//        // PopupWindow的显示及位置设置
//        // popWindow.showAtLocation(parent, Gravity.FILL, 0, 0);
//        popWindow.showAsDropDown(parent, 0, 0);
//
//        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//
//            }
//        });
//
//        // 监听触屏事件
//        popWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popWindow.dismiss();
//                return false;
//            }
//        });
//    }
//
    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public void onBackToFirstFragment() {
        mBottomBar.setCurrentItem(0);
    }

    /**
     * 这里暂没实现,忽略
     */
//    @Subscribe
//    public void onHiddenBottombarEvent(boolean hidden) {
//        if (hidden) {
//            mBottomBar.hide();
//        } else {
//            mBottomBar.show();
//        }
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        /**
         * 注意, 本示例中 requestLocationUpdates 和 removeUpdates 都可能被多次重复调用.
         * <p>
         * 重复调用 requestLocationUpdates, 将忽略之前的 reqest 并自动取消之前的 listener, 并使用最新的
         * request 和 listener 继续定位
         * <p>
         * 重复调用 removeUpdates, 将定位停止
         */

        // 退出 activity 前一定要停止定位!
        stopLocation(null);
    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_text://切换城市
                startActivityForResult(CityListActivity.class, null, 1);
                break;
        }
    }

    //创建位置监听器


    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {

            case REQUEST_CODE_PERMISSION_LOCATION:
                Timber.e(tag + " onFailed " + " AppPressionCode.TenCentMap ", tag);
                makeText(tag + " 您没有给相应的权限！！！我们可能无法提供更好的服务给你");
                break;
        }
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

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
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                makeText(tag + " 开始申请权限");
                Timber.e(tag + " onSucceed " + " AppPressionCode.TenCentMap", tag);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //int requestCode, int resultCode, Intent data 注意三个参数
        TbsLog.e(tag, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);
        //根据请求吗判断事务
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                makeText("欢迎回来");
                Timber.e(tag + " REQUEST_CODE_SETTING =" + REQUEST_CODE_SETTING, tag);
                break;
            }
            default:
                Timber.e(tag + "    requestCode   =" + REQUEST_CODE_SETTING, tag);
//                defaultFinish();
                break;
        }
        //根据结果吗判断事务
        switch (resultCode) {
            case 2:
                //定义规范
                cityBtn.setText(data.getStringExtra("city"));
                break;
            default:
                Timber.e(tag + "   resultCode   =" + REQUEST_CODE_SETTING, tag);
                break;

        }


    }


    /**
     * 开始定位,其他类中只需调用startLocation
     *
     * @param view
     */
    protected void startLocation(View view) {
        Timber.e(tag + " startLocation start ", tag);
        if (isHaveAndPermission(strPression)) {
            // 创建定位请求
//            TencentLocationRequest request = TencentLocationRequest.create();
            TencentLocationRequest request = TencentLocationRequest.create()
                    .setInterval(10000)
                    .setAllowCache(true)
                    .setAllowIndoorLocation(true)
                    .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA); // 设置定位level

            // 开始定位
//            mLocationManager.requestLocationUpdates(request, this);
            mLocationManager = TencentLocationManager.getInstance(mContext);
//            mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02); //腾讯定位默认,只能以网络形式开启,gps无效,反地理编码
            int error = mLocationManager.requestLocationUpdates(request, listener);
            switch (error) {
                case 0:
                    Timber.e(tag + " 注册位置监听器成功 ", tag);
                    break;

                case 1:
                    Timber.e(tag + " 设备缺少使用腾讯定位SDK需要的基本条件 ", tag);
                    break;
                case 2:
                    Timber.e(tag + " 配置的 key 不正确 ", tag);
                    break;
                case 3:
                    Timber.e(tag + " 自动加载libtencentloc.so失败，可能由以下原因造成：\n" +
                            "1、这往往是由工程中的so与设备不兼容造成的，应该添加相应版本so文件;\n" +
                            "2、如果您使用AndroidStudio,可能是gradle没有正确指向so文件加载位置，可以按照这里配置您的gradle; ", tag);
                    break;
            }
            Timber.e(tag + " startLocation 开始定位  ", tag);
        } else {
            getPression(this, REQUEST_CODE_PERMISSION_LOCATION, strPression);
            Timber.e(tag + " startLocation 正在获取权限  ", tag);
        }
        Timber.e(tag + " startLocation over ", tag);
    }


    /**
     * 移除监听器
     *
     * @param view
     */
    protected void stopLocation(View view) {
        Timber.e(tag + " stopLocation ", tag);
        if (listener != null) {
            mLocationManager.removeUpdates(listener);
        }
    }


    private TencentLocationListener listener = new TencentLocationListener() {
        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {

            if (TencentLocation.ERROR_OK == error) {
                // 定位成功
                //解析定位成功返回的数据,执行方法
                Timber.e(tag + " TencentLocation.ERROR_OK ", tag);
                //base REQ_LEVEL_GEO
                String latitude = String.valueOf(tencentLocation.getLatitude());//纬度 double
                String longitude = String.valueOf(tencentLocation.getLongitude());//	经度 double
                String altitude = String.valueOf(tencentLocation.getAltitude());//	海拔
                String accuracy = String.valueOf(tencentLocation.getAccuracy());//	精度

                //REQ_LEVEL_ADMIN_AREA
                String nation = tencentLocation.getNation();//国家
                String province = tencentLocation.getProvince(); //省
                String city = tencentLocation.getCity();//市
                String district = tencentLocation.getDistrict();//district	区
                String town = tencentLocation.getTown();//  镇
                String village = tencentLocation.getVillage();//村
                String street = tencentLocation.getStreet();//街道
                String streetNo = tencentLocation.getStreetNo();//门号


                if (!district.isEmpty()) {
                    cityBtn.setText(district + " | 切换");
                    Timber.e(tag + " " + district, tag);
                } else if (!city.isEmpty()) {
                    cityBtn.setText(city + "| 切换");//默认地址
                    Timber.e(tag + " " + city, tag);
                } else {
                    cityBtn.setText("宣城" + "| 切换");//默认地址
                    Timber.e(tag + " 默认城市 ", tag);
                }

            } else {
                // 定位失败
                Timber.e(tag + " TencentLocation.ERROR ,原因正在分析", tag);
                makeText("定位失败: 原因是" + reason);
                switch (error) {
                    case TencentLocation.ERROR_NETWORK:
                    case TencentLocation.ERROR_BAD_JSON:
                    case TencentLocation.ERROR_WGS84:
                    case TencentLocation.ERROR_UNKNOWN:
                        break;
                }
            }
        }

        @Override
        public void onStatusUpdate(String name, int status, String desc) {
            if (status == STATUS_DENIED) {
            /* 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用**很可能无法定位**
             * 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
			 */
                makeText(tag + " 提示:定位权限被禁用! " + name + status + desc);

                Timber.e(tag + " " + name + " " + status + " " + desc);

                //引导用户打开权限设置表
            }

            if (name.equals("cell") && status == STATUS_DISABLED) {
                makeText(tag + name + status + desc + "  模块未打开! ");
            }

            if (name.equals("wifi") && status == STATUS_DISABLED) {
                makeText(tag + name + status + desc + "  模块未打开! ");
            }

            if (name.equals("GPS") && status == STATUS_DISABLED) {
                makeText(tag + name + status + desc + "  模块未打开! ");
            }
        }
    };


}