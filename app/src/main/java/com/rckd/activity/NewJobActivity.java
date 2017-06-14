package com.rckd.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baidu.location.h.j.B;

/**
 * Created by LiZheng on 2017/6/6 0006.
 */
//最新招聘
public class NewJobActivity extends BaseActivity  implements View.OnClickListener{
    @BindView(R.id.left_btn) Button leftBtn;
    @BindView(R.id.title_et) AppCompatEditText title_et;
    @BindView(R.id.right_btn) Button rightBtn;
//    @BindView(R.id.nice_spinner1) NiceSpinner nice_spinner1;


    List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_job_layout);
        ButterKnife.bind(this);

        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);

//        nice_spinner1.attachDataSource(dataset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;
            case R.id.right_btn:
                makeText("正在搜索");
                //post请求

                break;
        }
    }
}
