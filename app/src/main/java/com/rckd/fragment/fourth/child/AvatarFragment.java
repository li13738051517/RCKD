package com.rckd.fragment.fourth.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rckd.R;

import org.bouncycastle.jce.provider.BrokenJCEBlockCipher;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LiZheng on 16/6/6.
 */
public class AvatarFragment extends com.rckd.base.BaseFragment implements View.OnClickListener {

    public static AvatarFragment newInstance() {
        Bundle args = new Bundle();
        AvatarFragment fragment = new AvatarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @BindView(R.id.image_icon) de.hdodenhof.circleimageview.CircleImageView image_icon;
    @BindView(R.id.tv_phne) TextView tv_phne;
    @BindView(R.id.tv_num) TextView tv_num;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment_fourth_avatar, container, false);
        ButterKnife.bind(view);
        return view;
    }


    //具体功能
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_person_resume:
                break;

            case R.id.tv_bar:
                break;

            case R.id.company:
                break;


            case R.id.take_job:
                break;

            case R.id.bar_job:
                break;

            case R.id.see_job:
                break;

            case R.id.advertise:
                break;

            case  R.id.join_mq:
                break;


            case R.id.update_psd:
                break;


            case R.id.record:
                break;

            case R.id.change_phone:
                break;

            case  R.id.bar:
                break;

            case R.id.charge:
                break;
            case R.id.logout:
                //注销
               break;


        }
    }
}
