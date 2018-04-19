package com.gzedu.xlims.dao.textbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;

public interface GjtTextbookPlanRepository extends JpaRepository<GjtTextbookPlan, String>, JpaSpecificationExecutor<GjtTextbookPlan> {
	
	public GjtTextbookPlan findByPlanCodeAndOrgIdAndIsDeleted(String planCode, String orgId, String isDeleted);
	
	public GjtTextbookPlan findByGradeIdAndOrgIdAndIsDeleted(String gradeId, String orgId, String isDeleted);
	
	@Query("select p from GjtTextbookPlan p where p.isDeleted = 'N' and p.status = 3 and p.orgId = ?1 and p.arrangeSdate <= sysdate and p.arrangeEdate >= sysdate and p.gjtGrade.startDate <= sysdate and p.gjtGrade.endDate >= sysdate")
	public GjtTextbookPlan findCurrentArrangePlan(String orgId);
	
	public List<GjtTextbookPlan> findByStatusAndOrgIdAndIsDeleted(int status, String orgId, String isDeleted);
	
	@Query("select p from GjtTextbookPlan p where p.isDeleted = 'N' and p.orgId = ?1 and p.gjtGrade.startDate <= sysdate and p.gjtGrade.endDate >= sysdate")
	public List<GjtTextbookPlan> findCurrentPlanList(String orgId);

	@Query(value = "select p from GjtTextbookPlan p inner join p.gjtTextbookGradeList g where p.orgId=?1 and g.gradeId=?2 and p.ofeedbackEdate >= trunc(sysdate) and p.status=3 and p.isDeleted='N' order by p.gjtGrade.startDate desc")
	public List<GjtTextbookPlan> findByOrgIdAndSysdate(String orgId, String gradeId);

}
