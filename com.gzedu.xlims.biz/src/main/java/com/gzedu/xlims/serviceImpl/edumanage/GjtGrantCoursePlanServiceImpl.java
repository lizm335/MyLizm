package com.gzedu.xlims.serviceImpl.edumanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtGrantCoursePlanDao;
import com.gzedu.xlims.pojo.GjtGrantCoursePlan;
import com.gzedu.xlims.service.edumanage.GjtGrantCoursePlanService;

@Service
public class GjtGrantCoursePlanServiceImpl implements GjtGrantCoursePlanService {
	@Autowired
	private GjtGrantCoursePlanDao gjtGrantCoursePlanDao;
	@Autowired
	private CommonDao commonDao;
	
	public GjtGrantCoursePlan findOne(String id) {
		return gjtGrantCoursePlanDao.findOne(id);
	}

	@Override
	public void save(GjtGrantCoursePlan plan) {
		gjtGrantCoursePlanDao.save(plan);
	}
	
	@Override
	public void save(Iterable<GjtGrantCoursePlan> entities) {
		gjtGrantCoursePlanDao.save(entities);
	}
	@Override
	public void delete(Iterable<GjtGrantCoursePlan> entities) {
		gjtGrantCoursePlanDao.deleteInBatch(entities);
	}
}
