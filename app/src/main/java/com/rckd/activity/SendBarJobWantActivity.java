package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */

public class SendBarJobWantActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    View view;
    LayoutInflater inflater;

    TextView text1;
    TextView text2;


    Button left_btn;
    TextView title_text;
    Button right_btn;

    FrameLayout frameLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_sendbar_ac);
        text1 = (TextView) findViewById(R.id.text1);
        text1.setVisibility(View.VISIBLE);
        text1.setText("全职招聘");
        text1.setOnClickListener(this);

        text2 = (TextView) findViewById(R.id.text2);
        text2.setVisibility(View.VISIBLE);
        text2.setText("兼职招聘");
        text2.setOnClickListener(this);


        left_btn = (Button) findViewById(R.id.left_btn);
        left_btn.setVisibility(View.VISIBLE);
        left_btn.setOnClickListener(this);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setVisibility(View.VISIBLE);
        title_text.setText("选择发布类型");
        frameLayout =(FrameLayout) findViewById(R.id.frame);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.left_btn:
                finish();
                break;
//全职招聘
            case R.id.text1:
                inflater = LayoutInflater.from(this);
                view = inflater.inflate(R.layout.activity_sendbarad, null);
                frameLayout.addView(view);
                frameLayout.setVisibility(View.VISIBLE);
                title_text.setText("全职工作");
                //当利用帧布局加载后  ,需要将   原来的布局 隐藏掉
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                break;

//兼职招聘
            case R.id.text2:
                inflater = LayoutInflater.from(this);
                view = inflater.inflate(R.layout.activity_sendbarad, null);
                //view的相关事件可以写在此处


                frameLayout.addView(view);
                title_text.setText("兼职工作");
                frameLayout.setVisibility(View.VISIBLE);

                //当利用帧布局加载后  ,需要将   原来的布局 隐藏掉
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
