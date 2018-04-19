package com.gzedu.xlims.serviceImpl.exam;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.exam.GjtExamGuideFeedbackDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.exam.GjtExamGuideFeedback;
import com.gzedu.xlims.service.exam.GjtExamGuideFeedbackService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 考试指引反馈业务逻辑类
 */
@Service
public class GjtExamGuideFeedbackServiceImpl extends BaseServiceImpl<GjtExamGuideFeedback> implements GjtExamGuideFeedbackService {

	@Autowired
	private GjtExamGuideFeedbackDao gjtExamGuideFeedbackDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Override
	protected BaseDao<GjtExamGuideFeedback, String> getBaseDao() {
		return this.gjtExamGuideFeedbackDao;
	}

	@Override
	public GjtExamGuideFeedback findByStudentIdAndExamBatchCode(String studentId, String examPlanId) {
		return gjtExamGuideFeedbackDao.findByGjtStudentInfoStudentIdAndExamBatchCodeAndIsDeleted(studentId, examPlanId, "N");
	}

	@Override
	public Page<GjtExamGuideFeedback> findAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<GjtExamGuideFeedback> spec = new Criteria<GjtExamGuideFeedback>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));

		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamGuideFeedbackDao.findAll(spec, pageRequest);
	}

	@Override
	public long count(String orgId, Map<String, Object> searchParams) {
		Criteria<GjtExamGuideFeedback> spec = new Criteria<GjtExamGuideFeedback>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));

		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamGuideFeedbackDao.count(spec);
	}

	@Override
	public boolean update(GjtExamGuideFeedback entity) {
		entity.setUpdatedDt(new Date());
		return gjtExamGuideFeedbackDao.save(entity) != null;
	}

}
