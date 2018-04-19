package com.gzedu.xlims.dao.api;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ApiDao {

	@Autowired
	private CommonDao commonDao;

	/**
	 * 缴费记录
	 * @return
	 */
	public int addFeesRecord(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_FEES_INFO");
		sql.append("  (FEES_ID,ATID,");
		sql.append("  REGISTRATION_TIME,");
		sql.append("  FULL_TUITION,");
		sql.append("  DISCOUNT_TUITION,");
		sql.append("  PAYABLE_TUITION,");
		sql.append("  PAID_TUITION,");
		sql.append("  SUM_TERM,");
		sql.append("  PAID_TERM,");
		sql.append("  PAY_FEES_TYPE,");
		sql.append("  PAY_FEES_STATE,");
		sql.append("  DISCOUNT_POLICY)");
		sql.append("  VALUES");
		sql.append("  (:FEES_ID, ");
		sql.append("  :ATID, ");
		sql.append("  to_date(:REGISTRATION_TIME,'yyyy-mm-dd'),");
		sql.append("  :FULL_TUITION,");
		sql.append("  :DISCOUNT_TUITION,");
		sql.append("  :PAYABLE_TUITION,");
		sql.append("  :PAID_TUITION,");
		sql.append("  :SUM_TERM,");
		sql.append("  :PAID_TERM,");
		sql.append("  :PAY_FEES_TYPE,");
		sql.append("  :PAY_FEES_STATE,");
		sql.append("  :DISCOUNT_POLICY");
		sql.append("  )");

		param.put("FEES_ID", ObjectUtils.toString(searchParams.get("FEES_ID")));
		param.put("ATID", ObjectUtils.toString(searchParams.get("ATID")));
		param.put("REGISTRATION_TIME", ObjectUtils.toString(searchParams.get("REGISTRATION_TIME")));
		param.put("FULL_TUITION", ObjectUtils.toString(searchParams.get("FULL_TUITION")));
		param.put("DISCOUNT_TUITION", ObjectUtils.toString(searchParams.get("DISCOUNT_TUITION")));
		param.put("PAYABLE_TUITION", ObjectUtils.toString(searchParams.get("PAYABLE_TUITION")));
		param.put("PAID_TUITION", ObjectUtils.toString(searchParams.get("PAID_TUITION")));
		param.put("SUM_TERM", ObjectUtils.toString(searchParams.get("SUM_TERM")));
		param.put("PAID_TERM", ObjectUtils.toString(searchParams.get("PAID_TERM")));
		param.put("PAY_FEES_TYPE", ObjectUtils.toString(searchParams.get("PAY_FEES_TYPE")));
		param.put("PAY_FEES_STATE", ObjectUtils.toString(searchParams.get("PAY_FEES_STATE")));
		param.put("DISCOUNT_POLICY", ObjectUtils.toString(searchParams.get("DISCOUNT_POLICY")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 删除缴费记录
	 * @return
	 */
	public int delFeesRecord(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE GJT_FEES_INFO GFI SET GFI.IS_DELETED = 'Y' WHERE GFI.ATID = :ATID");
		param.put("ATID", ObjectUtils.toString(searchParams.get("ATID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 缴费记录
	 * @return
	 */
	public int addFeesDetail(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_FEES_DETAIL");
		sql.append("  (");
		sql.append("  FEES_DETAIL_ID,");
		sql.append("  ATID,");
		sql.append("  PAY_FEES_MONTHLY,");
		sql.append("  PAYABLE_TUITION,");
		sql.append("  PAID_TUITION,");
		sql.append("  PAY_FEES_STATE,");
		sql.append("  PAY_FEES_TYPE,");
		sql.append("  PAY_FEES_TIME");
		sql.append("  )");
		sql.append("  VALUES");
		sql.append("  (");
		sql.append("  :FEES_DETAIL_ID,");
		sql.append("  :ATID,");
		sql.append("  :PAY_FEES_MONTHLY,");
		sql.append("  :PAYABLE_TUITION,");
		sql.append("  :PAID_TUITION,");
		sql.append("  :PAY_FEES_STATE,");
		sql.append("  :PAY_FEES_TYPE,");
		sql.append("  to_date(:PAY_FEES_TIME, 'yyyy-mm-dd'))");

		param.put("FEES_DETAIL_ID", ObjectUtils.toString(searchParams.get("FEES_DETAIL_ID")));
		param.put("ATID", ObjectUtils.toString(searchParams.get("ATID")));
		param.put("PAY_FEES_MONTHLY", ObjectUtils.toString(searchParams.get("PAY_FEES_MONTHLY")));
		param.put("PAYABLE_TUITION", ObjectUtils.toString(searchParams.get("PAYABLE_TUITION")));
		param.put("PAID_TUITION", ObjectUtils.toString(searchParams.get("PAID_TUITION")));
		param.put("PAY_FEES_STATE", ObjectUtils.toString(searchParams.get("PAY_FEES_STATE")));
		param.put("PAY_FEES_TYPE", ObjectUtils.toString(searchParams.get("PAY_FEES_TYPE")));
		param.put("PAY_FEES_TIME", ObjectUtils.toString(searchParams.get("PAY_FEES_TIME")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 删除缴费记录
	 * @return
	 */
	public int delFeesDetail(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE GJT_FEES_DETAIL GFD SET GFD.IS_DELETED = 'Y' WHERE GFD.ATID = :ATID");
		param.put("ATID", ObjectUtils.toString(searchParams.get("ATID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 更新学习记录
	 * @return
	 */
	@Transactional
	public int updStudyRecord(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_STUDY_RECORD GSR");
		sql.append("  SET GSR.UPDATED_DT  = SYSDATE,");
		/*if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GET_POINT")))) {
			sql.append("  GSR.GET_POINT   = :GET_POINT,");
			param.put("GET_POINT", ObjectUtils.toString(searchParams.get("GET_POINT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_SCORE")))) {
			sql.append("  GSR.STUDY_SCORE = :STUDY_SCORE,");
			param.put("STUDY_SCORE", ObjectUtils.toString(searchParams.get("STUDY_SCORE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_SCORE")))) {
			sql.append("  GSR.EXAM_SCORE  = :EXAM_SCORE,");
			param.put("EXAM_SCORE", ObjectUtils.toString(searchParams.get("EXAM_SCORE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SUM_SCORE")))) {
			sql.append("  GSR.SUM_SCORE   = :SUM_SCORE,");
			param.put("SUM_SCORE", ObjectUtils.toString(searchParams.get("SUM_SCORE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SCORE_STATE")))) {
			sql.append("  GSR.SCORE_STATE = :SCORE_STATE,");
			param.put("SCORE_STATE", ObjectUtils.toString(searchParams.get("SCORE_STATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATE")))) {
			sql.append("  GSR.STUDY_STATE = :STUDY_STATE,");
			param.put("STUDY_STATE", ObjectUtils.toString(searchParams.get("STUDY_STATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
			sql.append("  GSR.EXAM_STATE  = :EXAM_STATE,");
			param.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}*/
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("FIRST_DATE")))) {
			sql.append("  GSR.FIRST_DATE  = TO_DATE(:FIRST_DATE, 'yyyy-mm-dd hh24:mi:ss'),");
			param.put("FIRST_DATE", ObjectUtils.toString(searchParams.get("FIRST_DATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LAST_DATE")))) {
			sql.append("  GSR.LAST_DATE   = TO_DATE(:LAST_DATE, 'yyyy-mm-dd hh24:mi:ss'),");
			param.put("LAST_DATE", ObjectUtils.toString(searchParams.get("LAST_DATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LOGIN_COUNT")))) {
			sql.append("  GSR.LOGIN_COUNT = :LOGIN_COUNT,");
			param.put("LOGIN_COUNT", ObjectUtils.toString(searchParams.get("LOGIN_COUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PC_ONLINE_COUNT")))) {
			sql.append("  GSR.PC_ONLINE_COUNT = :PC_ONLINE_COUNT,");
			param.put("PC_ONLINE_COUNT", ObjectUtils.toString(searchParams.get("PC_ONLINE_COUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("APP_ONLINE_COUNT")))) {
			sql.append("  GSR.APP_ONLINE_COUNT = :APP_ONLINE_COUNT,");
			param.put("APP_ONLINE_COUNT", ObjectUtils.toString(searchParams.get("APP_ONLINE_COUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LOGIN_TIME")))) {
			sql.append("  GSR.LOGIN_TIME  = :LOGIN_TIME,");
			param.put("LOGIN_TIME", ObjectUtils.toString(searchParams.get("LOGIN_TIME")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PC_ONLINE_TIME")))) {
			sql.append("  GSR.PC_ONLINE_TIME  = :PC_ONLINE_TIME,");
			param.put("PC_ONLINE_TIME", ObjectUtils.toString(searchParams.get("PC_ONLINE_TIME")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("APP_ONLINE_TIME")))) {
			sql.append("  GSR.APP_ONLINE_TIME  = :APP_ONLINE_TIME,");
			param.put("APP_ONLINE_TIME", ObjectUtils.toString(searchParams.get("APP_ONLINE_TIME")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("IS_ONLINE")))) {
			sql.append("  GSR.IS_ONLINE   = :IS_ONLINE,");
			param.put("IS_ONLINE", ObjectUtils.toString(searchParams.get("IS_ONLINE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SCHEDULE")))) {
			sql.append("  GSR.SCHEDULE    = :SCHEDULE,");
			param.put("SCHEDULE", ObjectUtils.toString(searchParams.get("SCHEDULE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ACT_COUNT")))) {
			sql.append("  GSR.ACT_COUNT    = :ACT_COUNT,");
			param.put("ACT_COUNT", ObjectUtils.toString(searchParams.get("ACT_COUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MUST_ACTCOUNT")))) {
			sql.append("  GSR.MUST_ACTCOUNT    = :MUST_ACTCOUNT,");
			param.put("MUST_ACTCOUNT", ObjectUtils.toString(searchParams.get("MUST_ACTCOUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MY_ACTCOUNT")))) {
			sql.append("  GSR.MY_ACTCOUNT    = :MY_ACTCOUNT,");
			param.put("MY_ACTCOUNT", ObjectUtils.toString(searchParams.get("MY_ACTCOUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MY_MUSTACTCOUNT")))) {
			sql.append("  GSR.MY_MUSTACTCOUNT    = :MY_MUSTACTCOUNT,");
			param.put("MY_MUSTACTCOUNT", ObjectUtils.toString(searchParams.get("MY_MUSTACTCOUNT")));
		}
		sql.append("  GSR.UPDATED_BY  = '定时更新'");
		sql.append("  WHERE GSR.IS_DELETED = 'N'");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CHOOSE_ID")))) {
			sql.append("  AND GSR.CHOOSE_ID = :CHOOSE_ID");
			param.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));
		} else {
			sql.append("  AND GSR.EXAM_CHOOSE_ID = :EXAM_CHOOSE_ID");
			param.put("EXAM_CHOOSE_ID", ObjectUtils.toString(searchParams.get("EXAM_CHOOSE_ID")));
		}

		sql.append("  AND GSR.STUDENT_ID = :STUDENT_ID");
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 更新学习记录
	 * @return
	 */
	@Deprecated
	@Transactional
	public int updStudySituation(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_STUDENT_STUDY_SITUATION GSS");
		sql.append("  SET GSS.UPDATED_DT       = SYSDATE,");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_SCORE")))) {
			sql.append("  GSS.SCORE            = :STUDY_SCORE,");
			param.put("STUDY_SCORE", ObjectUtils.toString(searchParams.get("STUDY_SCORE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LOGIN_COUNT")))) {
			sql.append("  GSS.LOGIN_TIMES      = :LOGIN_COUNT,");
			param.put("LOGIN_COUNT", ObjectUtils.toString(searchParams.get("LOGIN_COUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SCHEDULE")))) {
			sql.append("  GSS.PROGRESS         = :SCHEDULE,");
			param.put("SCHEDULE", ObjectUtils.toString(searchParams.get("SCHEDULE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_SCORE")))) {
			sql.append("  GSS.EXAM_SCORE       = :EXAM_SCORE,");
			param.put("EXAM_SCORE", ObjectUtils.toString(searchParams.get("EXAM_SCORE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LAST_DATE")))) {
			sql.append("  GSS.LAST_LOGIN_DATE  = TO_DATE(:LAST_DATE, 'yyyy-mm-dd hh24:mi:ss'),");
			param.put("LAST_DATE", ObjectUtils.toString(searchParams.get("LAST_DATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LOGIN_TIME")))) {
			sql.append("  GSS.ONLINE_TIME      = :LOGIN_TIME,");
			param.put("LOGIN_TIME", ObjectUtils.toString(searchParams.get("LOGIN_TIME")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("FIRST_DATE")))) {
			sql.append("  GSS.FIRST_DATE       = TO_DATE(:FIRST_DATE, 'yyyy-mm-dd hh24:mi:ss'),");
			param.put("FIRST_DATE", ObjectUtils.toString(searchParams.get("FIRST_DATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ACT_COUNT")))) {
			sql.append("  GSS.ACT_COUNT        = :ACT_COUNT,");
			param.put("ACT_COUNT", ObjectUtils.toString(searchParams.get("ACT_COUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MY_ACTCOUNT")))) {
			sql.append("  GSS.MY_ACTCOUNT      = :MY_ACTCOUNT,");
			param.put("MY_ACTCOUNT", ObjectUtils.toString(searchParams.get("MY_ACTCOUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MY_MUSTACTCOUNT")))) {
			sql.append("  GSS.MY_MUSTACTCOUNT  = :MY_MUSTACTCOUNT,");
			param.put("MY_MUSTACTCOUNT", ObjectUtils.toString(searchParams.get("MY_MUSTACTCOUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("APP_ONLINE_COUNT")))) {
			sql.append("  GSS.APP_ONLINE_COUNT = :APP_ONLINE_COUNT,");
			param.put("APP_ONLINE_COUNT", ObjectUtils.toString(searchParams.get("APP_ONLINE_COUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PC_ONLINE_COUNT")))) {
			sql.append("  GSS.PC_ONLINE_COUNT  = :PC_ONLINE_COUNT,");
			param.put("PC_ONLINE_COUNT", ObjectUtils.toString(searchParams.get("PC_ONLINE_COUNT")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PC_ONLINE_TIME")))) {
			sql.append("  GSS.PC_ONLINE_TIME   = :PC_ONLINE_TIME,");
			param.put("PC_ONLINE_TIME", ObjectUtils.toString(searchParams.get("PC_ONLINE_TIME")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("APP_ONLINE_TIME")))) {
			sql.append("  GSS.APP_ONLINE_TIME  = :APP_ONLINE_TIME,");
			param.put("APP_ONLINE_TIME", ObjectUtils.toString(searchParams.get("APP_ONLINE_TIME")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("MUST_ACTCOUNT")))) {
			sql.append("  GSS.MUST_ACTCOUNT    = :MUST_ACTCOUNT,");
			param.put("MUST_ACTCOUNT", ObjectUtils.toString(searchParams.get("MUST_ACTCOUNT")));
		}
		sql.append("  GSS.UPDATED_BY = '定时更新'");
		sql.append("  WHERE GSS.IS_DELETED = 'N'");
		sql.append("  AND GSS.CHOOSE_ID = :CHOOSE_ID");
		param.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 获得学习考试记录
	 * @return
	 */
	public List getstudyRecord(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GTP.XF,");
		sql.append("  GTP.KCXXBZ,");
		sql.append("  GTP.KCKSBZ,");
		sql.append("  GSR.CHOOSE_ID,");
		sql.append("  GSR.EXAM_CHOOSE_ID,");
		sql.append("  GSR.STUDENT_ID,");
		sql.append("  GSR.STUDY_SCORE,");
		sql.append("  GSR.EXAM_SCORE");
		sql.append("  FROM GJT_STUDENT_INFO GSI,");
		sql.append("  VIEW_TEACH_PLAN   GTP,");
		sql.append("  GJT_REC_RESULT   GRR,");
		sql.append("  GJT_STUDY_RECORD GSR");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GTP.IS_DELETED = 'N'");
		sql.append("  AND GRR.IS_DELETED = 'N'");
		sql.append("  AND GSR.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  AND GRR.REC_ID = GSR.CHOOSE_ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 更新课程班动态数据
	 */
	public int updClassDyna(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE LCMS_CLASS_DYNA LCD");
		sql.append("  SET LCD.CLASS_SUM_USER         = :CLASS_SUM_USER,");
		sql.append("  LCD.CLASS_SUM_POINT        = :CLASS_SUM_POINT,");
		sql.append("  LCD.CLASS_SUM_PROGRESS     = :CLASS_SUM_PROGRESS,");
		sql.append("  LCD.CLASS_SUM_TIME         = :CLASS_SUM_TIME,");
		sql.append("  LCD.CLASS_SUM_COUNT        = :CLASS_SUM_COUNT,");
		sql.append("  LCD.CLASS_AVG_POINT        = :CLASS_AVG_POINT,");
		sql.append("  LCD.CLASS_AVG_PROGRESS     = :CLASS_AVG_PROGRESS,");
		sql.append("  LCD.CLASS_AVG_TIME         = :CLASS_AVG_TIME,");
		sql.append("  LCD.CLASS_AVG_COUNT        = :CLASS_AVG_COUNT,");
		sql.append("  LCD.NOT_LOGIN_NUM          = :NOT_LOGIN_NUM,");
		sql.append("  LCD.TODAY_LOGIN_NUM        = :TODAY_LOGIN_NUM,");
		sql.append("  LCD.ALL_MUST_ACT           = :ALL_MUST_ACT,");
		sql.append("  LCD.ALL_MUST_POINT         = :ALL_MUST_POINT,");
		sql.append("  LCD.STATE                  = :STATE,");
		sql.append("  LCD.NORM_AVG_PROGRESS      = :NORM_AVG_PROGRESS,");
		sql.append("  LCD.IS_NORM                = :IS_NORM,");
		sql.append("  LCD.COURSE_PASSING_MUSTACT = :COURSE_PASSING_MUSTACT,");
		sql.append("  LCD.COURSE_PASSING_SCORE   = :COURSE_PASSING_SCORE,");
		sql.append("  LCD.UPDATED_DT             = SYSDATE");
		sql.append("  WHERE LCD.ISDELETED = 'N'");
		sql.append("  AND LCD.CLASS_ID = :CLASS_ID");

		param.put("CLASS_SUM_USER", ObjectUtils.toString(searchParams.get("CLASS_SUM_USER")));
		param.put("CLASS_SUM_POINT", ObjectUtils.toString(searchParams.get("CLASS_SUM_POINT")));
		param.put("CLASS_SUM_PROGRESS", ObjectUtils.toString(searchParams.get("CLASS_SUM_PROGRESS")));
		param.put("CLASS_SUM_TIME", ObjectUtils.toString(searchParams.get("CLASS_SUM_TIME")));
		param.put("CLASS_SUM_COUNT", ObjectUtils.toString(searchParams.get("CLASS_SUM_COUNT")));
		param.put("CLASS_AVG_POINT", ObjectUtils.toString(searchParams.get("CLASS_AVG_POINT")));
		param.put("CLASS_AVG_PROGRESS", ObjectUtils.toString(searchParams.get("CLASS_AVG_PROGRESS")));
		param.put("CLASS_AVG_TIME", ObjectUtils.toString(searchParams.get("CLASS_AVG_TIME")));
		param.put("CLASS_AVG_COUNT", ObjectUtils.toString(searchParams.get("CLASS_AVG_COUNT")));
		param.put("NOT_LOGIN_NUM", ObjectUtils.toString(searchParams.get("NOT_LOGIN_NUM")));
		param.put("TODAY_LOGIN_NUM", ObjectUtils.toString(searchParams.get("TODAY_LOGIN_NUM")));
		param.put("ALL_MUST_ACT", ObjectUtils.toString(searchParams.get("ALL_MUST_ACT")));
		param.put("ALL_MUST_POINT", ObjectUtils.toString(searchParams.get("ALL_MUST_POINT")));
		param.put("STATE", ObjectUtils.toString(searchParams.get("STATE")));
		param.put("NORM_AVG_PROGRESS", ObjectUtils.toString(searchParams.get("NORM_AVG_PROGRESS")));
		param.put("IS_NORM", ObjectUtils.toString(searchParams.get("IS_NORM")));
		param.put("COURSE_PASSING_MUSTACT", ObjectUtils.toString(searchParams.get("COURSE_PASSING_MUSTACT")));
		param.put("COURSE_PASSING_SCORE", ObjectUtils.toString(searchParams.get("COURSE_PASSING_SCORE")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 查询没有在学习记录表的数据
	 * @return
	 */
	public List getRecChoose(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GRR.REC_ID, GRR.STUDENT_ID, GRR.COURSE_ID, GRR.TEACH_PLAN_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  LEFT JOIN GJT_STUDY_RECORD GSRD ON GRR.REC_ID = GSRD.CHOOSE_ID");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.STUDENT_ID = :STUDENT_ID");
		sql.append("  AND GSRD.CHOOSE_ID IS NULL");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询没有在学习记录表的数据
	 * @return
	 */
	@Deprecated
	public List getStudyChoose(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GRR.REC_ID, GRR.STUDENT_ID, GRR.COURSE_ID, GRR.TEACH_PLAN_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  LEFT JOIN GJT_STUDENT_STUDY_SITUATION GSSS ON GRR.REC_ID = GSSS.CHOOSE_ID");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.STUDENT_ID = :STUDENT_ID");
		sql.append("  AND GSSS.CHOOSE_ID IS NULL");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 获取今天登陆的student_id
	 * @return
	 */
	public List getLoginStudent(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT DISTINCT TPLL.CREATED_BY STUDENT_ID");
		sql.append("  FROM TBL_PRI_LOGIN_LOG TPLL");
		sql.append("  WHERE TPLL.IS_DELETED = 'N'");
		sql.append("  AND TO_CHAR(TPLL.CREATED_DT, 'yyyy-mm-dd') =");
		sql.append("  TO_CHAR(sysdate, 'yyyy-mm-dd')");

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 新增学习记录
	 * @return
	 */
	@Transactional
	public int insertStudyRecord(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_STUDY_RECORD");
		sql.append("  (CHOOSE_ID,");
		sql.append("  EXAM_CHOOSE_ID,");
		sql.append("  STUDENT_ID,");
		sql.append("  COURSE_ID,");
		//sql.append("  SCORE_STATE,");
		sql.append("  IS_DELETED,");
		sql.append("  TEACH_PLAN_ID,");
		sql.append("  CREATED_DT)");
		sql.append("  SELECT GRR.REC_ID,");
		sql.append("  (SELECT GEP.ID");
		sql.append("  FROM GJT_EXAM_PLAN GEP");
		sql.append("  WHERE GEP.IS_DELETED = 'N'");
		sql.append("  AND GEP.EXAM_COURSE = GRR.COURSE_ID");
		sql.append("  AND GEP.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND ROWNUM = 1),");
		sql.append("  GRR.STUDENT_ID,");
		sql.append("  GRR.COURSE_ID,");
		//sql.append("  '2',");
		sql.append("  'N',");
		sql.append("  GRR.TEACH_PLAN_ID,");
		sql.append("  SYSDATE");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  LEFT JOIN GJT_STUDY_RECORD GSRD ON GRR.REC_ID = GSRD.CHOOSE_ID");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GSRD.CHOOSE_ID IS NULL");
		sql.append("  AND GRR.STUDENT_ID = :STUDENT_ID");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 新增学习记录
	 * @return
	 */
	@Deprecated
	@Transactional
	public int insertStudySituation(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_STUDENT_STUDY_SITUATION");
		sql.append("  (CHOOSE_ID, STUDENT_ID, TEACH_PLAN_ID, COURSE_ID)");
		sql.append("  SELECT GRR.REC_ID, GRR.STUDENT_ID, GRR.TEACH_PLAN_ID, GRR.COURSE_ID");
		sql.append("  FROM GJT_REC_RESULT GRR");
		sql.append("  LEFT JOIN GJT_STUDENT_STUDY_SITUATION GSSS ON GRR.REC_ID = GSSS.CHOOSE_ID");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GSSS.CHOOSE_ID IS NULL");
		sql.append("  AND GRR.STUDENT_ID = :STUDENT_ID");

		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}

	/**
	 * 同步所有的学生数据
	 * @return
	 */
	public List syncAllStudentData() {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT DISTINCT gsi.STUDENT_ID ");
		sql.append(" FROM GJT_STUDENT_INFO GSI, GJT_REC_RESULT EGR ");
		sql.append(" WHERE GSI.IS_DELETED = 'N' ");
		sql.append(" AND EGR.IS_DELETED = 'N' ");
		sql.append(" AND GSI.STUDENT_ID = EGR.STUDENT_ID ");
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 获取所有院校的APPID
	 */
	public List getAppIdList() {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT GSI.APPID");
		sql.append(" FROM GJT_SCHOOL_INFO GSI");
		sql.append(" WHERE GSI.IS_DELETED = 'N'");
		sql.append(" AND GSI.APPID IS NOT NULL");
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询学习的选课表数据
	 */
	public List getRecStudy(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT GRR.REC_ID,");
		sql.append(" GRR.STUDENT_ID,");
		sql.append(" GRR.EXAM_SCORE,");
		sql.append(" GRR.EXAM_SCORE1,");
		sql.append(" GRR.EXAM_SCORE2,");
		sql.append(" GRR.EXAM_STATE");
		sql.append(" FROM GJT_REC_RESULT GRR");
		sql.append(" WHERE GRR.IS_DELETED = 'N'");
		sql.append(" AND GRR.EXAM_STATE = '2'");
		sql.append(" AND GRR.REC_ID = :CHOOSE_ID");
		
		param.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 更新选课表中的成绩得分
	 * @return
	 */
	@Transactional
	public int updRecScore(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_REC_RESULT GRR");
		sql.append("  SET GRR.UPDATED_DT  = SYSDATE,");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_SCORE")))) {
			sql.append("  GRR.EXAM_SCORE  = :STUDY_SCORE,");
			param.put("STUDY_SCORE", ObjectUtils.toString(searchParams.get("STUDY_SCORE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_SCORE")))) {
			sql.append("  GRR.EXAM_SCORE1 = :EXAM_SCORE,");
			param.put("EXAM_SCORE", ObjectUtils.toString(searchParams.get("EXAM_SCORE")));
		}
		sql.append("  GRR.UPDATED_BY  = '定时更新'");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GRR.EXAM_STATE = '2'");
		sql.append("  AND GRR.REC_ID = :CHOOSE_ID");
		sql.append("  AND GRR.STUDENT_ID = :STUDENT_ID");

		param.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 同步接口记录新增
	 * @return
	 */
	@Transactional
	public int addSyncRecord(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_SYNC_RECORD");
		sql.append("  (SYNC_ID, SYNC_URL, SYNC_PARM, RETURN_VAL)");
		sql.append("  VALUES");
		sql.append("  (:SYNC_ID, :SYNC_URL, :SYNC_PARM, :RETURN_VAL)");

		param.put("SYNC_ID", ObjectUtils.toString(searchParams.get("SYNC_ID")));
		param.put("SYNC_URL", ObjectUtils.toString(searchParams.get("SYNC_URL")));
		param.put("SYNC_PARM", ObjectUtils.toString(searchParams.get("SYNC_PARM")));
		param.put("RETURN_VAL", ObjectUtils.toString(searchParams.get("RETURN_VAL")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 获取所有院校的APPID
	 */
	public Page getPcourseList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT * FROM (");
		sql.append("  SELECT LCC.COURSE_ID,");
		sql.append("  LCC.COURSE_CODE,");
		sql.append("  LCC.COURSE_NAME,");
		sql.append("  TO_CHAR(LCC.PUBLISH_DT, 'yyyy-mm-dd') PUBLISH_DT,");
		sql.append("  LCC.CREATED_DT,");
		sql.append("  LCC.APP_ID,");
		sql.append("  LCC.PROFESSION,");
		sql.append("  LCC.EDUCATION_LEVEL,");
		sql.append("  LCC.LABEL,");
		sql.append("  (SELECT COUNT(LTT.TASK_ID)");
		sql.append("  FROM LCMS_TERM_COURSEINFO  LTC,");
		sql.append("  LCMS_TERMCOURSE_TASK  LTT,");
		sql.append("  LCMS_TERMCOURSE_CLASS LTS");
		sql.append("  WHERE LTC.ISDELETED = 'N'");
		sql.append("  AND LTT.ISDELETED = 'N'");
		sql.append("  AND LTS.ISDELETED = 'N'");
		sql.append("  AND LTC.TERMCOURSE_ID = LTS.TERMCOURSE_ID");
		sql.append("  AND LTC.TERMCOURSE_ID = LTT.TERMCOURSE_ID");
		sql.append("  AND LTS.TERMCOURSE_CLASS_ID = LTT.CLASS_ID");
		sql.append("  AND LTC.COURSE_ID = LCC.COURSE_ID");
		sql.append("  AND LTS.CLASS_NAME LIKE '%制作班级%'");
		sql.append("  AND LTC.TERM_ID = '614b0592ac1082a750505050c86c07e1') TASK_COUNT");
		sql.append("  FROM LCMS_COURSE_COURSEINFO LCC");
		sql.append("  WHERE LCC.ISDELETED = 'N'");
		
		// 演示用的
		if ("APP999".equals(ObjectUtils.toString(searchParams.get("APP_ID")))) {
			sql.append("  AND LCC.COURSE_ID IN");
			sql.append("  ('ded1406dac1082a578b7ea5c0bf20e58',");
			sql.append("  'b414dbdfac10869700a450bba7fdbf84',");
			sql.append("  '67fbf13a95d54fa5b9005488d38b4d0f')");
		} else {
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("APP_ID")))) {
				sql.append("  AND LCC.APP_ID = 'APP014'");
			} else {
				sql.append("  AND LCC.APP_ID = :APP_ID");
				param.put("APP_ID", ObjectUtils.toString(searchParams.get("APP_ID")));
			}
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_NAME")))) {
			sql.append("  AND LCC.COURSE_NAME LIKE :COURSE_NAME");
			param.put("COURSE_NAME", "%"+ObjectUtils.toString(searchParams.get("COURSE_NAME"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_CODE")))) {
			sql.append("  AND LCC.COURSE_CODE LIKE :COURSE_CODE");
			param.put("COURSE_CODE", "%"+ObjectUtils.toString(searchParams.get("COURSE_CODE"))+"%");
		}
		sql.append("  ) TAB WHERE 1=1");
		
		if ("1".equals(ObjectUtils.toString(searchParams.get("COURSE_STATE")))) {
			sql.append("  AND TASK_COUNT>0");
		} else if ("2".equals(ObjectUtils.toString(searchParams.get("COURSE_STATE")))) {
			sql.append("  AND TASK_COUNT=0");
		}
		sql.append("  ORDER BY CREATED_DT DESC");
		return (Page) commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}
	
	/**
	 * 获取最新一期的网考，省网考学生信息
	 */
	public List getStudentList() {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GEP.EXAM_NO, B.EXAM_BATCH_CODE, GEA.STUDENT_ID, GSI.XH, GSI.SFZH, GEA.APPOINTMENT_ID");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  LEFT JOIN GJT_EXAM_PLAN_NEW GEP");
		sql.append("  ON GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  LEFT JOIN GJT_STUDENT_INFO GSI");
		sql.append("  ON GSI.STUDENT_ID = GEA.STUDENT_ID");
		sql.append("  LEFT JOIN (SELECT MAX(GEAN.EXAM_BATCH_CODE) EXAM_BATCH_CODE, GEAN.TYPE");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEAN");
		sql.append("  GROUP BY GEAN.TYPE) B");
		sql.append("  ON B.TYPE = GEP.TYPE");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEP.IS_DELETED = '0'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GEP.TYPE IN ('7', '19')");
		sql.append("  AND GEA.EXAM_SCORE IS NULL");
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 登记成绩
	 * @return
	 */
	@Transactional
	public int updateExamScore(Map searchParams) {
		Map param = new HashMap();
		StringBuilder sql = new StringBuilder();
		
		sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  SET GEA.EXAM_SCORE  = :EXAM_SCORE,");
		sql.append("  GEA.EXAM_STATUS = :EXAM_STATUS,");
		sql.append("  GEA.EXAM_COUNT = :EXAM_COUNT,");
		sql.append("  GEA.UPDATED_BY = '定时任务更新成绩',");
		sql.append("  GEA.UPDATED_DT  = SYSDATE");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEA.APPOINTMENT_ID = :APPOINTMENT_ID");

		param.put("EXAM_SCORE", ObjectUtils.toString(searchParams.get("EXAM_SCORE")));
		param.put("EXAM_STATUS", ObjectUtils.toString(searchParams.get("EXAM_STATUS")));
		param.put("EXAM_COUNT", ObjectUtils.toString(searchParams.get("EXAM_COUNT")));
		param.put("APPOINTMENT_ID", ObjectUtils.toString(searchParams.get("APPOINTMENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 更新考试状态
	 * @return
	 */
	@Transactional
	public int updateExamState() {
		Map param = new HashMap();
		StringBuilder sql = new StringBuilder();
		
		//更新待考试--》考试中
		sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  SET GEA.EXAM_STATUS = '3'");
		sql.append("  ,GEA.UPDATED_DT = SYSDATE");
		sql.append("  ,GEA.UPDATED_BY = '定时任务更新考试状态'");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEA.EXAM_STATUS = '0'");
		sql.append("  AND GEA.EXAM_PLAN_ID IN");
		sql.append("  (SELECT GEP.EXAM_PLAN_ID");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP");
		sql.append("  WHERE GEP.IS_DELETED = '0'");
		sql.append("  AND (SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END))");
		commonDao.updateForMapNative(sql.toString(), param);
		
		//更新考试类型为形考(13)考试中--》已截止
		sql = new StringBuilder("");
		sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  SET GEA.EXAM_STATUS = '5'");
		sql.append("  ,GEA.UPDATED_DT = SYSDATE");
		sql.append("  ,GEA.UPDATED_BY = '定时任务更新考试状态'");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEA.TYPE = '13'");
		sql.append("  AND GEA.EXAM_STATUS = '3'");
		sql.append("  AND GEA.EXAM_PLAN_ID IN");
		sql.append("  (SELECT GEP.EXAM_PLAN_ID");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP");
		sql.append("  WHERE GEP.IS_DELETED = '0'");
		sql.append("  AND SYSDATE > GEP.EXAM_END)");
		commonDao.updateForMapNative(sql.toString(), param);
		
		//更新考试类型为笔试(8)/机考(11)/论文(14)/报告(15)/大作业(20)/论文报告(21) 考试中--》待批改
		sql = new StringBuilder("");
		sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  SET GEA.EXAM_STATUS = '4'");
		sql.append("  ,GEA.UPDATED_DT = SYSDATE");
		sql.append("  ,GEA.UPDATED_BY = '定时任务更新考试状态'");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEA.TYPE IN ('8', '11', '14', '15', '20', '21')");
		sql.append("  AND GEA.EXAM_STATUS = '3'");
		sql.append("  AND GEA.EXAM_PLAN_ID IN");
		sql.append("  (SELECT GEP.EXAM_PLAN_ID");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP");
		sql.append("  WHERE GEP.IS_DELETED = '0'");
		sql.append("  AND SYSDATE > GEP.EXAM_END)");
		commonDao.updateForMapNative(sql.toString(), param);
		
		//更新考试类型为网考(7)/省网考(19)考试中 --》通过/不通过
		sql = new StringBuilder("");
		sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  SET GEA.EXAM_STATUS = '2'");
		sql.append("  ,GEA.UPDATED_DT = SYSDATE");
		sql.append("  ,GEA.UPDATED_BY = '定时任务更新考试状态'");
		sql.append("  ,GEA.EXAM_SCORE = '0'");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEA.EXAM_STATUS = '3'");
		sql.append("  AND GEA.TYPE IN ('7','19')");
		sql.append("  AND GEA.EXAM_SCORE IS NULL");
		sql.append("  AND GEA.EXAM_PLAN_ID IN");
		sql.append("  (SELECT GEP.EXAM_PLAN_ID");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP");
		sql.append("  WHERE GEP.IS_DELETED = '0'");
		sql.append("  AND SYSDATE > GEP.EXAM_END)");
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	
}
