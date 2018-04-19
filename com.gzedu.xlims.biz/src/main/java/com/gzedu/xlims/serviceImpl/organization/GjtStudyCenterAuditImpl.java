/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.organization.GjtStudyCenterAuditDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.GjtStudyCenterAudit;
import com.gzedu.xlims.service.organization.GjtStudyCenterAuditService;

/**
 * 
 * 功能说明：学习中心审核
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月15日
 * @version 3.0
 *
 */
@Service
public class GjtStudyCenterAuditImpl extends BaseDaoImpl implements GjtStudyCenterAuditService {

	private static final Logger log = LoggerFactory.getLogger(GjtStudyCenterAuditImpl.class);

	@Autowired
	GjtStudyCenterAuditDao gjtStudyCenterAuditDao;

	@Override
	public List<GjtStudyCenterAudit> queryByIncidentId(String orgId) {
		log.info("学习中心审核查询参数：orgId={}", orgId);
		List<GjtStudyCenterAudit> list = gjtStudyCenterAuditDao.findByIncidentIdAndIsDeletedOrderByCreatedDtAsc(orgId,
				"N");
		return list;
	}

	@Override
	public boolean update(GjtStudyCenterAudit item) {
		log.info("学习中心审核查询参数：{}", item.toString());
		GjtStudyCenterAudit save = gjtStudyCenterAuditDao.save(item);
		return save == null ? false : true;
	}

	@Override
	public boolean save(GjtStudyCenterAudit item) {
		log.info("学习中心审核查询参数：{}", item.toString());
		GjtStudyCenterAudit save = gjtStudyCenterAuditDao.save(item);
		return save == null ? false : true;
	}
}