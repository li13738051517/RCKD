package com.rckd.fragment.second.child.childpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rckd.R;
import com.rckd.activity.GotMeActivity;
import com.rckd.activity.LoginActivity;
import com.rckd.activity.MainActivity;
import com.rckd.application.AppConfig;

import timber.log.Timber;

import static com.rckd.base.BaseActivity.RESULT_CODE_BAR_AD;

/**
 * Created by LiZheng on 16/6/5.
 */
public class OtherPagerFragment extends com.rckd.base.BaseFragment {
    private String tag=OtherPagerFragment.class.getName();
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



    @Override
    public void onResume() {
        super.onResume();
        //----------------在前台时 ,通过判断是否已经登陆去
        //
        if (!AppConfig.isLogin) {
            showDialog();
        }
    }

    public void showDialog() {
        new MaterialDialog.Builder(baseActivity)
                .content(R.string.shareLocationPrompt)
                .cancelable(false)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivityForResult(LoginActivity.class,300);
                    }
                })
                .negativeText(R.string.disagree)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        reStartActivity();
                    }
                })
                .show();
    }
    private void reStartActivity() {
        Intent intent = new Intent(baseActivity, MainActivity.class);
        finish();
        startActivity(intent);
    }

    // 处理登陆页面返回数据值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_CODE_BAR_AD:
                //如果任没有登陆,让页面跳转到第一个Fragment
                if (!AppConfig.isLogin){
//                    startFragment(findFragment(FirstHomeFragment.class));
                    //请求
                    Timber.e(tag+"  onActivityResult RESULT_CODE_BAR_AD ",tag);
//                    startActivityForResult(LoginActivity.class,300);
                    showDialog();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
