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
//更改手机号码,更改用户id绑定的手机号
public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_num);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
        }
    }
}
