package com.rckd.activity;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rckd.R.id.bt;

/**
 * Created by LiZheng on 2017/6/26 0026.
 */
//名企招聘
public class SeeMQRecruitActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;
    @BindView(R.id.bt) Button bt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_mq_recruit_layout);
        ButterKnife.bind(this);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.GONE);
        title.setText("名企招聘");
        right.setVisibility(View.GONE);

        bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;
            case R.id.bt:

                startActivity(JoinInMQActivity.class);
                finish();
                break;
        }
    }
}
