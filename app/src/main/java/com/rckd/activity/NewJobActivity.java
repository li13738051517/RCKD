package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LiZheng on 2017/6/6 0006.
 */
//最新招聘
public class NewJobActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.left_btn)
    Button leftBtn;
    @BindView(R.id.title_et)
    AppCompatEditText title_et;
    @BindView(R.id.right_btn)
    Button rightBtn;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.lv)
    ListView lv;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_job_layout);
        ButterKnife.bind(this);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(this);
        rightBtn.setText("搜索");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.right_btn:
                makeText("正在搜索");

                break;
        }
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                break;
            case R.id.tv2:
                break;
            case R.id.tv3:
                break;
            case R.id.tv4:
                break;
        }
    }
}
