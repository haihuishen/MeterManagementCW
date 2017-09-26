package com.zh.metermanagementcw.utils;

import android.content.Context;

import com.zh.metermanagementcw.bean.AcceptanceBean;
import com.zh.metermanagementcw.bean.AcceptanceByDayBean;
import com.zh.metermanagementcw.bean.AcceptanceByMonthBean;
import com.zh.metermanagementcw.bean.AssetNumberBean;
import com.zh.metermanagementcw.bean.ConcentratorBean;
import com.zh.metermanagementcw.bean.MeterBean1;
import com.zh.metermanagementcw.bean.MeterPhoneBean;
import com.zh.metermanagementcw.bean.ScatteredNewMeterBean;
import com.zh.metermanagementcw.bean.TransformerBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class POIExcelUtil {

	//Logger logger = Logger.getLogger(POIExcelUtil.class);

	/** 总行数 */
	public static int totalRows = 0;
	/** 总列数 */
	public static int totalCells = 0;



	private static Workbook getReadWorkbook(String fileName){
		Workbook workbook = null;
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());		// 后缀名

		File file = new File(fileName);							// 检查文件是否存在
		if (file == null || !file.exists()) {
			return workbook;
		}

		InputStream inputStream = null;							// 读取excel
		try {
			inputStream = new FileInputStream(file);
			// 根据版本选择创建Workbook的方式
			if (fileType.equalsIgnoreCase("xls")) {						// 	xls -- HSSFWorkbook
				workbook = new HSSFWorkbook(inputStream);
			}
			//else if (fileType.equalsIgnoreCase("xlsx")) {				// 	xlsx -- XSSFWorkbook
			//	workbook = new XSSFWorkbook(inputStream);
			//}

		} catch (Exception e) {
			LogUtils.i("打开" + file.getAbsolutePath() +"失败");

		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				//logger.error("关闭流异常:" + e, e);
			}
		}

		return workbook;
	}


	/**
	 * 读取--无工单--"电表信息excel"
	 *
	 * @param fileName 要读取的excel
	 * @return
	 */
	public static List<MeterBean1> readMeterInfoExcel(String fileName) {

		Workbook workbook = getReadWorkbook(fileName);
		List<MeterBean1> beanList = new ArrayList<MeterBean1>();

		if(workbook == null){
			return beanList;
		}

		try {

			Sheet sheet = workbook.getSheetAt(0);
			totalRows = sheet.getPhysicalNumberOfRows();					// 获取行数
			if (totalRows >= 1 && sheet.getRow(0) != null) {
				totalCells = sheet.getRow(0).getPhysicalNumberOfCells();	// 获取列数
			}

			for (int i = 1; i < totalRows; i++) {							// 循环Excel的行
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				MeterBean1 bean = new MeterBean1();
				bean.setUserNumber(row.getCell(0).getStringCellValue().trim());                   //用户编号
				bean.setUserName(row.getCell(1).getStringCellValue().trim());                     //用户名称
				bean.setUserAddr(row.getCell(2).getStringCellValue().trim());                     //用户地址
				bean.setOldAssetNumbers(row.getCell(4).getStringCellValue().trim());              //旧表资产编号(导入的)
				bean.setMeasurementPointNumber(row.getCell(5).getStringCellValue().trim());       //计量点编号
				bean.setPowerSupplyBureau(row.getCell(8).getStringCellValue().trim());            //供电局(供电单位)
				bean.setTheMeteringSection(row.getCell(13).getStringCellValue().trim());          //抄表区段
				bean.setCourts(row.getCell(14).getStringCellValue().trim());                       //台区
				bean.setMeasuringPointAddress(row.getCell(25).getStringCellValue().trim());    	//计量点地址
				bean.setFinish(false);          											// 是否完成(扫描完)

				beanList.add(bean);
			}

		} catch (Exception e) {
			LogUtils.i("readMeterInfoExcel -- e:" + e.getMessage());
		}

		return beanList;
	}

	/**
	 * 读取--无工单--"电表信息--电话 excel"
	 * @param fileName 要读取的excel
	 * @return
	 */
	public static List<MeterPhoneBean> readMeterPhoneExcel(String fileName) {

		Workbook workbook = getReadWorkbook(fileName);
		List<MeterPhoneBean> beanList = new ArrayList<MeterPhoneBean>();

		if(workbook == null){
			return beanList;
		}

		try {

			Sheet sheet = workbook.getSheetAt(0);
			totalRows = sheet.getPhysicalNumberOfRows();					// 获取行数
			if (totalRows >= 1 && sheet.getRow(0) != null) {
				totalCells = sheet.getRow(0).getPhysicalNumberOfCells();	// 获取列数
			}

			for (int i = 0; i < totalRows; i++) {							// 循环Excel的行
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}


				MeterPhoneBean bean = new MeterPhoneBean();

				bean.setUserNumber(getCellValue(row, 0));                   //用户编号
				bean.setUserPhone(getCellValue(row, 10));                     //用户电话
				bean.setTheMeteringSection(getCellValue(row, 11));            //抄表区段

				beanList.add(bean);
			}

		} catch (Exception e) {
			LogUtils.i("readMeterPhoneExcel -- e:" + e.getMessage());
		}


		return beanList;
	}



	/**
	 * 读取--数据复核--日冻结 excel"
	 * @param fileName 要读取的excel
	 * @return
	 */
	public static List<AcceptanceByDayBean> readDaysFreezingExcel(String fileName) {

		Workbook workbook = getReadWorkbook(fileName);
		List<AcceptanceByDayBean> beanList = new ArrayList<>();

		if(workbook == null){
			return beanList;
		}

		try {

			Sheet sheet = workbook.getSheetAt(0);
			totalRows = sheet.getPhysicalNumberOfRows();					// 获取行数
			if (totalRows >= 1 && sheet.getRow(0) != null) {
				totalCells = sheet.getRow(0).getPhysicalNumberOfCells();	// 获取列数
			}

			for (int i = 2; i < totalRows; i++) {							// 循环Excel的行
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}

				AcceptanceByDayBean bean = new AcceptanceByDayBean();

				bean.setDaysFreezingTimeIn(getCellValue(row, 0));            // 数据时标
				bean.setUserName(getCellValue(row, 1));         			 // 测量点名称
				bean.setMeterAddr(getCellValue(row, 2));         			 // 电表地址
				bean.setTerminalNo(getCellValue(row, 3));         			 // 终端内编号

				bean.setElectricityInByDay(getCellValue(row, 4));         	 // 正有功总(kWh)
				bean.setAssetNumbers(getCellValue(row, 12));  				 // 电表资产编号
				bean.setUserNumber(getCellValue(row, 14));         	 	 	 // 用户编号14

				//getMergedRegionValue(sheet, i+1, 12);
				beanList.add(bean);

				//LogUtils.i("readDaysFreezingExcel:" + bean.toString());
			}


		} catch (Exception e) {
			LogUtils.i("readDaysFreezingExcel -- e:" + e.getMessage());
		}

		return beanList;
	}


	/**
	 * 读取--数据复核--日冻结 excel"
	 * @param fileName 要读取的excel
	 * @return
	 */
	public static List<AcceptanceByMonthBean> readMonthFreezingExcel(String fileName) {

		Workbook workbook = getReadWorkbook(fileName);
		List<AcceptanceByMonthBean> beanList = new ArrayList<>();

		if(workbook == null){
			return beanList;
		}

		try {

			Sheet sheet = workbook.getSheetAt(0);
			totalRows = sheet.getPhysicalNumberOfRows();					// 获取行数
			if (totalRows >= 1 && sheet.getRow(0) != null) {
				totalCells = sheet.getRow(0).getPhysicalNumberOfCells();	// 获取列数
			}

			for (int i = 2; i < totalRows; i++) {							// 循环Excel的行
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}


				AcceptanceByMonthBean bean = new AcceptanceByMonthBean();

				bean.setMonthFreezingTimeIn(getCellValue(row, 0));           // 数据时标
				bean.setUserName(getCellValue(row, 1));         			 // 测量点名称
				bean.setMeterAddr(getCellValue(row, 2));         			 // 电表地址
				bean.setTerminalNo(getCellValue(row, 3));         			 // 终端内编号

				bean.setElectricityInByMonth(getCellValue(row, 4));          // 正有功总(kWh)
				bean.setAssetNumbers(getCellValue(row, 12));  				// 电表资产编号
				bean.setUserNumber(getCellValue(row, 14));         	 	 	 // 用户编号14

				beanList.add(bean);
				//LogUtils.i("readMonthFreezingExcel:" + bean.toString());
			}

		} catch (Exception e) {
			LogUtils.i("readMonthFreezingExcel -- e:" + e.getMessage());
		}

		return beanList;
	}

	/**
	 * 获取合并单元格的值
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getMergedRegionValue(Sheet sheet, int row, int column){
		int sheetMergeCount = sheet.getNumMergedRegions();

		for(int i = 0 ; i < sheetMergeCount ; i++){
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if(row >= firstRow && row <= lastRow){
				if(column >= firstColumn && column <= lastColumn){
					Row fRow = sheet.getRow(row);
					return getCellValue(fRow, column) ;
				}
			}
		}

		return null ;
	}


	/**
	 * 获取单元格的值
	 *
	 * @param row			哪一行的
	 * @param cellIndex		行中的哪一列
	 * @return
	 */
	private static String getCellValue(Row row, int cellIndex){

		Cell cell = row.getCell(cellIndex);										// 获取单元格的值
		String cellValue = "";

		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {					// 处理数字型的,自动去零
			if (HSSFDateUtil.isCellDateFormatted(cell)) {					// 在excel里,日期也是数字,在此要进行判断
				cellValue = TimeUtils.dateConvertString(cell.getDateCellValue(), TimeUtils.yyyy_MM_ddHHmmss);
			} else {
				//cellValue = StringUtil.getRightStr(cell.getNumericCellValue() + ""); //怎么去0
				cellValue = cell.getNumericCellValue() + "";
			}

		} else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {			// 处理字符串型
			cellValue = cell.getStringCellValue();

		} else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {			// 处理布尔型
			cellValue = cell.getBooleanCellValue() + "";

		} else {// 其它数据类型
			cellValue = cell.toString() + "";
		}

		return cellValue;
	}


	/**
	 * 03-XX供电所XX台区（抄表区段）户表改造台账.xls
	 *
	 * @param context
	 * @param meterBeanList
	 * @param assetNumberBeenList
	 * @param excelPath
	 * @return
	 */
	public static boolean writeExcel1(Context context,
									  List<MeterBean1> meterBeanList,
									  List<AssetNumberBean> assetNumberBeenList,
									  List<ConcentratorBean> concentratorBeanList,
									  List<TransformerBean> transformerBeanList,
									  String excelPath){

		String sheetName = "户表改造台账";
		String[] headers = new String[]{
				"序号", "供电所", "台区名称", "用户编号", "用户名称", "用户地址", "计量点编号", "计量点地址",
				"旧电能表资产编号", "旧电能表资产编号", "旧电能表止码", "旧电能表表地址",
				"新电能表资产编号", "新电能表止码", "新电能表表地址", "II型采集器资产编号",
				"新电能表表脚封扣", "新电能表表箱封扣1", "新电能表表箱封扣2",
				"采集器表脚封扣", "采集器表箱封扣1", "采集器表箱封扣2",
				"备注", "业务日期"
		};

		HSSFWorkbook workbook = new HSSFWorkbook();				// 声明一个工作薄
		HSSFSheet sheet = workbook.createSheet(sheetName);		// 生成一个表格

		sheet.setDefaultColumnWidth(25);						// 设置表格默认列宽15个字节

		//---------------------------------------------------------------------------------------
		HSSFCellStyle headStyle = setHSSFCellStyle1(workbook);
		HSSFCellStyle contentStyle = setHSSFCellStyle3(workbook);

		// 生成表头内容
		HSSFRow hssfRow = sheet.createRow(0);					// 创建一行
		hssfRow.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = hssfRow.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(headers[i]);
		}

		int rowIndex = 1;
		for (MeterBean1 bean : meterBeanList) {

//0.序号 1.供电所	 2.台区名称 3.用户编号	 4.用户名称 5.用户地址 6.计量点编号 7.计量点地址
//8.旧电能表资产编号 9.旧电能表资产编号 10.旧电能表止码 11.旧电能表表地址
//12.新电能表资产编号 13.新电能表止码 14.新电能表表地址 15.II型采集器资产编号
//16.新电能表电表表脚封扣 17.新电能表表箱封扣1 18.新电能表表箱封扣2
//19.采集器表脚封扣 20.采集器表箱封扣1 21.采集器表箱封扣2 22.备注 23.业务日期

			hssfRow = sheet.createRow(rowIndex);
			setCellStyle(contentStyle, hssfRow, 0, rowIndex + "");						// 0.序号
			setCellStyle(contentStyle, hssfRow, 1, bean.getPowerSupplyBureau());		// 1.供电所
			setCellStyle(contentStyle, hssfRow, 2, bean.getCourts());					// 2.台区名称
			setCellStyle(contentStyle, hssfRow, 3, bean.getUserNumber());				// 3.用户编号
			setCellStyle(contentStyle, hssfRow, 4, bean.getUserName());					// 4.用户名称
			setCellStyle(contentStyle, hssfRow, 5, bean.getUserAddr());					// 5.用户地址
			setCellStyle(contentStyle, hssfRow, 6, bean.getMeasurementPointNumber());	// 6.计量点编号
			setCellStyle(contentStyle, hssfRow, 7, bean.getMeasuringPointAddress());	// 7.计量点地址
			setCellStyle(contentStyle, hssfRow, 8, bean.getOldAssetNumbers());			// 8.旧电能表资产编号
			if(bean.isFinish()) {
				if(bean.getRelaceOrAnd().equals("0")) {
					setCellStyle(contentStyle, hssfRow, 9, bean.getOldAssetNumbers());            // 9.旧电能表资产编号
					setCellStyle(contentStyle, hssfRow, 10, bean.getOldElectricity());            // 10.旧电能表止码
					setCellStyle(contentStyle, hssfRow, 11, bean.getOldAddr());                    // 11.旧电能表表地址

					setCellStyle(contentStyle, hssfRow, 12, bean.getNewAssetNumbersScan());        // 12.新电能表资产编号
					setCellStyle(contentStyle, hssfRow, 13, bean.getNewElectricity());            // 13.新电能表止码
					setCellStyle(contentStyle, hssfRow, 14, bean.getNewAddr());                    // 14.新电能表表地址

					setCellStyle(contentStyle, hssfRow, 15, "");    // 15.II型采集器资产编号

					setCellStyle(contentStyle, hssfRow, 16, bean.getMeterFootNumbers());		// 16.电表表脚封扣
					setCellStyle(contentStyle, hssfRow, 17, bean.getMeterBodyNumbers1());		// 17.表箱封扣1
					setCellStyle(contentStyle, hssfRow, 18, bean.getMeterBodyNumbers1());		// 18.表箱封扣2

					setCellStyle(contentStyle, hssfRow, 19, "");	// 19.采集器电表表脚封扣
					setCellStyle(contentStyle, hssfRow, 20, "");	// 20.采集器表箱封扣1
					setCellStyle(contentStyle, hssfRow, 21, "");	// 21.采集器表箱封扣2

				}else {
					setCellStyle(contentStyle, hssfRow, 9, bean.getOldAssetNumbers());            // 9.旧电能表资产编号
					setCellStyle(contentStyle, hssfRow, 10, "");            // 10.旧电能表止码
					setCellStyle(contentStyle, hssfRow, 11, "");                    // 11.旧电能表表地址

					setCellStyle(contentStyle, hssfRow, 12, "");        // 12.新电能表资产编号
					setCellStyle(contentStyle, hssfRow, 13, "");            // 13.新电能表止码
					setCellStyle(contentStyle, hssfRow, 14, "");                    // 14.新电能表表地址

					setCellStyle(contentStyle, hssfRow, 15, bean.getCollectorAssetNumbersScan());    // 15.II型采集器资产编号

					setCellStyle(contentStyle, hssfRow, 16, "");								// 16.新电能表电表表脚封扣
					setCellStyle(contentStyle, hssfRow, 17, "");								// 17.新电能表表箱封扣1
					setCellStyle(contentStyle, hssfRow, 18, "");								// 18.新电能表表箱封扣2

					setCellStyle(contentStyle, hssfRow, 19, bean.getCollectorFootNumbers());	// 19.采集器电表表脚封扣
					setCellStyle(contentStyle, hssfRow, 20, bean.getCollectorBodyNumbers1());	// 20.采集器表箱封扣1
					setCellStyle(contentStyle, hssfRow, 21, bean.getCollectorBodyNumbers1());	// 21.采集器表箱封扣2
				}

				// 旧电能表地址与资产编码不匹配。
				// 新电能表地址与资产编码不匹配。
				// 有用户档案,现场找不到表计。
				// 现场有表计，无匹配用户档案。
				String notes = "";
				if(bean.isOldAddrAndAsset())
					if(bean.getRelaceOrAnd().equals("0"))
						notes += "旧电能表地址与资产编码不匹配";



				setCellStyle(contentStyle, hssfRow, 22, notes);								// 22.备注
				setCellStyle(contentStyle, hssfRow, 23, bean.getTime());					// 23.业务日期
			}else {

				setCellStyle(contentStyle, hssfRow, 9, "");            	// 9.旧电能表资产编号
				setCellStyle(contentStyle, hssfRow, 10, "");            // 10.旧电能表止码
				setCellStyle(contentStyle, hssfRow, 11, "");                    // 11.旧电能表表地址

				setCellStyle(contentStyle, hssfRow, 12, "");        	// 12.新电能表资产编号
				setCellStyle(contentStyle, hssfRow, 13, "");            // 13.新电能表止码
				setCellStyle(contentStyle, hssfRow, 14, "");                    // 14.新电能表表地址

				//16.新电能表电表表脚封扣 17.新电能表表箱封扣1 18.新电能表表箱封扣2
				//19.采集器表脚封扣 20.采集器表箱封扣1 21.采集器表箱封扣2 22.备注

				setCellStyle(contentStyle, hssfRow, 16, "");								// 16.新电能表电表表脚封扣
				setCellStyle(contentStyle, hssfRow, 17, "");								// 17.新电能表表箱封扣1
				setCellStyle(contentStyle, hssfRow, 18, "");								// 18.新电能表表箱封扣2

				setCellStyle(contentStyle, hssfRow, 19, "");								// 19.采集器表脚封扣
				setCellStyle(contentStyle, hssfRow, 20, "");								// 20.采集器表箱封扣1
				setCellStyle(contentStyle, hssfRow, 21, "");								// 21.采集器表箱封扣2

				setCellStyle(contentStyle, hssfRow, 22, "");								// 22.备注
				setCellStyle(contentStyle, hssfRow, 23, "");								// 23.业务日期
			}

			rowIndex++;
		}


		//------------------------------------- sheet1 ----------------------------------------------

		HSSFSheet sheet1 = workbook.createSheet("旧电能表地址与资产编码不匹配");		// 生成一个表格
		sheet1.setDefaultColumnWidth(25);											// 设置表格默认列宽15个字节

		String[] assetNumberTitle = { "无匹配的资产编号"};
		// 生成表头内容
		HSSFRow hssfRow1 = sheet1.createRow(0);					// 创建一行
		hssfRow1.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < assetNumberTitle.length; i++) {
			HSSFCell cell = hssfRow1.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(assetNumberTitle[i]);
		}

		rowIndex = 1;
		if(assetNumberBeenList != null && assetNumberBeenList.size() > 0) {
			for (AssetNumberBean bean : assetNumberBeenList) {
				hssfRow1 = sheet1.createRow(rowIndex);
				setCellStyle(contentStyle, hssfRow1, 0, bean.getAssetNumbers());        // 无匹配的资产编号
				rowIndex++;
			}
		}

		//------------------------------------- sheet2 ----------------------------------------------

		HSSFSheet sheet2 = workbook.createSheet("集中器");		// 生成一个表格
		sheet2.setDefaultColumnWidth(25);


		String[] concentratorTitle = {"资产编号", "纬度", "经度", "抄表区段", "地址",
				"电表表脚封扣", "表箱封扣1", "表箱封扣2"};
		// 生成表头内容
		HSSFRow hssfRow2 = sheet2.createRow(0);					// 创建一行
		hssfRow2.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < concentratorTitle.length; i++) {
			HSSFCell cell = hssfRow2.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(concentratorTitle[i]);
		}

		rowIndex = 1;
		if(concentratorBeanList != null && concentratorBeanList.size() > 0) {
			for (ConcentratorBean bean : concentratorBeanList) {
				hssfRow2 = sheet2.createRow(rowIndex);
				// LogUtils.i("ConcentratorBean:" + bean.toString());
				setCellStyle(contentStyle, hssfRow2, 0, bean.getAssetNumbers());        // 资产编号(集中器)
				setCellStyle(contentStyle, hssfRow2, 1, bean.getLatitude());        	// 纬度
				setCellStyle(contentStyle, hssfRow2, 2, bean.getLongitude());        	// 经度
				setCellStyle(contentStyle, hssfRow2, 3, bean.getTheMeteringSection());  // 抄表区段
				setCellStyle(contentStyle, hssfRow2, 4, bean.getAddr());        		// 地址

				setCellStyle(contentStyle, hssfRow2, 5, bean.getMeterFootNumbers());    // 电表表脚封扣
				setCellStyle(contentStyle, hssfRow2, 6, bean.getMeterBodyNumbers1());   // 表箱封扣1
				setCellStyle(contentStyle, hssfRow2, 7, bean.getMeterBodyNumbers1());   // 表箱封扣2

				rowIndex++;
			}
		}

		//------------------------------------- sheet3 ----------------------------------------------

		HSSFSheet sheet3 = workbook.createSheet("变压器");		// 生成一个表格
		sheet3.setDefaultColumnWidth(25);

		String[] transformerTitle = {"纬度", "经度", "抄表区段", "地址"};
		// 生成表头内容
		HSSFRow hssfRow3 = sheet3.createRow(0);					// 创建一行
		hssfRow3.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < transformerTitle.length; i++) {
			HSSFCell cell = hssfRow3.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(transformerTitle[i]);
		}

		rowIndex = 1;
		if(transformerBeanList != null && transformerBeanList.size() > 0) {
			for (TransformerBean bean : transformerBeanList) {
				hssfRow3 = sheet3.createRow(rowIndex);
				// LogUtils.i("TransformerBean:" + bean.toString());
				setCellStyle(contentStyle, hssfRow3, 0, bean.getLatitude());        	// 纬度
				setCellStyle(contentStyle, hssfRow3, 1, bean.getLongitude());        	// 经度
				setCellStyle(contentStyle, hssfRow3, 2, bean.getTheMeteringSection());  // 抄表区段
				setCellStyle(contentStyle, hssfRow3, 3, bean.getAddr());        		// 地址
				rowIndex++;
			}
		}

		LogUtils.i("excelPath1:" + excelPath);
		return writeHSSFWorkbook(workbook, excelPath);
	}

	/**
	 * xx供电所xx台区（抄表区段）集中器户表档案 (生成-计量自动化系统).xls
	 *
	 * @param context
	 * @param meterBeanList
	 * @param excelPath
	 * @return
	 */
	public static boolean writeExcel2(Context context,
									  List<MeterBean1> meterBeanList,
									  String excelPath){

		String sheetName = "集中器户表档案";
		String[] headers = new String[]{
				"测量点号", "电表资产编号", "电表通讯方式", "电表通讯地址", "通讯参数模板ID",
				"倍率", "是否是变压器总表", "接线方式", "采集器编号", "采集资产编号",
				"用户编号", "用户名称", "用电地址", "业务日期"
		};

		HSSFWorkbook workbook = new HSSFWorkbook();				// 声明一个工作薄
		HSSFSheet sheet = workbook.createSheet(sheetName);		// 生成一个表格

		sheet.setDefaultColumnWidth(25);						// 设置表格默认列宽15个字节

		//---------------------------------------------------------------------------------------
		HSSFCellStyle headStyle = setHSSFCellStyle1(workbook);
		HSSFCellStyle contentStyle = setHSSFCellStyle3(workbook);

		// 生成表头内容
		HSSFRow hssfRow = sheet.createRow(0);					// 创建一行
		hssfRow.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = hssfRow.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(headers[i]);
		}

		int rowIndex = 1;
		for (MeterBean1 bean : meterBeanList) {

//		0.测量点号（必填）
//	【序号从2开始递增】
//		1.电表资产编号(必填，不能重复)
//	【换表取新表资产编号，加装II型采集器取旧表资产编号】
//		2.电表通讯方式：
//		01:RS-485【RS-485表加装II型采集器】
//		02:载波）【换新表-载波表】
//		3.电表通讯地址(必填)
//	【换表取新表表地址，加装II型采集器取旧表表地址】
//		4.通讯参数模板ID
//		10:RS-485【RS-485表加装II型采集器】
//		14:载波）【换新表-载波表】
//		5.倍率(必填)
//	【默认1】
//		6.是否是变压器总表（01:是 02:否）
//	【默认为02】
//		7.接线方式
//		01:单相【24位编码的第6位代码为D】、【14位编码的第6位代码为2】
//		03:三相四线【24位编码的第6位代码为S】、【14位编码的第6位代码为3】
//		8.采集器编号(地址)
//	【默认为0】
//		9.采集资产编号
//	【默认为空】
//		10.用户编号
//	【数据源】
//		11.用户名称
//	【数据源】
//		12.用电地址
//	【数据源】

//		13.业务日期

			if(bean.isFinish() && bean.getRelaceOrAnd() != null) {


				boolean isRelaeMeter = bean.getRelaceOrAnd().equals("0");    // 是否是--换表

				hssfRow = sheet.createRow(rowIndex);
				setCellStyle(contentStyle, hssfRow, 0, (rowIndex + 1) + "");    // 0.测量点号 【序号从2开始递增】
				if (isRelaeMeter) {
					// 1.电表资产编号(必填，不能重复) 【换表取新表资产编号，加装II型采集器取旧表资产编号】
					setCellStyle(contentStyle, hssfRow, 1, bean.getNewAssetNumbersScan());        // 1.新电能表资产编号
					// 2.电表通讯方式： 01:RS-485【RS-485表加装II型采集器】 02:载波【换新表-载波表】
					setCellStyle(contentStyle, hssfRow, 2, "02");
					// 3.电表通讯地址(必填) 【换表取新表表地址，加装II型采集器取旧表表地址】
					setCellStyle(contentStyle, hssfRow, 3, bean.getNewAddr());                      // 3.新电能表表地址
					// 4.通讯参数模板ID 10:RS-485【RS-485表加装II型采集器】 14:载波【换新表-载波表】
					setCellStyle(contentStyle, hssfRow, 4, "14");
					// 5.倍率(必填)
					setCellStyle(contentStyle, hssfRow, 5, "1");
					// 6.是否是变压器总表（01:是 02:否）
					setCellStyle(contentStyle, hssfRow, 6, "02");

					// 7.接线方式
					//		01:单相【24位编码的第6位代码为D】、【14位编码的第6位代码为2】
					//		03:三相四线【24位编码的第6位代码为S】、【14位编码的第6位代码为3】
					String newAssetNumber = bean.getNewAssetNumbersScan();
					if (newAssetNumber.length() == 24) {
						if (newAssetNumber.substring(5, 6).toUpperCase().equalsIgnoreCase("D"))
							setCellStyle(contentStyle, hssfRow, 7, "01");
						else if (newAssetNumber.substring(5, 6).toUpperCase().equalsIgnoreCase("S"))
							setCellStyle(contentStyle, hssfRow, 7, "03");
						else
							setCellStyle(contentStyle, hssfRow, 7, "01");

					} else if (newAssetNumber.length() == 14) {
						if (newAssetNumber.substring(5, 6).toUpperCase().equalsIgnoreCase("2"))
							setCellStyle(contentStyle, hssfRow, 7, "01");
						else if (newAssetNumber.substring(5, 6).toUpperCase().equalsIgnoreCase("3"))
							setCellStyle(contentStyle, hssfRow, 7, "03");
						else
							setCellStyle(contentStyle, hssfRow, 7, "01");
					} else
						setCellStyle(contentStyle, hssfRow, 7, "01");


				} else {
					setCellStyle(contentStyle, hssfRow, 1, bean.getOldAssetNumbers());            // 1.旧电能表资产编号
					setCellStyle(contentStyle, hssfRow, 2, "01");
					setCellStyle(contentStyle, hssfRow, 3, bean.getOldAddr());                      // 3.旧电能表表地址
					setCellStyle(contentStyle, hssfRow, 4, "10");
					setCellStyle(contentStyle, hssfRow, 5, "1");
					setCellStyle(contentStyle, hssfRow, 6, "02");

					String oldAssetNumber = bean.getOldAssetNumbers();
					if (oldAssetNumber.length() == 24) {
						if (oldAssetNumber.substring(5, 6).toUpperCase().equalsIgnoreCase("D"))
							setCellStyle(contentStyle, hssfRow, 7, "01");
						else if (oldAssetNumber.substring(5, 6).toUpperCase().equalsIgnoreCase("S"))
							setCellStyle(contentStyle, hssfRow, 7, "03");
						else
							setCellStyle(contentStyle, hssfRow, 7, "01");

					} else if (oldAssetNumber.length() == 14) {
						if (oldAssetNumber.substring(5, 6).toUpperCase().equalsIgnoreCase("2"))
							setCellStyle(contentStyle, hssfRow, 7, "01");
						else if (oldAssetNumber.substring(5, 6).toUpperCase().equalsIgnoreCase("3"))
							setCellStyle(contentStyle, hssfRow, 7, "03");
						else
							setCellStyle(contentStyle, hssfRow, 7, "01");
					} else
						setCellStyle(contentStyle, hssfRow, 7, "01");
				}

				setCellStyle(contentStyle, hssfRow, 8, "0");                                // 8.采集器编号(地址) 【默认为0】
				setCellStyle(contentStyle, hssfRow, 9, "");                                    // 9.采集资产编号 【默认为空】
				setCellStyle(contentStyle, hssfRow, 10, bean.getUserNumber());                // 10.用户编号
				setCellStyle(contentStyle, hssfRow, 11, bean.getUserName());                // 11.用户名称
				setCellStyle(contentStyle, hssfRow, 12, bean.getUserAddr());                // 12.用户地址
				setCellStyle(contentStyle, hssfRow, 13, bean.getTime());                	// 13.业务日期
				rowIndex++;
			}
		}


		LogUtils.i("excelPath2:" + excelPath);
		return writeHSSFWorkbook(workbook, excelPath);
	}

	/**
	 * xx供电所xx台区（抄表区段）户表档案(生成-营销系统).xls
	 *
	 * @param context
	 * @param meterBeanList
	 * @param excelPath
	 * @return
	 */
	public static boolean writeExcel3(Context context,
									  List<MeterBean1> meterBeanList,
									  String excelPath){

		String sheetName = "户表档案";
		String[] headers = new String[]{
				"用户编号", "用户名称", "用电地址", "计量点编号", "计量点地址",
				"变更标志", "装拆标志", "资产编号", "有功总示数", "业务日期"

		};

		HSSFWorkbook workbook = new HSSFWorkbook();				// 声明一个工作薄
		HSSFSheet sheet = workbook.createSheet(sheetName);		// 生成一个表格

		sheet.setDefaultColumnWidth(25);						// 设置表格默认列宽15个字节

		//---------------------------------------------------------------------------------------
		HSSFCellStyle headStyle = setHSSFCellStyle1(workbook);
		HSSFCellStyle contentStyle = setHSSFCellStyle3(workbook);

		// 生成表头内容
		HSSFRow hssfRow = sheet.createRow(0);					// 创建一行
		hssfRow.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = hssfRow.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(headers[i]);
		}

		int rowIndex = 1;
		for (MeterBean1 bean : meterBeanList) {

			//0.用户编号 1.用户名称 2.用电地址 3.计量点编号 4.计量点地址 5.变更标志	 6.装拆标志 7.资产编号 8.有功总示数
			//040500231017251	韦美连	大化县景山村古旧新屯	0405002310172511	大化县景山村古旧新屯	更换前	未装拆	0223A2F0117786
			//040500231017251	韦美连	大化县景山村古旧新屯	0405002310172511	大化县景山村古旧新屯	更换后	未装拆

			boolean isFinish = bean.isFinish();
			boolean isRelaeMeter = bean.getRelaceOrAnd().equals("0");    // 是否是--换表

			hssfRow = sheet.createRow(rowIndex);
			setCellStyle(contentStyle, hssfRow, 0, bean.getUserNumber());				// 0.用户编号
			setCellStyle(contentStyle, hssfRow, 1, bean.getUserName());					// 1.用户名称
			setCellStyle(contentStyle, hssfRow, 2, bean.getUserAddr());					// 2.用电地址
			setCellStyle(contentStyle, hssfRow, 3, bean.getMeasurementPointNumber());	// 3.计量点编号
			setCellStyle(contentStyle, hssfRow, 4, bean.getMeasuringPointAddress());	// 4.计量点地址
			setCellStyle(contentStyle, hssfRow, 5, "更换前");							// 5.装拆标志
			setCellStyle(contentStyle, hssfRow, 6, "未装拆");							// 6.资产编号
			setCellStyle(contentStyle, hssfRow, 7, bean.getOldAssetNumbers());			// 7.旧电能表资产编号
			if(isFinish) {
				if(isRelaeMeter) {
					setCellStyle(contentStyle, hssfRow, 8, bean.getOldElectricity());       // 8.旧电能表止码
					setCellStyle(contentStyle, hssfRow, 9, bean.getTime());                    // 9.业务日期
				}else {
					setCellStyle(contentStyle, hssfRow, 8, "");                                    // 8.旧电能表止码
					setCellStyle(contentStyle, hssfRow, 9, "");            							// 9.业务日期
				}
			}
			else {
				setCellStyle(contentStyle, hssfRow, 8, "");                                    // 8.旧电能表止码
				setCellStyle(contentStyle, hssfRow, 9, "");            							// 9.业务日期
			}

			hssfRow = sheet.createRow(rowIndex + 1);
			setCellStyle(contentStyle, hssfRow, 0, bean.getUserNumber());				// 0.用户编号
			setCellStyle(contentStyle, hssfRow, 1, bean.getUserName());					// 1.用户名称
			setCellStyle(contentStyle, hssfRow, 2, bean.getUserAddr());					// 2.用电地址
			setCellStyle(contentStyle, hssfRow, 3, bean.getMeasurementPointNumber());	// 3.计量点编号
			setCellStyle(contentStyle, hssfRow, 4, bean.getMeasuringPointAddress());	// 4.计量点地址
			setCellStyle(contentStyle, hssfRow, 5, "更换后");							// 5.装拆标志
			setCellStyle(contentStyle, hssfRow, 6, "未装拆");							// 6.资产编号

			if(isFinish) {
				if(isRelaeMeter) {
					setCellStyle(contentStyle, hssfRow, 7, bean.getNewAssetNumbersScan());      // 7.新电能表资产编号
					setCellStyle(contentStyle, hssfRow, 8, bean.getNewElectricity());           // 8.新电能表止码
					setCellStyle(contentStyle, hssfRow, 9, bean.getTime());                        // 9.业务日期
				}else {
					setCellStyle(contentStyle, hssfRow, 7, "");      							// 7.新电能表资产编号
					setCellStyle(contentStyle, hssfRow, 8, "");            						// 8.新电能表止码
					setCellStyle(contentStyle, hssfRow, 9, "");            						// 9.业务日期
				}
			}
			else {
				setCellStyle(contentStyle, hssfRow, 7, "");      							// 7.新电能表资产编号
				setCellStyle(contentStyle, hssfRow, 8, "");            						// 8.新电能表止码
				setCellStyle(contentStyle, hssfRow, 9, "");            						// 9.业务日期
			}

			rowIndex = rowIndex + 2;
		}

		LogUtils.i("excelPath3:" + excelPath);
		return writeHSSFWorkbook(workbook, excelPath);
	}

	/**
	 * XX供电所XX台区（东兰武篆供电所江平村-弄文公变集中器）日冻结验收数据.xls
	 *
	 * @param context
	 * @param acceptanceBeanList
	 * @param excelPath
	 * @return
	 */
	public static boolean writeExcelAcceptanceByDay(Context context,
									  List<AcceptanceBean> acceptanceBeanList,
									  String excelPath){

		String sheetName = "日冻结验收数据";
		String[] headers = new String[]{
				"测量点名称", "电表地址", "终端内编号", "资产编号",
				"用户编号", "集抄正有功总(kWh)", "现场红外抄读正有功总(kWh)",
				"集抄与现场差异（(kWh)【H列-G列】", "验收结果"

		};

		HSSFWorkbook workbook = new HSSFWorkbook();				// 声明一个工作薄
		HSSFSheet sheet = workbook.createSheet(sheetName);		// 生成一个表格

		sheet.setDefaultColumnWidth(25);						// 设置表格默认列宽15个字节

		//---------------------------------------------------------------------------------------
		HSSFCellStyle headStyle = setHSSFCellStyle1(workbook);
		HSSFCellStyle contentStyle = setHSSFCellStyle3(workbook);

		// 生成表头内容
		HSSFRow hssfRow = sheet.createRow(0);					// 创建一行
		hssfRow.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = hssfRow.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(headers[i]);
		}

		int rowIndex = 1;
		for (AcceptanceBean bean : acceptanceBeanList) {

			hssfRow = sheet.createRow(rowIndex);
			setCellStyle(contentStyle, hssfRow, 0, bean.getUserName());						// 0.测量点名称
			setCellStyle(contentStyle, hssfRow, 1, bean.getMeterAddr());					// 1.电表地址
			setCellStyle(contentStyle, hssfRow, 2, bean.getTerminalNo());					// 2.终端内编号
			setCellStyle(contentStyle, hssfRow, 3, bean.getAssetNumbers());					// 3.资产编号
			setCellStyle(contentStyle, hssfRow, 4, bean.getUserNumber());					// 4.用户编号
			setCellStyle(contentStyle, hssfRow, 5, bean.getElectricityInByDay());			// 5.集抄正有功总(kWh)
			setCellStyle(contentStyle, hssfRow, 6, bean.getElectricityScanByDay());			// 6.现场红外抄读正有功总(kWh)
			setCellStyle(contentStyle, hssfRow, 7, bean.getDifferencesByDay());				// 7.集抄与现场差异（(kWh)【H列-G列】
			setCellStyle(contentStyle, hssfRow, 8, bean.getConclusionByDay());				// 8.验收结果
										// 【为0，验收结果为“正常”；大于0，验收结果为“异常大”；小于0，验收结果为“异常小”；】

			rowIndex = rowIndex + 1;
		}

		LogUtils.i("writeExcelAcceptanceByDay -- excelPath:" + excelPath);
		return writeHSSFWorkbook(workbook, excelPath);
	}


	/**
	 * XX供电所XX台区（东兰武篆供电所江平村-弄文公变集中器）月冻结验收数据.xls
	 *
	 * @param context
	 * @param acceptanceBeanList
	 * @param excelPath
	 * @return
	 */
	public static boolean writeExcelAcceptanceByMonth(Context context,
													List<AcceptanceBean> acceptanceBeanList,
													String excelPath){

		String sheetName = "月冻结验收数据";
		String[] headers = new String[]{
				"测量点名称", "电表地址", "终端内编号", "资产编号",
				"用户编号", "集抄正有功总(kWh)", "现场红外抄读正有功总(kWh)",
				"集抄与现场差异（(kWh)【H列-G列】", "验收结果"

		};

		HSSFWorkbook workbook = new HSSFWorkbook();				// 声明一个工作薄
		HSSFSheet sheet = workbook.createSheet(sheetName);		// 生成一个表格

		sheet.setDefaultColumnWidth(25);						// 设置表格默认列宽15个字节

		//---------------------------------------------------------------------------------------
		HSSFCellStyle headStyle = setHSSFCellStyle1(workbook);
		HSSFCellStyle contentStyle = setHSSFCellStyle3(workbook);

		// 生成表头内容
		HSSFRow hssfRow = sheet.createRow(0);					// 创建一行
		hssfRow.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = hssfRow.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(headers[i]);
		}

		int rowIndex = 1;
		for (AcceptanceBean bean : acceptanceBeanList) {

			hssfRow = sheet.createRow(rowIndex);
			setCellStyle(contentStyle, hssfRow, 0, bean.getUserName());						// 0.测量点名称
			setCellStyle(contentStyle, hssfRow, 1, bean.getMeterAddr());					// 1.电表地址
			setCellStyle(contentStyle, hssfRow, 2, bean.getTerminalNo());					// 2.终端内编号
			setCellStyle(contentStyle, hssfRow, 3, bean.getAssetNumbers());					// 3.资产编号
			setCellStyle(contentStyle, hssfRow, 4, bean.getUserNumber());					// 4.用户编号
			setCellStyle(contentStyle, hssfRow, 5, bean.getElectricityInByMonth());			// 5.集抄正有功总(kWh)
			setCellStyle(contentStyle, hssfRow, 6, bean.getElectricityScanByMonth());			// 6.现场红外抄读正有功总(kWh)
			setCellStyle(contentStyle, hssfRow, 7, bean.getDifferencesByMonth());				// 7.集抄与现场差异（(kWh)【H列-G列】
			setCellStyle(contentStyle, hssfRow, 8, bean.getConclusionByMonth());				// 8.验收结果
			// 【为0，验收结果为“正常”；大于0，验收结果为“异常大”；小于0，验收结果为“异常小”；】

			rowIndex = rowIndex + 1;
		}

		LogUtils.i("writeExcelAcceptanceByMonth -- excelPath:" + excelPath);
		return writeHSSFWorkbook(workbook, excelPath);
	}


	/**
	 * 零散新装.xls
	 *
	 * @param context
	 * @param scatteredNewMeterBeanList
	 * @param excelPath
	 * @return
	 */
	public static boolean writeExcelScatteredNewMeter(Context context,
													  List<ScatteredNewMeterBean> scatteredNewMeterBeanList,
													  String excelPath){


		String sheetName = "零散新装";
		String[] headers = new String[]{
				"用户编号", "用户名称", "用户地址", "用户电话",
				"新表表地址", "新表资产编号", "新电能表止码",
				"电表表脚封扣", "表箱封扣1", "表箱封扣2", "完成时间"

		};

		HSSFWorkbook workbook = new HSSFWorkbook();				// 声明一个工作薄
		HSSFSheet sheet = workbook.createSheet(sheetName);		// 生成一个表格

		sheet.setDefaultColumnWidth(25);						// 设置表格默认列宽15个字节

		//---------------------------------------------------------------------------------------
		HSSFCellStyle headStyle = setHSSFCellStyle1(workbook);
		HSSFCellStyle contentStyle = setHSSFCellStyle3(workbook);

		// 生成表头内容
		HSSFRow hssfRow = sheet.createRow(0);					// 创建一行
		hssfRow.setHeight((short) (15.625 * 40));				// n为行高的像素数。
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = hssfRow.createCell(i);				// 这一行的第i列的单元格
			cell.setCellStyle(headStyle);
			cell.setCellValue(headers[i]);
		}

		int rowIndex = 1;
		for (ScatteredNewMeterBean bean : scatteredNewMeterBeanList) {

			hssfRow = sheet.createRow(rowIndex);
			setCellStyle(contentStyle, hssfRow, 0, bean.getUserNumber());				// 0.用户编号
			setCellStyle(contentStyle, hssfRow, 1, bean.getUserName());					// 1.用户名称
			setCellStyle(contentStyle, hssfRow, 2, bean.getUserAddr());					// 2.用户地址
			setCellStyle(contentStyle, hssfRow, 3, bean.getUserPhone());				// 3.用户电话
			setCellStyle(contentStyle, hssfRow, 4, bean.getNewAddr());					// 4.新表表地址

			setCellStyle(contentStyle, hssfRow, 5, bean.getNewAssetNumbers());			// 5.新表资产编号
			setCellStyle(contentStyle, hssfRow, 6, bean.getNewElectricity());			// 6.新电能表止码
			setCellStyle(contentStyle, hssfRow, 7, bean.getMeterFootNumbers());			// 7.电表表脚封扣
			setCellStyle(contentStyle, hssfRow, 8, bean.getMeterBodyNumbers1());		// 8.表箱封扣1
			setCellStyle(contentStyle, hssfRow, 9, bean.getMeterBodyNumbers2());		// 9.表箱封扣2

			setCellStyle(contentStyle, hssfRow, 10, bean.getTime());		// 10.完成时间


			rowIndex = rowIndex + 1;
		}

		LogUtils.i("writeExcelScatteredNewMeter -- excelPath:" + excelPath);
		return writeHSSFWorkbook(workbook, excelPath);
	}




	/**
	 * 写入Excel文件 -- HSSFWorkbook -- xls
	 *
	 * @param workbook      		工作簿
	 * @param filePath		生成的文件(全名)
	 * @return
	 */
	public static boolean writeHSSFWorkbook(HSSFWorkbook workbook, String filePath) {

		boolean isSuccessful = false;

		FileOutputStream fileOutStream = null;
		if (workbook != null) {
			try {
				fileOutStream = new FileOutputStream( new File(filePath) );
				workbook.write(fileOutStream);
				isSuccessful = true;

			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (fileOutStream != null) {
					try {
						fileOutStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return isSuccessful;
	}

	private static void setCellStyle(HSSFCellStyle style, HSSFRow hssfRow, int index, String value){

		HSSFCell cell = hssfRow.createCell(index);
		cell.setCellStyle(style);
		cell.setCellValue(value);

	}

	/**
	 * 样式1
	 *
	 * @param workbook
	 * @return
	 */
	private static HSSFCellStyle setHSSFCellStyle1(HSSFWorkbook workbook){
		// 生成样式
		HSSFCellStyle style =  workbook.createCellStyle();

		// 背景色
//		style.setFillForegroundColor(HSSFColor.YELLOW.index);
//		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style.setFillBackgroundColor(HSSFColor.YELLOW.index);
		style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);	// 设置这些样式--表头
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);

		// 设置这些样式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

		style.setWrapText(true);									// 自动换行

		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);									// 把字体应用到当前的样式

		/**
		 * poi --高版本的
		 */
//		// 设置这些样式
//		// -- 1、设置背景色：
//		//style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());		// 绿色
//		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		// -- 2、设置边框:
//		style.setBorderBottom(BorderStyle.THIN);
//		style.setBorderLeft(BorderStyle.THIN);
//		style.setBorderRight(BorderStyle.THIN);
//		style.setBorderTop(BorderStyle.THIN);
//		// -- 3、设置居中:
//		style.setAlignment(HorizontalAlignment.CENTER);
//		style.setVerticalAlignment(VerticalAlignment.CENTER);
//
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		//font.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
//		font.setBold(true); 									// 加粗
//		font.setFontName("黑体"); 								// 字体
//		font.setFontHeightInPoints((short) 11); 				// 大小
//		style.setFont(font);									// 把字体应用到当前的样式

		HSSFDataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));				//设置CELL格式为文本格式

		return style;
	}

	/**
	 * 样式2
	 *
	 * @param workbook
	 * @return
	 */
	private HSSFCellStyle setHSSFCellStyle2(HSSFWorkbook workbook){
		// 生成样式
		HSSFCellStyle style = workbook.createCellStyle();

		style.setFillForegroundColor(HSSFColor.WHITE.index);	// 设置这些样式--表头
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);									// 把字体应用到当前的样式


//		// 设置这些样式
//		// -- 1、设置背景色：
//		//style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());		// 白色
//		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		// -- 2、设置边框:
//		style.setBorderBottom(BorderStyle.THIN);
//		style.setBorderLeft(BorderStyle.THIN);
//		style.setBorderRight(BorderStyle.THIN);
//		style.setBorderTop(BorderStyle.THIN);
//
//		// -- 3、设置居中:
//		style.setAlignment(HorizontalAlignment.CENTER);
//		style.setVerticalAlignment(VerticalAlignment.CENTER);
//
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		font.setBold(true); // 加粗
//		font.setFontName("黑体"); // 字体
//		font.setFontHeightInPoints((short) 11); // 大小
//		style.setFont(font);									// 把字体应用到当前的样式

		HSSFDataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));				//设置CELL格式为文本格式

		return style;
	}

	/**
	 * 样式3
	 *
	 * @param workbook
	 * @return
	 */
	private static HSSFCellStyle setHSSFCellStyle3(HSSFWorkbook workbook){
		// 生成样式
		HSSFCellStyle style = workbook.createCellStyle();

		style.setFillForegroundColor(HSSFColor.WHITE.index);	// 设置这些样式--表头
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(font);									// 把字体应用到当前的样式
//		// 设置这些样式
//		// -- 1、设置背景色：
//		//style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());		// 白色
//		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		// -- 2、设置边框:
//		style.setBorderBottom(BorderStyle.THIN);
//		style.setBorderLeft(BorderStyle.THIN);
//		style.setBorderRight(BorderStyle.THIN);
//		style.setBorderTop(BorderStyle.THIN);
//
//		// -- 3、设置居中:
//		style.setAlignment(HorizontalAlignment.CENTER);
//		style.setVerticalAlignment(VerticalAlignment.CENTER);
//
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		font.setBold(false); // 加粗
//		font.setFontName("黑体"); // 字体
//		font.setFontHeightInPoints((short) 11); // 大小
//		style.setFont(font);									// 把字体应用到当前的样式

		HSSFDataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));				//设置CELL格式为文本格式
		return style;
	}




	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------
//	/**
//	 * 根据文件名读取excel文件
//	 *
//	 * @param fileName		文件字符串
//	 * @return
//	 * @throws IOException
//	 */
//	public List<ArrayList<String>> readFile(String fileName) throws Exception {
//
//		List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();
//		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());		// 后缀名
//
//		File file = new File(fileName);							// 检查文件是否存在
//		if (file == null || !file.exists()) {
//			return dataLst;
//		}
//
//		InputStream inputStream = null;							// 读取excel
//		try {
//			inputStream = new FileInputStream(file);
//			Workbook wb = null;									// 根据版本选择创建Workbook的方式
//			if (fileType.equalsIgnoreCase("xls")) {						// 	xls -- HSSFWorkbook
//				wb = new HSSFWorkbook(inputStream);
//			} else if (fileType.equalsIgnoreCase("xlsx")) {				// 	xlsx -- XSSFWorkbook
//				wb = new XSSFWorkbook(inputStream);
//			}
//
//			if (wb == null) {
//				return dataLst;
//			}
//
//			dataLst = readWorkbook(wb);
//
//		} catch (Exception e) {
//			throw e;
//
//		} finally {
//			try {
//				if (inputStream != null) {
//					inputStream.close();
//				}
//			} catch (Exception e) {
//				//logger.error("关闭流异常:" + e, e);
//			}
//		}
//		return dataLst;
//	}
//
//
//	/**
//	 * 读取数据
//	 *
//	 * @param wb 	工作本
//	 * @return
//	 */
//	private List<ArrayList<String>> readWorkbook(Workbook wb) {
//
//		List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();
//
//		Sheet sheet = wb.getSheetAt(0);										// 得到第一个shell
//		this.totalRows = sheet.getPhysicalNumberOfRows();					// 获取行数
//		if (this.totalRows >= 1 && sheet.getRow(0) != null) {
//			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();	// 获取列数
//		}
//
//		for (int r = 0; r < this.totalRows; r++) {							// 循环Excel的行
//			Row row = sheet.getRow(r);
//			if (row == null) {
//				continue;
//			}
//
//			ArrayList<String> rowLst = new ArrayList<String>();
//			for (int cellIndex = 0; cellIndex < this.totalCells; cellIndex++) {		// 循环Excel的列
//				Cell cell = row.getCell(cellIndex);										// 获取单元格的值
//				String cellValue = "";
//				if (cell == null) {
//					rowLst.add(cellValue);
//					continue;
//				}
//
//				if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {					// 处理数字型的,自动去零
//					if (HSSFDateUtil.isCellDateFormatted(cell)) {					// 在excel里,日期也是数字,在此要进行判断
//						cellValue = TimeUtils.dateConvertString(cell.getDateCellValue(), TimeUtils.yyyy_MM_ddHHmmss);
//					} else {
//						//cellValue = StringUtil.getRightStr(cell.getNumericCellValue() + ""); //怎么去0
//						cellValue = cell.getNumericCellValue() + "";
//					}
//
//				} else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {			// 处理字符串型
//					cellValue = cell.getStringCellValue();
//
//				} else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {			// 处理布尔型
//					cellValue = cell.getBooleanCellValue() + "";
//
//				} else {// 其它数据类型
//					cellValue = cell.toString() + "";
//				}
//				rowLst.add(cellValue);
//			}
//			dataLst.add(rowLst);
//		}
//		return dataLst;
//	}


//	/**
//	 * 创建excel
//	 *
//	 * @param sheetName				页(表格)的名字
//	 * @param headers				行头
//	 * @param rows					多个行的每一列数据
//	 * @param filePathAndName
//	 * @return
//	 * @throws IOException
//	 */
//	public static boolean createExcel(String sheetName, String[] headers,
//									 List<String[]> rows, String filePathAndName,
//									 Map<Integer, Integer>... maps)
//			throws IOException {
//
//		HSSFWorkbook workbook = new HSSFWorkbook();				// 声明一个工作薄
//		HSSFSheet sheet = workbook.createSheet(sheetName);		// 生成一个表格
//
//		for (Map<Integer, Integer> map : maps) {				// 设置表格的宽
//			for (Integer row : map.keySet()) {
//				sheet.setColumnWidth(row, map.get(row));
//			}
//		}
//
//		sheet.setDefaultColumnWidth(15);						// 设置表格默认列宽15个字节
//
//		// 生成表头样式 -- (样式1)
//		HSSFCellStyle style = workbook.createCellStyle();
//		style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);	// 设置这些样式--表头
//		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		font.setColor(HSSFColor.BLACK.index);
//		font.setFontHeightInPoints((short) 11);
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//		style.setFont(font);									// 把字体应用到当前的样式
//
//
//		// 生成并设置另一个样式 -- (样式2)
//		HSSFCellStyle style2 = workbook.createCellStyle();
//		style2.setFillForegroundColor(HSSFColor.WHITE.index);
//		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font2 = workbook.createFont();
//		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		style2.setFont(font2);									// 把字体应用到当前的样式
//
//
//		// 生成并设置另另一个样式 -- (样式3)
//		HSSFCellStyle style3 = workbook.createCellStyle();
//		style3.setFillForegroundColor(HSSFColor.WHITE.index);
//		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font3 = workbook.createFont();
//		font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		style3.setFont(font3);									// 把字体应用到当前的样式
//
//		HSSFDataFormat format = workbook.createDataFormat();
//		style3.setDataFormat(format.getFormat("@"));
//
//		//---------------------------------------------------------------------------------------
//
//		// 生成表头内容
//		HSSFRow hssfRow = sheet.createRow(0);					// 创建一行
//		for (int i = 0; i < headers.length; i++) {
//			HSSFCell cell = hssfRow.createCell(i);				// 这一行的第i列的单元格
//			cell.setCellStyle(style);
//			cell.setCellValue(headers[i]);
//		}
//
//		// 生成表头以外的内容
//		int rowIndex = 1;
//		for (String[] row : rows) {
//			hssfRow = sheet.createRow(rowIndex);
//			for (int i = 0; i < row.length; i++) {
//
//				HSSFCell cell = hssfRow.createCell(i);
////				if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
////					cell.setCellStyle(style2);
////					cell.setCellValue(Double.valueOf(row[i]));
////
////				}else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
////					cell.setCellStyle(style2);
////					cell.setCellValue(Integer.valueOf(row[i]));
////
////				}else {
////					cell.setCellStyle(style3);
////					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
////					cell.setCellValue(row[i]);
////				}
//				cell.setCellStyle(style3);
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(row[i]);
//			}
//			rowIndex++;
//		}
//
//		return writeHSSFWorkbook(workbook, filePathAndName);
//	}
//
//	/**
//	 * 写入Excel文件 -- HSSFWorkbook -- xls
//	 *
//	 * @param workbook      		工作簿
//	 * @param filePathAndName		生成的文件(全名)
//	 * @return
//	 */
//	public static boolean writeHSSFWorkbook(HSSFWorkbook workbook, String filePathAndName) {
//
//		boolean isSuss = false;
//
//		if (workbook != null) {
//			try {
//				FileOutputStream fileOutStream = new FileOutputStream( new File(filePathAndName));
//				workbook.write(fileOutStream);
//				isSuss = true;
//				if (fileOutStream != null) {
//					fileOutStream.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return isSuss;
//	}
//
//
//	/**
//	 * 创建excel两个sheet
//	 *
//	 * @param sheetName
//	 * @param headers
//	 * @param rows
//	 * @param sheetName2
//	 * @param headers2
//	 * @param rows2
//	 * @param filePathAndName
//	 * @return
//	 * @throws IOException
//	 */
//	public static String createExcelTwo(String sheetName, String[] headers, List<String[]> rows,
//										String sheetName2, String[] headers2, List<String[]> rows2,
//										String filePathAndName) throws IOException {
//
//		// 声明一个工作薄
//		HSSFWorkbook workbook = new HSSFWorkbook();
//		// 生成一个表格
//		HSSFSheet sheet = workbook.createSheet(sheetName);
//		// 设置表格默认列宽15个字节
//		sheet.setDefaultColumnWidth(15);
//		// 生成一个表格2
//		HSSFSheet sheet2 = workbook.createSheet(sheetName2);
//		// 设置表格默认列宽15个字节
//		sheet2.setDefaultColumnWidth(15);
//
//		// 生成表头样式
//		HSSFCellStyle style = workbook.createCellStyle();
//		style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
//		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		font.setColor(HSSFColor.BLACK.index);
//		font.setFontHeightInPoints((short) 11);
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//		// 把字体应用到当前的样式
//		style.setFont(font);
//
//		// 生成并设置另一个样式
//		HSSFCellStyle style2 = workbook.createCellStyle();
//		style2.setFillForegroundColor(HSSFColor.WHITE.index);
//		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font2 = workbook.createFont();
//		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		// 把字体应用到当前的样式
//		style2.setFont(font2);
//
//		HSSFCellStyle style3 = workbook.createCellStyle();
//		style3.setFillForegroundColor(HSSFColor.WHITE.index);
//		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font3 = workbook.createFont();
//		font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		// 把字体应用到当前的样式
//		style3.setFont(font3);
//		HSSFDataFormat format = workbook.createDataFormat();
//		style3.setDataFormat(format.getFormat("@"));
//
//		// 生成表头内容
//		HSSFRow hssfRow = sheet.createRow(0);
//		for (int i = 0; i < headers.length; i++) {
//			HSSFCell cell = hssfRow.createCell(i);
//			cell.setCellStyle(style);
//			cell.setCellValue(headers[i]);
//		}
//
//		// 生成表头以外的内容
//		int rowIndex = 1;
//		for (String[] row : rows) {
//			hssfRow = sheet.createRow(rowIndex);
//			for (int i = 0; i < row.length; i++) {
//				HSSFCell cell = hssfRow.createCell(i);
//				if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
//					cell.setCellStyle(style2);
//					cell.setCellValue(Double.valueOf(row[i]));
//				} else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
//					cell.setCellStyle(style2);
//					cell.setCellValue(Integer.valueOf(row[i]));
//				} else {
//					cell.setCellStyle(style3);
//					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//					cell.setCellValue(row[i]);
//				}
//			}
//			rowIndex++;
//		}
//		// 生成表头内容
//		HSSFRow hssfRow2 = sheet2.createRow(0);
//		for (int i = 0; i < headers2.length; i++) {
//			HSSFCell cell = hssfRow2.createCell(i);
//			cell.setCellStyle(style);
//			cell.setCellValue(headers2[i]);
//		}
//
//		// 生成表头以外的内容
//		int rowIndex2 = 1;
//		for (String[] row : rows2) {
//			hssfRow2 = sheet2.createRow(rowIndex2);
//			for (int i = 0; i < row.length; i++) {
//				HSSFCell cell = hssfRow2.createCell(i);
//				if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
//					cell.setCellStyle(style2);
//					cell.setCellValue(Double.valueOf(row[i]));
//				} else if (NumberUtil.isPInteger(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
//					cell.setCellStyle(style2);
//					cell.setCellValue(Integer.valueOf(row[i]));
//				} else {
//					cell.setCellStyle(style3);
//					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//					cell.setCellValue(row[i]);
//				}
//			}
//			rowIndex2++;
//		}
//		return FileUtil.writeHSSFWorkbook(workbook, filePathAndName);
//	}
//
//	/**
//	 * 创建excel 样式2
//	 *
//	 * @param sheetTitle
//	 * @param headers
//	 * @param rows
//	 * @param filePathAndName
//	 * @return
//	 * @throws IOException
//	 */
//	public static String createExcel2(String sheetTitle, String[] headers, List<String[]> rows, String filePathAndName) throws IOException {
//		// 声明一个工作薄
//		HSSFWorkbook workbook = new HSSFWorkbook();
//		// 生成一个表格
//		HSSFSheet sheet = workbook.createSheet(sheetTitle);
//		// 设置表格默认列宽15个字节
//		sheet.setDefaultColumnWidth(28);
//		sheet.setDefaultRowHeight((short) 10);
//		// 生成表头样式
//		HSSFCellStyle style = workbook.createCellStyle();
//		style.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);// 设置这些样式--表头
//		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
//		style.setWrapText(true);
//		// 生成一个字体
//		HSSFFont font = workbook.createFont();
//		font.setColor(HSSFColor.BLACK.index);
//		font.setFontHeightInPoints((short) 13);
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		// 把字体应用到当前的样式
//		style.setFont(font);
//
//		// 生成并设置另一个样式
//		HSSFCellStyle style2 = workbook.createCellStyle();
//		style2.setFillForegroundColor(HSSFColor.WHITE.index);
//		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font2 = workbook.createFont();
//		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		// 把字体应用到当前的样式
//		style2.setFont(font2);
//
//		HSSFCellStyle style3 = workbook.createCellStyle();
//		style3.setFillForegroundColor(HSSFColor.WHITE.index);
//		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		// 生成另一个字体
//		HSSFFont font3 = workbook.createFont();
//		font3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//		// 把字体应用到当前的样式
//		style3.setFont(font3);
//		HSSFDataFormat format = workbook.createDataFormat();
//		style3.setDataFormat(format.getFormat("@"));
//
//		// 生成表头内容
//		HSSFRow hssfRow = sheet.createRow(0);
//		for (int i = 0; i < headers.length; i++) {
//			HSSFCell cell = hssfRow.createCell(i);
//			cell.setCellStyle(style);
//			cell.setCellValue(headers[i]);
//		}
//
//		// 生成表头以外的内容
//		int rowIndex = 1;
//		for (String[] row : rows) {
//			hssfRow = sheet.createRow(rowIndex);
//			for (int i = 0; i < row.length; i++) {
//				HSSFCell cell = hssfRow.createCell(i);
//				if (NumberUtil.isDecimal(row[i]) && row[i].indexOf(".") > 0) {
//					cell.setCellStyle(style2);
//					cell.setCellValue(Double.valueOf(row[i]));
//				} else if (NumberUtil.isNumberic(row[i]) && row[i].length() < 10) {// 32位机器,整型最大4294967296
//					cell.setCellStyle(style2);
//					cell.setCellValue(Integer.valueOf(row[i]));
//				} else {
//					cell.setCellStyle(style3);
//					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//					cell.setCellValue(row[i]);
//				}
//			}
//			rowIndex++;
//		}
//		return FileUtil.writeHSSFWorkbook(workbook, filePathAndName);
//	}
}
