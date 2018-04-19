package com.gzedu.xlims.dao.textbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.textbook.GjtTextbookOrder;

public interface GjtTextbookOrderRepository extends JpaRepository<GjtTextbookOrder, String>, JpaSpecificationExecutor<GjtTextbookOrder> {
	
	public GjtTextbookOrder findByPlanIdAndStatus(String planId, int status);
	
	@Query("select o from GjtTextbookOrder o where o.status = ?1 and o.gjtTextbookPlan.orgId = ?2")
	public List<GjtTextbookOrder> findByStatusAndOrgId(int status, String orgId);

}
