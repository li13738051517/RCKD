package com.rckd.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import lombok.Data;

/**
 * Created by LiZheng  on 17/05/24
 * 此类主要是为了将解析的数据的对象.
 */

public class  ProvinceBean implements IPickerViewData {
    private long id;  //id   每一个省市县/区对应一个 id
    private String name;
    private String description;
    private String others;

    public ProvinceBean(long id, String name, String description, String others){
        this.id = id;
        this.name = name;
        this.description = description;
        this.others = others;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过getPickerViewText方法获取字符串显示出来。
    @Override
    public String getPickerViewText() {
        return name;
    }
}
