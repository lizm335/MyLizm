/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.opi;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月19日
 * @version 2.5
 * @since JDK1.7
 *
 */
@XStreamAlias("tranceData")
public class OpiTeacherData {
	String APP_ID;

	@XStreamAlias("TEACHER")
	OpiTeacher TEACHER;

	public OpiTeacherData(String aPP_ID, OpiTeacher tEACHER) {
		super();
		APP_ID = aPP_ID;
		TEACHER = tEACHER;
	}

}
