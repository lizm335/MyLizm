package com.gzedu.xlims.serviceImpl.graduation;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.graduation.GjtGraduationDefencePlanDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationDefenceTeacherDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationDefencePlan;
import com.gzedu.xlims.service.graduation.GjtGraduationDefencePlanService;

@Service
public class GjtGraduationDefencePlanServiceImpl implements GjtGraduationDefencePlanService {

	private static final Log log = LogFactory.getLog(GjtGraduationDefencePlanServiceImpl.class);

	@Autowired
	private GjtGraduationDefencePlanDao gjtGraduationDefencePlanDao;

	@Autowired
	private GjtGraduationDefenceTeacherDao gjtGraduationDefenceTeacherDao;

	@Override
	public void insert(GjtGraduationDefencePlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setPlanId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		gjtGraduationDefencePlanDao.save(entity);
		/*if (entity.getGjtGraduationDefenceTeachers() != null) {
			gjtGraduationDefenceTeacherDao.save(entity.getGjtGraduationDefenceTeachers());
		}*/
	}

	@Override
	public void update(GjtGraduationDefencePlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtGraduationDefencePlanDao.save(entity);
	}

	@Override
	public GjtGraduationDefencePlan queryById(String id) {
		log.info("id:" + id);
		return gjtGraduationDefencePlanDao.findOne(id);
	}

	@Override
	public void delete(GjtGraduationDefencePlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setDeletedDt(new Date());
		entity.setIsDeleted("Y");
		gjtGraduationDefencePlanDao.save(entity);
	}

}
