package com.gzedu.xlims.serviceImpl.textbook;

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
import com.gzedu.xlims.dao.textbook.GjtTextbookStockOperaDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOpera;
import com.gzedu.xlims.service.textbook.GjtTextbookStockOperaService;

@Service
public class GjtTextbookStockOperaServiceImpl implements GjtTextbookStockOperaService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookStockOperaServiceImpl.class);

	@Autowired
	private GjtTextbookStockOperaDao gjtTextbookStockOperaDao;

	@Override
	public GjtTextbookStockOpera insert(GjtTextbookStockOpera entity) {
		log.info("entity:[" + entity + "]");
		entity.setOperaId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtTextbookStockOperaDao.save(entity);
	}

	@Override
	public List<GjtTextbookStockOpera> insert(List<GjtTextbookStockOpera> entities) {
		log.info("entities:[" + entities + "]");
		
		for (GjtTextbookStockOpera opera : entities) {
			opera.setOperaId(UUIDUtils.random());
			opera.setCreatedDt(new Date());
			opera.setIsDeleted("N");
		}
		
		return gjtTextbookStockOperaDao.save(entities);
	}

	@Override
	public Page<GjtTextbookStockOpera> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookStockOpera>  spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbookStockOpera.class);
		return gjtTextbookStockOperaDao.findAll(spec, pageRequst);
	}

}
