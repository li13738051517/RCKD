package com.rckd.inter;

/**
 * Created by LiZheng on 2017/6/14 0014.
 */
//MSM 信息的监听
public interface ISMSListener {
    //验证码的接收器------------------非常重要
    public void onSmsReceive(String verifyCode);
}
