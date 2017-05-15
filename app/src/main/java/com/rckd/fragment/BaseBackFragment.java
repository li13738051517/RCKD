package com.rckd.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rckd.R;
import com.rckd.base.BaseFragment;


/**
 * Created by LiZheng on 16/2/7.
 */
public class BaseBackFragment extends BaseFragment {

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.onBackPressed();
            }
        });
    }
}
