/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtClassInfo;

/**
 * 
 * 功能说明： 班级管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtClassInfoDao extends JpaRepository<GjtClassInfo, String>, JpaSpecificationExecutor<GjtClassInfo> {

	/**
	 * 更新同步状态
	 * 
	 * @param syncStatus
	 * @param classId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtClassInfo g set g.syncStatus=?1 where g.classId=?2 ")
	int updateSyncStatus(String syncStatus, String classId);

	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtClassInfo g set g.isDeleted=?2 where g.classId=?1 ")
	int deleteById(String id, String str);

	GjtClassInfo findByBjmcAndGjtSchoolInfoIdAndIsDeleted(String bjmc, String xxId, String isDeleted);

	GjtClassInfo findByClassTypeAndGradeIdAndSpecialtyIdAndBjmcAndGjtSchoolInfoIdAndIsDeleted(String classType,
			String gradeId, String specialtyId, String bjmc, String xxId, String isDeleted);

	List<GjtClassInfo> findByClassTypeAndGradeIdAndSpecialtyIdAndGjtSchoolInfoIdAndIsDeleted(String classType,
			String gradeId, String specialtyId, String xxId, String isDeleted);

	/**
	 * 是否启用1启用，0停用
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtClassInfo g set g.isEnabled=?2 where g.classId=?1 ")
	int updateIsEnabled(String id, String str);

	GjtClassInfo findByStudyYearCourseId(String studyyearCourseId);

	/**
	 * 查询教学班信息
	 * 
	 * @param classType
	 * @param gradeId
	 * @param specialtyId
	 * @param xxzxId
	 * @param xxId
	 * @param isDeleted
	 * @return
	 */
	@Query("SELECT t FROM GjtClassInfo t INNER JOIN t.gjtSchoolInfo b WHERE t.classType=?1 and t.gradeId=?2 and t.specialtyId=?3 and t.xxzxId=?4 and b.id=?5 and t.pycc=?6 and t.isDeleted=?7")
	List<GjtClassInfo> findByClassTypeAndGradeIdAndSpecialtyIdAndXxzxIdAndGjtSchoolInfoIdAndPyccAndIsDeletedOrderByCreatedDtDesc(
			String classType, String gradeId, String specialtyId, String xxzxId, String xxId, String pycc,
			String isDeleted);

	/**
	 * 查询课程班信息
	 * 
	 * @param classType
	 * @param actualGradeId
	 * @param courseId
	 * @param bh
	 * @param xxId
	 * @param isDeleted
	 * @return
	 */
	@Query("SELECT t FROM GjtClassInfo t INNER JOIN t.gjtSchoolInfo b WHERE t.classType=?1 and t.actualGradeId=?2 and t.courseId=?3 and t.bh=?4 and b.id=?5 and t.isDeleted=?6")
	GjtClassInfo findByClassTypeAndActualGradeIdAndCourseIdAndBhAndGjtSchoolInfoIdAndIsDeleted(String classType,
			String actualGradeId, String courseId, String bh, String xxId, String isDeleted);

	List<GjtClassInfo> findByGradeIdAndSpecialtyId(String gradeId, String specialtyId);

	/**
	 * 获取学员的教学班
	 * 
	 * @param studentId
	 * @return
	 */
	@Query(value = "select b from GjtClassStudent t inner join t.gjtClassInfo b where t.isDeleted = 'N' and b.isDeleted = 'N' and b.classType = 'teach' and t.studentId = ?1")
	GjtClassInfo findTeachClassByStudentId(String studentId);

	/**
	 * 获取学员的课程班
	 * 
	 * @param studentId
	 * @param courseId
	 * @return
	 */
	@Query(value = "select b from GjtClassStudent t inner join t.gjtClassInfo b where t.isDeleted = 'N' and b.isDeleted = 'N' and b.classType = 'course' and t.studentId = ?1 and b.courseId =?2")
	GjtClassInfo findCourseClassByStudentIdAndCourseId(String studentId, String courseId);

	/**
	 * 获取学员的所有课程班
	 * 
	 * @param studentId
	 * @return
	 */
	@Query(value = "select b from GjtClassStudent t inner join t.gjtClassInfo b where t.isDeleted = 'N' and b.isDeleted = 'N' and b.classType = 'course' and t.studentId = ?1")
	List<GjtClassInfo> findCourseClassListByStudentId(String studentId);

	GjtClassInfo findByCourseIdAndStudyYearCode(String courseId, int studyYearCode);

	@Query(value = "select class_id classId,termcourse_id termcourseId from gjt_class_info where supervisor_id=?1 and class_type=?2 and is_deleted=?3", nativeQuery = true)
	List<Object[]> findClassIdANDTermcourseId(String supervisorId, String classType, String isDeleted);

	@Query("select c from GjtClassInfo c where c.classType = ?1 and c.gjtSchoolInfo.id = ?2 and c.isDeleted = ?3 and c.gjtBzr is null")
	List<GjtClassInfo> findByClassTypeAndGjtSchoolInfoIdAndIsDeletedAndGjtBzrIsNull(String classType, String xxId,
			String isDeleted);

	@Query("select c from GjtClassInfo c where c.classType = ?1 and c.gjtSchoolInfo.id = ?2 and c.isDeleted = ?3 and c.gjtDuDao is null")
	List<GjtClassInfo> findByClassTypeAndGjtSchoolInfoIdAndIsDeletedAndGjtDuDaoIsNull(String classType, String xxId,
			String isDeleted);

	GjtClassInfo findBySpecialtyIdAndGradeIdAndPyccAndClassTypeAndXxzxIdAndIsDeleted(String newSpecialty,
			String gradeId, String pycc, String classType, String id, String isDeleted);

	GjtClassInfo findByBjmcAndXxzxIdAndIsDeletedAndClassType(String bjmc, String xxzxId, String isDeleted,
			String classType);

	List<GjtClassInfo> findByClassTypeAndGradeIdAndSpecialtyIdAndGjtSchoolInfoIdAndPyccAndIsDeletedOrderByCreatedDtDesc(
			String classType, String termId, String specialtyId, String xxId, String pycc, String booleanNo);

	GjtClassInfo findByGradeIdAndSpecialtyIdAndMemo(String gradeId, String specialtyId, String memo);

}