package com.zh.metermanagementcw.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;


import com.zh.metermanagementcw.activity.base.BaseActivity;
import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.AreaBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.NoWorkOrderPathBean;
import com.zh.metermanagementcw.bean.TransformerBean;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

	private final String Tag = "MyApplication";


	private static Context context;
	/** Activity集合 */
	public static ArrayList<BaseActivity> listActivity;

	private static MyApplication instance;



	/** 无工单 -- 用户/表主的信息 */
	private static List<MeterBean> mMeterBeanList;
	/** 无工单 -- 用户/表主的信息 */
	private static List<MeterBean1> mMeterBean1List;
	/** 无工单 -- 无匹配的资产编码 */
	private static List<AssetNumberBean> mAssetNumberBeanList;
	/** 无工单 -- 集中器的资产编码 */
	private static List<ConcentratorBean> mConcentratorBeanList;
	/** 无工单 -- 变压器 */
	private static List<TransformerBean> mTransformerBeanList;


	/** 区域 */
	private static AreaBean mAreaBean;


	/** 当前选中的"抄表区段" */
	private static String currentMeteringSection;

	/** 当前选中的"抄表区段"对应的文件夹(多个) */
	private static NoWorkOrderPathBean noWorkOrderPath;



	//--------------------------acceptance------------------------------
	/** acceptance -- 日/月冻结 */
	private static List<AcceptanceBean> mAcceptanceBeanList;



	/**
	 * 获取 MyApplication
	 * @return
	 */
	public static MyApplication getInstance() {
		if(instance == null){
			return new MyApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		listActivity = new ArrayList<BaseActivity>();

		context = this;

		mMeterBeanList = new ArrayList<MeterBean>();
		mMeterBean1List =  new ArrayList<MeterBean1>();
		mAssetNumberBeanList = new ArrayList<AssetNumberBean>();

		mAreaBean = new AreaBean();

		noWorkOrderPath = new NoWorkOrderPathBean();

	}


	public static Context getContext() {
		return context;
	}


	/** 用户/表主的信息 */
	public static List<MeterBean> getMeterBeanList() {
		return mMeterBeanList;
	}

	/** 用户/表主的信息 */
	public static void setMeterBeanList(List<MeterBean> meterBeanList) {
		mMeterBeanList = meterBeanList;
	}

	/** 用户/表主的信息1 */
	public static List<MeterBean1> getMeterBean1List() {
		return mMeterBean1List;
	}
	/** 用户/表主的信息1 */
	public static void setMeterBean1List(List<MeterBean1> mMeterBean1List) {
		MyApplication.mMeterBean1List = mMeterBean1List;
	}

	/** 无匹配的资产编码 */
	public static List<AssetNumberBean> getAssetNumberBeanList() {
		return mAssetNumberBeanList;
	}

	/** 无匹配的资产编码 */
	public static void setAssetNumberBeanList(List<AssetNumberBean> mAssetNumberBeanList) {
		MyApplication.mAssetNumberBeanList = mAssetNumberBeanList;
	}

	/** 无工单 -- 集中器的资产编码 */
	public static List<ConcentratorBean> getConcentratorBeanList() {
		return mConcentratorBeanList;
	}
	/** 无工单 -- 集中器的资产编码 */
	public static void setConcentratorBeanList(List<ConcentratorBean> concentratorBeanList) {
		mConcentratorBeanList = concentratorBeanList;
	}

	/** 无工单 -- 变压器 */
	public static List<TransformerBean> getTransformerBeanList() {
		return mTransformerBeanList;
	}
	/** 无工单 -- 变压器 */
	public static void setTransformerBeanList(List<TransformerBean> transformerBeanList) {
		MyApplication.mTransformerBeanList = transformerBeanList;
	}


	/** 当前选中的"抄表区段" */
	public static String getCurrentMeteringSection() {
		return currentMeteringSection;
	}
	/** 当前选中的"抄表区段" */
	public static void setCurrentMeteringSection(String currentMeteringSection) {
		MyApplication.currentMeteringSection = currentMeteringSection;
	}

	/** 区域 */
	public static AreaBean getAreaBean() {
		return mAreaBean;
	}
	/** 区域 */
	public static void setAreaBean(AreaBean mAreaBean) {
		MyApplication.mAreaBean = mAreaBean;
	}

	/** 当前选中的"抄表区段"对应的文件夹(多个) */
	public static NoWorkOrderPathBean getNoWorkOrderPath() {
		return noWorkOrderPath;
	}
	/** 当前选中的"抄表区段"对应的文件夹(多个) */
	public static void setNoWorkOrderPath(NoWorkOrderPathBean noWorkOrderPath) {
		MyApplication.noWorkOrderPath = noWorkOrderPath;
	}

	/** acceptance -- 日/月冻结 */
	public static List<AcceptanceBean> getAcceptanceBeanList() {
		return mAcceptanceBeanList;
	}
	/** acceptance -- 日/月冻结 */
	public static void setAcceptanceBeanList(List<AcceptanceBean> acceptanceBeanList) {
		mAcceptanceBeanList = acceptanceBeanList;
	}



	/**
	 * 退出APP
	 */
	public static void exitApp() {
		for (BaseActivity activity : listActivity) {
			if (activity != null) {
				activity.finish();
			}
		}
		Log.e("MyApplication", "exitApp");
		// 杀进程
		android.os.Process.killProcess(android.os.Process.myPid());
		// 终止虚拟机
		System.exit(0);
	}


//	@Override
//	public void onTerminate() {
//		// 程序终止的时候执行
//		Log.i("shen", "onTerminate");
//		super.onTerminate();
//	}
//	@Override
//	public void onLowMemory() {
//		// 低内存的时候执行
//		Log.i("shen", "onLowMemory");
//		super.onLowMemory();
//	}
//	@Override
//	public void onTrimMemory(int level) {
//		// 程序在内存清理的时候执行
//		Log.i("shen", "onTrimMemory");
//		super.onTrimMemory(level);
//	}

}
