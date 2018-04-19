package com.gzedu.xlims.dao.graduation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.graduation.GjtGraduationGuideRecord;

/**
 * 毕业指导记录
 * @author eenet09
 *
 */
public interface GjtGraduationGuideRecordDao extends JpaRepository<GjtGraduationGuideRecord, String>, JpaSpecificationExecutor<GjtGraduationGuideRecord> {

	/**
	 * 查询学生的指导记录
	 * @param batchId
	 * @param studentId
	 * @param recordType
	 * @return
	 */
	@Query("SELECT r FROM GjtGraduationGuideRecord r where r.isDeleted = 'N' and r.gjtGraduationBatch.batchId = ?1 and r.gjtStudentInfo.studentId = ?2 and r.recordType = ?3 order by r.createdDt")
	public List<GjtGraduationGuideRecord> queryListByStudent(String batchId, String studentId, int recordType);
	
	/**
	 * 根据进度码只查询学生所发的指导记录
	 * @param batchId
	 * @param studentId
	 * @param recordType
	 * @param progressCode
	 * @return
	 */
	@Query("SELECT r FROM GjtGraduationGuideRecord r where r.isDeleted = 'N' and r.gjtGraduationBatch.batchId = ?1 and r.gjtStudentInfo.studentId = ?2 and r.recordType = ?3 and r.progressCode = ?4 and r.isStudent = 1 order by r.createdDt")
	public List<GjtGraduationGuideRecord> queryListByStudentAndCode(String batchId, String studentId, int recordType, String progressCode);
	
}
