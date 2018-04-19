package com.ouchgzee.headTeacher.serviceImpl.textbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookFeedbackDao;
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookFeedbackDetailDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookFeedback;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookFeedbackDetail;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookFeedbackService;

@Deprecated @Service("bzrGjtTextbookFeedbackServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookFeedbackServiceImpl implements BzrGjtTextbookFeedbackService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookFeedbackServiceImpl.class);

	@Autowired
	private GjtTextbookFeedbackDao gjtTextbookFeedbackDao;

	@Autowired
	private GjtTextbookFeedbackDetailDao GjtTextbookFeedbackDetailDao;

	@Override
	public Page<BzrGjtTextbookFeedback> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbookFeedback> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtTextbookFeedback.class);
		return gjtTextbookFeedbackDao.findAll(spec, pageRequst);
	}

	@Override
	@Transactional(value="transactionManagerBzr")
	public BzrGjtTextbookFeedback insert(BzrGjtTextbookFeedback entity) {
		log.info("entity:[" + entity + "]");
		entity.setFeedbackId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		BzrGjtTextbookFeedback textbookFeedback = gjtTextbookFeedbackDao.save(entity);
		
		//保存明细
		List<BzrGjtTextbookFeedbackDetail> textbookFeedbackDetails = entity.getGjtTextbookFeedbackDetails();
		for (BzrGjtTextbookFeedbackDetail textbookFeedbackDetail : textbookFeedbackDetails) {
			textbookFeedbackDetail.setDetailId(UUIDUtils.random());
			textbookFeedbackDetail.setGjtTextbookFeedback(textbookFeedback);
		}
		GjtTextbookFeedbackDetailDao.save(textbookFeedbackDetails);
		
		return textbookFeedback;
	}

	@Override
	public void update(BzrGjtTextbookFeedback entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookFeedbackDao.save(entity);
	}

	@Override
	public BzrGjtTextbookFeedback findOne(String id) {
		return gjtTextbookFeedbackDao.findOne(id);
	}

}
