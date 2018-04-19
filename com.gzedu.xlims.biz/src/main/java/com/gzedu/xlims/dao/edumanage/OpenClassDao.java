package com.gzedu.xlims.dao.edumanage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.openClass.OnlineLessonVo;

@Repository
public class OpenClassDao extends BaseDaoImpl {

	@Autowired
	private CommonDao commonDao;

	/**
	 * 查询开课管理列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryGraduationSpecialtyList(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ").append("     gtp.ACTUAL_GRADE_ID as \"termId\",")
				.append("     gg.GRADE_NAME as \"termName\",").append("     gc.COURSE_ID as \"courseId\",")
				.append("     gc.KCH as \"courseCode\",").append("     gc.KCMC as \"courseName\",")
				.append("     count(distinct grr.REC_ID) as \"studentCount\",")
				.append("     count(distinct gci.CLASS_ID) as \"classCount\" ").append(" from ")
				.append("      GJT_GRADE gg, ").append("      GJT_COURSE gc,  ").append("      VIEW_TEACH_PLAN gtp ")
				.append(" left join ").append("      GJT_CLASS_INFO gci ").append(" on ")
				.append("      gci.IS_DELETED='N' and gci.ACTUAL_GRADE_ID = gtp.ACTUAL_GRADE_ID and gci.COURSE_ID = gtp.COURSE_ID ")
				.append(" left join ").append("      GJT_REC_RESULT grr ").append(" on ")
				.append("      grr.IS_DELETED='N' and grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID ").append(" where ")
				.append("      gg.GRADE_ID = gtp.ACTUAL_GRADE_ID ").append("      and gc.COURSE_ID = gtp.COURSE_ID ")
				.append("      and gtp.IS_DELETED='N' ").append("      and gg.IS_DELETED='N' ")
				.append("      and gc.IS_DELETED='N' ");

		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null && StringUtils.isNotBlank((String) orgId)) {
			sb.append(" and gtp.XX_ID = :orgId ");
			map.put("orgId", orgId);
		}

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId != null && StringUtils.isNotBlank(gradeId.toString())) {
			sb.append(" and gtp.ACTUAL_GRADE_ID = :gradeId ");
			map.put("gradeId", gradeId);
		}

		Object courseId = searchParams.get("EQ_courseId");
		if (courseId != null && StringUtils.isNotBlank(courseId.toString())) {
			sb.append(" and gc.COURSE_ID = :courseId ");
			map.put("courseId", courseId);
		}

		Object courseCode = searchParams.get("EQ_courseCode");
		if (courseCode != null && StringUtils.isNotBlank((String) courseCode)) {
			sb.append(" and gc.KCH = :kch ");
			map.put("kch", courseCode);
		}

		Object course = searchParams.get("LIKE_course");
		if (course != null && StringUtils.isNotBlank((String) course)) {
			sb.append(" and (gc.KCH = :kch OR gc.KCMC like :course) ");
			map.put("kch", course);
			map.put("course", "%" + course + "%");
		}

		sb.append(" group by gtp.ACTUAL_GRADE_ID, gg.GRADE_NAME, gg.START_DATE, gc.COURSE_ID, gc.KCH, gc.KCMC");
		sb.append(" order by gg.START_DATE desc");

		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}

	/**
	 * 条件查询可开设的课程，SQL语句
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map> queryCanCourseBy(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("");
		querySql.append(
				"  select distinct c.COURSE_ID,c.KCH,c.KCMC,c.WSJXZK,c.COURSE_NATURE,c.PYCC,c.CATEGORY,c.SUBJECT,c.SYHY,c.SYZY,c.HOUR,c.IS_ENABLED ");
		querySql.append("  from GJT_SPECIALTY_COLLEGE t");
		querySql.append("  inner join GJT_SPECIALTY_PLAN b on b.specialty_id=t.specialty_id");
		querySql.append("  inner join Gjt_Course c on c.course_id=b.course_id and c.is_deleted='N'");
		querySql.append("  ");
		querySql.append("  where t.org_id=:xxId");
		querySql.append(
				"  and not exists (select 1 from VIEW_TEACH_PLAN x where x.is_deleted='N' and x.course_id=c.course_id AND x.actual_grade_id=:termId AND x.xx_id=:xxId)");

		parameters.put("termId", searchParams.get("termId"));
		parameters.put("xxId", searchParams.get("xxId"));
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 条件查询拥有某课程的所有专业，SQL语句
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map> querySpecialtyByCourse(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("");
		querySql.append(
				"  select t.SPECIALTY_ID,t.SPECIALTY_LEVEL PYCC,b.COURSE_TYPE_ID,b.TERM_TYPE_CODE,b.COURSE_CATEGORY,c.COURSE_ID,c.CREDIT,c.HOUR");
		querySql.append("  from GJT_SPECIALTY_COLLEGE t");
		querySql.append("  inner join GJT_SPECIALTY_PLAN b on b.specialty_id=t.specialty_id");
		querySql.append("  inner join Gjt_Course c on c.course_id=b.course_id and c.is_deleted='N'");
		querySql.append("  where t.org_id=:xxId");
		querySql.append("  AND c.course_id=:courseId");

		parameters.put("courseId", searchParams.get("courseId"));
		parameters.put("xxId", searchParams.get("xxId"));
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 查询期课程列表
	 * 
	 * @param searchParams
	 * @return
	 */
	public List queryTermCoureList(Map searchParams) {
		Map param = queryTermCoureListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return commonDao.queryForMapListNative(sql, param);
	}

	/**
	 * 查询期课程列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryTermCoureList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map param = queryTermCoureListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return (Page) commonDao.queryForPageNative(sql, param, pageRequst);
	}

	/**
	 * 任课教师平台查询期课程列表
	 *
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月17日 下午6:01:16
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryTermCoureListByTeacher(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map param = queryTermCoureListSQLByTeacher(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return (Page) commonDao.queryForPageNative(sql, param, pageRequst);
	}

	/**
	 * 查询期课程列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Map queryTermCoureListSQL(Map searchParams) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GTC.TERMCOURSE_ID,");
		sql.append("  GTC.COURSE_ID,");
		sql.append("  GTC.TERM_ID,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GCE.KCH,GCE.is_open,");
		sql.append("  GCE.KCMC,");
		sql.append("  GTC.COPY_FLG,");
		sql.append("  GCE.IS_ENABLED,");
		sql.append("  GTC.TASK_COUNT,");
		sql.append("  GTC.TERM_TASK_COUNT,");
		sql.append("  NVL(GTC.SYNC_STATUS, 'N') SYNC_STATUS,");
		sql.append(
				" (SELECT GEI.XM FROM GJT_EMPLOYEE_INFO GEI WHERE GEI.EMPLOYEE_ID=GTC.CLASS_TEACHER) CLASS_TEACHER,");
		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append(
				"  FROM GJT_REC_RESULT GRR, GJT_STUDENT_INFO GSI, VIEW_TEACH_PLAN GTP,GJT_CLASS_INFO    GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GRR.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GTC.TERM_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = GTP.COURSE_ID) CHOOSE_COUNT,");
		sql.append("  (SELECT COUNT(GCI.CLASS_ID)");
		sql.append("  FROM GJT_CLASS_INFO GCI");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.TERMCOURSE_ID = GTC.TERMCOURSE_ID) CLASS_COUNT");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC, GJT_GRADE GRE, GJT_COURSE GCE");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERM_ID = GRE.GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = GCE.COURSE_ID");

		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null && StringUtils.isNotBlank((String) orgId)) {
			sql.append(" and GTC.XX_ID = :orgId ");
			map.put("orgId", orgId);
		}

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId != null && StringUtils.isNotBlank(gradeId.toString())) {
			sql.append(" and GTC.TERM_ID = :gradeId ");
			map.put("gradeId", gradeId);
		}

		Object courseId = searchParams.get("EQ_courseId");
		if (courseId != null && StringUtils.isNotBlank(courseId.toString())) {
			sql.append(" and GCE.COURSE_ID = :courseId ");
			map.put("courseId", courseId);
		}

		Object courseCode = searchParams.get("EQ_courseCode");
		if (courseCode != null && StringUtils.isNotBlank((String) courseCode)) {
			sql.append(" and GCE.KCH = :kch ");
			map.put("kch", courseCode);
		}

		Object course = searchParams.get("LIKE_course");
		if (course != null && StringUtils.isNotBlank((String) course)) {
			sql.append(" and (GCE.KCH = :kch OR GCE.KCMC like :course) ");
			map.put("kch", course);
			map.put("course", "%" + course + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COPY_FLG")))) {
			sql.append("  AND GTC.COPY_FLG = :COPY_FLG");
			map.put("COPY_FLG", ObjectUtils.toString(searchParams.get("COPY_FLG")));
		}

		sql.append("  ) TAB ORDER BY CHOOSE_COUNT DESC");

		map.put("sql", sql);
		return map;
	}

	/**
	 * 查询期课程列表
	 *
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map queryTermCoureListSQLByTeacher(Map searchParams) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GTC.TERMCOURSE_ID,");
		sql.append("  GTC.COURSE_ID,");
		sql.append("  GTC.TERM_ID,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GCE.KCH,");
		sql.append("  GCE.KCMC,");
		sql.append("  (TO_DATE(GRE.COURSE_END_DATE) - TO_DATE(SYSDATE)) END_DAYS,");
		sql.append(
				"  (SELECT GEI.XM FROM GJT_EMPLOYEE_INFO GEI WHERE GEI.EMPLOYEE_ID=GTC.CLASS_TEACHER) TEACHER_NAME,");
		sql.append("  (CASE");
		sql.append("  WHEN SYSDATE < GRE.COURSE_START_DATE THEN");
		sql.append("  0");
		sql.append("  WHEN SYSDATE > GRE.COURSE_END_DATE THEN");
		sql.append("  2");
		sql.append("  ELSE");
		sql.append("  1");
		sql.append("  END) STATUS,");
		sql.append("  ");
		sql.append(" (SELECT COUNT(GRR.REC_ID)");
		sql.append(" FROM GJT_REC_RESULT GRR,");
		sql.append(" GJT_STUDENT_INFO GSI,");
		sql.append(" VIEW_TEACH_PLAN GTP");
		sql.append(" WHERE GRR.IS_DELETED = 'N'");
		sql.append(" AND GSI.IS_DELETED = 'N'");
		sql.append(" AND GTP.IS_DELETED = 'N'");
		sql.append(" AND GRR.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append(" AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append(" AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append(" AND GTC.TERM_ID = GTP.ACTUAL_GRADE_ID");
		sql.append(" AND GTC.COURSE_ID = GTP.COURSE_ID) CHOOSE_COUNT,");
		sql.append(" ");
		sql.append(" (SELECT AVG(VSS.EXAM_SCORE)");
		sql.append(" FROM GJT_REC_RESULT GRR");
		sql.append(" LEFT JOIN VIEW_STUDENT_STUDY_SITUATION VSS");
		sql.append(" ON GRR.REC_ID = VSS.CHOOSE_ID");
		sql.append(" WHERE GRR.IS_DELETED = 'N'");
		sql.append(" AND GRR.TERMCOURSE_ID = GTC.TERMCOURSE_ID) as EXAM_SCORE_AVG,");
		sql.append(" (SELECT AVG(VSS.PROGRESS)");
		sql.append(" FROM GJT_REC_RESULT GRR");
		sql.append(" LEFT JOIN VIEW_STUDENT_STUDY_SITUATION VSS");
		sql.append(" ON GRR.REC_ID = VSS.CHOOSE_ID");
		sql.append(" WHERE GRR.IS_DELETED = 'N'");
		sql.append(" AND GRR.TERMCOURSE_ID = GTC.TERMCOURSE_ID) as  PROGRESS_AVG");
		sql.append(" ");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC, GJT_GRADE GRE, GJT_COURSE GCE");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERM_ID = GRE.GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = GCE.COURSE_ID");

		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null && StringUtils.isNotBlank((String) orgId)) {
			sql.append(" and GTC.XX_ID = :orgId ");
			map.put("orgId", orgId);
		}

		Object classTeacher = searchParams.get("EQ_classTeacher");
		if (classTeacher != null && StringUtils.isNotBlank((String) classTeacher)) {
			sql.append(" and GTC.class_teacher = :classTeacher ");
			map.put("classTeacher", classTeacher);
		}

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId != null && StringUtils.isNotBlank(gradeId.toString())) {
			sql.append(" and GTC.TERM_ID = :gradeId ");
			map.put("gradeId", gradeId);
		}

		List<String> gradeIds = (List<String>) searchParams.get("IN_gradeIds");
		if (CollectionUtils.isNotEmpty(gradeIds)) {
			sql.append(" and GTC.TERM_ID in (:gradeIds) ");
			map.put("gradeIds", gradeIds);
		}

		Object course = searchParams.get("LIKE_course");
		if (course != null && StringUtils.isNotBlank((String) course)) {
			sql.append(" and (GCE.KCH = :kch OR GCE.KCMC like :course) ");
			map.put("kch", course);
			map.put("course", "%" + course + "%");
		}

		List<String> termCourseIds = (List<String>) searchParams.get("NOTIN_termCourseIds");
		if (CollectionUtils.isNotEmpty(termCourseIds)) {
			sql.append(" and GTC.TERMCOURSE_ID not in (:termCourseIds) ");
			map.put("termCourseIds", termCourseIds);
		}

		sql.append("  ) TAB WHERE 1=1 ");

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank((String) status)) {
			sql.append(" and TAB.status = :status ");
			map.put("status", status);
		}

		Object teacherName = searchParams.get("LIKE_teacherName");
		if (StringUtils.isNotBlank((String) teacherName)) {
			sql.append(" and TAB.TEACHER_NAME like :teacherName ");
			map.put("teacherName", "%" + teacherName + "%");
		}

		// sql.append(" ORDER BY CHOOSE_COUNT DESC");

		map.put("sql", sql);
		return map;
	}

	/**
	 * 条件查询可开设的课程，SQL语句
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map> getCourseList(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");

		sql.append("  SELECT TAB.GRADE_ID,");
		sql.append("  TAB.COURSE_ID,");
		sql.append("  TAB.XX_ID,");
		sql.append("  GS.KCH,");
		sql.append("  GS.KCMC,");
		sql.append("  GS.WSJXZK,");
		sql.append("  GS.COURSE_NATURE,");
		sql.append("  GS.PYCC,");
		sql.append("  GS.CATEGORY,");
		sql.append("  GS.SUBJECT,");
		sql.append("  GS.SYHY,");
		// sql.append(" (SELECT GSY.ZYMC");
		// sql.append(" FROM GJT_SPECIALTY GSY");
		// sql.append(" WHERE GSY.IS_DELETED = 'N'");
		// sql.append(" AND GSY.SPECIALTY_ID = GS.SYZY) SYZY,");
		sql.append("  GS.HOUR,");
		sql.append("  GS.IS_ENABLED,gs.is_open ");
		sql.append("  FROM (SELECT GGE.GRADE_ID, GCE.COURSE_ID, GGE.XX_ID");
		sql.append("  FROM VIEW_TEACH_PLAN GTP, GJT_COURSE GCE, GJT_GRADE GGE, GJT_GRADE_SPECIALTY GGS");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GGS.IS_DELETED = 'N'");
		sql.append("  AND GGS.STATUS = 1");
		sql.append("  AND GTP.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GTP.XX_ID = :XX_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = :ACTUAL_GRADE_ID");
		sql.append("  GROUP BY GGE.GRADE_ID, GCE.COURSE_ID, GGE.XX_ID) TAB");
		sql.append("  LEFT JOIN GJT_COURSE GS ON GS.COURSE_ID = TAB.COURSE_ID");
		sql.append("  AND GS.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_TERM_COURSEINFO GTC ON GTC.TERM_ID = TAB.GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = TAB.COURSE_ID");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  WHERE 1 = 1");
		sql.append("  AND GS.COURSE_ID IS NOT NULL");
		sql.append("  AND GTC.TERMCOURSE_ID IS NULL");

		parameters.put("XX_ID", searchParams.get("xxId"));
		parameters.put("ACTUAL_GRADE_ID", searchParams.get("termId"));

		return super.findAllByToMap(sql, parameters, null);
	}

	/**
	 * 查询期课程是否需要新增同步
	 * 
	 * @return
	 */
	public List getTermCourseInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT TAB.GRADE_ID,");
		sql.append("  TAB.COURSE_ID,");
		sql.append("  TAB.XX_ID,");
		sql.append("  TAB.TERMCOURSE_ID,");
		sql.append("  LTC.TERMCOURSE_ID AS PCOURSE_TERMCOURSE_ID");
		sql.append("  FROM (SELECT GGE.GRADE_ID,");
		sql.append("  GCE.COURSE_ID,");
		sql.append("  GGE.XX_ID,");
		sql.append("  (SELECT GTC.TERMCOURSE_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERM_ID = GGE.GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND ROWNUM = 1) TERMCOURSE_ID");
		sql.append("  FROM VIEW_TEACH_PLAN GTP, GJT_COURSE GCE, GJT_GRADE GGE");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GTP.XX_ID = :XX_ID");
		sql.append("  AND GTP.COURSE_ID = :COURSE_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = :TERM_ID");

		sql.append("  GROUP BY GGE.GRADE_ID, GCE.COURSE_ID, GGE.XX_ID) TAB");
		sql.append("  LEFT JOIN LCMS_TERM_COURSEINFO LTC ON TAB.TERMCOURSE_ID = LTC.TERMCOURSE_ID");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 初始化期课程数据
	 * 
	 * @return
	 */
	@Transactional
	public int addTermCourse(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_TERM_COURSEINFO");
		sql.append("  (TERMCOURSE_ID, COURSE_ID, TERM_ID, XX_ID)");
		sql.append("  VALUES");
		sql.append("  (:TERMCOURSE_ID, :COURSE_ID, :TERM_ID, :XX_ID)");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		param.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 更新期课程同步状态
	 * 
	 * @return
	 */
	@Transactional
	public int updTermCourseSync(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append(
				"  UPDATE GJT_TERM_COURSEINFO GTC SET GTC.SYNC_STATUS = 'Y', GTC.UPDATED_DT = SYSDATE WHERE GTC.TERMCOURSE_ID = :TERMCOURSE_ID");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 查询期课程下面的班级是否创建
	 * 
	 * @return
	 */
	public List getTermClassInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GTC.TERMCOURSE_ID,");
		sql.append("  GTC.COURSE_ID,");
		sql.append("  GTC.TERM_ID,");
		sql.append("  GTC.XX_ID,");
		sql.append("  (SELECT GCI.CLASS_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.AOTU_FLG = 'Y'");
		sql.append("  AND GCI.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND ROWNUM = 1) TERM_CLASS_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 初始化课程班数据
	 * 
	 * @return
	 */
	@Transactional
	public int addCourseClass(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_CLASS_INFO");
		sql.append("  (CLASS_ID,");
		sql.append("  BH,");
		sql.append("  BJMC,");
		sql.append("  TERM_ID,");
		sql.append("  CLASS_TYPE,");
		sql.append("  COURSE_ID,");
		sql.append("  XX_ID,");
		sql.append("  ACTUAL_GRADE_ID,");
		sql.append("  AOTU_FLG,");
		sql.append("  LIMIT_NUM,");
		sql.append("  OLD_TERMCOURSE_ID,");
		sql.append("  TURN_TERM_FLG,");
		sql.append("  TERMCOURSE_ID)");
		sql.append("  VALUES");
		sql.append("  (:CLASS_ID,");
		sql.append("  :BH,");
		sql.append("  :BJMC,");
		sql.append("  :TERM_ID,");
		sql.append("  :CLASS_TYPE,");
		sql.append("  :COURSE_ID,");
		sql.append("  :XX_ID,");
		sql.append("  :ACTUAL_GRADE_ID,");
		sql.append("  :AOTU_FLG,");
		sql.append("  :LIMIT_NUM,");
		sql.append("  :OLD_TERMCOURSE_ID,");
		sql.append("  :TURN_TERM_FLG,");
		sql.append("  :TERMCOURSE_ID)");

		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		param.put("BH", ObjectUtils.toString(searchParams.get("BH")));
		param.put("BJMC", ObjectUtils.toString(searchParams.get("BJMC")));
		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("CLASS_TYPE", ObjectUtils.toString(searchParams.get("CLASS_TYPE")));
		param.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("ACTUAL_GRADE_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("AOTU_FLG", ObjectUtils.toString(searchParams.get("AOTU_FLG")));
		param.put("LIMIT_NUM", ObjectUtils.toString(searchParams.get("LIMIT_NUM")));
		param.put("OLD_TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("OLD_TERMCOURSE_ID")));
		if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("TURN_TERM_FLG")))) {
			param.put("TURN_TERM_FLG", "N");
		} else {
			param.put("TURN_TERM_FLG", ObjectUtils.toString(searchParams.get("TURN_TERM_FLG")));
		}
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 修改课程班数据
	 * 
	 * @return
	 */
	@Transactional
	public int updCourseClass(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_CLASS_INFO GCI");
		sql.append("  SET GCI.BH              = :BH,");
		sql.append("  GCI.BJMC            = :BJMC,");
		sql.append("  GCI.TERM_ID         = :TERM_ID,");
		sql.append("  GCI.COURSE_ID       = :COURSE_ID,");
		sql.append("  GCI.ACTUAL_GRADE_ID = :ACTUAL_GRADE_ID,");
		sql.append("  GCI.AOTU_FLG        = :AOTU_FLG,");
		sql.append("  GCI.LIMIT_NUM       = :LIMIT_NUM,");
		sql.append("  GCI.TERMCOURSE_ID   = :TERMCOURSE_ID,");
		sql.append("  GCI.UPDATED_DT      = SYSDATE,");
		sql.append("  GCI.UPDATED_BY      = '初始化期课程班级'");
		sql.append("  WHERE GCI.CLASS_ID = :CLASS_ID");

		param.put("BH", ObjectUtils.toString(searchParams.get("BH")));
		param.put("BJMC", ObjectUtils.toString(searchParams.get("BJMC")));
		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		param.put("ACTUAL_GRADE_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("AOTU_FLG", ObjectUtils.toString(searchParams.get("AOTU_FLG")));
		param.put("LIMIT_NUM", ObjectUtils.toString(searchParams.get("LIMIT_NUM")));
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 修改课程班同步状态
	 * 
	 * @return
	 */
	@Transactional
	public int updClassSync(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append(
				"  UPDATE GJT_CLASS_INFO GCI SET GCI.SYNC_STATUS = 'Y', GCI.UPDATED_DT = SYSDATE WHERE GCI.CLASS_ID = :CLASS_ID");

		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 修改课程班同步学习网状态
	 * 
	 * @return
	 */
	@Transactional
	public int updXxwSyncStatus(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE FROM GJT_CLASS_INFO GCI");
		sql.append("  SET GCI.XXW_SYNC_STATUS = :XXW_SYNC_STATUS, GCI.XXW_SYNC_MSG = :XXW_SYNC_MSG");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_ID = :CLASS_ID");

		param.put("XXW_SYNC_STATUS", ObjectUtils.toString(searchParams.get("XXW_SYNC_STATUS")));
		param.put("XXW_SYNC_MSG", ObjectUtils.toString(searchParams.get("XXW_SYNC_MSG")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 是否复制个数
	 *
	 * @param searchParams
	 * @return
	 */
	public int getCopyCount(Map searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");

		sql.append("  SELECT COUNT(GTC.TERMCOURSE_ID) COPY_COUNT");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TEMP_COPY_FLG")))) {
			sql.append("  AND GTC.COPY_FLG = :COPY_FLG");
			parameters.put("COPY_FLG", ObjectUtils.toString(searchParams.get("TEMP_COPY_FLG")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gradeId")))) {
			sql.append("  AND GTC.TERM_ID = :gradeId");
			parameters.put("gradeId", ObjectUtils.toString(searchParams.get("EQ_gradeId")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_orgId")))) {
			sql.append("  AND GTC.XX_ID = :orgId");
			parameters.put("orgId", ObjectUtils.toString(searchParams.get("EQ_orgId")));
		}
		int copy_count = 0;
		List tempList = super.findAllByToMap(sql, parameters, null);
		if (EmptyUtils.isNotEmpty(tempList)) {
			Map cMap = (Map) tempList.get(0);
			copy_count = Integer.parseInt(ObjectUtils.toString(cMap.get("COPY_COUNT")));
		}
		return copy_count;
	}

	/**
	 * 查询班级列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getClassList(Map searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GTC.TERMCOURSE_ID,");
		sql.append("  GCI.CLASS_ID,");
		sql.append("  GCI.BJMC,");
		sql.append("  GGE.GRADE_ID,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  GCE.COURSE_ID,");
		sql.append("  GCE.KCH,");
		sql.append("  GCE.KCMC,");
		sql.append("  (SELECT GEI.XM");
		sql.append("  FROM GJT_EMPLOYEE_INFO GEI");
		sql.append("  WHERE GEI.IS_DELETED = 'N'");
		sql.append("  AND GEI.EMPLOYEE_ID = GCI.BZR_ID) FD_TEACHER_NAME,");
		sql.append("  (SELECT GEI.XM");
		sql.append("  FROM GJT_EMPLOYEE_INFO GEI");
		sql.append("  WHERE GEI.IS_DELETED = 'N'");
		sql.append("  AND GEI.EMPLOYEE_ID = GCI.SUPERVISOR_ID) DD_TEACHER_NAME,");
		sql.append("  (SELECT COUNT(GCS.CLASS_STUDENT_ID)");
		sql.append("  FROM GJT_CLASS_STUDENT GCS, GJT_STUDENT_INFO GSI");
		sql.append("  WHERE GCS.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID) STUDENT_COUNT,");
		sql.append("  TO_CHAR(GGE.COURSE_START_DATE, 'yyyy-mm-dd') START_DATE,");
		sql.append("  TO_CHAR(GGE.COURSE_END_DATE, 'yyyy-mm-dd') END_DATE,");
		sql.append("  (CASE");
		sql.append("  WHEN TO_CHAR(GGE.COURSE_START_DATE, 'yyyy-mm-dd') >");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') THEN");
		sql.append("  '1'");
		sql.append("  WHEN TO_CHAR(GGE.COURSE_START_DATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(GGE.COURSE_END_DATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') THEN");
		sql.append("  '2'");
		sql.append("  WHEN TO_CHAR(GGE.COURSE_END_DATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') THEN");
		sql.append("  '3'");
		sql.append("  ELSE");
		sql.append("  ''");
		sql.append("  END) TIME_FLG,");
		sql.append("  abs(ceil(GGE.COURSE_END_DATE - SYSDATE)) XX_DAY,");
		sql.append("  abs(ceil(GGE.COURSE_START_DATE - SYSDATE)) WAIT_DAY");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_COURSE          GCE,");
		sql.append("  GJT_GRADE           GGE");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");

		sql.append(" and GTC.XX_ID = :XX_ID ");
		map.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" and GCI.XXZX_ID = :XXZX_ID ");
			map.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  AND GTC.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("  AND GTC.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = GGE.GRADE_ID) TAB");
		sql.append("  WHERE 1 = 1");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRADE_ID = :GRADE_ID");
			map.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND COURSE_ID = :COURSE_ID");
			map.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND BJMC LIKE '%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%'");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("DD_TEACHER_NAME")))) {
			sql.append(
					"  AND DD_TEACHER_NAME LIKE '%" + ObjectUtils.toString(searchParams.get("DD_TEACHER_NAME")) + "%'");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("FD_TEACHER_NAME")))) {
			sql.append(
					"  AND FD_TEACHER_NAME LIKE '%" + ObjectUtils.toString(searchParams.get("FD_TEACHER_NAME")) + "%'");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TIME_FLG")))) {
			sql.append("  AND TIME_FLG = :TIME_FLG");
			map.put("TIME_FLG", ObjectUtils.toString(searchParams.get("TIME_FLG")));
		} else if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NOTEACHER")))) {
			if ("1".equals(ObjectUtils.toString(searchParams.get("NOTEACHER")))) {
				sql.append("  AND DD_TEACHER_NAME IS NULL");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("NOTEACHER")))) {
				sql.append("  AND FD_TEACHER_NAME IS NULL");
			}
		}
		sql.append("  ORDER BY GRADE_NAME, FD_TEACHER_NAME desc, BJMC");
		return commonDao.queryForPageNative(sql.toString(), map, pageRequst);
	}

	/**
	 * 查询课程班级统计
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int getClassCount(Map searchParams) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT COUNT(*)");
		sql.append("  FROM (SELECT GTC.TERMCOURSE_ID,");
		sql.append("  GCI.CLASS_ID,");
		sql.append("  GCI.BJMC,");
		sql.append("  GGE.GRADE_ID,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  GCE.COURSE_ID,");
		sql.append("  GCE.KCH,");
		sql.append("  GCE.KCMC,");
		sql.append("  (SELECT GEI.XM");
		sql.append("  FROM GJT_EMPLOYEE_INFO GEI");
		sql.append("  WHERE GEI.IS_DELETED = 'N'");
		sql.append("  AND GEI.EMPLOYEE_ID = GCI.BZR_ID) FD_TEACHER_NAME,");
		sql.append("  (SELECT GEI.XM");
		sql.append("  FROM GJT_EMPLOYEE_INFO GEI");
		sql.append("  WHERE GEI.IS_DELETED = 'N'");
		sql.append("  AND GEI.EMPLOYEE_ID = GCI.SUPERVISOR_ID) DD_TEACHER_NAME,");
		sql.append("  (SELECT COUNT(GCS.CLASS_STUDENT_ID)");
		sql.append("  FROM GJT_CLASS_STUDENT GCS, GJT_STUDENT_INFO GSI");
		sql.append("  WHERE GCS.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID) STUDENT_COUNT,");
		sql.append("  TO_CHAR(GGE.COURSE_START_DATE, 'yyyy-mm-dd') START_DATE,");
		sql.append("  TO_CHAR(GGE.COURSE_END_DATE, 'yyyy-mm-dd') END_DATE,");
		sql.append("  (CASE");
		sql.append("  WHEN TO_CHAR(GGE.COURSE_START_DATE, 'yyyy-mm-dd') >");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') THEN");
		sql.append("  '1'");
		sql.append("  WHEN TO_CHAR(GGE.COURSE_START_DATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(GGE.COURSE_END_DATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') THEN");
		sql.append("  '2'");
		sql.append("  WHEN TO_CHAR(GGE.COURSE_END_DATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') THEN");
		sql.append("  '3'");
		sql.append("  ELSE");
		sql.append("  ''");
		sql.append("  END) TIME_FLG,");
		sql.append("  abs(ceil(GGE.COURSE_END_DATE - SYSDATE)) XX_DAY,");
		sql.append("  abs(ceil(GGE.COURSE_START_DATE - SYSDATE)) WAIT_DAY");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_COURSE          GCE,");
		sql.append("  GJT_GRADE           GGE");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");

		sql.append(" and GTC.XX_ID = :XX_ID ");
		map.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" and GCI.XXZX_ID = :XXZX_ID ");
			map.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  AND GTC.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("  AND GTC.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = GGE.GRADE_ID) TAB");
		sql.append("  WHERE 1 = 1");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRADE_ID = :GRADE_ID");
			map.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND COURSE_ID = :COURSE_ID");
			map.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND BJMC LIKE '%:BJMC%'");
			map.put("BJMC", ObjectUtils.toString(searchParams.get("BJMC")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("DD_TEACHER_NAME")))) {
			sql.append(
					"  AND DD_TEACHER_NAME LIKE '%" + ObjectUtils.toString(searchParams.get("DD_TEACHER_NAME")) + "%'");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("FD_TEACHER_NAME")))) {
			sql.append(
					"  AND FD_TEACHER_NAME LIKE '%" + ObjectUtils.toString(searchParams.get("FD_TEACHER_NAME")) + "%'");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TIME_FLG")))) {
			sql.append("  AND TIME_FLG = :TIME_FLG");
			map.put("TIME_FLG", ObjectUtils.toString(searchParams.get("TIME_FLG")));
		}

		if ("1".equals(ObjectUtils.toString(searchParams.get("NOTEACHER")))) {
			sql.append("  AND DD_TEACHER_NAME IS NULL");
		} else if ("2".equals(ObjectUtils.toString(searchParams.get("NOTEACHER")))) {
			sql.append("  AND FD_TEACHER_NAME IS NULL");
		}

		BigDecimal num = (BigDecimal) commonDao.queryObjectNative(sql.toString(), map);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	/**
	 * 获取开设课程的选课人数
	 * 
	 * @return
	 */
	public List getCourseChooseCount(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT VTP.ACTUAL_GRADE_ID TERM_ID,");
		sql.append("  VTP.COURSE_ID,");
		sql.append("  COUNT(DISTINCT GSI.STUDENT_ID) CHOOSE_COUNT");
		sql.append("  FROM GJT_STUDENT_INFO GSI, GJT_GRADE_SPECIALTY GGS, VIEW_TEACH_PLAN VTP");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GGS.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GGS.ID = VTP.GRADE_SPECIALTY_ID");
		sql.append("  AND VTP.ACTUAL_GRADE_ID = :TERM_ID");
		sql.append("  GROUP BY VTP.ACTUAL_GRADE_ID, VTP.COURSE_ID");

		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 初始化期课程的复制状态
	 * 
	 * @return
	 */
	@Transactional
	public int updTermCopyFlg1(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_TERM_COURSEINFO GTC");
		sql.append("  SET GTC.COPY_FLG = '1', GTC.TASK_COUNT = :TASK_COUNT, GTC.TERM_TASK_COUNT = :TERM_TASK_COUNT");
		sql.append("  WHERE GTC.TERMCOURSE_ID = :TERMCOURSE_ID");

		param.put("TASK_COUNT", ObjectUtils.toString(searchParams.get("TASK_COUNT")));
		param.put("TERM_TASK_COUNT", ObjectUtils.toString(searchParams.get("TERM_TASK_COUNT")));
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 初始化期课程的复制状态
	 * 
	 * @return
	 */
	@Transactional
	public int updTermCopyFlg2(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_TERM_COURSEINFO GTC");
		sql.append("  SET GTC.COPY_FLG = '2'");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERM_ID = :TERM_ID");

		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 获取期课程是否复制的数据
	 * 
	 * @return
	 */
	public List getTermCourseTaskCount(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM (SELECT LTC.TERMCOURSE_ID,");
		sql.append("  LTS.CLASS_ID,");
		sql.append("  (SELECT COUNT(LTCO.TERMCOURSE_ID)");
		sql.append("  FROM LCMS_TERM_COURSEINFO  LTCO,");
		sql.append("  LCMS_TERMCOURSE_CLASS LTCS,");
		sql.append("  LCMS_TERMCOURSE_TASK  LTT");
		sql.append("  WHERE LTCO.ISDELETED = 'N'");
		sql.append("  AND LTCS.ISDELETED = 'N'");
		sql.append("  AND LTT.ISDELETED = 'N'");
		sql.append("  AND LTT.DO_FINISH = 'Y'");
		sql.append("  AND LTCS.TERMCOURSE_CLASS_ID = LTT.CLASS_ID");
		sql.append("  AND LTCO.TERMCOURSE_ID = LTCS.TERMCOURSE_ID");
		sql.append("  AND LTCO.COURSE_ID = LTC.COURSE_ID");
		sql.append("  AND LTCS.CLASS_NAME = '测试班级'");
		sql.append("  AND LTCO.TERM_ID = '614b0592ac1082a750505050c86c07e1') TASK_COUNT,");
		sql.append("  (SELECT COUNT(LTT.TASK_ID)");
		sql.append("  FROM LCMS_TERMCOURSE_TASK LTT");
		sql.append("  WHERE LTT.ISDELETED = 'N'");
		sql.append("  AND LTT.DO_FINISH = 'Y'");
		sql.append("  AND LTT.CLASS_ID = LTS.CLASS_ID) TERM_TASK_COUNT");
		sql.append("  FROM LCMS_TERM_COURSEINFO  LTC,");
		sql.append("  GJT_CLASS_INFO LTS,");
		sql.append("  GJT_TERM_COURSEINFO   GTC");
		sql.append("  WHERE LTC.ISDELETED = 'N'");
		sql.append("  AND LTS.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND LTC.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND LTC.TERMCOURSE_ID = LTS.TERMCOURSE_ID");
		sql.append("  AND GTC.TERM_ID = :TERM_ID) TAB");
		sql.append("  WHERE (TAB.TASK_COUNT > TAB.TERM_TASK_COUNT OR TAB.TERM_TASK_COUNT = 0)");

		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	public int updateOnlineLesson(Map formMap, int type) {
		int re = 0;
		if (1 == type) {// add
			String onlineId = SequenceUUID.getSequence();
			formMap.put("onlineId", onlineId);
			if (saveOnlesson(formMap) > 0) {
				// 保存直播课程关系

				// 保存直播学员关系

			}
		} else if (2 == type) {// update

		} else if (3 == type) {// delete

		}
		return 0;
	}

	// 保存直播课程
	private int saveOnlesson(Map formMap) {
		int result = 1;
		String onlineTutorName = ObjectUtils.toString(formMap.get("onlineName"));
		String onlineTutorDesc = ObjectUtils.toString("暂无描述");
		String onlinetutor_id = ObjectUtils.toString(formMap.get("onlineId"));

		// 添加北京展示互动科技接口/training/room/created调用
		String url = "http://eenet.gensee.com/integration/site/training/courseware/list";// AppConfig.getProperty("roomCreate_url");
		PostMethod postMethod = new PostMethod(url);
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");
		SimpleDateFormat ps = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			NameValuePair nvp = new NameValuePair("loginName", "xuexipingtai@eenet.com");// AppConfig.getProperty("gensee_loginName")
			NameValuePair nvp1 = new NameValuePair("password", "xuexipingtai@88");// AppConfig.getProperty("gensee_password"));
			String subject = "" + new Date().getTime();
			NameValuePair nvp2 = new NameValuePair("subject", onlineTutorName + subject);
			NameValuePair nvp3 = new NameValuePair("startDate", ps.format(new Date()));
			// 支持Web端学生加入
			NameValuePair nvp4 = new NameValuePair("webJoin", "true");
			// 暂不支持客户端端学生加入
			NameValuePair nvp5 = new NameValuePair("clientJoin", "true");
			// 默认用户口令
			NameValuePair nvp6 = new NameValuePair("teacherToken", "654321");
			NameValuePair nvp7 = new NameValuePair("studentToken", "123456");
			NameValuePair nvp8 = new NameValuePair("assistantToken", "111111");
			NameValuePair nvp9 = new NameValuePair("description", onlineTutorDesc);
			NameValuePair nvp10 = new NameValuePair("uiMode", "4");

			String invalidDate = ObjectUtils.toString(formMap.get("ONLINETUTOR_FINISH"));
			if (!"".equals(invalidDate)) {
				invalidDate = ps.format(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(invalidDate));
			} else {
				// 为空则默认一天
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, 1);
				invalidDate = ps.format(calendar.getTime());
			}
			NameValuePair nvp11 = new NameValuePair("invalidDate", invalidDate);

			postMethod.addParameter(nvp);
			postMethod.addParameter(nvp1);
			postMethod.addParameter(nvp2);
			postMethod.addParameter(nvp3);
			postMethod.addParameter(nvp4);
			postMethod.addParameter(nvp5);
			postMethod.addParameter(nvp6);
			postMethod.addParameter(nvp7);
			postMethod.addParameter(nvp8);
			postMethod.addParameter(nvp9);
			postMethod.addParameter(nvp10);
			postMethod.addParameter(nvp11);

			httpClient.executeMethod(postMethod);
			if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
				String message = postMethod.getResponseBodyAsString();
				Gson gson = new Gson();
				OnlineLessonVo onlineLessonVo = gson.fromJson(message, OnlineLessonVo.class);
				if ("0".equals(onlineLessonVo.getCode())) {
					onlineLessonVo.setOnlineTutorId(onlinetutor_id);
					onlineLessonVo.setSubject(onlineTutorName + subject);
					onlineLessonVo.setSdkId(onlineLessonVo.getId());
					onlineLessonVo.setDescription(onlineLessonVo.getDescription());

					//
					StringBuffer sql = new StringBuffer();
					Map param = new HashMap();
					sql.append("  INSERT INTO GJT_ONLINE_LESSON (");
					sql.append("    ONLINE_ID,");
					sql.append("    ONLINE_NAME,");
					sql.append("    ONLINE_DESC,");
					sql.append("    TCH_TOKEN,");
					sql.append("    TCH_URL,");
					sql.append("    STU_TOKEN,");
					sql.append("    STU_URL,");
					sql.append("    STU_CLIENT_TOKEN,");
					sql.append("    VIDEO_TOKEN,");
					sql.append("    VIDEO_URL,");
					sql.append("    START_DT,");
					sql.append("    END_DT,");
					sql.append("    ASSISTANT_TOKEN,");
					sql.append("    NUMBER,");
					sql.append("    SDK_ID,");
					sql.append("    CREATED_DT,");
					sql.append("    CREATED_BY");
					sql.append("  )VALUES(");
					sql.append("    :ONLINE_ID,");
					sql.append("    :ONLINE_NAME,");
					sql.append("    :ONLINE_DESC,");
					sql.append("    :TCH_TOKEN,");
					sql.append("    :TCH_URL,");
					sql.append("    :STU_TOKEN,");
					sql.append("    :STU_URL,");
					sql.append("    :STU_CLIENT_TOKEN,");
					sql.append("    :VIDEO_TOKEN,");
					sql.append("    :VIDEO_URL,");
					sql.append("    :START_DT,");
					sql.append("    :END_DT,");
					sql.append("    :ASSISTANT_TOKEN,");
					sql.append("    :NUMBER,");
					sql.append("    :SDK_ID,");
					sql.append("    :CREATED_DT,");
					sql.append("    :CREATED_BY");
					sql.append("  )");
					param.put("ONLINE_ID", onlinetutor_id);
					param.put("ONLINE_NAME", onlineTutorName);
					param.put("ONLINE_DESC", onlineTutorDesc);
					param.put("START_DT", DateUtils.getDateToString(ObjectUtils.toString(formMap.get("startDt"))));
					param.put("END_DT", DateUtils.getDateToString(ObjectUtils.toString(formMap.get("endDt"))));
					param.put("CREATED_DT", new Date());
					param.put("CREATED_BY", ObjectUtils.toString(formMap.get("createdBy")));

					param.put("TCH_TOKEN", onlineLessonVo.getTeacherToken());
					param.put("TCH_URL", onlineLessonVo.getTeacherJoinUrl());
					param.put("STU_TOKEN", onlineLessonVo.getStudentToken());
					param.put("STU_URL", onlineLessonVo.getStudentJoinUrl());
					param.put("STU_CLIENT_TOKEN", onlineLessonVo.getStudentClientToken());
					param.put("VIDEO_TOKEN", onlineLessonVo.getVideoToken());
					param.put("VIDEO_URL", onlineLessonVo.getVideoJoinUrl());
					param.put("ASSISTANT_TOKEN", onlineLessonVo.getAssistantToken());
					param.put("SDK_ID", onlineLessonVo.getSdkId());
					param.put("NUMBER", onlineLessonVo.getNumber());

					result = commonDao.updateForMapNative(sql.toString(), param);

				} else {
					System.out.println("调用" + url + "接口失败!");
					result = 0;
				}
			} else {
				System.out.println("调用" + url + "接口失败!");
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		} finally {
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return result;
	}

	/**
	 * 获取需要辅导课程班级的学习中心
	 * 
	 * @return
	 */
	public List getXxzxClassList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GSC.ID XXZX_ID, GSC.SC_CODE, GSC.SC_NAME");
		sql.append("  FROM GJT_STUDY_CENTER GSC, GJT_ORG GOG");
		sql.append("  WHERE GSC.IS_DELETED = 'N'");
		sql.append("  AND GOG.IS_DELETED = 'N'");
		sql.append("  AND GSC.ID = GOG.ID");
		sql.append("  AND GSC.SERVICE_AREA LIKE '%4%'");
		sql.append("  AND GOG.ID IN (SELECT org.ID");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");
		sql.append("  START WITH org.ID = :XX_ID");
		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 获取需要辅导课程班级的学习中心
	 * 
	 * @return
	 */
	public List getXxzxCourseList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT ACTUAL_GRADE_ID,");
		sql.append("  COURSE_ID,");
		sql.append("  GRADE_NAME,");
		sql.append("  TO_CHAR(COURSE_START_DATE, 'yyyy-mm-dd') COURSE_START_DATE,");
		sql.append("  TO_CHAR(COURSE_END_DATE, 'yyyy-mm-dd') COURSE_END_DATE,");
		sql.append("  KCH,");
		sql.append("  KCMC,");
		sql.append("  TERMCOURSE_ID,");
		sql.append("  REC_COUNT,");
		sql.append("  (CASE");
		sql.append("  WHEN COURSE_START_DATE > SYSDATE THEN");
		sql.append("  '1'");
		sql.append("  ELSE");
		sql.append("  '0'");
		sql.append("  END) TIME_FLG,");

		sql.append("  (SELECT COUNT(GTO.TERM_ORG_ID)");
		sql.append("  FROM GJT_TERMCOURSE_ORG GTO");
		sql.append("  WHERE GTO.IS_DELETED = 'N'");
		sql.append("  AND GTO.XXZX_ID = :XXZX_ID");
		sql.append("  AND GTO.TERMCOURSE_ID = TAB.TERMCOURSE_ID) TERM_ORG_COUNT");

		sql.append("  FROM (SELECT VTP.ACTUAL_GRADE_ID,");
		sql.append("  VTP.COURSE_ID,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  GGE.COURSE_START_DATE,");
		sql.append("  GGE.COURSE_END_DATE,");
		sql.append("  VTP.KCH,");
		sql.append("  VTP.KCMC,");
		sql.append("  GTC.TERMCOURSE_ID,");
		sql.append("  COUNT(GSI.STUDENT_ID) REC_COUNT");
		sql.append("  FROM GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_GRADE_SPECIALTY GGS,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_GRADE           GGE,");
		sql.append("  GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GGS.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND VTP.ACTUAL_GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = VTP.GRADE_SPECIALTY_ID");
		sql.append("  AND VTP.ACTUAL_GRADE_ID = GTC.TERM_ID");
		sql.append("  AND VTP.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND GSI.XXZX_ID IN");
		sql.append("  (SELECT org.ID");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");
		sql.append("  START WITH org.ID = :XXZX_ID");
		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)");
		sql.append("  GROUP BY VTP.ACTUAL_GRADE_ID,");
		sql.append("  VTP.COURSE_ID,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  GGE.COURSE_START_DATE,");
		sql.append("  GGE.COURSE_END_DATE,");
		sql.append("  VTP.KCH,");
		sql.append("  VTP.KCMC,");
		sql.append("  GTC.TERMCOURSE_ID) TAB");
		sql.append("  ORDER BY GRADE_NAME DESC, KCMC");

		param.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 新增学习中心和期课程关系
	 * 
	 * @return
	 */
	@Transactional
	public int addTermcourseOrg(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_TERMCOURSE_ORG");
		sql.append("  (TERM_ORG_ID, XXZX_ID, TERMCOURSE_ID)");
		sql.append("  VALUES");
		sql.append("  (:TERM_ORG_ID, :XXZX_ID, :TERMCOURSE_ID)");

		param.put("TERM_ORG_ID", ObjectUtils.toString(searchParams.get("TERM_ORG_ID")));
		param.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 删除学习中心和期课程关系
	 * 
	 * @return
	 */
	@Transactional
	public int delTermcourseOrg(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_TERMCOURSE_ORG GTO");
		sql.append("  SET GTO.IS_DELETED = 'Y', GTO.UPDATED_DT = SYSDATE");
		sql.append("  WHERE GTO.XXZX_ID = :XXZX_ID");
		sql.append("  AND GTO.TERMCOURSE_ID = :TERMCOURSE_ID");

		param.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
}
