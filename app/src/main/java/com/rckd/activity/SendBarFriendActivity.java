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
 * Created by LiZheng on 2017/5/8 0008.
 */
//交友征婚
public class SendBarFriendActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title_text;
    @BindView(R.id.right_btn) Button right;
    @BindView(R.id.text_tie) TextView text_tie;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test_for_sign);
        setContentView(R.layout.activity_find_friend);
        ButterKnife.bind(this);

    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.left_btn:
                finish();
                break;

        }

    }
}
