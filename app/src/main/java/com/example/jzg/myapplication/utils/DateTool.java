package com.example.jzg.myapplication.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {

	private static String mYear;
	private static String mMonth;
	private static String mDay;
	private static String mWay;
	private static String mHour;
	private static String mMinute;
	/***
	 * yyyy-MM-dd HH:mm:ss
	 *-
	private static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";


	/**
	 * 获取时间2014-01-12
	 * 
	 * @return
	 */
	public static String StringDateTime() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd kk:mm:ss", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间2014/01/12
	 * 
	 * @return
	 */
	public static String StringDate() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间14:30
	 * 
	 * @return
	 */
	public static String StringTime() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("kk:mm", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间年
	 * 
	 * @return
	 */
	public static String StringTimeYear() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("yyyy", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间月
	 * 
	 * @return
	 */
	public static String StringTimeMonth() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("MM", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间日
	 * 
	 * @return
	 */
	public static String StringTimeDay() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("dd", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间小时14
	 * 
	 * @return
	 */
	public static String StringTimeHour() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("kk", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间分钟30
	 * 
	 * @return
	 */
	public static String StringTimeMin() {
		long sysTime = System.currentTimeMillis();
		CharSequence sysTimeStr = DateFormat.format("mm", sysTime);
		return sysTimeStr.toString();
	}

	/**
	 * 获取时间1月30日
	 * 
	 * @return
	 */
	public static String StringData() {
		final Calendar c = Calendar.getInstance();
		// c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		mMinute = String.valueOf(c.get(Calendar.MINUTE));

		return mMonth + "月" + mDay + "日";
	}

	/**
	 * 获取时间-->星期日
	 * 
	 * @return
	 */
	public static String StringWeek() {
		final Calendar c = Calendar.getInstance();
		// c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		mMinute = String.valueOf(c.get(Calendar.MINUTE));

		if ("1".equals(mWay)) {
			mWay = "天";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		// return mYear + "年" + mMonth + "月" + mDay+"日"+"/星期"+mWay;
		return "星期" + mWay;
	}

	/***
	 * 两个日期相差多少秒
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getTimeDelta(Date date1, Date date2) {
		long timeDelta = (date1.getTime() - date2.getTime()) / 1000;// 单位是秒
		int secondsDelta = timeDelta > 0 ? (int) timeDelta : (int) Math
				.abs(timeDelta);
		return secondsDelta;
	}

	/***
	 * 两个日期相差多少秒
	 * 
	 * @param dateStr1
	 *            :yyyy-MM-dd HH:mm:ss
	 * @param dateStr2
	 *            :yyyy-MM-dd HH:mm:ss
	 */
	public static int getTimeDelta(String dateStr1, String dateStr2) {
		Date date1 = parseDateByPattern(dateStr1, "yyyy-MM-dd HH:mm:ss");
		Date date2 = parseDateByPattern(dateStr2, "yyyy-MM-dd HH:mm:ss");
		return getTimeDelta(date1, date2);
	}

	public static Date parseDateByPattern(String dateStr, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}
	
	/**
	 * 
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String DateFormat(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		String day = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return day;
	}
	/**
	 * 
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String YearandMonthFormat(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		String day = new SimpleDateFormat("yyyy-MM").format(c.getTime());
		return day;
	}
	
	

}
