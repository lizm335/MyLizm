package com.gzedu.xlims.serviceImpl.practice;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.practice.GjtPracticePlanApprovalDao;
import com.gzedu.xlims.pojo.practice.GjtPracticePlanApproval;
import com.gzedu.xlims.service.practice.GjtPracticePlanApprovalService;

@Service
public class GjtPracticePlanApprovalServiceImpl implements GjtPracticePlanApprovalService {
	
	private static final Log log = LogFactory.getLog(GjtPracticePlanApprovalServiceImpl.class);
	
	@Autowired
	private GjtPracticePlanApprovalDao gjtPracticePlanApprovalDao;

	@Override
	public GjtPracticePlanApproval insert(GjtPracticePlanApproval entity) {
		log.info("entity:[" + entity + "]");
		entity.setApprovalId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtPracticePlanApprovalDao.save(entity);
	}

}
