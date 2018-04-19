/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.share;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtSpecialtyOwnership;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年12月29日
 * @version 1.0
 *
 */
public interface GjtSpecialtyShareDao
		extends JpaRepository<GjtSpecialtyOwnership, String>, JpaSpecificationExecutor<GjtSpecialtyOwnership> {

	/**
	 * @param orgId
	 * @return
	 */
	List<GjtSpecialtyOwnership> findByOrgCode(String orgCode);

}
