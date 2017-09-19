package com.zh.metermanagementcw.trasks;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.rscja.deviceapi.Infrared;
import com.zebra.adc.decoder.Barcode2DWithSoft;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;
import com.zh.metermanagementcw.bean.CollectorNumberBean;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.ScatteredNewMeterBean;
import com.zh.metermanagementcw.bean.TransformerBean;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public interface TaskPresenter1 {

    /**
     * 登录
     *
     * @param id
     * @param pwd
     */
    void loginData(String id, String pwd);


    /**
     * 查询
     *  @param plateNumber   车牌
     * @param observer    订阅
     */
    void queryData(String plateNumber, Observer observer);

    /**
     * 查询<p>
     *
     * 必须在Cursor使用之后，才可以关闭数据库连接。 例如：Cursor.moveToNext()执行的时候，才会去查询数据库中是否有数据。<p>
     *
     * Cursor cursor = database.query("person"(这个在某段代码填上了：表名),
     *                                  new String[]{"name,age,phone"}, "name=?", new String[]{"shen"}, null, null, null);
     *
     * @param table             表名
     * @param columns           返回列(要查询的字段)
     * @param selection         查询条件(过滤字段) 例如：id=?，?为通配符
     * @param selectionArgs     条件集合(过滤字段的值) 例如： new String[]{"1"}
     * @param groupBy           分组
     * @param having
     * @param orderBy           排序
     *
     * @param observer          订阅
     *
     */
     void queryData(String table, String[] columns, String selection,
                    String[] selectionArgs, String groupBy, String having,
                    String orderBy, Observer observer);


    /**
     * 添加车牌
     *
     */
    void addPlateNumber();


    /**
     * 将--集改无工单--"表信息"excel导入到数据库中
     *
     * @param observer
     */
    void importMeterInfoExcelToDb(Observer observer, File[] files);

    /**
     * 将--集改无工单--"表信息-电话"excel导入到数据库中
     *
     * @param observer
     */
    void importMeterPhoneExcelToDb(Observer observer, File[] files);

    /**
     * 将数据库中的数据读取到Bean
     *
     * @param observer
     * @return
     */
    List<MeterBean1> readDbToBean(Observer observer, final String meteringSection);


    /**
     * 保存数据
     *
     * @param observer
     * @return
     */
    void saveData(Observer observer, ContentValues values, String whereClause, String[] whereArgs);

    /**
     * 保存"新采集器"
     *
     * @param observer
     * @return
     */
    void saveNewCollector(Observer observer, String NewCollector, String[] MeterList);


    /**
     * 查询(统计)数据
     *
     * @param observer
     * @return
     */
    void statisticsData(Observer observer);


    /**
     * 添加不匹配的资产编码到数据库
     *
     * @param observer
     * @param values
     */
    void addMismatchingAssetNumbers(Observer observer, ContentValues values);


    /**
     * 导出excel
     *
     * @param observer
     * @param context
     * @param meterBeanList                 当前台区的表的数据
     * @param assetNumberBeenList           有表无号
     * @param concentratorBeanList          集中器
     * @param transformerBeanList           变压器
     * @param excelPath1
     * @param excelPath2
     * @param excelPath3
     */
    void generateReportsSetCopyTransformation(Observer observer, Context context, List<MeterBean1> meterBeanList,
                                              List<AssetNumberBean> assetNumberBeenList,
                                              List<ConcentratorBean> concentratorBeanList,
                                              List<TransformerBean> transformerBeanList,
                                              String excelPath1, String excelPath2, String excelPath3);


    /**
     * 获取 -- 抄表区段
     *
     * @param observer
     *
     */
    void getMeteringSection(Observer observer);


    /**
     * 获取--采集器
     *
     * @param observer
     * @param meteringSection       抄表区段
     * @param collector             采集器
     */
    void getCollector(Observer observer, String meteringSection, String collector);

    /**
     * 获取--采集器 -- 抄表区段所有的
     *
     * @param observer
     * @param meteringSection       抄表区段
     */
    void getCollectorList(Observer observer, String meteringSection);


    /**
     * 将数据库中的数据读取到Bean -- 根据采集器
     *
     * @param observer
     * @param meteringSection       抄表区段
     * @param collectorNumber       采集器
     * @return
     */
    List<MeterBean1> readDbToBeanForCollector(Observer observer, final String meteringSection, String collectorNumber);

    /**
     * 添加采集器 -- 到"电表详情的那张表"
     *
     * @param observer
     * @param meteringSection       抄表区段
     * @param collectorNumberBean   采集器 信息
     * @param meterBean1List        要添加到采集器的表 -- 列表
     */
    void addCollectorToMeterInfo(Observer observer, String meteringSection, CollectorNumberBean collectorNumberBean, List<MeterBean1> meterBean1List);

    /**
     * 添加采集器 -- 到"所有采集器那张表"
     *
     * @param observer
     * @param meteringSection               抄表区段
     * @param collectorNumberBean           采集器 信息
     */
    void addCollectorToCollectorTable(Observer observer, String meteringSection, CollectorNumberBean collectorNumberBean);


    /**
     * 根据条件查询表
     *
     * @param observer
     * @param meteringSection       抄表区段
     * @param conditionMap          查询条件
     * @return
     */
    List<MeterBean1> searchMeterInfo(Observer observer, final String meteringSection, HashMap<String, String> conditionMap);



    /**
     * 初始化二维扫描(成为)
     *
     * @param observer
     * @param context
     * @param scanCallback          二维扫描的返回
     * @return
     */
    boolean initBarcode2D(Observer observer, final Context context, Barcode2DWithSoft.ScanCallback scanCallback);



    /**
     * 初始化红外(成为)
     *
     * @param observer
     * @param baudrate          波特率
     * @return
     */
    boolean initInstance(Observer observer, int baudrate);


//    public enum DayOrMonthType{
//        Day,            // 日冻结数据
//        Month           // 月冻结数据
//    }
    /**
     * 将--acceptance--"日冻结数据"excel导入到数据库中
     * @param observer
     * @param files
     */
    void importDayExcelToDb(Observer observer, File[] files);

    /**
     * 将--acceptance--"月冻结数据"excel导入到数据库中
     * @param observer
     * @param files
     */
    void importMonthExcelToDb(Observer observer, File[] files);

    /**
     * 将数据库中的数据读取到Bean -- acceptance -- 日/月冻结
     *
     * @param observer
     * @return
     */
    void readDbToBeanForAcceptance(Observer observer);

    /**
     * 添加 -- 集中器
     *
     * @param observer
     * @param values
     * @param assetNumbers          集中器资产编码
     *
     */
    void addConcentrator(Observer observer, ContentValues values, String assetNumbers);

    /**
     * 获取 -- 集中器
     *
     * @param observer
     * @param meteringSection       抄表区段
     *
     */
    void getConcentrator(Observer observer, String meteringSection);

    /**
     * 添加 -- 变压器
     *
     * @param observer
     * @param values
     * @param meteringSection
     *
     */
    void addTransformer(Observer observer, ContentValues values, String meteringSection);

    /**
     * 获取 -- 变压器
     *
     * @param observer
     * @param meteringSection       抄表区段
     *
     */
    void getTransformer(Observer observer, String meteringSection);


    /**
     * 保存"日冻结/月冻结 -- 数据验收"
     *
     * @param observer
     * @param acceptanceBean
     * @return
     */
    void saveAcceptance(Observer observer, AcceptanceBean acceptanceBean);


    /**
     * 导出excel -- 验收数据(日/月冻结)
     *
     * @param observer
     * @param context
     * @param acceptanceBeanList               验收数据(日/月冻结)
     * @param excelPathByDay                        导出excel文件名
     * @param excelPathByMonth                      导出excel文件名
     */
    void generateReportsAcceptance(Observer observer, Context context,
                                            List<AcceptanceBean> acceptanceBeanList,
                                   String excelPathByDay,
                                   String excelPathByMonth);


    /**
     * 根据条件查询表 -- 数据验收
     *
     * @param observer
     * @param conditionMap          查询条件
     * @return
     */
    List<MeterBean1> searchAcceptance(Observer observer, HashMap<String, String> conditionMap);


    /**
     * 保存 "零散新装"
     *
     * @param observer
     * @param values
     * @param newAssetNumbers           新表资产编号
     *
     * @return
     */
    void saveScatteredNewMeterActivity(Observer observer, ContentValues values, String newAssetNumbers);


    /**
     * 根据条件查询表 -- "零散新装"
     *
     * @param observer
     * @param conditionMap          查询条件
     * @return
     */
    void searchScatteredNewMeter(Observer observer, HashMap<String, String> conditionMap);


    /**
     * 将数据库中的数据读取到Bean -- ScatteredNewMeter -- "零散新装"
     *
     * @param observer
     * @return
     */
    void readDbToBeanForScatteredNewMeter(Observer observer);

    /**
     * 导出excel -- 零散新装--数据
     *
     * @param observer
     * @param context
     * @param scatteredNewMeterBeanList        零散新装--数据
     * @param excelPath                        导出excel文件名
     */
    void generateReportsScatteredNewMeter(Observer observer, Context context,
                                   List<ScatteredNewMeterBean> scatteredNewMeterBeanList,
                                   String excelPath);

}
