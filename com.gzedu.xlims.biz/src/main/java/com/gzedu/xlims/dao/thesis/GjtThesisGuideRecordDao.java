package com.gzedu.xlims.dao.thesis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.thesis.repository.GjtThesisGuideRecordRepository;
import com.gzedu.xlims.pojo.thesis.GjtThesisGuideRecord;

@Repository
public class GjtThesisGuideRecordDao {
	
	@Autowired
	private GjtThesisGuideRecordRepository gjtThesisGuideRecordRepository;
	
	public Page<GjtThesisGuideRecord> findAll(Specification<GjtThesisGuideRecord> spec, PageRequest pageRequst) {
		return gjtThesisGuideRecordRepository.findAll(spec, pageRequst);
	}
	
	public GjtThesisGuideRecord save(GjtThesisGuideRecord entity) {
		return gjtThesisGuideRecordRepository.save(entity);
	}
	
	public GjtThesisGuideRecord findOne(String id) {
		return gjtThesisGuideRecordRepository.findOne(id);
	}
	
	public List<GjtThesisGuideRecord> findByThesisPlanIdAndStudentIdAndIsDeleted(String thesisPlanId, String studentId, String isDeleted) {
		return gjtThesisGuideRecordRepository.findByThesisPlanIdAndStudentIdAndIsDeletedOrderByCreatedDtDesc(thesisPlanId, studentId, isDeleted);
	}
	
	/**
	 * 根据进度码查询学生所发的指导记录
	 * @param thesisPlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	public List<GjtThesisGuideRecord> findStudentSubmitRecordByCode(String thesisPlanId, String studentId, String progressCode) {
		return gjtThesisGuideRecordRepository.findStudentSubmitRecordByCode(thesisPlanId, studentId, progressCode);
	}

	/**
	 * 根据进度码查询指导老师所发的指导记录
	 * @param thesisPlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	public List<GjtThesisGuideRecord> findTeacherSubmitRecordByCode(String thesisPlanId, String studentId, String progressCode) {
		return gjtThesisGuideRecordRepository.findTeacherSubmitRecordByCode(thesisPlanId, studentId, progressCode);
	}

}
