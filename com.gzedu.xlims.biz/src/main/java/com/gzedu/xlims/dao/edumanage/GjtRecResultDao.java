/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.edumanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtRecResult;

/**
 * 
 * 功能说明：成绩记录
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月12日
 * @version 2.5
 *
 */
public interface GjtRecResultDao extends BaseDao<GjtRecResult, String> {

	@Query("SELECT r FROM GjtRecResult r where r.isDeleted='N' and r.gjtStudentInfo.studentId = ?1 and r.gjtCourse.kcmc = ?2")
	GjtRecResult queryByStudentIdAndCourseName(String studentId, String courseName);

	GjtRecResult findByStudentIdAndTeachPlanIdAndIsDeleted(String studentId, String teachPlanId, String isDeleted);

	GjtRecResult findByTeachPlanIdAndStudentIdAndIsDeleted(String teachPlanId, String studentId, String isDeleted);

	@Modifying
	@Transactional
	@Query("update GjtRecResult grr set grr.bespeakState=?1 ,grr.updatedDt =sysdate where grr.studentId=?2 and grr.courseId=?3 ")
	int upBespeakState(String bespeakState, String studentId, String courseId);

	GjtRecResult findByCourseIdAndStudentIdAndIsDeleted(String courseId, String studentId, String isDeleted);

	@Modifying
	@Transactional
	@Query("update GjtRecResult grr set grr.bespeakState=?1 ,grr.payState=1,grr.updatedDt =sysdate where grr.studentId=?2 and grr.courseId=?3 ")
	int upBespeakStateAndPayState(String bespeakState, String studentId, String courseId);

	@Query("SELECT r FROM GjtRecResult r where r.isDeleted='N' and r.gjtTeachPlan.isDeleted='N' and r.gjtTeachPlan.grantData=0 and r.gjtTeachPlan.actualGradeId = ?1 and r.gjtTeachPlan.courseId in (?2)")
	List<GjtRecResult> findByGradeAndCourses(String gradeId, List<String> courseIds);

	@Query(value = "select r.* from gjt_rec_result r inner join gjt_student_info"
			+ " s on s.student_id=r.student_id where r.is_Deleted='N' "
			+ " and r.teach_plan_id in (?1) and s.nj in(?2)  and s.is_deleted='N' ", nativeQuery = true)
	List<GjtRecResult> findByTeachPlanIds(List<String> teachPlanIds, List<String> textbookGradeIds);

	GjtRecResult findByStudentIdAndTeachPlanIdAndCourseIdAndIsDeleted(String studentId, String teachPlanId,
			String courseId, String isDeleted);

	/**
	 * 更新同步状态
	 * 
	 * @param syncStatus
	 * @param recId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtRecResult g set g.syncStatus=?1 where g.recId=?2 ")
	int updateSyncStatus(String syncStatus, String recId);

	@Modifying
	@Transactional
	@Query("update GjtRecResult g set g.isDeleted=?3,g.memo=?2 where g.studentId=?1 ")
	int deleteByStudentId(String studentId, String memo, String isDeleted);

	/**
	 * 补考费-更改为已缴费
	 * 
	 * @param recId
	 * @param paySn
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtRecResult g set g.payState='1',g.orderMark=?2,g.updatedDt=SYSDATE where g.recId=?1")
	int updatePayState(String recId, String paySn);

	/**
	 * 更改为重修状态
	 * 
	 * @param recId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtRecResult g set g.rebuildState='1' where g.recId=?1")
	int updateRebuildState(String recId);
}
