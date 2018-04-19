package com.ouchgzee.study.dao.course;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;

@Repository
public class CourseLearningDao {

	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	/**
	 * 查询班级人数
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List queryClassStudentCount(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT ");
		sql.append("	COUNT(1) CLASS_STUDENT_NUM ");
		sql.append("FROM ");
		sql.append("	GJT_CLASS_INFO GCI ");
		sql.append("INNER JOIN GJT_CLASS_STUDENT GCS ON ");
		sql.append("	GCS.IS_DELETED = 'N' ");
		sql.append("	AND GCS.CLASS_ID = GCI.CLASS_ID ");
		sql.append("INNER JOIN GJT_STUDENT_INFO GSI ON ");
		sql.append("	GSI.IS_DELETED = 'N' ");
		sql.append("	AND GSI.STUDENT_ID = GCS.STUDENT_ID ");
		sql.append("INNER JOIN GJT_USER_ACCOUNT GUA ON ");
		sql.append("	GUA.IS_DELETED = 'N' ");
		sql.append("	AND GUA.ID = GSI.ACCOUNT_ID ");
		sql.append("WHERE ");
		sql.append("	GCI.CLASS_TYPE = 'teach' ");
		sql.append("	AND GCI.IS_DELETED = 'N' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("CLASS_ID"))) {
			sql.append("	AND GCI.CLASS_ID = :CLASS_ID ");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * 根据SQL查询出自己击败多少人。
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getOrderPM(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT ");
		sql.append("	COUNT(*) PMODER ");
		sql.append("FROM ");
		sql.append("	GJT_PROGRESS_ORDER PO, ");
		sql.append("	GJT_CLASS_STUDENT CS ");
		sql.append("WHERE ");
		sql.append("	PO.STUDENT_ID = CS.STUDENT_ID ");
		sql.append("	AND CS.IS_DELETED = 'N' ");

		if (EmptyUtils.isNotEmpty(searchParams.get("CLASS_ID"))) {
			sql.append("	AND CS.CLASS_ID = :CLASS_ID ");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("TERM_ID"))) {
			sql.append("	AND CS.GRADE_ID = :TERM_ID ");
			params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		}

		sql.append("	AND PO.DAY_ORDER >( ");
		sql.append("		SELECT ");
		sql.append("			DAY_ORDER ");
		sql.append("		FROM ");
		sql.append("			GJT_PROGRESS_ORDER PO1 ");
		sql.append("		WHERE 1 = 1");
		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("		AND	PO1.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		sql.append("	) ");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询进度排名
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getProgressOrder(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	(GPO.DAY_ORDER - GPO.CURRENT_LOGIN_ORDER) ZZBH,");
		sql.append("  	GPO.*");
		sql.append("  FROM");
		sql.append("  	GJT_PROGRESS_ORDER GPO,");
		sql.append("  	GJT_CLASS_STUDENT CS");
		sql.append("  WHERE");
		sql.append("	CS.IS_DELETED = 'N' ");
		sql.append("	AND GPO.STUDENT_ID = CS.STUDENT_ID ");

		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("  	AND GPO.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		sql.append("	AND ROWNUM = 1 ");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 更新排名
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateProgressOrder(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE");
		sql.append("  	GJT_PROGRESS_ORDER GPO");
		sql.append("  SET");
		if (EmptyUtils.isNotEmpty(searchParams.get("CURRENT_LOGIN_TIME"))) {
			sql.append("  	GPO.LAST_LOGIN_TIME = :CURRENT_LOGIN_TIME,");
			params.put("CURRENT_LOGIN_TIME", searchParams.get("CURRENT_LOGIN_TIME"));
		}
		sql.append("  	GPO.LAST_LOGIN_ORDER = :CURRENT_LOGIN_ORDER,");
		sql.append("  	GPO.CURRENT_LOGIN_TIME = :UPDATE_DT,");
		sql.append("  	GPO.CURRENT_LOGIN_ORDER = :DAY_ORDER,");
		sql.append("  	GPO.ORDER_CHANGE = :ZZBH ");
		sql.append("  WHERE");
		sql.append("  	GPO.STUDENT_ID = :STUDENT_ID ");

		// params.put("CURRENT_LOGIN_TIME",
		// searchParams.get("CURRENT_LOGIN_TIME"));
		params.put("CURRENT_LOGIN_ORDER",
				Integer.parseInt(ObjectUtils.toString(searchParams.get("CURRENT_LOGIN_ORDER"))));
		params.put("UPDATE_DT", searchParams.get("UPDATE_DT"));
		params.put("DAY_ORDER", Integer.parseInt(ObjectUtils.toString(searchParams.get("DAY_ORDER"))));
		params.put("ZZBH", Integer.parseInt(ObjectUtils.toString(searchParams.get("ZZBH"))));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.updateForMapNative(sql.toString(), params);

	}

	/**
	 * 查询班级人数
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getClassStudentCount(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.AVG_PROGRESS,");
		sql.append("  	TAB.STD_NAME,");
		sql.append("  	TAB.STD_ID,");
		sql.append("  	TAB.STD_RANK");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append(
				"  			ROUND( DECODE( COUNT( GSR.PROGRESS ), 0, 0, SUM( NVL( GSR.PROGRESS, 0 ))/ COUNT( GSR.PROGRESS )))|| '%' AVG_PROGRESS,");
		sql.append("  			GSI.XM STD_NAME,");
		sql.append("  			GSI.STUDENT_ID STD_ID,");
		sql.append("  			ROW_NUMBER() OVER(");
		sql.append("  			ORDER BY");
		sql.append(
				"  				ROUND( DECODE( COUNT( GSR.PROGRESS ), 0, 0, SUM( NVL( GSR.PROGRESS, 0 ))/ COUNT( GSR.PROGRESS ))) DESC");
		sql.append("  			) STD_RANK");
		sql.append("  		FROM");
		/*
		 * sql.append("  			GJT_STUDY_RECORD GSR");
		 */
		sql.append("  			VIEW_STUDENT_STUDY_SITUATION GSR"); // 从视图中查询学习进度排名
		sql.append("  		LEFT JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GSR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		LEFT JOIN GJT_GRADE GG ON");
		sql.append("  			GTP.ACTUAL_GRADE_ID = GG.GRADE_ID,");
		sql.append("  			GJT_CLASS_STUDENT GCS,");
		sql.append("  			GJT_CLASS_INFO GCI,");
		sql.append("  			GJT_STUDENT_INFO GSI");
		sql.append("  		WHERE");
		sql.append("  			GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  			AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  			AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  			AND GSR.STUDENT_ID = GSI.STUDENT_ID");

		if ("1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))) {
			sql.append("        AND SYSDATE BETWEEN GG.START_DATE AND NVL(GG.END_DATE, ADD_MONTHS(GG.START_DATE, 4))");
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("CLASS_ID"))) {
			sql.append("  			AND GCI.CLASS_ID = :CLASS_ID ");
			params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("TERM_ID"))) {
			sql.append("	AND GTP.ACTUAL_GRADE_ID = :TERM_ID ");
			params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		}

		sql.append("  		GROUP BY");
		sql.append("  			GSI.XM,");
		sql.append("  			GSI.STUDENT_ID");
		sql.append("  		ORDER BY");
		sql.append(
				"  			ROUND( DECODE( COUNT( GSR.PROGRESS ), 0, 0, SUM( NVL( GSR.PROGRESS, 0 ))/ COUNT( GSR.PROGRESS ))) DESC,");
		sql.append("  			GSI.XM");
		sql.append("  	) TAB");
		sql.append("  	WHERE 1=1");

		if ("1".equals(ObjectUtils.toString(searchParams.get("STUDENT_PM")))) {
			sql.append("  	AND TAB.STD_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 根据教学班级id获取进度学习排行榜top5
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getStudyRank(Map<String, Object> searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		/*
		 * sql.append(" SELECT * FROM ("); sql.append("  SELECT");
		 * sql.append("  	gpo.STUDENT_ID AS std_id,");
		 * sql.append("  	gsi.XM std_name,");
		 * sql.append("  	gpo.DAY_ORDER AS std_rank,"); sql.
		 * append("  	ROUND( decode(COUNT( gsr.SCHEDULE ),0,0,SUM( NVL( gsr.SCHEDULE, 0 ))/ COUNT( gsr.SCHEDULE )))||'%' avg_progress"
		 * );//decode(除数，0，显示0%，不为零的正常表达式） sql.append("  FROM");
		 * sql.append("  	GJT_PROGRESS_ORDER gpo,");
		 * sql.append("  	GJT_STUDY_RECORD gsr,");
		 * sql.append("  	GJT_STUDENT_INFO gsi"); sql.append("  WHERE");
		 * sql.append("  	gsr.IS_DELETED = 'N'");
		 * sql.append("  	AND gsi.IS_DELETED = 'N'");
		 * sql.append("  	AND gsr.STUDENT_ID = gsi.STUDENT_ID");
		 * sql.append("  	AND gsr.STUDENT_ID = gpo.STUDENT_ID");
		 * sql.append("  	AND gpo.TEACH_CLASS_ID = :class_id");
		 * sql.append("  	AND gpo.DAY_ORDER < 6"); sql.append("  GROUP BY");
		 * sql.append("  	gpo.STUDENT_ID,"); sql.append("  	gsi.XM,");
		 * sql.append("  	gpo.DAY_ORDER"); sql.append("  ORDER BY");
		 * sql.append("  	gpo.DAY_ORDER");
		 * sql.append(" ) where avg_progress <>'0%' and rownum <6");
		 */

		sql.append("  SELECT AVG_PROGRESS,STD_NAME,STD_ID,STD_RANK FROM ( ");
		sql.append("  SELECT");
		sql.append(
				"  	ROUND( DECODE( COUNT( gsr.SCHEDULE ), 0, 0, SUM( NVL( gsr.SCHEDULE, 0 ))/ COUNT( gsr.SCHEDULE )))|| '%' avg_progress,gsi.XM std_name,gsi.STUDENT_ID std_id,ROW_NUMBER() OVER(ORDER BY ROUND( DECODE( COUNT( gsr.SCHEDULE ), 0, 0, SUM( NVL( gsr.SCHEDULE, 0 ))/ COUNT( gsr.SCHEDULE ))) DESC ) std_rank");
		sql.append("  FROM");
		sql.append("  	GJT_STUDY_RECORD gsr");
		sql.append("  LEFT JOIN VIEW_TEACH_PLAN gtp ON gsr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID");
		sql.append("  LEFT JOIN GJT_GRADE gg ON gtp.ACTUAL_GRADE_ID=gg.GRADE_ID,");
		sql.append("  	GJT_CLASS_STUDENT gcs,");
		sql.append("  	GJT_CLASS_INFO gci,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  WHERE");
		sql.append("  	gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  	AND gci.CLASS_TYPE = 'teach'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gsr.STUDENT_ID = gsi.STUDENT_ID");

		if (EmptyUtils.isEmpty(searchParams.get("term_id"))) {
			sql.append("    AND SYSDATE BETWEEN gg.START_DATE AND NVL( gg.END_DATE, ADD_MONTHS(gg.START_DATE, 4 ))");// 当前学期
		} else {
			sql.append("	AND gtp.ACTUAL_GRADE_ID = :term_id");
			params.put("term_id", ObjectUtils.toString(searchParams.get("term_id")));
		}

		// -- AND gg.IS_DELETED='N' AND gg.START_DATE<=SYSDATE AND
		// gg.END_DATE>=SYSDATE AND rownum<2
		// --AND gg.XX_ID='2f5bfcce71fa462b8e1f65bcd0f4c632'
		sql.append("  	AND gci.CLASS_ID=:class_id ");
		sql.append(
				"GROUP BY gsi.XM,gsi.STUDENT_ID ORDER BY ROUND( DECODE( COUNT( gsr.SCHEDULE ), 0, 0, SUM( NVL( gsr.SCHEDULE, 0 ))/ COUNT( gsr.SCHEDULE ))) DESC,gsi.XM) WHERE rownum<6");

		params.put("class_id", ObjectUtils.toString(searchParams.get("class_id")));

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateRecResultScore(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  SET");
		sql.append("  	GRR.UPDATED_DT = SYSDATE,");
		sql.append("  	GRR.UPDATED_BY = :UPDATED_BY ");

		if (EmptyUtils.isNotEmpty(searchParams.get("progress"))) {
			sql.append("  	,GRR.PROGRESS = :progress ");
			params.put("progress", searchParams.get("progress"));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("xcxScore"))) {
			sql.append("  	,GRR.EXAM_SCORE = :xcxScore ");
			params.put("xcxScore", searchParams.get("xcxScore"));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("zjxScore"))) {
			sql.append("  	,GRR.EXAM_SCORE1 = :zjxScore ");
			params.put("zjxScore", searchParams.get("zjxScore"));
		}
		sql.append("  WHERE");
		sql.append("  	GRR.EXAM_STATE = '2'");
		sql.append("  	AND GRR.REC_ID = :CHOOSE_ID ");
		sql.append("	AND GRR.TEACH_PLAN_ID = :TEACH_PLAN_ID ");
		params.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
		params.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));
		params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));

		return commonDao.updateForMapNative(sql.toString(), params);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	public int updateStudySituation(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_STUDENT_STUDY_SITUATION GSS");
		sql.append("  SET GSS.SCORE = :SCORE, GSS.PROGRESS = :PROGRESS, GSS.UPDATED_DT = SYSDATE");
		sql.append("  WHERE GSS.CHOOSE_ID = :CHOOSE_ID");
		params.put("SCORE", ObjectUtils.toString(searchParams.get("SCORE")));
		params.put("PROGRESS", ObjectUtils.toString(searchParams.get("PROGRESS")));
		params.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));

		return commonDao.updateForMapNative(sql.toString(), params);
	}

	/**
	 * 根据选课ID查询选课详情
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getRecResultDetail(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GC.COURSE_ID,");
		sql.append("  	GC.KCMC COURSE_NAME,");
		sql.append("  	GC.KCH COURSE_CODE,");
		sql.append("  	GRR.REC_ID CHOOSE_ID,");
		sql.append("  	GRR.TEACH_PLAN_ID,");
		sql.append("  	TO_CHAR(GRR.EXAM_SCORE) XCX_SCORE,");
		sql.append("  	TO_CHAR(GRR.EXAM_SCORE1) ZJX_SCORE,");
		sql.append("  	TO_CHAR(GRR.EXAM_SCORE2) ZCJ_SCORE,");
		sql.append("  	GRR.COURSE_SCHEDULE,");
		sql.append("  	GRR.XX_ID");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  INNER JOIN GJT_COURSE GC ON");
		sql.append("  	GRR.COURSE_ID = GC.COURSE_ID");
		sql.append("  WHERE");
		sql.append("  	GRR.IS_DELETED = 'N'");
		sql.append("  	AND GC.IS_DELETED = 'N'");
		sql.append("  	AND GRR.REC_ID = :REC_ID ");

		params.put("REC_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 插入重修记录表
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertLearnRepair(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_LEARN_REPAIR GLR(");
		sql.append("  			REPAIR_ID,");
		sql.append("  			TEACH_PLAN_ID,");
		sql.append("  			XCX_SCORE,");
		sql.append("  			ZJX_SCORE,");
		sql.append("  			ZCJ_SCORE,");
		sql.append("  			STATUS,");
		sql.append("  			STUDENT_ID,");
		sql.append("  			XH,");
		sql.append("  			COURSE_CODE,");
		sql.append("  			CREATED_DT,");
		sql.append("  			CREATED_BY,");
		sql.append("  			STUDENT_NAME,");
		sql.append("  			SUBJECT_NAME");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:REPAIR_ID,");
		sql.append("  		:TEACH_PLAN_ID,");
		sql.append("  		:XCX_SCORE,");
		sql.append("  		:ZJX_SCORE,");
		sql.append("  		:ZCJ_SCORE,");
		sql.append("  		:STATUS,");
		sql.append("  		:STUDENT_ID,");
		sql.append("  		:XH,");
		sql.append("  		:COURSE_CODE,");
		sql.append("  		SYSDATE,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		:STUDENT_NAME,");
		sql.append("  		:SUBJECT_NAME ");
		sql.append("  	)");

		params.put("REPAIR_ID", ObjectUtils.toString(searchParams.get("REPAIR_ID")));
		params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		params.put("XCX_SCORE", ObjectUtils.toString(searchParams.get("XCX_SCORE")));
		params.put("ZJX_SCORE", ObjectUtils.toString(searchParams.get("ZJX_SCORE")));
		params.put("ZCJ_SCORE", ObjectUtils.toString(searchParams.get("ZCJ_SCORE")));
		params.put("STATUS", ObjectUtils.toString(searchParams.get("STATUS")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		params.put("COURSE_CODE", ObjectUtils.toString(searchParams.get("COURSE_CODE")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
		params.put("STUDENT_NAME", ObjectUtils.toString(searchParams.get("STUDENT_NAME")));
		params.put("SUBJECT_NAME", ObjectUtils.toString(searchParams.get("SUBJECT_NAME")));

		return commonDao.insertForMapNative(sql.toString(), params);

	}

	/**
	 * 更新选课表EXAM_STATE = '2',状态为学习中
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateRecordState(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  SET");
		sql.append("  	GRR.EXAM_STATE = '2',EXAM_SCORE1=null,EXAM_SCORE2=null,GET_CREDITS=null,");
		sql.append("  	GRR.VERSION = VERSION + 1,");
		sql.append("  	GRR.UPDATED_DT = SYSDATE,");
		// 20171127 特殊处理麦当劳项目2016秋学员期末考试预约工作，全部无需缴费，预约期过了就可以去掉代码，没办法老是不按流程来
		if (StringUtils.equals((String) searchParams.get("EXAM_BATCH_CODE"), "201711220006")) {
			sql.append("  	GRR.PAY_STATE = '2',");
		} else {
			sql.append("  	GRR.PAY_STATE = '0',");
		}
		sql.append("  	GRR.UPDATED_BY = :UPDATED_BY ");
		sql.append("  WHERE");
		sql.append("  	GRR.REC_ID = :CHOOSE_ID ");
		sql.append("  	AND GRR.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GRR.IS_DELETED = 'N'");

		params.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
		params.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.updateForMapNative(sql.toString(), params);

	}

	/**
	 * 查询是否存在重修记录
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryIsExistRepairRecord(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	COUNT(1) REPAIR_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_LEARN_REPAIR GLR,");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  WHERE");
		sql.append("  	GLR.IS_DELETED = 'N'");
		sql.append("  	AND GRR.IS_DELETED = 'N'");
		sql.append("  	AND GLR.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  	AND GRR.STUDENT_ID = GLR.STUDENT_ID");
		sql.append("  	AND GRR.TEACH_PLAN_ID = :TEACH_PLAN_ID ");
		sql.append("  	AND GRR.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GLR.XH = :XH ");

		params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("XH", ObjectUtils.toString(searchParams.get("XH")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询当前登录学员的开课学期
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getTermListByLoginStudent(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.KKXQ,");
		sql.append("  	TAB.TERM_ID,");
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
		sql.append("  	TAB.END_DATE,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < TAB.START_DATE_DT THEN '1'");
		sql.append("  			ELSE '0'");
		sql.append("  		END");
		sql.append("  	) IS_OPEN, ");

		sql.append("  	(CASE");
		sql.append("  	WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') >");
		sql.append("  	TO_CHAR(TAB.END_DATE_DT, 'yyyy-mm-dd') THEN");
		sql.append("  	'0'");
		sql.append("  	ELSE");
		sql.append("  	'1'");
		sql.append("  	END) IS_END");

		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT GG.GRADE_CODE TERM_CODE,");
		sql.append("  			GG.GRADE_ID TERM_ID,");
		sql.append("  			GG.GRADE_NAME TERM_NAME,");
		sql.append("  			TO_CHAR( GG.START_DATE, 'yyyy-MM-dd' ) START_DATE,");
		sql.append("  			TO_CHAR( NVL( GG.END_DATE, ADD_MONTHS(GG.START_DATE, 4 )), 'yyyy-MM-dd' ) END_DATE,");
		sql.append("  			GTP.KKXQ,");
		sql.append("  			GG.START_DATE START_DATE_DT,");
		sql.append("  			NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 )) END_DATE_DT ");
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
	 * 查询学员入学学期
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getEntranceTermByStu(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.NJ,");
		sql.append("  	GG.GRADE_ID,");
		sql.append("  	GG.GRADE_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI,");
		sql.append("  	GJT_GRADE GG");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GG.IS_DELETED = 'N'");
		sql.append("  	AND GSI.NJ = GG.GRADE_ID");
		sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");

		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

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

	/**
	 * 查询该学期下教学计划
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryTeachPlan(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.TERM_CODE,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	TAB.TERM_NAME,");
		sql.append("  	TAB.START_DATE,");
		sql.append("  	TAB.END_DATE,");
		sql.append("  	TAB.COURSE_START_DATE,");
		sql.append("  	TAB.COURSE_END_DATE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.TERMCOURSE_ID,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.WSJXZK,");
		sql.append("  	TAB.COURSE_CODE,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.SOURCE_COURSE_ID,");
		sql.append("  	TAB.SOURCE_KCH,");
		sql.append("  	TAB.SOURCE_KCMC,");
		sql.append("  	TAB.KCFM,");
		sql.append("  	TAB.COURSE_STYLE,");
		sql.append("  	TAB.COURSE_TYPE,");
		sql.append("  	TAB.PYCC,");
		sql.append("  	TAB.SDATE,");
		sql.append("  	TAB.EDATE,");
		sql.append("  	TAB.CREDIT,");
		sql.append("  	TAB.COUNSELOR,");
		sql.append("  	TAB.IS_ONLINE,");
		sql.append("  	TAB.CLASS_ID,");
		sql.append("  	TAB.KCXXBZ,");
		sql.append("  	TAB.KCKSBZ,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.CHOOSE_ID,");
		sql.append("  	TAB.EXAM_STATE,");
		sql.append("  	TAB.COURSE_SCHEDULE,");
		sql.append("  	TAB.EXAM_SCORE,");
		sql.append("  	TAB.EXAM_SCORE1,");
		sql.append("  	TAB.EXAM_SCORE2,");
		sql.append("  	TAB.SCHEDULE,");

		// 查询学习平台表：LCMS_USER_ONLINETIME，当前在学人数

		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT( DISTINCT LUO.USER_ID )");
		sql.append("  		FROM");
		sql.append("  			LCMS_USER_ONLINETIME LUO");
		sql.append("  		WHERE");
		sql.append("  			LUO.ISDELETED = 'N'");
		sql.append("  			AND LUO.IS_ONLINE = 'Y'");
		sql.append("  			AND(");
		sql.append("  				LUO.TERMCOURSE_ID = TAB.TERMCOURSE_ID");
		sql.append("  				OR LUO.TERMCOURSE_ID = TAB.TEACH_PLAN_ID");
		sql.append("  			)");
		sql.append("  			AND LUO.CLASS_ID = TAB.CLASS_ID");
		sql.append("  	) COUNTCOURSE,");

		// 课程是否已经启用，1启用,0停用
		sql.append("  	TAB.LEARN_STATUS, ");

		// 是否在课程教学周期时间内 0：否，1、2 ：是
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' )< TAB.COURSE_START_DATE THEN '0' ");
		sql.append(
				"  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' ) BETWEEN TAB.COURSE_START_DATE AND TAB.COURSE_END_DATE THEN '2' ");
		sql.append("  			ELSE '1' ");
		sql.append("  		END");
		sql.append("  	) IS_COURSE_STARTDATE, ");

		// 是否已经开课 0：未开课，1：已开课
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.TERMCOURSE_ID IS NULL");
		sql.append("  			OR TAB.TERMCOURSE_ID = '' THEN '0'");
		sql.append("  			ELSE '1'");
		sql.append("  		END");
		sql.append("  	) TERMCOURSE_ID_STATUS, ");

		sql.append("  	TAB.TEST_POINT ");

		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GTP.KKXQ TERM_CODE,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_ID");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  			) TERM_ID,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  			) TERM_NAME,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.START_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) START_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 )) FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) END_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_START_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_START_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_END_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_END_DATE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.START_DATE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) START_DATE_1,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 ))");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) END_DATE_1,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GRR.TERMCOURSE_ID,");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.WSJXZK,");
		sql.append("  			GC.KCH COURSE_CODE,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			GTP.SOURCE_COURSE_ID,");
		sql.append("  			GTP.SOURCE_KCH,");
		sql.append("  			GTP.SOURCE_KCMC,");
		sql.append("  			GC.KCFM,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					GTP.KCSX");
		sql.append("  					WHEN '0' THEN '必修'");
		sql.append("  					WHEN '1' THEN '选修'");
		sql.append("  					ELSE '补修'");
		sql.append("  				END");
		sql.append("  			) COURSE_STYLE,");
		sql.append("  			GTP.COURSE_TYPE,");
		sql.append("  			GTP.PYCC,");
		sql.append("  			GTP.SDATE,");
		sql.append("  			GTP.EDATE,");
		sql.append("  			NVL( GTP.XF, 0 ) AS CREDIT,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GUA.REAL_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI");
		sql.append("  				INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  				INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  					GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  				INNER JOIN GJT_USER_ACCOUNT GUA ON");
		sql.append("  					GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GEI.IS_DELETED = 'N'");
		sql.append("  					AND GUA.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			) COUNSELOR,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GUA.IS_ONLINE");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI");
		sql.append("  				INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  				INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  					GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  				INNER JOIN GJT_USER_ACCOUNT GUA ON");
		sql.append("  					GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GEI.IS_DELETED = 'N'");
		sql.append("  					AND GUA.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			) IS_ONLINE,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN GRR.TERMCOURSE_ID IS NOT NULL THEN(");
		sql.append("  						SELECT");
		sql.append("  							(");
		sql.append("  								SELECT");
		sql.append("  									GCI.CLASS_ID");
		sql.append("  								FROM");
		sql.append("  									GJT_CLASS_INFO GCI,");
		sql.append("  									GJT_CLASS_STUDENT GSI");
		sql.append("  								WHERE");
		sql.append("  									GCI.IS_DELETED = 'N'");
		sql.append("  									AND GSI.IS_DELETED = 'N'");
		sql.append("  									AND GCI.CLASS_ID = GSI.CLASS_ID");
		sql.append("  									AND GSI.STUDENT_ID = GRR1.STUDENT_ID");
		sql.append("  									AND GCI.TERMCOURSE_ID = GRR1.TERMCOURSE_ID");
		sql.append("  									AND GCI.CLASS_TYPE = 'course'");
		sql.append("  									AND ROWNUM = 1");
		sql.append("  							) CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_REC_RESULT GRR1");
		sql.append("  						WHERE");
		sql.append("  							GRR1.IS_DELETED = 'N'");
		sql.append("  							AND GRR1.TERMCOURSE_ID IS NOT NULL");
		sql.append("  							AND GRR1.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  							AND GRR1.COURSE_ID = GRR.COURSE_ID");
		sql.append("  							AND ROWNUM <= 1");
		sql.append("  					)");
		sql.append("  					ELSE(");
		sql.append("  						SELECT");
		sql.append("  							DISTINCT GCI.CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_CLASS_INFO GCI,");
		sql.append("  							GJT_CLASS_STUDENT GCS");
		sql.append("  						WHERE");
		sql.append("  							GCI.IS_DELETED = 'N'");
		sql.append("  							AND GCS.IS_DELETED = 'N'");
		sql.append("  							AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  							AND GCI.CLASS_TYPE = 'course'");
		sql.append("  							AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  							AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  							AND ROWNUM <= 1");
		sql.append("  					)");
		sql.append("  				END");
		sql.append("  			) CLASS_ID,");
		sql.append("  			GRR.COURSE_SCHEDULE AS KCXXBZ,");
		sql.append("  			100 - GRR.COURSE_SCHEDULE AS KCKSBZ,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.REC_ID CHOOSE_ID,");
		sql.append("  			GRR.EXAM_STATE,");
		sql.append("  			GRR.COURSE_SCHEDULE,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE ), DECODE( SUBSTR( GRR.EXAM_SCORE, 1, 1 ), '.', '0' || GRR.EXAM_SCORE, GRR.EXAM_SCORE ))) EXAM_SCORE,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE1 ), DECODE( SUBSTR( GRR.EXAM_SCORE1, 1, 1 ), '.', '0' || GRR.EXAM_SCORE1, GRR.EXAM_SCORE1 ))) EXAM_SCORE1,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE2 ), DECODE( SUBSTR( GRR.EXAM_SCORE2, 1, 1 ), '.', '0' || GRR.EXAM_SCORE2, GRR.EXAM_SCORE2 ))) EXAM_SCORE2,");
		// sql.append(" GSS.PROGRESS SCHEDULE,");

		// 查询学习平台LCMS_USER_DYNA的学习进度MY_PROGRESS
		sql.append("  			LUD.MY_PROGRESS SCHEDULE,");

		sql.append("  			GC.IS_ENABLED LEARN_STATUS,");

		// 同义词查询考试平台成绩
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( ETU.TEST_POINT )");
		sql.append("  				FROM");
		sql.append("  					EXAM_TEST_USER ETU");
		sql.append("  				WHERE");
		sql.append("  					ETU.IS_DELETED = 'N'");
		sql.append("  					AND ETU.USER_ID = GRR.REC_ID");
		sql.append("  			) TEST_POINT ");

		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("  	AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");

		/*
		 * 
		 * sql.append("  		LEFT JOIN GJT_STUDENT_STUDY_SITUATION GSS ON");
		 * sql.append("  			GSS.CHOOSE_ID = GRR.REC_ID");
		 */

		// 改为查学习平台LCMS_USER_DYNA的MY_PROGRESS

		sql.append("  		LEFT JOIN LCMS_USER_DYNA LUD ON");
		sql.append("  			LUD.ISDELETED = 'N'");
		sql.append("  			AND LUD.CHOOSE_ID = GRR.REC_ID ");

		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("TERM_ID"))) {
			sql.append("  	AND GTP.ACTUAL_GRADE_ID = :TERM_ID ");
			params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		}

		if ("1".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) { // 已通过
			sql.append("	AND GRR.EXAM_STATE = :EXAM_STATE ");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}

		if ("3".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
			sql.append("  	AND GRR.EXAM_STATE IN ('0','2','3','')");
		}

		sql.append("  		ORDER BY");
		sql.append("  			TERM_CODE");
		sql.append("  	) TAB");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 根据教学计划得到课程学习结果
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getCourseResult(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.TERM_CODE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.TERMCOURSE_ID,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	TAB.TERM_NAME,");
		sql.append("  	TAB.START_DATE,");
		sql.append("  	TAB.END_DATE,");
		sql.append("  	TAB.COURSE_START_DATE,");
		sql.append("  	TAB.COURSE_END_DATE,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.WSJXZK,");
		sql.append("  	TAB.COURSE_CODE,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.SOURCE_COURSE_ID,");
		sql.append("  	TAB.SOURCE_KCH,");
		sql.append("  	TAB.SOURCE_KCMC,");
		sql.append("  	TAB.KCFM,");
		sql.append("  	TAB.KCXXBZ,");
		sql.append("  	TAB.KCKSBZ,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.CHOOSE_ID,");
		sql.append("  	TAB.EXAM_STATE,");
		sql.append("  	TAB.COURSE_SCHEDULE,");
		sql.append("  	TAB.CREDIT,");
		sql.append("  	TAB.EXAM_SCORE,");
		sql.append("  	TAB.EXAM_SCORE1,");
		sql.append("  	TAB.EXAM_SCORE2,");
		sql.append("  	TAB.SCHEDULE,");

		// 从学习平台表LCMS_USER_DYNA取学习进度与成绩
		// sql.append(" TAB.LUD_SCHEDULE,"); /
		sql.append("  	TAB.MY_POINT,");

		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT( DISTINCT LUO.USER_ID )");
		sql.append("  		FROM");
		sql.append("  			LCMS_USER_ONLINETIME LUO");
		sql.append("  		WHERE");
		sql.append("  			LUO.ISDELETED = 'N'");
		sql.append("  			AND LUO.IS_ONLINE = 'Y'");
		sql.append("  			AND(");
		sql.append("  				LUO.TERMCOURSE_ID = TAB.TERMCOURSE_ID");
		sql.append("  				OR LUO.TERMCOURSE_ID = TAB.TEACH_PLAN_ID");
		sql.append("  			)");
		sql.append("  			AND LUO.CLASS_ID = TAB.CLASS_ID");
		sql.append("  	) COUNTCOURSE,");

		/*
		 * sql.append("  	("); sql.append("  		CASE");
		 * sql.append("  			WHEN SYSDATE < TAB.START_DATE_1 THEN '0'");
		 * sql.append("  			ELSE '1'"); sql.append("  		END");
		 * sql.append("  	) LEARN_STATUS,");
		 */

		sql.append("  	TAB.LEARN_STATUS,");

		// 是否在课程教学周期时间内 0：否，1、2 ：是
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' )< TAB.COURSE_START_DATE THEN '0'");
		sql.append(
				"  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' ) BETWEEN TAB.COURSE_START_DATE AND TAB.COURSE_END_DATE THEN '2'");
		sql.append("  			ELSE '1'");
		sql.append("  		END");
		sql.append("  	) IS_COURSE_STARTDATE,");

		// 是否已经开课 0：未开课，1：已开课
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.TERMCOURSE_ID IS NULL");
		sql.append("  			OR TAB.TERMCOURSE_ID = '' THEN '0'");
		sql.append("  			ELSE '1'");
		sql.append("  		END");
		sql.append("  	) TERMCOURSE_ID_STATUS, ");

		sql.append("  	TAB.TEST_POINT ");

		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GTP.KKXQ TERM_CODE,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GRR.TERMCOURSE_ID,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_ID");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  			) TERM_ID,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  			) TERM_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.START_DATE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) START_DATE_1,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 ))");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) END_DATE_2,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.START_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) START_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.END_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) END_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_START_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_START_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_END_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_END_DATE,");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.WSJXZK,");
		sql.append("  			GC.KCH COURSE_CODE,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			GTP.SOURCE_COURSE_ID,");
		sql.append("  			GTP.SOURCE_KCH,");
		sql.append("  			GTP.SOURCE_KCMC,");
		sql.append("  			GC.KCFM,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN GRR.TERMCOURSE_ID IS NOT NULL THEN(");
		sql.append("  						SELECT");
		sql.append("  							(");
		sql.append("  								SELECT");
		sql.append("  									GCI.CLASS_ID");
		sql.append("  								FROM");
		sql.append("  									GJT_CLASS_INFO GCI,");
		sql.append("  									GJT_CLASS_STUDENT GSI");
		sql.append("  								WHERE");
		sql.append("  									GCI.IS_DELETED = 'N'");
		sql.append("  									AND GSI.IS_DELETED = 'N'");
		sql.append("  									AND GCI.CLASS_ID = GSI.CLASS_ID");
		sql.append("  									AND GSI.STUDENT_ID = GRR1.STUDENT_ID");
		sql.append("  									AND GCI.TERMCOURSE_ID = GRR1.TERMCOURSE_ID");
		sql.append("  									AND GCI.CLASS_TYPE = 'course'");
		sql.append("  									AND ROWNUM = 1");
		sql.append("  							) CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_REC_RESULT GRR1");
		sql.append("  						WHERE");
		sql.append("  							GRR1.IS_DELETED = 'N'");
		sql.append("  							AND GRR1.TERMCOURSE_ID IS NOT NULL");
		sql.append("  							AND GRR1.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  							AND GRR1.COURSE_ID = GRR.COURSE_ID");
		sql.append("  							AND ROWNUM <= 1");
		sql.append("  					)");
		sql.append("  					ELSE(");
		sql.append("  						SELECT");
		sql.append("  							DISTINCT GCI.CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_CLASS_INFO GCI,");
		sql.append("  							GJT_CLASS_STUDENT GCS");
		sql.append("  						WHERE");
		sql.append("  							GCI.IS_DELETED = 'N'");
		sql.append("  							AND GCS.IS_DELETED = 'N'");
		sql.append("  							AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  							AND GCI.CLASS_TYPE = 'course'");
		sql.append("  							AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  							AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  							AND ROWNUM <= 1");
		sql.append("  					)");
		sql.append("  				END");
		sql.append("  			) CLASS_ID,");
		sql.append("  			GRR.COURSE_SCHEDULE AS KCXXBZ,");
		sql.append("  			100 - GRR.COURSE_SCHEDULE AS KCKSBZ,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.REC_ID CHOOSE_ID,");
		sql.append("  			GRR.EXAM_STATE,");
		sql.append("  			GRR.COURSE_SCHEDULE,");
		sql.append("  			NVL( GTP.XF, 0 ) AS CREDIT,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE ), DECODE( SUBSTR( GRR.EXAM_SCORE, 1, 1 ), '.', '0' || GRR.EXAM_SCORE, GRR.EXAM_SCORE ))) EXAM_SCORE,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE1 ), DECODE( SUBSTR( GRR.EXAM_SCORE1, 1, 1 ), '.', '0' || GRR.EXAM_SCORE1, GRR.EXAM_SCORE1 ))) EXAM_SCORE1,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE2 ), DECODE( SUBSTR( GRR.EXAM_SCORE2, 1, 1 ), '.', '0' || GRR.EXAM_SCORE2, GRR.EXAM_SCORE2 ))) EXAM_SCORE2,");
		// sql.append(" GSS.PROGRESS SCHEDULE,");
		sql.append("  			LUD.MY_PROGRESS SCHEDULE,");
		sql.append("  			LUD.MY_POINT,");
		sql.append("  			GC.IS_ENABLED LEARN_STATUS,");

		// 同义词查询考试平台成绩
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( ETU.TEST_POINT )");
		sql.append("  				FROM");
		sql.append("  					EXAM_TEST_USER ETU");
		sql.append("  				WHERE");
		sql.append("  					ETU.IS_DELETED = 'N'");
		sql.append("  					AND ETU.USER_ID = GRR.REC_ID");
		sql.append("  			) TEST_POINT ");

		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("  	AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");

		/*
		 * sql.append("  		LEFT JOIN GJT_STUDENT_STUDY_SITUATION GSS ON");
		 * sql.append("  			GSS.CHOOSE_ID = GRR.REC_ID");
		 */

		// 改为查学习平台LCMS_USER_DYNA的MY_PROGRESS(数据库已建同义词)

		sql.append("  		LEFT JOIN LCMS_USER_DYNA LUD ON");
		sql.append("  			LUD.ISDELETED = 'N'");
		sql.append("  			AND LUD.CHOOSE_ID = GRR.REC_ID");

		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if (!"1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))) {
			if (EmptyUtils.isNotEmpty(searchParams.get("TERM_ID"))) {
				sql.append("  	AND GTP.ACTUAL_GRADE_ID = :TERM_ID ");
				params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
			}
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("teachPlanIds"))) {
			String[] teachPlanIds = ObjectUtils.toString(searchParams.get("teachPlanIds")).split(",");
			if (teachPlanIds instanceof String[]) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i <= teachPlanIds.length - 1; i++) {
					buffer.append(teachPlanIds[i] + "','");
				}
				sql.append("	AND GTP.TEACH_PLAN_ID IN ('" + buffer.toString() + "')");
			} else {
				sql.append(" AND GTP.TEACH_PLAN_ID = :TEACH_PLAN_ID ");
				params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("teachPlanIds")));
			}
		}

		if ("2".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) { // 学习中
			sql.append("  	AND GRR.EXAM_STATE = :EXAM_STATE ");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}

		if ("1".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) { // 已通过
			sql.append("	AND GRR.EXAM_STATE = :EXAM_STATE ");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}

		if ("0".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) { // 未通过
			sql.append("	AND GRR.EXAM_STATE = :EXAM_STATE ");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			TERM_CODE");
		sql.append("  	) TAB");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询院校模式下课程
	 * 
	 * @param searchParams
	 * @return
	 */
	public List acadeMyLearnCourse(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.KKXQ,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	TAB.TERM_NAME,");
		sql.append("  	TAB.START_DATE,");
		sql.append("  	TAB.END_DATE,");
		sql.append("  	TAB.COURSE_START_DATE,");
		sql.append("  	TAB.COURSE_END_DATE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.TERMCOURSE_ID,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.WSJXZK,");
		sql.append("  	TAB.COURSE_CODE,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.COURSE_CATEGORY,");
		sql.append("  	TAB.SOURCE_COURSE_ID,");
		sql.append("  	TAB.SOURCE_KCH,");
		sql.append("  	TAB.SOURCE_KCMC,");
		sql.append("  	TAB.KCFM,");
		sql.append("  	TAB.COURSE_STYLE,");
		sql.append("  	TAB.COURSE_TYPE,");
		sql.append("  	TAB.CREDIT,");
		sql.append("  	TAB.COUNSELOR,");
		sql.append("  	TAB.IS_ONLINE,");
		sql.append("  	TAB.CLASS_ID,");
		sql.append("  	TAB.KCXXBZ,");
		sql.append("  	TAB.KCKSBZ,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.CHOOSE_ID,");
		sql.append("  	TAB.EXAM_STATE,");
		sql.append("  	TAB.COURSE_SCHEDULE,");
		// sql.append(" TAB.EXAM_SCORE,");
		// sql.append(" TAB.EXAM_SCORE1,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.EXAM_STATE = '2' THEN TO_CHAR( TAB.MY_POINT )");
		sql.append("  			ELSE TAB.EXAM_SCORE");
		sql.append("  		END");
		sql.append("  	) EXAM_SCORE,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.EXAM_STATE = '2' THEN TO_CHAR( TAB.TEST_POINT )");
		sql.append("  			ELSE TAB.EXAM_SCORE1");
		sql.append("  		END");
		sql.append("  	) EXAM_SCORE1,");
		sql.append("  	TAB.EXAM_SCORE2,");
		sql.append("  	TAB.SCHEDULE,");
		sql.append("  	TAB.MY_POINT,");

		// 增加查询当前在学课程人数(从学习平台取数据，数据库已建立同义词)

		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT( DISTINCT LUO.USER_ID )");
		sql.append("  		FROM");
		sql.append("  			LCMS_USER_ONLINETIME LUO");
		sql.append("  		WHERE");
		sql.append("  			LUO.ISDELETED = 'N'");
		sql.append("  			AND LUO.IS_ONLINE = 'Y'");
		sql.append("  			AND(");
		sql.append("  				LUO.TERMCOURSE_ID = TAB.TERMCOURSE_ID");
		sql.append("  				OR LUO.TERMCOURSE_ID = TAB.TEACH_PLAN_ID");
		sql.append("  			)");
		sql.append("  			AND LUO.CLASS_ID = TAB.CLASS_ID");
		sql.append("  	) COUNTCOURSE,");

		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < TAB.START_DATE THEN '0'");
		sql.append("  			ELSE '1'");
		sql.append("  		END");
		sql.append("  	) LEARN_STATUS, ");

		// 是否在课程教学周期时间内 0：否，1、2 ：是
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' )< TAB.COURSE_START_DATE THEN '0' ");
		sql.append(
				"  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' ) BETWEEN TAB.COURSE_START_DATE AND TAB.COURSE_END_DATE THEN '2' ");
		sql.append("  			ELSE '1' ");
		sql.append("  		END");
		sql.append("  	) IS_COURSE_STARTDATE, ");

		// 是否已经开课 0：未开课，1：已开课
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.TERMCOURSE_ID IS NULL");
		sql.append("  			OR TAB.TERMCOURSE_ID = '' THEN '0'");
		sql.append("  			ELSE '1'");
		sql.append("  		END");
		sql.append("  	) TERMCOURSE_ID_STATUS, ");
		sql.append("  	TAB.TEST_POINT, ");

		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < TAB.START_DATE THEN '1'");
		sql.append("  			ELSE '0'");
		sql.append("  		END");
		sql.append("  	) IS_OPEN ");

		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GTP.KKXQ,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_ID");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) TERM_ID,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) TERM_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.START_DATE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) START_DATE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 ))");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) END_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_START_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_START_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_END_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_END_DATE,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GRR.TERMCOURSE_ID,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.WSJXZK,");
		sql.append("  			GC.KCH COURSE_CODE,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			GC.COURSE_CATEGORY,");
		sql.append("  			GTP.SOURCE_COURSE_ID,");
		sql.append("  			GTP.SOURCE_KCH,");
		sql.append("  			GTP.SOURCE_KCMC,");
		sql.append("  			GC.KCFM,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					GTP.KCSX");
		sql.append("  					WHEN '0' THEN '必修'");
		sql.append("  					WHEN '1' THEN '选修'");
		sql.append("  					ELSE '补修'");
		sql.append("  				END");
		sql.append("  			) COURSE_STYLE,");
		sql.append("  			GTP.COURSE_TYPE,");
		sql.append("  			NVL( GTP.XF, 0 ) AS CREDIT,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GUA.REAL_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI");
		sql.append("  				INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  				INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  					GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  				INNER JOIN GJT_USER_ACCOUNT GUA ON");
		sql.append("  					GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GEI.IS_DELETED = 'N'");
		sql.append("  					AND GUA.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			) COUNSELOR,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GUA.IS_ONLINE");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI");
		sql.append("  				INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  				INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  					GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  				INNER JOIN GJT_USER_ACCOUNT GUA ON");
		sql.append("  					GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GEI.IS_DELETED = 'N'");
		sql.append("  					AND GUA.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			) IS_ONLINE,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN GRR.TERMCOURSE_ID IS NOT NULL THEN(");
		sql.append("  						SELECT");
		sql.append("  							(");
		sql.append("  								SELECT");
		sql.append("  									GCI.CLASS_ID");
		sql.append("  								FROM");
		sql.append("  									GJT_CLASS_INFO GCI,");
		sql.append("  									GJT_CLASS_STUDENT GSI");
		sql.append("  								WHERE");
		sql.append("  									GCI.IS_DELETED = 'N'");
		sql.append("  									AND GSI.IS_DELETED = 'N'");
		sql.append("  									AND GCI.CLASS_ID = GSI.CLASS_ID");
		sql.append("  									AND GSI.STUDENT_ID = GRR1.STUDENT_ID");
		sql.append("  									AND GCI.TERMCOURSE_ID = GRR1.TERMCOURSE_ID");
		sql.append("  									AND GCI.CLASS_TYPE = 'course'");
		sql.append("  									AND ROWNUM = 1");
		sql.append("  							) CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_REC_RESULT GRR1");
		sql.append("  						WHERE");
		sql.append("  							GRR1.IS_DELETED = 'N'");
		sql.append("  							AND GRR1.TERMCOURSE_ID IS NOT NULL");
		sql.append("  							AND GRR1.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  							AND GRR1.COURSE_ID = GRR.COURSE_ID");
		sql.append("  							AND ROWNUM <= 1");
		sql.append("  					)");
		sql.append("  					ELSE(");
		sql.append("  						SELECT");
		sql.append("  							DISTINCT GCI.CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_CLASS_INFO GCI,");
		sql.append("  							GJT_CLASS_STUDENT GCS");
		sql.append("  						WHERE");
		sql.append("  							GCI.IS_DELETED = 'N'");
		sql.append("  							AND GCS.IS_DELETED = 'N'");
		sql.append("  							AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  							AND GCI.CLASS_TYPE = 'course'");
		sql.append("  							AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  							AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  							AND ROWNUM <= 1");
		sql.append("  					)");
		sql.append("  				END");
		sql.append("  			) CLASS_ID,");
		sql.append("  			GRR.COURSE_SCHEDULE AS KCXXBZ,");
		sql.append("  			100 - GRR.COURSE_SCHEDULE AS KCKSBZ,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.REC_ID CHOOSE_ID,");
		sql.append("  			GRR.EXAM_STATE,");
		sql.append("  			GRR.COURSE_SCHEDULE,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE ), DECODE( SUBSTR( GRR.EXAM_SCORE, 1, 1 ), '.', '0' || GRR.EXAM_SCORE, GRR.EXAM_SCORE ))) EXAM_SCORE,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE1 ), DECODE( SUBSTR( GRR.EXAM_SCORE1, 1, 1 ), '.', '0' || GRR.EXAM_SCORE1, GRR.EXAM_SCORE1 ))) EXAM_SCORE1,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE2 ), DECODE( SUBSTR( GRR.EXAM_SCORE2, 1, 1 ), '.', '0' || GRR.EXAM_SCORE2, GRR.EXAM_SCORE2 ))) EXAM_SCORE2,");
		// sql.append(" GSS.PROGRESS SCHEDULE,");
		// 查询学习平台LCMS_USER_DYNA的学习进度MY_PROGRESS
		sql.append("  			LUD.MY_PROGRESS SCHEDULE,");
		sql.append("  			LUD.MY_POINT,");
		sql.append("  			GC.IS_ENABLED LEARN_STATUS,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( ETU.TEST_POINT )");
		sql.append("  				FROM");
		sql.append("  					EXAM_TEST_USER ETU");
		sql.append("  				WHERE");
		sql.append("  					ETU.IS_DELETED = 'N'");
		sql.append("  					AND ETU.USER_ID = GRR.REC_ID");
		sql.append("  			) TEST_POINT ");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");

		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("  	AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");

		/*
		 * sql.append("  		LEFT JOIN GJT_STUDENT_STUDY_SITUATION GSS ON");
		 * sql.append("  			GSS.CHOOSE_ID = GRR.REC_ID");
		 */

		// 改为查学习平台LCMS_USER_DYNA的MY_PROGRESS

		sql.append("  		LEFT JOIN LCMS_USER_DYNA LUD ON");
		sql.append("  			LUD.ISDELETED = 'N'");
		sql.append("  			AND LUD.CHOOSE_ID = GRR.REC_ID ");

		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))) {
			sql.append("  	AND GTP.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("EXAM_STATE"))) {
			sql.append("	AND GRR.EXAM_STATE = :EXAM_STATE");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			GTP.KKXQ");
		sql.append("  	) TAB");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 课程学习(无考试模式)
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getCourseLearningByNoExam(Map searchParams) {
		Map params = new LinkedHashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.KKXQ,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	TAB.TERM_NAME,");
		sql.append("  	TO_CHAR(TAB.START_DATE,'yyyy-MM-dd') START_DATE,");
		sql.append("  	TO_CHAR(TAB.END_DATE,'yyyy-MM-dd') END_DATE,");
		sql.append("  	TAB.COURSE_START_DATE,");
		sql.append("  	TAB.COURSE_END_DATE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.TERMCOURSE_ID,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.WSJXZK,");
		sql.append("  	TAB.COURSE_CODE,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.COURSE_CATEGORY,");
		sql.append("  	TAB.SOURCE_COURSE_ID,");
		sql.append("  	TAB.SOURCE_KCH,");
		sql.append("  	TAB.SOURCE_KCMC,");
		sql.append("  	TAB.KCFM,");
		sql.append("  	TAB.COURSE_STYLE,");
		sql.append("  	TAB.COURSE_TYPE,");
		sql.append("  	TAB.CREDIT,");
		sql.append("  	TAB.COUNSELOR,");
		sql.append("  	TAB.IS_ONLINE,");
		sql.append("  	TAB.CLASS_ID,");
		sql.append("  	TAB.KCXXBZ,");
		sql.append("  	TAB.KCKSBZ,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.CHOOSE_ID,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < TAB.START_DATE THEN '4'");
		sql.append("  			ELSE TO_CHAR(TAB.EXAM_STATE)");
		sql.append("  		END");
		sql.append("  	) LEARN_STATE,");
		sql.append("  	TAB.COURSE_SCHEDULE,");
		sql.append("  	TAB.EXAM_SCORE,");
		sql.append("  	TAB.SCHEDULE,");
		sql.append("  	NVL(TAB.STATE,'--') STATE,");

		// 增加查询当前在学课程人数(从学习平台取数据，数据库已建立同义词)
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT( DISTINCT LUO.USER_ID )");
		sql.append("  		FROM");
		sql.append("  			LCMS_USER_ONLINETIME LUO");
		sql.append("  		WHERE");
		sql.append("  			LUO.ISDELETED = 'N'");
		sql.append("  			AND LUO.IS_ONLINE = 'Y'");
		sql.append("  			AND(");
		sql.append("  				LUO.TERMCOURSE_ID = TAB.TERMCOURSE_ID");
		sql.append("  				OR LUO.TERMCOURSE_ID = TAB.TEACH_PLAN_ID");
		sql.append("  			)");
		sql.append("  			AND LUO.CLASS_ID = TAB.CLASS_ID");
		sql.append("  	) COUNTCOURSE,");

		// 课程是否已经启用，1启用,0停用
		sql.append("  	TAB.LEARN_STATUS, ");

		// 是否在课程教学周期时间内 0：否，1、2 ：是
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' )< TAB.COURSE_START_DATE THEN '0' ");
		sql.append(
				"  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' ) BETWEEN TAB.COURSE_START_DATE AND TAB.COURSE_END_DATE THEN '2' ");
		sql.append("  			ELSE '1' ");
		sql.append("  		END");
		sql.append("  	) IS_COURSE_STARTDATE, ");

		// 是否已经开课 0：未开课，1：已开课
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.TERMCOURSE_ID IS NULL");
		sql.append("  			OR TAB.TERMCOURSE_ID = '' THEN '0'");
		sql.append("  			ELSE '1'");
		sql.append("  		END");
		sql.append("  	) TERMCOURSE_ID_STATUS, ");

		// 学期时间是否开放：0开放,1时间未到，未开放
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < TAB.START_DATE THEN '1'");
		sql.append("  			ELSE '0'");
		sql.append("  		END");
		sql.append("  	) IS_OPEN ");

		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GTP.KKXQ,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_ID");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) TERM_ID,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) TERM_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.START_DATE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) START_DATE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 ))");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) END_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_START_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_START_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_END_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_END_DATE,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GRR.TERMCOURSE_ID,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.WSJXZK,");
		sql.append("  			GC.KCH COURSE_CODE,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			GC.COURSE_CATEGORY,");
		sql.append("  			GTP.SOURCE_COURSE_ID,");
		sql.append("  			GTP.SOURCE_KCH,");
		sql.append("  			GTP.SOURCE_KCMC,");
		sql.append("  			GC.KCFM,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					GTP.KCSX");
		sql.append("  					WHEN '0' THEN '必修'");
		sql.append("  					WHEN '1' THEN '选修'");
		sql.append("  					ELSE '补修'");
		sql.append("  				END");
		sql.append("  			) COURSE_STYLE,");
		sql.append("  			GTP.COURSE_TYPE,");
		sql.append("  			NVL( GTP.XF, 0 ) AS CREDIT,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GUA.REAL_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI");
		sql.append("  				INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  				INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  					GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  				INNER JOIN GJT_USER_ACCOUNT GUA ON");
		sql.append("  					GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GEI.IS_DELETED = 'N'");
		sql.append("  					AND GUA.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			) COUNSELOR,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GUA.IS_ONLINE");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI");
		sql.append("  				INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  				INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  					GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  				INNER JOIN GJT_USER_ACCOUNT GUA ON");
		sql.append("  					GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GEI.IS_DELETED = 'N'");
		sql.append("  					AND GUA.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			) IS_ONLINE,");
		/*
		 * sql.append("  	        ("); sql.append("  		        SELECT");
		 * sql.append("  			        DISTINCT GCI.CLASS_ID");
		 * sql.append("  		        FROM");
		 * sql.append("  			        GJT_CLASS_INFO GCI,");
		 * sql.append("  			        GJT_CLASS_STUDENT GCS");
		 * sql.append("  		        WHERE");
		 * sql.append("  			        GCI.IS_DELETED = 'N'");
		 * sql.append("  			        AND GCS.IS_DELETED = 'N'");
		 * sql.append("  			        AND GCI.CLASS_ID = GCS.CLASS_ID");
		 * sql.append("  			        AND GCI.CLASS_TYPE = 'course'");
		 * sql.append("  			        AND GCS.STUDENT_ID = GRR.STUDENT_ID"
		 * );
		 * sql.append("  			        AND GCI.COURSE_ID = GRR.COURSE_ID");
		 * sql.append("  			        AND ROWNUM <= 1 ");
		 * sql.append("  	        ) CLASS_ID,");
		 */
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN GRR.TERMCOURSE_ID IS NOT NULL THEN(");
		sql.append("  				SELECT");
		sql.append("  					(");
		sql.append("  						SELECT");
		sql.append("  							GCI.CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_CLASS_INFO GCI,");
		sql.append("  							GJT_CLASS_STUDENT GSI");
		sql.append("  						WHERE");
		sql.append("  							GCI.IS_DELETED = 'N'");
		sql.append("  							AND GSI.IS_DELETED = 'N'");
		sql.append("  							AND GCI.CLASS_ID = GSI.CLASS_ID");
		sql.append("  							AND GSI.STUDENT_ID = GRR1.STUDENT_ID");
		sql.append("  							AND GCI.TERMCOURSE_ID = GRR1.TERMCOURSE_ID");
		sql.append("  							AND GCI.CLASS_TYPE = 'course'");
		sql.append("  							AND ROWNUM = 1");
		sql.append("  					) CLASS_ID");
		sql.append("  				FROM");
		sql.append("  					GJT_REC_RESULT GRR1");
		sql.append("  				WHERE");
		sql.append("  					GRR1.IS_DELETED = 'N'");
		sql.append("  					AND GRR1.TERMCOURSE_ID IS NOT NULL");
		sql.append("  					AND GRR1.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GRR1.COURSE_ID = GRR.COURSE_ID");
		sql.append("  			)");
		sql.append("  			ELSE(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GCI.CLASS_ID");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI,");
		sql.append("  					GJT_CLASS_STUDENT GCS");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			)");
		sql.append("  		END");
		sql.append("  	) CLASS_ID,");
		sql.append("  			GRR.COURSE_SCHEDULE AS KCXXBZ,");
		sql.append("  			100 - GRR.COURSE_SCHEDULE AS KCKSBZ,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.REC_ID CHOOSE_ID,");
		sql.append("  			GRR.EXAM_STATE,");
		sql.append("  			GRR.COURSE_SCHEDULE,");
		// sql.append(" GSS.SCORE EXAM_SCORE,");
		sql.append("  			LUD.MY_POINT EXAM_SCORE,");
		// sql.append(" GSS.PROGRESS SCHEDULE,");

		sql.append("  			LUD.MY_PROGRESS SCHEDULE,");

		// sql.append(" GSS.STATE,");
		sql.append("  			LUD.STATE,");
		sql.append("  			GC.IS_OPEN LEARN_STATUS");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");

		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("  			AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");

		/*
		 * sql.
		 * append("  		LEFT JOIN VIEW_STUDENT_STUDY_SITUATIONVIEW_STUDENT_STUDY_SITUATION GSS ON"
		 * ); sql.append("  			GSS.CHOOSE_ID = GRR.REC_ID");
		 */

		sql.append("  		LEFT JOIN LCMS_USER_DYNA LUD ON");
		sql.append("  			LUD.ISDELETED = 'N'");
		sql.append("  			AND LUD.CHOOSE_ID = GRR.REC_ID");

		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))) {
			sql.append("  			AND GTP.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			GTP.KKXQ");
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		if (EmptyUtils.isNotEmpty(searchParams.get("CHOOSE_ID"))) {
			sql.append("  	AND TAB.CHOOSE_ID = :CHOOSE_ID ");
			params.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查看授课凭证
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getGrantCourseCert(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GCC.ID,");
		sql.append("  	GCC.TEACH_PLAN_ID,");
		sql.append("  	GGCP.ID GRANT_ID,");
		sql.append("  	GGCP.COURSE_THEME,");
		sql.append("  	GGCP.ADDR,");
		sql.append("  	TO_CHAR(GGCP.START_DATE,'yyyy-MM-dd hh24:mi:ss') START_DATE,");
		sql.append("  	TO_CHAR(GGCP.ENT_DATE,'yyyy-MM-dd hh24:mi:ss') ENT_DATE,");
		sql.append("  	GGCP.TEACHER,");
		sql.append("  	GGCP.CERTIFICATE_STATUS,");
		sql.append("  	GGCC.STUDENT_NAME,");
		sql.append("  	GGCC.STUDENT_NO,");
		sql.append("  	GGCC.SING_STATUS,");
		sql.append("  	GGCPI.IMAGE");
		sql.append("  FROM");
		sql.append("  	GJT_CUSTOM_COURSE GCC,");
		sql.append("  	GJT_GRANT_COURSE_PLAN GGCP ");
		sql.append("  LEFT JOIN GJT_GRANT_COURSE_CERTIFICATE GGCC ON ");
		sql.append("  	GGCP.ID = GGCC.GRANT_COURSE_PLAN_ID ");
		sql.append("  	AND GGCC.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_GRANT_COURSE_PLAN_IMAGE GGCPI ON ");
		sql.append("  	GGCC.GRANT_COURSE_PLAN_ID = GGCPI.GRANT_COURSE_PLAN_ID ");
		sql.append("  WHERE");
		sql.append("  	GCC.IS_DELETED = 'N' ");
		sql.append("  	AND GGCP.IS_DELETED = 'N' ");
		sql.append("  	AND GCC.ID = GGCP.CUSTOM_COURSE_ID ");
		sql.append("  	AND GCC.TEACH_PLAN_ID = :TEACH_PLAN_ID ");

		params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询授课计划
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getGrantCousePlan(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GCC.CUSTOM_COURSE_ID,");
		sql.append("  	GCC.TEACH_PLAN_ID,");
		sql.append("  	GGCP.GRANT_COURSE_PLAN_ID GRANT_ID,");
		sql.append("  	GGCP.COURSE_THEME,");
		sql.append("  	GGCP.ADDR,");
		sql.append("  	TO_CHAR( GGCP.START_DATE, 'yyyy-MM-dd hh24:mi:ss' ) START_DATE,");
		sql.append("  	TO_CHAR( GGCP.ENT_DATE, 'yyyy-MM-dd hh24:mi:ss' ) ENT_DATE,");
		sql.append("  	GGCP.TEACHER,");
		sql.append("  	GGCP.CERTIFICATE_STATUS ");
		sql.append("  FROM");
		sql.append("  	GJT_CUSTOM_COURSE GCC,");
		sql.append("  	GJT_GRANT_COURSE_PLAN GGCP");
		sql.append("  WHERE");
		sql.append("  	GCC.IS_DELETED = 'N'");
		sql.append("  	AND GGCP.IS_DELETED = 'N'");
		sql.append("  	AND GCC.CUSTOM_COURSE_ID = GGCP.CUSTOM_COURSE_ID");

		if (EmptyUtils.isNotEmpty(searchParams.get("TEACH_PLAN_ID"))) {
			sql.append("  	AND GCC.TEACH_PLAN_ID = :TEACH_PLAN_ID ");
			params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_ID"))) {
			sql.append("  	AND GCC.ORG_ID = :XXZX_ID ");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询授课凭证
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getGrantCourseCertData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GGCP.GRANT_COURSE_PLAN_ID GRANT_ID,");
		sql.append("  	GGCC.STUDENT_NAME,");
		sql.append("  	GGCC.STUDENT_NO,");
		sql.append("  	GGCC.SING_STATUS ");
		sql.append("  FROM");
		sql.append("  	GJT_GRANT_COURSE_PLAN GGCP");
		sql.append("  LEFT JOIN GJT_GRANT_COURSE_CERTIFICATE GGCC ON");
		sql.append("  	GGCP.GRANT_COURSE_PLAN_ID = GGCC.GRANT_COURSE_PLAN_ID ");
		sql.append("  	AND GGCC.IS_DELETED = 'N'");
		sql.append("  WHERE");
		sql.append("  	GGCP.IS_DELETED = 'N'");
		sql.append("  	AND GGCP.GRANT_COURSE_PLAN_ID = :GRANT_ID ");

		params.put("GRANT_ID", ObjectUtils.toString(searchParams.get("GRANT_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询扫描件
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getGrantCourseCertImage(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GGCPI.GRANT_COURSE_PLAN_ID,");
		sql.append("  	GGCPI.IMAGE ");
		sql.append("  FROM");
		sql.append("  	GJT_GRANT_COURSE_PLAN_IMAGE GGCPI ");
		sql.append("  WHERE");
		sql.append("  	GGCPI.GRANT_COURSE_PLAN_ID = :GRANT_ID ");

		params.put("GRANT_ID", ObjectUtils.toString(searchParams.get("GRANT_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 课程学习
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getCourseLearningData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.TERM_CODE,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	TAB.TERM_NAME,");
		sql.append("  	TAB.START_DATE,");
		sql.append("  	TAB.END_DATE,");
		sql.append("  	TAB.COURSE_START_DATE,");
		sql.append("  	TAB.COURSE_END_DATE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.TERMCOURSE_ID,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.WSJXZK,");
		sql.append("  	TAB.COURSE_CODE,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.COURSE_CATEGORY,");
		sql.append("  	TAB.SOURCE_COURSE_ID,");
		sql.append("  	TAB.SOURCE_KCH,");
		sql.append("  	TAB.SOURCE_KCMC,");
		sql.append("  	TAB.KCFM,");
		sql.append("  	TAB.COURSE_STYLE,");
		sql.append("  	TAB.COURSE_TYPE,");
		sql.append("  	TAB.PYCC,");
		sql.append("  	TAB.SDATE,");
		sql.append("  	TAB.EDATE,");
		sql.append("  	TAB.CREDIT,");
		sql.append("  	TAB.COUNSELOR,");
		sql.append("  	TAB.IS_ONLINE,");
		sql.append("  	TAB.CLASS_ID,");
		sql.append("  	TAB.KCXXBZ,");
		sql.append("  	TAB.KCKSBZ,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.CHOOSE_ID,");
		sql.append("  	TAB.EXAM_STATE,");
		sql.append("  	TAB.EXAM_SCORE STUDY_SCORE,");
		sql.append("  	TAB.PROGRESS,");
		sql.append("  	TAB.COURSE_SCHEDULE,");
		sql.append("  	TAB.GUIDE_PATH,");
		sql.append("  	TAB.LEARNING_STYLE,");
		// sql.append(" TAB.EXAM_SCORE,");
		// sql.append(" TAB.EXAM_SCORE1,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.EXAM_STATE = '2' THEN RTRIM( TO_CHAR( TAB.MY_POINT, 'fm9990.99' ), '.' ) ");
		sql.append("  			ELSE TAB.EXAM_SCORE ");
		sql.append("  		END");
		sql.append("  	) EXAM_SCORE,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.EXAM_STATE = '2' THEN RTRIM( TO_CHAR( TAB.TEST_POINT, 'fm9990.99' ), '.' ) ");
		sql.append("  			ELSE TAB.EXAM_SCORE1 ");
		sql.append("  		END");
		sql.append("  	) EXAM_SCORE1,");
		sql.append("  	TAB.EXAM_SCORE2,");
		sql.append("  	TAB.SCHEDULE,");
		sql.append("  	TAB.MY_POINT,");

		// 查询学习平台表：LCMS_USER_ONLINETIME，当前在学人数
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT( DISTINCT LUO.USER_ID )");
		sql.append("  		FROM");
		sql.append("  			LCMS_USER_ONLINETIME LUO");
		sql.append("  		WHERE");
		sql.append("  			LUO.ISDELETED = 'N'");
		sql.append("  			AND LUO.IS_ONLINE = 'Y'");
		sql.append("  			AND(");
		sql.append("  				LUO.TERMCOURSE_ID = TAB.TERMCOURSE_ID");
		sql.append("  				OR LUO.TERMCOURSE_ID = TAB.TEACH_PLAN_ID");
		sql.append("  			)");
		sql.append("  			AND LUO.CLASS_ID = TAB.CLASS_ID");
		sql.append("  	) COUNTCOURSE,");

		// 课程是否已经启用，1启用,0停用
		sql.append("  	TAB.LEARN_STATUS, ");

		// 是否在课程教学周期时间内 0：否，1、2 ：是
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' )< TAB.COURSE_START_DATE THEN '0' ");
		sql.append(
				"  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' ) BETWEEN TAB.COURSE_START_DATE AND TAB.COURSE_END_DATE THEN '2' ");
		sql.append("  			ELSE '1' ");
		sql.append("  		END");
		sql.append("  	) IS_COURSE_STARTDATE, ");

		// 是否已经开课 0：未开课，1：已开课
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.TERMCOURSE_ID IS NULL");
		sql.append("  			OR TAB.TERMCOURSE_ID = '' THEN '0'");
		sql.append("  			ELSE '1'");
		sql.append("  		END");
		sql.append("  	) TERMCOURSE_ID_STATUS, ");

		sql.append("  	TAB.TEST_POINT, ");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd' )< TAB.START_DATE THEN '1'");
		sql.append("  			ELSE '0'");
		sql.append("  		END");
		sql.append("  	) IS_OPEN ");

		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GTP.KKXQ TERM_CODE,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_ID");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  			) TERM_ID,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  			) TERM_NAME,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.START_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) START_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 )) FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) END_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_START_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_START_DATE,");
		sql.append(
				"  			TO_CHAR(( SELECT GG.COURSE_END_DATE FROM GJT_GRADE GG WHERE GG.GRADE_ID = GTP.ACTUAL_GRADE_ID AND GG.IS_DELETED = 'N' ), 'yyyy-MM-dd' ) COURSE_END_DATE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GG.START_DATE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) START_DATE_1,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 ))");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) END_DATE_1,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GRR.TERMCOURSE_ID,");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN GC.WSJXZK = '1' THEN '1'");
		sql.append("  					WHEN GC.WSJXZK = '0' THEN '0'");
		sql.append("  					ELSE '1'");
		sql.append("  				END");
		sql.append("  			) WSJXZK,");
		sql.append("  			GC.KCH COURSE_CODE,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			GC.COURSE_CATEGORY,");
		sql.append("  			GC.GUIDE_PATH,");
		sql.append("  			GTP.LEARNING_STYLE,");
		sql.append("  			GTP.SOURCE_COURSE_ID,");
		sql.append("  			GTP.SOURCE_KCH,");
		sql.append("  			GTP.SOURCE_KCMC,");
		sql.append("  			GC.KCFM,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					GTP.KCSX");
		sql.append("  					WHEN '0' THEN '必修'");
		sql.append("  					WHEN '1' THEN '选修'");
		sql.append("  					ELSE '补修'");
		sql.append("  				END");
		sql.append("  			) COURSE_STYLE,");
		sql.append("  			GTP.COURSE_TYPE,");
		sql.append("  			GTP.PYCC,");
		sql.append("  			GTP.SDATE,");
		sql.append("  			GTP.EDATE,");
		sql.append("  			NVL( GTP.XF, 0 ) AS CREDIT,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GUA.REAL_NAME");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI");
		sql.append("  				INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  				INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  					GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  				INNER JOIN GJT_USER_ACCOUNT GUA ON");
		sql.append("  					GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GEI.IS_DELETED = 'N'");
		sql.append("  					AND GUA.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			) COUNSELOR,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					DISTINCT GUA.IS_ONLINE");
		sql.append("  				FROM");
		sql.append("  					GJT_CLASS_INFO GCI");
		sql.append("  				INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  				INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  					GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  				INNER JOIN GJT_USER_ACCOUNT GUA ON");
		sql.append("  					GEI.ACCOUNT_ID = GUA.ID");
		sql.append("  				WHERE");
		sql.append("  					GCI.IS_DELETED = 'N'");
		sql.append("  					AND GCS.IS_DELETED = 'N'");
		sql.append("  					AND GEI.IS_DELETED = 'N'");
		sql.append("  					AND GUA.IS_DELETED = 'N'");
		sql.append("  					AND GCI.CLASS_TYPE = 'course'");
		sql.append("  					AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  					AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  					AND ROWNUM <= 1");
		sql.append("  			) IS_ONLINE,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN GRR.TERMCOURSE_ID IS NOT NULL THEN(");
		sql.append("  						SELECT");
		sql.append("  							(");
		sql.append("  								SELECT");
		sql.append("  									GCI.CLASS_ID");
		sql.append("  								FROM");
		sql.append("  									GJT_CLASS_INFO GCI,");
		sql.append("  									GJT_CLASS_STUDENT GSI");
		sql.append("  								WHERE");
		sql.append("  									GCI.IS_DELETED = 'N'");
		sql.append("  									AND GSI.IS_DELETED = 'N'");
		sql.append("  									AND GCI.CLASS_ID = GSI.CLASS_ID");
		sql.append("  									AND GSI.STUDENT_ID = GRR1.STUDENT_ID");
		sql.append("  									AND GCI.TERMCOURSE_ID = GRR1.TERMCOURSE_ID");
		sql.append("  									AND GCI.CLASS_TYPE = 'course'");
		sql.append("  									AND ROWNUM = 1");
		sql.append("  							) CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_REC_RESULT GRR1");
		sql.append("  						WHERE");
		sql.append("  							GRR1.IS_DELETED = 'N'");
		sql.append("  							AND GRR1.TERMCOURSE_ID IS NOT NULL");
		sql.append("  							AND GRR1.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  							AND GRR1.COURSE_ID = GRR.COURSE_ID");
		sql.append("  							AND ROWNUM <= 1");
		sql.append("  					)");
		sql.append("  					ELSE(");
		sql.append("  						SELECT");
		sql.append("  							DISTINCT GCI.CLASS_ID");
		sql.append("  						FROM");
		sql.append("  							GJT_CLASS_INFO GCI,");
		sql.append("  							GJT_CLASS_STUDENT GCS");
		sql.append("  						WHERE");
		sql.append("  							GCI.IS_DELETED = 'N'");
		sql.append("  							AND GCS.IS_DELETED = 'N'");
		sql.append("  							AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  							AND GCI.CLASS_TYPE = 'course'");
		sql.append("  							AND GCS.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  							AND GCI.COURSE_ID = GRR.COURSE_ID");
		sql.append("  							AND ROWNUM <= 1");
		sql.append("  					)");
		sql.append("  				END");
		sql.append("  			) CLASS_ID,");
		sql.append("  			GRR.COURSE_SCHEDULE AS KCXXBZ,");
		sql.append("  			100 - GRR.COURSE_SCHEDULE AS KCKSBZ,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.REC_ID CHOOSE_ID,");
		sql.append("  			GRR.EXAM_STATE,");
		sql.append("  			GRR.PROGRESS,");
		sql.append("  			GRR.COURSE_SCHEDULE,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE ), DECODE( SUBSTR( GRR.EXAM_SCORE, 1, 1 ), '.', '0' || GRR.EXAM_SCORE, GRR.EXAM_SCORE ))) EXAM_SCORE,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE1 ), DECODE( SUBSTR( GRR.EXAM_SCORE1, 1, 1 ), '.', '0' || GRR.EXAM_SCORE1, GRR.EXAM_SCORE1 ))) EXAM_SCORE1,");
		sql.append(
				"  			TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE2 ), DECODE( SUBSTR( GRR.EXAM_SCORE2, 1, 1 ), '.', '0' || GRR.EXAM_SCORE2, GRR.EXAM_SCORE2 ))) EXAM_SCORE2,");
		// sql.append(" GSS.PROGRESS SCHEDULE,");

		// 查询学习平台LCMS_USER_DYNA的学习进度MY_PROGRESS
		sql.append("  			LUD.MY_PROGRESS SCHEDULE,");
		sql.append("  			LUD.MY_POINT,");

		sql.append("  			GC.is_open LEARN_STATUS,");

		// 同义词查询考试平台成绩
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( ETU.TEST_POINT )");
		sql.append("  				FROM");
		sql.append("  					EXAM_TEST_USER ETU");
		sql.append("  				WHERE");
		sql.append("  					ETU.IS_DELETED = 'N'");
		sql.append("  					AND ETU.USER_ID = GRR.REC_ID");
		sql.append("  			) TEST_POINT ");

		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))) {
			sql.append("  	AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");

		/*
		 * sql.append("  		LEFT JOIN GJT_STUDENT_STUDY_SITUATION GSS ON");
		 * sql.append("  			GSS.CHOOSE_ID = GRR.REC_ID");
		 */

		// 改为查学习平台LCMS_USER_DYNA的MY_PROGRESS
		sql.append("  		LEFT JOIN LCMS_USER_DYNA LUD ON");
		sql.append("  			LUD.ISDELETED = 'N'");
		sql.append("  			AND LUD.CHOOSE_ID = GRR.REC_ID ");

		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("TERM_ID"))) {
			sql.append("  	AND GTP.ACTUAL_GRADE_ID = :TERM_ID ");
			params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		}

		if ("1".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) { // 已通过
			sql.append("	AND GRR.EXAM_STATE = :EXAM_STATE ");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}

		if ("2".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) { // 学习中
			sql.append("	AND GRR.EXAM_STATE = :EXAM_STATE ");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}

		if ("3".equals(ObjectUtils.toString(searchParams.get("EXAM_STATE")))) {
			sql.append("  	AND GRR.EXAM_STATE IN ('0','2','3','')");
		}

		sql.append("  		ORDER BY");
		sql.append("  			TERM_CODE");
		sql.append("  	) TAB");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

}
