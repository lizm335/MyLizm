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
public class GjtTeachCourseDao extends BaseDaoImpl {

	@Autowired
	private CommonDao commonDao;
	
	/**
	 * 任教管理列表
	 * @return
	 */
	public Page getTeachCourseList(Map searchParams, PageRequest pageRequst) {
		Map param = getTeachCourseListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return (Page) commonDao.queryForPageNative(sql, param, pageRequst);
	}
	
	/**
	 * 任教管理列表
	 * @return
	 */
	public List getTeachCourseList(Map searchParams) {
		Map param = getTeachCourseListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return commonDao.queryForMapListNative(sql, param);
	}
	
	/**
	 * 老师列表
	 * @return
	 */
	public Page getTeacherList(Map searchParams, PageRequest pageRequst) {
		Map param = getTeacherInfoSql(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return (Page) commonDao.queryForPageNative(sql, param, pageRequst);
	}
	
	/**
	 * 老师列表
	 * @return
	 */
	public List getTeacherList(Map searchParams) {
		Map param = getTeacherInfoSql(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		return commonDao.queryForMapListNative(sql, param);
	}
	
	/**
	 * 任教管理列表（统计）
	 * @return
	 */
	public List getTeachCourseCount(Map searchParams) {
		Map param = getTeachCourseListSQL(searchParams);
		String sql = ObjectUtils.toString(param.get("sql"));
		param.remove("sql");
		sql = "SELECT COUNT(*) STATE_COUNT FROM ("+sql+") ";
		return commonDao.queryForMapListNative(sql, param);
	}
	
	/**
	 * 任教管理列表（sql）
	 * @return
	 */
	public Map getTeachCourseListSQL(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GEI.ZP,");
		sql.append("  GEI.ZGH,");
		sql.append("  GEI.XM,");
		sql.append("  GC.KCMC,");
		sql.append("  GC.KCH,");
		sql.append("  GC.COURSE_ID,");
		sql.append("  GSC.SC_NAME,");
		sql.append("  GSC.SC_CODE,");
		sql.append("  GCT.TEACHER_TYPE,");
		sql.append("  GCT.STATE,");
		sql.append("  GCT.APP_ID CENTER_ID,");
		sql.append("  GCT.COURSE_TEACHER_ID,");
		sql.append("  GCT.IS_SYNCHRO");
		sql.append("  FROM GJT_COURSE_TEACHER GCT");
		sql.append("  LEFT JOIN GJT_EMPLOYEE_INFO GEI");
		sql.append("  ON GEI.EMPLOYEE_ID = GCT.TEACHER_ID");
		sql.append("  LEFT JOIN GJT_COURSE GC");
		sql.append("  ON GC.COURSE_ID = GCT.COURSE_ID");
		sql.append("  LEFT JOIN GJT_STUDY_CENTER GSC");
		sql.append("  ON GSC.ID = GCT.APP_ID");
		
		sql.append("  WHERE GEI.IS_DELETED = 'N'");
		sql.append("  AND GCT.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GSC.IS_DELETED = 'N'");
		
		sql.append("  AND GEI.XX_ID = :XX_ID");
		param.put("XX_ID",ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ZGH")))) {
			sql.append("  AND GEI.ZGH LIKE :ZGH");
			param.put("ZGH", "%"+ObjectUtils.toString(searchParams.get("ZGH"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GEI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("KCMC")))) {
			sql.append("  AND GC.KCMC LIKE :KCMC");
			param.put("KCMC", "%"+ObjectUtils.toString(searchParams.get("KCMC"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CENTER_ID")))) {
			sql.append("  AND GSC.ID = :CENTER_ID");
			param.put("CENTER_ID",ObjectUtils.toString(searchParams.get("CENTER_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TEACHER_TYPE")))) {
			sql.append("  AND GCT.TEACHER_TYPE = :TEACHER_TYPE");
			param.put("TEACHER_TYPE",ObjectUtils.toString(searchParams.get("TEACHER_TYPE")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TEACHER_ID")))) {
			sql.append("  AND GCT.TEACHER_ID = :TEACHER_ID");
			param.put("TEACHER_ID",ObjectUtils.toString(searchParams.get("TEACHER_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GCT.COURSE_ID = :COURSE_ID");
			param.put("COURSE_ID",ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STATE")))) {
			sql.append("  AND GCT.STATE = :STATE");
			param.put("STATE",ObjectUtils.toString(searchParams.get("STATE")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("IS_SYNCHRO")))) {
			sql.append("  AND GCT.IS_SYNCHRO = :IS_SYNCHRO");
			param.put("IS_SYNCHRO",ObjectUtils.toString(searchParams.get("IS_SYNCHRO")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_TEACHER_ID")))) {
			sql.append("  AND GCT.COURSE_TEACHER_ID = :COURSE_TEACHER_ID");
			param.put("COURSE_TEACHER_ID",ObjectUtils.toString(searchParams.get("COURSE_TEACHER_ID")));
		}
		
		param.put("sql", sql);
		return param;
	}
	
	/**
	 * 删除任教信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public int delTeacherInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_COURSE_TEACHER GCT");
		sql.append("  SET GCT.IS_DELETED = 'Y', GCT.UPDATED_DT = SYSDATE, GCT.UPDATED_BY = :UPDATED_BY ");
		sql.append("  WHERE GCT.IS_DELETED = 'N'");
		sql.append("  AND GCT.COURSE_TEACHER_ID = :COURSE_TEACHER_ID");

		param.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
		param.put("COURSE_TEACHER_ID", ObjectUtils.toString(searchParams.get("COURSE_TEACHER_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 更新任教信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public int uptTeacherInfo(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_COURSE_TEACHER GCT");
		sql.append("  SET GCT.UPDATED_DT = SYSDATE, GCT.UPDATED_BY = :UPDATED_BY ");
		
		param.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TEACHER_TYPE")))) {
			sql.append("  ,GCT.TEACHER_TYPE = :TEACHER_TYPE");
			param.put("TEACHER_TYPE",ObjectUtils.toString(searchParams.get("TEACHER_TYPE")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CENTER_ID")))) {
			sql.append("  ,GCT.APP_ID = :CENTER_ID");
			param.put("CENTER_ID",ObjectUtils.toString(searchParams.get("CENTER_ID")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STATE")))) {
			sql.append("  ,GCT.STATE = :STATE");
			param.put("STATE",ObjectUtils.toString(searchParams.get("STATE")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("IS_SYNCHRO")))) {
			sql.append("  ,GCT.IS_SYNCHRO = :IS_SYNCHRO");
			param.put("IS_SYNCHRO",ObjectUtils.toString(searchParams.get("IS_SYNCHRO")));
		}
		
		sql.append("  WHERE GCT.IS_DELETED = 'N'");
		sql.append("  AND GCT.COURSE_TEACHER_ID = :COURSE_TEACHER_ID");

		param.put("COURSE_TEACHER_ID", ObjectUtils.toString(searchParams.get("COURSE_TEACHER_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 查询老师列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getTeacherInfoSql(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GEI.EMPLOYEE_ID,");
		sql.append("  GEI.ZGH,");
		sql.append("  GEI.XM,");
		sql.append("  (CASE GEI.EMPLOYEE_TYPE");
		sql.append("  WHEN '1' THEN");
		sql.append("  '班主任'");
		sql.append("  WHEN '2' THEN");
		sql.append("  '辅导教师'");
		sql.append("  WHEN '4' THEN");
		sql.append("  '督导教师'");
		sql.append("  WHEN '10' THEN");
		sql.append("  '论文教师'");
		sql.append("  ELSE");
		sql.append("  '其它'");
		sql.append("  END) EMPLOYEE_TYPE");
		sql.append("  ");
		sql.append("  FROM GJT_EMPLOYEE_INFO GEI");
		sql.append("  WHERE GEI.IS_DELETED = 'N'");

		sql.append("  AND GEI.XX_ID = :XX_ID");
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ZGH")))) {
			sql.append("  AND GEI.ZGH LIKE :ZGH");
			param.put("ZGH", "%"+ObjectUtils.toString(searchParams.get("ZGH"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GEI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EMPLOYEE_TYPE")))) {
			sql.append("  AND GEI.EMPLOYEE_TYPE = :EMPLOYEE_TYPE");
			param.put("EMPLOYEE_TYPE", ObjectUtils.toString(searchParams.get("EMPLOYEE_TYPE")));
		}
		param.put("sql", sql);
		return param;
	}
	
	/**
	 * 新增选课数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int addCourseTeacher(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_COURSE_TEACHER");
		sql.append("  (COURSE_TEACHER_ID, TEACHER_ID, COURSE_ID, TEACHER_TYPE, APP_ID, CREATED_BY)");
		sql.append("  VALUES");
		sql.append("  (:COURSE_TEACHER_ID,:TEACHER_ID,:COURSE_ID,:TEACHER_TYPE,:CENTER_ID,:CREATED_BY)");

		param.put("COURSE_TEACHER_ID", ObjectUtils.toString(searchParams.get("COURSE_TEACHER_ID")));
		param.put("TEACHER_ID", ObjectUtils.toString(searchParams.get("TEACHER_ID")));
		param.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		param.put("TEACHER_TYPE", ObjectUtils.toString(searchParams.get("TEACHER_TYPE")));
		param.put("CENTER_ID", ObjectUtils.toString(searchParams.get("CENTER_ID")));
		param.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 班级信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getClassInfoById(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GCI.CLASS_ID,GCI.BH,GCI.BJMC,COUNT(GCS.STUDENT_ID) STUDENT_COUNT");
		sql.append("  FROM GJT_CLASS_INFO GCI");
		sql.append("  LEFT JOIN GJT_CLASS_STUDENT GCS");
		sql.append("  ON GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.XX_ID = GCS.XX_ID");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND GCS.CLASS_ID = :CLASS_ID");
			param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		}
		sql.append("  GROUP BY GCI.CLASS_ID,GCI.BH,GCI.BJMC");
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 更新批量调班
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public int updClassStudent(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_CLASS_STUDENT GCS");
		sql.append("  SET GCS.UPDATED_BY = :UPDATED_BY, ");
		sql.append("  GCS.UPDATED_DT = SYSDATE, ");
		sql.append("  GCS.CLASS_ID = :NEW_CLASS_ID");
		sql.append("  WHERE GCS.IS_DELETED = 'N'");
		sql.append("  AND GCS.CLASS_ID = :CLASS_ID");

		param.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
		param.put("NEW_CLASS_ID", ObjectUtils.toString(searchParams.get("NEW_CLASS_ID")));
		param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 学员信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getClassStudent(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.XH, GCI.BH, GC.KCH, GCS.CLASS_STUDENT_ID");
		sql.append("  FROM GJT_CLASS_STUDENT GCS");
		sql.append("  LEFT JOIN GJT_STUDENT_INFO GSI");
		sql.append("  ON GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_CLASS_INFO GCI");
		sql.append("  ON GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.XX_ID = GCS.XX_ID");
		sql.append("  LEFT JOIN GJT_COURSE GC");
		sql.append("  ON GC.COURSE_ID = GCI.COURSE_ID");
		sql.append("  WHERE GCS.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NEW_CLASS_ID")))) {
			sql.append("  AND GCS.CLASS_ID = :NEW_CLASS_ID");
			param.put("NEW_CLASS_ID", ObjectUtils.toString(searchParams.get("NEW_CLASS_ID")));
		}
		sql.append("  GROUP BY GCI.CLASS_ID,GCI.BH,GCI.BJMC");
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
}
