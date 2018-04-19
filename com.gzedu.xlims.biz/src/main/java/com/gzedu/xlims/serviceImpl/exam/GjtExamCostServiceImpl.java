package com.gzedu.xlims.serviceImpl.exam;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.exam.GjtExamCostDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.exam.GjtExamCost;
import com.gzedu.xlims.service.exam.GjtExamCostService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class GjtExamCostServiceImpl extends BaseServiceImpl<GjtExamCost> implements GjtExamCostService {

	@Autowired
	private GjtExamCostDao gjtExamCostDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Override
	protected BaseDao<GjtExamCost, String> getBaseDao() {
		return this.gjtExamCostDao;
	}

	@Override
	public List<GjtExamCost> findByStudentIdAndExamPlanId(String studentId, String examPlanId) {
		return gjtExamCostDao.findByStudentIdAndExamPlanIdAndPayStatusAndIsDeleted(studentId, examPlanId, Constants.BOOLEAN_0, "N");
	}

	@Override
	public Page<GjtExamCost> findAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		List<String> orgList = gjtOrgDao.queryChildsByParentId(schoolId);
		filters.put("student.gjtStudyCenter.id", new SearchFilter("student.gjtStudyCenter.id", SearchFilter.Operator.IN, orgList));
		//filters.put("student.xxId", new SearchFilter("student.xxId", Operator.EQ, schoolId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtExamCost> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamCost.class);
		return gjtExamCostDao.findAll(spec, pageRequest);
	}
}
