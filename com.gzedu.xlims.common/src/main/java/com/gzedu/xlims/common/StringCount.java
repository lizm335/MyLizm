/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串统计工具类 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 * @since JDK 1.7
 */
public class StringCount {

	/**
	 * 统计字符串出现的次数
	 * 
	 * @param str
	 *            需要查询的数据
	 * @param pattern
	 *            需要匹配的数据
	 * @return
	 */
	public static int getCount(String str, String pattern) {
		// pattern表示需要匹配的数据，使用Pattern建立匹配模式
		Pattern p = Pattern.compile(pattern);
		// 使用Matcher进行各种查找替换操作
		Matcher m = p.matcher(str);
		int count = 0;
		while (m.find()) {
			count++;
		}
		return count;
	}

	/**
	 * 统计字符串截取后的节数
	 * 
	 * @param str
	 *            需要查询的数据
	 * @param pattern
	 *            需要匹配的数据
	 * @return
	 */
	public static int getArrayCount(String str, String pattern) {
		if (str != null && !"".equals(str.trim()) && pattern != null && !"".equals(pattern.trim())) {
			return getCount(str, pattern) + 1;
		}
		return 0;
	}

	public static void main(String[] args) {
		// 测试数据
		String str = "fsdf,ofk,ldfl,i,ru,o";
		System.out.println(StringCount.getCount(str, ","));
		System.out.println(StringCount.getArrayCount(str, ","));
	}

}
