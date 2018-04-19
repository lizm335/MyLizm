/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.opi;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月14日
 * @version 2.5
 *
 */
@XStreamAlias("tranceData")
public class OpiCourseData {
	String APP_ID;

	@XStreamAlias("COURSE")
	OpiCourse COURSE;

	public OpiCourseData(String aPP_ID, OpiCourse cOURSE) {
		super();
		APP_ID = aPP_ID;
		COURSE = cOURSE;
	}

}
