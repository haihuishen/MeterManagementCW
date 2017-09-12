package com.zh.metermanagementcw.bean;

/**
 * 零散新装
 */
public class ScatteredNewMeterBean {
    /** 序号(数据库自动生成) */
    String _id;
    /** 用户编号 */
    String userNumber;
    /** 用户名称 */
    String userName;
    /** 用户地址 */
    String userAddr;
    /** 用户电话 */
    String userPhone;

    /** 新表表地址(需扫描) */
    String newAddr;
    /** 新表资产编号(需扫描) */
    String newAssetNumbers;
    /** 新电能表止码-电量(需扫描) */
    String newElectricity;

    /** 电表表脚封扣（条码） */
    String meterFootNumbers;
    /** 拍照图片的路径(电表表脚封扣) */
    String meterFootPicPath;
    /** 表箱封扣1（条码） */
    String meterBodyNumbers1;
    /** 拍照图片的路径(表箱封扣1) */
    String meterBodyPicPath1;
    /** 表箱封扣2（条码） */
    String meterBodyNumbers2;
    /** 拍照图片的路径(表箱封扣2) */
    String meterBodyPicPath2;

    /** 拍照图片的路径(变压器) */
    String picPath;


    //-------------------------------------------------------------
    /** 序号(数据库自动生成) */
    public String get_id() {
        return _id;
    }
    /** 序号(数据库自动生成) */
    public void set_id(String _id) {
        this._id = _id;
    }

    /** 用户编号 */
    public String getUserNumber() {
        return userNumber;
    }
    /** 用户编号 */
    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    /** 用户名称 */
    public String getUserName() {
        return userName;
    }
    /** 用户名称 */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** 用户地址 */
    public String getUserAddr() {
        return userAddr;
    }
    /** 用户地址 */
    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    /** 用户电话 */
    public String getUserPhone() {
        return userPhone;
    }
    /** 用户电话 */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /** 新表表地址(需扫描) */
    public String getNewAddr() {
        return newAddr;
    }
    /** 新表表地址(需扫描) */
    public void setNewAddr(String newAddr) {
        this.newAddr = newAddr;
    }

    /** 新表资产编号(需扫描) */
    public String getNewAssetNumbers() {
        return newAssetNumbers;
    }
    /** 新表资产编号(需扫描) */
    public void setNewAssetNumbers(String newAssetNumbers) {
        this.newAssetNumbers = newAssetNumbers;
    }

    /** 新电能表止码-电量(需扫描) */
    public String getNewElectricity() {
        return newElectricity;
    }
    /** 新电能表止码-电量(需扫描) */
    public void setNewElectricity(String newElectricity) {
        this.newElectricity = newElectricity;
    }

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

    /** 拍照图片的路径(变压器) */
    public String getPicPath() {
        return picPath;
    }
    /** 拍照图片的路径(变压器) */
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

}
