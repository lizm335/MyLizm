package com.ouchgzee.headTeacher.service.textbook;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookPlan;

@Deprecated public interface BzrGjtTextbookPlanService {
	
	public Page<BzrGjtTextbookPlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public BzrGjtTextbookPlan findOne(String id);

}
