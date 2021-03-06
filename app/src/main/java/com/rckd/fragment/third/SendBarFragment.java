package com.rckd.fragment.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rckd.R;
import com.rckd.base.BaseMainFragment;
import com.rckd.fragment.third.child.ShopFragment;

/**
 * Created by LiZheng on 16/6/3.
 */
public class SendBarFragment extends BaseMainFragment {

    public static SendBarFragment newInstance() {

        Bundle args = new Bundle();

        SendBarFragment fragment = new SendBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhihu_fragment_third, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            // ShopFragment是flow包里的
            loadRootFragment(R.id.fl_third_container, ShopFragment.newInstance());
        } else { // 这里可能会出现该Fragment没被初始化时,就被强杀导致的没有load子Fragment
            if (findChildFragment(ShopFragment.class) == null) {
                loadRootFragment(R.id.fl_third_container, ShopFragment.newInstance());
            }
        }
    }
}
