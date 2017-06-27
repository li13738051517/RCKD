package com.rckd.activity;

import com.rckd.base.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by LiZheng on 2017/6/26 0026.
 */
//全职人才跳转界面选择职业.....
public class ChoosePositionActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(null);

    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

}
