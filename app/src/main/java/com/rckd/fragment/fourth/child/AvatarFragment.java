package com.rckd.fragment.fourth.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rckd.R;

import org.bouncycastle.jce.provider.BrokenJCEBlockCipher;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LiZheng on 16/6/6.
 */
public class AvatarFragment extends com.rckd.base.BaseFragment implements  View.OnClickListener {

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
            case R.id.image_icon:
                makeText("更换头像成功!!!");
                break;
            //我的求职
            case R.id.job_want_bg:
                //--------------

                break;
            //我的招聘
            case R.id.my_recruit_bg:
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
