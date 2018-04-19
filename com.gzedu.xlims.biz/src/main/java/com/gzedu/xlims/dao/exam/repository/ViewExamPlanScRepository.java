package com.gzedu.xlims.dao.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.exam.ViewExamPlanSc;

public interface ViewExamPlanScRepository extends JpaRepository<ViewExamPlanSc, String>, JpaSpecificationExecutor<ViewExamPlanSc> {
	
	ViewExamPlanSc findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(String examBatchCode, String courseId, String specialtyId, int type);

}
