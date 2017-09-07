package com.zh.metermanagementcw.bean;

/**
 * Created by Administrator on 2017/8/1.
 */

public class MeterBean1 {

    /** 用户编号 */
    private String userNumber;
    /** 用户名称 */
    private String userName;
    /** 用户地址 */
    private String userAddr;
    /** 用户电话 */
    private String userPhone;
    /** 旧表资产编号(导入的) */
    private String oldAssetNumbers;
    /** 计量点编号 */
    private String measurementPointNumber;
    /** 供电局(供电单位) */
    private String powerSupplyBureau;
    /** 抄表区段 */
    private String theMeteringSection;
    /** 台区 */
    private String courts;
    /** 计量点地址 */
    private String measuringPointAddress;

    /** 旧表表地址(需扫描) */
    private String oldAddr;
    /** 旧表表地址 和 资产编码 比较  -- ture:不同 false:相同*/
    private boolean oldAddrAndAsset;
    /** 旧电能表止码-电量(需扫描) */
    private String oldElectricity;
    /** 新表表地址(需扫描) */
    private String newAddr;
    /** 新表表地址 和 资产编码 比较 */
    private boolean newAddrAndAsset;
    /** 新表资产编号(需扫描) */
    private String newAssetNumbersScan;
    /** 新电能表止码-电量(需扫描) */
    private String newElectricity;
    /** 采集器资产编号(需扫描) */
    private String collectorAssetNumbersScan;

    /** 完成换抄时间 */
    private String time;
    /** 拍照图片的路径(换表) */
    private String picPath;
    /** 拍照图片的路径(新装采集器对应的电表) */
    private String meterPicPath;
    /** 拍照图片的路径(新装采集器) */
    private String meterContentPicPath;

    /** 0:"换表"； 1："新装采集器" -- 要先判断是否抄完 */
    private String relaceOrAnd;
    /** 是否完成(扫描完) */
    private boolean isFinish;

    //---- 2017/09/04
    /** (新电表)电表表脚封扣（条码） */
    private String meterFootNumbers;
    /** (新电表)拍照图片的路径(电表表脚封扣) */
    private String meterFootPicPath;

    /** (新电表)表箱封扣1（条码） */
    private String meterBodyNumbers1;
    /** (新电表)拍照图片的路径(表箱封扣1) */
    private String meterBodyPicPath1;

    /** (新电表)表箱封扣2（条码） */
    private String meterBodyNumbers2;
    /** (新电表)拍照图片的路径(表箱封扣2) */
    private String meterBodyPicPath2;

    //---- 2017/09/06
    /** (采集器)电表表脚封扣（条码） */
    private String collectorFootNumbers;
    /** (采集器)拍照图片的路径(电表表脚封扣) */
    private String collectorFootPicPath;

    /** (采集器)表箱封扣1（条码） */
    private String collectorBodyNumbers1;
    /** (采集器)拍照图片的路径(表箱封扣1) */
    private String collectorBodyPicPath1;

    /** (采集器)表箱封扣2（条码） */
    private String collectorBodyNumbers2;
    /** (采集器)拍照图片的路径(表箱封扣2) */
    private String collectorBodyPicPath2;

    //------------------------------------------------------------

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

    /** 计量点编号 */
    public String getMeasurementPointNumber() {
        return measurementPointNumber;
    }
    /** 计量点编号 */
    public void setMeasurementPointNumber(String measurementPointNumber) {
        this.measurementPointNumber = measurementPointNumber;
    }

    /** 供电局(供电单位) */
    public String getPowerSupplyBureau() {
        return powerSupplyBureau;
    }
    /** 供电局(供电单位) */
    public void setPowerSupplyBureau(String powerSupplyBureau) {
        this.powerSupplyBureau = powerSupplyBureau;
    }

    /** 抄表区段 */
    public String getTheMeteringSection() {
        return theMeteringSection;
    }
    /** 抄表区段 */
    public void setTheMeteringSection(String theMeteringSection) {
        this.theMeteringSection = theMeteringSection;
    }

    /** 台区 */
    public String getCourts() {
        return courts;
    }
    /** 台区 */
    public void setCourts(String courts) {
        this.courts = courts;
    }

    /** 计量点地址 */
    public String getMeasuringPointAddress() {
        return measuringPointAddress;
    }
    /** 计量点地址 */
    public void setMeasuringPointAddress(String measuringPointAddress) {
        this.measuringPointAddress = measuringPointAddress;
    }

    /** 旧表资产编号(导入的) */
    public String getOldAssetNumbers() {
        return oldAssetNumbers;
    }
    /** 旧表资产编号(导入的) */
    public void setOldAssetNumbers(String oldAssetNumbers) {
        this.oldAssetNumbers = oldAssetNumbers;
    }

    /** 旧表表地址(需扫描) */
    public String getOldAddr() {
        return oldAddr;
    }
    /** 旧表表地址(需扫描) */
    public void setOldAddr(String oldAddr) {
        this.oldAddr = oldAddr;
    }

    /** 旧表表地址 和 资产编码 比较   -- ture:不同 false:相同*/
    public boolean isOldAddrAndAsset() {
        return oldAddrAndAsset;
    }
    /** 旧表表地址 和 资产编码 比较   -- ture:不同 false:相同*/
    public void setOldAddrAndAsset(boolean oldAddrAndAsset) {
        this.oldAddrAndAsset = oldAddrAndAsset;
    }

    /** 旧电能表止码-电量(需扫描) */
    public String getOldElectricity() {
        return oldElectricity;
    }
    /** 旧电能表止码-电量(需扫描) */
    public void setOldElectricity(String oldElectricity) {
        this.oldElectricity = oldElectricity;
    }

    /** 新表表地址(需扫描) */
    public String getNewAddr() {
        return newAddr;
    }
    /** 新表表地址(需扫描) */
    public void setNewAddr(String newAddr) {
        this.newAddr = newAddr;
    }

    /** 新表表地址 和 资产编码 比较 */
    public boolean isNewAddrAndAsset() {
        return newAddrAndAsset;
    }
    /** 新表表地址 和 资产编码 比较 */
    public void setNewAddrAndAsset(boolean newAddrAndAsset) {
        this.newAddrAndAsset = newAddrAndAsset;
    }

    /** 新表资产编号(需扫描) */
    public String getNewAssetNumbersScan() {
        return newAssetNumbersScan;
    }
    /** 新表资产编号(需扫描) */
    public void setNewAssetNumbersScan(String newAssetNumbersScan) {
        this.newAssetNumbersScan = newAssetNumbersScan;
    }

    /** 新电能表止码-电量(需扫描) */
    public String getNewElectricity() {
        return newElectricity;
    }
    /** 新电能表止码-电量(需扫描) */
    public void setNewElectricity(String newElectricity) {
        this.newElectricity = newElectricity;
    }

    /** 采集器资产编号(需扫描) */
    public String getCollectorAssetNumbersScan() {
        return collectorAssetNumbersScan;
    }
    /** 采集器资产编号(需扫描) */
    public void setCollectorAssetNumbersScan(String collectorAssetNumbersScan) {
        this.collectorAssetNumbersScan = collectorAssetNumbersScan;
    }

    /** 完成换抄时间 */
    public String getTime() {
        return time;
    }
    /** 完成换抄时间 */
    public void setTime(String time) {
        this.time = time;
    }

    /** 拍照图片的路径(换表) */
    public String getPicPath() {
        return picPath;
    }
    /** 拍照图片的路径(换表) */
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    /** 拍照图片的路径(新装采集器对应的电表) */
    public String getMeterPicPath() {
        return meterPicPath;
    }
    /** 拍照图片的路径(新装采集器对应的电表) */
    public void setMeterPicPath(String meterPicPath) {
        this.meterPicPath = meterPicPath;
    }

    /** 拍照图片的路径(新装采集器) */
    public String getMeterContentPicPath() {
        return meterContentPicPath;
    }
    /** 拍照图片的路径(新装采集器) */
    public void setMeterContentPicPath(String meterContentPicPath) {
        this.meterContentPicPath = meterContentPicPath;
    }

    /** 是否完成(扫描完) */
    public boolean isFinish() {
        return isFinish;
    }
    /** 是否完成(扫描完) */
    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    /** 0:"换表"； 1："新装采集器" -- 要先判断是否抄完 */
    public String getRelaceOrAnd() {
        return relaceOrAnd;
    }
    /** 0:"换表"； 1："新装采集器" -- 要先判断是否抄完 */
    public void setRelaceOrAnd(String relaceOrAnd) {
        this.relaceOrAnd = relaceOrAnd;
    }

    //---- 2017/09/04
    /** (新电表)电表表脚封扣（条码） */
    public String getMeterFootNumbers() {
        return meterFootNumbers;
    }
    /** (新电表)电表表脚封扣（条码） */
    public void setMeterFootNumbers(String meterFootNumbers) {
        this.meterFootNumbers = meterFootNumbers;
    }

    /** (新电表)拍照图片的路径(电表表脚封扣) */
    public String getMeterFootPicPath() {
        return meterFootPicPath;
    }
    /** (新电表)拍照图片的路径(电表表脚封扣) */
    public void setMeterFootPicPath(String meterFootPicPath) {
        this.meterFootPicPath = meterFootPicPath;
    }

    /** (新电表)表箱封扣1（条码） */
    public String getMeterBodyNumbers1() {
        return meterBodyNumbers1;
    }
    /** (新电表)表箱封扣1（条码） */
    public void setMeterBodyNumbers1(String meterBodyNumbers1) {
        this.meterBodyNumbers1 = meterBodyNumbers1;
    }

    /** (新电表)拍照图片的路径(表箱封扣1) */
    public String getMeterBodyPicPath1() {
        return meterBodyPicPath1;
    }
    /** (新电表)拍照图片的路径(表箱封扣1) */
    public void setMeterBodyPicPath1(String meterBodyPicPath1) {
        this.meterBodyPicPath1 = meterBodyPicPath1;
    }

    /** (新电表)表箱封扣2（条码） */
    public String getMeterBodyNumbers2() {
        return meterBodyNumbers2;
    }
    /** (新电表)表箱封扣2（条码） */
    public void setMeterBodyNumbers2(String meterBodyNumbers2) {
        this.meterBodyNumbers2 = meterBodyNumbers2;
    }

    /** (新电表)拍照图片的路径(表箱封扣2) */
    public String getMeterBodyPicPath2() {
        return meterBodyPicPath2;
    }
    /** (新电表)拍照图片的路径(表箱封扣2) */
    public void setMeterBodyPicPath2(String meterBodyPicPath2) {
        this.meterBodyPicPath2 = meterBodyPicPath2;
    }


    //---- 2017/09/06
    /** (采集器)电表表脚封扣（条码） */
    public String getCollectorFootNumbers() {
        return collectorFootNumbers;
    }
    /** (采集器)电表表脚封扣（条码） */
    public void setCollectorFootNumbers(String collectorFootNumbers) {
        this.collectorFootNumbers = collectorFootNumbers;
    }

    /** (采集器)拍照图片的路径(电表表脚封扣) */
    public String getCollectorFootPicPath() {
        return collectorFootPicPath;
    }
    /** (采集器)拍照图片的路径(电表表脚封扣) */
    public void setCollectorFootPicPath(String collectorFootPicPath) {
        this.collectorFootPicPath = collectorFootPicPath;
    }

    /** (采集器)表箱封扣1（条码） */
    public String getCollectorBodyNumbers1() {
        return collectorBodyNumbers1;
    }
    /** (采集器)表箱封扣1（条码） */
    public void setCollectorBodyNumbers1(String collectorBodyNumbers1) {
        this.collectorBodyNumbers1 = collectorBodyNumbers1;
    }

    /** (采集器)拍照图片的路径(表箱封扣1) */
    public String getCollectorBodyPicPath1() {
        return collectorBodyPicPath1;
    }
    /** (采集器)拍照图片的路径(表箱封扣1) */
    public void setCollectorBodyPicPath1(String collectorBodyPicPath1) {
        this.collectorBodyPicPath1 = collectorBodyPicPath1;
    }

    /** (采集器)表箱封扣2（条码） */
    public String getCollectorBodyNumbers2() {
        return collectorBodyNumbers2;
    }
    /** (采集器)表箱封扣2（条码） */
    public void setCollectorBodyNumbers2(String collectorBodyNumbers2) {
        this.collectorBodyNumbers2 = collectorBodyNumbers2;
    }

    /** (采集器)拍照图片的路径(表箱封扣2) */
    public String getCollectorBodyPicPath2() {
        return collectorBodyPicPath2;
    }
    /** (采集器)拍照图片的路径(表箱封扣2) */
    public void setCollectorBodyPicPath2(String collectorBodyPicPath2) {
        this.collectorBodyPicPath2 = collectorBodyPicPath2;
    }

    @Override
    public String toString() {
        return "MeterBean1{" +
                "userNumber='" + userNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", userAddr='" + userAddr + '\'' +
                ", userPhone='" + userPhone + '\'' +  "\n" +
                ", oldAssetNumbers='" + oldAssetNumbers + '\'' +
                ", measurementPointNumber='" + measurementPointNumber + '\'' +
                ", powerSupplyBureau='" + powerSupplyBureau + '\'' +
                ", theMeteringSection='" + theMeteringSection + '\'' +
                ", courts='" + courts + '\'' +
                ", measuringPointAddress='" + measuringPointAddress + '\'' +  "\n" +
                ", oldAddr='" + oldAddr + '\'' +
                ", oldAddrAndAsset=" + oldAddrAndAsset +
                ", oldElectricity='" + oldElectricity + '\'' +
                ", newAddr='" + newAddr + '\'' +
                ", newAddrAndAsset=" + newAddrAndAsset +
                ", newAssetNumbersScan='" + newAssetNumbersScan + '\'' +
                ", newElectricity='" + newElectricity + '\'' +
                ", collectorAssetNumbersScan='" + collectorAssetNumbersScan + '\'' +
                ", time='" + time + '\'' +  "\n" +
                ", picPath='" + picPath + '\'' +
                ", meterPicPath='" + meterPicPath + '\'' +
                ", meterContentPicPath='" + meterContentPicPath + '\'' +  "\n" +
                ", relaceOrAnd=" + relaceOrAnd +

                ", meterFootNumbers='" + meterFootNumbers + '\'' +  "\n" +
                ", meterFootPicPath='" + meterFootPicPath + '\'' +
                ", meterBodyNumbers1='" + meterBodyNumbers1 + '\'' +
                ", meterBodyPicPath1='" + meterBodyPicPath1 + '\'' +
                ", meterBodyNumbers2='" + meterBodyNumbers2 + '\'' +
                ", meterBodyPicPath2='" + meterBodyPicPath2 + '\'' +  "\n" +

                ", collectorFootNumbers='" + collectorFootNumbers + '\'' +  "\n" +
                ", collectorFootPicPath='" + collectorFootPicPath + '\'' +
                ", collectorBodyNumbers1='" + collectorBodyNumbers1 + '\'' +
                ", collectorBodyPicPath1='" + collectorBodyPicPath1 + '\'' +
                ", collectorBodyNumbers2='" + collectorBodyNumbers2 + '\'' +
                ", collectorBodyPicPath2='" + collectorBodyPicPath2 + '\'' +  "\n" +
                
                ", isFinish=" + isFinish +
                '}';
    }

}
