package com.gzedu.xlims.service.exam;

import java.util.Map;

import com.gzedu.xlims.pojo.exam.GjtExamBatchApproval;

public interface GjtExamBatchApprovalService {

	/**
	 * 保存审核记录
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map saveExamApproval(Map<String,Object> searchParams);
	
	/**
	 * 删除考试计划
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map delExamBatch(Map<String, Object> searchParams);
	
	/**
	 * 保存审核记录
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map saveApprovalData(Map<String, Object> searchParams);
	
	/**
	 * 重新发布考试计划
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map againApprovalData(Map<String, Object> searchParams);
	
	public GjtExamBatchApproval insert(GjtExamBatchApproval entity);
}
