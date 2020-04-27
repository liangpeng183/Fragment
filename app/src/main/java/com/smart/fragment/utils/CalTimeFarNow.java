package com.smart.fragment.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public  class CalTimeFarNow {

    //  传进来的时间格式
    private static String formatType = "yyyy-MM-dd HH:mm:ss";

    private static final long MINUTE_SECONDS = 60; //1分钟  60秒
    private static final long HOUR_SECONDS = MINUTE_SECONDS*60;  // 小时
    private static final long DAY_SECONDS = HOUR_SECONDS*24;   // 一天
    private static final long YEAR_SECONDS = DAY_SECONDS*365;   // 一年

    /*
    *  根据服务器获取到的时间戳 和当前系统时间
    *  计算 多少多少时间前  （）
    * */
     public static String calTime(String time){
         //Log.i("CalTime","发布时间："+ time);
         Date pubTime = null;  //当前时间
         try {
             pubTime = stringToDate(time,formatType);
             //Log.i("启动页","发布时间："+pubTime);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         // 获取系统现在时间
         Date now = new Date(System.currentTimeMillis());
         //Log.i("启动页","当前时间："+now);
         //Log.i("启动页","时间："+String.format("%s前", testPassedTime(now.getTime(), pubTime.getTime())));
         return  String.format("%s前", testPassedTime(now.getTime(), pubTime.getTime()));
     }

     // 计算时间差
    public static String testPassedTime(long nowMilliseconds, long oldMilliseconds) {
        long passed = (nowMilliseconds-oldMilliseconds) /1000;//转为秒
        if (passed > YEAR_SECONDS) {
            return passed/YEAR_SECONDS+"年";
        } else if (passed > DAY_SECONDS) {
            return passed/DAY_SECONDS+"天";
        } else if (passed > HOUR_SECONDS) {
            return passed/HOUR_SECONDS+"小时";
        } else if (passed > MINUTE_SECONDS) {
            return passed/MINUTE_SECONDS+"分钟";
        } else {
            return passed+"秒";
        }
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }




}
