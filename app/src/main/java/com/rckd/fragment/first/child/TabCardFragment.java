package com.rckd.fragment.first.child;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.adpter.BooksAdapter;
import com.rckd.base.BaseFragment;
import com.rckd.bean.Book;
import com.rckd.bean.BooksDataSource;
import com.rckd.bean.DataServer;
import com.shizhefei.mvc.IDataSource;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCSwipeRefreshHelper;

import java.util.List;


/**
 * Created by LiZheng on 2017/6/13 0013.
 */

public class TabCardFragment  extends BaseFragment{
    private   String mTitle;
    private MVCHelper<List<Book>> mvcHelper;

    BooksDataSource booksDataSource=new BooksDataSource();
//    View v;
    public static TabCardFragment getInstance(String title) {
        TabCardFragment sf = new TabCardFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View   v = inflater.inflate(R.layout.fr_simple_card, null);
        TextView card_title_tv = (TextView) v.findViewById(R.id.card_title_tv);
        card_title_tv.setText(mTitle);

        //-------------------
        ListView listView = (ListView) v.findViewById(R.id.listView);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mvcHelper = new MVCSwipeRefreshHelper<>(swipeRefreshLayout);
        // 设置数据源
        mvcHelper.setDataSource(booksDataSource);
        // 设置适配器
        mvcHelper.setAdapter(new BooksAdapter(baseActivity));
        // 加载数据
        mvcHelper.refresh();
        listView.setOnItemClickListener(onItemClickListener);
        setListViewHeightBasedOnChildren1(listView);
        return v;
    }
    private AdapterView.OnItemClickListener onItemClickListener  = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //点击每一项的时候

        }
    };


    public  static int  setListViewHeightBasedOnChildren1(ListView listView){
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        return params.height;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放资源
        mvcHelper.destory();
    }
}
