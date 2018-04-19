package com.gzedu.xlims.serviceImpl.thesis;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.thesis.GjtThesisPlanApprovalDao;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlanApproval;
import com.gzedu.xlims.service.thesis.GjtThesisPlanApprovalService;

@Service
public class GjtThesisPlanApprovalServiceImpl implements GjtThesisPlanApprovalService {
	
	private static final Log log = LogFactory.getLog(GjtThesisPlanApprovalServiceImpl.class);

	@Autowired
	private GjtThesisPlanApprovalDao gjtThesisPlanApprovalDao;

	@Override
	public GjtThesisPlanApproval insert(GjtThesisPlanApproval entity) {
		log.info("entity:[" + entity + "]");
		entity.setApprovalId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtThesisPlanApprovalDao.save(entity);
	}

}
