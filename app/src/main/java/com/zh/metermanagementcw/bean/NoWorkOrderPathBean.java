package com.zh.metermanagementcw.bean;

import android.content.Context;

import com.zh.metermanagementcw.utils.FilesUtils;

import java.io.File;

/**
 * 无工单要生成的文件夹
 */

public class NoWorkOrderPathBean {

    public static String ReplaceMeter = "换表" + File.separator;
    public static String NewCollector = "加装采集器" + File.separator;
    public static String Concentrator = "集中器" + File.separator;
    public static String Transformer = "变压器" + File.separator;

    public static String Photo = "图片" + File.separator;
    public static String ExportExcel = "导出Excel" + File.separator;


    /** 抄表区段 */
    private String areaPath;
    /** 抄表区段/图片 */
    private String areaPhotoPath;

    /** 抄表区段/导出Excel */
    private String areaExportPath;

    /** 抄表区段/图片/换表 */
    private String replaceMeterPhotoPath;
    /** 抄表区段/图片/加装采集器 */
    private String newCollectorPhotoPath;

    /** 抄表区段/图片/集中器 */
    private String concentratorPhotoPath;
    /** 抄表区段/图片/变压器 */
    private String transformerPhotoPath;


    /** 抄表区段 */
    public String getAreaPath() {
        return areaPath;
    }
    /** 抄表区段 */
    public void setAreaPath(String areaPath) {
        this.areaPath = areaPath;
    }

    /** 抄表区段/图片 */
    public String getAreaPhotoPath() {
        return areaPhotoPath;
    }
    /** 抄表区段/图片 */
    public void setAreaPhotoPath(String areaPhotoPath) {
        this.areaPhotoPath = areaPhotoPath;
    }

    /** 抄表区段/导出Excel */
    public String getAreaExportPath() {
        return areaExportPath;
    }
    /** 抄表区段/导出Excel */
    public void setAreaExportPath(String areaExportPath) {
        this.areaExportPath = areaExportPath;
    }

    /** 抄表区段/图片/换表 */
    public String getReplaceMeterPhotoPath() {
        return replaceMeterPhotoPath;
    }
    /** 抄表区段/图片/换表 */
    public void setReplaceMeterPhotoPath(String replaceMeterPhotoPath) {
        this.replaceMeterPhotoPath = replaceMeterPhotoPath;
    }


    /** 抄表区段/图片/加装采集器 */
    public String getNewCollectorPhotoPath() {
        return newCollectorPhotoPath;
    }
    /** 抄表区段/图片/加装采集器 */
    public void setNewCollectorPhotoPath(String newCollectorPhotoPath) {
        this.newCollectorPhotoPath = newCollectorPhotoPath;
    }


    /** 抄表区段/图片/集中器 */
    public String getConcentratorPhotoPath() {
        return concentratorPhotoPath;
    }
    /** 抄表区段/图片/集中器 */
    public void setConcentratorPhotoPath(String concentratorPhotoPath) {
        this.concentratorPhotoPath = concentratorPhotoPath;
    }

    /** 抄表区段/图片/变压器 */
    public String getTransformerPhotoPath() {
        return transformerPhotoPath;
    }
    /** 抄表区段/图片/变压器 */
    public void setTransformerPhotoPath(String transformerPhotoPath) {
        this.transformerPhotoPath = transformerPhotoPath;
    }


    public void onCreate(Context context){

        FilesUtils.createFile(context, areaPath);
        FilesUtils.createFile(context, areaPhotoPath);
        FilesUtils.createFile(context, areaExportPath);
        FilesUtils.createFile(context, replaceMeterPhotoPath);
        FilesUtils.createFile(context, newCollectorPhotoPath);
        FilesUtils.createFile(context, concentratorPhotoPath);
        FilesUtils.createFile(context, transformerPhotoPath);
    }

}
