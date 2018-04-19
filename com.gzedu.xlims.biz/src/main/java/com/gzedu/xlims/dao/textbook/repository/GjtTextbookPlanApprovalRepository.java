package com.gzedu.xlims.dao.textbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.textbook.GjtTextbookPlanApproval;

public interface GjtTextbookPlanApprovalRepository extends JpaRepository<GjtTextbookPlanApproval, String>, JpaSpecificationExecutor<GjtTextbookPlanApproval> {
	
	public List<GjtTextbookPlanApproval> findByPlanId(String planId);

}
