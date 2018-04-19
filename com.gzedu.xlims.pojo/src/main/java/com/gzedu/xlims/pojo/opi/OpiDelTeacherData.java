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
public class OpiDelTeacherData {
	String TEACHER_IDS;

	public OpiDelTeacherData(String tEACHER_IDS) {
		super();
		TEACHER_IDS = tEACHER_IDS;
	}

}
