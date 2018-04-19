package com.gzedu.xlims.service.transaction;

import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年12月19日
 * @version 2.5
 */
public interface GjtSchoolRollTransCostService {

	Boolean saveEntity(GjtSchoolRollTransCost transCost);

	GjtSchoolRollTransCost findByTransactionId(String transactionId);
	
}
