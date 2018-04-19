package com.gzedu.xlims.serviceImpl.graduation;

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
import com.gzedu.xlims.dao.graduation.GjtGraduationAdviserMsgDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationAdviserMsg;
import com.gzedu.xlims.service.graduation.GjtGraduationAdviserMsgService;

@Service
public class GjtGraduationAdviserMsgServiceImpl implements GjtGraduationAdviserMsgService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationAdviserMsgServiceImpl.class);

	@Autowired
	private GjtGraduationAdviserMsgDao gjtGraduationAdviserMsgDao;

	@Override
	public Page<GjtGraduationAdviserMsg> queryPage(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtGraduationAdviserMsg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtGraduationAdviserMsg.class);
		return gjtGraduationAdviserMsgDao.findAll(spec, pageRequst);
	}

	@Override
	public List<GjtGraduationAdviserMsg> queryAll(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtGraduationAdviserMsg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtGraduationAdviserMsg.class);
		return gjtGraduationAdviserMsgDao.findAll(spec);
	}

	@Override
	public void insert(GjtGraduationAdviserMsg entity) {
		log.info("entity:[" + entity + "]");
		entity.setId(UUIDUtils.random());
		gjtGraduationAdviserMsgDao.save(entity);
	}

	@Override
	public GjtGraduationAdviserMsg queryById(String id) {
		log.info("id:" + id);
		return gjtGraduationAdviserMsgDao.findOne(id);
	}

	@Override
	public void delete(String id) {
		log.info("id:" + id);
		gjtGraduationAdviserMsgDao.delete(id);
	}

}
