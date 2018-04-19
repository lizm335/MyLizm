package com.gzedu.xlims.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.textbook.repository.GjtTextbookPlanRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;

@Repository
public class GjtTextbookPlanDao {

	@Autowired
	private GjtTextbookPlanRepository gjtTextbookPlanRepository;
	
	public Page<GjtTextbookPlan> findAll(Specification<GjtTextbookPlan> spec, PageRequest pageRequst) {
		return gjtTextbookPlanRepository.findAll(spec, pageRequst);
	}
	
	public GjtTextbookPlan save(GjtTextbookPlan entity) {
		return gjtTextbookPlanRepository.save(entity);
	}
	
	public GjtTextbookPlan findOne(String id) {
		return gjtTextbookPlanRepository.findOne(id);
	}
	
	public GjtTextbookPlan findByPlanCodeAndOrgIdAndIsDeleted(String planCode, String orgId, String isDeleted) {
		return gjtTextbookPlanRepository.findByPlanCodeAndOrgIdAndIsDeleted(planCode, orgId, isDeleted);
	}
	
	public GjtTextbookPlan findByGradeIdAndOrgIdAndIsDeleted(String gradeId, String orgId, String isDeleted) {
		return gjtTextbookPlanRepository.findByGradeIdAndOrgIdAndIsDeleted(gradeId, orgId, isDeleted);
	}
	
	public GjtTextbookPlan findCurrentArrangePlan(String orgId) {
		return gjtTextbookPlanRepository.findCurrentArrangePlan(orgId);
	}
	
	public List<GjtTextbookPlan> findByStatusAndOrgIdAndIsDeleted(int status, String orgId, String isDeleted) {
		return gjtTextbookPlanRepository.findByStatusAndOrgIdAndIsDeleted(status, orgId, isDeleted);
	}
	
	public List<GjtTextbookPlan> findCurrentPlanList(String orgId) {
		return gjtTextbookPlanRepository.findCurrentPlanList(orgId);
	}

	public List<GjtTextbookPlan> findByOrgIdAndSysdate(String orgId, String gradeId) {
		return gjtTextbookPlanRepository.findByOrgIdAndSysdate(orgId, gradeId);
	}

}
