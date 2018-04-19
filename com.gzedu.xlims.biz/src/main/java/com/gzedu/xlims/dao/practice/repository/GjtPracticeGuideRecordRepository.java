package com.gzedu.xlims.dao.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.practice.GjtPracticeGuideRecord;

public interface GjtPracticeGuideRecordRepository
		extends JpaRepository<GjtPracticeGuideRecord, String>, JpaSpecificationExecutor<GjtPracticeGuideRecord> {

	public List<GjtPracticeGuideRecord> findByPracticePlanIdAndStudentIdAndIsDeletedOrderByCreatedDtAsc(
			String practicePlanId, String studentId, String isDeleted);

	public List<GjtPracticeGuideRecord> findByPracticePlanIdAndStudentIdAndIsDeletedOrderByCreatedDtDesc(
			String practicePlanId, String studentId, String isDeleted);

	/**
	 * 根据进度码查询学生所发的指导记录
	 * 
	 * @param practicePlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	@Query("SELECT r FROM GjtPracticeGuideRecord r where r.isDeleted = 'N' and r.practicePlanId = ?1 and r.studentId = ?2 and r.progressCode = ?3 and r.isStudent = 1 order by r.createdDt")
	public List<GjtPracticeGuideRecord> findStudentSubmitRecordByCode(String practicePlanId, String studentId,
			String progressCode);

	/**
	 * 根据进度码查询指导老师所发的指导记录
	 * 
	 * @param practicePlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	@Query("SELECT r FROM GjtPracticeGuideRecord r where r.isDeleted = 'N' and r.practicePlanId = ?1 and r.studentId = ?2 and r.progressCode = ?3 and r.isStudent = 0 order by r.createdDt")
	public List<GjtPracticeGuideRecord> findTeacherSubmitRecordByCode(String practicePlanId, String studentId,
			String progressCode);

}
