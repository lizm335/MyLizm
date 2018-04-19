package com.ouchgzee.headTeacher.serviceImpl.graduation;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.ouchgzee.headTeacher.dao.graduation.GjtGraduationApplyDao;
import com.ouchgzee.headTeacher.dao.graduation.GjtGraduationDao;
import com.ouchgzee.headTeacher.dao.graduation.GjtGraduationNativeDao;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationApply;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationApplyService;

@Deprecated @Service("bzrGjtGraduationApplyServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtGraduationApplyServiceImpl implements BzrGjtGraduationApplyService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationApplyServiceImpl.class);
	
	@Autowired
	private GjtGraduationApplyDao gjtGraduationApplyDao;

	@Autowired
	private GjtGraduationNativeDao gjtGraduationNativeDao;

	@Autowired
	private GjtGraduationDao gjtGraduationDao;

	@Override
	public List<BzrGjtGraduationApply> queryListBySpecialty(String batchId, String specialtyId) {
		log.info("batchId:" + batchId + ", specialtyId:" + specialtyId);
		return gjtGraduationApplyDao.queryListBySpecialty(batchId, specialtyId);
	}

	@Override
	public void update(List<BzrGjtGraduationApply> gjtGraduationApplyList) {
		log.info("gjtGraduationApplyList:[" + gjtGraduationApplyList + "]");
		gjtGraduationApplyDao.save(gjtGraduationApplyList);
	}

	@Override
	public Page<Map<String, Object>> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		//Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		//Specification<GjtGraduationApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtGraduationApply.class);
		return gjtGraduationNativeDao.queryGraduationApply(searchParams, pageRequst);
	}

	@Override
	public Map<String, Object> queryGraduationApplyCount(String batchId, int applyType, int teacherType,
			String teacherId) {
		log.info("batchId:" + batchId + ", applyType:" + applyType + ", teacherType:" + teacherType + ", teacherId:" + teacherId);
		return gjtGraduationNativeDao.queryGraduationApplyCount(batchId, applyType, teacherType, teacherId);
	}

	@Override
	public Page<BzrGjtGraduationApply> queryPage(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtGraduationApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtGraduationApply.class);
		return gjtGraduationApplyDao.findAll(spec, pageRequst);
	}

	@Override
	public BzrGjtGraduationApply queryOneByStudent(String batchId, String studentId, int applyType) {
		return gjtGraduationApplyDao.queryOneByStudent(batchId, studentId, applyType);
	}

	@Override
	public void update(BzrGjtGraduationApply entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtGraduationApplyDao.save(entity);
	}

	@Override
	public BzrGjtGraduationApply queryOne(String applyId) {
		log.info("applyId:" + applyId);
		return gjtGraduationApplyDao.findOne(applyId);
	}

	@Override
	public List<BzrGjtGraduationApply> queryList(String batchId, String specialtyId, int applyType, int defenceType, Set<Integer> status) {
		log.info("batchId:" + batchId + ", specialtyId:" + specialtyId);
		return gjtGraduationApplyDao.queryList(batchId, specialtyId, applyType, defenceType, status);
	}

	@Override
	public Page<BzrGjtGraduationApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		return gjtGraduationDao.queryMyGuideList(searchParams, pageRequst);
	}

}
