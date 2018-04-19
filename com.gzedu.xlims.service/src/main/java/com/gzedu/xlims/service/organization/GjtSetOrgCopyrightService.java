/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import javax.servlet.http.HttpServletRequest;

import com.gzedu.xlims.pojo.GjtSetOrgCopyright;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月15日
 * @version 2.5
 *
 */
public interface GjtSetOrgCopyrightService {
	// 根据院校ID和平台类型查询版权设置
	GjtSetOrgCopyright findBySchoolIdAndPlatfromType(String schoolId, int platfromType);

	// 根据域名查询域名所对应的信息
	GjtSetOrgCopyright findBySchoolRealmName(HttpServletRequest request, int platfromType);

}
