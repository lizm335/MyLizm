package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.openclass.GjtOnlineLessonFileDao;
import com.gzedu.xlims.dao.openclass.LcmsOnlineLessonDao;
import com.gzedu.xlims.dao.openclass.LcmsOnlineObjectDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.openClass.GjtOnlineLessonFile;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineObject;
import com.gzedu.xlims.pojo.openClass.LessonVO;
import com.gzedu.xlims.pojo.openClass.OnlineLessonVo;
import com.gzedu.xlims.service.edumanage.LcmsOnlineLessonService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class LcmsOnlineLessonServiceImpl extends BaseServiceImpl<LcmsOnlineLesson> implements LcmsOnlineLessonService {

	@Autowired
	private LcmsOnlineLessonDao lcmsOnlineLessonDao;

	@Autowired
	private LcmsOnlineObjectDao lcmsOnlineObjectDao;

	@Autowired
	private GjtOnlineLessonFileDao gjtOnlineLessonFileDao;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private CommonDao commonDao;

	@Override
	protected BaseDao<LcmsOnlineLesson, String> getBaseDao() {
		return lcmsOnlineLessonDao;
	}

	@Override
	public List<String> findTermCourseIdByOnlinetutorId(String onlinetutorId) {
		return lcmsOnlineObjectDao.findTermCourseIdByOnlinetutorId(onlinetutorId);
	}

	@Override
	public List<GjtGrade> findGjtGradeByOnlinetutorId(String onlinetutorId) {
		return lcmsOnlineObjectDao.findGjtGradeByOnlinetutorId(onlinetutorId);
	}

	@Override
	public List<GjtCourse> findGjtCourseByOnlinetutorId(String onlinetutorId) {
		return lcmsOnlineObjectDao.findGjtCourseByOnlinetutorId(onlinetutorId);
	}

	@Override
	public List<GjtOnlineLessonFile> findLessonFileByOnlinetutorId(String onlinetutorId) {
		return gjtOnlineLessonFileDao.findLessonFileByOnlinetutorId(onlinetutorId);
	}

	@Override
	public PageImpl<Map<String, Object>> findOnlineLesson(Map param, PageRequest pageRequest) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  SELECT LOL.ONLINETUTOR_ID \"onlinetutorId\"");
		sql.append("  FROM lcms_online_lesson    LOL,");
		sql.append("  LCMS_ONLINE_OBJECT LOO,");
		sql.append("  GJT_COURSE            GC,");
		sql.append("  GJT_TERM_COURSEINFO   GTC");
		sql.append("  WHERE LOL.IS_DELETED= 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND LOL.ONLINETUTOR_ID = LOO.ONLINETUTOR_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = LOO.OBJECT_ID");
		sql.append("  AND GC.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND LOO.OBJECT_TYPE='3'");

		if (EmptyUtils.isNotEmpty(param.get("onlinetutorId"))) {
			params.put("onlinetutorId", ObjectUtils.toString(param.get("onlinetutorId")));
			sql.append("  AND LOL.ONLINETUTOR_ID = :onlinetutorId");
		}

		if (EmptyUtils.isNotEmpty(param.get("termCourseId"))) {
			params.put("TERMCOURSE_ID", ObjectUtils.toString(param.get("termCourseId")));
			sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID");
		}
		if (EmptyUtils.isNotEmpty(param.get("termId")) && ObjectUtils.toString(param.get("termId")).length() > 10) {
			params.put("TERM_ID", ObjectUtils.toString(param.get("termId")));
			sql.append("  AND GTC.TERM_ID = :TERM_ID");
		}

		if (EmptyUtils.isNotEmpty(param.get("onlineName"))) {
			params.put("ONLINE_NAME", "%" + ObjectUtils.toString(param.get("onlineName")) + "%");
			sql.append("  AND LOL.ONLINETUTOR_NAME LIKE :ONLINE_NAME");
		}

		if (EmptyUtils.isNotEmpty(param.get("courseName"))) {
			params.put("COURSE_NAME", "%" + ObjectUtils.toString(param.get("courseName")) + "%");
			sql.append("  AND ( GC.KCMC LIKE :COURSE_NAME OR  GC.KCH LIKE :COURSE_NAME ) ");
		}

		String lessonType = ObjectUtils.toString(param.get("lessonType"));
		if (EmptyUtils.isNotEmpty(lessonType)) {
			if ("1".equals(lessonType)) {// 直播中
				sql.append("  AND (LOL.ONLINETUTOR_START < SYSDATE AND LOL.ONLINETUTOR_FINISH > SYSDATE )");
			} else if ("0".equals(lessonType)) {// 未开始
				sql.append("  AND LOL.ONLINETUTOR_START > SYSDATE");
			} else if ("2".equals(lessonType)) {// 已结束
				sql.append("  AND LOL.ONLINETUTOR_FINISH < SYSDATE");
			}
		}
		String classTeacher = ObjectUtils.toString(param.get("classTeacher"));
		if (EmptyUtils.isNotEmpty(classTeacher)) {
			params.put("CLASS_TEACHER", classTeacher);
			sql.append("  AND GTC.CLASS_TEACHER = :CLASS_TEACHER");
		}
		sql.append(" GROUP BY LOL.ONLINETUTOR_ID ORDER BY max(LOL.CREATED_DT) DESC");
		Page<Map<String, Object>> page = commonDao.queryForPageNative(sql.toString(), params, pageRequest);

		if (page.getTotalElements() == 0) {
			return new PageImpl<Map<String, Object>>(new ArrayList());
		}
		sql = new StringBuilder();
		params.clear();
		List<Object> onlinetutorIds = new ArrayList<Object>();
		for (Map numberMap : page.getContent()) {
			onlinetutorIds.add(numberMap.get("onlinetutorId"));
		}
		params.put("onlinetutorIds", onlinetutorIds);
		sql.append("  SELECT LOL.ID ONLINE_ID,");
		sql.append("  LOL.ONLINETUTOR_ID ONLINETUTOR_ID,");
		sql.append("  LOL.IMG_URL,");
		sql.append("  LOL.ONLINETUTOR_DESC,");
		sql.append("  LOL.NUMBER_ NUMBER_,");
		sql.append("  LOL.ONLINETUTOR_NAME ONLINE_NAME,");
		sql.append("  LOL.TEACHERTOKEN TCH_TOKEN,");
		sql.append("  LOL.TEACHERJOINURL TCH_URL,");
		sql.append("  LOL.VIDEOJOINURL VIDEO_URL,");
		sql.append("  LOL.VIDEOTOKEN VIDEO_TOKEN,");
		sql.append("  LOL.ONLINETUTOR_START START_DT,");
		sql.append("  LOL.ONLINETUTOR_FINISH END_DT,");
		sql.append("  GG.GRADE_NAME,");
		sql.append("  GC.KCMC,");
		sql.append("  GC.KCH,");
		sql.append("  (SELECT COUNT(1)");
		sql.append("  FROM GJT_TERM_COURSEINFO a, GJT_REC_RESULT b");
		sql.append("  WHERE a.termcourse_id = b.termcourse_id");
		sql.append("  and a.is_deleted = 'N'");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  and a.termcourse_id = LOO.OBJECT_ID) ALL_STU,");
		sql.append("  (SELECT COUNT(1)");
		sql.append("  FROM LCMS_ONLINETUTOR_USER t");
		sql.append("  WHERE t.ONLINETUTOR_ID = LOL.ONLINETUTOR_ID");
		sql.append("  AND t.ISDELETED = 'N'");
		sql.append("  and t.IS_INTO = 'Y') JOIN_STU,");
		sql.append("  NVL(GEI.XM, '--') CLASS_TEACHER,");
		sql.append("  (CASE");
		sql.append("  WHEN LOL.ONLINETUTOR_START > SYSDATE THEN");
		sql.append("  'needlesson'");
		sql.append("  WHEN LOL.ONLINETUTOR_FINISH < SYSDATE THEN");
		sql.append("  'lessoned'");
		sql.append("  ELSE");
		sql.append("  'lessoning'");
		sql.append("  END) LESSON_STATE,");
		sql.append("  FLOOR(LOL.ONLINETUTOR_START - SYSDATE) DAY");
		sql.append("  FROM lcms_online_lesson    LOL,");
		sql.append("  GJT_COURSE            GC,");
		sql.append("  GJT_GRADE             GG,");
		sql.append("  GJT_TERM_COURSEINFO   GTC,");
		sql.append("  LCMS_ONLINE_OBJECT LOO,");
		sql.append("  GJT_EMPLOYEE_INFO     GEI");
		sql.append("  WHERE LOL.IS_DELETED = 'N'");
		sql.append("  AND LOO.IS_DELETED='N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GG.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND LOL.ONLINETUTOR_ID = LOO.ONLINETUTOR_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = LOO.OBJECT_ID");
		sql.append("  AND GC.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND GG.GRADE_ID = GTC.TERM_ID");
		sql.append("  AND GEI.EMPLOYEE_ID = GTC.CLASS_TEACHER");
		sql.append("  AND LOL.ONLINETUTOR_ID IN (:onlinetutorIds)");
		sql.append("  ORDER BY LOL.CREATED_DT DESC ");
		List<Map<String, Object>> list = commonDao.queryForMapList(sql.toString(), params);

		return new PageImpl<Map<String, Object>>(list, pageRequest, page.getTotalElements());
	}

	@Override
	public int insertJoinStudent(String onlinetutorId, String termcourseId, String createBy) {
		StringBuilder sql=new StringBuilder();
		sql.append("  insert into lcms_onlinetutor_user");
		sql.append("  (ONLINETUTOR_ID, USER_ID, IS_INTO, ISDELETED, CREATED_BY, CREATED_DT)");
		sql.append("  select :onlinetutorId,");
		sql.append("  b.student_id,");
		sql.append("  'N',");
		sql.append("  'N',");
		sql.append("  :createBy,");
		sql.append("  sysdate");
		sql.append("  from gjt_term_courseinfo a, gjt_rec_result b");
		sql.append("  where a.termcourse_id = b.termcourse_id");
		sql.append("  and a.is_deleted = 'N'");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  and a.termcourse_id = :termcourseId");
		sql.append("  and not exists (select 1 from lcms_onlinetutor_user lou where lou.onlinetutor_id=:onlinetutorId and lou.user_id=b.student_id)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("onlinetutorId", onlinetutorId);
		params.put("termcourseId", termcourseId);
		params.put("createBy", createBy);
		return commonDao.insertForMapNative(sql.toString(), params);
	}


	@Override
	public long countJoinStudent(String onlinetutorId, String joinType, Map<String, Object> searchParams) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("select 1 from lcms_onlinetutor_user t,gjt_student_info s where t.user_id=s.student_id and t.onlinetutor_id=:onlinetutorId");
		sql.append(" and t.isdeleted='N'");
		sql.append(" and s.is_deleted='N'");
		if ("0".equals(joinType)) {
			sql.append(" and t.is_into='N'");
		} else if ("1".equals(joinType)) {
			sql.append(" and t.is_into='Y'");
		} else if ("2".equals(joinType)) {
			sql.append(
					" AND EXISTS(SELECT 1 FROM GJT_LESSON_VIEWRECORD GLV WHERE GLV.ONLINETUTOR_ID=t.ONLINETUTOR_ID AND GLV.STUDENT_ID=s.STUDENT_ID)");
		}
		if (StringUtils.isNotBlank(searchParams.get("studentName"))) {
			sql.append(" and s.xm like :studentName");
			params.put("studentName", "%" + searchParams.get("studentName") + "%");
		}
		if (StringUtils.isNotBlank(searchParams.get("studentCode"))) {
			sql.append(" and s.xh=:studentCode");
			params.put("studentCode", searchParams.get("studentCode"));
		}
		if (StringUtils.isNotBlank(searchParams.get("specialtyId"))) {
			sql.append(" and exists(");
			sql.append("  select 1");
			sql.append("  from gjt_specialty_base a, gjt_specialty b");
			sql.append("  where a.specialty_base_id = b.specialty_base_id");
			sql.append("  and b.specialty_id = s.major");
			sql.append("  and a.specialty_base_id = :sepcialtyId)");
			params.put("sepcialtyId", searchParams.get("specialtyId"));
		}
		params.put("onlinetutorId", onlinetutorId);

		return commonDao.queryForCountNative(sql.toString(), params);
	}
	
	@Override
	public Page<Map<String, Object>> findLessonStudent(Map<String, Object> searchParams, PageRequest pageRequest) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  SELECT GSI.AVATAR PIC,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GG.GRADE_NAME TERM_NAME,");
		sql.append("  GY.NAME GRADE_NAME,");
		sql.append("  GS.ZYMC,");
		sql.append("  GC.KCMC,");
		sql.append("  GC.KCH,");
		sql.append("  LOU.UPDATED_DT LOGIN_TIME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC,");
		sql.append("  (SELECT COUNT(1)");
		sql.append("  FROM GJT_LESSON_VIEWRECORD T");
		sql.append("  WHERE T.STUDENT_ID = LOU.USER_ID");
		sql.append("  AND T.ONLINETUTOR_ID = LOU.ONLINETUTOR_ID) VIEW_COUNT,");
		sql.append("  LOU.IS_INTO,");
		sql.append("  LOU.INTO_CLIENT");
		sql.append("  ");
		sql.append("  FROM LCMS_ONLINETUTOR_USER LOU,");
		sql.append("  LCMS_ONLINE_OBJECT    LOO,");
		sql.append("  GJT_STUDENT_INFO      GSI,");
		sql.append("  GJT_TERM_COURSEINFO   GTC,");
		sql.append("  GJT_REC_RESULT        GRR,");
		sql.append("  GJT_GRADE             GG,");
		sql.append("  GJT_YEAR              GY,");
		sql.append("  GJT_SPECIALTY         GS,");
		sql.append("  GJT_SPECIALTY_BASE    GSB,");
		sql.append("  GJT_COURSE            GC");
		sql.append("  WHERE LOU.USER_ID = GSI.STUDENT_ID");
		sql.append("  AND LOU.ONLINETUTOR_ID = LOO.ONLINETUTOR_ID");
		sql.append("  AND LOO.OBJECT_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = GRR.TERMCOURSE_ID");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GSI.NJ = GG.GRADE_ID");
		sql.append("  AND GG.YEAR_ID = GY.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GS.SPECIALTY_ID");
		sql.append("  AND GS.SPECIALTY_BASE_ID = GSB.SPECIALTY_BASE_ID");
		sql.append("  AND GTC.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND LOU.ISDELETED = 'N'");
		sql.append("  AND LOO.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GG.IS_DELETED = 'N'");
		sql.append("  AND GS.IS_DELETED = 'N'");
		sql.append("  AND GSB.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");

		if(StringUtils.isNotBlank(searchParams.get("onlinetutorId"))){
			sql.append("  AND LOU.ONLINETUTOR_ID = :onlinetutorId");
			params.put("onlinetutorId", searchParams.get("onlinetutorId"));
		}

		if (StringUtils.isNotBlank(searchParams.get("studentName"))) {
			sql.append("  AND GSI.XM LIKE :studentName");
			params.put("studentName", "%" + searchParams.get("studentName") + "%");
		}

		if (StringUtils.isNotBlank(searchParams.get("studentCode"))) {
			sql.append("  AND GSI.XH = :studentCode");
			params.put("studentCode", searchParams.get("studentCode"));
		}

		if (StringUtils.isNotBlank(searchParams.get("specialtyId"))) {
			sql.append("  AND GSB.SPECIALTY_BASE_ID = :specialtyId");
			params.put("specialtyId", searchParams.get("specialtyId"));
		}
		
		String joinType = ObjectUtils.toString(searchParams.get("joinType"));
		if ("0".equals(joinType))// 未参与
			sql.append(" AND LOU.IS_INTO = 'N'");
		else if ("1".equals(joinType))// 已参与
			sql.append(" AND LOU.IS_INTO = 'Y'");
		else if ("2".equals(joinType)) {// 已查看录播
			sql.append(
					" AND EXISTS(SELECT 1 FROM GJT_LESSON_VIEWRECORD GLV WHERE GLV.ONLINETUTOR_ID=LOU.ONLINETUTOR_ID AND GLV.STUDENT_ID=LOU.USER_ID)");
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}

	@Override
	public Page<Map<String, Object>> findActivityStudent(Map<String, Object> searchParams, PageRequest pageRequest) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  SELECT GSI.AVATAR PIC,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GG.GRADE_NAME TERM_NAME,");
		sql.append("  GS.ZYMC,");
		sql.append("  LOU.UPDATED_DT LOGIN_TIME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC,");
		sql.append("  (SELECT COUNT(1)");
		sql.append("  FROM GJT_LESSON_VIEWRECORD T");
		sql.append("  WHERE T.STUDENT_ID = LOU.USER_ID");
		sql.append("  AND T.ONLINETUTOR_ID = LOU.ONLINETUTOR_ID) VIEW_COUNT,");
		sql.append("  LOU.IS_INTO,");
		sql.append("  LOU.INTO_CLIENT");
		sql.append("  ");
		sql.append("  FROM LCMS_ONLINETUTOR_USER LOU,");
		sql.append("  GJT_STUDENT_INFO      GSI,");
		sql.append("  GJT_GRADE             GG,");
		sql.append("  GJT_SPECIALTY         GS,");
		sql.append("  GJT_SPECIALTY_BASE    GSB");
		sql.append("  WHERE LOU.USER_ID = GSI.STUDENT_ID");
		sql.append("  AND GSI.NJ = GG.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GS.SPECIALTY_ID");
		sql.append("  AND GS.SPECIALTY_BASE_ID = GSB.SPECIALTY_BASE_ID");
		sql.append("  AND LOU.ISDELETED = 'N'");
		sql.append("  AND GG.IS_DELETED = 'N'");
		sql.append("  AND GS.IS_DELETED = 'N'");
		sql.append("  AND GSB.IS_DELETED = 'N'");

		if (StringUtils.isNotBlank(searchParams.get("onlinetutorId"))) {
			sql.append("  AND LOU.ONLINETUTOR_ID = :onlinetutorId");
			params.put("onlinetutorId", searchParams.get("onlinetutorId"));
		}

		if (StringUtils.isNotBlank(searchParams.get("studentName"))) {
			sql.append("  AND GSI.XM LIKE :studentName");
			params.put("studentName", "%" + searchParams.get("studentName") + "%");
		}

		if (StringUtils.isNotBlank(searchParams.get("studentCode"))) {
			sql.append("  AND GSI.XH = :studentCode");
			params.put("studentCode", searchParams.get("studentCode"));
		}

		if (StringUtils.isNotBlank(searchParams.get("specialtyId"))) {
			sql.append("  AND GSB.SPECIALTY_BASE_ID = :specialtyId");
			params.put("specialtyId", searchParams.get("specialtyId"));
		}

		String joinType = ObjectUtils.toString(searchParams.get("joinType"));
		if ("0".equals(joinType))// 未参与
			sql.append(" AND LOU.IS_INTO = 'N'");
		else if ("1".equals(joinType))// 已参与
			sql.append(" AND LOU.IS_INTO = 'Y'");
		else if ("2".equals(joinType)) {// 已查看录播
			sql.append(
					" AND EXISTS(SELECT 1 FROM GJT_LESSON_VIEWRECORD GLV WHERE GLV.ONLINETUTOR_ID=LOU.ONLINETUTOR_ID AND GLV.STUDENT_ID=LOU.USER_ID)");
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}

	@Override
	public Page<? extends LcmsOnlineLesson> findOnlineActivityList(Map<String, Object> searchParams, PageRequest pageRequst) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select t.ID as \"id\",");
		sql.append("  t.onlinetutor_id \"onlinetutorId\",");
		sql.append("  t.onlinetutor_name \"onlinetutorName\",");
		sql.append("  t.onlinetutor_start \"onlinetutorStart\",");
		sql.append("  t.onlinetutor_finish \"onlinetutorFinish\",");
		sql.append("  t.teacherjoinurl \"teacherjoinurl\",");
		sql.append("  t.teachertoken \"teachertoken\",");
		sql.append("  (select count(1)");
		sql.append("  from lcms_onlinetutor_user lou,");
		sql.append("  gjt_student_info gsi");
		sql.append("  where lou.onlinetutor_id = t.onlinetutor_id");
		sql.append("  and lou.user_id=gsi.student_id");
		sql.append("  and gsi.is_deleted='N'");
		sql.append("  and lou.isdeleted = 'N') \"allNum\",");
		sql.append("  (select count(1)");
		sql.append("  from lcms_onlinetutor_user lou");
		sql.append("  where lou.onlinetutor_id = t.onlinetutor_id");
		sql.append("  and lou.isdeleted = 'N'");
		sql.append("  and lou.is_into = 'Y') \"joinNum\"");
		sql.append("  from lcms_online_lesson t");
		sql.append("  where t.created_by=:createdBy");
		sql.append("  and t.is_deleted='N'");
		sql.append("  and t.lesson_type=1");// 活动直播
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("createdBy", searchParams.get("createdBy"));
		if (StringUtils.isNotBlank(searchParams.get("onlinetutorId"))) {
			sql.append(" and onlinetutor_id = :onlinetutorId");
			params.put("onlinetutorId", searchParams.get("onlinetutorId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("onlinetutorName"))) {
			sql.append(" and t.onlinetutor_name like :onlinetutorName");
			params.put("onlinetutorName", "%" + searchParams.get("onlinetutorName") + "%");
		}
		if (StringUtils.isNotBlank(searchParams.get("onlinetutorStart"))) {
			sql.append(" and to_char(t.onlinetutor_start,'yyyy-MM-dd') = :onlinetutorStart");
			params.put("onlinetutorStart", searchParams.get("onlinetutorStart"));
		}
		if (StringUtils.isNotBlank(searchParams.get("lessonType"))) {
			if ("0".equals(searchParams.get("lessonType"))) {
				sql.append(" and t.onlinetutor_start >sysdate");
			} else if ("1".equals(searchParams.get("lessonType"))) {
				sql.append(" and t.onlinetutor_start <=sysdate and t.onlinetutor_finish>=sysdate");
			} else if ("2".equals(searchParams.get("lessonType"))) {
				sql.append(" and t.onlinetutor_start <=sysdate and t.onlinetutor_finish<=sysdate");
			}
		}
		sql.append(" order by t.created_dt desc");
		Page<LessonVO> list = commonDao.queryBeanForPageNative(sql.toString(), params, pageRequst, LessonVO.class);
		return list;
	}

	@Override
	public List<LcmsOnlineObject> findOnlineObjectByOnlinetutorId(String onlinetutorId) {
		return lcmsOnlineObjectDao.findByOnlinetutorId(onlinetutorId);
	}

	@Override
	public boolean saveVideoUrl(LcmsOnlineLesson onlineLesson) {
		try {
			String url = AppConfig.getProperty("getCourseware_url");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roomId", onlineLesson.getSdkId());
			params.put("loginName", AppConfig.getProperty("gensee_loginName"));
			params.put("password", AppConfig.getProperty("gensee_password"));
			String res = HttpClientUtils.doHttpPost(url, params, 3000, "UTF-8");
			JSONObject json = JSONObject.parseObject(res);
			if ("0".equals(json.get("code")) && json.get("coursewares") != null) {
				JSONArray array = json.getJSONArray("coursewares");
				json = array.getJSONObject(0);
				String videoUrl = json.getString("url");
				String videoToken = json.getString("token");
				onlineLesson.setVideojoinurl(videoUrl);
				onlineLesson.setVideotoken(videoToken);
				lcmsOnlineLessonDao.save(onlineLesson);
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public List<String> queryActivityStudentIds(String orgId, List<String> pyccIds, List<String> gradeIds, List<String> specialtyIds,
			List<String> courseIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select distinct(gsi.student_id)");
		sql.append("  from gjt_student_info gsi, gjt_grade_specialty ggs");
		sql.append("  where gsi.grade_specialty_id = ggs.id");
		sql.append("  and gsi.is_deleted = 'N'");
		sql.append("  and ggs.is_deleted = 'N'");
		sql.append("  and gsi.xx_id = (select o.id");
		sql.append("  from gjt_org o");
		sql.append("  where o.is_deleted = 'N'");
		sql.append("  and o.org_type = '1'");
		sql.append("  START WITH o.id = :orgId");
		sql.append("  CONNECT BY PRIOR o.perent_id = o.id)");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);

		if (CollectionUtils.isNotEmpty(pyccIds)) {
			sql.append("  and gsi.pycc in (:pyccIds)");
			params.put("pyccIds", pyccIds);
		}

		if (CollectionUtils.isNotEmpty(gradeIds)) {
			sql.append("  and ggs.grade_id in (:gradeIds)");
			params.put("gradeIds", gradeIds);
		}

		if (CollectionUtils.isNotEmpty(specialtyIds)) {
			sql.append("  and ggs.specialty_id in (:specialtyIds)");
			params.put("specialtyIds", specialtyIds);
		}

		if (CollectionUtils.isNotEmpty(courseIds)) {
			sql.append("  and exists (select 1");
			sql.append("  from gjt_term_courseinfo gtc, gjt_rec_result grr");
			sql.append("  where gtc.termcourse_id = grr.termcourse_id");
			sql.append("  and gtc.is_deleted = 'N'");
			sql.append("  and grr.is_deleted = 'N'");
			sql.append("  and grr.student_id = gsi.student_id");
			sql.append("  and grr.course_id in (:courseIds))");
			params.put("courseIds", courseIds);
		}
		return commonDao.queryForStringListNative(sql.toString(), params);
	}

	@Override
	public List<String> queryActivityStudentIds(String onlinetutorId) {
		return lcmsOnlineLessonDao.queryActivityStudentIds(onlinetutorId);
	}

	@Override
	public Page<Map<String, Object>> queryActivityStudent(Map<String, Object> searchParams, PageRequest pageRequst) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select gsi.student_id,");
		sql.append("  gsi.avatar,");
		sql.append("  gsi.xm,");
		sql.append("  gsi.xh,");
		sql.append("  gsi.sjh,");
		sql.append("  gsi.xjzt,");
		sql.append("  gsi.xxzx_id,");
		sql.append("  gsi.pycc,");
		sql.append("  ggs.grade_id,");
		sql.append("  ggs.specialty_id");
		sql.append("  from gjt_student_info gsi, gjt_grade_specialty ggs");
		sql.append("  where gsi.grade_specialty_id = ggs.id");
		sql.append("  and gsi.is_deleted = 'N'");

		@SuppressWarnings("unchecked")
		List<String> studentIds = (List<String>) searchParams.get("studentIds");
		if (CollectionUtils.isNotEmpty(studentIds)) {
			if (studentIds.size() > 1000) {// in(...)大于1000报错，只能拆分
				sql.append(" and (");
				int loopCount = studentIds.size() % 1000 == 0 ? studentIds.size() / 1000 : (studentIds.size() / 1000 + 1);
				for (int i = 0; i < loopCount; i++) {
					// 子List的起始值
					int startNum = i * 1000;
					// 子List的终止值
					int endNum = (i + 1) * 1000;
					// 不能整除的时候最后一个List的终止值为原始List的最后一个
					if (i == loopCount - 1) {
						endNum = studentIds.size();
					}
					// 拆分List
					List<String> subList = studentIds.subList(startNum, endNum);
					String tag = UUIDUtils.random();
					if (i > 0) {
						sql.append(" or");
					}
					sql.append(" gsi.student_id in ").append(String.format("(:%s)", tag));
					params.put(tag, subList);
				}
				sql.append(" )");
			} else {
				sql.append("  and gsi.student_id in (:studentIds)");
				params.put("studentIds", searchParams.get("studentIds"));
			}
		}

		if (StringUtils.isNotBlank(searchParams.get("studyCenterId"))) {
			sql.append("  and gsi.xxzx_id = :studyCenterId");
			params.put("studyCenterId", searchParams.get("studyCenterId"));
		} else {
			sql.append("  and gsi.xx_id = (select o.id");
			sql.append("  from gjt_org o");
			sql.append("  where o.is_deleted = 'N'");
			sql.append("  and o.org_type = '1'");
			sql.append("  START WITH o.id = :orgId");
			sql.append("  CONNECT BY PRIOR o.perent_id = o.id)");
			params.put("orgId", searchParams.get("orgId"));
		}

		if (StringUtils.isNotBlank(searchParams.get("pyccId"))) {
			sql.append("  and gsi.pycc = :pyccId");
			params.put("pyccId", searchParams.get("pyccId"));
		}

		if (StringUtils.isNotBlank(searchParams.get("gradeId"))) {
			sql.append("  and ggs.grade_id = :gradeId");
			params.put("gradeId", searchParams.get("gradeId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("specialtyId"))) {
			sql.append("  and ggs.specialty_id = :specialtyId");
			params.put("specialtyId", searchParams.get("specialtyId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("studentName"))) {
			sql.append("  and gsi.xm like :studentName");
			params.put("studentName", "%" + searchParams.get("studentName").toString() + "%");
		}

		if (StringUtils.isNotBlank(searchParams.get("studentCode"))) {
			sql.append("  and gsi.xh like :studentCode");
			params.put("studentCode", searchParams.get("studentCode").toString());
		}


		if (StringUtils.isNotBlank(searchParams.get("signupStatus"))) {
			sql.append("  and gsi.xjzt = :signupStatus");
			params.put("signupStatus", searchParams.get("signupStatus"));
		}


		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	@Override
	public List<String> queryActivityStudent(Map<String, Object> searchParams) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select gsi.student_id");
		sql.append("  from gjt_student_info gsi, gjt_grade_specialty ggs");
		sql.append("  where gsi.grade_specialty_id = ggs.id");
		sql.append("  and gsi.is_deleted = 'N'");
		if (StringUtils.isNotBlank(searchParams.get("studyCenterId"))) {
			sql.append("  and gsi.xxzx_id = :studyCenterId");
			params.put("studyCenterId", searchParams.get("studyCenterId"));
		} else {
			sql.append("  and gsi.xx_id = (select o.id");
			sql.append("  from gjt_org o");
			sql.append("  where o.is_deleted = 'N'");
			sql.append("  and o.org_type = '1'");
			sql.append("  START WITH o.id = :orgId");
			sql.append("  CONNECT BY PRIOR o.perent_id = o.id)");
			params.put("orgId", searchParams.get("orgId"));
		}

		if (StringUtils.isNotBlank(searchParams.get("pyccId"))) {
			sql.append("  and gsi.pycc = :pyccId");
			params.put("pyccId", searchParams.get("pyccId"));
		}

		if (StringUtils.isNotBlank(searchParams.get("gradeId"))) {
			sql.append("  and ggs.grade_id = :gradeId");
			params.put("gradeId", searchParams.get("gradeId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("specialtyId"))) {
			sql.append("  and ggs.specialty_id = :specialtyId");
			params.put("specialtyId", searchParams.get("specialtyId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("studentName"))) {
			sql.append("  and gsi.xm like :studentName");
			params.put("studentName", "%" + searchParams.get("studentName").toString() + "%");
		}

		if (StringUtils.isNotBlank(searchParams.get("studentCode"))) {
			sql.append("  and gsi.xh like :studentCode");
			params.put("studentCode", searchParams.get("studentCode").toString());
		}

		if (StringUtils.isNotBlank(searchParams.get("signupStatus"))) {
			sql.append("  and gsi.xjzt = :signupStatus");
			params.put("signupStatus", searchParams.get("signupStatus"));
		}
		
		return commonDao.queryForStringListNative(sql.toString(), params);
	}

	@Override
	public void updateOnlineActivity(LcmsOnlineLesson lesson, List<LcmsOnlineObject> onlineObjects, List<String> studentIds, List<String> files) {
		Date now = new Date();
		LcmsOnlineLesson reLesson = lcmsOnlineLessonDao.findOne(lesson.getId());
		reLesson.setOnlinetutorName(lesson.getOnlinetutorName());
		reLesson.setOnlinetutorLabel(lesson.getOnlinetutorLabel());
		reLesson.setSubject(lesson.getSubject());
		reLesson.setOnlinetutorDesc(lesson.getOnlinetutorDesc());
		reLesson.setDescription(lesson.getOnlinetutorDesc());
		reLesson.setImgUrl(lesson.getImgUrl());
		reLesson.setActivityContent(lesson.getActivityContent());
		reLesson.setStartdate(lesson.getOnlinetutorStart());
		reLesson.setInvaliddate(lesson.getOnlinetutorFinish());
		reLesson.setOnlinetutorStart(lesson.getOnlinetutorStart());
		reLesson.setOnlinetutorFinish(lesson.getOnlinetutorFinish());
		reLesson.setUpdatedDt(now);
		reLesson.setUpdatedBy(lesson.getUpdatedBy());
		reLesson = updateRotmeLesson(reLesson);
		lcmsOnlineLessonDao.save(reLesson);
		// 删除原来的LcmsOnlineObject
		List<LcmsOnlineObject> lcmsOnlineObjects = lcmsOnlineObjectDao.findByOnlinetutorId(reLesson.getOnlinetutorId());
		for (LcmsOnlineObject obj : lcmsOnlineObjects) {
			obj.setIsDeleted("Y");
			obj.setUpdatedDt(now);
			obj.setUpdatedBy(reLesson.getUpdatedBy());
			lcmsOnlineObjectDao.save(obj);
		}
		for (LcmsOnlineObject obj : onlineObjects) {
			obj.setIsDeleted(Constants.NODELETED);
			obj.setOnlineObjectId(UUIDUtils.random());
			obj.setCreatedDt(now);
			obj.setOnlinetutorId(reLesson.getOnlinetutorId());
			lcmsOnlineObjectDao.save(obj);
		}
		// 删除原来的参与学员
		lcmsOnlineLessonDao.deleteStudentByOnlinetutorId(reLesson.getOnlinetutorId());
		for (String studentId : studentIds) {
			lcmsOnlineLessonDao.insertOnlinetutorUser(reLesson.getOnlinetutorId(), studentId, Constants.BOOLEAN_NO);
		}
		List<GjtOnlineLessonFile> lessonFiles = gjtOnlineLessonFileDao.findLessonFileByOnlinetutorId(reLesson.getOnlinetutorId());
		for (GjtOnlineLessonFile lessonFile : lessonFiles) {
			lessonFile.setIsDeleted(Constants.ISDELETED);
			lessonFile.setUpdatedDt(now);
			lessonFile.setUpdatedBy(reLesson.getUpdatedBy());
			gjtOnlineLessonFileDao.save(lessonFile);
		}
		if (files != null) {
			for (String fileStr : files) {
				JSONObject json = JSONObject.parseObject(fileStr);
				GjtOnlineLessonFile lessonFile = new GjtOnlineLessonFile();
				lessonFile.setId(UUIDUtils.random());
				lessonFile.setFileName(json.getString("name"));
				lessonFile.setFileUrl(json.getString("path"));
				lessonFile.setOnlinetutorId(reLesson.getOnlinetutorId());
				lessonFile.setIsDeleted(Constants.NODELETED);
				lessonFile.setCreatedBy(reLesson.getUpdatedBy());
				lessonFile.setCreatedDt(now);
				gjtOnlineLessonFileDao.save(lessonFile);
			}
		}
	}

	@Override
	@Transactional
	public void saveOnlineActivity(LcmsOnlineLesson lesson, List<LcmsOnlineObject> onlineObjects, List<String> studentIds, List<String> files) {
		Date now = new Date();
		lesson = generateRotmeLesson(lesson);
		lesson.setId(UUIDUtils.random());
		lesson.setOnlinetutorId(UUIDUtils.random());
		lesson.setStartdate(lesson.getOnlinetutorStart());
		lesson.setInvaliddate(lesson.getOnlinetutorFinish());
		lesson.setLessonType(1);
		lesson.setIsDeleted(Constants.NODELETED);
		lesson.setCreatedDt(now);
		lcmsOnlineLessonDao.save(lesson);
		for (LcmsOnlineObject obj : onlineObjects) {
			obj.setIsDeleted("N");
			obj.setOnlineObjectId(UUIDUtils.random());
			obj.setCreatedDt(now);
			obj.setOnlinetutorId(lesson.getOnlinetutorId());
			lcmsOnlineObjectDao.save(obj);
		}
		for (String studentId : studentIds) {
			lcmsOnlineLessonDao.insertOnlinetutorUser(lesson.getOnlinetutorId(), studentId, Constants.BOOLEAN_NO);
		}
		if (files != null) {
			for (String fileStr : files) {
				JSONObject json = JSONObject.parseObject(fileStr);
				GjtOnlineLessonFile lessonFile = new GjtOnlineLessonFile();
				lessonFile.setId(UUIDUtils.random());
				lessonFile.setFileName(json.getString("name"));
				lessonFile.setFileUrl(json.getString("path"));
				lessonFile.setOnlinetutorId(lesson.getOnlinetutorId());
				lessonFile.setIsDeleted(Constants.NODELETED);
				lessonFile.setCreatedBy(lesson.getCreatedBy());
				lessonFile.setCreatedDt(now);
				gjtOnlineLessonFileDao.save(lessonFile);
			}
		}
	}


	private LcmsOnlineLesson updateRotmeLesson(LcmsOnlineLesson onlineLesson){
		String url = AppConfig.getProperty("roomModify_url");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", AppConfig.getProperty("gensee_loginName"));
		params.put("password", AppConfig.getProperty("gensee_password"));
		String subject = onlineLesson.getOnlinetutorName() + System.currentTimeMillis();
		params.put("subject", subject);
		params.put("startDate", DateUtils.getStringToDate(onlineLesson.getOnlinetutorStart()));
		params.put("invalidDate", DateUtils.getStringToDate(onlineLesson.getOnlinetutorFinish()));
		params.put("webJoin", "true");
		params.put("clientJoin", "true");
		params.put("teacherToken", onlineLesson.getTeachertoken());
		params.put("studentToken", onlineLesson.getStudenttoken());
		params.put("assistantToken", "111111");
		params.put("description", onlineLesson.getDescription());
		params.put("uiMode", "");
		params.put("Id", onlineLesson.getSdkId());
		String message = HttpClientUtils.doHttpPost(url, params, 10000, "UTF-8");
		OnlineLessonVo onlineLessonVo = JsonUtils.toObject(message, OnlineLessonVo.class);
		if ("0".equals(onlineLessonVo.getCode())) {
			onlineLesson.setSubject(subject);
			return onlineLesson;
		}
		return null;
	}

	private LcmsOnlineLesson generateRotmeLesson(LcmsOnlineLesson onlineLesson) {
		String url = AppConfig.getProperty("roomCreate_url");
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("loginName",  AppConfig.getProperty("gensee_loginName"));
		params.put("password",  AppConfig.getProperty("gensee_password"));
		String subject = onlineLesson.getOnlinetutorName() + System.currentTimeMillis();
		params.put("subject", subject);
		params.put("startDate",  DateUtils.getStringToDate(onlineLesson.getOnlinetutorStart()));
		params.put("invalidDate",  DateUtils.getStringToDate(onlineLesson.getOnlinetutorFinish()));
		params.put("webJoin", "true");
		params.put("clientJoin", "true");
		params.put("teacherToken", "654321");
		params.put("studentToken", "123456");
		params.put("assistantToken", "111111");
		params.put("description", onlineLesson.getDescription());
		params.put("uiMode", "4");
		
		String message = HttpClientUtils.doHttpPost(url, params, 10000, "UTF-8");
		OnlineLessonVo onlineLessonVo = JsonUtils.toObject(message, OnlineLessonVo.class);
		if ("0".equals(onlineLessonVo.getCode())) {
			onlineLesson.setSubject(subject);
			onlineLesson.setAssistanttoken(onlineLessonVo.getAssistantToken());
			onlineLesson.setClientjoin(onlineLessonVo.getClientJoin());
			onlineLesson.setCode(onlineLessonVo.getCode());
			onlineLesson.setSdkId(onlineLessonVo.getId());
			onlineLesson.setNumber(onlineLessonVo.getNumber());
			onlineLesson.setScene(onlineLessonVo.getScene());
			onlineLesson.setStudentclienttoken(onlineLessonVo.getStudentClientToken());
			onlineLesson.setStudentjoinurl(onlineLessonVo.getStudentJoinUrl());
			onlineLesson.setTeacherjoinurl(onlineLessonVo.getTeacherJoinUrl());
			onlineLesson.setTeachertoken(onlineLessonVo.getTeacherToken());
			onlineLesson.setWebjoin(onlineLessonVo.getWebJoin());
			onlineLesson.setStudenttoken(onlineLessonVo.getStudentToken());
			onlineLesson.setVideojoinurl(onlineLessonVo.getVideoJoinUrl());
			onlineLesson.setVideotoken(onlineLessonVo.getVideoToken());
			onlineLesson.setOnlinetutorType("2");
			onlineLesson.setIsDeleted(Constants.NODELETED);
			return onlineLesson;
		}
		return null;
	}

	@Override
	public void saveViewOnlineLessionRecord(String onlinetutorId, String studentId, int client) {
		Object onlinetutorUser = lcmsOnlineLessonDao.queryOnlinetutorUser(onlinetutorId, studentId);
		if (onlinetutorUser != null) {
			lcmsOnlineLessonDao.updateOnlinetutorUser(onlinetutorId, studentId, client);
		} else {
			lcmsOnlineLessonDao.insertOnlinetutorUser(onlinetutorId, studentId, Constants.BOOLEAN_YES);
		}
	}

	@Override
	public void insertGjtLessonViewrecord(String onlinetutorId, String studentId) {
		lcmsOnlineLessonDao.insertGjtLessonViewrecord(studentId, onlinetutorId);
	}


	@Override
	public int initStudentOnlineLesson(String studentId) {
		GjtStudentInfo student = gjtStudentInfoDao.findOne(studentId);
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select onlinetutor_id");
		sql.append("  from lcms_online_lesson lol");
		sql.append("  where lol.is_deleted = 'N'");
		sql.append("  and lol.lesson_type = 0");
		sql.append("  and exists");
		sql.append("  (select 1");
		sql.append("  from lcms_online_object loo");
		sql.append("  where loo.onlinetutor_id = lol.onlinetutor_id");
		sql.append("  and loo.is_deleted = 'N'");
		sql.append("  and (loo.object_id = :gradeId or exists");
		sql.append("  (select 1");
		sql.append("  from gjt_rec_result grr");
		sql.append("  where grr.is_deleted = 'N'");
		sql.append("  and grr.student_id = :studentId");
		sql.append("  and (grr.termcourse_id = loo.object_id or");
		sql.append("  grr.teach_plan_id = loo.object_id)) or exists");
		sql.append("  (select 1");
		sql.append("  from gjt_class_student gcs");
		sql.append("  where gcs.is_deleted = 'N'");
		sql.append("  and gcs.student_id = :studentId");
		sql.append("  and gcs.class_id = loo.object_id)))");
		params.put("gradeId", student.getGjtGrade().getGradeId());
		params.put("studentId", studentId);
		List<String> onlinetutorIds = commonDao.queryForStringListNative(sql.toString(), params);
		List<String> onlinetutorIds2 = lcmsOnlineLessonDao.findActivityOnlintutorId();

		for (String onlinetutorId : onlinetutorIds2) {
			params.clear();
			params.put("onlinetutorId", onlinetutorId);
			List<String> objectTypes = lcmsOnlineObjectDao.findObjectTypeByOnlinetutorId(onlinetutorId);
			sql = new StringBuilder();
			sql.append("  select 1");
			sql.append("  from lcms_online_lesson lol");
			sql.append("  where lol.onlinetutor_id=:onlinetutorId");
			for (String type : objectTypes) {
				if ("2".equals(type)) {// 课程班
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from lcms_online_object loo");
					sql.append("  where loo.onlinetutor_id = lol.onlinetutor_id");
					sql.append("  and loo.object_type = 2");
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from gjt_class_student gcs");
					sql.append("  where gcs.is_deleted = 'N'");
					sql.append("  and gcs.student_id = :studentId");
					sql.append("  and gcs.class_id = loo.object_id))");
					params.put("studentId", studentId);
				} else if ("3".equals(type)) {// 期课程
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from lcms_online_object loo");
					sql.append("  where loo.onlinetutor_id = lol.onlinetutor_id");
					sql.append("  and loo.object_type = 3");
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from gjt_rec_result grr");
					sql.append("  where grr.is_deleted = 'N'");
					sql.append("  and grr.student_id = :studentId");
					sql.append("  and (grr.termcourse_id = loo.object_id or");
					sql.append("  grr.teach_plan_id = loo.object_id)))");
					params.put("studentId", studentId);
				} else if ("4".equals(type)) {// 年级
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from lcms_online_object loo");
					sql.append("  where loo.onlinetutor_id = lol.onlinetutor_id");
					sql.append("  and loo.object_type = 4");
					sql.append("  and loo.object_id = :gradeId)");
					params.put("gradeId", student.getGjtGrade().getGradeId());
				} else if ("5".equals(type)) {// 专业
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from lcms_online_object loo");
					sql.append("  where loo.onlinetutor_id = lol.onlinetutor_id");
					sql.append("  and loo.object_type = 5");
					sql.append("  and loo.object_id = :specialtyId)");
					params.put("specialtyId", student.getGjtSpecialty().getSpecialtyId());
				} else if ("8".equals(type)) {// 培养层次
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from lcms_online_object loo");
					sql.append("  where loo.onlinetutor_id = lol.onlinetutor_id");
					sql.append("  and loo.object_type = 8");
					sql.append("  and loo.object_id = :pycc)");
					params.put("pycc", student.getPycc());
				} else if ("9".equals(type)) {// 课程
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from lcms_online_object loo");
					sql.append("  where loo.onlinetutor_id = lol.onlinetutor_id");
					sql.append("  and loo.object_type = 9");
					sql.append("  and exists");
					sql.append("  (select 1");
					sql.append("  from gjt_rec_result grr");
					sql.append("  where grr.is_deleted = 'N'");
					sql.append("  and grr.student_id = :studentId");
					sql.append("  and loo.object_id = grr.course_id))");
					params.put("gradeId", student.getGjtGrade().getGradeId());
				}
			}
			long count = commonDao.queryForCountNative(sql.toString(), params);
			if (count > 0) {
				onlinetutorIds.add(onlinetutorId);
			}
		}

		// 插入直播用户
		int insertCount = 0;
		for (String onlinetutorId : onlinetutorIds) {
			try {
				lcmsOnlineLessonDao.insertOnlinetutorUser(onlinetutorId, student.getStudentId());
				insertCount++;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return insertCount;
	}

}

