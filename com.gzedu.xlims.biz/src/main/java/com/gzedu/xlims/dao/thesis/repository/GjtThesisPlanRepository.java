package com.gzedu.xlims.dao.thesis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;

public interface GjtThesisPlanRepository
		extends JpaRepository<GjtThesisPlan, String>, JpaSpecificationExecutor<GjtThesisPlan> {

	public GjtThesisPlan findByGradeIdAndOrgIdAndIsDeleted(String gradeId, String orgId, String isDeleted);

	public GjtThesisPlan findByGradeIdAndOrgIdAndStatusAndIsDeleted(String gradeId, String orgId, int status,
			String isDeleted);

	public List<GjtThesisPlan> findByOrgIdAndStatusAndIsDeleted(String orgId, int status, String isDeleted);

}
