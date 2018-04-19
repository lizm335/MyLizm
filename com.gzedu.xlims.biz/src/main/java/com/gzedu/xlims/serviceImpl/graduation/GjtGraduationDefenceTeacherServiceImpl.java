package com.gzedu.xlims.serviceImpl.graduation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.graduation.GjtGraduationDefenceTeacherDao;
import com.gzedu.xlims.service.graduation.GjtGraduationDefenceTeacherService;

@Service
public class GjtGraduationDefenceTeacherServiceImpl implements GjtGraduationDefenceTeacherService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationDefenceTeacherServiceImpl.class);

	@Autowired
	private GjtGraduationDefenceTeacherDao gjtGraduationDefenceTeacherDao;

	@Override
	public void deleteByPlanId(String planId) {
		log.info("planId:" + planId);
		gjtGraduationDefenceTeacherDao.deleteByPlanId(planId);
	}

}
