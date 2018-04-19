package com.gzedu.xlims.dao.exam;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.exam.GjtExamGuideFeedback;

/**
 * 考试指引反馈数据操作类
 */
public interface GjtExamGuideFeedbackDao extends BaseDao<GjtExamGuideFeedback, String> {

	/**
	 * 获取学员对应的考试计划的反馈信息
	 * @param studentId
	 * @param examBatchCode
	 * @param isDeleted
	 * @return
	 */
	GjtExamGuideFeedback findByGjtStudentInfoStudentIdAndExamBatchCodeAndIsDeleted(String studentId, String examBatchCode, String isDeleted);

}
