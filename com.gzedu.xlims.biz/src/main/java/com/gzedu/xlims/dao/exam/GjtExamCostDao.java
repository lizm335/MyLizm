package com.gzedu.xlims.dao.exam;

import java.util.List;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.exam.GjtExamCost;

public interface GjtExamCostDao extends BaseDao<GjtExamCost, String> {
	
	/**
	 * 获取已支付的支付记录
	 * @param studentId
	 * @param examPlanId
	 * @param isDeleted
	 * @return
	 */
	List<GjtExamCost> findByStudentIdAndExamPlanIdAndPayStatusAndIsDeleted(String studentId, String examPlanId, String payStatus, String isDeleted);

}
