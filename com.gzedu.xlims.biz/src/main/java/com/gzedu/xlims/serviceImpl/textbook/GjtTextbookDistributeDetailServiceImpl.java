package com.gzedu.xlims.serviceImpl.textbook;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.textbook.GjtTextbookDistributeDetailDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;
import com.gzedu.xlims.service.textbook.GjtTextbookDistributeDetailService;

@Service
public class GjtTextbookDistributeDetailServiceImpl implements GjtTextbookDistributeDetailService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookDistributeDetailServiceImpl.class);

	@Autowired
	private GjtTextbookDistributeDetailDao gjtTextbookDistributeDetailDao;

	@Override
	public void update(GjtTextbookDistributeDetail entity) {
		log.info("entity:[" + entity + "]");
		gjtTextbookDistributeDetailDao.save(entity);
	}

	@Override
	public void update(List<GjtTextbookDistributeDetail> entities) {
		log.info("entities:[" + entities + "]");
		gjtTextbookDistributeDetailDao.save(entities);
	}

	@Override
	public List<GjtTextbookDistributeDetail> findAll(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookDistributeDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbookDistributeDetail.class);
		return gjtTextbookDistributeDetailDao.findAll(spec);
	}

}
