package com.gzedu.xlims.dao.textbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOperaBatch;

public interface GjtTextbookStockOperaBatchRepository extends JpaRepository<GjtTextbookStockOperaBatch, String>, JpaSpecificationExecutor<GjtTextbookStockOperaBatch> {

}
