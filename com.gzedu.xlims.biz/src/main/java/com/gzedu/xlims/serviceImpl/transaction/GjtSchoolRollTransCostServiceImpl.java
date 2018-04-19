package com.gzedu.xlims.serviceImpl.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.transaction.GjtSchoolRollTransCostDao;
import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTransCostService;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年12月19日
 * @version 2.5
 */
@Service
public class GjtSchoolRollTransCostServiceImpl implements GjtSchoolRollTransCostService{
	
	@Autowired
	private GjtSchoolRollTransCostDao gjtSchoolRollTransCostDao;
	/**
	 * 添加学员费用信息
	 */
	@Override
	public Boolean saveEntity(GjtSchoolRollTransCost transCost) {
		GjtSchoolRollTransCost save =gjtSchoolRollTransCostDao.save(transCost);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public GjtSchoolRollTransCost findByTransactionId(String transactionId) {
		return gjtSchoolRollTransCostDao.findByTransactionId(transactionId);
	}

}
