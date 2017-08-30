package com.zh.metermanagementcw.bean;

/**
 * Created by Administrator on 2017/8/25.
 */

public class ConcentratorBean {

    /** 序号(数据库自动生成) */
    private String _id;
    /** 资产编号(集中器) */
    private String assetNumbers;
    /** 纬度 */
    private String latitude;
    /** 经度 */
    private String longitude;
    /** 抄表区段 */
    private String theMeteringSection;
    /** 地址 */
    private String addr;


    /** 序号(数据库自动生成) */
    public String get_id() {
        return _id;
    }
    /** 序号(数据库自动生成) */
    public void set_id(String _id) {
        this._id = _id;
    }

    /** 资产编号(集中器) */
    public String getAssetNumbers() {
        return assetNumbers;
    }
    /** 资产编号(集中器) */
    public void setAssetNumbers(String assetNumbers) {
        this.assetNumbers = assetNumbers;
    }

    /** 纬度 */
    public String getLatitude() {
        return latitude;
    }
    /** 纬度 */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /** 经度 */
    public String getLongitude() {
        return longitude;
    }
    /** 经度 */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    /** 抄表区段 */
    public String getTheMeteringSection() {
        return theMeteringSection;
    }
    /** 抄表区段 */
    public void setTheMeteringSection(String theMeteringSection) {
        this.theMeteringSection = theMeteringSection;
    }

    /** 地址 */
    public String getAddr() {
        return addr;
    }
    /** 地址 */
    public void setAddr(String addr) {
        this.addr = addr;
    }


    @Override
    public String toString() {
        return "ConcentratorBean{" +
                "_id='" + _id + '\'' +
                ", assetNumbers='" + assetNumbers + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", theMeteringSection='" + theMeteringSection + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}
