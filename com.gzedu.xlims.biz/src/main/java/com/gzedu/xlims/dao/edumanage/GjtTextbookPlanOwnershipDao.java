/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.edumanage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtTextbookPlanOwnership;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年1月17日
 * @version 1.0
 *
 */
public interface GjtTextbookPlanOwnershipDao
		extends JpaRepository<GjtTextbookPlanOwnership, String>, JpaSpecificationExecutor<GjtTextbookPlanOwnership> {

	public List<GjtTextbookPlanOwnership> findBySpecialtyPlanIdAndTextbookType(String specialtyPlanId,
			String textbookType);

	public GjtTextbookPlanOwnership findBySpecialtyPlanId(String specialtyPlanId);

	@Modifying
	@Transactional
	@Query("delete GjtTextbookPlanOwnership g where g.specialtyPlanId=?1 ")
	public int deleteBySpecialtyPlanId(String specialtyPlanId);
}
