/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.share;

import java.util.List;

import com.gzedu.xlims.pojo.GjtSpecialtyOwnership;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年12月29日
 * @version 1.0
 *
 */
public interface GjtSpecialtyShareService {
	Boolean saveGjtShare(GjtSpecialtyOwnership gjtSpecialtyOwnership);

	Boolean updateGjtShare(GjtSpecialtyOwnership gjtSpecialtyOwnership);

	GjtSpecialtyOwnership queryById(String id);

	public void delete(String id);

	public List<GjtSpecialtyOwnership> findByOrgCode(String orgCode);
}
