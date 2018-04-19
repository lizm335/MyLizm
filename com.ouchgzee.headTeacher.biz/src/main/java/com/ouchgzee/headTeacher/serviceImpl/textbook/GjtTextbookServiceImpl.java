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
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookDao;
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookStockDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbook;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStock;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookService;

@Deprecated @Service("bzrGjtTextbookServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookServiceImpl implements BzrGjtTextbookService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookServiceImpl.class);

	@Autowired
	private GjtTextbookDao gjtTextbookDao;
	
	@Autowired
	private GjtTextbookStockDao gjtTextbookStockDao;

	@Override
	public Page<BzrGjtTextbook> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		return gjtTextbookDao.findAll(searchParams, pageRequst);
	}

	@Override
	public List<BzrGjtTextbook> findAll(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbook> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtTextbook.class);
		return gjtTextbookDao.findAll(spec);
	}

	@Override
	public BzrGjtTextbook findOne(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbook> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtTextbook.class);
		return gjtTextbookDao.findOne(spec);
	}

	@Override
	public BzrGjtTextbook findOne(String id) {
		log.info("id:[" + id + "]");
		return gjtTextbookDao.findOne(id);
	}

	@Override
	public BzrGjtTextbook findByCode(String textbookCode, String orgId) {
		log.info("textbookCode:[" + textbookCode + "]");
		return gjtTextbookDao.findByCode(textbookCode, orgId);
	}

	@Override
	public BzrGjtTextbook insert(BzrGjtTextbook entity) {
		log.info("entity:[" + entity + "]");
		entity.setTextbookId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		BzrGjtTextbook textbook = gjtTextbookDao.save(entity);
		
		//初始化库存
		BzrGjtTextbookStock textbookStock = new BzrGjtTextbookStock();
		textbookStock.setStockId(UUIDUtils.random());
		textbookStock.setGjtTextbook(entity);
		gjtTextbookStockDao.save(textbookStock);
		
		return textbook;
	}

	@Override
	public void update(BzrGjtTextbook entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookDao.save(entity);
	}

}
