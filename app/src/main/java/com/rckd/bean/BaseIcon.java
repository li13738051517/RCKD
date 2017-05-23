package com.rckd.bean;

/**
 * Created by LiZheng on 2015/9/24 0024.
 */
public class BaseIcon {
    private int iId;
    private String iName;

    public BaseIcon() {
    }

    public BaseIcon(int iId, String iName) {
        this.iId = iId;
        this.iName = iName;
    }

    public BaseIcon(String iName, int iId) {
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
