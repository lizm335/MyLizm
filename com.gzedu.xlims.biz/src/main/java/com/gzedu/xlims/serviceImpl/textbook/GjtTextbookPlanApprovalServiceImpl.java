package com.gzedu.xlims.serviceImpl.textbook;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.textbook.GjtTextbookPlanApprovalDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlanApproval;
import com.gzedu.xlims.service.textbook.GjtTextbookPlanApprovalService;

@Service
public class GjtTextbookPlanApprovalServiceImpl implements GjtTextbookPlanApprovalService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookPlanApprovalServiceImpl.class);

	@Autowired
	private GjtTextbookPlanApprovalDao gjtTextbookPlanApprovalDao;
	
	@Override
	public GjtTextbookPlanApproval insert(GjtTextbookPlanApproval entity) {
		log.info("entity:[" + entity + "]");
		entity.setApprovalId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtTextbookPlanApprovalDao.save(entity);
	}

	@Override
	public List<GjtTextbookPlanApproval> findByPlanId(String planId) {
		return gjtTextbookPlanApprovalDao.findByPlanId(planId);
	}

}
