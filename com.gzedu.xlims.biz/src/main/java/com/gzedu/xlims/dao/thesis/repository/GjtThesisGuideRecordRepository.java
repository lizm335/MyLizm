package com.gzedu.xlims.dao.thesis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.thesis.GjtThesisGuideRecord;

public interface GjtThesisGuideRecordRepository extends JpaRepository<GjtThesisGuideRecord, String>, JpaSpecificationExecutor<GjtThesisGuideRecord> {
	
	public List<GjtThesisGuideRecord> findByThesisPlanIdAndStudentIdAndIsDeletedOrderByCreatedDtDesc(String thesisPlanId, String studentId, String isDeleted);

	/**
	 * 根据进度码查询学生所发的指导记录
	 * @param thesisPlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	@Query("SELECT r FROM GjtThesisGuideRecord r where r.isDeleted = 'N' and r.thesisPlanId = ?1 and r.studentId = ?2 and r.progressCode = ?3 and r.isStudent = 1 order by r.createdDt")
	public List<GjtThesisGuideRecord> findStudentSubmitRecordByCode(String thesisPlanId, String studentId, String progressCode);

	/**
	 * 根据进度码查询指导老师所发的指导记录
	 * @param thesisPlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	@Query("SELECT r FROM GjtThesisGuideRecord r where r.isDeleted = 'N' and r.thesisPlanId = ?1 and r.studentId = ?2 and r.progressCode = ?3 and r.isStudent = 0 order by r.createdDt")
	public List<GjtThesisGuideRecord> findTeacherSubmitRecordByCode(String thesisPlanId, String studentId, String progressCode);

}
