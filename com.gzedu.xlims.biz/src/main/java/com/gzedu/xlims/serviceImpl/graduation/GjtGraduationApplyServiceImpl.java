package com.gzedu.xlims.serviceImpl.graduation;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationApplyDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationNativeDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GjtGraduationApplyServiceImpl extends BaseServiceImpl<GjtGraduationApply> implements GjtGraduationApplyService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationApplyServiceImpl.class);
	
	@Autowired
	private GjtGraduationApplyDao gjtGraduationApplyDao;

	@Autowired
	private GjtGraduationNativeDao gjtGraduationNativeDao;

	@Autowired
	private GjtGraduationDao gjtGraduationDao;

	@Autowired
	public GjtOrgDao gjtOrgDao;

	@Override
	protected BaseDao<GjtGraduationApply, String> getBaseDao() {
		return this.gjtGraduationApplyDao;
	}

	@Override
	public List<GjtGraduationApply> queryListBySpecialty(String batchId, String specialtyId) {
		log.info("batchId:" + batchId + ", specialtyId:" + specialtyId);
		return gjtGraduationApplyDao.queryListBySpecialty(batchId, specialtyId);
	}

	@Override
	public void update(List<GjtGraduationApply> gjtGraduationApplyList) {
		log.info("gjtGraduationApplyList:[" + gjtGraduationApplyList + "]");
		gjtGraduationApplyDao.save(gjtGraduationApplyList);
	}

	@Override
	public Page<GjtGraduationApply> queryGraduationApplyByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
        searchParamsNew.putAll(searchParams);
		Criteria<GjtGraduationApply> spec = new Criteria();
		// 设置连接方式，如果是内连接可省略掉
		//spec.setJoinType("gjtStudyCenter", JoinType.LEFT);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		// 机构ID
		String orgId = (String) searchParamsNew.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
        spec.addAll(Restrictions.parse(searchParamsNew));

		Page<GjtGraduationApply> pageInfos = gjtGraduationApplyDao.findAll(spec, pageRequest);
		return pageInfos;
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
	public Page<GjtGraduationApply> queryPage(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtGraduationApply> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtGraduationApply.class);
		return gjtGraduationApplyDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtGraduationApply queryOneByStudent(String batchId, String studentId, int applyType) {
		return gjtGraduationApplyDao.queryOneByStudent(batchId, studentId, applyType);
	}

	@Override
	public void update(GjtGraduationApply entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtGraduationApplyDao.save(entity);
	}

	@Override
	public GjtGraduationApply queryOne(String applyId) {
		log.info("applyId:" + applyId);
		return gjtGraduationApplyDao.findOne(applyId);
	}

	@Override
	public List<GjtGraduationApply> queryList(String batchId, String specialtyId, int applyType, int defenceType, Set<Integer> status) {
		log.info("batchId:" + batchId + ", specialtyId:" + specialtyId);
		return gjtGraduationApplyDao.queryList(batchId, specialtyId, applyType, defenceType, status);
	}

	@Override
	public Page<GjtGraduationApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		return gjtGraduationDao.queryMyGuideList(searchParams, pageRequst);
	}

	@Override
	public GjtGraduationApply findByStudentIdAndApplyTypeAndStatusGreaterThanEqual(String studentId, int applyType, int status) {
		return gjtGraduationApplyDao.findByIsDeletedAndGjtStudentInfoStudentIdAndApplyTypeAndStatusGreaterThanEqual("N", studentId, applyType, status);
	}

}
