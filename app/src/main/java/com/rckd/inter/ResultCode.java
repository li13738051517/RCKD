package com.rckd.inter;

import android.support.annotation.IntDef;

import com.rckd.base.BaseFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by LiZheng on 2017/3/21 0021.
 */
@IntDef({BaseFragment.RESULT_OK, BaseFragment.RESULT_CANCELED})
@Retention(RetentionPolicy.SOURCE)
public @interface ResultCode {
}
