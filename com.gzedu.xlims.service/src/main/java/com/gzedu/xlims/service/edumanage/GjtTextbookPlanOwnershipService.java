/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.edumanage;

import java.util.List;

import com.gzedu.xlims.pojo.GjtTextbookPlanOwnership;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年1月18日
 * @version 1.0
 *
 */
public interface GjtTextbookPlanOwnershipService {
	public List<GjtTextbookPlanOwnership> findBySpecialtyPlanIdAndTextbookType(String specialtyPlanId,
			String textbookType);

	public GjtTextbookPlanOwnership findBySpecialtyPlanId(String specialtyPlanId);

	public void deleteBySpecialtyPlanId(String specialtyPlanId);
}
