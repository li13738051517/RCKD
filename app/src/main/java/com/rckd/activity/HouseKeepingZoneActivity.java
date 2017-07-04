package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LiZheng on 2017/7/3 0003.
 */
//家政专区
public class HouseKeepingZoneActivity extends BaseActivity {
    @BindView(R.id.left_btn)
    Button leftBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_btn)
    Button rightBtn;
    //    @BindView(R.id.tab)
//    TabLayout tab;
    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.tv1)
    Button tv1;
    @BindView(R.id.tv2)
    Button tv2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.house_keep_layout, null);
        setContentView(view);
        ButterKnife.bind(this);
        leftBtn.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("家政专区");
        rightBtn.setVisibility(View.GONE);
        onViewClicked(view);
    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @OnClick({R.id.left_btn,R.id.tv1, R.id.tv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                break;
            case R.id.tv2:
                break;
            case R.id.left_btn:
                finish();
                break;
        }
    }

}

