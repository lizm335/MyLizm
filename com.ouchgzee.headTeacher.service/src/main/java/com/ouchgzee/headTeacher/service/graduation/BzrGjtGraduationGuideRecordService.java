package com.ouchgzee.headTeacher.service.graduation;

import java.util.List;

import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationGuideRecord;

/**
 * 毕业指导记录
 * @author eenet09
 *
 */
@Deprecated public interface BzrGjtGraduationGuideRecordService {
	
	/**
	 * 查询学生的指导记录
	 * @param batchId
	 * @param studentId
	 * @param recordType
	 * @return
	 */
	public List<BzrGjtGraduationGuideRecord> queryListByStudent(String batchId, String studentId, int recordType);

	/**
	 * 根据进度码只查询学生所发的指导记录
	 * @param batchId
	 * @param studentId
	 * @param recordType
	 * @param progressCode
	 * @return
	 */
	public List<BzrGjtGraduationGuideRecord> queryListByStudentAndCode(String batchId, String studentId, int recordType, String progressCode);
	
	public void insert(BzrGjtGraduationGuideRecord entity);

}
