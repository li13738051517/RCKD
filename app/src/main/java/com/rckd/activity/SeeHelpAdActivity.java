package com.rckd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.rckd.R;
import com.rckd.adpter.MultipleItemQuickAdapter;
import com.rckd.base.BaseActivity;
import com.rckd.bean.DataServer;
import com.rckd.bean.MultipleItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiZheng on 2017/6/13 0013.
 */
//查看广而告之帖子
public class SeeHelpAdActivity extends BaseActivity implements View.OnClickListener {
    @Nullable@BindView(R.id.left_btn) Button leftBtn;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button rightBtn;
    @BindView(R.id.rv_list) RecyclerView mRecyclerView;
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }
    List<MultipleItem> data;
    MultipleItemQuickAdapter multipleItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_help_ad);
        ButterKnife.bind(this);
        leftBtn.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("打听求助");
        //先定位




        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("城市切换");
        rightBtn.setTextColor(getResources().getColor(R.color.white));
        rightBtn.setOnClickListener(this);

        data  = DataServer.getMultipleItemData(); //
        multipleItemAdapter = new MultipleItemQuickAdapter(this, data);
        GridLayoutManager manager = new GridLayoutManager(this,4);
        mRecyclerView.setLayoutManager(manager);
        multipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        mRecyclerView.setAdapter(multipleItemAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;
            case R.id.right_btn:
                startActivityForResult(CityListActivity.class ,null ,300);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
