package com.gzedu.xlims.serviceImpl.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.exam.ViewExamPlanScDao;
import com.gzedu.xlims.pojo.exam.ViewExamPlanSc;
import com.gzedu.xlims.service.exam.ViewExamPlanScService;

@Service
public class ViewExamPlanScServiceImpl implements ViewExamPlanScService {

	@Autowired
	private ViewExamPlanScDao viewExamPlanScDao;

	@Override
	public ViewExamPlanSc findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(String examBatchCode, String courseId,
			String specialtyId, int type) {
		return viewExamPlanScDao.findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(examBatchCode, courseId, specialtyId, type);
	}

}
