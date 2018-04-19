/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.systemManger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.model.PriRoleInfoWorkDao;
import com.gzedu.xlims.pojo.PriRoleInfoWork;
import com.gzedu.xlims.service.usermanage.PriRoleInfoWorkService;

/**
 * 
 * 功能说明：工单发送角色
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年10月25日
 * @version 2.5
 *
 */
@Service
public class PriRoleInfoWorkServiceImpl implements PriRoleInfoWorkService {

	@Autowired
	PriRoleInfoWorkDao priRoleInfoWorkDao;

	@Override
	public void save(List<PriRoleInfoWork> list) {
		priRoleInfoWorkDao.save(list);
	}

	@Override
	public void delete(String roleId) {
		priRoleInfoWorkDao.deleteByRoleId(roleId);
	}

}
