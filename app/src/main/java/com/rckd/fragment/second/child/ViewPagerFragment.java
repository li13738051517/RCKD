package com.rckd.fragment.second.child;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rckd.R;
import com.rckd.activity.LoginActivity;
import com.rckd.activity.MainActivity;
import com.rckd.adpter.ZhihuPagerFragmentAdapter;
import com.rckd.application.AppConfig;

import timber.log.Timber;

import static com.rckd.base.BaseActivity.RESULT_CODE_BAR_AD;

/**
 * Created by LiZheng on 16/6/5.
 */
public class ViewPagerFragment extends com.rckd.base.BaseFragment {
    private  String  tag=ViewPagerFragment.class.getName();
    private TabLayout mTab;
    private ViewPager mViewPager;

    public static ViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment_second_pager, container, false);
        initView(view);
        return view;
    }



    private void initView(View view) {
        mTab = (TabLayout) view.findViewById(R.id.tab);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
//        mTab.addTab(mTab.newTab());
        mViewPager.setAdapter(new ZhihuPagerFragmentAdapter(getChildFragmentManager()));
        mTab.setupWithViewPager(mViewPager);
    }
}
