package com.gzedu.xlims.dao.thesis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.thesis.GjtThesisApply;

public interface GjtThesisApplyRepository
		extends JpaRepository<GjtThesisApply, String>, JpaSpecificationExecutor<GjtThesisApply> {

	public GjtThesisApply findByThesisPlanIdAndStudentIdAndIsDeleted(String thesisPlanId, String studentId,
			String isDeleted);

	@Query("select a from GjtThesisApply a where a.isDeleted = 'N' and a.thesisPlanId = ?1 and a.gjtStudentInfo.gjtSpecialty.specialtyBaseId = ?2")
	public List<GjtThesisApply> findByThesisPlanIdAndSpecialtyBaseId(String thesisPlanId, String specialtyBaseId);

	public GjtThesisApply findByIsDeletedAndStudentIdAndStatus(String isDeleted, String studentId, int status);

	@Query("select a from GjtThesisApply a where a.isDeleted = 'N' and a.studentId = ?1 and a.gjtThesisPlan.gradeId=?2 ")
	public List<GjtThesisApply> findIsApplyByStudentId(String studentId, String gradeId);

}
