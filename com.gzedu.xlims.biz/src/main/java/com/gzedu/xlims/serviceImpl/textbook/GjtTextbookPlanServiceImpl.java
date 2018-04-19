package com.gzedu.xlims.serviceImpl.textbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.textbook.GjtTextbookPlanApprovalDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookPlanDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookGradeDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookGrade;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlanApproval;
import com.gzedu.xlims.service.textbook.GjtTextbookPlanService;

@Service
public class GjtTextbookPlanServiceImpl implements GjtTextbookPlanService {

	private static final Logger log = LoggerFactory.getLogger(GjtTextbookPlanServiceImpl.class);

	@Autowired
	private GjtTextbookPlanDao gjtTextbookPlanDao;

	@Autowired
	private GjtTextbookPlanApprovalDao gjtTextbookPlanApprovalDao;

	@Autowired
	GjtTextbookGradeDao gjtTextbookGradeDao;

	@Override
	public Page<GjtTextbookPlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtTextbookPlan.class);
		return gjtTextbookPlanDao.findAll(spec, pageRequst);
	}

	@Override
	@Transactional
	public GjtTextbookPlan insert(GjtTextbookPlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setPlanId(UUIDUtils.random());
		entity.setStatus(1);
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		GjtTextbookPlan textbookPlan = gjtTextbookPlanDao.save(entity);

		// 增加审批记录
		GjtTextbookPlanApproval approval = new GjtTextbookPlanApproval();
		approval.setApprovalId(UUIDUtils.random());
		approval.setPlanId(entity.getPlanId());
		approval.setOperaRole(1);
		approval.setOperaType(1);
		approval.setCreatedBy(entity.getCreatedBy());
		approval.setCreatedDt(new Date());
		approval.setIsDeleted("N");
		gjtTextbookPlanApprovalDao.save(approval);

		for (GjtTextbookGrade item : entity.getGjtTextbookGradeList()) {
			gjtTextbookGradeDao.save(item);
		}

		return textbookPlan;
	}

	@Override
	public void update(GjtTextbookPlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookPlanDao.save(entity);
	}

	@Override
	public GjtTextbookPlan findOne(String id) {
		return gjtTextbookPlanDao.findOne(id);
	}

	@Override
	public GjtTextbookPlan findByPlanCodeAndOrgId(String planCode, String orgId) {
		return gjtTextbookPlanDao.findByPlanCodeAndOrgIdAndIsDeleted(planCode, orgId, "N");
	}

	@Override
	public GjtTextbookPlan findByGradeIdAndOrgId(String gradeId, String orgId) {
		return gjtTextbookPlanDao.findByGradeIdAndOrgIdAndIsDeleted(gradeId, orgId, "N");
	}

	@Override
	public GjtTextbookPlan findCurrentArrangePlan(String orgId) {
		return gjtTextbookPlanDao.findCurrentArrangePlan(orgId);
	}

	@Override
	public List<GjtTextbookPlan> findByOrgIdAndSysdate(String orgId, String gradeId) {
		return gjtTextbookPlanDao.findByOrgIdAndSysdate(orgId, gradeId);
	}

}
