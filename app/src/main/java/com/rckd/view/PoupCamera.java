package com.rckd.view;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.rckd.R;

import razerdp.basepopup.BasePopupWindow;
import timber.log.Timber;

/**
 * Created by LiZheng on 2017/6/1 0001.
 */

public class PoupCamera extends BasePopupWindow  {
    private String tag=PoupCamera.class.getName();
    private Context context;
    private View popupView;
    public   PoupCamera(Activity context){
        super(context);
        bindEvent();
        Timber.e(tag+" con context init");
    }

    public   PoupCamera(Activity activity , Context context){
        super(activity);
        bindEvent();
        this.context=context;
        Timber.e(tag+" con  activity , context  init");
    }


    public void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tv_camera);
            popupView.findViewById(R.id.tv_camera_ku);
//            popupView.findViewById(R.id.checkBox);
        }
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_camera, null);
        return popupView;
    }

//    public View onCreatePopupView(int resLayout) {
//        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_camera, null);
//        return popupView;
//    }


    @Override
    public View initAnimaView() {
            return popupView.findViewById(R.id.popup_anima);
    }



    public View getView(){
        return  popupView;
    }

    @Override
    protected Animation initShowAnimation() {
        //start .end .dur
        return getTranslateAnimation(250 * 2, 0, 300);
    }


    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//
//        }
//    }
}
