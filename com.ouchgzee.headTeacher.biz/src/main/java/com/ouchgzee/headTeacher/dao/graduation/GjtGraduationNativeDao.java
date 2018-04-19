package com.ouchgzee.headTeacher.dao.graduation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.pojo.status.GraduationApplyStatusEnum;
import com.ouchgzee.headTeacher.pojo.system.BzrStudyYear;

@Deprecated @Repository("bzrGjtGraduationNativeDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtGraduationNativeDao {
	
	@Autowired
	private CommonDao commonDao;
	
	/**
	 * 查询毕业专业安排列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryGraduationSpecialtyList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     b.BATCH_ID as \"batchId\",")
			.append("     b.BATCH_CODE as \"batchCode\",")
			.append("     b.BATCH_NAME as \"batchName\",")
			.append("     b.STUDY_YEAR_CODE as \"studyYearCode\",")
			//.append("     t1.GRADE_ID as \"gradeId\",")
			.append("     s.SPECIALTY_ID as \"specialtyId\",")
			.append("     s.ZYMC as \"specialtyName\",")
			.append("     s.PYCC as \"trainingLevel\",")
			.append("     d.NAME as \"trainingName\"")
			.append(" from ")
			.append("      GJT_GRADUATION_BATCH b")
			/*.append(" inner join (")
			.append("              select ")
			.append("                    GRADE_ID,")
			.append("                    max(STUDY_YEAR_CODE) as STUDY_YEAR_CODE")
			.append("              from ")
			.append("                   GJT_GRADE_SPECIALTY_PLAN")
			.append("              group by grade_id")
			.append("              having max(STUDY_YEAR_CODE) in (")
			.append("                                               select ")
			.append("                                                     STUDY_YEAR_CODE")
			.append("                                               from ")
			.append("                                                    GJT_GRADUATION_BATCH")
			.append("                                               where ")
			.append("                                                     IS_DELETED = 'N'")
			.append("                                              ) ")
			.append("             ) t1")
			.append(" on t1.STUDY_YEAR_CODE = b.STUDY_YEAR_CODE")
			.append(" inner join ")
			.append("      GJT_GRADE_SPECIALTY gs ")
			.append(" on gs.GRADE_ID = t1.GRADE_ID")*/
			.append(" inner join ")
			.append("      GJT_SPECIALTY s ")
			//.append(" on s.SPECIALTY_ID = gs.SPECIALTY_ID")
			.append(" on s.IS_DELETED = 'N' and s.ORG_ID = b.ORG_ID ")
			//.append(" on s.SPECIALTY_ID in ")
			//.append("                      (select distinct KKZY from VIEW_TEACH_PLAN where IS_DELETED='N' and GRADE_ID = 'a3347e80d84c45afbb34260b978484d3') ")
			.append(" inner join ")
			.append("      TBL_SYS_DATA d ")
			.append(" on s.PYCC = d.CODE and d.TYPE_CODE = 'TrainingLevel'")
			.append(" where ")
			.append("      b.IS_DELETED = 'N' ");
		
		/*Object batchCode = searchParams.get("LIKE_batchCode");
		if (batchCode != null && StringUtils.isNotBlank((String)batchCode)) {
			sb.append(" and b.BATCH_CODE like :batchCode ");
			map.put("batchCode", "%" + batchCode + "%");
		}
		
		Object batchName = searchParams.get("LIKE_batchName");
		if (batchName != null && StringUtils.isNotBlank((String)batchName)) {
			sb.append(" and b.BATCH_NAME like :batchName ");
			map.put("batchName", "%" + batchName + "%");
		}*/
		
		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null && StringUtils.isNotBlank((String)orgId)) {
			sb.append(" and b.ORG_ID in (select ID from GJT_ORG start with ID = :orgId connect by prior ID = PERENT_ID AND PERENT_ID != ID ) ");
			map.put("orgId", orgId);
		}

		Object batchId = searchParams.get("EQ_batchId");
		if (batchId != null && StringUtils.isNotBlank((String)batchId)) {
			sb.append(" and b.BATCH_ID = :batchId ");
			map.put("batchId", batchId);
		}

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank((String)status)) {
			if ("1".equals(status)) {  //未开始
				sb.append(" and b.STUDY_YEAR_CODE > :nowCode ");
				map.put("nowCode", BzrStudyYear.getNowCode());
			} else if ("2".equals(status)) {  //进行中
				sb.append(" and b.STUDY_YEAR_CODE = :nowCode ");
				map.put("nowCode", BzrStudyYear.getNowCode());
			} else if ("3".equals(status)) {  //已结束
				sb.append(" and b.STUDY_YEAR_CODE < :nowCode ");
				map.put("nowCode", BzrStudyYear.getNowCode());
			}
		}

		Object specialtyId = searchParams.get("EQ_specialtyId");
		if (specialtyId != null && StringUtils.isNotBlank((String)specialtyId)) {
			sb.append(" and s.SPECIALTY_ID = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}

		Object trainingLevel = searchParams.get("EQ_trainingLevel");
		if (trainingLevel != null && StringUtils.isNotBlank((String)trainingLevel)) {
			sb.append(" and s.PYCC = :trainingLevel ");
			map.put("trainingLevel", trainingLevel);
		}
		
		sb.append(" order by b.STUDY_YEAR_CODE desc");

		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}
	
	/**
	 * 查询批次集合
	 * @return
	 */
	public Map<String, String> getGraduationBatchMap(String orgId) {
		String sql = "select BATCH_ID as id, BATCH_NAME as name from GJT_GRADUATION_BATCH where IS_DELETED = 'N'";

		if (orgId != null && !"".equals(orgId)) {
			sql += " and ORG_ID in (select ID from GJT_ORG start with ID = '" + orgId + "' connect by prior ID = PERENT_ID AND PERENT_ID != ID )";
		}
		
		return commonDao.getMapNative(sql);
	}
	
	/**
	 * 查询毕业学员申请
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryGraduationApply(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     s.STUDENT_ID as \"studentId\",")
			.append("     s.XM as \"studentName\",")
			.append("     s.XH as \"studentCode\",")
			.append("     s2.SJH as \"phone\",")
			.append("     s2.DZXX as \"email\",")
			.append("     s.GRADE_NAME as \"grade\",")
			.append("     d.NAME as \"trainingLevel\",")
			.append("     s.ZYMC as \"specialtyName\",")
			.append("     a1.APPLY_ID as \"applyId1\",")
			.append("     a1.BATCH_ID as \"batchId1\",")
			.append("     b1.BATCH_CODE as \"batchCode1\",")
			.append("     b1.BATCH_NAME as \"batchName1\",")
			.append("     a1.APPLY_TYPE as \"applyType1\",")
			.append("     a1.APPLY_DEGREE as \"applyDegree1\",")
			.append("     a1.NEED_DEFENCE as \"needDefence1\",")
			.append("     a1.GUIDE_TEACHER as \"guideTeacher1\",")
			.append("     e1.XM as \"guideTeacherName1\",")
			.append("     a1.DEFENCE_TEACHER as \"defenceTeacher1\",")
			.append("     e2.XM as \"defenceTeacherName1\",")
			.append("     a1.REVIEW_SCORE as \"reviewScore1\",")
			.append("     a1.DEFENCE_SCORE as \"defenceScore1\",")
			.append("     a1.STATUS as \"status1\",")
			.append("     a2.APPLY_ID as \"applyId2\",")
			.append("     a2.BATCH_ID as \"batchId2\",")
			.append("     b2.BATCH_CODE as \"batchCode2\",")
			.append("     b2.BATCH_NAME as \"batchName2\",")
			.append("     a2.APPLY_TYPE as \"applyType2\",")
			.append("     a2.APPLY_DEGREE as \"applyDegree2\",")
			.append("     a2.NEED_DEFENCE as \"needDefence2\",")
			.append("     a2.GUIDE_TEACHER as \"guideTeacher2\",")
			.append("     e3.XM as \"guideTeacherName2\",")
			.append("     a2.REVIEW_SCORE as \"reviewScore2\",")
			.append("     a2.DEFENCE_SCORE as \"defenceScore2\",")
			.append("     a2.STATUS as \"status2\"")
			.append(" from ")
			.append("      VIEW_STUDENT_INFO s,")
			.append("      TBL_SYS_DATA d,")
			.append("      GJT_STUDENT_INFO s2")
			.append(" left join ")
			.append("      GJT_GRADUATION_APPLY a1 ")
			.append(" on a1.STUDENT_ID = s2.STUDENT_ID and a1.IS_DELETED = 'N' and a1.APPLY_TYPE = 1")
			.append(" left join ")
			.append("      GJT_GRADUATION_BATCH b1 ")
			.append(" on b1.BATCH_ID = a1.BATCH_ID")
			.append(" left join ")
			.append("      GJT_EMPLOYEE_INFO e1 ")
			.append(" on e1.EMPLOYEE_ID = a1.GUIDE_TEACHER")
			.append(" left join ")
			.append("      GJT_EMPLOYEE_INFO e2 ")
			.append(" on e2.EMPLOYEE_ID = a1.DEFENCE_TEACHER")
			.append(" left join ")
			.append("       GJT_GRADUATION_APPLY a2 ")
			.append(" on a2.STUDENT_ID = s2.STUDENT_ID and a2.IS_DELETED='N' and a2.APPLY_TYPE=2")
			.append(" left join ")
			.append("       GJT_GRADUATION_BATCH b2 ")
			.append(" on b2.BATCH_ID = a2.BATCH_ID")
			.append(" left join ")
			.append("       GJT_EMPLOYEE_INFO e3 ")
			.append(" on e3.EMPLOYEE_ID = a2.GUIDE_TEACHER")
			.append(" where ")
			.append("      s.PYCC = d.CODE and d.TYPE_CODE = 'TrainingLevel' ")
			.append("      and s2.STUDENT_ID = s.STUDENT_ID ")
			.append("      and s2.STUDENT_ID in ( ")
			.append("                             select ")
			.append("                                   a.STUDENT_ID ")
			.append("                             from ")
			.append("                                   GJT_GRADUATION_APPLY a, ")
			.append("                                   GJT_GRADUATION_BATCH b ")
			.append("                             where ")
			.append("                                   a.IS_DELETED='N' ")
			.append("                                   and a.BATCH_ID = b.BATCH_ID ")
			.append("                                   and b.IS_DELETED = 'N' ");

		Object batchId = searchParams.get("EQ_batchId");
		if (batchId != null && StringUtils.isNotBlank((String)batchId)) {
			sb.append(" and b.BATCH_ID = :batchId ");
			map.put("batchId", batchId);
		}
		
		sb.append("                           ) ");

		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null && StringUtils.isNotBlank((String)orgId)) {
			sb.append(" and s2.ORG_ID in (select ID from GJT_ORG start with ID = :orgId connect by prior ID = PERENT_ID AND PERENT_ID != ID ) ");
			map.put("orgId", orgId);
		}
		
		Object classId = searchParams.get("EQ_classId");
		if (classId != null) {
			sb.append(" and exists (select 1 from GJT_CLASS_STUDENT gcs where gcs.IS_DELETED = 'N' and s.STUDENT_ID = gcs.STUDENT_ID and  gcs.CLASS_ID = :classId ) ");
			map.put("classId", classId);
		}
		
		Object studentName = searchParams.get("LIKE_xm");
		if (studentName != null && StringUtils.isNotBlank((String)studentName)) {
			sb.append(" and s.XM like :studentName ");
			map.put("studentName", "%" + studentName + "%");
		}
		
		Object studentCode = searchParams.get("LIKE_xh");
		if (studentCode != null && StringUtils.isNotBlank((String)studentCode)) {
			sb.append(" and s.XH like :studentCode ");
			map.put("studentCode", "%" + studentCode + "%");
		}

		if (batchId != null && StringUtils.isNotBlank((String)batchId)) {
			sb.append(" and (b1.BATCH_ID = :batchId or b1.BATCH_ID is null) ");
			sb.append(" and (b2.BATCH_ID = :batchId or b2.BATCH_ID is null) ");
		}

		Object specialtyId = searchParams.get("EQ_specialtyId");
		if (specialtyId != null && StringUtils.isNotBlank((String)specialtyId)) {
			sb.append(" and s.MAJOR = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}
		
		Object status1 = searchParams.get("EQ_status1");
		if (status1 != null && StringUtils.isNotBlank((String)status1)) {
			if ("-1".equals((String)status1)) {
				sb.append(" and a1.STATUS is null ");
			} else {
				sb.append(" and a1.STATUS = :status1 ");
				map.put("status1", status1);
			}
		}

		Object needDefence = searchParams.get("EQ_needDefence");
		if (needDefence != null && StringUtils.isNotBlank((String)needDefence)) {
			sb.append(" and a1.NEED_DEFENCE = :needDefence ");
			map.put("needDefence", needDefence);
		}

		Object status2 = searchParams.get("EQ_status2");
		if (status2 != null && StringUtils.isNotBlank((String)status2)) {
			if ("-1".equals((String)status2)) {
				sb.append(" and a2.STATUS is null ");
			} else {
				sb.append(" and a2.STATUS = :status2 ");
				map.put("status2", status2);
			}
		}

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId != null && StringUtils.isNotBlank((String)gradeId)) {
			sb.append(" and s.GRADE_ID = :gradeId ");
			map.put("gradeId", gradeId);
		}

		Object trainingLevel = searchParams.get("EQ_pycc");
		if (trainingLevel != null && StringUtils.isNotBlank((String)trainingLevel)) {
			sb.append(" and s.PYCC = :trainingLevel ");
			map.put("trainingLevel", trainingLevel);
		}

		Object applyDegree = searchParams.get("EQ_applyDegree");
		if (applyDegree != null && StringUtils.isNotBlank((String)applyDegree)) {
			sb.append(" and a1.APPLY_DEGREE = :applyDegree ");
			map.put("applyDegree", applyDegree);
		}

		Object isConfirm = searchParams.get("EQ_isConfirm");
		if (isConfirm != null && StringUtils.isNotBlank((String)isConfirm)) {
			if ("0".equals(isConfirm)) {
				sb.append(" and a1.STATUS < 7 ");
			} else if ("1".equals(isConfirm)) {
				sb.append(" and a1.STATUS > 6 ");
			}
		}
		
		sb.append(" order by s.XM");

		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}
	
	/**
	 * 查询指导老师信息
	 * @param adviserType
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryGraduationAdviser(int adviserType, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     b.BATCH_ID as \"batchId\",")
			.append("     b.BATCH_CODE as \"batchCode\",")
			.append("     b.BATCH_NAME as \"batchName\",")
			.append("     a.TEACHER_ID as \"teacherId\",")
			.append("     ua.LOGIN_ACCOUNT as \"loginAccount\",")
			.append("     e.XM as \"teacherName\",")
			.append("     wmsys.wm_concat(gs.ZYMC) as \"specialtyNames\"")
			.append(" from ")
			.append("      GJT_GRADUATION_BATCH b,")
			.append("      GJT_GRADUATION_SPECIALTY s,")
			.append("      GJT_GRADUATION_ADVISER a,")
			.append("      GJT_EMPLOYEE_INFO e,")
			.append("      GJT_USER_ACCOUNT ua,")
			.append("      GJT_SPECIALTY gs")
			.append(" where ")
			.append("      b.IS_DELETED = 'N' ")
			.append("      and b.BATCH_ID = s.BATCH_ID ")
			.append("      and s.IS_DELETED = 'N' ")
			.append("      and s.SETTING_ID = a.SETTING_ID ")
			.append("      and a.ADVISER_TYPE = :adviserType ")
			.append("      and a.TEACHER_ID = e.EMPLOYEE_ID ")
			.append("      and e.ACCOUNT_ID = ua.ID ")
			.append("      and s.SPECIALTY_ID = gs.SPECIALTY_ID ");
		
		map.put("adviserType", adviserType);
		
		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null && StringUtils.isNotBlank((String)orgId)) {
			sb.append(" and b.ORG_ID = :orgId ");
			map.put("orgId", orgId);
		}
		
		Object loginAccount = searchParams.get("LIKE_loginAccount");
		if (loginAccount != null && StringUtils.isNotBlank((String)loginAccount)) {
			sb.append(" and ua.LOGIN_ACCOUNT like :loginAccount ");
			map.put("loginAccount", "%" + loginAccount + "%");
		}
		
		Object teacherName = searchParams.get("LIKE_teacherName");
		if (teacherName != null && StringUtils.isNotBlank((String)teacherName)) {
			sb.append(" and e.XM like :teacherName ");
			map.put("teacherName", "%" + teacherName + "%");
		}
		
		Object batchId = searchParams.get("EQ_batchId");
		if (batchId != null && StringUtils.isNotBlank((String)batchId)) {
			sb.append(" and b.BATCH_ID = :batchId ");
			map.put("batchId", batchId);
		}

		Object specialtyId = searchParams.get("EQ_specialtyId");
		if (specialtyId != null && StringUtils.isNotBlank((String)specialtyId)) {
			sb.append(" and s.SPECIALTY_ID = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}
		
		sb.append(" group by b.BATCH_ID, b.BATCH_CODE, b.BATCH_NAME, a.TEACHER_ID, ua.LOGIN_ACCOUNT, e.XM")
			.append(" order by e.XM");

		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}
	
	/**
	 * 查询申请学员总数：totalCount-总数，completedCount-已通过数量，noCompletedCount-未通过数量
	 * @param batchId
	 * @param applyType 申请类型：1-毕业论文，2-社会实践
	 * @param teacherType  指导老师类型：1-指导老师，2-答辩老师
	 * @param teacherId
	 * @return
	 */
	public Map<String, Object> queryGraduationApplyCount(String batchId, int applyType, int teacherType, String teacherId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     count(1) as \"totalCount\",")
			.append("     nvl(sum(case when t.STATUS = :completedStatus then 1 else 0 end), 0) as \"completedCount\",")
			.append("     nvl(sum(case when t.STATUS <> :completedStatus then 1 else 0 end), 0) as \"noCompletedCount\"")
			.append(" from ")
			.append("      GJT_GRADUATION_APPLY t")
			.append(" where ")
			.append("      t.IS_DELETED = 'N' ")
			.append("      and t.BATCH_ID = :batchId ")
			.append("      and t.APPLY_TYPE = :applyType ");
		
		if (teacherType == 1) {
			sb.append(" and t.GUIDE_TEACHER = :teacherId");
		} else if (teacherType == 2) {
			sb.append(" and t.DEFENCE_TEACHER = :teacherId");
		}
		
		if (applyType == 1) {
			map.put("completedStatus", GraduationApplyStatusEnum.THESIS_COMPLETED.getValue());
		} else if (applyType == 2) {
			map.put("completedStatus", GraduationApplyStatusEnum.PRACTICE_COMPLETED.getValue());
		}
		
		map.put("batchId", batchId);
		map.put("applyType", applyType);
		map.put("teacherId", teacherId);
		
		return commonDao.queryObjectToMapNative(sb.toString(), map);
	}
	
	/**
	 * 查询学生的模块学分获得详情
	 * @param studentId
	 * @return
	 */
	public List<Object[]> queryModuleScore(String studentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     tsd.NAME as \"moduleName\",")
			.append("     gc.KCMC as \"courseName\",")
			.append("     gtp.XF as \"xf\",")
			.append("     grr.EXAM_SCORE2 as \"score\",")
			.append("     gsml.SCORE as \"limitScore\"")
			.append(" from ")
			.append("      VIEW_STUDENT_INFO vsi")
			.append(" inner join ")
			.append("      VIEW_TEACH_PLAN gtp")
			.append("      on ")
			.append("      vsi.GRADE_ID = gtp.GRADE_ID")
			.append("      and vsi.PYCC = gtp.PYCC")
			.append("      and vsi.MAJOR = gtp.KKZY")
			.append("      and gtp.IS_DELETED = 'N'")
			.append(" inner join ")
			.append("      GJT_COURSE gc")
			.append("      on ")
			.append("      gtp.COURSE_ID = gc.COURSE_ID")
			.append("      and gc.IS_DELETED = 'N'")
			.append(" inner join ")
			.append("       TBL_SYS_DATA tsd")
			.append("      on ")
			.append("      tsd.TYPE_CODE = 'CourseType'")
			.append("      and tsd.CODE = gtp.KCLBM")
			.append(" left join ")
			.append("       GJT_REC_RESULT grr")
			.append("      on ")
			.append("      grr.STUDENT_ID = vsi.STUDENT_ID")
			.append("      and grr.COURSE_ID = gtp.COURSE_ID")
			.append(" left join ")
			.append("       GJT_SPECIALTY_MODULE_LIMIT gsml")
			.append("      on ")
			.append("      gsml.SPECIALTY_ID = vsi.MAJOR")
			.append("      and gsml.MODULE_ID = tsd.id")
			.append(" where ")
			.append("      vsi.STUDENT_ID = :studentId ")
			.append(" order by gtp.KCLBM ");
		
		map.put("studentId", studentId);
		
		return commonDao.queryForObjectListNative(sb.toString(), map);
	}
	
	/**
	 * 查询学生的必修课程平均分
	 * @param studentId
	 * @return
	 */
	public float queryCompulsorySumScore(String studentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     avg(case when grr.EXAM_SCORE2 is null then 0 else grr.EXAM_SCORE2 end) as \"score\" ")
			.append(" from ")
			.append("      VIEW_STUDENT_INFO vsi")
			.append(" inner join ")
			.append("      VIEW_TEACH_PLAN gtp")
			.append("      on ")
			.append("      vsi.GRADE_ID = gtp.GRADE_ID")
			.append("      and vsi.PYCC = gtp.PYCC")
			.append("      and vsi.MAJOR = gtp.KKZY")
			.append("      and gtp.KCSX = 1")
			.append("      and gtp.IS_DELETED = 'N'")
			.append(" left join ")
			.append("       GJT_REC_RESULT grr")
			.append("      on ")
			.append("      grr.STUDENT_ID = vsi.STUDENT_ID")
			.append("      and grr.COURSE_ID = gtp.COURSE_ID")
			.append(" where ")
			.append("      vsi.STUDENT_ID = :studentId ");
		
		map.put("studentId", studentId);

		float score = 0;
		Object object = commonDao.queryObjectNative(sb.toString(), map);
		if (object != null) {
			score = ((BigDecimal)object).floatValue();
		}
		
		return score;
	}
	
	/**
	 * 查询学生的其它课程平均分
	 * @param studentId
	 * @return
	 */
	public float queryOtherSumScore(String studentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     avg(case when grr.EXAM_SCORE2 is null then 0 else grr.EXAM_SCORE2 end) as \"score\" ")
			.append(" from ")
			.append("      VIEW_STUDENT_INFO vsi")
			.append(" inner join ")
			.append("      VIEW_TEACH_PLAN gtp")
			.append("      on ")
			.append("      vsi.GRADE_ID = gtp.GRADE_ID")
			.append("      and vsi.PYCC = gtp.PYCC")
			.append("      and vsi.MAJOR = gtp.KKZY")
			.append("      and gtp.KCSX != 1")
			.append("      and gtp.IS_DELETED = 'N'")
			.append(" left join ")
			.append("       GJT_REC_RESULT grr")
			.append("      on ")
			.append("      grr.STUDENT_ID = vsi.STUDENT_ID")
			.append("      and grr.COURSE_ID = gtp.COURSE_ID")
			.append(" where ")
			.append("      vsi.STUDENT_ID = :studentId ");
		
		map.put("studentId", studentId);

		float score = 0;
		Object object = commonDao.queryObjectNative(sb.toString(), map);
		if (object != null) {
			score = ((BigDecimal)object).floatValue();
		}
		
		return score;
	}
	
	/**
	 * 查询学生的学位课程分数
	 * @param studentId
	 * @return
	 */
	public float queryDegreeScore(String studentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     grr.EXAM_SCORE2 as \"score\" ")
			.append(" from ")
			.append("      VIEW_STUDENT_INFO vsi")
			.append(" inner join ")
			.append("      VIEW_TEACH_PLAN gtp")
			.append("      on ")
			.append("      vsi.GRADE_ID = gtp.GRADE_ID")
			.append("      and vsi.PYCC = gtp.PYCC")
			.append("      and vsi.MAJOR = gtp.KKZY")
			.append("      and gtp.COURSE_TYPE = 3")
			.append("      and gtp.IS_DELETED = 'N'")
			.append(" left join ")
			.append("       GJT_REC_RESULT grr")
			.append("      on ")
			.append("      grr.STUDENT_ID = vsi.STUDENT_ID")
			.append("      and grr.COURSE_ID = gtp.COURSE_ID")
			.append(" where ")
			.append("      vsi.STUDENT_ID = :studentId ");
		
		map.put("studentId", studentId);

		float score = 0;
		Object object = commonDao.queryObjectNative(sb.toString(), map);
		if (object != null) {
			score = ((BigDecimal)object).floatValue();
		}
		
		return score;
	}
	
	/**
	 * 查询学生的毕业设计分数
	 * @param studentId
	 * @return
	 */
	public float queryDesignScore(String studentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     grr.EXAM_SCORE2 as \"score\" ")
			.append(" from ")
			.append("      VIEW_STUDENT_INFO vsi")
			.append(" inner join ")
			.append("      VIEW_TEACH_PLAN gtp")
			.append("      on ")
			.append("      vsi.GRADE_ID = gtp.GRADE_ID")
			.append("      and vsi.PYCC = gtp.PYCC")
			.append("      and vsi.MAJOR = gtp.KKZY")
			.append("      and gtp.COURSE_TYPE = 1")
			.append("      and gtp.IS_DELETED = 'N'")
			.append(" left join ")
			.append("       GJT_REC_RESULT grr")
			.append("      on ")
			.append("      grr.STUDENT_ID = vsi.STUDENT_ID")
			.append("      and grr.COURSE_ID = gtp.COURSE_ID")
			.append(" where ")
			.append("      vsi.STUDENT_ID = :studentId ");
		
		map.put("studentId", studentId);

		float score = 0;
		Object object = commonDao.queryObjectNative(sb.toString(), map);
		if (object != null) {
			score = ((BigDecimal)object).floatValue();
		}
		
		return score;
	}
	
	/**
	 * 查询学生的毕业登记信息
	 * @param studentId
	 * @return
	 */
	public Map<String, Object> queryStudentRegisterMsg(String studentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     gsi.XM as \"studentName\", ")
			.append("     gsi.XH as \"studentCode\", ")
			.append("     nvl(to_char(to_date(gsi.CSRQ, 'YYYYMMDD'), 'YYYY\"年\"MM\"月\"DD\"日\"'), ' ') as \"birthday\", ")
			.append("     nvl(d1.NAME, ' ') as \"sex\", ")
			.append("     nvl(d2.NAME, ' ') as \"nations\", ")
			.append("     '身份证' as \"credentialsType\", ")
			.append("     nvl(gsi.SFZH, ' ') as \"credentialsNo\", ")
			.append("     nvl(gsi.POLITICSSTATUS, ' ') as \"politicalStatus\", ")
			.append("     nvl(d3.NAME, ' ') as \"studentType\", ")
			.append("     nvl(d4.NAME, ' ') as \"trainingLevel\", ")
			.append("     nvl(gs.ZYMC, ' ') as \"specialty\", ")
			.append("     nvl(vsi.GRADE_NAME, ' ') as \"grade\", ")
			.append("     nvl(r.COMPANY, ' ') as \"company\", ")
			.append("     nvl(r.COMPANY_PHONE, ' ') as \"companyPhone\", ")
			.append("     nvl(r.PHONE, ' ') as \"phone\", ")
			.append("     nvl(r.HOME_PHONE, ' ') as \"homePhone\", ")
			.append("     nvl(r.EMAIL, ' ') as \"email\", ")
			.append("     nvl(r.PRACTICE_CONTENT, ' ') as \"practiceContent\", ")
			.append("     nvl(r.GRADUATION_DESIGN, ' ') as \"graduationDesign\", ")
			.append("     nvl(r.AWARD_RECORD, ' ') as \"awardRecord\", ")
			.append("     nvl(r.EVALUATION, ' ') as \"evaluation\", ")
			.append("     nvl(r.PHOTO, ' ') as \"photo\" ")
			.append(" from ")
			.append("      GJT_STUDENT_INFO gsi")
			.append(" inner join ")
			.append("      GJT_SPECIALTY gs")
			.append("      on ")
			.append("      gs.SPECIALTY_ID = gsi.MAJOR")
			.append(" inner join ")
			.append("      VIEW_STUDENT_INFO vsi")
			.append("      on ")
			.append("      vsi.STUDENT_ID = gsi.STUDENT_ID")
			.append(" inner join ")
			.append("       GJT_GRADUATION_REGISTER r")
			.append("      on ")
			.append("      r.IS_DELETED = 'N'")
			.append("      and r.STUDENT_ID = gsi.STUDENT_ID")
			.append(" left join ")
			.append("       TBL_SYS_DATA d1")
			.append("      on ")
			.append("      d1.TYPE_CODE = 'Sex'")
			.append("      and d1.CODE = gsi.XBM")
			.append(" left join ")
			.append("       TBL_SYS_DATA d2")
			.append("      on ")
			.append("      d2.TYPE_CODE = 'NationalityCode'")
			.append("      and d2.CODE = gsi.MZM")
			.append(" left join ")
			.append("       TBL_SYS_DATA d3")
			.append("      on ")
			.append("      d3.TYPE_CODE = 'SPECIALTY_CATEGORY'")
			.append("      and d3.CODE = gs.SPECIALTY_CATEGORY")
			.append(" left join ")
			.append("       TBL_SYS_DATA d4")
			.append("      on ")
			.append("      d4.TYPE_CODE = 'TrainingLevel'")
			.append("      and d4.CODE = gsi.PYCC")
			.append(" where ")
			.append("      gsi.STUDENT_ID = :studentId ");
		
		map.put("studentId", studentId);

		return commonDao.queryObjectToMapNative(sb.toString(), map);
	}

}
