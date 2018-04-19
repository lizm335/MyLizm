package com.gzedu.xlims.dao.edumanage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.GjtCustomCourse;

public interface GjtCustomCourseDao extends JpaRepository<GjtCustomCourse, String>, JpaSpecificationExecutor<GjtCustomCourse> {

	@Query("select c from GjtCustomCourse c where c.teachPlanId = ?1 and c.orgId=?2 and isDeleted='N'")
	public GjtCustomCourse findByTeachPlanIdAndOrgId(String teachPlanId,String orgId);
}
