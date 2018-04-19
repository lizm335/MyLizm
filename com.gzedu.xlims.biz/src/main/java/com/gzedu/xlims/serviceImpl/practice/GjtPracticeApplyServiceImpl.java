package com.gzedu.xlims.serviceImpl.practice;

import java.math.BigDecimal;
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

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.practice.GjtPracticeApplyDao;
import com.gzedu.xlims.dao.practice.GjtPracticeStudentProgDao;
import com.gzedu.xlims.pojo.practice.GjtPracticeApply;
import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;
import com.gzedu.xlims.pojo.status.PracticeProgressCodeEnum;
import com.gzedu.xlims.pojo.status.PracticeStatusEnum;
import com.gzedu.xlims.service.practice.GjtPracticeApplyService;

@Service
public class GjtPracticeApplyServiceImpl implements GjtPracticeApplyService {

	private static final Log log = LogFactory.getLog(GjtPracticeApplyServiceImpl.class);

	@Autowired
	private GjtPracticeApplyDao gjtPracticeApplyDao;

	@Autowired
	private GjtPracticeStudentProgDao gjtPracticeStudentProgDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Override
	public Page<GjtPracticeApply> findAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");

		Criteria<GjtPracticeApply> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.or(Restrictions.eq("gjtStudentInfo.xxId", orgId, true),
				Restrictions.in("gjtStudentInfo.xxId", orgList, true)));
		spec.addAll(Restrictions.parse(searchParams));

		return gjtPracticeApplyDao.findAll(spec, pageRequst);
	}

	@Override
	public long count(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtPracticeApply> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtPracticeApply.class);
		return gjtPracticeApplyDao.count(spec);
	}

	@Override
	public GjtPracticeApply insert(GjtPracticeApply entity) {
		log.info("entity:[" + entity + "]");
		entity.setApplyId(UUIDUtils.random());
		entity.setStatus(0);
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");

		// 添加进度
		GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
		prog.setProgressId(UUIDUtils.random());
		prog.setStudentId(entity.getStudentId());
		prog.setPracticePlanId(entity.getPracticePlanId());
		prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_APPLY.getCode());
		prog.setCreatedBy(entity.getCreatedBy());
		prog.setCreatedDt(new Date());
		gjtPracticeStudentProgDao.save(prog);

		return gjtPracticeApplyDao.save(entity);
	}

	@Override
	public void update(GjtPracticeApply entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtPracticeApplyDao.save(entity);
	}

	@Override
	public GjtPracticeApply findOne(String id) {
		return gjtPracticeApplyDao.findOne(id);
	}

	@Override
	public GjtPracticeApply findByPracticePlanIdAndStudentId(String practicePlanId, String studentId) {
		return gjtPracticeApplyDao.findByPracticePlanIdAndStudentIdAndIsDeleted(practicePlanId, studentId, "N");
	}

	@Override
	public List<GjtPracticeApply> findPracticePlanIdByStudentId(String studentId, String gradeId) {
		return gjtPracticeApplyDao.findPracticePlanIdByStudentId(studentId, gradeId);
	}

	@Override
	public List<GjtPracticeApply> findByPracticePlanIdAndSpecialtyBaseId(String practicePlanId,
			String specialtyBaseId) {
		return gjtPracticeApplyDao.findByPracticePlanIdAndSpecialtyBaseId(practicePlanId, specialtyBaseId);
	}

	@Override
	public GjtPracticeApply findCompletedApply(String studentId) {
		return gjtPracticeApplyDao.findByIsDeletedAndStudentIdAndStatus("N", studentId,
				PracticeStatusEnum.PRACTICE_COMPLETED.getValue());
	}

	@Override
	public boolean getCanApply(String orgId, String gradeId, String gradeSpecialtyId, String studentId) {
		List<Map<String, Object>> list = gjtPracticeApplyDao.getCanApply(orgId, gradeId, gradeSpecialtyId, studentId);

		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public Float getScore(String orgId, String gradeId, String gradeSpecialtyId, String studentId) {
		List<Map<String, Object>> list = gjtPracticeApplyDao.getCanApply(orgId, gradeId, gradeSpecialtyId, studentId);
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				Object score = map.get("score");
				if (score != null) {
					float floatValue = ((BigDecimal) score).floatValue();
					if (floatValue >= 60) {
						return floatValue;
					}
				}
			}

		}

		return 0f;
	}

	@Override
	public Page<Map<String, Object>> findTeacherGuideList(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		return gjtPracticeApplyDao.findTeacherGuideList(orgId, searchParams, pageRequst);
	}

	@Override
	public void updateScore(String studentId, float score) {
		gjtPracticeApplyDao.updateScore(studentId, score);
	}

	@Override
	public Page<GjtPracticeApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		return gjtPracticeApplyDao.queryMyGuideList(searchParams, pageRequst);
	}

}
