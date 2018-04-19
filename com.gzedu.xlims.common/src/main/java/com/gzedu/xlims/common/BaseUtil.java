/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * 来源于gzdec-commonlib-1.2.4-beta.jar<br>
 * package com.gzdec.common.util<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 * @since JDK 1.7
 */
public class BaseUtil {

	public static String timesTampString(Object s) {
		return (s == null) || (s.equals("")) ? "" : s.toString().substring(0, 10);
	}

	public static String timesTampToString(Object s) {
		return (s == null) || (s.equals("")) ? "" : s.toString().substring(0, 19);
	}

	public static String toString(String s) {
		return s == null ? "" : s;
	}

	public static String trimString(String s, int length) {
		if (s == null) {
			return "";
		}
		if (s.length() > length) {
			return s.substring(0, length - 3) + "...";
		}
		return s;
	}

	public static String toString(java.sql.Date date) {
		return null == date ? "" : date.toString();
	}

	public static String toShortDate(java.util.Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return null == date ? "" : df.format(date);
	}

	public static String toLongDate(java.util.Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return null == date ? "" : df.format(date);
	}

	public static String toLongDate(String date) {
		if (date != null) {
			return date.substring(0, 19);
		}
		return "";
	}

	public static String toDateMin(java.util.Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return null == date ? "" : df.format(date);
	}

	public static String toHourMin(java.util.Date date) {
		DateFormat df = new SimpleDateFormat("HH:mm");
		return null == date ? "" : df.format(date);
	}

	public static String toString(int i) {
		return i == -1 ? "" : String.valueOf(i);
	}

	public static String toString(long l) {
		return String.valueOf(l);
	}

	public static String toString(double d) {
		return String.valueOf(d);
	}

	public static String toString(Object object) {
		return null == object ? "" : object.toString();
	}

	public static String toString(Object object, int length) {
		if (object == null) {
			return "";
		}
		if (object.toString().trim().length() > length) {
			return object.toString().trim().substring(0, length - 4) + "....";
		}
		return object.toString().trim();
	}

	public static java.sql.Date toDate(String s) {
		if ((null == s) || ("".equals(s))) {
			return null;
		}
		if (s.indexOf(":") < 0) {
			return java.sql.Date.valueOf(s);
		}
		if (s.indexOf(":") != s.lastIndexOf(":")) {
			return new java.sql.Date(Timestamp.valueOf(s).getTime());
		}
		return new java.sql.Date(Timestamp.valueOf(s.concat(":0")).getTime());
	}

	public static java.sql.Date toDate(Object s) {
		return toDate(toString(s));
	}

	public static Timestamp toTimestamp(String time) {
		if (time != null) {
			return new Timestamp(tolong(time));
		}
		return null;
	}

	public static long compareTo(Timestamp orgTime, Timestamp currentTime) {
		if ((orgTime == null) || (currentTime == null)) {
			return -1L;
		}
		return Math.abs(currentTime.getTime() - orgTime.getTime());
	}

	public static java.sql.Date toEndDate(Object s) {
		return getDayEnd(toDate(toString(s)));
	}

	public static java.sql.Date getDayEnd(java.sql.Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(11, 23);
		c.set(12, 59);
		c.set(13, 59);
		return new java.sql.Date(c.getTime().getTime());
	}

	public static java.sql.Date getDayStart(java.sql.Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(11, 0);
		c.set(12, 0);
		c.set(13, 0);
		return new java.sql.Date(c.getTime().getTime());
	}

	public static int toInt(String s) {
		try {
			return (null == s) || ("".equals(s)) ? -1 : Integer.valueOf(s).intValue();
		} catch (NumberFormatException ex) {
		}
		return -1;
	}

	public static Double toDouble(String s) {
		return (null == s) || ("".equals(s)) ? null : Double.valueOf(s);
	}

	public static Long toLong(String s) {
		return (null == s) || ("".equals(s)) ? null : Long.valueOf(s);
	}

	public static double todouble(String s) {
		return (null == s) || ("".equals(s)) ? -1.0D : Double.valueOf(s).doubleValue();
	}

	public static long tolong(String s) {
		return (null == s) || ("".equals(s)) ? -1L : Long.valueOf(s).longValue();
	}

	public static int toInt(Object s) {
		return toInt(toString(s));
	}

	public static Integer toInteger(String s) {
		if ((s == null) || ("".equals(s)) || (s.equals("-1")) || ("null".equalsIgnoreCase(s))) {
			return null;
		}
		return Integer.valueOf(s);
	}

	public static Integer toInteger(Object o) {
		if (o == null) {
			return null;
		}
		return Integer.valueOf(o.toString());
	}

	public static Object toInteger(int i) {
		return i == -1 ? null : new Integer(i);
	}

	public static String getNull() {
		return null;
	}

	public static String getStr(Calendar calValue) {
		return getStr(calValue, "yyyyMMddHHmmss");
	}

	public static String getStr(Calendar calValue, String patten) {
		SimpleDateFormat formatter = new SimpleDateFormat(patten);
		if (calValue == null) {
			return "";
		}
		return formatter.format(calValue.getTime());
	}

	public static java.sql.Date getCurrentDate() {
		Calendar calValue = Calendar.getInstance();
		return new java.sql.Date(calValue.getTime().getTime());
	}

	public static java.sql.Date getAfterDateByYears(int years) {
		Calendar calValue = Calendar.getInstance();
		calValue.add(1, years);
		return new java.sql.Date(calValue.getTime().getTime());
	}

	public static java.sql.Date getAfterDateByDays(int days) {
		Calendar calValue = Calendar.getInstance();
		calValue.add(5, days);
		return new java.sql.Date(calValue.getTime().getTime());
	}

	public static String formatStr(int len, int number) {
		String num = String.valueOf(number);
		String space30 = "000000000000000000000000000000";
		if (num.length() < len) {
			return space30.substring(30 - len + num.length()) + num;
		}
		return num.substring(num.length() - len);
	}

	public static String formatStr(int len, String num) {
		String space30 = "000000000000000000000000000000";
		if (num.length() < len) {
			return space30.substring(30 - len + num.length()) + num;
		}
		return num.substring(num.length() - len);
	}

	public static String getRandomStr(int len) {
		return formatStr(len, toString(Math.round(Math.pow(10.0D, len) * Math.random())));
	}

	public static java.sql.Date[] getBetweenDate(int year, int weeksOfYear) {
		Calendar c = Calendar.getInstance();
		if (year <= 0) {
			year = c.get(1);
		}
		if (weeksOfYear <= 0) {
			weeksOfYear = c.get(3) - 1;
		}
		c.set(1, year);
		java.sql.Date[] betweens = new java.sql.Date[2];

		c.set(7, 1);
		c.set(3, weeksOfYear);
		betweens[0] = toDate(new java.sql.Date(c.getTime().getTime()).toString());

		c.set(7, 7);
		c.set(3, weeksOfYear);
		betweens[1] = toEndDate(new java.sql.Date(c.getTime().getTime()).toString());

		return betweens;
	}

	public static int getSeconds(java.sql.Date startdate, java.sql.Date enddate) {
		long time = enddate.getTime() - startdate.getTime();
		int totalS = new Long(time / 1000L).intValue();
		return totalS;
	}

	public static int getCurrentYear() {
		Calendar c = Calendar.getInstance();
		return c.get(1);
	}

	public static Vector groupByItems(Vector list, int itemPerGroup) {
		int listLen = list.size();
		if (itemPerGroup >= listLen) {
			itemPerGroup = listLen;
		}
		if (itemPerGroup <= 0) {
			itemPerGroup = 1;
		}
		int groupCounts = listLen / itemPerGroup;
		if (listLen % itemPerGroup > 0) {
			groupCounts++;
		}
		return group(list, itemPerGroup, groupCounts);
	}

	private static Vector group(Vector list, int itemPerGroup, int groups) {
		Vector newVector = new Vector();
		Vector childV = new Vector();
		for (int j = 0; j < groups; j += itemPerGroup) {
			for (int i = 0; i < itemPerGroup; i++) {
				int count = i * (j + 1);
				if (count >= list.size()) {
					break;
				}
				childV.add(i, list.elementAt(count));
			}
			newVector.add(childV);
			childV = new Vector();
		}
		return newVector;
	}

	public static Vector groupByGroups(Vector list, int groupCounts) {
		int listLen = list.size();
		if (groupCounts >= listLen) {
			groupCounts = listLen;
		}
		if (groupCounts <= 0) {
			groupCounts = 1;
		}
		int itemPerGroup = listLen / groupCounts;
		if (listLen % groupCounts > 0) {
			itemPerGroup++;
		}
		return group(list, itemPerGroup, groupCounts);
	}

	public static Enumeration splitString(String s, String delim) {
		Vector vTokens = new Vector();
		int currpos = 0;
		for (int delimpos = s.indexOf(delim, currpos); delimpos != -1; delimpos = s.indexOf(delim, currpos)) {
			String ss = s.substring(currpos, delimpos);
			vTokens.addElement(ss);
			currpos = delimpos + delim.length();
		}
		vTokens.addElement(s.substring(currpos));
		return vTokens.elements();
	}

	public static String paddingToEight(String s) {
		String space8 = "        ";

		int len = s.getBytes().length % 8;
		if (len == 0) {
			return s;
		}
		return s + "        ".substring(len);
	}

	public static String getTime(java.sql.Date startdate, java.sql.Date enddate) {
		long time = enddate.getTime() - startdate.getTime();
		int totalM = new Long(time / 60000L).intValue();

		StringBuffer s = new StringBuffer();
		return totalM + "分钟";
	}

	public static String[] split(String source, String delim) {
		if (source == null) {
			String[] wordLists = new String[1];
			wordLists[0] = source;
			return wordLists;
		}
		if (delim == null) {
			delim = ",";
		}
		StringTokenizer st = new StringTokenizer(source, delim);
		int total = st.countTokens();
		String[] wordLists = new String[total];
		for (int i = 0; i < total; i++) {
			wordLists[i] = st.nextToken();
		}
		return wordLists;
	}

}
