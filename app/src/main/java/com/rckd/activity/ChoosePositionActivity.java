package com.rckd.activity;

import com.rckd.R;
import com.rckd.adpter.MyAdapter;
import com.rckd.base.BaseActivity;
import com.rckd.bean.UserOlde;
import com.rckd.view.GridViewNoScroll;
import com.rckd.view.ListViewNoScroll;
import com.yanzhenjie.nohttp.NoHttp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rckd.R.id.gv1;

/**
 * Created by LiZheng on 2017/6/26 0026.
 */
//全职人才跳转界面选择职业.....
public class ChoosePositionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.left_btn) Button left;
    @BindView (R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;

    @BindView(R.id.tv1) TextView tv1;
    @BindView(R.id.tv2) TextView tv2;
    @BindView(R.id.tv3) TextView tv3;
    @BindView(R.id.gv1) ListViewNoScroll gv1;
    @BindView(R.id.gv2) ListViewNoScroll gv2;
    @BindView(R.id.gv3) ListViewNoScroll gv3;

    List<UserOlde> list=new ArrayList<>();//模拟数据

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_position_layout);
        ButterKnife.bind(this);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("全职职位");
        right.setVisibility(View.GONE);
        initList();//初始化数据

        gv1.setAdapter(new MyAdapter(this,list));
        gv2.setAdapter(new MyAdapter(this,list));
        gv3.setAdapter(new MyAdapter(this,list));
    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case  R.id.left_btn:
                finish();
                break;


        }
    }




    //----------------------------------以下仅仅只是测试数据--------------实际开发过程中请重接口处取得

    /**
     * 模拟假数据(原始数据)
     */
    public void initList(){
        for (int i=0;i<10;i++){
            List<String> lists=new ArrayList<>();
            list.add(new UserOlde("母"+i,lists));
            for (int j=0;j<4;j++){
                lists.add("母"+i+":子"+j);
            }
        }
    }







}
