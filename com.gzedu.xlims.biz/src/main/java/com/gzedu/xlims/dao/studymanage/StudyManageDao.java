package com.gzedu.xlims.dao.studymanage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.GjtOrg;

@Repository
public class StudyManageDao {
	
	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	/**
	 * 学习管理=》成绩查询sql处理方法
	 *
	 * @param searchParams
	 * @return
	 */
	private Map<String, Object> getScoreListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  SELECT * FROM (");
		sql.append("  SELECT ");
		sql.append(
				"  	  GSI.STUDENT_ID,GSI.XH,GSI.XM,GSI.SFZH,GSI.USER_TYPE,GSI.SJH,GSI.PYCC,GSI.AVATAR ZP,gsc.SC_NAME,GY.NAME YEAR_NAME,GTP.SOURCE_KCH,GTP.SOURCE_KCMC,GTP.KSFS,");
		sql.append(
				"  	  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append(
				"  	  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GTP.KSFS AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME,");
		sql.append(
				"  	  GRE.GRADE_ID,GRE.GRADE_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,GTP.KSDW,GTP.KKXQ,GTP.COURSE_ID,GTP.KCMC,GTP.KCH,NVL(GTP.XF, 0) XF,NVL(TO_CHAR(GRR.COURSE_SCHEDULE),'') KCXXBZ,");
		sql.append(
				"  	  NVL(GTP.KCKSBZ, 0) KCKSBZ,GRR.EXAM_SCORE,GRR.EXAM_SCORE1,GRR.EXAM_SCORE2,GRR.EXAM_STATE,GRR.GET_CREDITS,");
        sql.append("  (NVL (LUO.ONLINE_COUNT, 0) + NVL (LUO.ONLINE_COUNT_MOBILE, 0)) STUDY_TIMES,ROUND((NVL(LUO.ONLINE_TIME, 0) + NVL(LUO.ONLINE_TIME_MOBILE, 0))/60,1) ONLINE_TIME,NVL (LUD.MY_PROGRESS, 0) PROGRESS,NVL(LUD.STATE, '0') STATE,");
		sql.append("  (CASE WHEN NVL(LUD.STATE, '0')='0' THEN '未学习' WHEN NVL(LUD.STATE, '0')='1' THEN '落后' WHEN NVL(LUD.STATE, '0')='2' THEN '正常' WHEN NVL(LUD.STATE, '0')='3' THEN '学霸' WHEN NVL(LUD.STATE, '0')='4' THEN '考核通过' ELSE '--' END ) STATE_NAME,");
		sql.append("  	  (SELECT gci.BJMC FROM GJT_CLASS_INFO gci,GJT_CLASS_STUDENT gcs ");
		sql.append(
				"  	  	WHERE gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='teach' AND ROWNUM=1) BJMC,");
		sql.append(
				"  	  (SELECT gei.XM FROM GJT_CLASS_INFO gci LEFT JOIN GJT_EMPLOYEE_INFO gei ON gci.BZR_ID=gei.EMPLOYEE_ID, GJT_CLASS_STUDENT gcs ");
		sql.append(
				"  	  	WHERE gei.EMPLOYEE_TYPE='1' AND  gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='teach' AND ROWNUM=1) BZR_NAME,");
		sql.append(
				"  	  (SELECT gei.XM FROM GJT_CLASS_INFO gci LEFT JOIN GJT_EMPLOYEE_INFO gei ON gci.BZR_ID=gei.EMPLOYEE_ID, GJT_CLASS_STUDENT gcs ");
		sql.append(
				"  	  	WHERE gei.EMPLOYEE_TYPE='2' AND  gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='course' AND gci.COURSE_ID=GTP.COURSE_ID  AND ROWNUM=1) FD_NAME,");
		sql.append(
				"  	  TO_CHAR((SELECT (CASE  WHEN GGR.START_DATE > SYSDATE THEN 0 ELSE 1 END) FROM GJT_GRADE GGR WHERE GGR.IS_DELETED = 'N' AND GGR.GRADE_ID = GTP.ACTUAL_GRADE_ID)) STUDY_FLG,");
        sql.append("          (");
        sql.append("            select EXAM_NO from (");
        sql.append("                  select gea.appointment_id,gea.student_id,gea.rec_id,GEP.EXAM_NO");
        sql.append("                  from GJT_EXAM_APPOINTMENT_NEW GEA");
        sql.append("                  inner join Gjt_Exam_Plan_New GEP on GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
        sql.append("                  where GEA.IS_DELETED = 0");
        sql.append("                  order by gea.created_dt desc");
        sql.append("            ) xxx");
        sql.append("            where xxx.student_id = GRR.STUDENT_ID and xxx.rec_id = GRR.Rec_Id AND ROWNUM = 1");
        sql.append("          ) EXAM_NO,");
		sql.append("          (");
		sql.append("            select (select tsd.name from tbl_sys_data tsd where tsd.is_deleted='N' and tsd.type_code='ExaminationMode' and tsd.code=type) from (");
		sql.append("                  select gea.appointment_id,gea.student_id,gea.rec_id,GEP.type");
		sql.append("                  from GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("                  inner join Gjt_Exam_Plan_New GEP on GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("                  where GEA.IS_DELETED = 0");
		sql.append("                  order by gea.created_dt desc");
		sql.append("            ) xxx");
		sql.append("            where xxx.student_id = GRR.STUDENT_ID and xxx.rec_id = GRR.Rec_Id AND ROWNUM = 1");
		sql.append("          ) EXAM_PLAN_KSFS_NAME");
		sql.append("  	  FROM GJT_STUDENT_INFO GSI");
		sql.append("  	    INNER JOIN GJT_GRADE GRE ON GSI.NJ = GRE.GRADE_ID AND GRE.IS_DELETED = 'N'");
		sql.append("  	    INNER JOIN GJT_YEAR GY ON GRE.YEAR_ID = GY.GRADE_ID");
		sql.append("  	    INNER JOIN GJT_SPECIALTY GSY ON GSI.MAJOR = GSY.SPECIALTY_ID AND GSY.IS_DELETED = 'N'");
        sql.append("  	    INNER JOIN GJT_REC_RESULT GRR ON GSI.STUDENT_ID = GRR.STUDENT_ID AND GRR.IS_DELETED = 'N'");
		sql.append("  	    INNER JOIN VIEW_TEACH_PLAN GTP ON GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
        sql.append("        LEFT JOIN GJT_STUDY_CENTER gsc ON gsc.IS_DELETED = 'N' AND gsc.ID = gsi.XXZX_ID");
        sql.append("  	    LEFT JOIN LCMS_USER_ONLINETIME LUO ON GRR.REC_ID = LUO.CHOOSE_ID");
        sql.append("  	    LEFT JOIN LCMS_USER_DYNA LUD ON GRR.REC_ID = LUD.CHOOSE_ID AND LUD.ISDELETED = 'N'");
		sql.append("  	  WHERE GSI.IS_DELETED = 'N'");

		//限制教务班级学生
        String teachClassId = ObjectUtils.toString(searchParams.get("TEACH_CLASS_ID"));
        if(StringUtils.isNotEmpty(teachClassId)) {
			sql.append(" AND gsi.student_id IN (SELECT gsi1.student_id from GJT_STUDENT_INFO gsi1 INNER JOIN GJT_CLASS_STUDENT gcs1 ON gsi1.is_deleted = 'N' AND gsi1.student_id = gcs1.student_id INNER JOIN GJT_CLASS_INFO gci1 ON gcs1.is_deleted = 'N' AND gci1.is_deleted = 'N' AND gci1.class_id = gcs1.class_id AND gci1.class_id = :teachClassId)");
			params.put("teachClassId", teachClassId);
		}
        
		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GRR.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_TYPE")))) {
			sql.append("  AND GTP.KSFS = :EXAM_TYPE");
			params.put("EXAM_TYPE", ObjectUtils.toString(searchParams.get("EXAM_TYPE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			sql.append("  AND GRE.GRADE_ID = :NJ");
			params.put("NJ", ObjectUtils.toString(searchParams.get("NJ")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("YEARID")))) {
			sql.append("  AND GY.GRADE_ID = :YEARID");
			params.put("YEARID", ObjectUtils.toString(searchParams.get("YEARID")));
		}
		sql.append("  ) TAB WHERE 1 = 1");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
			if ("4".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND TAB.STUDY_FLG = 0");
			} else {
				sql.append("  AND TAB.STUDY_FLG = 1");
				sql.append("  AND TAB.EXAM_STATE = :EXAM_STATE");
				params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
			}
		}

		sql.append(" ORDER BY PROGRESS DESC");

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 学习管理=》成绩查询
	 *
	 * @return
	 */
	public Page getScoreList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getScoreListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 成绩管理=》成绩查询无分页
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, Object>> getScoreList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getScoreListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForStringObjectMapListNative(sql, params);
	}

	/**
	 * 学习管理=》成绩查询（查询条件统计项）
	 *
	 * @return
	 */
	public long getScoreCount(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getScoreListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	/**
	 * 学习管理=》学分查询
	 *
	 * @return
	 */
	public Page getCreditsList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCreditsSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql, params, pageRequst);
	}

	/**
	 * 学习管理=》学分查询
	 *
	 * @return
	 */
	public long getCreditsCount(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCreditsSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForCountNative(sql, params);
	}

	/**
	 * 学习管理=》学分查询 sql
	 * @param searchParams
	 * @return
	 */
	private Map<String, Object> getCreditsSqlHandler(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT GSI.STUDENT_ID,GSI.XM,GSI.XH,GSI.SJH,GGE.GRADE_ID,GGE.GRADE_CODE,GGE.GRADE_NAME,GSY.ZYMC,GSI.PYCC,GSI.AVATAR ZP,GYR.NAME YEAR_NAME,");
		sql.append(
				"  	 (SELECT TSD.NAME  FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append(" GSY.BXXF,GSY.XXXF,");
		sql.append(" NVL(GSY.ZDBYXF, 0) ZDBYXF,z.ZXF,z.SUM_CREDITS,z.COUNT_COURSE,z.PASS_COURSE_COUNT");
		sql.append(" FROM GJT_STUDENT_INFO GSI");
		sql.append(" inner join GJT_SPECIALTY    GSY on GSY.IS_DELETED = 'N' AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append(" inner join GJT_GRADE        GGE on GGE.IS_DELETED = 'N' AND GSI.NJ = GGE.GRADE_ID");
		sql.append(" inner join GJT_YEAR         GYR on GGE.YEAR_ID = GYR.GRADE_ID");
		sql.append(" left join (select GRR.student_id, NVL(SUM(NVL(GTP.XF, 0)), 0) ZXF,");
		sql.append(" 			NVL(SUM(CASE GRR.EXAM_STATE WHEN '1' THEN NVL(GTP.XF, 0) ELSE 0 END), 0) SUM_CREDITS,");
		sql.append(" 			COUNT(GRR.REC_ID) COUNT_COURSE,");
		sql.append(" 			COUNT(CASE GRR.EXAM_STATE WHEN '1' THEN GRR.REC_ID ELSE NULL END) PASS_COURSE_COUNT");
		sql.append("        from GJT_REC_RESULT GRR");
		sql.append(" 		inner join VIEW_TEACH_PLAN GTP on GTP.TEACH_PLAN_ID=GRR.Teach_Plan_Id");
		sql.append(" 		where GRR.IS_DELETED = 'N' group by GRR.student_id) z on z.student_id=GSI.student_id");
		sql.append(" WHERE GSI.IS_DELETED = 'N'");


		//限制教务班级学生
        String teachClassId = ObjectUtils.toString(searchParams.get("TEACH_CLASS_ID"));
        if(StringUtils.isNotEmpty(teachClassId)) {
        	sql.append(" AND gsi.student_id IN (SELECT gsi1.student_id from GJT_STUDENT_INFO gsi1 INNER JOIN GJT_CLASS_STUDENT gcs1 ON gsi1.is_deleted = 'N' AND gsi1.student_id = gcs1.student_id INNER JOIN GJT_CLASS_INFO gci1 ON gcs1.is_deleted = 'N' AND gci1.is_deleted = 'N' AND gci1.class_id = gcs1.class_id AND gci1.class_id = :teachClassId)");
			params.put("teachClassId", teachClassId);
		}
        
		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SFZH")))) {
			sql.append("  AND GSI.SFZH LIKE :SFZH");
			params.put("SFZH", "%" + ObjectUtils.toString(searchParams.get("SFZH")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			sql.append("  AND GSI.NJ = :NJ");
			params.put("NJ", ObjectUtils.toString(searchParams.get("NJ")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("YEARID")))) {
			sql.append("  AND GYR.GRADE_ID = :YEARID");
			params.put("YEARID", ObjectUtils.toString(searchParams.get("YEARID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MAJOR")))) {
			sql.append("  AND GSI.MAJOR = :MAJOR");
			params.put("MAJOR", ObjectUtils.toString(searchParams.get("MAJOR")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		if (ObjectUtils.toString(searchParams.get("SCORE_TYPE")).equals("1")) {
			sql.append("  AND z.SUM_CREDITS < NVL(GSY.ZDBYXF, 0)");
		} else if (ObjectUtils.toString(searchParams.get("SCORE_TYPE")).equals("2")) {
			sql.append("  AND z.SUM_CREDITS >= NVL(GSY.ZDBYXF, 0)");
		}

		sql.append(" ORDER BY z.SUM_CREDITS DESC NULLS LAST");

		Map result = new HashMap();
		result.put("params", params);
		result.put("sql", sql.toString());
		return result;
	}

	/**
	 * 学习管理=》课程学情sql处理
	 *
	 * @return
	 */
	public Map<String, Object> getCourseStudyListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");

		sql.append("  SELECT");
		sql.append(
				"  	COURSE_ID,KCH,SDATE,EDATE,TIME_FLG,GRADE_ID,GRADE_NAME,KCMC,REC_COUNT,SUM_SCHEDULE,SUM_STUDY_SCORE,SUM_EXAM_SCORE,SUM_TOTAL_SCORE,");
		sql.append(
				"  	SUM_LOGIN_COUNT,SUM_LOGIN_TIME,SUM_PASS_COUNT,SUM_REGISTER_COUNT,SUM_UNPASS_COUNT,AVG_SCHEDULE,AVG_STUDY_SCORE,AVG_EXAM_SCORE,AVG_TOTAL_SCORE,AVG_LOGIN_COUNT,");
		sql.append(
				"  	AVG_LOGIN_TIME,AVG_PASS_COUNT,AVG_REGISTER_COUNT,AVG_UNPASS_COUNT,NEVER_STUDY_PERCENT,SUM_NEVER_STUDY,SUM_STUDY_IMG,STUDY_IMG_PERCENT");
		sql.append("  FROM (");
		sql.append("  		SELECT gtp.TERMCOURSE_ID,");
		sql.append("  			GCE.COURSE_ID,");
		sql.append("  			GCE.KCH,");
		sql.append("  			TO_CHAR(gre.START_DATE, 'yyyy-mm-dd') SDATE,");
		sql.append("  			TO_CHAR(gre.END_DATE, 'yyyy-mm-dd') EDATE,");
		sql.append("  			GRE.GRADE_ID,");
		sql.append("  			GRE.GRADE_NAME,");
		sql.append("  			GCE.KCMC,");
		sql.append("  			(CASE WHEN SYSDATE >= gre.START_DATE AND SYSDATE <= gre.END_DATE THEN '1'");
		sql.append("  	WHEN SYSDATE < gre.START_DATE THEN '2'");
		sql.append("  	WHEN SYSDATE > gre.END_DATE THEN '3'");
		sql.append("  	ELSE '0' END) TIME_FLG,");
		sql.append("  			z.REC_COUNT,z.SUM_SCHEDULE,z.SUM_STUDY_SCORE,z.SUM_EXAM_SCORE,z.SUM_TOTAL_SCORE,z.SUM_LOGIN_COUNT,z.SUM_LOGIN_TIME,");
		sql.append("  			z.SUM_NEVER_STUDY,z.SUM_STUDY_IMG,z.SUM_PASS_COUNT,z.SUM_REGISTER_COUNT,z.SUM_UNPASS_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_SCHEDULE / REC_COUNT)");
		sql.append("  	ELSE 0 END) AVG_SCHEDULE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_STUDY_SCORE / REC_COUNT)");
		sql.append("  	ELSE 0 END) AVG_STUDY_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_LOGIN_COUNT / REC_COUNT)");
		sql.append("  	ELSE 0 END) AVG_LOGIN_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND(SUM_LOGIN_TIME / REC_COUNT, 1)");
		sql.append("  	ELSE 0 END) AVG_LOGIN_TIME,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_PASS_COUNT / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) AVG_PASS_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_REGISTER_COUNT / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) AVG_REGISTER_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_UNPASS_COUNT / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) AVG_UNPASS_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR((SUM_EXAM_SCORE / REC_COUNT))");
		sql.append("  	ELSE 0 END) AVG_EXAM_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR((SUM_TOTAL_SCORE / REC_COUNT))");
		sql.append("  	ELSE 0 END) AVG_TOTAL_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_NEVER_STUDY / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) NEVER_STUDY_PERCENT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_STUDY_IMG / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) STUDY_IMG_PERCENT");
		sql.append("  	FROM GJT_TERM_COURSEINFO GTP");
		sql.append("  	INNER JOIN GJT_GRADE           GRE ON GRE.IS_DELETED = 'N' AND GTP.TERM_ID = GRE.GRADE_ID");
		sql.append("  	INNER JOIN GJT_COURSE          GCE ON GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  	left join (select GRR.TERMCOURSE_ID, COUNT(GRR.REC_ID) REC_COUNT,");
		sql.append("  			NVL(SUM(NVL(VSS.PROGRESS, 0)), 0) SUM_SCHEDULE,");
		sql.append("  			NVL(SUM(NVL(GRR.EXAM_SCORE, 0)), 0) SUM_STUDY_SCORE,");
		sql.append("  			NVL(SUM(NVL(GRR.EXAM_SCORE1, 0)), 0) SUM_EXAM_SCORE,");
		sql.append("  			NVL(SUM(NVL(GRR.EXAM_SCORE2, 0)), 0) SUM_TOTAL_SCORE,");
		sql.append("  			NVL(SUM(NVL(VSS.LOGIN_TIMES, 0)), 0) SUM_LOGIN_COUNT,");
		sql.append("  			ROUND(NVL(SUM(NVL(VSS.ONLINE_TIME, 0)), 0) / 60, 1) SUM_LOGIN_TIME,");
		sql.append("  			COUNT(CASE WHEN VSS.EXAM_STATE = '2' AND VSS.LOGIN_TIMES = 0 THEN GRR.REC_ID ELSE NULL END) SUM_NEVER_STUDY,");
		sql.append("  			COUNT(CASE WHEN GRR.EXAM_STATE = '2' AND VSS.LOGIN_TIMES > 0 THEN GRR.REC_ID ELSE NULL END) SUM_STUDY_IMG,");
		sql.append("  			COUNT(CASE WHEN VSS.EXAM_STATE = '1' THEN GRR.REC_ID ELSE NULL END) SUM_PASS_COUNT,");
		sql.append("  			COUNT(CASE WHEN VSS.EXAM_STATE = '3' THEN GRR.REC_ID ELSE NULL END) SUM_REGISTER_COUNT,");
		sql.append("  			COUNT(CASE WHEN VSS.EXAM_STATE = '0' THEN GRR.REC_ID ELSE NULL END) SUM_UNPASS_COUNT");
		sql.append("  	from GJT_REC_RESULT GRR");
		sql.append("  	LEFT JOIN VIEW_STUDENT_STUDY_SITUATION VSS ON GRR.REC_ID = VSS.CHOOSE_ID");
		sql.append("  	LEFT JOIN GJT_STUDENT_INFO gsi ON gsi.is_deleted='N' AND grr.STUDENT_ID = gsi.STUDENT_ID");
		
		//限制教学班学员范围
		String teachClassId = ObjectUtils.toString(searchParams.get("TEACH_CLASS_ID"));
		if(StringUtils.isNotEmpty(teachClassId)) {
			sql.append(" INNER JOIN GJT_CLASS_STUDENT gcs ON gcs.is_deleted = 'N' AND gcs.student_id = gsi.student_id ");
			sql.append(" INNER JOIN GJT_CLASS_INFO gci ON gci.is_deleted = 'N' AND gci.class_id = gcs.class_id AND gci.class_id = :teachClassId");
			params.put("teachClassId", teachClassId);
			
		}
		
		
		sql.append("  	where GRR.IS_DELETED = 'N'");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}
		
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}
		sql.append("  	group by GRR.TERMCOURSE_ID) z on z.TERMCOURSE_ID=GTP.TERMCOURSE_ID");
		sql.append("  	WHERE GTP.IS_DELETED = 'N'");
		sql.append("    AND GTP.XX_ID = :xxId");
		params.put("xxId", xxIdParam);

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRE.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GRE.GRADE_NAME LIKE :GRADE_NAME");
			params.put("GRADE_NAME", "%" + ObjectUtils.toString(searchParams.get("GRADE_NAME")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND GCE.KCMC LIKE :KCMC");
			params.put("KCMC", "%" + ObjectUtils.toString(searchParams.get("KCMC")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_TEACHER")))) {
			sql.append("  AND GTP.CLASS_TEACHER = :CLASS_TEACHER");
			params.put("CLASS_TEACHER", ObjectUtils.toString(searchParams.get("CLASS_TEACHER")));
		}
		sql.append(" ) TAB WHERE 1 = 1");
		sql.append(" AND TAB.REC_COUNT > 0");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE > " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE < " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE >= " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE <= " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE = " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			}
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE > " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE < " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE >= " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE <= " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE = " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			}
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT > " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT < " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT >= " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT <= " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT = " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			}
		}

		String STUDY_PROGRESS_SORT = ObjectUtils.toString(searchParams.get("STUDY_PROGRESS_SORT"));
		String CHOOSE_COUNT_SORT = ObjectUtils.toString(searchParams.get("CHOOSE_COUNT_SORT"));
		String STUDY_COUNT_SORT = ObjectUtils.toString(searchParams.get("STUDY_COUNT_SORT"));
		String STUDY_TIMES_SORT = ObjectUtils.toString(searchParams.get("STUDY_TIMES_SORT"));
		String PASS_COUNT_SORT = ObjectUtils.toString(searchParams.get("PASS_COUNT_SORT"));
		String UNPASS_COUNT_SORT = ObjectUtils.toString(searchParams.get("UNPASS_COUNT_SORT"));
		String STUDYING_COUNT_SORT = ObjectUtils.toString(searchParams.get("STUDYING_COUNT_SORT"));
		String REGISTER_COUNT_SORT = ObjectUtils.toString(searchParams.get("REGISTER_COUNT_SORT"));
		String UNSTUDY_COUNT_SORT = ObjectUtils.toString(searchParams.get("UNSTUDY_COUNT_SORT"));
		// 平均学习进度
		boolean flag = false;
		if (EmptyUtils.isNotEmpty(STUDY_PROGRESS_SORT)) {
			sql.append(" ORDER BY AVG_SCHEDULE " + STUDY_PROGRESS_SORT);
		} else {
			if (EmptyUtils.isEmpty(STUDY_PROGRESS_SORT) && EmptyUtils.isEmpty(CHOOSE_COUNT_SORT)
					&& EmptyUtils.isEmpty(STUDY_COUNT_SORT) && EmptyUtils.isEmpty(STUDY_TIMES_SORT)
					&& EmptyUtils.isEmpty(PASS_COUNT_SORT) && EmptyUtils.isEmpty(UNPASS_COUNT_SORT)
					&& EmptyUtils.isEmpty(STUDYING_COUNT_SORT) && EmptyUtils.isEmpty(REGISTER_COUNT_SORT)
					&& EmptyUtils.isEmpty(UNSTUDY_COUNT_SORT)) {
				sql.append(" ORDER BY AVG_SCHEDULE DESC");
			} else {
				sql.append(" ORDER BY ");
				flag = true;
			}
		}
		// 选课人数排序
		if (EmptyUtils.isNotEmpty(CHOOSE_COUNT_SORT)) {
			if (flag) {
				sql.append(" REC_COUNT " + CHOOSE_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,REC_COUNT " + CHOOSE_COUNT_SORT);
			}
		}
		// 平均学习次数
		if (EmptyUtils.isNotEmpty(STUDY_COUNT_SORT)) {
			if (flag) {
				sql.append(" AVG_LOGIN_COUNT " + STUDY_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,AVG_LOGIN_COUNT " + STUDY_COUNT_SORT);
			}
		}
		// 平均学习时长
		if (EmptyUtils.isNotEmpty(STUDY_TIMES_SORT)) {
			if (flag) {
				sql.append(" AVG_LOGIN_TIME " + STUDY_TIMES_SORT);
				flag = false;
			} else {
				sql.append(" ,AVG_LOGIN_TIME " + STUDY_TIMES_SORT);
			}
		}
		// 已通过人数
		if (EmptyUtils.isNotEmpty(PASS_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_PASS_COUNT " + PASS_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_PASS_COUNT " + PASS_COUNT_SORT);
			}
		}
		// 未通过人数
		if (EmptyUtils.isNotEmpty(UNPASS_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_UNPASS_COUNT " + UNPASS_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_UNPASS_COUNT " + UNPASS_COUNT_SORT);
			}
		}
		// 学习中人数
		if (EmptyUtils.isNotEmpty(STUDYING_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_STUDY_IMG " + STUDYING_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_STUDY_IMG " + STUDYING_COUNT_SORT);
			}
		}
		// 登记中人数
		if (EmptyUtils.isNotEmpty(REGISTER_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_REGISTER_COUNT " + REGISTER_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_REGISTER_COUNT " + REGISTER_COUNT_SORT);
			}
		}
		// 未学习中人数
		if (EmptyUtils.isNotEmpty(UNSTUDY_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_NEVER_STUDY " + UNSTUDY_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_NEVER_STUDY " + UNSTUDY_COUNT_SORT);
			}
		}

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 学习管理=》课程学情
	 *
	 * @return
	 */
	public Page getCourseStudyList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseStudyListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 导出数据
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getCourseStudyList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseStudyListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql, params);
	}

	/**
	 * 学情分析--课程班学情sql处理方法
	 *
	 * @param searchParams
	 * @return
	 */
	private Map<String, Object> getCourseClassListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");

		sql.append("  SELECT * ");
		sql.append("  FROM (");
		sql.append("      SELECT gtp.TERMCOURSE_ID,");
		sql.append("        GCE.COURSE_ID,");
		sql.append("        GCE.KCH,");
		sql.append("        TO_CHAR(gre.START_DATE, 'yyyy-mm-dd') SDATE,");
		sql.append("        TO_CHAR(gre.END_DATE, 'yyyy-mm-dd') EDATE,");
		sql.append("        GRE.GRADE_ID,");
		sql.append("        GRE.GRADE_NAME,");
		sql.append("        GCE.KCMC,");
		sql.append("        GCI.CLASS_ID,GCI.BJMC,");
		sql.append("        (CASE WHEN SYSDATE >= gre.START_DATE AND SYSDATE <= gre.END_DATE THEN '1'");
		sql.append("    WHEN SYSDATE < gre.START_DATE THEN '2'");
		sql.append("    WHEN SYSDATE > gre.END_DATE THEN '3'");
		sql.append("    ELSE '0' END) TIME_FLG,");
		sql.append("        z.REC_COUNT,z.SUM_SCHEDULE,z.SUM_STUDY_SCORE,z.SUM_EXAM_SCORE,z.SUM_TOTAL_SCORE,z.SUM_LOGIN_COUNT,z.SUM_LOGIN_TIME,");
		sql.append("        z.SUM_NEVER_STUDY,z.SUM_STUDY_IMG,z.SUM_PASS_COUNT,z.SUM_REGISTER_COUNT,z.SUM_UNPASS_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_SCHEDULE / REC_COUNT)");
		sql.append("    ELSE 0 END) AVG_SCHEDULE,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_STUDY_SCORE / REC_COUNT)");
		sql.append("    ELSE 0 END) AVG_STUDY_SCORE,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_LOGIN_COUNT / REC_COUNT)");
		sql.append("    ELSE 0 END) AVG_LOGIN_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND(SUM_LOGIN_TIME / REC_COUNT, 1)");
		sql.append("    ELSE 0 END) AVG_LOGIN_TIME,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_PASS_COUNT / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) AVG_PASS_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_REGISTER_COUNT / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) AVG_REGISTER_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_UNPASS_COUNT / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) AVG_UNPASS_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR((SUM_EXAM_SCORE / REC_COUNT))");
		sql.append("    ELSE 0 END) AVG_EXAM_SCORE,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR((SUM_TOTAL_SCORE / REC_COUNT))");
		sql.append("    ELSE 0 END) AVG_TOTAL_SCORE,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_NEVER_STUDY / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) NEVER_STUDY_PERCENT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_STUDY_IMG / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) STUDY_IMG_PERCENT");
		sql.append("    FROM GJT_TERM_COURSEINFO GTP");
		sql.append("    INNER JOIN GJT_GRADE           GRE ON GRE.IS_DELETED = 'N' AND GTP.TERM_ID = GRE.GRADE_ID");
		sql.append("    INNER JOIN GJT_COURSE          GCE ON GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("    INNER JOIN GJT_CLASS_INFO      GCI ON GCI.IS_DELETED = 'N' AND GTP.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("    left join (select GRR.TERMCOURSE_ID, COUNT(GRR.REC_ID) REC_COUNT,");
		sql.append("        NVL(SUM(NVL(VSS.PROGRESS, 0)), 0) SUM_SCHEDULE,");
		sql.append("        NVL(SUM(NVL(GRR.EXAM_SCORE, 0)), 0) SUM_STUDY_SCORE,");
		sql.append("        NVL(SUM(NVL(GRR.EXAM_SCORE1, 0)), 0) SUM_EXAM_SCORE,");
		sql.append("        NVL(SUM(NVL(GRR.EXAM_SCORE2, 0)), 0) SUM_TOTAL_SCORE,");
		sql.append("        NVL(SUM(NVL(VSS.LOGIN_TIMES, 0)), 0) SUM_LOGIN_COUNT,");
		sql.append("        ROUND(NVL(SUM(NVL(VSS.ONLINE_TIME, 0)), 0) / 60, 1) SUM_LOGIN_TIME,");
		sql.append("        COUNT(CASE WHEN VSS.EXAM_STATE = '2' AND VSS.LOGIN_TIMES = 0 THEN GRR.REC_ID ELSE NULL END) SUM_NEVER_STUDY,");
		sql.append("        COUNT(CASE WHEN GRR.EXAM_STATE = '2' AND VSS.LOGIN_TIMES > 0 THEN GRR.REC_ID ELSE NULL END) SUM_STUDY_IMG,");
		sql.append("        COUNT(CASE WHEN VSS.EXAM_STATE = '1' THEN GRR.REC_ID ELSE NULL END) SUM_PASS_COUNT,");
		sql.append("        COUNT(CASE WHEN VSS.EXAM_STATE = '3' THEN GRR.REC_ID ELSE NULL END) SUM_REGISTER_COUNT,");
		sql.append("        COUNT(CASE WHEN VSS.EXAM_STATE = '0' THEN GRR.REC_ID ELSE NULL END) SUM_UNPASS_COUNT");
		sql.append("    from GJT_REC_RESULT GRR");
		sql.append("    LEFT JOIN VIEW_STUDENT_STUDY_SITUATION VSS ON GRR.REC_ID = VSS.CHOOSE_ID");
		sql.append("    LEFT JOIN GJT_STUDENT_INFO gsi ON gsi.is_deleted='N' AND grr.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("    where GRR.IS_DELETED = 'N'");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}
		sql.append("  	group by GRR.TERMCOURSE_ID) z on z.TERMCOURSE_ID=GTP.TERMCOURSE_ID");
		sql.append("  	WHERE GTP.IS_DELETED = 'N'");
		sql.append("    AND GTP.XX_ID = :xxId");
		params.put("xxId", xxIdParam);

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRE.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GRE.GRADE_NAME LIKE :GRADE_NAME");
			params.put("GRADE_NAME", "%" + ObjectUtils.toString(searchParams.get("GRADE_NAME")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND GCE.KCMC LIKE :KCMC");
			params.put("KCMC", "%" + ObjectUtils.toString(searchParams.get("KCMC")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		sql.append(" ) TAB WHERE 1 = 1");
		sql.append(" AND TAB.REC_COUNT > 0");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE > " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE < " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE >= " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE <= " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("SCORE_FLG")))) {
				sql.append("  AND TAB.AVG_STUDY_SCORE = " + ObjectUtils.toString(searchParams.get("AVG_STUDY_SCORE")));
			}
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE > " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE < " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE >= " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE <= " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("SCHEDULE_FLG")))) {
				sql.append("  AND TAB.AVG_SCHEDULE = " + ObjectUtils.toString(searchParams.get("AVG_SCHEDULE")));
			}
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT > " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT < " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT >= " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT <= " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND TAB.AVG_PASS_COUNT = " + ObjectUtils.toString(searchParams.get("AVG_PASS_COUNT")));
			}
		}

		String STUDY_PROGRESS_SORT = ObjectUtils.toString(searchParams.get("STUDY_PROGRESS_SORT"));
		String CHOOSE_COUNT_SORT = ObjectUtils.toString(searchParams.get("CHOOSE_COUNT_SORT"));
		String STUDY_COUNT_SORT = ObjectUtils.toString(searchParams.get("STUDY_COUNT_SORT"));
		String STUDY_TIMES_SORT = ObjectUtils.toString(searchParams.get("STUDY_TIMES_SORT"));
		String PASS_COUNT_SORT = ObjectUtils.toString(searchParams.get("PASS_COUNT_SORT"));
		String UNPASS_COUNT_SORT = ObjectUtils.toString(searchParams.get("UNPASS_COUNT_SORT"));
		String STUDYING_COUNT_SORT = ObjectUtils.toString(searchParams.get("STUDYING_COUNT_SORT"));
		String REGISTER_COUNT_SORT = ObjectUtils.toString(searchParams.get("REGISTER_COUNT_SORT"));
		String UNSTUDY_COUNT_SORT = ObjectUtils.toString(searchParams.get("UNSTUDY_COUNT_SORT"));
		// 平均学习进度
		boolean flag = false;
		if (EmptyUtils.isNotEmpty(STUDY_PROGRESS_SORT)) {
			sql.append(" ORDER BY AVG_SCHEDULE " + STUDY_PROGRESS_SORT);
		} else {
			if (EmptyUtils.isEmpty(STUDY_PROGRESS_SORT) && EmptyUtils.isEmpty(CHOOSE_COUNT_SORT)
					&& EmptyUtils.isEmpty(STUDY_COUNT_SORT) && EmptyUtils.isEmpty(STUDY_TIMES_SORT)
					&& EmptyUtils.isEmpty(PASS_COUNT_SORT) && EmptyUtils.isEmpty(UNPASS_COUNT_SORT)
					&& EmptyUtils.isEmpty(STUDYING_COUNT_SORT) && EmptyUtils.isEmpty(REGISTER_COUNT_SORT)
					&& EmptyUtils.isEmpty(UNSTUDY_COUNT_SORT)) {
				sql.append(" ORDER BY AVG_SCHEDULE DESC");
			} else {
				sql.append(" ORDER BY ");
				flag = true;
			}
		}
		// 选课人数排序
		if (EmptyUtils.isNotEmpty(CHOOSE_COUNT_SORT)) {
			if (flag) {
				sql.append(" REC_COUNT " + CHOOSE_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,REC_COUNT " + CHOOSE_COUNT_SORT);
			}
		}
		// 平均学习次数
		if (EmptyUtils.isNotEmpty(STUDY_COUNT_SORT)) {
			if (flag) {
				sql.append(" AVG_LOGIN_COUNT " + STUDY_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,AVG_LOGIN_COUNT " + STUDY_COUNT_SORT);
			}
		}
		// 平均学习时长
		if (EmptyUtils.isNotEmpty(STUDY_TIMES_SORT)) {
			if (flag) {
				sql.append(" AVG_LOGIN_TIME " + STUDY_TIMES_SORT);
				flag = false;
			} else {
				sql.append(" ,AVG_LOGIN_TIME " + STUDY_TIMES_SORT);
			}
		}
		// 已通过人数
		if (EmptyUtils.isNotEmpty(PASS_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_PASS_COUNT " + PASS_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_PASS_COUNT " + PASS_COUNT_SORT);
			}
		}
		// 未通过人数
		if (EmptyUtils.isNotEmpty(UNPASS_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_UNPASS_COUNT " + UNPASS_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_UNPASS_COUNT " + UNPASS_COUNT_SORT);
			}
		}
		// 学习中人数
		if (EmptyUtils.isNotEmpty(STUDYING_COUNT_SORT)) {
			if (flag) {
				sql.append(" STUDY_IMG " + STUDYING_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,STUDY_IMG " + STUDYING_COUNT_SORT);
			}
		}
		// 登记中人数
		if (EmptyUtils.isNotEmpty(REGISTER_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_REGISTER_COUNT " + REGISTER_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_REGISTER_COUNT " + REGISTER_COUNT_SORT);
			}
		}
		// 未学习中人数
		if (EmptyUtils.isNotEmpty(UNSTUDY_COUNT_SORT)) {
			if (flag) {
				sql.append(" NEVER_STUDY " + UNSTUDY_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,NEVER_STUDY " + UNSTUDY_COUNT_SORT);
			}
		}

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 学习管理=》课程班学情
	 *
	 * @return
	 */
	public Page getCourseClassList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseClassListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 学习管理=》课程班学情
	 *
	 * @return
	 */
	public List<Map<String, String>> getCourseClassList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseClassListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql, params);
	}

	/**
	 * 学习管理=》教学班学情
	 *
	 * @return
	 */
	public Page getTeachClassList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		/*
		 * sql.append("  SELECT *"); sql.append("  FROM (SELECT GRADE_ID,");
		 * sql.append("  CLASS_ID,"); sql.append("  GRADE_NAME,"); sql.append(
		 * "  BJMC,"); sql.append("  STUDENT_ID,"); sql.append("  XM,");
		 * sql.append("  XH,"); sql.append("  SJH,"); sql.append("  PYCC,");
		 * sql.append("  PYCC_NAME,"); sql.append("  SPECIALTY_ID,");
		 * sql.append("  ZYMC,"); sql.append("  ZXF,"); sql.append("  ZDBYXF,");
		 * sql.append("  SUM_GET_CREDITS,"); sql.append("  REC_COUNT,");
		 * sql.append("  PASS_REC_COUNT,"); sql.append("  (CASE"); sql.append(
		 * "  WHEN ZXF > 0 THEN"); sql.append(
		 * "  floor(SUM_GET_CREDITS / ZXF * 100) "); sql.append("  ELSE");
		 * sql.append("  0"); sql.append("  END) XF_BL,"); sql.append("  (CASE"
		 * ); sql.append("  WHEN REC_COUNT > 0 THEN"); sql.append(
		 * "  floor(PASS_REC_COUNT / REC_COUNT * 100) "); sql.append("  ELSE");
		 * sql.append("  0"); sql.append("  END) PASS_BL"); sql.append(
		 * "  FROM (SELECT GR.GRADE_ID,"); sql.append("  GCI.CLASS_ID,");
		 * sql.append("  GR.GRADE_NAME,"); sql.append("  GCI.BJMC,");
		 * sql.append("  GSI.STUDENT_ID,"); sql.append("  GSI.XM,"); sql.append(
		 * "  GSI.XH,"); sql.append("  GSI.SJH,"); sql.append("  GSI.PYCC,");
		 * sql.append("  (SELECT TSD.NAME"); sql.append(
		 * "  FROM TBL_SYS_DATA TSD"); sql.append("  WHERE TSD.IS_DELETED = 'N'"
		 * ); sql.append("  AND TSD.CODE = GSI.PYCC"); sql.append(
		 * "  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,"); sql.append(
		 * "  GSY.SPECIALTY_ID,"); sql.append("  GSY.ZYMC,"); sql.append(
		 * "  NVL(GSY.ZXF, 0) ZXF,"); sql.append("  NVL(GSY.ZDBYXF, 0) ZDBYXF,"
		 * ); sql.append("  (SELECT NVL(SUM(GRR.GET_CREDITS), 0)"); sql.append(
		 * "  FROM GJT_REC_RESULT GRR"); sql.append(
		 * "  WHERE GRR.IS_DELETED = 'N'"); sql.append(
		 * "  AND GRR.STUDENT_ID = GSI.STUDENT_ID) SUM_GET_CREDITS,");
		 *
		 * sql.append("  (SELECT COUNT(GRR.REC_ID)"); sql.append(
		 * "  FROM GJT_REC_RESULT GRR"); sql.append(
		 * "  WHERE GRR.IS_DELETED = 'N'"); sql.append(
		 * "  AND GRR.STUDENT_ID = GSI.STUDENT_ID) REC_COUNT,");
		 *
		 * sql.append("  (SELECT COUNT(GRR.REC_ID)"); sql.append(
		 * "  FROM GJT_REC_RESULT GRR"); sql.append(
		 * "  WHERE GRR.IS_DELETED = 'N'"); sql.append(
		 * "  AND GRR.EXAM_STATE = '1'"); sql.append(
		 * "  AND GRR.STUDENT_ID = GSI.STUDENT_ID) PASS_REC_COUNT");
		 *
		 * sql.append("  FROM GJT_GRADE         GR,"); sql.append(
		 * "  GJT_CLASS_INFO    GCI,"); sql.append("  GJT_CLASS_STUDENT GCS,");
		 * sql.append("  GJT_STUDENT_INFO  GSI,"); sql.append(
		 * "  GJT_SPECIALTY     GSY"); sql.append("  WHERE GR.IS_DELETED = 'N'"
		 * ); sql.append("  AND GCI.IS_DELETED = 'N'"); sql.append(
		 * "  AND GSI.IS_DELETED = 'N'"); sql.append(
		 * "  AND GSY.IS_DELETED = 'N'"); sql.append(
		 * "  AND GCI.CLASS_TYPE = 'teach'"); sql.append(
		 * "  AND GR.GRADE_ID = GCI.GRADE_ID"); sql.append(
		 * "  AND GCI.CLASS_ID = GCS.CLASS_ID"); sql.append(
		 * "  AND GCS.STUDENT_ID = GSI.STUDENT_ID"); sql.append(
		 * "  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		 *
		 * sql.append("  AND GCI.XX_ID = :XX_ID"); param.put("XX_ID",
		 * ObjectUtils.toString(searchParams.get("XX_ID")));
		 *
		 * if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get(
		 * "GRADE_NAME")))) { sql.append("  AND GR.GRADE_NAME = :GRADE_NAME");
		 * param.put("GRADE_NAME",
		 * "%"+ObjectUtils.toString(searchParams.get("GRADE_NAME")).trim()+"%");
		 * }
		 *
		 * if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC"
		 * )))) { sql.append("  AND GCI.BJMC = :BJMC"); param.put("BJMC",
		 * "%"+ObjectUtils.toString(searchParams.get("BJMC")).trim()+"%"); }
		 *
		 * if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH"))
		 * )) { sql.append("  AND GSI.XH = :XH"); param.put("XH",
		 * "%"+ObjectUtils.toString(searchParams.get("XH")).trim()+"%"); }
		 *
		 * if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM"))
		 * )) { sql.append("  AND GSI.XM = :XM"); param.put("XM",
		 * "%"+ObjectUtils.toString(searchParams.get("XM")).trim()+"%"); }
		 *
		 * sql.append("))  WHERE 1 = 1");
		 *
		 * if
		 * (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XF_BL")
		 * ))) { if
		 * ("GT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
		 * sql.append("  AND XF_BL > "
		 * +ObjectUtils.toString(searchParams.get("XF_BL"))+""); } else if
		 * ("LT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
		 * sql.append("  AND XF_BL < "
		 * +ObjectUtils.toString(searchParams.get("XF_BL"))+""); } else if
		 * ("GTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
		 * sql.append("  AND XF_BL >= "
		 * +ObjectUtils.toString(searchParams.get("XF_BL"))+""); } else if
		 * ("LTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
		 * sql.append("  AND XF_BL <= "
		 * +ObjectUtils.toString(searchParams.get("XF_BL"))+""); } else if
		 * ("EQ".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
		 * sql.append("  AND XF_BL = "
		 * +ObjectUtils.toString(searchParams.get("XF_BL"))+""); } }
		 *
		 * if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get(
		 * "PASS_BL")))) { if
		 * ("GT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
		 * sql.append("  AND PASS_BL > "
		 * +ObjectUtils.toString(searchParams.get("PASS_BL"))+""); } else if
		 * ("LT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
		 * sql.append("  AND PASS_BL < "
		 * +ObjectUtils.toString(searchParams.get("PASS_BL"))+""); } else if
		 * ("GTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
		 * sql.append("  AND PASS_BL >= "
		 * +ObjectUtils.toString(searchParams.get("PASS_BL"))+""); } else if
		 * ("LTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
		 * sql.append("  AND PASS_BL <= "
		 * +ObjectUtils.toString(searchParams.get("PASS_BL"))+""); } else if
		 * ("EQ".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
		 * sql.append("  AND  PASS_BL = "
		 * +ObjectUtils.toString(searchParams.get("PASS_BL"))+""); } }
		 *
		 */

		sql.append("  SELECT");
		sql.append("  	COUNT(STUDENT_ID) STUDENT_COUNT,");
		sql.append(
				"    (CASE WHEN COUNT(STUDENT_ID)>0 THEN FLOOR(SUM(NVL(SUM_GET_CREDITS,0))/COUNT(STUDENT_ID)) ELSE 0 END) SUM_GET_CREDITS,");
		sql.append(
				"    (CASE WHEN COUNT(STUDENT_ID)>0 THEN FLOOR(SUM(NVL(REC_COUNT,0))/COUNT(STUDENT_ID)) ELSE 0 END) REC_COUNT,");
		sql.append(
				"    (CASE WHEN COUNT(STUDENT_ID)>0 THEN FLOOR(SUM(NVL(PASS_REC_COUNT,0))/COUNT(STUDENT_ID)) ELSE 0 END) PASS_REC_COUNT,");
		sql.append(
				"    (CASE WHEN COUNT(STUDENT_ID)>0 THEN FLOOR(SUM(NVL(PASS_EXAM_COUNT,0))/COUNT(STUDENT_ID)) ELSE 0 END) PASS_EXAM_COUNT,");
		sql.append(
				"  	(CASE WHEN COUNT(STUDENT_ID)>0 THEN FLOOR(SUM(NVL(XF_BL,0))/COUNT(STUDENT_ID)) ELSE 0 END) XF_BL,");
		sql.append(
				"  	(CASE WHEN COUNT(STUDENT_ID)>0 THEN FLOOR(SUM(NVL(PASS_BL,0))/COUNT(STUDENT_ID)) ELSE 0 END) PASS_BL,");
		sql.append(
				"  	(CASE WHEN COUNT(STUDENT_ID)>0 THEN FLOOR(SUM(NVL(PASS_EXAM_BL,0))/COUNT(STUDENT_ID)) ELSE 0 END) PASS_EXAM_BL,");
		sql.append("  	GRADE_ID,CLASS_ID,GRADE_NAME,BJMC,PYCC,PYCC_NAME,SPECIALTY_ID,ZYMC,ZXF,ZDBYXF");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GRADE_ID,");
		sql.append("  			CLASS_ID,");
		sql.append("  			GRADE_NAME,");
		sql.append("  			BJMC,");
		sql.append("  			STUDENT_ID,");
		sql.append("  			PYCC,");
		sql.append("  			PYCC_NAME,");
		sql.append("  			SPECIALTY_ID,");
		sql.append("  			ZYMC,");
		sql.append("  			ZXF,");
		sql.append("  			ZDBYXF,");
		sql.append("  			SUM_GET_CREDITS,");
		sql.append("  			REC_COUNT,");
		sql.append("  			PASS_REC_COUNT,");
		sql.append("  			PASS_EXAM_COUNT,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN ZXF > 0 THEN FLOOR( SUM_GET_CREDITS / ZXF * 100 )");
		sql.append("  					ELSE 0");
		sql.append("  				END");
		sql.append("  			) XF_BL,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN REC_COUNT > 0 THEN FLOOR( PASS_REC_COUNT / REC_COUNT * 100 )");
		sql.append("  					ELSE 0");
		sql.append("  				END");
		sql.append("  			) PASS_BL,");
		sql.append(
				"            (CASE WHEN REC_COUNT > 0 THEN FLOOR( PASS_EXAM_COUNT / REC_COUNT * 100 ) ELSE 0 END ) PASS_EXAM_BL");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GR.GRADE_ID,");
		sql.append("  					GCI.CLASS_ID,");
		sql.append("  					GR.GRADE_NAME,");
		sql.append("  					GCI.BJMC,");
		sql.append("  					GSI.STUDENT_ID,");
		sql.append("  					GSI.PYCC,");
		sql.append("  					(");
		sql.append("  						SELECT");
		sql.append("  							TSD.NAME");
		sql.append("  						FROM");
		sql.append("  							TBL_SYS_DATA TSD");
		sql.append("  						WHERE");
		sql.append("  							TSD.IS_DELETED = 'N'");
		sql.append("  							AND TSD.CODE = GSI.PYCC");
		sql.append("  							AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  					) PYCC_NAME,");
		sql.append("  					GSY.SPECIALTY_ID,");
		sql.append("  					GSY.ZYMC,");
		sql.append("  					NVL( GSY.ZXF, 0 ) ZXF,");
		sql.append("  					NVL( GSY.ZDBYXF, 0 ) ZDBYXF,");
		sql.append("  					(");
		sql.append("  						SELECT");
		sql.append("  							NVL( SUM( GRR.GET_CREDITS ), 0 )");
		sql.append("  						FROM");
		sql.append("  							GJT_REC_RESULT GRR");
		sql.append("  						WHERE");
		sql.append("  							GRR.IS_DELETED = 'N'");
		sql.append("  							AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  					) SUM_GET_CREDITS,");
		sql.append("  					(");
		sql.append("  						SELECT");
		sql.append("  							COUNT( GRR.REC_ID )");
		sql.append("  						FROM");
		sql.append("  							GJT_REC_RESULT GRR");
		sql.append("  						WHERE");
		sql.append("  							GRR.IS_DELETED = 'N'");
		sql.append("  							AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  					) REC_COUNT,");
		sql.append("  					(");
		sql.append("  						SELECT");
		sql.append("  							COUNT( GRR.REC_ID )");
		sql.append("  						FROM");
		sql.append("  							GJT_REC_RESULT GRR");
		sql.append("  						WHERE");
		sql.append("  							GRR.IS_DELETED = 'N'");
		sql.append("  							AND GRR.EXAM_STATE = '1'");
		sql.append("  							AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  					) PASS_REC_COUNT,");
		sql.append("                    (");
		sql.append("                SELECT COUNT( GRR.REC_ID )");
		sql.append("                    FROM");
		sql.append("                       GJT_REC_RESULT GRR");
		sql.append("                    WHERE");
		sql.append("                       GRR.IS_DELETED = 'N'");
		sql.append("                       AND GRR.EXAM_STATE  IN('0','1')");
		sql.append("                       AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("                    ) PASS_EXAM_COUNT");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GR,");
		sql.append("  					GJT_CLASS_INFO GCI,");
		sql.append("  					GJT_CLASS_STUDENT GCS,");
		sql.append("  					GJT_STUDENT_INFO GSI,");
		sql.append("  					GJT_SPECIALTY GSY");
		sql.append("  				WHERE");
		sql.append("  					GR.IS_DELETED = 'N'");
		sql.append("  					AND GCI.IS_DELETED = 'N'");
		sql.append("  					AND GSI.IS_DELETED = 'N'");
		sql.append("  					AND GSY.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  					AND GR.GRADE_ID = GCI.GRADE_ID");
		sql.append("  					AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  					AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  					AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  					AND GCI.XX_ID =:XX_ID");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GR.GRADE_NAME LIKE :GRADE_NAME");
			param.put("GRADE_NAME", "%" + ObjectUtils.toString(searchParams.get("GRADE_NAME")).trim() + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			param.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")).trim() + "%");
		}

		sql.append("  			)");
		sql.append("  	)");
		sql.append("  WHERE 1 = 1 ");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XF_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append(" AND XF_BL > " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append(" AND XF_BL < " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append(" AND XF_BL >= " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append(" AND XF_BL <= " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append(" AND XF_BL = " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			}
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PASS_EXAM_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("EXAM_FLG")))) {
				sql.append(" AND XF_BL > " + ObjectUtils.toString(searchParams.get("PASS_EXAM_BL")) + "");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("EXAM_FLG")))) {
				sql.append(" AND XF_BL < " + ObjectUtils.toString(searchParams.get("PASS_EXAM_BL")) + "");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("EXAM_FLG")))) {
				sql.append(" AND XF_BL >= " + ObjectUtils.toString(searchParams.get("PASS_EXAM_BL")) + "");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("EXAM_FLG")))) {
				sql.append(" AND XF_BL <= " + ObjectUtils.toString(searchParams.get("PASS_EXAM_BL")) + "");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("EXAM_FLG")))) {
				sql.append(" AND XF_BL = " + ObjectUtils.toString(searchParams.get("PASS_EXAM_BL")) + "");
			}
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PASS_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL > " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL < " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL >= " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL <= " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND  PASS_BL = " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			}
		}

		sql.append(" GROUP BY GRADE_ID,CLASS_ID,GRADE_NAME,BJMC,PYCC,PYCC_NAME,SPECIALTY_ID,ZYMC,ZXF,ZDBYXF");

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学情分析--》学员学情列表sql处理类
	 *
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> getStudentCourseListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  	  SELECT * FROM (");
		sql.append("   SELECT GRADE_ID,CLASS_ID,YEAR_NAME,GRADE_NAME,START_GRADE,");
		sql.append("            STUDENT_ID,XM,XH,SJH,PYCC,PYCC_NAME,XXZX_ID,SC_NAME,ZP,EENO,XJZT,USER_TYPE,");
		sql.append("            SPECIALTY_ID,BJMC,ZYMC,ZXF,ZDBYXF,");
		sql.append("            SUM_GET_CREDITS,REC_COUNT,UNPASS_REC_COUNT,LEARNING_REC_COUNT,UNLEARN_REC_COUNT,");
		sql.append("            PASS_REC_COUNT,LOGIN_TIMES,LOGIN_TIME_COUNT,LAST_LOGIN_TIME,BEHIND_REC_COUNT,NORMAL_REC_COUNT,SUM_REGISTER_COUNT,");
		sql.append("                 (CASE");
		sql.append("                   WHEN ZXF > 0 THEN FLOOR(SUM_GET_CREDITS / ZXF * 100)");
		sql.append("                   ELSE 0");
		sql.append("                 END) XF_BL,");
		sql.append("                 (CASE WHEN REC_COUNT > 0 THEN FLOOR(PASS_REC_COUNT / REC_COUNT * 100)");
		sql.append("                   ELSE 0");
		sql.append("                 END) PASS_BL,");
		sql.append("                 NVL((SELECT vss2.IS_ONLINE FROM VIEW_STUDENT_STUDY_SITUATION vss2");
		sql.append("              WHERE vss2.STUDENT_ID = x.STUDENT_ID AND vss2.LAST_LOGIN_DATE=MAX_LAST_LOGIN_TIME AND ROWNUM = 1), 'N') IS_ONLINE,");
		sql.append("             (SELECT (CASE WHEN vss2.BYOD_TYPE = 'PC' THEN 'PC'");
		sql.append("                       WHEN vss2.BYOD_TYPE = 'PHONE' THEN 'APP'");
		sql.append("                       WHEN vss2.BYOD_TYPE = 'PAD' THEN 'APP'");
		sql.append("                       ELSE '--'");
		sql.append("                     END) FROM VIEW_STUDENT_STUDY_SITUATION vss2");
		sql.append("              WHERE vss2.STUDENT_ID = x.STUDENT_ID AND vss2.LAST_LOGIN_DATE=MAX_LAST_LOGIN_TIME");
		sql.append("              AND ROWNUM = 1) DEVICE");
		sql.append("   from (");
		sql.append("        SELECT");
		sql.append("           GSI.STUDENT_ID,GR.GRADE_ID,GCI.CLASS_ID,GR.GRADE_NAME,GR.GRADE_NAME START_GRADE,GCI.BJMC,gsi.XXZX_ID,gsc.SC_NAME,");
		sql.append("           GSI.XM,GSI.XH,GSI.SJH,GSI.PYCC,GY.NAME YEAR_NAME,gsi.AVATAR ZP,gsi.EENO,GSI.XJZT,GSI.USER_TYPE,TSD.NAME PYCC_NAME,");
		sql.append("           GSY.SPECIALTY_ID,GSY.ZYMC,NVL(GSY.ZDBYXF, 0) ZDBYXF,");
		sql.append("           NVL(SUM(NVL(GTP.XF, 0)), 0) ZXF,");
		sql.append("           NVL(SUM(NVL(GRR.GET_CREDITS, 0)), 0) SUM_GET_CREDITS,");
		sql.append("           COUNT(GRR.REC_ID) REC_COUNT,");
		sql.append("           COUNT(CASE WHEN VSS.EXAM_STATE = '0' THEN GRR.REC_ID ELSE NULL END) UNPASS_REC_COUNT,");
		sql.append("           COUNT(CASE WHEN GRR.EXAM_STATE = '2' AND VSS.LOGIN_TIMES > 0 THEN GRR.REC_ID ELSE NULL END) LEARNING_REC_COUNT,");
		sql.append("           COUNT(CASE WHEN NVL(VSS.LOGIN_TIMES, 0) = 0 THEN GRR.REC_ID ELSE NULL END) UNLEARN_REC_COUNT,");
		sql.append("           COUNT(CASE WHEN GRR.EXAM_STATE = '1' THEN GRR.REC_ID ELSE NULL END) PASS_REC_COUNT,");
		sql.append("           NVL(SUM(NVL(vss.LOGIN_TIMES, 0)), 0) LOGIN_TIMES,");
		sql.append("           ROUND(NVL(SUM(NVL(vss.ONLINE_TIME, 0)), 0) / 60, 1) LOGIN_TIME_COUNT,");
		sql.append("           FLOOR(SYSDATE - MAX(vss.LAST_LOGIN_DATE)) LAST_LOGIN_TIME,");
		sql.append("           MAX(vss.LAST_LOGIN_DATE) MAX_LAST_LOGIN_TIME,");
		sql.append("           COUNT(CASE WHEN vss.STATE = '1' THEN GRR.REC_ID ELSE NULL END) BEHIND_REC_COUNT,");
		sql.append("           COUNT(GRR.REC_ID) - COUNT(CASE WHEN vss.STATE = '1' THEN GRR.REC_ID ELSE NULL END) NORMAL_REC_COUNT,");
		sql.append("           COUNT(CASE WHEN GRR.EXAM_STATE = '3' THEN GRR.REC_ID ELSE NULL END) SUM_REGISTER_COUNT");
		sql.append("      FROM GJT_STUDENT_INFO  GSI");
		sql.append("      INNER JOIN     GJT_GRADE         GR ON GR.IS_DELETED = 'N' AND GR.GRADE_ID = GSI.NJ");
		sql.append("      INNER JOIN     GJT_YEAR          GY ON GY.GRADE_ID = GR.YEAR_ID");
		sql.append("      INNER JOIN     GJT_SPECIALTY     GSY ON GSY.IS_DELETED = 'N' AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("      INNER JOIN     GJT_CLASS_STUDENT GCS ON GCS.IS_DELETED = 'N' AND GCS.Student_Id=GSI.Student_Id");
		sql.append("      INNER JOIN     GJT_CLASS_INFO    GCI ON GCI.IS_DELETED = 'N' AND GCI.Class_Id=GCS.Class_Id AND GCI.CLASS_TYPE = 'teach'");
		sql.append("      INNER JOIN     GJT_REC_RESULT GRR ON GRR.IS_DELETED = 'N' AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("      INNER JOIN     VIEW_TEACH_PLAN GTP ON GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("      LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID = vss.CHOOSE_ID");
		sql.append("      LEFT JOIN     GJT_STUDY_CENTER gsc ON gsc.IS_DELETED = 'N' AND gsc.ID = gsi.XXZX_ID");
		sql.append("      LEFT JOIN     TBL_SYS_DATA TSD ON TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'TrainingLevel' AND TSD.CODE = GSI.PYCC");
		sql.append("     WHERE GSI.IS_DELETED = 'N'");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GR.GRADE_NAME LIKE :GRADE_NAME");
			params.put("GRADE_NAME", "%" + ObjectUtils.toString(searchParams.get("GRADE_NAME")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND gsi.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("     GROUP BY ");
		sql.append("           GSI.STUDENT_ID,GR.GRADE_ID,GCI.CLASS_ID,GR.GRADE_NAME,GR.GRADE_NAME,GCI.BJMC,gsi.XXZX_ID,gsc.SC_NAME,");
		sql.append("           GSI.XM,GSI.XH,GSI.SJH,GSI.PYCC,GY.NAME,gsi.AVATAR,gsi.EENO,GSI.XJZT,GSI.USER_TYPE,TSD.NAME,");
		sql.append("           GSY.SPECIALTY_ID,GSY.ZYMC,NVL(GSY.ZDBYXF, 0)");
		sql.append("   ) x");
		sql.append("   ) TAB WHERE 1=1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XF_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL > " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL < " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL >= " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL <= " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL = " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			}
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PASS_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL > " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL < " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL >= " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL <= " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL = " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			}
		}

		String pass_status = ObjectUtils.toString(searchParams.get("PASS_STATUS"), "");// 是否完成课程的状态
		if (EmptyUtils.isNotEmpty(pass_status)) {// 已经完成课程
			if ("Y".equals(pass_status)) {
				sql.append(" AND PASS_REC_COUNT = REC_COUNT");
			}
			if ("N".equals(pass_status)) {
				sql.append(" AND PASS_REC_COUNT < REC_COUNT");
			}
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("IS_ONLINE"))) {
			if ("Y".equals(ObjectUtils.toString(searchParams.get("IS_ONLINE")))) {
				sql.append("	AND IS_ONLINE = 'Y'");
			}
			if ("N".equals(ObjectUtils.toString(searchParams.get("IS_ONLINE")))) {
				sql.append("	AND IS_ONLINE = 'N'");
			}
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 学情正常
				sql.append("  			AND BEHIND_REC_COUNT = 0");
			} else if ("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 异常，有落后课程
				sql.append("  			AND BEHIND_REC_COUNT > 0");
			}
		}

		sql.append(" ORDER BY PASS_REC_COUNT DESC NULLS LAST");

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 学习管理=》学员课程学情
	 *
	 * @return
	 */
	public Page getStudentCourseList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentCourseListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 学习管理=》学员课程学情统计
	 *
	 * @return
	 */
	public long getStudentCourseCount(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentCourseListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	/**
	 * 学习管理=》学员课程学情
	 *
	 * @return
	 */
	public List<Map<String, String>> getStudentCourseList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentCourseListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql, params);
	}

	/**
	 * 学习管理=》学员专业学情
	 *
	 * @return
	 */
	public Page getStudentMajorList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM (SELECT SPECIALTY_ID,");
		sql.append("  ZYMC,");
		sql.append("  ZXF,");
		sql.append("  ZDBYXF, ");
		sql.append("  GRADE_ID,");
		sql.append("  GRADE_NAME,");
		sql.append(
				"  (SELECT GG.GRADE_NAME FROM GJT_GRADE gg WHERE gg.IS_DELETED='N' AND gg.START_DATE<=SYSDATE AND gg.END_DATE>=SYSDATE AND gg.XX_ID=:XX_ID AND rownum<2) NOW_TERM,");
		sql.append("  PYCC,");
		sql.append("  PYCC_NAME,");
		sql.append("  STUDENT_COUNT,");
		sql.append("  SUM_PASS_COUNT,");
		sql.append("  COURSE_COUNT,");
		sql.append("  (CASE");
		sql.append("  WHEN STUDENT_COUNT > 0 THEN");
		sql.append("  floor(SUM_GET_CREDITS / STUDENT_COUNT)");
		sql.append("  ELSE");
		sql.append("  0");
		sql.append("  END) AVG_GET_CREDITS,");

		sql.append("  CASE");
		sql.append("  WHEN ZXF > 0 THEN");
		sql.append("  floor((CASE");
		sql.append("  WHEN STUDENT_COUNT > 0 THEN");
		sql.append("  floor(SUM_GET_CREDITS / STUDENT_COUNT)");
		sql.append("  ELSE");
		sql.append("  0");
		sql.append("  END) / ZXF * 100) ELSE 0 END XF_BL,");

		sql.append("  (CASE");
		sql.append("  WHEN STUDENT_COUNT > 0 THEN");
		sql.append("  floor(SUM_PASS_COUNT / STUDENT_COUNT)");
		sql.append("  ELSE");
		sql.append("  0");
		sql.append("  END) AVG_PASS_COUNT,");

		sql.append("  (CASE");
		sql.append("  WHEN COURSE_COUNT > 0 THEN");
		sql.append("  floor((CASE");
		sql.append("  WHEN STUDENT_COUNT > 0 THEN");
		sql.append("  floor(SUM_PASS_COUNT / STUDENT_COUNT)");
		sql.append("  ELSE");
		sql.append("  0");
		sql.append("  END) / COURSE_COUNT * 100) ELSE 0 END) PASS_BL");

		sql.append("  FROM (SELECT GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  NVL(GSY.ZXF,0) ZXF,");
		sql.append("  NVL(GSY.ZDBYXF,0) ZDBYXF,");
		sql.append("  GR.GRADE_ID,");
		sql.append("  GR.GRADE_NAME,");
		sql.append("  GSY.PYCC,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSY.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");

		sql.append("  (SELECT COUNT(DISTINCT GRR.STUDENT_ID)");
		sql.append("  FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GTP.GRADE_ID = GGS.GRADE_ID");
		sql.append("  AND GTP.KKZY = GSY.SPECIALTY_ID) STUDENT_COUNT,");

		sql.append("  (SELECT NVL(SUM(GRR.GET_CREDITS), 0)");
		sql.append("  FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.EXAM_STATE = '1'");
		sql.append("  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GTP.GRADE_ID = GGS.GRADE_ID");
		sql.append("  AND GTP.KKZY = GSY.SPECIALTY_ID) SUM_GET_CREDITS,");

		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.EXAM_STATE = '1'");
		sql.append("  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GTP.GRADE_ID = GGS.GRADE_ID");
		sql.append("  AND GTP.KKZY = GSY.SPECIALTY_ID) SUM_PASS_COUNT,");

		sql.append("  (SELECT COUNT(GTP.COURSE_ID)");
		sql.append("  FROM VIEW_TEACH_PLAN GTP");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GTP.GRADE_ID = GGS.GRADE_ID");
		sql.append("  AND GTP.KKZY = GSY.SPECIALTY_ID) COURSE_COUNT");

		sql.append("  FROM GJT_GRADE_SPECIALTY GGS,");
		sql.append("  GJT_SPECIALTY       GSY,");
		sql.append("  GJT_GRADE           GR");
		sql.append("  WHERE GGS.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GR.IS_DELETED = 'N'");
		sql.append("  AND GGS.SPECIALTY_ID = GSY.SPECIALTY_ID");
		sql.append("  AND GGS.GRADE_ID = GR.GRADE_ID");

		sql.append("  AND GSY.XX_ID = :XX_ID");
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GR.GRADE_ID = :GRADE_ID");
			param.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			param.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSY.PYCC = :PYCC");
			param.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		sql.append("  )) TAB WHERE 1 = 1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PASS_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL > " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL < " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL >= " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL <= " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("PASS_FLG")))) {
				sql.append("  AND PASS_BL = " + ObjectUtils.toString(searchParams.get("PASS_BL")) + "");
			}
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XF_BL")))) {
			if ("GT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL > " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("LT".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL < " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("GTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL >= " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("LTE".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL <= " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			} else if ("EQ".equals(ObjectUtils.toString(searchParams.get("XF_FLG")))) {
				sql.append("  AND XF_BL = " + ObjectUtils.toString(searchParams.get("XF_BL")) + "");
			}
		}

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学习管理=》教务班考勤
	 *
	 * @return
	 */
	public Page getClassLoginList(Map searchParams, PageRequest pageRequst) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GCI.CLASS_ID,");
		sql.append("  GCI.BJMC,");
		sql.append("  (SELECT COUNT(GCS.CLASS_ID)");
		sql.append("  FROM GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCS.IS_DELETED = 'N'");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID) STUDENT_COUNT");
		sql.append("  FROM GJT_CLASS_INFO GCI");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		if (StringUtils.isNotBlank(xxId)) {
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(xxId);
			sql.append(" AND GCI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")).trim() + "%");
		}

		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 学习管理=》教务班考勤统计个数
	 *
	 * @return
	 */
	public int getClassLoginCount(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT COUNT(STUDENT_ID) STUDENT_COUNT");
		sql.append("  FROM (SELECT GCS.STUDENT_ID, MAX(GSR.LAST_DATE) LAST_DATE");
		sql.append("  FROM GJT_CLASS_STUDENT GCS, GJT_STUDY_RECORD GSR");
		sql.append("  WHERE GSR.IS_DELETED = 'N' AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCS.STUDENT_ID = GSR.STUDENT_ID");
		sql.append("  AND GCS.CLASS_ID = :CLASS_ID");
		sql.append("  GROUP BY GCS.STUDENT_ID) TAB");
		if ("7".equals(ObjectUtils.toString(searchParams.get("TYPE")))) {
			sql.append("  WHERE LAST_DATE < SYSDATE - 7");
		} else if ("3_7".equals(ObjectUtils.toString(searchParams.get("TYPE")))) {
			sql.append("  WHERE LAST_DATE >= SYSDATE - 7 AND LAST_DATE < SYSDATE-3");
		} else if ("3".equals(ObjectUtils.toString(searchParams.get("TYPE")))) {
			sql.append("  WHERE LAST_DATE >= SYSDATE - 3");
		}
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")).trim());

		List tempList = commonDao.queryForMapListNative(sql.toString(), param);
		if (EmptyUtils.isNotEmpty(tempList)) {
			Map tempMap = (Map) tempList.get(0);
			int count = Integer.parseInt(ObjectUtils.toString(tempMap.get("STUDENT_COUNT")));
			return count;
		}
		return 0;
	}

	/**
	 * 学员考勤列表统计项
	 *
	 * @param searchParams
	 * @return
	 */
	public int getStudentLoginCount(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  SELECT COUNT(STUDENT_ID) ");
		sql.append("  FROM");
		sql.append("  	   (SELECT");
		sql.append("  			distinct gsi.STUDENT_ID,");
		sql.append("  			NVL((SELECT LUO.IS_ONLINE FROM GJT_REC_RESULT grr LEFT JOIN LCMS_USER_ONLINETIME LUO ON GRR.REC_ID = LUO.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=gsi.STUDENT_ID AND LUO.IS_ONLINE='Y' AND ROWNUM=1),'N') IS_ONLINE,");
		sql.append("  			(select LOGIN_DT from gjt_student_info xxx inner join (SELECT  grr.STUDENT_ID,LUO.LOGIN_DT  FROM GJT_REC_RESULT grr LEFT JOIN LCMS_USER_ONLINETIME LUO ON GRR.REC_ID = LUO.CHOOSE_ID WHERE grr.IS_DELETED='N' ORDER BY luo.LOGIN_DT DESC NULLS LAST ) yyy on yyy.STUDENT_ID=xxx.STUDENT_ID where xxx.student_id=gsi.student_id and ROWNUM=1) LAST_LOGIN_TIME,");
		sql.append("  			(SELECT SUM(NVL(LUO.ONLINE_COUNT_MOBILE, 0) + NVL (LUO.ONLINE_COUNT, 0)) FROM GJT_REC_RESULT grr LEFT JOIN LCMS_USER_ONLINETIME LUO ON grr.REC_ID=LUO.CHOOSE_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=gsi.STUDENT_ID) ALL_ONLINE_COUNT");
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


		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
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
		sql.append("  ) TAB WHERE 1=1");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND TAB.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 在线
				sql.append("  			AND IS_ONLINE = 'Y' AND ALL_ONLINE_COUNT <> 0");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 从未登录
				sql.append("  			AND (ALL_ONLINE_COUNT = 0 OR ALL_ONLINE_COUNT IS NULL)");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（3天内未学习）
				sql.append("  			AND LAST_LOGIN_TIME <SYSDATE AND LAST_LOGIN_TIME >=SYSDATE-3");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（3天以上未学习）
				sql.append("  			AND LAST_LOGIN_TIME <SYSDATE-3 AND LAST_LOGIN_TIME >=SYSDATE-7 ");
			} else if ("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（7天以上未学习）
				sql.append("  			AND LAST_LOGIN_TIME <SYSDATE-7");
			}
		}

		BigDecimal num = (BigDecimal) commonDao.queryObjectNative(sql.toString(), params);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	/**
	 * 学习管理=》学员考勤
	 *
	 * @return
	 */
	public Map<String, Object> getStudentLoginListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  	  SELECT * FROM (");
		sql.append("   SELECT GRADE_ID,CLASS_ID,YEAR_NAME,GRADE_NAME,START_GRADE,");
		sql.append("            STUDENT_ID,XM,XH,SJH,PYCC,PYCC_NAME,XXZX_ID,SC_NAME,ZP,EENO,XJZT,USER_TYPE,");
		sql.append("            SPECIALTY_ID,BJMC,ZYMC,ZXF,ZDBYXF,");
		sql.append("            SUM_GET_CREDITS,REC_COUNT,UNPASS_REC_COUNT,LEARNING_REC_COUNT,UNLEARN_REC_COUNT,");
		sql.append("            PASS_REC_COUNT,LOGIN_TIMES,LOGIN_TIME_COUNT,LAST_LOGIN_TIME,MAX_LAST_LOGIN_TIME,PC_ONLINE_COUNT,APP_ONLINE_COUNT,ALL_ONLINE_COUNT,");
		sql.append("                 (CASE");
		sql.append("                   WHEN ALL_ONLINE_COUNT > 0 THEN CEIL(PC_ONLINE_COUNT / ALL_ONLINE_COUNT * 100)");
		sql.append("                   ELSE 0");
		sql.append("                 END) PC_ONLINE_PERCENT,");
		sql.append("                 (CASE");
		sql.append("                   WHEN ALL_ONLINE_COUNT > 0 THEN 100 - CEIL(PC_ONLINE_COUNT / ALL_ONLINE_COUNT * 100)");
		sql.append("                   ELSE 0");
		sql.append("                 END) APP_ONLINE_PERCENT,");
		sql.append("                 (CASE");
		sql.append("                   WHEN ZXF > 0 THEN FLOOR(SUM_GET_CREDITS / ZXF * 100)");
		sql.append("                   ELSE 0");
		sql.append("                 END) XF_BL,");
		sql.append("                 (CASE WHEN REC_COUNT > 0 THEN FLOOR(PASS_REC_COUNT / REC_COUNT * 100)");
		sql.append("                   ELSE 0");
		sql.append("                 END) PASS_BL,");
		sql.append("                 NVL((SELECT vss2.IS_ONLINE FROM VIEW_STUDENT_STUDY_SITUATION vss2");
		sql.append("              WHERE vss2.STUDENT_ID = x.STUDENT_ID AND vss2.LAST_LOGIN_DATE=MAX_LAST_LOGIN_TIME AND ROWNUM = 1), 'N') IS_ONLINE,");
		sql.append("             (SELECT (CASE WHEN vss2.BYOD_TYPE = 'PC' THEN 'PC'");
		sql.append("                       WHEN vss2.BYOD_TYPE = 'PHONE' THEN 'APP'");
		sql.append("                       WHEN vss2.BYOD_TYPE = 'PAD' THEN 'APP'");
		sql.append("                       ELSE '--'");
		sql.append("                     END) FROM VIEW_STUDENT_STUDY_SITUATION vss2");
		sql.append("              WHERE vss2.STUDENT_ID = x.STUDENT_ID AND vss2.LAST_LOGIN_DATE=MAX_LAST_LOGIN_TIME");
		sql.append("              AND ROWNUM = 1) DEVICE");
		sql.append("   from (");
		sql.append("        SELECT");
		sql.append("           GSI.STUDENT_ID,GR.GRADE_ID,GCI.CLASS_ID,GR.GRADE_NAME,GR.GRADE_NAME START_GRADE,GCI.BJMC,gsi.XXZX_ID,gsc.SC_NAME,");
		sql.append("           GSI.XM,GSI.XH,GSI.SJH,GSI.PYCC,GY.NAME YEAR_NAME,gsi.AVATAR ZP,gsi.EENO,GSI.XJZT,GSI.USER_TYPE,TSD.NAME PYCC_NAME,");
		sql.append("           GSY.SPECIALTY_ID,GSY.ZYMC,NVL(GSY.ZDBYXF, 0) ZDBYXF,");
		sql.append("           NVL(SUM(NVL(GTP.XF, 0)), 0) ZXF,");
		sql.append("           NVL(SUM(NVL(GRR.GET_CREDITS, 0)), 0) SUM_GET_CREDITS,");
		sql.append("           COUNT(GRR.REC_ID) REC_COUNT,");
		sql.append("           COUNT(CASE WHEN VSS.EXAM_STATE = '0' THEN GRR.REC_ID ELSE NULL END) UNPASS_REC_COUNT,");
		sql.append("           COUNT(CASE WHEN GRR.EXAM_STATE = '2' AND VSS.LOGIN_TIMES > 0 THEN GRR.REC_ID ELSE NULL END) LEARNING_REC_COUNT,");
		sql.append("           COUNT(CASE WHEN NVL(VSS.LOGIN_TIMES, 0) = 0 THEN GRR.REC_ID ELSE NULL END) UNLEARN_REC_COUNT,");
		sql.append("           COUNT(CASE WHEN GRR.EXAM_STATE = '1' THEN GRR.REC_ID ELSE NULL END) PASS_REC_COUNT,");
		sql.append("           NVL(SUM(NVL(vss.LOGIN_TIMES, 0)), 0) LOGIN_TIMES,");
		sql.append("           ROUND(NVL(SUM(NVL(vss.ONLINE_TIME, 0)), 0) / 60, 1) LOGIN_TIME_COUNT,");
		sql.append("           FLOOR(SYSDATE - MAX(vss.LAST_LOGIN_DATE)) LAST_LOGIN_TIME,");
		sql.append("           SUM(NVL(vss.PC_ONLINE_COUNT, 0)) PC_ONLINE_COUNT,");
		sql.append("           SUM(NVL(vss.APP_ONLINE_COUNT, 0)) APP_ONLINE_COUNT,");
		sql.append("           SUM(NVL(vss.APP_ONLINE_COUNT, 0)) + SUM(NVL(vss.PC_ONLINE_COUNT, 0)) ALL_ONLINE_COUNT,");
		sql.append("           MAX(vss.LAST_LOGIN_DATE) MAX_LAST_LOGIN_TIME");
		sql.append("      FROM GJT_STUDENT_INFO  GSI");
		sql.append("      INNER JOIN     GJT_GRADE         GR ON GR.IS_DELETED = 'N' AND GR.GRADE_ID = GSI.NJ");
		sql.append("      INNER JOIN     GJT_YEAR          GY ON GY.GRADE_ID = GR.YEAR_ID");
		sql.append("      INNER JOIN     GJT_SPECIALTY     GSY ON GSY.IS_DELETED = 'N' AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("      INNER JOIN     GJT_CLASS_STUDENT GCS ON GCS.IS_DELETED = 'N' AND GCS.Student_Id=GSI.Student_Id");
		sql.append("      INNER JOIN     GJT_CLASS_INFO    GCI ON GCI.IS_DELETED = 'N' AND GCI.Class_Id=GCS.Class_Id AND GCI.CLASS_TYPE = 'teach'");
		sql.append("      INNER JOIN     GJT_REC_RESULT GRR ON GRR.IS_DELETED = 'N' AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("      INNER JOIN     VIEW_TEACH_PLAN GTP ON GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("      LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID = vss.CHOOSE_ID");
		sql.append("      LEFT JOIN     GJT_STUDY_CENTER gsc ON gsc.IS_DELETED = 'N' AND gsc.ID = gsi.XXZX_ID");
		sql.append("      LEFT JOIN     TBL_SYS_DATA TSD ON TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'TrainingLevel' AND TSD.CODE = GSI.PYCC");
		sql.append("     WHERE GSI.IS_DELETED = 'N'");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND gsi.STUDENT_ID =:STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GR.GRADE_NAME LIKE :GRADE_NAME");
			params.put("GRADE_NAME", "%" + ObjectUtils.toString(searchParams.get("GRADE_NAME")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%");
		}
		
		//限制了指定班级的学生
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH",ObjectUtils.toString(searchParams.get("XH")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND gsi.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("     GROUP BY ");
		sql.append("           GSI.STUDENT_ID,GR.GRADE_ID,GCI.CLASS_ID,GR.GRADE_NAME,GR.GRADE_NAME,GCI.BJMC,gsi.XXZX_ID,gsc.SC_NAME,");
		sql.append("           GSI.XM,GSI.XH,GSI.SJH,GSI.PYCC,GY.NAME,gsi.AVATAR,gsi.EENO,GSI.XJZT,GSI.USER_TYPE,TSD.NAME,");
		sql.append("           GSY.SPECIALTY_ID,GSY.ZYMC,NVL(GSY.ZDBYXF, 0)");
		sql.append("   ) x");
		sql.append("   ) TAB WHERE 1=1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND TAB.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 在线
				sql.append("  			AND IS_ONLINE = 'Y' AND ALL_ONLINE_COUNT <> 0");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 从未登录
				sql.append("  			AND (ALL_ONLINE_COUNT = 0 OR ALL_ONLINE_COUNT IS NULL)");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（3天内未学习）
				sql.append("  			AND LAST_LOGIN_TIME<=3");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（3天以上未学习）
				sql.append("  			AND LAST_LOGIN_TIME>3 AND LAST_LOGIN_TIME<=7 ");
			} else if ("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（7天以上未学习）
				sql.append("  			AND LAST_LOGIN_TIME>7");
			}
		}

		sql.append(" ORDER BY LOGIN_TIME_COUNT DESC NULLS LAST");

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 学习管理=》学员考勤
	 *
	 * @return
	 */
	public Page getStudentLoginList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentLoginListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 学习管理=》学员考勤无分页
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getStudentLoginList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentLoginListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql, params);
	}

	/**
	 * 管理后台--考勤分析=》课程班考勤sql处理类
	 *
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> getCourseClassLoginListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();


		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");

		sql.append("  SELECT * ");
		sql.append("  FROM (");
		sql.append("      SELECT gtp.TERMCOURSE_ID,");
		sql.append("        GCE.COURSE_ID,");
		sql.append("        GCE.KCH,");
		sql.append("        TO_CHAR(gre.START_DATE, 'yyyy-mm-dd') SDATE,");
		sql.append("        TO_CHAR(gre.END_DATE, 'yyyy-mm-dd') EDATE,");
		sql.append("        GRE.GRADE_ID,");
		sql.append("        GRE.GRADE_NAME,");
		sql.append("        GCE.KCMC,");
		sql.append("        GCI.CLASS_ID,GCI.BJMC,");
		sql.append("        (CASE WHEN SYSDATE >= gre.START_DATE AND SYSDATE <= gre.END_DATE THEN '1'");
		sql.append("    WHEN SYSDATE < gre.START_DATE THEN '2'");
		sql.append("    WHEN SYSDATE > gre.END_DATE THEN '3'");
		sql.append("    ELSE '0' END) TIME_FLG,");
		sql.append("        z.REC_COUNT,z.SUM_SCHEDULE,z.SUM_STUDY_SCORE,z.SUM_EXAM_SCORE,z.SUM_TOTAL_SCORE,z.SUM_LOGIN_COUNT,z.SUM_LOGIN_TIME,");
		sql.append("        z.SUM_NEVER_STUDY,z.SUM_STUDY_IMG,z.SUM_PASS_COUNT,z.SUM_REGISTER_COUNT,z.SUM_UNPASS_COUNT,");
		sql.append("		z.DAY7_LOGIN,z.DAY3_7_LOGIN,z.DAY3_LOGIN,z.SUM_NEVER_STUDY NO_DAY_LOGIN,z.ONLINE_STUDENT_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_SCHEDULE / REC_COUNT)");
		sql.append("    ELSE 0 END) AVG_SCHEDULE,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_STUDY_SCORE / REC_COUNT)");
		sql.append("    ELSE 0 END) AVG_STUDY_SCORE,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_LOGIN_COUNT / REC_COUNT)");
		sql.append("    ELSE 0 END) AVG_LOGIN_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND(SUM_LOGIN_TIME / REC_COUNT, 1)");
		sql.append("    ELSE 0 END) AVG_LOGIN_TIME,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_PASS_COUNT / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) AVG_PASS_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_REGISTER_COUNT / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) AVG_REGISTER_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_UNPASS_COUNT / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) AVG_UNPASS_COUNT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR((SUM_EXAM_SCORE / REC_COUNT))");
		sql.append("    ELSE 0 END) AVG_EXAM_SCORE,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN FLOOR((SUM_TOTAL_SCORE / REC_COUNT))");
		sql.append("    ELSE 0 END) AVG_TOTAL_SCORE,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_NEVER_STUDY / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) NEVER_STUDY_PERCENT,");
		sql.append("        (CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_STUDY_IMG / REC_COUNT) * 100, 1)");
		sql.append("    ELSE 0 END) STUDY_IMG_PERCENT");
		sql.append("    FROM GJT_TERM_COURSEINFO GTP");
		sql.append("    INNER JOIN GJT_GRADE           GRE ON GRE.IS_DELETED = 'N' AND GTP.TERM_ID = GRE.GRADE_ID");
		sql.append("    INNER JOIN GJT_COURSE          GCE ON GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("    INNER JOIN GJT_CLASS_INFO      GCI ON GCI.IS_DELETED = 'N' AND GTP.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("    left join (select GRR.TERMCOURSE_ID, COUNT(GRR.REC_ID) REC_COUNT,");
		sql.append("        NVL(SUM(NVL(VSS.PROGRESS, 0)), 0) SUM_SCHEDULE,");
		sql.append("        NVL(SUM(NVL(GRR.EXAM_SCORE, 0)), 0) SUM_STUDY_SCORE,");
		sql.append("        NVL(SUM(NVL(GRR.EXAM_SCORE1, 0)), 0) SUM_EXAM_SCORE,");
		sql.append("        NVL(SUM(NVL(GRR.EXAM_SCORE2, 0)), 0) SUM_TOTAL_SCORE,");
		sql.append("        NVL(SUM(NVL(VSS.LOGIN_TIMES, 0)), 0) SUM_LOGIN_COUNT,");
		sql.append("        ROUND(NVL(SUM(NVL(VSS.ONLINE_TIME, 0)), 0) / 60, 1) SUM_LOGIN_TIME,");
		sql.append("        COUNT(CASE WHEN VSS.EXAM_STATE = '2' AND VSS.LOGIN_TIMES = 0 THEN GRR.REC_ID ELSE NULL END) SUM_NEVER_STUDY,");
		sql.append("        COUNT(CASE WHEN GRR.EXAM_STATE = '2' AND VSS.LOGIN_TIMES > 0 THEN GRR.REC_ID ELSE NULL END) SUM_STUDY_IMG,");
		sql.append("        COUNT(CASE WHEN VSS.EXAM_STATE = '1' THEN GRR.REC_ID ELSE NULL END) SUM_PASS_COUNT,");
		sql.append("        COUNT(CASE WHEN VSS.EXAM_STATE = '3' THEN GRR.REC_ID ELSE NULL END) SUM_REGISTER_COUNT,");
		sql.append("        COUNT(CASE WHEN VSS.EXAM_STATE = '0' THEN GRR.REC_ID ELSE NULL END) SUM_UNPASS_COUNT,");
		sql.append("        COUNT(CASE WHEN VSS.LAST_LOGIN_DATE < SYSDATE - 7 THEN GRR.REC_ID ELSE NULL END) DAY7_LOGIN,");
		sql.append("        COUNT(CASE WHEN VSS.LAST_LOGIN_DATE < SYSDATE - 3 AND VSS.LAST_LOGIN_DATE >= SYSDATE - 7 THEN GRR.REC_ID ELSE NULL END) DAY3_7_LOGIN,");
		sql.append("        COUNT(CASE WHEN VSS.LAST_LOGIN_DATE >= SYSDATE - 3 THEN GRR.REC_ID ELSE NULL END) DAY3_LOGIN,");
		sql.append("        COUNT(CASE WHEN VSS.IS_ONLINE = 'Y' THEN GRR.REC_ID ELSE NULL END) ONLINE_STUDENT_COUNT");
		sql.append("    from GJT_REC_RESULT GRR");
		sql.append("    LEFT JOIN VIEW_STUDENT_STUDY_SITUATION VSS ON GRR.REC_ID = VSS.CHOOSE_ID");
		sql.append("    LEFT JOIN GJT_STUDENT_INFO gsi ON gsi.is_deleted='N' AND grr.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("    where GRR.IS_DELETED = 'N'");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}
		sql.append("  	group by GRR.TERMCOURSE_ID) z on z.TERMCOURSE_ID=GTP.TERMCOURSE_ID");
		sql.append("  	WHERE GTP.IS_DELETED = 'N'");
		sql.append("    AND GTP.XX_ID = :xxId");
		params.put("xxId", xxIdParam);

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRE.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GRE.GRADE_NAME LIKE :GRADE_NAME");
			params.put("GRADE_NAME", "%" + ObjectUtils.toString(searchParams.get("GRADE_NAME")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND GCE.KCMC LIKE :KCMC");
			params.put("KCMC", "%" + ObjectUtils.toString(searchParams.get("KCMC")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		sql.append(" ) TAB WHERE 1 = 1");
		sql.append(" AND TAB.REC_COUNT > 0");

		String STUDY_TIMES_SORT = ObjectUtils.toString(searchParams.get("STUDY_TIMES_SORT"));
		String CHOOSE_COUNT_SORT = ObjectUtils.toString(searchParams.get("CHOOSE_COUNT_SORT"));
		String STUDY_COUNT_SORT = ObjectUtils.toString(searchParams.get("STUDY_COUNT_SORT"));
		String DAY7_LOGIN_SORT = ObjectUtils.toString(searchParams.get("DAY7_LOGIN_SORT"));
		String DAY3_7_LOGIN_SORT = ObjectUtils.toString(searchParams.get("DAY3_7_LOGIN_SORT"));
		String DAY3_LOGIN_SORT = ObjectUtils.toString(searchParams.get("DAY3_LOGIN_SORT"));
		String UNSTUDY_COUNT_SORT = ObjectUtils.toString(searchParams.get("UNSTUDY_COUNT_SORT"));
		String STUDYING_COUNT_SORT = ObjectUtils.toString(searchParams.get("STUDYING_COUNT_SORT"));
		// 学习总时长
		boolean flag = false;
		if (EmptyUtils.isNotEmpty(STUDY_TIMES_SORT)) {
			sql.append(" ORDER BY SUM_LOGIN_TIME " + STUDY_TIMES_SORT);
		} else {
			if (EmptyUtils.isEmpty(STUDY_COUNT_SORT) && EmptyUtils.isEmpty(CHOOSE_COUNT_SORT)
					&& EmptyUtils.isEmpty(STUDY_TIMES_SORT) && EmptyUtils.isEmpty(DAY7_LOGIN_SORT)
					&& EmptyUtils.isEmpty(DAY3_7_LOGIN_SORT) && EmptyUtils.isEmpty(DAY3_LOGIN_SORT)
					&& EmptyUtils.isEmpty(UNSTUDY_COUNT_SORT) && EmptyUtils.isEmpty(STUDYING_COUNT_SORT)) {
				sql.append(" ORDER BY SUM_LOGIN_TIME DESC");
			} else {
				sql.append(" ORDER BY ");
				flag = true;
			}
		}
		// 选课人数排序
		if (EmptyUtils.isNotEmpty(CHOOSE_COUNT_SORT)) {
			if (flag) {
				sql.append(" REC_COUNT " + CHOOSE_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,REC_COUNT " + CHOOSE_COUNT_SORT);
			}
		}
		// 学习总次数
		if (EmptyUtils.isNotEmpty(STUDY_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_LOGIN_COUNT " + STUDY_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_LOGIN_COUNT " + STUDY_COUNT_SORT);
			}
		}
		// 7天以上未学习人数
		if (EmptyUtils.isNotEmpty(DAY7_LOGIN_SORT)) {
			if (flag) {
				sql.append(" DAY7_LOGIN " + DAY7_LOGIN_SORT);
				flag = false;
			} else {
				sql.append(" ,DAY7_LOGIN " + DAY7_LOGIN_SORT);
			}
		}
		// 3天以上未学习人数
		if (EmptyUtils.isNotEmpty(DAY3_7_LOGIN_SORT)) {
			if (flag) {
				sql.append(" DAY3_7_LOGIN " + DAY3_7_LOGIN_SORT);
				flag = false;
			} else {
				sql.append(" ,DAY3_7_LOGIN " + DAY3_7_LOGIN_SORT);
			}
		}
		// 3天以内未学习人数
		if (EmptyUtils.isNotEmpty(DAY3_LOGIN_SORT)) {
			if (flag) {
				sql.append(" DAY3_LOGIN " + DAY3_LOGIN_SORT);
				flag = false;
			} else {
				sql.append(" ,DAY3_LOGIN " + DAY3_LOGIN_SORT);
			}
		}
		// 从未学习中人数
		if (EmptyUtils.isNotEmpty(UNSTUDY_COUNT_SORT)) {
			if (flag) {
				sql.append(" NO_DAY_LOGIN " + UNSTUDY_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,NO_DAY_LOGIN " + UNSTUDY_COUNT_SORT);
			}
		}
		//
		if (EmptyUtils.isNotEmpty(STUDYING_COUNT_SORT)) {
			if (flag) {
				sql.append(" ONLINE_STUDENT_COUNT " + STUDYING_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,ONLINE_STUDENT_COUNT " + STUDYING_COUNT_SORT);
			}
		}

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 考勤分析=》课程班考勤
	 *
	 * @return
	 */
	public Page getCourseClassLoginList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseClassLoginListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 考勤分析--》班级考勤无分页
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getCourseClassLoginList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseClassLoginListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForMapListNative(sql, params);
	}

	/**
	 * 课程学情=>课程学情详情
	 *
	 * @param seachParams
	 * @param pageRequst
	 * @return
	 */
	public Page getCourseStudyDetails(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append(
				"  	gtp.TERMCOURSE_ID,gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsr.EXAM_SCORE,gsr.EXAM_SCORE1,gsr.EXAM_SCORE2,grr.COURSE_SCHEDULE,gsr.LOGIN_TIMES LOGIN_COUNT,ROUND( NVL( gsr.ONLINE_TIME, 0 )/ 60, 1 ) LOGIN_TIME,gci.BJMC,");
		sql.append(
				"  	(CASE WHEN grr.EXAM_STATE = '0' THEN '未通过' WHEN grr.EXAM_STATE = '1' THEN '已通过' WHEN grr.EXAM_STATE = '2' THEN '学习中' WHEN grr.EXAM_STATE = '3' THEN '登记中' ELSE '--' END) EXAM_STATE,");
		sql.append(
				"  	gsr.PROGRESS SCHEDULE, (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append(
				"  	gg.GRADE_NAME,gy.NAME YEAR_NAME,gsy.ZYMC,gc.KCMC,GC.KCH,gci.CLASS_ID,gci.CLASS_TYPE,gci.COURSE_ID,gci.SPECIALTY_ID,gci.TEACH_PLAN_ID,");
		sql.append(
				"  	(SELECT g.GRADE_NAME FROM GJT_GRADE g WHERE g.GRADE_ID = gsi.NJ AND g.IS_DELETED = 'N' AND rownum = 1) START_GRADE,");
		sql.append(
				"  	(SELECT gsc.SC_NAME FROM GJT_STUDY_CENTER gsc WHERE gsc.IS_DELETED='N' AND gsc.ID=gsi.XXZX_ID) SC_NAME,");
		sql.append(
				"  	(CASE WHEN SYSDATE >= gg.START_DATE AND SYSDATE <= gg.END_DATE THEN '1' WHEN SYSDATE < gg.START_DATE THEN '2' WHEN SYSDATE > gg.END_DATE THEN '3' ELSE '0' END ) TIME_FLG");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT grr");
		sql.append("  LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON");
		sql.append("  	grr.REC_ID = gsr.CHOOSE_ID");
		sql.append("  LEFT JOIN GJT_TERM_COURSEINFO gtp ON");
		sql.append("  	grr.TERMCOURSE_ID = gtp.TERMCOURSE_ID");
		sql.append("  	AND gtp.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_COURSE gc ON");
		sql.append("  	gtp.COURSE_ID = gc.COURSE_ID");
		sql.append("  LEFT JOIN GJT_GRADE gg ON");
		sql.append("  	gg.GRADE_ID = gtp.TERM_ID");
		sql.append("  LEFT JOIN GJT_YEAR gy ON");
		sql.append("  	gy.GRADE_ID = gg.YEAR_ID,");
		sql.append("  	gjt_class_info gci,");
		sql.append("  	gjt_class_student gcs,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  LEFT JOIN GJT_SPECIALTY GSY ON");
		sql.append("  	gsi.MAJOR = gsy.SPECIALTY_ID");
		sql.append("  WHERE");
		sql.append("  	grr.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND grr.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  	AND gci.CLASS_TYPE = 'course'");
		sql.append("  	AND gci.TERMCOURSE_ID = gtp.TERMCOURSE_ID");
		sql.append("  	AND gtp.COURSE_ID = :courseId");

		param.put("courseId", ObjectUtils.toString(searchParams.get("courseId")));
		
		if(EmptyUtils.isNotEmpty(searchParams.get("gradeId"))) {
			sql.append("	AND gtp.TERM_ID = :gradeId");
			param.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));
		}
		
		
		//限制教学班学员范围
		String teachClassId = ObjectUtils.toString(searchParams.get("TEACH_CLASS_ID"));
		if(StringUtils.isNotEmpty(teachClassId)) {
			sql.append(" AND gsi.student_id IN (SELECT gsi1.student_id from GJT_STUDENT_INFO gsi1 INNER JOIN GJT_CLASS_STUDENT gcs1 ON gsi1.is_deleted = 'N' AND gsi1.student_id = gcs1.student_id INNER JOIN GJT_CLASS_INFO gci1 ON gcs1.is_deleted = 'N' AND gci1.is_deleted = 'N' AND gci1.class_id = gcs1.class_id AND gci1.class_id = :teachClassId)");
			param.put("teachClassId", teachClassId);
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XX_ID")))) {
			sql.append(
					" AND (GSI.XX_ID=:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
			param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			param.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
			sql.append("  	AND gsi.XXZX_ID = :XXZX_ID");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			param.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
			sql.append("  	AND gsi.XM LIKE :XM");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")));
			sql.append("  	AND gsi.XH = :XH");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			param.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
			sql.append("  	AND gsy.SPECIALTY_ID = :SPECIALTY_ID");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("classId")))) {
			param.put("classId", ObjectUtils.toString(searchParams.get("classId")));
			sql.append("  	AND gci.CLASS_ID = :classId");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("examScore")))) {
			param.put("examScore", ObjectUtils.toString(searchParams.get("examScore")));
			sql.append("  	AND grr.EXAM_SCORE " + ObjectUtils.toString(searchParams.get("examSymbol"), "=")
					+ " :examScore");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("schedule")))) {
			param.put("schedule", ObjectUtils.toString(searchParams.get("schedule")));
			sql.append("  	AND gsr.SCHEDULE " + ObjectUtils.toString(searchParams.get("scheduleSymbol"), "=")
					+ " :schedule");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("startGrade")))) {
			param.put("startGrade", ObjectUtils.toString(searchParams.get("startGrade")));
			sql.append("  	AND gsi.NJ = :startGrade");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			param.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
			sql.append("  	AND gsi.PYCC = :PYCC");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("examState")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '0'");
			} else if ("1".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '1'");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '2' AND gsr.LOGIN_TIMES > 0");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '3'");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '2' AND NVL(gsr.LOGIN_TIMES,0) = 0");
			}
		}

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 根据开课状态返回考勤统计数
	 *
	 * @param searchParams
	 * @return
	 */
	public long getCourseLoginCount(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseLoginListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	/**
	 * 考勤分析--》课程考勤sql处理类
	 *
	 * @param searchParams
	 * @return
	 */
	private Map<String, Object> getCourseLoginListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");

		sql.append("  SELECT");
		sql.append(
				"  	COURSE_ID,KCH,SDATE,EDATE,TIME_FLG,GRADE_ID,GRADE_NAME,KCMC,REC_COUNT,SUM_SCHEDULE,SUM_STUDY_SCORE,SUM_EXAM_SCORE,SUM_TOTAL_SCORE,");
		sql.append(
				"  	SUM_LOGIN_COUNT,SUM_LOGIN_TIME,SUM_PASS_COUNT,SUM_REGISTER_COUNT,SUM_UNPASS_COUNT,AVG_SCHEDULE,AVG_STUDY_SCORE,AVG_EXAM_SCORE,AVG_TOTAL_SCORE,AVG_LOGIN_COUNT,");
		sql.append(
				"  	AVG_LOGIN_TIME,AVG_PASS_COUNT,AVG_REGISTER_COUNT,AVG_UNPASS_COUNT,NEVER_STUDY_PERCENT,SUM_NEVER_STUDY,SUM_STUDY_IMG,STUDY_IMG_PERCENT,");
		sql.append(" DAY7_LOGIN,DAY3_7_LOGIN,DAY3_LOGIN,NO_DAY_LOGIN,ONLINE_STUDENT_COUNT");
		sql.append("  FROM (");
		sql.append("  		SELECT gtp.TERMCOURSE_ID,");
		sql.append("  			GCE.COURSE_ID,");
		sql.append("  			GCE.KCH,");
		sql.append("  			TO_CHAR(gre.START_DATE, 'yyyy-mm-dd') SDATE,");
		sql.append("  			TO_CHAR(gre.END_DATE, 'yyyy-mm-dd') EDATE,");
		sql.append("  			GRE.GRADE_ID,");
		sql.append("  			GRE.GRADE_NAME,");
		sql.append("  			GCE.KCMC,");
		sql.append("  			(CASE WHEN SYSDATE >= gre.START_DATE AND SYSDATE <= gre.END_DATE THEN '1'");
		sql.append("  	WHEN SYSDATE < gre.START_DATE THEN '2'");
		sql.append("  	WHEN SYSDATE > gre.END_DATE THEN '3'");
		sql.append("  	ELSE '0' END) TIME_FLG,");
		sql.append("  			z.REC_COUNT,z.SUM_SCHEDULE,z.SUM_STUDY_SCORE,z.SUM_EXAM_SCORE,z.SUM_TOTAL_SCORE,z.SUM_LOGIN_COUNT,z.SUM_LOGIN_TIME,");
		sql.append("  			z.SUM_NEVER_STUDY,z.SUM_STUDY_IMG,z.SUM_PASS_COUNT,z.SUM_REGISTER_COUNT,z.SUM_UNPASS_COUNT,");
		sql.append("			z.DAY7_LOGIN,z.DAY3_7_LOGIN,z.DAY3_LOGIN,z.SUM_NEVER_STUDY NO_DAY_LOGIN,z.ONLINE_STUDENT_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_SCHEDULE / REC_COUNT)");
		sql.append("  	ELSE 0 END) AVG_SCHEDULE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_STUDY_SCORE / REC_COUNT)");
		sql.append("  	ELSE 0 END) AVG_STUDY_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR(SUM_LOGIN_COUNT / REC_COUNT)");
		sql.append("  	ELSE 0 END) AVG_LOGIN_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND(SUM_LOGIN_TIME / REC_COUNT, 1)");
		sql.append("  	ELSE 0 END) AVG_LOGIN_TIME,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_PASS_COUNT / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) AVG_PASS_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_REGISTER_COUNT / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) AVG_REGISTER_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_UNPASS_COUNT / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) AVG_UNPASS_COUNT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR((SUM_EXAM_SCORE / REC_COUNT))");
		sql.append("  	ELSE 0 END) AVG_EXAM_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN FLOOR((SUM_TOTAL_SCORE / REC_COUNT))");
		sql.append("  	ELSE 0 END) AVG_TOTAL_SCORE,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_NEVER_STUDY / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) NEVER_STUDY_PERCENT,");
		sql.append("  			(CASE WHEN REC_COUNT > 0 THEN ROUND((SUM_STUDY_IMG / REC_COUNT) * 100, 1)");
		sql.append("  	ELSE 0 END) STUDY_IMG_PERCENT");
		sql.append("  	FROM GJT_TERM_COURSEINFO GTP");
		sql.append("  	INNER JOIN GJT_GRADE           GRE ON GRE.IS_DELETED = 'N' AND GTP.TERM_ID = GRE.GRADE_ID");
		sql.append("  	INNER JOIN GJT_COURSE          GCE ON GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  	left join (select GRR.TERMCOURSE_ID, COUNT(GRR.REC_ID) REC_COUNT,");
		sql.append("  			NVL(SUM(NVL(VSS.PROGRESS, 0)), 0) SUM_SCHEDULE,");
		sql.append("  			NVL(SUM(NVL(GRR.EXAM_SCORE, 0)), 0) SUM_STUDY_SCORE,");
		sql.append("  			NVL(SUM(NVL(GRR.EXAM_SCORE1, 0)), 0) SUM_EXAM_SCORE,");
		sql.append("  			NVL(SUM(NVL(GRR.EXAM_SCORE2, 0)), 0) SUM_TOTAL_SCORE,");
		sql.append("  			NVL(SUM(NVL(VSS.LOGIN_TIMES, 0)), 0) SUM_LOGIN_COUNT,");
		sql.append("  			ROUND(NVL(SUM(NVL(VSS.ONLINE_TIME, 0)), 0) / 60, 1) SUM_LOGIN_TIME,");
		sql.append("  			COUNT(CASE WHEN VSS.EXAM_STATE = '2' AND VSS.LOGIN_TIMES = 0 THEN GRR.REC_ID ELSE NULL END) SUM_NEVER_STUDY,");
		sql.append("  			COUNT(CASE WHEN GRR.EXAM_STATE = '2' AND VSS.LOGIN_TIMES > 0 THEN GRR.REC_ID ELSE NULL END) SUM_STUDY_IMG,");
		sql.append("  			COUNT(CASE WHEN VSS.EXAM_STATE = '1' THEN GRR.REC_ID ELSE NULL END) SUM_PASS_COUNT,");
		sql.append("  			COUNT(CASE WHEN VSS.EXAM_STATE = '3' THEN GRR.REC_ID ELSE NULL END) SUM_REGISTER_COUNT,");
		sql.append("  			COUNT(CASE WHEN VSS.EXAM_STATE = '0' THEN GRR.REC_ID ELSE NULL END) SUM_UNPASS_COUNT,");
		sql.append("  			COUNT(CASE WHEN VSS.LAST_LOGIN_DATE < SYSDATE - 7 THEN GRR.REC_ID ELSE NULL END) DAY7_LOGIN,");
		sql.append("  			COUNT(CASE WHEN VSS.LAST_LOGIN_DATE < SYSDATE - 3 AND VSS.LAST_LOGIN_DATE >= SYSDATE - 7 THEN GRR.REC_ID ELSE NULL END) DAY3_7_LOGIN,");
		sql.append("  			COUNT(CASE WHEN VSS.LAST_LOGIN_DATE >= SYSDATE - 3 THEN GRR.REC_ID ELSE NULL END) DAY3_LOGIN,");
		sql.append("  			COUNT(CASE WHEN VSS.IS_ONLINE = 'Y' THEN GRR.REC_ID ELSE NULL END) ONLINE_STUDENT_COUNT");
		sql.append("  	from GJT_REC_RESULT GRR ");
		sql.append("  	LEFT JOIN VIEW_STUDENT_STUDY_SITUATION VSS ON GRR.IS_DELETED = 'N' AND GRR.REC_ID = VSS.CHOOSE_ID");
		sql.append("  	LEFT JOIN GJT_STUDENT_INFO gsi ON gsi.is_deleted='N' AND grr.STUDENT_ID = gsi.STUDENT_ID ");
		
		//教学班学员范围,需要关联gjt_class_student,保留教学班学生
		String teachClassId = ObjectUtils.toString(searchParams.get("TEACH_CLASS_ID"));
		if(EmptyUtils.isNotEmpty(teachClassId)) {
			sql.append(" INNER JOIN GJT_CLASS_STUDENT gcs ON gcs.is_deleted = 'N' AND gcs.student_id = gsi.student_id ");
			sql.append(" INNER JOIN GJT_CLASS_INFO gci ON gci.is_deleted = 'N' AND  gci.class_id = gcs.class_id AND gci.class_id = :teachClassId ");
			params.put("teachClassId", teachClassId);
		}
		
		sql.append("  where  1 = 1");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}
		
		
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}
		sql.append("  	group by GRR.TERMCOURSE_ID) z on z.TERMCOURSE_ID=GTP.TERMCOURSE_ID");
		sql.append("  	WHERE GTP.IS_DELETED = 'N'");
		sql.append("    AND GTP.XX_ID = :xxId");
		params.put("xxId", xxIdParam);

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRE.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GRE.GRADE_NAME LIKE :GRADE_NAME");
			params.put("GRADE_NAME", "%" + ObjectUtils.toString(searchParams.get("GRADE_NAME")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND GCE.KCMC LIKE :KCMC");
			params.put("KCMC", "%" + ObjectUtils.toString(searchParams.get("KCMC"), "") + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		sql.append(" ) TAB WHERE 1 = 1");
		sql.append(" AND TAB.REC_COUNT > 0");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TIME_FLG")))) {
			sql.append(" AND TAB.TIME_FLG = :TIME_FLG");
			params.put("TIME_FLG", ObjectUtils.toString(searchParams.get("TIME_FLG")));
		}

		// }
		// DAY7_LOGIN - DAY3_7_LOGIN - DAY3_LOGIN
		String STUDY_TIMES_SORT = ObjectUtils.toString(searchParams.get("STUDY_TIMES_SORT"));
		String CHOOSE_COUNT_SORT = ObjectUtils.toString(searchParams.get("CHOOSE_COUNT_SORT"));
		String STUDY_COUNT_SORT = ObjectUtils.toString(searchParams.get("STUDY_COUNT_SORT"));
		String DAY7_LOGIN_SORT = ObjectUtils.toString(searchParams.get("DAY7_LOGIN_SORT"));
		String DAY3_7_LOGIN_SORT = ObjectUtils.toString(searchParams.get("DAY3_7_LOGIN_SORT"));
		String DAY3_LOGIN_SORT = ObjectUtils.toString(searchParams.get("DAY3_LOGIN_SORT"));
		String UNSTUDY_COUNT_SORT = ObjectUtils.toString(searchParams.get("UNSTUDY_COUNT_SORT"));
		String STUDYING_COUNT_SORT = ObjectUtils.toString(searchParams.get("STUDYING_COUNT_SORT"));
		// 学习总时长
		boolean flag = false;
		if (EmptyUtils.isNotEmpty(STUDY_TIMES_SORT)) {
			sql.append(" ORDER BY SUM_LOGIN_TIME " + STUDY_TIMES_SORT);
		} else {
			if (EmptyUtils.isEmpty(STUDY_COUNT_SORT) && EmptyUtils.isEmpty(CHOOSE_COUNT_SORT)
					&& EmptyUtils.isEmpty(STUDY_TIMES_SORT) && EmptyUtils.isEmpty(DAY7_LOGIN_SORT)
					&& EmptyUtils.isEmpty(DAY3_7_LOGIN_SORT) && EmptyUtils.isEmpty(DAY3_LOGIN_SORT)
					&& EmptyUtils.isEmpty(UNSTUDY_COUNT_SORT) && EmptyUtils.isEmpty(STUDYING_COUNT_SORT)) {
				sql.append(" ORDER BY SUM_LOGIN_TIME DESC");
			} else {
				sql.append(" ORDER BY ");
				flag = true;
			}
		}
		// 选课人数排序
		if (EmptyUtils.isNotEmpty(CHOOSE_COUNT_SORT)) {
			if (flag) {
				sql.append(" REC_COUNT " + CHOOSE_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,REC_COUNT " + CHOOSE_COUNT_SORT);
			}
		}
		// 学习总次数
		if (EmptyUtils.isNotEmpty(STUDY_COUNT_SORT)) {
			if (flag) {
				sql.append(" SUM_LOGIN_COUNT " + STUDY_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,SUM_LOGIN_COUNT " + STUDY_COUNT_SORT);
			}
		}
		// 7天以上未学习人数
		if (EmptyUtils.isNotEmpty(DAY7_LOGIN_SORT)) {
			if (flag) {
				sql.append(" DAY7_LOGIN " + DAY7_LOGIN_SORT);
				flag = false;
			} else {
				sql.append(" ,DAY7_LOGIN " + DAY7_LOGIN_SORT);
			}
		}
		// 3天以上未学习人数
		if (EmptyUtils.isNotEmpty(DAY3_7_LOGIN_SORT)) {
			if (flag) {
				sql.append(" DAY3_7_LOGIN " + DAY3_7_LOGIN_SORT);
				flag = false;
			} else {
				sql.append(" ,DAY3_7_LOGIN " + DAY3_7_LOGIN_SORT);
			}
		}
		// 3天以内未学习人数
		if (EmptyUtils.isNotEmpty(DAY3_LOGIN_SORT)) {
			if (flag) {
				sql.append(" DAY3_LOGIN " + DAY3_LOGIN_SORT);
				flag = false;
			} else {
				sql.append(" ,DAY3_LOGIN " + DAY3_LOGIN_SORT);
			}
		}
		// 从未学习中人数
		if (EmptyUtils.isNotEmpty(UNSTUDY_COUNT_SORT)) {
			if (flag) {
				sql.append(" NO_DAY_LOGIN " + UNSTUDY_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,NO_DAY_LOGIN " + UNSTUDY_COUNT_SORT);
			}
		}
		//
		if (EmptyUtils.isNotEmpty(STUDYING_COUNT_SORT)) {
			if (flag) {
				sql.append(" ONLINE_STUDENT_COUNT " + STUDYING_COUNT_SORT);
				flag = false;
			} else {
				sql.append(" ,ONLINE_STUDENT_COUNT " + STUDYING_COUNT_SORT);
			}
		}

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 考勤分析--》课程考勤
	 *
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getCourseLoginList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseLoginListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 考勤分析--》课程考勤下载
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getCourseLoginList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseLoginListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForMapListNative(sql, params);
	}

	/**
	 * 考勤分析--》课程考勤--》课程考情详情
	 *
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getCourseClockingDetail(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseClockingDetailSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}
	
	/**
	 * 查询指定设备的考勤数量
	 * 规定设备 MAIN_DEVICE
	 * @param searchParams
	 * @return
	 */
	public long getCourseClockingDetailCount(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseClockingDetailSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForCountNative(sql.toString(), params);
	}
	

	/**
	 * 考勤分析--》课程考勤--》课程考情详情无分页
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getCourseClockingDetail(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getCourseClockingDetailSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForMapListNative(sql, params);
	}
	
	/**
	 * 考勤分析--》课程考勤--》课程考情详情sql处理方法
	 *
	 * @param searchParams
	 * @param appointClassType 约定班级类型 : 可选值 course , teach 
	 * @return
	 */
	private Map<String, Object> getCourseClockingDetailSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  SELECT ");
		sql.append(
				"  	STUDENT_ID,XH,XM,SJH,PYCC,PYCC_NAME,GRADE_ID,KCH,GRADE_NAME,SPECIALTY_ID,RUXUE_TERM,RUXUE_YEAR,ZP,EENO,TIME_FLG,");
		sql.append("  	ZYMC,LOGIN_COUNT,TERMCOURSE_ID,COURSE_ID,LOGIN_TIME,LEFT_DAY,LAST_DATE,");
		sql.append("  	IS_ONLINE,BJMC,YEAR_NAME,KCMC,PC_ONLINE_COUNT,APP_ONLINE_COUNT,ALL_ONLINE_COUNT,");
		sql.append(
				"  	(CASE WHEN LOGIN_COUNT > 0 THEN CEIL(PC_ONLINE_COUNT / LOGIN_COUNT*100) ELSE 0 END) PC_ONLINE_PERCENT,");
		sql.append(
				"  	(CASE WHEN LOGIN_COUNT > 0 THEN FLOOR(( 1 - PC_ONLINE_COUNT / LOGIN_COUNT )* 100 ) ELSE 0 END) APP_ONLINE_PERCENT,DEVICE");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append(
				"  			GSI.STUDENT_ID,GSI.XH,GSI.XM,GSI.SJH,GSI.PYCC,GTP.TERMCOURSE_ID,GCE.COURSE_ID,GCE.KCH,gsi.AVATAR ZP,gsi.EENO,");
		sql.append(
				"  			(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append(
				"  			GRE.GRADE_ID,GRE.GRADE_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,NVL( GSR.LOGIN_TIMES, 0 ) LOGIN_COUNT,NVL( ROUND( GSR.ONLINE_TIME / 60,1), 0 ) LOGIN_TIME,");
		sql.append(
				"  			(CASE WHEN SYSDATE >= gre.START_DATE AND SYSDATE <= gre.END_DATE THEN '1' WHEN SYSDATE < gre.START_DATE THEN '2' WHEN SYSDATE > gre.END_DATE THEN '3' ELSE '0' END) TIME_FLG,");
		sql.append(
				"  			FLOOR( SYSDATE - GSR.LAST_LOGIN_DATE ) LEFT_DAY,TO_CHAR( GSR.LAST_LOGIN_DATE, 'yyyy-mm-dd hh24:mi' ) LAST_DATE,GSR.IS_ONLINE,");
		sql.append(
				"  			(SELECT GCI.BJMC FROM GJT_CLASS_INFO GCI,GJT_CLASS_STUDENT GCS WHERE GCI.IS_DELETED = 'N' AND GCS.IS_DELETED = 'N' AND GCI.CLASS_ID = GCS.CLASS_ID    AND GCI.CLASS_TYPE = 'course'");
		
		sql.append("  					AND gci.COURSE_ID = gtp.COURSE_ID AND GCS.STUDENT_ID = GSI.STUDENT_ID ");
		
		//限制课程班级
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("        AND gci.CLASS_ID=:CLASS_ID ");
		}
		
		sql.append("            AND ROWNUM = 1) BJMC,GY.NAME YEAR_NAME,");
		sql.append(
				"  			(SELECT gc.KCMC FROM GJT_COURSE gc WHERE gc.IS_DELETED = 'N' AND gc.COURSE_ID = gtp.COURSE_ID AND ROWNUM = 1) KCMC,");
		sql.append("  			NVL( gsr.PC_ONLINE_COUNT, 0 ) PC_ONLINE_COUNT,");
		sql.append("  			NVL( gsr.APP_ONLINE_COUNT, 0 ) APP_ONLINE_COUNT,");
		sql.append(
				"  			(CASE WHEN gsr.BYOD_TYPE = 'PC' THEN 'PC' WHEN gsr.BYOD_TYPE = 'PHONE' THEN 'APP' WHEN gsr.BYOD_TYPE = 'PAD' THEN 'APP' ELSE '--' END) DEVICE,");
		sql.append("  			(NVL( gsr.APP_ONLINE_COUNT, 0 )+NVL( gsr.APP_ONLINE_COUNT, 0 ))  ALL_ONLINE_COUNT,");
		sql.append("  			(SELECT g.GRADE_NAME FROM GJT_GRADE g WHERE g.GRADE_ID=gsi.NJ) RUXUE_TERM,");
		sql.append(
				"  			(SELECT Y.NAME FROM GJT_GRADE  g LEFT JOIN GJT_YEAR y ON g.YEAR_ID=y.GRADE_ID WHERE g.GRADE_ID=gsi.NJ ) RUXUE_YEAR");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO GSI,");
		sql.append("  			GJT_CLASS_STUDENT GCS,");
		sql.append("  			GJT_CLASS_INFO GCI,");
		sql.append("  			GJT_GRADE GRE,");
		sql.append("  			GJT_YEAR gy,");
		sql.append("  			GJT_SPECIALTY GSY,");
		sql.append("  			GJT_COURSE GCE,");
		sql.append("  			GJT_TERM_COURSEINFO GTP,");
		sql.append(
				"  			GJT_REC_RESULT GRR LEFT JOIN VIEW_TEACH_PLAN vtp ON grr.TEACH_PLAN_ID=vtp.TEACH_PLAN_ID");
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
		sql.append("  			AND GSI.GRADE_SPECIALTY_ID = vtp.GRADE_SPECIALTY_ID");
		sql.append("  			AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  			AND GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  			AND GCI.CLASS_TYPE = 'course'");
		sql.append("  			AND GTP.TERM_ID = GRE.GRADE_ID");
		sql.append("  			AND gy.GRADE_ID = gre.YEAR_ID");
		sql.append("  			AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  			AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  			AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  			AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  			AND vtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID");
		sql.append("  			AND vtp.ACTUAL_GRADE_ID = gtp.TERM_ID");
		sql.append("  			AND GRR.TERMCOURSE_ID = GTP.TERMCOURSE_ID");
		sql.append("  			AND grr.COURSE_ID =:COURSE_ID");
		sql.append(
				"  			AND (gsi.XX_ID=:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");
		

		params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		//限制教学班级的学生范围
		String teachClassId = ObjectUtils.toString(searchParams.get("TEACH_CLASS_ID"));
		if(EmptyUtils.isNotEmpty(teachClassId)) {
			sql.append(" AND gsi.student_id IN (SELECT gcs1.student_id from GJT_CLASS_STUDENT gcs1 JOIN GJT_CLASS_INFO gci1 ON gcs1.is_deleted = 'N' AND gci1.is_deleted = 'N' and gcs1.class_id = gci1.class_id WHERE gci1.class_id = :teachClassId) ");
			params.put("teachClassId", teachClassId);
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("startGrade")))) {
			sql.append("  		AND GSI.NJ = :startGrade");
			params.put("startGrade", ObjectUtils.toString(searchParams.get("startGrade")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("gradeId")))) {
			sql.append("  		AND GTP.TERM_ID = :gradeId");
			params.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  			AND gsi.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  			AND gsi.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  			AND gsi.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  			AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ZYMC")))) {
			sql.append("  			AND GSY.ZYMC LIKE :ZYMC");
			params.put("ZYMC", "%" + ObjectUtils.toString(searchParams.get("ZYMC")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  			AND GCI.CLASS_ID=:CLASS_ID");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			if ("PC".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
				sql.append("  			AND GSR.PC_ONLINE_COUNT>GSR.APP_ONLINE_COUNT");
			} else if ("APP".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
				sql.append("  			AND GSR.PC_ONLINE_COUNT<GSR.APP_ONLINE_COUNT");
			} else if ("NO".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
				sql.append("	AND NVL(GSR.LOGIN_TIMES,0) = 0 ");
			}
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 在线
				sql.append("  			AND GSR.IS_ONLINE = 'Y' AND GSR.LOGIN_TIMES >0 ");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 从未登录
				sql.append("  			AND NVL(GSR.LOGIN_TIMES,0) = 0");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（3天内未学习）
				sql.append("  			AND GSR.LAST_LOGIN_DATE <SYSDATE AND GSR.LAST_LOGIN_DATE >=SYSDATE-3");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（3天以上未学习）
				sql.append("  			AND GSR.LAST_LOGIN_DATE <SYSDATE-3 AND GSR.LAST_LOGIN_DATE >=SYSDATE-7 ");
			} else if ("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线（7天以上未学习）
				sql.append("  			AND GSR.LAST_LOGIN_DATE <SYSDATE-7");
			}
		}
		sql.append("  	) TAB WHERE 1 = 1  ORDER BY XM");

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);

		return handlerMap;
	}

	/**
	 * 学员 课程考勤明细
	 *
	 * @param searchParams
	 * @return
	 */
	public List getStudentLoginDetail(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append(
				"  	gtc.TERMCOURSE_ID,grr.REC_ID,gtp.teach_plan_id,gc.COURSE_ID,GC.KCH,grr.STUDENT_ID,grr.EXAM_SCORE,gsr.IS_ONLINE,gtp.KKXQ,gc.KCMC,gsr.PROGRESS SCHEDULE,");
		sql.append(
				"  	(SELECT gg.GRADE_NAME FROM GJT_GRADE gg WHERE gg.GRADE_ID = gtp.ACTUAL_GRADE_ID AND gg.IS_DELETED = 'N') GRADE_NAME,");
		sql.append(
				"  	NVL( gsr.LOGIN_TIMES, 0 ) LOGIN_COUNT,ROUND( NVL( gsr.ONLINE_TIME / 60, 0 ), 1 ) LOGIN_TIME,TO_CHAR( gsr.LAST_LOGIN_DATE, 'yyyy-MM-dd HH24:mi' ) LAST_DATE,");
		sql.append(
				"  	NVL( gsr.PC_ONLINE_COUNT, 0 ) PC_ONLINE_COUNT,NVL( gsr.APP_ONLINE_COUNT, 0 ) APP_ONLINE_COUNT,FLOOR(SYSDATE-gsr.LAST_LOGIN_DATE) LEFT_DAY,");
		sql.append(
				"  	(CASE WHEN(gsr.APP_ONLINE_COUNT + gsr.PC_ONLINE_COUNT )> 0 THEN ROUND( PC_ONLINE_COUNT /( gsr.APP_ONLINE_COUNT + gsr.PC_ONLINE_COUNT )* 100 ) ELSE 0 END) PC_ONLINE_PERCENT,");
		sql.append(
				"  	(CASE WHEN(gsr.APP_ONLINE_COUNT + gsr.PC_ONLINE_COUNT )> 0 THEN ROUND( APP_ONLINE_COUNT /( gsr.APP_ONLINE_COUNT + gsr.PC_ONLINE_COUNT )* 100 ) ELSE 0 END) APP_ONLINE_PERCENT,");
		sql.append(
				"  	(SELECT tsd.NAME FROM TBL_SYS_DATA tsd WHERE tsd.CODE = gtp.KCLBM AND tsd.TYPE_CODE = 'CourseType') KCLB_NAME");
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

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID"), ""));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	public Page<Map<String, Object>> getStudentStudyList(Map<String, Object> searchParams, Map<String, String> orderMap,
														 PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT GSI.STUDENT_ID STUDENT_ID, ");
		sql.append("       GSI.XH LOGIN_ACCOUNT, GSI.XM NAME, ");
		sql.append("       GSI.IS_ENTERING_SCHOOL IS_ENTERING_SCHOOL, GSI.AVATAR AVATAR, ");
		sql.append("       GS.ZYMC PROFESSION, ");
		sql.append("       GS.ZXF ZXF, ");
		sql.append("       T1.NAME PYCC, ");
		sql.append("       GG.GRADE_NAME GRADE, ");
		sql.append("       T2.NAME CREDIT_STATUS, ");
		sql.append("       (SELECT NVL(SUM(LOGIN_COUNT), 0) SUM_LOGIN_COUNT ");
		sql.append("          FROM GJT_STUDY_RECORD ");
		sql.append("         WHERE IS_DELETED = 'N' ");
		sql.append("           AND STUDENT_ID = GSI.STUDENT_ID) SUM_LOGIN_COUNT, ");
		sql.append("       (SELECT NVL(SUM(LOGIN_TIME), 0) SUM_LOGIN_TIME ");
		sql.append("          FROM GJT_STUDY_RECORD ");
		sql.append("         WHERE IS_DELETED = 'N' ");
		sql.append("           AND STUDENT_ID = GSI.STUDENT_ID) SUM_LOGIN_TIME, ");
		sql.append("       (SELECT NVL(SUM(GET_CREDITS), 0) SUM_GET_CREDITS ");
		sql.append("          FROM GJT_REC_RESULT ");
		sql.append("         WHERE IS_DELETED = 'N' ");
		sql.append("           AND STUDENT_ID = GSI.STUDENT_ID) SUM_GET_CREDITS ");
		sql.append("  FROM (SELECT STUDENT_ID, XH, XM, MAJOR, PYCC, NJ, XJZT, IS_ENTERING_SCHOOL, AVATAR ");
		sql.append("          FROM GJT_STUDENT_INFO ");
		sql.append("         WHERE IS_DELETED = 'N' ");
		sql.append("           AND  XXZX_ID in (:ORG_ID) ) GSI, ");
		sql.append("       GJT_SPECIALTY GS, ");
		sql.append("       TBL_SYS_DATA T1, ");
		sql.append("       GJT_GRADE GG, ");
		sql.append("       TBL_SYS_DATA T2 ");
		sql.append(" WHERE GSI.MAJOR = GS.SPECIALTY_ID ");
		sql.append("   AND GSI.PYCC = T1.CODE ");
		sql.append("   AND T1.TYPE_CODE = 'TrainingLevel' ");
		sql.append("   AND GSI.NJ = GG.GRADE_ID ");
		sql.append("   AND GSI.XJZT = T2.CODE ");
		sql.append("   AND T2.TYPE_CODE = 'StudentNumberStatus' ");
		sql.append("   AND GS.IS_DELETED = 'N' ");
		sql.append("   AND T1.IS_DELETED = 'N' ");
		sql.append("   AND GG.IS_DELETED = 'N' ");
		sql.append("   AND T2.IS_DELETED = 'N' ");

		if (searchParams.get("XXZX_ID") != null) {
			param.put("ORG_ID", searchParams.get("XXZX_ID"));
		}
		if (searchParams.get("GRADE_ID") != null) {
			sql.append("  AND GG.GRADE_ID=:GRADE_ID");
			param.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")).trim());
		}
		if (searchParams.get("NAME") != null) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%" + ObjectUtils.toString(searchParams.get("NAME")).trim() + "%");
		}
		StringBuilder orderBySql = new StringBuilder(" ORDER BY GRADE_NAME DESC ,");
		if (null != orderMap) {
			for (Entry<String, String> entry : orderMap.entrySet()) {
				orderBySql.append(entry.getKey() + " " + entry.getValue() + ",");
			}
		}
		if (orderBySql.length() > 0) {
			sql.append(orderBySql.substring(0, orderBySql.length() - 1));
		}
		// return commonDao.queryForMapListNative(sql.toString(), param);
		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 查询学员学生ID
	 *
	 * @param searchParams
	 * @return
	 */
	public List getStudentId(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.XM,");
		sql.append("  	GSI.XH,");
		sql.append("  	GSI.ATID");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GSI.ATID = :atId ");
		params.put("atId", ObjectUtils.toString(searchParams.get("atId")));
		sql.append("  	AND ROWNUM = 1");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 根据手机号查询学员身份证、姓名、学号
	 *
	 * @param searchParams
	 * @return
	 */
	public List getStudentBaseInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.XM,");
		sql.append("  	GSI.XH,");
		sql.append("  	GSI.SFZH,");
		sql.append("  	GSI.SJH,");
		sql.append("  	GSI.ATID,");
		sql.append("  	GSI.NJ,");
		sql.append("  	GSI.XJZT,");
		sql.append("  	GSI.MAJOR,");
		sql.append("  	GSI.PYCC,");
		sql.append("  	GSI.AVATAR");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GSI.XJZT <> '5' ");
		sql.append("  	AND GSI.SJH = :sjh ");

		params.put("sjh", ObjectUtils.toString(searchParams.get("sjh")));

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 专本连读多个专业情况
	 *
	 * @param searchParams
	 * @return
	 */
	public List getMoreSpecialtyInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GSI.STUDENT_ID,GSI.ACCOUNT_ID,GSI.XX_ID,GSI.XXZX_ID,");
		sql.append("         GSI.XM,GSI.XH,GSI.SJH,GSI.EENO,GSI.CERTIFICATETYPE,GSI.SFZH,");
		sql.append("         GSI.CSRQ AS BIRTHDAY,GSI.DZXX AS EMAIL,GSI.ATID,GSI.USER_ID,");
		sql.append("         GSI.CLASS_TYPE,GSI.FIRSTLOGIN,GSI.AVATAR,GSI.HKSZD,");
		sql.append("         GSI.SC_CO AS SCCO,GSI.WEIXIN,GSI.QQ,GSI.PRE_ZYMC,GSI.XBM,GSI.STATUS,");
		sql.append("         DECODE(GSI.STATUS, '1', 1, 0) CONFIRM,");
		sql.append("         GSI.COMPANY,GSI.USER_TYPE,GSI.LXDH,GSI.ZYH,GSI.BH,");
		sql.append("         GSI.TXDZ AS ADDRESS,GSI.MZM,GSI.NATIVEPLACE JG,");
		sql.append("         GSI.PROVINCE AS PROVINCE_ID,GSI.PROVINCE PROVINCE_CODE,GSI.CITY AS CITY_ID,GSI.CITY CITY_CODE,GSI.AREA AS AREA_ID,GSI.AREA AREA_CODE,");
		sql.append("         (SELECT GCI.CLASS_ID");
		sql.append("            FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("           WHERE GCI.IS_DELETED = 'N'");
		sql.append("             AND GCS.IS_DELETED = 'N'");
		sql.append("             AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("             AND GCI.CLASS_TYPE = 'teach'");
		sql.append("             AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("             AND ROWNUM = 1) CLAZZ,");
		sql.append("         (SELECT GCI.BJMC");
		sql.append("            FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("           WHERE GCI.IS_DELETED = 'N'");
		sql.append("             AND GCS.IS_DELETED = 'N'");
		sql.append("             AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("             AND GCI.CLASS_TYPE = 'teach'");
		sql.append("             AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("             AND ROWNUM = 1) TEACHING_CLASS_NAME,");
		sql.append("         GSI.PYCC,");
		sql.append("         GSI.XJZT,");
		sql.append("         GSP.SPECIALTY_ID,GSP.ZYMC,");
		sql.append("         GS.CHARGE,GS.AUDIT_STATE,");
		sql.append("         GSO.APPID,FB.Org_Name XX_NAME,GO.Org_Name FX_NAME,XXZX.Org_Name XXZX_NAME");
		sql.append("    FROM GJT_STUDENT_INFO GSI");
		sql.append("    INNER JOIN GJT_SIGNUP GS ON GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("    INNER JOIN GJT_SPECIALTY GSP ON GSP.SPECIALTY_ID = GSI.MAJOR");
		sql.append("    LEFT JOIN GJT_SCHOOL_INFO GSO ON GSO.ID = GSI.XX_ID");
        sql.append("    LEFT JOIN GJT_ORG GO ON GO.ID = GSI.XX_ID");
        sql.append("    LEFT JOIN GJT_ORG FB ON FB.ID = GO.PERENT_ID");
		sql.append("    LEFT JOIN GJT_ORG XXZX ON XXZX.ID = GSI.XXZX_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GO.IS_DELETED = 'N'");
		sql.append("  	AND GSI.ACCOUNT_ID IS NOT NULL"); // 已生成账号的
		sql.append("  	AND GSI.XJZT <> '5'");
		// 学员不属于电大的学员
		sql.append("  	AND GO.CODE <> '085'");

		if (EmptyUtils.isNotEmpty(searchParams.get("sfzh"))) {
			sql.append("  	AND GSI.SFZH = :sfzh ");
			params.put("sfzh", ObjectUtils.toString(searchParams.get("sfzh")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("atId"))) {
			sql.append("  	AND GSI.ATID = :atId ");
			params.put("atId", ObjectUtils.toString(searchParams.get("atId")));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询学员专业信息
	 *
	 * @param searchParams
	 * @return
	 */
	public List getSpecialtyInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.XM,");
		sql.append("  	GSI.XX_ID,");
		sql.append("  	GSI.XH,");
		sql.append("  	GSI.PYCC,");
		sql.append("  	GSI.ATID,");
		sql.append("  	GS.SPECIALTY_ID,");
		sql.append("  	GS.ZYMC");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI,");
		sql.append("  	GJT_SPECIALTY GS");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GS.IS_DELETED = 'N'");
		sql.append("  	AND GSI.MAJOR = GS.SPECIALTY_ID");
		sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");

		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询学员入学学期
	 *
	 * @param searchParams
	 * @return
	 */
	public List getRuXueTerm(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GG.GRADE_ID,");
		sql.append("  	GG.GRADE_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE GG,");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GSI.IS_DELETED = 'N'");
		sql.append("  	AND GG.GRADE_ID = GSI.NJ");
		sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");

		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询学期列表
	 *
	 * @param searchParams
	 * @return
	 */
	public List getTermListByLoginStudent(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.KKXQ,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	TAB.TERM_NAME GRADE_NAME,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GG.GRADE_NAME");
		sql.append("  		FROM");
		sql.append("  			GJT_GRADE GG,");
		sql.append("  			GJT_STUDENT_INFO GSI1");
		sql.append("  		WHERE");
		sql.append("  			GG.IS_DELETED = 'N'");
		sql.append("  			AND GSI1.IS_DELETED = 'N'");
		sql.append("  			AND GG.GRADE_ID = GSI1.NJ");

		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("  			AND GSI1.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		sql.append("  	)||(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = TAB.KKXQ");
		sql.append("  			AND TSD.TYPE_CODE = 'KKXQ'");
		sql.append("  	) TERM_NAME,");
		sql.append("  	TAB.START_DATE,");
		sql.append("  	TAB.END_DATE");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT GG.GRADE_CODE TERM_CODE,");
		sql.append("  			GG.GRADE_ID TERM_ID,");
		sql.append("  			GG.GRADE_NAME TERM_NAME,");
		sql.append("  			TO_CHAR( GG.START_DATE, 'yyyy-MM-dd' ) START_DATE,");
		sql.append("  			TO_CHAR( NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 )), 'yyyy-MM-dd' ) END_DATE,");
		sql.append("  			GTP.KKXQ");
		sql.append("  		FROM");
		sql.append("  			GJT_GRADE GG");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			AND GG.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  		LEFT JOIN GJT_REC_RESULT GRR ON");
		sql.append("  			GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  			AND GRR.IS_DELETED = 'N'");
		sql.append("  		INNER JOIN GJT_STUDENT_INFO GSI ON");
		sql.append("  			GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GSI.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("  			AND GSI.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			GTP.KKXQ ASC");
		sql.append("  	) TAB");

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 定位当前学员在第几个开课学期
	 *
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getTermListIndex(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GG2.GRADE_ID,");
		sql.append("  	GG2.GRADE_NAME,");
		sql.append("  	GG2.GRADE_INDEX,");
		sql.append("  	GG2.START_DATE,");
		sql.append("  	GG2.END_DATE");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GG1.GRADE_ID,");
		sql.append("  			GG1.GRADE_NAME,");
		sql.append("  			GG1.START_DATE,");
		sql.append("  			GG1.END_DATE,");
		sql.append("  			RANK() OVER(");
		sql.append("  			ORDER BY");
		sql.append("  				GG1.START_DATE");
		sql.append("  			) AS GRADE_INDEX");
		sql.append("  		FROM");
		sql.append("  			GJT_GRADE GG1");
		sql.append("  		WHERE");
		sql.append("  			GG1.XX_ID = :XX_ID ");
		sql.append("  			AND GG1.IS_DELETED = 'N'");
		sql.append("  			AND GG1.START_DATE >=(");
		sql.append("  				SELECT");
		sql.append("  					GG.START_DATE");
		sql.append("  				FROM");
		sql.append("  					GJT_STUDENT_INFO GSI,");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GSI.NJ = GG.GRADE_ID");
		sql.append("  					AND GSI.IS_DELETED = 'N'");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  					AND GSI.XX_ID = :XX_ID_1 ");
		sql.append("  					AND GSI.STUDENT_ID = :STUDENT_ID_1 ");
		sql.append("  			)");
		sql.append("  	) GG2");
		sql.append("  WHERE");
		sql.append("  	SYSDATE BETWEEN GG2.START_DATE AND NVL( GG2.END_DATE, ADD_MONTHS(GG2.START_DATE, 4 ))");
		sql.append("  	AND ROWNUM = 1");

		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("XX_ID_1", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("STUDENT_ID_1", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	public List<Map<String, Object>> countByOrgIdsAndSpecialtyName(List<String> orgIdList, String specialtyName) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1),gs.zymc");
		sql.append("  from (select student_id, major");
		sql.append("          from gjt_student_info");
		sql.append("         where is_deleted = 'N' ");
		sql.append("           and xxzx_id in (:orgId) ) gsi,");
		sql.append("       gjt_specialty gs");
		sql.append(" where gs.specialty_id = gsi.major");
		sql.append("   and gs.is_deleted = 'N' ");
		if (StringUtils.isNotEmpty(specialtyName)) {
			sql.append("   and gs.zymc like :specialtyName ");
		}
		sql.append(" group by gs.zymc");
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("orgId", orgIdList);
		if (StringUtils.isNotEmpty(specialtyName)) {
			query.setParameter("specialtyName", "%" + specialtyName + "%");
		}
		List<Object[]> list = query.getResultList();
		List<Map<String, Object>> resultList = Lists.newArrayList();
		for (Object[] objects : list) {
			try {
				BigDecimal count = (BigDecimal) objects[0];
				String specialtyNameStr = ObjectUtils.toString(objects[1]);
				Map<String, Object> m = Maps.newHashMap();
				m.put("specialtyName", specialtyNameStr);
				m.put("count", count.intValue());
				resultList.add(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/**
	 * 学情详情导出dao处理方法
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getCommonCourseDetail(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append(
				"  	gtp.TERMCOURSE_ID,gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsr.EXAM_SCORE,gsr.EXAM_SCORE1,gsr.EXAM_SCORE2,grr.COURSE_SCHEDULE,gsr.LOGIN_TIMES LOGIN_COUNT,ROUND( NVL( gsr.ONLINE_TIME, 0 )/ 60, 1 ) LOGIN_TIME,gci.BJMC,");
		sql.append(
				"  	(CASE WHEN gsr.EXAM_STATE = '0' THEN '未通过' WHEN gsr.EXAM_STATE = '1' THEN '已通过' WHEN gsr.EXAM_STATE = '2' THEN '学习中' WHEN gsr.EXAM_STATE = '3' THEN '登记中' ELSE '--' END) EXAM_STATE,");
		sql.append(
				"  	gsr.PROGRESS SCHEDULE, (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append(
				"  	gg.GRADE_NAME,gy.NAME YEAR_NAME,gsy.ZYMC,VTP.KCMC,VTP.KCLBM,gci.CLASS_ID,gci.CLASS_TYPE,gci.COURSE_ID,gci.SPECIALTY_ID,gci.TEACH_PLAN_ID,");
		sql.append(
				"  	(SELECT g.GRADE_NAME FROM GJT_GRADE g WHERE g.GRADE_ID = gsi.NJ AND g.IS_DELETED = 'N' AND rownum = 1) START_GRADE,");
		sql.append(
				"  	(SELECT gsc.SC_NAME FROM GJT_STUDY_CENTER gsc WHERE gsc.IS_DELETED='N' AND gsc.ID=gsi.XXZX_ID) SC_NAME,");
		sql.append(
				"  	(CASE WHEN SYSDATE >= gg.START_DATE AND SYSDATE <= gg.END_DATE THEN '1' WHEN SYSDATE < gg.START_DATE THEN '2' WHEN SYSDATE > gg.END_DATE THEN '3' ELSE '0' END ) TIME_FLG");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT grr");
		sql.append("  INNER JOIN view_teach_plan VTP ON VTP.TEACH_PLAN_ID=grr.TEACH_PLAN_ID");
		sql.append("  LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON");
		sql.append("  	grr.REC_ID = gsr.CHOOSE_ID");
		sql.append("  LEFT JOIN GJT_TERM_COURSEINFO gtp ON");
		sql.append("  	grr.TERMCOURSE_ID = gtp.TERMCOURSE_ID");
		sql.append("  	AND gtp.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_GRADE gg ON");
		sql.append("  	gg.GRADE_ID = gtp.TERM_ID");
		sql.append("  LEFT JOIN GJT_YEAR gy ON");
		sql.append("  	gy.GRADE_ID = gg.YEAR_ID,");
		sql.append("  	gjt_class_info gci,");
		sql.append("  	gjt_class_student gcs,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  LEFT JOIN GJT_SPECIALTY GSY ON");
		sql.append("  	gsi.MAJOR = gsy.SPECIALTY_ID");
		sql.append("  WHERE");
		sql.append("  	grr.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND grr.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  	AND gci.CLASS_TYPE = 'course'");
		sql.append("  	AND gci.TERMCOURSE_ID = gtp.TERMCOURSE_ID");

		
		//限制教学班学员范围
		String teachClassId = ObjectUtils.toString(searchParams.get("TEACH_CLASS_ID"));
		if(StringUtils.isNotEmpty(teachClassId)) {
			sql.append(" AND gsi.student_id IN (SELECT gsi1.student_id from GJT_STUDENT_INFO gsi1 INNER JOIN GJT_CLASS_STUDENT gcs1 ON gsi1.is_deleted = 'N' AND gsi1.student_id = gcs1.student_id INNER JOIN GJT_CLASS_INFO gci1 ON gci1.is_deleted = 'N' AND gci1.class_id = gcs1.class_id AND gci1.class_id = :teachClassId)");
			params.put("teachClassId", teachClassId);
		}
		
		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("courseId")))) {
			sql.append("  	AND gtp.COURSE_ID = :courseId");
			params.put("courseId", ObjectUtils.toString(searchParams.get("courseId")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  	AND gtp.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("gradeId")))) {
			sql.append("	AND gtp.TERM_ID = :gradeId");
			params.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("	AND gsi.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
			sql.append("  	AND gsi.XM LIKE :XM");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
			sql.append("  	AND gsi.XH = :XH");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
			sql.append("  	AND gsy.SPECIALTY_ID = :SPECIALTY_ID");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("classId")))) {
			params.put("classId", ObjectUtils.toString(searchParams.get("classId")));
			sql.append("  	AND gci.CLASS_ID = :classId");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
			sql.append("  	AND gci.CLASS_ID = :CLASS_ID");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("examScore")))) {
			params.put("examScore", ObjectUtils.toString(searchParams.get("examScore")));
			sql.append("  	AND grr.EXAM_SCORE " + ObjectUtils.toString(searchParams.get("examSymbol"), "=")
					+ " :examScore");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("schedule")))) {
			params.put("schedule", ObjectUtils.toString(searchParams.get("schedule")));
			sql.append("  	AND gsr.SCHEDULE " + ObjectUtils.toString(searchParams.get("scheduleSymbol"), "=")
					+ " :schedule");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("startGrade")))) {
			params.put("startGrade", ObjectUtils.toString(searchParams.get("startGrade")));
			sql.append("  	AND gsi.NJ = :startGrade");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
			sql.append("  	AND gsi.PYCC = :PYCC");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("examState")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '0'");
			} else if ("1".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '1'");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '2'  AND gsr.LOGIN_TIMES > 0");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '3'");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("examState")))) {
				sql.append("  	AND grr.EXAM_STATE = '2' AND NVL(gsr.LOGIN_TIMES,0) = 0");
			}
		}
		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 查询学籍信息(企业大学平台接口)
	 *
	 * @param searchParams
	 * @return
	 */
	public Page getRollDataInfo(Map searchParams, PageRequest pageRequest) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	T.STUDENT_ID,");
		sql.append("  	T.XH,");
		sql.append("  	T.XM,");
		sql.append("  	T.SJH,");
		sql.append("  	T.SFZH,");
		sql.append("  	T.XBM,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'Sex'");
		sql.append("  			AND TSD.CODE = T.XBM");
		sql.append("  	) XBM_NAME,");
		sql.append("  	B.GRADE_ID,");
		sql.append("  	B.GRADE_NAME,");
		sql.append("  	C.SPECIALTY_ID,");
		sql.append("  	C.ZYMC,");
		sql.append("  	T.PYCC,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  			AND TSD.CODE = T.PYCC");
		sql.append("  	) PYCC_NAME,");
		sql.append("  	D.XXMC,");
		sql.append("  	T.XJZT,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'StudentNumberStatus'");
		sql.append("  			AND TSD.CODE = T.XJZT");
		sql.append("  	) XJZT_NAME ");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO T");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = T.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.specialty_id = T.major");
		sql.append("  	AND C.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = T.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = T.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			student_id S_ID,");
		sql.append("  			audit_state FLOW_AUDIT_STATE,");
		sql.append("  			audit_operator_role FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					f.student_id,");
		sql.append("  					f.audit_state,");
		sql.append("  					f.audit_operator_role,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY f.student_id");
		sql.append("  					ORDER BY");
		sql.append("  						f.created_dt DESC,");
		sql.append("  						f.audit_operator_role DESC");
		sql.append("  					) id");
		sql.append("  				FROM");
		sql.append("  					gjt_flow_record f");
		sql.append("  				WHERE");
		sql.append("  					f.is_deleted = 'N'");
		sql.append("  			) temp");
		sql.append("  		WHERE");
		sql.append("  			temp.id = 1");
		sql.append("  	) temp2 ON");
		sql.append("  	temp2.s_id = t.student_id");
		sql.append("  WHERE");
		sql.append("  	T.IS_DELETED = 'N'");

		sql.append("  	AND T.XXZX_ID IN(");
		sql.append("  		SELECT");
		sql.append("  			org.ID");
		sql.append("  		FROM");
		sql.append("  			GJT_ORG org");
		sql.append("  		WHERE");
		// sql.append(" org.IS_DELETED = 'N' START WITH org.ID = :XXZX_ID
		// CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

		sql.append("  			org.IS_DELETED = 'N' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");
		sql.append("  	)");

		/*
		 * if(EmptyUtils.isNotEmpty(searchParams.get("XXZX_ID"))){ sql.append(
		 * "  	AND T.XXZX_ID IN("); sql.append("  		SELECT"); sql.append(
		 * "  			org.ID"); sql.append("  		FROM"); sql.append(
		 * "  			GJT_ORG org"); sql.append("  		WHERE"); sql.append(
		 * "  			org.IS_DELETED = 'N' START WITH org.ID = :XXZX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID"
		 * ); sql.append("  	)");
		 * params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")
		 * )); }else { if (EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
		 * sql.append("  	AND T.XXZX_ID IN("); sql.append("  		SELECT");
		 * sql.append("  			org.ID"); sql.append("  		FROM");
		 * sql.append("  			GJT_ORG org"); sql.append("  		WHERE");
		 * sql.append(
		 * "  			org.IS_DELETED = 'N' START WITH org.ID = :XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID"
		 * ); sql.append("  	)");
		 * params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		 * } }
		 */

		sql.append("  	AND B.AUDIT_STATE = '1' ");

		// 年级
		if (EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))) {
			sql.append("  	AND B.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		// 专业
		if (EmptyUtils.isNotEmpty(searchParams.get("SPECIALTY_ID"))) {
			sql.append("  	AND C.SPECIALTY_ID= :SPECIALTY_ID ");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		// 层次
		if (EmptyUtils.isNotEmpty(searchParams.get("PYCC"))) {
			sql.append("  	AND T.PYCC= :PYCC ");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		// 学生状态
		if (EmptyUtils.isNotEmpty(searchParams.get("XJZT"))) {
			sql.append("  	AND T.XJZT = :XJZT ");
			params.put("XJZT", ObjectUtils.toString(searchParams.get("XJZT")));
		}

		// 姓名
		if (EmptyUtils.isNotEmpty(searchParams.get("XM"))) {
			sql.append("  	AND T.XM LIKE :XM ");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")).trim() + "%");
		}

		// 性别
		if (EmptyUtils.isNotEmpty(searchParams.get("XBM"))) {
			sql.append("  	AND T.XBM = :XBM ");
			params.put("XBM", ObjectUtils.toString(searchParams.get("XBM")));
		}

		return (Page) commonDao.queryForPageNative(sql.toString(), params, pageRequest);

	}

	/**
	 * 学籍信息统计项
	 *
	 * @param searchParams
	 * @return
	 */
	public List getRollCountInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	T.STUDENT_ID,");
		sql.append("  	T.XH,");
		sql.append("  	T.XM,");
		sql.append("  	T.XBM,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'Sex'");
		sql.append("  			AND TSD.CODE = T.XBM");
		sql.append("  	) XBM_NAME,");
		sql.append("  	B.GRADE_ID,");
		sql.append("  	B.GRADE_NAME,");
		sql.append("  	C.SPECIALTY_ID,");
		sql.append("  	C.ZYMC,");
		sql.append("  	T.PYCC,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  			AND TSD.CODE = T.PYCC");
		sql.append("  	) PYCC_NAME,");
		sql.append("  	D.XXMC,");
		sql.append("  	T.XJZT,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'StudentNumberStatus'");
		sql.append("  			AND TSD.CODE = T.XJZT");
		sql.append("  	) XJZT_NAME ");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO T");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = T.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.specialty_id = T.major");
		sql.append("  	AND C.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = T.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = T.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			student_id S_ID,");
		sql.append("  			audit_state FLOW_AUDIT_STATE,");
		sql.append("  			audit_operator_role FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					f.student_id,");
		sql.append("  					f.audit_state,");
		sql.append("  					f.audit_operator_role,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY f.student_id");
		sql.append("  					ORDER BY");
		sql.append("  						f.created_dt DESC,");
		sql.append("  						f.audit_operator_role DESC");
		sql.append("  					) id");
		sql.append("  				FROM");
		sql.append("  					gjt_flow_record f");
		sql.append("  				WHERE");
		sql.append("  					f.is_deleted = 'N'");
		sql.append("  			) temp");
		sql.append("  		WHERE");
		sql.append("  			temp.id = 1");
		sql.append("  	) temp2 ON");
		sql.append("  	temp2.s_id = t.student_id");
		sql.append("  WHERE");
		sql.append("  	T.IS_DELETED = 'N'");

		sql.append("  	AND T.XXZX_ID IN(");
		sql.append("  		SELECT");
		sql.append("  			org.ID");
		sql.append("  		FROM");
		sql.append("  			GJT_ORG org");
		sql.append("  		WHERE");
		// sql.append(" org.IS_DELETED = 'N' START WITH org.ID = :XXZX_ID
		// CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

		sql.append("  			org.IS_DELETED = 'N' ");

		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");

		sql.append("  	)");

		/*
		 * if(EmptyUtils.isNotEmpty(searchParams.get("XXZX_ID"))){ sql.append(
		 * "  	AND T.XXZX_ID IN("); sql.append("  		SELECT"); sql.append(
		 * "  			org.ID"); sql.append("  		FROM"); sql.append(
		 * "  			GJT_ORG org"); sql.append("  		WHERE"); sql.append(
		 * "  			org.IS_DELETED = 'N' START WITH org.ID = :XXZX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID"
		 * ); sql.append("  	)");
		 * params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")
		 * )); }else { if (EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
		 * sql.append("  	AND T.XXZX_ID IN("); sql.append("  		SELECT");
		 * sql.append("  			org.ID"); sql.append("  		FROM");
		 * sql.append("  			GJT_ORG org"); sql.append("  		WHERE");
		 * sql.append(
		 * "  			org.IS_DELETED = 'N' START WITH org.ID = :XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID"
		 * ); sql.append("  	)");
		 * params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		 * } }
		 */

		sql.append("  	AND B.AUDIT_STATE = '1' ");

		// 年级
		if (EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))) {
			sql.append("  	AND B.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		// 专业
		if (EmptyUtils.isNotEmpty(searchParams.get("SPECIALTY_ID"))) {
			sql.append("  	AND C.SPECIALTY_ID= :SPECIALTY_ID ");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		// 层次
		if (EmptyUtils.isNotEmpty(searchParams.get("PYCC"))) {
			sql.append("  	AND T.PYCC= :PYCC ");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		// 条件检索
		if ("0".equals(ObjectUtils.toString(searchParams.get("FLAG")))) {
			// 学生状态
			if (EmptyUtils.isNotEmpty(searchParams.get("XJZT"))) {
				sql.append("  	AND T.XJZT = :XJZT ");
				params.put("XJZT", ObjectUtils.toString(searchParams.get("XJZT")));
			}
		}

		// 姓名
		if (EmptyUtils.isNotEmpty(searchParams.get("XM"))) {
			sql.append("  	AND T.XM LIKE :XM ");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		// 性别
		if (EmptyUtils.isNotEmpty(searchParams.get("XBM"))) {
			sql.append("  	AND T.XBM = :XBM ");
			params.put("XBM", ObjectUtils.toString(searchParams.get("XBM")));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询学期
	 *
	 * @param searchParams
	 * @return
	 */
	public List getGradeData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		GjtOrg xxzx = gjtOrgDao.queryByCode((String) searchParams.get("XXZX_CODE"));
		sql.append("SELECT GRADE_ID,GRADE_NAME FROM (");
		sql.append("  SELECT");
		sql.append("  	DISTINCT G.GRADE_ID,G.GRADE_NAME,G.START_DATE");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE G");
		sql.append("  INNER JOIN GJT_STUDENT_INFO B ON B.IS_DELETED='N' AND B.NJ=G.GRADE_ID AND B.XXZX_ID='" + xxzx.getId() + "'");
		sql.append("  WHERE");
		sql.append("  	G.IS_DELETED = 'N'");
		sql.append("  	AND G.IS_ENABLED = '1'");
		sql.append("  	AND G.XX_ID =(");
		sql.append("  		SELECT");
		sql.append("  			ORG.ID");
		sql.append("  		FROM");
		sql.append("  			GJT_ORG ORG");
		sql.append("  		WHERE");
		sql.append("  			ORG.IS_DELETED = 'N'");
		// sql.append(" AND ORG.ORG_TYPE = '1' START WITH ORG.ID = :XXZX_ID
		// CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID");

		sql.append("  			AND ORG.ORG_TYPE = '1' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH ORG.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH ORG.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		sql.append("  			CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID ");

		sql.append("  	)");
		sql.append(") TEMP");
		sql.append("  ORDER BY START_DATE DESC");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询专业信息
	 *
	 * @param searchParams
	 * @return
	 */
	public List getSpecialtyData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		GjtOrg xxzx = gjtOrgDao.queryByCode((String) searchParams.get("XXZX_CODE"));
		sql.append("  SELECT");
		sql.append("  	DISTINCT SPECIALTY_ID,");
		sql.append("  	ZYMC");
		sql.append("  FROM");
		sql.append("  	GJT_SPECIALTY T");
		sql.append("  INNER JOIN GJT_STUDENT_INFO B ON B.IS_DELETED='N' AND B.MAJOR=T.SPECIALTY_ID AND B.XXZX_ID='" + xxzx.getId() + "'");
		sql.append("  WHERE");
		sql.append("  	T.IS_DELETED = 'N'");
		sql.append("  	AND T.IS_ENABLED = '1'");
		sql.append("  	AND T.XX_ID =(");
		sql.append("  		SELECT");
		sql.append("  			ORG.ID");
		sql.append("  		FROM");
		sql.append("  			GJT_ORG ORG");
		sql.append("  		WHERE");
		sql.append("  			ORG.IS_DELETED = 'N'");
		// sql.append(" AND ORG.ORG_TYPE = '1' START WITH ORG.ID = :XXZX_ID
		// CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID");

		sql.append("  			AND ORG.ORG_TYPE = '1' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH ORG.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH ORG.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		sql.append("  			CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID ");
		sql.append("  	)");

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 查询层次
	 *
	 * @param searchParams
	 * @return
	 */
	public List getPyccData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	CODE PYCC,");
		sql.append("  	NAME PYCC_NAME ");
		sql.append("  FROM");
		sql.append("  	TBL_SYS_DATA");
		sql.append("  WHERE");
		sql.append("  	TBL_SYS_DATA.TYPE_CODE = 'TrainingLevel'");
		sql.append("  	AND IS_DELETED = 'N'");
		sql.append("  	AND(");
		sql.append("  		ORG_ID IS NULL");
		// sql.append(" OR ORG_ID = :XXZX_ID ");

		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append(" OR CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  OR ORG_ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  	)");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询学籍状态
	 *
	 * @param searchParams
	 * @return
	 */
	public List getXjztData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TSD.CODE XJZT,");
		sql.append("  	TSD.NAME XJZT_NAME");
		sql.append("  FROM");
		sql.append("  	TBL_SYS_DATA TSD");
		sql.append("  WHERE");
		sql.append("  	TSD.IS_DELETED = 'N'");
		sql.append("  	AND TSD.TYPE_CODE = 'StudentNumberStatus'");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询性别
	 *
	 * @param searchParams
	 * @return
	 */
	public List getSexData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TSD.CODE XBM,");
		sql.append("  	TSD.NAME XBM_NAME");
		sql.append("  FROM");
		sql.append("  	TBL_SYS_DATA TSD");
		sql.append("  WHERE");
		sql.append("  	TSD.IS_DELETED = 'N'");
		sql.append("  	AND TSD.TYPE_CODE = 'Sex'");

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 查询考试计划
	 *
	 * @param searchParams
	 * @return
	 */
	public List getExamBatchInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GEB.EXAM_BATCH_CODE,");
		sql.append("  	GEB.NAME");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GEB.IS_DELETED = 0");
		sql.append("  	AND GEB.PLAN_STATUS = '3'");
		sql.append("  	AND GEB.XX_ID IN(");
		sql.append("  		SELECT");
		sql.append("  			org.ID");
		sql.append("  		FROM");
		sql.append("  			GJT_ORG org");
		sql.append("  		WHERE");
		// sql.append(" org.IS_DELETED = 'N' START WITH org.ID = :XXZX_ID
		// CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

		sql.append("  			org.IS_DELETED = 'N' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  			CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID ");
		sql.append("  	)");

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 查询考试科目
	 *
	 * @param searchParams
	 * @return
	 */
	public List getExamPlanInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_PLAN_ID,");
		sql.append("  	GEP.EXAM_PLAN_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 0");

		if (EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))) {
			sql.append("  	AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		sql.append("  	AND GEP.XX_ID IN(");
		sql.append("  		SELECT");
		sql.append("  			org.ID");
		sql.append("  		FROM");
		sql.append("  			GJT_ORG org");
		sql.append("  		WHERE");
		// sql.append(" org.IS_DELETED = 'N' START WITH org.ID = :XXZX_ID
		// CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

		sql.append("  			org.IS_DELETED = 'N' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		sql.append("  			CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID ");
		sql.append("  	)");

		/*
		 * if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_ID"))){ sql.append(
		 * "  	AND GEP.XX_ID IN("); sql.append("  		SELECT"); sql.append(
		 * "  			org.ID"); sql.append("  		FROM"); sql.append(
		 * "  			GJT_ORG org"); sql.append("  		WHERE"); sql.append(
		 * "  			org.IS_DELETED = 'N' START WITH org.ID = :XXZX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID"
		 * ); sql.append("  	)");
		 *
		 * params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")
		 * )); }
		 */

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询考试形式
	 *
	 * @param searchParams
	 * @return
	 */
	public List getExaminationMode(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TSD.CODE,");
		sql.append("  	TSD.NAME");
		sql.append("  FROM");
		sql.append("  	TBL_SYS_DATA TSD");
		sql.append("  WHERE");
		sql.append("  	TSD.IS_DELETED = 'N'");
		sql.append("  	AND TSD.TYPE_CODE = 'ExaminationMode'");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询课程信息
	 *
	 * @param searchParams
	 * @return
	 */
	public List getCourseInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GC.COURSE_ID,");
		sql.append("  	GC.KCMC");
		sql.append("  FROM");
		sql.append("  	GJT_COURSE GC");
		sql.append("  WHERE");
		sql.append("  	GC.IS_DELETED = 'N'");
		sql.append("  	AND GC.XX_ID IN(");
		sql.append("  		SELECT");
		sql.append("  			org.ID");
		sql.append("  		FROM");
		sql.append("  			GJT_ORG org");
		sql.append("  		WHERE");
		// sql.append(" org.IS_DELETED = 'N' START WITH org.ID = :XXZX_ID
		// CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");
		sql.append("  			org.IS_DELETED = 'N' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  			CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID ");
		sql.append("  	)");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 获得学员的成绩统计(企业大学平台接口)
	 *
	 * @param searchParams
	 * @return
	 */
	public int getExamScoreCount(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT COUNT(STUDENT_ID)");
		sql.append("  FROM (SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GRE.GRADE_ID,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GYR.NAME YEAR_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GEB.EXAM_BATCH_CODE,");
		sql.append("  GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.EXAM_PLAN_ID,");
		sql.append("  GEP.TYPE EXAM_PLAN_TYPE,");
		sql.append("  GEP.COURSE_ID,");
		sql.append("  (SELECT GRR.EXAM_STATE");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GRR.COURSE_ID = GEP.COURSE_ID");
		sql.append("  AND ROWNUM = 1) EXAM_STATE");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_SPECIALTY            GSY,");
		sql.append("  GJT_GRADE                GRE,");
		sql.append("  GJT_YEAR                 GYR,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW       GEB,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GRE.YEAR_ID = GYR.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  AND GSI.STUDENT_ID = GEA.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRE.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSI.MAJOR = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")))) {
			sql.append("  AND GEP.EXAM_PLAN_ID = :EXAM_PLAN_ID");
			params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		}

		sql.append("  AND GSI.XXZX_ID IN");
		sql.append("  (SELECT org.ID");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_CODE")))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)) TAB");
		sql.append("  WHERE 1 = 1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")))) {
			if ("4".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")))) {
				sql.append("  AND EXAM_STATE IS NULL");
			} else {
				sql.append("  AND EXAM_STATE = :EXAM_STATE");
				params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")));
			}
		}
		BigDecimal num = (BigDecimal) commonDao.queryObjectNative(sql.toString(), params);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	/**
	 * 获得学员的成绩(企业大学平台接口)
	 *
	 * @param searchParams
	 * @return
	 */
	public Page getExamScoreList(Map searchParams, PageRequest pageRequest) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GRE.GRADE_ID,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GYR.NAME YEAR_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GEB.EXAM_BATCH_CODE,");
		sql.append("  GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.EXAM_PLAN_ID,");
		sql.append("  GEP.TYPE EXAM_TYPE,");
		sql.append("  GEP.COURSE_ID,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GEP.TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') EXAM_TYPE_NAME,");
		sql.append("  NVL((SELECT GRR.EXAM_STATE");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GRR.COURSE_ID = GEP.COURSE_ID");
		sql.append("  AND ROWNUM = 1),");
		sql.append("  4) EXAM_STATE");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_SPECIALTY            GSY,");
		sql.append("  GJT_GRADE                GRE,");
		sql.append("  GJT_YEAR                 GYR,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW       GEB,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GRE.YEAR_ID = GYR.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  AND GSI.STUDENT_ID = GEA.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GRE.GRADE_ID = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSI.MAJOR = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")))) {
			sql.append("  AND GEP.EXAM_PLAN_ID = :EXAM_PLAN_ID");
			params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		}

		sql.append("  AND GSI.XXZX_ID IN");
		sql.append("  (SELECT org.ID");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_CODE")))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)) TAB");
		sql.append("  WHERE 1 = 1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
			if ("4".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND EXAM_STATE IS NULL");
			} else {
				sql.append("  AND EXAM_STATE = :EXAM_STATE");
				params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
			}
		}
		return (Page) commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}

	public List getLearningRank(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	T.STUDENT_ID,T.XM,T.XH,T.AVATAR,");
		sql.append("  	T.GRADE_ID,");
		sql.append("  	T.GRADE_NAME,");
		sql.append("  	T.LOGIN_TIMES,");
		sql.append("  	T.ONLINE_TIME,");
		sql.append("  	T.STD_RANK");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TAB.STUDENT_ID,");
		sql.append("  			TAB.XM,");
		sql.append("  			TAB.XH,TAB.AVATAR,");
		sql.append("  			TAB.GRADE_ID,");
		sql.append("  			TAB.GRADE_NAME,");
		sql.append("  			NVL(TAB.LOGIN_TIMES, 0) LOGIN_TIMES,");
		sql.append("  			NVL(TAB.ONLINE_TIME, 0) ONLINE_TIME,");
		sql.append("  			ROW_NUMBER() OVER(");
		sql.append("  			ORDER BY");
		sql.append("  				NVL(TAB.ONLINE_TIME, 0) DESC");
		sql.append("  			) STD_RANK");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GSI.STUDENT_ID,");
		sql.append("  					GSI.XM,");
		sql.append("  					GSI.XH,GSI.AVATAR,");
		sql.append("  					GG.GRADE_ID,");
		sql.append("  					GG.GRADE_NAME,");
		sql.append("  					(");
		sql.append("  						SELECT");
		sql.append("  							SUM( GSR.LOGIN_TIMES )");
		sql.append("  						FROM");
		sql.append("  							VIEW_STUDENT_STUDY_SITUATION GSR");
		sql.append("  						WHERE");
		sql.append("  							GSI.STUDENT_ID = GSR.STUDENT_ID");
		sql.append("  					) LOGIN_TIMES,");
		sql.append("  					(");
		sql.append("  						SELECT");
		sql.append("  							ROUND( SUM( GSR.ONLINE_TIME )/ 60, 2 )");
		sql.append("  						FROM");
		sql.append("  							VIEW_STUDENT_STUDY_SITUATION GSR");
		sql.append("  						WHERE");
		sql.append("  							GSI.STUDENT_ID = GSR.STUDENT_ID");
		sql.append("  					) ONLINE_TIME");
		sql.append("  				FROM");
		sql.append("  					GJT_STUDENT_INFO GSI");
		sql.append("  				LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  					B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  				LEFT JOIN GJT_GRADE GG ON");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = B.GRADE_ID");
		sql.append("  				WHERE");
		sql.append("  					GSI.IS_DELETED = 'N'");
		sql.append("  					AND GSI.XXZX_ID IN(");
		sql.append("  						SELECT");
		sql.append("  							org.ID");
		sql.append("  						FROM");
		sql.append("  							GJT_ORG org");
		sql.append("  						WHERE");

		sql.append("  			org.IS_DELETED = 'N' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");
		sql.append("  	)");

		if (EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))) {
			sql.append("  	AND GG.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		sql.append("  			) TAB");
		sql.append("  		ORDER BY");
		sql.append("  			ONLINE_TIME DESC");
		sql.append("  	) T");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		sql.append("  	AND ROWNUM <= 3");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 学习平台答疑统计
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getSubjectList(Map<String, Object> searchParams) {

		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT LTT.TERM_NAME,");
		sql.append("  LCC.COURSE_NAME,");
		sql.append("  LMS.SUBJECT_TITLE,");
		sql.append(" dbms_lob.substr(LMS.SUBJECT_CONTENT,4000,1) SUBJECT_CONTENT,");
		sql.append("  (SELECT LUS.REALNAME");
		sql.append("  FROM LCMS_USER_STUD LUS");
		sql.append("  WHERE LUS.ISDELETED = 'N'");
		sql.append("  AND LUS.STUD_ID = LMS.USER_ID) ask_student,");
		sql.append("  (SELECT dbms_lob.substr(wm_concat(DISTINCT LMR.REPLY_CONTENT))");
		sql.append("  FROM LCMS_MUTUAL_REPLY LMR, LCMS_USER_MANAGER LUM");
		sql.append("  WHERE LMR.ISDELETED = 'N'");
		sql.append("  AND LUM.ISDELETED = 'N'");
		sql.append("  AND LMR.SUBJECT_ID = LMS.SUBJECT_ID");
		sql.append("  AND LMR.USER_ID = LUM.MANAGER_ID) ask_content,");
		sql.append("  TO_CHAR(LMS.CREATED_DT, 'yyyy-MM-dd hh24:mi:ss') student_ask_time,");
		sql.append("  (SELECT dbms_lob.substr(wm_concat(DISTINCT LUM.REALNAME))");
		sql.append("  FROM LCMS_MUTUAL_REPLY LMR, LCMS_USER_MANAGER LUM");
		sql.append("  WHERE LMR.ISDELETED = 'N'");
		sql.append("  AND LUM.ISDELETED = 'N'");
		sql.append("  AND LMR.SUBJECT_ID = LMS.SUBJECT_ID");
		sql.append("  AND LMR.USER_ID = LUM.MANAGER_ID) reply_teacher,");
		sql.append("  (SELECT LMR.REPLY_CONTENT");
		sql.append("  FROM LCMS_MUTUAL_REPLY LMR");
		sql.append("  WHERE LMR.ISDELETED = 'N'");
		sql.append("  AND LMR.SUBJECT_ID = LMS.SUBJECT_ID");
		sql.append("  AND LMR.Parent_Id is null and rownum=1 ) reply_content,");
		sql.append("  (SELECT TO_CHAR(LMR.Created_Dt, 'yyyy-MM-dd hh24:mi:ss')");
		sql.append("  FROM LCMS_MUTUAL_REPLY LMR");
		sql.append("  WHERE LMR.ISDELETED = 'N'");
		sql.append("  AND LMR.SUBJECT_ID = LMS.SUBJECT_ID");
		sql.append("  AND LMR.Parent_Id is null and rownum=1) reply_time     ");
		sql.append("  FROM LCMS_TERM_TERMINFO     LTT,");
		sql.append("  LCMS_TERM_COURSEINFO   LTC,");
		sql.append("  LCMS_COURSE_COURSEINFO LCC,");
		sql.append("  LCMS_MUTUAL_SUBJECT    LMS");
		sql.append("  WHERE LTT.ISDELETED = 'N'");
		sql.append("  AND LTC.ISDELETED = 'N'");
		sql.append("  AND LMS.ISDELETED = 'N'");
		sql.append("  AND LCC.ISDELETED = 'N'");
		sql.append("  AND LCC.COURSE_ID = LTC.COURSE_ID");
		sql.append("  AND LTC.TERM_ID = LTT.TERM_ID");
		sql.append("  AND LTC.TERMCOURSE_ID = LMS.TERMCOURSE_ID");
		sql.append("  AND LMS.USER_ID NOT IN ('testClass5',");
		sql.append("  'testClass4',");
		sql.append("  'testClass3',");
		sql.append("  'testClass2',");
		sql.append("  'testClass1')");
		sql.append("  AND LTT.APP_ID = 'APP014'");
		sql.append("  AND LMS.CREATED_DT >= TO_DATE (:startDate, 'yyyy-MM-dd')");
		sql.append("  AND LMS.CREATED_DT < TO_DATE (:endDate, 'yyyy-MM-dd')");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", searchParams.get("startDate"));
		params.put("endDate", searchParams.get("endDate"));
		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	public List<Map<String, String>> getCourseActivityList(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT LFF.FORUM_ID,");
		sql.append("  LTO.TERM_NAME ,");
		sql.append("  LCC.COURSE_NAME ,");
		sql.append("  LFF.FORUM_TITLE,");
		sql.append("  LFF.FORUM_CONTENT,");
		sql.append("  TO_CHAR(LFF.CREATED_DT, 'yyyy-mm-dd hh24:mi:ss') CREATED_DT");
		sql.append("  FROM LCMS_TERM_COURSEINFO   LTC,");
		sql.append("  LCMS_COURSE_COURSEINFO LCC,");
		sql.append("  LCMS_TERM_TERMINFO     LTO,");
		sql.append("  LCMS_TERMCOURSE_TASK   LTT,");
		sql.append("  LCMS_TERMCOURSE_ACT    LTA,");
		sql.append("  LCMS_FORUM_FORUMINFO   LFF");
		sql.append("  WHERE LTT.ISDELETED = 'N'");
		sql.append("  AND LTC.ISDELETED = 'N'");
		sql.append("  AND LCC.ISDELETED = 'N'");
		sql.append("  AND LTO.ISDELETED = 'N'");
		sql.append("  AND LTA.ISDELETED = 'N'");
		sql.append("  AND LFF.ISDELETED = 'N'");
		sql.append("  AND LTC.TERMCOURSE_ID = LTT.TERMCOURSE_ID");
		sql.append("  AND LTC.COURSE_ID = LCC.COURSE_ID");
		sql.append("  AND LTC.TERM_ID = LTO.TERM_ID");
		sql.append("  AND LTT.TASK_ID = LTA.TASK_ID");
		sql.append("  AND LTA.ACT_ID = LFF.FORUM_ID");
		sql.append("  and ltc.app_id='APP014'");
		sql.append("  and LFF.Created_Dt >= TO_DATE  (:startDate, 'yyyy-MM-dd')");
		sql.append("  and LFF.Created_Dt < TO_DATE  (:endDate, 'yyyy-MM-dd')");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", searchParams.get("startDate"));
		params.put("endDate", searchParams.get("endDate"));
		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	public long getCourseActivityCount(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT LFF.FORUM_ID ");
		sql.append("  FROM LCMS_TERM_COURSEINFO   LTC,");
		sql.append("  LCMS_COURSE_COURSEINFO LCC,");
		sql.append("  LCMS_TERM_TERMINFO     LTO,");
		sql.append("  LCMS_TERMCOURSE_TASK   LTT,");
		sql.append("  LCMS_TERMCOURSE_ACT    LTA,");
		sql.append("  LCMS_FORUM_FORUMINFO   LFF");
		sql.append("  WHERE LTT.ISDELETED = 'N'");
		sql.append("  AND LTC.ISDELETED = 'N'");
		sql.append("  AND LCC.ISDELETED = 'N'");
		sql.append("  AND LTO.ISDELETED = 'N'");
		sql.append("  AND LTA.ISDELETED = 'N'");
		sql.append("  AND LFF.ISDELETED = 'N'");
		sql.append("  AND LTC.TERMCOURSE_ID = LTT.TERMCOURSE_ID");
		sql.append("  AND LTC.COURSE_ID = LCC.COURSE_ID");
		sql.append("  AND LTC.TERM_ID = LTO.TERM_ID");
		sql.append("  AND LTT.TASK_ID = LTA.TASK_ID");
		sql.append("  AND LTA.ACT_ID = LFF.FORUM_ID");
		sql.append("  and ltc.app_id='APP014'");
		sql.append("  and LFF.Created_Dt >= TO_DATE  (:startDate, 'yyyy-MM-dd')");
		sql.append("  and LFF.Created_Dt < TO_DATE  (:endDate, 'yyyy-MM-dd')");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", searchParams.get("startDate"));
		params.put("endDate", searchParams.get("endDate"));
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	// public List<Map<String, String>> getRealyCourseActivityList(String id) {
	// StringBuffer sql = new StringBuffer();
	// sql.append(" SELECT lfry.forum_content, lfry.user_name,
	// lfry.created_dt");
	// sql.append(" FROM LCMS_FORUM_REPLY LFRY");
	// sql.append(" WHERE LFRY.ISDELETED = 'N'");
	// sql.append(" AND LFRY.PARENT_ID = :id");
	// Map<String, Object> searchParams = new HashMap<String, Object>();
	// searchParams.put("id", id);
	// return commonDao.queryForMapListNative(sql.toString(), searchParams);
	// }

	// 查询主题下面的回复。回复下面的讨论
	public List<Map<String, String>> getRealyActivityList(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT lfry.forum_content, lfry.user_name, lfry.created_dt");
		sql.append("  ,lfry.parent_id FROM LCMS_FORUM_REPLY LFRY");
		sql.append("  WHERE LFRY.ISDELETED = 'N'");
		sql.append("  AND LFRY.FORUM_ID = :id order by lfry.created_dt desc");
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("id", id);
		return commonDao.queryForMapListNative(sql.toString(), searchParams);
	}
	


	/**
	 * 学生综合信息查询=》链接
	 *
	 * @return
	 */
	public Page getStudentLinkList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentLinkListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 学生综合信息查询=》链接
	 *
	 * @return
	 */
	public long getStudentLinkCount(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentLinkListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	/**
	 * 学生综合信息查询=》链接
	 *
	 * @return
	 */
	public List<Map<String, Object>> getStudentLinkList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentLinkListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForStringObjectMapListNative(sql, params);
	}

	/**
	 * 学生综合信息查询=》链接 sql处理类
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> getStudentLinkListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append(" SELECT * FROM (");
		sql.append("   SELECT GRADE_ID,CLASS_ID,YEAR_NAME,GRADE_NAME,START_GRADE,");
		sql.append("            STUDENT_ID,XM,XH,SJH,PYCC,PYCC_NAME,XXZX_ID,SC_NAME,ZP,EENO,XJZT,USER_TYPE,PERFECT_STATUS,");
		sql.append("            SPECIALTY_ID,BJMC,ZYMC,ZXF,ZDBYXF,");
		sql.append("            IS_BANDING_WX,LOGIN_TIME_COUNT,LAST_LOGIN_TIME,PC_MAX_LAST_LOGIN_TIME,PC_LAST_LOGIN_TIME,APP_MAX_LAST_LOGIN_TIME,APP_LAST_LOGIN_TIME,");
		sql.append("            'PC' PC_DEVICE,");
		sql.append("            NVL((SELECT vss2.IS_ONLINE FROM VIEW_STUDENT_STUDY_SITUATION vss2");
		sql.append("         		WHERE vss2.STUDENT_ID = x.STUDENT_ID AND vss2.LAST_LOGIN_DATE=PC_MAX_LAST_LOGIN_TIME AND ROWNUM = 1), 'N') PC_IS_ONLINE,");
		sql.append("            'APP' APP_DEVICE,");
		sql.append("            NVL((SELECT vss2.IS_ONLINE FROM VIEW_STUDENT_STUDY_SITUATION vss2");
		sql.append("         		WHERE vss2.STUDENT_ID = x.STUDENT_ID AND vss2.LAST_LOGIN_DATE=APP_MAX_LAST_LOGIN_TIME AND ROWNUM = 1), 'N') APP_IS_ONLINE");
		sql.append("   from (");
		sql.append("        SELECT");
		sql.append("           GSI.STUDENT_ID,GR.GRADE_ID,GCI.CLASS_ID,GR.GRADE_NAME,GR.GRADE_NAME START_GRADE,GCI.BJMC,gsi.XXZX_ID,gsc.SC_NAME,");
		sql.append("           GSI.XM,GSI.XH,GSI.SJH,GSI.PYCC,GY.NAME YEAR_NAME,gsi.AVATAR ZP,gsi.EENO,GSI.XJZT,GSI.USER_TYPE,TSD.NAME PYCC_NAME,GSI.PERFECT_STATUS,");
		sql.append("           GSY.SPECIALTY_ID,GSY.ZYMC,NVL(GSY.ZDBYXF, 0) ZDBYXF,");
		sql.append("           NVL(SUM(NVL(GTP.XF, 0)), 0) ZXF,");
		sql.append("           (CASE WHEN GUA.WX_OPENID IS NOT NULL THEN 1 ELSE 0 END) IS_BANDING_WX,");
		sql.append("           ROUND(NVL(SUM(NVL(vss.ONLINE_TIME, 0)), 0) / 60, 1) LOGIN_TIME_COUNT,");
		sql.append("           FLOOR(SYSDATE - MAX(vss.LAST_LOGIN_DATE)) LAST_LOGIN_TIME,");
		sql.append("           MAX(CASE WHEN vss.BYOD_TYPE = 'PC' THEN vss.LAST_LOGIN_DATE ELSE NULL END) PC_MAX_LAST_LOGIN_TIME,");
		sql.append("           FLOOR(SYSDATE - MAX(CASE WHEN vss.BYOD_TYPE = 'PC' THEN vss.LAST_LOGIN_DATE ELSE NULL END)) PC_LAST_LOGIN_TIME,");
		sql.append("           MAX(CASE WHEN vss.BYOD_TYPE = 'PHONE' OR vss.BYOD_TYPE = 'PAD' THEN vss.LAST_LOGIN_DATE ELSE NULL END) APP_MAX_LAST_LOGIN_TIME,");
		sql.append("           FLOOR(SYSDATE - MAX(CASE WHEN vss.BYOD_TYPE = 'PHONE' OR vss.BYOD_TYPE = 'PAD' THEN vss.LAST_LOGIN_DATE ELSE NULL END)) APP_LAST_LOGIN_TIME");
		sql.append("      FROM GJT_STUDENT_INFO  GSI");
		sql.append("      INNER JOIN     GJT_USER_ACCOUNT GUA ON GUA.ID=GSI.ACCOUNT_ID");
		sql.append("      INNER JOIN     GJT_GRADE         GR ON GR.IS_DELETED = 'N' AND GR.GRADE_ID = GSI.NJ");
		sql.append("      INNER JOIN     GJT_YEAR          GY ON GY.GRADE_ID = GR.YEAR_ID");
		sql.append("      INNER JOIN     GJT_SPECIALTY     GSY ON GSY.IS_DELETED = 'N' AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("      INNER JOIN     GJT_CLASS_STUDENT GCS ON GCS.IS_DELETED = 'N' AND GCS.Student_Id=GSI.Student_Id");
		sql.append("      INNER JOIN     GJT_CLASS_INFO    GCI ON GCI.IS_DELETED = 'N' AND GCI.Class_Id=GCS.Class_Id AND GCI.CLASS_TYPE = 'teach'");
		sql.append("      INNER JOIN     GJT_REC_RESULT GRR ON GRR.IS_DELETED = 'N' AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("      INNER JOIN     VIEW_TEACH_PLAN GTP ON GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("      LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID = vss.CHOOSE_ID");
		sql.append("      LEFT JOIN     GJT_STUDY_CENTER gsc ON gsc.IS_DELETED = 'N' AND gsc.ID = gsi.XXZX_ID");
		sql.append("      LEFT JOIN     TBL_SYS_DATA TSD ON TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'TrainingLevel' AND TSD.CODE = GSI.PYCC");
		sql.append("     WHERE GSI.IS_DELETED = 'N'");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND gsi.STUDENT_ID =:STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_NAME")))) {
			sql.append("  AND GR.GRADE_NAME LIKE :GRADE_NAME");
			params.put("GRADE_NAME", "%" + ObjectUtils.toString(searchParams.get("GRADE_NAME")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND gsi.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		sql.append("     GROUP BY ");
		sql.append("           GSI.STUDENT_ID,GR.GRADE_ID,GCI.CLASS_ID,GR.GRADE_NAME,GR.GRADE_NAME,GCI.BJMC,gsi.XXZX_ID,gsc.SC_NAME,");
		sql.append("           GSI.XM,GSI.XH,GSI.SJH,GSI.PYCC,GY.NAME,gsi.AVATAR,gsi.EENO,GSI.XJZT,GSI.USER_TYPE,TSD.NAME,GSI.PERFECT_STATUS,");
		sql.append("           GSY.SPECIALTY_ID,GSY.ZYMC,NVL(GSY.ZDBYXF, 0),(CASE WHEN GUA.WX_OPENID IS NOT NULL THEN 1 ELSE 0 END)");
		sql.append("   ) x");
		sql.append("   ) TAB WHERE 1=1");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND TAB.BJMC LIKE :BJMC");
			params.put("BJMC", "%" + ObjectUtils.toString(searchParams.get("BJMC")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 链接正常
				sql.append("  			AND PC_MAX_LAST_LOGIN_TIME IS NOT NULL AND APP_MAX_LAST_LOGIN_TIME IS NOT NULL AND LAST_LOGIN_TIME<=7 AND IS_BANDING_WX=1");
			} if ("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 链接异常-未使用PC或者未安装APP或者离线（7天以上未学习）或者未绑定公众号
				sql.append("  			AND (PC_MAX_LAST_LOGIN_TIME IS NULL OR APP_MAX_LAST_LOGIN_TIME IS NULL OR LAST_LOGIN_TIME>7 OR IS_BANDING_WX=0)");
			} if ("2".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 未安装APP
				sql.append("  			AND APP_MAX_LAST_LOGIN_TIME IS NULL");
			} if ("3".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 未使用PC
				sql.append("  			AND PC_MAX_LAST_LOGIN_TIME IS NULL");
			} if ("4".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 未关注微信公众号
				sql.append("  			AND IS_BANDING_WX=0");
			} if ("5".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 已关注微信公众号
				sql.append("  			AND IS_BANDING_WX=1");
			} if ("6".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 已安装APP
				sql.append("  			AND APP_MAX_LAST_LOGIN_TIME IS NOT NULL");
			} if ("8".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 已使用PC
				sql.append("  			AND PC_MAX_LAST_LOGIN_TIME IS NOT NULL");
			}
		}

		sql.append(" ORDER BY LOGIN_TIME_COUNT DESC NULLS LAST");

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 学员学习情况统计
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> countStudentStudySituation(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  SELECT ROUND(AVG(NVL(PC_ONLINE_COUNT, 0))) AVG_PC_LOGIN,");
		sql.append("         ROUND(AVG(NVL(APP_ONLINE_COUNT, 0))) AVG_APP_LOGIN,");
		sql.append("         ROUND(AVG(NVL(ALL_ONLINE_COUNT, 0))) AVG_LOGIN,");
		sql.append("         COUNT(CASE LOGIN_TIMES WHEN 0 THEN NULL ELSE 1 END) STUDY_STUDENT_NUM,");
		sql.append("         ROUND(AVG(NVL(LOGIN_TIME_COUNT, 0)), 1) AVG_STUDY_TIME,");
		sql.append("         COUNT(CASE BEHIND_REC_COUNT WHEN 0 THEN NULL ELSE 1 END) BEHIND_STUDENT_NUM");
		sql.append("   FROM (SELECT GSI.STUDENT_ID,");
		sql.append("               SUM(NVL(vss.PC_ONLINE_COUNT, 0)) PC_ONLINE_COUNT,");
		sql.append("               SUM(NVL(vss.APP_ONLINE_COUNT, 0)) APP_ONLINE_COUNT,");
		sql.append("               SUM(NVL(vss.APP_ONLINE_COUNT, 0)) + SUM(NVL(vss.PC_ONLINE_COUNT, 0)) ALL_ONLINE_COUNT,");
		sql.append("               NVL(SUM(NVL(vss.LOGIN_TIMES, 0)), 0) LOGIN_TIMES,");
		sql.append("               ROUND(NVL(SUM(NVL(vss.ONLINE_TIME, 0)), 0) / 60, 1) LOGIN_TIME_COUNT,");
		sql.append("               COUNT(CASE WHEN vss.STATE = '1' THEN GRR.REC_ID ELSE NULL END) BEHIND_REC_COUNT");
		sql.append("          FROM GJT_STUDENT_INFO GSI");
		sql.append("         INNER JOIN GJT_REC_RESULT GRR");
		sql.append("            ON GRR.IS_DELETED = 'N'");
		sql.append("           AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("          LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss");
		sql.append("            ON grr.REC_ID = vss.CHOOSE_ID");
		sql.append("         WHERE GSI.IS_DELETED = 'N'");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND gsi.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		sql.append("     GROUP BY GSI.STUDENT_ID) TAB");
		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(), params);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

}
