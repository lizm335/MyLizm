/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.mobileMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.home.mobileMessage.GjtMoblieMessageAuditDao;
import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageAudit;
import com.gzedu.xlims.service.home.mobileMessage.GjtMoblieMessageAuditService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月16日
 * @version 3.0
 *
 */
@Service
public class GjtMoblieMessageAuditServiceImpl implements GjtMoblieMessageAuditService {

	@Autowired
	GjtMoblieMessageAuditDao gjtMoblieMessageAuditDao;

	@Override
	public GjtMoblieMessageAudit save(GjtMoblieMessageAudit entity) {
		return gjtMoblieMessageAuditDao.save(entity);
	}

}
