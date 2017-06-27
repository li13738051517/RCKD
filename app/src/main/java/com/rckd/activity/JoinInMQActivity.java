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

/**
 * Created by LiZheng on 2017/6/23 0023.
 */
//加入名企 ----------名企信息
public class JoinInMQActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Nullable@BindView(R.id.left_btn) Button leftBtn;
    @Nullable@BindView(R.id.title_text) TextView title;
    @Nullable@BindView(R.id.right_btn) Button right;

    @Nullable@BindView(R.id.bt) Button bt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_in_mq_layout);
        ButterKnife.bind(this);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("名企信息");
        right.setVisibility(View.GONE);
        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        super.onClick(v);
            switch (v.getId()){
                case R.id.left_btn:
                    finish();
                    break;
                case R.id.bt:
                    //----------------------
                    //---------------------跳转到充值界面

                    finish();
                    break;


            }
    }
}
