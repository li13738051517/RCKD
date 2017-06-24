package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiZheng on 2017/6/21 0021.
 */
//查看已收藏的简历
public class SeeCollectedCVActivity extends BaseActivity implements View.OnClickListener{

    //-----------------------
    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;

//    @BindView(R.id.photo) Button photo;
//    @BindView(R.id.button) Button commitBtn;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_collected_cv_layout);
        ButterKnife.bind(this);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("查看已收藏的简历");
        right.setVisibility(View.GONE);
//        commitBtn.setOnClickListener(this);
//        photo.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
//        super.onClick(v);
        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;
//            case R.id.button:
//                //-------------------
//                //--------------------------ti
//                //----------------------帖子传值类型
//                //我的招聘贴(全职)
//                startActivity(SeeMyRecruitmentPostActivity.class);
//                finish();
//                break;
//            case R.id.photo:
//                break;

        }
    }
}
