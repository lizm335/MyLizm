package com.gzedu.xlims.dao.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.thesis.GjtThesisArrange;

public interface GjtThesisArrangeRepository extends JpaRepository<GjtThesisArrange, String>, JpaSpecificationExecutor<GjtThesisArrange> {
	
	public GjtThesisArrange findByThesisPlanIdAndSpecialtyBaseIdAndIsDeleted(String thesisPlanId, String specialtyBaseId, String isDeleted);

}
