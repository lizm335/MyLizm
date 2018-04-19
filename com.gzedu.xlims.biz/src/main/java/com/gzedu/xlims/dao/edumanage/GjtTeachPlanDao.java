/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.edumanage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtTeachPlan;

/**
 * 
 * 功能说明：年级管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月13日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtTeachPlanDao extends JpaRepository<GjtTeachPlan, String>, JpaSpecificationExecutor<GjtTeachPlan> {
	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtTeachPlan g set g.isDeleted=?2 where g.teachPlanId=?1 ")
	int deleteById(String id, String str);

	/**
	 * 是否启用1启用，0停用
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtTeachPlan g set g.isEnabled=?2 where g.teachPlanId=?1 ")
	int updateIsEnabled(String id, String str);

	GjtTeachPlan findByGradeIdAndKkzyAndIsDeletedAndCourseCode(String gradeId, String kkzy, String isDeleted,
			String courseCode);

	GjtTeachPlan findByGradeIdAndKkzyAndIsDeletedAndCourseId(String gradeId, String kkzy, String isDeleted,
			String courseId);

	List<GjtTeachPlan> findByGradeIdAndKkzyAndIsDeletedOrderByCreatedDtAsc(String gradeId, String specialtyId,
			String isDeleted);

	@Query("from GjtTeachPlan where gradeId=?1 and kkzy=?2 and isDeleted='N' order by kkxq,createdDt")
	List<GjtTeachPlan> findByGradeIdAndKkzy(String gradeId, String specialtyId);

	@Query(value = "select t.* from Gjt_Teach_Plan t left join gjt_Course b on b.course_id=t.course_id and b.is_Deleted='N' where t.is_Deleted='N' and t.grade_Specialty_Id=?1 order by t.kkxq,t.created_Dt", nativeQuery = true)
	List<GjtTeachPlan> findByGradeSpecialtyId(String gradeSpecialtyId);

	@Query("select teachPlanId from GjtTeachPlan where gradeSpecialtyId=?1 and isDeleted='N'")
	List<String> findTeachPlanIdByGradeSpecialtyId(String gradeSpecialtyId);

	@Query("from GjtTeachPlan where gradeSpecialtyId=?1 and isDeleted='N'")
	List<GjtTeachPlan> findAllByGradeSpecialtyId(String gradeSpecialtyId);

	List<GjtTeachPlan> findByActualGradeIdAndCourseIdAndIsDeleted(String actualGradeId, String courseId,
			String isDeleted);

	/**
	 * 查询某学期的课程教学计划
	 * 
	 * @param specialtyId
	 * @param courseId
	 * @param actualTermId
	 * @param xxId
	 * @param isDeleted
	 * @return
	 */
	List<GjtTeachPlan> findByKkzyAndCourseIdAndActualGradeIdAndXxIdAndIsDeletedOrderByCreatedDtDesc(String specialtyId,
			String courseId, String actualTermId, String xxId, String isDeleted);

	List<GjtTeachPlan> findByGradeSpecialtyIdAndKkxq(String gradeSpecialtyId, int kkxq);




}