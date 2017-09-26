package com.zh.metermanagementcw.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gbh on 16/12/3.
 */

public class TimeUtils {

    public static String yyyy_MM_ddHHmmss = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentTimeyyyyMMdd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }


    public static String getCurrentyyyyMMdd() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }
    public static String getCurrentTimeyyyyMM() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        return simpleDateFormat.format(new Date());
    }
    public static String getCurrentyyyyMM() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentTimeRq() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentTime_yyyyMMdd_HHmm() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 将 Date 转化为字符串
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateConvertString(Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static boolean compareTime(String time1, String time2, String dateFormat){
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try {
            Date dt1 = df.parse(time1);//将字符串转换为date类型
            Date dt2 = df.parse(time2);
            if(dt1.getTime()>= dt2.getTime())//比较时间大小,dt1小于dt2
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 两个 时间相差多少天  time1-time2
     * @param time1             减数
     * @param time2             被减数
     * @param dateFormat        //创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
     * @return
     */
    public static long timeDifference(String time1, String time2, String dateFormat){

        long days = 1;
        SimpleDateFormat df = new SimpleDateFormat(dateFormat); //创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try
        {
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);

            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别

            days = diff / (1000 * 60 * 60 * 24);

            //LogUtils.i("time1:" + time1 + "------------ time2:" + time2);

            // long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            // long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            // System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
        }catch (Exception e) {
            LogUtils.i("e.getMessage():" + e.getMessage());
        }

        return days;
    }

    public static int getMonthSpace(String date1, String date2)
            throws ParseException {

        int result = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));

        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

//        Date d = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(d);
//        System.out.println(c.get(Calendar.MONTH));
//        一月对应0
//        十二月对应11

        return result == 0 ? 1 : Math.abs(result);

    }


    /**
     *  获取两个日期相差的月数
     * @param time1    较小的日期
     * @param time2    较大的日期
     * @return  如果d1>d2返回 月数差 否则返回0
     */
    public static int getMonthDiff(String time1, String time2, String dateFormat) {

        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = df.parse(time1);
            d2 = df.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(d1);
        c2.setTime(d2);

        if(c2.getTimeInMillis() < c1.getTimeInMillis())
            return 0;

        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);

        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);

        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);

        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year2 - year1;

        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if(month2 < month1 || month2 == month1 && day2 < day1)
            yearInterval --;

        // 获取月数差值
        int monthInterval =  (month2 + 12) - month1  ;
        if(day2 < day1)
            monthInterval --;

        monthInterval %= 12;

        return yearInterval * 12 + monthInterval;
    }

}
