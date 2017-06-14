package com.rckd.fragment.first.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseFragment;

import static com.rckd.R.mipmap.sf;

/**
 * Created by LiZheng on 2017/6/13 0013.
 */

public class TabCardFragment  extends BaseFragment{
    private   String mTitle;

    public static TabCardFragment getInstance(String title) {
        TabCardFragment sf = new TabCardFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_simple_card, null);
        TextView card_title_tv = (TextView) v.findViewById(R.id.card_title_tv);
        card_title_tv.setText(mTitle);
        return v;
    }


}
