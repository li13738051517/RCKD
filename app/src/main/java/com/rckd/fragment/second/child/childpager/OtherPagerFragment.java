package com.rckd.fragment.second.child.childpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.activity.GotMeActivity;

/**
 * Created by LiZheng on 16/6/5.
 */
public class OtherPagerFragment extends com.rckd.base.BaseFragment {
    private static final String ARG_TYPE = "arg_pos";
    public static int TYPE_HOT = 1;
    public static int TYPE_FAV = 2;

    private int mType = TYPE_HOT;

    private TextView mTvTitle;

    LinearLayout linearLayout;
    public static OtherPagerFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        OtherPagerFragment fragment = new OtherPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(ARG_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment_second_pager_other, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        linearLayout=(LinearLayout)view.findViewById(R.id.lin);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GotMeActivity.class);
            }
        });
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);

        if (mType == TYPE_HOT) {
            mTvTitle.setText("热门");
        }
//        else {
//            mTvTitle.setText("收藏");
//        }
    }
}
