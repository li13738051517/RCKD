package com.rckd.fragment.fourth.child;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rckd.R;
import com.rckd.activity.ChangePhoneActivity;
import com.rckd.activity.ChangePhonePasswordActivity;
import com.rckd.activity.ImproveHrDataActivity;
import com.rckd.activity.JoinInMQActivity;
import com.rckd.activity.LoginActivity;
import com.rckd.activity.MainActivity;
import com.rckd.activity.PrefectPersonData;
import com.rckd.activity.ReChargeActivity;
import com.rckd.activity.RecordActivity;
import com.rckd.activity.SeeBarBMActivity;
import com.rckd.activity.SeeCollectedCVActivity;
import com.rckd.activity.SeeMeCompanyActivity;
import com.rckd.activity.SeeMyPositionFullTimeActivity;
import com.rckd.activity.SeeMyPositionPartTimeActivity;
import com.rckd.activity.SeeMyRecruitmentPostActivity;
import com.rckd.activity.SeeMyRecruitmentPostPartActivity;
import com.rckd.activity.SeeSeekerCVActivity;
import com.rckd.application.AppConfig;
import com.rckd.fragment.first.HomeFragment;
import com.rckd.fragment.first.child.FirstHomeFragment;
import com.rckd.view.SlideAppPostPopup;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.blankj.utilcode.util.ClipboardUtils.getIntent;
import static com.rckd.base.BaseActivity.RESULT_CODE_BAR_AD;
import static com.tencent.mm.opensdk.diffdev.a.g.av;

/**
 * Created by LiZheng on 16/6/6.
 */
//子界面的额实际界面
public class AvatarFragment extends com.rckd.base.BaseFragment implements  View.OnClickListener {
    private  static  final String tag=AvatarFragment.class.getName();

    Intent intent;
    public static AvatarFragment newInstance() {
        Bundle args = new Bundle();
        AvatarFragment fragment = new AvatarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    //-------------------top
    @Nullable@BindView(R.id.image_icon) de.hdodenhof.circleimageview.CircleImageView image_icon;
    @Nullable@BindView(R.id.tv_phne) TextView tv_phne;
    @Nullable@BindView(R.id.tv_num) TextView tv_num;
    //---------------
    @Nullable@BindView(R.id.job_want_bg) RelativeLayout job_want_bg;
    @Nullable@BindView(R.id.my_recruit_bg) RelativeLayout my_recruit_bg;
    @Nullable@BindView(R.id.person_bg) RelativeLayout  person_bg;
    @Nullable@BindView(R.id.company_bg) RelativeLayout  company_bg;
    @Nullable@BindView(R.id.pay_bg) RelativeLayout pay_bg;
    @Nullable@BindView(R.id.logout_bg) RelativeLayout  logout_bg;
    @Nullable@BindView(R.id.company_new) RelativeLayout company_new;//商企动态
    //-------------------------------------
    View view;//对话框布局仕途
    Dialog dialog; //对话框

    SlideAppPostPopup appPostPopup;

    //-----------------------------------
    Button button_left;
    TextView title;
    RelativeLayout job_want_bg1;
    RelativeLayout job_want_bg2;
    RelativeLayout job_want_bg3;
    RelativeLayout job_want_bg4;
    RelativeLayout job_want_bg5;
    ImageView button2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //-----------------先判断登陆相关----------------------------
        View view=null;

        view= inflater.inflate(R.layout.zhihu_fragment_fourth_avatar, container, false);
        //        EventBus.getDefault().register(this);
        ButterKnife.bind(this,view);
        // TODO Use fields...
        initView(view);
        return view;
    }

    //



    @Override
    public void onResume() {
        super.onResume();
        //----------------在前台时 ,通过判断是否已经登陆去
        //
        if (!AppConfig.isLogin) {
            //请求------------------的
//            startActivityForResult(LoginActivity.class,300);
//            finish();
            showDialog();
        }
        //-----------------前台需要被操作的资源包括更新UI均在此处理
    }
    public void showDialog() {
        new MaterialDialog.Builder(baseActivity)
                .content(R.string.shareLocationPrompt)
                .cancelable(false)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivityForResult(LoginActivity.class,300);
                    }
                })
                .negativeText(R.string.disagree)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        reStartActivity();
                    }
                })
                .show();
    }


    //----------------------------
    private void reStartActivity() {
        Intent intent = new Intent(baseActivity, MainActivity.class);
        finish();
        startActivity(intent);
//        baseActivity.onBackPressed();
//        startFragment(AvatarFragment.class);
    }

    private void initView(View view){
        job_want_bg.setOnClickListener(this);
        my_recruit_bg.setOnClickListener(this);
        person_bg.setOnClickListener(this);
        company_bg.setOnClickListener(this);
        pay_bg.setOnClickListener(this);
        logout_bg.setOnClickListener(this);
        company_new.setOnClickListener(this);
    }

//    protected OnBackToFirstListener _mBackToFirstListener;
//
//    public interface OnBackToFirstListener {
//        void onBackToFirstFragment();
//    }
//    @Override
//    public boolean onBackPressedSupport() {
//        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
//            popChild();
//        } else {
////            if (this instanceof HomeFragment) {   // 如果是 第一个Fragment 则退出app
////                baseActivity.finish();
////            } else {                                    // 如果不是,则回到第一个Fragment
//                _mBackToFirstListener.onBackToFirstFragment();
////            }
//        }
//
//        return super.onBackPressedSupport();
//    }

    //处理登陆页面返回数据值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_CODE_BAR_AD:
                //如果任没有登陆,让页面跳转到第一个Fragment
                if (!AppConfig.isLogin){
//                    startFragment(findFragment(FirstHomeFragment.class));
                    //请求
                    Timber.e(tag+"  onActivityResult RESULT_CODE_BAR_AD ",tag);
//                    startActivityForResult(LoginActivity.class,300);
                    showDialog();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //在此之前销毁必须要销毁的视图接口,事件等
        EventBus.getDefault().unregister(this);
    }

    //具体功能
    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.image_icon:
//                makeText("更换头像成功!!!");
//                break;
            //我的求职
            case R.id.job_want_bg:
                //--------------
                Timber.e(tag+"我的求职!!!",tag);
                view=LayoutInflater.from(baseActivity).inflate(R.layout.user_job_apply_in_jobwanted,null);
                dialog = new  MaterialDialog.Builder(baseActivity).build();
                dialog.show();
                dialog.getWindow().setContentView(view);
                button_left= ButterKnife.findById(view, R.id.left_btn);
                title=ButterKnife.findById(view ,R.id.title_text);
                title.setVisibility(View.VISIBLE);
                title.setText("我的求职");
                button_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //完善个人简历
                job_want_bg1=ButterKnife.findById(view ,R.id.job_want_bg1);
                job_want_bg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //开启新的界面
                        dialog.dismiss();
                        startActivity(PrefectPersonData.class);
                    }
                });

                //求职贴管理
                job_want_bg2=ButterKnife.findById(view,R.id.job_want_bg2);
                job_want_bg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        dialog.dismiss();
                        appPostPopup=new SlideAppPostPopup(baseActivity);

                        //全职
                        TextView tvFullJob=(TextView)appPostPopup.getView().findViewById(R.id.tx_1);
                        tvFullJob.setText("我的求职贴(全职)");
                        tvFullJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(SeeMyPositionFullTimeActivity.class);
                                appPostPopup.dismiss();
                            }
                        });
                        //兼职
                        TextView tvPartJob=(TextView)appPostPopup.getView().findViewById(R.id.tx_2) ;
                        tvFullJob.setText("我的求职贴(兼职)");
                        tvPartJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(SeeMyPositionPartTimeActivity.class);
                                appPostPopup.dismiss();
                            }
                        });
                        //点击取消按钮时候的,优先级高
                        TextView textView =(TextView) appPostPopup.getView().findViewById(R.id.dissmiss);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appPostPopup.dismiss();
                            }
                        });
                        appPostPopup.showPopupWindow();

                        //---------------------求职贴管理

                        //此处弹出底部选择框
                    }
                });
                //已查看的公司
                job_want_bg3=ButterKnife.findById(view ,R.id.job_want_bg3);
                job_want_bg3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(SeeMeCompanyActivity.class);
                    }
                });

                button2=ButterKnife.findById(view,R.id.button2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                break;
            //我的招聘
            case R.id.my_recruit_bg:
                view=LayoutInflater.from(baseActivity).inflate(R.layout.user_job_recruit_in_my_recruit,null);
                dialog = new  MaterialDialog.Builder(baseActivity).build();
                dialog.show();
                dialog.getWindow().setContentView(view);
                button_left= ButterKnife.findById(view, R.id.left_btn);
                title=ButterKnife.findById(view ,R.id.title_text);
                title.setVisibility(View.VISIBLE);
                title.setText("我的招聘");
                button_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //完善招聘方资料
                job_want_bg1=ButterKnife.findById(view ,R.id.job_want_bg1);
                job_want_bg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //开启新的界面 完善招聘方资料
                        dialog.dismiss();
                        startActivity(ImproveHrDataActivity.class);
                    }
                });

                //招聘贴管理
                job_want_bg2=ButterKnife.findById(view,R.id.job_want_bg2);
                job_want_bg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        dialog.dismiss();
                        appPostPopup=new SlideAppPostPopup(baseActivity);

                        //全职
                        TextView tvFullJob=(TextView)appPostPopup.getView().findViewById(R.id.tx_1);
                        tvFullJob.setText("我的招聘贴(全职)");
                        tvFullJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //查看我的招聘贴(全职)
                                startActivity(SeeMyRecruitmentPostActivity.class);
                                appPostPopup.dismiss();
                            }
                        });
                        //兼职
                        TextView tvPartJob=(TextView)appPostPopup.getView().findViewById(R.id.tx_2) ;
                        tvPartJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //查看我的招聘贴(兼职)
                                startActivity(SeeMyRecruitmentPostPartActivity.class);
                                appPostPopup.dismiss();
                            }
                        });
                        //点击取消按钮时候的,优先级高
                        TextView textView =(TextView) appPostPopup.getView().findViewById(R.id.dissmiss);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appPostPopup.dismiss();
                            }
                        });
                        appPostPopup.showPopupWindow();

                        //---------------------求职贴管理

                        //此处弹出底部选择框
                    }
                });
                //已查看的简历
                job_want_bg3=ButterKnife.findById(view ,R.id.job_want_bg3);
                job_want_bg3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //已查看的简历,是当我为招聘方时,查看求职者的简历
                        startActivity(SeeSeekerCVActivity.class);
                    }
                });

                //已收藏的简历
                job_want_bg4=ButterKnife.findById(view,R.id.job_want_bg4);
                job_want_bg4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //已收藏的简历
                        dialog.dismiss();
                        startActivity(SeeCollectedCVActivity.class);
                    }
                });
                //加入名企
                job_want_bg5=ButterKnife.findById(view ,R.id.job_want_bg5);
                job_want_bg5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //加入名企
                        dialog.dismiss();
                        startActivity(JoinInMQActivity.class);
                        finish();

                    }
                });

                button2=ButterKnife.findById(view,R.id.button2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                break;
            //账号管理
            case R.id.person_bg:

                //--------------
                Timber.e(tag+" 账号管理!!!",tag);
                view=LayoutInflater.from(baseActivity).inflate(R.layout.user_container_in_person_bg,null);
                dialog = new  MaterialDialog.Builder(baseActivity).build();
                dialog.show();
                dialog.getWindow().setContentView(view);
                button_left= ButterKnife.findById(view, R.id.left_btn);
                title=ButterKnife.findById(view ,R.id.title_text);
                title.setVisibility(View.VISIBLE);
                title.setText("账号管理");
                button_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //修改密码
                job_want_bg1=ButterKnife.findById(view ,R.id.job_want_bg1);
                job_want_bg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //开启新的界面
                        dialog.dismiss();
                        startActivity(ChangePhonePasswordActivity.class);
                    }
                });

                //消费记录
                job_want_bg2=ButterKnife.findById(view,R.id.job_want_bg2);
                job_want_bg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        dialog.dismiss();
                        startActivity(RecordActivity.class);
                    }
                });
                //更换手机号码
                job_want_bg3=ButterKnife.findById(view ,R.id.job_want_bg3);
                job_want_bg3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(ChangePhoneActivity.class);
                    }
                });

                button2=ButterKnife.findById(view,R.id.button2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                break;
            //便名帖管理
            case R.id.company_bg:
                //-------------------------
                startActivity(SeeBarBMActivity.class);
                break;

            //动态新闻
            case R.id.company_new:
                break;

            //充值
            case R.id.pay_bg:
                //跳转到充值界面
                startActivity(ReChargeActivity.class);
                break;

            //注销账号
            case R.id.logout_bg:
                makeText("注销账号成功");
                break;

        }
    }
}
