package com.zh.metermanagementcw.bean;

/**
 * 日/月冻结数据 -- 数据复核
 */

public class AcceptanceByDayBean {

    /** 用户编号 */
    private String userNumber;
    /** 资产编号(导入的) */
    private String assetNumbers;
    /** 测量点名称 (用户名) */
    private String userName;

    /** 电表表地址 */
    private String meterAddr;
    /** 终端内编号 */
    private String terminalNo;

    /** 数据时标(导入) -- 日冻结时间 */
    private String daysFreezingTimeIn;
    /** 日冻结读数(导入) */
    private String electricityInByDay;


    /** 用户编号 */
    public String getUserNumber() {
        return userNumber;
    }
    /** 用户编号 */
    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    /** 资产编号(导入的) */
    public String getAssetNumbers() {
        return assetNumbers;
    }
    /** 资产编号(导入的) */
    public void setAssetNumbers(String assetNumbers) {
        this.assetNumbers = assetNumbers;
    }


    /** 测量点名称 (用户名) */
    public String getUserName() {
        return userName;
    }
    /** 测量点名称 (用户名) */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** 电表表地址 */
    public String getMeterAddr() {
        return meterAddr;
    }
    /** 电表表地址 */
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr;
    }

    /** 终端内编号 */
    public String getTerminalNo() {
        return terminalNo;
    }
    /** 终端内编号 */
    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }



    /** 数据时标(导入) -- 日冻结时间 */
    public String getDaysFreezingTimeIn() {
        return daysFreezingTimeIn;
    }
    /** 数据时标(导入) -- 日冻结时间 */
    public void setDaysFreezingTimeIn(String daysFreezingTimeIn) {
        this.daysFreezingTimeIn = daysFreezingTimeIn;
    }

    /** 日冻结读数(导入) */
    public String getElectricityInByDay() {
        return electricityInByDay;
    }
    /** 日冻结读数(导入) */
    public void setElectricityInByDay(String electricityInByDay) {
        this.electricityInByDay = electricityInByDay;
    }


    @Override
    public String toString() {
        return "AcceptanceByDayBean{" +
                ", userNumber='" + userNumber + '\'' +
                ", assetNumbers='" + assetNumbers + '\'' +
                ", userName='" + userName + '\'' +
                ", meterAddr='" + meterAddr + '\'' +
                ", terminalNo='" + terminalNo + '\'' +
                ", daysFreezingTimeIn='" + daysFreezingTimeIn + '\'' +
                ", electricityInByDay='" + electricityInByDay + '\'' +
                '}';
    }
}
