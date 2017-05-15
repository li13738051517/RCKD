package com.rckd.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rckd.R;
import com.rckd.base.BaseActivity;

/**
 * Created by LiZheng on 2017/3/28 0015.
 */
public class ScrollingActivity extends BaseActivity {

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}
