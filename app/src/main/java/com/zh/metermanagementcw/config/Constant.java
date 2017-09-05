package com.zh.metermanagementcw.config;

import android.os.Environment;

import java.io.File;

/**
 * 全局变量
 */
public class Constant {

    /** 如果使用"/"==>File.separator<p> */

    /** 资源放置路径 */
    public final static String srcPathDir = Environment.getExternalStorageDirectory().getPath() + "/电表换装/";

    /** 电表换装(excel存放)路径 */
    public final static String excelPathDir = srcPathDir + "导入数据" + File.separator;

    /** 导入excel文件名 */
    //public final static String importExcel = "import.xls";
    //public final static String importExcel = "台区户表清单.xlsx";
    public final static String importExcel = "XX供电所XX台区（抄表区段）电能表.xlsx";

    /** 导出excel文件名 */
    //public final static String exportExcel = "export.xls";
    public final static String exportExcel = "XX供电所XX台区户表.xlsx";

    //-----------------------------------------------------------------------------------------------------

    /** CacheImage图片文件存放地址 <p> /storage/emulated/0/电表换装/CacheImage/ <br> */
    public final static String CACHE_IMAGE_PATH = srcPathDir + "CacheImage" + File.separator;


    /** 集抄改造 存放地址 <p>  /storage/emulated/0/电表换装/集抄改造/ <br>  */
    public final static String SetCopyTransformation_PATH = srcPathDir + "集抄改造" + File.separator;

    /** acceptance 存放地址 <p>  /storage/emulated/0/电表换装/数据验收/ <br> */
    public final static String Acceptance_PATH = srcPathDir + "数据验收" + File.separator;

    /** 批量换表 存放地址 <p>  /storage/emulated/0/电表换装/批量换表/ <br> */
    public final static String BatchReplaceMeter_PATH = srcPathDir + "批量换表" + File.separator;

    /** 批量新装 存放地址 <p>  /storage/emulated/0/电表换装/批量新装/ <br> */
    public final static String BatchNewMeter_PATH = srcPathDir + "批量新装" + File.separator;


    /** 零散换表 存放地址 <p>  /storage/emulated/0/电表换装/零散换表/ <br> */
    public final static String ScatteredReplaceMeter_PATH = srcPathDir + "零散换表" + File.separator;

    /** 零散新装 存放地址 <p>  /storage/emulated/0/电表换装/零散新装/ <br> */
    public final static String ScatteredNewMeter_PATH = srcPathDir + "零散新装" + File.separator;



    //-----------------------------------------------------------------------------------------------------
    /** "说明Image图片"文件存放地址 <p> /storage/emulated/0/电表换装/CacheImage/DirectionsForUseImage/ <br> */
    public final static String DIRECTIONSFORUSEIMAGE_PATH = CACHE_IMAGE_PATH + "DirectionsForUseImage" + File.separator;


    /** 电话导入文件 存放地址 <p> /storage/emulated/0/电表换装/集抄改造/导入数据/电话导入文件/ <br>  */
    public final static String IMPORT_PHONE_PATH = SetCopyTransformation_PATH + "导入数据"
            + File.separator + "电话导入文件" + File.separator;
    /** 换表导入文件 存放地址 <p> /storage/emulated/0/电表换装/集抄改造/导入数据/换表导入文件/ <br> */
    public final static String IMPORT_METER_INFO_PATH = SetCopyTransformation_PATH + "导入数据"
            + File.separator + "换表导入文件" + File.separator;


    /** 批量换表--导入文件夹 存放地址 <p> /storage/emulated/0/电表换装/批量换表/导入数据/ <br>  */
    public final static String BatchReplaceMeter_ImportInfo_PATH = BatchReplaceMeter_PATH + "导入数据" + File.separator;
    /** 批量换表--导出文件夹 存放地址 <p> /storage/emulated/0/电表换装/批量换表/导出数据/ <br>  */
    public final static String BatchReplaceMeter_ExportInfo_PATH = BatchReplaceMeter_PATH + "导出数据" + File.separator;

    /** 批量新装--导入文件夹 存放地址 <p> /storage/emulated/0/电表换装/批量新装/导入数据/ <br>  */
    public final static String BatchNewMeter_ImportInfo_PATH = BatchNewMeter_PATH + "导入数据" + File.separator;
    /** 批量新装--导出文件夹 存放地址 <p> /storage/emulated/0/电表换装/批量新装/导出数据/ <br>  */
    public final static String BatchNewMeter_ExportInfo_PATH = BatchNewMeter_PATH + "导出数据" + File.separator;


    /** 日冻结 存放地址 <p> /storage/emulated/0/电表换装/数据验收/导入数据/日冻结/ <br> */
    public final static String Acceptance_Day_PATH = Acceptance_PATH + "导入数据"
            + File.separator + "日冻结" + File.separator;
    /** 月冻结 存放地址 <p> /storage/emulated/0/电表换装/数据验收/导入数据/月冻结/ <br> */
    public final static String Acceptance_Month_PATH = Acceptance_PATH + "导入数据"
            + File.separator + "月冻结" + File.separator;

    //-----------------------------------------------------------------------------------------------------
    /** 导出Excel--日冻结 存放地址 <p> /storage/emulated/0/电表换装/数据验收/导出数据/导出Excel/日冻结/ <br> */
    public final static String Acceptance_ExportExcel_Day_PATH = Acceptance_PATH + "导出数据"
            + File.separator + "导出Excel" + File.separator + "日冻结" + File.separator;
    /** 导出Excel--月冻结 存放地址 <p> /storage/emulated/0/电表换装/数据验收/导出数据/导出Excel/月冻结/ <br> */
    public final static String Acceptance_ExportExcel_Month_PATH = Acceptance_PATH + "导出数据"
            + File.separator + "导出Excel" + File.separator + "月冻结" + File.separator;



    //----------------------------------------------------------------------------------------

    public final static String type_all = "总户数";
    public final static String type_finish = "已完成";
    public final static String type_unfinished = "未完成";
    public final static String type_replaceMeter = "换表";
    public final static String type_newCollector = "加装采集器";
    public final static String type_assetsNumber_Mismatches = "编码无匹配用户";
    public final static String type_concentrator = "集中器";
    public final static String type_transformer = "变压器";
    public final static String type_old_addrAndassetsNumber_Mismatches = "旧表地址和编码不匹配";
    public final static String type_new_addrAndassetsNumber_Mismatches = "新表地址和编码不匹配";
    //----------------------------------------------------------------------------------------

    /** 数据库名 */
	public final static String DB_NAME = "MeterManagement.db";

    /** 表名 -- meterinfo */
    public final static String TABLE_METERINFO = "meterinfo";
    /** 表名 -- meterinfo1 -- 数据采集(中的表数据) */
    public final static String TABLE_METERINFO1 = "meterinfo1";
    /** 表名 -- assetnumber -- 无匹配用户 -- 有表无户 */
    public final static String TABLE_ASSETNUMBER = "assetnumber";
    /** 表名 -- collectornumber -- 采集器 */
    public final static String TABLE_COLLECTORNUMBER = "collectornumber";
    /** 表名 -- acceptance -- 数据验收 */
    public final static String TABLE_ACCEPTANCE = "acceptance";
    /** 表名 -- concentrator -- 集中器 */
    public final static String TABLE_CONCENTRATOR = "concentrator";
    /** 表名 -- transformer -- 变压器 */
    public final static String TABLE_TRANSFORMER = "transformer";

    //----------------------------------------------------------------------------------------


    /** 序号(数据库自动生成) */
    public final static String TABLE_METERINFO_STR__id = "_id";
    /** 序号 */
    public final static String TABLE_METERINFO_STR_sequenceNumber = "sequenceNumber";
    /** 用户编号 */
    public final static String TABLE_METERINFO_STR_userNumber = "userNumber";
    /** 用户名称 */
    public final static String TABLE_METERINFO_STR_userName = "userName";
    /** 区域(哪个区域的表) */
    public final static String TABLE_METERINFO_STR_area = "area";
    /** 用户地址 */
    public final static String TABLE_METERINFO_STR_userAddr = "userAddr";
    /** 旧表资产编号(导入的) */
    public final static String TABLE_METERINFO_STR_oldAssetNumbers = "oldAssetNumbers";
    /** 旧表表地址(需扫描) */
    public final static String TABLE_METERINFO_STR_oldAddr = "oldAddr";
    /** 旧表表地址 和 资产编码 比较 */
    public final static String TABLE_METERINFO_STR_oldAddrAndAsset = "oldAddrAndAsset";
    /** 旧表资产编号(需扫描) */
    public final static String TABLE_METERINFO_STR_oldAssetNumbersScan = "oldAssetNumbersScan";
    /** 旧电能表止码-电量(需扫描) */
    public final static String TABLE_METERINFO_STR_oldElectricity = "oldElectricity";
    /** 新表表地址(需扫描) */
    public final static String TABLE_METERINFO_STR_newAddr = "newAddr";
    /** 新表表地址 和 资产编码 比较 */
    public final static String TABLE_METERINFO_STR_newAddrAndAsset = "newAddrAndAsset";
    /** 新表资产编号(需扫描) */
    public final static String TABLE_METERINFO_STR_newAssetNumbersScan = "newAssetNumbersScan";
    /** 新电能表止码-电量(需扫描) */
    public final static String TABLE_METERINFO_STR_newElectricity = "newElectricity";
    /** 采集器资产编号(需扫描) */
    public final static String TABLE_METERINFO_STR_collectorAssetNumbersScan = "collectorAssetNumbersScan";
    /** 完成换抄时间 */
    public final static String TABLE_METERINFO_STR_time = "time";
    /** 拍照图片的路径 */
    public final static String TABLE_METERINFO_STR_picPath = "picPath";
    /** 是否完成(扫描完) */
    public final static String TABLE_METERINFO_STR_isFinish = "isFinish";
    
    //-------------------------------------------------------------------------------

    /** 序号(数据库自动生成) */
    public final static String TABLE_METERINFO1_STR__id = "_id";
    /** 用户编号 */
    public final static String TABLE_METERINFO1_STR_userNumber = "userNumber";
    /** 用户名称 */
    public final static String TABLE_METERINFO1_STR_userName = "userName";
    /** 用户地址 */
    public final static String TABLE_METERINFO1_STR_userAddr = "userAddr";
    /** 用户电话 */
    public final static String TABLE_METERINFO1_STR_userPhone = "userPhone";
    /** 计量点编号 */
    public final static String TABLE_METERINFO1_STR_measurementPointNumber = "measurementPointNumber";
    /** 供电局(供电单位) */
    public final static String TABLE_METERINFO1_STR_powerSupplyBureau = "powerSupplyBureau";
    /** 抄表区段 */
    public final static String TABLE_METERINFO1_STR_theMeteringSection = "theMeteringSection";
    /** 台区 */
    public final static String TABLE_METERINFO1_STR_courts = "courts";
    /** 计量点地址 */
    public final static String TABLE_METERINFO1_STR_measuringPointAddress = "measuringPointAddress";

    /** 旧表资产编号(导入的) */
    public final static String TABLE_METERINFO1_STR_oldAssetNumbers = "oldAssetNumbers";
    /** 旧表表地址(需扫描) */
    public final static String TABLE_METERINFO1_STR_oldAddr = "oldAddr";
    /** 旧表表地址 和 资产编码 比较 */
    public final static String TABLE_METERINFO1_STR_oldAddrAndAsset = "oldAddrAndAsset";
    /** 旧电能表止码-电量(需扫描) */
    public final static String TABLE_METERINFO1_STR_oldElectricity = "oldElectricity";
    /** 新表表地址(需扫描) */
    public final static String TABLE_METERINFO1_STR_newAddr = "newAddr";
    /** 新表表地址 和 资产编码 比较 */
    public final static String TABLE_METERINFO1_STR_newAddrAndAsset = "newAddrAndAsset";
    /** 新表资产编号(需扫描) */
    public final static String TABLE_METERINFO1_STR_newAssetNumbersScan = "newAssetNumbersScan";
    /** 新电能表止码-电量(需扫描) */
    public final static String TABLE_METERINFO1_STR_newElectricity = "newElectricity";
    /** 采集器资产编号(需扫描) */
    public final static String TABLE_METERINFO1_STR_collectorAssetNumbersScan = "collectorAssetNumbersScan";
    /** 完成换抄时间 */
    public final static String TABLE_METERINFO1_STR_time = "time";
    /** 拍照图片的路径(换表) */
    public final static String TABLE_METERINFO1_STR_picPath = "picPath";
    /** 拍照图片的路径(新装采集器对应的电表) */
    public final static String TABLE_METERINFO1_STR_meterPicPath = "meterPicPath";
    /** 拍照图片的路径(新装采集器) */
    public final static String TABLE_METERINFO1_STR_meterContentPicPath = "meterContentPicPath";
    /** 0:"换表"； 1："新装采集器" -- 要先判断是否抄完 */
    public final static String TABLE_METERINFO1_STR_relaceOrAnd = "relaceOrAnd";
    /** 是否完成(扫描完) */
    public final static String TABLE_METERINFO1_STR_isFinish = "isFinish";
    //----2017/09/04
    /** 电表表脚封扣（条码） */
    public final static String TABLE_METERINFO1_STR_meterFootNumbers = "meterFootNumbers";
    /** 拍照图片的路径(电表表脚封扣) */
    public final static String TABLE_METERINFO1_STR_meterFootPicPath = "meterFootPicPath";
    /** 表箱封扣1（条码） */
    public final static String TABLE_METERINFO1_STR_meterBodyNumbers1 = "meterBodyNumbers1";
    /** 拍照图片的路径(表箱封扣1) */
    public final static String TABLE_METERINFO1_STR_meterBodyPicPath1 = "meterBodyPicPath1";
    /** 表箱封扣2（条码） */
    public final static String TABLE_METERINFO1_STR_meterBodyNumbers2 = "meterBodyNumbers2";
    /** 拍照图片的路径(表箱封扣2) */
    public final static String TABLE_METERINFO1_STR_meterBodyPicPath2 = "meterBodyPicPath2";

    //-------------------------------------------------------------------------------
    /** 序号(数据库自动生成) */
    public final static String TABLE_ASSETNUMBER_STR__id = "_id";
    /** 资产编号 */
    public final static String TABLE_ASSETNUMBER_STR_assetNumbers = "assetNumbers";



    //-------------------------------- 采集器(资产编号) --------------------------------
    /** 序号(数据库自动生成) */
    public final static String TABLE_COLLECTORNUMBER_STR__id = "_id";
    /** 采集器资产编码 */
    public final static String TABLE_COLLECTORNUMBER_STR_collectorNumbers = "collectorNumbers";
    /** 抄表区段 */
    public final static String TABLE_COLLECTORNUMBER_STR_theMeteringSection = "theMeteringSection";
    /** 采集器图片 */
    public final static String TABLE_COLLECTORNUMBER_STR_collectorPicPath = "collectorPicPath";



    //-------------------------------- 日/月冻结数据 -- 数据复核 --------------------------------

    /** 日/月冻结数据 -- 数据复核 */
    public static enum ACCEPTANCE{
        /** 序号(数据库自动生成) */
        _id,
        /** 用户编号 */
        userNumber,
        /** 资产编号(导入的) */
        assetNumbers,
        /** 测量点名称 (用户名) */
        userName,
        /** 电表表地址 */
        meterAddr,
        /** 终端内编号 */
        terminalNo,

        /** 数据时标(导入) -- 日冻结时间 */
        daysFreezingTimeIn,
        /** 日冻结读数(导入) */
        electricityInByDay,
        /** 日冻结读数(扫描) */
        electricityScanByDay,
        /** 数据时标(复核时间) -- 日冻结时间 */
        daysFreezingTimeScan,
        /** 差异：electricityScanByDay - electricityInByDay */
        differencesByDay,
        /** 日冻结差异总结 */
        conclusionByDay,
        /** 日冻结是否完成 */
        isFinishByDay,

        /** 数据时标(导入) -- 月冻结时间 */
        monthFreezingTimeIn,
        /** 月冻结读数(导入) */
        electricityInByMonth,
        /** 月冻结读数(扫描) */
        electricityScanByMonth,
        /** 数据时标(复核时间) -- 月冻结时间 */
        monthFreezingTimeScan,
        /** 差异：electricityScanByDay - electricityInByDay */
        differencesByMonth,
        /** 月冻结差异总结 */
        conclusionByMonth,
        /** 月冻结是否完成 */
        isFinishByMonth
    }


    //-------------------------------- 集中器 --------------------------------

    /** 集中器 */
    public static enum CONCENTRATOR{
        /** 序号(数据库自动生成) */
        _id,
        /** 资产编号(集中器) */
        assetNumbers,
        /** 纬度 */
        latitude,
        /** 经度 */
        longitude,
        /** 抄表区段 */
        theMeteringSection,
        /** 地址 */
        addr,
        //----2017/09/04
        /** 电表表脚封扣（条码） */
        meterFootNumbers,
        /** 拍照图片的路径(电表表脚封扣) */
        meterFootPicPath,
        /** 表箱封扣1（条码） */
        meterBodyNumbers1,
        /** 拍照图片的路径(表箱封扣1) */
        meterBodyPicPath1,
        /** 表箱封扣2（条码） */
        meterBodyNumbers2,
        /** 拍照图片的路径(表箱封扣2) */
        meterBodyPicPath2,

        /** 拍照图片的路径(集中器) */
        picPath

    }

    //-------------------------------- 变压器 --------------------------------

    /** 变压器 */
    public static enum TRANSFORMER{
        /** 序号(数据库自动生成) */
        _id,
        /** 纬度 */
        latitude,
        /** 经度 */
        longitude,
        /** 抄表区段 */
        theMeteringSection,
        /** 地址 */
        addr,

        /** 拍照图片的路径(变压器) */
        picPath
    }

}
