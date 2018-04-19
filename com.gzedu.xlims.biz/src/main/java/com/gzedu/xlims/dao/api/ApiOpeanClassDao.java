package com.gzedu.xlims.dao.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;

@Repository
public class ApiOpeanClassDao {
	
	@Autowired
	private CommonDao commonDao;
	
	/**
	 * 查询学员信息
	 * @return
	 */
	public List getStudentInfo(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.EENO,");
		sql.append("  GSI.DZXX,");
		sql.append("  GSI.ZP,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XBM,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  GSI.ATID,");
		sql.append("  GSI.HKSZD,");
		sql.append("  GSI.GRADE_SPECIALTY_ID,");
		sql.append("  TAB.BZR_ID,");
		sql.append("  TAB.CLASS_ID,");
		sql.append("  TAB.BJMC,");
		sql.append("  (SELECT LUS.STUD_ID");
		sql.append("  FROM LCMS_USER_STUD LUS");
		sql.append("  WHERE LUS.ISDELETED = 'N'");
		sql.append("  AND LUS.STUD_ID = GSI.STUDENT_ID) PCOURSE_STUD_ID,");
		sql.append("  GSO.SCHOOL_MODEL,");
		sql.append("  GSO.APPID");
		sql.append("  FROM GJT_GRADE_SPECIALTY GGS, GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN (SELECT GCI.BZR_ID, GCI.CLASS_ID, GCI.BJMC, GCS.STUDENT_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID) TAB ON GSI.STUDENT_ID =");
		sql.append("  TAB.STUDENT_ID");
		
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO GSO ON GSI.XX_ID = GSO.ID");
		
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GGS.IS_DELETED = 'N'");
		sql.append("  AND GGS.STATUS = 1");
		sql.append("  AND GGS.ID = GSI.GRADE_SPECIALTY_ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 更新同步学习平台状态
	 * @return
	 */
	@Transactional
	public int updStudentSyncState(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_STUDENT_INFO GSI");
		sql.append("  SET GSI.SYNC_STATUS = 'Y'");
		sql.append("  WHERE GSI.STUDENT_ID = :STUDENT_ID");
		
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询是否还有未选课的课程
	 * @return
	 */
	public List getNoRecResult(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT STUDENT_ID,");
		sql.append("  ACTUAL_GRADE_ID,");
		sql.append("  COURSE_ID,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  XXZX_ID,");
		sql.append("  XX_ID,");
		sql.append("  TERMCOURSE_ID");
		sql.append("  FROM (SELECT GSI.STUDENT_ID,");
		sql.append("  GGS.GRADE_ID,");
		sql.append("  GGS.SPECIALTY_ID,");
		sql.append("  GTP.TEACH_PLAN_ID,");
		sql.append("  GTP.COURSE_ID,");
		sql.append("  GTP.ACTUAL_GRADE_ID,");
		sql.append("  GSI.XXZX_ID,");
		sql.append("  GSI.XX_ID,");
		sql.append("  (SELECT GCI.CLASS_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND ROWNUM = 1) CLASS_ID,");
		sql.append("  (SELECT GRR.REC_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.COURSE_ID = GTP.COURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND ROWNUM = 1) REC_ID,");
		
		sql.append("  (SELECT GTC.TERMCOURSE_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERM_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = GTP.COURSE_ID");
		sql.append("  AND ROWNUM = 1) TERMCOURSE_ID");
		
		sql.append("  FROM GJT_GRADE_SPECIALTY GGS,");
		sql.append("  VIEW_TEACH_PLAN     GTP,");
		sql.append("  GJT_SPECIALTY       GSY,");
		sql.append("  GJT_GRADE           GGE,");
		sql.append("  GJT_STUDENT_INFO    GSI");
		sql.append("  WHERE GGS.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GGS.GRADE_ID = GTP.GRADE_ID");
		sql.append("  AND GGS.SPECIALTY_ID = GTP.KKZY");
		sql.append("  AND GGS.SPECIALTY_ID = GSY.SPECIALTY_ID");
		sql.append("  AND GGS.GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GGS.SPECIALTY_ID");
		sql.append("  AND GSI.NJ = GGS.GRADE_ID");
		sql.append("  AND GGS.ID = GTP.GRADE_SPECIALTY_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID) TAB");
		sql.append("  WHERE 1 = 1");
		sql.append("  AND TAB.REC_ID IS NULL");
		sql.append("  AND TAB.CLASS_ID IS NOT NULL");
		
		// 学习网必须先开课再选课，其它模式还是按原来的
		if ("5".equals(searchParams.get("SCHOOL_MODEL"))) {
			sql.append("  AND TAB.TERMCOURSE_ID IS NOT NULL");
		}

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 新增选课
	 * @return
	 */
	@Transactional
	public int addRecResult(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_REC_RESULT");
		sql.append("  (REC_ID,");
		sql.append("  STUDENT_ID,");
		sql.append("  TERM_ID,");
		sql.append("  COURSE_ID,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  UPDATED_BY,");
		sql.append("  XXZX_ID,");
		sql.append("  XX_ID)");
		sql.append("  SELECT getsequence,");
		sql.append("  STUDENT_ID,");
		sql.append("  ACTUAL_GRADE_ID,");
		sql.append("  COURSE_ID,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  :BATCHNO,");
		sql.append("  XXZX_ID,");
		sql.append("  XX_ID");
		sql.append("  FROM (SELECT GSI.STUDENT_ID,");
		sql.append("  GGS.GRADE_ID,");
		sql.append("  GGS.SPECIALTY_ID,");
		sql.append("  GTP.TEACH_PLAN_ID,");
		sql.append("  GTP.COURSE_ID,");
		sql.append("  GTP.ACTUAL_GRADE_ID,");
		sql.append("  GSI.XXZX_ID,");
		sql.append("  GSI.XX_ID,");
		sql.append("  (SELECT GCI.CLASS_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND ROWNUM = 1) CLASS_ID,");
		sql.append("  (SELECT GRR.REC_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.COURSE_ID = GTP.COURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND ROWNUM = 1) REC_ID,");
		sql.append("  (SELECT GTC.TERMCOURSE_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERM_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = GTP.COURSE_ID");
		sql.append("  AND ROWNUM = 1) TERMCOURSE_ID");
		sql.append("  FROM GJT_GRADE_SPECIALTY GGS,");
		sql.append("  VIEW_TEACH_PLAN     GTP,");
		sql.append("  GJT_SPECIALTY       GSY,");
		sql.append("  GJT_GRADE           GGE,");
		sql.append("  GJT_STUDENT_INFO    GSI");
		sql.append("  WHERE GGS.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GGS.GRADE_ID = GTP.GRADE_ID");
		sql.append("  AND GGS.SPECIALTY_ID = GTP.KKZY");
		sql.append("  AND GGS.SPECIALTY_ID = GSY.SPECIALTY_ID");
		sql.append("  AND GGS.GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GGS.SPECIALTY_ID");
		sql.append("  AND GSI.NJ = GGS.GRADE_ID");
		sql.append("  AND GGS.ID = GTP.GRADE_SPECIALTY_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID) TAB");
		sql.append("  WHERE 1 = 1");
		sql.append("  AND TAB.REC_ID IS NULL");
		sql.append("  AND TAB.CLASS_ID IS NOT NULL");

		param.put("BATCHNO", ObjectUtils.toString(searchParams.get("BATCHNO")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		// 学习网模式先开课再选课
		if ("5".equals(param.get("SCHOOL_MODEL"))) {
			sql.append("  AND TAB.TERMCOURSE_ID IS NOT NULL");
		}
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	
	/**
	 * 查询未分配到课程班的选课
	 * @return
	 */
	public List getNoCourseClass(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GSI.XXZX_ID,");
		sql.append("  GSI.XX_ID,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  GRR.REC_ID,");
		sql.append("  GRR.TEACH_PLAN_ID,");
		sql.append("  GTP.ACTUAL_GRADE_ID TERM_ID,");
		sql.append("  GTP.COURSE_ID,");
		sql.append("  GCE.KCMC,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GTP.KCH,");
		sql.append("  (SELECT GTC.TERMCOURSE_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERMCOURSE_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GTC.COURSE_ID = GTP.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.XX_ID = GSI.XX_ID");
		sql.append("  AND ROWNUM = 1) PLAN_TERMCOURSE_ID,");
		sql.append("  (SELECT GTC.TERMCOURSE_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.COURSE_ID = GTP.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.XX_ID = GSI.XX_ID");
		sql.append("  AND ROWNUM = 1) TERMCOURSE_ID,");
		sql.append("  (SELECT GCI.CLASS_ID");
		sql.append("  FROM GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_CLASS_STUDENT   GCS");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GTC.COURSE_ID = GTP.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  AND ROWNUM = 1) CLASS_ID,");
		
		sql.append("  (SELECT GRRT.TERMCOURSE_ID");
		sql.append("  FROM GJT_REC_RESULT GRRT, VIEW_TEACH_PLAN GTPN");
		sql.append("  WHERE GRRT.IS_DELETED = 'Y'");
		sql.append("  AND GRRT.TEACH_PLAN_ID = GTPN.TEACH_PLAN_ID");
		sql.append("  AND GRRT.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRRT.COURSE_ID = GRR.COURSE_ID");
		sql.append("  AND GRRT.UPDATED_DT =");
		sql.append("  (SELECT MAX(GRRT1.UPDATED_DT)");
		sql.append("  FROM GJT_REC_RESULT GRRT1, VIEW_TEACH_PLAN GTPN1");
		sql.append("  WHERE GRRT1.IS_DELETED = 'Y'");
		sql.append("  AND GRRT1.TEACH_PLAN_ID = GTPN1.TEACH_PLAN_ID");
		sql.append("  AND GTPN.COURSE_ID = GTPN1.COURSE_ID");
		sql.append("  AND GRRT1.STUDENT_ID = GRRT.STUDENT_ID) and rownum=1) OLD_TERMCOURSE_ID");
		
		sql.append("  FROM GJT_GRADE_SPECIALTY GGS,");
		sql.append("  VIEW_TEACH_PLAN     GTP,");
		sql.append("  GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  GJT_COURSE          GCE,");
		sql.append("  GJT_GRADE           GRE");
		sql.append("  WHERE GGS.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GGS.ID = GTP.GRADE_SPECIALTY_ID");
		sql.append("  AND GGS.GRADE_ID = GTP.GRADE_ID");
		sql.append("  AND GGS.SPECIALTY_ID = GTP.KKZY");
		sql.append("  AND GSI.MAJOR = GGS.SPECIALTY_ID");
		sql.append("  AND GSI.NJ = GGS.GRADE_ID");
		sql.append("  AND GGS.ID = GTP.GRADE_SPECIALTY_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GTP.COURSE_ID = GRR.COURSE_ID");
		sql.append("  AND GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GRE.GRADE_ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID) TAB");
		sql.append("  WHERE TAB.CLASS_ID IS NULL");
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询需要分配的班级
	 * @return
	 */
	public List getClassInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GCI.CLASS_ID,");
		sql.append("  GCI.TERMCOURSE_ID,");
		sql.append("  GCI.BH,");
		sql.append("  GCI.BJMC,");
		sql.append("  (SELECT COUNT(GCS.CLASS_STUDENT_ID)");
		sql.append("  FROM GJT_CLASS_STUDENT GCS, GJT_STUDENT_INFO GSI");
		sql.append("  WHERE GCS.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID) STUDENT_COUNT,");
		sql.append("  NVL(GCI.LIMIT_NUM, 500) LIMIT_NUM");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.AOTU_FLG = 'Y'");
		sql.append("  AND GCI.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		
		if ("Y".equals(ObjectUtils.toString(searchParams.get("TURN_TERM_FLG"))) 
				&& EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OLD_TERMCOURSE_ID")))) {
			sql.append("  AND GCI.OLD_TERMCOURSE_ID = :OLD_TERMCOURSE_ID");
			param.put("OLD_TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("OLD_TERMCOURSE_ID")));
			sql.append(" AND GCI.TURN_TERM_FLG = 'Y'");
		} else {
			sql.append(" AND GCI.OLD_TERMCOURSE_ID IS NULL");
			sql.append(" AND GCI.TURN_TERM_FLG = 'N'");
		}
		
		sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID) TAB");
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		sql.append("  WHERE 1 = 1");
		sql.append("  AND STUDENT_COUNT < LIMIT_NUM");
		sql.append("  ORDER BY STUDENT_COUNT DESC");

		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 新增课程班学员
	 * @return
	 */
	@Transactional
	public int addCourseClassStu(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_CLASS_STUDENT");
		sql.append("  (CLASS_STUDENT_ID, CLASS_ID, XXZX_ID, XX_ID, STUDENT_ID)");
		sql.append("  VALUES");
		sql.append("  (:CLASS_STUDENT_ID, :CLASS_ID, :XXZX_ID, :XX_ID, :STUDENT_ID)");

		param.put("CLASS_STUDENT_ID", ObjectUtils.toString(searchParams.get("CLASS_STUDENT_ID")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		param.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
		
	/**
	 * 查询未同步到学习平台的选课
	 * @return
	 */
	public List getNoSyncRecResult(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GRR.REC_ID,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  LUC.CHOOSE_ID,");
		sql.append("  GCI.TERMCOURSE_ID,");
		sql.append("  GCI.CLASS_ID,");
		sql.append("  LUC.TERMCOURSE_ID AS P_TERMCOURSE_ID,");
		sql.append("  LUC.CLASS_ID AS P_CLASS_ID");
		sql.append("  FROM GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_CLASS_STUDENT   GCS,");
		sql.append("  VIEW_TEACH_PLAN     GTP,");
		sql.append("  GJT_GRADE_SPECIALTY GGS,");
		sql.append("  GJT_REC_RESULT      GRR");
		sql.append("  LEFT JOIN LCMS_USER_CHOOSE LUC ON GRR.REC_ID = LUC.CHOOSE_ID");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GGS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GTP.GRADE_SPECIALTY_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GRR.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GRR.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID)");
		sql.append("  WHERE 1 = 1");
		sql.append("  AND CHOOSE_ID IS NULL");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 更新选课同步状态
	 * @return
	 */
	@Transactional
	public int updRecSyncStatus(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET GRR.UPDATED_DT = SYSDATE, GRR.SYNC_STATUS = 'Y'");
		sql.append("  WHERE GRR.REC_ID = :REC_ID");

		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 更新选课同步状态
	 * @return
	 */
	@Transactional
	public int updClassStuSyncStatus(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_CLASS_STUDENT GCS");
		sql.append("  SET GCS.UPDATED_DT = SYSDATE, GCS.SYNC_STATUS = 'Y'");
		sql.append("  WHERE GCS.CLASS_ID = :CLASS_ID");
		sql.append("  AND GCS.STUDENT_ID = :STUDENT_ID");

		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询未合并的教学计划
	 * @return
	 */
	public List getNoTermCourse(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GRADE_ID,");
		sql.append("  COURSE_ID,");
		sql.append("  XX_ID,");
		sql.append("  (SELECT GTC.TERMCOURSE_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.COURSE_ID = TAB.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = TAB.GRADE_ID) TERMCOURSE_ID");
		sql.append("  FROM (SELECT GGE.GRADE_ID, GCE.COURSE_ID, GGE.XX_ID");
		sql.append("  FROM VIEW_TEACH_PLAN GTP, GJT_COURSE GCE, GJT_GRADE GGE");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GTP.XX_ID = :XX_ID");
		sql.append("  GROUP BY GGE.GRADE_ID, GCE.COURSE_ID, GGE.XX_ID) TAB)");
		sql.append("  WHERE 1 = 1");
		sql.append("  AND TERMCOURSE_ID IS NULL");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询合并的教学计划Id
	 * @return
	 */
	public List getTeachPlanId(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GTP.TEACH_PLAN_ID,");
		sql.append("  (SELECT COUNT(GSI.STUDENT_ID)");
		sql.append("  FROM GJT_CLASS_INFO    GCI,");
		sql.append("  GJT_CLASS_STUDENT GCS,");
		sql.append("  GJT_REC_RESULT    GRR,");
		sql.append("  GJT_STUDENT_INFO  GSI");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID) CHOOSE_COUNT");
		sql.append("  FROM VIEW_TEACH_PLAN GTP");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = :GRADE_ID");
		sql.append("  AND GTP.COURSE_ID = :COURSE_ID");
		sql.append("  AND GTP.XX_ID = :XX_ID)");
		sql.append("  ORDER BY CHOOSE_COUNT DESC");

		param.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		param.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 新增期课程
	 * @return
	 */
	@Transactional
	public int addTermCourseinfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_TERM_COURSEINFO");
		sql.append("  (TERMCOURSE_ID, TERM_ID, COURSE_ID, XX_ID)");
		sql.append("  VALUES");
		sql.append("  (:TERMCOURSE_ID, :TERM_ID, :COURSE_ID, :XX_ID)");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询需要初始化TERMCOURSE_ID的班级
	 * @return
	 */
	public List getClassTerm(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GCI.CLASS_ID,");
		sql.append("  (SELECT COUNT(GCS.CLASS_STUDENT_ID)");
		sql.append("  FROM GJT_STUDENT_INFO GSI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID) STUDENT_COUNT");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.TEACH_PLAN_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID) TAB");
		sql.append("  WHERE 1 = 1");
		sql.append("  ORDER BY STUDENT_COUNT DESC");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 初始化课程班级里面的期课程ID
	 * @return
	 */
	@Transactional
	public int updClassTerm(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_CLASS_INFO GCI");
		sql.append("  SET GCI.AOTU_FLG = 'Y', GCI.TERMCOURSE_ID = :TERMCOURSE_ID");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.CLASS_ID = :CLASS_ID");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 初始化选课里面的期课程ID
	 * @return
	 */
	@Transactional
	public int updRecTerm(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET GRR.TERMCOURSE_ID = :TERMCOURSE_ID");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.TEACH_PLAN_ID = :TEACH_PLAN_ID");
		sql.append("  AND GRR.STUDENT_ID IN (SELECT GCS.STUDENT_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
		sql.append("  AND GCI.TERMCOURSE_ID = :TERMCOURSE_ID)");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		param.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询需要调班的选课数据
	 * @return
	 */
	public List getRecClassList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GRR.REC_ID,");
		sql.append("  GTP.COURSE_ID,");
		sql.append("  GTP.TEACH_PLAN_ID,");
		sql.append("  GRR.TERMCOURSE_ID,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GTC.TERMCOURSE_ID NEW_TERMCOURSE_ID,");
		sql.append("  GTC.XX_ID,");
		sql.append("  GC.KCMC,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GTC.TERM_ID,");
		sql.append("  (SELECT DISTINCT GCI.CLASS_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GCI.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND ROWNUM <= 1) CLASS_ID,");
		
		sql.append("  (SELECT DISTINCT GCS.CLASS_STUDENT_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GCI.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND ROWNUM <= 1) CLASS_STUDENT_ID,");

		sql.append("  (SELECT COUNT(GCI.CLASS_ID)");
		sql.append("  FROM GJT_CLASS_INFO GCI");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.AOTU_FLG = 'Y'");
		sql.append("  AND GCI.TERMCOURSE_ID = GTC.TERMCOURSE_ID) CLASS_COUNT");
		sql.append("  FROM GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  VIEW_TEACH_PLAN     GTP,");
		sql.append("  GJT_COURSE          GC,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_GRADE           GRE");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GTC.TERM_ID");
		sql.append("  AND GTP.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GRE.GRADE_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID");
		sql.append("  AND GTP.TEACH_PLAN_ID NOT IN");
		sql.append("  (SELECT GTPN.TEACH_PLAN_ID");
		sql.append("  FROM GJT_GRADE_SPECIALTY GGS, VIEW_TEACH_PLAN GTPN");
		sql.append("  WHERE GGS.IS_DELETED = 'N'");
		sql.append("  AND GTPN.IS_DELETED = 'N'");
		sql.append("  AND GGS.ID = GTPN.GRADE_SPECIALTY_ID");
		sql.append("  AND GGS.APPLY_RANGE = '溢达学历班专用')");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 删除原有课程班关系数据
	 * @return
	 */
	@Transactional
	public int delClassStudent(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_CLASS_STUDENT GCS");
		sql.append("  SET GCS.IS_DELETED = 'Y',");
		sql.append("  GCS.UPDATED_DT = SYSDATE,");
		sql.append("  GCS.UPDATED_BY = '调班'");
		sql.append("  WHERE GCS.CLASS_STUDENT_ID = :CLASS_STUDENT_ID");

		param.put("CLASS_STUDENT_ID", ObjectUtils.toString(searchParams.get("CLASS_STUDENT_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 新增学员课程班级数据
	 * @return
	 */
	@Transactional
	public int addClassStudent(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_CLASS_STUDENT");
		sql.append("  (CLASS_STUDENT_ID, CLASS_ID, XX_ID, STUDENT_ID)");
		sql.append("  VALUES");
		sql.append("  (:CLASS_STUDENT_ID, :CLASS_ID, :XX_ID, :STUDENT_ID)");

		param.put("CLASS_STUDENT_ID", ObjectUtils.toString(searchParams.get("CLASS_STUDENT_ID")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 删除原有选课数据
	 * @return
	 */
	@Transactional
	public int delRecInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET GRR.UPDATED_DT = SYSDATE,");
		sql.append("  GRR.UPDATED_BY = '调班',");
		sql.append("  GRR.IS_DELETED = 'Y'");
		sql.append("  WHERE GRR.REC_ID = :REC_ID");

		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 新增选课数据
	 * @return
	 */
	@Transactional
	public int addRecInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_REC_RESULT");
		sql.append("  (REC_ID,");
		sql.append("  STUDENT_ID,");
		sql.append("  TERM_ID,");
		sql.append("  COURSE_ID,");
		sql.append("  IS_OVER,");
		sql.append("  IS_RESERVE,");
		sql.append("  RESERVE_TIME,");
		sql.append("  EXAM_SCORE,");
		sql.append("  EXAM_SCORE1,");
		sql.append("  EXAM_SCORE2,");
		sql.append("  GET_CREDITS,");
		sql.append("  SIGNUP_ID,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  IS_RESERVE_BOOK,");
		sql.append("  XXZX_ID,");
		sql.append("  XX_ID,");
		sql.append("  MEMO,");
		sql.append("  EXAM_STATE,");
		sql.append("  COURSE_SCHEDULE,");
		sql.append("  DELETED_TYPE,");
		sql.append("  ORG_CODE,");
		sql.append("  ORG_ID,");
		sql.append("  SCORE_STATE,");
		sql.append("  BESPEAK_STATE,");
		sql.append("  PAY_STATE,");
		sql.append("  ORDER_MARK,");
		sql.append("  REMARK,");
		sql.append("  TERMCOURSE_ID)");
		sql.append("  SELECT :NEW_REC_ID,");
		sql.append("  GRR.STUDENT_ID,");
		sql.append("  :TERM_ID,");
		sql.append("  GRR.COURSE_ID,");
		sql.append("  GRR.IS_OVER,");
		sql.append("  GRR.IS_RESERVE,");
		sql.append("  GRR.RESERVE_TIME,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.GET_CREDITS,");
		sql.append("  GRR.SIGNUP_ID,");
		sql.append("  GRR.TEACH_PLAN_ID,");
		sql.append("  GRR.IS_RESERVE_BOOK,");
		sql.append("  GRR.XXZX_ID,");
		sql.append("  GRR.XX_ID,");
		sql.append("  GRR.MEMO,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  GRR.COURSE_SCHEDULE,");
		sql.append("  GRR.DELETED_TYPE,");
		sql.append("  GRR.ORG_CODE,");
		sql.append("  GRR.ORG_ID,");
		sql.append("  GRR.SCORE_STATE,");
		sql.append("  GRR.BESPEAK_STATE,");
		sql.append("  GRR.PAY_STATE,");
		sql.append("  GRR.ORDER_MARK,");
		sql.append("  GRR.REMARK,");
		sql.append("  :TERMCOURSE_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.REC_ID = :REC_ID");

		param.put("NEW_REC_ID", ObjectUtils.toString(searchParams.get("NEW_REC_ID")));
		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 删除调班学情数据
	 * @return
	 */
	@Transactional
	public int delStudyRecord(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_STUDY_RECORD GSR");
		sql.append("  SET GSR.IS_DELETED = 'N',");
		sql.append("  GSR.UPDATED_DT = SYSDATE,");
		sql.append("  GSR.UPDATED_BY = '调班'");
		sql.append("  WHERE GSR.CHOOSE_ID = :REC_ID");

		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 新增调班学情数据
	 * @return
	 */
	@Transactional
	public int addStudyRecord(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_STUDY_RECORD");
		sql.append("  (CHOOSE_ID,");
		sql.append("  STUDENT_ID,");
		sql.append("  COURSE_ID,");
		sql.append("  SCHEDULE,");
		sql.append("  FIRST_DATE,");
		sql.append("  LAST_DATE,");
		sql.append("  LOGIN_COUNT,");
		sql.append("  LOGIN_TIME,");
		sql.append("  IS_ONLINE,");
		sql.append("  REGISTER_DT,");
		sql.append("  BEFORE_DT,");
		sql.append("  ACT_COUNT,");
		sql.append("  MY_ACTCOUNT,");
		sql.append("  MY_MUSTACTCOUNT,");
		sql.append("  IS_EXEMPT,");
		sql.append("  MUST_ACTCOUNT,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  APP_ONLINE_COUNT,");
		sql.append("  PC_ONLINE_COUNT,");
		sql.append("  PC_ONLINE_TIME,");
		sql.append("  APP_ONLINE_TIME)");
		sql.append("  SELECT :NEW_REC_ID,");
		sql.append("  GSR.STUDENT_ID,");
		sql.append("  GSR.COURSE_ID,");
		sql.append("  GSR.SCHEDULE,");
		sql.append("  GSR.FIRST_DATE,");
		sql.append("  GSR.LAST_DATE,");
		sql.append("  GSR.LOGIN_COUNT,");
		sql.append("  GSR.LOGIN_TIME,");
		sql.append("  GSR.IS_ONLINE,");
		sql.append("  GSR.REGISTER_DT,");
		sql.append("  GSR.BEFORE_DT,");
		sql.append("  GSR.ACT_COUNT,");
		sql.append("  GSR.MY_ACTCOUNT,");
		sql.append("  GSR.MY_MUSTACTCOUNT,");
		sql.append("  GSR.IS_EXEMPT,");
		sql.append("  GSR.MUST_ACTCOUNT,");
		sql.append("  GSR.TEACH_PLAN_ID,");
		sql.append("  GSR.APP_ONLINE_COUNT,");
		sql.append("  GSR.PC_ONLINE_COUNT,");
		sql.append("  GSR.PC_ONLINE_TIME,");
		sql.append("  GSR.APP_ONLINE_TIME");
		sql.append("  FROM GJT_STUDY_RECORD GSR");
		sql.append("  WHERE GSR.CHOOSE_ID = :REC_ID");

		param.put("NEW_REC_ID", ObjectUtils.toString(searchParams.get("NEW_REC_ID")));
		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 删除调班学情数据
	 * @return
	 */
	@Deprecated
	@Transactional
	public int delStudySituation(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_STUDENT_STUDY_SITUATION GSS");
		sql.append("  SET GSS.UPDATED_DT = SYSDATE,");
		sql.append("  GSS.UPDATED_BY = '调班',");
		sql.append("  GSS.IS_DELETED = 'Y'");
		sql.append("  WHERE GSS.CHOOSE_ID = :REC_ID");

		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 新增调班学情数据
	 * @return
	 */
	@Deprecated
	@Transactional
	public int addStudySituation(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_STUDENT_STUDY_SITUATION");
		sql.append("  (CHOOSE_ID,");
		sql.append("  SCORE,");
		sql.append("  LOGIN_TIMES,");
		sql.append("  PROGRESS,");
		sql.append("  STUDENT_ID,");
		sql.append("  EXAM_SCORE,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  LAST_LOGIN_DATE,");
		sql.append("  ONLINE_TIME,");
		sql.append("  COURSE_ID,");
		sql.append("  FIRST_DATE,");
		sql.append("  ACT_COUNT,");
		sql.append("  MY_ACTCOUNT,");
		sql.append("  MY_MUSTACTCOUNT,");
		sql.append("  APP_ONLINE_COUNT,");
		sql.append("  PC_ONLINE_COUNT,");
		sql.append("  PC_ONLINE_TIME,");
		sql.append("  APP_ONLINE_TIME,");
		sql.append("  MUST_ACTCOUNT)");
		sql.append("  SELECT :NEW_REC_ID,");
		sql.append("  GSS.SCORE,");
		sql.append("  GSS.LOGIN_TIMES,");
		sql.append("  GSS.PROGRESS,");
		sql.append("  GSS.STUDENT_ID,");
		sql.append("  GSS.EXAM_SCORE,");
		sql.append("  GSS.TEACH_PLAN_ID,");
		sql.append("  GSS.LAST_LOGIN_DATE,");
		sql.append("  GSS.ONLINE_TIME,");
		sql.append("  GSS.COURSE_ID,");
		sql.append("  GSS.FIRST_DATE,");
		sql.append("  GSS.ACT_COUNT,");
		sql.append("  GSS.MY_ACTCOUNT,");
		sql.append("  GSS.MY_MUSTACTCOUNT,");
		sql.append("  GSS.APP_ONLINE_COUNT,");
		sql.append("  GSS.PC_ONLINE_COUNT,");
		sql.append("  GSS.PC_ONLINE_TIME,");
		sql.append("  GSS.APP_ONLINE_TIME,");
		sql.append("  GSS.MUST_ACTCOUNT");
		sql.append("  FROM GJT_STUDENT_STUDY_SITUATION GSS");
		sql.append("  WHERE GSS.CHOOSE_ID = :REC_ID");

		param.put("NEW_REC_ID", ObjectUtils.toString(searchParams.get("NEW_REC_ID")));
		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 更新选课表中的期课程
	 * @return
	 */
	@Transactional
	public int updRecTermCourse(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET GRR.TERM_ID = :TERM_ID, GRR.TERMCOURSE_ID = :TERMCOURSE_ID, GRR.UPDATED_DT = SYSDATE");
		sql.append("  WHERE GRR.REC_ID = :REC_ID");

		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询课程班级的辅导老师和督导老师
	 * @return
	 */
	public List getCourseTeacher(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GEI.EMPLOYEE_ID,");
		sql.append("  GEI.XM,");
		sql.append("  GUA.LOGIN_ACCOUNT,");
		sql.append("  GUA.PASSWORD,");
		sql.append("  GEI.SJH,");
		sql.append("  GEI.DZXX,");
		sql.append("  GUA.EENO,");
		sql.append("  GEI.ZP,");
		sql.append("  GEI.XBM,");
		sql.append("  GEI.CREATED_BY,");
		sql.append("  GCI.TERMCOURSE_ID,");
		sql.append("  GCI.CLASS_ID,");
		sql.append("  GSI.APPID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_USER_ACCOUNT    GUA,");
		sql.append("  GJT_EMPLOYEE_INFO   GEI,");
		sql.append("  GJT_SCHOOL_INFO GSI");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GUA.IS_DELETED = 'N'");
		sql.append("  AND GEI.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GTC.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("  AND GCI.BZR_ID = GEI.EMPLOYEE_ID");
		sql.append("  AND GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  AND GSI.ID = GTC.XX_ID");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")))) {
			sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID");
			param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		} else {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询课程班级的辅导老师和督导老师
	 * @return
	 */
	public List getCourseInspector(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GEI.EMPLOYEE_ID,");
		sql.append("  GEI.XM,");
		sql.append("  GUA.LOGIN_ACCOUNT,");
		sql.append("  GUA.PASSWORD,");
		sql.append("  GEI.SJH,");
		sql.append("  GEI.DZXX,");
		sql.append("  GUA.EENO,");
		sql.append("  GEI.ZP,");
		sql.append("  GEI.XBM,");
		sql.append("  GEI.CREATED_BY,");
		sql.append("  GCI.TERMCOURSE_ID,");
		sql.append("  GCI.CLASS_ID,");
		sql.append("  GSI.APPID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_USER_ACCOUNT    GUA,");
		sql.append("  GJT_EMPLOYEE_INFO   GEI,");
		sql.append("  GJT_SCHOOL_INFO GSI");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GUA.IS_DELETED = 'N'");
		sql.append("  AND GEI.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GTC.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("  AND GCI.SUPERVISOR_ID = GEI.EMPLOYEE_ID");
		sql.append("  AND GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  AND GSI.ID = GTC.XX_ID");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")))) {
			sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID");
			param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		} else {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 更新课程班中的督导老师和辅导老师
	 * @return
	 */
	@Transactional
	public int updCourseClassTch(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_CLASS_INFO GCI");
		sql.append("  SET GCI.UPDATED_DT = SYSDATE");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OLD_BZR_ID")))) {
			sql.append(" , GCI.BZR_ID = :OLD_BZR_ID");
			param.put("OLD_BZR_ID", ObjectUtils.toString(searchParams.get("OLD_BZR_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OLD_SUPERVISOR_ID")))) {
			sql.append(" , GCI.SUPERVISOR_ID = :OLD_SUPERVISOR_ID");
			param.put("OLD_SUPERVISOR_ID", ObjectUtils.toString(searchParams.get("OLD_SUPERVISOR_ID")));
		}
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_ID = :CLASS_ID");

		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询期课程未同步到学习平台的数据
	 * @return
	 */
	public List getTermCourseNoPcourse(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GTC.TERMCOURSE_ID,");
		sql.append("  GTC.COURSE_ID,");
		sql.append("  GTC.TERM_ID,");
		sql.append("  GTC.XX_ID,");
		sql.append("  LTC.TERMCOURSE_ID PCOURSE_TERMCOURSE_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC");
		sql.append("  LEFT JOIN LCMS_TERM_COURSEINFO LTC ON GTC.TERMCOURSE_ID =");
		sql.append("  LTC.TERMCOURSE_ID");
		sql.append("  AND LTC.ISDELETED = 'N'");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GTC.TERM_ID = :TERM_ID");
		
		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询课程班级未同步到学习平台的数据
	 * @return
	 */
	public List getCourseClassNoPcourse(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GTC.TERMCOURSE_ID,");
		sql.append("  GTC.TERM_ID,");
		sql.append("  GTC.XX_ID,");
		sql.append("  GCE.COURSE_ID,");
		sql.append("  GCE.KCMC,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  GCI.CLASS_ID,");
		sql.append("  GCI.BJMC,");
		sql.append("  LTC.TERMCOURSE_CLASS_ID PCOURSE_CLASS_ID");
		sql.append("  FROM GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_COURSE          GCE,");
		sql.append("  GJT_GRADE           GGE,");
		sql.append("  GJT_CLASS_INFO      GCI");
		sql.append("  LEFT JOIN LCMS_TERMCOURSE_CLASS LTC ON GCI.CLASS_ID =");
		sql.append("  LTC.TERMCOURSE_CLASS_ID");
		sql.append("  AND LTC.ISDELETED = 'N'");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GTC.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = GGE.GRADE_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = GCI.TERMCOURSE_ID");
		sql.append("  AND GTC.TERM_ID = :TERM_ID");

		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询期课程下面的课程班级个数
	 * @return
	 */
	public int getClassCount(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT COUNT(GCI.CLASS_ID) CLASS_COUNT");
		sql.append("  FROM GJT_CLASS_INFO GCI");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.TERMCOURSE_ID = :TERMCOURSE_ID");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		
		int count = 0;
		List list = commonDao.queryForMapListNative(sql.toString(), param);
		if (EmptyUtils.isNotEmpty(list)) {
			Map tempMap = (Map)list.get(0);
			count = Integer.parseInt(ObjectUtils.toString(tempMap.get("CLASS_COUNT")));
		}
		return count;
	}
	
	/**
	 * 查询不存在自己报读的产品教学计划内的选课
	 * @return
	 */
	public List getNoPlanRecList(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GRRT.REC_ID,");
		sql.append("  GRRT.STUDENT_ID,");
		sql.append("  GRRT.TERMCOURSE_ID,");
		sql.append("  (SELECT GCI.CLASS_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.TERMCOURSE_ID = GRRT.TERMCOURSE_ID");
		sql.append("  AND ROWNUM = 1) CLASS_ID");
		sql.append("  FROM GJT_REC_RESULT GRRT");
		sql.append("  WHERE GRRT.IS_DELETED = 'N'");
		//sql.append("  AND GRRT.EXAM_STATE = '2'");
		sql.append("  AND GRRT.STUDENT_ID = :STUDENT_ID");
		sql.append("  AND GRRT.REC_ID NOT IN");
		sql.append("  (SELECT GRR.REC_ID");
		sql.append("  FROM GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_GRADE_SPECIALTY GRS,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_COURSE          GCE");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRS.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GSI.XX_ID = VTP.XX_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GRS.ID");
		sql.append("  AND GRS.ID = VTP.GRADE_SPECIALTY_ID");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND VTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID)");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 学习平台查询不到的选课记录数据
	 * @return
	 */
	public List getNoPcoruseRecList(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT LUC.CHOOSE_ID, LUC.TERMCOURSE_ID, LUC.CLASS_ID, LUC.STUD_ID");
		sql.append("  FROM LCMS_USER_CHOOSE LUC");
		sql.append("  WHERE LUC.ISDELETED = 'N'");
		sql.append("  AND LUC.STUD_ID = :STUD_ID");
		sql.append("  AND LUC.CHOOSE_ID NOT IN");
		sql.append("  (SELECT GRR.REC_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.STUDENT_ID = :STUDENT_ID)");

		param.put("STUD_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 删除不存在自己报读的产品教学计划内的选课
	 * @return
	 */
	@Transactional
	public int delNoPlanRecList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE GJT_REC_RESULT GRRT");
		sql.append("  SET GRRT.IS_DELETED = 'Y', GRRT.UPDATED_DT = SYSDATE,");
		sql.append("  GRRT.UPDATED_BY = :BATCHNO");
		sql.append("  WHERE GRRT.IS_DELETED = 'N'");
		// sql.append("  AND GRRT.EXAM_STATE = '2'");
		sql.append("  AND GRRT.STUDENT_ID = :STUDENT_ID");
		sql.append("  AND GRRT.REC_ID NOT IN (SELECT GRR.REC_ID");
		sql.append("  FROM GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_GRADE_SPECIALTY GRS,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_COURSE          GCE");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRS.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GSI.XX_ID = VTP.XX_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GRS.ID");
		sql.append("  AND GRS.ID = VTP.GRADE_SPECIALTY_ID");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND VTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID)");
		
		param.put("BATCHNO", ObjectUtils.toString(searchParams.get("BATCHNO")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 删除不存在自己报读的产品教学计划内的选课课程班关系数据
	 * @return
	 */
	@Transactional
	public int delNoPlanRecClass(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE GJT_CLASS_STUDENT GCST");
		sql.append("  SET GCST.IS_DELETED = 'Y', GCST.UPDATED_DT = SYSDATE");
		sql.append("  WHERE GCST.CLASS_STUDENT_ID IN");
		sql.append("  (SELECT GCS.CLASS_STUDENT_ID");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS, GJT_REC_RESULT GRR");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.TERMCOURSE_ID = GRR.TERMCOURSE_ID");
		sql.append("  AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.TERMCOURSE_ID = :TERMCOURSE_ID");
		sql.append("  AND GRR.STUDENT_ID = :STUDENT_ID)");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询报读该产品的学员
	 * @return
	 */
	public List getPlanStudentList(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.STUDENT_ID");
		sql.append("  FROM GJT_STUDENT_INFO GSI, GJT_GRADE_SPECIALTY GGS");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GGS.IS_DELETED = 'N'");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = :GRADE_SPECIALTY_ID");
		
		param.put("GRADE_SPECIALTY_ID", ObjectUtils.toString(searchParams.get("GRADE_SPECIALTY_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询期课程的选课人数
	 * @return
	 */
	public List getTermcourseStuRec(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT DISTINCT STUDENT_ID");
		sql.append("  FROM (SELECT GSI.STUDENT_ID,");
		sql.append("  (SELECT GRR.REC_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GRR.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND ROWNUM = 1) REC_ID,");
		sql.append("  (SELECT LUC.CHOOSE_ID");
		sql.append("  FROM LCMS_USER_CHOOSE LUC");
		sql.append("  WHERE LUC.ISDELETED = 'N'");
		sql.append("  AND LUC.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND LUC.STUD_ID = GSI.STUDENT_ID");
		sql.append("  AND ROWNUM = 1) CHOOSE_ID");
		sql.append("  FROM GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_GRADE_SPECIALTY GRS,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_COURSE          GCE,");
		sql.append("  GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRS.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GCE.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GRS.ID");
		sql.append("  AND GRS.ID = VTP.GRADE_SPECIALTY_ID");
		sql.append("  AND VTP.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND VTP.ACTUAL_GRADE_ID = GTC.TERM_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID) TAB");
		sql.append("  WHERE 1 = 1");
		sql.append("  AND (REC_ID IS NULL OR CHOOSE_ID IS NULL)");

		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 获取课程信息
	 * @return
	 */
	public List getCourseInfo(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GCE.COURSE_ID,");
		sql.append("  GCE.KCH,");
		sql.append("  GCE.KCMC,");
		sql.append("  GCE.KCJJ,");
		sql.append("  GCE.LABEL,");
		sql.append("  GCE.SYZY,");
		sql.append("  GCE.PYCC,");
		sql.append("  GCE.CREATED_BY,");
		sql.append("  TO_CHAR(GCE.CREATED_DT, 'yyyy-mm-dd hh24:mi:ss') CREATED_DT,");
		sql.append("  (SELECT TO_CHAR(WMSYS.WM_CONCAT(GSY.ZYMC))");
		sql.append("  FROM GJT_SPECIALTY GSY");
		sql.append("  WHERE GSY.IS_DELETED = 'N'");
		sql.append("  AND INSTR(GCE.SYZY,GSY.SPECIALTY_ID)>0) ZYMC,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GCE.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append("  (SELECT GSI.APPID");
		sql.append("  FROM GJT_SCHOOL_INFO GSI");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.ID = GCE.XX_ID) APPID");
		sql.append("  FROM GJT_COURSE GCE");
		sql.append("  WHERE GCE.IS_DELETED = 'N'");
		sql.append("  AND GCE.XX_ID = :XX_ID");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 获取课程信息
	 * @return
	 */
	public List getOldRecId(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GSI.XH,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  GRR.REC_ID,");
		sql.append("  GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  NVL((SELECT LUC.CHOOSE_ID");
		sql.append("  FROM LCMS_USER_CHOOSE     LUC,");
		sql.append("  LCMS_TERM_COURSEINFO LTC,");
		sql.append("  LCMS_USER_DYNA       LUD");
		sql.append("  WHERE LUC.TERMCOURSE_ID = LTC.TERMCOURSE_ID");
		sql.append("  AND LUC.CHOOSE_ID = LUD.CHOOSE_ID");
		sql.append("  AND LUC.STUD_ID = GSI.STUDENT_ID");
		sql.append("  AND LTC.COURSE_ID = GRR.COURSE_ID");
		sql.append("  AND (LUD.MY_PROGRESS > 0 OR LUD.MY_POINT > 0)");
		sql.append("  AND LUD.MY_PROGRESS =");
		sql.append("  (SELECT MAX(LUDA.MY_PROGRESS)");
		sql.append("  FROM LCMS_USER_CHOOSE     LUCE,");
		sql.append("  LCMS_USER_DYNA       LUDA,");
		sql.append("  LCMS_TERM_COURSEINFO LTCF");
		sql.append("  WHERE LUCE.TERMCOURSE_ID = LTCF.TERMCOURSE_ID");
		sql.append("  AND LUCE.CHOOSE_ID = LUDA.CHOOSE_ID");
		sql.append("  AND LUCE.STUD_ID = LUC.STUD_ID");
		sql.append("  AND LTCF.COURSE_ID = LTC.COURSE_ID)");
		sql.append("  AND ROWNUM = 1),");
		sql.append("  'NULL') OLD_CHOOSE_ID,");
		sql.append("  ");
		sql.append("  (SELECT GRRT.REC_ID");
		sql.append("  FROM GJT_REC_RESULT GRRT, VIEW_TEACH_PLAN GTPN");
		sql.append("  WHERE GRRT.IS_DELETED = 'Y'");
		sql.append("  AND GRRT.TEACH_PLAN_ID = GTPN.TEACH_PLAN_ID");
		sql.append("  AND GRRT.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND (GTPN.SOURCE_KCH = VTP.SOURCE_KCH OR");
		sql.append("  GRRT.COURSE_ID = GRR.COURSE_ID)");
		sql.append("  AND GRRT.UPDATED_DT =");
		sql.append("  (SELECT MAX(GRRT1.UPDATED_DT)");
		sql.append("  FROM GJT_REC_RESULT GRRT1, VIEW_TEACH_PLAN GTPN1");
		sql.append("  WHERE GRRT1.IS_DELETED = 'Y'");
		sql.append("  AND GRRT1.TEACH_PLAN_ID = GTPN1.TEACH_PLAN_ID");
		sql.append("  AND (GTPN.SOURCE_KCH = GTPN1.SOURCE_KCH OR");
		sql.append("  GTPN.KCH = GTPN1.KCH)");
		sql.append("  AND GRRT1.STUDENT_ID = GRRT.STUDENT_ID)) OLD_CHOOSE_ID1");
		sql.append("  ");
		sql.append("  FROM GJT_STUDENT_INFO GSI, GJT_REC_RESULT GRR, VIEW_TEACH_PLAN  VTP");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.TEACH_PLAN_ID = VTP.TEACH_PLAN_ID");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID) TAB");
		sql.append("  WHERE (TAB.OLD_CHOOSE_ID IS NOT NULL OR TAB.OLD_CHOOSE_ID1 IS NOT NULL)");
		sql.append("  AND TAB.OLD_CHOOSE_ID != TAB.REC_ID");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 *学员转专业后恢复相同课程的数据 
	 * @param string
	 * @param string2
	 * @return
	 */
	@Transactional
	public int updateGjtRecResult(String oldRecId, String newRecId, String batchNo) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET (GRR.EXAM_SCORE,");
		sql.append("  GRR.EXAM_SCORE1,");
		sql.append("  GRR.EXAM_SCORE2,");
		sql.append("  GRR.EXAM_STATE,");
		sql.append("  GRR.BESPEAK_STATE,");
		sql.append("  GRR.PAY_STATE,");
		sql.append("  GRR.REBUILD_STATE,");
		sql.append("  GRR.GET_CREDITS) =");
		sql.append("  (SELECT OLDGRR.EXAM_SCORE,");
		sql.append("  OLDGRR.EXAM_SCORE1,");
		sql.append("  OLDGRR.EXAM_SCORE2,");
		sql.append("  OLDGRR.EXAM_STATE,");
		sql.append("  OLDGRR.BESPEAK_STATE,");
		sql.append("  OLDGRR.PAY_STATE,");
		sql.append("  OLDGRR.REBUILD_STATE,");
		sql.append("  OLDGRR.GET_CREDITS");
		sql.append("  FROM GJT_REC_RESULT OLDGRR");
		sql.append("  WHERE OLDGRR.IS_DELETED = 'Y'");
		sql.append("  AND OLDGRR.UPDATED_BY = GRR.UPDATED_BY");
		sql.append("  AND OLDGRR.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND OLDGRR.COURSE_ID = GRR.COURSE_ID)");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.UPDATED_BY = :BATCHNO");
		sql.append("  AND GRR.REC_ID = :REC_ID");
		sql.append("  AND (SELECT COUNT(OLDGRR.REC_ID)");
		sql.append("  FROM GJT_REC_RESULT OLDGRR");
		sql.append("  WHERE OLDGRR.IS_DELETED = 'Y'");
		sql.append("  AND OLDGRR.UPDATED_BY = GRR.UPDATED_BY");
		sql.append("  AND OLDGRR.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND OLDGRR.COURSE_ID = GRR.COURSE_ID) > 0");
		
		param.put("BATCHNO",batchNo);
		param.put("REC_ID",newRecId);
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	/**
	 * 更改学员预约表中的REC_ID
	 * @param string
	 * @param string2
	 * @return
	 */
	@Transactional
	public int updateGjtExamAppointment(String oldRecId, String newRecId) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEAN");
		sql.append("  SET GEAN.REC_ID =:newRecId");
		sql.append("  WHERE GEAN.APPOINTMENT_ID IN");
		sql.append("  (SELECT APPOINTMENT_ID");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GE");
		sql.append("  WHERE GE.REC_ID =:oldRecId");
		sql.append("  AND GE.IS_DELETED = 0)");
		param.put("oldRecId",oldRecId);
		param.put("newRecId",newRecId);
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 
	 * @param string
	 * @param string2
	 */
	public List getGjtLearnRepair(String oldTeachPlanId, String studentId) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GLR.REPAIR_ID, GLR.TEACH_PLAN_ID, GLR.STUDENT_ID");
		sql.append("  FROM GJT_LEARN_REPAIR GLR");
		sql.append("  WHERE GLR.TEACH_PLAN_ID =:OLD_TEACH_PLAN_ID");
		sql.append("  AND GLR.STUDENT_ID =:STUDENT_ID");
		sql.append("  AND GLR.IS_DELETED = 'N'");
		param.put("OLD_TEACH_PLAN_ID",oldTeachPlanId);
		param.put("STUDENT_ID",studentId);
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	/**
	 * 更新历史成绩表中的TEACH_PLAN_ID
	 * @param learnRepairMap
	 * @return
	 */
	@Transactional
	public int updateLearnRepair(Map learnRepairMap,String newTeachPlanId) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  UPDATE GJT_LEARN_REPAIR GLR");
		sql.append("  SET GLR.TEACH_PLAN_ID =:TEACH_PLAN_ID,");
		sql.append("  GLR.UPDATED_DT = SYSDATE,");
		sql.append("  GLR.UPDATED_BY = '学员转专业更新教学计划ID'");
		sql.append("  WHERE GLR.REPAIR_ID =:REPAIR_ID");
		param.put("TEACH_PLAN_ID", ObjectUtils.toString(newTeachPlanId));
		param.put("REPAIR_ID",ObjectUtils.toString(learnRepairMap.get("REPAIR_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
}
