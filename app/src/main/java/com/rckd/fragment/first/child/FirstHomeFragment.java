package com.rckd.fragment.first.child;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.rckd.R;
import com.rckd.activity.ChoosePositionActivity;
import com.rckd.activity.ChoosePositionPartActivity;
import com.rckd.activity.HouseKeepingZoneActivity;
import com.rckd.activity.MainActivity;
import com.rckd.activity.NewFindJobActivity;
import com.rckd.activity.NewJobActivity;
import com.rckd.activity.PartTimeZoneActivity;
import com.rckd.activity.SeeAdActivity;
import com.rckd.activity.SeeArtCratfsAdActivity;
import com.rckd.activity.SeeCarAdActivity;
import com.rckd.activity.SeeHelpAdActivity;
import com.rckd.activity.SeeMQRecruitActivity;
import com.rckd.activity.SeeMakeFriendsAdActivity;
import com.rckd.activity.SeeOldHomeAdActivity;
import com.rckd.activity.SeeSeleHouseAdActivity;
import com.rckd.activity.SeeTempWorkAdActivity;
import com.rckd.activity.TalentsActivity;
import com.rckd.activity.WantedJobActivity;
import com.rckd.activity.WantedPersonActivity;
import com.rckd.adpter.BaseAdapterQd;
import com.rckd.adpter.FirstHomeAdapter;
import com.rckd.base.BaseFragment;
import com.rckd.bean.Article;
import com.rckd.bean.BaseIcon;
import com.rckd.event.TabSelectedEvent;
import com.rckd.helper.DetailTransition;
import com.rckd.inter.OnItemClickListener;
import com.rckd.loader.GlidImageLoader;
import com.rckd.view.FindJobPopup;
import com.rckd.view.GridViewNoScroll;
import com.rckd.view.SlideAppPostPopup;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by LiZheng on 16/6/5.
 */
public class FirstHomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener  /* , com.yanzhenjie.permission.PermissionListener  */ {

    FindJobPopup findJobPopup;//底部弹窗找工作
    SlideAppPostPopup appPostPopup;//
    String[] titles = {"名企招聘", "便民动态", "新闻动态"};
    //    MyScrollView myScrollView;
    @BindView(R.id.lin_one)
    LinearLayout linOne;
    @BindView(R.id.lin_two)
    LinearLayout linTwo;
    @BindView(R.id.lin_three)
    LinearLayout linThree;
    @BindView(R.id.lin_right_in)
    LinearLayout linRightIn;
    @BindView(R.id.lin_right)
    LinearLayout linRight;
    @BindView(R.id.bar_job)
    LinearLayout barJob;
    @BindView(R.id.tv_char)
    TextView tvChar;
    @BindView(R.id.gv2)
    GridViewNoScroll gv2;
    //    @BindView(R.id.tv_show)
//    TextView tvShow;
//    @BindView(R.id.viewPager)
//    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    SegmentTabLayout tabLayout;
    @BindView(R.id.recy)
    RecyclerView recy;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.re_base)
    LinearLayout reBase;
    //    @BindView(R.id.scrollView)
//    ScrollViewInter scrollView;
    Unbinder unbinder;
//    @BindView(R.id.banner)
//    Banner banner;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    private int kkk;
    private int kkkk;//index
    ArrayList<Fragment> mFragments = new ArrayList<>();//集合用来管理单独的页面
    //    ViewPager viewPager;
    private View mDecorView;
    //    TextView tvshow;
    SegmentTabLayout tab_layout;
    static String tag = FirstHomeFragment.class.getName();//tag标记
    Banner banner;
    GridView gridView;//gv  1
    BaseAdapter mAdapterGv;//adpter 1
    ArrayList<BaseIcon> mData;
    GridView gridView2;   // gv 2
    BaseAdapter mAdapterGv2; // madapter 2
    ArrayList<BaseIcon> mData2;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //-----------------------------
        Timber.tag(tag);
        Timber.e(tag + " onCreateView start", tag);
    /*getWindow().addFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        View view = inflater.inflate(R.layout.zhihu_fragment_first_home_index, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        unbinder = ButterKnife.bind(this, view);
        onViewClicked(view);
        return view;
    }

    TextView tvFullJob;
    TextView tvPartJob;
    TextView tvHire;
    TextView textView;

    private void initView(View view) {
//        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
//        initImgData();
        //Fragment的假数据初始化
//        for (String title : titles) {
//            mFragments.add(TabCardFragment.getInstance("Switch ViewPager " + title));
//        }
//        tvshow = (TextView) view.findViewById(R.id.tv_show);

        banner = (Banner) view.findViewById(R.id.banner);
        //本地图片数据（资源文件）
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.b1);
        list.add(R.mipmap.b2);
        list.add(R.mipmap.b3);
        list.add(R.mipmap.b1);
        list.add(R.mipmap.b2);
        list.add(R.mipmap.b3);
        banner.setImages(list)
                .setImageLoader(new GlidImageLoader())
                .start();


        gridView = (GridView) view.findViewById(R.id.gv);
        mData = new ArrayList<BaseIcon>();
        //此处添加数据 ,仅仅只是添加几张图片的视图,可以这样写
        mData.add(new BaseIcon(R.mipmap.icon_bm_r1_c1, "最新招聘"));
        mData.add(new BaseIcon(R.mipmap.icon_bm_r1_c2, "最新求职"));
        mData.add(new BaseIcon(R.mipmap.icon_bm_r1_c3, "我的匹配"));
        mData.add(new BaseIcon(R.mipmap.icon_bm_r1_c4, "本地客服"));
        mData.add(new BaseIcon(R.mipmap.icon_bm_r2_c5, "扎工作"));
        mData.add(new BaseIcon(R.mipmap.icon_bm_r2_c6, "找人才"));
        mData.add(new BaseIcon(R.mipmap.icon_bm_r2_c7, "名企招聘"));
        mData.add(new BaseIcon(R.mipmap.icon_bm_r2_c8, "置顶帖子"));

        mAdapterGv = new BaseAdapterQd<BaseIcon>(mData, R.layout.item_grid_icon) {
            @Override
            public void bindView(ViewHolder holder, BaseIcon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };
        gridView.setAdapter(mAdapterGv);
        //gridview加载
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    //最新招聘
                    case 0:
                        startActivity(NewJobActivity.class);
                        break;
                    case 1:
                        //最新求职
                        startActivity(NewFindJobActivity.class);
                        //--------------------------
                        break;
                    case 2:
                        //-------------------------------
                        appPostPopup = new SlideAppPostPopup(baseActivity);
                        //全职
                        tvFullJob = (TextView) appPostPopup.getView().findViewById(R.id.tx_1);
                        tvFullJob.setText("我要找的工作");
                        tvFullJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//我要找的工作
                                startActivity(WantedJobActivity.class);
                                appPostPopup.dismiss();
                            }
                        });

                        tvPartJob = (TextView) appPostPopup.getView().findViewById(R.id.tx_2);
                        tvPartJob.setText("我要找的人才");
                        tvPartJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //---------------------我要找的人才
                                startActivity(WantedPersonActivity.class);
                                appPostPopup.dismiss();
                            }
                        });
                        //点击取消按钮时候的,优先级高
                        textView = (TextView) appPostPopup.getView().findViewById(R.id.dissmiss);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appPostPopup.dismiss();
                            }
                        });
                        appPostPopup.showPopupWindow();

                        break;

                    case 3:
                        //本地客服
                        appPostPopup = new SlideAppPostPopup(baseActivity);
                        //全职
                        tvFullJob = (TextView) appPostPopup.getView().findViewById(R.id.tx_1);
                        tvFullJob.setText("电话:0563-6626168");
                        tvFullJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //----------------全职人才出和兼职人才获取数据不同
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + "05636626168");
                                intent.setData(data);
                                startActivity(intent);
                                appPostPopup.dismiss();
                            }
                        });

                        tvPartJob = (TextView) appPostPopup.getView().findViewById(R.id.tx_2);
                        tvPartJob.setVisibility(View.GONE);
                        View view1 = (View) appPostPopup.getView().findViewById(R.id.v2);
                        view1.setVisibility(View.GONE);
                        //点击取消按钮时候的,优先级高
                        textView = (TextView) appPostPopup.getView().findViewById(R.id.dissmiss);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appPostPopup.dismiss();
                            }
                        });
                        appPostPopup.showPopupWindow();

                        break;

                    case 4:
                        //找工作
                        findJobPopup = new FindJobPopup(baseActivity);

                        tvFullJob = (TextView) findJobPopup.getView().findViewById(R.id.tx_1);
                        tvFullJob.setText("全职工作");
                        tvFullJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //----------------全职工作
                                startActivity(ChoosePositionActivity.class);
                                findJobPopup.dismiss();
                            }
                        });
                        tvPartJob = (TextView) findJobPopup.getView().findViewById(R.id.tx_2);
                        tvPartJob.setText("兼职工作");
                        tvPartJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(ChoosePositionPartActivity.class);
                                findJobPopup.dismiss();
                            }
                        });
                        tvHire = (TextView) findJobPopup.getView().findViewById(R.id.tx_3);
                        tvHire.setText("让人聘我");
                        tvHire.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                findJobPopup.dismiss();

                            }
                        });

                        break;
                    case 5:
                        //找人才-------匹配搜索条件后跳转到最新求职

                        appPostPopup = new SlideAppPostPopup(baseActivity);
                        //全职
                        tvFullJob = (TextView) appPostPopup.getView().findViewById(R.id.tx_1);
                        tvFullJob.setText("全职人才");
                        tvFullJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //----------------全职人才出和兼职人才获取数据不同
//
                                startActivity(ChoosePositionActivity.class);
                                appPostPopup.dismiss();
                            }
                        });

                        tvPartJob = (TextView) appPostPopup.getView().findViewById(R.id.tx_2);
                        tvPartJob.setText("兼职人才");
                        tvPartJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(ChoosePositionPartActivity.class);
                                appPostPopup.dismiss();
                            }
                        });
                        //点击取消按钮时候的,优先级高
                        textView = (TextView) appPostPopup.getView().findViewById(R.id.dissmiss);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appPostPopup.dismiss();
                            }
                        });
                        appPostPopup.showPopupWindow();

                        break;

                    case 6:
                        //名企招聘
                        startActivity(SeeMQRecruitActivity.class);
                        break;
                    case 7:
                        //置顶帖子
                        appPostPopup = new SlideAppPostPopup(baseActivity);
                        //全职
                        tvFullJob = (TextView) appPostPopup.getView().findViewById(R.id.tx_1);
                        tvFullJob.setText("求职贴");
                        tvFullJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                startActivity(SeeMyPositionFullTimeActivity.class);
                                makeText("您已刷新");
                                appPostPopup.dismiss();
                            }
                        });

                        tvPartJob = (TextView) appPostPopup.getView().findViewById(R.id.tx_2);
                        tvPartJob.setText("招聘贴");
                        tvPartJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                startActivity(SeeMyPositionPartTimeActivity.class);
                                makeText("您已刷新");
                                appPostPopup.dismiss();
                            }
                        });
                        //点击取消按钮时候的,优先级高
                        textView = (TextView) appPostPopup.getView().findViewById(R.id.dissmiss);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appPostPopup.dismiss();
                            }
                        });
                        appPostPopup.showPopupWindow();

                        break;

                    default:
                        break;

                }

            }
        });

        gridView2 = (GridView) view.findViewById(R.id.gv2);
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
            public void bindView(ViewHolder holder, BaseIcon obj) {
                holder.setImageResource(R.id.img_icon, obj.getiId());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };
        gridView2.setAdapter(mAdapterGv2);
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //便民贴,都需要做是否登陆的判断
                    case 0:
                        //查看匠工约定便民贴
                        startActivity(SeeArtCratfsAdActivity.class);
                        break;
                    case 1:
                        startActivity(SeeCarAdActivity.class);
                        break;
                    case 2:
                        //查看临时短工便民贴
                        startActivity(SeeTempWorkAdActivity.class);
                        break;
                    case 3:
                        //查看交友征婚便民贴
                        startActivity(SeeMakeFriendsAdActivity.class);

                        break;
                    case 4:
                        //查看打听求助中的内容
                        startActivity(SeeHelpAdActivity.class);
                        break;
                    case 5:
                        //二手之家
                        startActivity(SeeOldHomeAdActivity.class);

                        break;
                    case 6:
                        startActivity(SeeSeleHouseAdActivity.class);
                        break;
                    case 7:
                        //查看广而告之中的内容
                        startActivity(SeeAdActivity.class);
                        break;
                    default:
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


//        myScrollView = (MyScrollView) view.findViewById(R.id.scrollView);

//        tab_layout = (SegmentTabLayout) view.findViewById(R.id.tab_layout);

//        tab_layout.setTabData();   //设置标题叔叔
//        viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
//
//        tab_layout.setTabData(titles);
//        tab_layout.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                viewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//
//            }
//        });
//        //viewpage 的页监听
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                tab_layout.setCurrentTab(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        viewPager.setCurrentItem(1);


//        myScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
//            @Override
//            public void onScrollchanged(int scrollY) {
//                Log.e("haha ", scrollY + "  " + viewPager.getTop());
//                int translation = Math.max(scrollY, viewPager.getTop() - kkkk);
//                tab_layout.setTranslationY(translation);
//
////                if (scrollY > kkk) {
//////                    ingo2top.setVisibility(View.VISIBLE);
////                } else {
//////                    ingo2top.setVisibility(View.GONE);
////                }
//            }
//
//            @Override
//            public void onTouchUp() {
//            }
//
//            @Override
//            public void onTouchDown() {
//            }
//        });


        //获取控件大小
//        tvshow.post(new Runnable() {
//            @Override
//            public void run() {
//                kkk = tab_layout.getHeight();
//                kkkk = tab_layout.getHeight();
//            }
//        });


        //其他控件相关操作,如在最下方处增加点击回到最顶层操作
//       myScrollView.smoothScrollTo(0, 0);
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
        unbinder.unbind();
    }


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

    @OnClick({R.id.lin_one, R.id.lin_two, R.id.lin_three  ,R.id.tv1,R.id.tv2,R.id.tv3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                break;
            case R.id.tv2:
                break;
            case R.id.tv3:
                break;
            case R.id.lin_one:
                //---------------------------
                //家政专区
                startActivity(HouseKeepingZoneActivity.class);
                break;
            case R.id.lin_two:
                //兼职专区
                startActivity(PartTimeZoneActivity.class);
                break;
            case R.id.lin_three:
                //高级人才
                startActivity(TalentsActivity.class);
                break;
        }
    }



    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //
        @Override
        public int getCount() {
//            return 0;
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return super.getPageTitle(position);
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
//            return null;
            return mFragments.get(position);
        }
    }
}
