/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.map;

import java.util.HashMap;
import java.util.Map;

public class ChangeTypeMap {
	public static Map<String, String> getChangeType() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("101", "转专业");
		map.put("102", "转学习中心");
		map.put("103", "转年级");
		return map;
	}
}
