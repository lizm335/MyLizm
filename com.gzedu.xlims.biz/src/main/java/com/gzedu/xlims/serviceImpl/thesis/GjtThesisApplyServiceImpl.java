package com.gzedu.xlims.serviceImpl.thesis;

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
import com.gzedu.xlims.dao.thesis.GjtThesisApplyDao;
import com.gzedu.xlims.dao.thesis.GjtThesisStudentProgDao;
import com.gzedu.xlims.pojo.status.ThesisProgressCodeEnum;
import com.gzedu.xlims.pojo.status.ThesisStatusEnum;
import com.gzedu.xlims.pojo.thesis.GjtThesisApply;
import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;

@Service
public class GjtThesisApplyServiceImpl implements GjtThesisApplyService {

	private static final Log log = LogFactory.getLog(GjtThesisApplyServiceImpl.class);

	@Autowired
	private GjtThesisApplyDao gjtThesisApplyDao;

	@Autowired
	private GjtThesisStudentProgDao gjtThesisStudentProgDao;

	@Autowired
	GjtOrgDao gjtOrgDao;

	@Override
	public Page<GjtThesisApply> findAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");

		Criteria<GjtThesisApply> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.or(Restrictions.eq("gjtStudentInfo.xxId", orgId, true),
				Restrictions.in("gjtStudentInfo.xxzxId", orgList, true)));
		spec.addAll(Restrictions.parse(searchParams));

		return gjtThesisApplyDao.findAll(spec, pageRequst);
	}

	@Override
	public long count(Map<String, Object> searchParams) {
		log.info("searchParams:[" + searchParams + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtThesisApply> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtThesisApply.class);
		return gjtThesisApplyDao.count(spec);
	}

	@Override
	public GjtThesisApply insert(GjtThesisApply entity) {
		log.info("entity:[" + entity + "]");
		entity.setApplyId(UUIDUtils.random());
		entity.setStatus(0);
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");

		// 添加进度
		GjtThesisStudentProg prog = new GjtThesisStudentProg();
		prog.setProgressId(UUIDUtils.random());
		prog.setStudentId(entity.getStudentId());
		prog.setThesisPlanId(entity.getThesisPlanId());
		prog.setProgressCode(ThesisProgressCodeEnum.THESIS_APPLY.getCode());
		prog.setCreatedBy(entity.getCreatedBy());
		prog.setCreatedDt(new Date());
		gjtThesisStudentProgDao.save(prog);

		return gjtThesisApplyDao.save(entity);
	}

	@Override
	public void update(GjtThesisApply entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtThesisApplyDao.save(entity);
	}

	@Override
	public GjtThesisApply findOne(String id) {
		return gjtThesisApplyDao.findOne(id);
	}

	@Override
	public GjtThesisApply findByThesisPlanIdAndStudentId(String thesisPlanId, String studentId) {
		return gjtThesisApplyDao.findByThesisPlanIdAndStudentIdAndIsDeleted(thesisPlanId, studentId, "N");
	}

	@Override
	public List<GjtThesisApply> findByThesisPlanIdAndSpecialtyBaseId(String thesisPlanId, String specialtyBaseId) {
		return gjtThesisApplyDao.findByThesisPlanIdAndSpecialtyBaseId(thesisPlanId, specialtyBaseId);
	}

	@Override
	public List<GjtThesisApply> findIsApplyByStudentId(String studentId, String gradeId) {
		return gjtThesisApplyDao.findIsApplyByStudentId(studentId, gradeId);
	}

	@Override
	public GjtThesisApply findCompletedApply(String studentId) {
		return gjtThesisApplyDao.findByIsDeletedAndStudentIdAndStatus("N", studentId,
				ThesisStatusEnum.THESIS_COMPLETED.getValue());
	}

	@Override
	public boolean getCanApply(String orgId, String gradeId, String gradeSpecialtyId, String studentId) {
		List<Map<String, Object>> list = gjtThesisApplyDao.getCanApply(orgId, gradeId, gradeSpecialtyId, studentId);

		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public Float getScore(String orgId, String gradeId, String gradeSpecialtyId, String studentId) {
		List<Map<String, Object>> list = gjtThesisApplyDao.getCanApply(orgId, gradeId, gradeSpecialtyId, studentId);
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
		return gjtThesisApplyDao.findTeacherGuideList(orgId, searchParams, pageRequst);
	}

	@Override
	public void updateScore(String studentId, float score) {
		gjtThesisApplyDao.updateScore(studentId, score);
	}

	@Override
	public Page<GjtThesisApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		return gjtThesisApplyDao.queryMyGuideList(searchParams, pageRequst);
	}

}
