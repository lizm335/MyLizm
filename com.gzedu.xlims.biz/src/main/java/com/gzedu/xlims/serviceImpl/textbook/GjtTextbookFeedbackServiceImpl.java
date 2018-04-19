package com.gzedu.xlims.serviceImpl.textbook;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookFeedbackDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookFeedbackDetailDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedback;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedbackDetail;
import com.gzedu.xlims.service.textbook.GjtTextbookFeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GjtTextbookFeedbackServiceImpl implements GjtTextbookFeedbackService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookFeedbackServiceImpl.class);

	@Autowired
	private GjtTextbookFeedbackDao gjtTextbookFeedbackDao;

	@Autowired
	private GjtTextbookFeedbackDetailDao GjtTextbookFeedbackDetailDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Override
	public Page<GjtTextbookFeedback> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		Criteria<GjtTextbookFeedback> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		// 院校ID
		String xxId = (String) searchParams.remove("EQ_gjtStudentInfo.xxId");
		if (StringUtils.isNotBlank(xxId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(xxId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParams));

		searchParams.put("EQ_gjtStudentInfo.xxId", xxId);
		return gjtTextbookFeedbackDao.findAll(spec, pageRequst);
	}

	@Override
	@Transactional
	public GjtTextbookFeedback insert(GjtTextbookFeedback entity) {
		log.info("entity:[" + entity + "]");
		entity.setFeedbackId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		GjtTextbookFeedback textbookFeedback = gjtTextbookFeedbackDao.save(entity);
		
		//保存明细
		List<GjtTextbookFeedbackDetail> textbookFeedbackDetails = entity.getGjtTextbookFeedbackDetails();
		for (GjtTextbookFeedbackDetail textbookFeedbackDetail : textbookFeedbackDetails) {
			textbookFeedbackDetail.setDetailId(UUIDUtils.random());
			textbookFeedbackDetail.setGjtTextbookFeedback(textbookFeedback);
		}
		GjtTextbookFeedbackDetailDao.save(textbookFeedbackDetails);
		
		return textbookFeedback;
	}

	@Override
	public void update(GjtTextbookFeedback entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookFeedbackDao.save(entity);
	}

	@Override
	public GjtTextbookFeedback findOne(String id) {
		return gjtTextbookFeedbackDao.findOne(id);
	}

}
