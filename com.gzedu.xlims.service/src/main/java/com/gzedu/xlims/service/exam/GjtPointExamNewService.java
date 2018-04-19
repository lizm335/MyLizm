package com.gzedu.xlims.service.exam;

import java.util.Map;

import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GjtPointExamNewService extends BaseService<GjtExamPointNew> {

	/**
	 * 考点管理
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page<Map<String, Object>> getExamPointList(Map<String, Object> searchParams,PageRequest pageRequest);
	
	/**
	 * 启用/停用考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map examPointStatus(Map<String, Object> searchParams);
	
	/**
	 * 删除考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map delExamPoint(Map<String, Object> searchParams);
	
}
