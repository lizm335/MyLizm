package com.gzedu.xlims.dao.textbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;

public interface GjtTextbookDistributeDetailRepository extends JpaRepository<GjtTextbookDistributeDetail, String>, JpaSpecificationExecutor<GjtTextbookDistributeDetail> {

}
