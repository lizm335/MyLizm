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
@XStreamAlias("TERM")
public class OpiTerm {
	String TERM_ID;// 期id
	String TERM_CODE;// 期编号
	String TERM_NAME;// 期名称
	String TERM_START_DT;// 期起始时间(年-月-日)
	String TERM_END_DT;// 期结束时间(年-月-日)
	String CREATED_BY;// 创建者id

	public String getTERM_ID() {
		return TERM_ID;
	}

	public void setTERM_ID(String tERM_ID) {
		TERM_ID = tERM_ID;
	}

	public String getTERM_CODE() {
		return TERM_CODE;
	}

	public void setTERM_CODE(String tERM_CODE) {
		TERM_CODE = tERM_CODE;
	}

	public String getTERM_NAME() {
		return TERM_NAME;
	}

	public void setTERM_NAME(String tERM_NAME) {
		TERM_NAME = tERM_NAME;
	}

	public String getTERM_START_DT() {
		return TERM_START_DT;
	}

	public void setTERM_START_DT(String tERM_START_DT) {
		TERM_START_DT = tERM_START_DT;
	}

	public String getTERM_END_DT() {
		return TERM_END_DT;
	}

	public void setTERM_END_DT(String tERM_END_DT) {
		TERM_END_DT = tERM_END_DT;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

}
