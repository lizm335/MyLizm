package com.gzedu.xlims.dao.edumanage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtCustomCourse;
import com.gzedu.xlims.pojo.GjtGrantCoursePlan;

public interface GjtGrantCoursePlanDao extends JpaRepository<GjtGrantCoursePlan, String>, JpaSpecificationExecutor<GjtGrantCoursePlan> {

}
