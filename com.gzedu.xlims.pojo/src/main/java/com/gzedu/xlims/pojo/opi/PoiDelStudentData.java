
/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.opi;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月18日
 * @version 2.5
 * @since JDK1.7
 *
 */
@XStreamAlias("tranceData")
public class PoiDelStudentData {
	String STUDENT_IDS;

	String UPDATED_BY;

	public PoiDelStudentData(String sTUDENT_IDS, String UPDATED_BY) {
		super();
		this.STUDENT_IDS = sTUDENT_IDS;
		this.UPDATED_BY = UPDATED_BY;
	}

}
