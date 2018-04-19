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
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookOrderDetailDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookOrderDetail;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookOrderDetailService;

@Deprecated @Service("bzrGjtTextbookOrderDetailServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookOrderDetailServiceImpl implements BzrGjtTextbookOrderDetailService {

	private static final Log log = LogFactory.getLog(GjtTextbookOrderDetailServiceImpl.class);

	@Autowired
	private GjtTextbookOrderDetailDao gjtTextbookOrderDetailDao;

	@Override
	public Page<Map<String, Object>> findAllSummary(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		return gjtTextbookOrderDetailDao.findAll(searchParams, pageRequst);
	}

	@Override
	public Page<BzrGjtTextbookOrderDetail> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbookOrderDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				BzrGjtTextbookOrderDetail.class);
		return gjtTextbookOrderDetailDao.findAll(spec, pageRequst);
	}

	@Override
	public List<BzrGjtTextbookOrderDetail> findAll(List<String> ids) {
		return gjtTextbookOrderDetailDao.findAll(ids);
	}

	@Override
	public BzrGjtTextbookOrderDetail insert(BzrGjtTextbookOrderDetail entity) {
		log.info("entity:[" + entity + "]");
		entity.setDetailId(UUIDUtils.random());
		entity.setNeedDistribute(1);
		entity.setStatus(0);
		entity.setCreatedDt(new Date());
		return gjtTextbookOrderDetailDao.save(entity);
	}

	@Override
	public void insert(List<BzrGjtTextbookOrderDetail> entities) {
		log.info("entities:[" + entities + "]");
		for (BzrGjtTextbookOrderDetail entity : entities) {
			entity.setDetailId(UUIDUtils.random());
			entity.setNeedDistribute(1);
			entity.setStatus(0);
			entity.setCreatedDt(new Date());
		}

		gjtTextbookOrderDetailDao.save(entities);
	}

	@Override
	public void update(BzrGjtTextbookOrderDetail entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookOrderDetailDao.save(entity);
	}

	@Override
	public void update(List<BzrGjtTextbookOrderDetail> entities) {
		log.info("entities:[" + entities + "]");
		for (BzrGjtTextbookOrderDetail entity : entities) {
			entity.setUpdatedDt(new Date());
		}

		gjtTextbookOrderDetailDao.save(entities);
	}

	@Override
	public BzrGjtTextbookOrderDetail findOne(String id) {
		return gjtTextbookOrderDetailDao.findOne(id);
	}

	@Override
	public void delete(BzrGjtTextbookOrderDetail entity) {
		log.info("entity:[" + entity + "]");
		gjtTextbookOrderDetailDao.delete(entity);
	}

	@Override
	public void delete(List<BzrGjtTextbookOrderDetail> entities) {
		log.info("entities:[" + entities + "]");
		gjtTextbookOrderDetailDao.delete(entities);
	}

	@Override
	public List<BzrGjtTextbookOrderDetail> findByStudentIdAndTextbookIdAndStatus(String studentId, String textbookId,
																				 int status) {
		return gjtTextbookOrderDetailDao.findByStudentIdAndTextbookIdAndStatus(studentId, textbookId, status);
	}

	@Override
	public BzrGjtTextbookOrderDetail findByStudentIdAndTextbookIdAndCourseIdAndPlanId(String studentId, String textbookId,
																					  String courseId, String planId) {
		return gjtTextbookOrderDetailDao.findByStudentIdAndTextbookIdAndCourseIdAndPlanId(studentId, textbookId,
				courseId, planId);
	}

}
