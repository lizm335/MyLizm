package com.gzedu.xlims.common.gzdec.framework.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * Title: i-framework<br>
 * Description: 字符串操作工具类 继承了apache-commons-lang包中的StringUtils类 增加了时间转换为mills字符串
 * mills字符串转换为时间等相关方法 <br>
 * Copyright: 广州远程教育中心 2010 <br>
 * Create DateTime: 2010-4-19 下午02:14:35 <br>
 * CVS last modify person: lianghuahuang <br>
 * CVS last modify DateTime: 2010-4-19 下午02:14:35 <br>
 * CVS last version: <br>
 * 
 * @author 梁华璜
 */

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	private static final char[] LT_ENCODE = "&lt;".toCharArray();
	private static final char[] GT_ENCODE = "&gt;".toCharArray();
	private static final char[] zeroArray = "0000000000000000000000000000000000000000000000000000000000000000"
			.toCharArray();

	/**
	 * Formats a Date as a fifteen character long String made up of the Date's
	 * padded millisecond value.
	 * 
	 * @return a Date encoded as a String.
	 */
	public static String dateToMillis(Date date) {
		return zeroPadString(Long.toString(date.getTime()), 15);
	}

	/**
	 * Pads the supplied String with 0's to the specified length and returns the
	 * result as a new String. For example, if the initial String is "9999" and
	 * the desired length is 8, the result would be "00009999". This type of
	 * padding is useful for creating numerical values that need to be stored
	 * and sorted as character data. Note: the current implementation of this
	 * method allows for a maximum <tt>length</tt> of 64.
	 * 
	 * @param string
	 *            the original String to pad.
	 * @param length
	 *            the desired length of the new padded String.
	 * @return a new String padded with the required number of 0's.
	 */
	public static String zeroPadString(String string, int length) {
		if (string == null || string.length() > length) {
			return string;
		}
		StringBuilder buf = new StringBuilder(length);
		buf.append(zeroArray, 0, length - string.length()).append(string);
		return buf.toString();
	}

	/**
	 * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
	 * &lt;table&gt;, etc) and converts the '&lt'' and '&gt;' characters to
	 * their HTML escape sequences. It will also replace LF with &lt;br&gt;.
	 * 
	 * @param in
	 *            the text to be converted.
	 * @return the input string with the characters '&lt;' and '&gt;' replaced
	 *         with their HTML escape sequences.
	 */
	public static String escapeHTMLTags(String in) {
		if (in == null) {
			return null;
		}
		char ch;
		int i = 0;
		int last = 0;
		char[] input = in.toCharArray();
		int len = input.length;
		StringBuilder out = new StringBuilder((int) (len * 1.3));
		for (; i < len; i++) {
			ch = input[i];
			if (ch > '>') {
			} else if (ch == '<') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(LT_ENCODE);
			} else if (ch == '>') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(GT_ENCODE);
			} else if (ch == '\n') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append("<br>");
			}
		}
		if (last == 0) {
			return in;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}

	/**
	 * Format mills String to Date
	 * 
	 * @param millsStr
	 * @return Date
	 */
	public static Date stringToDate(String millsStr) {
		Calendar cal = Calendar.getInstance();
		if (StringUtils.contains(millsStr, "-")) {
			millsStr = "-" + StringUtils.substringAfter(millsStr, "-");
		}
		cal.setTimeInMillis(Long.parseLong(millsStr));
		return cal.getTime();
	}

	/**
	 * Format mills String to Calendar
	 * 
	 * @param millsStr
	 * @return Calendar
	 */
	public static Calendar stringToCalendar(String millsStr) {
		Calendar cal = Calendar.getInstance();
		if (StringUtils.contains(millsStr, "-")) {
			millsStr = "-" + StringUtils.substringAfter(millsStr, "-");
		}
		cal.setTimeInMillis(Long.parseLong(millsStr));
		return cal;
	}

	/**
	 * 除去字符串中的空格、回车、换行符、制表符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		String after = m.replaceAll("");
		return after;
	}

	/**
	 * js日历控件日期转换为字符串类型的mills
	 * 
	 * @param jsCalendar
	 *            js日期 支持格式为20100520,2010-05-20
	 * @param separator
	 *            分隔符 如格式为20100520 则为空 格式为2010-05-20则为"-"
	 * @return 如果jsCalendar为空则返回空字符串
	 */
	public static String jsCalendarToString(String jsCalendar, String separator) {
		Calendar cal = Calendar.getInstance();
		if (StringUtils.isBlank(jsCalendar)) {
			return "";
		}
		if (separator != null && !separator.trim().equals("")) {
			String[] jsCalendarArr = StringUtils.split(jsCalendar, separator);
			cal.set(Integer.parseInt(jsCalendarArr[0]),
					Integer.parseInt(jsCalendarArr[1]) - 1,
					Integer.parseInt(jsCalendarArr[2]));
		} else {
			cal.set(Integer.parseInt(StringUtils.substring(jsCalendar, 0, 4)),
					Integer.parseInt(StringUtils.substring(jsCalendar, 4, 6)) - 1,
					Integer.parseInt(StringUtils.substring(jsCalendar, 6, 8)));
		}
		return StringUtils.dateToMillis(cal.getTime());
	}

	/**
	 * 字符串转换成日历控件日期
	 * 
	 * @param mills
	 * @param pattern
	 * @return
	 */
	public static String stringToJsCalendar(String mills, String pattern) {
		if (StringUtils.isBlank(mills)) {
			return "";
		}
		return DateFormatUtils.format(StringUtils.stringToCalendar(mills),
				pattern);
	}

	public static boolean hasLength(String str) {
		return org.springframework.util.StringUtils.hasLength(str);
	}

	public static boolean hasText(String str) {
		return org.springframework.util.StringUtils.hasText(str);
	}

	public static String concatString(String... args) {
		StringBuilder buf = new StringBuilder();
		for (String s : args) {
			buf.append(s);
		}
		return buf.toString();
	}

	public static String getStackTrace(Throwable ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static String getSuperID(String parentID) {
		String returnStr = parentID.substring(0, parentID.lastIndexOf("."));
		int len = returnStr.length();
		return returnStr.substring(returnStr.lastIndexOf(".") + 1, len);
	}

	public static String replace(String text, String repl, String with) {
		return StringUtils.replace(text, repl, with);
	}

	public static String getParentIDFormFullParentID(String fullparentID) {
		String parentID = fullparentID.substring(0, fullparentID.length() - 1);
		if (StringUtils.hasLength(parentID)) {
			parentID = parentID.substring(parentID.lastIndexOf(".") + 1);
		}
		return parentID;
	}

	public static boolean getBoolValueFromStr(String boolStr) {
		if ("1".equals(boolStr)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getSeparator() {
		return System.getProperties().getProperty("file.separator");
	}

	/**
	 * isEmpty
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return null == str || str.trim().length() == 0;
	}

	/**
	 * isNotEmpty
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String objectToString(Object obj) {
		return ObjectUtils.toString(obj);
	}

	/**
	 * getRandomNum creates a random Munber
	 * 
	 * @param pwd_len
	 *            you want to get a random Munber's length
	 * @return String
	 */
	public static String getRandomStringAndNum(int pwd_len) {
		final int maxNum = 36;
		int i;
		int count = 0;
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			i = Math.abs(r.nextInt(maxNum));

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * getRandomNum creates a random Munber
	 * 
	 * @param pwd_len
	 *            you want to get a random Munber's length
	 * @return String
	 */
	public static String getRandomInt(int pwd_len) {
		final int maxNum = 36;
		int i;
		int count = 0;
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			i = Math.abs(r.nextInt(maxNum));

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	public static String filterHtml(String str) {
		Pattern pattern = Pattern.compile("<([^>]*)>");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String clobToString(Clob clob) {
		try {

			Reader inStreamDoc = clob.getCharacterStream();

			char[] tempDoc = new char[(int) clob.length()];
			inStreamDoc.read(tempDoc);
			inStreamDoc.close();
			return new String(tempDoc);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException es) {
			es.printStackTrace();
		}
		return null;
	}

	public static String doubleToBit(double value, int bit) {
		String format = "0";
		if (bit > 0) {
			format += ".";
			for (int i = 0; i < bit; i++) {
				format += "0";
			}
		}
		DecimalFormat df = new DecimalFormat(format);

		String db = df.format(value);

		return db;
	}

}
