/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.student;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 班级信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtClassInfoDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtClassInfoDao extends BaseDao<BzrGjtClassInfo, String> {

	/**
	 * 查询班级学员数
	 * 
	 * @param classId
	 * @return HQL-返回Long<br>
	 *         SQL-返回BigDecimal<br>
	 */
	@Query("SELECT COUNT(*) FROM BzrGjtClassInfo t INNER JOIN t.gjtClassStudentList b INNER JOIN b.gjtStudentInfo c WHERE t.isDeleted='N' AND b.isDeleted='N' AND c.isDeleted='N' AND t.classId=:classId")
	Long countStudent(@Param("classId") String classId);

	/**
	 * 查询班主任管理的班级数
	 *
	 * @param employeeId
	 * @return HQL-返回Long<br>
	 *         SQL-返回BigDecimal<br>
	 */
	@Query("SELECT COUNT(*) FROM BzrGjtClassInfo t WHERE t.isDeleted='N' AND t.classType='teach' AND t.bzrId=:employeeId")
	Long countClassByBzr(@Param("employeeId") String employeeId);

	/**
	 * 检验该班级是否在班主任管理下的班级
	 *
	 * @param classId
	 * @param employeeId
	 * @return HQL-返回Long<br>
	 *         SQL-返回BigDecimal<br>
	 */
	@Query("SELECT COUNT(*) FROM BzrGjtClassInfo t WHERE t.isDeleted='N' AND t.classType='teach' AND t.classId=:classId AND t.bzrId=:employeeId")
	Long countClassToBzrManage(@Param("classId") String classId, @Param("employeeId") String employeeId);

	/**
	 * 学籍班获取课程班
	 * 
	 * @param classId
	 * @return
	 */
	@Query(value = "select distinct d.course_id,d.kcmc from  ( SELECT gsi.student_id FROM GJT_STUDENT_INFO gsi"
			+ "  INNER JOIN GJT_CLASS_STUDENT gcs ON gcs.STUDENT_ID = gsi.STUDENT_ID AND gcs.IS_DELETED = 'N'"
			+ "  WHERE gsi.IS_DELETED = 'N'   AND gcs.class_id=:classId ) t "
			+ " inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N'"
			+ " inner join gjt_class_info c on b.class_id=c.class_id and c.is_deleted='N'"
			+ " left join gjt_course d on c.course_id=d.course_id and d.is_deleted='N'"
			+ " where c.class_type='course'", nativeQuery = true)
	List<Object[]> queryCourseClassInfoByTeachClassId(@Param("classId") String classId);

	/**
	 * 学籍班获取某学期的课程班
	 *
	 * @param classId
	 * @return
	 */
	@Query(value = "select distinct d.course_id,d.kcmc from  ( SELECT gsi.student_id FROM GJT_STUDENT_INFO gsi"
			+ "  INNER JOIN GJT_CLASS_STUDENT gcs ON gcs.STUDENT_ID = gsi.STUDENT_ID AND gcs.IS_DELETED = 'N'"
			+ "  WHERE gsi.IS_DELETED = 'N'   AND gcs.class_id=:classId ) t "
			+ " inner join gjt_class_student b on t.student_id=b.student_id and b.is_deleted='N'"
			+ " inner join gjt_class_info c on b.class_id=c.class_id and c.is_deleted='N'"
			+ " left join gjt_course d on c.course_id=d.course_id and d.is_deleted='N'"
			+ " where c.class_type='course'"
			+ "   and c.actual_grade_id=:termId", nativeQuery = true)
	List<Object[]> queryCourseClassInfoByTeachClassIdAndTermId(@Param("classId") String classId, @Param("termId") String termId);

	/**
	 * 根据班级Id查询班级学生Id
	 * 
	 * @param classId
	 * @return
	 */
	@Query(value = "select student_id  from gjt_class_student where class_id=:classId and is_deleted='N'", nativeQuery = true)
	List<String> queryTeacherClassStudent(@Param("classId") String classId);

	@Query(value = "select to_char(sum(d.xf)) XF, to_char(sum(d.zdf)) ZDXF,   to_char(sum(d.YXXF)) YXXF,"
			+ "  d.student_id  from (select t.kclbm KCLBM, sum(t.xf) XF,  count(*) KCSL, t.kclbm_code,"
			+ "  sum(EXAM_SCORE2) YXXF,   t.student_id,  (select sum(a.score)  from gjt_specialty_module_limit a, tbl_sys_data b"
			+ "   WHERE a.MODULE_ID = b.id   AND b.name = t.kclbm   and a.is_deleted = 'N'   and b.is_deleted = 'N'"
			+ "  AND a.specialty_id = :specialtyId) as ZDF  from (SELECT (SELECT tsd.name FROM tbl_sys_data tsd"
			+ "   WHERE IS_DELETED = 'N'  AND tsd.type_code = 'CourseType'   AND tsd.code = gtp.kclbm) AS KCLBM,"
			+ "   (SELECT GTP.XF FROM GJT_REC_RESULT grr   WHERE grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID"
			+ "  and vs.STUDENT_ID = grr.student_id     and grr.is_deleted = 'N'   and grr.exam_state=1 ) AS EXAM_SCORE2,"
			+ "   GTP.XF,     gtp.kclbm as kclbm_code,   vs.student_id   FROM VIEW_TEACH_PLAN    gtp,    GJT_COURSE  gc,"
			+ "   GJT_SPECIALTY     gs,gjt_student_info vs WHERE gtp.course_id = gc.course_id"
			+ "   AND gs.specialty_id = gtp.kkzy  AND gc.IS_DELETED = 'N'  AND GTP.IS_DELETED = 'N'"
			+ "  AND vs.PYCC = gtp.PYCC  AND vs.MAJOR = gtp.KKZY  AND vs.STUDENT_ID in(:ids)  and vs.nj = gtp.grade_id) t"
			+ "  group by t.kclbm, t.kclbm_code,t.student_id   order by kclbm_code) d group by d.student_id ", nativeQuery = true)
	List<Object[]> getStudentSpecialty(@Param("specialtyId") String specialtyId, @Param("ids") List<String> ids);

	/**
	 * 更新班级的状态
	 * 
	 * @param classId
	 * @param isEnabled
	 * @param updatedBy
	 * @param updatedDt
	 */
	@Modifying
	@Query("UPDATE BzrGjtClassInfo t SET t.version=t.version+1,t.isEnabled=:isEnabled,t.updatedBy=:updatedBy,t.updatedDt=:updatedDt WHERE t.classId=:classId")
	@Transactional(value="transactionManagerBzr")
	int updateEnabled(@Param("classId") String classId, @Param("isEnabled") String isEnabled,
			@Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);


	/**
	 * 查询教学班级
	 */
	@Query("select gci from  BzrGjtClassStudent g  inner join g.gjtStudentInfo gsi inner join g.gjtClassInfo gci"
			+ "  where g.isDeleted='N' and gci.isDeleted='N' and gsi.isDeleted='N' and gci.classType='teach'  and gsi.studentId=?1 ")
	BzrGjtClassInfo queryTeachByStudetnId(String studentId);
}
