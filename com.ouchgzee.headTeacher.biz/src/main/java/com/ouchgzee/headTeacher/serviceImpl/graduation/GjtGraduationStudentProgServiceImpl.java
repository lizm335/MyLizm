package com.ouchgzee.headTeacher.serviceImpl.graduation;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dao.graduation.GjtGraduationStudentProgDao;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationStudentProg;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationStudentProgService;

@Deprecated @Service("bzrGjtGraduationStudentProgServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtGraduationStudentProgServiceImpl implements BzrGjtGraduationStudentProgService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationStudentProgServiceImpl.class);

	@Autowired
	private GjtGraduationStudentProgDao gjtGraduationStudentProgDao;

	@Override
	public BzrGjtGraduationStudentProg queryOneByCode(String batchId, String studentId, int progressType,
													  String progressCode) {
		return gjtGraduationStudentProgDao.queryOneByCode(batchId, studentId, progressType, progressCode);
	}

	@Override
	public void insert(BzrGjtGraduationStudentProg entity) {
		log.info("entity:[" + entity + "]");
		
		entity.setProgressId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		
		gjtGraduationStudentProgDao.save(entity);
	}

	@Override
	public void insert(List<BzrGjtGraduationStudentProg> entityList) {
		log.info("entityList:[" + entityList + "]");
		gjtGraduationStudentProgDao.save(entityList);
	}

	@Override
	public List<BzrGjtGraduationStudentProg> queryListByStudent(String batchId, String studentId, int progressType) {
		return gjtGraduationStudentProgDao.queryListByStudent(batchId, studentId, progressType);
	}

	@Override
	public void delete(List<BzrGjtGraduationStudentProg> entityList) {
		log.info("entityList:[" + entityList + "]");
		gjtGraduationStudentProgDao.delete(entityList);
	}

}
