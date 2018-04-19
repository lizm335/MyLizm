package com.gzedu.xlims.serviceImpl.graduation;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.graduation.GjtGraduationStudentProgDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationStudentProg;
import com.gzedu.xlims.service.graduation.GjtGraduationStudentProgService;

@Service
public class GjtGraduationStudentProgServiceImpl implements GjtGraduationStudentProgService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationStudentProgServiceImpl.class);

	@Autowired
	private GjtGraduationStudentProgDao gjtGraduationStudentProgDao;

	@Override
	public GjtGraduationStudentProg queryOneByCode(String batchId, String studentId, int progressType,
			String progressCode) {
		return gjtGraduationStudentProgDao.queryOneByCode(batchId, studentId, progressType, progressCode);
	}

	@Override
	public void insert(GjtGraduationStudentProg entity) {
		log.info("entity:[" + entity + "]");
		
		entity.setProgressId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		
		gjtGraduationStudentProgDao.save(entity);
	}

	@Override
	public void insert(List<GjtGraduationStudentProg> entityList) {
		log.info("entityList:[" + entityList + "]");
		gjtGraduationStudentProgDao.save(entityList);
	}

	@Override
	public List<GjtGraduationStudentProg> queryListByStudent(String batchId, String studentId, int progressType) {
		return gjtGraduationStudentProgDao.queryListByStudent(batchId, studentId, progressType);
	}

	@Override
	public void delete(List<GjtGraduationStudentProg> entityList) {
		log.info("entityList:[" + entityList + "]");
		gjtGraduationStudentProgDao.delete(entityList);
	}

}
