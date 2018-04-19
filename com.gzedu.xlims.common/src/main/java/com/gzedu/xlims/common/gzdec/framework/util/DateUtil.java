package com.gzedu.xlims.common.gzdec.framework.util;

import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 此类用来计算两个日期的相隔时间，相隔时间格式化为需要的形式
 *
 * @author cxg
 *
 */
public class DateUtil {
	// 一天的毫秒数 86400000 = 24*60*60*1000;
	private static final int millisPerDay = 86400000;
	// 一小时的毫秒数 3600000 = 24*60*60*1000;
	private static final int millisPerHour = 3600000;

	public static final long ONE_SECOND = 1000;
	public static final long ONE_MINUTE= ONE_SECOND * 60;
	public static final long ONE_HOUR = ONE_MINUTE * 60;
	public static final long ONE_DAY = ONE_HOUR * 24;
	public static final long ONE_MONTH = ONE_DAY * 30;
	public static final long ONE_YEAR = ONE_DAY * 365;

	/**
	 * 计算时间差 (时间单位,开始时间,结束时间) 调用方法
	 * howLong("h","2007-08-09 10:22:26","2007-08-09 20:21:30") ///9小时56分 返回9小时
	 * */
	public static long howLong(String unit, String time1, String time2)
			throws ParseException {
		// 时间单位(如：不足1天(24小时) 则返回0)，开始时间，结束时间
		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time1);
		Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time2);
		long ltime = date1.getTime() - date2.getTime() < 0 ? date2.getTime()
				- date1.getTime() : date1.getTime() - date2.getTime();
		if (unit.equals("s")) {
			return ltime / 1000;// 返回秒
		} else if (unit.equals("m")) {
			return ltime / 60000;// 返回分钟
		} else if (unit.equals("h")) {
			return ltime / millisPerHour;// 返回小时
		} else if (unit.equals("d")) {
			return ltime / millisPerDay;// 返回天数
		} else {
			return 0;
		}
	}

	/**
	 * 计算系统日期与目标日期的时间差, 检查传入时间是否早于系统时间， 不符合则返回""
	 *
	 * @param tar
	 *            与系统时间对比的目标日期
	 * @return author: cxg 2009-5-24 下午02:06:10
	 */
	public static String getIntervalString(Date tar) {
		return getIntervalString(new Date(), tar, true);
	}

	/**
	 * 计算系统日期与目标日期的相隔天数
	 *
	 * @param tar
	 *            与系统时间对比的目标日期
	 * @return 相隔天数, 参数无效返回-1 author: cxg 2009-5-24 下午02:16:54
	 */
	public static int getIntervalDay(Date tar) {
		int ret = -1;
		Calendar calNow = Calendar.getInstance();
		if (null != tar && tar.before(calNow.getTime())) {// 参数有效
			// 获得指定时间的Calendar
			Calendar calTar = Calendar.getInstance();
			calTar.setTime(tar);

			long millisNow = calNow.getTimeInMillis();
			long millisTar = tar.getTime();

			// 指定时间小于系统时间才处理， 否则返回空字符串
			if (millisTar < millisNow) {
				// 86400000 = 24*60*60*1000;
				ret = (int) ((millisNow - millisTar) / (millisPerDay));
			}
		}
		return ret;
	}

	/**
	 * 计算系统日期与目标日期的相隔天数
	 *
	 * @param tar
	 *            与系统时间对比的目标日期
	 * @return 相隔天数, 参数无效返回-1 author: cxg 2009-5-24 下午02:16:54
	 */
	public static int getIntervalDayAfter(Date tar) {
		int ret = -1;
		Calendar calNow = Calendar.getInstance();
		if (null != tar && tar.after(calNow.getTime())) {// 参数有效
			// 获得指定时间的Calendar
			Calendar calTar = Calendar.getInstance();
			calTar.setTime(tar);

			long millisNow = calNow.getTimeInMillis();
			long millisTar = tar.getTime();

			// 指定时间小于系统时间才处理， 否则返回空字符串
			if (millisTar > millisNow) {
				// 86400000 = 24*60*60*1000;
				ret = (int) ((millisTar - millisNow) / (millisPerDay));
			}
		}
		return ret;
	}

	public static int getIntervalDays(Date ymd) {
		int ret = -1;
		if (ymd != null) { // 参数有效
			long currentTimeMillis = System.currentTimeMillis();
			long millisYmd = ymd.getTime();
			long diff = millisYmd - currentTimeMillis;

			double d = Math.ceil(diff * 1.0 / millisPerDay);

			ret = new Double(d).intValue();
		}

		return ret;
	}

	/**
	 * @param tar
	 *            与系统时间对比的目标日期(字符串格式)
	 * @param formats
	 *            解析日期的格式
	 * @return author: cxg 2009-5-24 下午02:20:11
	 */
	public static String getIntervalString(String tar, String[] formats) {
		Date dTar = null;
		try {
			dTar = DateUtils.parseDate(tar, formats);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		return getIntervalString(dTar);
	}

	/**
	 * 获得指定年月的天数
	 *
	 * @param year
	 * @param month
	 * @return author: cxg 2009-5-24 下午03:48:17
	 */
	private static int getDaysOfMonth(int year, int month) {
		int day = 0;
		switch (month) {
			// 大月
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = 31;
				break;
			// 小月
			case 4:
			case 6:
			case 9:
			case 11:
				day = 30;
				break;
			case 2:
				if ((0 == year % 400) || (0 == year % 4 && 0 != year % 100)) {// 闰年
					day = 29;
				} else {
					day = 28;
				}
				break;
		}
		return day;
	}

	/**
	 * 计算两个日期的差， 返回如"1年2月14天11.5小时"的字符串， 异常情况返回""
	 *
	 * @param after
	 * @param before
	 * @return author: cxg 2009-5-24 下午03:09:18
	 */
	private static String getIntervalString(Date after, Date before) {
		StringBuffer ret = new StringBuffer();
		Calendar calAfter = Calendar.getInstance();
		calAfter.setTime(after);

		// 获得指定时间的Calendar
		Calendar calBefore = Calendar.getInstance();
		calBefore.setTime(before);

		float hour = 0F;
		int day = 0;
		int month = 0;
		int year = 0;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);// 保留1位小数

		// 计算小时
		hour = ((calAfter.getTimeInMillis() - calBefore.getTimeInMillis()) % millisPerDay)
				/ (float) millisPerHour;

		// 计算天
		day = calAfter.get(Calendar.DAY_OF_MONTH)
				- calBefore.get(Calendar.DAY_OF_MONTH);
		if (day < 0) {
			day = day
					+ getDaysOfMonth(calAfter.get(Calendar.YEAR),
					calAfter.get(Calendar.MONTH));
			month--;
		}

		// 计算月
		month = month + calAfter.get(Calendar.MONTH)
				- calBefore.get(Calendar.MONTH);
		if (month < 0) {
			month = month + 12;
			year--;
		}

		// 计算年
		year = year + calAfter.get(Calendar.YEAR)
				- calBefore.get(Calendar.YEAR);

		if (year > 0) {
			ret.append(year).append("年");
		}
		if (month > 0) {
			ret.append(month).append("月");
		}
		if (day > 0) {
			ret.append(day).append("天");
		}
		if (hour > 0F) {
			ret.append(nf.format(hour)).append("小时");
		}
		return ret.toString();
	}

	/**
	 * 计算两个日期的差， 返回如"1年2月14天"的字符串， 异常情况返回""
	 *
	 * @param after
	 * @param before
	 * @param check
	 *            是否检查参数 after.before(before), 检查不符合返回""
	 * @return author: cxg 2009-5-24 下午03:09:18
	 */
	public static String getIntervalString(Date after, Date before,
										   boolean check) {
		if (null != after && null != before) {// 参数有效
			if (after.before(before)) {// 期望tar1日期更大
				if (check) {// 强制检查则返""
					return "";
				} else {// 调换
					Date tmpDate = after;
					after = before;
					before = tmpDate;
				}
			}

			return getIntervalString(after, before);
		} else {
			return "";
		}
	}

	/**
	 * 把String类型的日期转换成java.util.Date型的日期
	 *
	 * @param date
	 *            日期字符串
	 * @return 日期类型实例(java.util.Date类型)
	 * @throws ParseException
	 */
	public static Date parseDate(String date) {
		final String[] patterns = { "yyyy-MM-dd", "yyyy/MM/dd",
				"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.S",
				"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S",
				"yyyy-MM-dd HH:mm", "yyyyMMdd" };
		Date dt = null;
		try {
			dt = DateUtils.parseDate(date, patterns);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return dt;
	}

	/**
	 * 把String类型的日期转换成java.sql.Timestamp型的日期
	 *
	 * @param date
	 *            日期字符串
	 * @return 日期类型实例(java.sql.Timestamp类型)
	 * @throws ParseException
	 */
	public static Timestamp parseTimestamp(String date) throws ParseException {
		final String[] patterns = { "yyyy-MM-dd", "yyyy/MM/dd",
				"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.S",
				"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S" };
		return new Timestamp(DateUtils.parseDate(
				date, patterns).getTime());
	}

	/**
	 * 把Timestamp类型的日期转换成java.sql.Date型的日期
	 *
	 * @param timestamp
	 *            传入日期(java.sql.Date类型)
	 * @return 日期类型实例(java.sql.Date类型)
	 */
	public static Date parseDate(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		return new Date(timestamp.getTime());
	}

	/**
	 * 把java.sql.Timestamp型的日期按指定格式转换成String类型
	 *
	 * @param timestamp
	 *            传入日期(java.sql.Timestamp类型)
	 * @param format
	 *            转换格式
	 * @return 字符串格式的日期值
	 */
	public static String toString(Timestamp timestamp, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(timestamp);
	}

	/**
	 * 把java.util.Date型的日期按指定格式转换成String类型
	 *
	 * @param date
	 *            传入日期(java.util.Date类型)
	 * @param format
	 *            转换格式
	 * @return 字符串格式的日期值
	 */
	public static String toString(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * 取得当前日期
	 *
	 * @return 表示当前日期的java.sql.Date类型对象
	 */
	public static Date getDate() {
		return new Date(new Date().getTime());
	}

	/**
	 * 取得7天后日期
	 *
	 * @return 表示当前日期的String类型对象
	 */
	public static Date getBefore7Date() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(c.DAY_OF_YEAR, 7);
		return c.getTime();
	}

	/**
	 * 取得当前日期和时间
	 *
	 * @return 表示当前日期和时间的java.sql.Timestamp类型对象
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 比较日期，第一个日期是否在第二个日期前
	 *
	 * @param arg1
	 * @param arg2
	 */
	public static boolean before(Date arg1, Date arg2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(arg1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(arg2);
		return cal1.before(cal2);
	}

	/**
	 * 取得当前系统时间
	 *
	 * @return 表示当前系统时间的Timestamp实例
	 * @param format
	 *            取得的系统时间的表示格式，默认为“yyyy-MM-dd HH:mm:ss”
	 */
	public static Timestamp getCurrentTime(String format) {
		if (StringUtils.hasText(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		// 取得当前时间
		SimpleDateFormat formater = new SimpleDateFormat(format);
		String dateString = formater.format(DateUtil.getDate());

		return Timestamp.valueOf(dateString);
	}

	public static Date parseTIMESTAMP(Timestamp t) {
		if(t==null) {
			return null;
		}

		try {
			Date dt1 = new Date(t.getTime());
			return dt1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseDateTime(String datetime) {
		try {
			return parseDate(datetime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
