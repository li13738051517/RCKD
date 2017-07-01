package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.adpter.MyAdapter;
import com.rckd.base.BaseActivity;
import com.rckd.bean.UserOlde;
import com.rckd.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.list;

/**
 * Created by LiZheng on 2017/7/1 0001.
 */
//我要找的工作
public class WantedJobActivity extends BaseActivity implements View.OnClickListener  ,OnItemClickListener{


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
        setContentView(R.layout.wanted_job_layout);
        ButterKnife.bind(this);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("我要找的工作");
        rightBtn.setVisibility(View.GONE);
        tv1.setText("职位");
        tv1.setOnClickListener(this);
        tv2.setText("薪资范围");
        tv2.setOnClickListener(this);
        tv3.setText("工作经验");
        tv3.setOnClickListener(this);

//        listView.setAdapter(new MyAdapter(""));

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.tv1:
                //---------- 刷新相关数据

                break;

            case R.id.tv2:
                break;

            case R.id.tv3:
                break;

        }
    }


    @Override
    public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
        //listview的子选项爱你关卡
    }
}
