package com.rckd.activity;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by LiZheng on 2017/6/24 0024.
 */
//消费记录-----------------界面可能需要重修改版
public class RecordActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);
        ButterKnife.bind(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
