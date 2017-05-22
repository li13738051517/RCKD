package com.rckd.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import com.rckd.R;

/**
 * Created by LiZheng on 2017/5/3 0003.
 */

public class SlideFromBottonPoup extends razerdp.basepopup.BasePopupWindow implements View.OnClickListener {
    protected View popupView;
    protected Context mContext;
    Button button2;

    public SlideFromBottonPoup(Activity activity) {
        super(activity);
        bindEvent();
    }

    public void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.button2).setOnClickListener(this);
            popupView.findViewById(R.id.grid_photo);
        }
    }

    public SlideFromBottonPoup(Activity activity, Context context) {
        super(activity);
        mContext = context;
    }

    //2
    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.ft_pop_win, null);
        return popupView;
    }

    //3
    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);

    }

    @Override
    protected Animation initShowAnimation() {

        return getTranslateAnimation(250 * 2, 0, 300);
//        return  null;
    }


    //动画效果
    @Override
    protected Animator initShowAnimator() {
        return super.initShowAnimator();
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(ObjectAnimator.ofFloat(mAnimaView, "rotationX", 90f, 0f).setDuration(400),
//                ObjectAnimator.ofFloat(mAnimaView, "translationY", 250f, 0f).setDuration(400),
//                ObjectAnimator.ofFloat(mAnimaView, "alpha", 0f, 1f).setDuration(400 * 3 / 2));
//        return set;
    }

    //注意此处这样写,是为了测试buttton的功能
    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
//        return getPopupWindowView();
    }

    public View getView() {
        return popupView;
    }


    @Override
    public void onClick(View v) {
        if (popupView != null) {
            switch (v.getId()) {
                case R.id.button2:
//                    button2.setBackgroundColor();
                    dismiss();
            }
        }
    }
}
