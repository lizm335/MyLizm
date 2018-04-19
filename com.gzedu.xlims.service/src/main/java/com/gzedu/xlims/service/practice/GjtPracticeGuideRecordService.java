package com.gzedu.xlims.service.practice;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.practice.GjtPracticeGuideRecord;

public interface GjtPracticeGuideRecordService {

	public Page<GjtPracticeGuideRecord> findAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public GjtPracticeGuideRecord insert(GjtPracticeGuideRecord entity);

	public void update(GjtPracticeGuideRecord entity);

	public GjtPracticeGuideRecord findOne(String id);

	public List<GjtPracticeGuideRecord> findByPracticePlanIdAndStudentId(String practicePlanId, String studentId,
			String order);

	/**
	 * 根据进度码查询学生所发的指导记录
	 * 
	 * @param practicePlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
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
	public List<GjtPracticeGuideRecord> findTeacherSubmitRecordByCode(String practicePlanId, String studentId,
			String progressCode);

}
