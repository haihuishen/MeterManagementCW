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

    //----2017/09/04
    /** 电表表脚封扣（条码） */
    private String meterFootNumbers;
    /** 拍照图片的路径(电表表脚封扣) */
    private String meterFootPicPath;
    /** 表箱封扣1（条码） */
    private String meterBodyNumbers1;
    /** 拍照图片的路径(表箱封扣1) */
    private String meterBodyPicPath1;
    /** 表箱封扣2（条码） */
    private String meterBodyNumbers2;
    /** 拍照图片的路径(表箱封扣2) */
    private String meterBodyPicPath2;

    /** 拍照图片的路径(集中器) */
    private String picPath;


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


    //---- 2017/09/04
    /** 电表表脚封扣（条码） */
    public String getMeterFootNumbers() {
        return meterFootNumbers;
    }
    /** 电表表脚封扣（条码） */
    public void setMeterFootNumbers(String meterFootNumbers) {
        this.meterFootNumbers = meterFootNumbers;
    }

    /** 拍照图片的路径(电表表脚封扣) */
    public String getMeterFootPicPath() {
        return meterFootPicPath;
    }
    /** 拍照图片的路径(电表表脚封扣) */
    public void setMeterFootPicPath(String meterFootPicPath) {
        this.meterFootPicPath = meterFootPicPath;
    }

    /** 表箱封扣1（条码） */
    public String getMeterBodyNumbers1() {
        return meterBodyNumbers1;
    }
    /** 表箱封扣1（条码） */
    public void setMeterBodyNumbers1(String meterBodyNumbers1) {
        this.meterBodyNumbers1 = meterBodyNumbers1;
    }

    /** 拍照图片的路径(表箱封扣1) */
    public String getMeterBodyPicPath1() {
        return meterBodyPicPath1;
    }
    /** 拍照图片的路径(表箱封扣1) */
    public void setMeterBodyPicPath1(String meterBodyPicPath1) {
        this.meterBodyPicPath1 = meterBodyPicPath1;
    }

    /** 表箱封扣2（条码） */
    public String getMeterBodyNumbers2() {
        return meterBodyNumbers2;
    }
    /** 表箱封扣2（条码） */
    public void setMeterBodyNumbers2(String meterBodyNumbers2) {
        this.meterBodyNumbers2 = meterBodyNumbers2;
    }

    /** 拍照图片的路径(表箱封扣2) */
    public String getMeterBodyPicPath2() {
        return meterBodyPicPath2;
    }
    /** 拍照图片的路径(表箱封扣2) */
    public void setMeterBodyPicPath2(String meterBodyPicPath2) {
        this.meterBodyPicPath2 = meterBodyPicPath2;
    }

    /** 拍照图片的路径(集中器) */
    public String getPicPath() {
        return picPath;
    }
    /** 拍照图片的路径(集中器) */
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }


    @Override
    public String toString() {
        return "ConcentratorBean{" +
                "_id='" + _id + '\'' +
                ", assetNumbers='" + assetNumbers + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", theMeteringSection='" + theMeteringSection + '\'' +
                ", addr='" + addr + '\'' + "\n" +
                ", meterFootNumbers='" + meterFootNumbers + '\'' +
                ", meterFootPicPath='" + meterFootPicPath + '\'' + "\n" +
                ", meterBodyNumbers1='" + meterBodyNumbers1 + '\'' +
                ", meterBodyPicPath1='" + meterBodyPicPath1 + '\'' + "\n" +
                ", meterBodyNumbers2='" + meterBodyNumbers2 + '\'' +
                ", meterBodyPicPath2='" + meterBodyPicPath2 + '\'' + "\n" +
                ", picPath='" + picPath + '\'' +
                '}';
    }
}
