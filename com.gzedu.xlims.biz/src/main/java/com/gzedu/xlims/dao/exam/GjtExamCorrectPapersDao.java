package com.gzedu.xlims.dao.exam;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.exam.GjtExamCorrectPapers;

/**
 * 报告批改数据操作类
 */
public interface GjtExamCorrectPapersDao extends BaseDao<GjtExamCorrectPapers, String> {

	/**
	 * 获取学员考试科目的报告批改
	 * @param studentId
	 * @param examPlanId
	 * @param isDeleted
	 * @return
	 */
	GjtExamCorrectPapers findByGjtStudentInfoStudentIdAndGjtExamPlanNewExamPlanIdAndIsDeleted(String studentId, String examPlanId, String isDeleted);

}
