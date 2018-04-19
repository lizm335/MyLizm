package com.gzedu.xlims.service.exam;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.exam.GjtExamCost;
import com.gzedu.xlims.service.base.BaseService;

public interface GjtExamCostService extends BaseService<GjtExamCost> {
	
	List<GjtExamCost> findByStudentIdAndExamPlanId(String studentId, String examPlanId);
	
	Page<GjtExamCost> findAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequest);

}
