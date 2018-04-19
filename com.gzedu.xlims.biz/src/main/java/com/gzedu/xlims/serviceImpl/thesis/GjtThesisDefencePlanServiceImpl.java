package com.gzedu.xlims.serviceImpl.thesis;

import java.util.Date;
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
import com.gzedu.xlims.dao.thesis.GjtThesisDefencePlanDao;
import com.gzedu.xlims.pojo.thesis.GjtThesisDefencePlan;
import com.gzedu.xlims.service.thesis.GjtThesisDefencePlanService;

@Service
public class GjtThesisDefencePlanServiceImpl implements GjtThesisDefencePlanService {
	
	private static final Log log = LogFactory.getLog(GjtThesisDefencePlanServiceImpl.class);
	
	@Autowired
	private GjtThesisDefencePlanDao gjtThesisDefencePlanDao;

	@Override
	public Page<GjtThesisDefencePlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtThesisDefencePlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtThesisDefencePlan.class);
		return gjtThesisDefencePlanDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtThesisDefencePlan insert(GjtThesisDefencePlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setPlanId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtThesisDefencePlanDao.save(entity);
	}

	@Override
	public void update(GjtThesisDefencePlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtThesisDefencePlanDao.save(entity);
	}

	@Override
	public GjtThesisDefencePlan findOne(String id) {
		return gjtThesisDefencePlanDao.findOne(id);
	}

}
