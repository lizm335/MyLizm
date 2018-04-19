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
@XStreamAlias("STUDCHOOSE")
public class OpiStudchoose {

	String CHOOSE_ID;// 学员选课id
	String TERMCOURSE_ID;// 期id
	String STUD_ID;// 学员id
	String CLASS_ID;// 期班级id
	String CREATED_BY;// 创建者id

	public String getCHOOSE_ID() {
		return CHOOSE_ID;
	}

	public void setCHOOSE_ID(String cHOOSE_ID) {
		CHOOSE_ID = cHOOSE_ID;
	}

	public String getTERMCOURSE_ID() {
		return TERMCOURSE_ID;
	}

	public void setTERMCOURSE_ID(String tERMCOURSE_ID) {
		TERMCOURSE_ID = tERMCOURSE_ID;
	}

	public String getSTUD_ID() {
		return STUD_ID;
	}

	public void setSTUD_ID(String sTUD_ID) {
		STUD_ID = sTUD_ID;
	}

	public String getCLASS_ID() {
		return CLASS_ID;
	}

	public void setCLASS_ID(String cLASS_ID) {
		CLASS_ID = cLASS_ID;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

}
