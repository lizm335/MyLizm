package com.gzedu.xlims.serviceImpl.exam;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.service.exam.GjtExamPlanCourseService;
@Service
public class GjtExamPlanCourseServiceImpl implements GjtExamPlanCourseService {

	@Autowired
	GjtExamPlanNewDao gjtExamPlanNewDao;
	
	@Override
	public Map createExamPlan(Map<String, Object> formMap) {
		return null;
	}

}
