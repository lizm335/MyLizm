/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.systemManage;

import javax.servlet.http.HttpServletRequest;

import com.ouchgzee.headTeacher.pojo.BzrGjtSetOrgCopyright;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月15日
 * @version 2.5
 *
 */
@Deprecated public interface BzrGjtSetOrgCopyrightService {
	// 根据院校ID和平台类型查询版权设置
	BzrGjtSetOrgCopyright findBySchoolIdAndPlatfromType(String schoolId, int platfromType);

	// 根据域名查询域名所对应的信息
	BzrGjtSetOrgCopyright findBySchoolRealmName(HttpServletRequest request, int platfromType);

}
