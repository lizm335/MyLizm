/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.systemManager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.dao.systemManager.GjtSetOrgCopyrightDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtSetOrgCopyright;
import com.ouchgzee.headTeacher.service.systemManage.BzrGjtSetOrgCopyrightService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月15日
 * @version 2.5
 *
 */
@Deprecated @Service("bzrGjtSetOrgCopyrightServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtSetOrgCopyrightServiceImpl implements BzrGjtSetOrgCopyrightService {

	@Autowired
	GjtSetOrgCopyrightDao gjtSetOrgCopyrightDao;

	@Override
	public BzrGjtSetOrgCopyright findBySchoolIdAndPlatfromType(String schoolId, int platfromType) {
		return gjtSetOrgCopyrightDao.findByXxIdAndPlatfromType(schoolId, String.valueOf(platfromType));
	}

	@Override
	public BzrGjtSetOrgCopyright findBySchoolRealmName(HttpServletRequest request, int platfromType) {
		StringBuffer url = request.getRequestURL();
		String realmName = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
		realmName = realmName.replace("www.", "");
		return gjtSetOrgCopyrightDao.findBySchoolRealmNameAndPlatfromType(realmName, String.valueOf(platfromType));
	}

}
