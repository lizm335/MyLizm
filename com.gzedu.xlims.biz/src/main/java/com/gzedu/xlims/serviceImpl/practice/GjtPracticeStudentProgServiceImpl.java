package com.gzedu.xlims.serviceImpl.practice;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.practice.GjtPracticeStudentProgDao;
import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;
import com.gzedu.xlims.service.practice.GjtPracticeStudentProgService;

@Service
public class GjtPracticeStudentProgServiceImpl implements GjtPracticeStudentProgService {
	
	private static final Log log = LogFactory.getLog(GjtPracticeStudentProgServiceImpl.class);
	
	@Autowired
	private GjtPracticeStudentProgDao gjtPracticeStudentProgDao;

	@Override
	public Page<GjtPracticeStudentProg> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtPracticeStudentProg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtPracticeStudentProg.class);
		return gjtPracticeStudentProgDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtPracticeStudentProg insert(GjtPracticeStudentProg entity) {
		log.info("entity:[" + entity + "]");
		entity.setProgressId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		
		return gjtPracticeStudentProgDao.save(entity);
	}

	@Override
	public GjtPracticeStudentProg findOne(String id) {
		return gjtPracticeStudentProgDao.findOne(id);
	}

	@Override
	public void deleteByPracticePlanIdAndStudentId(String practicePlanId, String studentId) {
		gjtPracticeStudentProgDao.deleteByPracticePlanIdAndStudentId(practicePlanId, studentId);
	}

	@Override
	public List<GjtPracticeStudentProg> findByPracticePlanIdAndStudentId(String practicePlanId, String studentId) {
		return gjtPracticeStudentProgDao.findByPracticePlanIdAndStudentId(practicePlanId, studentId);
	}

}
