package com.gzedu.xlims.serviceImpl.practice;

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

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.practice.GjtPracticePlanApprovalDao;
import com.gzedu.xlims.dao.practice.GjtPracticePlanDao;
import com.gzedu.xlims.pojo.practice.GjtPracticePlan;
import com.gzedu.xlims.pojo.practice.GjtPracticePlanApproval;
import com.gzedu.xlims.service.practice.GjtPracticePlanService;

@Service
public class GjtPracticePlanServiceImpl implements GjtPracticePlanService {

	private static final Log log = LogFactory.getLog(GjtPracticePlanServiceImpl.class);

	@Autowired
	private GjtPracticePlanDao gjtPracticePlanDao;

	@Autowired
	private GjtPracticePlanApprovalDao gjtPracticePlanApprovalDao;

	@Override
	public Page<GjtPracticePlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtPracticePlan> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtPracticePlan.class);
		return gjtPracticePlanDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtPracticePlan insert(GjtPracticePlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setPracticePlanId(UUIDUtils.random());
		entity.setStatus(1);
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		GjtPracticePlan gjtPracticePlan = gjtPracticePlanDao.save(entity);

		// 增加审批记录
		GjtPracticePlanApproval approval = new GjtPracticePlanApproval();
		approval.setApprovalId(UUIDUtils.random());
		approval.setPracticePlanId(gjtPracticePlan.getPracticePlanId());
		approval.setOperaRole(1);
		approval.setOperaType(1);
		approval.setCreatedBy(entity.getCreatedBy());
		approval.setCreatedDt(new Date());
		approval.setIsDeleted("N");
		gjtPracticePlanApprovalDao.save(approval);

		return gjtPracticePlan;
	}

	@Override
	public void update(GjtPracticePlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtPracticePlanDao.save(entity);
	}

	@Override
	public GjtPracticePlan findOne(String id) {
		return gjtPracticePlanDao.findOne(id);
	}

	@Override
	public GjtPracticePlan findByGradeIdAndOrgId(String gradeId, String orgId) {
		return gjtPracticePlanDao.findByGradeIdAndOrgIdAndIsDeleted(gradeId, orgId, "N");
	}

	@Override
	public GjtPracticePlan findByGradeIdAndOrgIdAndStatus(String gradeId, String orgId, int status) {
		return gjtPracticePlanDao.findByGradeIdAndOrgIdAndStatusAndIsDeleted(gradeId, orgId, status, "N");
	}

	@Override
	public Map<String, String> getPracticePlanMap(String orgId) {
		return gjtPracticePlanDao.getPracticePlanMap(orgId);
	}

	@Override
	public List<GjtPracticePlan> findByOrgIdAndStatus(String orgId, int status) {
		return gjtPracticePlanDao.findByOrgIdAndStatus(orgId, status, "N");
	}

}
