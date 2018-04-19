/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.pageConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtPageDef;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月12日
 * @version 1.0
 *
 */
public interface GjtPageConfigDao extends JpaRepository<GjtPageDef, String>, JpaSpecificationExecutor<GjtPageDef> {
}
