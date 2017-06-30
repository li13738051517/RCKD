package com.rckd.activity;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiZheng on 2017/6/29 0029.
 */
//人才入库
public class TalentPersonActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //使用外部类和接口方式 是为了 让共有部分实现相同的逻辑
        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;
            case R.id.button:
                //--------------------------
                //----------------------------请求之后 ,结束掉当前   Activity

                finish();
                break;
        }
    }

    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;

    @BindView(R.id.button) Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_job_liepin);
        ButterKnife.bind(this);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("工作意向");
        right.setVisibility(View.GONE);
        //        right.setOnClickListener(null);
        button.setOnClickListener(this);

    }
}
