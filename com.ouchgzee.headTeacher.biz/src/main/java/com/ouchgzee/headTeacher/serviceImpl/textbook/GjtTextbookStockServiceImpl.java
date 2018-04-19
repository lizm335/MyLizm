package com.ouchgzee.headTeacher.serviceImpl.textbook;

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
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookStockDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStock;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookStockService;

@Deprecated @Service("bzrGjtTextbookStockServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookStockServiceImpl implements BzrGjtTextbookStockService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookStockServiceImpl.class);

	@Autowired
	private GjtTextbookStockDao gjtTextbookStockDao;

	@Override
	public Page<BzrGjtTextbookStock> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbookStock> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtTextbookStock.class);
		return gjtTextbookStockDao.findAll(spec, pageRequst);
	}

	@Override
	public List<BzrGjtTextbookStock> findAll(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbookStock> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtTextbookStock.class);
		return gjtTextbookStockDao.findAll(spec);
	}

	@Override
	public BzrGjtTextbookStock findOne(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbookStock> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtTextbookStock.class);
		return gjtTextbookStockDao.findOne(spec);
	}

	@Override
	public BzrGjtTextbookStock findOne(String id) {
		log.info("id:[" + id + "]");
		return gjtTextbookStockDao.findOne(id);
	}

	@Override
	public BzrGjtTextbookStock insert(BzrGjtTextbookStock entity) {
		log.info("entity:[" + entity + "]");
		entity.setStockId(UUIDUtils.random());
		return gjtTextbookStockDao.save(entity);
	}

	@Override
	public void update(BzrGjtTextbookStock entity) {
		log.info("entity:[" + entity + "]");
		gjtTextbookStockDao.save(entity);
	}

	@Override
	public void update(List<BzrGjtTextbookStock> entities) {
		log.info("entities:[" + entities + "]");
		gjtTextbookStockDao.save(entities);
	}

}
