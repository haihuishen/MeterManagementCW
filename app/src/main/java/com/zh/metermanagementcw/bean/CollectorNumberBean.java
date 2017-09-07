package com.zh.metermanagementcw.bean;

/**
 * 采集器(资产编号)
 */

public class CollectorNumberBean {

    /** 采集器资产编码 */
    private String collectorNumbers;
    /** 抄表区段 */
    private String theMeteringSection;
    /** 采集器图片 */
    private String collectorPicPath;

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


    /** 采集器资产编码 */
    public String getCollectorNumbers() {
        return collectorNumbers;
    }
    /** 采集器资产编码 */
    public void setCollectorNumbers(String collectorNumbers) {
        this.collectorNumbers = collectorNumbers;
    }

    /** 抄表区段 */
    public String getTheMeteringSection() {
        return theMeteringSection;
    }
    /** 抄表区段 */
    public void setTheMeteringSection(String theMeteringSection) {
        this.theMeteringSection = theMeteringSection;
    }

    /** 采集器图片 */
    public String getCollectorPicPath() {
        return collectorPicPath;
    }
    /** 采集器图片 */
    public void setCollectorPicPath(String collectorPicPath) {
        this.collectorPicPath = collectorPicPath;
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
        return "CollectorNumberBean{" +
                "collectorNumbers='" + collectorNumbers + '\'' +
                ", theMeteringSection='" + theMeteringSection + '\'' +
                ", collectorPicPath='" + collectorPicPath + '\'' +
                ", collectorFootNumbers='" + collectorFootNumbers + '\'' +
                ", collectorFootPicPath='" + collectorFootPicPath + '\'' +
                ", collectorBodyNumbers1='" + collectorBodyNumbers1 + '\'' +
                ", collectorBodyPicPath1='" + collectorBodyPicPath1 + '\'' +
                ", collectorBodyNumbers2='" + collectorBodyNumbers2 + '\'' +
                ", collectorBodyPicPath2='" + collectorBodyPicPath2 + '\'' +
                '}';
    }

    public void clean(){
        collectorNumbers = "";
        theMeteringSection = "";
        collectorPicPath = "";

        collectorFootNumbers = "";
        collectorFootPicPath = "";
        collectorBodyNumbers1 = "";
        collectorBodyPicPath1 = "";
        collectorBodyNumbers2 = "";
        collectorBodyPicPath2 = "";

    }
}
