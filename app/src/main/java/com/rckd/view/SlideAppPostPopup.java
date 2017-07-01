package com.rckd.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

//import com.blankj.utilcode.util.ToastUtils;
import com.rckd.R;
import com.rckd.activity.SeeMyPositionFullTimeActivity;
import com.rckd.activity.SeeMyPositionPartTimeActivity;
import com.rckd.utils.ToastUtils;

import razerdp.basepopup.BasePopupWindow;

import static com.baidu.location.h.j.t;
//import razerdp.basepopup.R;
//import razerdp.demo.utils.ToastUtils;

//import static com.baidu.location.h.j.R;

/**
 * Created by LiZheng on 2016/6/20.
 * 从底部滑上来的popup ,实现求职贴类型管理
 */
public class SlideAppPostPopup extends BasePopupWindow implements View.OnClickListener {

//    Handler handler;
    private View popupView;
//    Activity activity;
    public SlideAppPostPopup(Activity context) {
        super(context);
//        activity=context;
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
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_slide_application_post , null);
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
            popupView.findViewById(R.id.dissmiss).setOnClickListener(this);
//            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
        }

    }
   Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1:
                ToastUtils.ToastMessage(getContext(), "click tx_1");

//                intent=new Intent(activity, SeeMyPositionFullTimeActivity.class);
////intent 可以添加bundle
//                activity.startActivity(intent);
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(getContext(), "click tx_2");
//                intent=new Intent(activity, SeeMyPositionPartTimeActivity.class);
//intent 可以添加bundle
//                activity.startActivity(intent);
                break;
            //
            case R.id.dissmiss:
//                activity.finish();
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
