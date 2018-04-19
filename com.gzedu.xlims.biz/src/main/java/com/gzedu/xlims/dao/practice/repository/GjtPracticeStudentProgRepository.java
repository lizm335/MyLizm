package com.gzedu.xlims.dao.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;

public interface GjtPracticeStudentProgRepository extends JpaRepository<GjtPracticeStudentProg, String>, JpaSpecificationExecutor<GjtPracticeStudentProg> {
	
	@Modifying
	@Transactional
	@Query("delete from GjtPracticeStudentProg p where p.practicePlanId = ?1 and p.studentId = ?2")
	public void deleteByPracticePlanIdAndStudentId(String practicePlanId, String studentId);
	
	public List<GjtPracticeStudentProg> findByPracticePlanIdAndStudentIdOrderByCreatedDtAsc(String practicePlanId, String studentId);

}
