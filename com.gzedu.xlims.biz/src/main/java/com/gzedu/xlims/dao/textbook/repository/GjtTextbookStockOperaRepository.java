package com.gzedu.xlims.dao.textbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOpera;

public interface GjtTextbookStockOperaRepository extends JpaRepository<GjtTextbookStockOpera, String>, JpaSpecificationExecutor<GjtTextbookStockOpera> {

}
