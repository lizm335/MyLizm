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
import com.gzedu.xlims.dao.textbook.GjtTextbookDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookStockDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;
import com.gzedu.xlims.service.textbook.GjtTextbookService;

@Service
public class GjtTextbookServiceImpl implements GjtTextbookService {

	private static final Log log = LogFactory.getLog(GjtTextbookServiceImpl.class);

	@Autowired
	private GjtTextbookDao gjtTextbookDao;

	@Autowired
	private GjtTextbookStockDao gjtTextbookStockDao;

	@Override
	public Page<GjtTextbook> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		return gjtTextbookDao.findAll(searchParams, pageRequst);
	}

	@Override
	public List<GjtTextbook> findAll(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbook> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbook.class);
		return gjtTextbookDao.findAll(spec);
	}

	@Override
	public GjtTextbook findOne(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbook> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbook.class);
		return gjtTextbookDao.findOne(spec);
	}

	@Override
	public GjtTextbook findOne(String id) {
		log.info("id:[" + id + "]");
		return gjtTextbookDao.findOne(id);
	}

	@Override
	public List<GjtTextbook> findAll(Iterable<String> ids) {
		return gjtTextbookDao.findAll(ids);
	}

	@Override
	public GjtTextbook findByCode(String textbookCode, String orgId) {
		log.info("textbookCode:[" + textbookCode + "]");
		return gjtTextbookDao.findByCode(textbookCode, orgId);
	}

	@Override
	public GjtTextbook insert(GjtTextbook entity) {
		log.info("entity:[" + entity + "]");
		entity.setTextbookId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");

		GjtTextbook textbook = gjtTextbookDao.save(entity);

		// 初始化库存
		GjtTextbookStock textbookStock = new GjtTextbookStock();
		textbookStock.setStockId(UUIDUtils.random());
		textbookStock.setGjtTextbook(entity);
		gjtTextbookStockDao.save(textbookStock);

		return textbook;
	}

	@Override
	public void update(GjtTextbook entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookDao.save(entity);
	}

	@Override
	public List<GjtTextbook> findCurrentArrangeTextbook(String planId, String orgId) {
		return gjtTextbookDao.findCurrentArrangeTextbook(planId, orgId);
	}

	@Override
	public List<GjtTextbook> findUnsetTextBookByGradeId(String gradeId) {
		return gjtTextbookDao.findUnsetTextBookByGradeId(gradeId);
	}

	@Override
	public Page<GjtTextbook> findArrangeTextBook(Map<String, Object> searchParams, PageRequest pageRequest) {
		return gjtTextbookDao.findArrangeTextBook(searchParams, pageRequest);
	}

	@Override
	public List findByGradeSpecialtyId(String gradeSpecialtyId) {

		return gjtTextbookDao.findByGradeSpecialtyId(gradeSpecialtyId);
	}

	@Override
	public List findByGradeId(String gradeId, String gradeSpecialtyId) {

		return gjtTextbookDao.findByGradeId(gradeId, gradeSpecialtyId);
	}

	@Override
	public List<Map<String, Object>> findByDistributeId(String distributeId, String studentId) {

		return gjtTextbookDao.findByDistributeId(distributeId, studentId);
	}

}
