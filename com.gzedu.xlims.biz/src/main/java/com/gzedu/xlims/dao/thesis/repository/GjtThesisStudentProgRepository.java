package com.gzedu.xlims.dao.thesis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;

public interface GjtThesisStudentProgRepository extends JpaRepository<GjtThesisStudentProg, String>, JpaSpecificationExecutor<GjtThesisStudentProg> {
	
	@Modifying
	@Transactional
	@Query("delete from GjtThesisStudentProg p where p.thesisPlanId = ?1 and p.studentId = ?2")
	public void deleteByThesisPlanIdAndStudentId(String thesisPlanId, String studentId);
	
	public List<GjtThesisStudentProg> findByThesisPlanIdAndStudentIdOrderByCreatedDtAsc(String thesisPlanId, String studentId);

}
