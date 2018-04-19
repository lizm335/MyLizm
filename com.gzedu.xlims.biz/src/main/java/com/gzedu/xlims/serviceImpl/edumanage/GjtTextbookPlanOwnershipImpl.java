/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.edumanage.GjtTextbookPlanOwnershipDao;
import com.gzedu.xlims.pojo.GjtTextbookPlanOwnership;
import com.gzedu.xlims.service.edumanage.GjtTextbookPlanOwnershipService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年1月18日
 * @version 1.0
 *
 */
@Service
public class GjtTextbookPlanOwnershipImpl implements GjtTextbookPlanOwnershipService {

	@Autowired
	private GjtTextbookPlanOwnershipDao gjtTextbookPlanOwnershipDao;

	@Override
	public List<GjtTextbookPlanOwnership> findBySpecialtyPlanIdAndTextbookType(String specialtyPlanId,
			String textbookType) {
		return gjtTextbookPlanOwnershipDao.findBySpecialtyPlanIdAndTextbookType(specialtyPlanId, textbookType);
	}

	@Override
	public GjtTextbookPlanOwnership findBySpecialtyPlanId(String specialtyPlanId) {
		return gjtTextbookPlanOwnershipDao.findBySpecialtyPlanId(specialtyPlanId);
	}

	@Override
	public void deleteBySpecialtyPlanId(String specialtyPlanId) {
		gjtTextbookPlanOwnershipDao.deleteBySpecialtyPlanId(specialtyPlanId);
	}
}
