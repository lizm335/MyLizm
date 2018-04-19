package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.graduation.GjtGraduationDefenceTeacher;

/**
 * 毕业专业答辩老师
 * @author eenet09
 *
 */
public interface GjtGraduationDefenceTeacherDao extends JpaRepository<GjtGraduationDefenceTeacher, String>, JpaSpecificationExecutor<GjtGraduationDefenceTeacher> {

	@Modifying
	@Transactional
	@Query("delete from GjtGraduationDefenceTeacher t where t.gjtGraduationDefencePlan.planId = ?1")
	public void deleteByPlanId(String planId);

}
