package com.gzedu.xlims.dao.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.exam.repository.ViewExamPlanScRepository;
import com.gzedu.xlims.pojo.exam.ViewExamPlanSc;

@Repository
public class ViewExamPlanScDao {

	@Autowired
	private ViewExamPlanScRepository viewExamPlanScRepository;

	public ViewExamPlanSc findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(String examBatchCode, String courseId, String specialtyId, int type) {
		return viewExamPlanScRepository.findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(examBatchCode, courseId, specialtyId, type);
	}

}
