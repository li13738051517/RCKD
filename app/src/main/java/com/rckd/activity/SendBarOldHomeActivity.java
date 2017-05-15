package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rckd.R;
import com.rckd.base.BaseActivity;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */

public class SendBarOldHomeActivity extends BaseActivity{
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendbarad);
    }
}
