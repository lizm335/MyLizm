package com.ouchgzee.study.dao.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;


@Repository
public class ExamServeDao {

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * 查询考试须知
	 * 
	 * @param searchParams
	 * @return
	 */
	public List examAttention(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GA.ID AS ART_ID,");
		sql.append("  	GA.TITLE,");
		sql.append("  	GA.CONTENT ");
		sql.append("  FROM");
		sql.append("  	GJT_ARTICLE GA");
		sql.append("  WHERE");
		sql.append("  	GA.TITLE LIKE '%考试须知%'");
		sql.append("  	AND GA.XX_ID = :XX_ID ");
		sql.append("  	AND ROWNUM = 1");
		sql.append("  ORDER BY GA.CREATED_DT DESC");
		
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询准考证信息(考生，考点信息)
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List admissionByUserInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.XM AS ADMISSION_NAME,");
		sql.append("  	GSI.XH AS STU_NUMBER,");
		//sql.append("  	GSI.ZP AS STU_PHOTO,");
        sql.append("  	TO_CHAR( GSD.URL_NEW ) STU_PHOTO,");
		sql.append("  	GEP.NAME AS EXAM_POINT_NAME,");
		sql.append("  	GEP.ADDRESS AS EXAM_ADDRESS,");
		sql.append("  	GSO.XXMC,");
		sql.append("  	GG.GRADE_NAME AS STUDY_YEAR_NAME ");
		sql.append("  FROM");
		sql.append("  	GJT_SCHOOL_INFO GSO,");
		sql.append("  	GJT_EXAM_POINT_NEW GEP");
		sql.append("  LEFT JOIN GJT_EXAM_BATCH_NEW GEBN ON GEBN.EXAM_BATCH_ID=GEP.EXAM_BATCH_ID");
		sql.append("  LEFT JOIN GJT_GRADE GG ON GG.GRADE_ID=GEBN.GRADE_ID");
		sql.append("  LEFT JOIN GJT_EXAM_POINT_APPOINTMENT_NEW GEPA ON");
		sql.append("  	GEPA.EXAM_POINT_ID = GEP.EXAM_POINT_ID");
		sql.append("  LEFT JOIN GJT_STUDENT_INFO GSI ON");
		sql.append("  	GSI.STUDENT_ID = GEPA.STUDENT_ID");
        sql.append("  LEFT JOIN GJT_SIGNUP_DATA GSD ON");
        sql.append("  	GSI.STUDENT_ID = GSD.STUDENT_ID");
        sql.append("  	AND GSD.FILE_TYPE = 'zp' ");
		sql.append("  WHERE");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GSO.IS_DELETED = 'N'");
		sql.append("  	AND GEP.IS_DELETED = 'N'");
		sql.append("  	AND GEPA.IS_DELETED = 0");
		sql.append("  	AND GSI.IS_DELETED = 'N'");
		sql.append("  	AND GSO.ID = GEP.XX_ID");
		
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("	AND GSI.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("TYPE"))){
			sql.append("  	AND GEP.EXAM_TYPE = :TYPE ");
			params.put("TYPE", Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		}
		

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * 查询准考证信息(准考证详细信息)
	 * 
	 * @param searchParams
	 * @return
	 */
	public List admissionByExamInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_NO,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'EXAM_STYLE'");
		sql.append("  			AND TSD.CODE = GEP.EXAM_STYLE");
		sql.append("  	) EXAM_TYPE,");
		sql.append("  	GC.COURSE_ID,");
		sql.append("  	GEP.EXAM_PLAN_NAME COURSE_NAME,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  			AND TSD.CODE = GEP.TYPE");
		sql.append("  	) EXAM_STYLE,");
		sql.append("  	TO_CHAR( GEP.EXAM_ST, 'yyyy-MM-dd' ) AS EXAM_DATE,");
		sql.append("  	TO_CHAR( GEP.EXAM_ST, 'hh24:mi' )|| '~' || TO_CHAR( GEP.EXAM_END, 'hh24:mi' ) AS EXAM_TIME,");
		sql.append("  	GER.NAME || GESR.SEAT_NO AS SEAT_NO,");
		sql.append("  	GEP.TYPE,");
		sql.append("  	GEP.EXAM_BATCH_CODE,");
		sql.append("  	GESR.EXAM_ROOM_ID,");
		
		sql.append("  	(SELECT COUNT(GEPN.EXAM_PLAN_ID)");
		sql.append("  	FROM GJT_EXAM_APPOINTMENT_NEW GEAN, GJT_EXAM_PLAN_NEW GEPN");
		sql.append("  	WHERE GEAN.IS_DELETED = 0");
		sql.append("  	AND GEPN.IS_DELETED = 0");
		sql.append("  	AND GEAN.EXAM_PLAN_ID = GEPN.EXAM_PLAN_ID");
		sql.append("  	AND GEAN.STUDENT_ID = GEA.STUDENT_ID");
		sql.append("  	AND GEPN.EXAM_BATCH_CODE = GEP.EXAM_BATCH_CODE");
		sql.append("  	AND GEPN.EXAM_ST = GEP.EXAM_ST");
		sql.append("  	AND GEPN.EXAM_END = GEP.EXAM_END) PLAN_COUNT");
		
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  INNER JOIN GJT_EXAM_STUDENT_ROOM_NEW GESR ON");
		sql.append("  	GEA.APPOINTMENT_ID = GESR.APPOINTMENT_ID");
		sql.append("  INNER JOIN GJT_EXAM_PLAN_NEW GEP ON");
		sql.append("  	GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  INNER JOIN Gjt_Rec_Result GRR ON");
		sql.append("  	GRR.Rec_Id=GEA.Rec_Id");
		sql.append("  INNER JOIN GJT_COURSE GC ON");
		sql.append("  	GRR.COURSE_ID = GC.COURSE_ID");
		sql.append("  LEFT JOIN GJT_EXAM_ROOM_NEW GER ON");
		sql.append("  	GESR.EXAM_ROOM_ID = GER.EXAM_ROOM_ID");
		sql.append("  	AND GER.IS_DELETED = 0");
		sql.append("  WHERE");
		sql.append("  	GEA.IS_DELETED = 0");

		if(EmptyUtils.isNotEmpty(searchParams.get("TYPE"))){
			sql.append("  	AND GEP.TYPE = :TYPE ");
			params.put("TYPE", Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		}

		sql.append("  	AND GEA.STATUS = 1");
		sql.append("  	AND GEP.IS_DELETED = 0");
		sql.append("  	AND GC.IS_DELETED = 'N'");

		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GEA.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("COURSE_ID"))){
			sql.append("	AND GC.COURSE_ID = :COURSE_ID ");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		sql.append(" ORDER BY EXAM_ST");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}
	
	/**
	 * 查询预约考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryApointmentExamPoint(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT ");
		sql.append(" 	GEP.EXAM_POINT_ID, ");
		sql.append(" 	GEP.CODE, ");
		sql.append(" 	GEP.NAME, ");
		sql.append(" 	GEP.ADDRESS, ");
		sql.append(" 	GEPA.STUDENT_ID ");
		sql.append(" FROM ");
		sql.append(" 	GJT_EXAM_POINT_APPOINTMENT_NEW GEPA, ");
		sql.append(" 	GJT_EXAM_POINT_NEW GEP, ");
		sql.append(" 	GJT_AREA GA ");
		sql.append(" WHERE ");
		sql.append(" 	GEPA.IS_DELETED = 0 ");
		sql.append(" 	AND GEP.IS_DELETED = 'N' ");
		sql.append(" 	AND GA.IS_DELETED = 'N' ");
		sql.append(" 	AND GEPA.EXAM_POINT_ID = GEP.EXAM_POINT_ID ");
		sql.append(" 	AND GEP.AREA_ID = GA.DISTRICT ");
		sql.append(" 	AND GEPA.STUDENT_ID = :STUDENT_ID ");
//		sql.append(" 	AND GEPA.STUDENT_ID = '2e674547a0ea48339ce5fc2423da8347' ");
		
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	
	/**
	 * 查询考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryPointInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_POINT_ID,");
		sql.append("  	GEP.NAME POINT_NAME,");
		sql.append("  	GEP.ADDRESS,");
		sql.append("  	GEP.CODE POINT_CODE,");
		sql.append("  	GEP.AREA_ID");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_POINT_NEW GEP");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 0 ");
		sql.append("  	AND GEP.XX_ID = :XX_ID ");
//		sql.append("  	AND GEP.XX_ID = 'fccafe375dba41608688bc28f5e0c91f'");
		
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if(EmptyUtils.isNotEmpty(searchParams.get("POINT_NAME"))){
			sql.append(" AND GEP.NAME LIKE :POINT_NAME ");
			params.put("POINT_NAME", "%"+ObjectUtils.toString(searchParams.get("POINT_NAME"))+"%");
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	/**
	 * 保存学生预约考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveStudentExamPoint(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_STUDENT_EXAM_POINT( ");
		sql.append("  			STUDENT_ID, ");
		sql.append("  			TERM_ID, ");
		sql.append("  			EXAM_POINT_ID, ");
		sql.append("  			CREATED_DT ");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:STUDENT_ID, ");
		sql.append("  		:TERM_ID, ");
		sql.append("  		:EXAM_POINT_ID, ");
		sql.append("  		SYSDATE ");
		sql.append("  	) ");
		
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		
		return commonDao.insertForMapNative(sql.toString(), params);
	}
	
	/**
	 * 查询学员预约的考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryAppointmentByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEPA.ID,");
		sql.append("  	GEPA.STUDENT_ID,");
		sql.append("  	GEPA.STUDY_YEAR_ID,");
		sql.append("  	GEPA.EXAM_POINT_ID");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_POINT_APPOINTMENT_NEW GEPA");
		sql.append("  WHERE");
		sql.append("  	GEPA.IS_DELETED = 0 ");
		sql.append("  	AND GEPA.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GEPA.STUDY_YEAR_ID = :STUDY_YEAR_ID ");
		
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("STUDY_YEAR_ID", ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);

	}
	
	/**
	 * 保存学生下载准考证记录
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveStudentDownToken(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_TOKEN_LOG( ");
		sql.append("  			LOG_ID, ");
		sql.append("  			STUDENT_ID, ");
		sql.append("  			LOG_TYPE, ");
		sql.append("  			APPOINTMENT_ID, ");
		sql.append("  			CREATED_DT ");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:LOG_ID, ");
		sql.append("  		:STUDENT_ID, ");
		sql.append("  		:LOG_TYPE, ");
		sql.append("  		(SELECT GEA.APPOINTMENT_ID");
		sql.append("  		FROM GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  		LEFT JOIN GJT_EXAM_PLAN_NEW GEP");
		sql.append("  		ON GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  		WHERE GEA.IS_DELETED = 0");
		sql.append("  		AND GEP.IS_DELETED = 0");
		sql.append("  		AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  		AND GEP.EXAM_NO = :EXAM_NO");
		sql.append("  		AND GEA.STUDENT_ID = :STUDENT_ID),");
		sql.append("  		SYSDATE ");
		sql.append("  	) ");
		
		params.put("LOG_ID", ObjectUtils.toString(searchParams.get("LOG_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("LOG_TYPE", ObjectUtils.toString(searchParams.get("LOG_TYPE")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		params.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.insertForMapNative(sql.toString(), params);
	}
	
	/**
	 * 更新学员预约考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateAppointmentByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_POINT_APPOINTMENT_NEW GEPA");
		sql.append("  SET");
		sql.append("  	GEPA.UPDATED_DT = SYSDATE,");
		sql.append("  	GEPA.UPDATED_BY = :UPDATED_BY,");
		sql.append("  	GEPA.IS_DELETED = 0 ");
		sql.append("  WHERE");
		sql.append("  	GEPA.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GEPA.STUDY_YEAR_ID = :STUDY_YEAR_ID ");
		
		params.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);

	}
	
	/**
	 * 保存考点预约信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveExamPointApp(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_POINT_APPOINTMENT_NEW(");
		sql.append("  			ID,");
		sql.append("  			STUDENT_ID,");
		sql.append("  			GRADE_ID,");
		sql.append("  			EXAM_POINT_ID,");
		sql.append("  			CREATED_BY,");
		sql.append("  			EXAM_BATCH_CODE,");
		sql.append("  			CREATED_DT,");
		sql.append("  			IS_DELETED ");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:APPOINTMENT_POINT_ID, ");
		sql.append("  		:STUDENT_ID, ");
		sql.append("  		:GRADE_ID, ");
		sql.append("  		:EXAM_POINT_ID,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		:EXAM_BATCH_CODE,");
		sql.append("  		SYSDATE,");
		sql.append("  		0 ");
		sql.append("  	)");
		
		params.put("APPOINTMENT_POINT_ID", ObjectUtils.toString(searchParams.get("APPOINTMENT_POINT_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));

		return commonDao.insertForMapNative(sql.toString(), params);

	}
	
	/**
	 * 查询学员是否预约过该考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryMakeApponitmentByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GSEP.STUDENT_ID,");
		sql.append("  	GSEP.TERM_ID,");
		sql.append("  	GSEP.EXAM_POINT_ID,");
		sql.append("  	GSEP.UPDATED_DT,");
		sql.append("  	GSEP.CREATED_DT,");
		sql.append("  	GSEP.IS_DELETED");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_EXAM_POINT GSEP");
		sql.append("  WHERE");
		sql.append("  	GSEP.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GSEP.TERM_ID = :TERM_ID ");

		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 更新学员预约考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateMakeApponitmentByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_STUDENT_EXAM_POINT GSEP");
		sql.append("  SET");
		sql.append("  	GSEP.IS_DELETED = 'N',");
		sql.append("  	GSEP.UPDATED_DT = SYSDATE,");
		sql.append("  	GSEP.EXAM_POINT_ID =(");
		sql.append("  		SELECT");
		sql.append("  			GEP.EXAM_POINT_ID");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_POINT_NEW GEP");
		sql.append("  		WHERE");
		sql.append("  			GEP.IS_DELETED = 0 ");
		sql.append("  			AND GEP.EXAM_POINT_ID = :EXAM_POINT_ID ");
		sql.append("  	)");
		sql.append("  WHERE");
		sql.append("  	GSEP.STUDENT_ID =(");
		sql.append("  		SELECT");
		sql.append("  			GSI.STUDENT_ID");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO GSI");
		sql.append("  		WHERE");
		sql.append("  			GSI.IS_DELETED = 'N'");
		sql.append("  			AND GSI.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	)");
		sql.append("  	AND GSEP.TERM_ID =(");
		sql.append("  		SELECT");
		sql.append("  			GTI.TERM_ID");
		sql.append("  		FROM");
		sql.append("  			GJT_TERM_INFO GTI");
		sql.append("  		WHERE");
		sql.append("  			GTI.IS_DELETED = 'N'");
		sql.append("  			AND GTI.TERM_ID = :TERM_ID ");
		sql.append("  	)");
		
		params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);

	}
	
	/**
	 * 保存学员预约的考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveExamPointByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_STUDENT_EXAM_POINT(");
		sql.append("  			STUDENT_ID,");
		sql.append("  			TERM_ID,");
		sql.append("  			EXAM_POINT_ID,");
		sql.append("  			CREATED_DT");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:STUDENT_ID,");
		sql.append("  		:TERM_ID,");
		sql.append("  		:EXAM_POINT_ID,");
		sql.append("  		SYSDATE");
		sql.append("  	)");
		
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		
		return commonDao.insertForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询预约考试
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryAppointExam(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	A.REC_ID,");
		sql.append("  	TP.TEACH_PLAN_ID,");
		sql.append("  	TP.COURSE_CODE,");
		sql.append("  	TP.KKXQ,");
		sql.append("  	TI.TERM_NAME,");
		sql.append("  	A.COURSE_ID,");
		sql.append("  	C.KCMC,");
		sql.append("  	NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'CourseType' AND TSD.CODE = TP.KCLBM ), '未知' ) KCLBM,");
		sql.append("  	TP.KCSX,");
		sql.append("  	TP.XF,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = TP.KSFS");
		sql.append("  			AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  	) KSFS,");
		sql.append("  	A.EXAM_SCORE2,");
		sql.append("  	TO_CHAR(TP.EXAM_STIME,'yyyy-MM-dd') EXAM_STIME,");
		sql.append("  	TO_CHAR(TP.EXAM_ETIME,'yyyy-MM-dd') EXAM_ETIME,");
		sql.append("  	TP.BOOK_STARTTIME,");
		sql.append("  	TP.BOOK_ENDTIME,");
		sql.append("  	A.EXAM_STATE,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = TP.EXAM_STYLE");
		sql.append("  			AND TSD.TYPE_CODE = 'EXAM_STYLE'");
		sql.append("  	) EXAM_STYLE");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT A");
		sql.append("  INNER JOIN VIEW_TEACH_PLAN TP ON");
		sql.append("  	TP.TEACH_PLAN_ID = A.TEACH_PLAN_ID");
		sql.append("  	AND TP.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_COURSE C ON");
		sql.append("  	C.COURSE_ID = TP.COURSE_ID");
		sql.append("  	AND C.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_TERM_INFO TI ON");
		sql.append("  	TI.TERM_ID = TP.TERM_ID");
		sql.append("  	AND TI.IS_DELETED = 'N'");
		sql.append("  WHERE");
		sql.append("  	A.IS_DELETED = 'N'");
		sql.append("  	AND A.STUDENT_ID = :STUDENT_ID");
		sql.append("  	AND A.EXAM_STATE IN(");
		sql.append("  		'1',");
		sql.append("  		'2'");
		sql.append("  	)");
		sql.append("  ORDER BY");
		sql.append("  	TI.TERM_CODE ASC,");
		sql.append("  	C.KCMC ASC");
		sql.append("  ");

		
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);

	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExamPlanByTeachPlan(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	TO_CHAR( GEP.BOOK_ST, 'yyyy-mm-dd' ) AS BOOK_ST,");
		sql.append("  	TO_CHAR( GEP.BOOK_END, 'yyyy-mm-dd' ) AS BOOK_END,");
		sql.append("  	TO_CHAR( GEP.EXAM_ST, 'yyyy-mm-dd hh24:mi' ) AS EXAM_ST,");
		sql.append("  	TO_CHAR( GEP.EXAM_END, 'yyyy-mm-dd hh24:mi' ) AS EXAM_END");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP,");
		sql.append("  	GJT_EXAM_SUBJECT_NEW GES,");
		sql.append("  	GJT_STUDYYEAR_INFO GSI,");
		sql.append("  	VIEW_TEACH_PLAN GTP");
		sql.append("  WHERE ");
		sql.append("	GEP.IS_DELETED = 0 ");
		sql.append("	AND GES.IS_DELETED = 0 ");
		sql.append("	AND GTP.IS_DELETED = 'N' ");
		sql.append("  	AND GTP.TEACH_PLAN_ID = :TEACH_PLAN_ID");
		sql.append("  	AND GTP.SUBJECT_ID = GES.SUBJECT_ID");
		sql.append("  	AND GEP.SUBJECT_CODE = GES.SUBJECT_CODE");
		sql.append("  	AND GEP.STUDY_YEAR_ID = GSI.ID");
		sql.append("  	AND GSI.STUDY_YEAR_CODE = :STUDY_YEAR_CODE ");
		
		params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		params.put("STUDY_YEAR_CODE", Integer.parseInt(ObjectUtils.toString(searchParams.get("STUDY_YEAR_CODE"))));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 统计待预约科目
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List appointmentProjectCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT( DISTINCT GES.SUBJECT_ID ) APPOINTMENT_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT GRR,");
		sql.append("  	VIEW_TEACH_PLAN GTP,");
		sql.append("  	GJT_EXAM_SUBJECT_NEW GES,");
		sql.append("  	GJT_TERM_INFO GTI,");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GRR.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GES.IS_DELETED = 0");
		sql.append("  	AND GTI.IS_DELETED = 'N'");
		sql.append("  	AND GSI.IS_DELETED = 'N'");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  	AND GTP.SUBJECT_ID = GES.SUBJECT_ID");
		sql.append("  	AND GTP.TERM_ID = GTI.TERM_ID");
		sql.append("  	AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  	AND GRR.COURSE_ID = GTP.COURSE_ID");
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		sql.append("  	AND GRR.EXAM_STATE = '1'");
		sql.append("  ORDER BY");
		sql.append("  	GTI.TERM_CODE,");
		sql.append("  	GES.NAME");
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 统计待考试科目
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List examProjectCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(DISTINCT GES.SUBJECT_ID) NEED_EXAM ");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI,");
		sql.append("  	VIEW_TEACH_PLAN GTP,");
		sql.append("  	GJT_EXAM_SUBJECT_NEW GES,");
		sql.append("  	GJT_TERM_INFO GTI,");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  LEFT JOIN GJT_STUDY_RECORD GSR ON");
		sql.append("  	GRR.REC_ID = GSR.CHOOSE_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GES.IS_DELETED = 0");
		sql.append("  	AND GTI.IS_DELETED = 'N'");
		sql.append("  	AND GSR.IS_DELETED = 'N'");
		sql.append("  	AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  	AND GTP.SUBJECT_ID = GES.SUBJECT_ID");
		sql.append("  	AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  	AND GTP.TERM_ID = GTI.TERM_ID");
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		sql.append("  	AND GSR.EXAM_STATE = '1'");
		sql.append("  ORDER BY");
		sql.append("  	GTI.TERM_CODE,");
		sql.append("  	GES.NAME");
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 统计考试科目信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List countExamSubjectInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT( 1 ) KM_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI,");
		sql.append("  	GJT_GRADE GRE,");
		sql.append("  	GJT_SPECIALTY GSY,");
		sql.append("  	GJT_COURSE GCE,");
		sql.append("  	VIEW_TEACH_PLAN GTP,");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  LEFT JOIN GJT_STUDY_RECORD GSR ON");
		sql.append("  	GRR.REC_ID = GSR.CHOOSE_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GRE.IS_DELETED = 'N'");
		sql.append("  	AND GSY.IS_DELETED = 'N'");
		sql.append("  	AND GRR.IS_DELETED = 'N'");
		sql.append("  	AND GCE.IS_DELETED = 'N'");
		sql.append("  	AND GTP.IS_DELETED = 'N'");
		sql.append("  	AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  	AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  	AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  	AND GRR.COURSE_ID = GCE.COURSE_ID");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("KSFS"))){
			sql.append("	AND GTP.KSFS = :KSFS ");
			params.put("KSFS", ObjectUtils.toString(searchParams.get("KSFS")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("SCORE_STATE"))){
			sql.append("	AND GSR.SCORE_STATE = :SCORE_STATE ");
			params.put("SCORE_STATE", ObjectUtils.toString(searchParams.get("SCORE_STATE")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
		
	}
	
	/**
	 * 查询待预约考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List countAppointmentExmaPoint(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT( DISTINCT GEP.EXAM_POINT_ID ) APPOINTMENT_POINT_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_POINT_NEW GEP,");
		sql.append("  	GJT_EXAM_POINT_APPOINTMENT_NEW GEPA,");
		sql.append("  	GJT_STUDYYEAR_INFO GTI,");
		sql.append("  	GJT_AREA GA,");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 'N'");
		sql.append("  	AND GEPA.IS_DELETED = 0");
		sql.append("  	AND GSI.IS_DELETED = 'N'");
		sql.append("  	AND GA.IS_DELETED = 'N'");
		sql.append("  	AND GEP.EXAM_POINT_ID = GEPA.EXAM_POINT_ID");
		sql.append("  	AND GEPA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  	AND GEPA.STUDY_YEAR_ID = GTI.ID");
		sql.append("  	AND GEP.AREA_ID = GA.DISTRICT");
		sql.append("  	AND GSI.XX_ID = :XX_ID");
//		sql.append("  	AND GSI.XX_ID = '2f5bfcce71fa462b8e1f65bcd0f4c632'");
		sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");
//		sql.append("  	AND GSI.STUDENT_ID = 'd735550b4d064b5a83def047b3bb5dc1'");
		sql.append("  	AND(");
		sql.append("  		GEPA.APPOINTMENT_FLG IS NULL");
		sql.append("  		OR GEPA.APPOINTMENT_FLG = '1'");
		sql.append("  	)");
		
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);

		
	}
	
	/**
	 * 查询待预约考试(已废弃)
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryAppointmentExam(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GTP.KKXQ TERM_CODE_FLAG,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'KKXQ'");
		sql.append("  			AND TSD.CODE = GTP.KKXQ");
		sql.append("  	) TERM_CODE,");
		sql.append("  	GG.GRADE_ID TERM_ID,");
		sql.append("  	GG.GRADE_NAME TERM_NAME,");
		sql.append("  	GES.COURSE_ID,");
		sql.append("  	GES.NAME COURSE_NAME,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  			AND TSD.CODE = DECODE( GES.TYPE, 1, 7, 2, 8, 3, 11, 4, 13, 5, 19, 6, 17, 7, 18, 8, 14, 9, 15, 10, 20, 11, 21, 16 )");
		sql.append("  	) EXAM_STYLE,");
		sql.append("  	GES.TYPE AS KSFS_FLAG,");
		sql.append("  	TO_CHAR( GTP.EXAM_STIME, 'yyyy-MM-dd' ) EXAM_STIME,");
		sql.append("  	TO_CHAR( GTP.EXAM_ETIME, 'yyyy-MM-dd' ) EXAM_ETIME,");
		sql.append("  	GTP.BOOK_STARTTIME,");
		sql.append("  	GTP.BOOK_ENDTIME,");
		sql.append("  	TO_CHAR(GEP.BOOK_ST,'yyyy-MM-dd hh24:mi:ss') BOOK_ST,");
		sql.append("  	TO_CHAR(GEP.BOOK_END,'yyyy-MM-dd hh24:mi:ss') BOOK_END,");
		sql.append("  	GRR.BESPEAK_STATE,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  	GRR.REC_ID,");
		sql.append("  	GTP.TEACH_PLAN_ID,");
		sql.append("  	GES.SUBJECT_CODE,");
		sql.append("  	GEP.EXAM_PLAN_ID,");
		sql.append("  	GTP.KKZY,");
		sql.append("  	GTP.PYCC,");
		sql.append("  	GRR.STUDENT_ID,");
		sql.append("  	A.URL_NEW ");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE GG,");
		sql.append("  	VIEW_TEACH_PLAN GTP,");
		sql.append("  	GJT_EXAM_SUBJECT_NEW GES,");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP,");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB,");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			GSI.STUDENT_ID,");
		sql.append("  			GSI.SFZH,");
		sql.append("  			GSD.ID_NO,");
		sql.append("  			GSD.FILE_TYPE,");
		sql.append("  			GSD.URL,");
		sql.append("  			TO_CHAR( GSD.URL_NEW ) URL_NEW");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO GSI,");
		sql.append("  			GJT_SIGNUP GS,");
		sql.append("  			GJT_SIGNUP_DATA GSD");
		sql.append("  		WHERE");
		sql.append("  			GSI.IS_DELETED = 'N'");
		sql.append("  			AND GS.IS_DELETED = 'N'");
		sql.append("  			AND GSI.STUDENT_ID = GS.STUDENT_ID");
		sql.append("  			AND GS.STUDENT_ID = GSD.STUDENT_ID");
		sql.append("  			AND GSD.FILE_TYPE = 'zp'");
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("		AND GSI.STUDENT_ID = :STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		sql.append("  	) A ON");
		sql.append("  	A.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GTP.IS_DELETED = 'N'");
		sql.append("  	AND GES.IS_DELETED = 0");
		sql.append("  	AND GEP.IS_DELETED = 0");
		sql.append("  	AND GEB.IS_DELETED = 0");
		sql.append("  	AND GG.IS_DELETED = 'N'");
		sql.append("  	AND GRR.IS_DELETED = 'N'");
		sql.append("  	AND GES.COURSE_ID = GTP.COURSE_ID");
		sql.append("  	AND GES.KCH = GTP.COURSE_CODE");
		sql.append("  	AND GES.SUBJECT_CODE = GEP.SUBJECT_CODE");
		sql.append("  	AND GES.TYPE = GEP.TYPE");
		sql.append("  	AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  	AND GEB.GRADE_ID = GG.GRADE_ID");
		sql.append("  	AND GRR.COURSE_ID = GTP.COURSE_ID");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  	AND GRR.COURSE_ID = GES.COURSE_ID");
		if(EmptyUtils.isNotEmpty(searchParams.get("KKZY"))){
			sql.append("  	AND GTP.KKZY = :KKZY ");
			params.put("KKZY", ObjectUtils.toString(searchParams.get("KKZY")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("PYCC"))){
			sql.append("  	AND GTP.PYCC = :PYCC ");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		if("0".equals(ObjectUtils.toString(searchParams.get("BESPEAK_STATE")))){
			sql.append("	AND GRR.BESPEAK_STATE = :BESPEAK_STATE ");
			params.put("BESPEAK_STATE", ObjectUtils.toString(searchParams.get("BESPEAK_STATE")));
		}
		if("1".equals(ObjectUtils.toString(searchParams.get("BESPEAK_STATE")))){
			sql.append("	AND GRR.BESPEAK_STATE IN ('0','1') ");
		}
		sql.append("  ORDER BY");
		sql.append("  	GTP.KKXQ");
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询考试计划
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExamBatchData(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	TAB.EXAM_BATCH_ID,");
		sql.append("  	TAB.EXAM_BATCH_CODE,");
		sql.append("  	TAB.EXAM_BATCH_NAME,");
		sql.append("  	TO_CHAR( TAB.BOOK_ST, 'yyyy-MM-dd' ) BOOK_ST,");
		sql.append("  	TO_CHAR( TAB.BOOK_END, 'yyyy-MM-dd' ) BOOK_END,");
		sql.append("  	TO_CHAR( TAB.PAPER_ST, 'yyyy-MM-dd' ) PAPER_ST,");
		sql.append("  	TO_CHAR( TAB.PAPER_END, 'yyyy-MM-dd' ) PAPER_END,");
		sql.append("  	TO_CHAR( TAB.THESIS_END, 'yyyy-MM-dd' ) THESIS_END,");
		sql.append("  	TO_CHAR( TAB.ONLINE_ST, 'yyyy-MM-dd' ) ONLINE_ST,");
		sql.append("  	TO_CHAR( TAB.ONLINE_END, 'yyyy-MM-dd' ) ONLINE_END,");
		sql.append("  	TO_CHAR( TAB.MACHINE_ST, 'yyyy-MM-dd' ) MACHINE_ST,");
		sql.append("  	TO_CHAR( TAB.MACHINE_END, 'yyyy-MM-dd' ) MACHINE_END,");
		sql.append("  	TO_CHAR( TAB.REPORT_END, 'yyyy-MM-dd' ) REPORT_END,");
		sql.append("  	TO_CHAR( TAB.PROVINCE_ONLINE_ST, 'yyyy-MM-dd' ) PROVINCE_ONLINE_ST,");
		sql.append("  	TO_CHAR( TAB.PROVINCE_ONLINE_END, 'yyyy-MM-dd' ) PROVINCE_ONLINE_END,");
		sql.append("  	TO_CHAR( TAB.SHAPE_END, 'yyyy-MM-dd' ) SHAPE_END,");
		sql.append("  	TO_CHAR( TAB.RECORD_ST, 'yyyy-MM-dd' ) RECORD_ST,");
		sql.append("  	TO_CHAR( TAB.RECORD_END, 'yyyy-MM-dd' ) RECORD_END,");
        sql.append("  	TO_CHAR( TAB.BOOKS_ST, 'yyyy-MM-dd' ) BOOKS_ST,");
        sql.append("  	TO_CHAR( TAB.BOOKS_END, 'yyyy-MM-dd' ) BOOKS_END,");
        sql.append("  	TO_CHAR( TAB.BKTK_BOOK_ST, 'yyyy-MM-dd' ) BKTK_BOOK_ST,");
        sql.append("  	TO_CHAR( TAB.BKTK_BOOK_END, 'yyyy-MM-dd' ) BKTK_BOOK_END,");
        sql.append("  	TO_CHAR( TAB.XWYY_BOOK_ST, 'yyyy-MM-dd' ) XWYY_BOOK_ST,");
        sql.append("  	TO_CHAR( TAB.XWYY_BOOK_END, 'yyyy-MM-dd' ) XWYY_BOOK_END,");
        sql.append("  	TO_CHAR( TAB.BKTK_EXAM_ST, 'yyyy-MM-dd' ) BKTK_EXAM_ST,");
        sql.append("  	TO_CHAR( TAB.BKTK_EXAM_END, 'yyyy-MM-dd' ) BKTK_EXAM_END,");
        sql.append("  	TO_CHAR( TAB.XWYY_EXAM_ST, 'yyyy-MM-dd' ) XWYY_EXAM_ST,");
        sql.append("  	TO_CHAR( TAB.XWYY_EXAM_END, 'yyyy-MM-dd' ) XWYY_EXAM_END,");
		sql.append("  	TAB.CREATED_DT,");
		sql.append("  	TAB.END_DATE,");
		sql.append("  	TAB.GRADE_ID");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GEB.EXAM_BATCH_ID,");
		sql.append("  			GEB.EXAM_BATCH_CODE,");
		sql.append("  			GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  			GEB.BOOK_ST,");
		sql.append("  			GEB.BOOK_END,");
		sql.append("  			GEB.PAPER_ST,");
		sql.append("  			GEB.PAPER_END,");
		sql.append("  			GEB.THESIS_END,");
		sql.append("  			GEB.ONLINE_ST,");
		sql.append("  			GEB.ONLINE_END,");
		sql.append("  			GEB.MACHINE_ST,");
		sql.append("  			GEB.MACHINE_END,");
		sql.append("  			GEB.REPORT_END,");
		sql.append("  			GEB.PROVINCE_ONLINE_ST,");
		sql.append("  			GEB.PROVINCE_ONLINE_END,");
		sql.append("  			GEB.SHAPE_END,");
		sql.append("  			GEB.RECORD_ST,");
		sql.append("  			GEB.RECORD_END,");
        sql.append("  			GEB.BOOKS_ST,");
        sql.append("  			GEB.BOOKS_END,");
        sql.append("  			GEB.BKTK_BOOK_ST,");
        sql.append("  			GEB.BKTK_BOOK_END,");
        sql.append("  			GEB.XWYY_BOOK_ST,");
        sql.append("  			GEB.XWYY_BOOK_END,");
        sql.append("  			GEB.BKTK_EXAM_ST,");
        sql.append("  			GEB.BKTK_EXAM_END,");
        sql.append("  			GEB.XWYY_EXAM_ST,");
        sql.append("  			GEB.XWYY_EXAM_END,");
		sql.append("  			GEB.CREATED_DT,");
		sql.append("  			TO_CHAR( NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 )),'yyyy-MM-dd') END_DATE, ");
		sql.append("  			GG.GRADE_ID ");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_BATCH_NEW GEB,");
		sql.append("  			GJT_GRADE GG");
		sql.append("  		WHERE");
		sql.append("  			GEB.IS_DELETED = 0");
		sql.append("  			AND GG.IS_DELETED = 'N' ");
		sql.append("  			AND GEB.PLAN_STATUS = '3' ");
		sql.append("  			AND GEB.GRADE_ID = GG.GRADE_ID ");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GEB.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		
		sql.append("  		ORDER BY");
		sql.append("  			GEB.CREATED_DT DESC");
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		
		if("1".equals(ObjectUtils.toString(searchParams.get("CURRENT_FLAG")))){
			sql.append("  	AND SYSDATE BETWEEN TAB.BOOK_ST AND TAB.RECORD_END");
			sql.append("  	AND ROWNUM <= 1");
		}

		if("2".equals(ObjectUtils.toString(searchParams.get("CURRENT_FLAG")))){ //一、二级教学组织模式
			sql.append("  	AND SYSDATE BETWEEN TAB.BOOK_ST AND TAB.ONLINE_END");
			sql.append("  	AND ROWNUM <= 1");
		}


		sql.append("  ORDER BY");
		sql.append("  	TAB.CREATED_DT DESC");
		
		return commonDao.queryForMapListNative(sql.toString(), params);

	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExamBatchTime(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEB.EXAM_BATCH_ID,");
		sql.append("  	GEB.EXAM_BATCH_CODE,");
		sql.append("  	GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  	GEB.BOOK_ST,");
		sql.append("  	GEB.BOOK_END,");
		sql.append("  	GEB.ONLINE_ST,");
		sql.append("  	GEB.ONLINE_END,");
		sql.append("  	GEB.OFFLINE_ST");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GEB.IS_DELETED = 0");
		
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GEB.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		
		sql.append("  ORDER BY");
		sql.append("  	GEB.CREATED_DT DESC");

		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	
	
	/**
	 * 我的考试
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List myExamDataList(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.TERM_CODE,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	TAB.TERM_NAME,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.KSFS,");
		sql.append("  	TAB.KCH,");
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = TAB.EXAM_SCORE1 ), TAB.EXAM_SCORE1 )) EXAM_SCORE,");
		sql.append("  	TAB.COURSE_COST,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.PAY_STATE,");
		sql.append("  	TAB.BESPEAK_STATE,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.TEACH_PLAN_ID");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_CODE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  			) TERM_CODE,");
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
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			NVL(( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = GC.KCH ),( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = 'other' )) COURSE_COST,");
		sql.append("  			GC.KCH,");
		sql.append("  			GTP.KSFS,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			GRR.EXAM_SCORE,");
		sql.append("  			GRR.EXAM_SCORE1,");
		sql.append("  			GRR.EXAM_SCORE2,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  			GRR.BESPEAK_STATE,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GTP.TEACH_PLAN_ID");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");

		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  			AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  			AND GTP.COURSE_CODE = GC.KCH");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GTP.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			TERM_CODE");
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		sql.append("  	AND TAB.BESPEAK_STATE = '1'");


		/*
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("	AND TAB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		*/

		/*
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_STATE"))){ //0：待考试,1：考试中,2：考试结束
			sql.append("	AND TAB.EXAM_STATE = :EXAM_STATE ");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
			sql.append("  	AND TAB.BESPEAK_STATE = '1'");
		}
		*/

		/*
		if("3".equals(ObjectUtils.toString(searchParams.get("FLAG")))){
			sql.append("	AND TAB.BESPEAK_STATE = '1' ");
		}
		*/

		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	;
	/**
	 * 查询考点信息列表
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page queryPointList(Map searchParams,PageRequest pageRequst){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append("  	TAB.EXAM_POINT_ID,");
		sql.append("  	TAB.POINT_NAME,");
		sql.append("  	TAB.ADDRESS,");
		sql.append("  	TAB.CODE,");
		sql.append("  	TAB.IS_ENABLED,");
		sql.append("  	TAB.PROVINCE_NAME,");
		sql.append("  	TAB.CITY_NAME,");
		sql.append("  	TAB.DISTRICT_NAME,");
		sql.append("  	TAB.EXAM_BATCH_CODE");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GEP.EXAM_POINT_ID,");
		sql.append("  			GEP.NAME AS POINT_NAME,");
		sql.append("  			GEP.ADDRESS,");
		sql.append("  			GEP.CODE,");
		sql.append("  			GEP.IS_ENABLED,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = SUBSTR( GEP.AREA_ID, 1, 2 )|| '0000'");
		sql.append("  			) PROVINCE_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = SUBSTR( GEP.AREA_ID, 1, 4 )|| '00'");
		sql.append("  			) CITY_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = GEP.AREA_ID");
		sql.append("  			) DISTRICT_NAME,");
		sql.append("  			GEP.EXAM_BATCH_CODE");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_POINT_NEW GEP");
		sql.append("  		INNER JOIN GJT_EXAM_POINT_NEW_STUDYCENTER B ON B.EXAM_POINT_ID=GEP.EXAM_POINT_ID");
		sql.append("  		WHERE");
		sql.append("  			GEP.IS_DELETED = 'N'");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GEP.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("studyCenterId"))){
			// 通用或指定学习中心
			sql.append("  			AND (B.STUDY_CENTER_ID = '-1' OR B.STUDY_CENTER_ID = :studyCenterId OR B.STUDY_CENTER_ID = :zsdId) ");
			params.put("studyCenterId", ObjectUtils.toString(searchParams.get("studyCenterId")));
			params.put("zsdId", ObjectUtils.toString(searchParams.get("zsdId")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  			AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_TYPE"))){
			sql.append("  			AND GEP.EXAM_TYPE = :EXAM_TYPE ");
			params.put("EXAM_TYPE", ObjectUtils.toString(searchParams.get("EXAM_TYPE")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("POINT_NAME_NEW"))){
			sql.append("  			AND GEP.NAME = :POINT_NAME_NEW ");
			params.put("POINT_NAME_NEW", ObjectUtils.toString(searchParams.get("POINT_NAME_NEW")));
		}
		
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		if(EmptyUtils.isNotEmpty(searchParams.get("PROVINCE_NAME"))){
			sql.append("  	AND TAB.PROVINCE_NAME LIKE :PROVINCE_NAME ");
			params.put("PROVINCE_NAME", ObjectUtils.toString(searchParams.get("PROVINCE_NAME") + "%"));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("CITY_NAME"))){
			sql.append("	AND TAB.CITY_NAME LIKE :CITY_NAME ");
			params.put("CITY_NAME", ObjectUtils.toString(searchParams.get("CITY_NAME") + "%"));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("DISTRICT_NAME"))){
			sql.append("	AND TAB.DISTRICT_NAME LIKE :DISTRICT_NAME ");
			params.put("DISTRICT_NAME", ObjectUtils.toString(searchParams.get("DISTRICT_NAME") + "%"));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("POINT_NAME"))){
			sql.append("	AND TAB.POINT_NAME LIKE :POINT_NAME ");
			params.put("POINT_NAME", "%"+ObjectUtils.toString(searchParams.get("POINT_NAME"))+"%");
		}
		
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
		
	}
	
	/**
	 * 查询考试预约上限
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List countAppointMents(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(1) COUNTAPPOINTMENTS");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  WHERE");
		sql.append("  	GEA.IS_DELETED = 0 ");
		sql.append("  	AND GEA.STATUS = '0'");
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GEA.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询对应考试计划
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List queryExamPlan(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEPN.EXAM_PLAN_ID,");
		sql.append("  	GEPN.XX_ID,");
		sql.append("  	GEPN.TYPE");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_PLAN_NEW GEPN");
		sql.append("  WHERE");
		sql.append("  	GEPN.IS_DELETED = 0");
		if(EmptyUtils.isNotEmpty(searchParams.get("SUBJECT_CODE"))){
			sql.append("  	AND GEPN.SUBJECT_CODE = :SUBJECT_CODE ");
			params.put("SUBJECT_CODE", ObjectUtils.toString(searchParams.get("SUBJECT_CODE")));
		}
		sql.append("  	AND ROWNUM = 1 ");
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询考试计划对应的预约信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryAppointmentExamPlan(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEA.APPOINTMENT_ID,");
		sql.append("  	GEA.STUDENT_ID,");
		sql.append("  	GEA.EXAM_PLAN_ID,");
		sql.append("  	GEA.EXAM_ROUND_ID,");
		sql.append("  	GEA.SEAT_NO,");
		sql.append("  	GEA.TYPE,");
		sql.append("  	GEA.XX_ID,");
		sql.append("  	GEA.CREATED_BY,");
		sql.append("  	GEA.CREATED_DT,");
		sql.append("  	GEA.UPDATED_BY,");
		sql.append("  	GEA.UPDATED_DT,");
		sql.append("  	GEA.STATUS,");
		sql.append("  	GEA.IS_DELETED");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  WHERE");
		sql.append("  	ROWNUM = 1");
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GEA.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GEA.EXAM_PLAN_ID = :EXAM_PLAN_ID ");
			params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 新增考试预约计划
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertAppointmentPlan(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_APPOINTMENT_NEW(");
		sql.append("  			APPOINTMENT_ID,");
		sql.append("  			STUDENT_ID,");
		sql.append("  			EXAM_PLAN_ID,");
		sql.append("  			TYPE,");
		sql.append("  			XX_ID,");
		sql.append("  			CREATED_BY,");
		sql.append("  			CREATED_DT,");
		sql.append("  			UPDATED_BY,");
		sql.append("  			UPDATED_DT,");
		sql.append("  			STATUS,");
		sql.append("  			IS_DELETED");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:APPOINTMENT_ID,");
		sql.append("  		:STUDENT_ID,");
		sql.append("  		:EXAM_PLAN_ID,");
		sql.append("  		:TYPE,");
		sql.append("  		:XX_ID,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		SYSDATE,");
		sql.append("  		:UPDATED_BY,");
		sql.append("  		SYSDATE,");
		sql.append("  		0,");
		sql.append("  		0");
		sql.append("  	)");
		
		params.put("APPOINTMENT_ID", ObjectUtils.toString(searchParams.get("APPOINTMENT_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		params.put("TYPE", ObjectUtils.toString(searchParams.get("TYPE")));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
		params.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
		
		return commonDao.insertForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 修改选课表状态
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int setRecResultExamState(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  SET");
		sql.append("  	GRR.EXAM_STATE = :EXAM_STATE ");
		sql.append("  WHERE");
		sql.append("  	GRR.REC_ID = :REC_ID ");
		
		params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));

		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	/**
	 *修改考试预约情况
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int inactiveExamState(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_RECORD GER");
		sql.append("  SET ");
		sql.append("  	GER.EXAM_STATE = '0' ");
		sql.append("  WHERE");
		sql.append("  	GER.REC_ID = :REC_ID ");
		
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 学员预约考试
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int bookExamRecordByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_RECORD(");
		sql.append("  			ID,");
		sql.append("  			REC_ID,");
		sql.append("  			TEACH_PLAN_ID,");
		sql.append("  			EXAM_STATE,");
		sql.append("  			BOOK_TIME,");
		sql.append("  			CREATED_DT,");
		sql.append("  			IS_CANCEL,");
		sql.append("  			STUDENT_ID");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:ID,");
		sql.append("  		:REC_ID,");
		sql.append("  		:TEACH_PLAN_ID,");
		sql.append("  		:EXAM_STATE,");
		sql.append("  		SYSDATE,");
		sql.append("  		SYSDATE,");
		sql.append("  		:IS_CANCEL,");
		sql.append("  		:STUDENT_ID ");
		sql.append("  	)");
		
		params.put("ID", ObjectUtils.toString(searchParams.get("EXAM_RECORD_ID")));
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		params.put("IS_CANCEL", ObjectUtils.toString(searchParams.get("IS_CANCEL")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.insertForMapNative(sql.toString(), params);

	}
	
	/**
	 * 查询学年度
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getStudyYearId(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GSI.ID,");
		sql.append("  	GSI.STUDY_YEAR_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_STUDYYEAR_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDY_YEAR_CODE"))){
			sql.append("  	AND GSI.STUDY_YEAR_CODE = :STUDY_YEAR_CODE ");
			params.put("STUDY_YEAR_CODE", ObjectUtils.toString(searchParams.get("STUDY_YEAR_CODE")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GSI.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		sql.append("  	AND ROWNUM <=1");

		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	/**
	 * 查询个人证件照
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryPersonalZP(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.SFZH,");
		sql.append("  	GSD.ID_NO,");
		sql.append("  	GSD.FILE_TYPE,");
		sql.append("  	GSD.URL,");
		sql.append("  	TO_CHAR(GSD.URL_NEW) URL_NEW");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI,");
		sql.append("  	GJT_SIGNUP GS,");
		sql.append("  	GJT_SIGNUP_DATA GSD");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GS.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = GS.STUDENT_ID");
		sql.append("  AND GS.STUDENT_ID = GSD.STUDENT_ID");
		sql.append("  AND GSD.FILE_TYPE = 'zp'");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID ");
		
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 更新个人证件照
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updatePersonalZP(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_SIGNUP_DATA GSD");
		sql.append("  SET");
		sql.append("  	GSD.URL_NEW = :URL_ADDRESS ");
		sql.append("  WHERE");
		sql.append("  	GSD.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GSD.FILE_TYPE = 'zp'");
		
		params.put("URL_ADDRESS", ObjectUtils.toString(searchParams.get("URL_ADDRESS")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 添加考试预约，并设置状态为预约状态
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveExamRecord(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_RECORD(");
		sql.append("  			ID,");
		sql.append("  			REC_ID,");
		sql.append("  			TEACH_PLAN_ID,");
		sql.append("  			EXAM_STATE,");
		sql.append("  			BOOK_TIME,");
		sql.append("  			CREATED_DT,");
		sql.append("  			IS_CANCEL,");
		sql.append("  			STUDENT_ID");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:RECORD_ID,");
		sql.append("  		:REC_ID,");
		sql.append("  		:TEACH_PLAN_ID,");
		sql.append("  		:EXAM_STATE,");
		sql.append("  		SYSDATE,");
		sql.append("  		SYSDATE,");
		sql.append("  		:IS_CANCEL,");
		sql.append("  		:STUDENT_ID");
		sql.append("  	)");
		
		params.put("RECORD_ID", ObjectUtils.toString(searchParams.get("RECORD_ID")));
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		params.put("IS_CANCEL", ObjectUtils.toString(searchParams.get("IS_CANCEL")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.insertForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 添加考试预约记录
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveAppointmentData(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_APPOINTMENT_NEW(");
		sql.append("  			APPOINTMENT_ID,");
		sql.append("  			STUDENT_ID,");
		sql.append("  			EXAM_PLAN_ID,");
		sql.append("  			TYPE,");
		sql.append("  			CREATED_BY,");
		sql.append("  			CREATED_DT,");
		sql.append("  			XX_ID,");
		sql.append("  			STATUS,");
		sql.append("  			EXAM_BATCH_CODE,");
		sql.append("  			REC_ID");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:APPOINTMENT_ID,");
		sql.append("  		:STUDENT_ID,");
		sql.append("  		:EXAM_PLAN_ID,");
		sql.append("  		:TYPE,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		SYSDATE,");
		sql.append("  		:XX_ID,");
		sql.append("  		:STATUS,");
		sql.append("  		:EXAM_BATCH_CODE,");
		sql.append("  		:REC_ID");
		sql.append("  	)");
		
		params.put("APPOINTMENT_ID", ObjectUtils.toString(searchParams.get("APPOINTMENT_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		params.put("TYPE", ObjectUtils.toString(searchParams.get("TYPE")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("STATUS", ObjectUtils.toString(searchParams.get("STATUS")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));

		return commonDao.insertForMapNative(sql.toString(), params);
	}
	
	
	/**
	 * 查询考试计划
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExamPlanBySubjectCode(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_PLAN_ID,");
		sql.append("  	GEP.XX_ID,");
		sql.append("  	GEP.TYPE");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 0 ");
		sql.append("  	AND GEP.EXAM_PLAN_ID = :EXAM_PLAN_ID ");

		params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询预约考试上限,最多预约不超过8门课程
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List makeAppointCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	COUNT(1) KS_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  INNER JOIN GJT_EXAM_PLAN_NEW GEP ON");
		sql.append("  	GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  WHERE");
		sql.append("  	GEA.IS_DELETED = 0");
		sql.append("  	AND GEP.IS_DELETED = 0");
		sql.append("  	AND GEA.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");

		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));

		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	
	
	/**
	 * 查询考试预约信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAppointmentByStuidAndPlanid(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEA.APPOINTMENT_ID,");
		sql.append("  	GEA.STUDENT_ID,");
		sql.append("  	GEA.EXAM_PLAN_ID,");
		sql.append("  	GEA.EXAM_ROUND_ID,");
		sql.append("  	GEA.SEAT_NO,");
		sql.append("  	GEA.TYPE,");
		sql.append("  	GEA.XX_ID,");
		sql.append("  	GEA.CREATED_BY,");
		sql.append("  	GEA.CREATED_DT,");
		sql.append("  	GEA.UPDATED_BY,");
		sql.append("  	GEA.UPDATED_DT,");
		sql.append("  	GEA.STATUS,");
		sql.append("  	GEA.IS_DELETED");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  WHERE");
		sql.append("  	GEA.IS_DELETED=0 AND GEA.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GEA.EXAM_PLAN_ID = :EXAM_PLAN_ID ");
		sql.append("  	AND GEA.REC_ID = :REC_ID ");
		sql.append("  	AND GEA.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");

		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));

		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 修改考试预约信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateAppointmentPlan(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  SET");
		sql.append("  	GEA.UPDATED_DT = SYSDATE,");
		sql.append("  	GEA.UPDATED_BY = :UPDATED_BY,");
		sql.append("  	GEA.STATUS = 0,");
		sql.append("  	GEA.IS_DELETED = :IS_DELETED ");
		sql.append("  WHERE 1 = 1");
		sql.append("  	AND GEA.APPOINTMENT_ID = :APPOINTMENT_ID ");
		sql.append("  	AND GEA.EXAM_PLAN_ID = :EXAM_PLAN_ID ");
		sql.append("  	AND GEA.STUDENT_ID = :STUDENT_ID ");
		sql.append("  	AND GEA.REC_ID = :REC_ID ");
		sql.append("  	AND GEA.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
		params.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
		params.put("IS_DELETED", Integer.parseInt(ObjectUtils.toString(searchParams.get("IS_DELETED"))));
		params.put("APPOINTMENT_ID", ObjectUtils.toString(searchParams.get("APPOINTMENT_ID")));
		params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));

		return commonDao.updateForMapNative(sql.toString(), params);
	}
	
	/**
	 * 修改选课表状态为已预约
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateRecResultState(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  SET");
		sql.append("  	GRR.BESPEAK_STATE = :BESPEAK_STATE ");
		sql.append("  WHERE");
		sql.append("  	GRR.IS_DELETED = 'N'");
		sql.append("  	AND GRR.REC_ID = :REC_ID ");
		
		params.put("BESPEAK_STATE", ObjectUtils.toString(searchParams.get("BESPEAK_STATE")));
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询当前选课状态
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getRecResultExamState(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GRR.REC_ID,");
		sql.append("  	GRR.EXAM_SCORE,");
		sql.append("  	GRR.TEACH_PLAN_ID,");
		sql.append("  	GRR.EXAM_STATE,");
		sql.append("  	GRR.BESPEAK_STATE,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  	GRR.CREATED_DT,");
		sql.append("  	GRR.STUDENT_ID");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  WHERE");
		sql.append("  	GRR.IS_DELETED = 'N'");
		sql.append("  	AND REC_ID = :REC_ID ");
		
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);

	}
	
	/**
	 * 查询学员预约考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAppointExamPointByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_POINT_ID,");
		sql.append("  	GEP.CODE,");
		sql.append("  	GEP.NAME AS POINT_NAME,");
		sql.append("  	GEP.EXAM_TYPE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = SUBSTR( GEP.AREA_ID, 1, 2 )|| '0000'");
		sql.append("  			) ||");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = SUBSTR( GEP.AREA_ID, 1, 4 )|| '00'");
		sql.append("  			) ||");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = GEP.AREA_ID");
		sql.append("  			) AREA_NAME,");
		sql.append("  	GEP.ADDRESS,");
		sql.append("  	NVL( GEPA.ID, '' ) AS EXAM_POINT");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_POINT_NEW GEP");
		sql.append("  LEFT JOIN GJT_EXAM_POINT_APPOINTMENT_NEW GEPA ON");
		sql.append("  	GEP.EXAM_POINT_ID = GEPA.EXAM_POINT_ID");
		sql.append("  	AND GEPA.IS_DELETED = 0");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 'N'");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GEP.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GEPA.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("	AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		
		sql.append("	AND ROWNUM <=2");
		sql.append(" ORDER BY  GEPA.CREATED_DT DESC ");

		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}



	public List getStuXjZtAndUserType(Map searchParams){
	    Map params = new HashMap();
        StringBuffer sql = new StringBuffer();

        sql.append("  SELECT");
        sql.append("  	GSI.STUDENT_ID,");
        sql.append("  	GSI.XJZT,");
		sql.append("  	GSI.USER_TYPE");
        sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
        sql.append("  WHERE");
        sql.append("  	GSI.IS_DELETED = 'N'");
        sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");

        params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));

        return commonDao.queryForMapListNative(sql.toString(),params);

    }


	/**
	 * 拿到订单号
	 * @param studentId
	 * @return
     */
	public Map<String, Object> getOrderSn(String studentId) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	ORDER_SN");
		sql.append("  FROM");
		sql.append("  	GJT_SIGNUP");
		sql.append("  WHERE");
		sql.append("  	IS_DELETED = 'N'");
		sql.append("  	AND STUDENT_ID = :STUDENT_ID ");

		params.put("STUDENT_ID", studentId);
		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(),params);
		return list != null && list.size() > 0 ?list.get(0) : null;
	}
	
	
	
	/**
	 * 统计网考,笔考,机考科目
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryCountSubject(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GTP.KKXQ TERM_CODE,");
		sql.append("  	GG.GRADE_ID TERM_ID,");
		sql.append("  	GG.GRADE_NAME TERM_NAME,");
		sql.append("  	GES.COURSE_ID,");
		sql.append("  	GES.NAME SUBJECT_NAME,");
		sql.append("  	GES.TYPE AS KSFS_FLAG,");
		sql.append("  	GRR.REC_ID,");
		sql.append("  	GTP.TEACH_PLAN_ID,");
		sql.append("  	GES.SUBJECT_ID,");
		sql.append("  	GES.SUBJECT_CODE,");
		sql.append("  	GEP.EXAM_PLAN_ID,");
		sql.append("  	GRR.EXAM_SCORE,");
		sql.append("  	GRR.EXAM_SCORE1,");
		sql.append("  	GRR.EXAM_SCORE2,");
		sql.append("  	GRR.EXAM_STATE");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE GG,");
		sql.append("  	GJT_REC_RESULT GRR,");
		sql.append("  	VIEW_TEACH_PLAN GTP,");
		sql.append("  	GJT_EXAM_SUBJECT_NEW GES,");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP,");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GTP.IS_DELETED = 'N'");
		sql.append("  	AND GES.IS_DELETED = 0");
		sql.append("  	AND GEP.IS_DELETED = 0");
		sql.append("  	AND GEB.IS_DELETED = 0");
		sql.append("  	AND GG.IS_DELETED = 'N'");
		sql.append("  	AND GRR.IS_DELETED = 'N'");
		sql.append("  	AND GES.COURSE_ID = GTP.COURSE_ID");
		sql.append("  	AND GES.KCH = GTP.COURSE_CODE");
		sql.append("  	AND GES.SUBJECT_CODE = GEP.SUBJECT_CODE");
		sql.append("  	AND GES.TYPE = GEP.TYPE");
		sql.append("  	AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  	AND GEB.GRADE_ID = GG.GRADE_ID");
		sql.append("  	AND GRR.COURSE_ID = GTP.COURSE_ID");
		sql.append("  	AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  	AND GRR.COURSE_ID = GES.COURSE_ID");
		if(EmptyUtils.isNotEmpty(searchParams.get("KKZY"))){
			sql.append("  	AND GTP.KKZY = :KKZY ");
			params.put("KKZY", ObjectUtils.toString(searchParams.get("KKZY")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("PYCC"))){
			sql.append("  	AND GTP.PYCC = :PYCC ");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("KSFS_FLAG"))){
			sql.append("	AND GES.TYPE = :KSFS_FLAG ");
			params.put("KSFS_FLAG", Integer.parseInt(ObjectUtils.toString(searchParams.get("KSFS_FLAG"))));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_STATE"))){
			sql.append("	AND GRR.EXAM_STATE = :EXAM_STATE ");
			params.put("EXAM_STATE", ObjectUtils.toString(searchParams.get("EXAM_STATE")));
		}
		if("0".equals(ObjectUtils.toString(searchParams.get("DKS_FLAG")))){
			sql.append("  	AND TO_CHAR( SYSDATE, 'yyyy-MM-dd hh24:mi:ss' )< TO_CHAR( GTP.EXAM_STIME, 'yyyy-MM-dd hh24:mi:ss' )");
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	
	/**
	 * 查询当前学期
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getCurrentTerm(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GG.GRADE_ID,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	GG.XX_ID");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE GG");
		sql.append("  WHERE");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GG.XX_ID = :XX_ID ");
		sql.append("  	AND SYSDATE BETWEEN GG.START_DATE AND NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 ))");
		sql.append("  	AND ROWNUM <=1");

		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询考点是否有效
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List isPointAppointAvailable(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT( 1 )  IS_POINTAPPOINT_AVAILABLE");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_CONFIG GEC");
		sql.append("  WHERE");
		sql.append("  	GEC.ORG_CODE LIKE(");
		sql.append("  		SELECT");
		sql.append("  			SUBSTR( GSI.ORG_CODE, 0, 8 )");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO GSI");
		sql.append("  		WHERE");
		sql.append("  			GSI.IS_DELETED = 'N'");
		sql.append("  			AND GSI.STUDENT_ID = :STUDENT_ID ");
		sql.append("  			AND ROWNUM = 1");
		sql.append("  	)");
//		sql.append("  	AND GEC.SDATE < SYSDATE");
//		sql.append("  	AND GEC.EDATE > SYSDATE");

		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	
	/**
	 * 查询预约时间配置
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List appointmentTimes(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TO_CHAR( GEB.BOOK_ST, 'yyyy-MM-dd' ) BOOK_ST,");
		sql.append("  	TO_CHAR( GEB.BOOK_END, 'yyyy-MM-dd' ) BOOK_END,");
		sql.append("  	TO_CHAR( GEB.BOOKS_ST, 'yyyy-MM-dd' ) BOOKS_ST,");
		sql.append("  	TO_CHAR( GEB.BOOKS_END, 'yyyy-MM-dd' ) BOOKS_END");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GEB.IS_DELETED = 0");

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		sql.append("  	AND ROWNUM <= 1");
		sql.append("  ORDER BY");
		sql.append("  	GEB.CREATED_DT DESC");


		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	/**
	 * 查询学员是否预约过考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getIsExistAppointmentPoint(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
//		sql.append("  SELECT");
//		sql.append("  	COUNT(1) TEMP");
//		sql.append("  FROM");
//		sql.append("  	GJT_EXAM_POINT_APPOINTMENT_NEW GEPA");
//		sql.append("  WHERE");
//		sql.append("  	GEPA.IS_DELETED = 0");
//		sql.append("  	AND GEPA.ID = :EXAM_POINT ");
//		sql.append("  	AND GEPA.STUDENT_ID = :STUDENT_ID ");
//		sql.append("  	AND GEPA.EXAM_POINT_ID = :EXAM_POINT_ID ");
//		sql.append("  	AND GEPA.GRADE_ID = :TERM_ID ");
//
//		params.put("EXAM_POINT", ObjectUtils.toString(searchParams.get("EXAM_POINT")));
//		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
//		params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
//		params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		
		sql.append("  SELECT");
		sql.append("  	GEPA.ID EXAM_POINT,");
		sql.append("  	GEPA.STUDENT_ID,");
		sql.append("  	GEPA.EXAM_POINT_ID");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_POINT_APPOINTMENT_NEW GEPA");
		sql.append("  WHERE ");
		sql.append("  	 GEPA.IS_DELETED = 0");
		sql.append("  	AND GEPA.ID = :EXAM_POINT ");

		params.put("EXAM_POINT", ObjectUtils.toString(searchParams.get("EXAM_POINT")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 修改预约考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateAppointmentPoint(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
//		sql.append("  UPDATE");
//		sql.append("  	GJT_EXAM_POINT_APPOINTMENT_NEW GEPA");
//		sql.append("  SET");
//		sql.append("  	GEPA.UPDATED_DT = SYSDATE,");
//		sql.append("  	GEPA.UPDATED_BY = :UPDATED_BY,");
//		sql.append("  	GEPA.IS_DELETED = 1 ");
//		sql.append("  WHERE");
//		sql.append("  	GEPA.ID = :EXAM_POINT");
//		sql.append("  	AND GEPA.STUDENT_ID = :STUDENT_ID ");
//		sql.append("  	AND GEPA.GRADE_ID = :TERM_ID ");
//		
//		params.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
//		params.put("EXAM_POINT", ObjectUtils.toString(searchParams.get("EXAM_POINT")));
//		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
//		params.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_POINT_APPOINTMENT_NEW GEPA");
		sql.append("  SET");
		sql.append("  	GEPA.UPDATED_DT = SYSDATE,");
		sql.append("  	GEPA.CREATED_DT = SYSDATE,");
		sql.append("  	GEPA.UPDATED_BY = :UPDATED_BY,");
		sql.append("  	GEPA.EXAM_POINT_ID = :EXAM_POINT_ID");
		sql.append("  WHERE");
		sql.append("  	GEPA.ID = :EXAM_POINT ");
		sql.append("  	AND GEPA.STUDENT_ID = :STUDENT_ID ");
		
		params.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		params.put("EXAM_POINT", ObjectUtils.toString(searchParams.get("EXAM_POINT")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	
	/**
	 * 查询可预约的考试
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAppointmentExamList(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.TERM_CODE,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	TAB.TERM_NAME,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.KCH,");
		sql.append("  	TAB.COURSE_COST,");
		sql.append("  	TAB.MAKEUP,");
		sql.append("  	TAB.PAY_STATE,");
		sql.append("  	TAB.BESPEAK_STATE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.COURSE_CODE,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.KSFS,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.URL_NEW");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_CODE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  					AND GG.IS_DELETED = 'N'");
		sql.append("  			) TERM_CODE,");
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
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			GC.KCH,");
		sql.append("  			NVL(( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = GC.KCH ),( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = 'other' )) COURSE_COST,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  			GRR.BESPEAK_STATE,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GTP.COURSE_CODE,");
		sql.append("  			GTP.KKZY,");
		sql.append("  			GTP.KSFS,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			C.USER_TYPE,");
		sql.append("  			C.URL_NEW");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");

		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  			AND GRR.STUDENT_ID = :STUDENT_ID");
			params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  			AND GTP.COURSE_CODE = GC.KCH");
		sql.append("  		LEFT JOIN(");
		sql.append("  				SELECT");
		sql.append("  					GSI.STUDENT_ID,");
		sql.append("  					GSI.SFZH,");
		sql.append("  					GSI.USER_TYPE,");
		sql.append("  					GSI.XJZT,");
		sql.append("  					GSD.ID_NO,");
		sql.append("  					GSD.FILE_TYPE,");
		sql.append("  					GSD.URL,");
		sql.append("  					TO_CHAR( GSD.URL_NEW ) URL_NEW");
		sql.append("  				FROM");
		sql.append("  					GJT_STUDENT_INFO GSI,");
		sql.append("  					GJT_SIGNUP GS,");
		sql.append("  					GJT_SIGNUP_DATA GSD");
		sql.append("  				WHERE");
		sql.append("  					GSI.IS_DELETED = 'N'");
		sql.append("  					AND GS.IS_DELETED = 'N'");
		sql.append("  					AND GSI.STUDENT_ID = GS.STUDENT_ID");
		sql.append("  					AND GS.STUDENT_ID = GSD.STUDENT_ID");
		sql.append("  					AND GSD.FILE_TYPE = 'zp'");
		sql.append("  			) C ON");
		sql.append("  			C.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GTP.KSFS NOT IN ('21','17','18','14','15') ");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GTP.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			TERM_CODE");
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		sql.append("	AND TAB.BESPEAK_STATE NOT IN ('2')");

//		//可参加三种考试的学员类型:正式跟读生;电大续读生;外校预科生
//		if ("12".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
//				|| "42".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
//				|| "51".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))) {
//			sql.append("	AND TAB.KSFS IN ('7','8','19') ");
//		}
//		//只可以参加学院网考的学生类型:非正式跟读生;课程预读生;本科预读生
//		if ("13".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
//				|| "41".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
//				|| "61".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))) {
//			sql.append("	AND TAB.KSFS  = '7' ");
//		}		
//		//学籍状态是0,3只能参加网考
//		if ("0".equals(ObjectUtils.toString(searchParams.get("XJZT")))
//				|| "3".equals(ObjectUtils.toString(searchParams.get("XJZT")))) {
//			sql.append("	AND TAB.KSFS  = '7' ");
//		}
		// 2018春新规 可参加条件
		// 正式生，学籍状态为在籍和注册中      笔试、形考(登录个人学习中心)、网考、易考通、机考、形考(登录国开学习网)、论文、实践报告
		if ("11".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
				&& ("2".equals(ObjectUtils.toString(searchParams.get("XJZT"))) || "0".equals(ObjectUtils.toString(searchParams.get("XJZT"))))) {
			sql.append("  	AND GEP.TYPE IN (8,13,7,19,11,22,14,20) ");
		}
			// 正式跟读生和电大续读生，学籍状态为待注册      笔试、形考(登录个人学习中心)、网考、易考通、
		 else if (("12".equals(ObjectUtils.toString(searchParams.get("USER_TYPE"))) || "42".equals(ObjectUtils.toString(searchParams.get("USER_TYPE"))))
				&& "3".equals(ObjectUtils.toString(searchParams.get("XJZT")))) {
			sql.append("  	AND GEP.TYPE IN (8,13,7,19) ");
		} else {
			sql.append("  	AND 1=2 ");
		}

		if("0".equals(ObjectUtils.toString(searchParams.get("BESPEAK_STATE")))){ //待预约
			sql.append("	AND TAB.BESPEAK_STATE = :BESPEAK_STATE ");
			params.put("BESPEAK_STATE", ObjectUtils.toString(searchParams.get("BESPEAK_STATE")));
		}

		/*

		if("1".equals(ObjectUtils.toString(searchParams.get("BESPEAK_STATE")))){ //已预约
			sql.append("	AND TAB.BESPEAK_STATE = :BESPEAK_STATE");
			params.put("BESPEAK_STATE", ObjectUtils.toString(searchParams.get("BESPEAK_STATE")));
			sql.append("    AND SYSDATE BETWEEN TAB.BATCH_BOOK_ST AND TAB.BATCH_BOOK_END ");
		}


		if("3".equals(ObjectUtils.toString(searchParams.get("BESPEAK_STATE")))){ //包含待预约与已预约
			sql.append("  	AND(");
			sql.append("  		TAB.BESPEAK_STATE IN(");
			sql.append("  			'0',");
			sql.append("  			'1'");
			sql.append("  		)");
			sql.append("  		AND SYSDATE BETWEEN TAB.BATCH_BOOK_ST AND TAB.BATCH_BOOK_END ");
			sql.append("  	)");
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("	AND TAB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		*/


		return commonDao.queryForMapListNative(sql.toString(), params);
	}


	/**
	 * 查询课程对应的开考科目信息
	 * @param searchParams
	 * @return
	 */
	public List getExamPlanInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	DISTINCT GEP.EXAM_PLAN_ID,");
		sql.append("  	GEP.EXAM_NO,");
		sql.append("  	TO_CHAR( GEP.EXAM_ST, 'yyyy-MM-dd hh24:mi' ) EXAM_STIME,");
		sql.append("  	TO_CHAR( GEP.EXAM_END, 'yyyy-MM-dd hh24:mi' ) EXAM_ETIME,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  			WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  			WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  			ELSE '--'");
		sql.append("  		END");
		sql.append("  	) EXAM_STATE,");
		sql.append("  	GEP.TYPE,");
		sql.append("  	GEP.EXAM_BATCH_CODE,");
		sql.append("  	VEPC.SPECIALTY_ID");
		sql.append("  FROM");
		sql.append("  	VIEW_EXAM_PLAN_SC VEPC");
		sql.append("  INNER JOIN GJT_EXAM_PLAN_NEW GEP ON");
		sql.append("  	VEPC.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 0");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GEP.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND VEPC.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("KSFS_FLAG"))){
			sql.append("  	AND VEPC.TYPE = :KSFS_FLAG ");
			params.put("KSFS_FLAG",Integer.parseInt(ObjectUtils.toString(searchParams.get("KSFS_FLAG"))));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("COURSE_ID"))){
			sql.append("  	AND VEPC.COURSE_ID = :COURSE_ID ");
			params.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		return commonDao.queryForMapListNative(sql.toString(),params);

	}

	/**
	 * 查询开考科目与与课程直接的关联
	 * @param searchParams
	 * @return
	 */
	public List getAppointmentDataByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_PLAN_ID,");
		sql.append("  	GEP.EXAM_BATCH_CODE,");
		sql.append("  	GEP.EXAM_NO,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  			AND TSD.CODE = GEP.TYPE");
		sql.append("  	) EXAM_STYLE,");
		sql.append("  	GEP.TYPE KSFS_FLAG,");
		sql.append("    (");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  			WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  			WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  			ELSE '--'");
		sql.append("  		END");
		sql.append("  	 ) EXAM_STATE,");
		sql.append("  	TO_CHAR(GEP.EXAM_ST,'yyyy-MM-dd hh24:mi') EXAM_STIME,");
		sql.append("  	TO_CHAR(GEP.EXAM_END,'yyyy-MM-dd hh24:mi') EXAM_ETIME,");
		sql.append("  	TO_CHAR(GEB.BOOK_ST,'yyyy-MM-dd hh24:mi') BOOK_STARTTIME,");
		sql.append("  	TO_CHAR(GEB.BOOK_END,'yyyy-MM-dd hh24:mi') BOOK_ENDTIME,");
		sql.append("  	GC.COURSE_ID,");
		sql.append("  	GC.KCMC COURSE_NAME,");
		sql.append("  	GC.KCH");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP");
		sql.append("  INNER JOIN GJT_EXAM_PLAN_NEW_COURSE GEPC ON");
		sql.append("  	GEP.EXAM_PLAN_ID = GEPC.EXAM_PLAN_ID");
		sql.append("  INNER JOIN GJT_COURSE GC ON");
		sql.append("  	GEPC.COURSE_ID = GC.COURSE_ID");
		sql.append("  INNER JOIN GJT_EXAM_BATCH_NEW GEB ON");
		sql.append("  	GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 0");
		sql.append("  	AND GC.IS_DELETED = 'N'");
		sql.append("  	AND GEB.IS_DELETED = 0");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GC.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("KCH"))){
			sql.append("  	AND GC.KCH = :KCH ");
			params.put("KCH",ObjectUtils.toString(searchParams.get("KCH")));
		}

		//sql.append("	AND SYSDATE BETWEEN GEB.BOOK_ST AND GEB.BOOK_END");

		return commonDao.queryForMapListNative(sql.toString(),params);

	}

	/**
	 * 查询专业与课程的关联
	 * @param searchParams
	 * @return
	 */
	public List getViewExamPlanData(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

        sql.append("  SELECT");
        sql.append("  	VEPC.EXAM_PLAN_ID,");
        sql.append("  	VEPC.COURSE_ID,");
        sql.append("  	VEPC.TYPE,");
        sql.append("  	VEPC.SPECIALTY_ID,");
        sql.append("  	GEP.EXAM_NO,");
		sql.append("    (");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  			WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  			WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  			ELSE '--'");
		sql.append("  		END");
		sql.append("  	) EXAM_STATE,");
		sql.append("  	TO_CHAR(GEP.EXAM_ST,'yyyy-MM-dd hh24:mi') EXAM_ST,");
        sql.append("  	TO_CHAR(GEP.EXAM_END,'yyyy-MM-dd hh24:mi') EXAM_END");
        sql.append("  FROM");
        sql.append("  	VIEW_EXAM_PLAN_SC VEPC");
        sql.append("  LEFT JOIN GJT_EXAM_PLAN_NEW GEP ON");
        sql.append("  	VEPC.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
        sql.append("  	AND GEP.IS_DELETED = 0");
        sql.append("  WHERE");

        if(EmptyUtils.isNotEmpty(searchParams.get("COURSE_ID"))){
			sql.append("  	VEPC.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND VEPC.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("TYPE"))){
			sql.append("  	AND VEPC.TYPE = :TYPE ");
			params.put("TYPE",Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		}

		return commonDao.queryForMapListNative(sql.toString(),params);

	}




	
	/**
	 * 下载准考证信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAdmissionData(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	DISTINCT GSI.STUDENT_ID,");
		sql.append("  	GSI.XM AS ADMISSION_NAME,");
		sql.append("  	GSI.XH AS STU_NUMBER,");
		sql.append("  	TO_CHAR( GSD.URL_NEW ) STU_PHOTO,");
		sql.append("  	GEP.NAME AS EXAM_POINT_NAME,");
		sql.append("  	GEP.ADDRESS AS EXAM_ADDRESS,");
		sql.append("  	GSO.XXMC,");
		sql.append("  	GSO.XXMC || GG.GRADE_NAME || '准考证' AS ADMISSION_ZKZ,");
		sql.append("  	GG.GRADE_NAME AS STUDY_YEAR_NAME,");
		sql.append("  	A.EXAM_NO,");
		sql.append("  	A.EXAM_PLAN_NAME COURSE_NAME,");
		sql.append("  	A.EXAM_TYPE,");
		sql.append("  	A.EXAM_STYLE,");
		sql.append("  	A.EXAM_DATE,");
		sql.append("  	A.EXAM_TIME,");
		sql.append("  	A.SEAT_NO,");
		sql.append("  	A.TYPE,");
		sql.append("  	A.PLAN_COUNT,");
		sql.append("  	A.EXAM_ST");
		sql.append("  FROM");
		sql.append("  	GJT_SCHOOL_INFO GSO,");
		sql.append("  	GJT_EXAM_POINT_NEW GEP");
		sql.append("  LEFT JOIN GJT_EXAM_BATCH_NEW GEBN ON GEBN.EXAM_BATCH_ID=GEP.EXAM_BATCH_ID");
		sql.append("  LEFT JOIN GJT_GRADE GG ON GG.GRADE_ID=GEBN.GRADE_ID");
		sql.append("  LEFT JOIN GJT_EXAM_POINT_APPOINTMENT_NEW GEPA ON");
		sql.append("  	GEPA.EXAM_POINT_ID = GEP.EXAM_POINT_ID");
		sql.append("  LEFT JOIN GJT_STUDENT_INFO GSI ON");
		sql.append("  	GSI.STUDENT_ID = GEPA.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP_DATA GSD ON");
		sql.append("  	GSI.STUDENT_ID = GSD.STUDENT_ID");
		sql.append("  	AND GSD.FILE_TYPE = 'zp' ");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			GEA.STUDENT_ID,");
		sql.append("  			GEP.EXAM_PLAN_NAME,");
		sql.append("  			GEP.EXAM_NO,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					TSD.NAME");
		sql.append("  				FROM");
		sql.append("  					TBL_SYS_DATA TSD");
		sql.append("  				WHERE");
		sql.append("  					TSD.IS_DELETED = 'N'");
		sql.append("  					AND TSD.TYPE_CODE = 'EXAM_STYLE'");
		sql.append("  					AND TSD.CODE = GEP.EXAM_STYLE");
		sql.append("  			) EXAM_TYPE,");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					TSD.NAME");
		sql.append("  				FROM");
		sql.append("  					TBL_SYS_DATA TSD");
		sql.append("  				WHERE");
		sql.append("  					TSD.IS_DELETED = 'N'");
		sql.append("  					AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  					AND TSD.CODE = GEP.TYPE");
		sql.append("  			) EXAM_STYLE,");
		sql.append("  			GEP.EXAM_ST,");
		sql.append("  			TO_CHAR( GEP.EXAM_ST, 'yyyy-MM-dd' ) AS EXAM_DATE,");
		sql.append("  			TO_CHAR( GEP.EXAM_ST, 'hh24:mi' )|| '~' || TO_CHAR( GEP.EXAM_END, 'hh24:mi' ) AS EXAM_TIME,");
		sql.append("  			GER.NAME || GESR.SEAT_NO AS SEAT_NO,");
		sql.append("  			GEP.TYPE,");
		sql.append("  			GEP.EXAM_BATCH_CODE,");
		sql.append("  			GESR.EXAM_ROOM_ID,");
		
		sql.append("  			(SELECT COUNT(GEPN.EXAM_PLAN_ID)");
		sql.append("  			FROM GJT_EXAM_APPOINTMENT_NEW GEAN,");
		sql.append("  			GJT_EXAM_PLAN_NEW        GEPN");
		sql.append("  			WHERE GEAN.IS_DELETED = 0");
		sql.append("  			AND GEPN.IS_DELETED = 0");
		sql.append("  			AND GEAN.EXAM_PLAN_ID = GEPN.EXAM_PLAN_ID");
		sql.append("  			AND GEAN.STUDENT_ID = GEA.STUDENT_ID");
		sql.append("  			AND GEPN.EXAM_BATCH_CODE = GEP.EXAM_BATCH_CODE");
		sql.append("  			AND GEPN.EXAM_ST = GEP.EXAM_ST");
		sql.append("  			AND GEPN.EXAM_END = GEP.EXAM_END) PLAN_COUNT");
		
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  		INNER JOIN GJT_EXAM_STUDENT_ROOM_NEW GESR ON");
		sql.append("  			GEA.APPOINTMENT_ID = GESR.APPOINTMENT_ID");
		sql.append("  		INNER JOIN GJT_EXAM_PLAN_NEW GEP ON");
		sql.append("  			GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  		INNER JOIN Gjt_Rec_Result GRR ON");
		sql.append("  			GRR.Rec_Id=GEA.Rec_Id");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GRR.COURSE_ID = GC.COURSE_ID");
		sql.append("  		LEFT JOIN GJT_EXAM_ROOM_NEW GER ON");
		sql.append("  			GESR.EXAM_ROOM_ID = GER.EXAM_ROOM_ID");
		sql.append("  			AND GER.IS_DELETED = 0");
		sql.append("  		WHERE");
		sql.append("  			GEA.IS_DELETED = 0");

		if(EmptyUtils.isNotEmpty(searchParams.get("TYPE"))){
			sql.append("  	AND GEA.TYPE = :TYPE ");
			params.put("TYPE", Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		}

		sql.append("  			AND GEA.STATUS = 1");
		sql.append("  			AND GEP.IS_DELETED = 0");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  			AND GEA.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  			AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		sql.append("  	) A ON");
		sql.append("  	A.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GSO.IS_DELETED = 'N'");
		sql.append("  	AND GEP.IS_DELETED = 'N'");
		sql.append("  	AND GEPA.IS_DELETED = 0");
		sql.append("  	AND GSI.IS_DELETED = 'N'");
		sql.append("  	AND GSO.ID = GEP.XX_ID");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GSI.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  			AND GSI.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}


		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("TYPE"))){
			sql.append("  	AND GEP.EXAM_TYPE = :TYPE ");
			params.put("TYPE", Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		}

		sql.append(" ORDER BY EXAM_ST");
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 判断报名资料表是否存在记录
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getIsExistZP(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT( 1 ) TEMP");
		sql.append("  FROM");
		sql.append("  	GJT_SIGNUP_DATA GSD");
		sql.append("  WHERE");
		sql.append("  	GSD.FILE_TYPE = 'zp'");
		sql.append("  	AND GSD.STUDENT_ID = :STUDENT_ID ");
		
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 保存预约考试上传个人照片
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertSiguUpData(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_SIGNUP_DATA(");
		sql.append("  			ID,");
		sql.append("  			STUDENT_ID,");
		sql.append("  			ID_NO,");
		sql.append("  			FILE_TYPE,");
		sql.append("  			URL_NEW,AUDIT_DATE");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:SING_ID,");
		sql.append("  		:STUDENT_ID,");
		sql.append("  		:ID_NO,");
		sql.append("  		:FILE_TYPE,");
		sql.append("  		:URL_NEW,SYSDATE");
		sql.append("  	)");

		params.put("SING_ID", ObjectUtils.toString(searchParams.get("SING_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("ID_NO", ObjectUtils.toString(searchParams.get("ID_NO")));
		params.put("FILE_TYPE", ObjectUtils.toString(searchParams.get("FILE_TYPE")));
		params.put("URL_NEW", ObjectUtils.toString(searchParams.get("URL_NEW")));
		
		return commonDao.insertForMapNative(sql.toString(), params);
		
	}
	
	
	/**
	 * 查询是否需要缴费
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getIsPayStateByCourse(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GRR.REC_ID,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  	GRR.BESPEAK_STATE");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  WHERE");
		sql.append("  	GRR.IS_DELETED = 'N'");
		sql.append("  	AND GRR.REC_ID = :REC_ID ");
		
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	
	/**
	 * 更新缴费状态
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean updatePayState(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  SET");
		sql.append("  	GRR.UPDATED_DT = SYSDATE,");
		sql.append("  	GRR.UPDATED_BY = :UPDATED_BY ,");
		sql.append("  	GRR.PAY_STATE = :PAY_STATE,");
		sql.append("	GRR.ORDER_MARK = :ORDER_MARK ");
		sql.append("  WHERE");
		sql.append("  	GRR.REC_ID = :REC_ID AND GRR.STUDENT_ID=:STUDENT_ID");
		
		params.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("PAY_STATE", ObjectUtils.toString(searchParams.get("PAY_STATE")));
		params.put("ORDER_MARK", ObjectUtils.toString(searchParams.get("ORDER_MARK")));
		params.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));

		return commonDao.updateForMapNative(sql.toString(), params) == 1;

		
	}
	/**
	 * 更新订单号
	 * @param recId
	 * @param orderMark
	 * @return
	 */
	public int updateOrderNoById(String recId, String orderMark){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE");
		sql.append("  	GJT_REC_RESULT GRR");
		sql.append("  SET");
		sql.append("	GRR.ORDER_MARK=:ORDER_MARK ");
		sql.append("  WHERE");
		sql.append("  	GRR.REC_ID=:REC_ID AND GRR.PAY_STATE='0'");

		params.put("REC_ID", recId);
		params.put("ORDER_MARK", orderMark);
		return commonDao.updateForMapNative(sql.toString(), params);


	}
	
	/**
	 * 查询报考ID
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getSignUpId(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GS.SIGNUP_ID");
		sql.append("  FROM");
		sql.append("  	GJT_SIGNUP GS,");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GSI.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		
		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GS.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAppointmentByStudentAndTeachPlanId(Map searchParams){
		Map params = new HashMap();
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append("  SELECT");
    	sql.append("  	GEA.APPOINTMENT_ID,");
    	sql.append("  	GEA.STUDENT_ID,");
    	sql.append("  	GEA.EXAM_PLAN_ID,");
    	sql.append("  	GEA.EXAM_ROUND_ID,");
    	sql.append("  	GEA.SEAT_NO,");
    	sql.append("  	GEA.TYPE,");
    	sql.append("  	GEA.XX_ID,");
    	sql.append("  	GEA.CREATED_BY,");
    	sql.append("  	GEA.CREATED_DT,");
    	sql.append("  	GEA.UPDATED_BY,");
    	sql.append("  	GEA.UPDATED_DT,");
    	sql.append("  	GEA.IS_DELETED,");
    	sql.append("  	GEA.STATUS,");
    	sql.append("  	GEP.EXAM_NO");
    	sql.append("  FROM");
    	sql.append("  	GJT_EXAM_APPOINTMENT_NEW GEA,");
    	sql.append("  	GJT_EXAM_PLAN_NEW GEP,");
    	sql.append("  	VIEW_TEACH_PLAN GTP,");
    	sql.append("  	GJT_GRADE GG,");
    	sql.append("  	GJT_COURSE GC,");
		sql.append("  	GJT_EXAM_PLAN_NEW_COURSE GEPC");
    	sql.append("  WHERE");
    	sql.append("  	GEA.IS_DELETED = 0");
    	sql.append("  	AND GEP.IS_DELETED = 0");
    	sql.append("  	AND GTP.IS_DELETED = 'N'");
    	sql.append("  	AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
    	sql.append("  	AND GC.COURSE_ID = GTP.COURSE_ID ");
    	sql.append("  	AND GC.COURSE_ID = GEPC.COURSE_ID");
    	sql.append("    AND GEP.EXAM_PLAN_ID = GEPC.EXAM_PLAN_ID");
    	sql.append("  	AND GEP.GRADE_ID = GG.GRADE_ID");
    	sql.append("  	AND GEA.STUDENT_ID = :STUDENT_ID ");
    	sql.append("  	AND GTP.TEACH_PLAN_ID = :TEACH_PLAN_ID ");
    	sql.append("    ORDER BY  GEA.UPDATED_DT DESC ") ;
    	params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
    	params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		return commonDao.queryForMapListNative(sql.toString(), params);
    	
    }
	
	/**
	 * 查询当前考试计划标志
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getCurrentExamBathcFlag(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEB.BOOK_ST,");
		sql.append("  	GEB.BOOK_END,(");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE BETWEEN GEB.BOOK_ST AND GEB.BOOK_END THEN '0'");
		sql.append("  			WHEN SYSDATE > GEB.BOOK_END THEN '1'");
		sql.append("  			ELSE '--'");
		sql.append("  		END");
		sql.append("  	) TIME_FLAG");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GEB.IS_DELETED = 0");
		sql.append("  	AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");

		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询预约记录详情
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAppointmentRecordDetail(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.TERM_CODE,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = TAB.KKXQ");
		sql.append("  			AND TSD.TYPE_CODE = 'KKXQ'");
		sql.append("  	) TERM_NAME,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = TAB.EXAM_SCORE1 ), TAB.EXAM_SCORE1 )) EXAM_SCORE,");
		sql.append("  	TAB.KCH,");
		sql.append("  	TAB.COURSE_COST,");
		sql.append("  	TAB.MAKEUP,");
		sql.append("  	TAB.PAY_STATE,");
		sql.append("  	TAB.BESPEAK_STATE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.COURSE_CODE,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.KSFS,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.URL_NEW");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_CODE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) TERM_CODE,");
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
		sql.append("  			GTP.KKXQ,");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.KCMC AS COURSE_NAME,");
		sql.append("  			GC.KCH,");
		sql.append("  			NVL(( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = GC.KCH ),( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = 'other' )) COURSE_COST,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  			GRR.BESPEAK_STATE,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GTP.COURSE_CODE,");
		sql.append("  			GTP.KKZY,");
		sql.append("  			GTP.KSFS,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			GRR.EXAM_SCORE,");
		sql.append("  			GRR.EXAM_SCORE1,");
		sql.append("  			GRR.EXAM_SCORE2,");
		sql.append("  			C.URL_NEW");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");

		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  			AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  			AND GTP.COURSE_CODE = GC.KCH");
		sql.append("  		LEFT JOIN(");
		sql.append("  				SELECT");
		sql.append("  					GSI.STUDENT_ID,");
		sql.append("  					GSI.SFZH,");
		sql.append("  					GSI.USER_TYPE,");
		sql.append("  					GSI.XJZT,");
		sql.append("  					GSD.ID_NO,");
		sql.append("  					GSD.FILE_TYPE,");
		sql.append("  					GSD.URL,");
		sql.append("  					TO_CHAR( GSD.URL_NEW ) URL_NEW");
		sql.append("  				FROM");
		sql.append("  					GJT_STUDENT_INFO GSI,");
		sql.append("  					GJT_SIGNUP GS,");
		sql.append("  					GJT_SIGNUP_DATA GSD");
		sql.append("  				WHERE");
		sql.append("  					GSI.IS_DELETED = 'N'");
		sql.append("  					AND GS.IS_DELETED = 'N'");
		sql.append("  					AND GSI.STUDENT_ID = GS.STUDENT_ID");
		sql.append("  					AND GS.STUDENT_ID = GSD.STUDENT_ID");
		sql.append("  					AND GSD.FILE_TYPE = 'zp'");
		sql.append("  			) C ON");
		sql.append("  			C.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GTP.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		sql.append("  			AND GRR.BESPEAK_STATE NOT IN('2')");

		if(EmptyUtils.isNotEmpty(searchParams.get("REC_ID"))){
			sql.append("  			AND GRR.REC_ID = :REC_ID ");
			params.put("REC_ID",ObjectUtils.toString(searchParams.get("REC_ID")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			TERM_CODE");
		sql.append("  	) TAB");

		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}

	/**
	 * 根据开考科目ID得到考试方式
	 * @param searchParams
	 * @return
	 */
	public List getExamPlanType(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_PLAN_ID,");
		sql.append("  	GEP.EXAM_NO,");
		sql.append("  	GEP.TYPE AS KSFS_FLAG,");
		sql.append("  	GEP.EXAM_BATCH_CODE ");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 0");

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_PLAN_ID"))){
			sql.append("  	AND GEP.EXAM_PLAN_ID = :EXAM_PLAN_ID ");
			params.put("EXAM_PLAN_ID",ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		}

		return commonDao.queryForMapListNative(sql.toString(),params);

	}
	
	
	/**
	 * 保存缴费记录
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveExamCostRecord(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_COST(");
		sql.append("  			COST_ID,PAY_SN,");
		sql.append("  			STUDENT_ID,");
		sql.append("  			COURSE_ID,");
		sql.append("  			GRADE_ID,");
		sql.append("  			TEACH_PLAN_ID,");
		sql.append("  			EXAM_BATCH_CODE,");
		sql.append("  			EXAM_PLAN_ID,");
		sql.append("  			KSFS,");
		sql.append("  			MAKEUP,");
		sql.append("  			PAY_STATUS,");
		sql.append("  			COURSE_COST,");
		sql.append("  			COURSE_CODE,");
		sql.append("  			CREATED_BY,");
		sql.append("  			CREATED_DT");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:COST_ID,:PAY_SN,");
		sql.append("  		:STUDENT_ID,");
		sql.append("  		:COURSE_ID,");
		sql.append("  		:GRADE_ID,");
		sql.append("  		:TEACH_PLAN_ID,");
		sql.append("  		:EXAM_BATCH_CODE,");
		sql.append("  		:EXAM_PLAN_ID,");
		sql.append("  		:KSFS,");
		sql.append("  		:MAKEUP,");
		sql.append("  		:PAY_STATUS,");
		sql.append("  		:COURSE_COST,");
		sql.append("  		:COURSE_CODE,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		SYSDATE");
		sql.append("  	)");

		params.put("COST_ID", ObjectUtils.toString(searchParams.get("COST_ID")));
		params.put("PAY_SN", ObjectUtils.toString(searchParams.get("PAY_SN")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		params.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		params.put("KSFS", ObjectUtils.toString(searchParams.get("KSFS_FLAG")));
		params.put("MAKEUP", ObjectUtils.toString(searchParams.get("MAKEUP")));
		params.put("PAY_STATUS", ObjectUtils.toString(searchParams.get("PAY_STATUS")));
		params.put("COURSE_COST", ObjectUtils.toString(searchParams.get("COURSE_COST")));
		params.put("COURSE_CODE", ObjectUtils.toString(searchParams.get("COURSE_CODE")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.insertForMapNative(sql.toString(), params);
		
	}


	/**
	 *
	 * 根据考试计划查询开考科目与课程的关联(院校模式)
	 * @param searchParams
	 * @return
	 */
	public List getExamPlanAndCourseInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.EXAM_PLAN_ID,");
		sql.append("  	TAB.EXAM_PLAN_LIMIT,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.TYPE,");
        sql.append("  	TAB.EXAM_TYPE,");
		sql.append("  	TAB.EXAM_STYLE,");
		sql.append("  	TAB.EXAM_NO,");
		sql.append("  	TAB.EXAM_STATE,");
		sql.append("  	TAB.EXAM_STIME,");
		sql.append("  	TAB.EXAM_ETIME,");
		sql.append("  	TAB.BOOK_STARTTIME,");
		sql.append("  	TAB.BOOK_ENDTIME,");
		sql.append("  	TAB.BOOKS_STARTTIME,");
		sql.append("  	TAB.BOOKS_ENDTIME,");
		sql.append("  	TAB.RECORD_END,");
		sql.append("  	TAB.EXAM_BATCH_CODE,");
		sql.append("  	TAB.XK_PERCENT,");
		sql.append("  	TAB.GRADE_ID");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GEP.EXAM_PLAN_ID,");
		sql.append("  			GEP.EXAM_PLAN_LIMIT,");
		sql.append("  			VEPS.COURSE_ID,");
		sql.append("  			GC.KCMC AS COURSE_NAME,");
		sql.append("  			GEP.TYPE,");
        sql.append("  			(");
        sql.append("  				SELECT");
        sql.append("  					TSD.NAME");
        sql.append("  				FROM");
        sql.append("  					TBL_SYS_DATA TSD");
        sql.append("  				WHERE");
        sql.append("  					TSD.IS_DELETED = 'N'");
        sql.append("  					AND TSD.TYPE_CODE = 'EXAM_STYLE'");
        sql.append("  					AND TSD.CODE = GEP.EXAM_STYLE");
        sql.append("  			) EXAM_TYPE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					TSD.NAME");
		sql.append("  				FROM");
		sql.append("  					TBL_SYS_DATA TSD");
		sql.append("  				WHERE");
		sql.append("  					TSD.IS_DELETED = 'N'");
		sql.append("  					AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  					AND TSD.CODE = GEP.TYPE");
		sql.append("  			) EXAM_STYLE,");
		sql.append("  			GEP.EXAM_NO,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  					WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  					WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  					ELSE(");
		sql.append("  						CASE");
		sql.append("  							WHEN GEP.TYPE = 13 THEN(");
		sql.append("  								CASE");
		sql.append("  									WHEN GEP.EXAM_ST IS NULL");
		sql.append("  									OR GEP.EXAM_END IS NULL THEN(");
		sql.append("  										CASE");
		sql.append("  											WHEN SYSDATE < GEB.SHAPE_END THEN '0'");
		sql.append("  											WHEN SYSDATE > GEB.SHAPE_END THEN '2'");
		sql.append("  											ELSE '1'");
		sql.append("  										END");
		sql.append("  									)");
		sql.append("  									ELSE(");
		sql.append("  										CASE");
		sql.append("  											WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  											WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  											WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  											ELSE '--'");
		sql.append("  										END");
		sql.append("  									)");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  							WHEN GEP.TYPE = 14 THEN(");
		sql.append("  								CASE");
		sql.append("  									WHEN GEP.EXAM_ST IS NULL");
		sql.append("  									OR GEP.EXAM_END IS NULL THEN(");
		sql.append("  										CASE");
		sql.append("  											WHEN SYSDATE < GEB.THESIS_END THEN '0'");
		sql.append("  											WHEN SYSDATE > GEB.THESIS_END THEN '2'");
		sql.append("  											ELSE '1'");
		sql.append("  										END");
		sql.append("  									)");
		sql.append("  									ELSE(");
		sql.append("  										CASE");
		sql.append("  											WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  											WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  											WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  											ELSE '--'");
		sql.append("  										END");
		sql.append("  									)");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  							WHEN GEP.TYPE = 15 THEN(");
		sql.append("  								CASE");
		sql.append("  									WHEN GEP.EXAM_ST IS NULL");
		sql.append("  									OR GEP.EXAM_END IS NULL THEN(");
		sql.append("  										CASE");
		sql.append("  											WHEN SYSDATE < GEB.REPORT_END THEN '0'");
		sql.append("  											WHEN SYSDATE > GEB.REPORT_END THEN '2'");
		sql.append("  											ELSE '1'");
		sql.append("  										END");
		sql.append("  									)");
		sql.append("  									ELSE(");
		sql.append("  										CASE");
		sql.append("  											WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  											WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  											WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  											ELSE '--'");
		sql.append("  										END");
		sql.append("  									)");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  							ELSE '--'");
		sql.append("  						END");
		sql.append("  					)");
		sql.append("  				END");
		sql.append("  			) EXAM_STATE,");
		sql.append("  			TO_CHAR( GEP.EXAM_ST, 'yyyy-MM-dd hh24:mi' ) EXAM_STIME,");
		sql.append("  			TO_CHAR( GEP.EXAM_END, 'yyyy-MM-dd hh24:mi' ) EXAM_ETIME,");
		sql.append("  			TO_CHAR( GEB.BOOK_ST, 'yyyy-MM-dd hh24:mi' ) BOOK_STARTTIME,");
		sql.append("  			TO_CHAR( GEB.BOOK_END, 'yyyy-MM-dd hh24:mi' ) BOOK_ENDTIME,");
		sql.append("  			TO_CHAR( GEB.BOOKS_ST, 'yyyy-MM-dd hh24:mi' ) BOOKS_STARTTIME,");
		sql.append("  			TO_CHAR( GEB.BOOKS_END, 'yyyy-MM-dd hh24:mi' ) BOOKS_ENDTIME,");
		sql.append("  			TO_CHAR( GEB.RECORD_END, 'yyyy-MM-dd' ) RECORD_END,");
		sql.append("  			GEB.EXAM_BATCH_CODE,");
		sql.append("  			GEP.XK_PERCENT,");
		sql.append("  			GEB.GRADE_ID");
		sql.append("  		FROM GJT_EXAM_PLAN_NEW GEP");
		sql.append("  		INNER JOIN View_Exam_Plan_Sc VEPS ON GEP.EXAM_PLAN_ID = VEPS.EXAM_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON VEPS.COURSE_ID = GC.COURSE_ID");
		sql.append("  		INNER JOIN GJT_EXAM_BATCH_NEW GEB ON GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  		WHERE");
		sql.append("  			GEP.IS_DELETED = 0");
		sql.append("  			AND GEB.IS_DELETED = 0");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GEB.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIALTY_ID"))){
			sql.append("  	AND (VEPS.SPECIALTY_ID=:SPECIALTY_ID OR VEPS.SPECIALTY_ID = '-1') ");
			params.put("SPECIALTY_ID",ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("COURSE_ID"))){
			sql.append("  	AND VEPS.COURSE_ID = :COURSE_ID ");
			params.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		// 2018春新规 按照教学计划的试卷号查询
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_NO"))){
			sql.append("  	AND GEP.EXAM_NO = :EXAM_NO ");
			params.put("EXAM_NO",ObjectUtils.toString(searchParams.get("EXAM_NO")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("TERM_ID"))){
			sql.append("  	AND GEB.GRADE_ID = :TERM_ID ");
			params.put("TERM_ID",ObjectUtils.toString(searchParams.get("TERM_ID")));
		}

		if ("1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))){
			sql.append("  	AND GEP.TYPE = :TYPE ");
			params.put("TYPE", 7);
		}
		if("1".equals(ObjectUtils.toString(searchParams.get("APPOINTMENT_FLAG")))) {
			if(searchParams.get("condition") == null) {
				sql.append("	AND ( (SYSDATE BETWEEN GEB.BOOK_ST AND GEB.BOOK_END) OR ( SYSDATE BETWEEN GEB.BOOKS_ST AND GEB.BOOKS_END ) ) ");
			}
			sql.append("	AND GEP.EXAM_PLAN_ORDER = '1' ");

			if(!"1".equals(ObjectUtils.toString(searchParams.get("SCHOOL_MODEL")))){
//				//可参加三种考试的学员类型:正式跟读生;电大续读生;外校预科生
//				if ("12".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
//						|| "42".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
//						|| "51".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))){
//					sql.append("  	AND GEP.TYPE IN (7,8,19) ");
//				}
//				//只可以参加学院网考的学生类型:非正式跟读生;课程预读生;本科预读生
//				if("13".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
//						|| "41".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
//						|| "61".equals(ObjectUtils.toString(searchParams.get("USER_TYPE"))) ){
//					sql.append("  	AND GEP.TYPE =7 ");
//				}
//				if ("0".equals(ObjectUtils.toString(searchParams.get("XJZT")))
//						|| "3".equals(ObjectUtils.toString(searchParams.get("XJZT")))){
//					sql.append("  	AND GEP.TYPE =7 ");
//				}
				// 2018春新规 可参加条件
				// 正式生，学籍状态为在籍和注册中      笔试、形考(登录个人学习中心)、网考、易考通、机考、形考(登录国开学习网)、论文、实践报告
				if ("11".equals(ObjectUtils.toString(searchParams.get("USER_TYPE")))
						&& ("2".equals(ObjectUtils.toString(searchParams.get("XJZT"))) || "0".equals(ObjectUtils.toString(searchParams.get("XJZT"))))) {
					sql.append("  	AND GEP.TYPE IN (8,13,7,19,11,22,14,20) ");
				}
					// 正式跟读生和电大续读生，学籍状态为待注册      笔试、形考(登录个人学习中心)、网考、易考通、
				 else if (("12".equals(ObjectUtils.toString(searchParams.get("USER_TYPE"))) || "42".equals(ObjectUtils.toString(searchParams.get("USER_TYPE"))))
						&& "3".equals(ObjectUtils.toString(searchParams.get("XJZT")))) {
					sql.append("  	AND GEP.TYPE IN (8,13,7,19) ");
				} else {
					sql.append("  	AND 1=2 ");
				}
			}

		}
		sql.append(" ORDER BY VEPS.SPECIALTY_ID DESC"); // 降序排序是为了指定专业优先，通用专业在后

		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");

		if("0".equals(ObjectUtils.toString(searchParams.get("CURRENT_FLAG")))){
			sql.append("  	AND TAB.EXAM_STATE = '2'");
		}

		return commonDao.queryForMapListNative(sql.toString(),params);
	}


	/**
	 * 根据课程ID,考试方式，考试计划匹配设置的开考科目，
	 * @param searchParams
	 * @return
	 */
	public List getViewExamPlanByAcadeMy(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	VEPC.EXAM_PLAN_ID,");
		sql.append("  	VEPC.EXAM_BATCH_CODE,");
		sql.append("  	VEPC.TYPE,");
		sql.append("  	VEPC.SPECIALTY_ID,b.XK_PERCENT");
		sql.append("  FROM");
		sql.append("  	VIEW_EXAM_PLAN_SC VEPC");
		sql.append("  	inner join gjt_exam_plan_new b on b.exam_plan_id=VEPC.EXAM_PLAN_ID");
		sql.append("  WHERE");
		sql.append("	1=1");

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND VEPC.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("COURSE_ID"))){
			sql.append("  	AND VEPC.COURSE_ID = :COURSE_ID ");
			params.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("TYPE"))){
			sql.append("  	AND VEPC.TYPE = :TYPE ");
			params.put("TYPE",Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		}

		return commonDao.queryForMapListNative(sql.toString(),params);
	}


	/**
	 * 查询学员选课信息(院校模式)
	 * @param searchParams
	 * @return
	 */
	public List getChooseCourseByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.TERM_CODE,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = TAB.KKXQ");
		sql.append("  			AND TSD.TYPE_CODE = 'KKXQ'");
		sql.append("  	) TERM_NAME,");
		sql.append("  	TAB.TERM_NAME GRADE_NAME,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.EXAM_SCORE NEW_EXAM_SCORE,");
		sql.append("  	TAB.EXAM_SCORE1 NEW_EXAM_SCORE1,");
		sql.append("  	TAB.EXAM_SCORE2 NEW_EXAM_SCORE2,");
		sql.append("  	TAB.EXAM_STATE,");
		sql.append("  	TAB.COURSE_SCHEDULE AS KCXXBZ,");
		sql.append("  	100 - TAB.COURSE_SCHEDULE AS KCKSBZ,");
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = TAB.EXAM_SCORE1 ), TAB.EXAM_SCORE1 )) EXAM_SCORE,"); //形成性成绩
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = TAB.EXAM_SCORE ), DECODE( SUBSTR( TAB.EXAM_SCORE, 1, 1 ), '.', '0' || TAB.EXAM_SCORE, TAB.EXAM_SCORE ) )) EXAM_SCORE1,"); //终结性成绩
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = TAB.EXAM_SCORE2 ), DECODE( SUBSTR( TAB.EXAM_SCORE2, 1, 1 ), '.', '0' || TAB.EXAM_SCORE2, TAB.EXAM_SCORE2 ) )) EXAM_SCORE2,"); //总成绩
		sql.append("  	TAB.KCH,");
		sql.append("  	TAB.COURSE_COST,");
		sql.append("  	TAB.MAKEUP,");
		sql.append("  	TAB.PAY_STATE,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TAB.BESPEAK_STATE = '0' THEN '0'");
		sql.append("  			ELSE '1'");
		sql.append("  		END");
		sql.append("  	) BESPEAK_STATE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.COURSE_CODE,");
		sql.append("  	TAB.sourceCourseKch \"sourceCourseKch\",TAB.sourceCourseKcmc \"sourceCourseKcmc\",");
		sql.append("  	TAB.courseKch \"courseKch\",TAB.courseKcmc \"courseKcmc\",TAB.EXAM_NO,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.KSFS,");
		sql.append("  	TAB.LEARNING_STYLE,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.ACTUAL_GRADE_ID,");
		sql.append("  	TAB.START_DATE, ");
		sql.append("  	TAB.END_DATE ");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GG.GRADE_ID TERM_ID,GG.GRADE_CODE TERM_CODE,GG.GRADE_NAME TERM_NAME,");
		sql.append("  			GTP.KKXQ,");
		sql.append("  			GTP.COURSE_ID,");
		sql.append("  			GTP.KCMC AS COURSE_NAME,");
		sql.append("  			GTP.KCH,");
		sql.append("  			NVL(( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = GTP.KCH ),( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = 'other' )) COURSE_COST,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					TO_CHAR( COUNT( 1 ))");
		sql.append("  				FROM");
		sql.append("  					GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  				WHERE");
		sql.append("  					GEA.IS_DELETED = 0");
		sql.append("  					AND GEA.REC_ID = GRR.REC_ID");
		if (EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  					AND GEA.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		sql.append("  			) BESPEAK_STATE,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GTP.COURSE_CODE,");
		sql.append("  			GTP.KKZY,");
		sql.append("  			GTP.SOURCE_KCH sourceCourseKch,GTP.SOURCE_KCMC sourceCourseKcmc,");
		sql.append("  			(case when GTP.SOURCE_COURSE_ID!=GTP.COURSE_ID then GTP.KCH else null end) courseKch,(case when GTP.SOURCE_COURSE_ID!=GTP.COURSE_ID then GTP.kcmc else null end) courseKcmc,");
		sql.append("  			(case when GTP.SOURCE_COURSE_ID!=GTP.COURSE_ID then GTP.REPLACE_EXAM_NO else GTP.EXAM_NO end) EXAM_NO,");
		sql.append("  			GTP.KSFS,");
		sql.append("  			GTP.LEARNING_STYLE,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			GRR.EXAM_STATE,");
		sql.append("  			GRR.COURSE_SCHEDULE,");
		sql.append("  			GRR.EXAM_SCORE,");
		sql.append("  			GRR.EXAM_SCORE1,");
		sql.append("  			GRR.EXAM_SCORE2,");
		sql.append("  			GTP.ACTUAL_GRADE_ID,");
		sql.append("  			TO_CHAR(GG.START_DATE,'yyyy-MM-dd') START_DATE,");
		sql.append("  			TO_CHAR(NVL(GG.END_DATE,ADD_MONTHS(GG.START_DATE,4)),'yyyy-MM-dd') END_DATE ");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
        sql.append("  		INNER JOIN GJT_GRADE GG ON GTP.ACTUAL_GRADE_ID = GG.GRADE_ID");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N' ");


		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  			AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GTP.XX_ID = :XX_ID");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}
        if (EmptyUtils.isEmpty(searchParams.get("EXAM_FLG"))){
            sql.append("  			AND GRR.EXAM_STATE <> '1' AND GRR.EXAM_STATE <> '3'"); // 已通过的和登记中的不能预约
        }

		sql.append("  		ORDER BY");
		sql.append("  			TERM_CODE,COURSE_ID");
		sql.append("  	) TAB");
        sql.append("  WHERE");
        sql.append("  	1 = 1");


        if (EmptyUtils.isNotEmpty(searchParams.get("EXAM_FLG"))){  //我的考试功能增加查询预约表
			sql.append("  		AND TAB.BESPEAK_STATE='1'");
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("BESPEAK_STATE"))){
			sql.append("		AND TAB.BESPEAK_STATE = :BESPEAK_STATE");
			params.put("BESPEAK_STATE",ObjectUtils.toString(searchParams.get("BESPEAK_STATE")));
		}

        if(EmptyUtils.isNotEmpty(searchParams.get("END_DATE"))){
            sql.append("  	AND TAB.END_DATE <= :END_DATE ");
            params.put("END_DATE",ObjectUtils.toString(searchParams.get("END_DATE")));
        }


		return commonDao.queryForMapListNative(sql.toString(),params);
	}

    /**
     * 根据考试计划查询学期数据
     * @param searchParams
     * @return
     */
	public List getEndTermByExamBatch(Map searchParams){
	    Map params = new HashMap();
        StringBuffer sql = new StringBuffer();

        sql.append("  SELECT");
        sql.append("  	GEB.EXAM_BATCH_ID,");
        sql.append("  	GEB.EXAM_BATCH_CODE,");
        sql.append("  	GG.GRADE_ID,");
        sql.append("  	GG.GRADE_CODE,");
        sql.append("  	GG.GRADE_NAME,");
        sql.append("  	GG.START_DATE,");
        sql.append("  	TO_CHAR(NVL(GG.END_DATE,ADD_MONTHS(GG.START_DATE,4)),'yyyy-MM-dd') END_DATE ");
        sql.append("  FROM");
        sql.append("  	GJT_EXAM_BATCH_NEW GEB,");
        sql.append("  	GJT_GRADE GG");
        sql.append("  WHERE");
        sql.append("  	GEB.IS_DELETED = 0");
        sql.append("  	AND GG.IS_DELETED = 'N'");
        sql.append("  	AND GEB.GRADE_ID = GG.GRADE_ID");

        if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
            sql.append("  	AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
            params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
        }

        sql.append("  	AND ROWNUM = 1 ");

        return commonDao.queryForMapListNative(sql.toString(),params);

    }


	/**
	 * 查询学员选课数据(关联开考科目)  暂未用
	 * @param searchParams
	 * @return
	 */
	public List getExamDataByStudent(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.TERM_CODE,");
		sql.append("  	TAB.TERM_ID,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = TAB.KKXQ");
		sql.append("  			AND TSD.TYPE_CODE = 'KKXQ'");
		sql.append("  	) TERM_NAME,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = TAB.EXAM_SCORE1 ), TAB.EXAM_SCORE1 )) EXAM_SCORE,");
		sql.append("  	TAB.KCH,");
		sql.append("  	TAB.COURSE_COST,");
		sql.append("  	TAB.MAKEUP,");
		sql.append("  	TAB.PAY_STATE,");
		sql.append("  	TAB.BESPEAK_STATE,");
		sql.append("  	TAB.TEACH_PLAN_ID,");
		sql.append("  	TAB.REC_ID,");
		sql.append("  	TAB.STUDENT_ID,");
		sql.append("  	TAB.URL_NEW,");
		sql.append("  	TAB.EXAM_PLAN_ID,");
		sql.append("  	TAB.EXAM_NO,");
		sql.append("  	TAB.EXAM_STIME,");
		sql.append("  	TAB.EXAM_ETIME,");
		sql.append("  	TAB.EXAM_STATE,");
		sql.append("  	TAB.TYPE,");
		sql.append("  	TAB.BOOK_STARTTIME,");
		sql.append("  	TAB.BOOK_ENDTIME,");
		sql.append("  	TAB.EXAM_BATCH_CODE");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT(");
		sql.append("  				SELECT");
		sql.append("  					GG.GRADE_CODE");
		sql.append("  				FROM");
		sql.append("  					GJT_GRADE GG");
		sql.append("  				WHERE");
		sql.append("  					GG.IS_DELETED = 'N'");
		sql.append("  					AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  			) TERM_CODE,");
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
		sql.append("  			GTP.KKXQ,");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			GC.KCH,");
		sql.append("  			NVL(( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = GC.KCH ),( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = 'other' )) COURSE_COST,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  			GRR.BESPEAK_STATE,");
		sql.append("  			GTP.TEACH_PLAN_ID,");
		sql.append("  			GTP.COURSE_CODE,");
		sql.append("  			GTP.KKZY,");
		sql.append("  			GRR.REC_ID,");
		sql.append("  			GRR.STUDENT_ID,");
		sql.append("  			GRR.EXAM_SCORE,");
		sql.append("  			GRR.EXAM_SCORE1,");
		sql.append("  			GRR.EXAM_SCORE2,");
		sql.append("  			GEP.EXAM_PLAN_ID,");
		sql.append("  			GEP.EXAM_NO,");
		sql.append("  			TO_CHAR( GEP.EXAM_ST, 'yyyy-MM-dd hh24:mi' ) EXAM_STIME,");
		sql.append("  			TO_CHAR( GEP.EXAM_END, 'yyyy-MM-dd hh24:mi' ) EXAM_ETIME,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  					WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  					WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) EXAM_STATE,");
		sql.append("  			GEP.TYPE,");
		sql.append("  			TO_CHAR( GEB.BOOK_ST, 'yyyy-MM-dd hh24:mi' ) BOOK_STARTTIME,");
		sql.append("  			TO_CHAR( GEB.BOOK_END, 'yyyy-MM-dd hh24:mi' ) BOOK_ENDTIME,");
		sql.append("  			GEB.EXAM_BATCH_CODE,");
		sql.append("  			C.URL_NEW");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_PLAN_NEW GEP");
		sql.append("  		INNER JOIN GJT_EXAM_PLAN_NEW_COURSE GEPC ON");
		sql.append("  			GEP.EXAM_PLAN_ID = GEPC.EXAM_PLAN_ID");
		sql.append("  		INNER JOIN GJT_REC_RESULT GRR ON");
		sql.append("  			GEPC.COURSE_ID = GRR.COURSE_ID");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");

		if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  			AND GRR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}


		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  		INNER JOIN GJT_EXAM_BATCH_NEW GEB ON");
		sql.append("  			GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  		LEFT JOIN(");
		sql.append("  				SELECT");
		sql.append("  					GSI.STUDENT_ID,");
		sql.append("  					GSI.SFZH,");
		sql.append("  					GSI.USER_TYPE,");
		sql.append("  					GSI.XJZT,");
		sql.append("  					GSD.ID_NO,");
		sql.append("  					GSD.FILE_TYPE,");
		sql.append("  					GSD.URL,");
		sql.append("  					TO_CHAR( GSD.URL_NEW ) URL_NEW");
		sql.append("  				FROM");
		sql.append("  					GJT_STUDENT_INFO GSI,");
		sql.append("  					GJT_SIGNUP GS,");
		sql.append("  					GJT_SIGNUP_DATA GSD");
		sql.append("  				WHERE");
		sql.append("  					GSI.IS_DELETED = 'N'");
		sql.append("  					AND GS.IS_DELETED = 'N'");
		sql.append("  					AND GSI.STUDENT_ID = GS.STUDENT_ID");
		sql.append("  					AND GS.STUDENT_ID = GSD.STUDENT_ID");
		sql.append("  					AND GSD.FILE_TYPE = 'zp'");
		sql.append("  			) C ON");
		sql.append("  			C.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  		WHERE");
		sql.append("  			GEP.IS_DELETED = 0");
		sql.append("  			AND GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GEB.IS_DELETED = 0");

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  			AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GEP.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}


		if(EmptyUtils.isNotEmpty(searchParams.get("BESPEAK_STATE"))){
			sql.append("  			AND GRR.BESPEAK_STATE IN(");
			sql.append("  				'0',");
			sql.append("  				'1'");
			sql.append("  			)");
		}

		sql.append("			AND SYSDATE BETWEEN GEB.BOOK_ST AND GEB.BOOK_END");

		sql.append("  		ORDER BY");
		sql.append("  			TERM_CODE");
		sql.append("  	) TAB");

		return  commonDao.queryForMapListNative(sql.toString(),params);

	}


    /**
     * 查询机考准考证
     * @param searchParams
     * @return
     */
	public List getJKAdmissionInfo(Map searchParams){
	    Map params = new HashMap();
        StringBuffer sql = new StringBuffer();

        sql.append("  SELECT");
        sql.append("  	DISTINCT ");
        sql.append("  	GSI.XM ADMISSION_NAME,");
        sql.append("  	GSI.XH STU_NUMBER,");
        sql.append("  	TO_CHAR( GSD.URL_NEW ) STU_PHOTO,");
        sql.append("  	GAT.EXAM_PONIT EXAM_POINT_NAME,");
        sql.append("  	GAT.EXAM_POINT_ADDRESS EXAM_ADDRESS,");
        sql.append("  	GSO.XXMC,");
        sql.append("  	GAT.EXAM_NO,");
        sql.append("  	GAT.COURSE_NAME,");
        sql.append("  	GAT.EXAM_STYLE ,");
        sql.append("  	('机考') EXAM_TYPE ,");
        sql.append("  	GAT.EXAM_DATE ,");
        sql.append("  	GAT.EXAM_TIME,");
        sql.append("  	GAT.EXAM_SEAT_NUMBER SEAT_NO,");
        sql.append("  	GSI.STUDENT_ID");
        sql.append("  FROM");
        sql.append("  	GJT_ADMISSION_TICKET GAT,");
        sql.append("  	GJT_STUDENT_INFO GSI");
        sql.append("  LEFT JOIN GJT_SIGNUP_DATA GSD ON");
        sql.append("  	GSI.STUDENT_ID = GSD.STUDENT_ID");
        sql.append("  LEFT JOIN GJT_SCHOOL_INFO GSO ON");
        sql.append("  	GSI.XX_ID = GSO.ID");
        sql.append("  WHERE");
        sql.append("  	GAT.IS_DELETED = 'N'");
        sql.append("  	AND GSO.IS_DELETED = 'N'");

        if(EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
            sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");
            params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
        }
        sql.append("  	AND GSI.STUDENT_ID = GAT.STUDENT_ID");

        return commonDao.queryForMapListNative(sql.toString(),params);
    }


	/**
	 * 查询往期考试记录
	 * @param searchParams
	 * @return
	 */
	public List getPastLearnRecord(Map searchParams){
    	Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TAB.EXAM_PLAN_ID,");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.TYPE KSFS_FLAG,");
		sql.append("  	TAB.EXAM_TYPE,");
		sql.append("  	TAB.EXAM_STYLE,");
		sql.append("  	TAB.EXAM_NO,");
		sql.append("  	TAB.EXAM_STATE,");
		sql.append("  	TAB.EXAM_STIME,");
		sql.append("  	TAB.EXAM_ETIME,");
		sql.append("  	TAB.BOOK_STARTTIME,");
		sql.append("  	TAB.BOOK_ENDTIME,");
		sql.append("  	TAB.EXAM_BATCH_CODE,");
		sql.append("  	TAB.GRADE_ID");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT GEP.EXAM_PLAN_ID,");
		sql.append("  			GEPC.COURSE_ID,");
		sql.append("  			GC.KCMC AS COURSE_NAME,");
		sql.append("  			GEP.TYPE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					TSD.NAME");
		sql.append("  				FROM");
		sql.append("  					TBL_SYS_DATA TSD");
		sql.append("  				WHERE");
		sql.append("  					TSD.IS_DELETED = 'N'");
		sql.append("  					AND TSD.TYPE_CODE = 'EXAM_STYLE'");
		sql.append("  					AND TSD.CODE = GEP.EXAM_STYLE");
		sql.append("  			) EXAM_TYPE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					TSD.NAME");
		sql.append("  				FROM");
		sql.append("  					TBL_SYS_DATA TSD");
		sql.append("  				WHERE");
		sql.append("  					TSD.IS_DELETED = 'N'");
		sql.append("  					AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  					AND TSD.CODE = GEP.TYPE");
		sql.append("  			) EXAM_STYLE,");
		sql.append("  			GEP.EXAM_NO,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  					WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  					WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) EXAM_STATE,");
		sql.append("  			TO_CHAR( GEP.EXAM_ST, 'yyyy-MM-dd hh24:mi' ) EXAM_STIME,");
		sql.append("  			TO_CHAR( GEP.EXAM_END, 'yyyy-MM-dd hh24:mi' ) EXAM_ETIME,");
		sql.append("  			TO_CHAR( GEB.BOOK_ST, 'yyyy-MM-dd hh24:mi' ) BOOK_STARTTIME,");
		sql.append("  			TO_CHAR( GEB.BOOK_END, 'yyyy-MM-dd hh24:mi' ) BOOK_ENDTIME,");
		sql.append("  			GEB.EXAM_BATCH_CODE,");
		sql.append("  			GEB.GRADE_ID");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_PLAN_NEW GEP");
		sql.append("  		INNER JOIN GJT_EXAM_PLAN_NEW_COURSE GEPC ON");
		sql.append("  			GEP.EXAM_PLAN_ID = GEPC.EXAM_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GEPC.COURSE_ID = GC.COURSE_ID");
		sql.append("  		INNER JOIN GJT_EXAM_BATCH_NEW GEB ON");
		sql.append("  			GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  		WHERE");
		sql.append("  			GEP.IS_DELETED = 0");
		sql.append("  			AND GEB.IS_DELETED = 0");
		sql.append("  			AND GC.IS_DELETED = 'N'");

		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GEB.XX_ID = :XX_ID ");
			params.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODES"))){
			String[] examBatchCodes = ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODES")).split(",");
			if (examBatchCodes instanceof String[]){
				StringBuffer buffer = new StringBuffer();
				for (int i=0;i<=examBatchCodes.length-1;i++){
					buffer.append(examBatchCodes[i]+"','");
				}
				sql.append("	AND GEP.EXAM_BATCH_CODE IN ('"+buffer.toString()+"')");
			}else{
				sql.append("  			AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODES ");
				params.put("EXAM_BATCH_CODES",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODES")));
			}
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("COURSE_ID"))){
			sql.append("  			AND GC.COURSE_ID = :COURSE_ID ");
			params.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("TERM_ID"))){
			sql.append("  			AND GEB.GRADE_ID = :TERM_ID ");
			params.put("TERM_ID",ObjectUtils.toString(searchParams.get("TERM_ID")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			GEB.EXAM_BATCH_CODE");
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");


		return commonDao.queryForMapListNative(sql.toString(),params);

	}


	//我的考试sql(暂未用)
	public List getMyExamDataList(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GTP.KKXQ,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GG.GRADE_CODE");
		sql.append("  		FROM");
		sql.append("  			GJT_GRADE GG");
		sql.append("  		WHERE");
		sql.append("  			GG.IS_DELETED = 'N'");
		sql.append("  			AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  	) TERM_CODE,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GG.GRADE_ID");
		sql.append("  		FROM");
		sql.append("  			GJT_GRADE GG");
		sql.append("  		WHERE");
		sql.append("  			GG.IS_DELETED = 'N'");
		sql.append("  			AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID");
		sql.append("  	) TERM_ID,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = GTP.KKXQ");
		sql.append("  			AND TSD.TYPE_CODE = 'KKXQ'");
		sql.append("  	) TERM_NAME,");
		sql.append("  	TO_CHAR(( SELECT GG.START_DATE FROM GJT_GRADE GG WHERE GG.IS_DELETED = 'N' AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID ), 'yyyy-MM-dd' ) START_DATE,");
		sql.append("  	TO_CHAR( NVL(( SELECT GG.END_DATE FROM GJT_GRADE GG WHERE GG.IS_DELETED = 'N' AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID ), ADD_MONTHS(( SELECT GG.START_DATE FROM GJT_GRADE GG WHERE GG.IS_DELETED = 'N' AND GG.GRADE_ID = GTP.ACTUAL_GRADE_ID ), 4 )), 'yyyy-MM-dd' ) END_DATE,");
		sql.append("  	GC.COURSE_ID,");
		sql.append("  	GC.KCMC AS COURSE_NAME,");
		sql.append("  	GC.KCH,");
		sql.append("  	NVL(( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = GC.KCH ),( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = 'other' )) COURSE_COST,");
		sql.append("  			(");
		sql.append("  				CASE NVL(GRR.PAY_STATE,'2')");
		sql.append("  					WHEN '2' THEN '否'");
		sql.append("  					WHEN '0' THEN '是'");
		sql.append("  					WHEN '1' THEN '是'");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			) MAKEUP,");
		sql.append("  			NVL(GRR.PAY_STATE,'2') PAY_STATE,");
		sql.append("  	GRR.BESPEAK_STATE,");
		sql.append("  	GTP.TEACH_PLAN_ID,");
		sql.append("  	GTP.COURSE_CODE,");
		sql.append("  	GTP.KKZY,");
		sql.append("  	GTP.KSFS,");
		sql.append("  	GRR.REC_ID,");
		sql.append("  	GRR.STUDENT_ID,");
		sql.append("  	GRR.COURSE_SCHEDULE,");
		sql.append("  	GRR.COURSE_SCHEDULE AS KCXXBZ,");
		sql.append("  	100 - GRR.COURSE_SCHEDULE AS KCKSBZ,");
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE1 ), GRR.EXAM_SCORE1 )) EXAM_SCORE,");
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE ), DECODE( SUBSTR( GRR.EXAM_SCORE, 1, 1 ), '.', '0' || GRR.EXAM_SCORE, GRR.EXAM_SCORE ))) EXAM_SCORE1,");
		sql.append("  	TO_CHAR( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'EXAM_SCORE' AND TSD.CODE = GRR.EXAM_SCORE2 ), DECODE( SUBSTR( GRR.EXAM_SCORE2, 1, 1 ), '.', '0' || GRR.EXAM_SCORE2, GRR.EXAM_SCORE2 ))) EXAM_SCORE2,");
		sql.append("  	GTP.ACTUAL_GRADE_ID,");
		sql.append("  	GEP.EXAM_BATCH_CODE,");
		sql.append("  	GEP.EXAM_NO,");
		sql.append("  	GEP.TYPE,");
		sql.append("  	TO_CHAR( GEP.EXAM_ST, 'yyyy-MM-dd hh24:mi' ) EXAM_STIME,");
		sql.append("  	TO_CHAR( GEP.EXAM_END, 'yyyy-MM-dd hh24:mi' ) EXAM_ETIME,");
		sql.append("  	TO_CHAR( GEB.BOOK_ST, 'yyyy-MM-dd hh24:mi' ) BOOK_STARTTIME,");
		sql.append("  	TO_CHAR( GEB.BOOK_END, 'yyyy-MM-dd hh24:mi' ) BOOK_ENDTIME,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'EXAM_STYLE'");
		sql.append("  			AND TSD.CODE = GEP.EXAM_STYLE");
		sql.append("  	) EXAM_TYPE,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  			AND TSD.CODE = GEP.TYPE");
		sql.append("  	) EXAM_STYLE,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  			WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  			WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  			ELSE(");
		sql.append("  				CASE");
		sql.append("  					WHEN GEP.TYPE = 13 THEN(");
		sql.append("  						CASE");
		sql.append("  							WHEN GEP.EXAM_ST IS NULL");
		sql.append("  							OR GEP.EXAM_END IS NULL THEN(");
		sql.append("  								CASE");
		sql.append("  									WHEN SYSDATE < GEB.SHAPE_END THEN '0'");
		sql.append("  									WHEN SYSDATE > GEB.SHAPE_END THEN '2'");
		sql.append("  									ELSE '1'");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  							ELSE(");
		sql.append("  								CASE");
		sql.append("  									WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  									WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  									WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  									ELSE '--'");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  						END");
		sql.append("  					)");
		sql.append("  					WHEN GEP.TYPE = 14 THEN(");
		sql.append("  						CASE");
		sql.append("  							WHEN GEP.EXAM_ST IS NULL");
		sql.append("  							OR GEP.EXAM_END IS NULL THEN(");
		sql.append("  								CASE");
		sql.append("  									WHEN SYSDATE < GEB.THESIS_END THEN '0'");
		sql.append("  									WHEN SYSDATE > GEB.THESIS_END THEN '2'");
		sql.append("  									ELSE '1'");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  							ELSE(");
		sql.append("  								CASE");
		sql.append("  									WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  									WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  									WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  									ELSE '--'");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  						END");
		sql.append("  					)");
		sql.append("  					WHEN GEP.TYPE = 15 THEN(");
		sql.append("  						CASE");
		sql.append("  							WHEN GEP.EXAM_ST IS NULL");
		sql.append("  							OR GEP.EXAM_END IS NULL THEN(");
		sql.append("  								CASE");
		sql.append("  									WHEN SYSDATE < GEB.REPORT_END THEN '0'");
		sql.append("  									WHEN SYSDATE > GEB.REPORT_END THEN '2'");
		sql.append("  									ELSE '1'");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  							ELSE(");
		sql.append("  								CASE");
		sql.append("  									WHEN SYSDATE < GEP.EXAM_ST THEN '0'");
		sql.append("  									WHEN SYSDATE BETWEEN GEP.EXAM_ST AND GEP.EXAM_END THEN '1'");
		sql.append("  									WHEN SYSDATE > GEP.EXAM_END THEN '2'");
		sql.append("  									ELSE '--'");
		sql.append("  								END");
		sql.append("  							)");
		sql.append("  						END");
		sql.append("  					)");
		sql.append("  					ELSE '--'");
		sql.append("  				END");
		sql.append("  			)");
		sql.append("  		END");
		sql.append("  	) EXAM_STATE");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_APPOINTMENT_NEW GEA,");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP,");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB,");
		sql.append("  	GJT_EXAM_PLAN_NEW_COURSE GEPC,");
		sql.append("  	GJT_REC_RESULT GRR,");
		sql.append("  	VIEW_TEACH_PLAN GTP,");
		sql.append("  	GJT_COURSE GC");
		sql.append("  WHERE");
		sql.append("  	GEA.IS_DELETED = 0");
		sql.append("  	AND GEP.IS_DELETED = 0");
		sql.append("  	AND GEB.IS_DELETED = 0");
		sql.append("  	AND GRR.IS_DELETED = 'N'");
		sql.append("  	AND GC.IS_DELETED = 'N'");
		sql.append("  	AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  	AND GEPC.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  	AND GRR.STUDENT_ID = GEA.STUDENT_ID");
		sql.append("  	AND GEPC.COURSE_ID = GRR.COURSE_ID");
		sql.append("  	AND GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID");
		sql.append("  	AND GC.COURSE_ID = GEPC.COURSE_ID");
		sql.append("  	AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");

		if (EmptyUtils.isNotEmpty(searchParams.get("STUDENT_ID"))){
			sql.append("  	AND GEA.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("  	AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE",ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		sql.append("  	AND GRR.BESPEAK_STATE = '1'");
		sql.append("  ORDER BY");
		sql.append("  	GTP.KKXQ");

		return commonDao.queryForMapListNative(sql.toString(),params);

	}

	/**
	 * 我的考试更新成绩和考试状态
	 * @param searchParams
	 * @return
	 */
	public int updateExamScore(Map searchParams){
		try{
			Map param = new HashMap();
			StringBuilder sql = new StringBuilder();
			
			sql.append("  UPDATE GJT_EXAM_APPOINTMENT_NEW GEA");
			sql.append("  SET GEA.EXAM_SCORE  = :EXAM_SCORE,");
			sql.append("  GEA.EXAM_STATUS = :EXAM_STATUS,");
			sql.append("  GEA.EXAM_COUNT = :EXAM_COUNT,");
			sql.append("  GEA.UPDATED_BY = '个人空间更新成绩',");
			sql.append("  GEA.UPDATED_DT  = SYSDATE");
			sql.append("  WHERE GEA.IS_DELETED = '0'");
			sql.append("  AND GEA.APPOINTMENT_ID = :APPOINTMENT_ID");

			param.put("EXAM_SCORE", ObjectUtils.toString(searchParams.get("EXAM_SCORE")));
			param.put("EXAM_STATUS", ObjectUtils.toString(searchParams.get("EXAM_STATUS")));
			param.put("EXAM_COUNT", ObjectUtils.toString(searchParams.get("EXAM_COUNT")));
			param.put("APPOINTMENT_ID", ObjectUtils.toString(searchParams.get("APPOINTMENT_ID")));
			
			return commonDao.updateForMapNative(sql.toString(), param);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
}
