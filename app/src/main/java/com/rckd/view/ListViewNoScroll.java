package com.rckd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by LiZheng on 2017/6/3 0003.
 * 自定义gridview，解决ScrollView中嵌套gridview显示不正常的问题（1行）
 */
public class ListViewNoScroll extends ListView {
    public  ListViewNoScroll(Context context) {
        // TODO Auto-generated method stub
        super(context);
    }

    public  ListViewNoScroll(Context context, AttributeSet attrs) {
        // TODO Auto-generated method stub
        super(context, attrs);
    }

    public ListViewNoScroll(Context context, AttributeSet attrs, int defStyle) {
        // TODO Auto-generated method stub
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
