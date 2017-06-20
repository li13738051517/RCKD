package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rckd.R;
import com.rckd.adpter.BooksAdapter;
import com.rckd.base.BaseActivity;
import com.rckd.bean.Book;
import com.rckd.bean.BooksDataSource;
import com.rckd.task.InitTokenTask;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCPullrefshHelper;
import com.shizhefei.task.datasource.DataSources;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiZheng on 2017/6/19 0019.
 */
//我收到的简历
public class GotMeActivity extends BaseActivity  implements View.OnClickListener{
     MVCHelper<List<Book>> mvcHelper;
    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;
    @BindView(R.id.pullToRefreshListView) PullToRefreshListView pullToRefreshListView;//下拉刷新和加载更多
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.got_me_activity);
        ButterKnife.bind(this);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("我收到的简历");
        right.setVisibility(View.GONE);

        //-----------------mvchelper的形式
        //加载布局控件
        mvcHelper = new MVCPullrefshHelper<>(pullToRefreshListView);
        // 设置数据源
        mvcHelper.setDataSource(DataSources.concatWith(new InitTokenTask(), new BooksDataSource()));
        // 设置适配器
        mvcHelper.setAdapter(new BooksAdapter(this));
        // 加载数据
        mvcHelper.refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.left_btn:
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvcHelper.destory();
    }
}
