package com.gzedu.xlims.serviceImpl.textbook;

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
import com.gzedu.xlims.dao.textbook.GjtTextbookStockDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;
import com.gzedu.xlims.service.textbook.GjtTextbookStockService;

@Service
public class GjtTextbookStockServiceImpl implements GjtTextbookStockService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookStockServiceImpl.class);

	@Autowired
	private GjtTextbookStockDao gjtTextbookStockDao;

	@Override
	public Page<GjtTextbookStock> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookStock> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbookStock.class);
		return gjtTextbookStockDao.findAll(spec, pageRequst);
	}

	@Override
	public List<GjtTextbookStock> findAll(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookStock> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbookStock.class);
		return gjtTextbookStockDao.findAll(spec);
	}

	@Override
	public GjtTextbookStock findOne(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookStock> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbookStock.class);
		return gjtTextbookStockDao.findOne(spec);
	}

	@Override
	public GjtTextbookStock findOne(String id) {
		log.info("id:[" + id + "]");
		return gjtTextbookStockDao.findOne(id);
	}

	@Override
	public GjtTextbookStock insert(GjtTextbookStock entity) {
		log.info("entity:[" + entity + "]");
		entity.setStockId(UUIDUtils.random());
		return gjtTextbookStockDao.save(entity);
	}

	@Override
	public void update(GjtTextbookStock entity) {
		log.info("entity:[" + entity + "]");
		gjtTextbookStockDao.save(entity);
	}

	@Override
	public void update(List<GjtTextbookStock> entities) {
		log.info("entities:[" + entities + "]");
		gjtTextbookStockDao.save(entities);
	}

}
