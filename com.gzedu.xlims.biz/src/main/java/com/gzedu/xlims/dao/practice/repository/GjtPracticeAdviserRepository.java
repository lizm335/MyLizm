package com.gzedu.xlims.dao.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.practice.GjtPracticeAdviser;

public interface GjtPracticeAdviserRepository extends JpaRepository<GjtPracticeAdviser, String>, JpaSpecificationExecutor<GjtPracticeAdviser> {
	
	@Modifying
	@Transactional
	@Query("delete from GjtPracticeAdviser a where a.arrangeId = ?1")
	public void deleteByArrangeId(String arrangeId);

}
