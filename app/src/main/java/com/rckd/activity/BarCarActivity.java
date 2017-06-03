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
//顺风拼车
public class BarCarActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.left_btn) Button left_btn;

    @BindView(R.id.title_text)TextView title;

    @BindView(R.id.right_btn)Button right_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_bar );
        ButterKnife.bind(this);
        title.setText("顺风拼车");


    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;

        }
    }
}
