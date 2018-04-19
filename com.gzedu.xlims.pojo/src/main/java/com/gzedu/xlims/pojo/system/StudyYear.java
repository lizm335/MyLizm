/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.system;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 功能说明：学年度控制，一年分成上半年和下半年，1990开始，1990上半年为 0 ，1990下半年为 1 ，1991 上半年为 2，...以此类推
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月8日
 * @version 2.5
 *
 */
public class StudyYear {

	final static int firstYear = 1990;
	final static int startMonth = 2;// 3月开始，0是一月
	final static int endMonth = 8;// 9月结束/开始

	/**
	 * 获取当前学年度CODE
	 * 
	 * @return
	 */
	public static int getNowCode() {
		return getCode(new Date(), 0);
	}

	/**
	 * 根据开始时间和学期数获取学年度CODE
	 * 
	 * @param beginDate
	 * @param termNum
	 * @return
	 */
	public static int getCode(Date beginDate, int termNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);

		int code = 0;
		if (month >= startMonth && month < endMonth) { // 是否是上半年
			code = (year - firstYear) * 2;
		} else if (month < startMonth) {
			code = (year - firstYear) * 2 - 1;
		} else if (month >= endMonth) {
			code = (year - firstYear) * 2 + 1;
		}
		return code + termNum;
	}

	public static int getCode(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);

		int code = 0;
		if (month >= startMonth && month < endMonth) { // 是否是上半年
			code = (year - firstYear) * 2;
		} else if (month < startMonth) {
			code = (year - firstYear) * 2 - 1;
		} else if (month >= endMonth) {
			code = (year - firstYear) * 2 + 1;
		}
		return code;
	}

	/**
	 * 根据年度开始时间获取所有学年度CODE [五个学期]
	 * 
	 * @param beginDate
	 * @return
	 */
	public static int[] getCodes(Date beginDate) {
		int[] codes = new int[5];
		int beginCode = getCode(beginDate, 0);
		for (int i = 0; i < 5; i++) {
			codes[i] = beginCode + i;
		}
		return codes;
	}

	public static String getName(int code) {
		if (code % 2 == 0) {// 偶数
			return firstYear + code / 2 + "上半年";
		} else {// 奇数
			return firstYear + code / 2 + "下半年";
		}
	}

	public static Date getStartDate(int code) {
		Calendar calendar = Calendar.getInstance();
		int year = firstYear + code / 2;
		int month = 0;
		int date = 1;
		if (code % 2 == 0) {// 偶数
			month = startMonth;
		} else {// 奇数
			month = endMonth;
		}
		calendar.set(year, month, date, 0, 0, 0);
		return calendar.getTime();
	}

	public static Date getEndDate(int code) {
		Calendar calendar = Calendar.getInstance();
		int year = firstYear + code / 2;
		int month = 0;
		int date = 1;
		if (code % 2 == 0) {// 偶数 上半年
			month = endMonth;
		} else {// 奇数下半年
			year++;
			month = startMonth;
		}
		calendar.set(year, month, date, 0, 0, 0);
		return calendar.getTime();
	}

	public static Map<Integer, String> getList() {
		// 当前年
		int year = Calendar.getInstance().get(Calendar.YEAR);
		// 往后追加10年
		int endYear = year + 10;

		int betweenYear = endYear - firstYear;
		Map<Integer, String> m = new LinkedHashMap<Integer, String>();
		for (int i = 0; i < betweenYear * 2; i++) {
			m.put(i, getName(i));
		}
		return m;
	}

	public static Map<Integer, String> getTenYearList() {
		// 当前年
		int year = Calendar.getInstance().get(Calendar.YEAR);
		// 往后追加5年
		int endYear = year + 5;
		int startYear = year - 4;

		Map<Integer, String> m = new LinkedHashMap<Integer, String>();
		for (int i = (startYear - firstYear) * 2; i < (endYear - firstYear) * 2; i++) {
			m.put(i, getName(i));
		}
		return m;
	}

	public static void main(String[] args) {

		// 所有学期
		Map<Integer, String> m = StudyYear.getList();
		for (Entry<Integer, String> year : m.entrySet()) {
			System.out.println(year.getKey());
			System.out.println(year.getValue());
		}

		System.out.println(StudyYear.getName(9));

		System.out.println(StudyYear.getCode(new Date()));

		System.out.println(StudyYear.getName(StudyYear.getCode(new Date())));
	}

}
