/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.opi;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月15日
 * @version 2.5
 *
 */
@XStreamAlias("tranceData")
public class OpiTermCourseData {
	String APP_ID;

	@XStreamAlias("TERM")
	OpiTerm TERM;

	@XStreamAlias("TERM_COURSE")
	OpiTermCourse TERM_COURSE;

	public OpiTermCourseData(String aPP_ID, OpiTerm term, OpiTermCourse termCourse) {
		super();
		APP_ID = aPP_ID;
		this.TERM = term;
		this.TERM_COURSE = termCourse;
	}

}