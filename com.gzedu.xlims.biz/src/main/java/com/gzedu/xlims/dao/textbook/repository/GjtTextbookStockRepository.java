package com.gzedu.xlims.dao.textbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;

public interface GjtTextbookStockRepository extends JpaRepository<GjtTextbookStock, String>, JpaSpecificationExecutor<GjtTextbookStock> {
	
	@Query("select s from GjtTextbookStock s where s.gjtTextbook.orgId = ?1 and s.planOutStockNum > s.stockNum")
	List<GjtTextbookStock> findNotEnoughStock(String orgId);

}
