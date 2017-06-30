package com.rckd.bean;

import java.util.List;

/**
 * 原始数据
 * Created by WangCheng on 2017/6/27.
 */
public class UserOlde {
    private String name;
    private List<String> child;//子数据

    public UserOlde(String name,List<String> child){
        this.name=name;
        this.child=child;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChild() {
        return child;
    }

    public void setChild(List<String> child) {
        this.child = child;
    }
}
