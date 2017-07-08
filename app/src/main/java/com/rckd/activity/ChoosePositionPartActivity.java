package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;
import com.rckd.view.GridViewNoScroll;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiZheng on 2017/6/29 0029.
 */
//兼职职位
public class ChoosePositionPartActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.line) LinearLayout line;
    @BindView(R.id.titleView) RelativeLayout titleView;
    @BindView(R.id.sh) EditText sh;
    @BindView(R.id.codebtn) Button codebtn;
    @BindView(R.id.psdbg) RelativeLayout psdbg;
    @BindView(R.id.tv1) TextView tv1;

    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;
    @BindView(R.id.gv1) GridViewNoScroll gv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_position_part_layout);
        ButterKnife.bind(this);
        //-------------------
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("兼职职位");
        right.setVisibility(View.GONE);
        //---------------------
        //------------------------兼职职位列表

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
        }
    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }
}
