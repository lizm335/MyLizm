package com.ouchgzee.headTeacher.serviceImpl.textbook;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookStockApprovalDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStockApproval;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookStockApprovalService;

@Deprecated @Service("bzrGjtTextbookStockApprovalServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookStockApprovalServiceImpl implements BzrGjtTextbookStockApprovalService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookStockApprovalServiceImpl.class);

	@Autowired
	private GjtTextbookStockApprovalDao gjtTextbookStockApprovalDao;

	@Override
	public BzrGjtTextbookStockApproval insert(BzrGjtTextbookStockApproval entity) {
		log.info("entity:[" + entity + "]");
		entity.setApprovalId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtTextbookStockApprovalDao.save(entity);
	}

	@Override
	public List<BzrGjtTextbookStockApproval> insert(List<BzrGjtTextbookStockApproval> entities) {
		log.info("entities:[" + entities + "]");
		
		for (BzrGjtTextbookStockApproval approval : entities) {
			approval.setApprovalId(UUIDUtils.random());
			approval.setCreatedDt(new Date());
			approval.setIsDeleted("N");
		}
		
		return gjtTextbookStockApprovalDao.save(entities);
	}

}
