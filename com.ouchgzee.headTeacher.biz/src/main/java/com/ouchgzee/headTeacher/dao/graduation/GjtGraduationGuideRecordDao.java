package com.ouchgzee.headTeacher.dao.graduation;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationGuideRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 毕业指导记录
 * @author eenet09
 *
 */
@Deprecated @Repository("bzrGjtGraduationGuideRecordDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtGraduationGuideRecordDao extends BaseDao<BzrGjtGraduationGuideRecord, String> {

	/**
	 * 查询学生的指导记录
	 * @param batchId
	 * @param studentId
	 * @param recordType
	 * @return
	 */
	@Query("SELECT r FROM BzrGjtGraduationGuideRecord r where r.isDeleted = 'N' and r.gjtGraduationBatch.batchId = ?1 and r.gjtStudentInfo.studentId = ?2 and r.recordType = ?3 order by r.createdDt")
	public List<BzrGjtGraduationGuideRecord> queryListByStudent(String batchId, String studentId, int recordType);
	
	/**
	 * 根据进度码只查询学生所发的指导记录
	 * @param batchId
	 * @param studentId
	 * @param recordType
	 * @param progressCode
	 * @return
	 */
	@Query("SELECT r FROM BzrGjtGraduationGuideRecord r where r.isDeleted = 'N' and r.gjtGraduationBatch.batchId = ?1 and r.gjtStudentInfo.studentId = ?2 and r.recordType = ?3 and r.progressCode = ?4 and r.isStudent = 1 order by r.createdDt")
	public List<BzrGjtGraduationGuideRecord> queryListByStudentAndCode(String batchId, String studentId, int recordType, String progressCode);
	
}
