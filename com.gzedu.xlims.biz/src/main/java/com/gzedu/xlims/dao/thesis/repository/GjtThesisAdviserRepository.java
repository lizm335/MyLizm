package com.gzedu.xlims.dao.thesis.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.thesis.GjtThesisAdviser;

public interface GjtThesisAdviserRepository extends JpaRepository<GjtThesisAdviser, String>, JpaSpecificationExecutor<GjtThesisAdviser> {

	@Modifying
	@Transactional
	@Query("delete from GjtThesisAdviser a where a.arrangeId = ?1 and a.adviserType in ?2")
	public void deleteByArrangeIdAndAdviserTypes(String arrangeId, Set<Integer> dviserTypes);
	
}
