package com.rckd.fragment.fourth.child;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rckd.R;
import com.rckd.activity.ImproveHrDataActivity;
import com.rckd.activity.JoinInMQActivity;
import com.rckd.activity.PrefectPersonData;
import com.rckd.activity.SeeCollectedCVActivity;
import com.rckd.activity.SeeMeCompanyActivity;
import com.rckd.activity.SeeMyPositionFullTimeActivity;
import com.rckd.activity.SeeMyPositionPartTimeActivity;
import com.rckd.activity.SeeMyRecruitmentPostActivity;
import com.rckd.activity.SeeMyRecruitmentPostPartActivity;
import com.rckd.activity.SeeSeekerCVActivity;
import com.rckd.view.SlideAppPostPopup;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by LiZheng on 16/6/6.
 */
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
    @BindView(R.id.image_icon) de.hdodenhof.circleimageview.CircleImageView image_icon;
    @BindView(R.id.tv_phne) TextView tv_phne;
    @BindView(R.id.tv_num) TextView tv_num;
    //---------------
    @BindView(R.id.job_want_bg) RelativeLayout job_want_bg;
    @BindView(R.id.my_recruit_bg) RelativeLayout my_recruit_bg;
    @BindView(R.id.person_bg) RelativeLayout  person_bg;
    @BindView(R.id.company_bg) RelativeLayout  company_bg;
    @BindView(R.id.pay_bg) RelativeLayout pay_bg;
    @BindView(R.id.logout_bg) RelativeLayout  logout_bg;
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
        View view = inflater.inflate(R.layout.zhihu_fragment_fourth_avatar, container, false);
//        EventBus.getDefault().register(this);
        ButterKnife.bind(this,view);
        // TODO Use fields...
        initView(view);
        return view;
    }

    private void initView(View view){
        job_want_bg.setOnClickListener(this);
        my_recruit_bg.setOnClickListener(this);
        person_bg.setOnClickListener(this);
        company_bg.setOnClickListener(this);
        pay_bg.setOnClickListener(this);
        logout_bg.setOnClickListener(this);
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
                break;
            //便名帖管理
            case R.id.company_bg:
                break;
            //充值
            case R.id.pay_bg:
                //跳转到充值界面
                break;

            //注销账号
            case R.id.logout_bg:
                makeText("注销账号成功");
                break;



        }
    }
}
