package com.gzedu.xlims.dao.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年12月19日
 * @version 2.5
 */
public interface GjtSchoolRollTransCostDao extends JpaRepository<GjtSchoolRollTransCost, String>, JpaSpecificationExecutor<GjtSchoolRollTransCost>{

	GjtSchoolRollTransCost findByTransactionId(String transactionId);

}
