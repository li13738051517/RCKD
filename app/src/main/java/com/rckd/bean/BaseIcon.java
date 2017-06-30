package com.rckd.bean;

import cn.addapp.pickers.picker.DateTimePicker;
import lombok.Data;

/**
 * Created by LiZheng on 2015/9/24 0024.
 * 请注意google官方推荐是不要写 set  和get而是 直接暴露在外露
 *原因
 * 1,占用资源小
 * 2,方便使用
 */
//@Data
public   class BaseIcon {
    private int iId;
    private String iName;

    public BaseIcon() {
    }

    public BaseIcon(int iId, String iName) {
        this.iId = iId;
        this.iName = iName;
    }

    public int getiId() {
        return iId;
    }

    public String getiName() {
        return iName;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}
