/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.organization.GjtSetOrgCopyrightDao;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月15日
 * @version 2.5
 *
 */
@Service
public class GjtSetOrgCopyrightServiceImpl implements GjtSetOrgCopyrightService {

	@Autowired
	GjtSetOrgCopyrightDao gjtSetOrgCopyrightDao;

	@Override
	public GjtSetOrgCopyright findBySchoolIdAndPlatfromType(String schoolId, int platfromType) {
		return gjtSetOrgCopyrightDao.findByXxIdAndPlatfromType(schoolId, String.valueOf(platfromType));
	}

	@Override
	public GjtSetOrgCopyright findBySchoolRealmName(HttpServletRequest request, int platfromType) {
		StringBuffer url = request.getRequestURL();
		String realmName = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
		realmName = realmName.replace("www.", "");
		return gjtSetOrgCopyrightDao.findBySchoolRealmNameAndPlatfromType(realmName, String.valueOf(platfromType));
	}

}
