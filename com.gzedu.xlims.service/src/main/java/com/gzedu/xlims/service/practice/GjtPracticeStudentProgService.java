package com.gzedu.xlims.service.practice;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;

public interface GjtPracticeStudentProgService {
	
	public Page<GjtPracticeStudentProg> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtPracticeStudentProg insert(GjtPracticeStudentProg entity);

	public GjtPracticeStudentProg findOne(String id);
	
	public void deleteByPracticePlanIdAndStudentId(String practicePlanId, String studentId);
	
	public List<GjtPracticeStudentProg> findByPracticePlanIdAndStudentId(String practicePlanId, String studentId);

}
