/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.systemManger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.model.PriRoleInfoRunDao;
import com.gzedu.xlims.pojo.PriRoleInfoRun;
import com.gzedu.xlims.service.usermanage.PriRoleInfoRunService;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月25日
 * @version 2.5
 *
 */
@Service
public class PriRoleInfoRunServiceImpl implements PriRoleInfoRunService {

	@Autowired
	PriRoleInfoRunDao priRoleInfoRunDao;

	@Override
	public void save(List<PriRoleInfoRun> list) {
		priRoleInfoRunDao.save(list);
	}

	@Override
	public void delete(String roleId) {
		priRoleInfoRunDao.deleteByRoleId(roleId);
	}

}
