package com.gzedu.xlims.dao.exam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;

@Repository
public class GjtExamRecordNewDao {
	
	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;
	
	/**
	 * 考试管理=》登记成绩查询列表
	 * @return
	 */
	public Page getExamRegisterList(Map searchParams, PageRequest pageRequst) {
		Map param = getExamRegisterListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return (Page) commonDao.queryForPageNative(sql, param, pageRequst);
	}
	
	/**
	 * 考试管理=》登记成绩查询列表
	 * @return
	 */
	public List getExamRegisterList(Map searchParams) {
		Map param = getExamRegisterListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return commonDao.queryForMapListNative(sql, param);
	}
	
	/**
	 * 考试管理=》登记成绩查询列表（sql）
	 * @return
	 */
	public Map getExamRegisterListSQL(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT XH,");
		sql.append("  XM,");
		sql.append("  SJH,");
		sql.append("  SFZH,");
		sql.append("  PYCC,");
		sql.append("  ZP,");
		sql.append("  ORG_NAME,");
		sql.append("  CREATED_DT,");
		sql.append("  GRADE_ID,");
		sql.append("  GRADE_NAME,");
		sql.append("  YEAR_ID,");
		sql.append("  YEAR_NAME,");
		sql.append("  SPECIALTY_ID,");
		sql.append("  ZYMC,");
		sql.append("  COURSE_ID,");
		sql.append("  KCMC,");
		sql.append("  KCH,");
		sql.append("  EXAM_NO,");
		sql.append("  EXAM_PLAN_NAME,");
		sql.append("  EXAM_TYPE,");
		sql.append("  XK_PERCENT,");
		sql.append("  EXAM_SCORE,");
		sql.append("  EXAM_SCORE1,");
		sql.append("  EXAM_SCORE2,");
		sql.append("  EXAM_STATE,");
		sql.append("  EXAM_BATCH_CODE,");
		sql.append("  EXAM_BATCH_NAME,");
		sql.append("  KSDW,");
		sql.append("  KKXQ,");
		sql.append("  SOURCE_KCH,");
		sql.append("  SOURCE_KCMC,");
		sql.append("  PYCC_NAME,");
		sql.append("  KSFS_NAME,");
		sql.append("  STUDY_FLG,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.TYPE_CODE = 'USER_TYPE'");
		sql.append("  AND TSD.CODE = USER_TYPE) USER_TYPE_NAM");
		sql.append("  FROM (SELECT GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GSI.ZP,");
		sql.append("  GSI.USER_TYPE,");
		sql.append("  TO_CHAR(GSI.CREATED_DT, 'yyyymm') CREATED_DT,");
		sql.append("  GRE.GRADE_ID,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GYR.GRADE_ID YEAR_ID,");
		sql.append("  GYR.NAME YEAR_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GCE.COURSE_ID,");
		sql.append("  GCE.KCMC,");
		sql.append("  GCE.KCH,");
		sql.append("  GEP.EXAM_NO,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.TYPE EXAM_TYPE,");
		sql.append("  GEP.XK_PERCENT,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  EBC.EXAM_BATCH_CODE,");
		sql.append("  EBC.NAME EXAM_BATCH_NAME,");
		sql.append("  VTP.KSDW,");
		sql.append("  VTP.KKXQ,");
		sql.append("  VTP.SOURCE_KCH,");
		sql.append("  VTP.SOURCE_KCMC,");
		sql.append("  (SELECT GOG.ORG_NAME");
		sql.append("  FROM GJT_ORG GOG");
		sql.append("  WHERE GOG.IS_DELETED = 'N'");
		sql.append("  AND GOG.ID = GSI.XXZX_ID) ORG_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GEP.TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME,");
		sql.append("  TO_CHAR((SELECT (CASE");
		sql.append("  WHEN GGRE.START_DATE > SYSDATE THEN");
		sql.append("  0");
		sql.append("  ELSE");
		sql.append("  1");
		sql.append("  END)");
		sql.append("  FROM GJT_GRADE GGRE");
		sql.append("  WHERE GGRE.IS_DELETED = 'N'");
		sql.append("  AND GGRE.GRADE_ID = VTP.ACTUAL_GRADE_ID)) STUDY_FLG");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_SPECIALTY            GSY,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_PLAN_NEW_COURSE GEN,");
		sql.append("  GJT_EXAM_BATCH_NEW       EBC,");
		sql.append("  GJT_COURSE               GCE,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_GRADE                GRE,");
		sql.append("  GJT_YEAR                 GYR,");
		sql.append("  VIEW_TEACH_PLAN          VTP");
		sql.append("  WHERE GEA.IS_DELETED = 0");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND EBC.IS_DELETED = 0");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND GEP.EXAM_PLAN_ID = GEN.EXAM_PLAN_ID");
		sql.append("  AND (GEN.COURSE_ID = VTP.COURSE_ID OR GEN.COURSE_ID = VTP.SOURCE_COURSE_ID)");
		sql.append("  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  AND GRE.YEAR_ID = GYR.GRADE_ID");
		
		sql.append("  AND GSI.XX_ID = :XX_ID");
		param.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))){
			sql.append(" AND EBC.EXAM_BATCH_CODE= :EXAM_BATCH_CODE");
			param.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))){
			sql.append(" AND GSI.XXZX_ID= :XXZX_ID");
			param.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			param.put("PYCC",ObjectUtils.toString(searchParams.get("PYCC")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSI.MAJOR = :SPECIALTY_ID");
			param.put("SPECIALTY_ID",ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			sql.append("  AND GSI.NJ = :NJ");
			param.put("NJ",ObjectUtils.toString(searchParams.get("NJ")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("YEAR_ID")))) {
			sql.append("  AND GYR.GRADE_ID = :YEAR_ID");
			param.put("YEAR_ID",ObjectUtils.toString(searchParams.get("YEAR_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
			param.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		
		sql.append("  ) TAB");
		sql.append("  WHERE 1 = 1");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
			if ("4".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND TAB.STUDY_FLG = 0");
			} else {
				sql.append("  AND TAB.STUDY_FLG = 1");
				sql.append("  AND TAB.EXAM_STATE = :EXAM_STATE");
				param.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
			}
		}
		param.put("sql", sql);
		return param;
	}
	
	/**
	 * 考试管理=》登记成绩(统计)
	 * @return
	 */
	public int getRegisterCount(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT COUNT(XH) ");
		sql.append("  FROM (SELECT GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GSI.ZP,");
		sql.append("  GRE.GRADE_ID,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GYR.GRADE_ID YEAR_ID,");
		sql.append("  GYR.NAME YEAR_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GCE.COURSE_ID,");
		sql.append("  GCE.KCMC,");
		sql.append("  GCE.KCH,");
		sql.append("  GEP.EXAM_NO,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.TYPE EXAM_TYPE,");
		sql.append("  GEP.XK_PERCENT,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  EBC.EXAM_BATCH_CODE,");
		sql.append("  EBC.NAME EXAM_BATCH_NAME,");
		sql.append("  VTP.KSDW,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GEP.TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME,");
		sql.append("  TO_CHAR((SELECT (CASE");
		sql.append("  WHEN GGRE.START_DATE > SYSDATE THEN");
		sql.append("  0");
		sql.append("  ELSE");
		sql.append("  1");
		sql.append("  END)");
		sql.append("  FROM GJT_GRADE GGRE");
		sql.append("  WHERE GGRE.IS_DELETED = 'N'");
		sql.append("  AND GGRE.GRADE_ID = VTP.ACTUAL_GRADE_ID)) STUDY_FLG");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_SPECIALTY            GSY,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_PLAN_NEW_COURSE GEN,");
		sql.append("  GJT_EXAM_BATCH_NEW       EBC,");
		sql.append("  GJT_COURSE               GCE,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  VIEW_TEACH_PLAN          VTP,");
		sql.append("  GJT_GRADE                GRE,");
		sql.append("  GJT_YEAR                 GYR");
		sql.append("  WHERE GEA.IS_DELETED = 0");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND EBC.IS_DELETED = 0");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND GEP.EXAM_PLAN_ID = GEN.EXAM_PLAN_ID");
		sql.append("  AND (GEN.COURSE_ID = VTP.COURSE_ID OR GEN.COURSE_ID = VTP.SOURCE_COURSE_ID)");
		sql.append("  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  AND GRE.YEAR_ID = GYR.GRADE_ID");
		
		sql.append("  AND GSI.XX_ID = :XX_ID");
		param.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))){
			sql.append(" AND EBC.EXAM_BATCH_CODE= :EXAM_BATCH_CODE");
			param.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))){
			sql.append(" AND GSI.XXZX_ID= :XXZX_ID");
			param.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			param.put("PYCC",ObjectUtils.toString(searchParams.get("PYCC")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSI.MAJOR = :SPECIALTY_ID");
			param.put("SPECIALTY_ID",ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			sql.append("  AND GSI.NJ = :NJ");
			param.put("NJ",ObjectUtils.toString(searchParams.get("NJ")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("YEAR_ID")))) {
			sql.append("  AND GYR.GRADE_ID = :YEAR_ID");
			param.put("YEAR_ID",ObjectUtils.toString(searchParams.get("YEAR_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GCE.COURSE_ID = :COURSE_ID");
			param.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		
		sql.append("  ) TAB");
		sql.append("  WHERE 1 = 1");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")))) {
			if ("4".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")))) {
				sql.append("  AND TAB.STUDY_FLG = 0");
			} else {
				sql.append("  AND TAB.STUDY_FLG = 1");
				sql.append("  AND TAB.EXAM_STATE = :EXAM_STATE_TEMP");
				param.put("EXAM_STATE_TEMP", ObjectUtils.toString(searchParams.get("EXAM_STATE_TEMP")));
			}
		}

		BigDecimal num = (BigDecimal)commonDao.queryObjectNative(sql.toString(), param);
		return Integer.parseInt(ObjectUtils.toString(num));
	}
	
	/**
	 * 考试管理=》考试成绩查看详情
	 * @return
	 */
	public List getExamRecordDetail(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.STUDENT_ID,GSI.XH,GSI.XM,GSI.XBM,GSI.SJH,GSI.PYCC,GSI.RXNY,GSI.COMPANY,GSI.SC_CO,GSI.TXDZ,GSI.AVATAR ZP,GSI.SFZH,GSI.DZXX,");
		sql.append("  TO_CHAR(GSI.CREATED_DT, 'yyyy-mm-dd') CREATED_DT,");
		sql.append("  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  GRE.GRADE_ID,GRE.GRADE_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,GSY.RULE_CODE,");

		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.USER_TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'USER_TYPE') USER_TYPE_NAME,");
		
		sql.append("  (SELECT GYR.NAME FROM GJT_YEAR GYR WHERE GYR.GRADE_ID = GRE.YEAR_ID) YEAR_NAME,");
		sql.append("  GSY.ZYDDKSXF,");
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

		sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 考试管理=》成绩查询
	 * @return
	 */
	public List getScoreList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.STUDENT_ID,");
		sql.append("  GTP.TEACH_PLAN_ID,");
		sql.append("  GTP.KCSX,");
		sql.append("  GTP.COURSE_TYPE,");
		sql.append("  GTP.KSDW,");
		sql.append("  GCE.KCH,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.PYCC,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  GTP.KSFS,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GTP.KSFS");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.TYPE_CODE = 'CourseType'");
		sql.append("  AND TSD.CODE = GTP.KCLBM) KCLBM_NAME,");
		sql.append("  GTP.KCLBM,");
		sql.append("  GRE.GRADE_ID,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GTP.KKXQ,");
		sql.append("  GCE.COURSE_ID,");
		sql.append("  GCE.KCMC,");
		sql.append("  NVL(GTP.XF, 0) XF,");
		sql.append("  NVL(TO_CHAR(GRR.COURSE_SCHEDULE),'') KCXXBZ,");
//		sql.append("  NVL(GTP.KCKSBZ, 0) KCKSBZ,");
		sql.append("  NVL(GRR.EXAM_SCORE, 0) EXAM_SCORE,");
		sql.append("  NVL(GRR.EXAM_SCORE1, 0) EXAM_SCORE1,");
		sql.append("  NVL(GRR.EXAM_SCORE2, 0) EXAM_SCORE2,");
		sql.append("  NVL(GRR.GET_CREDITS, 0) GET_CREDITS,");
		sql.append("  GRR.EXAM_STATE,");
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
		       
		sql.append("  TO_CHAR((SELECT CASE");
		sql.append("  WHEN GGR.START_DATE > SYSDATE THEN");
		sql.append("  0");
		sql.append("  ELSE");
		sql.append("  1");
		sql.append("  END");
		sql.append("  FROM GJT_GRADE GGR");
		sql.append("  WHERE GGR.IS_DELETED = 'N'");
		sql.append("  AND GGR.GRADE_ID = GTP.ACTUAL_GRADE_ID)) STUDY_FLG");
		
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
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID");
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MODULE_ID")))) {
			sql.append("  AND GTP.KCLBM IN (SELECT TSD.CODE");
			sql.append("  FROM TBL_SYS_DATA TSD");
			sql.append("  WHERE TSD.IS_DELETED = 'N'");
			sql.append("  AND TSD.ID = :MODULE_ID)");
			param.put("MODULE_ID", ObjectUtils.toString(searchParams.get("MODULE_ID")));
		}
		
		sql.append("  ORDER BY KKXQ, GTP.KCLBM");
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 考试管理=》模块详情
	 * @return
	 */
	public List getModuleList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT KCLBM_NAME,");
		sql.append("  KCLBM,");
		sql.append("  SPECIALTY_ID,");
		sql.append("  SUM(XF) XF,");
		sql.append("  COUNT(COURSE_ID) ALL_COURSE,");
		sql.append("  SUM(GET_POINT) GET_POINT,");
		sql.append("  SUM(SCORE_STATE) PASS_COURSE,");
		sql.append("  (select sum(a.score)");
		sql.append("  from gjt_specialty_module_limit a, tbl_sys_data b");
		sql.append("  WHERE a.MODULE_ID = b.id");
		sql.append("  AND b.code = TAB.KCLBM");
		sql.append("  and a.is_deleted = 'N'");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  AND a.specialty_id = TAB.SPECIALTY_ID) as ZDF");
		sql.append("  FROM (SELECT (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.TYPE_CODE = 'CourseType'");
		sql.append("  AND TSD.CODE = GTP.KCLBM) KCLBM_NAME,");
		sql.append("  GTP.KCLBM,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  NVL(GTP.XF, 0) XF,");
		sql.append("  GTP.COURSE_ID,");
		sql.append("  NVL(grr.GET_CREDITS, 0) GET_POINT,");
		sql.append("  CASE");
		sql.append("  WHEN grr.EXAM_STATE = '1' THEN");
		sql.append("  1");
		sql.append("  ELSE");
		sql.append("  0");
		sql.append("  END SCORE_STATE");
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
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID) TAB");
		sql.append("  GROUP BY KCLBM_NAME, SPECIALTY_ID, KCLBM");
		sql.append("  ORDER BY KCLBM_NAME");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 导出成绩查询数据
	 * @return
	 */
	public List getExpExamRecord(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.XH,");
		sql.append("  TO_CHAR(GSI.CREATED_DT, 'yyyymm') CREATED_DT,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GCE.COURSE_ID,");
		sql.append("  GCE.KCMC,");
		sql.append("  GCE.KCH,");
		sql.append("  GEP.EXAM_NO,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.TYPE EXAM_TYPE,");
		sql.append("  GEP.XK_PERCENT,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GEP.TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_SPECIALTY            GSY,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW       EBC,");
		sql.append("  GJT_COURSE               GCE,");
		sql.append("  GJT_REC_RESULT           GRR");
		sql.append("  WHERE GEA.IS_DELETED = 0");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND EBC.IS_DELETED = 0");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND GEP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND EBC.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  AND GSI.XX_ID = :XX_ID");
		//sql.append("  ORDER BY GSI.XH");
		
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 设置登记中成绩
	 * @return
	 */
	@Transactional
	public int updRecordRegister(Map searchParams) {
		int rs = 0;
		StringBuilder sql = new StringBuilder();
		
		sql.append("  UPDATE GJT_STUDY_RECORD GSRD");
		sql.append("  SET GSRD.BEFORE_DT          = SYSDATE,");
		sql.append("  GSRD.BEFORE_STUDY_SCORE = GSRD.STUDY_SCORE,");
		sql.append("  GSRD.BEFORE_EXAM_SCORE  = GSRD.EXAM_SCORE,");
		sql.append("  GSRD.SCORE_STATE        = '3'");
		sql.append("  WHERE GSRD.IS_DELETED = 'N'");
		sql.append("  AND GSRD.SCORE_STATE = '2'");
		sql.append("  AND GSRD.CHOOSE_ID IN");
		sql.append("  (SELECT GRR.REC_ID");
		sql.append("  FROM GJT_STUDENT_INFO GSI,");
		sql.append("  GJT_GRADE        GRE,");
		sql.append("  GJT_SPECIALTY    GSY,");
		sql.append("  GJT_COURSE       GCE,");
		sql.append("  VIEW_TEACH_PLAN   GTP,");
		sql.append("  GJT_REC_RESULT   GRR");
		sql.append("  LEFT JOIN GJT_STUDY_RECORD GSR ON GRR.REC_ID = GSR.CHOOSE_ID");
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
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE "+"'%"+ObjectUtils.toString(searchParams.get("XM")).trim()+"%"+"'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH LIKE '"+"%"+ObjectUtils.toString(searchParams.get("XH")).trim()+"%"+"'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SCORE_STATE")))) {
			sql.append("  AND GSR.SCORE_STATE = '"+ObjectUtils.toString(searchParams.get("SCORE_STATE"))+"'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = '"+ObjectUtils.toString(searchParams.get("SPECIALTY_ID"))+"'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND GCE.KCMC LIKE '"+"%"+ObjectUtils.toString(searchParams.get("KCMC")).trim()+"%"+"'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_TYPE")))) {
			sql.append("  AND GTP.KSFS = '"+ObjectUtils.toString(searchParams.get("EXAM_TYPE"))+"'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = '"+ObjectUtils.toString(searchParams.get("PYCC"))+"'");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XX_ID")))) {
			sql.append("  AND GSI.XX_ID = '"+ObjectUtils.toString(searchParams.get("XX_ID"))+"'");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			sql.append("  AND GRE.GRADE_ID = '"+ObjectUtils.toString(searchParams.get("NJ"))+"'");
		}
		
		sql.append("  )");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();
		return rs;
	}
	
	/**
	 * 根据学号试卷号查询预约信息
	 * @return
	 */
	public List getStuExamNoInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  (SELECT GEA.APPOINTMENT_ID");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW       GEB,");
		sql.append("  GJT_EXAM_PLAN_NEW_COURSE GEC");
		sql.append("  WHERE GEA.IS_DELETED = 0");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEP.EXAM_PLAN_ID = GEC.EXAM_PLAN_ID");
		sql.append("  AND GEB.EXAM_BATCH_CODE = GEP.EXAM_BATCH_CODE");
		sql.append("  AND GEA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GEP.EXAM_NO = :EXAM_NO");
		sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE and rownum=1) APPOINTMENT_ID");
		sql.append("  FROM GJT_STUDENT_INFO GSI");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.XH = :XH");
		sql.append("  AND GSI.XX_ID = :XX_ID");
		
		param.put("EXAM_NO", ObjectUtils.toString(searchParams.get("examNo")));
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("examBatchCode")));
		param.put("XH", ObjectUtils.toString(searchParams.get("xh")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("xxId")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 根据学号学号查询预约信息
	 * @return
	 */
	public List getStuRecByKch(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XH,");
		sql.append("  VTP.TEACH_PLAN_ID,");
		sql.append("  GRR.REC_ID,");
		sql.append("  NVL(VTP.XF, '') XF,");
		sql.append("  NVL(GRR.EXAM_SCORE, '') EXAM_SCORE,");
		sql.append("  NVL(GRR.EXAM_SCORE1, '') EXAM_SCORE1,");
		sql.append("  NVL(GRR.EXAM_SCORE2, '') EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  NVL(GRR.COURSE_SCHEDULE, '') COURSE_SCHEDULE");
		sql.append("  FROM GJT_STUDENT_INFO GSI, VIEW_TEACH_PLAN VTP, GJT_REC_RESULT GRR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND (VTP.KCH = :KCH OR VTP.SOURCE_KCH = :SOURCE_KCH)");
		sql.append("  AND GSI.XH = :XH");
		
		param.put("KCH", ObjectUtils.toString(searchParams.get("kch")));
		param.put("SOURCE_KCH", ObjectUtils.toString(searchParams.get("kch")));
		param.put("XH", ObjectUtils.toString(searchParams.get("xh")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询导入免修\统考成绩
	 * @return
	 */
	public List getExemptRecord(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GRR.REC_ID,");
		sql.append("  GRR.TEACH_PLAN_ID,");
		sql.append("  VTP.COURSE_ID,");
		sql.append("  VTP.KCH,");
		sql.append("  VTP.XF,");
		sql.append("  GRR.PROGRESS,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  GEP.TYPE EXAM_TYPE,");
		sql.append("  GEP.XK_PERCENT");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  VIEW_TEACH_PLAN          VTP,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW       GEB,");
		sql.append("  GJT_EXAM_PLAN_NEW_COURSE GEC");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GEA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GEP.EXAM_PLAN_ID = GEC.EXAM_PLAN_ID");
		sql.append("  AND (VTP.COURSE_ID = GEC.COURSE_ID OR");
		sql.append("  VTP.SOURCE_COURSE_ID = GEC.COURSE_ID)");
		sql.append("  AND GSI.XH = :XH");
		sql.append("  AND GEP.EXAM_NO = :EXAM_NO");
		sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  AND GSI.XX_ID = :XX_ID");
		
		param.put("XH", ObjectUtils.toString(searchParams.get("xh")));
		param.put("EXAM_NO", ObjectUtils.toString(searchParams.get("examNo")));
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("examBatchCode")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("xxId")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 查询导入免修\统考成绩  按照学号课程代码对应得上就是干
	 * @return
	 */
	public List getExemptRecordTeshuchuli(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GRR.REC_ID,");
		sql.append("  GRR.TEACH_PLAN_ID,");
		sql.append("  VTP.COURSE_ID,");
		sql.append("  VTP.KCH,");
		sql.append("  VTP.XF,");
		sql.append("  GRR.PROGRESS,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  VIEW_TEACH_PLAN          VTP,");
		sql.append("  GJT_REC_RESULT           GRR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND (VTP.KCH =:courseNo OR VTP.SOURCE_KCH =:courseNo)");
		sql.append("  AND GSI.XH = :XH");
		sql.append("  AND GSI.XX_ID = :XX_ID");

		param.put("XH", ObjectUtils.toString(searchParams.get("xh")));
		param.put("courseNo", ObjectUtils.toString(searchParams.get("examNo")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("xxId")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 登记成绩
	 * @return
	 */
	@Transactional
	public int updRecRegister(Map searchParams) {
		int rs = 0;
		StringBuilder sql = new StringBuilder();
		
		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET GRR.EXAM_SCORE  = '"+searchParams.get("EXAM_SCORE")+"',");
		sql.append("  GRR.EXAM_SCORE1 = '"+searchParams.get("EXAM_SCORE1")+"',");
		sql.append("  GRR.EXAM_SCORE2 = '"+searchParams.get("EXAM_SCORE2")+"',");
		sql.append("  GRR.EXAM_STATE = '"+searchParams.get("EXAM_STATE")+"',");
		if(searchParams.get("GET_CREDITS") != null) {
			sql.append("  GRR.GET_CREDITS = '" + searchParams.get("GET_CREDITS") + "',");
		}
		sql.append("  GRR.COURSE_SCHEDULE = '"+searchParams.get("COURSE_SCHEDULE")+"',");
		// 20171023 去掉下面代码，因为在学习空间有控制了学员的预约
		/*if(StringUtils.equals((String) searchParams.get("EXAM_STATE"), Constants.BOOLEAN_0)) {
			sql.append("  GRR.PAY_STATE = '0',"); // 成绩不通过改为待缴费
			sql.append("  GRR.REBUILD_STATE = '0',"); // 成绩不通过改为重修改为否，可以让学员去学习空间点击重修
		}*/
		sql.append("  GRR.UPDATED_BY  = '登记成绩',");
		sql.append("  GRR.UPDATED_DT  = SYSDATE");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.REC_ID = '"+searchParams.get("REC_ID")+"'");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();
		return rs;
	}
	
	/**
	 * 登记成绩
	 * @return
	 */
	@Transactional
	public int addRecRegister(Map searchParams) {
		int rs = 0;
		StringBuilder sql = new StringBuilder();
		
		sql.append("  INSERT INTO GJT_LEARN_REPAIR");
		sql.append("  (REPAIR_ID,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  XCX_SCORE,");
		sql.append("  ZJX_SCORE,");
		sql.append("  ZCJ_SCORE,");
		sql.append("  STATUS,");
		sql.append("  STUDENT_ID,");
		sql.append("  XH,");
		sql.append("  COURSE_CODE,");
		sql.append("  RATIO,");
		sql.append("  EXAM_CODE,");
		sql.append("  EXAM_BATCH_CODE,");
		sql.append("  PROGRESS,");
		sql.append("  REMARK,");
		sql.append("  SUBJECT_NAME)");
		sql.append("  VALUES");
		sql.append("  ('"+ObjectUtils.toString(searchParams.get("REPAIR_ID"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("XCX_SCORE"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("ZJX_SCORE"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("ZCJ_SCORE"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("STATUS"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("STUDENT_ID"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("XH"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("COURSE_CODE"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("RATIO"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("EXAM_CODE"))+"',");
		sql.append("  '"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))+"',");
		if(searchParams.get("PROGRESS") != null) {
			sql.append("  '" + searchParams.get("PROGRESS") + "',");
		} else {
			sql.append("  null,");
		}
		sql.append("  '"+searchParams.get("REMARK")+"',");
		sql.append("  '"+searchParams.get("SUBJECT_NAME")+"')");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();
		return rs;
	}
	
	/**
	 * 登记成绩
	 * @return
	 */
	@Transactional
	public int updateExamScore(Map searchParams) {
		int rs = 0;
		StringBuilder sql = new StringBuilder();
		
		sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  SET GEA.EXAM_SCORE  = '"+ObjectUtils.toString(searchParams.get("ZJX_SCORE"))+"',");
		sql.append("  GEA.EXAM_STATUS  = '"+ObjectUtils.toString(searchParams.get("EXAM_STATUS"))+"',");
		sql.append("  GEA.UPDATED_DT = SYSDATE");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEA.REC_ID = '"+ObjectUtils.toString(searchParams.get("REC_ID"))+"'");
		sql.append("  AND GEA.EXAM_BATCH_CODE = '"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))+"'");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();
		return rs;
	}
	
	/**
	 * 登记成绩
	 * @return
	 */
	@Transactional
	public int updLearnRepair(Map searchParams) {
		int rs = 0;
		StringBuilder sql = new StringBuilder();
		
		sql.append("  UPDATE GJT_LEARN_REPAIR GLR");
		sql.append("  SET GLR.XCX_SCORE  = '"+ObjectUtils.toString(searchParams.get("XCX_SCORE"))+"',");
		sql.append("  GLR.ZJX_SCORE  = '"+ObjectUtils.toString(searchParams.get("ZJX_SCORE"))+"',");
		sql.append("  GLR.ZCJ_SCORE  = '"+ObjectUtils.toString(searchParams.get("ZCJ_SCORE"))+"',");
		sql.append("  GLR.STATUS     = '"+ObjectUtils.toString(searchParams.get("STATUS"))+"',");
		sql.append("  GLR.RATIO      = '"+ObjectUtils.toString(searchParams.get("RATIO"))+"',");
		sql.append("  GLR.PROGRESS   = '"+ObjectUtils.toString(searchParams.get("PROGRESS"))+"',");
		sql.append("  GLR.UPDATED_DT = SYSDATE");
		sql.append("  WHERE GLR.IS_DELETED = 'N'");
		sql.append("  AND GLR.TEACH_PLAN_ID = '"+ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID"))+"'");
		sql.append("  AND GLR.STUDENT_ID = '"+ObjectUtils.toString(searchParams.get("STUDENT_ID"))+"'");
		sql.append("  AND GLR.EXAM_BATCH_CODE = '"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))+"'");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();
		return rs;
	}
	
	/**
	 * 查询历史登记表是否在考试计划中已经登记
	 * @return
	 */
	public List queryLearnRepair(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GLR.REPAIR_ID");
		sql.append("  FROM GJT_LEARN_REPAIR GLR");
		sql.append("  WHERE GLR.IS_DELETED = 'N'");
		sql.append("  AND GLR.TEACH_PLAN_ID = :TEACH_PLAN_ID");
		sql.append("  AND GLR.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  AND GLR.STUDENT_ID = :STUDENT_ID");

		param.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 登记成绩
	 * @return
	 */
	@Transactional
	public int updResultRegister(Map searchParams) {
		int rs = 0;
		StringBuilder sql = new StringBuilder();
		
		sql.append("  UPDATE GJT_STUDY_RECORD GSR");
		sql.append("  SET GSR.SUM_SCORE   = '"+ObjectUtils.toString(searchParams.get("SUM_SCORE"))+"',");
		sql.append("  GSR.SCORE_STATE = '"+ObjectUtils.toString(searchParams.get("SCORE_STATE"))+"',");
		sql.append("  GSR.IS_EXEMPT   = '"+ObjectUtils.toString(searchParams.get("IS_EXEMPT"))+"',");
		sql.append("  GSR.GET_POINT   = '"+ObjectUtils.toString(searchParams.get("GET_POINT"))+"',");
		sql.append("  GSR.REGISTER_DT = SYSDATE");
		sql.append("  WHERE GSR.IS_DELETED = 'N'");
		sql.append("  AND GSR.CHOOSE_ID = '"+ObjectUtils.toString(searchParams.get("CHOOSE_ID"))+"'");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();
		return rs;
	}

	public List<Map<String,String>> getHistoryScore(Map<String, Object> formMap) {
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
	
	/**
	 * 查询定时锁定登记成绩
	 * @return
	 */
	public List getRegisterExamList(Map searchParams) {

		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GRR.REC_ID,");
		sql.append("  GEP.EXAM_BATCH_CODE,");
		sql.append("  GRR.STUDENT_ID,");
		sql.append("  GRR.COURSE_ID,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  (SELECT LUD.MY_PROGRESS");
		sql.append("  FROM LCMS_USER_DYNA LUD");
		sql.append("  WHERE LUD.ISDELETED = 'N'");
		sql.append("  AND LUD.CHOOSE_ID = GRR.REC_ID) PROGRESS,");
		sql.append("  (SELECT LUD.MY_POINT");
		sql.append("  FROM LCMS_USER_DYNA LUD");
		sql.append("  WHERE LUD.ISDELETED = 'N'");
		sql.append("  AND LUD.CHOOSE_ID = GRR.REC_ID) MY_POINT,");
		sql.append("  (SELECT MAX(ETU.TEST_POINT)");
		sql.append("  FROM EXAM_TEST_USER ETU");
		sql.append("  WHERE ETU.IS_DELETED = 'N'");
		sql.append("  AND ETU.USER_ID = GRR.REC_ID) TEST_POINT");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_SPECIALTY            GSY,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW       EBC,");
		sql.append("  GJT_EXAM_PLAN_NEW_COURSE GEC,");
		sql.append("  GJT_COURSE               GCE,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_GRADE                GRE,");
		sql.append("  VIEW_TEACH_PLAN          VTP");
		sql.append("  WHERE GEA.IS_DELETED = 0");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND EBC.IS_DELETED = 0");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND GEC.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND (GEC.COURSE_ID = VTP.COURSE_ID OR GEC.COURSE_ID = VTP.SOURCE_COURSE_ID)");
		sql.append("  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  AND EBC.RECORD_END IS NOT NULL");
		sql.append("  AND TO_CHAR(EBC.RECORD_END, 'yyyy-mm-dd') = TO_CHAR(SYSDATE, 'yyyy-mm-dd')");
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 定时锁定登记成绩
	 * @return
	 */
	@Transactional
	public int registerExamState(Map searchParams) {
		int rs = 0;
		StringBuilder sql = new StringBuilder();
		
		sql.append("  UPDATE GJT_REC_RESULT GRRT");
		sql.append("  SET GRRT.UPDATED_DT  = SYSDATE,");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_SCORE")))) {
			sql.append("  GRRT.EXAM_SCORE  = '"+ObjectUtils.toString(searchParams.get("EXAM_SCORE"))+"',");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_SCORE1")))) {
			sql.append("  GRRT.EXAM_SCORE1  = '"+ObjectUtils.toString(searchParams.get("EXAM_SCORE1"))+"',");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PROGRESS")))) {
			sql.append("  GRRT.PROGRESS  = '"+ObjectUtils.toString(searchParams.get("PROGRESS"))+"',");
		}
		sql.append("  GRRT.EXAM_STATE  = '3',");
		sql.append("  GRRT.MEMO        = '定时任务登记锁定成绩中'");
		sql.append("  WHERE GRRT.IS_DELETED = 'N'");
		sql.append("  AND GRRT.REC_ID = '"+ObjectUtils.toString(searchParams.get("REC_ID"))+"'");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();
		return rs;
	}
	
	/**
	 * 定时锁定更新考试状态
	 * @return
	 */
	@Transactional
	public int updateExamState(Map searchParams) {
		int rs = 0;
		/*StringBuilder sql = new StringBuilder();
		
		sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  SET GEA.UPDATED_DT  = SYSDATE,");
		sql.append("  GEA.EXAM_STATUS  = '4' ");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEA.REC_ID = '"+ObjectUtils.toString(searchParams.get("REC_ID"))+"'");
		sql.append("  AND GEA.EXAM_BATCH_CODE = '"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))+"'");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();*/
		return rs;
	}
	
	/**
	 * 考试管理=》考情分析(考试预约明细)
	 * @return
	 */
	public Page getRecordAppointmentList(Map searchParams, PageRequest pageRequst) {
		Map param = getRecordAppointmentListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return (Page) commonDao.queryForPageNative(sql, param, pageRequst);
	}
	
	/**
	 * 考试管理=》考情分析(考试预约明细)
	 * @return
	 */
	public List getRecordAppointmentList(Map searchParams) {
		Map param = getRecordAppointmentListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return commonDao.queryForMapListNative(sql, param);
	}
	
	/**
	 * 考试管理=》考情分析(考试预约明细)
	 * @return
	 */
	public int getExamAppointmentCount(Map searchParams) {
		Map param = getRecordAppointmentListSQL(searchParams);
		String sql = "SELECT COUNT(*) FROM ( ";
		sql += ObjectUtils.toString(param.get("sql"));
		sql += " )";
		param.remove("sql");
		BigDecimal num = (BigDecimal)commonDao.queryObjectNative(sql.toString(), param);
		return Integer.parseInt(ObjectUtils.toString(num));
	}
	
	/**
	 * 考试管理=》考情分析(考试预约明细)(SQL语句)
	 * @return
	 */
	public Map getRecordAppointmentListSQL(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM (  SELECT GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.ZP,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GSI.PYCC_NAME,");
		sql.append("  GSI.GRADE_ID,");
		sql.append("  GSI.GRADE_NAME,");
		sql.append("  GSI.MAJOR,");
		sql.append("  GSI.ZYMC,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.EXAM_NO,");
		sql.append("  GEP.TYPE EXAM_TYPE,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GEP.TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') EXAM_TYPE_NAME,");
		sql.append("  VTP.SOURCE_KCMC,");
		sql.append("  VTP.SOURCE_KCH,");
		sql.append("  VSS.PROGRESS,");
		// sql.append("  VSS.SCORE,");
		
		sql.append("  NVL((CASE GRR.EXAM_STATE");
		sql.append("  WHEN '2' THEN");
		sql.append("  VSS.SCORE");
		sql.append("  ELSE");
		sql.append("  GRR.EXAM_SCORE");
		sql.append("  END), 0) SCORE,");
		
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  GRA.BESPEAK_STATE,");
		sql.append("  NVL(GRA.EXAM_PLAN_LIMIT, 0) EXAM_PLAN_LIMIT");
		sql.append("  FROM VIEW_STUDENT_INFO            GSI,");
		sql.append("  GJT_REC_RESULT               GRR,");
		sql.append("  VIEW_TEACH_PLAN              VTP,");
		sql.append("  VIEW_STUDENT_STUDY_SITUATION VSS,");
		sql.append("  GJT_RECORD_APPOINTMENT       GRA,");
		sql.append("  GJT_EXAM_BATCH_NEW           GEB,");
		sql.append("  GJT_EXAM_PLAN_NEW            GEP");
		sql.append("  WHERE GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.TEACH_PLAN_ID = VTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.REC_ID = VSS.CHOOSE_ID");
		sql.append("  AND GRR.REC_ID = GRA.REC_ID");
		sql.append("  AND GSI.STUDENT_ID = GRA.STUDENT_ID");
		sql.append("  AND GRA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GRA.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GRA.IS_DELETED = 'N'");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GEP.IS_DELETED = 0");
			
		sql.append("  AND GSI.XX_ID = :XX_ID");
		sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		
		param.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_NAME")))){
			sql.append("  AND GEP.EXAM_PLAN_NAME LIKE :EXAM_PLAN_NAME");
			param.put("EXAM_PLAN_NAME", "%"+ObjectUtils.toString(searchParams.get("EXAM_PLAN_NAME"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TYPE")))) {
			sql.append("  AND GEP.TYPE = :TYPE");
			param.put("TYPE", ObjectUtils.toString(searchParams.get("TYPE")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			param.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GSI.GRADE_ID = :GRADE_ID");
			param.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSI.MAJOR = :SPECIALTY_ID");
			param.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		sql.append(" ) tab WHERE 1=1");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
			if ("1".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND tab.SCORE>=tab.EXAM_PLAN_LIMIT");
				sql.append("  AND tab.BESPEAK_STATE = '0'");

			} else if ("2".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND tab.SCORE<tab.EXAM_PLAN_LIMIT");
				sql.append("  AND tab.BESPEAK_STATE = '0'");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND tab.SCORE>=tab.EXAM_PLAN_LIMIT");
				sql.append("  AND tab.BESPEAK_STATE = '1'");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND tab.SCORE<tab.EXAM_PLAN_LIMIT");
				sql.append("  AND tab.BESPEAK_STATE = '1'");
			}
		}
		
		param.put("sql", sql);
		return param;
	}
	
	/**
	 * 考试管理=》导出考试预约情况统计表
	 * @return
	 */
	public List getRecordAppointmentPlan(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GEP.EXAM_NO,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GEP.TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') EXAM_TYPE_NAME,");
		sql.append("  (SELECT to_char(wm_concat(GCE.KCMC || '（' || GCE.KCH || '）'))");
		sql.append("  FROM GJT_EXAM_PLAN_NEW_COURSE GPC, GJT_COURSE GCE");
		sql.append("  WHERE GPC.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GPC.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) COURSE_NAME,");
		sql.append("  (SELECT COUNT(GRA.STUDENT_ID)");
		sql.append("  FROM GJT_RECORD_APPOINTMENT GRA, VIEW_STUDENT_STUDY_SITUATION VSS");
		sql.append("  WHERE GRA.IS_DELETED = 'N'");
		sql.append("  AND GRA.REC_ID = VSS.CHOOSE_ID");
		sql.append("  AND VSS.SCORE >= GRA.EXAM_PLAN_LIMIT");
		sql.append("  AND GRA.BESPEAK_STATE = '0'");
		sql.append("  AND GRA.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GRA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE1,");
		sql.append("  (SELECT COUNT(GRA.STUDENT_ID)");
		sql.append("  FROM GJT_RECORD_APPOINTMENT GRA, VIEW_STUDENT_STUDY_SITUATION VSS");
		sql.append("  WHERE GRA.IS_DELETED = 'N'");
		sql.append("  AND GRA.REC_ID = VSS.CHOOSE_ID");
		sql.append("  AND VSS.SCORE >= GRA.EXAM_PLAN_LIMIT");
		sql.append("  AND GRA.BESPEAK_STATE = '1'");
		sql.append("  AND GRA.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GRA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE2,");
		sql.append("  (SELECT COUNT(GRA.STUDENT_ID)");
		sql.append("  FROM GJT_RECORD_APPOINTMENT GRA, VIEW_STUDENT_STUDY_SITUATION VSS");
		sql.append("  WHERE GRA.IS_DELETED = 'N'");
		sql.append("  AND GRA.REC_ID = VSS.CHOOSE_ID");
		sql.append("  AND VSS.SCORE < GRA.EXAM_PLAN_LIMIT");
		sql.append("  AND GRA.BESPEAK_STATE = '1'");
		sql.append("  AND GRA.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GRA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE3,");
		sql.append("  (SELECT COUNT(GRA.STUDENT_ID)");
		sql.append("  FROM GJT_RECORD_APPOINTMENT GRA, VIEW_STUDENT_STUDY_SITUATION VSS");
		sql.append("  WHERE GRA.IS_DELETED = 'N'");
		sql.append("  AND GRA.REC_ID = VSS.CHOOSE_ID");
		sql.append("  AND VSS.SCORE < GRA.EXAM_PLAN_LIMIT");
		sql.append("  AND GRA.BESPEAK_STATE = '0'");
		sql.append("  AND GRA.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GRA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE4");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP, GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  AND GEB.XX_ID = :XX_ID");
		
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 考试情况明细查询列表
	 * @return
	 */
	public Page getExamDetailList(Map searchParams, PageRequest pageRequst) {
		Map param = getExamDetailListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return (Page) commonDao.queryForPageNative(sql, param, pageRequst);
	}
	
	/**
	 * 考试情况明细查询列表
	 * @return
	 */
	public List getExamDetailList(Map searchParams) {
		Map param = getExamDetailListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return commonDao.queryForMapListNative(sql, param);
	}
	
	/**
	 * 考试情况明细查询列表(统计数字)
	 * @return
	 */
	public int getExamDetailCount(Map searchParams) {
		Map param = getExamDetailListSQL(searchParams);
		String sql = "SELECT COUNT(*) FROM ( ";
		sql += ObjectUtils.toString(param.get("sql"));
		sql += " )";
		param.remove("sql");
		BigDecimal num = (BigDecimal)commonDao.queryObjectNative(sql.toString(), param);
		return Integer.parseInt(ObjectUtils.toString(num));
	}
	
	/**
	 * 考试情况明细查询列表（sql）
	 * @return
	 */
	public Map getExamDetailListSQL(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GSI.ZP,");
		sql.append("  GSI.USER_TYPE,");
		sql.append("  TO_CHAR(GSI.CREATED_DT, 'yyyymm') CREATED_DT,");
		sql.append("  GRE.GRADE_ID,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GYR.GRADE_ID YEAR_ID,");
		sql.append("  GYR.NAME YEAR_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  VTP.SOURCE_COURSE_ID COURSE_ID,");
		sql.append("  VTP.SOURCE_KCMC KCMC,");
		sql.append("  VTP.SOURCE_KCH KCH,");
		sql.append("  GEP.EXAM_NO,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.TYPE EXAM_TYPE,");
		sql.append("  GEP.XK_PERCENT,");
		sql.append("  GEP.EXAM_PLAN_LIMIT,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  EBC.EXAM_BATCH_CODE,");
		sql.append("  EBC.NAME EXAM_BATCH_NAME,");
		sql.append("  VTP.KSDW,");
		sql.append("  VTP.KKXQ,");
		sql.append("  VTP.SOURCE_KCH,");
		sql.append("  VTP.SOURCE_KCMC,");
		sql.append("  (SELECT GOG.ORG_NAME");
		sql.append("  FROM GJT_ORG GOG");
		sql.append("  WHERE GOG.IS_DELETED = 'N'");
		sql.append("  AND GOG.ID = GSI.XXZX_ID) ORG_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GEP.TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') KSFS_NAME,");
		
		sql.append("  (SELECT GLR.XCX_SCORE");
		sql.append("  FROM GJT_LEARN_REPAIR GLR");
		sql.append("  WHERE GLR.IS_DELETED = 'N'");
		sql.append("  AND GLR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GLR.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND ROWNUM = 1) XCX_SCORE,");

		sql.append("  (SELECT GLR.ZJX_SCORE");
		sql.append("  FROM GJT_LEARN_REPAIR GLR");
		sql.append("  WHERE GLR.IS_DELETED = 'N'");
		sql.append("  AND GLR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GLR.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND ROWNUM = 1) ZJX_SCORE,");

		sql.append("  (SELECT GLR.ZCJ_SCORE");
		sql.append("  FROM GJT_LEARN_REPAIR GLR");
		sql.append("  WHERE GLR.IS_DELETED = 'N'");
		sql.append("  AND GLR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GLR.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND ROWNUM = 1) ZCJ_SCORE,");

		sql.append("  (SELECT GLR.RATIO");
		sql.append("  FROM GJT_LEARN_REPAIR GLR");
		sql.append("  WHERE GLR.IS_DELETED = 'N'");
		sql.append("  AND GLR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GLR.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND ROWNUM = 1) EXAM_RATIO,");
		sql.append("  ");
		sql.append("  (SELECT GLR.PROGRESS");
		sql.append("  FROM GJT_LEARN_REPAIR GLR");
		sql.append("  WHERE GLR.IS_DELETED = 'N'");
		sql.append("  AND GLR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GLR.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND ROWNUM = 1) PROGRESS");
		sql.append("  ");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_SPECIALTY            GSY,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW       EBC,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_GRADE                GRE,");
		sql.append("  GJT_YEAR                 GYR,");
		sql.append("  VIEW_TEACH_PLAN          VTP");
		sql.append("  WHERE GEA.IS_DELETED = 0");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND EBC.IS_DELETED = 0");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GEA.REC_ID = GRR.REC_ID");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = EBC.EXAM_BATCH_CODE");
		sql.append("  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  AND GRE.YEAR_ID = GYR.GRADE_ID");

		
		sql.append("  AND GSI.XX_ID = :XX_ID");
		param.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		
		sql.append(" AND EBC.EXAM_BATCH_CODE= :EXAM_BATCH_CODE");
		param.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))){
			sql.append(" AND GSI.XXZX_ID= :XXZX_ID");
			param.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			param.put("PYCC",ObjectUtils.toString(searchParams.get("PYCC")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSI.MAJOR = :SPECIALTY_ID");
			param.put("SPECIALTY_ID",ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			sql.append("  AND GSI.NJ = :NJ");
			param.put("NJ",ObjectUtils.toString(searchParams.get("NJ")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("YEAR_ID")))) {
			sql.append("  AND GYR.GRADE_ID = :YEAR_ID");
			param.put("YEAR_ID",ObjectUtils.toString(searchParams.get("YEAR_ID")));
		}
		
		sql.append("  ) TAB");
		sql.append("  WHERE 1 = 1");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
			if ("1".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND ZCJ_SCORE IS NULL");
				sql.append("  AND EXAM_STATE = '3'");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND ZJX_SCORE = '-1'");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND ZJX_SCORE = '-20'");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND ZJX_SCORE>=60");
				sql.append("  AND XCX_SCORE>=EXAM_PLAN_LIMIT");
			} else if ("5".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND ZJX_SCORE>=0");
				sql.append("  AND ZJX_SCORE<=60");
				sql.append("  AND XCX_SCORE>=EXAM_PLAN_LIMIT");
			} else if ("6".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
				sql.append("  AND ZJX_SCORE>=0");
				sql.append("  AND ZJX_SCORE<=60");
				sql.append("  AND XCX_SCORE<EXAM_PLAN_LIMIT");
			}
		}
		
		param.put("sql", sql);
		return param;
	}
	
	/**
	 * 考试情况明细
	 * @return
	 */
	public List getExamDetailPlan(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GEP.EXAM_NO,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GEP.TYPE");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode') EXAM_TYPE_NAME,");
		sql.append("  (SELECT to_char(wm_concat(GCE.KCMC || '（' || GCE.KCH || '）'))");
		sql.append("  FROM GJT_EXAM_PLAN_NEW_COURSE GPC, GJT_COURSE GCE");
		sql.append("  WHERE GPC.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GPC.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) COURSE_NAME,");
		sql.append("  (SELECT COUNT(GSI.STUDENT_ID)");
		sql.append("  FROM GJT_STUDENT_INFO GSI,");
		sql.append("  GJT_REC_RESULT   GRR,");
		sql.append("  GJT_LEARN_REPAIR GLR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GLR.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.TEACH_PLAN_ID = GLR.TEACH_PLAN_ID");
		sql.append("  AND GSI.STUDENT_ID = GLR.STUDENT_ID");
		sql.append("  AND GLR.EXAM_NO = GEP.EXAM_NO");
		sql.append("  AND GLR.EXAM_BATCH_CODE = GEP.EXAM_BATCH_CODE");
		sql.append("  AND GLR.ZJX_SCORE = '-1'");
		sql.append("  AND GRR.EXAM_SCORE >= GEP.EXAM_PLAN_LIMIT) EXAM_STATE1,");
		sql.append("  ");
		sql.append("  (SELECT COUNT(GSI.STUDENT_ID)");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.REC_ID = GEA.REC_ID");
		sql.append("  AND GRR.EXAM_STATE = '3'");
		sql.append("  AND GRR.EXAM_SCORE >= GEP.EXAM_PLAN_LIMIT");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE2,");
		sql.append("  (SELECT COUNT(GSI.STUDENT_ID)");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_LEARN_REPAIR         GLR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GLR.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.REC_ID = GEA.REC_ID");
		sql.append("  AND GLR.STATUS = '0'");
		sql.append("  AND GRR.EXAM_SCORE >= GEP.EXAM_PLAN_LIMIT");
		sql.append("  AND GLR.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE3,");
		sql.append("  (SELECT COUNT(GSI.STUDENT_ID)");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_LEARN_REPAIR         GLR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GLR.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.REC_ID = GEA.REC_ID");
		sql.append("  AND GLR.STATUS = '1'");
		sql.append("  AND GRR.EXAM_SCORE >= GEP.EXAM_PLAN_LIMIT");
		sql.append("  AND GLR.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE4,");
		sql.append("  (SELECT COUNT(GSI.STUDENT_ID)");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_LEARN_REPAIR         GLR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GLR.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.REC_ID = GEA.REC_ID");
		sql.append("  AND GLR.ZJX_SCORE = '-1'");
		sql.append("  AND GRR.EXAM_SCORE < GEP.EXAM_PLAN_LIMIT");
		sql.append("  AND GLR.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE5,");
		sql.append("  (SELECT COUNT(GSI.STUDENT_ID)");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.REC_ID = GEA.REC_ID");
		sql.append("  AND GRR.EXAM_STATE = '3'");
		sql.append("  AND GRR.EXAM_SCORE < GEP.EXAM_PLAN_LIMIT");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE6,");
		sql.append("  (SELECT COUNT(GSI.STUDENT_ID)");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_REC_RESULT           GRR,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  GJT_LEARN_REPAIR         GLR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GLR.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.REC_ID = GEA.REC_ID");
		sql.append("  AND GLR.Status = '1'");
		sql.append("  AND GRR.EXAM_SCORE < GEP.EXAM_PLAN_LIMIT");
		sql.append("  AND GLR.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID) EXAM_STATE7");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP, GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  AND GEB.XX_ID = :XX_ID");
		
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	
	
	/**
	 * 学生综合信息查询=》考试
	 *
	 * @return
	 */
	public Page<Map<String, Object>> queryStudentExamListByPage(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentExamListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}
	
	/**
	 * 学生综合信息查询=》考试
	 *
	 * @return
	 */
	public long getStudentExamCount(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentExamListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	/**
	 * 学生综合信息查询=》考试
	 *
	 * @return
	 */
	public List<Map<String, Object>> getStudentExamList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentExamListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForStringObjectMapListNative(sql, params);
	}

	/**
	 * 学生综合信息查询=》考试 sql处理类
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> getStudentExamListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("select * from (");
		sql.append("  select t.STUDENT_ID,GR.GRADE_ID,GR.GRADE_NAME,GY.NAME YEAR_NAME,GSY.SPECIALTY_ID,GSY.ZYMC,GSY.RULE_CODE,");
		sql.append("         t.XXZX_ID,GO.Org_Name XXZX_NAME,t.XM,t.XH,t.SJH,t.PYCC,t.AVATAR,t.EENO,t.XJZT,t.USER_TYPE,");
		sql.append("         (select count(*) from GJT_RECORD_APPOINTMENT x where x.is_deleted='N' and x.student_id=t.student_id and x.EXAM_BATCH_CODE=:examBatchCode) SHOULD_EXAM_COUNT,");
		sql.append("         EBN.book_st,EBN.book_end,EBN.books_st,EBN.books_end,");
		sql.append("         NVL(MAKE_COUNT,0) MAKE_COUNT,NVL(BEEN_EXAM_COUNT,0) BEEN_EXAM_COUNT,NVL(BEEN_THROUGH_COUNT,0) BEEN_THROUGH_COUNT,NVL(NOT_THROUGH_COUNT,0) NOT_THROUGH_COUNT");
		sql.append("  from gjt_student_info t");
		sql.append("  inner join GJT_GRADE GR on t.NJ = GR.GRADE_ID");
		sql.append("  INNER JOIN GJT_YEAR GY ON GY.GRADE_ID = GR.YEAR_ID");
		sql.append("  inner join GJT_SPECIALTY GSY on t.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  LEFT JOIN GJT_org GO ON GO.ID = t.XXZX_ID");
		sql.append("  left join (");
		sql.append("       select * from gjt_exam_batch_new x where x.is_deleted=0 and x.EXAM_BATCH_CODE=:examBatchCode and rownum=1");
		sql.append("  ) EBN ON 1=1");
		sql.append("  left join (");
		sql.append("      select x.STUDENT_ID EAC_STUDENT_ID,COUNT(*) MAKE_COUNT,COUNT(CASE x.EXAM_STATUS WHEN '1' THEN 1 WHEN '2' THEN 1 ELSE NULL END) BEEN_EXAM_COUNT,COUNT(CASE x.EXAM_STATUS WHEN '1' THEN 1 ELSE NULL END) BEEN_THROUGH_COUNT,COUNT(CASE x.EXAM_STATUS WHEN '2' THEN 1 ELSE NULL END) NOT_THROUGH_COUNT");
		sql.append("      from gjt_exam_appointment_new x");
		sql.append("      where x.is_deleted=0 and x.EXAM_BATCH_CODE=:examBatchCode");
		sql.append("      group by x.STUDENT_ID");
		sql.append("   ) EAC ON EAC.EAC_STUDENT_ID=T.STUDENT_ID");
		sql.append("  WHERE t.IS_DELETED = 'N'");
		params.put("examBatchCode", searchParams.get("examBatchCode")); // 必传考试计划CODE
		
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND t.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND t.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}
		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND t.USER_TYPE=:userType");
			params.put("userType", userType);
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
			sql.append(" AND t.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		} else if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND t.XXZX_ID IN (:orgChilds)");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND t.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND t.STUDENT_ID =:STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND t.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND t.MAJOR = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND t.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND t.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND t.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		sql.append(") TAB");
		sql.append(" WHERE 1=1");

		/**
		 * 这里的逻辑是:
		 * ├─1.第一种情况：应考科目 = 已约科目					===→ 考试正常
		 * ├─2.第二种情况：应考科目 > 已约科目 (再次细分)
		 * │  ├─2.1.当前时间未到预约时间						===→ 考试正常
		 * │  ├─2.2.当前时间在预约范围之内(再次细分)
		 * │  │  ├─2.2.1.已约满8科							===→ 异常，已约满，需下次再约
		 * │  │  └─2.2.2.未约满8科							===→ 异常，预约范围内，需督促
		 * │  │  
		 * │  └─2.3.当前时间在预约结束之后（如果有第二次预约时间，那么有两个区间，第一次预约结束时间至第二次预约开始时间/第二次预约结束时间之后）(再次细分)
		 * │     ├─2.3.1.已约满8科							===→ 异常，已约满，需下次再约
		 * │     └─2.3.2.未约满8科							===→ 异常，预约已过期，漏报考
		 * │     
		 */
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STATUS")))) {
			if ("1".equals(ObjectUtils.toString(searchParams.get("STATUS")))) {// 考试正常
				sql.append(" AND (SHOULD_EXAM_COUNT=MAKE_COUNT or sysdate<book_st)");
			} if ("2".equals(ObjectUtils.toString(searchParams.get("STATUS")))) {// 异常，预约范围内，需督促
				sql.append(" AND SHOULD_EXAM_COUNT>MAKE_COUNT AND MAKE_COUNT<8 AND (sysdate between book_st and book_end or sysdate between books_st and books_end)");
			} if ("3".equals(ObjectUtils.toString(searchParams.get("STATUS")))) {// 异常，预约已过期，漏报考
				sql.append(" AND SHOULD_EXAM_COUNT>MAKE_COUNT AND MAKE_COUNT<8 AND ((books_end is null and sysdate>book_end) or (books_end is not null and (sysdate between book_end and books_st or sysdate>books_end)))");
			} if ("4".equals(ObjectUtils.toString(searchParams.get("STATUS")))) {// 异常，已约满，需下次再约
				sql.append(" AND SHOULD_EXAM_COUNT>MAKE_COUNT AND MAKE_COUNT>=8");
			}
		}

		sql.append(" ORDER BY MAKE_COUNT DESC NULLS LAST");

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

	/**
	 * 学员考试情况
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> countStudentExamSituation(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("select  count(case MAKE_COUNT when 0 then 1 else null end) NOT_JOIN_COUNT,");
		sql.append("		count(case when MAKE_COUNT>0 then 1 else null end) JOIN_COUNT,");
		sql.append("		count(case when MAKE_COUNT>0 and NOT_THROUGH_COUNT=0 then 1 else null end) THROUGH_COUNT,");
		sql.append("		count(case when MAKE_COUNT>0 and NOT_THROUGH_COUNT>0 then 1 else null end) NOT_THROUGH_COUNT");
		sql.append(" from ( select t.STUDENT_ID,");
		sql.append("         NVL(MAKE_COUNT,0) MAKE_COUNT,NVL(BEEN_EXAM_COUNT,0) BEEN_EXAM_COUNT,NVL(BEEN_THROUGH_COUNT,0) BEEN_THROUGH_COUNT,NVL(NOT_THROUGH_COUNT,0) NOT_THROUGH_COUNT");
		sql.append("  from gjt_student_info t");
		sql.append("  left join (");
		sql.append("      select x.STUDENT_ID EAC_STUDENT_ID,COUNT(*) MAKE_COUNT,COUNT(CASE x.EXAM_STATUS WHEN '1' THEN 1 WHEN '2' THEN 1 ELSE NULL END) BEEN_EXAM_COUNT,COUNT(CASE x.EXAM_STATUS WHEN '1' THEN 1 ELSE NULL END) BEEN_THROUGH_COUNT,COUNT(CASE x.EXAM_STATUS WHEN '2' THEN 1 ELSE NULL END) NOT_THROUGH_COUNT");
		sql.append("      from gjt_exam_appointment_new x");
		sql.append("      where x.is_deleted=0 and x.EXAM_BATCH_CODE=:examBatchCode");
		sql.append("      group by x.STUDENT_ID");
		sql.append("   ) EAC ON EAC.EAC_STUDENT_ID=T.STUDENT_ID");
		sql.append("  WHERE t.IS_DELETED = 'N'");
		params.put("examBatchCode", searchParams.get("examBatchCode")); // 必传考试计划CODE

		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND t.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND t.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}
		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND t.USER_TYPE=:userType");
			params.put("userType", userType);
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
			sql.append(" AND t.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		} else if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND t.XXZX_ID IN (:orgChilds)");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND t.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND t.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		sql.append(") TAB");

		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(), params);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
}
