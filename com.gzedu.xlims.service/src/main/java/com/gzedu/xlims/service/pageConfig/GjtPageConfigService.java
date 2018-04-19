/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.pageConfig;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtPageDef;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月12日
 * @version 1.0
 *
 */
public interface GjtPageConfigService {
	public Page<GjtPageDef> queryPageList(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * @param gjtPageDef
	 * @return
	 */
	Boolean savePageConfig(GjtPageDef gjtPageDef);

	/**
	 * @param gjtPageDef
	 * @return
	 */
	Boolean updatePageConfig(GjtPageDef gjtPageDef);

	/**
	 * @param id
	 * @return
	 */
	GjtPageDef queryById(String id);
}
