package com.rckd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ScrollView;

/**
 * Created by LiZheng on 2017/5/13 0013.
 *
 * 自定义gridview，解决ScrollView中嵌套gridview显示不正常的问题（1行）
 */
public class GridViewNoScroll extends GridView {

    public GridViewNoScroll(android.content.Context context,
                            android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewNoScroll(Context context){
        super(context);
    }

    public  GridViewNoScroll(Context context, AttributeSet attrs, int defStyle){
        super(context,  attrs,  defStyle);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
