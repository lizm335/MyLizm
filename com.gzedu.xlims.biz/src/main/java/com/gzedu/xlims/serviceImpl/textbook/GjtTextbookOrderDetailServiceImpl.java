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
import com.gzedu.xlims.dao.textbook.GjtTextbookOrderDetailDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;
import com.gzedu.xlims.service.textbook.GjtTextbookOrderDetailService;

@Service
public class GjtTextbookOrderDetailServiceImpl implements GjtTextbookOrderDetailService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookOrderDetailServiceImpl.class);
	
	@Autowired
	private GjtTextbookOrderDetailDao gjtTextbookOrderDetailDao;

	@Override
	public Page<Map<String, Object>> findAllSummary(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		return gjtTextbookOrderDetailDao.findAll(searchParams, pageRequst);
	}

	@Override
	public Page<GjtTextbookOrderDetail> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookOrderDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbookOrderDetail.class);
		return gjtTextbookOrderDetailDao.findAll(spec, pageRequst);
	}

	@Override
	public List<GjtTextbookOrderDetail> findAll(List<String> ids) {
		return gjtTextbookOrderDetailDao.findAll(ids);
	}

	@Override
	public GjtTextbookOrderDetail insert(GjtTextbookOrderDetail entity) {
		log.info("entity:[" + entity + "]");
		entity.setDetailId(UUIDUtils.random());
		entity.setNeedDistribute(1);
		entity.setStatus(0);
		entity.setCreatedDt(new Date());
		return gjtTextbookOrderDetailDao.save(entity);
	}

	@Override
	public void insert(List<GjtTextbookOrderDetail> entities) {
		log.info("entities:[" + entities + "]");
		for (GjtTextbookOrderDetail entity : entities) {
			entity.setDetailId(UUIDUtils.random());
			entity.setNeedDistribute(1);
			entity.setStatus(0);
			entity.setCreatedDt(new Date());
		}
		
		gjtTextbookOrderDetailDao.save(entities);
	}

	@Override
	public void update(GjtTextbookOrderDetail entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookOrderDetailDao.save(entity);
	}

	@Override
	public void update(List<GjtTextbookOrderDetail> entities) {
		log.info("entities:[" + entities + "]");
		for (GjtTextbookOrderDetail entity : entities) {
			entity.setUpdatedDt(new Date());
		}
		
		gjtTextbookOrderDetailDao.save(entities);
	}

	@Override
	public GjtTextbookOrderDetail findOne(String id) {
		return gjtTextbookOrderDetailDao.findOne(id);
	}

	@Override
	public void delete(GjtTextbookOrderDetail entity) {
		log.info("entity:[" + entity + "]");
		gjtTextbookOrderDetailDao.delete(entity);
	}

	@Override
	public void delete(List<GjtTextbookOrderDetail> entities) {
		log.info("entities:[" + entities + "]");
		gjtTextbookOrderDetailDao.delete(entities);
	}

	@Override
	public List<GjtTextbookOrderDetail> findByStudentIdAndTextbookIdAndStatus(String studentId, String textbookId,
			int status) {
		return gjtTextbookOrderDetailDao.findByStudentIdAndTextbookIdAndStatus(studentId, textbookId, status);
	}

	@Override
	public GjtTextbookOrderDetail findByStudentIdAndTextbookIdAndCourseIdAndPlanId(String studentId, String textbookId, String courseId, String planId) {
		return gjtTextbookOrderDetailDao.findByStudentIdAndTextbookIdAndCourseIdAndPlanId(studentId, textbookId, courseId, planId);
	}

	@Override
	public List<Map<String, String>> queryCurrentDistributeList(String planId, int textbookType) {
		return gjtTextbookOrderDetailDao.queryCurrentDistributeList(planId, textbookType);
	}

	@Override
	public List<Map<String, String>> queryCurrentDistributeList2(String orgId) {
		return gjtTextbookOrderDetailDao.queryCurrentDistributeList2(orgId);
	}

}
