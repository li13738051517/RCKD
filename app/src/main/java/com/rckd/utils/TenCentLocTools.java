package com.rckd.utils;

import android.content.Context;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * Created by LiZheng on 2017/5/4 0004.
 */

public class TenCentLocTools implements TencentLocationListener {
    private static  String tag=TenCentLocTools.class.getName();
    String LocInfo;
    String task;
    Context mContext;
    TencentLocationManager mLocationManager;

    public String getLocInfo() {
        return LocInfo;
    }

    public void setLocInfo(String locInfo) {
        LocInfo = locInfo;
    }

    public TenCentLocTools(Context mContext) {
        this.mContext = mContext;
        mLocationManager = TencentLocationManager.getInstance(mContext);
    }

    public void init() {
        int error = TencentLocationManager.getInstance(mContext)
                .requestLocationUpdates(
                        TencentLocationRequest
                                .create().setInterval(5000)
                                .setRequestLevel(
                                        TencentLocationRequest.REQUEST_LEVEL_NAME), this);
        if (error == 0) {
            Log.e(tag+"监听状态:", "监听成功!");
        } else if (error == 1) {
            Log.e(tag+"监听状态:", "设备缺少使用腾讯定位SDK需要的基本条件");
        } else if (error == 2) {
            Log.e(tag+"监听状态:", "配置的 key 不正确");
        }
    }

    /**
     * @param location 新的位置
     * @param error    错误码
     * @param reason   错误描述
     */
    @Override
    public void onLocationChanged(TencentLocation location, int error,
                                  String reason) {
        // TODO Auto-generated method stub
        if (TencentLocation.ERROR_OK == error) {
            double lat = location.getLatitude();// 纬度
            double lot = location.getLongitude();// 经度
            double altitude = location.getAltitude();// 海拔
            float accuracy = location.getAccuracy();// 精度
            String nation = location.getNation();// 国家
            String province = location.getProvince();// 省份
            String city = location.getCity();// 城市
            String district = location.getDistrict();// 区
            String town = location.getTown();// 镇
            String village = location.getVillage();// 村
            String street = location.getStreet();// 街道
            String streetNo = location.getStreetNo();// 门号

            Log.e("定位信息:", lat + " | " + lot + " | " + altitude + " | "
                    + accuracy + " | " + nation + " | " + province + " | "
                    + city + " | " + district + " | " + town + " | " + village
                    + " | " + street + " | " + streetNo);

            task = lat + " | " + lot + " | " + altitude + " | " + accuracy
                    + " | " + nation + " | " + province + " | " + city + " | "
                    + district + " | " + town + " | " + village + " | "
                    + street + " | " + streetNo + " | ";
            //注意定位方式跟自己在前面设置的参数有关
            //其中地球坐标情况下,不支持反地理编码,但支持gps定位
            //反地理编码，就必须把坐标改成火星坐标，mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);无法使用纯GPS定位
            String provider = location.getProvider();//定位方式
            if (TencentLocation.GPS_PROVIDER.equals(provider)) {
                // location 是GPS定位结果
                Log.e("定位方式:", "GPS");
                Log.e(tag,"腾讯定位:" + task + "GPS");
            } else if (TencentLocation.NETWORK_PROVIDER.equals(provider)) {
                // location 是网络定位结果
                Log.e("定位方式:", "NetWork");
                Log.e(tag,"腾讯定位:" + task + "NetWork" + " | " + location.getAddress());
                Log.e("地址:", location.getAddress());
            }
            mLocationManager.removeUpdates(this);
        } else {
            Log.e("reason:", reason);
            Log.e("error:", error + "");
        }

    }

    /**
     * @param name   GPS，Wi-Fi等
     * @param status 新的状态, 启用或禁用
     * @param desc   状态描述
     */
    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        // TODO Auto-generated method stub
        Log.e("name:", name);
        Log.e("status:", "" + status);
        Log.e("desc:", desc);
    }

    public void destroyLocManager() {
        if (mLocationManager != null)
            mLocationManager.removeUpdates(this);
        mLocationManager = null;
    }

}
