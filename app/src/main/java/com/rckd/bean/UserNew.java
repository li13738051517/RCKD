package com.rckd.bean;

/**
 * 整理后的数据（父子数据共用）
 * Created by WangCheng on 2017/6/27.
 */
public class UserNew {
    private String name;//名称
    private boolean isOne;//是否是一级目录
    private boolean isClin=false;//是否是选中状态，默认false
    private String parent;//子类记录父类，父类这个值为null

    public UserNew(String name,boolean isOne,String parent){
        this.name=name;
        this.isOne=isOne;
        this.parent=parent;
    }
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOne() {
        return isOne;
    }

    public void setOne(boolean one) {
        isOne = one;
    }

    public boolean isClin() {
        return isClin;
    }

    public void setClin(boolean clin) {
        isClin = clin;
    }

    @Override
    public String toString() {
        return "name:"+name+"|isOne:"+isOne+"|isClin:"+isClin+"|arent:"+parent;
    }
}
