package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiZheng on 2017/7/3 0003.
 */
//兼职专区
public class PartTimeZoneActivity extends BaseActivity {
    @BindView(R.id.left_btn)
    Button leftBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_btn)
    Button rightBtn;
    @BindView(R.id.tv_left)
    Button tvLeft;
    @BindView(R.id.tv_right)
    Button tvRight;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.gv)
    ListView gv;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part_time_zone_layout);
        ButterKnife.bind(this);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("兼职专区");
        rightBtn.setVisibility(View.GONE);
        tvLeft.setText("兼职招聘");
        tvLeft.setOnClickListener(this);
        tvRight.setText("兼职求职");
        tvRight.setOnClickListener(this);
        tv1.setText("广德县");//获取的当前地理位置
        tv2.setText("全部");
        tv3.setText("更多");

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case  R.id.left_btn:
                finish();
                break;
            case R.id.tv_left:
                //刷新数据源
                break;
            case R.id.tv_right:
                //刷新数据源
                break;
        }
    }
}
