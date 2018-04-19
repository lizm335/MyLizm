/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.share;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtCourseOwnership;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年12月28日
 * @version 1.0
 *
 */
public interface GjtShareDao
		extends JpaRepository<GjtCourseOwnership, String>, JpaSpecificationExecutor<GjtCourseOwnership> {

	/**
	 * @param orgId
	 * @return
	 */
	List<GjtCourseOwnership> findByOrgCode(String orgCode);
	
	/**
	 * @param orgId
	 * @return
	 */
	List<GjtCourseOwnership> findByOrgId(String orgId);

}
