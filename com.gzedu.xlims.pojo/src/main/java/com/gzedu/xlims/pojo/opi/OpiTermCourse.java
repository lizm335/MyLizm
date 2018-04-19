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
@XStreamAlias("TERM_COURSE")
public class OpiTermCourse {
	String TERMCOURSE_ID;// 期课程id
	String COURSE_ID;// 课程id
	String TERM_ID;// 期id
	String TERMCOURSE_START_DT;// 期课程起始时间(年-月-日)
	String TERMCOURSE_END_DT;// 期课程结束时间(年-月-日)
	String CREATED_BY;// 创建者id

	public String getTERMCOURSE_ID() {
		return TERMCOURSE_ID;
	}

	public void setTERMCOURSE_ID(String tERMCOURSE_ID) {
		TERMCOURSE_ID = tERMCOURSE_ID;
	}

	public String getCOURSE_ID() {
		return COURSE_ID;
	}

	public void setCOURSE_ID(String cOURSE_ID) {
		COURSE_ID = cOURSE_ID;
	}

	public String getTERM_ID() {
		return TERM_ID;
	}

	public void setTERM_ID(String tERM_ID) {
		TERM_ID = tERM_ID;
	}

	public String getTERMCOURSE_START_DT() {
		return TERMCOURSE_START_DT;
	}

	public void setTERMCOURSE_START_DT(String tERMCOURSE_START_DT) {
		TERMCOURSE_START_DT = tERMCOURSE_START_DT;
	}

	public String getTERMCOURSE_END_DT() {
		return TERMCOURSE_END_DT;
	}

	public void setTERMCOURSE_END_DT(String tERMCOURSE_END_DT) {
		TERMCOURSE_END_DT = tERMCOURSE_END_DT;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

}
