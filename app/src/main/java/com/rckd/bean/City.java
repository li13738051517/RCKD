package com.rckd.bean;

/**
 * 新建城市类，根据城市名字和  拼音 字符去从接口中查找数据
 *  Created by LiZheng on 2015/9/24 0024.
 *  可以将CityBean中的数据添加
 */
public class City {
    public String name;
    public String pinyi;

    public City(String name, String pinyi) {
//        super();
        this.name = name;
        this.pinyi = pinyi;
    }

    public City() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyi() {
        return pinyi;
    }

    public void setPinyi(String pinyi) {
        this.pinyi = pinyi;
    }

}
