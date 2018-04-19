package com.gzedu.xlims.service.exam;

import com.gzedu.xlims.pojo.exam.GjtExamGuideFeedback;
import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

/**
 * 考试指引反馈业务逻辑类
 */
public interface GjtExamGuideFeedbackService extends BaseService<GjtExamGuideFeedback> {

	/**
	 * 获取学员对应的考试计划的反馈信息
	 * @param studentId
	 * @param examBatchCode
	 * @return
	 */
	GjtExamGuideFeedback findByStudentIdAndExamBatchCode(String studentId, String examBatchCode);

	/**
	 * 按条件查询考试指引反馈信息，带分页
	 * @param orgId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<GjtExamGuideFeedback> findAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 按条件查询考试指引反馈数量，统计
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	long count(String orgId, Map<String, Object> searchParams);

	/**
	 * 修改考试指引反馈信息
	 * @param entity
	 * @return
	 */
	boolean update(GjtExamGuideFeedback entity);

}
