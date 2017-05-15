package com.rckd.view;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import com.rckd.R;

import cn.bingoogolapple.progressbar.BGAProgressBar;

/**
 * Created by LiZheng on 2017/3/27 0027.
 * 进度条使用的是 https://github.com/bingoogolapple/BGAProgressBar-Android
 */

public class DownloadingDialog extends AppCompatDialog {
    private BGAProgressBar mProgressBar;
    public  DownloadingDialog(Context context){
        super(context, R.style.AppDialogTheme);
        setContentView(R.layout.dialog_downloading);
        mProgressBar = (BGAProgressBar) findViewById(R.id.pb_downloading_content);
        setCancelable(false);
    }
    public void setProgress(long progress, long maxProgress) {
        mProgressBar.setMax((int) maxProgress);
        mProgressBar.setProgress((int) progress);
    }

    @Override
    public void show() {
        super.show();
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);
    }
}
