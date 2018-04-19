/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.record;

import java.util.Map;

/**
 * 统计记录
 *
 */
public interface RecordServer {
	
	/**
	 * 初始化考试预约数据
	 * 
	 * @param formMap
	 */
	Map initRecordAppointment(Map formMap);
}
