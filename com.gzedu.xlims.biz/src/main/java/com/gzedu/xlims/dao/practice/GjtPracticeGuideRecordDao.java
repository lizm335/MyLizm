package com.gzedu.xlims.dao.practice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.practice.repository.GjtPracticeGuideRecordRepository;
import com.gzedu.xlims.pojo.practice.GjtPracticeGuideRecord;

@Repository
public class GjtPracticeGuideRecordDao {

	@Autowired
	private GjtPracticeGuideRecordRepository gjtPracticeGuideRecordRepository;

	public Page<GjtPracticeGuideRecord> findAll(Specification<GjtPracticeGuideRecord> spec, PageRequest pageRequst) {
		return gjtPracticeGuideRecordRepository.findAll(spec, pageRequst);
	}

	public GjtPracticeGuideRecord save(GjtPracticeGuideRecord entity) {
		return gjtPracticeGuideRecordRepository.save(entity);
	}

	public GjtPracticeGuideRecord findOne(String id) {
		return gjtPracticeGuideRecordRepository.findOne(id);
	}

	public List<GjtPracticeGuideRecord> findByPracticePlanIdAndStudentIdAndIsDeleted(String practicePlanId,
			String studentId, String isDeleted, String order) {
		if ("Asc".equals(order)) {
			return gjtPracticeGuideRecordRepository.findByPracticePlanIdAndStudentIdAndIsDeletedOrderByCreatedDtAsc(
					practicePlanId, studentId, isDeleted);
		} else {
			return gjtPracticeGuideRecordRepository.findByPracticePlanIdAndStudentIdAndIsDeletedOrderByCreatedDtDesc(
					practicePlanId, studentId, isDeleted);
		}

	}

	/**
	 * 根据进度码查询学生所发的指导记录
	 * 
	 * @param practicePlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	public List<GjtPracticeGuideRecord> findStudentSubmitRecordByCode(String practicePlanId, String studentId,
			String progressCode) {
		return gjtPracticeGuideRecordRepository.findStudentSubmitRecordByCode(practicePlanId, studentId, progressCode);
	}

	/**
	 * 根据进度码查询指导老师所发的指导记录
	 * 
	 * @param practicePlanId
	 * @param studentId
	 * @param progressCode
	 * @return
	 */
	public List<GjtPracticeGuideRecord> findTeacherSubmitRecordByCode(String practicePlanId, String studentId,
			String progressCode) {
		return gjtPracticeGuideRecordRepository.findTeacherSubmitRecordByCode(practicePlanId, studentId, progressCode);
	}

}
