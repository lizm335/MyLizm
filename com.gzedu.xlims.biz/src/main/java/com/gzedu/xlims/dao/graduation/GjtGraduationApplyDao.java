package com.gzedu.xlims.dao.graduation;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface GjtGraduationApplyDao extends BaseDao<GjtGraduationApply, String> {
	
	@Query("SELECT a FROM GjtGraduationApply a where a.isDeleted='N' and a.gjtGraduationBatch.batchId = ?1 and a.gjtStudentInfo.gjtSpecialty.specialtyId = ?2")
	public List<GjtGraduationApply> queryListBySpecialty(String batchId, String specialtyId);
	
	@Query("SELECT a FROM GjtGraduationApply a where a.isDeleted='N' and a.gjtGraduationBatch.batchId = ?1 and a.gjtStudentInfo.studentId = ?2 and a.applyType = ?3")
	public GjtGraduationApply queryOneByStudent(String batchId, String studentId, int applyType);

	@Query("SELECT a FROM GjtGraduationApply a where a.isDeleted='N' and a.gjtGraduationBatch.batchId = ?1 and a.gjtStudentInfo.gjtSpecialty.specialtyId = ?2 and a.applyType = ?3 and a.needDefence = ?4 and a.status in (?5)")
	public List<GjtGraduationApply> queryList(String batchId, String specialtyId, int applyType, int defenceType, Set<Integer> status);
	
	public List<GjtGraduationApply> findByGjtGraduationBatchBatchIdAndApplyTypeAndIsDeletedAndGuideTeacherIsNull(String batchId, int applyType, String isDeleted);

	public List<GjtGraduationApply> findByGjtGraduationBatchBatchIdAndApplyTypeAndIsDeletedAndDefenceTeacherIsNull(String batchId, int applyType, String isDeleted);
	
	public GjtGraduationApply findByIsDeletedAndGjtStudentInfoStudentIdAndApplyTypeAndStatusGreaterThanEqual(String isDeleted, String studentId, int applyType, int status);

}
