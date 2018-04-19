/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */
public class DateUtils {
	/**
	 * 获取当前时间
	 * 
	 * @return Date
	 * 
	 */
	public static Date getDate() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return date;
	}
	
	/**
	 * 获取当前时间
	 * 
	 * @return Date
	 * 
	 */
	public static String getStrDate() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return ObjectUtils.toString(date.getTime());
	}

	/**
	 * 获取当前时间
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getNowTime() {
		Calendar calendar = Calendar.getInstance();
		Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
		return timestamp;
	}

	/**
	 * Date转Timestamp没有固定格式
	 * 
	 * @param date
	 * @return Timestamp
	 */
	public static Timestamp getNowTime(Date date) {
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp;
	}

	/**
	 * Date转Timestamp，返回固定格式
	 * 
	 * @param date
	 *            yyyy-MM-dd
	 * @return Timestamp
	 */
	public static Timestamp getNowTime(String date) throws Exception {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp timestamp = new Timestamp(sd.parse(date + " 00:00:00").getTime());
		return timestamp;
	}

	/**
	 * 获取当前时间，返回固定格式的字符串
	 * 
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String getTodayTime() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return sd.format(date);
	}
	
	/**
	 * 获取当前时间，返回固定格式的字符串
	 * 
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String getTodayStr(String str) {
		SimpleDateFormat sd = new SimpleDateFormat(str);
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return sd.format(date);
	}


	/**
	 * Date转String，返回固定格式的字符串
	 * 
	 * @param date
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringToDate(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(date);
	}

	/**
	 * String转Date，返回固定格式的字符串
	 * 
	 * @param dateStr
	 * @return Date yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 */
	public static Date getDateToString(String dateStr) throws ParseException {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.parse(dateStr);
	}

	/**
	 * 格式化：获取当前年月日 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeYMD(Date date) {
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			return sd.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 
	 * @param date
	 * @return yyyy-MM-dd
	 * @throws ParseException
	 */
	public static Date getYMDToString(String date) throws ParseException {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		return sd.parse(date);
	}

	/**
	 * 格式化：获取当前年月日 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeYM(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		return sd.format(date);
	}

	/**
	 * 格式化：获取当前年月日
	 * 
	 * @param String
	 * @return date yyyy-MM-dd HH:mm
	 * @throws ParseException
	 */
	public static Date getTime(String date) throws ParseException {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sd.parse(date);
	}

	/**
	 * 格式：获取当前年月日 yyyy-MM-dd
	 * 
	 * @return string
	 */
	public static String getToday() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return sd.format(date);
	}

	/**
	 * 得到本周周一
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getMondayOfThisWeek() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		return sd.format(getMondayOfThisWeek(new Date()));
	}

	/**
	 * 得到本周周日
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getSundayOfThisWeek() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		return sd.format(getSundayOfThisWeek(new Date()));
	}

	/**
	 * 得到当前时间周内的周一
	 *
	 * @return
	 */
	public static Date getMondayOfThisWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		return c.getTime();
	}

	/**
	 * 得到当前时间周内的周日
	 *
	 * @return
	 */
	public static Date getSundayOfThisWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 7);
		return c.getTime();
	}

	public static boolean isValidDateFormat(String str, String formatStr) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			// 设置lenient为false.
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 
	 * @param dateStr
	 * @return yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 */
	public static Date getStrToDate(String dateStr) throws ParseException {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.parse(dateStr);
	}

	public static void main(String[] args) {
		System.out.println(getStrDate());
		System.out.println(getStrDate());
	}

}
