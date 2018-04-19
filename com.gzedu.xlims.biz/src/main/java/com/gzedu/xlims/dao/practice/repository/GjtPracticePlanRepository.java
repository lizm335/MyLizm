package com.gzedu.xlims.dao.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.practice.GjtPracticePlan;

public interface GjtPracticePlanRepository
		extends JpaRepository<GjtPracticePlan, String>, JpaSpecificationExecutor<GjtPracticePlan> {

	public GjtPracticePlan findByGradeIdAndOrgIdAndIsDeleted(String gradeId, String orgId, String isDeleted);

	public GjtPracticePlan findByGradeIdAndOrgIdAndStatusAndIsDeleted(String gradeId, String orgId, int status,
			String isDeleted);

	public List<GjtPracticePlan> findByOrgIdAndStatusAndIsDeleted(String orgId, int status, String isDeleted);

}
