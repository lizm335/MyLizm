package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.graduation.GjtGraduationDefencePlan;

/**
 * 毕业专业答辩安排
 * @author eenet09
 *
 */
public interface GjtGraduationDefencePlanDao extends JpaRepository<GjtGraduationDefencePlan, String>, JpaSpecificationExecutor<GjtGraduationDefencePlan> {

}
