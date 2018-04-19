/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.edumanage.GjtStudyYearInfoDao;
import com.gzedu.xlims.dao.organization.GjtStudyYearCourseDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtStudyYearCourse;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.dto.YearCourseDto;
import com.gzedu.xlims.pojo.system.StudyYear;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;

/**
 * 
 * 功能说明：学年度基础信息
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtStudyYearServiceImpl implements GjtStudyYearService {

	@Autowired
	CommonMapService commonMapService;

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager em;

	@Autowired
	GjtStudyYearInfoDao gjtStudyYearInfoDao;

	@Autowired
	GjtStudyYearCourseDao gjtStudyyearCourseDao;

	@Override
	public Page<GjtStudyYearInfo> queryAll(String orgId, Map<String, Object> map, PageRequest pageRequest) {
		Map<String, SearchFilter> filters = SearchFilter.parse(map);

		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, orgId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));

		Specification<GjtStudyYearInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtStudyYearInfo.class);
		return gjtStudyYearInfoDao.findAll(spec, pageRequest);
	}

	@Override
	public Boolean saveEntity(GjtStudyYearInfo entity) {
		GjtStudyYearInfo info = gjtStudyYearInfoDao.save(entity);
		if (info != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean updateEntity(GjtStudyYearInfo entity) {
		GjtStudyYearInfo info = gjtStudyYearInfoDao.save(entity);
		if (info != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GjtStudyYearInfo queryById(String id) {
		return gjtStudyYearInfoDao.findOne(id);
	}

	@Override
	public List<GjtStudyYearInfo> queryAll(String orgId) {
		return gjtStudyYearInfoDao.findByXxIdAndIsDeleted(orgId, "N");
	}

	@Override
	public Page<YearCourseDto> queryYearCourse(String orgId, Map<String, Object> searchMap, PageRequest pageRequest) {

		StringBuffer sqlCount = new StringBuffer("select count(*) from (");
		StringBuffer sql = new StringBuffer(
				"select a.study_year_code yearId, a.course_id courseId, b.kcmc courseName, d.xm coachTeach "
						+ ", e.xxmc  schoolName  from GJT_GRADE_SPECIALTY_PLAN a, gjt_course b,  "
						+ "gjt_employee_info d, gjt_school_info  e  where a.course_id = b.course_id  and a.xx_id=e.id"
						+ "   and d.employee_id=b.employee_id  and a.xx_id='" + orgId + "'");

		Map<String, String> map = new HashMap<String, String>();
		String yearCode = (String) searchMap.get("EQ_studyYearCode");
		if (StringUtils.isNotBlank(yearCode)) {
			sql.append(" and a.study_year_code  = :yearCode ");
			map.put("yearCode", "" + yearCode + "");
		}
		String courseName = (String) searchMap.get("LIKE_courseName");
		if (StringUtils.isNotBlank(courseName)) {
			sql.append(" and b.kcmc LIKE :courseName");
			map.put("courseName", "%" + courseName + "%");
		}

		String coachTeach = (String) searchMap.get("LIKE_coachTeach");
		if (StringUtils.isNotBlank(courseName)) {
			sql.append(" and d.xm LIKE :coachTeach");
			map.put("coachTeach", "%" + coachTeach + "%");
		}

		sql.append("  group by a.study_year_code, a.course_id, b.kcmc, d.xm,e.xxmc");

		Query queryTotal = em.createNativeQuery(sqlCount.toString() + sql.toString() + " ) newtable");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			queryTotal.setParameter(entry.getKey(), entry.getValue());
		}

		Query query = em.createNativeQuery(sql.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(YearCourseDto.class));
		// query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		query.setFirstResult(pageRequest.getOffset());
		query.setMaxResults(pageRequest.getPageSize());
		List<YearCourseDto> resultList = query.getResultList();

		Page<YearCourseDto> page = new PageImpl<YearCourseDto>(resultList, pageRequest,
				NumberUtils.toLong(queryTotal.getSingleResult().toString()));
		return page;
	}

	/**
	 * 学年度课程管理 根据院校ID,课程ID，学年度ID 查询每个学年度的课程开设了多少个班级
	 */
	@Override
	public Integer queryCount(String schoolId, String course_id, int study_year_code) {
		String sql = "select count(0) from gjt_class_info a where a.class_type = 'course' and a.teach_plan_id in ("
				+ " select a.id from GJT_GRADE_SPECIALTY_PLAN a,gjt_course b   " + " where a.course_id = b.course_id "
				+ "and a.course_id = '" + course_id + "' and a.study_year_code = " + study_year_code + " and a.xx_id='"
				+ schoolId + "' ) and a.xx_id='" + schoolId + "'";
		Query query = em.createNativeQuery(sql.toString());
		int num = NumberUtils.toInt(query.getSingleResult().toString());
		return num;
	}

	@Override
	public synchronized GjtStudyYearInfo queryByStudyYearCodeAndXxId(int studyYearCode, String xxId) {
		GjtStudyYearInfo studyYearInfo = gjtStudyYearInfoDao.findByStudyYearCodeAndXxId(studyYearCode, xxId);

		if (studyYearInfo == null) {
			studyYearInfo = new GjtStudyYearInfo();
			studyYearInfo.setId(UUIDUtils.random());
			studyYearInfo.setStudyYearCode(studyYearCode);
			studyYearInfo.setStudyYearName(StudyYear.getName(studyYearCode));
			studyYearInfo.setXxId(xxId);
			studyYearInfo.setCreatedBy("admin");
			studyYearInfo.setCreatedDt(new Date());

			Date startDate = StudyYear.getStartDate(studyYearCode);
			Date endDate = StudyYear.getEndDate(studyYearCode);

			studyYearInfo.setStudyyearStartDate(startDate);
			studyYearInfo.setStudyyearEndDate(endDate);
			this.saveEntity(studyYearInfo);
		}
		return studyYearInfo;
	}

	@Override
	public GjtStudyYearInfo queryByStudyYearCodeAndXxId2(int studyYearCode, String xxId) {
		GjtStudyYearInfo studyYearInfo = gjtStudyYearInfoDao.findByStudyYearCodeAndXxId(studyYearCode, xxId);
		return studyYearInfo;
	}

	@Override
	public synchronized GjtStudyYearCourse queryByCourseAndStudyYearInfo(GjtCourse course,
			GjtStudyYearInfo studyYearInfo) {
		GjtStudyYearCourse studyyearCourse = gjtStudyyearCourseDao.findByCourseIdAndStudyYearId(course.getCourseId(),
				studyYearInfo.getId());

		if (studyyearCourse == null) {
			studyyearCourse = new GjtStudyYearCourse();
			studyyearCourse.setId(UUIDUtils.random());
			studyyearCourse.setCourse(course);
			studyyearCourse.setStudyYearInfo(studyYearInfo);
			gjtStudyyearCourseDao.save(studyyearCourse);
		}
		return studyyearCourse;
	}

	/**
	 * 每个学年度开设了多少个课程
	 */
	@Override
	public Integer queryCourseCount(int studyYearCode) {
		String sql = "select count(0) from gjt_grade_specialty_plan gg where gg.study_year_code=" + studyYearCode + "";
		Query query = em.createNativeQuery(sql.toString());
		int num = NumberUtils.toInt(query.getSingleResult().toString());
		return num;
	}

	@Override
	public List<GjtStudyYearInfo> queryByStudyStartDateAfter(Date studyyearDt) {
		return gjtStudyYearInfoDao.findByStudyyearStartDateAfterOrderByStudyyearStartDateAsc(studyyearDt);
	}

	@Override
	public GjtStudyYearInfo queryByBeginDateAndTermCode(Date beginDate, int termCode) {
		return null;
	}

	@Override
	public void delete(Iterable<String> ids) {
		for (String id : ids) {
			gjtStudyYearInfoDao.delete(id);
		}
	}
	@Override
	public void delete(String id) {
		gjtStudyYearInfoDao.delete(id);
	}

}
