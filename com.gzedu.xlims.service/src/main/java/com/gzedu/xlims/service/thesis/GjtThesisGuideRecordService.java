package com.gzedu.xlims.service.thesis;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.thesis.GjtThesisGuideRecord;

public interface GjtThesisGuideRecordService {
	
	public Page<GjtThesisGuideRecord> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtThesisGuideRecord insert(GjtThesisGuideRecord entity);;
	
	public void update(GjtThesisGuideRecord entity);

	public GjtThesisGuideRecord findOne(String id);
	
	public List<GjtThesisGuideRecord> findByThesisPlanIdAndStudentId(String thesisPlanId, String studentId);
	
	/**
	 * 根据进度码查询学生所发的指导记录
	 * @param thesisPlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	public List<GjtThesisGuideRecord> findStudentSubmitRecordByCode(String thesisPlanId, String studentId, String progressCode);

	/**
	 * 根据进度码查询指导老师所发的指导记录
	 * @param thesisPlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	public List<GjtThesisGuideRecord> findTeacherSubmitRecordByCode(String thesisPlanId, String studentId, String progressCode);

}
