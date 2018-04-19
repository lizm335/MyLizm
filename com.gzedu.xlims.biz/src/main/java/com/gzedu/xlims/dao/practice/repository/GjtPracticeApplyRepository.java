package com.gzedu.xlims.dao.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.practice.GjtPracticeApply;

public interface GjtPracticeApplyRepository
		extends JpaRepository<GjtPracticeApply, String>, JpaSpecificationExecutor<GjtPracticeApply> {

	public GjtPracticeApply findByPracticePlanIdAndStudentIdAndIsDeleted(String practicePlanId, String studentId,
			String isDeleted);

	@Query("select a from GjtPracticeApply a where a.isDeleted = 'N' and a.practicePlanId = ?1 and a.gjtStudentInfo.gjtSpecialty.specialtyBaseId = ?2")
	public List<GjtPracticeApply> findByPracticePlanIdAndSpecialtyBaseId(String practicePlanId, String specialtyBaseId);

	public GjtPracticeApply findByIsDeletedAndStudentIdAndStatus(String isDeleted, String studentId, int status);

	@Query("select a from GjtPracticeApply a where a.isDeleted = 'N' and a.studentId= ?1 and a.gjtPracticePlan.gradeId=?2")
	public List<GjtPracticeApply> findPracticePlanIdByStudentId(String studentId, String gradeId);

}
