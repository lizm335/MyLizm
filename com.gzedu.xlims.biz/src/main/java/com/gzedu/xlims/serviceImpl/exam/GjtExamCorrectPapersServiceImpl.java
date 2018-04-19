package com.gzedu.xlims.serviceImpl.exam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.edumanage.GjtRecResultDao;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamCorrectPapersDao;
import com.gzedu.xlims.dao.exam.GjtExamRecordNewDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamCorrectPapers;
import com.gzedu.xlims.service.exam.GjtExamCorrectPapersService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 报告批改业务逻辑类
 */
@Service
public class GjtExamCorrectPapersServiceImpl extends BaseServiceImpl<GjtExamCorrectPapers> implements GjtExamCorrectPapersService {

	@Autowired
	private GjtExamCorrectPapersDao gjtExamCorrectPapersDao;

	@Autowired
	private GjtRecResultDao gjtRecResultDao;

	@Autowired
	private GjtExamRecordNewDao gjtExamRecordNewDao;

	@Autowired
	private GjtExamAppointmentNewDao gjtExamAppointmentNewDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Override
	protected BaseDao<GjtExamCorrectPapers, String> getBaseDao() {
		return this.gjtExamCorrectPapersDao;
	}

	@Override
	public GjtExamCorrectPapers findByStudentIdAndExamPlanId(String studentId, String examPlanId) {
		return gjtExamCorrectPapersDao.findByGjtStudentInfoStudentIdAndGjtExamPlanNewExamPlanIdAndIsDeleted(studentId, examPlanId, "N");
	}

	@Override
	public Page<GjtExamCorrectPapers> findAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<GjtExamCorrectPapers> spec = new Criteria<GjtExamCorrectPapers>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));

		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamCorrectPapersDao.findAll(spec, pageRequest);
	}

	@Override
	public long count(String orgId, Map<String, Object> searchParams) {
		Criteria<GjtExamCorrectPapers> spec = new Criteria<GjtExamCorrectPapers>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));

		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamCorrectPapersDao.count(spec);
	}

	@Override
	public boolean update(GjtExamCorrectPapers entity) {
		entity.setUpdatedDt(new Date());
		return gjtExamCorrectPapersDao.save(entity) != null;
	}

	@Override
	public boolean updateScore(String id, int score, String correctBy) {
		GjtExamCorrectPapers info = super.queryById(id);
		//报告已批改，不能重复批改！
		if(info.getCorrectState() == 1 || info.getCorrectState() == 2) {
			return false;
		}
		Date now = new Date();
		// 1.更新报告批改信息
		info.setScore(score);
		if(score >= 60) {
			info.setCorrectState(1);
		} else {
			info.setCorrectState(2);
		}
		info.setCorrectBy(correctBy);
		info.setCorrectDt(now);
		info.setUpdatedBy(correctBy);
		info.setUpdatedDt(now);
		gjtExamCorrectPapersDao.save(info);
		
		// 2.更新考试预约的成绩
		GjtExamAppointmentNew examAppointmentNew = gjtExamAppointmentNewDao.findByStudentIdAndExamPlanId(info.getGjtStudentInfo().getStudentId(), info.getGjtExamPlanNew().getExamPlanId());
		examAppointmentNew.setExamScore(score + "");
		if(score >= 60) {
			examAppointmentNew.setExamStatus(1 + "");
		} else {
			examAppointmentNew.setExamStatus(2 + "");
		}
		examAppointmentNew.setUpdatedBy(correctBy);
		examAppointmentNew.setUpdatedDt(now);
		gjtExamAppointmentNewDao.save(examAppointmentNew);

		// 3.更新选课的考试成绩
		GjtRecResult recResult = gjtRecResultDao.findOne(info.getRecId());
		// 大于60分已通过
		String examState = null, status = null;
		Double get_credits = null;
		if (score >= 60) {
			examState = "1";
			status = "0";
			get_credits = recResult.getGjtTeachPlan().getXf();
		} else {
			examState = "0";
			status = "1";
		}

		Map searchParams = new HashMap();
		double impStudyScore = recResult.getExamScore() != null ? recResult.getExamScore().doubleValue() : 0;
		int impSumScore = ToolUtil.getIntRound(impStudyScore * info.getGjtExamPlanNew().getXkPercent() / 100 + score * (100 - info.getGjtExamPlanNew().getXkPercent()) / 100);
		searchParams.put("EXAM_SCORE", impStudyScore);
		searchParams.put("EXAM_SCORE1", score);
		searchParams.put("EXAM_SCORE2", impSumScore);
		searchParams.put("EXAM_STATE", examState);
		searchParams.put("REC_ID", info.getRecId());
		searchParams.put("GET_CREDITS", get_credits);
		searchParams.put("COURSE_SCHEDULE", info.getGjtExamPlanNew().getXkPercent());

		int num = gjtExamRecordNewDao.updRecRegister(searchParams);
		if (num>0) {
			searchParams.put("REPAIR_ID", SequenceUUID.getSequence());
			searchParams.put("TEACH_PLAN_ID", recResult.getTeachPlanId());
			searchParams.put("XCX_SCORE", impStudyScore);
			searchParams.put("ZJX_SCORE", score);
			searchParams.put("ZCJ_SCORE", impSumScore);
			searchParams.put("STATUS", status);
			searchParams.put("STUDENT_ID", info.getGjtStudentInfo().getStudentId());
			searchParams.put("XH", info.getGjtStudentInfo().getXh());
			searchParams.put("COURSE_CODE", recResult.getGjtCourse().getKch());
			searchParams.put("RATIO", info.getGjtExamPlanNew().getXkPercent());
			searchParams.put("EXAM_CODE", info.getGjtExamPlanNew().getExamNo());
			searchParams.put("EXAM_BATCH_CODE", info.getExamBatchCode());
			searchParams.put("PROGRESS", recResult.getProgress());
			searchParams.put("SUBJECT_NAME", info.getGjtExamPlanNew().getExamPlanName());
			if (EmptyUtils.isNotEmpty(gjtExamRecordNewDao.queryLearnRepair(searchParams))) {
				gjtExamRecordNewDao.updLearnRepair(searchParams);
			} else {
				gjtExamRecordNewDao.addRecRegister(searchParams);
			}
		}
		return true;
	}
}
