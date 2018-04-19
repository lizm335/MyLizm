package com.gzedu.xlims.dao.exam;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtExamPlan;

public interface GjtExamPlanDao extends BaseDao<GjtExamPlan, String> {

	GjtExamPlan findByExamCourseAndStudentIdAndIsDeleted(String examCourseId, String studentId, String isDeleted);

}
