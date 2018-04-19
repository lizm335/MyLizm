package com.ouchgzee.headTeacher.daoImpl;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.student.ScorePointDao;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by llx on 2017/02/28.
 */
@Deprecated @Repository("bzrScorePointDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class ScorePointDaoImpl implements ScorePointDao{

	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactoryBzr")
	public EntityManager em;


	/**
	 * 学支平台--学习管理=》成绩查询sql处理
	 * @param searchParams
	 * @return
	 */
	public Map<String,Object> getScoreListSqlHandler(Map searchParams){
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  SELECT * FROM (");
		sql.append("  SELECT ");
		sql.append("  	  GSI.STUDENT_ID,GSI.XH,GSI.XM,GSI.SJH,GSI.PYCC,GSI.AVATAR ZP,GY.NAME YEAR_NAME,GTP.KSFS,vss.LOGIN_TIMES STUDY_TIMES,ROUND(NVL(vss.ONLINE_TIME,0)/60,1) ONLINE_TIME,");
		sql.append("  	  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  	  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GTP.KSFS AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME,");
		sql.append("  	  GRE.GRADE_ID,GRE.GRADE_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,GTP.KKXQ,GCE.COURSE_ID,GCE.KCMC,GCE.KCH,NVL(GTP.XF, 0) XF,NVL(TO_CHAR(GRR.COURSE_SCHEDULE),'') KCXXBZ,");
		sql.append("  	  NVL(GTP.KCKSBZ, 0) KCKSBZ,GRR.EXAM_SCORE,GRR.EXAM_SCORE1,GRR.EXAM_SCORE2,GRR.EXAM_STATE,GRR.GET_CREDITS,VSS.PROGRESS,vss.STATE,");
		sql.append("  (CASE WHEN vss.STATE='0' THEN '未学习' WHEN vss.STATE='1' THEN '落后' WHEN vss.STATE='2' THEN '正常' WHEN vss.STATE='3' THEN '学霸' WHEN vss.STATE='4' THEN '考核通过' ELSE '--' END ) STATE_NAME,");
		sql.append("  	  (SELECT gci.BJMC FROM GJT_CLASS_INFO gci,GJT_CLASS_STUDENT gcs ");
		sql.append("  	  	WHERE gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='teach' AND ROWNUM=1) BJMC,");
		sql.append("  	  (SELECT gei.XM FROM GJT_CLASS_INFO gci LEFT JOIN GJT_EMPLOYEE_INFO gei ON gci.BZR_ID=gei.EMPLOYEE_ID, GJT_CLASS_STUDENT gcs ");
		sql.append("  	  	WHERE gei.EMPLOYEE_TYPE='1' AND  gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='teach' AND ROWNUM=1) BZR_NAME,");
		sql.append("  	  (SELECT gei.XM FROM GJT_CLASS_INFO gci LEFT JOIN GJT_EMPLOYEE_INFO gei ON gci.BZR_ID=gei.EMPLOYEE_ID, GJT_CLASS_STUDENT gcs ");
		sql.append("  	  	WHERE gei.EMPLOYEE_TYPE='2' AND  gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='course' AND gci.COURSE_ID=gce.COURSE_ID  AND ROWNUM=1) FD_NAME,");
		sql.append("  	  TO_CHAR((SELECT (CASE  WHEN GGR.START_DATE > SYSDATE THEN 0 ELSE 1 END) FROM GJT_GRADE GGR WHERE GGR.IS_DELETED = 'N' AND GGR.GRADE_ID = GTP.ACTUAL_GRADE_ID)) STUDY_FLG,");
		sql.append("  	  (SELECT TSD.NAME FROM GJT_EXAM_APPOINTMENT_NEW GEA,GJT_EXAM_PLAN_NEW_COURSE GEP,TBL_SYS_DATA TSD WHERE GEA.IS_DELETED = 0 AND TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  	  	AND GEA.TYPE = TSD.CODE AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID AND GEA.STUDENT_ID = GRR.STUDENT_ID AND GEP.COURSE_ID = GRR.COURSE_ID AND ROWNUM = 1) EXAM_PLAN_KSFS_NAME");
		sql.append("  	  FROM GJT_STUDENT_INFO GSI,");
		sql.append("  	  GJT_CLASS_STUDENT gcs,");
		sql.append("  	  GJT_CLASS_INFO gci,");
		sql.append("  	  GJT_GRADE GRE,");
		sql.append("  	  GJT_YEAR GY,");
		sql.append("  	  GJT_SPECIALTY GSY,");
		sql.append("  	  GJT_COURSE GCE,");
		sql.append("  	  VIEW_TEACH_PLAN GTP,");
		sql.append("  	  GJT_REC_RESULT GRR LEFT JOIN VIEW_STUDENT_STUDY_SITUATION VSS ON GRR.REC_ID=VSS.CHOOSE_ID ");
		sql.append("  	  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  	  AND gcs.IS_DELETED = 'N'");
		sql.append("  	  AND gci.IS_DELETED = 'N'");
		sql.append("  	  AND GRE.IS_DELETED = 'N'");
		sql.append("  	  AND GSY.IS_DELETED = 'N'");
		sql.append("  	  AND GRR.IS_DELETED = 'N'");
		sql.append("  	  AND GCE.IS_DELETED = 'N'");
		sql.append("  	  AND GTP.IS_DELETED = 'N'");
		sql.append("  	  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  	  AND GRE.YEAR_ID = GY.GRADE_ID");
		sql.append("  	  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  	  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  	  AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	  AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("	  AND gci.CLASS_TYPE = 'teach'");
		sql.append("  	  AND gci.CLASS_ID = :CLASS_ID");
		sql.append("  	  AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  	  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("	  AND (GSI.XX_ID=:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");


		params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("CLASS_ID",ObjectUtils.toString(searchParams.get("CLASS_ID")));


		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
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
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
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

		sql.append("  ORDER BY gtp.KKXQ,GSI.XM");

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

		Map<String,Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql",sql.toString());
		handlerMap.put("params",params);
		return handlerMap;
	}

	/**
	 * 学习管理=》成绩查询
	 * @return
	 */
	public Page getScoreList(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.getScoreListSqlHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(),params,pageRequst);
	}

	/**
	 * 成绩管理=》成绩查询无分页
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, Object>> getScoreListNoPage(Map searchParams){
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap = this.getScoreListSqlHandler(searchParams);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForMapListNative(sql,params);
	}

	/**
	 * 学习管理=》成绩查询（查询条件统计项）
	 * @return
	 */
	public int getScoreCount(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT COUNT(STUDENT_ID) FROM ( ");
		sql.append("  SELECT ");
		sql.append("  	  GSI.STUDENT_ID,GSI.XH,GSI.XM,GSI.SJH,GSI.PYCC,GSI.AVATAR ZP,GY.NAME YEAR_NAME,GTP.KSFS,vss.LOGIN_TIMES STUDY_TIMES,ROUND(NVL(vss.ONLINE_TIME,0)/60,1) ONLINE_TIME,");
		sql.append("  	  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  	  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GTP.KSFS AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME,");
		sql.append("  	  GRE.GRADE_ID,GRE.GRADE_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,GTP.KKXQ,GCE.COURSE_ID,GCE.KCMC,GCE.KCH,NVL(GTP.XF, 0) XF,NVL(TO_CHAR(GRR.COURSE_SCHEDULE),'') KCXXBZ,");
		sql.append("  	  NVL(GTP.KCKSBZ, 0) KCKSBZ,GRR.EXAM_SCORE,GRR.EXAM_SCORE1,GRR.EXAM_SCORE2,GRR.EXAM_STATE,GRR.GET_CREDITS,VSS.PROGRESS,vss.STATE,");
		sql.append("  (CASE WHEN vss.STATE='0' THEN '未学习' WHEN vss.STATE='1' THEN '落后' WHEN vss.STATE='2' THEN '正常' WHEN vss.STATE='3' THEN '学霸' WHEN vss.STATE='4' THEN '考核通过' ELSE '--' END ) STATE_NAME,");
		sql.append("  	  (SELECT gci.BJMC FROM GJT_CLASS_INFO gci,GJT_CLASS_STUDENT gcs ");
		sql.append("  	  	WHERE gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='teach' AND ROWNUM=1) BJMC,");
		sql.append("  	  (SELECT gei.XM FROM GJT_CLASS_INFO gci LEFT JOIN GJT_EMPLOYEE_INFO gei ON gci.BZR_ID=gei.EMPLOYEE_ID, GJT_CLASS_STUDENT gcs ");
		sql.append("  	  	WHERE gei.EMPLOYEE_TYPE='1' AND  gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='teach' AND ROWNUM=1) BZR_NAME,");
		sql.append("  	  (SELECT gei.XM FROM GJT_CLASS_INFO gci LEFT JOIN GJT_EMPLOYEE_INFO gei ON gci.BZR_ID=gei.EMPLOYEE_ID, GJT_CLASS_STUDENT gcs ");
		sql.append("  	  	WHERE gei.EMPLOYEE_TYPE='2' AND  gci.IS_DELETED='N' AND gcs.IS_DELETED='N' AND gci.CLASS_ID=gcs.CLASS_ID AND gcs.STUDENT_ID=gsi.STUDENT_ID AND gci.CLASS_TYPE='course' AND gci.COURSE_ID=gce.COURSE_ID  AND ROWNUM=1) FD_NAME,");
		sql.append("  	  TO_CHAR((SELECT (CASE  WHEN GGR.START_DATE > SYSDATE THEN 0 ELSE 1 END) FROM GJT_GRADE GGR WHERE GGR.IS_DELETED = 'N' AND GGR.GRADE_ID = GTP.ACTUAL_GRADE_ID)) STUDY_FLG,");
		sql.append("  	  (SELECT TSD.NAME FROM GJT_EXAM_APPOINTMENT_NEW GEA,GJT_EXAM_PLAN_NEW_COURSE GEP,TBL_SYS_DATA TSD WHERE GEA.IS_DELETED = 0 AND TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  	  	AND GEA.TYPE = TSD.CODE AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID AND GEA.STUDENT_ID = GRR.STUDENT_ID AND GEP.COURSE_ID = GRR.COURSE_ID AND ROWNUM = 1) EXAM_PLAN_KSFS_NAME");
		sql.append("  	  FROM GJT_STUDENT_INFO GSI,");
		sql.append("  	  GJT_CLASS_STUDENT gcs,");
		sql.append("  	  GJT_CLASS_INFO gci,");
		sql.append("  	  GJT_GRADE GRE,");
		sql.append("  	  GJT_YEAR GY,");
		sql.append("  	  GJT_SPECIALTY GSY,");
		sql.append("  	  GJT_COURSE GCE,");
		sql.append("  	  VIEW_TEACH_PLAN GTP,");
		sql.append("  	  GJT_REC_RESULT GRR LEFT JOIN VIEW_STUDENT_STUDY_SITUATION VSS ON GRR.REC_ID=VSS.CHOOSE_ID ");
		sql.append("  	  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  	  AND gcs.IS_DELETED = 'N'");
		sql.append("  	  AND gci.IS_DELETED = 'N'");
		sql.append("  	  AND GRE.IS_DELETED = 'N'");
		sql.append("  	  AND GSY.IS_DELETED = 'N'");
		sql.append("  	  AND GRR.IS_DELETED = 'N'");
		sql.append("  	  AND GCE.IS_DELETED = 'N'");
		sql.append("  	  AND GTP.IS_DELETED = 'N'");
		sql.append("  	  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  	  AND GRE.YEAR_ID = GY.GRADE_ID");
		sql.append("  	  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  	  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  	  AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	  AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("	  AND gci.CLASS_TYPE = 'teach'");
		sql.append("  	  AND gci.CLASS_ID = :CLASS_ID");
		sql.append("  	  AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  	  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("	  AND (GSI.XX_ID=:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");


		params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("CLASS_ID",ObjectUtils.toString(searchParams.get("CLASS_ID")));

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
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
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
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

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")))) {
			if ("4".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")))) {
				sql.append("  AND TAB.STUDY_FLG = 0");
			} else {
				sql.append("  AND TAB.STUDY_FLG = 1");
				sql.append("  AND TAB.EXAM_STATE = :EXAM_STATE_TEMP");
				params.put("EXAM_STATE_TEMP", ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")));
			}
		}

		BigDecimal num = (BigDecimal)commonDao.queryObjectNative(sql.toString(), params);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	/**
	 * 学习管理=》学分查询
	 * @return
	 */
	public Page getCreditsList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT * FROM ");
		sql.append("  	 (SELECT GSI.STUDENT_ID,GSI.XM,GSI.XH,GSI.SJH,GGE.GRADE_ID,GGE.GRADE_CODE,GGE.GRADE_NAME,GSY.ZYMC,GSI.PYCC,GSI.AVATAR ZP,GYR.NAME YEAR_NAME,");
		sql.append("  	 (SELECT TSD.NAME  FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  	 (SELECT SUM(GTP.XF) FROM VIEW_TEACH_PLAN GTP, GJT_REC_RESULT GRR WHERE GTP.IS_DELETED = 'N' AND GRR.IS_DELETED = 'N' AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID AND GTP.KKZY = GSY.SPECIALTY_ID AND GTP.GRADE_ID = GGE.GRADE_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) ZXF,");
		sql.append("  NVL(GSY.ZDBYXF, 0) ZDBYXF,GSY.BXXF,GSY.XXXF,");
		sql.append("  	 (SELECT NVL(SUM(NVL(GTP.XF, 0)), 0) FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND GRR.EXAM_STATE = '1' AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) SUM_CREDITS,");
		sql.append("  	 (SELECT COUNT(DISTINCT GRR.REC_ID) FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) COUNT_COURSE,");
		sql.append("  	 (SELECT COUNT(DISTINCT GRR.REC_ID) FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND GRR.EXAM_STATE = '1' AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) PASS_COURSE_COUNT");
		sql.append("  FROM GJT_STUDENT_INFO GSI,");
		sql.append("  	 GJT_CLASS_STUDENT gcs,");
		sql.append("  	 GJT_CLASS_INFO gci,");
		sql.append("  	 GJT_GRADE GGE, ");
		sql.append("  	 GJT_SPECIALTY GSY, ");
		sql.append("  	 GJT_YEAR GYR");
		sql.append("  WHERE ");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED = 'N'");
		sql.append("  	AND GGE.IS_DELETED = 'N'");
		sql.append("  	AND GSY.IS_DELETED = 'N'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  	AND GGE.YEAR_ID = GYR.GRADE_ID");
		sql.append("  	AND GSI.NJ = GGE.GRADE_ID");
		sql.append("  	AND gci.GRADE_ID=gsi.NJ");
		sql.append("  	AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  	AND gci.CLASS_TYPE='teach'");
		sql.append("  	AND gci.CLASS_ID= :CLASS_ID");
		sql.append("	AND (GSI.XX_ID=:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");

		param.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("CLASS_ID",ObjectUtils.toString(searchParams.get("CLASS_ID")));

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  	AND gsi.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  	AND gsi.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SFZH")))) {
			sql.append("  	AND gsi.SFZH LIKE :SFZH");
			param.put("SFZH", "%"+ObjectUtils.toString(searchParams.get("SFZH"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MAJOR")))) {
			sql.append("  	AND gsi.MAJOR = :MAJOR");
			param.put("MAJOR", ObjectUtils.toString(searchParams.get("MAJOR")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			sql.append("  	AND gsi.NJ= :NJ");
			param.put("NJ", ObjectUtils.toString(searchParams.get("NJ")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  	AND gs.PYCC= :PYCC");
			param.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		sql.append(" ) TAB WHERE 1=1 ");

		if (ObjectUtils.toString(searchParams.get("SCORE_TYPE")).equals("1")) {
			sql.append("AND TAB.SUM_CREDITS < TAB.BXXF");
		} else if (ObjectUtils.toString(searchParams.get("SCORE_TYPE")).equals("2")) {
			sql.append("AND TAB.SUM_CREDITS >= TAB.BXXF");
		}

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学习管理=》学分查询
	 * @return
	 */
	public int getCreditsCount(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT COUNT(*) FROM ");
		sql.append("  	 (SELECT GSI.STUDENT_ID,GSI.XM,GSI.XH,GSI.SJH,GGE.GRADE_ID,GGE.GRADE_CODE,GGE.GRADE_NAME,GSY.ZYMC,GSI.PYCC,GSI.AVATAR ZP,GYR.NAME YEAR_NAME,");
		sql.append("  	 (SELECT TSD.NAME  FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  	 (SELECT SUM(GTP.XF) FROM VIEW_TEACH_PLAN GTP, GJT_REC_RESULT GRR WHERE GTP.IS_DELETED = 'N' AND GRR.IS_DELETED = 'N' AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID AND GTP.KKZY = GSY.SPECIALTY_ID AND GTP.GRADE_ID = GGE.GRADE_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) ZXF,");
		sql.append("  NVL(GSY.ZDBYXF, 0) ZDBYXF,GSY.BXXF,GSY.XXXF,");
		sql.append("  	 (SELECT NVL(SUM(NVL(GTP.XF, 0)), 0) FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND GRR.EXAM_STATE = '1' AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) SUM_CREDITS,");
		sql.append("  	 (SELECT COUNT(DISTINCT GRR.REC_ID) FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) COUNT_COURSE,");
		sql.append("  	 (SELECT COUNT(DISTINCT GRR.REC_ID) FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND GRR.EXAM_STATE = '1' AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) PASS_COURSE_COUNT");
		sql.append("  FROM GJT_STUDENT_INFO GSI,");
		sql.append("  	 GJT_CLASS_STUDENT gcs,");
		sql.append("  	 GJT_CLASS_INFO gci,");
		sql.append("  	 GJT_GRADE GGE, ");
		sql.append("  	 GJT_SPECIALTY GSY, ");
		sql.append("  	 GJT_YEAR GYR");
		sql.append("  WHERE ");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED = 'N'");
		sql.append("  	AND GGE.IS_DELETED = 'N'");
		sql.append("  	AND GSY.IS_DELETED = 'N'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  	AND GGE.YEAR_ID = GYR.GRADE_ID");
		sql.append("  	AND GSI.NJ = GGE.GRADE_ID");
		sql.append("  	AND gci.GRADE_ID=gsi.NJ");
		sql.append("  	AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  	AND gci.CLASS_TYPE='teach'");
		sql.append("  	AND gci.CLASS_ID= :CLASS_ID");
		sql.append("	AND (GSI.XX_ID=:XX_ID OR GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:XX_ID CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID))");

		param.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("CLASS_ID",ObjectUtils.toString(searchParams.get("CLASS_ID")));

		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  	AND gsi.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  	AND gsi.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SFZH")))) {
			sql.append("  	AND gsi.SFZH LIKE :SFZH");
			param.put("SFZH", "%"+ObjectUtils.toString(searchParams.get("SFZH"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MAJOR")))) {
			sql.append("  	AND gsi.MAJOR = :MAJOR");
			param.put("MAJOR", ObjectUtils.toString(searchParams.get("MAJOR")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			sql.append("  	AND gsi.NJ= :NJ");
			param.put("NJ", ObjectUtils.toString(searchParams.get("NJ")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  	AND gs.PYCC= :PYCC");
			param.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		sql.append(" ) TAB WHERE 1=1 ");

		if (ObjectUtils.toString(searchParams.get("SCORE_TYPE_TEMP")).equals("1")) {
			sql.append("AND TAB.SUM_CREDITS < TAB.BXXF");
		} else if (ObjectUtils.toString(searchParams.get("SCORE_TYPE_TEMP")).equals("2")) {
			sql.append("AND TAB.SUM_CREDITS >= TAB.BXXF");
		}

		BigDecimal num = (BigDecimal)commonDao.queryObjectNative(sql.toString(), param);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	/**
	 * 学习管理=》课程学情
	 * @return
	 */
	public Page getCourseStudyList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GSC.ID,");
		sql.append("  GSI.ID STUDY_YEAR_ID,");
		sql.append("  GSI.STUDY_YEAR_NAME,");
		sql.append("  GC.COURSE_ID,");
		sql.append("  GC.KCMC,");
		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  FROM GJT_GRADE_SPECIALTY_PLAN GGSP, GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GGSP.ID = GRR.REC_ID");
		sql.append("  AND GGSP.STUDYYEAR_COURSE_ID = GSC.ID) STUDENT_COUNT");
		sql.append("  FROM GJT_STUDYYEAR_INFO GSI, GJT_STUDYYEAR_COURSE GSC, GJT_COURSE GC");
		sql.append("  WHERE GC.IS_DELETED = 'N'");
		sql.append("  AND GSI.ID = GSC.STUDY_YEAR_ID");
		sql.append("  AND GSC.COURSE_ID = GC.COURSE_ID");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_YEAR_NAME")))) {
			sql.append("  AND GSI.STUDY_YEAR_NAME LIKE :STUDY_YEAR_NAME");
			param.put("STUDY_YEAR_NAME", "%"+ObjectUtils.toString(searchParams.get("STUDY_YEAR_NAME")).trim()+"%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND GC.KCMC LIKE :KCMC");
			param.put("KCMC", "%"+ObjectUtils.toString(searchParams.get("KCMC")).trim()+"%");
		}
		sql.append("  ORDER BY GSI.STUDY_YEAR_NAME");


		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学习管理=》课程班学情
	 * @return
	 */
	public Page getCourseClassList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM GJT_CLASS_INFO GCI");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学习管理=》教学班学情
	 * @return
	 */
	public Page getTeachClassList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学习管理=》学员课程学情
	 * @return
	 */
	public Page getStudentCourseList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GC.COURSE_ID,");
		sql.append("  GC.KCMC,");
		sql.append("  GCI.CLASS_ID,");
		sql.append("  GCI.BJMC,");
		sql.append("  (SELECT GCI1.BJMC");
		sql.append("  FROM GJT_CLASS_INFO GCI1, GJT_CLASS_STUDENT GCS1");
		sql.append("  WHERE GCI1.IS_DELETED = 'N'");
		sql.append("  AND GCS1.IS_DELETED = 'N'");
		sql.append("  AND GCI1.CLASS_TYPE = 'teach'");
		sql.append("  AND GCI1.CLASS_ID = GCS1.CLASS_ID");
		sql.append("  AND GCS1.STUDENT_ID = GSI.STUDENT_ID) TEACH_CLASS_NAME");
		sql.append("  FROM GJT_STUDENT_INFO  GSI,");
		sql.append("  GJT_CLASS_STUDENT GCS,");
		sql.append("  GJT_CLASS_INFO    GCI,");
		sql.append("  GJT_COURSE        GC,");
		sql.append("  GJT_REC_RESULT    GRR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  AND GCI.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND GRR.COURSE_ID = GCI.COURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = GCS.STUDENT_ID");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM")).trim()+"%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND GC.KCMC LIKE :KCMC");
			param.put("KCMC", "%"+ObjectUtils.toString(searchParams.get("KCMC")).trim()+"%");
		}
		sql.append(") TAB  WHERE 1 = 1");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TEACH_CLASS_NAME")))) {
			sql.append("  AND TEACH_CLASS_NAME LIKE :TEACH_CLASS_NAME");
			param.put("TEACH_CLASS_NAME", "%"+ObjectUtils.toString(searchParams.get("TEACH_CLASS_NAME")).trim()+"%");
		}


		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学习管理=》学员专业学情
	 * @return
	 */
	public Page getStudentMajorList(Map searchParams, PageRequest pageRequst){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GSI.PYCC,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  GCI.CLASS_ID,");
		sql.append("  GCI.BJMC");
		sql.append("  FROM GJT_STUDENT_INFO  GSI,");
		sql.append("  GJT_SPECIALTY     GSY,");
		sql.append("  GJT_CLASS_INFO    GCI,");
		sql.append("  GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM")).trim()+"%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			param.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			param.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			param.put("BJMC", "%"+ObjectUtils.toString(searchParams.get("BJMC")).trim()+"%");
		}

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学习管理=》教务班考勤
	 * @return
	 */
	public Page getClassLoginList(Map searchParams, PageRequest pageRequst){
		Map param = new HashMap();
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
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			param.put("BJMC", "%"+ObjectUtils.toString(searchParams.get("BJMC")).trim()+"%");
		}
		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学习管理=》学员考勤
	 * @return
	 */
	public Page getStudentLoginList(Map searchParams, PageRequest pageRequst){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GSI.STUDENT_ID, GSI.XH, GSI.XM, GCI.BJMC");
		sql.append("  FROM GJT_STUDENT_INFO  GSI,");
		sql.append("  GJT_CLASS_INFO    GCI,");
		sql.append("  GJT_CLASS_STUDENT GCS,");
		sql.append("  GJT_GRADE         GR,");
		sql.append("  GJT_SPECIALTY     GSY");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GSI.NJ = GR.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM")).trim()+"%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GR.GRADE_ID = :GRADE_ID");
			param.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			param.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append("  AND GCI.BJMC LIKE :BJMC");
			param.put("BJMC", "%"+ObjectUtils.toString(searchParams.get("BJMC")).trim()+"%");
		}

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 学员选课成绩查询
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public List getScoreList(Map<String, Object> searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GSI.STUDENT_ID,GTP.TEACH_PLAN_ID,GSI.XH,GSI.XM,GSI.SJH,GSI.PYCC,GTP.KSFS,GRR.EXAM_STATE,");
		sql.append("  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GTP.KSFS AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME,");
		sql.append("  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'CourseType' AND TSD.CODE = GTP.KCLBM) KCLBM_NAME,");
		sql.append("  GTP.KCLBM,GRE.GRADE_ID,GRE.GRADE_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,GTP.KKXQ,GCE.COURSE_ID,GCE.KCMC,NVL(GTP.XF, 0) XF,NVL(TO_CHAR(GRR.COURSE_SCHEDULE),'') KCXXBZ,");
		sql.append("  NVL(GRR.EXAM_SCORE, 0) EXAM_SCORE,NVL(GRR.EXAM_SCORE1, 0) EXAM_SCORE1,NVL(GRR.EXAM_SCORE2, 0) EXAM_SCORE2,NVL(GRR.GET_CREDITS, 0) GET_CREDITS,");
		sql.append("  (SELECT DISTINCT COUNT(*) FROM GJT_LEARN_REPAIR glr WHERE glr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID AND glr.STUDENT_ID=grr.STUDENT_ID AND glr.COURSE_CODE=gtp.COURSE_CODE) EXAM_NUM,");

		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_EXAM_PLAN_NEW_COURSE GEP,");
		sql.append("  TBL_SYS_DATA             TSD");
		sql.append("  WHERE GEA.IS_DELETED = 0");
		sql.append("  AND TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  AND GEA.TYPE = TSD.CODE");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEA.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GEP.COURSE_ID = GRR.COURSE_ID");
		sql.append("  AND ROWNUM = 1) EXAM_PLAN_KSFS_NAME,");

		sql.append("  TO_CHAR((SELECT CASE WHEN GGR.START_DATE > SYSDATE THEN 0 ELSE 1 END FROM GJT_GRADE GGR WHERE GGR.IS_DELETED = 'N' AND GGR.GRADE_ID = GTP.ACTUAL_GRADE_ID)) STUDY_FLG");

		sql.append("  FROM GJT_STUDENT_INFO GSI,");
		sql.append("  GJT_GRADE        GRE,");
		sql.append("  GJT_SPECIALTY    GSY,");
		sql.append("  GJT_COURSE       GCE,");
		sql.append("  VIEW_TEACH_PLAN   GTP,");
		sql.append("  GJT_REC_RESULT   GRR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GSI.STUDENT_ID = :studentId");
		param.put("studentId", ObjectUtils.toString(searchParams.get("studentId")));
		sql.append("  ORDER BY KKXQ, GTP.KCLBM");
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 学支平台--学员信息--选课信息总览
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public List getStudentRecordDetail(Map<String, Object> searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GSI.STUDENT_ID,GSI.XH,GSI.XM,GSI.XBM,GSI.SJH,GSI.PYCC,GSI.RXNY,GSI.COMPANY,GSI.SC_CO,GSI.TXDZ,GSI.AVATAR ZP,GSI.SFZH,GSI.DZXX,");
		sql.append("  TO_CHAR(GSI.CREATED_DT, 'yyyy-mm-dd') CREATED_DT,");
		sql.append("  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  GRE.GRADE_ID,GRE.GRADE_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,");

		sql.append("  (SELECT SUM(GTP.XF)");
		sql.append("  	FROM GJT_GRADE_SPECIALTY GGS, VIEW_TEACH_PLAN GTP, GJT_REC_RESULT GRR");
		sql.append("  	WHERE GGS.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N' AND GRR.IS_DELETED = 'N' ");
		sql.append("  	AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  	AND GGS.GRADE_ID = GTP.GRADE_ID");
		sql.append("  	AND GGS.SPECIALTY_ID = GTP.KKZY");
		sql.append("  	AND GRE.GRADE_ID = GGS.GRADE_ID");
		sql.append("  	AND GSY.SPECIALTY_ID = GGS.SPECIALTY_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID) ZXF,");
		sql.append("  NVL(GSY.ZDBYXF, 0) ZDBYXF,GSY.BXXF,GSIO.XXMC,GSC.SC_NAME,");

		sql.append("  (SELECT NVL(SUM(NVL(GRR.GET_CREDITS, 0)), 0)");
		sql.append("  	FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP");
		sql.append("  	WHERE GRR.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID) GET_POINT,");

		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  	FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP");
		sql.append("  	WHERE GRR.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID) ALL_COURSE_COUNT,");

		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  	FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP");
		sql.append("  	WHERE GRR.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GRR.EXAM_STATE = '0'");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID) NOT_PASS_COURSE_COUNT,");

		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  	FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP");
		sql.append("  	WHERE GRR.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GRR.EXAM_STATE = '1'");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID) PASS_COURSE_COUNT,");

		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  	FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP, GJT_GRADE GGR");
		sql.append("  	WHERE GRR.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GGR.IS_DELETED = 'N'");
		sql.append("  	AND GRR.EXAM_STATE = '2'");
		sql.append("  	AND GGR.START_DATE<=SYSDATE");
		sql.append("  	AND GTP.ACTUAL_GRADE_ID = GGR.GRADE_ID");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID) STUDY_COURSE_COUNT,");

		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  	FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP, GJT_GRADE GGR");
		sql.append("  	WHERE GRR.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GGR.IS_DELETED = 'N'");
		sql.append("  	AND GRR.EXAM_STATE = '2'");
		sql.append("  	AND GGR.START_DATE>SYSDATE");
		sql.append("  	AND GTP.ACTUAL_GRADE_ID = GGR.GRADE_ID");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID) NOT_STUDY_COURSE_COUNT,");

		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  	FROM GJT_REC_RESULT GRR, VIEW_TEACH_PLAN GTP");
		sql.append("  	WHERE GRR.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GRR.EXAM_STATE = '3'");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID) REGISTER_COURSE_COUNT");

		sql.append("  FROM GJT_GRADE GRE, GJT_SPECIALTY GSY, GJT_STUDENT_INFO GSI");
		sql.append("  	LEFT JOIN GJT_SCHOOL_INFO GSIO ON GSIO.ID = GSI.XX_ID");
		sql.append("  	LEFT JOIN GJT_STUDY_CENTER GSC ON GSC.ID = GSI.XXZX_ID");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  	AND GRE.IS_DELETED = 'N'");
		sql.append("  	AND GSY.IS_DELETED = 'N'");
		sql.append("  	AND GSIO.IS_DELETED = 'N'");
		sql.append("  	AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  	AND GSI.MAJOR = GSY.SPECIALTY_ID");

		sql.append("  	AND GSI.STUDENT_ID = :studentId");

		param.put("studentId", ObjectUtils.toString(searchParams.get("studentId")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 学支平台--查看历史成绩
	 *
	 * @param formMap
	 * @return
	 */
	@Override
	public List getHistoryScore(Map<String, Object> formMap) {
		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT");
		sql.append("  	gg.GRADE_ID TERM_ID,");
		sql.append("  	gg.GRADE_NAME TERM_NAME,");
		sql.append("  	glr.TEACH_PLAN_ID,");
		sql.append("  	NVL(glr.XCX_SCORE,0) XCX_SCORE,");
		sql.append("  	NVL(glr.ZJX_SCORE,0) ZJX_SCORE,");
		sql.append("  	NVL(glr.ZCJ_SCORE,0) ZCJ_SCORE,");
		sql.append("  	NVL(TO_CHAR(GRR.COURSE_SCHEDULE),'') XCX_PERCENT,");
		sql.append("  	glr.STATUS");
		sql.append("  FROM");
		sql.append("  	GJT_LEARN_REPAIR glr");
		sql.append("  	LEFT JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  	gtp.TEACH_PLAN_ID=glr.TEACH_PLAN_ID");
		sql.append("  	AND gtp.IS_DELETED='N' LEFT JOIN GJT_GRADE gg ON");
		sql.append("  	gtp.ACTUAL_GRADE_ID = gg.GRADE_ID AND gg.IS_DELETED='N'");
		sql.append("  LEFT JOIN GJT_REC_RESULT grr ON grr.STUDENT_ID=glr.STUDENT_ID AND grr.TEACH_PLAN_ID=glr.TEACH_PLAN_ID");
		sql.append("  WHERE");
		sql.append("  	glr.IS_DELETED = 'N'");
		sql.append("  	AND grr.IS_DELETED='N'");

		sql.append("  	AND glr.STUDENT_ID = :studentId");
		sql.append("  	AND GTP.TEACH_PLAN_ID = :teachPlanId");
		sql.append("  ORDER BY");
		sql.append("  	glr.CREATED_DT");

		return commonDao.queryForMapListNative(sql.toString(),formMap);
	}
}
