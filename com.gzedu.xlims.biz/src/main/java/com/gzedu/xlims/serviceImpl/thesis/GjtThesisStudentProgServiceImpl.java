package com.gzedu.xlims.serviceImpl.thesis;

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
import com.gzedu.xlims.dao.thesis.GjtThesisStudentProgDao;
import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;
import com.gzedu.xlims.service.thesis.GjtThesisStudentProgService;

@Service
public class GjtThesisStudentProgServiceImpl implements GjtThesisStudentProgService {
	
	private static final Log log = LogFactory.getLog(GjtThesisStudentProgServiceImpl.class);
	
	@Autowired
	private GjtThesisStudentProgDao gjtThesisStudentProgDao;

	@Override
	public Page<GjtThesisStudentProg> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtThesisStudentProg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtThesisStudentProg.class);
		return gjtThesisStudentProgDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtThesisStudentProg insert(GjtThesisStudentProg entity) {
		log.info("entity:[" + entity + "]");
		entity.setProgressId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		
		return gjtThesisStudentProgDao.save(entity);
	}

	@Override
	public GjtThesisStudentProg findOne(String id) {
		return gjtThesisStudentProgDao.findOne(id);
	}

	@Override
	public void deleteByThesisPlanIdAndStudentId(String thesisPlanId, String studentId) {
		gjtThesisStudentProgDao.deleteByThesisPlanIdAndStudentId(thesisPlanId, studentId);
	}

	@Override
	public List<GjtThesisStudentProg> findByThesisPlanIdAndStudentId(String thesisPlanId, String studentId) {
		return gjtThesisStudentProgDao.findByThesisPlanIdAndStudentId(thesisPlanId, studentId);
	}

}
