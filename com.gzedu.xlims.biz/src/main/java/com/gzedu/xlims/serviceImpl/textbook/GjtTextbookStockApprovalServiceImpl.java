package com.gzedu.xlims.serviceImpl.textbook;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.textbook.GjtTextbookStockApprovalDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockApproval;
import com.gzedu.xlims.service.textbook.GjtTextbookStockApprovalService;

@Service
public class GjtTextbookStockApprovalServiceImpl implements GjtTextbookStockApprovalService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookStockApprovalServiceImpl.class);

	@Autowired
	private GjtTextbookStockApprovalDao gjtTextbookStockApprovalDao;

	@Override
	public GjtTextbookStockApproval insert(GjtTextbookStockApproval entity) {
		log.info("entity:[" + entity + "]");
		entity.setApprovalId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtTextbookStockApprovalDao.save(entity);
	}

	@Override
	public List<GjtTextbookStockApproval> insert(List<GjtTextbookStockApproval> entities) {
		log.info("entities:[" + entities + "]");
		
		for (GjtTextbookStockApproval approval : entities) {
			approval.setApprovalId(UUIDUtils.random());
			approval.setCreatedDt(new Date());
			approval.setIsDeleted("N");
		}
		
		return gjtTextbookStockApprovalDao.save(entities);
	}

}
