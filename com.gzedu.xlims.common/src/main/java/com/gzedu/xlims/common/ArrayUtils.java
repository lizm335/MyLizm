/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 集合工具类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年10月08日
 * @version 2.5
 * @since JDK 1.7
 */
public class ArrayUtils {

	/**
	 * 字符串，数组转化为List<br/>
	 * 支持单个字符串、数组字符串及String数组<br/>
	 * @param obj
	 * @return
     */
	public static List<String> toList(final Object obj) {
		List<String> list = new ArrayList();
		if(obj instanceof String) {
			String objStr = (String) obj;
			Pattern pattern = Pattern.compile("^\\[(\\d+(, )?)*\\]$");
			Matcher matcher = pattern.matcher(objStr);
			if(matcher.matches()) {
				String[] objArray = GsonUtils.toArray(objStr);
				for (int i = 0; i < objArray.length; i++) {
					list.add(objArray[i]);
				}
			} else {
				list.add(objStr);
			}
		} else if(obj instanceof String[]) {
			String[] objArray = (String[]) obj;
			for (int i = 0; i < objArray.length; i++) {
				list.add(objArray[i]);
			}
		} else {
			return null;
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println(ArrayUtils.toList("[1, 2, 3, 4, 5]"));
		System.out.println(ArrayUtils.toList(new String[] {"11", "12", "13"}));
	}

}
