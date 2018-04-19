package com.ouchgzee.headTeacher.dao.exam;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated @Repository("bzrGjtExamAppointmentDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtExamAppointmentDao {

	@Autowired
	private CommonDao commonDao;

	/**
	 * 查询当前学期的考试预约情况
	 * @param classId
	 * @param batchCode
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> findCurrentAppointmentList(String classId, String batchCode, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select ")
			.append("     g1.STUDENT_ID as \"studentId\", ")
			.append("     g1.XM as \"xm\", ")
			.append("     g1.XH as \"xh\", ")
			.append("     g1.SJH as \"sjh\", ")
			.append("     (select NAME from TBL_SYS_DATA where TYPE_CODE = 'TrainingLevel' and IS_DELETED = 'N' and CODE = g1.PYCC) as \"pycc\", ")
			.append("     gy.NAME as \"year\", ")
			.append("     gg.GRADE_NAME as \"grade\", ")
			.append("     gs.ZYMC as \"specialty\", ")
			.append("     nvl(t1.COUNT1, 0) as \"count1\", ")
			.append("     nvl(t2.COUNT2, 0) as \"count2\", ")
			.append("     case when t3.STUDENT_ID is null then 0 else 1 end as \"pointStatus\" ")
			.append("from ")
			.append("     GJT_STUDENT_INFO g1 ")
			.append("left join ")
			.append("     GJT_GRADE gg ")
			.append("on ")
			.append("     gg.GRADE_ID = g1.NJ ")
			.append("left join ")
			.append("     GJT_YEAR gy ")
			.append("on ")
			.append("     gy.GRADE_ID = gg.YEAR_ID ")
			.append("left join ")
			.append("     GJT_SPECIALTY gs ")
			.append("on ")
			.append("     gs.SPECIALTY_ID = g1.MAJOR ")
			.append("left join ")
			.append("     (select ")
			.append("     		gsi.STUDENT_ID, ")
			.append("     		count(distinct vep.COURSE_ID) COUNT1 ")
			.append("      from ")
			.append("     		VIEW_EXAM_PLAN_SC vep, ")
			.append("     		GJT_STUDENT_INFO gsi, ")
			.append("     		GJT_REC_RESULT grr,  ")
			.append("     		GJT_EXAM_BATCH_NEW geb, ")
			.append("     		GJT_GRADE gg1, ")
			.append("     		VIEW_TEACH_PLAN gtp, ")
			.append("     		GJT_GRADE gg2 ")
			.append("      where ")
			.append("     		vep.EXAM_BATCH_CODE = :batchCode ")
			.append("     		and gsi.STUDENT_ID in ( ")
			.append("     								select ")
			.append("     									STUDENT_ID ")
			.append("     								from ")
			.append("     									GJT_CLASS_STUDENT ")
			.append("     								where ")
			.append("     									CLASS_ID = :classId and IS_DELETED='N' ")
			.append("     								) ")
			.append("     		and grr.STUDENT_ID = gsi.STUDENT_ID ")
			.append("     		and grr.IS_DELETED = 'N' ")
			.append("     		and vep.COURSE_ID = grr.COURSE_ID ")
			.append("     		and (vep.SPECIALTY_ID = gsi.MAJOR or vep.SPECIALTY_ID = '-1') ")
			.append("     		and grr.EXAM_STATE != '1' ")
			.append("     		and geb.EXAM_BATCH_CODE = vep.EXAM_BATCH_CODE ")
			.append("     		and gg1.GRADE_ID = geb.GRADE_ID ")
			.append("     		and gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID ")
			.append("     		and gg2.GRADE_ID = gtp.ACTUAL_GRADE_ID ")
			.append("     		and gg2.START_DATE <= gg1.START_DATE ")
			.append("      group by gsi.STUDENT_ID ")
			.append("      ) t1 ")
			.append("on ")
			.append("     g1.STUDENT_ID = t1.STUDENT_ID ")
			.append("left join ")
			.append("     (select ")
			.append("     		gea.STUDENT_ID, ")
			.append("     		count(*) COUNT2 ")
			.append("      from ")
			.append("     		GJT_EXAM_APPOINTMENT_NEW gea, ")
			.append("     		GJT_EXAM_PLAN_NEW gep, ")
			.append("     		GJT_EXAM_PLAN_NEW_COURSE gepc, ")
			.append("     		GJT_REC_RESULT           grr ")
			.append("      where ")
			.append("     		gea.IS_DELETED = 0 ")
			.append("     		and gea.EXAM_PLAN_ID = gep.EXAM_PLAN_ID ")
			.append("     		and gep.IS_DELETED = 0 ")
			.append("     		and gepc.EXAM_PLAN_ID = gep.EXAM_PLAN_ID ")
			.append("     		and grr.IS_DELETED = 'N' ")
			.append("     		and grr.STUDENT_ID = gea.STUDENT_ID ")
			.append("     		and gepc.COURSE_ID = grr.COURSE_ID ")
			.append("     		and gep.EXAM_BATCH_CODE = :batchCode ")
			.append("     		and gea.STUDENT_ID in ( ")
			.append("     								select ")
			.append("     									STUDENT_ID ")
			.append("     								from ")
			.append("     									GJT_CLASS_STUDENT ")
			.append("     								where ")
			.append("     									CLASS_ID = :classId and IS_DELETED='N' ")
			.append("     								) ")
			.append("      group by gea.STUDENT_ID ")
			.append("      ) t2 ")
			.append("on ")
			.append("     g1.STUDENT_ID = t2.STUDENT_ID ")
			.append("left join ")
			.append("     (select ")
			.append("     		distinct gepa.STUDENT_ID ")
			.append("      from ")
			.append("     		GJT_EXAM_POINT_APPOINTMENT_NEW gepa, ")
			.append("     		GJT_EXAM_BATCH_NEW geb ")
			.append("      where ")
			.append("     		geb.EXAM_BATCH_CODE = :batchCode ")
			.append("     		and gepa.GRADE_ID = geb.GRADE_ID ")
			.append("     		and gepa.IS_DELETED = 0  ")
			.append("     		and gepa.STUDENT_ID in ( ")
			.append("     								select ")
			.append("     									STUDENT_ID ")
			.append("     								from ")
			.append("     									GJT_CLASS_STUDENT ")
			.append("     								where ")
			.append("     									CLASS_ID = :classId and IS_DELETED='N' ")
			.append("     								) ")
			.append("      ) t3 ")
			.append("on ")
			.append("     g1.STUDENT_ID = t3.STUDENT_ID ")
			.append("where ")
			.append("     g1.IS_DELETED = 'N' and g1.STUDENT_ID in (")
			.append("     								select ")
			.append("     									STUDENT_ID ")
			.append("     								from ")
			.append("     									GJT_CLASS_STUDENT ")
			.append("     								where ")
			.append("     									CLASS_ID = :classId and IS_DELETED='N' ")
			.append("     								) ");
		
		param.put("batchCode", batchCode);
		param.put("classId", classId);
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LIKE_xh")))) {
			sql.append("  and g1.XH like :xh ");
			param.put("xh", "%" + ObjectUtils.toString(searchParams.get("LIKE_xh") + "%"));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LIKE_xm")))) {
			sql.append("  and g1.XM like :xm ");
			param.put("xm", "%" + ObjectUtils.toString(searchParams.get("LIKE_xm")) + "%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gradeId")))) {
			sql.append("  and g1.NJ = :gradeId ");
			param.put("gradeId", ObjectUtils.toString(searchParams.get("EQ_gradeId")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_specialtyId")))) {
			sql.append("  and g1.MAJOR = :specialtyId ");
			param.put("specialtyId", ObjectUtils.toString(searchParams.get("EQ_specialtyId")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_pycc")))) {
			sql.append("  and g1.pycc = :pycc ");
			param.put("pycc", ObjectUtils.toString(searchParams.get("EQ_pycc")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_appointmentStatus")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("EQ_appointmentStatus")))) {  //未预约科目
				sql.append("  and t2.COUNT2 is null ");
			} else {
				sql.append("  and t2.COUNT2 is not null ");
			}
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_pointStatus")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("EQ_pointStatus")))) {  //未预约考点
				sql.append("  and t3.STUDENT_ID is null ");
			} else {
				sql.append("  and t3.STUDENT_ID is not null ");
			}
		}
		
		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 查询历史学期的考试预约情况
	 * @param classId
	 * @param batchCode
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> findHistoryAppointmentList(String classId, String batchCode, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select ")
			.append("     g1.STUDENT_ID as \"studentId\", ")
			.append("     g1.XM as \"xm\", ")
			.append("     g1.XH as \"xh\", ")
			.append("     g1.SJH as \"sjh\", ")
			.append("     (select NAME from TBL_SYS_DATA where TYPE_CODE = 'TrainingLevel' and IS_DELETED = 'N' and CODE = g1.PYCC) as \"pycc\", ")
			.append("     gy.NAME as \"year\", ")
			.append("     gg.GRADE_NAME as \"grade\", ")
			.append("     gs.ZYMC as \"specialty\", ")
			.append("     nvl(t2.COUNT2, 0) as \"count2\" ")
			.append("from ")
			.append("     GJT_STUDENT_INFO g1 ")
			.append("left join ")
			.append("     GJT_GRADE gg ")
			.append("on ")
			.append("     gg.GRADE_ID = g1.NJ ")
			.append("left join ")
			.append("     GJT_YEAR gy ")
			.append("on ")
			.append("     gy.GRADE_ID = gg.YEAR_ID ")
			.append("left join ")
			.append("     GJT_SPECIALTY gs ")
			.append("on ")
			.append("     gs.SPECIALTY_ID = g1.MAJOR ")
			.append("left join ")
			.append("     (select ")
			.append("     		gea.STUDENT_ID, ")
			.append("     		count(*) COUNT2 ")
			.append("      from ")
			.append("     		GJT_EXAM_APPOINTMENT_NEW gea, ")
			.append("     		GJT_EXAM_PLAN_NEW gep, ")
			.append("     		GJT_EXAM_PLAN_NEW_COURSE gepc, ")
			.append("     		GJT_REC_RESULT           grr ")
			.append("      where ")
			.append("     		gea.IS_DELETED = 0 ")
			.append("     		and gea.EXAM_PLAN_ID = gep.EXAM_PLAN_ID ")
			.append("     		and gep.IS_DELETED = 0 ")
			.append("     		and gepc.EXAM_PLAN_ID = gep.EXAM_PLAN_ID ")
			.append("     		and grr.IS_DELETED = 'N' ")
			.append("     		and grr.STUDENT_ID = gea.STUDENT_ID ")
			.append("     		and gepc.COURSE_ID = grr.COURSE_ID ")
			.append("     		and gep.EXAM_BATCH_CODE = :batchCode ")
			.append("     		and gea.STUDENT_ID in ( ")
			.append("     								select ")
			.append("     									STUDENT_ID ")
			.append("     								from ")
			.append("     									GJT_CLASS_STUDENT ")
			.append("     								where ")
			.append("     									CLASS_ID = :classId and IS_DELETED='N' ")
			.append("     								) ")
			.append("      group by gea.STUDENT_ID ")
			.append("      ) t2 ")
			.append("on ")
			.append("     g1.STUDENT_ID = t2.STUDENT_ID ")
			.append("where ")
			.append("     g1.IS_DELETED = 'N' and g1.STUDENT_ID in (")
			.append("     								select ")
			.append("     									STUDENT_ID ")
			.append("     								from ")
			.append("     									GJT_CLASS_STUDENT ")
			.append("     								where ")
			.append("     									CLASS_ID = :classId and IS_DELETED='N' ")
			.append("     								) ");
		
		param.put("batchCode", batchCode);
		param.put("classId", classId);
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LIKE_xh")))) {
			sql.append("  and g1.XH like :xh ");
			param.put("xh", "%" + ObjectUtils.toString(searchParams.get("LIKE_xh") + "%"));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LIKE_xm")))) {
			sql.append("  and g1.XM like :xm ");
			param.put("xm", "%" + ObjectUtils.toString(searchParams.get("LIKE_xm")) + "%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gradeId")))) {
			sql.append("  and g1.NJ = :gradeId ");
			param.put("gradeId", ObjectUtils.toString(searchParams.get("EQ_gradeId")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_specialtyId")))) {
			sql.append("  and g1.MAJOR = :specialtyId ");
			param.put("specialtyId", ObjectUtils.toString(searchParams.get("EQ_specialtyId")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_pycc")))) {
			sql.append("  and g1.pycc = :pycc ");
			param.put("pycc", ObjectUtils.toString(searchParams.get("EQ_pycc")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_appointmentStatus")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("EQ_appointmentStatus")))) {  //未预约科目
				sql.append("  and t2.COUNT2 is null ");
			} else {
				sql.append("  and t2.COUNT2 is not null ");
			}
		}
		
		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 查询学员当前学期的考试预约情况
	 * @param studentId
	 * @param batchCode
	 * @return
	 */
	public List<Map<String, Object>> findCurrentStudentAppointment(String studentId, String batchCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select ")
			.append("     t1.STUDENT_ID as \"studentId\", ")
			.append("     t1.COURSE_ID as \"courseId\", ")
			.append("     t1.KCMC as \"courseName\", ")
			.append("     t1.KKXQ as \"term\", ")
			.append("     t1.TYPE as \"type\", ")
			.append("     t1.EXAM_ST as \"examSt\", ")
			.append("     t1.EXAM_END as \"examEnd\", ")
			.append("     t1.BOOK_ST as \"bookSt\", ")
			.append("     t1.BOOK_END as \"bookEnd\", ")
			.append("     t1.PAY_STATE as \"payState\", ")
			.append("     t2.COURSE_ID as \"courseId2\" ")
			.append("from ")
			.append("     (select ")
			.append("     		distinct ")
			.append("     		gsi.STUDENT_ID, ")
			.append("     		vep.COURSE_ID, ")
			.append("     		gc.KCMC, ")
			.append("     		gtp.KKXQ, ")
			.append("     		(select NAME from TBL_SYS_DATA where TYPE_CODE = 'ExaminationMode' and IS_DELETED = 'N' and CODE = vep.TYPE) as TYPE, ")
			.append("     		gep.EXAM_ST, ")
			.append("     		gep.EXAM_END, ")
			.append("     		geb.BOOK_ST, ")
			.append("     		geb.BOOK_END, ")
			.append("     		grr.PAY_STATE ")
			.append("      from ")
			.append("     		VIEW_EXAM_PLAN_SC vep, ")
			.append("     		GJT_STUDENT_INFO gsi, ")
			.append("     		GJT_REC_RESULT grr,  ")
			.append("     		GJT_COURSE gc, ")
			.append("     		VIEW_TEACH_PLAN gtp, ")
			.append("     		GJT_EXAM_PLAN_NEW gep, ")
			.append("     		GJT_EXAM_BATCH_NEW geb, ")
			.append("     		GJT_GRADE gg1, ")
			.append("     		GJT_GRADE gg2 ")
			.append("      where ")
			.append("     		vep.EXAM_BATCH_CODE = :batchCode ")
			.append("     		and gsi.STUDENT_ID = :studentId ")
			.append("     		and grr.STUDENT_ID = gsi.STUDENT_ID ")
			.append("     		and grr.IS_DELETED = 'N' ")
			.append("     		and vep.COURSE_ID = grr.COURSE_ID ")
			.append("     		and (vep.SPECIALTY_ID = gsi.MAJOR or vep.SPECIALTY_ID = '-1') ")
			.append("     		and grr.EXAM_STATE != '1' ")
			.append("     		and gc.COURSE_ID = vep.COURSE_ID ")
			.append("     		and gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID ")
			.append("     		and gep.EXAM_PLAN_ID = vep.EXAM_PLAN_ID ")
			.append("     		and geb.EXAM_BATCH_CODE = gep.EXAM_BATCH_CODE ")
			.append("     		and gg1.GRADE_ID = geb.GRADE_ID ")
			.append("     		and gg2.GRADE_ID = gtp.ACTUAL_GRADE_ID ")
			.append("     		and gg2.START_DATE <= gg1.START_DATE ")
			.append("      ) t1 ")
			.append("left join ")
			.append("     (select ")
			.append("     		distinct ")
			.append("     		gea.STUDENT_ID, ")
			.append("     		gepc.COURSE_ID ")
			.append("      from ")
			.append("     		GJT_EXAM_APPOINTMENT_NEW gea, ")
			.append("     		GJT_EXAM_PLAN_NEW gep, ")
			.append("     		GJT_EXAM_PLAN_NEW_COURSE gepc, ")
			.append("     		GJT_REC_RESULT           grr ")
			.append("      where ")
			.append("     		gea.IS_DELETED = 0 ")
			.append("     		and gea.EXAM_PLAN_ID = gep.EXAM_PLAN_ID ")
			.append("     		and gep.IS_DELETED = 0 ")
			.append("     		and gepc.EXAM_PLAN_ID = gep.EXAM_PLAN_ID ")
			.append("     		and grr.IS_DELETED = 'N' ")
			.append("     		and grr.STUDENT_ID = gea.STUDENT_ID ")
			.append("     		and gepc.COURSE_ID = grr.COURSE_ID ")
			.append("     		and gep.EXAM_BATCH_CODE = :batchCode ")
			.append("     		and gea.STUDENT_ID = :studentId ")
			.append("      ) t2 ")
			.append("on ")
			.append("     t1.STUDENT_ID = t2.STUDENT_ID and t1.COURSE_ID = t2.COURSE_ID ")
			.append("order by t1.KKXQ ");
		
		param.put("batchCode", batchCode);
		param.put("studentId", studentId);
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 查询学员历史学期的考试预约情况
	 * @param studentId
	 * @param batchCode
	 * @return
	 */
	public List<Map<String, Object>> findHistoryStudentAppointment(String studentId, String batchCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("     select ")
			.append("     		distinct ")
			.append("     		gea.STUDENT_ID as \"studentId\", ")
			.append("     		gepc.COURSE_ID as \"courseId\", ")
			.append("     		gtp.KKXQ as \"term\", ")
			.append("     		gc.KCMC as \"courseName\", ")
			.append("     		(select NAME from TBL_SYS_DATA where TYPE_CODE = 'ExaminationMode' and IS_DELETED = 'N' and CODE = gep.TYPE) as \"type\", ")
			.append("     		gep.EXAM_ST as \"examSt\", ")
			.append("     		gep.EXAM_END as \"examEnd\", ")
			.append("     		TO_CHAR( NVL(( SELECT NAME FROM TBL_SYS_DATA TSD WHERE IS_DELETED = 'N' AND TYPE_CODE = 'EXAM_SCORE' AND CODE = grr.EXAM_SCORE1 ), grr.EXAM_SCORE1 )) as \"examScore\" ")
			.append("      from ")
			.append("     		GJT_EXAM_APPOINTMENT_NEW gea, ")
			.append("     		GJT_EXAM_PLAN_NEW gep, ")
			.append("     		GJT_EXAM_PLAN_NEW_COURSE gepc, ")
			.append("     		GJT_REC_RESULT grr, ")
			.append("     		VIEW_TEACH_PLAN gtp, ")
			.append("     		GJT_COURSE gc ")
			.append("      where ")
			.append("     		gea.IS_DELETED = 0 ")
			.append("     		and gea.EXAM_PLAN_ID = gep.EXAM_PLAN_ID ")
			.append("     		and gep.IS_DELETED = 0 ")
			.append("     		and gepc.EXAM_PLAN_ID = gep.EXAM_PLAN_ID ")
			.append("     		and gep.EXAM_BATCH_CODE = :batchCode ")
			.append("     		and gea.STUDENT_ID = :studentId ")
			.append("     		and grr.IS_DELETED = 'N' ")
			.append("     		and grr.STUDENT_ID = gea.STUDENT_ID ")
			.append("     		and gepc.COURSE_ID = grr.COURSE_ID ")
			.append("     		and gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID ")
			.append("     		and gc.COURSE_ID = gepc.COURSE_ID ")
			.append("     	order by gtp.KKXQ ");
		
		param.put("batchCode", batchCode);
		param.put("studentId", studentId);
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询学员的预约考点
	 * @param studentId
	 * @param batchCode
	 * @return
	 */
	public List<Map<String, Object>> findStudentPointAppointment(String studentId, String batchCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEP.NAME AS \"pointName\",");
		sql.append("  	(");
		sql.append("  		(");
		sql.append("  			SELECT");
		sql.append("  				MAX( GAA.CITYNAME )");
		sql.append("  			FROM");
		sql.append("  				GJT_AREA GAA");
		sql.append("  			WHERE");
		sql.append("  				GAA.DISTRICT LIKE '%' || SUBSTR( GA.DISTRICT, 1, 2 )|| '0000%'");
		sql.append("  		)|| '-' ||(");
		sql.append("  			SELECT");
		sql.append("  				MAX( GAA.CITYNAME )");
		sql.append("  			FROM");
		sql.append("  				GJT_AREA GAA");
		sql.append("  			WHERE");
		sql.append("  				GAA.DISTRICT LIKE '%' || SUBSTR( GA.DISTRICT, 1, 4 )|| '00%'");
		sql.append("  		)|| '-' || GA.CITYNAME");
		sql.append("  	) AS \"areaName\",");
		sql.append("  	GEP.ADDRESS AS \"address\" ");
		sql.append("  FROM");
		sql.append("  	GJT_AREA GA,");
		sql.append("  	GJT_EXAM_POINT_NEW GEP");
		sql.append("  JOIN GJT_EXAM_POINT_APPOINTMENT_NEW GEPA ON");
		sql.append("  	GEP.EXAM_POINT_ID = GEPA.EXAM_POINT_ID");
		sql.append("  	AND GEPA.IS_DELETED = 0");
		sql.append("  WHERE");
		sql.append("  	GA.IS_DELETED = 'N'");
		sql.append("  	AND GEP.IS_DELETED = 'N'");
		sql.append("  	AND GEP.AREA_ID = GA.DISTRICT");
		sql.append("  	AND GEPA.STUDENT_ID = :studentId ");
		sql.append("	AND GEP.EXAM_BATCH_CODE = :batchCode ");
		sql.append(" ORDER BY  GEPA.CREATED_DT DESC ");
		
		param.put("batchCode", batchCode);
		param.put("studentId", studentId);
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}

}
