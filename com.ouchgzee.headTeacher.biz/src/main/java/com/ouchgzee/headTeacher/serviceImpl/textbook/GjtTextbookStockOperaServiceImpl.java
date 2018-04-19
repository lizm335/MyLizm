package com.ouchgzee.headTeacher.serviceImpl.textbook;

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
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookStockOperaDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStockOpera;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookStockOperaService;

@Deprecated @Service("bzrGjtTextbookStockOperaServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookStockOperaServiceImpl implements BzrGjtTextbookStockOperaService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookStockOperaServiceImpl.class);

	@Autowired
	private GjtTextbookStockOperaDao gjtTextbookStockOperaDao;

	@Override
	public BzrGjtTextbookStockOpera insert(BzrGjtTextbookStockOpera entity) {
		log.info("entity:[" + entity + "]");
		entity.setOperaId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtTextbookStockOperaDao.save(entity);
	}

	@Override
	public List<BzrGjtTextbookStockOpera> insert(List<BzrGjtTextbookStockOpera> entities) {
		log.info("entities:[" + entities + "]");
		
		for (BzrGjtTextbookStockOpera opera : entities) {
			opera.setOperaId(UUIDUtils.random());
			opera.setCreatedDt(new Date());
			opera.setIsDeleted("N");
		}
		
		return gjtTextbookStockOperaDao.save(entities);
	}

	@Override
	public Page<BzrGjtTextbookStockOpera> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbookStockOpera>  spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtTextbookStockOpera.class);
		return gjtTextbookStockOperaDao.findAll(spec, pageRequst);
	}

}
