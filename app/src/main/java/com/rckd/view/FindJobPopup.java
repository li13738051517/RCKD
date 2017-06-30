package com.rckd.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import com.rckd.R;
import com.rckd.utils.ToastUtils;

import razerdp.basepopup.BasePopupWindow;


/**
 * Created by LiZheng on 2016/6/20.
 * 从底部滑上来的popup ,实现求职贴类型管理
 */
public class FindJobPopup extends BasePopupWindow implements View.OnClickListener {


    private View popupView;

    public FindJobPopup(Activity context) {
        super(context);
        bindEvent();
    }
    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_find_job , null);
        return popupView;
    }

    @Override
    public View initAnimaView() {

        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
            popupView.findViewById(R.id.dissmiss).setOnClickListener(this);
        }
    }
   Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1:
                ToastUtils.ToastMessage(getContext(), "click tx_1");
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(getContext(), "click tx_2");
                break;
            case R.id.tx_3:
                ToastUtils.ToastMessage(getContext(),"click tx_3");
                break;
            case R.id.dissmiss:
                dismiss();
                break;
            default:
                break;
        }
    }

    public View getView(){
        return popupView;
    }
}
