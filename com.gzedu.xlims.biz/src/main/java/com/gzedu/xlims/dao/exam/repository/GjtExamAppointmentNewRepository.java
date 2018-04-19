package com.gzedu.xlims.dao.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;

public interface GjtExamAppointmentNewRepository extends BaseDao<GjtExamAppointmentNew, String> {

	GjtExamAppointmentNew findByStudentIdAndExamPlanIdAndIsDeleted(String studentId, String examPlanId, int isDeleted);	
	
	@Query("select a from GjtExamAppointmentNew a where a.isDeleted = 0 and a.examPlanNew.examBatchCode = ?1 and a.status = ?2")
	List<GjtExamAppointmentNew> findByExamBatchCodeAndStatus(String examBatchCode, int status);

	/**
	 * 查看学员考试预约
	 * @param examBatchCode
	 * @param studentId
	 * @param recId
	 * @return
	 */
	@Query("select t from GjtExamAppointmentNew t where t.isDeleted=0 and t.examBatchCode=?1 and t.studentId=?2 and t.recId=?3")
	GjtExamAppointmentNew findStudentExamAppointment(String examBatchCode, String studentId, String recId);

	/**
	 * 取消考试预约
	 * @param examBatchCode
	 * @param studentId
	 * @param recId
     * @return
     */
	@Modifying
	@Transactional
	@Query("update GjtExamAppointmentNew t set t.isDeleted=1 where t.isDeleted=0 and t.examBatchCode=?1 and t.studentId=?2 and t.recId=?3")
	int deleteExamAppointment(String examBatchCode, String studentId, String recId);

}
