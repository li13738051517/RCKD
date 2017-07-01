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
 * Created by LiZheng on 2017/7/1 0001.
 */
//我要找的人
public class WantedPersonActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.left_btn)
    Button leftBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_btn)
    Button rightBtn;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.list_view)
    ListView listView;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wanted_person_layout);
        ButterKnife.bind(this);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("我要找的人才");
        rightBtn.setVisibility(View.GONE);
        tv1.setText("职位");
        tv1.setOnClickListener(this);
        tv2.setText("工作经验");
        tv2.setOnClickListener(this);
        tv3.setText("年龄");
        tv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
            switch (v.getId()){
                case R.id.left_btn:
                    finish();
                    break;
                case R.id.tv1:
                    break;
                case R.id.tv2:
                    break;
                case R.id.tv3:
                    break;

            }
    }
}
