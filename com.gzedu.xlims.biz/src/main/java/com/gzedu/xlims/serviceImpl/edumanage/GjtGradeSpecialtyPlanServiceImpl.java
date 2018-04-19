package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.organization.GjtGradeSpecialtyPlanDao;
import com.gzedu.xlims.dao.organization.GjtStudyYearCourseDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtStudyYearCourse;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.system.StudyYear;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.pcourse.PCourseServer;

/**
 * 
 * 功能说明：年级专业 实施教学计划
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
@Service
public class GjtGradeSpecialtyPlanServiceImpl implements GjtGradeSpecialtyPlanService {
	@Autowired
	private GjtGradeSpecialtyPlanDao gjtGradeSpecialtyPlanDao;

	@Autowired
	private GjtStudyYearService gjtStudyYearService;

	@Autowired
	GjtStudyYearCourseDao gjtStudyyearCourseDao;

	@Autowired
	GjtCourseService gjtCourseService;

	@Autowired
	PCourseServer pCourseServer;

	@Override
	public boolean createGradeSpecialtyPlan(GjtGrade grade, GjtSpecialty specialty) {

		Date enterDt = grade.getEnterDt();// 开学时间

		List<GjtSpecialtyPlan> specialtyPlans = specialty.getGjtSpecialtyPlans();
		if (specialtyPlans == null || specialtyPlans.isEmpty())
			return true;
		List<GjtGradeSpecialtyPlan> gradeSpecialtyPlans = new ArrayList<GjtGradeSpecialtyPlan>();
		for (GjtSpecialtyPlan gjtSpecialtyPlan : specialtyPlans) {
			GjtGradeSpecialtyPlan gradeSpecialtyPlan = new GjtGradeSpecialtyPlan();
			gradeSpecialtyPlan.setId(UUIDUtils.random());

			gradeSpecialtyPlan.setTermTypeCode(gjtSpecialtyPlan.getTermTypeCode());

			int studyYearCode = StudyYear.getCode(enterDt, Integer.valueOf(gjtSpecialtyPlan.getTermTypeCode()));

			// 自动生成学年度
			GjtStudyYearInfo studyYearInfo = gjtStudyYearService.queryByStudyYearCodeAndXxId(studyYearCode,
					gjtSpecialtyPlan.getXxId());

			GjtCourse gjtCourse = gjtCourseService.queryBy(gjtSpecialtyPlan.getCourseId());

			// 自动生成学期课程
			GjtStudyYearCourse studyyearCourse = gjtStudyYearService.queryByCourseAndStudyYearInfo(gjtCourse,
					studyYearInfo);

			gradeSpecialtyPlan.setStudyYearId(studyYearInfo.getId());
			gradeSpecialtyPlan.setStudyYearCourseId(studyyearCourse.getId());
			gradeSpecialtyPlan.setStudyYearCode(studyYearCode);
			gradeSpecialtyPlan.setGradeId(grade.getGradeId());
			gradeSpecialtyPlan.setCounselorId(gjtSpecialtyPlan.getCounselorId());
			gradeSpecialtyPlan.setCourseCategory(gjtSpecialtyPlan.getCourseCategory());
			gradeSpecialtyPlan.setCourseId(gjtSpecialtyPlan.getCourseId());
			gradeSpecialtyPlan.setCourseTypeId(gjtSpecialtyPlan.getCourseTypeId());
			gradeSpecialtyPlan.setCreatedBy(gjtSpecialtyPlan.getCreatedBy());
			gradeSpecialtyPlan.setCreatedDt(new Date());
			gradeSpecialtyPlan.setExamRatio(gjtSpecialtyPlan.getExamRatio());
			gradeSpecialtyPlan.setExamType(gjtSpecialtyPlan.getExamType());
			gradeSpecialtyPlan.setHours(gjtSpecialtyPlan.getHours());
			// gradeSpecialtyPlan.setScore(gjtSpecialtyPlan.getScore());
			gradeSpecialtyPlan.setSpecialtyId(gjtSpecialtyPlan.getSpecialtyId());
			gradeSpecialtyPlan.setStudyRatio(gjtSpecialtyPlan.getStudyRatio());
			gradeSpecialtyPlan.setXxId(gjtSpecialtyPlan.getXxId());

			List<GjtTextbook> gjtTextbookList1 = gjtSpecialtyPlan.getGjtTextbookList1();
			List<GjtTextbook> gjtTextbookList2 = gjtSpecialtyPlan.getGjtTextbookList2();

			gradeSpecialtyPlan.setGjtTextbookList1(gjtTextbookList1);
			gradeSpecialtyPlan.setGjtTextbookList2(gjtTextbookList2);

			gradeSpecialtyPlans.add(gradeSpecialtyPlan);
		}
		gjtGradeSpecialtyPlanDao.save(gradeSpecialtyPlans);

		return true;
	}

	@Override
	public List<GjtGradeSpecialtyPlan> queryGradeSpecialtyPlan(String gradeId, String gjtSpecialtyId) {
		return gjtGradeSpecialtyPlanDao.findByGradeIdAndSpecialtyIdOrderByCreatedDtAsc(gradeId, gjtSpecialtyId);
	}

	@Override
	public Page<GjtGradeSpecialtyPlan> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, schoolId));

		Specification<GjtGradeSpecialtyPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtGradeSpecialtyPlan.class);

		return gjtGradeSpecialtyPlanDao.findAll(spec, pageRequst);
	}

	@Override
	public void removeGradeSpecialtyPlan(String gradeId, String gjtSpecialtyId) {
		List<GjtGradeSpecialtyPlan> list = queryGradeSpecialtyPlan(gradeId, gjtSpecialtyId);
		if (list != null && !list.isEmpty())
			gjtGradeSpecialtyPlanDao.delete(list);
	}

	@Override
	public GjtGradeSpecialtyPlan queryBy(String id) {
		return gjtGradeSpecialtyPlanDao.findOne(id);
	}

	@Override
	public void delete(Iterable<String> ids) {
		for (String id : ids) {
			gjtGradeSpecialtyPlanDao.delete(id);
		}
	}

	@Override
	public void delete(String id) {
		gjtGradeSpecialtyPlanDao.delete(id);
	}

	@Override
	public void insert(GjtGradeSpecialtyPlan entity) {
		entity.setId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		gjtGradeSpecialtyPlanDao.save(entity);
	}

	@Override
	public void update(GjtGradeSpecialtyPlan entity) {
		gjtGradeSpecialtyPlanDao.save(entity);
	}

	public void groupBy() {

	}

}
