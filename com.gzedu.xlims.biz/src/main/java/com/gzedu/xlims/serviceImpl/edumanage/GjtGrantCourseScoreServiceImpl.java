package com.gzedu.xlims.serviceImpl.edumanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtGrantCourseScoreDao;
import com.gzedu.xlims.pojo.GjtGrantCourseScore;
import com.gzedu.xlims.service.edumanage.GjtGrantCourseScoreService;
@Service
public class GjtGrantCourseScoreServiceImpl implements GjtGrantCourseScoreService {
	
	
	@Autowired
	private GjtGrantCourseScoreDao gjtGrantCourseScoreDao;
	@Autowired
	private CommonDao commonDao;
	
	public void delete(Iterable<GjtGrantCourseScore> entities) {
		gjtGrantCourseScoreDao.deleteInBatch(entities);
	}

	
}
