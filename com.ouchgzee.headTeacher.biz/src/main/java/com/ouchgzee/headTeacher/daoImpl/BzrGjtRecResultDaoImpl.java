/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.daoImpl;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.daoImpl.base.BaseDaoImpl;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约选课考试信息操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtRecResultDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
@Transactional(value="transactionManagerBzr", readOnly = true)
public class BzrGjtRecResultDaoImpl extends BaseDaoImpl {

	@Autowired
	private CommonDao commonDao;

	private static Logger log = LoggerFactory.getLogger(BzrGjtRecResultDaoImpl.class);

	/**
	 * 分页条件查询学员预约选课考试信息，SQL语句
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findStudentRecResultByPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = createSqlToFindStudentRecResult(classId, searchParams, parameters);
		return super.findByPageToMap(querySql, parameters, pageRequest);
	}

	/**
	 * 根据条件查询学员预约选课考试信息，SQL语句
	 * 
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> findAllStudentRecResult(String classId, Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = createSqlToFindStudentRecResult(classId, searchParams, parameters);
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 根据条件查询学员预约选课考试情况，SQL语句
	 *
	 * @param studentId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> findStudentRecResultSituation(String studentId, Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select a.rec_id,a.teach_plan_id,a.student_id,");
		querySql.append("ti.term_id,ti.term_name,c.course_id,c.kcmc,tp.ksfs,tp.exam_stime,tp.exam_etime,");
		querySql.append(
				"tp.book_starttime,tp.book_endtime,floor(sysdate-TO_DATE(tp.book_starttime, 'yyyy-mm-dd hh24:mi:ss')) reserveStartDays,");
		querySql.append(
				"floor(TO_DATE(tp.book_endtime, 'yyyy-mm-dd hh24:mi:ss')-sysdate) reserveEndDays,a.exam_state,a.updated_dt,ua.real_name,ua.user_type");
		querySql.append(" from gjt_rec_result a");
		querySql.append(" inner join VIEW_TEACH_PLAN tp on tp.teach_plan_id=a.teach_plan_id and tp.is_deleted='N'");
		querySql.append(" inner join gjt_course c on c.course_id=tp.course_id and c.is_deleted='N'");
		querySql.append(" inner join gjt_term_info ti on ti.term_id=tp.term_id and ti.is_deleted='N'");
		querySql.append(" left join gjt_user_account ua on ua.id=a.updated_by");
		querySql.append(" where a.is_deleted = 'N' and a.student_id = :studentId");
		querySql.append(" order by ti.term_code,c.kcmc");
		parameters.put("studentId", studentId);
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 获取学员预约选课的课程成绩，SQL语句
	 *
	 * @param studentId
	 * @return
	 */
	public List<Map> findStudentRecResultScore(String studentId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT");
		sql.append("  			grr.TEACH_PLAN_ID,gtc.TERMCOURSE_ID,gc.COURSE_ID,gsr.PROGRESS SCHEDULE,gtp.XF,NVL(gsr.IS_ONLINE,'N') IS_ONLINE,FLOOR(SYSDATE-gsr.LAST_LOGIN_DATE) LEFT_DATE,");
		sql.append("  			grr.STUDENT_ID,gc.KCMC,gc.KCH,ROUND(NVL(gsr.ONLINE_TIME,0)/60,1) LOGIN_TIME,gsr.LOGIN_TIMES LOGIN_COUNT,");
		sql.append("  			(SELECT gg.GRADE_NAME FROM GJT_GRADE gg WHERE gg.GRADE_ID = gtp.ACTUAL_GRADE_ID AND gg.IS_DELETED = 'N') TERM_NAME,");
		sql.append("  			(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'CourseType' AND tsd.code = gtp.KCLBM) KCLBM_NAME,");
		sql.append("  			(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'ExaminationMode' AND tsd.code = gtp.KSFS) KSFS_NAME,");
		sql.append("  			NVL( TO_CHAR(grr.GET_CREDITS), '--' ) GET_CREDITS,NVL( TO_CHAR( grr.COURSE_SCHEDULE ), '--' ) XK_PERCENT,");
		sql.append("  			grr.EXAM_SCORE,grr.EXAM_SCORE1,grr.EXAM_SCORE2,grr.EXAM_STATE");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT grr");
		sql.append("  		LEFT JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  			grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID");
		sql.append("  		LEFT JOIN GJT_TERM_COURSEINFO gtc ON grr.TERMCOURSE_ID=gtc.TERMCOURSE_ID");
		sql.append("  		LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON");
		sql.append("  			grr.REC_ID = gsr.CHOOSE_ID");
		sql.append("  		LEFT JOIN GJT_COURSE gc ON");
		sql.append("  			gtp.COURSE_ID = gc.COURSE_ID");
		sql.append("  		WHERE");
		sql.append("  			grr.IS_DELETED = 'N'");
		sql.append("  			AND gc.IS_DELETED = 'N'");
		sql.append("  			AND grr.STUDENT_ID =:studentId");
		sql.append("  		ORDER BY");
		sql.append("  			gtp.KKXQ");

		parameters.put("studentId", studentId);
		return super.findAllByToMap(sql, parameters, null);
	}

	/**
	 * 获取学员的课程班级
	 * 
	 * @param studentId
	 * @param teachPlanIds
	 * @return
	 */
	public List<Map> findStudentTeachPlanCourseClass(String studentId, String... teachPlanIds) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select t.teach_plan_id,b.class_id");
		querySql.append(" from VIEW_TEACH_PLAN t");
		querySql.append(
				" inner join gjt_class_info b on t.course_id=b.course_id and t.pycc=b.pycc and t.kkzy=b.specialty_id and b.term_id=t.term_id and b.is_deleted='N'");
		querySql.append(" inner join gjt_class_student c on b.class_id=c.class_id and c.is_deleted='N'");
		querySql.append(" where t.is_deleted='N' and b.class_type='course' and c.student_id=:studentId");
		if (teachPlanIds.length > 0) {
			if (teachPlanIds.length == 1) {
				querySql.append(" and t.teach_plan_id=:teachPlanId");
				parameters.put("teachPlanId", teachPlanIds[0]);
			} else {
				querySql.append(" and t.teach_plan_id in (");
				for (int i = 0; i < teachPlanIds.length; i++) {
					querySql.append("'" + teachPlanIds[i] + "',");
				}
				querySql.setLength(querySql.length() - 1);
				querySql.append(")");
			}
		}
		parameters.put("studentId", studentId);
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 获取课程班学员的学习情况[总学员数、进度正常数、进度落后数、成绩合格数、成绩不合格数、通过数、未通过数]
	 * 
	 * @param teachClassId
	 *            教学班
	 * @param courseId
	 *            课程
	 * @return studentNum-总学员数 normalNum-进度正常数 backwardNum-进度落后数
	 *         qualifiedNum-成绩合格数 unQualifiedNum-成绩不合格数 passNum-通过数
	 *         unPassNum-未通过数
	 */
	public Map countClassStudentLearningSituation(String teachClassId, String courseId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder("SELECT count(*) studentNum,")
				.append(" count(case d.EXAM_STATE when '1' then 1 else null end) passNum,")
				.append(" count(case d.EXAM_STATE when '0' then 1 else null end) unPassNum FROM")
				.append(" ( SELECT gsi.student_id FROM GJT_STUDENT_INFO gsi")
				.append(" INNER JOIN GJT_CLASS_STUDENT gcs ON gcs.STUDENT_ID = gsi.STUDENT_ID AND gcs.IS_DELETED = 'N'")
				.append(" WHERE gsi.IS_DELETED = 'N'AND gcs.CLASS_ID=:teachClassId ) t")
				.append(" LEFT JOIN gjt_rec_result d ON t.STUDENT_ID = d.STUDENT_ID")
				.append(" where d.COURSE_ID=:courseId");
		parameters.put("teachClassId", teachClassId);
		parameters.put("courseId", courseId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 获取班级学员的考试情况[总学员数、通过数、未通过数]
	 * 
	 * @param teachClassId
	 *            教学班
	 * @param classId
	 *            课程班
	 * @return studentNum-总学员数 passNum-通过数 unPassNum-未通过数
	 */
	public Map countClassStudentExamSituation(String teachClassId, String classId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"select count(*) studentNum,count(case when unPassNum=0 then 1 else null end) passNum,count(case when unPassNum>0 then 1 else null end) unPassNum");
		querySql.append(" from (");
		querySql.append(
				"     select a.student_id,count(*),count(case when a.SCORE_STATE is not null and a.score_state=0 then 1 else null end) passNum,count(case when a.SCORE_STATE is not null and a.score_state<>0 then 1 else null end) unPassNum");
		querySql.append("     from gjt_rec_result a").append(" inner join ").append(" (")
				.append(" SELECT gsi.student_id FROM GJT_STUDENT_INFO gsi")
				.append(" INNER JOIN GJT_CLASS_STUDENT gcs ON gcs.STUDENT_ID = gsi.STUDENT_ID AND gcs.IS_DELETED = 'N'")
				.append(" WHERE gsi.IS_DELETED = 'N'").append(" AND gcs.CLASS_ID=:teachClassId").append(" ) t")
				.append(" on a.student_id = t.student_id");
		querySql.append("     inner join gjt_class_student b on a.student_id = b.student_id and b.is_deleted='N'");
		querySql.append("     inner join gjt_class_info c on b.class_id = c.class_id and c.is_deleted='N'");
		querySql.append("     where a.is_deleted = 'N' and c.class_id = :classId");
		querySql.append("     group by a.student_id");
		querySql.append(" ) temp");
		parameters.put("teachClassId", teachClassId);
		parameters.put("classId", classId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 获取学员的预约情况[可预约、已预约]
	 * 
	 * @param studentId
	 * @return
	 */
	public Object[] countStudentReserveSituation(String studentId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select count(e.rec_id) canExamNum,count(f.id) alreadyExamNum,");
		querySql.append(" gsep.EXAM_POINT_ID AS EXAM_POINT");
		querySql.append(" from GJT_REC_RESULT e");
		querySql.append(" left join GJT_EXAM_RECORD f on f.rec_id = e.rec_id and f.exam_state = '2'");
		querySql.append(" LEFT JOIN GJT_STUDENT_EXAM_POINT gsep ON e.STUDENT_ID=gsep.STUDENT_ID");
		querySql.append(" where e.is_deleted = 'N' and e.student_id=:studentId GROUP BY gsep.EXAM_POINT_ID");
		parameters.put("studentId", studentId);
		List<List> countList = super.findAllByToList(querySql, parameters, null);
		return (countList != null && countList.size() > 0) ? countList.get(0).toArray() : new Object[2];
	}

	/**
	 * 获取班级待预约考试的学员数
	 * 
	 * @param classId
	 * @return
	 */
	public long countClassStudentWaitExam(String classId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select count(distinct a.student_id)");
		querySql.append(" from gjt_rec_result a");
		querySql.append(" left join GJT_EXAM_RECORD f on f.rec_id = a.rec_id and f.exam_state = '2'");
		querySql.append(" inner join VIEW_TEACH_PLAN tp on tp.teach_plan_id=a.teach_plan_id and tp.is_deleted='N'");
		querySql.append(" inner join gjt_class_student c on a.student_id=c.student_id and c.is_deleted='N'");
		querySql.append(" where a.is_deleted = 'N'");
		querySql.append(
				" and (sysdate between TO_DATE(tp.book_starttime, 'yyyy-mm-dd hh24:mi:ss') and TO_DATE(tp.book_endtime, 'yyyy-mm-dd hh24:mi:ss'))");
		querySql.append(" and (tp.KSFS='1' or tp.KSFS='2' or tp.KSFS='3' or tp.KSFS='4' or tp.KSFS='8')");
		querySql.append(" and f.id is null and c.class_id = :classId");
		parameters.put("classId", classId);
		return super.countBySql(querySql, parameters);
	}

	/**
	 * 获取班级待预约考点的学员数
	 * 
	 * @param classId
	 * @return
	 */
	public long countClassStudentWaitExamPoint(String classId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select count(distinct gcs.student_id)");
		querySql.append(" from GJT_STUDENT_EXAM_POINT gsep");
		querySql.append(" right join gjt_class_student gcs on gsep.student_id=gcs.student_id");
		querySql.append(" where gcs.is_deleted='N' and (" + "      select count(a.term_id) from gjt_rec_result a"
				+ "      inner join VIEW_TEACH_PLAN tp on tp.teach_plan_id=a.teach_plan_id and tp.is_deleted='N'"
				+ "      where a.is_deleted = 'N' "
				+ "      and (sysdate between TO_DATE(tp.book_starttime, 'yyyy-mm-dd hh24:mi:ss') and TO_DATE(tp.book_endtime, 'yyyy-mm-dd hh24:mi:ss'))"
				+ "      and a.student_id = gcs.student_id) > 0" + "    and" + "      ("
				+ "        gsep.term_id NOT IN (select distinct a.term_id from gjt_rec_result a"
				+ "        inner join VIEW_TEACH_PLAN tp on tp.teach_plan_id=a.teach_plan_id and tp.is_deleted='N'"
				+ "        where a.is_deleted = 'N' "
				+ "        and (sysdate between TO_DATE(tp.book_starttime, 'yyyy-mm-dd hh24:mi:ss') and TO_DATE(tp.book_endtime, 'yyyy-mm-dd hh24:mi:ss'))"
				+ "        and a.student_id = gcs.student_id)" + "      or" + "        ("
				+ "          gsep.term_id IN (select distinct a.term_id from gjt_rec_result a"
				+ "          inner join VIEW_TEACH_PLAN tp on tp.teach_plan_id=a.teach_plan_id and tp.is_deleted='N'"
				+ "          where a.is_deleted = 'N' "
				+ "          and (sysdate between TO_DATE(tp.book_starttime, 'yyyy-mm-dd hh24:mi:ss') and TO_DATE(tp.book_endtime, 'yyyy-mm-dd hh24:mi:ss'))"
				+ "          and a.student_id = gcs.student_id)" + "          OR" + "          gsep.is_deleted = 'Y'"
				+ "        )" + "      )" + "      and gcs.class_id = :classId");
		parameters.put("classId", classId);
		return super.countBySql(querySql, parameters);
	}

	/**
	 * 学员预约考点
	 * 
	 * @param studentId
	 * @param termId
	 * @param examPointId
	 * @return
	 */
	@Transactional(value="transactionManagerBzr")
	public int reserveRecExamPoint(String studentId, String termId, String examPointId) {
		StringBuilder updateSql = new StringBuilder(
				"MERGE INTO GJT_STUDENT_EXAM_POINT G USING (SELECT :studentId AS STUDENT_ID,:termId AS TERM_ID FROM DUAL) T ON (T.STUDENT_ID = G.STUDENT_ID AND T.TERM_ID=G.TERM_ID)");
		updateSql.append(" WHEN MATCHED THEN UPDATE SET EXAM_POINT_ID=:examPointId,UPDATED_DT=SYSDATE,IS_DELETED='N'");
		updateSql.append(
				" WHEN NOT MATCHED THEN INSERT(STUDENT_ID, TERM_ID, EXAM_POINT_ID, CREATED_DT) VALUES (:studentId, :termId, :examPointId, SYSDATE)");

		Query q = em.createNativeQuery(updateSql.toString());
		q.setParameter("studentId", studentId);
		q.setParameter("termId", termId);
		q.setParameter("examPointId", examPointId);
		return q.executeUpdate();
	}

	/**
	 * 拼接SQL，查询学员预约选课考试信息
	 *
	 * @param classId
	 * @param searchParams
	 * @param parameters
	 * @return
	 */
	private StringBuilder createSqlToFindStudentRecResult(String classId, final Map<String, Object> searchParams,
			Map<String, Object> parameters) {
		StringBuilder querySql = new StringBuilder(
				"select t.student_id,t.xm,t.pycc,d.zymc,w.grade_name,count(e.rec_id) canExamNum,count(f.id) alreadyExamNum,h.name examPointName");
		querySql.append(
				" from gjt_student_info t inner join gjt_class_student b on b.student_id = t.student_id and b.is_deleted = 'N' inner join gjt_class_info c on c.class_id = b.class_id and c.is_deleted = 'N' left join gjt_specialty d on d.specialty_id = t.major");
		querySql.append(
				" inner join gjt_signup u on u.student_id = t.student_id and u.is_deleted = 'N' inner join gjt_enroll_batch v on v.enroll_batch_id = u.enroll_batch_id and v.is_deleted = 'N' inner join gjt_grade w on w.grade_id = v.grade_id and w.is_deleted = 'N'");
		querySql.append(" left join GJT_REC_RESULT e on e.student_id = t.student_id and e.is_deleted = 'N'");
		querySql.append(" left join GJT_EXAM_RECORD f on f.rec_id = e.rec_id and f.exam_state = '2'");
		querySql.append(
				" left join GJT_STUDENT_EXAM_POINT g on g.student_id = t.student_id and g.term_id = :termId and g.is_deleted = 'N'");
		querySql.append(" left join GJT_EXAM_POINT h on h.id = g.exam_point_id");
		querySql.append(" where t.is_deleted = 'N' and c.class_id = :classId");
		parameters.put("classId", classId);
		parameters.put("termId", searchParams.get("termId"));

		if (StringUtils.isNotBlank(searchParams.get("xm"))) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("xm") + "%");
		}
		querySql.append(" group by t.student_id,t.xm,t.pycc,d.zymc,w.grade_name,h.name,t.created_dt");
		// 考点状态 1.已预约 2. 未预约
		if (StringUtils.isNotBlank(searchParams.get("examState"))) {
			int examState = NumberUtils.toInt(searchParams.get("examState").toString());
			if (examState == 1) {
				querySql.append(" having count(f.id) > 0");
			} else if (examState == 2) {
				querySql.append(" having count(f.id) = 0");
			}
		}
		return querySql;
	}

	/**
	 * 课程考勤列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> courseAttendanceList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.courseAttendanceListSqlHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForPageNative(sql,params,pageRequst);
	}


	/**
	 * 课程考勤列表sql处理
	 * @param searchParams
	 * @return
	 */
	public Map<String,Object> courseAttendanceListSqlHandler(Map searchParams){
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  SELECT");
		sql.append("  	COURSE_ID,SDATE,EDATE,TIME_FLG,E_XC_DAY,S_XC_DAY,GRADE_ID,GRADE_NAME,KCMC,REC_COUNT,SUM_SCHEDULE,SUM_STUDY_SCORE,");
		sql.append("  	NVL(SUM_LOGIN_COUNT,0) SUM_LOGIN_COUNT,NVL(SUM_LOGIN_TIME,0) SUM_LOGIN_TIME,SUM_PASS_COUNT,DAY7_LOGIN,DAY3_7_LOGIN,DAY3_LOGIN,STUDENT_COUNT,ONLINE_STUDENT_COUNT,");
		sql.append("  	(CASE WHEN STUDENT_COUNT > (DAY7_LOGIN + DAY3_7_LOGIN +  DAY3_LOGIN ) THEN  STUDENT_COUNT -  DAY7_LOGIN -  DAY3_7_LOGIN -  DAY3_LOGIN  ELSE 0 END) NO_DAY_LOGIN,");
		sql.append("  	(CASE WHEN REC_COUNT > 0 THEN FLOOR( NVL(SUM_SCHEDULE / REC_COUNT,0) ) ELSE 0 END) AVG_SCHEDULE,");
		sql.append("  	(CASE WHEN REC_COUNT > 0 THEN FLOOR( NVL(SUM_STUDY_SCORE / REC_COUNT,0) ) ELSE 0 END) AVG_STUDY_SCORE,");
		sql.append("  	(CASE WHEN REC_COUNT > 0 THEN FLOOR( NVL(SUM_LOGIN_COUNT / REC_COUNT,0) ) ELSE 0 END) AVG_LOGIN_COUNT,");
		sql.append("  	(CASE WHEN REC_COUNT > 0 THEN ROUND( NVL(SUM_LOGIN_TIME / REC_COUNT,0), 1 ) ELSE 0 END) AVG_LOGIN_TIME,");
		sql.append("  	(CASE WHEN REC_COUNT > 0 THEN FLOOR(( SUM_PASS_COUNT / REC_COUNT )* 100 ) ELSE 0 END) AVG_PASS_COUNT");
		sql.append("  FROM (");
		sql.append("  		SELECT");
		sql.append("  			gtp.TERMCOURSE_ID,GTP.COURSE_ID,TO_CHAR( gre.START_DATE, 'yyyy-mm-dd' ) SDATE,TO_CHAR( gre.END_DATE, 'yyyy-mm-dd' ) EDATE,GRE.GRADE_ID,");
		sql.append("  			(CASE WHEN SYSDATE >= gre.START_DATE AND SYSDATE <= gre.END_DATE THEN '1' WHEN SYSDATE < gre.START_DATE THEN '2' WHEN SYSDATE > gre.END_DATE THEN '3' ELSE '0' END) TIME_FLG,");
		sql.append("  			TRUNC( TO_DATE( TO_CHAR( gre.END_DATE, 'yyyy-mm-dd' ), 'YYYY-MM-DD ' )- TO_DATE( TO_CHAR( SYSDATE, 'yyyy-mm-dd' ), 'yyyy-mm-dd' )) E_XC_DAY,GRE.GRADE_NAME,");
		sql.append("  			TRUNC( TO_DATE( TO_CHAR( SYSDATE, 'yyyy-mm-dd' ), 'YYYY-MM-DD ' )- TO_DATE( TO_CHAR( gre.START_DATE, 'yyyy-mm-dd' ), 'yyyy-mm-dd' )) S_XC_DAY,GCE.KCMC,");
		sql.append("  			(SELECT COUNT( GRR.REC_ID ) FROM GJT_REC_RESULT GRR LEFT JOIN GJT_STUDENT_INFO gsi ON grr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE GRR.IS_DELETED = 'N' AND gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND GRR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID AND EXISTS(SELECT 1 FROM GJT_STUDENT_INFO gsi WHERE gsi.STUDENT_ID = grr.STUDENT_ID)");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) REC_COUNT,");
		sql.append("  			(SELECT SUM( NVL( GSR.PROGRESS, 0 )) FROM VIEW_STUDENT_STUDY_SITUATION GSR LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND  GSR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) SUM_SCHEDULE,");
		sql.append("  			(SELECT SUM( NVL( GRR.EXAM_SCORE2, 0 )) FROM GJT_REC_RESULT GRR LEFT JOIN GJT_STUDENT_INFO gsi ON grr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE GRR.IS_DELETED = 'N' AND gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND GRR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) SUM_STUDY_SCORE,");
		sql.append("  			(SELECT ROUND( SUM( NVL( gsr.ONLINE_TIME, 0 ))/ 60, 1 ) FROM VIEW_STUDENT_STUDY_SITUATION gsr LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND GSR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) SUM_LOGIN_TIME,");
		sql.append("  			(SELECT SUM( NVL( GSR.LOGIN_TIMES, 0 )) FROM VIEW_STUDENT_STUDY_SITUATION GSR LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND GSR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) SUM_LOGIN_COUNT,");
		sql.append("  			(SELECT COUNT( GRR.REC_ID ) FROM GJT_REC_RESULT GRR LEFT JOIN GJT_STUDENT_INFO gsi ON grr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE GRR.IS_DELETED = 'N' AND gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND GRR.EXAM_STATE = '1' AND GRR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) SUM_PASS_COUNT,");
		sql.append("  			(SELECT COUNT( DISTINCT GSR.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION GSR LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N'AND  GSR.LAST_LOGIN_DATE < SYSDATE - 7 AND GSR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) DAY7_LOGIN,");
		sql.append("  			(SELECT COUNT( DISTINCT GSR.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION GSR LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND  GSR.LAST_LOGIN_DATE < SYSDATE - 3 AND GSR.LAST_LOGIN_DATE >= SYSDATE - 7 AND GSR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) DAY3_7_LOGIN,");
		sql.append("  			(SELECT COUNT( DISTINCT GSR.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION GSR LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND GSR.LAST_LOGIN_DATE >= SYSDATE - 3 AND GSR.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) DAY3_LOGIN,");
		sql.append("  			(SELECT COUNT( DISTINCT grr.REC_ID ) FROM GJT_REC_RESULT grr LEFT JOIN GJT_STUDENT_INFO gsi ON grr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE grr.IS_DELETED = 'N' AND gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND grr.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			) STUDENT_COUNT,");
		sql.append("  			(SELECT COUNT( DISTINCT gsr.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION gsr LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID=gsi.STUDENT_ID,GJT_CLASS_STUDENT GCS WHERE gsi.IS_DELETED='N'  AND gcs.IS_DELETED='N' AND  gsr.IS_ONLINE = 'Y' AND gsr.TERMCOURSE_ID = GTP.TERMCOURSE_ID AND GSI.STUDENT_ID=GCS.STUDENT_ID AND GCS.CLASS_ID=T.CLASS_ID");
		sql.append("  				AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		sql.append("  			 ) ONLINE_STUDENT_COUNT");
		sql.append("  		FROM");
		sql.append("  			GJT_TERM_COURSEINFO GTP,");
		sql.append("  			GJT_GRADE GRE,");
		sql.append("  			GJT_COURSE GCE,");
		sql.append("  			GJT_ORG GOG,");
		sql.append("  		  	(SELECT");
		sql.append("  		  		grr.COURSE_ID,gtp.TERM_ID,gci.CLASS_ID,gtp.TERMCOURSE_ID");
		sql.append("  		  	FROM");
		sql.append("  		  		GJT_STUDENT_INFO gsi LEFT JOIN GJT_REC_RESULT grr ON gsi.STUDENT_ID = grr.STUDENT_ID");
		sql.append("  		  		LEFT JOIN GJT_TERM_COURSEINFO gtp ON grr.TERMCOURSE_ID = gtp.TERMCOURSE_ID,");
		sql.append("  		  		GJT_CLASS_INFO gci,");
		sql.append("  		  		GJT_CLASS_STUDENT gcs ");
		sql.append("  		  	WHERE");
		sql.append("  		  		gsi.IS_DELETED = 'N'");
		sql.append("  		  		AND grr.IS_DELETED = 'N'");
		sql.append("  		  		AND gci.IS_DELETED = 'N'");
		sql.append("  		  		AND gcs.IS_DELETED = 'N'");
		sql.append("  		  		AND gtp.IS_DELETED = 'N'");
		sql.append("  		  		AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  		  		AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  		  		AND gci.CLASS_TYPE = 'teach'");
		sql.append("  		  		AND gci.CLASS_ID=:CLASS_ID GROUP BY grr.COURSE_ID,gtp.TERM_ID,gci.CLASS_ID,gtp.TERMCOURSE_ID");
		sql.append("  		   ) T");
		sql.append("  		WHERE");
		sql.append("  			GTP.IS_DELETED = 'N'");
		sql.append("  			AND GRE.IS_DELETED = 'N'");
		sql.append("  			AND GCE.IS_DELETED = 'N'");
		sql.append("  			AND GOG.IS_DELETED = 'N'");
		sql.append("  			AND GRE.GRADE_ID = GTP.TERM_ID");
		sql.append("  			AND GTP.TERM_ID = T.TERM_ID");
		sql.append("  			AND GTP.TERMCOURSE_ID = T.TERMCOURSE_ID");
		sql.append("  			AND GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  			AND GCE.COURSE_ID = T.COURSE_ID");
		sql.append("  			AND gtp.XX_ID = GOG.ID");
		sql.append("  			AND ( GOG.ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID))");

		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRE.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		sql.append("  	GROUP BY gtp.TERMCOURSE_ID,GTP.COURSE_ID,gre.START_DATE,gre.END_DATE,gre.GRADE_ID,gre.GRADE_NAME,gce.KCMC,t.class_id) TAB  WHERE 1=1");

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TIME_FLG")))){
			sql.append(" AND TAB.TIME_FLG = :TIME_FLG");
			params.put("TIME_FLG",ObjectUtils.toString(searchParams.get("TIME_FLG")));
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("orgType")))&&"6".equals(ObjectUtils.toString(searchParams.get("orgType")))){//学习中心的帐号登录，过滤掉不属于此个人中心的课程
			sql.append(" AND TAB.REC_COUNT >0");
		}

		Map<String,Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql",sql.toString());
		handlerMap.put("params",params);

		return handlerMap;
	}


	/**
	 * 课程考勤列表-无分页
	 * @param searchParams
	 * @return
	 */
	public List<Map<String,Object>> courseAttendanceList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.courseAttendanceListSqlHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForMapListNative(sql,params);
	}

	/**
	 * 课程考勤详情
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String,Object>> courseAttendanceDetails(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.courseAttendanceDetailsHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(),params,pageRequst);
	}

	/**
	 * 课程考勤明细sql处理
	 * @param searchParams
	 * @return
	 */
	public Map<String,Object> courseAttendanceDetailsHandler(Map searchParams){
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  SELECT");
		sql.append("  	STUDENT_ID,XH,XM,SJH,PYCC,PYCC_NAME,GRADE_ID,GRADE_NAME,SPECIALTY_ID,RUXUE_TERM,RUXUE_YEAR,ZP,EENO,TIME_FLG,ZYMC,LOGIN_COUNT,CLASS_ID,CLASS_TYPE,");
		sql.append("  	TERMCOURSE_ID,COURSE_ID,LOGIN_TIME,LEFT_DAY,LAST_DATE,IS_ONLINE,BJMC,YEAR_NAME,KCMC,PC_ONLINE_COUNT,APP_ONLINE_COUNT,ALL_ONLINE_COUNT,");
		sql.append("  	(CASE WHEN LOGIN_COUNT > 0 THEN ROUND(PC_ONLINE_COUNT / LOGIN_COUNT*100) ELSE 0 END) PC_ONLINE_PERCENT,");
		sql.append("  	(CASE WHEN LOGIN_COUNT > 0 THEN ROUND(( 1 - PC_ONLINE_COUNT / LOGIN_COUNT )* 100 ) ELSE 0 END) APP_ONLINE_PERCENT");
		sql.append("  FROM (");
		sql.append("  		SELECT");
		sql.append("  			GSI.STUDENT_ID,GSI.XH,GSI.XM,GSI.SJH,GSI.PYCC,GTP.TERMCOURSE_ID,GCE.COURSE_ID,gsi.AVATAR ZP,gsi.EENO,gci.CLASS_ID,gci.CLASS_TYPE,");
		sql.append("  			(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  			GRE.GRADE_ID,GRE.GRADE_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,NVL( GSR.LOGIN_TIMES, 0 ) LOGIN_COUNT,NVL( ROUND( GSR.ONLINE_TIME / 60, 1 ), 0 ) LOGIN_TIME,");
		sql.append("  			(CASE WHEN SYSDATE >= gre.START_DATE AND SYSDATE <= gre.END_DATE THEN '1' WHEN SYSDATE < gre.START_DATE THEN '2' WHEN SYSDATE > gre.END_DATE THEN '3' ELSE '0' END) TIME_FLG,");
		sql.append("  			FLOOR( SYSDATE - GSR.LAST_LOGIN_DATE ) LEFT_DAY,TO_CHAR( GSR.LAST_LOGIN_DATE, 'yyyy-mm-dd hh24:mi' ) LAST_DATE,GSR.IS_ONLINE,GY.NAME YEAR_NAME,");
		sql.append("  			(SELECT GCI.BJMC FROM GJT_CLASS_INFO GCI,GJT_CLASS_STUDENT GCS WHERE GCI.IS_DELETED = 'N' AND GCS.IS_DELETED = 'N' AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  					AND GCI.CLASS_TYPE = 'course' AND gci.COURSE_ID = gtp.COURSE_ID AND GCS.STUDENT_ID = GSI.STUDENT_ID AND ROWNUM = 1) BJMC,");
		sql.append("  			(SELECT gc.KCMC FROM GJT_COURSE gc WHERE gc.IS_DELETED = 'N' AND gc.COURSE_ID = gtp.COURSE_ID AND ROWNUM = 1) KCMC,");
		sql.append("  			NVL( gsr.PC_ONLINE_COUNT, 0 ) PC_ONLINE_COUNT,NVL( gsr.APP_ONLINE_COUNT, 0 ) APP_ONLINE_COUNT,");
		sql.append("  			(NVL( gsr.APP_ONLINE_COUNT, 0 )+ NVL( gsr.APP_ONLINE_COUNT, 0 )) ALL_ONLINE_COUNT,");
		sql.append("  			(SELECT g.GRADE_NAME FROM GJT_GRADE g WHERE g.GRADE_ID = gsi.NJ) RUXUE_TERM,");
		sql.append("  			(SELECT Y.NAME FROM GJT_GRADE g LEFT JOIN GJT_YEAR y ON g.YEAR_ID = y.GRADE_ID WHERE g.GRADE_ID = gsi.NJ) RUXUE_YEAR");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO GSI LEFT JOIN GJT_CLASS_STUDENT gst ON gsi.STUDENT_ID=gst.STUDENT_ID LEFT JOIN GJT_CLASS_INFO gio ON gst.CLASS_ID=gio.CLASS_ID,");
		sql.append("  			GJT_CLASS_STUDENT GCS,");
		sql.append("  			GJT_CLASS_INFO GCI,");
		sql.append("  			GJT_GRADE GRE,");
		sql.append("  			GJT_YEAR gy,");
		sql.append("  			GJT_SPECIALTY GSY,");
		sql.append("  			GJT_COURSE GCE,");
		sql.append("  			GJT_TERM_COURSEINFO GTP,");
		sql.append("  			GJT_REC_RESULT GRR LEFT JOIN VIEW_TEACH_PLAN vtp ON grr.TEACH_PLAN_ID=vtp.TEACH_PLAN_ID");
		sql.append("  		LEFT JOIN VIEW_STUDENT_STUDY_SITUATION GSR ON");
		sql.append("  			GRR.REC_ID = GSR.CHOOSE_ID");
		sql.append("  		WHERE");
		sql.append("  			GSI.IS_DELETED = 'N'");
		sql.append("  			AND GCS.IS_DELETED = 'N'");
		sql.append("  			AND GCI.IS_DELETED = 'N'");
		sql.append("  			AND GRE.IS_DELETED = 'N'");
		sql.append("  			AND GSY.IS_DELETED = 'N'");
		sql.append("  			AND GCE.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GRR.IS_DELETED = 'N'");
		sql.append("  			AND gst.IS_DELETED = 'N'");
		sql.append("  			AND gio.IS_DELETED = 'N'");
		sql.append("  			AND GSI.GRADE_SPECIALTY_ID = vtp.GRADE_SPECIALTY_ID");
		sql.append("  			AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  			AND GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  			AND GCI.CLASS_TYPE = 'course'");
		sql.append("  			AND gio.CLASS_TYPE='teach'");
		sql.append("  			AND GTP.TERM_ID = GRE.GRADE_ID");
		sql.append("  			AND gy.GRADE_ID = gre.YEAR_ID");
		sql.append("  			AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  			AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  			AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  			AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  			AND gce.COURSE_ID = gtp.COURSE_ID");
		sql.append("  			AND vtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID");
		sql.append("  			AND vtp.ACTUAL_GRADE_ID = gtp.TERM_ID");
		sql.append("  			AND GRR.TERMCOURSE_ID = GTP.TERMCOURSE_ID");
		sql.append("  			AND grr.COURSE_ID =:COURSE_ID");
		sql.append("  			AND gio.CLASS_ID=:CLASS_ID");
		sql.append("  			AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");


		params.put("CLASS_ID",ObjectUtils.toString(searchParams.get("CLASS_ID")));
		params.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));

		if(EmptyUtils.isNotEmpty(searchParams.get("courseClass"))){
			params.put("courseClass",ObjectUtils.toString(searchParams.get("courseClass")));
			sql.append(" AND GCI.CLASS_ID=:courseClass");
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIALTY_ID"))){
			params.put("SPECIALTY_ID",ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
			sql.append(" AND GS.SPECIALTY_ID=:SPECIALTY_ID");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("startGrade")))){
			sql.append("  		AND GSI.NJ = :startGrade");
			params.put("startGrade",ObjectUtils.toString(searchParams.get("startGrade")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("gradeId")))){
			sql.append("  		AND GTP.TERM_ID = :gradeId");
			params.put("gradeId",ObjectUtils.toString(searchParams.get("gradeId")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))){
			sql.append("  			AND gsi.XM LIKE '%%'");
			params.put("XM","%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))){
			sql.append("  			AND gsi.XH = :XH");
			params.put("XH",ObjectUtils.toString(searchParams.get("XH")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))){
			sql.append("  			AND gsi.PYCC = :PYCC");
			params.put("PYCC",ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ZYMC")))){
			sql.append("  			AND GSY.ZYMC LIKE :ZYMC");
			params.put("ZYMC","%"+ObjectUtils.toString(searchParams.get("ZYMC"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))){
			if("PC".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))){
				sql.append("  			AND GSR.PC_ONLINE_COUNT>GSR.APP_ONLINE_COUNT");
			}else if("APP".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))){
				sql.append("  			AND GSR.PC_ONLINE_COUNT<GSR.APP_ONLINE_COUNT");
			}
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))){
			if("0".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))){//在线
				sql.append("  			AND GSR.IS_ONLINE = 'Y' AND GSR.LOGIN_TIMES >0 ");
			}else if("4".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))){//从未登录
				sql.append("  			AND (GSR.LOGIN_TIMES =0 or GSR.LOGIN_TIMES is null)");
			}else if("3".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))){//离线（3天内未学习）
				sql.append("  			AND GSR.LAST_LOGIN_DATE <SYSDATE AND GSR.LAST_LOGIN_DATE >=SYSDATE-3");
			}else if("2".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))){//离线（3天以上未学习）
				sql.append("  			AND GSR.LAST_LOGIN_DATE <SYSDATE-3 AND GSR.LAST_LOGIN_DATE >=SYSDATE-7 ");
			}else if("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//离线（7天以上未学习）
				sql.append("  			AND GSR.LAST_LOGIN_DATE <SYSDATE-7");
			}
		}

		sql.append("  	) TAB WHERE 1 = 1");

		Map<String,Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql",sql.toString());
		handlerMap.put("params",params);

		return handlerMap;
	}

	/**
	 * 课程考勤详情-无分页
	 * @param searchParams
	 * @return
	 */
	public List<Map<String,Object>> courseAttendanceDetails(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.courseAttendanceDetailsHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql,params);
	}

	/**
	 * 课程学情
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map<String,Object>> getCourseListPage(Map<String, Object> searchParams, PageRequest pageRequest) {

		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.getCourseListHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}

	/**
	 * 课程学情 sql处理类
	 * @param searchParams
	 * @return
	 */
	public Map<String,Object> getCourseListHandler(Map searchParams){
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();


		sql.append("  SELECT");
		sql.append("  	COURSE_ID,SDATE,EDATE,TIME_FLG,GRADE_ID,GRADE_NAME,KCMC,REC_COUNT,SUM_SCHEDULE,SUM_STUDY_SCORE,SUM_EXAM_SCORE,SUM_TOTAL_SCORE,SUM_LOGIN_COUNT,SUM_LOGIN_TIME,SUM_REGISTER_COUNT,SUM_PASS_COUNT,SUM_UNPASS_COUNT,");
		sql.append("  	AVG_SCHEDULE,AVG_STUDY_SCORE,AVG_EXAM_SCORE,AVG_TOTAL_SCORE,AVG_LOGIN_COUNT,AVG_LOGIN_TIME,AVG_PASS_COUNT,NEVER_STUDY_PERCENT,SUM_NEVER_STUDY,SUM_STUDY_IMG,STUDY_IMG_PERCENT");
		sql.append("  FROM (");
		sql.append("  		SELECT");
		sql.append("  			COURSE_ID,SDATE,EDATE,TIME_FLG,GRADE_ID,GRADE_NAME,KCMC,REC_COUNT,SUM_SCHEDULE,SUM_STUDY_SCORE,SUM_LOGIN_COUNT,SUM_LOGIN_TIME,SUM_REGISTER_COUNT,SUM_PASS_COUNT,SUM_UNPASS_COUNT,SUM_EXAM_SCORE,SUM_TOTAL_SCORE,SUM_NEVER_STUDY,SUM_STUDY_IMG,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR( SUM_SCHEDULE / REC_COUNT ) ELSE 0 END) AVG_SCHEDULE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR( SUM_STUDY_SCORE / REC_COUNT ) ELSE 0 END) AVG_STUDY_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR( SUM_LOGIN_COUNT / REC_COUNT ) ELSE 0 END) AVG_LOGIN_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND( SUM_LOGIN_TIME / REC_COUNT, 1 ) ELSE 0 END) AVG_LOGIN_TIME,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND(( SUM_PASS_COUNT / REC_COUNT )* 100, 1 ) ELSE 0 END) AVG_PASS_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND(( SUM_REGISTER_COUNT / REC_COUNT )* 100, 1 ) ELSE 0 END) AVG_REGISTER_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND(( SUM_UNPASS_COUNT / REC_COUNT )* 100, 1 ) ELSE 0 END) AVG_UNPASS_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR(( SUM_EXAM_SCORE / REC_COUNT )) ELSE 0 END ) AVG_EXAM_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR(( SUM_TOTAL_SCORE / REC_COUNT )) ELSE 0 END) AVG_TOTAL_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND(( SUM_NEVER_STUDY / REC_COUNT )* 100, 1 ) ELSE 0 END) NEVER_STUDY_PERCENT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND(( SUM_STUDY_IMG / REC_COUNT )* 100, 1 ) ELSE 0 END) STUDY_IMG_PERCENT");
		sql.append("  		FROM (");
		sql.append("  						SELECT");
		sql.append("  							gtp.TERMCOURSE_ID,gce.COURSE_ID,TO_CHAR( gre.START_DATE, 'yyyy-mm-dd' ) SDATE,TO_CHAR( gre.END_DATE, 'yyyy-mm-dd' ) EDATE,GRE.GRADE_ID,GRE.GRADE_NAME,GCE.KCMC,");
		sql.append("  							(CASE WHEN SYSDATE >= gre.START_DATE AND SYSDATE <= gre.END_DATE THEN '1' WHEN SYSDATE < gre.START_DATE THEN '2' WHEN SYSDATE > gre.END_DATE THEN '3' ELSE '0' END) TIME_FLG,");
		sql.append("  							(SELECT COUNT( GRR.REC_ID ) FROM GJT_REC_RESULT GRR LEFT JOIN GJT_STUDENT_INFO gsi ON grr.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE GRR.IS_DELETED = 'N' AND gsi.IS_DELETED='N' AND gcs.IS_DELETED='N' AND GRR.TERMCOURSE_ID=GTP.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID");
		sql.append("  									AND EXISTS (SELECT 1 FROM GJT_STUDENT_INFO gsi WHERE gsi.STUDENT_ID = grr.STUDENT_ID) AND grr.COURSE_ID=gce.COURSE_ID");
		sql.append("  									AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) REC_COUNT,");
		sql.append("  							(SELECT SUM( NVL( GSR.PROGRESS, 0 )) FROM VIEW_STUDENT_STUDY_SITUATION GSR LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE gsr.TERMCOURSE_ID=gtp.TERMCOURSE_ID  AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID ");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_SCHEDULE,");
		sql.append("  							(SELECT SUM( NVL( GRR.EXAM_SCORE, 0 )) FROM GJT_REC_RESULT GRR LEFT JOIN GJT_STUDENT_INFO gsi ON grr.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE GRR.IS_DELETED = 'N' AND GRR.TERMCOURSE_ID=GTP.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID ");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_STUDY_SCORE,");
		sql.append("  							(SELECT SUM( NVL( GRR.EXAM_SCORE1, 0 )) FROM GJT_REC_RESULT GRR LEFT JOIN GJT_STUDENT_INFO gsi ON grr.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE GRR.IS_DELETED = 'N' AND GRR.TERMCOURSE_ID=GTP.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_EXAM_SCORE,");
		sql.append("  							(SELECT SUM( NVL( GRR.EXAM_SCORE2, 0 )) FROM GJT_REC_RESULT GRR LEFT JOIN GJT_STUDENT_INFO gsi ON grr.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE GRR.IS_DELETED = 'N' AND GRR.TERMCOURSE_ID=GTP.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_TOTAL_SCORE,");
		sql.append("  							(SELECT SUM( NVL( GSR.LOGIN_TIMES, 0 )) FROM VIEW_STUDENT_STUDY_SITUATION GSR LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE  gsr.TERMCOURSE_ID=gtp.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_LOGIN_COUNT,");
		sql.append("  							(SELECT ROUND( SUM( NVL( GSR.ONLINE_TIME, 0 ))/ 60, 1 ) FROM VIEW_STUDENT_STUDY_SITUATION GSR LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE  gsr.TERMCOURSE_ID=gtp.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID");
		sql.append("  								AND(gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_LOGIN_TIME,");
		sql.append("  							(SELECT COUNT( GSR.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION gsr LEFT JOIN GJT_STUDENT_INFO gsi ON gsr.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE  gsr.TERMCOURSE_ID=gtp.TERMCOURSE_ID");
		sql.append("  								AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID AND NVL(gsr.LOGIN_TIMES,0) = 0");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_NEVER_STUDY,");
		sql.append("  							(SELECT COUNT( VSS.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION VSS LEFT JOIN GJT_STUDENT_INFO gsi ON VSS.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE VSS.TERMCOURSE_ID=GTP.TERMCOURSE_ID");
		sql.append("  								AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID AND VSS.EXAM_STATE = '2' AND VSS.LOGIN_TIMES > 0 AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_STUDY_IMG,");
		sql.append("  							(SELECT COUNT( VSS.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION VSS LEFT JOIN GJT_STUDENT_INFO gsi ON VSS.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE VSS.EXAM_STATE = '0' AND VSS.TERMCOURSE_ID=GTP.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_UNPASS_COUNT,");
		sql.append("  							(SELECT COUNT( VSS.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION VSS LEFT JOIN GJT_STUDENT_INFO gsi ON VSS.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE VSS.EXAM_STATE = '3' AND VSS.TERMCOURSE_ID=GTP.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_REGISTER_COUNT,");
		sql.append("  							(SELECT COUNT( VSS.CHOOSE_ID ) FROM VIEW_STUDENT_STUDY_SITUATION VSS LEFT JOIN GJT_STUDENT_INFO gsi ON VSS.STUDENT_ID = gsi.STUDENT_ID,GJT_CLASS_STUDENT gcs WHERE VSS.EXAM_STATE = '1' AND VSS.TERMCOURSE_ID=GTP.TERMCOURSE_ID AND gsi.STUDENT_ID=gcs.STUDENT_ID AND gcs.CLASS_ID=t.CLASS_ID");
		sql.append("  								AND (gsi.XX_ID =:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID))");
		sql.append("  							) SUM_PASS_COUNT");
		sql.append("  						FROM");
		sql.append("  							GJT_TERM_COURSEINFO GTP,");
		sql.append("  							GJT_GRADE GRE,");
		sql.append("  							GJT_COURSE GCE,");
		sql.append("  							GJT_ORG GOG,");
		sql.append("  							(SELECT");
		sql.append("  							  		grr.COURSE_ID,gtp.TERM_ID,gci.CLASS_ID,GTP.TERMCOURSE_ID");
		sql.append("  							  	FROM");
		sql.append("  							  		GJT_STUDENT_INFO gsi LEFT JOIN GJT_REC_RESULT grr ON gsi.STUDENT_ID = grr.STUDENT_ID");
		sql.append("  							  		LEFT JOIN GJT_TERM_COURSEINFO gtp ON grr.TERMCOURSE_ID = gtp.TERMCOURSE_ID,");
		sql.append("  							  		GJT_CLASS_INFO gci,");
		sql.append("  							  		GJT_CLASS_STUDENT gcs ");
		sql.append("  							  	WHERE");
		sql.append("  							  		gsi.IS_DELETED = 'N'");
		sql.append("  							  		AND grr.IS_DELETED = 'N'");
		sql.append("  							  		AND gci.IS_DELETED = 'N'");
		sql.append("  							  		AND gcs.IS_DELETED = 'N'");
		sql.append("  							  		AND gtp.IS_DELETED = 'N'");
		sql.append("  							  		AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  							  		AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  							  		AND gci.CLASS_TYPE = 'teach'");
		sql.append("  							  		AND gci.CLASS_ID=:CLASS_ID GROUP BY grr.COURSE_ID,gtp.TERM_ID,gci.CLASS_ID,GTP.TERMCOURSE_ID");
		sql.append("  							  ) t");
		sql.append("  						WHERE");
		sql.append("  							GTP.IS_DELETED = 'N'");
		sql.append("  							AND GRE.IS_DELETED = 'N'");
		sql.append("  							AND GCE.IS_DELETED = 'N'");
		sql.append("  							AND GOG.IS_DELETED = 'N'");
		sql.append("  							AND GRE.GRADE_ID = GTP.TERM_ID");
		sql.append("  							AND GTP.TERM_ID = T.TERM_ID");
		sql.append("  							AND GTP.TERMCOURSE_ID = T.TERMCOURSE_ID");
		sql.append("  							AND GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  							AND GCE.COURSE_ID = T.COURSE_ID");
		sql.append("  							AND gtp.XX_ID = GOG.ID");
		sql.append("  							AND (GOG.ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID))");

		params.put("CLASS_ID",ObjectUtils.toString(searchParams.get("CLASS_ID")));
		params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))){
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
			sql.append("	AND GCE.COURSE_ID=:COURSE_ID");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
			sql.append(" AND GRE.GRADE_ID=:GRADE_ID");
		}

		sql.append("  							GROUP BY gtp.TERMCOURSE_ID,gce.COURSE_ID,gre.START_DATE,gre.END_DATE,gre.GRADE_ID,gre.GRADE_NAME,gce.KCMC,t.class_id");
		sql.append("  )) TAB WHERE 1 = 1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE > "+ ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE < "+ ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE >= "+ ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE <= "+ ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE = "+ ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			}
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE > "+ ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE < "+ ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE >= "+ ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE <= "+ ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE = "+ ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			}
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PASS_PERCENT")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.PASS_PERCENT > "+ ObjectUtils.toString(searchParams.get("PASS_PERCENT")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.PASS_PERCENT < "+ ObjectUtils.toString(searchParams.get("PASS_PERCENT")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.PASS_PERCENT >= "+ ObjectUtils.toString(searchParams.get("PASS_PERCENT")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.PASS_PERCENT <= "+ ObjectUtils.toString(searchParams.get("PASS_PERCENT")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.PASS_PERCENT = "+ ObjectUtils.toString(searchParams.get("PASS_PERCENT")));
			}
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("orgType")))&&"6".equals(ObjectUtils.toString(searchParams.get("orgType")))){//学习中心的帐号登录，过滤掉不属于此个人中心的课程
			sql.append(" AND TAB.REC_COUNT >0");
		}

		Map<String,Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql",sql.toString());
		handlerMap.put("params",params);

		return handlerMap;

	}

	/**
	 * 课程学情-无分页
	 * @param searchParams
	 * @return
	 */
	public List<Map<String,Object>> getCourseList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.getCourseListHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 课程学情详情页
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map<String,Object>> courseLearnConditionDetails(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.courseLearnConditionDetailsHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(),params,pageRequest);
	}


	public Map<String,Object> courseLearnConditionDetailsHandler(Map searchParams){
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  SELECT");
		sql.append("  	gtp.TERMCOURSE_ID,gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsr.EXAM_SCORE,gsr.EXAM_SCORE1,gsr.EXAM_SCORE2,grr.COURSE_SCHEDULE,gsr.LOGIN_TIMES LOGIN_COUNT,gci.CLASS_ID,gci.CLASS_TYPE,gci.COURSE_ID,");
		sql.append("  	ROUND( NVL( gsr.ONLINE_TIME, 0 )/ 60, 1 ) LOGIN_TIME,gci.BJMC,gsr.PROGRESS SCHEDULE,gg.GRADE_NAME,gy.NAME YEAR_NAME,gsy.ZYMC,gc.KCMC,gci.SPECIALTY_ID,gci.TEACH_PLAN_ID,");
		sql.append("  	(CASE WHEN gsr.EXAM_STATE = '0' THEN '未通过' WHEN gsr.EXAM_STATE = '1' THEN '已通过' WHEN gsr.EXAM_STATE = '2' THEN '学习中' WHEN gsr.EXAM_STATE = '3' THEN '登记中' ELSE '--' END) EXAM_STATE,");
		sql.append("  	(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  	(SELECT g.GRADE_NAME FROM GJT_GRADE g WHERE g.GRADE_ID = gsi.NJ AND g.IS_DELETED = 'N' AND rownum = 1) START_GRADE,");
		sql.append("  	(SELECT gsc.SC_NAME FROM GJT_STUDY_CENTER gsc WHERE gsc.IS_DELETED = 'N' AND gsc.ID = gsi.XXZX_ID) SC_NAME,");
		sql.append("  	(CASE WHEN SYSDATE >= gg.START_DATE AND SYSDATE <= gg.END_DATE THEN '1' WHEN SYSDATE < gg.START_DATE THEN '2' WHEN SYSDATE > gg.END_DATE THEN '3' ELSE '0' END) TIME_FLG");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT grr");
		sql.append("  LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON");
		sql.append("  	grr.REC_ID = gsr.CHOOSE_ID");
		sql.append("  LEFT JOIN GJT_TERM_COURSEINFO gtp ON");
		sql.append("  	grr.TERMCOURSE_ID = gtp.TERMCOURSE_ID");
		sql.append("  LEFT JOIN GJT_COURSE gc ON");
		sql.append("  	gtp.COURSE_ID = gc.COURSE_ID");
		sql.append("  LEFT JOIN GJT_GRADE gg ON");
		sql.append("  	gg.GRADE_ID = gtp.TERM_ID");
		sql.append("  LEFT JOIN GJT_YEAR gy ON");
		sql.append("  	gy.GRADE_ID = gg.YEAR_ID,");
		sql.append("  	gjt_class_info gci,");
		sql.append("  	gjt_class_student gcs,");
		sql.append("  	GJT_STUDENT_INFO gsi  LEFT JOIN GJT_CLASS_STUDENT gst ON gsi.STUDENT_ID=gst.STUDENT_ID LEFT JOIN GJT_CLASS_INFO gio ON gst.CLASS_ID=gio.CLASS_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY GSY ON");
		sql.append("  	gsi.MAJOR = gsy.SPECIALTY_ID");
		sql.append("  WHERE");
		sql.append("  	grr.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED = 'N'");
		sql.append("  	AND gtp.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gst.IS_DELETED = 'N'");
		sql.append("  	AND gio.IS_DELETED = 'N'");
		sql.append("  	AND grr.XX_ID = gsi.XX_ID");
		sql.append("  	AND grr.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  	AND gci.TERMCOURSE_ID = gtp.TERMCOURSE_ID");
		sql.append("  	AND gci.CLASS_TYPE = 'course'");
		sql.append("  	AND gio.CLASS_TYPE='teach'");


		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XX_ID")))){
			sql.append(" AND (GSI.XX_ID=:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))){
			sql.append("  	AND gio.BJMC LIKE :BJMC");//教务班名称
			params.put("BJMC", "%"+ObjectUtils.toString(searchParams.get("BJMC"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))){
			sql.append("  	AND gio.CLASS_ID=:CLASS_ID");//教务班
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))){
			sql.append("  	AND gtp.COURSE_ID =:COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("gradeId")))){
			sql.append("  	AND gtp.TERM_ID =:gradeId");
			params.put("gradeId",ObjectUtils.toString(searchParams.get("gradeId")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))){
			sql.append("  			AND gsi.XM LIKE :XM");
			params.put("XM","%"+ ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))){
			sql.append("  			AND gsi.XH = :XH");
			params.put("XH",ObjectUtils.toString(searchParams.get("XH")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIALTY_ID"))){
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
			sql.append(" AND GSY.SPECIALTY_ID=:SPECIALTY_ID");
		}

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("examScore")))){
			params.put("examScore",ObjectUtils.toString(searchParams.get("examScore")));
			sql.append("  	AND grr.EXAM_SCORE "+ObjectUtils.toString(searchParams.get("examSymbol"),"=")+" :examScore");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("schedule")))){
			params.put("schedule",ObjectUtils.toString(searchParams.get("schedule")));
			sql.append("  	AND gsr.SCHEDULE "+ObjectUtils.toString(searchParams.get("scheduleSymbol"),"=")+" :schedule");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("startGrade")))){
			params.put("startGrade",ObjectUtils.toString(searchParams.get("startGrade")));
			sql.append("  	AND gsi.NJ = :startGrade");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))){
			params.put("PYCC",ObjectUtils.toString(searchParams.get("PYCC")));
			sql.append("  	AND gsi.PYCC = :PYCC");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("examState")))){
			params.put("examState",ObjectUtils.toString(searchParams.get("examState")));
			sql.append("  	AND grr.EXAM_STATE = :examState");
		}

		Map<String,Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql",sql.toString());
		handlerMap.put("params",params);
		return handlerMap;
	}

	/**
	 * 课程学情详情
	 * @param searchParams
	 * @return
	 */
	public List<Map<String,Object>> courseLearnConditionDetails(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.courseLearnConditionDetailsHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForMapListNative(sql.toString(), params);
	}


	/**
	 * 学员课程考勤列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getStudentLoginList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.getStudentLoginListSqlHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(),params,pageRequst);
	}

	/**
	 * 学习管理=》学员考勤无分页
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, Object>> getStudentLoginList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.getStudentLoginListSqlHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql,params);
	}

	public  int getStudentLoginCount(Map searchParams){
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  	  SELECT  COUNT(STUDENT_ID)");
		sql.append("  FROM");
		sql.append("  	   (SELECT");
		sql.append("  			gsi.STUDENT_ID,gsi.AVATAR ZP,gsi.EENO,gsi.XM,gsi.XH,gsi.SJH,gci.BJMC,gg.GRADE_NAME,gy.NAME YEAR_NAME,gs.ZYMC,gs.RULE_CODE,gsi.XXZX_ID,");
		sql.append("  			(SELECT GSC.SC_NAME FROM GJT_STUDY_CENTER gsc WHERE gsc.IS_DELETED='N' AND gsc.ID=gsi.XXZX_ID) SC_NAME,");
		sql.append("  			(SELECT vss.IS_ONLINE  FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID AND VSS.IS_ONLINE='Y' AND ROWNUM=1) IS_ONLINE,");
		sql.append("  			(SELECT MAX( vss.LAST_LOGIN_DATE ) FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID) LAST_LOGIN_TIME,");
		sql.append("  			(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  			(SELECT vss.BYOD_TYPE FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID ");
		sql.append("  			AND vss.LAST_LOGIN_DATE =(SELECT MAX( vs.LAST_LOGIN_DATE ) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vs ON grr.REC_ID=vs.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vs.STUDENT_ID AND vs.STUDENT_ID = gsi.STUDENT_ID AND ROWNUM =1) AND ROWNUM = 1) DEVICE,");
		sql.append("  			(SELECT SUM( NVL( vss.LOGIN_TIMES, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) LOGIN_COUNT,");
		sql.append("  			(SELECT ROUND( SUM( NVL( vss.ONLINE_TIME, 0 ))/ 60, 1 ) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIME,");
		sql.append("  			(SELECT SUM( NVL( vss.PC_ONLINE_COUNT, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) PC_ONLINE_COUNT,");
		sql.append("  			(SELECT SUM( NVL( vss.APP_ONLINE_COUNT, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) APP_ONLINE_COUNT,");
		sql.append("  			(SELECT SUM( NVL( vss.APP_ONLINE_COUNT, 0 ))+ SUM( NVL( vss.PC_ONLINE_COUNT, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) ALL_ONLINE_COUNT");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO gsi");
		sql.append("  		LEFT JOIN GJT_GRADE GG ON");
		sql.append("  			gsi.NJ = gg.GRADE_ID");
		sql.append("  		LEFT JOIN GJT_YEAR gy ON");
		sql.append("  			gy.GRADE_ID = gg.YEAR_ID");
		sql.append("  		LEFT JOIN GJT_SPECIALTY gs ON");
		sql.append("  			gsi.MAJOR = gs.SPECIALTY_ID,");
		sql.append("  			GJT_CLASS_INFO gci,");
		sql.append("  			GJT_CLASS_STUDENT gcs");
		sql.append("  		WHERE");
		sql.append("  			gsi.IS_DELETED = 'N'");
		sql.append("  			AND gci.IS_DELETED = 'N'");
		sql.append("  			AND gcs.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N'");
		sql.append("  			AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  			AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  			AND gci.CLASS_TYPE = 'teach'");

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		if (EmptyUtils.isNotEmpty(studyId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxId);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND gsi.STUDENT_ID =:STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")) );
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GG.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND gs.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND gsi.PYCC=:PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND gci.CLASS_ID=:CLASS_ID");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  ORDER BY gsi.XM) TAB WHERE 1=1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND TAB.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//在线
				sql.append(" AND IS_ONLINE = 'Y'");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//从未登录
				sql.append(" AND (ALL_ONLINE_COUNT = 0 OR ALL_ONLINE_COUNT IS NULL)");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//离线（3天内未学习）
				sql.append(" AND LAST_LOGIN_TIME <SYSDATE AND LAST_LOGIN_TIME >=SYSDATE-3");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//离线（3天以上未学习）
				sql.append(" AND LAST_LOGIN_TIME <SYSDATE-3 AND LAST_LOGIN_TIME >=SYSDATE-7 ");
			} else if ("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//离线（7天以上未学习）
				sql.append(" AND LAST_LOGIN_TIME <SYSDATE-7");
			}
		}
		BigDecimal num = (BigDecimal)commonDao.queryObjectNative(sql.toString(), params);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	/**
	 * 学习管理=》考勤分析=》学员考勤
	 * @return
	 */
	public Map<String,Object> getStudentLoginListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  	  SELECT ");
		sql.append("  	STUDENT_ID,ZP,EENO,XM,XH,SJH,BJMC,GRADE_NAME,YEAR_NAME,ZYMC,RULE_CODE,NVL(IS_ONLINE,'N') IS_ONLINE,LOGIN_TIME,XXZX_ID,SC_NAME,");
		sql.append("  	TO_CHAR( LAST_LOGIN_TIME, 'yyyy-MM-dd hh24:mi' ) LAST_LOGIN_TIME,PYCC_NAME,LOGIN_COUNT,FLOOR(( SYSDATE - LAST_LOGIN_TIME )) LEFT_DAY,");
		sql.append("  	(CASE WHEN ALL_ONLINE_COUNT > 0 THEN CEIL(PC_ONLINE_COUNT / ALL_ONLINE_COUNT*100) ELSE 0 END) PC_ONLINE_PERCENT,");
		sql.append("  	(CASE WHEN DEVICE = 'PC' THEN 'PC' WHEN DEVICE = 'PHONE' THEN 'APP' WHEN DEVICE = 'PAD' THEN 'APP' ELSE '--' END) DEVICE,");
		sql.append("  	(CASE WHEN ALL_ONLINE_COUNT > 0 THEN FLOOR(( 1 - PC_ONLINE_COUNT / ALL_ONLINE_COUNT )* 100 ) ELSE 0 END) APP_ONLINE_PERCENT,");
		sql.append("  	PC_ONLINE_COUNT,APP_ONLINE_COUNT,ALL_ONLINE_COUNT");
		sql.append("  FROM");
		sql.append("  	   (SELECT");
		sql.append("  			gsi.STUDENT_ID,gsi.AVATAR ZP,gsi.EENO,gsi.XM,gsi.XH,gsi.SJH,gci.BJMC,gg.GRADE_NAME,gy.NAME YEAR_NAME,gs.ZYMC,gs.RULE_CODE,gsi.XXZX_ID,");
		sql.append("  			(SELECT GSC.SC_NAME FROM GJT_STUDY_CENTER gsc WHERE gsc.IS_DELETED='N' AND gsc.ID=gsi.XXZX_ID) SC_NAME,");
		sql.append("  			(SELECT vss.IS_ONLINE  FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID AND VSS.IS_ONLINE='Y' AND ROWNUM=1) IS_ONLINE,");
		sql.append("  			(SELECT MAX( vss.LAST_LOGIN_DATE ) FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID) LAST_LOGIN_TIME,");
		sql.append("  			(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  			(SELECT vss.BYOD_TYPE FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID ");
		sql.append("  			AND vss.LAST_LOGIN_DATE =(SELECT MAX( vs.LAST_LOGIN_DATE ) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vs ON grr.REC_ID=vs.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vs.STUDENT_ID AND vs.STUDENT_ID = gsi.STUDENT_ID AND ROWNUM =1) AND ROWNUM = 1) DEVICE,");
		sql.append("  			(SELECT SUM( NVL( vss.LOGIN_TIMES, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) LOGIN_COUNT,");
		sql.append("  			(SELECT ROUND( SUM( NVL( vss.ONLINE_TIME, 0 ))/ 60, 1 ) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIME,");
		sql.append("  			(SELECT SUM( NVL( vss.PC_ONLINE_COUNT, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) PC_ONLINE_COUNT,");
		sql.append("  			(SELECT SUM( NVL( vss.APP_ONLINE_COUNT, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) APP_ONLINE_COUNT,");
		sql.append("  			(SELECT SUM( NVL( vss.LOGIN_TIMES, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=vss.STUDENT_ID AND vss.STUDENT_ID = gsi.STUDENT_ID) ALL_ONLINE_COUNT");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO gsi");
		sql.append("  		LEFT JOIN GJT_GRADE GG ON");
		sql.append("  			gsi.NJ = gg.GRADE_ID");
		sql.append("  		LEFT JOIN GJT_YEAR gy ON");
		sql.append("  			gy.GRADE_ID = gg.YEAR_ID");
		sql.append("  		LEFT JOIN GJT_SPECIALTY gs ON");
		sql.append("  			gsi.MAJOR = gs.SPECIALTY_ID,");
		sql.append("  			GJT_CLASS_INFO gci,");
		sql.append("  			GJT_CLASS_STUDENT gcs");
		sql.append("  		WHERE");
		sql.append("  			gsi.IS_DELETED = 'N'");
		sql.append("  			AND gci.IS_DELETED = 'N'");
		sql.append("  			AND gcs.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N'");
		sql.append("  			AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  			AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  			AND gci.CLASS_TYPE = 'teach'");

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		if (EmptyUtils.isNotEmpty(studyId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxId);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND gsi.STUDENT_ID =:STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GG.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND gs.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND gsi.PYCC=:PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND gci.CLASS_ID=:CLASS_ID");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  ORDER BY gsi.XM) TAB WHERE 1=1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND TAB.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//在线
				sql.append(" AND IS_ONLINE = 'Y'");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//从未登录
				sql.append(" AND (ALL_ONLINE_COUNT = 0 OR ALL_ONLINE_COUNT IS NULL)");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//离线（3天内未学习）
				sql.append(" AND LAST_LOGIN_TIME <SYSDATE AND LAST_LOGIN_TIME >=SYSDATE-3");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//离线（3天以上未学习）
				sql.append(" AND LAST_LOGIN_TIME <SYSDATE-3 AND LAST_LOGIN_TIME >=SYSDATE-7 ");
			} else if ("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {//离线（7天以上未学习）
				sql.append(" AND LAST_LOGIN_TIME <SYSDATE-7");
			}
		}

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}


	/**
	 * 学支平台，模块学分查询
	 * @param param
	 * @return
	 */
	public List getCreditInfoAnd(Map<String, Object> param) {

		StringBuffer sql = new StringBuffer();

		Map params = new HashMap();

		sql.append("  SELECT");
		sql.append("  	tsd.NAME,");
		sql.append("  	gsi.MAJOR SPECIALTY_ID,");
		sql.append("  	gtp.kclbm kclbm_code,");
		sql.append("  	COUNT( gtp.TEACH_PLAN_ID ) AS COUNT_COURSE,");
		sql.append("  	SUM((CASE WHEN grr.EXAM_STATE='0' THEN 1 ELSE 0 END)) UN_PASS_COURSE,");//未通过课程
		sql.append("  	SUM((CASE WHEN grr.EXAM_STATE='1' THEN 1 ELSE 0 END)) PASS_COURSE,");//已通过课程
		sql.append("  	gsml.TOTALSCORE AS XF_COUNT,");
		sql.append("  	SUM( NVL( grr.GET_CREDITS, 0 )) AS GET_POINT,");
		sql.append("  	SUM( NVL( grr.EXAM_SCORE2, 0 )) AS SUM_SCORE,");
		sql.append("  	gsml.SCORE AS LIMIT_SCORE");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE GG,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  LEFT JOIN GJT_SPECIALTY_MODULE_LIMIT gsml ON");
		sql.append("  	gsi.MAJOR = gsml.SPECIALTY_ID,");
		sql.append("  	VIEW_TEACH_PLAN gtp");
		sql.append("  LEFT JOIN TBL_SYS_DATA tsd ON");
		sql.append("  	tsd.TYPE_CODE = 'CourseType'");
		sql.append("  	AND tsd.CODE = gtp.KCLBM,");
		sql.append("  	GJT_REC_RESULT grr");
		sql.append("  WHERE");
		sql.append("  	gg.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gtp.IS_DELETED = 'N'");
		sql.append("  	AND grr.IS_DELETED = 'N'");
		sql.append("  	AND gg.GRADE_ID = gtp.ACTUAL_GRADE_ID");
		sql.append("  	AND grr.COURSE_ID = gtp.COURSE_ID");
		sql.append("  	AND gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID");
		sql.append("  	AND gsi.STUDENT_ID = grr.STUDENT_ID");
		sql.append("  	AND gsml.SPECIALTY_ID = gsi.MAJOR");
		sql.append("  	AND gsml.MODULE_ID = tsd.ID");
		sql.append("  	AND gsi.STUDENT_ID = :studentId");
		sql.append("  GROUP BY");
		sql.append("  	GTP.KCLBM,");
		sql.append("  	tsd.NAME,");
		sql.append("  	gsi.MAJOR,");
		sql.append("  	gsml.SCORE,");
		sql.append("  	gsml.TOTALSCORE");
		sql.append("  ORDER BY");
		sql.append("  	GTP.KCLBM");

		params.put("studentId",ObjectUtils.toString(param.get("studentId"),""));

		return  commonDao.queryForMapListNative(sql.toString(),params);
	}

	/**
	 * 学支平台--首页批量导出班级学员信息
	 * @param searchParams
	 * @return
	 */
	public List<Map<String,Object>> getClassStudInfo(Map<String, Object> searchParams) {

		Map<String,Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	gsi.XM,gsi.XH,gsi.SFZH,gsi.SJH,gg.GRADE_NAME,gci.BJMC,");
		sql.append("  	(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  	(SELECT gei.XM FROM GJT_EMPLOYEE_INFO gei WHERE gei.IS_DELETED='N' AND gei.EMPLOYEE_ID=gci.BZR_ID AND gei.EMPLOYEE_TYPE='1' AND ROWNUM=1) TEACHER_NAME,");
		sql.append("  	(SELECT gsy.ZYMC FROM GJT_SPECIALTY gsy WHERE gsy.IS_DELETED='N' AND gsy.SPECIALTY_ID=gsi.MAJOR AND ROWNUM=1) ZYMC,");
		sql.append("  	(SELECT gsc.SC_NAME FROM GJT_STUDY_CENTER gsc WHERE gsc.IS_DELETED='N' AND gsc.ID=gsi.XXZX_ID) SC_NAME,");
		sql.append("  	(SELECT gsa.MOBILE FROM GJT_STUDENT_ADDRESS gsa WHERE gsa.IS_DEFAULT='0' AND gsa.IS_DELETED='N' AND gsa.STUDENT_ID=gsi.STUDENT_ID) MOBILE,");
		sql.append("  	(SELECT gsa.RECEIVER FROM GJT_STUDENT_ADDRESS gsa WHERE gsa.IS_DEFAULT='0' AND gsa.IS_DELETED='N' AND gsa.STUDENT_ID=gsi.STUDENT_ID) RECEIVER,");
		sql.append("  	(SELECT gsa.ADDRESS FROM GJT_STUDENT_ADDRESS gsa WHERE gsa.IS_DEFAULT='0' AND gsa.IS_DELETED='N' AND gsa.STUDENT_ID=gsi.STUDENT_ID) ADDRESS,");
		sql.append("  	gsi.SC_CO,gsi.DZXX,gsi.STUDENT_ID");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO gsi LEFT JOIN GJT_GRADE gg ON gsi.NJ = gg.GRADE_ID,");
		sql.append("  	GJT_CLASS_STUDENT gcs,");
		sql.append("  	GJT_CLASS_INFO gci");
		sql.append("  WHERE");
		sql.append("  	gsi.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED = 'N'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  	AND gci.CLASS_TYPE = 'teach'");


		if(EmptyUtils.isNotEmpty(searchParams.get("CLASS_ID"))){
			sql.append("  	AND gci.CLASS_ID = :CLASS_ID");
			params.put("CLASS_ID",ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND gsi.XX_ID = :XX_ID");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("BZR_ID"))){
			sql.append("  	AND gci.BZR_ID = :BZR_ID");
			params.put("BZR_ID",ObjectUtils.toString(searchParams.get("BZR_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("gradeId"))){
			sql.append("  	AND gg.GRADE_ID= :gradeId");
			params.put("gradeId",ObjectUtils.toString(searchParams.get("gradeId")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("BJMC"))){
			sql.append("  	AND gci.BJMC LIKE :BJMC");
			params.put("BJMC","%"+ObjectUtils.toString(searchParams.get("BJMC"))+"%");
		}
		return commonDao.queryForMapListNative(sql.toString(),params);
	}
}
