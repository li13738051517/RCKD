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
 * Created by LiZheng on 2017/6/19 0019.
 */
//查看顺风拼车的帖子
public class SeeCarAdActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.left_btn) Button leftBtn;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button rightBtn;
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_car_layout);
        ButterKnife.bind(this);
        leftBtn.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("顺风拼车");
        //先定位
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("城市切换");
        rightBtn.setTextColor(getResources().getColor(R.color.white));
        rightBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.left_btn:
                finish();
                break;
            case R.id.right_btn:
                startActivityForResult(CityListActivity.class ,null ,300);
                break;
        }
    }
}
