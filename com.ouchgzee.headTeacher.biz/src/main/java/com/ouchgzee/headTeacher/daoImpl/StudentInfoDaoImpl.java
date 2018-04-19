/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.daoImpl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.daoImpl.base.BaseDaoImpl;
import com.ouchgzee.headTeacher.dto.CountLoginDto;
import com.ouchgzee.headTeacher.dto.StudentDto;
import com.ouchgzee.headTeacher.dto.StudentStateDto;
import com.ouchgzee.headTeacher.pojo.status.SignupAuditStateEnum;

/**
 * 学员信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月4日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrStudentInfoDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
@Transactional(value="transactionManagerBzr", readOnly = true)
public class StudentInfoDaoImpl extends BaseDaoImpl {

	@Autowired
	private CommonDao commonDao;

	/**
	 * 分页条件查询学员信息，HQL语句
	 * 
	 * @param xm
	 * @param pageRequest
	 * @return
	 */
	public Page<StudentDto> findByPage(String xm, PageRequest pageRequest) {
		StringBuilder queryHql = new StringBuilder(
				"select new com.ouchgzee.headTeacher.dto.StudentDto(t.studentId,t.xm)");
		queryHql.append(" FROM BzrGjtStudentInfo t");
		queryHql.append(" WHERE t.isDeleted=:isDeleted");
		Map<String, Object> parameters = new HashMap();
		parameters.put("isDeleted", Constants.BOOLEAN_NO);

		if (StringUtils.isNotBlank(xm)) {
			queryHql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + xm + "%");
		}
		// DAO类一般忽略设置默认排序，可放在SERVICE层
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.createdDt"));
		}
		return super.findByPageHql(queryHql, parameters, pageRequest, StudentDto.class);
	}

	/**
	 * 分页条件查询学员信息，SQL语句
	 *
	 * @param xm
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findByPageSQLTest(String xm, PageRequest pageRequest) {
		StringBuilder querySql = new StringBuilder("select t.student_id,t.xm,t.sfzh");
		querySql.append(" FROM gjt_student_info t");
		querySql.append(" WHERE t.is_deleted=:isDeleted");
		Map<String, Object> parameters = new HashMap();
		parameters.put("isDeleted", Constants.BOOLEAN_NO);

		if (StringUtils.isNotBlank(xm)) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + xm + "%");
		}
		// DAO类一般忽略设置默认排序，可放在SERVICE层
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.created_dt"));
		}
		return super.findByPageToMap(querySql, parameters, pageRequest);
	}

	// --------------------------------------- 上面是用来做测试写的方法 华丽丽的分割线 牛不牛掰
	// ---------------------------------------//

	/**
	 * 分页条件查询学员状态信息，HQL语句
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<StudentStateDto> findStudentStateByPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder queryHql = createHqlToFindStudentState(classId, searchParams, parameters);
		return super.findByPageHql(queryHql, parameters, pageRequest, StudentStateDto.class);
	}

	/**
	 * 根据条件查询学员状态信息，HQL语句
	 * 
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<StudentStateDto> findAllStudentState(String classId, Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder queryHql = createHqlToFindStudentState(classId, searchParams, parameters);
		return super.findAllByHql(queryHql, parameters, sort, StudentStateDto.class);
	}

	/**
	 * 拼接HQL，查询学员状态信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param parameters
	 * @return
	 */
	private StringBuilder createHqlToFindStudentState(String classId, final Map<String, Object> searchParams,
			Map<String, Object> parameters) {
		StringBuilder queryHql = new StringBuilder(
				"select new com.ouchgzee.headTeacher.dto.StudentStateDto(t.studentId,t.xm,t.sfzh,f.createdDt,e.zymc,h.gradeName,j.value,(SELECT CASE WHEN COUNT(*)>=4 THEN '1' ELSE '0' END FROM GjtSignupData u WHERE u.signupId=f.signupId AND u.fileType IN ('sfz-z','sfz-f','zp','byz-z')),t.xjzt,i.receiveStatus,(CASE WHEN i.fhbytj='1' THEN 1 ELSE 0 END)+(CASE WHEN i.fhxwtj='1' THEN 1 ELSE 0 END))");
		queryHql.append(
				" from BzrGjtStudentInfo t inner join t.gjtClassStudentList b inner join b.gjtClassInfo c inner join t.gjtUserAccount d inner join t.gjtSpecialty e inner join t.gjtSignup f inner join f.gjtEnrollBatch g inner join g.gjtGrade h left join t.gjtGraduationStu i left join t.gjtStudentPropertyList j");
		queryHql.append(
				" where t.isDeleted=:isDeleted and b.isDeleted=:isDeleted and c.isDeleted=:isDeleted and d.isDeleted=:isDeleted and e.isDeleted=:isDeleted and f.isDeleted=:isDeleted and (i.id IS NULL or i.isDeleted=:isDeleted)");
		queryHql.append(" and (j.id IS NULL or j.key='PAYSTATE') and c.classId=:classId");
		parameters.put("isDeleted", Constants.BOOLEAN_NO);
		parameters.put("classId", classId);

		if (StringUtils.isNotBlank(searchParams.get("specialtyId"))) {
			queryHql.append(" AND e.specialtyId = :specialtyId");
			parameters.put("specialtyId", searchParams.get("specialtyId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("loginAccount"))) {
			queryHql.append(" AND d.loginAccount = :loginAccount");
			parameters.put("loginAccount", searchParams.get("loginAccount"));
		}
		if (StringUtils.isNotBlank(searchParams.get("xm"))) {
			queryHql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("xm") + "%");
		}
		if (StringUtils.isNotBlank(searchParams.get("learningState"))) {
			if (Constants.BOOLEAN_1.equals(searchParams.get("learningState").toString())) {
				queryHql.append(" AND (j.id IS NULL or j.value = '1')");
			} else {
				queryHql.append(" AND j.value = '0'");
			}
		}
		return queryHql;
	}

	/**
	 * 未确认入学学员数
	 * 
	 * @param classId
	 * @return
	 */
	public long countNotLearningStudentByClassId(String classId) {
		Map<String, Object> parameters = new HashMap();
		/*
		 * StringBuilder queryHql = new StringBuilder("select COUNT(*)");
		 * queryHql.append(
		 * " from BzrGjtStudentInfo t inner join t.gjtClassStudentList b inner join b.gjtClassInfo c"
		 * ); queryHql.append(" left join t.gjtStudentPropertyList j");
		 * queryHql.append(
		 * " WHERE t.isDeleted=:isDeleted and b.isDeleted=:isDeleted and c.isDeleted=:isDeleted"
		 * ); queryHql.append(
		 * " AND (j.id IS NULL or j.key='PAYSTATE') and c.classId=:classId");
		 * queryHql.append(" AND j.value = '0'");
		 */
		StringBuilder queryHql = new StringBuilder("select COUNT(*)")
				.append(" FROM BzrGjtStudentInfo t inner join t.gjtClassStudentList b inner join b.gjtClassInfo c")
				.append(" WHERE t.isDeleted=:isDeleted and b.isDeleted=:isDeleted and c.isDeleted=:isDeleted")
				.append(" AND c.classId=:classId AND t.isEnteringSchool='0'");
		parameters.put("isDeleted", Constants.BOOLEAN_NO);
		parameters.put("classId", classId);
		return super.countByHql(queryHql, parameters);
	}

	/**
	 * 未完善资料学员数
	 * @param classId
	 * @return
     */
	public long countNotPerfectStudentByClassId(String classId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder queryHql = new StringBuilder("select COUNT(*)")
				.append(" FROM BzrGjtStudentInfo t inner join t.gjtClassStudentList b inner join b.gjtClassInfo c")
				.append(" WHERE t.isDeleted=:isDeleted and b.isDeleted=:isDeleted and c.isDeleted=:isDeleted")
				.append(" AND c.classId=:classId AND t.perfectStatus<>1");
		parameters.put("isDeleted", Constants.BOOLEAN_NO);
		parameters.put("classId", classId);
		return super.countByHql(queryHql, parameters);
	}

	/**
	 * 分页条件查询学员的考勤信息，SQL语句
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findStudentClockingInByPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = createSqlToFindStudentClockingIn(classId, searchParams, parameters);
		return super.findByPageToMap(querySql, parameters, pageRequest);
	}

	/**
	 * 根据条件查询学员的考勤信息，SQL语句
	 * 
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> findAllStudentClockingIn(String classId, Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = createSqlToFindStudentClockingIn(classId, searchParams, parameters);
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 统计学员的考勤，SQL语句
	 * 
	 * @param username
	 * @return
	 */
	public Map countStudentClockingInSituation(String username) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"select min(d.created_dt) firstLogin,max(d.created_dt) lastLogin,count(d.login_id) countLogin,sum(d.login_time) totalMinute,floor(sysdate-max(d.created_dt)) noLoginDays");
		querySql.append(" from TBL_PRI_LOGIN_LOG d");
		querySql.append(" where d.is_deleted='N' and d.username=:username");
		parameters.put("username", username);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 统计学员的考勤，SQL语句
	 * 
	 * @param username
	 * @return
	 */
	public Map countStudentClockingInSituation(String username, String classId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder();

		querySql.append("  SELECT");
		querySql.append("  	d.USERNAME,");
		querySql.append("  	gsi.XM,");
		querySql.append("  	gsi.XH,");
		querySql.append("  	gsi.SJH,");
		querySql.append("  	gua.LOGIN_ACCOUNT,");
		querySql.append("  	d.LOGIN_TYPE,");
		querySql.append("  	gg.GRADE_NAME,");
		querySql.append("  	gy.NAME,");
		querySql.append("  	gci.BJMC,");
		querySql.append("  	(");
		querySql.append("  		SELECT");
		querySql.append("  			gs.zymc");
		querySql.append("  		FROM");
		querySql.append("  			GJT_SPECIALTY gs");
		querySql.append("  		WHERE");
		querySql.append("  			gs.IS_DELETED = 'N'");
		querySql.append("  			AND gs.SPECIALTY_ID = gsi.MAJOR");
		querySql.append("  	) ZYMC,");
		querySql.append("  	(");
		querySql.append("  		SELECT");
		querySql.append("  			TSD.NAME");
		querySql.append("  		FROM");
		querySql.append("  			TBL_SYS_DATA TSD");
		querySql.append("  		WHERE");
		querySql.append("  			TSD.IS_DELETED = 'N'");
		querySql.append("  			AND TSD.CODE = gsi.PYCC");
		querySql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		querySql.append("  	) PYCC_NAME,");
		querySql.append("  	d.created_dt  firstLogin,");
		querySql.append("  	d.UPDATED_DT  lastLogin,");
		querySql.append("  	TO_CHAR(d.login_time/60) totalMinute,");
		querySql.append("  	d.LOGIN_IP");
		querySql.append("  FROM");
		querySql.append("  	TBL_PRI_LOGIN_LOG d");
		querySql.append("  LEFT JOIN GJT_USER_ACCOUNT gua ON");
		querySql.append("  	d.USERNAME = gua.LOGIN_ACCOUNT");
		querySql.append("  	AND gua.IS_DELETED = 'N'");
		querySql.append("  LEFT JOIN GJT_STUDENT_INFO gsi ON");
		querySql.append("  	gua.ID = gsi.ACCOUNT_ID");
		querySql.append("  	AND gsi.IS_DELETED = 'N'");
		querySql.append("  INNER JOIN gjt_class_student gcs ON");
		querySql.append("  	gsi.student_id = gcs.STUDENT_ID");
		querySql.append("  	AND gcs.is_deleted = 'N'");
		querySql.append("  LEFT JOIN GJT_CLASS_INFO gci ON");
		querySql.append("  	gci.CLASS_ID = gcs.CLASS_ID");
		querySql.append("  	AND gci.IS_DELETED = 'N'");
		querySql.append("  LEFT JOIN GJT_GRADE gg ON");
		querySql.append("  	gg.GRADE_ID = gcs.GRADE_ID");
		querySql.append("  	AND gg.IS_DELETED = 'N'");
		querySql.append("  LEFT JOIN GJT_YEAR gy ON");
		querySql.append("  	gy.GRADE_ID = gg.GRADE_ID");
		querySql.append("  WHERE");
		querySql.append("  	d.is_deleted = 'N'");
		querySql.append("  	AND d.username = :username");
		querySql.append("  	AND gci.CLASS_ID = :classId");

		parameters.put("username", username);
		parameters.put("classId", classId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 统计班级学员登录情况 [总人数、当前在线人数、今日登录人数、从未学习人数、三天以内登录人数、三天以上未登录人数、七天以上未登录人数]
	 *
	 * @param classId
	 * @param queryParams
	 * @return studentNum-总人数
	 * 			onLineLoginNum-当前在线人数
	 * 			todayLoginNum-今日登录人数
	 * 			notLoginNum-从未学习人数
	 *          withinThreeDayNotLoginNum-三天以内登录人数
	 *          moreThanThreeDayNotLoginNum-三天以上未登录人数
	 *          moreThanSevenDayNotLoginNum-七天以上未登录人数
	 */
	public Map countClockInSituationByClass(String classId, Map<String, Object> queryParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select count(t.student_id) studentNum,");
		querySql.append(" count(case when to_char(sysdate, 'yyyy-mm-dd')=to_char(max(d.created_dt), 'yyyy-mm-dd') AND c.IS_ONLINE = 'Y' then 1 else null end) onLineLoginNum,");
		querySql.append(" count(case when to_char(sysdate, 'yyyy-mm-dd')=to_char(max(d.created_dt), 'yyyy-mm-dd') then 1 else null end) todayLoginNum,");
		querySql.append(" count(case when max(d.created_dt) is null then 1 else null end) notLoginNum,");
		querySql.append(" count(case when floor(sysdate-max(d.created_dt))<=3 and to_char(sysdate, 'yyyy-mm-dd')<>to_char(max(d.created_dt), 'yyyy-mm-dd') then 1 else null end) withinThreeDayNotLoginNum,");
		querySql.append(" count(case when floor(sysdate-max(d.created_dt))>3 and floor(sysdate-max(d.created_dt))<=7 then 1 else null end) moreThanThreeDayNotLoginNum,");
		querySql.append(" count(case when floor(sysdate-max(d.created_dt))>7 then 1 else null end) moreThanSevenDayNotLoginNum");
		querySql.append(" from gjt_student_info t inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N' inner join gjt_user_account c on t.account_id=c.id");
		querySql.append(" left join TBL_PRI_LOGIN_LOG d on c.login_account=d.username and d.is_deleted='N'");
		String startDate = (String) queryParams.get("startDate");
		String endDate = (String) queryParams.get("endDate");
		if(startDate != null && endDate != null) {
			querySql.append(" and d.created_dt between to_date(:startDate,'yyyy-mm-dd hh24:mi:ss') and to_date(:endDate,'yyyy-mm-dd hh24:mi:ss')");
			parameters.put("startDate", startDate);
			parameters.put("endDate", endDate);
		}
		querySql.append(" where t.is_deleted='N' and b.class_id=:classId");
		querySql.append(" GROUP BY t.student_id,c.IS_ONLINE");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);
		parameters.put("classId", classId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 统计班级学员学习情况 [学习总次数、学习总时长]
	 *
	 * @param classId
	 * @param queryParams
	 * @return studyNum-学习总次数 studyHourNum-学习总时长
	 */
	public Map countStudySituationByClass(String classId, Map<String, Object> queryParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select count(*) studyNum, floor(nvl(sum(d.LOGIN_TIME)/60,0)) studyHourNum");
		querySql.append(" from gjt_student_info t inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N' inner join gjt_user_account c on t.account_id=c.id");
		querySql.append(" inner join TBL_PRI_LOGIN_LOG d on c.login_account=d.username and d.is_deleted='N'");
		String startDate = (String) queryParams.get("startDate");
		String endDate = (String) queryParams.get("endDate");
		if(startDate != null && endDate != null) {
			querySql.append(" and d.created_dt between to_date(:startDate,'yyyy-mm-dd hh24:mi:ss') and to_date(:endDate,'yyyy-mm-dd hh24:mi:ss')");
			parameters.put("startDate", startDate);
			parameters.put("endDate", endDate);
		}
		querySql.append(" where t.is_deleted='N' and b.class_id=:classId");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);
		parameters.put("classId", classId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 按照课程统计班级学员登录情况 [总人数、当前在线人数、今日登录人数、从未学习人数、三天以内登录人数、三天以上未登录人数、七天以上未登录人数]
	 * [学习总次数、学习总时长]
	 * @param classId
	 * @param courseId
	 * @return studentNum-总人数
	 * 			onLineLoginNum-当前在线人数
	 * 			todayLoginNum-今日登录人数
	 * 			notLoginNum-从未学习人数
	 *          withinThreeDayNotLoginNum-三天以内登录人数
	 *          moreThanThreeDayNotLoginNum-三天以上未登录人数
	 *          moreThanSevenDayNotLoginNum-七天以上未登录人数
	 *          studyNum-学习总次数 studyHourNum-学习总时长
	 */
	public Map countClockInSituationByCourseClass(String classId, String courseId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("");
		querySql.append("  select count(distinct t.student_id) studentNum,");
		querySql.append("  count(case when to_char(sysdate, 'yyyy-mm-dd')=to_char(D.LAST_DATE, 'yyyy-mm-dd') AND D.IS_ONLINE = '0' then 1 else null end) onLineLoginNum,");
		querySql.append("  count(case when to_char(sysdate, 'yyyy-mm-dd')=to_char(D.LAST_DATE, 'yyyy-mm-dd') then 1 else null end) todayLoginNum,");
		querySql.append("  count(case when D.LAST_DATE is null then 1 else null end) notLoginNum,");
		querySql.append("  count(case when floor(sysdate-D.LAST_DATE)<=3 and to_char(sysdate, 'yyyy-mm-dd')<>to_char(D.LAST_DATE, 'yyyy-mm-dd') then 1 else null end) withinThreeDayNotLoginNum,");
		querySql.append("  count(case when floor(sysdate-D.LAST_DATE)>3 and floor(sysdate-D.LAST_DATE)<=7 then 1 else null end) moreThanThreeDayNotLoginNum,");
		querySql.append("  count(case when floor(sysdate-D.LAST_DATE)>7 then 1 else null end) moreThanSevenDayNotLoginNum,");
		querySql.append("  nvl(sum(D.LOGIN_COUNT),0) studyNum,");
		querySql.append("  floor(nvl(sum(D.LOGIN_TIME)/60,0)) studyHourNum");
		querySql.append("  from (");
		querySql.append("  	select x.student_id from gjt_student_info x");
		querySql.append("  	inner join gjt_class_student y on y.student_id=x.student_id and y.is_deleted='N'");
		querySql.append("  	where x.is_deleted='N' and y.class_id=:classId");
		querySql.append("  ) t ");
		querySql.append("  inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N' ");
		querySql.append("  inner join gjt_class_info c on c.class_id=b.class_id and c.is_deleted='N'");
		querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N' AND D.COURSE_ID=:courseId");
		querySql.append("  where c.class_type='course'");
		querySql.append("  	and c.course_id=:courseId");
		parameters.put("classId", classId);
		parameters.put("courseId", courseId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 按照课程统计班级学员课程学情 [总人数、总学习进度、总学习成绩、学习总次数、学习总时长]
	 * [学习总次数、学习总时长]
	 * @param classId
	 * @param courseId
	 * @return studentNum-总人数
	 *          totalSchedule-总学习进度 totalStudyScore-总学习成绩
	 *          studyNum-学习总次数 studyHourNum-学习总时长
	 */
	public Map countLearnSituationByCourseClass(String classId, String courseId, Map<String, Object> queryParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("");
		querySql.append("  select count(distinct t.student_id) studentNum,");
		querySql.append("  nvl(sum(D.SCHEDULE),0) totalSchedule,");
		querySql.append("  nvl(sum(E.EXAM_SCORE),0) totalStudyScore,");
		querySql.append("  nvl(sum(D.LOGIN_COUNT),0) studyNum,");
		querySql.append("  floor(nvl(sum(D.LOGIN_TIME)/60,0)) studyHourNum");
		querySql.append("  from (");
		querySql.append("  	select x.student_id from gjt_student_info x");
		querySql.append("  	inner join gjt_class_student y on y.student_id=x.student_id and y.is_deleted='N'");
		querySql.append("  	where x.is_deleted='N' and y.class_id=:classId");
		querySql.append("  ) t ");
		if(StringUtils.isNotBlank(courseId)) {
			querySql.append("  inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N' ");
			querySql.append("  inner join gjt_class_info c on c.class_id=b.class_id and c.is_deleted='N'");
			querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N' AND D.COURSE_ID=:courseId");
			querySql.append("  left join gjt_rec_result e on E.STUDENT_ID=T.STUDENT_ID AND E.IS_DELETED='N' AND E.COURSE_ID=:courseId ");
			querySql.append("  where c.class_type='course'");
			querySql.append("  	and c.course_id=:courseId");
			parameters.put("courseId", courseId);
		} else {
			String startDate = (String) queryParams.get("startDate");
			String endDate = (String) queryParams.get("endDate");

			querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N'");
			if(startDate != null && endDate != null) {
				querySql.append(" and d.LAST_DATE between to_date(:startDate,'yyyy-mm-dd hh24:mi:ss') and to_date(:endDate,'yyyy-mm-dd hh24:mi:ss')");
				parameters.put("startDate", startDate);
				parameters.put("endDate", endDate);
			}
			querySql.append("  left join gjt_rec_result e on E.STUDENT_ID=T.STUDENT_ID AND E.IS_DELETED='N' ");
			querySql.append("  	AND EXISTS (SELECT 1 FROM GJT_STUDY_RECORD SR WHERE SR.COURSE_ID=E.COURSE_ID AND SR.STUDENT_ID=E.STUDENT_ID AND SR.IS_DELETED='N'");
			if(startDate != null && endDate != null) {
				querySql.append("  		and SR.LAST_DATE between to_date(:startDate,'yyyy-mm-dd hh24:mi:ss') and to_date(:endDate,'yyyy-mm-dd hh24:mi:ss')");
			}
			querySql.append("  )");
		}
		parameters.put("classId", classId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 获取班级学员的学习排行Top10-学习进度
	 * @param teachClassId
	 * @param courseId
	 * @return
	 */
	public List<Map> queryStudyRankingTopTenScheduleByCourseClass(String teachClassId, String courseId, Map<String, Object> queryParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("");
		querySql.append("select * from (");
		querySql.append("  select t.student_id,t.xm,");
		querySql.append("  	floor(nvl(avg(D.SCHEDULE),0)) avg_Schedule");
		querySql.append("  from (");
		querySql.append("  	select x.student_id,x.xm from gjt_student_info x");
		querySql.append("  	inner join gjt_class_student y on y.student_id=x.student_id and y.is_deleted='N'");
		querySql.append("  	where x.is_deleted='N' and y.class_id=:classId");
		querySql.append("  ) t ");
		if(StringUtils.isNotBlank(courseId)) {
			querySql.append("  inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N' ");
			querySql.append("  inner join gjt_class_info c on c.class_id=b.class_id and c.is_deleted='N'");
			querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N' AND D.COURSE_ID=:courseId");
			querySql.append("  where c.class_type='course'");
			querySql.append("  	and c.course_id=:courseId");
			parameters.put("courseId", courseId);
		} else {
			querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N'");
			String startDate = (String) queryParams.get("startDate");
			String endDate = (String) queryParams.get("endDate");
			if(startDate != null && endDate != null) {
				querySql.append(" and d.LAST_DATE between to_date(:startDate,'yyyy-mm-dd hh24:mi:ss') and to_date(:endDate,'yyyy-mm-dd hh24:mi:ss')");
				parameters.put("startDate", startDate);
				parameters.put("endDate", endDate);
			}
		}
		querySql.append("  group by t.student_id,t.xm");
		querySql.append("  order by avg_Schedule desc");
		querySql.append(") temp where rownum<=10");
		parameters.put("classId", teachClassId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list;
	}

	/**
	 * 获取班级学员的学习排行Top10-学习次数
	 * @param teachClassId
	 * @param courseId
	 * @return
	 */
	public List<Map> queryStudyRankingTopTenStudyNumByCourseClass(String teachClassId, String courseId, Map<String, Object> queryParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("");
		querySql.append("select * from (");
		querySql.append("  select t.student_id,t.xm,");
		querySql.append("  	floor(nvl(avg(D.LOGIN_COUNT),0)) avg_studyNum");
		querySql.append("  from (");
		querySql.append("  	select x.student_id,x.xm from gjt_student_info x");
		querySql.append("  	inner join gjt_class_student y on y.student_id=x.student_id and y.is_deleted='N'");
		querySql.append("  	where x.is_deleted='N' and y.class_id=:classId");
		querySql.append("  ) t ");
		if(StringUtils.isNotBlank(courseId)) {
			querySql.append("  inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N' ");
			querySql.append("  inner join gjt_class_info c on c.class_id=b.class_id and c.is_deleted='N'");
			querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N' AND D.COURSE_ID=:courseId");
			querySql.append("  where c.class_type='course'");
			querySql.append("  	and c.course_id=:courseId");
			parameters.put("courseId", courseId);
		} else {
			querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N'");
			String startDate = (String) queryParams.get("startDate");
			String endDate = (String) queryParams.get("endDate");
			if(startDate != null && endDate != null) {
				querySql.append(" and d.LAST_DATE between to_date(:startDate,'yyyy-mm-dd hh24:mi:ss') and to_date(:endDate,'yyyy-mm-dd hh24:mi:ss')");
				parameters.put("startDate", startDate);
				parameters.put("endDate", endDate);
			}
		}
		querySql.append("  group by t.student_id,t.xm");
		querySql.append("  order by avg_studyNum desc");
		querySql.append(") temp where rownum<=10");
		parameters.put("classId", teachClassId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list;
	}

	/**
	 * 获取班级学员的学习排行Top10-学习时长
	 * @param teachClassId
	 * @param courseId
	 * @return
	 */
	public List<Map> queryStudyRankingTopTenStudyHourByCourseClass(String teachClassId, String courseId, Map<String, Object> queryParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("");
		querySql.append("select * from (");
		querySql.append("  select t.student_id,t.xm,");
		querySql.append("  	floor(nvl(avg(D.LOGIN_TIME)/60,0)) avg_studyHour");
		querySql.append("  from (");
		querySql.append("  	select x.student_id,x.xm from gjt_student_info x");
		querySql.append("  	inner join gjt_class_student y on y.student_id=x.student_id and y.is_deleted='N'");
		querySql.append("  	where x.is_deleted='N' and y.class_id=:classId");
		querySql.append("  ) t ");
		if(StringUtils.isNotBlank(courseId)) {
			querySql.append("  inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N' ");
			querySql.append("  inner join gjt_class_info c on c.class_id=b.class_id and c.is_deleted='N'");
			querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N' AND D.COURSE_ID=:courseId");
			querySql.append("  where c.class_type='course'");
			querySql.append("  	and c.course_id=:courseId");
			parameters.put("courseId", courseId);
		} else {
			querySql.append("  left join GJT_STUDY_RECORD d on D.STUDENT_ID=T.STUDENT_ID AND D.IS_DELETED='N'");
			String startDate = (String) queryParams.get("startDate");
			String endDate = (String) queryParams.get("endDate");
			if(startDate != null && endDate != null) {
				querySql.append(" and d.LAST_DATE between to_date(:startDate,'yyyy-mm-dd hh24:mi:ss') and to_date(:endDate,'yyyy-mm-dd hh24:mi:ss')");
				parameters.put("startDate", startDate);
				parameters.put("endDate", endDate);
			}
		}
		querySql.append("  group by t.student_id,t.xm");
		querySql.append("  order by avg_studyHour desc");
		querySql.append(") temp where rownum<=10");
		parameters.put("classId", teachClassId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list;
	}

	/**
	 * 统计班级学员某时间之后的每日登录情况
	 * 
	 * @param classId
	 * @param date
	 * @return dayName-日期 studentLoginNum-登录人数
	 */
	public List<CountLoginDto> countClockInSituationAfterDateByClass(String classId, Date date) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select to_char(d.created_dt, 'yyyy-mm-dd') dayName,");
		querySql.append(" count(distinct t.student_id) studentLoginNum");
		querySql.append(
				" from gjt_student_info t inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N' inner join gjt_user_account c on t.account_id=c.id left join TBL_PRI_LOGIN_LOG d on c.login_account=d.username and d.is_deleted='N'");
		querySql.append(" where t.is_deleted='N' and b.class_id=:classId");
		querySql.append(" and d.created_dt >= :date");
		querySql.append(" GROUP BY to_char(d.created_dt, 'yyyy-mm-dd')");
		querySql.append(" ORDER BY dayName");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);
		parameters.put("classId", classId);
		parameters.put("date", date);
		List<CountLoginDto> list = super.findAllBySql(querySql, parameters, null, CountLoginDto.class);
		return list;
	}

	/**
	 * 拼接SQL，查询学员的考勤信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param parameters
	 * @return
	 */
	private StringBuilder createSqlToFindStudentClockingIn(String classId, final Map<String, Object> searchParams,
			Map<String, Object> parameters) {

		// StringBuilder querySql = new StringBuilder("select
		// t.student_id,t.xm,min(d.created_dt) firstLogin,max(d.created_dt)
		// lastLogin,count(d.login_id) countLogin,sum(d.login_time)
		// totalMinute,floor(sysdate-max(d.created_dt)) noLoginDays");
		// querySql.append(" from gjt_student_info t inner join
		// gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N'
		// inner join gjt_user_account c on t.account_id=c.id left join
		// TBL_PRI_LOGIN_LOG d on c.login_account=d.username and
		// d.is_deleted='N'");
		// querySql.append(" where t.is_deleted='N' and b.class_id=:classId");

		StringBuilder querySql = new StringBuilder();

		querySql.append("  SELECT");
		querySql.append("  	t.student_id,");
		querySql.append("  	t.XH,");
		querySql.append("  	t.SJH,");
		querySql.append("  	t.XX_ID,");
		querySql.append("  	t.ACCOUNT_ID,");
		querySql.append("  	c.IS_ONLINE,");
		querySql.append("  	gg.GRADE_NAME,");
		querySql.append("  	gy.NAME,");
		querySql.append("  	gci.BJMC,");
		querySql.append("  	gci.CLASS_ID,");
		querySql.append("  	(");
		querySql.append("  		SELECT");
		querySql.append("  			TSD.NAME");
		querySql.append("  		FROM");
		querySql.append("  			TBL_SYS_DATA TSD");
		querySql.append("  		WHERE");
		querySql.append("  			TSD.IS_DELETED = 'N'");
		querySql.append("  			AND TSD.CODE = t.PYCC");
		querySql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		querySql.append("  	) PYCC_NAME,");
		querySql.append("  	(");
		querySql.append("  		SELECT");
		querySql.append("  			gs.zymc");
		querySql.append("  		FROM");
		querySql.append("  			GJT_SPECIALTY gs");
		querySql.append("  		WHERE");
		querySql.append("  			gs.IS_DELETED = 'N'");
		querySql.append("  			AND gs.SPECIALTY_ID = t.MAJOR");
		querySql.append("  	) ZYMC,");
		querySql.append("  	t.xm,");
		querySql.append("  	MIN( d.created_dt ) firstLogin,");
		querySql.append("  	MAX( d.created_dt ) lastLogin,");
		querySql.append("  	COUNT( d.login_id ) countLogin,");
		querySql.append("  	SUM( d.login_time ) totalMinute,");
		querySql.append("  	FLOOR( SYSDATE - MAX( d.created_dt )) noLoginDays");
		querySql.append("  FROM");
		querySql.append("  	gjt_student_info t");
		querySql.append("  INNER JOIN gjt_class_student b ON");
		querySql.append("  	t.student_id = b.student_id");
		querySql.append("  	AND b.is_deleted = 'N'");
		querySql.append("  	LEFT JOIN GJT_CLASS_INFO gci ON gci.CLASS_ID=b.CLASS_ID AND gci.IS_DELETED='N'");
		querySql.append("  LEFT JOIN GJT_GRADE gg ON");
		querySql.append("  	gg.GRADE_ID = b.GRADE_ID");
		querySql.append("  	AND gg.IS_DELETED = 'N'");
		querySql.append("  LEFT JOIN GJT_YEAR gy ON");
		querySql.append("  	gy.GRADE_ID = gg.YEAR_ID");
		querySql.append("  INNER JOIN gjt_user_account c ON");
		querySql.append("  	t.account_id = c.id");
		querySql.append("  LEFT JOIN TBL_PRI_LOGIN_LOG d ON");
		querySql.append("  	c.login_account = d.username");
		querySql.append("  	AND d.is_deleted = 'N'");
		querySql.append("  WHERE");
		querySql.append("  	t.is_deleted = 'N'");
		querySql.append("  	AND b.class_id = :classId");

		// parameters.put("isDeleted", Constants.BOOLEAN_NO);
		if (EmptyUtils.isNotEmpty(searchParams.get("classId"))) {
			classId = ObjectUtils.toString(searchParams.get("classId"));
		}
		parameters.put("classId", classId);

		if (StringUtils.isNotBlank(searchParams.get("loginAccount"))) {
			querySql.append(" AND c.login_account = :loginAccount");
			parameters.put("loginAccount", searchParams.get("loginAccount"));
		}
		if (StringUtils.isNotBlank(searchParams.get("xm"))) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("xm") + "%");
		}
		if (StringUtils.isNotBlank(searchParams.get("xh"))) {// 学号
			querySql.append(" AND t.xh LIKE :xh");
			parameters.put("xh", "%" + searchParams.get("xh") + "%");
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("gradeId"))) {
			querySql.append(" AND gg.GRADE_ID= :gradeId");
			parameters.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("specialtyId"))) {
			querySql.append(" AND t.MAJOR = :specialtyId");
			parameters.put("specialtyId", ObjectUtils.toString(searchParams.get("specialtyId")));
		}

		// querySql.append(" GROUP BY t.student_id,t.xm,t.created_dt");
		querySql.append("  GROUP BY");
		querySql.append("  	T.STUDENT_ID,");
		querySql.append("  	T.ACCOUNT_ID,");
		querySql.append("  	C.IS_ONLINE,");
		querySql.append("  	T.XM,");
		querySql.append("  	T.XH,");
		querySql.append("  	T.SJH,");
		querySql.append("  	t.XX_ID,");
		querySql.append("  	t.MAJOR,");
		querySql.append("  	t.PYCC,");
		querySql.append("  	gg.GRADE_NAME,");
		querySql.append("  	gy.NAME,");
		querySql.append("  	gci.BJMC,");
		querySql.append("  	gci.CLASS_ID,");
		querySql.append("  	T.CREATED_DT");

		Object noLoginDaysObj = searchParams.get("noLoginDays");
		if (StringUtils.isNotBlank(noLoginDaysObj) && NumberUtils.isNumber(noLoginDaysObj.toString())) {
			int noLoginDays = NumberUtils.toInt(noLoginDaysObj.toString());
			switch (noLoginDays) {
				case 1:
					querySql.append(" HAVING (sysdate-max(d.created_dt))>=:noLoginDays");
					parameters.put("noLoginDays", 7);
					break;
				case 2:
					querySql.append(" HAVING (sysdate-max(d.created_dt))>:noLoginDays");
					parameters.put("noLoginDays", 3);
					break;
				case 3:
					querySql.append(" HAVING (sysdate-max(d.created_dt))<=:noLoginDays");
					parameters.put("noLoginDays", 3);
					break;
				case 4:
					querySql.append(" HAVING max(d.created_dt) IS NULL");
					break;
				case 5:
					querySql.append(" HAVING to_char(sysdate,'yyyy-mm-dd')=to_char(max(d.created_dt),'yyyy-mm-dd')");
					break;
				case 6:
					querySql.append(
							" HAVING to_char(sysdate,'yyyy-mm-dd')=to_char(max(d.created_dt),'yyyy-mm-dd') AND c.IS_ONLINE = 'Y' ");
					break;
			}
		}
		return querySql;
	}

	/**
	 * 分页条件查询学员的缴费信息，SQL语句
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findPaymentSituationByPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = createSqlToFindPaymentSituation(classId, searchParams, parameters);
		return super.findByPageToMap(querySql, parameters, pageRequest);
	}

	/**
	 * 根据条件查询学员的缴费信息，SQL语句
	 * 
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> findAllPaymentSituation(String classId, Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = createSqlToFindPaymentSituation(classId, searchParams, parameters);
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 拼接SQL，查询学员的缴费信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param parameters
	 * @return
	 */
	private StringBuilder createSqlToFindPaymentSituation(String classId, final Map<String, Object> searchParams,
			Map<String, Object> parameters) {
		StringBuilder querySql = new StringBuilder("select t.student_id,t.xm,t.sfzh,t.sjh,d.login_account,g.value_");
		querySql.append(
				" from gjt_student_info t inner join gjt_class_student b on t.student_id=b.student_id inner join gjt_class_info c on b.class_id=c.class_id inner join gjt_user_account d on t.account_id=d.id left outer join gjt_student_property g on t.student_id=g.student_id and g.key_='PAYSTATE'");
		querySql.append(
				" where t.is_deleted=:isDeleted and b.is_deleted=:isDeleted and c.is_deleted=:isDeleted and d.is_deleted=:isDeleted and c.class_id=:classId and c.class_type='teach'");
		parameters.put("isDeleted", Constants.BOOLEAN_NO);
		parameters.put("classId", classId);

		if (StringUtils.isNotBlank(searchParams.get("loginAccount"))) {
			querySql.append(" AND d.login_account = :loginAccount");
			parameters.put("loginAccount", searchParams.get("loginAccount"));
		}
		if (StringUtils.isNotBlank(searchParams.get("xm"))) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("xm") + "%");
		}
		return querySql;
	}

	/**
	 * 更改学员的学习状态
	 * 
	 * @param studentId
	 * @param payState
	 * @return
	 */
	@Transactional(value="transactionManagerBzr")
	public int changePaymentState(String studentId, int payState) {
		StringBuilder updateSql = new StringBuilder(
				"MERGE INTO GJT_STUDENT_PROPERTY GSP USING (SELECT :studentId AS STUDENT_ID,'PAYSTATE' AS KEY_  FROM DUAL) T ON (T.STUDENT_ID = GSP.STUDENT_ID AND T.KEY_=GSP.KEY_)");
		updateSql.append(" WHEN MATCHED THEN UPDATE SET VALUE_ = :payState");
		updateSql.append(
				" WHEN NOT MATCHED THEN INSERT(ID_, STUDENT_ID, KEY_, VALUE_) VALUES ((SELECT SYS_GUID() FROM DUAL), :studentId, 'PAYSTATE', :payState)");

		Query q = em.createNativeQuery(updateSql.toString());
		q.setParameter("studentId", studentId);
		q.setParameter("payState", payState);
		return q.executeUpdate();
	}

	/**
	 * 获取学员已修学分情况
	 * 
	 * @param studentXh
	 * @param specialtyId
	 * @return XF-学分 ZDXF-最低学分 YXXF-已修学分
	 */
	public Map getMinAndSum(String studentXh, String specialtyId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("SELECT sum(d.xf) XF, sum(d.zdf) ZDXF, sum(d.YXXF) YXXF");
		querySql.append(
				" FROM (SELECT t.kclbm KCLBM, sum(t.xf) XF, count(*) KCSL, t.kclbm_code, sum(EXAM_SCORE2) YXXF, (SELECT sum(a.score) FROM gjt_specialty_module_limit a, tbl_sys_data b WHERE a.MODULE_ID = b.id AND b.name = t.kclbm AND a.is_deleted='N' AND a.specialty_id = :specialtyId) AS ZDF FROM (SELECT (SELECT tsd.name FROM tbl_sys_data tsd WHERE tsd.type_code = 'CourseType' AND tsd.code = gtp.kclbm) AS KCLBM, (SELECT GTP.XF FROM GJT_REC_RESULT grr WHERE grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID AND vs.STUDENT_ID = grr.student_id AND grr.is_deleted = 'N' AND grr.exam_score2 >= 60) AS EXAM_SCORE2, GTP.XF, gtp.kclbm AS kclbm_code FROM VIEW_TEACH_PLAN gtp, GJT_COURSE gc, GJT_TERM_INFO gti, GJT_SPECIALTY gs, view_student_info vs WHERE gtp.course_id = gc.course_id AND gtp.TERM_ID = gti.term_id AND gs.specialty_id = gtp.kkzy AND gc.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND vs.PYCC = gtp.PYCC AND vs.MAJOR = gtp.KKZY AND vs.xh = :studentXh AND vs.GRADE_ID = gti.grade_id) t GROUP BY t.kclbm, t.kclbm_code ORDER BY kclbm_code) d");
		parameters.put("studentXh", studentXh);
		parameters.put("specialtyId", specialtyId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 获取学员的报读信息及相关信息
	 * 
	 * @param studentId
	 * @return
	 */
	public Map<String, Object> getStudentSignupInfoById(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("studentId", studentId);

		StringBuilder querySql = new StringBuilder();
		querySql.append(
				"SELECT T.*,B.GRADE_ID,B.GRADE_NAME,C.ZYMC,C.SPECIALTY_CATEGORY,D.XXMC,E.ORG_NAME,F.JOB_POST,F.AUDIT_STATE,F.ORDER_SN,F.ZGXL_RADIO_TYPE");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID ");
		querySql.append(" LEFT JOIN  GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N' ");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'  ");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N' ");
		querySql.append(" INNER JOIN GJT_SIGNUP F ON F.STUDENT_ID=T.STUDENT_ID AND F.IS_DELETED = 'N' ");
		querySql.append(" WHERE T.IS_DELETED='N' AND T.STUDENT_ID=:studentId");
		List<Map> list = super.findAllByToMap(querySql, params, null);
		return list != null && list.size() > 0 ? list.get(0) : Collections.emptyMap();
	}

	/**
	 * 分页条件查询学员的学籍资料，SQL语句
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findStudentSignupInfoByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder();
		querySql.append(" SELECT T.*,Y.BJMC,B.GRADE_ID,B.GRADE_NAME,Y.NAME YEAR_NAME,C.ZYMC,C.SPECIALTY_CATEGORY,D.XXMC,E.ORG_NAME,B.AUDIT_STATE,");
		querySql.append(" 	temp2.FLOW_AUDIT_OPERATOR_ROLE,temp2.FLOW_AUDIT_STATE,");
		querySql.append(" 	(SELECT DISTINCT(GO.CODE) FROM GJT_STUDENT_INFO GSI  LEFT JOIN  GJT_ORG GO  ON  GSI.XX_ID = GO.ID  AND  GO.IS_DELETED = 'N'  WHERE  GSI.IS_DELETED = 'N' AND GSI.STUDENT_ID = T.STUDENT_ID ) ORGCODE ");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_GRADE X ON X.GRADE_ID=B.GRADE_ID");
		querySql.append(" LEFT JOIN GJT_YEAR Y ON Y.GRADE_ID=X.YEAR_ID");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" INNER JOIN GJT_CLASS_STUDENT X ON X.STUDENT_ID=T.STUDENT_ID AND X.IS_DELETED='N'");
		querySql.append(
				" INNER JOIN GJT_CLASS_INFO Y ON Y.CLASS_ID=X.CLASS_ID AND Y.IS_DELETED='N' AND Y.CLASS_TYPE='teach'");
		// <!-- 和审核记录关联，获取最新审核状态 -->
		querySql.append(" LEFT JOIN (");
		querySql.append(
				"   SELECT student_id S_ID,audit_state FLOW_AUDIT_STATE,audit_operator_role FLOW_AUDIT_OPERATOR_ROLE FROM (");
		querySql.append(
				"       select f.student_id,f.audit_state,f.audit_operator_role,row_number() over (partition by f.student_id order by f.created_dt desc,f.audit_operator_role desc) id FROM gjt_flow_record f WHERE f.is_deleted='N'");
		querySql.append("   ) temp WHERE temp.id=1");
		querySql.append(" ) temp2 ON temp2.s_id=t.student_id");
		querySql.append(" WHERE T.IS_DELETED='N'");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);

		conditionJoin(searchParams, querySql, parameters);
		return super.findByPageToMap(querySql, parameters, pageRequest);
	}

	/**
	 * 条件查询所有学员的学籍资料，SQL语句
	 *
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> findStudentSignupInfoBy(Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder();
		querySql.append(" SELECT T.*,B.GRADE_ID,B.GRADE_NAME,C.RULE_CODE,C.ZYMC,C.SPECIALTY_CATEGORY,D.XXMC,E.ORG_NAME,B.AUDIT_STATE,");
		querySql.append(" 	Y.BJMC,J.XM HEADTEACHER,F.RECEIVER,F.MOBILE,F.PROVINCE_CODE,F.CITY_CODE,F.AREA_CODE,F.ADDRESS");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_STUDENT_ADDRESS F ON F.STUDENT_ID=T.STUDENT_ID AND F.IS_DEFAULT=1 AND F.IS_DELETED='N'");
		querySql.append(" INNER JOIN GJT_CLASS_STUDENT X ON X.STUDENT_ID=T.STUDENT_ID AND X.IS_DELETED='N'");
		querySql.append(
				" INNER JOIN GJT_CLASS_INFO Y ON Y.CLASS_ID=X.CLASS_ID AND Y.IS_DELETED='N' AND Y.CLASS_TYPE='teach'");
		// <!-- 和审核记录关联，获取最新审核状态 -->
		querySql.append(" LEFT JOIN (");
		querySql.append(
				"   SELECT student_id S_ID,audit_state FLOW_AUDIT_STATE,audit_operator_role FLOW_AUDIT_OPERATOR_ROLE FROM (");
		querySql.append(
				"       select f.student_id,f.audit_state,f.audit_operator_role,row_number() over (partition by f.student_id order by f.created_dt desc,f.audit_operator_role desc) id FROM gjt_flow_record f WHERE f.is_deleted='N'");
		querySql.append("   ) temp WHERE temp.id=1");
		querySql.append(" ) temp2 ON temp2.s_id=t.student_id");
		querySql.append(" LEFT JOIN GJT_EMPLOYEE_INFO J ON J.EMPLOYEE_ID = Y.BZR_ID");
		querySql.append(" WHERE T.IS_DELETED='N'");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);

		conditionJoin(searchParams, querySql, parameters);
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 统计审核状态
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map> countGroupbyAuditState(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT (case when t.xjzt = '5' then '5' when B.AUDIT_STATE = '1' then '1' when B.AUDIT_STATE = '0' then '0' when temp2.FLOW_AUDIT_OPERATOR_ROLE is null then '4' when temp2.FLOW_AUDIT_OPERATOR_ROLE = 2 then '3' when temp2.FLOW_AUDIT_OPERATOR_ROLE is not null then '2' else ' ' end) audit_state,count(*) student_num");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" INNER JOIN GJT_CLASS_STUDENT X ON X.STUDENT_ID=T.STUDENT_ID AND X.IS_DELETED='N'");
		querySql.append(
				" INNER JOIN GJT_CLASS_INFO Y ON Y.CLASS_ID=X.CLASS_ID AND Y.IS_DELETED='N' AND Y.CLASS_TYPE='teach'");
		// <!-- 和审核记录关联，获取最新审核状态 -->
		querySql.append(" LEFT JOIN (");
		querySql.append(
				"   SELECT student_id S_ID,audit_state FLOW_AUDIT_STATE,audit_operator_role FLOW_AUDIT_OPERATOR_ROLE FROM (");
		querySql.append(
				"       select f.student_id,f.audit_state,f.audit_operator_role,row_number() over (partition by f.student_id order by f.created_dt desc,f.audit_operator_role desc) id FROM gjt_flow_record f WHERE f.is_deleted='N'");
		querySql.append("   ) temp WHERE temp.id=1");
		querySql.append(" ) temp2 ON temp2.s_id=t.student_id");
		querySql.append(" WHERE T.IS_DELETED='N'");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);

		conditionJoin(searchParams, querySql, parameters);
		querySql.append(
				" group by (case when t.xjzt = '5' then '5' when B.AUDIT_STATE = '1' then '1' when B.AUDIT_STATE = '0' then '0' when temp2.FLOW_AUDIT_OPERATOR_ROLE is null then '4' when temp2.FLOW_AUDIT_OPERATOR_ROLE = 2 then '3' when temp2.FLOW_AUDIT_OPERATOR_ROLE is not null then '2' else ' ' end)");
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 统计完善情况
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map> countGroupbyPerfectStatus(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT decode(T.perfect_status,1,1,0) perfect_status,count(*) student_num");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" INNER JOIN GJT_CLASS_STUDENT X ON X.STUDENT_ID=T.STUDENT_ID AND X.IS_DELETED='N'");
		querySql.append(
				" INNER JOIN GJT_CLASS_INFO Y ON Y.CLASS_ID=X.CLASS_ID AND Y.IS_DELETED='N' AND Y.CLASS_TYPE='teach'");
		querySql.append(" WHERE T.IS_DELETED='N'");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);

		conditionJoin(searchParams, querySql, parameters);
		querySql.append(" group by decode(T.perfect_status,1,1,0)");
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 统计入学确认情况
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map> countGroupbyIsEnteringSchool(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT decode(T.IS_ENTERING_SCHOOL,'1','1','0') IS_ENTERING_SCHOOL,count(*) student_num");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" INNER JOIN GJT_CLASS_STUDENT X ON X.STUDENT_ID=T.STUDENT_ID AND X.IS_DELETED='N'");
		querySql.append(
				" INNER JOIN GJT_CLASS_INFO Y ON Y.CLASS_ID=X.CLASS_ID AND Y.IS_DELETED='N' AND Y.CLASS_TYPE='teach'");
		querySql.append(" WHERE T.IS_DELETED='N'");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);

		conditionJoin(searchParams, querySql, parameters);
		querySql.append(" group by decode(T.IS_ENTERING_SCHOOL,'1','1','0')");
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 统计学籍状态
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map> countGroupbyXjzt(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("SELECT decode(T.xjzt,null,' ',T.xjzt) xjzt,count(*) student_num");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" INNER JOIN GJT_CLASS_STUDENT X ON X.STUDENT_ID=T.STUDENT_ID AND X.IS_DELETED='N'");
		querySql.append(
				" INNER JOIN GJT_CLASS_INFO Y ON Y.CLASS_ID=X.CLASS_ID AND Y.IS_DELETED='N' AND Y.CLASS_TYPE='teach'");
		querySql.append(" WHERE T.IS_DELETED='N'");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);

		conditionJoin(searchParams, querySql, parameters);
		querySql.append(" group by decode(T.xjzt,null,' ',T.xjzt)");
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 查询条件拼接
	 *
	 * @param searchParams
	 * @param querySql
	 * @param parameters
	 */
	private void conditionJoin(Map<String, Object> searchParams, StringBuilder querySql,
			Map<String, Object> parameters) {
		// 学习中心
		String studyId = (String) searchParams.get("EQ_studyId");
		if(StringUtils.isNotBlank(studyId)) {
			querySql.append(" AND T.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			parameters.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = (String) searchParams.get("EQ_gjtSchoolInfo.id");
			if (StringUtils.isNotBlank(xxId)) {
				querySql.append(" AND T.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				parameters.put("xxId", xxId);
			}
		}
		// 学号
		if (StringUtils.isNotBlank(searchParams.get("EQ_xh"))) {
			querySql.append(" AND T.XH = :xh");
			parameters.put("xh", searchParams.get("EQ_xh"));
		}
		// 姓名
		if (StringUtils.isNotBlank(searchParams.get("LIKE_xm"))) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("LIKE_xm") + "%");
		}
		// 身份证号
		if (StringUtils.isNotBlank(searchParams.get("EQ_sfzh"))) {
			querySql.append(" AND T.sfzh = :sfzh");
			parameters.put("sfzh", searchParams.get("EQ_sfzh"));
		}
		// 年级
		if (StringUtils.isNotBlank(searchParams.get("EQ_viewStudentInfo.gradeId"))) {
			querySql.append(" AND B.GRADE_ID = :gradeId");
			parameters.put("gradeId", searchParams.get("EQ_viewStudentInfo.gradeId"));
		}
		// 培养层次
		if (StringUtils.isNotBlank(searchParams.get("EQ_pycc"))) {
			querySql.append(" AND T.pycc = :pycc");
			parameters.put("pycc", searchParams.get("EQ_pycc"));
		}
		// 专业
		if (StringUtils.isNotBlank(searchParams.get("EQ_specialtyId"))) {
			querySql.append(" AND T.major = :specialtyId");
			parameters.put("specialtyId", searchParams.get("EQ_specialtyId"));
		}
		// 学员类型
		if (StringUtils.isNotBlank(searchParams.get("EQ_userType"))) {
			querySql.append(" AND T.USER_TYPE = :userType");
			parameters.put("userType", searchParams.get("EQ_userType"));
		}
		// 单位名称
		if (StringUtils.isNotBlank(searchParams.get("LIKE_scCo"))) {
			querySql.append(" AND T.SC_CO LIKE :scCo");
			parameters.put("scCo", "%" + searchParams.get("LIKE_scCo") + "%");
		}
		// 学籍状态
		if (StringUtils.isNotBlank(searchParams.get("EQ_xjzt"))) {
			querySql.append(" AND T.XJZT = :xjzt");
			parameters.put("xjzt", searchParams.get("EQ_xjzt"));
		}

		// 完善状态
		String perfectStatus = (String) searchParams.get("EQ_perfectStatus");
		if (StringUtils.isNotBlank(perfectStatus)) {
			if ("1".equals(perfectStatus)) {
				querySql.append(" AND T.perfect_status = :perfectStatus");
				parameters.put("perfectStatus", perfectStatus);
			} else {
				querySql.append(" AND T.perfect_status <> :perfectStatus");
				parameters.put("perfectStatus", "1");
			}
		}

		// 资料审核状态
		String auditState = (String) searchParams.get("EQ_signupAuditState");
		if (StringUtils.isNotBlank(auditState)) {
			if ("5".equals(auditState)) { // 已退费，无需审核
				querySql.append(" AND t.xjzt='5'");
			} else if ((SignupAuditStateEnum.待审核.getValue() + "").equals(auditState)) {
				// querySql.append(" AND B.AUDIT_STATE IN
				// (:auditState1,:auditState2,:auditState3)");
				// parameters.put("auditState1",
				// SignupAuditStateEnum.重提交.getValue()+"");
				// parameters.put("auditState2",
				// SignupAuditStateEnum.待审核.getValue()+"");
				// parameters.put("auditState3",
				// SignupAuditStateEnum.未提交.getValue()+"");
				querySql.append(" AND t.xjzt <>'5' and B.AUDIT_STATE<>'1' and B.AUDIT_STATE<>'0' and temp2.FLOW_AUDIT_OPERATOR_ROLE=2");
			} else if ((SignupAuditStateEnum.未提交.getValue() + "").equals(auditState)) { // 未审核
				querySql.append(" AND t.xjzt <>'5' and B.AUDIT_STATE<>'1' and B.AUDIT_STATE<>'0' and temp2.FLOW_AUDIT_OPERATOR_ROLE is null");
			} else if ((SignupAuditStateEnum.重提交.getValue() + "").equals(auditState)) { // 审核中
				querySql.append(" AND t.xjzt <>'5' and B.AUDIT_STATE<>'1' and B.AUDIT_STATE<>'0' and temp2.FLOW_AUDIT_OPERATOR_ROLE is not null and temp2.FLOW_AUDIT_OPERATOR_ROLE<>2");
			} else {
				querySql.append(" AND B.AUDIT_STATE = :auditState");
				parameters.put("auditState", auditState);
			}
		}

		// 入学确认状态
		String isEnteringSchool = (String) searchParams.get("EQ_isEnteringSchool");
		if (StringUtils.isNotBlank(isEnteringSchool)) {
			if ("1".equals(isEnteringSchool)) {
				querySql.append(" AND T.IS_ENTERING_SCHOOL = :isEnteringSchool");
				parameters.put("isEnteringSchool", isEnteringSchool);
			} else {
				querySql.append(" AND (T.IS_ENTERING_SCHOOL IS NULL OR T.IS_ENTERING_SCHOOL <> :isEnteringSchool)");
				parameters.put("isEnteringSchool", "1");
			}
		}

		String classId = (String) searchParams.get("EQ_classId");
		if (StringUtils.isNotBlank(classId)) {
			querySql.append(" AND Y.CLASS_ID = :classId");
			parameters.put("classId", classId);
		}
	}

	/**
	 * 考勤分析--》学员课程考勤统计
	 *  过时，请使用 GjtRecResultDaoImpl recResultDao.getStudentLoginList(searchParams)
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@Deprecated
	public Page getStudentLoginList(Map searchParams, PageRequest pageRequest) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT DISTINCT");
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
		sql.append("			(SELECT vss.IS_ONLINE  FROM VIEW_STUDENT_STUDY_SITUATION vss  WHERE vss.STUDENT_ID=gsi.STUDENT_ID AND VSS.IS_ONLINE='Y' AND ROWNUM=1) IS_ONLINE,");
		sql.append("  			(SELECT MAX( vss.LAST_LOGIN_DATE ) FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID) LAST_LOGIN_TIME,");
		sql.append("  			(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  			(SELECT vss.BYOD_TYPE FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID AND vss.LAST_LOGIN_DATE =(SELECT MAX( vs.LAST_LOGIN_DATE ) FROM VIEW_STUDENT_STUDY_SITUATION vs WHERE vs.STUDENT_ID = GSI.STUDENT_ID AND ROWNUM < 2) AND ROWNUM < 2) DEVICE,");
		sql.append("  			(SELECT SUM( NVL( vss.LOGIN_TIMES, 0 )) FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID) LOGIN_COUNT,");
		sql.append("  			(SELECT ROUND( SUM( NVL( vss.ONLINE_TIME, 0 ))/ 60, 1 ) FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE  vss.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIME,");
		sql.append("  			(SELECT SUM( NVL( vss.PC_ONLINE_COUNT, 0 )) FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID) PC_ONLINE_COUNT,");
		sql.append("  			(SELECT SUM( NVL( vss.APP_ONLINE_COUNT, 0 )) FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID) APP_ONLINE_COUNT,");
		sql.append("  			(SELECT SUM( NVL( vss.APP_ONLINE_COUNT, 0 ))+ SUM( NVL( vss.PC_ONLINE_COUNT, 0 )) FROM VIEW_STUDENT_STUDY_SITUATION vss WHERE vss.STUDENT_ID = gsi.STUDENT_ID) ALL_ONLINE_COUNT");
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


		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");
		if (org.apache.commons.lang3.StringUtils.isNotBlank(xxId)) {
			sql.append(" AND (GSI.XX_ID=:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
			param.put("XX_ID", xxId);
		}

		if (EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND gsi.STUDENT_ID =:STUDENT_ID");
			param.put("STUDENT_ID", org.apache.commons.lang.ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ org.apache.commons.lang.ObjectUtils.toString(searchParams.get("XM"))+"%");
		}

		if (EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", org.apache.commons.lang.ObjectUtils.toString(searchParams.get("XH")));
		}

		if (EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GG.GRADE_ID = :GRADE_ID");
			param.put("GRADE_ID", org.apache.commons.lang.ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND gs.SPECIALTY_ID = :SPECIALTY_ID");
			param.put("SPECIALTY_ID", org.apache.commons.lang.ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		if(EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("PYCC")))){
			sql.append("  AND gsi.PYCC=:PYCC");
			param.put("PYCC", org.apache.commons.lang.ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if(EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("CLASS_ID")))){
			sql.append("  AND gci.CLASS_ID=:CLASS_ID");
			param.put("CLASS_ID", org.apache.commons.lang.ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		if(EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("XXZX_ID")))){
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			param.put("XXZX_ID", org.apache.commons.lang.ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}


		sql.append("  ORDER BY");
		sql.append("  	gsi.XM) TAB WHERE 1=1");

		if (EmptyUtils.isNotEmpty(org.apache.commons.lang.ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND TAB.BJMC LIKE :BJMC");
			param.put("BJMC", "%"+ org.apache.commons.lang.ObjectUtils.toString(searchParams.get("BJMC"))+"%");
		}


		return commonDao.queryForPageNative(sql.toString(), param, pageRequest);
	}

	/**
	 * 学员 课程考勤明细
	 * @param searchParams
	 * @return
	 */
	public List getStudentLoginDetail(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append("  	gtc.TERMCOURSE_ID,grr.REC_ID,gtp.teach_plan_id,gc.COURSE_ID,grr.STUDENT_ID,grr.EXAM_SCORE,gsr.IS_ONLINE,gtp.KKXQ,gc.KCMC,gsr.PROGRESS SCHEDULE,");
		sql.append("  	(SELECT gg.GRADE_NAME FROM GJT_GRADE gg WHERE gg.GRADE_ID = gtp.ACTUAL_GRADE_ID AND gg.IS_DELETED = 'N') GRADE_NAME,");
		sql.append("  	NVL( gsr.LOGIN_TIMES, 0 ) LOGIN_COUNT,ROUND( NVL( gsr.ONLINE_TIME / 60, 0 ), 1 ) LOGIN_TIME,TO_CHAR( gsr.LAST_LOGIN_DATE, 'yyyy-MM-dd HH24:mi' ) LAST_DATE,");
		sql.append("  	NVL( gsr.PC_ONLINE_COUNT, 0 ) PC_ONLINE_COUNT,NVL( gsr.APP_ONLINE_COUNT, 0 ) APP_ONLINE_COUNT,FLOOR(SYSDATE-gsr.LAST_LOGIN_DATE) LEFT_DAY,");
		sql.append("  	(CASE WHEN(gsr.APP_ONLINE_COUNT + gsr.PC_ONLINE_COUNT )> 0 THEN ROUND( PC_ONLINE_COUNT /( gsr.APP_ONLINE_COUNT + gsr.PC_ONLINE_COUNT )* 100 ) ELSE 0 END) PC_ONLINE_PERCENT,");
		sql.append("  	(CASE WHEN(gsr.APP_ONLINE_COUNT + gsr.PC_ONLINE_COUNT )> 0 THEN ROUND( APP_ONLINE_COUNT /( gsr.APP_ONLINE_COUNT + gsr.PC_ONLINE_COUNT )* 100 ) ELSE 0 END) APP_ONLINE_PERCENT,");
		sql.append("  	(SELECT tsd.NAME FROM TBL_SYS_DATA tsd WHERE tsd.CODE = gtp.KCLBM AND tsd.TYPE_CODE = 'CourseType') KCLB_NAME");
		sql.append("  	FROM");
		sql.append("  		GJT_REC_RESULT grr");
		sql.append("  	LEFT JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  		grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID");
		sql.append("  	LEFT JOIN GJT_TERM_COURSEINFO gtc ON grr.TERMCOURSE_ID=gtc.TERMCOURSE_ID");
		sql.append("  	LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON");
		sql.append("  		grr.REC_ID = gsr.CHOOSE_ID");
		sql.append("  	LEFT JOIN GJT_COURSE gc ON");
		sql.append("  		gtp.COURSE_ID = gc.COURSE_ID");
		sql.append("  	WHERE");
		sql.append("  		grr.IS_DELETED = 'N'");
		sql.append("  		AND gc.IS_DELETED = 'N'");
		sql.append("  		AND grr.STUDENT_ID =:STUDENT_ID");
		sql.append("  ORDER BY");
		sql.append("  	gtp.KKXQ");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID"),""));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	public List<Map<String, Object>> findStudentClockingIns(Map searchParams) {
		StringBuilder querySql = new StringBuilder();
		Map parameters = new HashMap();

		querySql.append("  SELECT");
		querySql.append("  	t.student_id,");
		querySql.append("  	t.XH,");
		querySql.append("  	t.SJH,");
		querySql.append("  	t.XX_ID,");
		querySql.append("  	t.ACCOUNT_ID,");
		querySql.append("  	c.IS_ONLINE,");
		querySql.append("  	gg.GRADE_NAME,");
		querySql.append("  	gy.NAME,");
		querySql.append("  	gci.BJMC,");
		querySql.append("  	gci.CLASS_ID,");
		querySql.append("  	(");
		querySql.append("  		SELECT");
		querySql.append("  			TSD.NAME");
		querySql.append("  		FROM");
		querySql.append("  			TBL_SYS_DATA TSD");
		querySql.append("  		WHERE");
		querySql.append("  			TSD.IS_DELETED = 'N'");
		querySql.append("  			AND TSD.CODE = t.PYCC");
		querySql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		querySql.append("  	) PYCC_NAME,");
		querySql.append("  	(");
		querySql.append("  		SELECT");
		querySql.append("  			gs.zymc");
		querySql.append("  		FROM");
		querySql.append("  			GJT_SPECIALTY gs");
		querySql.append("  		WHERE");
		querySql.append("  			gs.IS_DELETED = 'N'");
		querySql.append("  			AND gs.SPECIALTY_ID = t.MAJOR");
		querySql.append("  	) ZYMC,");
		querySql.append("  	t.xm,");
		querySql.append("  	MIN( d.created_dt ) firstLogin,");
		querySql.append("  	MAX( d.created_dt ) lastLogin,");
		querySql.append("  	COUNT( d.login_id ) countLogin,");
		querySql.append("  	SUM( d.login_time ) totalMinute,");
		querySql.append("  	FLOOR( SYSDATE - MAX( d.created_dt )) noLoginDays");
		querySql.append("  FROM");
		querySql.append("  	gjt_student_info t");
		querySql.append("  INNER JOIN gjt_class_student b ON");
		querySql.append("  	t.student_id = b.student_id");
		querySql.append("  	AND b.is_deleted = 'N'");
		querySql.append("  	LEFT JOIN GJT_CLASS_INFO gci ON gci.CLASS_ID=b.CLASS_ID AND gci.IS_DELETED='N'");
		querySql.append("  LEFT JOIN GJT_GRADE gg ON");
		querySql.append("  	gg.GRADE_ID = b.GRADE_ID");
		querySql.append("  	AND gg.IS_DELETED = 'N'");
		querySql.append("  LEFT JOIN GJT_YEAR gy ON");
		querySql.append("  	gy.GRADE_ID = gg.YEAR_ID");
		querySql.append("  INNER JOIN gjt_user_account c ON");
		querySql.append("  	t.account_id = c.id");
		querySql.append("  LEFT JOIN TBL_PRI_LOGIN_LOG d ON");
		querySql.append("  	c.login_account = d.username");
		querySql.append("  	AND d.is_deleted = 'N'");
		querySql.append("  WHERE");
		querySql.append("  	t.is_deleted = 'N'");
		querySql.append("  	AND b.class_id = :classId");

		parameters.put("classId", ObjectUtils.toString(searchParams.get("classId")));

		if (StringUtils.isNotBlank(searchParams.get("loginAccount"))) {
			querySql.append(" AND c.login_account = :loginAccount");
			parameters.put("loginAccount", searchParams.get("loginAccount"));
		}
		if (StringUtils.isNotBlank(searchParams.get("xm"))) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("xm") + "%");
		}
		if (StringUtils.isNotBlank(searchParams.get("xh"))) {// 学号
			querySql.append(" AND t.xh LIKE :xh");
			parameters.put("xh", "%" + searchParams.get("xh") + "%");
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("gradeId"))) {
			querySql.append(" AND gg.GRADE_ID= :gradeId");
			parameters.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("specialtyId"))) {
			querySql.append(" AND t.MAJOR = :specialtyId");
			parameters.put("specialtyId", ObjectUtils.toString(searchParams.get("specialtyId")));
		}

		// querySql.append(" GROUP BY t.student_id,t.xm,t.created_dt");
		querySql.append("  GROUP BY");
		querySql.append("  	T.STUDENT_ID,");
		querySql.append("  	T.ACCOUNT_ID,");
		querySql.append("  	C.IS_ONLINE,");
		querySql.append("  	T.XM,");
		querySql.append("  	T.XH,");
		querySql.append("  	T.SJH,");
		querySql.append("  	t.XX_ID,");
		querySql.append("  	t.MAJOR,");
		querySql.append("  	t.PYCC,");
		querySql.append("  	gg.GRADE_NAME,");
		querySql.append("  	gy.NAME,");
		querySql.append("  	gci.BJMC,");
		querySql.append("  	gci.CLASS_ID,");
		querySql.append("  	T.CREATED_DT");

		Object noLoginDaysObj = searchParams.get("noLoginDays");
		if (StringUtils.isNotBlank(noLoginDaysObj) && NumberUtils.isNumber(noLoginDaysObj.toString())) {
			int noLoginDays = NumberUtils.toInt(noLoginDaysObj.toString());
			switch (noLoginDays) {
				case 1:
					querySql.append(" HAVING (sysdate-max(d.created_dt))>=:noLoginDays");
					parameters.put("noLoginDays", 7);
					break;
				case 2:
					querySql.append(" HAVING (sysdate-max(d.created_dt))>:noLoginDays");
					parameters.put("noLoginDays", 3);
					break;
				case 3:
					querySql.append(" HAVING (sysdate-max(d.created_dt))<=:noLoginDays");
					parameters.put("noLoginDays", 3);
					break;
				case 4:
					querySql.append(" HAVING max(d.created_dt) IS NULL");
					break;
				case 5:
					querySql.append(" HAVING to_char(sysdate,'yyyy-mm-dd')=to_char(max(d.created_dt),'yyyy-mm-dd')");
					break;
				case 6:
					querySql.append(
							" HAVING to_char(sysdate,'yyyy-mm-dd')=to_char(max(d.created_dt),'yyyy-mm-dd') AND c.IS_ONLINE = 'Y' ");
					break;
			}
		}
		return commonDao.queryForMapListNative(querySql.toString(), parameters);
	}
}
