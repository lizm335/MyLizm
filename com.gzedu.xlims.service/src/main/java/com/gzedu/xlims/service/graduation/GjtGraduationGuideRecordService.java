package com.gzedu.xlims.service.graduation;

import java.util.List;

import com.gzedu.xlims.pojo.graduation.GjtGraduationGuideRecord;

/**
 * 毕业指导记录
 * @author eenet09
 *
 */
public interface GjtGraduationGuideRecordService {
	
	/**
	 * 查询学生的指导记录
	 * @param batchId
	 * @param studentId
	 * @param recordType
	 * @return
	 */
	public List<GjtGraduationGuideRecord> queryListByStudent(String batchId, String studentId, int recordType);

	/**
	 * 根据进度码只查询学生所发的指导记录
	 * @param batchId
	 * @param studentId
	 * @param recordType
	 * @param progressCode
	 * @return
	 */
	public List<GjtGraduationGuideRecord> queryListByStudentAndCode(String batchId, String studentId, int recordType, String progressCode);
	
	public void insert(GjtGraduationGuideRecord entity);

}
