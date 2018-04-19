package com.gzedu.xlims.dao.edumanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;

@Repository
public class GjtChooseManageDao extends BaseDaoImpl {

	@Autowired
	private CommonDao commonDao;
	
	/**
	 * 选课管理列表
	 * @return
	 */
	public Page getChooseManageList(Map searchParams, PageRequest pageRequst) {
		Map param = getChooseManageListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return (Page) commonDao.queryForPageNative(sql, param, pageRequst);
	}
	
	/**
	 * 选课管理列表
	 * @return
	 */
	public List getChooseManageList(Map searchParams) {
		Map param = getChooseManageListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return commonDao.queryForMapListNative(sql, param);
	}
	
	/**
	 * 选课管理列表（统计）
	 * @return
	 */
	public List getChooseManageCount(Map searchParams) {
		Map param = getChooseManageListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		sql = "SELECT COUNT(*) CHOOSE_COUNT FROM ("+sql+") ";
		return commonDao.queryForMapListNative(sql, param);
	}
	
	/**
	 * 选课管理列表（sql）
	 * @return
	 */
	public Map getChooseManageListSQL(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.ZP,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.PYCC,");
		
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		
		sql.append("  VTP.KCH,");
		sql.append("  VTP.KCMC,");
		sql.append("  GGE.GRADE_ID,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GGA.GRADE_ID OPEN_GRADE_ID,");
		sql.append("  GGA.GRADE_NAME OPEN_GRADE_NAME,");
		sql.append("  GRR.REC_ID,");
		sql.append("  GRR.REBUILD_STATE,");
		sql.append("  GRR.SYNC_STATUS,");
		sql.append("  GRR.XXW_SYNC_STATUS,");
		sql.append("  GRR.XXW_SYNC_MSG,");
		sql.append("  (SELECT GCI.BJMC");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.TERMCOURSE_ID = GRR.TERMCOURSE_ID");
		sql.append("  AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND ROWNUM = 1) BJMC,");
		sql.append("  (SELECT GEI.XM");
		sql.append("  FROM GJT_CLASS_INFO    GCI,");
		sql.append("  GJT_CLASS_STUDENT GCS,");
		sql.append("  GJT_EMPLOYEE_INFO GEI");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GEI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.TERMCOURSE_ID = GRR.TERMCOURSE_ID");
		sql.append("  AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GCI.BZR_ID = GEI.EMPLOYEE_ID");
		sql.append("  AND ROWNUM = 1) TEACH_NAME");
		sql.append("  FROM GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_GRADE           GGE,");
		sql.append("  GJT_SPECIALTY       GSY,");
		sql.append("  GJT_GRADE_SPECIALTY GGS,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_GRADE           GGA,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  GJT_COURSE GCE");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GGS.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GGA.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GRR.TERMCOURSE_ID IS NOT NULL");
		sql.append("  AND GCE.IS_ENABLED = '1'");
		sql.append("  AND VTP.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GSI.NJ = GGE.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GGS.ID = VTP.GRADE_SPECIALTY_ID");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND VTP.ACTUAL_GRADE_ID = GGA.GRADE_ID");
		
		sql.append("  AND GSI.XX_ID = :XX_ID");
		param.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CURRENT_GRADE_ID")))) {
			/*sql.append("  AND TO_CHAR(GGA.END_DATE, 'yyyy-mm-dd') <= (");
			sql.append("  SELECT TO_CHAR(GRD.END_DATE, 'yyyy-mm-dd') FROM GJT_GRADE GRD WHERE GRD.IS_DELETED = 'N' AND GRD.GRADE_ID = :CURRENT_GRADE_ID");
			sql.append("  )");
			
			param.put("CURRENT_GRADE_ID",ObjectUtils.toString(searchParams.get("CURRENT_GRADE_ID")));*/
			
			sql.append("  AND GGA.GRADE_ID = :CURRENT_GRADE_ID ");
			param.put("CURRENT_GRADE_ID",ObjectUtils.toString(searchParams.get("CURRENT_GRADE_ID")));
			
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH LIKE :XH");
			param.put("XH", "%"+ObjectUtils.toString(searchParams.get("XH"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SFZH")))) {
			sql.append("  AND GSI.SFZH LIKE :SFZH");
			param.put("SFZH", "%"+ObjectUtils.toString(searchParams.get("SFZH"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			param.put("PYCC",ObjectUtils.toString(searchParams.get("PYCC")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GGE.GRADE_ID = :GRADE_ID");
			param.put("GRADE_ID",ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OPEN_GRADE_ID")))) {
			sql.append("  AND GGA.GRADE_ID = :OPEN_GRADE_ID");
			param.put("OPEN_GRADE_ID",ObjectUtils.toString(searchParams.get("OPEN_GRADE_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ZYMC")))) {
			sql.append("  AND GSY.ZYMC LIKE :ZYMC");
			param.put("ZYMC",ObjectUtils.toString(searchParams.get("ZYMC")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GGA.GRADE_ID = :GRADE_ID");
			param.put("GRADE_ID",ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND VTP.KCMC LIKE :KCMC");
			param.put("KCMC",ObjectUtils.toString(searchParams.get("KCMC")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("REBUILD_STATE")))) {
			sql.append("  AND GRR.REBUILD_STATE = :REBUILD_STATE");
			param.put("REBUILD_STATE",ObjectUtils.toString(searchParams.get("REBUILD_STATE")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXW_SYNC_STATUS")))) {
			sql.append("  AND GRR.XXW_SYNC_STATUS = :XXW_SYNC_STATUS");
			param.put("XXW_SYNC_STATUS",ObjectUtils.toString(searchParams.get("XXW_SYNC_STATUS")));
		}
		
		sql.append("  ) WHERE 1 = 1");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BJMC")))) {
			sql.append(" AND BJMC LIKE :BJMC");
			param.put("BJMC",ObjectUtils.toString(searchParams.get("BJMC")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TEACH_NAME")))) {
			sql.append(" AND TEACH_NAME LIKE :TEACH_NAME");
			param.put("TEACH_NAME",ObjectUtils.toString(searchParams.get("TEACH_NAME")));
		}

		param.put("sql", sql);
		return param;
	}
	
	/**
	 * 删除选课数据
	 * @return
	 */
	@Transactional
	public int delRecResult(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET GRR.IS_DELETED = 'Y', GRR.UPDATED_DT = SYSDATE");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.REC_ID = :REC_ID");

		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询期课程下面未选课学员
	 * @return
	 */
	public List getNoChooseInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.ZP,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GGE.GRADE_ID,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GGA.GRADE_ID OPEN_GRADE_ID,");
		sql.append("  GGA.GRADE_NAME OPEN_GRADE_NAME,");
		sql.append("  VTP.COURSE_ID,");
		sql.append("  VTP.KCH,");
		sql.append("  VTP.KCMC,");
		sql.append("  VTP.TEACH_PLAN_ID,");
		sql.append("  GTC.TERMCOURSE_ID,");
		sql.append("  GTC.TERM_ID,");
		sql.append("  (SELECT GRR.REC_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GRR.TEACH_PLAN_ID = VTP.TEACH_PLAN_ID");
		sql.append("  AND ROWNUM = 1) REC_ID");
		sql.append("  FROM GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_GRADE           GGE,");
		sql.append("  GJT_SPECIALTY       GSY,");
		sql.append("  GJT_GRADE_SPECIALTY GGS,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_GRADE           GGA,");
		sql.append("  GJT_TERM_COURSEINFO GTC");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GGS.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GGA.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GSI.NJ = GGE.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GSI.GRADE_SPECIALTY_ID = GGS.ID");
		sql.append("  AND GGS.ID = VTP.GRADE_SPECIALTY_ID");
		sql.append("  AND VTP.ACTUAL_GRADE_ID = GGA.GRADE_ID");
		sql.append("  AND GTC.TERM_ID = VTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND GSI.XX_ID = :XX_ID");
		sql.append("  AND VTP.ACTUAL_GRADE_ID = :ACTUAL_GRADE_ID)");
		sql.append("  WHERE 1 = 1");
		sql.append("  AND REC_ID IS NULL");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("ACTUAL_GRADE_ID", ObjectUtils.toString(searchParams.get("ACTUAL_GRADE_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 新增选课数据
	 * @return
	 */
	public int addRecResult(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_REC_RESULT");
		sql.append("  (REC_ID,");
		sql.append("  STUDENT_ID,");
		sql.append("  TERM_ID,");
		sql.append("  COURSE_ID,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  XX_ID,");
		sql.append("  TERMCOURSE_ID)");
		sql.append("  VALUES (");
		sql.append("  :REC_ID,");
		sql.append("  :STUDENT_ID,");
		sql.append("  :TERM_ID,");
		sql.append("  :COURSE_ID,");
		sql.append("  :TEACH_PLAN_ID,");
		sql.append("  :XX_ID,");
		sql.append("  :TERMCOURSE_ID");
		sql.append("  )");

		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		param.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		param.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		param.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		param.put("TERMCOURSE_ID", ObjectUtils.toString(searchParams.get("TERMCOURSE_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询同步选课集合
	 * @return
	 */
	public List getSyncRecList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GGE.GRADE_ID,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  (SELECT COUNT(DISTINCT GTC.TERMCOURSE_ID)");
		sql.append("  FROM VIEW_TEACH_PLAN GTP, GJT_TERM_COURSEINFO GTC, GJT_COURSE GC");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_ENABLED = '1'");
		sql.append("  AND GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND GTP.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GTC.TERM_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GGE.GRADE_ID) OPEN_COURSE_COUNT,");
		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  FROM VIEW_TEACH_PLAN     GTP,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  GJT_COURSE          GC");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_ENABLED = '1'");
		sql.append("  AND GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND GRR.TERMCOURSE_ID IS NOT NULL");
		sql.append("  AND GTP.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GTC.TERM_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = GRR.TERMCOURSE_ID) REC_COUNT,");
		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  FROM VIEW_TEACH_PLAN     GTP,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  GJT_COURSE          GC");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_ENABLED = '1'");
		sql.append("  AND GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND GRR.TERMCOURSE_ID IS NOT NULL");
		sql.append("  AND GTP.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GTC.TERM_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = GRR.TERMCOURSE_ID");
		sql.append("  AND GRR.XXW_SYNC_STATUS = '1') YES_SYNC_COUNT,");
		sql.append("  (SELECT COUNT(GRR.REC_ID)");
		sql.append("  FROM VIEW_TEACH_PLAN     GTP,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  GJT_COURSE          GC");
		sql.append("  WHERE GTP.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_ENABLED = '1'");
		sql.append("  AND GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND GRR.TERMCOURSE_ID IS NOT NULL");
		sql.append("  AND GTP.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GTC.TERM_ID");
		sql.append("  AND GTP.ACTUAL_GRADE_ID = GGE.GRADE_ID");
		sql.append("  AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = GRR.TERMCOURSE_ID");
		sql.append("  AND GRR.XXW_SYNC_STATUS = '0') NO_SYNC_COUNT");
		sql.append("  FROM GJT_GRADE GGE");

		sql.append("  WHERE GGE.IS_DELETED = 'N'");
		sql.append("  AND GGE.GRADE_ID = :CURRENT_GRADE_ID");
		sql.append("  AND GGE.XX_ID = :XX_ID");

		param.put("CURRENT_GRADE_ID", ObjectUtils.toString(searchParams.get("CURRENT_GRADE_ID")));
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 更新选课表中的同步状态
	 * @return
	 */
	@Transactional
	public int updRecSyncStatus(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET GRR.XXW_SYNC_STATUS = :XXW_SYNC_STATUS, GRR.XXW_SYNC_MSG = :XXW_SYNC_MSG");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.REC_ID = :REC_ID");

		param.put("XXW_SYNC_STATUS", ObjectUtils.toString(searchParams.get("XXW_SYNC_STATUS")));
		param.put("XXW_SYNC_MSG", ObjectUtils.toString(searchParams.get("XXW_SYNC_MSG")));
		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 更新班级的任教老师
	 * @return
	 */
	@Transactional
	public int updaClassTeacher(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_CLASS_INFO GCI");
		sql.append("  SET GCI.BZR_ID = (SELECT GCT.TEACHER_ID");
		sql.append("  FROM GJT_COURSE_TEACHER GCT");
		sql.append("  WHERE GCT.IS_DELETED = 'N'");
		sql.append("  AND GCT.COURSE_ID = GCI.COURSE_ID");
		sql.append("  AND ROWNUM = 1)");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.BZR_ID IS NULL");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.XX_ID = :XX_ID");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询未同步的班级
	 * @return
	 */
	public List getNoSyncClassList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GCI.CLASS_ID,");
		sql.append("  GCI.BH,");
		sql.append("  GCI.BJMC,");
		sql.append("  VTP.KCH,");
		sql.append("  GCI.BZR_ID,");
		sql.append("  (SELECT GUA.LOGIN_ACCOUNT");
		sql.append("  FROM GJT_EMPLOYEE_INFO GEI, GJT_USER_ACCOUNT GUA");
		sql.append("  WHERE GEI.IS_DELETED = 'N'");
		sql.append("  AND GUA.IS_DELETED = 'N'");
		sql.append("  AND GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  AND GEI.EMPLOYEE_ID = GCI.BZR_ID) LOGIN_ACCOUNT");
		sql.append("  FROM GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_COURSE          GC");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_ENABLED = '1'");
		sql.append("  AND GC.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND GCI.XXW_SYNC_STATUS IN ('0', '2')");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND GTC.TERM_ID = VTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = VTP.COURSE_ID");

		sql.append("  AND VTP.XX_ID = :XX_ID");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询未同步的辅导老师班级关系
	 * @return
	 */
	public List getNoSyncTchClassList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GCI.CLASS_ID,");
		sql.append("  GCI.BH,");
		sql.append("  GCI.BJMC,");
		sql.append("  VTP.KCH,");
		sql.append("  GCI.BZR_ID,");
		sql.append("  (SELECT GUA.LOGIN_ACCOUNT");
		sql.append("  FROM GJT_EMPLOYEE_INFO GEI, GJT_USER_ACCOUNT GUA");
		sql.append("  WHERE GEI.IS_DELETED = 'N'");
		sql.append("  AND GUA.IS_DELETED = 'N'");
		sql.append("  AND GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  AND GEI.EMPLOYEE_ID = GCI.BZR_ID) LOGIN_ACCOUNT");
		sql.append("  FROM GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_COURSE          GC");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_ENABLED = '1'");
		sql.append("  AND GC.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND GCI.XXW_SYNC_STATUS = '1'");
		sql.append("  AND GCI.XXW_TCH_SYNC_STATUS IN ('0', '2')");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND GTC.TERM_ID = VTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = VTP.COURSE_ID");

		sql.append("  AND VTP.XX_ID = :XX_ID");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询未同步的学员班级关系
	 * @return
	 */
	public List getNoSyncStudClassList(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GCI.CLASS_ID,");
		sql.append("  GCI.BH,");
		sql.append("  GCI.BJMC,");
		sql.append("  VTP.KCH,");
		sql.append("  GSI.XH,");
		sql.append("  GCS.CLASS_STUDENT_ID");
		sql.append("  FROM GJT_CLASS_INFO      GCI,");
		sql.append("  GJT_TERM_COURSEINFO GTC,");
		sql.append("  VIEW_TEACH_PLAN     VTP,");
		sql.append("  GJT_STUDENT_INFO    GSI,");
		sql.append("  GJT_CLASS_STUDENT   GCS,");
		sql.append("  GJT_REC_RESULT      GRR,");
		sql.append("  GJT_COURSE GC");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND VTP.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_ENABLED = '1'");
		sql.append("  AND GC.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND GCI.XXW_SYNC_STATUS = '1'");
		sql.append("  AND GCS.XXW_SYNC_STATUS IN ('0', '2')");
		sql.append("  AND GCI.CLASS_TYPE = 'course'");
		sql.append("  AND GCI.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  AND GTC.TERM_ID = VTP.ACTUAL_GRADE_ID");
		sql.append("  AND GTC.COURSE_ID = VTP.COURSE_ID");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND VTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");

		sql.append("  AND VTP.XX_ID = :XX_ID");

		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 修改课程班同步学习网状态
	 * @return
	 */
	@Transactional
	public int updClassXxwSyncStatus(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE GJT_CLASS_INFO GCI");
		sql.append("  SET GCI.XXW_SYNC_STATUS = :XXW_SYNC_STATUS, GCI.XXW_SYNC_MSG = :XXW_SYNC_MSG");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_ID = :CLASS_ID");

		param.put("XXW_SYNC_STATUS", ObjectUtils.toString(searchParams.get("XXW_SYNC_STATUS")));
		param.put("XXW_SYNC_MSG", ObjectUtils.toString(searchParams.get("XXW_SYNC_MSG")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 修改辅导老师课程班同步学习网状态
	 * @return
	 */
	@Transactional
	public int updClassXxwTchSyncStatus(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE GJT_CLASS_INFO GCI");
		sql.append("  SET GCI.XXW_TCH_SYNC_STATUS = :XXW_TCH_SYNC_STATUS, GCI.XXW_TCH_SYNC_MSG = :XXW_TCH_SYNC_MSG");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_ID = :CLASS_ID");

		param.put("XXW_TCH_SYNC_STATUS", ObjectUtils.toString(searchParams.get("XXW_TCH_SYNC_STATUS")));
		param.put("XXW_TCH_SYNC_MSG", ObjectUtils.toString(searchParams.get("XXW_TCH_SYNC_MSG")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 更新班级的学员关系
	 * @return
	 */
	@Transactional
	public int updStudClassXxwSyncStatus(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_CLASS_STUDENT GCS");
		sql.append("  SET GCS.XXW_SYNC_STATUS = :XXW_SYNC_STATUS, GCS.XXW_SYNC_MSG = :XXW_SYNC_MSG");
		sql.append("  WHERE GCS.IS_DELETED = 'N'");
		sql.append("  AND GCS.CLASS_STUDENT_ID = :CLASS_STUDENT_ID");

		param.put("XXW_SYNC_STATUS", ObjectUtils.toString(searchParams.get("CLASS_STUXXW_SYNC_STATUSDENT_ID")));
		param.put("XXW_SYNC_MSG", ObjectUtils.toString(searchParams.get("XXW_SYNC_MSG")));
		param.put("CLASS_STUDENT_ID", ObjectUtils.toString(searchParams.get("CLASS_STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
}
