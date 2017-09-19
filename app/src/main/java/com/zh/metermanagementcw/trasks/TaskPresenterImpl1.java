package com.zh.metermanagementcw.trasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.rscja.deviceapi.Infrared;
import com.zebra.adc.decoder.Barcode2DWithSoft;
import com.zh.metermanagementcw.application.MyApplication;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.AcceptanceByDayBean;
import com.zh.metermanagementcw.bean.AcceptanceByMonthBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;
import com.zh.metermanagementcw.bean.CollectorNumberBean;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.MeterPhoneBean;
import com.zh.metermanagementcw.bean.ScatteredNewMeterBean;
import com.zh.metermanagementcw.bean.TransformerBean;
import com.zh.metermanagementcw.config.Constant;
import com.zh.metermanagementcw.db.biz.TableEx;
import com.zh.metermanagementcw.db.helper.DataManagement;
import com.zh.metermanagementcw.utils.LogUtils;
import com.zh.metermanagementcw.utils.POIExcelUtil;
import com.zh.metermanagementcw.utils.StringUtils;
import com.zh.metermanagementcw.view.linkagerecyclerview.ShenBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gbh on 16/12/7.
 */

public class TaskPresenterImpl1 implements TaskPresenter1 {


    private Context mContext;
    private TableEx mTableEx;

    public TaskPresenterImpl1(Context context){
        mContext = context;
        mTableEx = new TableEx(MyApplication.getContext());
    }


    @Override
    public void loginData(String id, String pwd) {

    }

    @Override
    public void queryData(String plateNumber, Observer observer) {

    }

    @Override
    public void queryData(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, Observer observer) {

    }

    @Override
    public void addPlateNumber() {

    }


    @Override
    public void importMeterInfoExcelToDb(Observer observer, File[] files) {

        //LogUtils.i("files.length:" + files.length);
        String[] importExcels = new String[files.length];
        for(int i=0; i<files.length; i++){
            //LogUtils.i("files[i].getAbsolutePath():" + files[i].getAbsolutePath());
            importExcels[i] = files[i].getAbsolutePath();

        }

        DataManagement.deleteTable(mContext, Constant.DB_NAME, Constant.TABLE_METERINFO1);
        DataManagement.deleteTable(mContext, Constant.DB_NAME, Constant.TABLE_ASSETNUMBER);
        DataManagement.deleteTable(mContext, Constant.DB_NAME, Constant.TABLE_COLLECTORNUMBER);
        DataManagement.deleteTable(mContext, Constant.DB_NAME, Constant.TABLE_CONCENTRATOR);
        DataManagement.deleteTable(mContext, Constant.DB_NAME, Constant.TABLE_TRANSFORMER);

        Observable.fromArray(importExcels)
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(@NonNull String importExcel)
                            throws Exception {

                        List<MeterBean1> beanList = POIExcelUtil.readMeterInfoExcel(importExcel);
                        //Log.i("shen", "beanList.size():"+beanList.size());

                        long i = 0;

                        if(beanList == null || beanList.size() == 0)
                            return i;

                        for(MeterBean1 bean : beanList) {
                            ContentValues values = new ContentValues();
                            values.put(Constant.TABLE_METERINFO1_STR_userNumber, bean.getUserNumber());
                            values.put(Constant.TABLE_METERINFO1_STR_userName, bean.getUserName());
                            values.put(Constant.TABLE_METERINFO1_STR_userAddr, bean.getUserAddr());
                            values.put(Constant.TABLE_METERINFO1_STR_oldAssetNumbers, bean.getOldAssetNumbers());
                            values.put(Constant.TABLE_METERINFO1_STR_measurementPointNumber, bean.getMeasurementPointNumber());
                            values.put(Constant.TABLE_METERINFO1_STR_powerSupplyBureau, bean.getPowerSupplyBureau());
                            values.put(Constant.TABLE_METERINFO1_STR_theMeteringSection, bean.getTheMeteringSection());
                            values.put(Constant.TABLE_METERINFO1_STR_courts, bean.getCourts());
                            values.put(Constant.TABLE_METERINFO1_STR_measuringPointAddress, bean.getMeasuringPointAddress());
                            values.put(Constant.TABLE_METERINFO1_STR_isFinish, bean.isFinish());

                            i = mTableEx.Add(Constant.TABLE_METERINFO1, values);  // 返回值是  -1  失败

                            //Log.i("shen", "i 返回值：" + i);
                            //Log.i("shen", "bean.toString()：" + bean.toString());
                        }

                        return (long) beanList.size();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void importMeterPhoneExcelToDb(Observer observer, File[] files) {

        //LogUtils.i("files.length:" + files.length);
        String[] importExcels = new String[files.length];
        for(int i=0; i<files.length; i++){
            //LogUtils.i("files[i].getAbsolutePath():" + files[i].getAbsolutePath());
            importExcels[i] = files[i].getAbsolutePath();

        }

        Observable.fromArray(importExcels)
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(@NonNull String importExcel)
                            throws Exception {

                        List<MeterPhoneBean> beanList = POIExcelUtil.readMeterPhoneExcel(importExcel);
                        //Log.i("shen", "beanList.size():"+beanList.size());

                        long i = 0;

                        if(beanList == null || beanList.size() == 0)
                            return i;

                        for(MeterPhoneBean bean : beanList) {

                            ContentValues values = new ContentValues();
                            values.put("userPhone", bean.getUserPhone());

                            i = mTableEx.Update(Constant.TABLE_METERINFO1, values,
                                    "userNumber=?",
                                    new String[]{bean.getUserNumber()});


                            //Log.i("shen", "i 返回值：" + i);
                            //Log.i("shen", "bean.toString()：" + bean.toString());
                        }

                        return (long) beanList.size();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    /**
     *
     * @param observer
     * @param meteringSection  为"" 不筛选
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MeterBean1> readDbToBean(Observer observer, final String meteringSection) {


        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<MeterBean1>>() {
                    @Override
                    public List<MeterBean1> apply(@NonNull String s) throws Exception {

                        Cursor cursor;
                        if(StringUtils.isNotEmpty(meteringSection)) {
                            cursor  = mTableEx.Query(Constant.TABLE_METERINFO1,
                                    null,
                                    "theMeteringSection=?",
                                    new String[]{meteringSection},
                                    null, null, null);
                        }else {
                            cursor  = mTableEx.Query(Constant.TABLE_METERINFO1,
                                    null,
                                    null,
                                    null,
                                    null, null, null);
                        }

                        List<MeterBean1> beanList = new ArrayList<MeterBean1>();
                        if(cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    MeterBean1 bean = new MeterBean1();

                                    try {

                                        bean.setUserNumber(getValue(cursor, Constant.TABLE_METERINFO1_STR_userNumber));
                                        bean.setUserName(getValue(cursor, Constant.TABLE_METERINFO1_STR_userName));
                                        bean.setUserAddr(getValue(cursor, Constant.TABLE_METERINFO1_STR_userAddr));
                                        bean.setUserPhone(getValue(cursor, Constant.TABLE_METERINFO1_STR_userPhone));
                                        bean.setMeasurementPointNumber(getValue(cursor, Constant.TABLE_METERINFO1_STR_measurementPointNumber));
                                        bean.setPowerSupplyBureau(getValue(cursor, Constant.TABLE_METERINFO1_STR_powerSupplyBureau));
                                        bean.setTheMeteringSection(getValue(cursor, Constant.TABLE_METERINFO1_STR_theMeteringSection));
                                        bean.setCourts(getValue(cursor, Constant.TABLE_METERINFO1_STR_courts));
                                        bean.setMeasuringPointAddress(getValue(cursor, Constant.TABLE_METERINFO1_STR_measuringPointAddress));

                                        bean.setOldAssetNumbers(getValue(cursor,Constant.TABLE_METERINFO1_STR_oldAssetNumbers));
                                        bean.setOldAddr(getValue(cursor, Constant.TABLE_METERINFO1_STR_oldAddr));
                                        //------------------------
                                        String oldAddrAndAsset = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldAddrAndAsset));
                                        if(StringUtils.isNotEmpty(oldAddrAndAsset) ) {
                                            bean.setOldAddrAndAsset(oldAddrAndAsset.equals("1"));
                                        }else {
                                            bean.setOldAddrAndAsset(false);
                                        }
                                        //------------------------
                                        bean.setOldElectricity(getValue(cursor, Constant.TABLE_METERINFO1_STR_oldElectricity));
                                        bean.setNewAddr(getValue(cursor,Constant.TABLE_METERINFO1_STR_newAddr));
                                        //------------------------
                                        String newAddrAndAsset = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newAddrAndAsset));
                                        if(StringUtils.isNotEmpty(newAddrAndAsset) ) {
                                            bean.setNewAddrAndAsset(newAddrAndAsset.equals("1"));
                                        }else {
                                            bean.setNewAddrAndAsset(false);
                                        }
                                        //------------------------
                                        bean.setNewAssetNumbersScan(getValue(cursor,Constant.TABLE_METERINFO1_STR_newAssetNumbersScan));
                                        bean.setNewElectricity(getValue(cursor,Constant.TABLE_METERINFO1_STR_newElectricity));
                                        bean.setCollectorAssetNumbersScan(getValue(cursor,Constant.TABLE_METERINFO1_STR_collectorAssetNumbersScan));
                                        bean.setTime(getValue(cursor,Constant.TABLE_METERINFO1_STR_time));
                                        bean.setPicPath(getValue(cursor,Constant.TABLE_METERINFO1_STR_picPath));
                                        bean.setMeterPicPath(getValue(cursor,Constant.TABLE_METERINFO1_STR_meterPicPath));
                                        bean.setMeterContentPicPath(getValue(cursor,Constant.TABLE_METERINFO1_STR_meterContentPicPath));
                                        bean.setRelaceOrAnd(getValue(cursor, Constant.TABLE_METERINFO1_STR_relaceOrAnd));

                                        //----2017/09/04
                                        bean.setMeterFootNumbers(getValue(cursor, Constant.TABLE_METERINFO1_STR_meterFootNumbers));
                                        bean.setMeterFootPicPath(getValue(cursor, Constant.TABLE_METERINFO1_STR_meterFootPicPath));
                                        bean.setMeterBodyNumbers1(getValue(cursor, Constant.TABLE_METERINFO1_STR_meterBodyNumbers1));
                                        bean.setMeterBodyPicPath1(getValue(cursor, Constant.TABLE_METERINFO1_STR_meterBodyPicPath1));
                                        bean.setMeterBodyNumbers2(getValue(cursor, Constant.TABLE_METERINFO1_STR_meterBodyNumbers2));
                                        bean.setMeterBodyPicPath2(getValue(cursor, Constant.TABLE_METERINFO1_STR_meterBodyPicPath2));

                                        //----2017/09/06
                                        bean.setCollectorFootNumbers(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorFootNumbers));
                                        bean.setCollectorFootPicPath(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorFootPicPath));
                                        bean.setCollectorBodyNumbers1(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorBodyNumbers1));
                                        bean.setCollectorBodyPicPath1(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorBodyPicPath1));
                                        bean.setCollectorBodyNumbers2(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorBodyNumbers2));
                                        bean.setCollectorBodyPicPath2(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorBodyPicPath2));

                                        //------------------------
                                        String isFinish = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_isFinish));
                                        if(StringUtils.isNotEmpty(isFinish) ) {
                                            bean.setFinish(isFinish.equals("1"));
                                        }else {
                                            bean.setFinish(false);
                                        }
                                        //------------------------

                                    }catch (Exception e){
                                        LogUtils.i("e.getMessage()22222:" + e.getMessage());  // 因为有些空值
                                    }

                                    //Log.i("shen", "nei:" + meterBean.toString());
                                    beanList.add(bean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "beanList.size():"+beanList.size());

                        return beanList;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        return null;
    }


    @Override
    public void saveData(Observer observer, final ContentValues values, final String whereClause, final String[] whereArgs) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(@NonNull String s) throws Exception {

                        long i = mTableEx.Update(Constant.TABLE_METERINFO1, values, whereClause, whereArgs);

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    @Override
    public void saveNewCollector(Observer observer, final String NewCollector, String[] meters) {
        Observable.fromArray(meters)
                .observeOn(Schedulers.io())
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(@NonNull String s) throws Exception {

                        ContentValues values = new ContentValues();
                        values.put("collectorAssetNumbersScan", NewCollector);
                        long i = mTableEx.Update(Constant.TABLE_METERINFO, values, "oldAssetNumbers=?", new String[]{s});

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override
    public void statisticsData(Observer observer) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<AssetNumberBean>>() {
                    @Override
                    public List<AssetNumberBean> apply(@NonNull String s) throws Exception {

                        Cursor cursor = mTableEx.Query(Constant.TABLE_ASSETNUMBER,
                                null,
                                null,
                                null,
                                null, null, null);

                        List<AssetNumberBean> beanList = new ArrayList<AssetNumberBean>();
                        if(cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    AssetNumberBean assetNumberBean = new AssetNumberBean();

                                    try {


                                        assetNumberBean.setAssetNumbers(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_ASSETNUMBER_STR_assetNumbers)));

                                    }catch (Exception e){
                                        LogUtils.i("e.getMessage():" + e.getMessage());
                                    }

                                    //LogUtils.i("nei:" + assetNumberBean.toString());
                                    beanList.add(assetNumberBean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "beanList.size():"+beanList.size());

                        return beanList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void addMismatchingAssetNumbers(Observer observer, final ContentValues values) {
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(@NonNull String s) throws Exception {

                        Long i = mTableEx.Add(Constant.TABLE_ASSETNUMBER, values);

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void generateReportsSetCopyTransformation(Observer observer, final Context context,
                                                     final List<MeterBean1> meterBeanList,
                                                     final List<AssetNumberBean> assetNumberBeenList,
                                                     final List<ConcentratorBean> concentratorBeanList,
                                                     final List<TransformerBean> transformerBeanList,
                                                     final String excelPath1,
                                                     final String excelPath2,
                                                     final String excelPath3) {
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String s) throws Exception {

                        //String str = ExcelUtil.writeExcel1(context, meterBeanList, assetNumberBeenList);

                        POIExcelUtil.writeExcel1(context, meterBeanList,
                                assetNumberBeenList,
                                concentratorBeanList,
                                transformerBeanList,
                                excelPath1);
                        POIExcelUtil.writeExcel2(context, meterBeanList,  excelPath2);
                        POIExcelUtil.writeExcel3(context, meterBeanList,  excelPath3);

                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    @Override
    public void getMeteringSection(Observer observer) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<ShenBean>>() {
                    @Override
                    public List<ShenBean> apply(@NonNull String s) throws Exception {

                        /**
                         * 查询<p>
                         *
                         * 必须在Cursor使用之后，才可以关闭数据库连接。 例如：Cursor.moveToNext()执行的时候，才会去查询数据库中是否有数据。<p>
                         *
                         * Cursor cursor = database.query("person"(这个在某段代码填上了：表名),
                         *                                  new String[]{"name,age,phone"}, "name=?",
                         *                                  new String[]{"shen"}, null, null, null);
                         *
                         * @param columns           返回列(要查询的字段)
                         * @param selection         查询条件(过滤字段) 例如：id=?，?为通配符
                         * @param selectionArgs     条件集合(过滤字段的值) 例如： new String[]{"1"}
                         * @param groupBy           分组
                         * @param having
                         * @param orderBy           排序
                         *
                         */
                        Cursor cursor = mTableEx.Query(Constant.TABLE_METERINFO1,
                                new String[]{"powerSupplyBureau","courts","theMeteringSection"},
                                null,
                                null,
                                "theMeteringSection,courts", //"theMeteringSection,courts,powerSupplyBureau",
                                null, "powerSupplyBureau,courts");

                        List<ShenBean> beanList = new ArrayList<ShenBean>();
                        String powerSupplyBureau = "";
                        boolean isStart = true;
                        ShenBean shenBean = null;
                        int count = cursor.getCount();
                        LogUtils.i("ount:" + count);
                        int j = 0;
                        if(count != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    j++;
                                    try {
                                        String supplyBureau = cursor.getString(0);
                                        if(!TextUtils.isEmpty(supplyBureau) && isStart){
                                            isStart = false;
                                            shenBean = new ShenBean();
                                            shenBean.setPowerSupplyBureau(cursor.getString(0));
                                            ArrayList<ShenBean.SupplyBureau> supplyBureaus = new ArrayList<ShenBean.SupplyBureau>();
                                            ShenBean.SupplyBureau bureau = new ShenBean.SupplyBureau();
                                            bureau.setCourts(cursor.getString(1));
                                            bureau.setTheMeteringSection(cursor.getString(2));
                                            if(!bureau.getTheMeteringSection().toString().trim().equals(""))
                                                supplyBureaus.add(bureau);
                                            shenBean.setSupplyBureaus(supplyBureaus);
                                            powerSupplyBureau = supplyBureau;

                                        }else if (!TextUtils.isEmpty(supplyBureau) && supplyBureau.equals(powerSupplyBureau ) && !isStart){
                                            ArrayList<ShenBean.SupplyBureau> supplyBureaus = shenBean.getSupplyBureaus();
                                            ShenBean.SupplyBureau bureau = new ShenBean.SupplyBureau();
                                            bureau.setCourts(cursor.getString(1));
                                            bureau.setTheMeteringSection(cursor.getString(2));
                                            if(!bureau.getTheMeteringSection().toString().trim().equals(""))
                                                supplyBureaus.add(bureau);
                                            shenBean.setSupplyBureaus(supplyBureaus);

                                        }else if(!TextUtils.isEmpty(supplyBureau) && !supplyBureau.equals(powerSupplyBureau) && !isStart){
                                            beanList.add(shenBean);
                                            shenBean = new ShenBean();
                                            shenBean.setPowerSupplyBureau(cursor.getString(0));
                                            ArrayList<ShenBean.SupplyBureau> supplyBureaus = new ArrayList<ShenBean.SupplyBureau>();
                                            ShenBean.SupplyBureau bureau = new ShenBean.SupplyBureau();
                                            bureau.setCourts(cursor.getString(1));
                                            bureau.setTheMeteringSection(cursor.getString(2));
                                            if(!bureau.getTheMeteringSection().toString().trim().equals(""))
                                                supplyBureaus.add(bureau);
                                            shenBean.setSupplyBureaus(supplyBureaus);
                                        }

                                        powerSupplyBureau = supplyBureau;
                                    }catch (Exception e){
                                        LogUtils.i("e.getMessage():" + e.getMessage());
                                    }

                                    if(j == count){
                                        beanList.add(shenBean);
                                    }
                                }while (cursor.moveToNext());
                            }
                        }

                        for (ShenBean shenBean1 : beanList){
                            //LogUtils.i("shenBean1.toString():" + shenBean1.toString());
                        }

                        Log.i("shen", "beanList.size():"+beanList.size());

                        return beanList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void getCollector(Observer observer, final String meteringSection, final String collector) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, CollectorNumberBean>() {
                    @Override
                    public CollectorNumberBean apply(@NonNull String s) throws Exception {

                        /**
                         * 查询<p>
                         *
                         * 必须在Cursor使用之后，才可以关闭数据库连接。 例如：Cursor.moveToNext()执行的时候，才会去查询数据库中是否有数据。<p>
                         *
                         * Cursor cursor = database.query("person"(这个在某段代码填上了：表名),
                         *                                  new String[]{"name,age,phone"}, "name=?",
                         *                                  new String[]{"shen"}, null, null, null);
                         *
                         * @param columns           返回列(要查询的字段)
                         * @param selection         查询条件(过滤字段) 例如：id=?，?为通配符
                         * @param selectionArgs     条件集合(过滤字段的值) 例如： new String[]{"1"}
                         * @param groupBy           分组
                         * @param having
                         * @param orderBy           排序
                         *
                         */
                        Cursor cursor = mTableEx.Query(Constant.TABLE_COLLECTORNUMBER,
                                null,
                                "theMeteringSection=? and collectorNumbers=?",
                                new String[]{meteringSection, collector},
                                null, null, null);


                        CollectorNumberBean bean = new CollectorNumberBean();
                        if(cursor != null) {
                            int count = cursor.getCount();

                            if (count != 0) {
                                if (cursor.moveToFirst()) {
                                    bean.setCollectorNumbers(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorNumbers));
                                    bean.setTheMeteringSection(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_theMeteringSection));
                                    bean.setCollectorPicPath(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorPicPath));

                                    //----2017/09/06
                                    bean.setCollectorFootNumbers(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorFootNumbers));
                                    bean.setCollectorFootPicPath(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorFootPicPath));
                                    bean.setCollectorBodyNumbers1(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorBodyNumbers1));
                                    bean.setCollectorBodyPicPath1(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorBodyPicPath1));
                                    bean.setCollectorBodyNumbers2(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorBodyNumbers2));
                                    bean.setCollectorBodyPicPath2(getValue(cursor, Constant.TABLE_METERINFO1_STR_collectorBodyPicPath2));

                                }
                            }
                        }
                        return bean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void getCollectorList(Observer observer, final String meteringSection) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, ArrayList<CollectorNumberBean>>() {
                    @Override
                    public ArrayList<CollectorNumberBean> apply(@NonNull String s) throws Exception {

                        /**
                         * 查询<p>
                         *
                         * 必须在Cursor使用之后，才可以关闭数据库连接。 例如：Cursor.moveToNext()执行的时候，才会去查询数据库中是否有数据。<p>
                         *
                         * Cursor cursor = database.query("person"(这个在某段代码填上了：表名),
                         *                                  new String[]{"name,age,phone"}, "name=?",
                         *                                  new String[]{"shen"}, null, null, null);
                         *
                         * @param columns           返回列(要查询的字段)
                         * @param selection         查询条件(过滤字段) 例如：id=?，?为通配符
                         * @param selectionArgs     条件集合(过滤字段的值) 例如： new String[]{"1"}
                         * @param groupBy           分组
                         * @param having
                         * @param orderBy           排序
                         *
                         */
                        Cursor cursor = mTableEx.Query(Constant.TABLE_COLLECTORNUMBER,
                                null,
                                "theMeteringSection=?",
                                new String[]{meteringSection},
                                null, null, null);


                        ArrayList<CollectorNumberBean> beanList = new ArrayList<CollectorNumberBean>();
                        if(cursor != null) {
                            int count = cursor.getCount();

                            if (count != 0) {
                                if (cursor.moveToFirst()) {
                                    do{
                                        CollectorNumberBean bean = new CollectorNumberBean();
                                        bean.setCollectorNumbers(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorNumbers));
                                        bean.setTheMeteringSection(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_theMeteringSection));
                                        bean.setCollectorPicPath(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorPicPath));

                                        //----2017/09/06
                                        bean.setCollectorFootNumbers(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorFootNumbers));
                                        bean.setCollectorFootPicPath(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorFootPicPath));
                                        bean.setCollectorBodyNumbers1(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorBodyNumbers1));
                                        bean.setCollectorBodyPicPath1(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorBodyPicPath1));
                                        bean.setCollectorBodyNumbers2(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorBodyNumbers2));
                                        bean.setCollectorBodyPicPath2(getValue(cursor, Constant.TABLE_COLLECTORNUMBER_STR_collectorBodyPicPath2));

                                        beanList.add(bean);
                                    }while (cursor.moveToNext());
                                }
                            }
                        }
                        return beanList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public List<MeterBean1> readDbToBeanForCollector(Observer observer, final String meteringSection, final String collectorNumber) {
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<MeterBean1>>() {
                    @Override
                    public List<MeterBean1> apply(@NonNull String s) throws Exception {

                        Cursor cursor;
                        cursor  = mTableEx.Query(Constant.TABLE_METERINFO1,
                                null,
                                "theMeteringSection=? and collectorAssetNumbersScan=?",
                                new String[]{meteringSection, collectorNumber},
                                null, null, null);

                        List<MeterBean1> beanList = new ArrayList<MeterBean1>();
                        if(cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    MeterBean1 meterBean = new MeterBean1();

                                    try {

                                        meterBean.setUserNumber(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_userNumber)));
                                        meterBean.setUserName(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_userName)));
                                        meterBean.setUserAddr(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_userAddr)));
                                        meterBean.setUserPhone(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_userPhone)));
                                        meterBean.setMeasurementPointNumber(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_measurementPointNumber)));
                                        meterBean.setPowerSupplyBureau(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_powerSupplyBureau)));
                                        meterBean.setTheMeteringSection(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_theMeteringSection)));
                                        meterBean.setCourts(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_courts)));
                                        meterBean.setMeasuringPointAddress(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_measuringPointAddress)));

                                        meterBean.setOldAssetNumbers(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldAssetNumbers)));
                                        meterBean.setOldAddr(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldAddr)));
                                        //------------------------
                                        String oldAddrAndAsset = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldAddrAndAsset));
                                        if(StringUtils.isNotEmpty(oldAddrAndAsset) ) {
                                            meterBean.setOldAddrAndAsset(oldAddrAndAsset.equals("1"));
                                        }else {
                                            meterBean.setOldAddrAndAsset(false);
                                        }
                                        //------------------------
                                        meterBean.setOldElectricity(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldElectricity)));
                                        meterBean.setNewAddr(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newAddr)));
                                        //------------------------
                                        String newAddrAndAsset = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newAddrAndAsset));
                                        if(StringUtils.isNotEmpty(newAddrAndAsset) ) {
                                            meterBean.setNewAddrAndAsset(newAddrAndAsset.equals("1"));
                                        }else {
                                            meterBean.setNewAddrAndAsset(false);
                                        }
                                        //------------------------
                                        meterBean.setNewAssetNumbersScan(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newAssetNumbersScan)));
                                        meterBean.setNewElectricity(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newElectricity)));
                                        meterBean.setCollectorAssetNumbersScan(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_collectorAssetNumbersScan)));
                                        meterBean.setTime(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_time)));
                                        meterBean.setPicPath(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_picPath)));
                                        meterBean.setMeterPicPath(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_meterPicPath)));
                                        meterBean.setMeterContentPicPath(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_meterContentPicPath)));
                                        meterBean.setRelaceOrAnd(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_relaceOrAnd)));
                                        //------------------------
                                        String isFinish = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_isFinish));
                                        if(StringUtils.isNotEmpty(isFinish) ) {
                                            meterBean.setFinish(isFinish.equals("1"));
                                        }else {
                                            meterBean.setFinish(false);
                                        }
                                        //------------------------
                                    }catch (Exception e){
                                        LogUtils.i("readDbToBeanForCollector -- e.getMessage()3:" + e.getMessage());  // 因为有些空值
                                    }

                                    //Log.i("shen", "nei:" + meterBean.toString());
                                    beanList.add(meterBean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "readDbToBeanForCollector -- beanList.size():"+beanList.size());

                        return beanList;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        return null;
    }

    @Override
    public void addCollectorToMeterInfo(Observer observer, final String meteringSection, final CollectorNumberBean collectorNumberBean, List<MeterBean1> meterBean1List) {


        MeterBean1[] meterBean1arr = new MeterBean1[meterBean1List.size()];
        for(int i=0; i< meterBean1List.size(); i++){
            meterBean1arr[i] = meterBean1List.get(i);
        }

        Observable.fromArray(meterBean1arr)
                .observeOn(Schedulers.io())
                .map(new Function<MeterBean1, Long>() {

                    @Override
                    public Long apply(@NonNull MeterBean1 meterBean1) throws Exception {

                        ContentValues values = new ContentValues();
                        values.put("collectorAssetNumbersScan", collectorNumberBean.getCollectorNumbers());

                        values.put("collectorFootNumbers", collectorNumberBean.getCollectorFootNumbers());
                        values.put("collectorFootPicPath", collectorNumberBean.getCollectorFootPicPath());
                        values.put("collectorBodyNumbers1", collectorNumberBean.getCollectorBodyNumbers1());
                        values.put("collectorBodyPicPath1", collectorNumberBean.getCollectorBodyPicPath1());
                        values.put("collectorBodyNumbers2", collectorNumberBean.getCollectorBodyNumbers2());
                        values.put("collectorBodyPicPath2", collectorNumberBean.getCollectorBodyPicPath2());

                        values.put("relaceOrAnd", "1");         // 0:"换表"； 1："新装采集器" -- 要先判断是否抄完
                        values.put("meterPicPath", meterBean1.getMeterPicPath());
                        values.put("isFinish", true);

                        long i = mTableEx.Update(Constant.TABLE_METERINFO1, values,
                                "theMeteringSection=? and oldAssetNumbers=?",
                                new String[]{meteringSection, meterBean1.getOldAssetNumbers()});

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }



    @Override
    public void addCollectorToCollectorTable(Observer observer, final String meteringSection, final CollectorNumberBean collectorNumberBean) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {

                    @Override
                    public Long apply(@NonNull String s) throws Exception {

                        LogUtils.i("CollectorNumberBean:" + collectorNumberBean.toString());

                        ContentValues values = new ContentValues();

                        values.put("collectorFootNumbers", collectorNumberBean.getCollectorFootNumbers());
                        values.put("collectorFootPicPath", collectorNumberBean.getCollectorFootPicPath());
                        values.put("collectorBodyNumbers1", collectorNumberBean.getCollectorBodyNumbers1());
                        values.put("collectorBodyPicPath1", collectorNumberBean.getCollectorBodyPicPath1());
                        values.put("collectorBodyNumbers2", collectorNumberBean.getCollectorBodyNumbers2());
                        values.put("collectorBodyPicPath2", collectorNumberBean.getCollectorBodyPicPath2());

                        values.put("collectorNumbers", collectorNumberBean.getCollectorNumbers());
                        values.put("theMeteringSection", meteringSection);         // 0:"换表"； 1："新装采集器" -- 要先判断是否抄完
                        values.put("collectorPicPath", collectorNumberBean.getCollectorPicPath());


                        Cursor cursor =mTableEx.Query(Constant.TABLE_COLLECTORNUMBER,
                                null,
                                "collectorNumbers=?",
                                new String[]{collectorNumberBean.getCollectorNumbers()},
                                null,
                                null,
                                null);

                        long i = 0;
                        if(cursor != null) {
                            int count = cursor.getCount();

                            LogUtils.i("count:" + count);

                            if (count > 0) {
                                i = mTableEx.Update(Constant.TABLE_COLLECTORNUMBER, values,
                                        "collectorNumbers=?",
                                        new String[]{collectorNumberBean.getCollectorNumbers()});
                            } else {
                                i = mTableEx.Add(Constant.TABLE_COLLECTORNUMBER, values);
                            }



                        } else {
                            i = mTableEx.Add(Constant.TABLE_COLLECTORNUMBER, values);
                        }

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public List<MeterBean1> searchMeterInfo(Observer observer, final String meteringSection, HashMap<String, String> conditionMap) {

        int size = conditionMap.keySet().size();
        if(size <=0 ){
            return null;
        }

        String selection = "";
        final String[] selectionArgs = new String[size];

        int i = 0;
        for(String set : conditionMap.keySet()){
//            if(i == size-1){                                            // 最后一个
//                if(set.equals("userNumber") || set.equals("userName")) {
//                    selectionArgs[i] = "%" + conditionMap.get(set) + "%";
//                    selection += set + " like ?";
//
//                }else {
//
//                    selection += set + "=?";
//                    selectionArgs[i] = conditionMap.get(set);
//                }
//            }else {
//
//                if(set.equals("userNumber") || set.equals("userName")) {
//                    selectionArgs[i] = "%" + conditionMap.get(set) + "%";
//                    selection += set + " like ? and ";
//
//                }else {
//                    selection += set + "=? and ";
//                    selectionArgs[i] = conditionMap.get(set);
//                }
//            }
            if(i == size-1){                                            // 最后一个
                selectionArgs[i] = "%" + conditionMap.get(set) + "%";
                selection += set + " like ?";
            }else {
                selectionArgs[i] = "%" + conditionMap.get(set) + "%";
                selection += set + " like ? and ";
            }

            i++;
        }

        LogUtils.i(selection);

        final String finalSelection = selection;
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<MeterBean1>>() {
                    @Override
                    public List<MeterBean1> apply(@NonNull String s) throws Exception {

                        Cursor cursor;
                        cursor  = mTableEx.Query(Constant.TABLE_METERINFO1,
                                null,
                                finalSelection,
                                selectionArgs,
                                null, null, null);

                        List<MeterBean1> beanList = new ArrayList<MeterBean1>();
                        if(cursor != null && cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    MeterBean1 meterBean = new MeterBean1();

                                    try {

                                        meterBean.setUserNumber(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_userNumber)));
                                        meterBean.setUserName(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_userName)));
                                        meterBean.setUserAddr(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_userAddr)));
                                        meterBean.setUserPhone(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_userPhone)));
                                        meterBean.setMeasurementPointNumber(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_measurementPointNumber)));
                                        meterBean.setPowerSupplyBureau(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_powerSupplyBureau)));
                                        meterBean.setTheMeteringSection(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_theMeteringSection)));
                                        meterBean.setCourts(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_courts)));
                                        meterBean.setMeasuringPointAddress(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_measuringPointAddress)));

                                        meterBean.setOldAssetNumbers(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldAssetNumbers)));
                                        meterBean.setOldAddr(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldAddr)));
                                        //------------------------
                                        String oldAddrAndAsset = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldAddrAndAsset));
                                        if(StringUtils.isNotEmpty(oldAddrAndAsset) ) {
                                            meterBean.setOldAddrAndAsset(oldAddrAndAsset.equals("1"));
                                        }else {
                                            meterBean.setOldAddrAndAsset(false);
                                        }
                                        //------------------------
                                        meterBean.setOldElectricity(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_oldElectricity)));
                                        meterBean.setNewAddr(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newAddr)));
                                        //------------------------
                                        String newAddrAndAsset = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newAddrAndAsset));
                                        if(StringUtils.isNotEmpty(newAddrAndAsset) ) {
                                            meterBean.setNewAddrAndAsset(newAddrAndAsset.equals("1"));
                                        }else {
                                            meterBean.setNewAddrAndAsset(false);
                                        }
                                        //------------------------
                                        meterBean.setNewAssetNumbersScan(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newAssetNumbersScan)));
                                        meterBean.setNewElectricity(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_newElectricity)));
                                        meterBean.setCollectorAssetNumbersScan(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_collectorAssetNumbersScan)));
                                        meterBean.setTime(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_time)));
                                        meterBean.setPicPath(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_picPath)));
                                        meterBean.setMeterPicPath(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_meterPicPath)));
                                        meterBean.setMeterContentPicPath(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_meterContentPicPath)));
                                        meterBean.setRelaceOrAnd(cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_relaceOrAnd)));
                                        //------------------------
                                        String isFinish = cursor.getString(
                                                cursor.getColumnIndex(Constant.TABLE_METERINFO1_STR_isFinish));
                                        if(StringUtils.isNotEmpty(isFinish) ) {
                                            meterBean.setFinish(isFinish.equals("1"));
                                        }else {
                                            meterBean.setFinish(false);
                                        }
                                        //------------------------
                                    }catch (Exception e){
                                        LogUtils.i("readDbToBeanForCollector -- e.getMessage()3:" + e.getMessage());  // 因为有些空值
                                    }

                                    //Log.i("shen", "nei:" + meterBean.toString());
                                    beanList.add(meterBean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "readDbToBeanForCollector -- beanList.size():"+beanList.size());

                        return beanList;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


        return null;
    }

    @Override
    public boolean initBarcode2D(Observer observer, final Context context,
                                 final Barcode2DWithSoft.ScanCallback scanCallback) {


        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Barcode2DWithSoft>() {

                    @Override
                    public Barcode2DWithSoft apply(@NonNull String s) throws Exception {


                        boolean reuslt = false;
                        Barcode2DWithSoft barcode2DWithSoft = Barcode2DWithSoft.getInstance();
                        if(barcode2DWithSoft != null) {
                            reuslt =  barcode2DWithSoft.open(context);
                            LogUtils.i("open barcode2DWithSoft = "+reuslt);
                            barcode2DWithSoft.setScanCallback(scanCallback);
                        }

                        if(reuslt)
                            return barcode2DWithSoft;
                        else
                            return null;
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


        return false;
    }

    //Instance
    //mInstance = Infrared.getInstance();

    @Override
    public boolean initInstance(Observer observer, final int baudrate) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Infrared>() {

                    @Override
                    public Infrared apply(@NonNull String s) throws Exception {


                        boolean reuslt = false;
                        Infrared instance = Infrared.getInstance();

                        if(instance.open(baudrate, 2)){  //
                            return  instance;
                        }else {
                            return null;
                        }


                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        return false;
    }



    @Override
    public void importDayExcelToDb(Observer observer, File[] files) {

        //LogUtils.i("files.length:" + files.length);
        String[] importExcels = new String[files.length];
        for(int i=0; i<files.length; i++){
            //LogUtils.i("files[i].getAbsolutePath():" + files[i].getAbsolutePath());
            importExcels[i] = files[i].getAbsolutePath();
        }

        DataManagement.deleteTable(mContext, Constant.DB_NAME, Constant.TABLE_ACCEPTANCE);

        Observable.fromArray(importExcels)
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(@NonNull String importExcel)
                            throws Exception {

                        List<AcceptanceByDayBean> beanList = POIExcelUtil.readDaysFreezingExcel(importExcel);
                        //Log.i("shen", "beanList.size():"+beanList.size());

                        long i = 0;

                        if(beanList == null || beanList.size() == 0)
                            return i;

                        long count = 0;

                        for(AcceptanceByDayBean bean : beanList) {
                            ContentValues values = new ContentValues();
                            values.put(Constant.ACCEPTANCE.userNumber.toString(), bean.getUserNumber());
                            values.put(Constant.ACCEPTANCE.assetNumbers.toString(), bean.getAssetNumbers());
                            values.put(Constant.ACCEPTANCE.userName.toString(), bean.getUserName());
                            values.put(Constant.ACCEPTANCE.meterAddr.toString(), bean.getMeterAddr());
                            values.put(Constant.ACCEPTANCE.terminalNo.toString(), bean.getTerminalNo());
                            values.put(Constant.ACCEPTANCE.daysFreezingTimeIn.toString(), bean.getDaysFreezingTimeIn());
                            values.put(Constant.ACCEPTANCE.electricityInByDay.toString(), bean.getElectricityInByDay());

                            i = mTableEx.Update(Constant.TABLE_ACCEPTANCE, values,
                                    Constant.ACCEPTANCE.assetNumbers.toString() + "=?",
                                    new String[]{bean.getAssetNumbers()});  // 返回值是  -1  失败

                            if(i <= 0){
                                i = mTableEx.Add(Constant.TABLE_ACCEPTANCE, values);  // 返回值是  -1  失败
                            }

                            if(i>0){
                                count++;
                            }
                            //Log.i("shen", "i 返回值：" + i);
                            //Log.i("shen", "bean.toString()：" + bean.toString());
                        }

                        return count;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    @Override
    public void importMonthExcelToDb(Observer observer, File[] files) {


        //LogUtils.i("files.length:" + files.length);
        String[] importExcels = new String[files.length];
        for(int i=0; i<files.length; i++){
            //LogUtils.i("files[i].getAbsolutePath():" + files[i].getAbsolutePath());
            importExcels[i] = files[i].getAbsolutePath();
        }

        //DataManagement.deleteTable(mContext, Constant.DB_NAME, Constant.TABLE_METERINFO1);

        Observable.fromArray(importExcels)
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(@NonNull String importExcel)
                            throws Exception {

                        List<AcceptanceByMonthBean> beanList = POIExcelUtil.readMonthFreezingExcel(importExcel);
                        //Log.i("shen", "beanList.size():"+beanList.size());

                        long i = 0;

                        if(beanList == null || beanList.size() == 0)
                            return i;

                        long count = 0;

                        for(AcceptanceByMonthBean bean : beanList) {
                            ContentValues values = new ContentValues();
                            values.put(Constant.ACCEPTANCE.userNumber.toString(), bean.getUserNumber());
                            values.put(Constant.ACCEPTANCE.assetNumbers.toString(), bean.getAssetNumbers());
                            values.put(Constant.ACCEPTANCE.userName.toString(), bean.getUserName());
                            values.put(Constant.ACCEPTANCE.meterAddr.toString(), bean.getMeterAddr());
                            values.put(Constant.ACCEPTANCE.terminalNo.toString(), bean.getTerminalNo());
                            values.put(Constant.ACCEPTANCE.monthFreezingTimeIn.toString(), bean.getMonthFreezingTimeIn());
                            values.put(Constant.ACCEPTANCE.electricityInByMonth.toString(), bean.getElectricityInByMonth());

                            i = mTableEx.Update(Constant.TABLE_ACCEPTANCE, values,
                                    Constant.ACCEPTANCE.assetNumbers.toString() + "=?",
                                    new String[]{bean.getAssetNumbers()});  // 返回值是  -1  失败

                            if(i <= 0){
                                i = mTableEx.Add(Constant.TABLE_ACCEPTANCE, values);  // 返回值是  -1  失败
                            }

                            if(i>0){
                                count++;
                            }
                            //Log.i("shen", "i 返回值：" + i);
                            //Log.i("shen", "bean.toString()：" + bean.toString());
                        }

                        return (long) beanList.size();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override
    public void readDbToBeanForAcceptance(Observer observer) {
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<AcceptanceBean>>() {
                    @Override
                    public List<AcceptanceBean> apply(@NonNull String s) throws Exception {

                        Cursor cursor;
                        cursor  = mTableEx.Query(Constant.TABLE_ACCEPTANCE,
                                null,
                                null,
                                null,
                                null, null, null);

                        List<AcceptanceBean> beanList = new ArrayList<>();
                        if(cursor != null && cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {

                                do{
                                    AcceptanceBean bean = new AcceptanceBean();

                                    try {


                                        bean.setUserNumber(getValue(cursor, Constant.ACCEPTANCE.userNumber.toString()));
                                        bean.setAssetNumbers(getValue(cursor, Constant.ACCEPTANCE.assetNumbers.toString()));
                                        bean.setUserName(getValue(cursor, Constant.ACCEPTANCE.userName.toString()));
                                        bean.setMeterAddr(getValue(cursor, Constant.ACCEPTANCE.meterAddr.toString()));
                                        bean.setTerminalNo(getValue(cursor, Constant.ACCEPTANCE.terminalNo.toString()));

                                        //---------------------------
                                        bean.setDaysFreezingTimeIn(getValue(cursor, Constant.ACCEPTANCE.daysFreezingTimeIn.toString()));
                                        bean.setElectricityInByDay(getValue(cursor, Constant.ACCEPTANCE.electricityInByDay.toString()));
                                        bean.setMonthFreezingTimeIn(getValue(cursor, Constant.ACCEPTANCE.monthFreezingTimeIn.toString()));
                                        bean.setElectricityInByMonth(getValue(cursor, Constant.ACCEPTANCE.electricityInByMonth.toString()));

                                        bean.setElectricityScanByDay(getValue(cursor, Constant.ACCEPTANCE.electricityScanByDay.toString()));
                                        bean.setDaysFreezingTimeScan(getValue(cursor, Constant.ACCEPTANCE.daysFreezingTimeScan.toString()));
                                        bean.setDifferencesByDay(getValue(cursor, Constant.ACCEPTANCE.differencesByDay.toString()));
                                        bean.setConclusionByDay(getValue(cursor, Constant.ACCEPTANCE.conclusionByDay.toString()));
                                        //----------------------------
                                        String isFinishByDay = getValue(cursor, Constant.ACCEPTANCE.isFinishByDay.toString());
                                        if(StringUtils.isNotEmpty(isFinishByDay) ) {
                                            bean.setFinishByDay(isFinishByDay.equals("1"));
                                        }else {
                                            bean.setFinishByDay(false);
                                        }

                                        bean.setElectricityScanByMonth(getValue(cursor, Constant.ACCEPTANCE.electricityScanByMonth.toString()));
                                        bean.setMonthFreezingTimeScan(getValue(cursor, Constant.ACCEPTANCE.monthFreezingTimeScan.toString()));
                                        bean.setDifferencesByMonth(getValue(cursor, Constant.ACCEPTANCE.differencesByMonth.toString()));
                                        bean.setConclusionByMonth(getValue(cursor, Constant.ACCEPTANCE.conclusionByMonth.toString()));
                                        //----------------------------
                                        String isFinishByMonth = getValue(cursor, Constant.ACCEPTANCE.isFinishByMonth.toString());
                                        if(StringUtils.isNotEmpty(isFinishByMonth) ) {
                                            bean.setFinishByMonth(isFinishByMonth.equals("1"));
                                        }else {
                                            bean.setFinishByMonth(false);
                                        }

                                        //------------------------
                                    }catch (Exception e){
                                        LogUtils.i("readDbToBeanForAcceptance -- e.getMessage()3:" + e.getMessage());  // 因为有些空值
                                    }

                                    Log.i("shen", "nei:" + bean.toString());
                                    beanList.add(bean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "readDbToBeanForAcceptance -- beanList.size():"+beanList.size());

                        return beanList;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    @Override
    public void addConcentrator(Observer observer, final ContentValues values, final String assetNumbers) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(@NonNull String s) throws Exception {

                        long i = 0;
                        i = mTableEx.Update(Constant.TABLE_CONCENTRATOR, values,
                                Constant.CONCENTRATOR.assetNumbers.toString() + "=?",
                                new String[]{assetNumbers});                    // 返回值是  -1  失败


                        if(i <= 0){
                            i = mTableEx.Add(Constant.TABLE_CONCENTRATOR, values);  // 返回值是  -1  失败
                        }

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void getConcentrator(Observer observer, final String meteringSection) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<ConcentratorBean>>() {
                    @Override
                    public List<ConcentratorBean> apply(@NonNull String s) throws Exception {

                        Cursor cursor = mTableEx.Query(Constant.TABLE_CONCENTRATOR,
                                null,
                                Constant.CONCENTRATOR.theMeteringSection + "=?",
                                new String[]{meteringSection},
                                null, null, null);

                        List<ConcentratorBean> beanList = new ArrayList<>();
                        if(cursor != null && cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    ConcentratorBean concentratorBean = new ConcentratorBean();

                                    try {

                                        concentratorBean.setAssetNumbers(
                                                getValue(cursor, Constant.CONCENTRATOR.assetNumbers.toString()));
                                        concentratorBean.setLatitude(
                                                getValue(cursor, Constant.CONCENTRATOR.latitude.toString()));
                                        concentratorBean.setLongitude(
                                                getValue(cursor, Constant.CONCENTRATOR.longitude.toString()));
                                        concentratorBean.setTheMeteringSection(
                                                getValue(cursor, Constant.CONCENTRATOR.theMeteringSection.toString()));
                                        concentratorBean.setAddr(
                                                getValue(cursor, Constant.CONCENTRATOR.addr.toString()));

                                        //----2017/09/04
                                        concentratorBean.setMeterFootNumbers(getValue(cursor, Constant.CONCENTRATOR.meterFootNumbers.toString()));
                                        concentratorBean.setMeterFootPicPath(getValue(cursor, Constant.CONCENTRATOR.meterFootPicPath.toString()));
                                        concentratorBean.setMeterBodyNumbers1(getValue(cursor, Constant.CONCENTRATOR.meterBodyNumbers1.toString()));
                                        concentratorBean.setMeterBodyPicPath1(getValue(cursor, Constant.CONCENTRATOR.meterBodyPicPath1.toString()));
                                        concentratorBean.setMeterBodyNumbers2(getValue(cursor, Constant.CONCENTRATOR.meterBodyNumbers2.toString()));
                                        concentratorBean.setMeterBodyPicPath2(getValue(cursor, Constant.CONCENTRATOR.meterBodyPicPath2.toString()));
                                        concentratorBean.setPicPath(getValue(cursor, Constant.CONCENTRATOR.picPath.toString()));


                                    }catch (Exception e){
                                        LogUtils.i("e.getMessage():" + e.getMessage());
                                    }

                                    //LogUtils.i("nei:" + assetNumberBean.toString());
                                    beanList.add(concentratorBean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "beanList.size():"+beanList.size());

                        return beanList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void addTransformer(Observer observer, final ContentValues values, final String meteringSection) {
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(@NonNull String s) throws Exception {

                        long i = 0;
                        i = mTableEx.Update(Constant.TABLE_TRANSFORMER, values,
                                Constant.TRANSFORMER.theMeteringSection.toString() + "=?",
                                new String[]{meteringSection});                    // 返回值是  -1  失败


                        if(i <= 0){
                            i = mTableEx.Add(Constant.TABLE_TRANSFORMER, values);  // 返回值是  -1  失败
                        }

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void getTransformer(Observer observer, final String meteringSection) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<TransformerBean>>() {
                    @Override
                    public List<TransformerBean> apply(@NonNull String s) throws Exception {

                        Cursor cursor = mTableEx.Query(Constant.TABLE_TRANSFORMER,
                                null,
                                Constant.CONCENTRATOR.theMeteringSection + "=?",
                                new String[]{meteringSection},
                                null, null, null);

                        List<TransformerBean> beanList = new ArrayList<>();
                        if(cursor != null && cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    TransformerBean transformerBean = new TransformerBean();

                                    try {

                                        transformerBean.setLatitude(
                                                getValue(cursor, Constant.TRANSFORMER.latitude.toString()));
                                        transformerBean.setLongitude(
                                                getValue(cursor, Constant.TRANSFORMER.longitude.toString()));
                                        transformerBean.setTheMeteringSection(
                                                getValue(cursor, Constant.TRANSFORMER.theMeteringSection.toString()));
                                        transformerBean.setAddr(
                                                getValue(cursor, Constant.TRANSFORMER.addr.toString()));
                                        transformerBean.setPicPath(getValue(cursor, Constant.TRANSFORMER.picPath.toString()));

                                    }catch (Exception e){
                                        LogUtils.i("e.getMessage():" + e.getMessage());
                                    }

                                    //LogUtils.i("nei:" + assetNumberBean.toString());
                                    beanList.add(transformerBean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "beanList.size():"+beanList.size());

                        return beanList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override
    public void saveAcceptance(Observer observer, final AcceptanceBean acceptanceBean) {

        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(@NonNull String s) throws Exception {

                        ContentValues values = new ContentValues();
//                        values.put(Constant.ACCEPTANCE.userNumber.toString(), acceptanceBean.getUserName());
//                        values.put(Constant.ACCEPTANCE.assetNumbers.toString(), acceptanceBean.getAssetNumbers());
//                        values.put(Constant.ACCEPTANCE.userName.toString(), acceptanceBean.getUserName());
//                        values.put(Constant.ACCEPTANCE.meterAddr.toString(), acceptanceBean.getMeterAddr());
//                        values.put(Constant.ACCEPTANCE.terminalNo.toString(), acceptanceBean.getTerminalNo());

//                        values.put(Constant.ACCEPTANCE.daysFreezingTimeIn.toString(), acceptanceBean.getDaysFreezingTimeIn());
//                        values.put(Constant.ACCEPTANCE.electricityInByDay.toString(), acceptanceBean.getElectricityInByDay());
//                        values.put(Constant.ACCEPTANCE.monthFreezingTimeIn.toString(), acceptanceBean.getMonthFreezingTimeIn());
//                        values.put(Constant.ACCEPTANCE.electricityInByMonth.toString(), acceptanceBean.getElectricityInByMonth());

                        LogUtils.i("saveAcceptance:" + acceptanceBean.toString());

                        if(acceptanceBean.isFinishByDay()) {
                            values.put(Constant.ACCEPTANCE.daysFreezingTimeScan.toString(), acceptanceBean.getDaysFreezingTimeScan());
                            values.put(Constant.ACCEPTANCE.electricityScanByDay.toString(), acceptanceBean.getElectricityScanByDay());
                            values.put(Constant.ACCEPTANCE.differencesByDay.toString(), acceptanceBean.getDifferencesByDay());
                            values.put(Constant.ACCEPTANCE.conclusionByDay.toString(), acceptanceBean.getConclusionByDay());
                            values.put(Constant.ACCEPTANCE.isFinishByDay.toString(), acceptanceBean.isFinishByDay());
                        }else {
                            values.put(Constant.ACCEPTANCE.daysFreezingTimeScan.toString(), "");
                            values.put(Constant.ACCEPTANCE.electricityScanByDay.toString(), "");
                            values.put(Constant.ACCEPTANCE.differencesByDay.toString(), "");
                            values.put(Constant.ACCEPTANCE.conclusionByDay.toString(), "");
                            values.put(Constant.ACCEPTANCE.isFinishByDay.toString(), false);
                        }

                        if(acceptanceBean.isFinishByMonth()) {

                            values.put(Constant.ACCEPTANCE.monthFreezingTimeScan.toString(), acceptanceBean.getMonthFreezingTimeScan());
                            values.put(Constant.ACCEPTANCE.electricityScanByMonth.toString(), acceptanceBean.getElectricityScanByMonth());
                            values.put(Constant.ACCEPTANCE.differencesByMonth.toString(), acceptanceBean.getDifferencesByMonth());
                            values.put(Constant.ACCEPTANCE.conclusionByMonth.toString(), acceptanceBean.getConclusionByMonth());
                            values.put(Constant.ACCEPTANCE.isFinishByMonth.toString(), acceptanceBean.isFinishByMonth());
                        }else {
                            values.put(Constant.ACCEPTANCE.monthFreezingTimeScan.toString(), "");
                            values.put(Constant.ACCEPTANCE.electricityScanByMonth.toString(), "");
                            values.put(Constant.ACCEPTANCE.differencesByMonth.toString(), "");
                            values.put(Constant.ACCEPTANCE.conclusionByMonth.toString(), "");
                            values.put(Constant.ACCEPTANCE.isFinishByMonth.toString(), false);
                        }

                        long i = mTableEx.Update(Constant.TABLE_ACCEPTANCE, values,
                                Constant.ACCEPTANCE.assetNumbers.toString() + "=?",
                                new String[]{acceptanceBean.getAssetNumbers()});

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void generateReportsAcceptance(Observer observer, final Context context,
                                          final List<AcceptanceBean> acceptanceBeanList,
                                          final String excelPathByDay,
                                          final String excelPathByMonth) {
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String s) throws Exception {

                        boolean b = false;
                        boolean b1 = false;

                        b = POIExcelUtil.writeExcelAcceptanceByDay(context, acceptanceBeanList,
                                excelPathByDay);
                        b1 = POIExcelUtil.writeExcelAcceptanceByMonth(context, acceptanceBeanList,
                                excelPathByMonth);

                        return (b && b1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public List<MeterBean1> searchAcceptance(Observer observer, HashMap<String, String> conditionMap) {

        int size = conditionMap.keySet().size();
        if(size <=0 ){
            return null;
        }

        String selection = "";
        final String[] selectionArgs = new String[size];

        int i = 0;
        for(String set : conditionMap.keySet()){
            if(i == size-1){                                            // 最后一个
                selectionArgs[i] = "%" + conditionMap.get(set) + "%";
                selection += set + " like ?";
            }else {
                selectionArgs[i] = "%" + conditionMap.get(set) + "%";
                selection += set + " like ? and ";
            }

            i++;
        }

        LogUtils.i(selection);

        final String finalSelection = selection;
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<AcceptanceBean>>() {
                    @Override
                    public List<AcceptanceBean> apply(@NonNull String s) throws Exception {

                        Cursor cursor;
                        cursor  = mTableEx.Query(Constant.TABLE_ACCEPTANCE,
                                null,
                                finalSelection,
                                selectionArgs,
                                null, null, null);

                        List<AcceptanceBean> beanList = new ArrayList<>();
                        if(cursor != null && cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    AcceptanceBean bean = new AcceptanceBean();

                                    try {


                                        bean.setUserNumber(getValue(cursor, Constant.ACCEPTANCE.userNumber.toString()));
                                        bean.setAssetNumbers(getValue(cursor, Constant.ACCEPTANCE.assetNumbers.toString()));
                                        bean.setUserName(getValue(cursor, Constant.ACCEPTANCE.userName.toString()));
                                        bean.setMeterAddr(getValue(cursor, Constant.ACCEPTANCE.meterAddr.toString()));
                                        bean.setTerminalNo(getValue(cursor, Constant.ACCEPTANCE.terminalNo.toString()));

                                        //---------------------------
                                        bean.setDaysFreezingTimeIn(getValue(cursor, Constant.ACCEPTANCE.daysFreezingTimeIn.toString()));
                                        bean.setElectricityInByDay(getValue(cursor, Constant.ACCEPTANCE.electricityInByDay.toString()));
                                        bean.setMonthFreezingTimeIn(getValue(cursor, Constant.ACCEPTANCE.monthFreezingTimeIn.toString()));
                                        bean.setElectricityInByMonth(getValue(cursor, Constant.ACCEPTANCE.electricityInByMonth.toString()));

                                        bean.setElectricityScanByDay(getValue(cursor, Constant.ACCEPTANCE.electricityScanByDay.toString()));
                                        bean.setDaysFreezingTimeScan(getValue(cursor, Constant.ACCEPTANCE.daysFreezingTimeScan.toString()));
                                        bean.setDifferencesByDay(getValue(cursor, Constant.ACCEPTANCE.differencesByDay.toString()));
                                        bean.setConclusionByDay(getValue(cursor, Constant.ACCEPTANCE.conclusionByDay.toString()));
                                        //----------------------------
                                        String isFinishByDay = getValue(cursor, Constant.ACCEPTANCE.isFinishByDay.toString());
                                        if(StringUtils.isNotEmpty(isFinishByDay) ) {
                                            bean.setFinishByDay(isFinishByDay.equals("1"));
                                        }else {
                                            bean.setFinishByDay(false);
                                        }

                                        bean.setElectricityScanByMonth(getValue(cursor, Constant.ACCEPTANCE.electricityScanByMonth.toString()));
                                        bean.setMonthFreezingTimeScan(getValue(cursor, Constant.ACCEPTANCE.monthFreezingTimeScan.toString()));
                                        bean.setDifferencesByMonth(getValue(cursor, Constant.ACCEPTANCE.differencesByMonth.toString()));
                                        bean.setConclusionByMonth(getValue(cursor, Constant.ACCEPTANCE.conclusionByMonth.toString()));
                                        //----------------------------
                                        String isFinishByMonth = getValue(cursor, Constant.ACCEPTANCE.isFinishByMonth.toString());
                                        if(StringUtils.isNotEmpty(isFinishByMonth) ) {
                                            bean.setFinishByMonth(isFinishByMonth.equals("1"));
                                        }else {
                                            bean.setFinishByMonth(false);
                                        }

                                        //------------------------
                                    }catch (Exception e){
                                        LogUtils.i("readDbToBeanForCollector -- e.getMessage()3:" + e.getMessage());  // 因为有些空值
                                    }

                                    Log.i("shen", "nei:" + bean.toString());
                                    beanList.add(bean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "readDbToBeanForCollector -- beanList.size():"+beanList.size());

                        return beanList;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


        return null;
    }

    @Override
    public void saveScatteredNewMeterActivity(Observer observer, final ContentValues values, final String newAssetNumbers) {
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(@NonNull String s) throws Exception {

                        long i = mTableEx.Update(Constant.TABLE_SCATTEREDNEWMETER, values,
                                Constant.SCATTEREDNEWMETER.newAssetNumbers.toString() + "=?",
                                new String[]{newAssetNumbers});

                        if(i <= 0){
                            i = mTableEx.Add(Constant.TABLE_SCATTEREDNEWMETER, values);  // 返回值是  -1  失败
                        }

                        return i;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void searchScatteredNewMeter(Observer observer, HashMap<String, String> conditionMap) {
        int size = conditionMap.keySet().size();
        if(size <=0 ){
            return ;
        }

        String selection = "";
        final String[] selectionArgs = new String[size];

        int i = 0;
        for(String set : conditionMap.keySet()){
            if(i == size-1){                                            // 最后一个
                selectionArgs[i] = "%" + conditionMap.get(set) + "%";
                selection += set + " like ?";
            }else {
                selectionArgs[i] = "%" + conditionMap.get(set) + "%";
                selection += set + " like ? and ";
            }

            i++;
        }

        LogUtils.i(selection);

        final String finalSelection = selection;
        Observable.just("")
                .observeOn(Schedulers.io())
                .map(new Function<String, List<ScatteredNewMeterBean>>() {
                    @Override
                    public List<ScatteredNewMeterBean> apply(@NonNull String s) throws Exception {

                        Cursor cursor;
                        cursor  = mTableEx.Query(Constant.TABLE_SCATTEREDNEWMETER,
                                null,
                                finalSelection,
                                selectionArgs,
                                null, null, null);

                        List<ScatteredNewMeterBean> beanList = new ArrayList<>();
                        if(cursor != null && cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {
                                do{
                                    ScatteredNewMeterBean bean = new ScatteredNewMeterBean();

                                    try {

                                        bean.setUserNumber(getValue(cursor, Constant.SCATTEREDNEWMETER.userNumber.toString()));
                                        bean.setUserName(getValue(cursor, Constant.SCATTEREDNEWMETER.userName.toString()));
                                        bean.setUserAddr(getValue(cursor, Constant.SCATTEREDNEWMETER.userAddr.toString()));
                                        bean.setUserPhone(getValue(cursor, Constant.SCATTEREDNEWMETER.userPhone.toString()));

                                        bean.setNewAddr(getValue(cursor, Constant.SCATTEREDNEWMETER.newAddr.toString()));
                                        bean.setNewAssetNumbers(getValue(cursor, Constant.SCATTEREDNEWMETER.newAssetNumbers.toString()));
                                        bean.setNewElectricity(getValue(cursor, Constant.SCATTEREDNEWMETER.newElectricity.toString()));

                                        bean.setMeterFootNumbers(getValue(cursor, Constant.SCATTEREDNEWMETER.meterFootNumbers.toString()));
                                        bean.setMeterFootPicPath(getValue(cursor, Constant.SCATTEREDNEWMETER.meterFootPicPath.toString()));
                                        bean.setMeterBodyNumbers1(getValue(cursor, Constant.SCATTEREDNEWMETER.meterBodyNumbers1.toString()));
                                        bean.setMeterBodyPicPath1(getValue(cursor, Constant.SCATTEREDNEWMETER.meterBodyPicPath1.toString()));
                                        bean.setMeterBodyNumbers2(getValue(cursor, Constant.SCATTEREDNEWMETER.meterBodyNumbers2.toString()));
                                        bean.setMeterBodyPicPath2(getValue(cursor, Constant.SCATTEREDNEWMETER.meterBodyPicPath2.toString()));

                                        bean.setPicPath(getValue(cursor, Constant.SCATTEREDNEWMETER.picPath.toString()));

                                        //------------------------
                                    }catch (Exception e){
                                        LogUtils.i("readDbToBeanForCollector -- e.getMessage()3:" + e.getMessage());  // 因为有些空值
                                    }

                                    Log.i("shen", "nei:" + bean.toString());
                                    beanList.add(bean);

                                }while (cursor.moveToNext());
                            }
                        }

                        Log.i("shen", "readDbToBeanForCollector -- beanList.size():"+beanList.size());

                        return beanList;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


        return ;
    }

    @Override
    public void readDbToBeanForScatteredNewMeter(Observer observer) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception { // Observable  (被观察者)
                emitter.onNext("");
                emitter.onComplete();

            }

        }).observeOn(Schedulers.io())
                .map(new Function<String, List<ScatteredNewMeterBean>>() {
                    @Override
                    public List<ScatteredNewMeterBean> apply(@NonNull String s) throws Exception {

                        Cursor cursor;
                        cursor  = mTableEx.Query(Constant.TABLE_SCATTEREDNEWMETER,
                                null,
                                null,
                                null,
                                null, null, null);

                        List<ScatteredNewMeterBean> beanList = new ArrayList<>();
                        if(cursor != null && cursor.getCount() != 0) {
                            if(cursor.moveToFirst()) {

                                do{
                                    ScatteredNewMeterBean bean = new ScatteredNewMeterBean();

                                    try {
                                        bean.setUserNumber(getValue(cursor, Constant.SCATTEREDNEWMETER.userNumber.toString()));
                                        bean.setUserName(getValue(cursor, Constant.SCATTEREDNEWMETER.userName.toString()));

                                        bean.setUserAddr(getValue(cursor, Constant.SCATTEREDNEWMETER.userAddr.toString()));
                                        bean.setUserPhone(getValue(cursor, Constant.SCATTEREDNEWMETER.userPhone.toString()));

                                        bean.setNewAddr(getValue(cursor, Constant.SCATTEREDNEWMETER.newAddr.toString()));
                                        bean.setNewAssetNumbers(getValue(cursor, Constant.SCATTEREDNEWMETER.newAssetNumbers.toString()));
                                        bean.setNewElectricity(getValue(cursor, Constant.SCATTEREDNEWMETER.newElectricity.toString()));

                                        bean.setMeterFootNumbers(getValue(cursor, Constant.SCATTEREDNEWMETER.meterFootNumbers.toString()));
                                        bean.setMeterFootPicPath(getValue(cursor, Constant.SCATTEREDNEWMETER.meterFootPicPath.toString()));
                                        bean.setMeterBodyNumbers1(getValue(cursor, Constant.SCATTEREDNEWMETER.meterBodyNumbers1.toString()));
                                        bean.setMeterBodyPicPath1(getValue(cursor, Constant.SCATTEREDNEWMETER.meterBodyPicPath1.toString()));
                                        bean.setMeterBodyNumbers2(getValue(cursor, Constant.SCATTEREDNEWMETER.meterBodyNumbers2.toString()));
                                        bean.setMeterBodyPicPath2(getValue(cursor, Constant.SCATTEREDNEWMETER.meterBodyPicPath2.toString()));

                                        bean.setPicPath(getValue(cursor, Constant.SCATTEREDNEWMETER.picPath.toString()));
                                        //------------------------
                                    }catch (Exception e){
                                        LogUtils.i("readDbToBeanForScatteredNewMeter -- e.getMessage():" + e.getMessage());  // 因为有些空值
                                    }

                                    Log.i("shen", "nei:" + bean.toString());
                                    beanList.add(bean);

                                }while (cursor.moveToNext());

                                cursor.close();             // 关闭游标！
                            }
                        }

                        Log.i("shen", "readDbToBeanForScatteredNewMeter -- beanList.size():"+beanList.size());

                        return beanList;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    @Override
    public void generateReportsScatteredNewMeter(Observer observer, final Context context,
                                          final List<ScatteredNewMeterBean> scatteredNewMeterBeanList,
                                          final String excelPath) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception { // Observable  (被观察者)
                emitter.onNext("");
                emitter.onComplete();

            }

        }).observeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String s) throws Exception {

                        boolean b = false;

                        b = POIExcelUtil.writeExcelScatteredNewMeter(context, scatteredNewMeterBeanList,
                                excelPath);


                        return b;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }



    /**
     * 从游标中获取值
     *
     * @param cursor    游标
     * @param field     字段
     */
    private String getValue(Cursor cursor, String field){

        if(cursor.getString(cursor.getColumnIndex(field)) == null)
            return "";
        else
            return cursor.getString(cursor.getColumnIndex(field));
    }
}
