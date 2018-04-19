package com.gzedu.xlims.dao.exam;

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
import com.gzedu.xlims.pojo.GjtCourse;

@Repository
public class GjtExamSubjectCourseDao {

	@Autowired
	private CommonDao commonDao;
	
	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager em;
	
	
	/**
	 * 查询选择的课程列表信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getGjtCourseList(Map searchParams,PageRequest pageRequest){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	TAB.COURSE_ID,");
		sql.append("  	TAB.COURSE_NAME,");
		sql.append("  	TAB.KCH,");
		sql.append("  	TAB.COURSE_TYPE_NAME,");
		sql.append("  	TAB.PYCC_NAME,");
		sql.append("  	TAB.EMPLOYEE_NAME,");
		sql.append("  	TAB.HOUR,");
		sql.append("  	TAB.COURSE_TYPE,");
		sql.append("  	TAB.PYCC,");
		sql.append("  	TAB.CREATED_DT");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GC.COURSE_ID,");
		sql.append("  			GC.KCMC COURSE_NAME,");
		sql.append("  			GC.KCH,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					TSD.NAME");
		sql.append("  				FROM");
		sql.append("  					TBL_SYS_DATA TSD");
		sql.append("  				WHERE");
		sql.append("  					TSD.IS_DELETED = 'N'");
		sql.append("  					AND TSD.TYPE_CODE = 'CourseType'");
		sql.append("  					AND TSD.CODE = GC.COURSE_TYPE");
		sql.append("  			) COURSE_TYPE_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					TSD.NAME");
		sql.append("  				FROM");
		sql.append("  					TBL_SYS_DATA TSD");
		sql.append("  				WHERE");
		sql.append("  					TSD.IS_DELETED = 'N'");
		sql.append("  					AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  					AND TSD.CODE = GC.PYCC");
		sql.append("  			) PYCC_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					GEI.XM");
		sql.append("  				FROM");
		sql.append("  					GJT_EMPLOYEE_INFO GEI");
		sql.append("  				WHERE");
		sql.append("  					GEI.IS_DELETED = 'N'");
		sql.append("  					AND GEI.EMPLOYEE_ID = GC.LECTURER");
		sql.append("  			) EMPLOYEE_NAME,");
		sql.append("  			GC.HOUR,");
		sql.append("  			GC.COURSE_TYPE,");
		sql.append("  			GC.PYCC,");
		sql.append("  			GC.CREATED_DT");
		sql.append("  		FROM");
		sql.append("  			GJT_COURSE GC");
		sql.append("  		WHERE");
		sql.append("  			GC.IS_DELETED = 'N'");
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  			AND GC.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		if(EmptyUtils.isNotEmpty(searchParams.get("COURSE_NAME"))){
			sql.append("  	AND TAB.COURSE_NAME LIKE :COURSE_NAME ");
			params.put("COURSE_NAME", "%"+ObjectUtils.toString(searchParams.get("COURSE_NAME"))+"%");
			
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("COURSE_TYPE"))){
			sql.append("  	AND TAB.COURSE_TYPE = :COURSE_TYPE ");
			params.put("COURSE_TYPE", ObjectUtils.toString(searchParams.get("COURSE_TYPE")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("PYCC"))){
			sql.append("  	AND TAB.PYCC = :PYCC ");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EMPLOYEE_NAME"))){
			sql.append("  	AND TAB.EMPLOYEE_NAME LIKE :EMPLOYEE_NAME ");
			params.put("EMPLOYEE_NAME", "%"+ObjectUtils.toString(searchParams.get("EMPLOYEE_NAME"))+"%");
		}
		sql.append("  ORDER BY");
		sql.append("  	TAB.CREATED_DT DESC");

		return (Page)commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}
	
	/**
	 * 查询课程类型
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getCourseTypeList(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	TSD.CODE,");
		sql.append("  	TSD.NAME");
		sql.append("  FROM");
		sql.append("  	TBL_SYS_DATA TSD");
		sql.append("  WHERE");
		sql.append("  	TSD.IS_DELETED = 'N'");
		sql.append("  	AND TSD.TYPE_CODE = 'CourseType'");
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	/**
	 * 保存考试科目信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertExamSubject(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_SUBJECT_NEW(");
		sql.append("  			SUBJECT_ID,");
		sql.append("  			SUBJECT_CODE,");
		sql.append("  			NAME,");
		sql.append("  			TYPE,");
		sql.append("  			EXAM_NO,");
		sql.append("  			XX_ID,");
		sql.append("  			STATUS,");
		sql.append("  			CREATED_BY,");
		sql.append("  			CREATED_DT ");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:SUBJECT_ID,");
		sql.append("  		:SUBJECT_CODE,");
		sql.append("  		:NAME,");
		sql.append("  		:TYPE,");
		sql.append("  		:EXAM_NO,");
		sql.append("  		:XX_ID,");
		sql.append("  		:STATUS,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		SYSDATE");
		sql.append("  	)");

		params.put("SUBJECT_ID", ObjectUtils.toString(searchParams.get("SUBJECT_ID")));
		params.put("SUBJECT_CODE", ObjectUtils.toString(searchParams.get("SUBJECT_CODE")));
		params.put("NAME", ObjectUtils.toString(searchParams.get("NAME")));
		params.put("TYPE", Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		params.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("STATUS", Integer.parseInt(ObjectUtils.toString(searchParams.get("STATUS"))));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
		
		return commonDao.insertForMapNative(sql.toString(), params);

	}
	
	/**
	 * 保存考试科目与课程直接的关联
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertExamSubjectCourse(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_SUBJECT_COURSE(");
		sql.append("  			SUBJECT_COURSE_ID,");
		sql.append("  			COURSE_ID,");
		sql.append("  			SUBJECT_ID,");
		sql.append("  			SUBJECT_CODE,");
		sql.append("  			SUBJECT_TYPE,");
		sql.append("  			KCH,");
		sql.append("  			CREATED_DT,");
		sql.append("  			CREATED_BY,");
		sql.append("  			XX_ID,");
		sql.append("  			EXAM_NO");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:SUBJECT_COURSE_ID,");
		sql.append("  		:COURSE_ID,");
		sql.append("  		:SUBJECT_ID,");
		sql.append("  		:SUBJECT_CODE,");
		sql.append("  		:SUBJECT_TYPE,");
		sql.append("  		:KCH,");
		sql.append("  		SYSDATE,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		:XX_ID,");
		sql.append("  		:EXAM_NO");
		sql.append("  	)");
		
		params.put("SUBJECT_COURSE_ID", ObjectUtils.toString(searchParams.get("SUBJECT_COURSE_ID")));
		params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		params.put("SUBJECT_ID", ObjectUtils.toString(searchParams.get("SUBJECT_ID")));
		params.put("SUBJECT_CODE", ObjectUtils.toString(searchParams.get("SUBJECT_CODE")));
		params.put("SUBJECT_TYPE", ObjectUtils.toString(searchParams.get("SUBJECT_TYPE")));
		params.put("KCH", ObjectUtils.toString(searchParams.get("KCH")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")));
		
		return commonDao.insertForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询考试科目列表
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getExamSubjectCourseList(Map searchParams,PageRequest pageRequest){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GES.SUBJECT_ID,");
		sql.append("  	GES.SUBJECT_CODE,");
		sql.append("  	GES.NAME AS SUBJECT_NAME,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TO_CHAR( WM_CONCAT( GC.KCMC || '（' || GC.KCH || '）' ))");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_SUBJECT_COURSE GESC,");
		sql.append("  			GJT_COURSE GC");
		sql.append("  		WHERE");
		sql.append("  			GESC.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GESC.COURSE_ID = GC.COURSE_ID");
		sql.append("  			AND GESC.SUBJECT_ID = GES.SUBJECT_ID");
		sql.append("  	) COURSE_NAME,");
		sql.append("  	GES.EXAM_NO,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT( 1 )");
		sql.append("  		FROM");
		sql.append("  			VIEW_TEACH_PLAN GTP");
		sql.append("  		LEFT JOIN GJT_COURSE GC ON");
		sql.append("  			GC.COURSE_ID = GTP.COURSE_ID");
		sql.append("  			AND GC.KCH = GTP.COURSE_CODE");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  		LEFT JOIN GJT_EXAM_SUBJECT_COURSE GESC ON");
		sql.append("  			GESC.COURSE_ID = GC.COURSE_ID");
		sql.append("  			AND GESC.IS_DELETED = 'N'");
		sql.append("  		WHERE");
		sql.append("  			GTP.IS_DELETED = 'N'");
		sql.append("  			AND GESC.SUBJECT_ID = GES.SUBJECT_ID");
		sql.append("  	) ARRANGE_COUNT,");
		sql.append("  	GES.STATUS");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_SUBJECT_NEW GES");
		sql.append("  WHERE");
		sql.append("  	GES.IS_DELETED = 0");
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GES.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		
		return commonDao.queryForPageNative(sql.toString(), params, pageRequest);
		
	}
	
	/**
	 * 查询是否存在试卷号
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getIsExistExamNo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(1) TEMP");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_SUBJECT_NEW GES");
		sql.append("  WHERE");
		sql.append("  	GES.IS_DELETED = 0");
		sql.append("  	AND GES.XX_ID = :XX_ID ");
		sql.append("  	AND GES.EXAM_NO = :EXAM_NO ");
		sql.append("  	AND GES.TYPE = :TYPE ");
		
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")).trim());
		params.put("TYPE", Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	/**
	 * 查询未创建考试科目的课程目录表
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List noSubjectCourseList(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GC.KCH,GC.KCMC");
		sql.append("  FROM");
		sql.append("  	GJT_COURSE GC");
		sql.append("  WHERE");
		sql.append("  	GC.IS_DELETED = 'N'");
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GC.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		sql.append("  	AND GC.COURSE_ID NOT IN(");
		sql.append("  		SELECT");
		sql.append("  			GESC.COURSE_ID");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_SUBJECT_COURSE GESC");
		sql.append("  		WHERE");
		sql.append("  			GESC.IS_DELETED = 'N'");
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GESC.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		sql.append("  			AND GESC.SUBJECT_TYPE = :SUBJECT_TYPE ");
		sql.append("  	)");

		params.put("SUBJECT_TYPE", ObjectUtils.toString(searchParams.get("SUBJECT_TYPE")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 获得课程是否存在
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getCourseExamNo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GC.COURSE_ID,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT( GESC.SUBJECT_COURSE_ID )");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_SUBJECT_COURSE GESC");
		sql.append("  		WHERE");
		sql.append("  			GESC.IS_DELETED = 'N'");
		sql.append("  			AND GESC.SUBJECT_TYPE = :TYPE ");
		sql.append("  			AND GESC.COURSE_ID = GC.COURSE_ID");
		sql.append("  	) SUBJECT_COUNT,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GES.SUBJECT_CODE");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_SUBJECT_NEW GES");
		sql.append("  		WHERE");
		sql.append("  			GES.IS_DELETED = 0");
		sql.append("  			AND GES.EXAM_NO = :EXAM_NO ");
		sql.append("  			AND ROWNUM = 1");
		sql.append("  	) SUBJECT_CODE");
		sql.append("  FROM");
		sql.append("  	GJT_COURSE GC");
		sql.append("  WHERE");
		sql.append("  	GC.IS_DELETED = 'N'");
		sql.append("  	AND GC.KCH = :KCH ");
		sql.append("  	AND GC.XX_ID = :XX_ID ");
		
		params.put("TYPE", ObjectUtils.toString(searchParams.get("examType")));
		params.put("EXAM_NO", ObjectUtils.toString(searchParams.get("examNo")));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("xxId")));
		params.put("KCH", ObjectUtils.toString(searchParams.get("kch")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
}
