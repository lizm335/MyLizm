package com.gzedu.xlims.dao.exam.repository;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GjtExamPointAppointmentNewRepository extends BaseDao<GjtExamPointAppointmentNew, String> {

	/**
	 * 按照考试方式查询学员所约考点记录
	 * @param examBatchCode
	 * @param studentId
	 * @param examType
	 * @param isDeleted
     * @return
     */
	GjtExamPointAppointmentNew findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamTypeAndIsDeleted(String examBatchCode, String studentId, String examType, int isDeleted);


	/**
	 * 取消考点预约
	 * @param examBatchCode
	 * @param studentId
	 * @param examType 考试方式 8:笔试;11:机考
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtExamPointAppointmentNew t set t.isDeleted=1 where t.isDeleted=0 and t.examBatchCode=?1 and t.studentId=?2 and t.examPointId in (select x from GjtExamPointNew x where x.isDeleted='N' and x.examBatchCode=?1 and x.examType=?3)")
	int deletePointExamAppointment(String examBatchCode, String studentId, String examType);

}
