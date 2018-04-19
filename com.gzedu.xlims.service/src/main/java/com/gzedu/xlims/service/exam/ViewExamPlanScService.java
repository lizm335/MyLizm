package com.gzedu.xlims.service.exam;

import com.gzedu.xlims.pojo.exam.ViewExamPlanSc;

public interface ViewExamPlanScService {
	
	ViewExamPlanSc findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(String examBatchCode, String courseId, String specialtyId, int type);

}
