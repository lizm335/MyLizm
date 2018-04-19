package com.ouchgzee.headTeacher.serviceImpl.textbook;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookDistributeDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookDistribute;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookDistributeService;

@Deprecated @Service("bzrGjtTextbookDistributeServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookDistributeServiceImpl implements BzrGjtTextbookDistributeService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookDistributeServiceImpl.class);

	@Autowired
	private GjtTextbookDistributeDao gjtTextbookDistributeDao;

	@Override
	public void update(BzrGjtTextbookDistribute entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookDistributeDao.save(entity);
	}

	@Override
	public void update(List<BzrGjtTextbookDistribute> entities) {
		log.info("entities:[" + entities + "]");
		
		for (BzrGjtTextbookDistribute entity : entities) {
			entity.setUpdatedDt(new Date());
		}
		
		gjtTextbookDistributeDao.save(entities);
	}

	@Override
	public BzrGjtTextbookDistribute findByOrderCodeAndStatusAndIsDeleted(String orderCode, int status,
																		 String isDeleted) {
		log.info("orderCode:" + orderCode + ", status:" + status + ", isDeleted:" + isDeleted);
		return gjtTextbookDistributeDao.findByOrderCodeAndStatusAndIsDeleted(orderCode, status, isDeleted);
	}

	@Override
	public List<BzrGjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted,
																				 Collection<Integer> statuses) {
		log.info("studentId:" + studentId + ", statuses:" + statuses + ", isDeleted:" + isDeleted);
		return gjtTextbookDistributeDao.findByStudentIdAndIsDeletedAndStatusIn(studentId, isDeleted, statuses);
	}

	@Override
	public List<BzrGjtTextbookDistribute> findByIsDeletedAndStatusIn(String orgId, String classId, String isDeleted,
																	 Collection<Integer> statuses) {
		log.info("orgId:" + orgId + ", statuses:" + statuses + ", isDeleted:" + isDeleted);
		return gjtTextbookDistributeDao.findByIsDeletedAndStatusIn(orgId, classId, isDeleted, statuses);
	}

	@Override
	public Page<Map<String, Object>> findDistributeSummary(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "]");
		return gjtTextbookDistributeDao.findDistributeSummary(searchParams, pageRequst);
	}

	@Override
	public int queryCurrentDistributeCount(String orgId, int studyYearCode) {
		log.info("orgId:" + orgId + ", studyYearCode:" + studyYearCode);
		return gjtTextbookDistributeDao.queryCurrentDistributeCount(orgId, studyYearCode);
	}

	@Override
	public List<Object[]> queryCurrentDistributeList(String orgId, int studyYearCode, Boolean isEnough) {
		log.info("orgId:" + orgId + ", studyYearCode:" + studyYearCode);
		return gjtTextbookDistributeDao.queryCurrentDistributeList(orgId, studyYearCode, isEnough);
	}

	@Override
	public List<Object[]> queryStudentCurrentDistributeList(String orgId, int studyYearCode) {
		log.info("orgId:" + orgId + ", studyYearCode:" + studyYearCode);
		return gjtTextbookDistributeDao.queryStudentCurrentDistributeList(orgId, studyYearCode);
	}

}
