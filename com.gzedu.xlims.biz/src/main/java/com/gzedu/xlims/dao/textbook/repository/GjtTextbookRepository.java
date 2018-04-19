package com.gzedu.xlims.dao.textbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.textbook.GjtTextbook;

public interface GjtTextbookRepository extends JpaRepository<GjtTextbook, String>, JpaSpecificationExecutor<GjtTextbook> {

	@Query("select b from GjtTextbook b where b.isDeleted = 'N' and b.textbookCode = ?1 and b.orgId = ?2 ")
	public GjtTextbook findByCode(String textbookCode, String orgId);

	@Query("select b from GjtTextbook b where b.isDeleted = 'N' and b.status = 1 and b.textbookType = 1 and b.orgId = ?2 "
			+ "and b not in (select a.gjtTextbook from GjtTextbookArrange a where a.gjtTextbookPlan.planId = ?1)")
	public List<GjtTextbook> findCurrentArrangeTextbook(String planId, String orgId);
	
	@Modifying
	@Query(value = "insert into GJT_TEACH_PLAN_TEXTBOOK (ID, TEACH_PLAN_ID, COURSE_ID, TEXTBOOK_ID) values (?1,?2,?3,?4)  ", nativeQuery = true)
	void insert(String id, String teachPlanId, String courseId, String textbookId);
}
