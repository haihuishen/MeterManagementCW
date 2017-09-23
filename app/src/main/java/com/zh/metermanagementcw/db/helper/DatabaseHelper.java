package com.zh.metermanagementcw.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

/**
 * 数据库管理
 *
 * */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 *
	 * @param context 	     上下文对象
	 * @param dbName 		数据库名称
	 * @param factory       游标结果集工厂，如果需要使用则需要自定义结果集工厂，null值代表使用默认结果集工厂
	 * @param version 	     数据库版本号，必须大于等于1
	 */
	private DatabaseHelper(Context context, String dbName, CursorFactory factory, int version) {
		super(context, dbName, factory, version);
	}

	/**
	 *  新建数据库
	 *
	 * @param context 	     上下文对象
	 * @param dbName		数据库名称
	 * @param version	     数据库版本号，必须大于等于1
	 */
	public DatabaseHelper(Context context, String dbName, int version) {
		this(context, dbName, null, version); // this -->"私有的构造函数"

	}

	/**
	 * 数据库第一次被调用时调用该方法，
	 *
	 * 	private DatabaseHelper dbHelper = null;
	 *  private SQLiteDatabase db = null;
	 * 		dbHelper = new DatabaseHelper(mContext, Constant.DB_NAME, dbVersion);
	 *      db = dbHelper.getWritableDatabase();        -----------> 这句调用时，此方法被调用
	 *
	 * 这里面主要进行对数据库的初始化操作
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		System.out.println("DBHelper onCreate");

		/** 资产编号无匹配的(资产编号) */
		db.execSQL("create table if not exists assetnumber(" +
				"_id integer primary key autoincrement," +
				"assetNumbers varchar(50) not null unique )");									// 是否完成(扫描完)



		/** 采集器(资产编号) */
		db.execSQL("create table if not exists collectornumber(" +
				"_id integer primary key autoincrement," +
				"collectorNumbers varchar(50) not null unique," +			// 采集器资产编码
				"theMeteringSection varchar(50)," +							// 抄表区段
				"collectorPicPath varchar(1000)," +							// 采集器图片

				"collectorFootNumbers varchar(500)," +						// (采集器)表脚封扣（条码）		 -- 2017/09/04
				"collectorFootPicPath varchar(500)," +						// (采集器)拍照图片的路径(电表表脚封扣)	 -- 2017/09/04

				"collectorBodyNumbers1 varchar(500)," +						// (采集器)表箱封扣1（条码）			 -- 2017/09/04
				"collectorBodyPicPath1 varchar(500)," +						// (采集器)拍照图片的路径(表箱封扣1)	 -- 2017/09/04

				"collectorBodyNumbers2 varchar(500)," +						// (采集器)表箱封扣2（条码）			 -- 2017/09/04
				"collectorBodyPicPath2 varchar(500)" +						// (采集器)拍照图片的路径(表箱封扣2)	 -- 2017/09/04


				")");


		/** 用户数据(表) */
		db.execSQL("create table if not exists meterinfo(" +
				"_id integer primary key autoincrement," +
				"sequenceNumber varchar(50) not null," +					// 序号
				"userNumber varchar(50) not null," +						// 用户编号
				"userName varchar(50) not null," +							// 用户名称
				"userAddr varchar(50) not null," +							// 用户地址
				"area varchar(50) not null," +								// 区域(哪个区域的表)
				"oldAssetNumbers varchar(50) not null," +					// 旧表资产编号(导入的)
				"oldAddr varchar(50)," +									// 旧表表地址(需扫描)
				"oldAddrAndAsset tinyint(1)," +								// 旧表表地址 和 资产编码 比较
				"oldAssetNumbersScan varchar(50)," +						// 旧表资产编号(需扫描)
				"oldElectricity varchar(50)," +								// 旧电能表止码-电量(需扫描)
				"newAddr varchar(50)," +									// 新表表地址(需扫描)
				"newAddrAndAsset tinyint(1)," +								// 新表表地址 和 资产编码 比较
				"newAssetNumbersScan varchar(50)," +						// 新表资产编号(需扫描)
				"newElectricity varchar(50)," +								// 新电能表止码-电量(需扫描)
				"collectorAssetNumbersScan varchar(50)," +					// 采集器资产编号(需扫描)
				"time date," +												// 完成换抄时间
				"picPath varchar(500)," +									// 拍照图片的路径
				"isFinish tinyint(1))");									// 是否完成(扫描完)

		/** 用户数据1(表) */
		db.execSQL("create table if not exists meterinfo1(" +
				"_id integer primary key autoincrement," +
				"userNumber varchar(50)," +									// 用户编号
				"userName varchar(50)," +									// 用户名称
				"userAddr varchar(50)," +									// 用户地址
				"userPhone varchar(50)," +									// 用户电话
				"measurementPointNumber varchar(50)," +						// 计量点编号
				"powerSupplyBureau varchar(50)," +							// 供电局(供电单位)
				"theMeteringSection varchar(50)," +							// 抄表区段
				"courts varchar(50)," +										// 台区
				"measuringPointAddress varchar(50)," +						// 计量点地址
				"oldAssetNumbers varchar(50) not null," +					// 旧表资产编号(导入的)
				"oldAddr varchar(50)," +									// 旧表表地址(需扫描)
				"oldAddrAndAsset tinyint(1)," +								// 旧表表地址 和 资产编码 比较
				"oldElectricity varchar(50)," +								// 旧电能表止码-电量(需扫描)
				"newAddr varchar(50)," +									// 新表表地址(需扫描)
				"newAddrAndAsset tinyint(1)," +								// 新表表地址 和 资产编码 比较
				"newAssetNumbersScan varchar(50)," +						// 新表资产编号(需扫描)
				"newElectricity varchar(50)," +								// 新电能表止码-电量(需扫描)
				"collectorAssetNumbersScan varchar(50)," +					// 采集器资产编号(需扫描)
				"time date," +												// 完成换抄时间
				"picPath varchar(1000)," +									// 拍照图片的路径(换表)
				"meterPicPath varchar(500)," +								// 拍照图片的路径 (新装采集器对应的电表)
				"meterContentPicPath varchar(500)," +						// 拍照图片的路径	(新装采集器)
				"relaceOrAnd tinyint(1)," +									// 0:"换表"； 1："新装采集器"  -- 要先判断是否抄完

				"meterFootNumbers varchar(500)," +							// （新电表）表脚封扣（条码）		 -- 2017/09/04
				"meterFootPicPath varchar(500)," +							// （新电表）拍照图片的路径(电表表脚封扣)	 -- 2017/09/04

				"meterBodyNumbers1 varchar(500)," +							// （新电表）表箱封扣1（条码）			 -- 2017/09/04
				"meterBodyPicPath1 varchar(500)," +							// （新电表）拍照图片的路径(表箱封扣1)	 -- 2017/09/04

				"meterBodyNumbers2 varchar(500)," +							// （新电表）表箱封扣2（条码）			 -- 2017/09/04
				"meterBodyPicPath2 varchar(500)," +							// （新电表）拍照图片的路径(表箱封扣2)	 -- 2017/09/04


				"collectorFootNumbers varchar(500)," +						// (采集器)表脚封扣（条码）		 -- 2017/09/04
				"collectorFootPicPath varchar(500)," +						// (采集器)拍照图片的路径(电表表脚封扣)	 -- 2017/09/04

				"collectorBodyNumbers1 varchar(500)," +						// (采集器)表箱封扣1（条码）			 -- 2017/09/04
				"collectorBodyPicPath1 varchar(500)," +						// (采集器)拍照图片的路径(表箱封扣1)	 -- 2017/09/04

				"collectorBodyNumbers2 varchar(500)," +						// (采集器)表箱封扣2（条码）			 -- 2017/09/04
				"collectorBodyPicPath2 varchar(500)," +						// (采集器)拍照图片的路径(表箱封扣2)	 -- 2017/09/04

				"isFinish tinyint(1))");									// 是否完成(扫描完)


		/** 日/月冻结数据 -- 数据复核 */
		db.execSQL("create table if not exists acceptance(" +
				"_id integer primary key autoincrement," +
				"userNumber varchar(50)," +									// 用户编号
				"assetNumbers varchar(50) not null," +						// 资产编号(导入的)
				"userName varchar(50)," +									// 测量点名称 (用户名)
				"meterAddr varchar(50)," +									// 电表表地址
				"terminalNo varchar(50)," +									// 终端内编号

				"daysFreezingTimeIn varchar(50)," +							// 数据时标(导入) -- 日冻结时间
				"electricityInByDay varchar(50)," +							// 日冻结读数(导入)
				"electricityScanByDay varchar(50)," +						// 日冻结读数(扫描)
				"daysFreezingTimeScan varchar(50)," +						// 数据时标(复核时间) -- 日冻结时间
				"differencesByDay varchar(50)," +							// 差异：electricityScanByDay - electricityInByDay
				"conclusionByDay varchar(50)," +							// 日冻结差异总结
				"isFinishByDay tinyint(1)," +								// 日冻结是否完成

				"monthFreezingTimeIn varchar(50)," +						// 数据时标(导入) -- 月冻结时间
				"electricityInByMonth varchar(50)," +						// 月冻结读数(导入)
				"electricityScanByMonth varchar(50)," +						// 月冻结读数(扫描)
				"monthFreezingTimeScan varchar(50)," +						// 数据时标(复核时间) -- 月冻结时间
				"differencesByMonth varchar(50)," +							// 差异：electricityScanByDay - electricityInByDay
				"conclusionByMonth varchar(50)," +							// 月冻结差异总结
				"isFinishByMonth tinyint(1))");								// 月冻结是否完成


		/** 集中器 */
		db.execSQL("create table if not exists concentrator(" +
				"_id integer primary key autoincrement," +
				"assetNumbers varchar(50) not null unique," +				// 资产编号(集中器)
				"latitude varchar(50)," +									// 纬度
				"longitude varchar(50)," +									// 经度
				"theMeteringSection varchar(50)," +							// 抄表区段
				"addr varchar(50)," +										// 地址

				"meterFootNumbers varchar(500)," +							// 表脚封扣（条码）		 -- 2017/09/04
				"meterFootPicPath varchar(500)," +							// 拍照图片的路径(电表表脚封扣)	 -- 2017/09/04

				"meterBodyNumbers1 varchar(500)," +							// 表箱封扣1（条码）			 -- 2017/09/04
				"meterBodyPicPath1 varchar(500)," +							// 拍照图片的路径(表箱封扣1)	 -- 2017/09/04

				"meterBodyNumbers2 varchar(500)," +							// 表箱封扣2（条码）			 -- 2017/09/04
				"meterBodyPicPath2 varchar(500)," +							// 拍照图片的路径(表箱封扣2)	 -- 2017/09/04

				"picPath varchar(1000)" +									// 拍照图片的路径(集中器)		 -- 2017/09/04

				")");

		/** 变压器 */
		db.execSQL("create table if not exists transformer(" +
				"_id integer primary key autoincrement," +
				"latitude varchar(50)," +									// 纬度
				"longitude varchar(50)," +									// 经度
				"theMeteringSection varchar(50)," +							// 抄表区段
				"addr varchar(50)," +										// 地址

				"picPath varchar(1000)" +									// 拍照图片的路径(变压器)		 -- 2017/09/04
				")");

//
//		/** 零散新装  -- 2017/09/12 */
//		db.execSQL("create table if not exists scatterednewmeter(" +
//				"_id integer primary key autoincrement," +
//
//				"userNumber varchar(50) not null," +						// 用户编号
//				"userName varchar(50)," +									// 用户名称
//				"userAddr varchar(50)," +									// 用户地址
//				"userPhone varchar(50)," +									// 用户电话
//
//				"newAddr varchar(50)," +									// 新表表地址(需扫描)
//				"newAssetNumbers varchar(50)," +							// 新表资产编号(需扫描)
//				"newElectricity varchar(50)," +								// 新电能表止码-电量(需扫描)
//
//				"meterFootNumbers varchar(500)," +							// 表脚封扣（条码）
//				"meterFootPicPath varchar(500)," +							// 拍照图片的路径(电表表脚封扣)
//
//				"meterBodyNumbers1 varchar(500)," +							// 表箱封扣1（条码）
//				"meterBodyPicPath1 varchar(500)," +							// 拍照图片的路径(表箱封扣1)
//
//				"meterBodyNumbers2 varchar(500)," +							// 表箱封扣2（条码）
//				"meterBodyPicPath2 varchar(500)," +							// 拍照图片的路径(表箱封扣2)
//
//				"picPath varchar(1000)," +									// 拍照图片的路径(新装电表)
//
// 				"time date" +												// 完成时间
//				")");
//
//		/** 零散换装  -- 2017/09/22 */
//		db.execSQL("create table if not exists scatteredreplacemeter(" +
//				"_id integer primary key autoincrement," +
//
//				"userNumber varchar(50) not null," +						// 用户编号
//				"userName varchar(50)," +									// 用户名称
//				"userAddr varchar(50)," +									// 用户地址
//				"userPhone varchar(50)," +									// 用户电话
//
//				"oldAddr varchar(50)," +									// 旧表表地址(需扫描)
//				"oldAssetNumbers varchar(50)," +							// 旧表资产编号(需扫描)
//				"oldElectricity varchar(50)," +								// 旧电能表止码-电量(需扫描)
//
//				"newAddr varchar(50)," +									// 新表表地址(需扫描)
//				"newAssetNumbers varchar(50)," +							// 新表资产编号(需扫描)
//				"newElectricity varchar(50)," +								// 新电能表止码-电量(需扫描)
//
//				"meterFootNumbers varchar(500)," +							// 表脚封扣（条码）
//				"meterFootPicPath varchar(500)," +							// 拍照图片的路径(电表表脚封扣)
//
//				"meterBodyNumbers1 varchar(500)," +							// 表箱封扣1（条码）
//				"meterBodyPicPath1 varchar(500)," +							// 拍照图片的路径(表箱封扣1)
//
//				"meterBodyNumbers2 varchar(500)," +							// 表箱封扣2（条码）
//				"meterBodyPicPath2 varchar(500)," +							// 拍照图片的路径(表箱封扣2)
//
//				"picPath varchar(1000)," +									// 拍照图片的路径(新装电表)
//
//				"time date" +												// 完成时间
//				")");

	}



	/**
	 * 数据库更新的时候调用该方法
	 * @param db 				当前操作的数据库对象
	 * @param oldVersion 		老版本号
	 * @param newVersion 		新版本号
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("DBHelper onUpgrade");

		String sql1,sql2,sql3,sql4,sql5,sql6;

		try {
			// // 备份数据库到SD卡的/aDBTest/DBTest.db
			// CopyDBToSDCard.CopyDB(mContext);
			for (int i = oldVersion; i < newVersion; i++) {
				switch (i) {
					case 1:
//					sql1 = " ALTER TABLE sbinfo DROP COLUMN TableNumber";
//
//					sql2 = " ALTER TABLE sbinfo ADD COLUMN OldMeterNumber varchar(200) not null";
//
//					db.execSQL(sql1);
//					db.execSQL(sql2);
						//LogUtils.sysout("==========升级数据库", "");
						break;

//					case 5:								// 2017/09/12
//
//						sql1 = "create table if not exists scatterednewmeter(" +
//								"_id integer primary key autoincrement," +
//								"userNumber varchar(50) not null," +						// 用户编号
//								"userName varchar(50)," +									// 用户名称
//								"userAddr varchar(50)," +									// 用户地址
//								"userPhone varchar(50)," +									// 用户电话
//								"newAddr varchar(50)," +									// 新表表地址(需扫描)
//								"newAssetNumbers varchar(50)," +							// 新表资产编号(需扫描)
//								"newElectricity varchar(50)," +								// 新电能表止码-电量(需扫描)
//								"meterFootNumbers varchar(500)," +							// 表脚封扣（条码）
//								"meterFootPicPath varchar(500)," +							// 拍照图片的路径(电表表脚封扣)
//								"meterBodyNumbers1 varchar(500)," +							// 表箱封扣1（条码）
//								"meterBodyPicPath1 varchar(500)," +							// 拍照图片的路径(表箱封扣1)
//								"meterBodyNumbers2 varchar(500)," +							// 表箱封扣2（条码）
//								"meterBodyPicPath2 varchar(500)," +							// 拍照图片的路径(表箱封扣2)
//								"picPath varchar(1000)," +									// 拍照图片的路径(新装电表)
// 								"time date" +												// 完成时间
//								")";
//
//						sql2 = "create table if not exists scatteredreplacemeter(" +
//								"_id integer primary key autoincrement," +
//								"userNumber varchar(50) not null," +						// 用户编号
//								"userName varchar(50)," +									// 用户名称
//								"userAddr varchar(50)," +									// 用户地址
//								"userPhone varchar(50)," +									// 用户电话
//								"oldAddr varchar(50)," +									// 旧表表地址(需扫描)
//								"oldAssetNumbers varchar(50)," +							// 旧表资产编号(需扫描)
//								"oldElectricity varchar(50)," +								// 旧电能表止码-电量(需扫描)
//								"newAddr varchar(50)," +									// 新表表地址(需扫描)
//								"newAssetNumbers varchar(50)," +							// 新表资产编号(需扫描)
//								"newElectricity varchar(50)," +								// 新电能表止码-电量(需扫描)
//								"meterFootNumbers varchar(500)," +							// 表脚封扣（条码）
//								"meterFootPicPath varchar(500)," +							// 拍照图片的路径(电表表脚封扣)
//								"meterBodyNumbers1 varchar(500)," +							// 表箱封扣1（条码）
//								"meterBodyPicPath1 varchar(500)," +							// 拍照图片的路径(表箱封扣1)
//								"meterBodyNumbers2 varchar(500)," +							// 表箱封扣2（条码）
//								"meterBodyPicPath2 varchar(500)," +							// 拍照图片的路径(表箱封扣2)
//								"picPath varchar(1000)," +									// 拍照图片的路径(新装电表)
//								"time date" +												// 完成时间
//								")";
//
//						db.execSQL(sql1);
//						db.execSQL(sql2);
//
//
//
//						break;

					default:
						break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
