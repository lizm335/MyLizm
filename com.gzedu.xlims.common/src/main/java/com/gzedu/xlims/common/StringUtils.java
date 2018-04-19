/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 字符串工具类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 * @since JDK 1.7
 */
public class StringUtils {

	public static boolean isEmpty(final Object obj) {
		return obj == null || obj.toString().trim().length() == 0 || "null".equals(obj.toString());
	}

	public static boolean isNotBlank(final Object obj) {
		return !isEmpty(obj) && org.apache.commons.lang3.StringUtils.isNotBlank(obj.toString());
	}

	/**
	 * 去除字符串中的空格、回车、换行符、制表符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {  
	    String dest = "";  
	    if (str!=null) {  
	        Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
	        Matcher m = p.matcher(str);  
	        dest = m.replaceAll("");  
	    }  
	    return dest;  
	}

	/**
	 * 返回浏览器能够正确显示的不乱码的中文名称字符串
	 * @param request
	 * @param oldString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getBrowserStr(HttpServletRequest request,String oldString){
		try {
			String userAgent = request.getHeader("USER-AGENT");
			if(org.apache.commons.lang.StringUtils.contains(userAgent, "MSIE")){//IE浏览器
				oldString = URLEncoder.encode(oldString,"UTF8");
			}else if(org.apache.commons.lang.StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
				oldString = new String(oldString.getBytes(), "ISO8859-1");
			}else{
				oldString = URLEncoder.encode(oldString,"UTF8");//其他浏览器
			}
			return oldString;
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * List 装换成以","连接的字符串
	 *
	 * @param list
	 * @return
	 */
	public static String listToString(List<?> list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				sb.append(list.get(i));
			} else {
				sb.append(list.get(i));
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * List 装换成以regex连接的字符串
	 *
	 * @param list
	 * @return
	 */
	public static String listToString(List<?> list, String regex) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				sb.append(list.get(i));
			} else {
				sb.append(list.get(i));
				sb.append(regex);
			}
		}
		return sb.toString();
	}

	/**
	 * 以","连接的字符串 封装成List(清空空值)
	 *
	 * @param s
	 * @param regex
	 * @return
	 */
	public static List<String> stringToList(String s, String regex) {
		List<String> list = new ArrayList<String>();
		String[] ss = s.split(regex);
		for (String o : ss) {
			if (!isEmpty(o)) {
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * 以","连接的字符串 封装成List(清空空值)
	 *
	 * @param s 字符串
	 * @return
	 */
	public static List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		String[] ss = s.split(",");
		for (String o : ss) {
			if (!isEmpty(o)) {
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * 以 regex 连接的字符串
	 *
	 * @param arr
	 * @param regex
	 * @return
	 */
	public static String arrayToString(Object[] arr, String regex) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1) {
				sb.append(arr[i]);
			} else {
				sb.append(arr[i]);
				sb.append(regex);
			}
		}
		return sb.toString();
	}
	/**
	 * 以 "," 连接的字符串
	 *
	 * @param arr
	 * @return
	 */
	public static String arrayToString(Object[] arr) {
		return arrayToString(arr, ",");
	}

	/**
	 * 将此字符串截取长度
	 * @return
	 */
	public static String strlength(String str, int length) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(str)) {
			if (str.length() > length)
				return str.substring(0, length) + "...";
			else
				return str;
		}
		return "";
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.isEmpty(new int[0]));
	}

}
