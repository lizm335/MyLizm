package com.gzedu.xlims.serviceImpl.transaction;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.transaction.GjtExemptExamInfoAuditDao;
import com.gzedu.xlims.pojo.GjtExemptExamInfoAudit;
import com.gzedu.xlims.service.transaction.GjtExemptExamInfoAuditService;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月29日
 * @version 2.5
 */
@Service
public class GjtExemptExamInfoAuditServiceImpl implements GjtExemptExamInfoAuditService{
	private static final Logger log = LoggerFactory.getLogger(GjtExemptExamInfoAuditServiceImpl.class);
	
	@Autowired
	private GjtExemptExamInfoAuditDao gjtExemptExamInfoAuditDao;

	@Override
	public List<GjtExemptExamInfoAudit> findByExemptExamIdOrderByAuditDtAsc(String exemptExamId) {		
		return gjtExemptExamInfoAuditDao.findByExemptExamIdOrderByAuditDtAsc(exemptExamId);
	}

}
