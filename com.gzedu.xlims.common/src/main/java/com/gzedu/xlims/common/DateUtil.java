/**
 * 
 */
package com.gzedu.xlims.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author Administrator
 * 
 */
public class DateUtil {

	public final static DateFormat YYYY_MM_DD_MM_HH_SS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public final static DateFormat YYYY_MM_DD = new SimpleDateFormat(
			"yyyy-MM-dd");

	public final static DateFormat YYYYMMDDMMHHSSSSS = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	public final static DateFormat YYYYMMDDHHMMSS = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 时间转换为yyyy-MM-dd HH:mm:ss格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return YYYY_MM_DD_MM_HH_SS.format(date);
	}
	
	/**
	 * 根据格式转换日期为字符串
	 * @param date
	 * @param partten
	 * @return
	 */
	public static String dateToString(Date date, String partten) {
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		DateFormat format = new SimpleDateFormat(partten);
		return format.format(time.getTime());
	}

	public static Date strToDate(String dateString) {
		Date date = null;
		try {
			date = YYYY_MM_DD_MM_HH_SS.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date strToYYMMDDDate(String dateString) {
		Date date = null;
		try {
			date = YYYY_MM_DD.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 计算两个时间之间相差的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long diffDays(Date startDate, Date endDate) {
		long days = 0;
		long start = startDate.getTime();
		long end = endDate.getTime();
		// 一天的毫秒数1000 * 60 * 60 * 24=86400000
		days = (end - start) / 86400000;
		return days;
	}

	/**
	 * 日期加上月数的时间
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date dateAddMonth(Date date, int month) {
		return add(date, Calendar.MONTH, month);
	}

	/**
	 * 日期加上天数的时间
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date dateAddDay(Date date, int day) {
		return add(date, Calendar.DAY_OF_YEAR, day);
	}

	/**
	 * 日期加上年数的时间
	 * 
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date dateAddYear(Date date, int year) {
		return add(date, Calendar.YEAR, year);
	}

	/**
	 * 计算剩余时间 (多少天多少时多少分)
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	public static String remainDateToString(Date startDate, Date endDate) {
		StringBuilder result = new StringBuilder();
		if (endDate == null) {
			return "过期";
		}
		long times = endDate.getTime() - startDate.getTime();
		if (times < -1) {
			result.append("过期");
		} else {
			long temp = 1000 * 60 * 60 * 24;
			// 天数
			long d = times / temp;

			// 小时数
			times %= temp;
			temp /= 24;
			long m = times / temp;
			// 分钟数
			times %= temp;
			temp /= 60;
			long s = times / temp;

			result.append(d);
			result.append("天");
			result.append(m);
			result.append("小时");
			result.append(s);
			result.append("分");
		}
		return result.toString();
	}

	private static Date add(Date date, int type, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, value);
		return calendar.getTime();
	}

	/**
	 * @MethodName: getLinkUrl
	 * @Param: DateUtil flag ： true 转换 false 不转换
	 * @Author: gang.lv
	 * @Date: 2013-5-8 下午02:52:44
	 * @Return:
	 * @Descb:
	 * @Throws:
	 */
	public static String getLinkUrl(boolean flag, String content, String id) {
		if (flag) {
			content = "<a href='finance.do?id=" + id + "'>" + content + "</a>";
		}
		return content;
	}

	/**
	 * 时间转换为时间戳
	 * 
	 * @param format
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getTimeCur(String format, String date)
			throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.parse(sf.format(date)).getTime();
	}

	/**
	 * 时间转换为时间戳
	 * 
	 * @param format
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getTimeCur(String format, Date date)
			throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.parse(sf.format(date)).getTime();
	}

	/**
	 * 将时间戳转为字符串
	 * 
	 * @param cc_time
	 * @return
	 */
	public static String getStrTime(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/**
	 * 时间转换为时间戳
	 * 
	 * @param format
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getTimeCurS(String format, Date date)
			throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.parse(sf.format(date)).getTime() + "";
	}
	
	/**
	 * 判断是否是当月
	 * @param date
	 * @return
	 */
	public static boolean isCurrMonth(Date date) {
		Calendar curr = Calendar.getInstance();
		Calendar time = Calendar.getInstance();
		
		time.setTime(date);
		if(curr.get(Calendar.YEAR) == time.get(Calendar.YEAR)
				&& curr.get(Calendar.MONTH) == time.get(Calendar.MONTH)) {
			return true;
		} else return false;
	}
	
	/**
	 * 和当前时间比较大小
	 * @param date
	 * @return
	 */
	public static int compareDate(Date date) {
		Calendar curr = Calendar.getInstance();
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		return curr.compareTo(time);
	}
	
	/**
	 * 计算时间段内相差的天数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long bettweenDateByTime(Date startTime, Date endTime) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(startTime);
		end.setTime(endTime);
		long day = (end.getTimeInMillis() - start.getTimeInMillis())/(24 * 60  * 60 * 1000);
		return day;
	}
	
	/**
	 * 计算时间和系统时间相差的月数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int bettweenMonthBySysTime(Date startTime) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(startTime);
		
		int differMonth = 0;
		if(start.get(Calendar.YEAR) == end.get(Calendar.YEAR)) {
			differMonth = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		} else {
			int differYear = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
			differMonth = differYear * 12 + end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		}
		return differMonth;
	}
	
	/**
	 * 计算时间段内相差的月数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int bettweenMonthByTime(Date startTime, Date endTime) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(startTime);
		end.setTime(endTime);
		
		int differMonth = 0;
		if(start.get(Calendar.YEAR) == end.get(Calendar.YEAR)) {
			differMonth = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		} else {
			int differYear = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
			differMonth = differYear * 12 + end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		}
		
		Calendar temp = Calendar.getInstance();
		temp.setTime(startTime);
		temp.add(Calendar.MONTH, differMonth);
		if(temp.compareTo(end) > 0 && differMonth != 0) {
			differMonth = differMonth - 1;
		}
		return differMonth;
	}
	/**   
	 * @MethodName: getMonthFirstDay  
	 * @Param: UtilDate  
	 * @Author: home
	 * @Date: 2013-3-22 下午07:14:34
	 * @Return:    
	 * @Descb: 获取当月的第一天
	 * @Throws:
	*/
	public static String getMonthFirstDay() {
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		String firstday = new SimpleDateFormat("yyyy-MM-dd")
				.format(f.getTime());
		firstday = firstday +" 00:00:00";
		return firstday;

	}

	
	/**   
	 * @MethodName: getMonthLastDay  
	 * @Param: UtilDate  
	 * @Author: home
	 * @Date: 2013-3-22 下午07:14:41
	 * @Return:    
	 * @Descb: 获取当月的最后一天
	 * @Throws:
	*/
	public static String getMonthLastDay() {
		Calendar cal = Calendar.getInstance();
		Calendar l = (Calendar) cal.clone();
		l.clear();
		l.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		l.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		l.set(Calendar.MILLISECOND, -1);
		String lastday = new SimpleDateFormat("yyyy-MM-dd").format(l.getTime());
		lastday = lastday+" 23:59:59";
		return lastday;
	}
	
	public static void main(String[] args) {
		
		System.out.println(compareDate(strToYYMMDDDate("2018-12-01")));
	}
}
