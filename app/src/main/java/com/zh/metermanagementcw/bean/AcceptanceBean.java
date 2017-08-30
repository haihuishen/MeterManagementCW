package com.zh.metermanagementcw.bean;

/**
 * 日/月冻结数据 -- 数据复核
 */

public class AcceptanceBean {

    /** 序号(数据库自动生成) */
    private String _id;
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
    /** 日冻结读数(扫描) */
    private String electricityScanByDay;
    /** 数据时标(复核时间) -- 日冻结时间 */
    private String daysFreezingTimeScan;
    /** 差异：electricityScanByDay - electricityInByDay */
    private String differencesByDay;
    /** 日冻结差异总结 */
    private String conclusionByDay;
    /** 日冻结是否完成 */
    private boolean isFinishByDay;

    /** 数据时标(导入) -- 月冻结时间 */
    private String monthFreezingTimeIn;
    /** 月冻结读数(导入) */
    private String electricityInByMonth;
    /** 月冻结读数(扫描) */
    private String electricityScanByMonth;
    /** 数据时标(复核时间) -- 月冻结时间 */
    private String monthFreezingTimeScan;
    /** 差异：electricityScanByDay - electricityInByDay */
    private String differencesByMonth;
    /** 月冻结差异总结 */
    private String conclusionByMonth;
    /** 月冻结是否完成 */
    private boolean isFinishByMonth;

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

    /** 日冻结读数(扫描) */
    public String getElectricityScanByDay() {
        return electricityScanByDay;
    }
    /** 日冻结读数(扫描) */
    public void setElectricityScanByDay(String electricityScanByDay) {
        this.electricityScanByDay = electricityScanByDay;
    }

    /** 数据时标(复核时间) -- 日冻结时间 */
    public String getDaysFreezingTimeScan() {
        return daysFreezingTimeScan;
    }
    /** 数据时标(复核时间) -- 日冻结时间 */
    public void setDaysFreezingTimeScan(String daysFreezingTimeScan) {
        this.daysFreezingTimeScan = daysFreezingTimeScan;
    }

    /** 差异：electricityScanByDay - electricityInByDay */
    public String getDifferencesByDay() {
        return differencesByDay;
    }
    /** 差异：electricityScanByDay - electricityInByDay */
    public void setDifferencesByDay(String differencesByDay) {
        this.differencesByDay = differencesByDay;
    }

    /** 日冻结差异总结 */
    public String getConclusionByDay() {
        return conclusionByDay;
    }
    /** 日冻结差异总结 */
    public void setConclusionByDay(String conclusionByDay) {
        this.conclusionByDay = conclusionByDay;
    }

    /** 日冻结是否完成 */
    public boolean isFinishByDay() {
        return isFinishByDay;
    }
    /** 日冻结是否完成 */
    public void setFinishByDay(boolean finishByDay) {
        isFinishByDay = finishByDay;
    }



    /** 数据时标(导入) -- 月冻结时间 */
    public String getMonthFreezingTimeIn() {
        return monthFreezingTimeIn;
    }
    /** 数据时标(导入) -- 月冻结时间 */
    public void setMonthFreezingTimeIn(String monthFreezingTimeIn) {
        this.monthFreezingTimeIn = monthFreezingTimeIn;
    }

    /** 月冻结读数(导入) */
    public String getElectricityInByMonth() {
        return electricityInByMonth;
    }
    /** 月冻结读数(导入) */
    public void setElectricityInByMonth(String electricityInByMonth) {
        this.electricityInByMonth = electricityInByMonth;
    }

    /** 月冻结读数(扫描) */
    public String getElectricityScanByMonth() {
        return electricityScanByMonth;
    }
    /** 月冻结读数(扫描) */
    public void setElectricityScanByMonth(String electricityScanByMonth) {
        this.electricityScanByMonth = electricityScanByMonth;
    }

    /** 数据时标(复核时间) -- 月冻结时间 */
    public String getMonthFreezingTimeScan() {
        return monthFreezingTimeScan;
    }
    /** 数据时标(复核时间) -- 月冻结时间 */
    public void setMonthFreezingTimeScan(String monthFreezingTimeScan) {
        this.monthFreezingTimeScan = monthFreezingTimeScan;
    }

    /** 差异：electricityScanByDay - electricityInByDay */
    public String getDifferencesByMonth() {
        return differencesByMonth;
    }
    /** 差异：electricityScanByDay - electricityInByDay */
    public void setDifferencesByMonth(String differencesByMonth) {
        this.differencesByMonth = differencesByMonth;
    }

    /** 月冻结差异总结 */
    public String getConclusionByMonth() {
        return conclusionByMonth;
    }
    /** 月冻结差异总结 */
    public void setConclusionByMonth(String conclusionByMonth) {
        this.conclusionByMonth = conclusionByMonth;
    }

    /** 月冻结是否完成 */
    public boolean isFinishByMonth() {
        return isFinishByMonth;
    }
    /** 月冻结是否完成 */
    public void setFinishByMonth(boolean finishByMonth) {
        isFinishByMonth = finishByMonth;
    }

    @Override
    public String toString() {
        return "AcceptanceBean{" +
                "_id='" + _id + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", assetNumbers='" + assetNumbers + '\'' +
                ", userName='" + userName + '\'' +
                ", meterAddr='" + meterAddr + '\'' +
                ", terminalNo='" + terminalNo + '\'' +          "\n" +


                ", daysFreezingTimeIn='" + daysFreezingTimeIn + '\'' +
                ", electricityInByDay='" + electricityInByDay + '\'' +
                ", electricityScanByDay='" + electricityScanByDay + '\'' +
                ", daysFreezingTimeScan='" + daysFreezingTimeScan + '\'' +
                ", differencesByDay='" + differencesByDay + '\'' +
                ", conclusionByDay='" + conclusionByDay + '\'' +
                ", isFinishByDay=" + isFinishByDay +            "\n" +


                ", monthFreezingTimeIn='" + monthFreezingTimeIn + '\'' +
                ", electricityInByMonth='" + electricityInByMonth + '\'' +
                ", electricityScanByMonth='" + electricityScanByMonth + '\'' +
                ", monthFreezingTimeScan='" + monthFreezingTimeScan + '\'' +
                ", differencesByMonth='" + differencesByMonth + '\'' +
                ", conclusionByMonth='" + conclusionByMonth + '\'' +
                ", isFinishByMonth=" + isFinishByMonth +
                '}';
    }
}
