package com.example.jzg.myapplication.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/5/23 15:41
 * @desc:
 */
public class DateTimeUtils {
    private static final String TAG = "DateTimeUtils";

    private DateTimeUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";
    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static final String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.SSS
     */
    private static final String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 中文简写 如：2010年12月01日
     */
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd";
    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";



    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDD_CN = "yyyy年MM月dd日";
    public static final String YYYYMM_CN = "yyyy年MM";
    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String YYYYMM = "yyyy-MM";
    public static final String HHMM = "HH:mm";

    public static final List<String> FORMATS = new ArrayList<>();
    static{
        FORMATS.add(YYYYMMDDHHMMSS);
        FORMATS.add(YYYYMMDDHHMM);
        FORMATS.add(YYYYMMDD_CN);
        FORMATS.add(YYYYMM_CN);
        FORMATS.add(YYYYMMDD);
        FORMATS.add(YYYYMM);
        FORMATS.add(HHMM);
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
    public static String formatMillsStr(String time, String format){
        LogUtil.e(TAG,"time:"+time);
        if(TextUtils.isEmpty(time))
           return "";
        if(format.length()>19){
            time.substring(0,19);
        }

        time = time.replace("T"," ");
        if(!time.startsWith("2")){
            return "";
        }

        if(TextUtils.isEmpty(format) || !FORMATS.contains(format)){
            throw new IllegalArgumentException("'"+format+"'不是合法的格式");
        }
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date==null)
            return "";
        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        String result = sdf2.format(date);
        return result;
    }

    /***
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatMillsStrM(String time, String format){
//        LogUtil.e(TAG,"time:"+time);
        if(TextUtils.isEmpty(time))
            return "";
        if(format.length()>19){
            time.substring(0,19);
        }

        time = time.replace("T"," ");
        if(time.startsWith("0")){
            return "";
        }

        if(TextUtils.isEmpty(format) || !FORMATS.contains(format)){
            throw new IllegalArgumentException("'"+format+"'不是合法的格式");
        }
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
        if(date==null)
            return "";
        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        String result = sdf2.format(date);
        return result;
    }

    /**
     * 获得默认的 date pattern
     */
    private static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 根据预设格式返回当前日期
     *
     * @return
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     *
     * @param format
     * @return
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }

    /**
     * 使用预设格式格式化日期
     *
     * @param date
     * @return
     */
    private static String format(Date date) {
        return format(date, getDatePattern());
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    private static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */
    private static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    private static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL, Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取日期年份
     *
     * @param date 日期
     * @return
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户给的时间戳获取预设格式的时间
     * @param date 时间戳
     * @param pattern 预设时间格式
     * @return
     */
    public static String getDate(String date, String pattern){
        Date dates=new Date();
        dates.setTime(Long.parseLong(date));
        SimpleDateFormat format= new SimpleDateFormat(pattern);
        return format.format(dates);
    }

    /**
     * 格式化时间 判断一个日期 是否为 今天、昨天
     * @param time
     * @return
     */
    public static String formatDateTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(time==null ||"".equals(time)){
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();	//今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set( Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();	//昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set( Calendar.HOUR_OF_DAY, 0);
        yesterday.set( Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if(current.after(today)){
            return "今天 "+time.split(" ")[1];
        }else if(current.before(today) && current.after(yesterday)){

            return "昨天 "+time.split(" ")[1];
        }else{
//            int index = time.indexOf("-")+1;
//            return time.substring(index, time.length());
            int index = time.indexOf(" ");
            return time.substring(0, index);
        }
    }


    public static boolean laterThanNow(int year,int month,int day){
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        if(year<nowYear){
            return false;
        }else if(year==nowYear){
            if(month<nowMonth){
                return false;
            }else if(month==nowMonth){
                if(day<=nowDay){
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean beforeThanNow(int year,int month,int day){
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        if(year>nowYear){
            return false;
        }else if(year==nowYear){
            if(month>nowMonth){
                return false;
            }else if(month==nowMonth){
                if(day>nowDay){
                    return false;
                }
            }
        }
        return true;
    }

}
