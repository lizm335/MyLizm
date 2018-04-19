/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.study;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtTeachPlan;
import org.springframework.stereotype.Repository;

/**
 * 教学计划信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtTeachPlanDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtTeachPlanDao extends BaseDao<BzrGjtTeachPlan, String> {

	/**
	 * 根据教职工ID获取教学计划ID，班级ID
	 * 
	 * @param employeeId
	 * @return
	 */
	@Query(value = "SELECT t.teach_plan_id, c.class_id FROM VIEW_TEACH_PLAN t, gjt_class_info c WHERE t.course_id=c.course_id AND t.kkzy= c.specialty_id AND t.term_id=c.term_id AND t.is_deleted='N' AND c.is_deleted='N' AND c.class_type='course' AND c.supervisor_id=:employeeId", nativeQuery = true)
	List<Object[]> findTachPlanClassByEmpId(@Param("employeeId") String employeeId);

	List<BzrGjtTeachPlan> findByGradeIdAndKkzyAndIsDeletedOrderByCreatedDtAsc(String gradeId, String specialtyId,
																			  String isDeleted);
}
