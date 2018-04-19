/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.share;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtCourseOwnership;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年12月28日
 * @version 1.0
 *
 */
public interface GjtShareService {
	public Page<GjtCourseOwnership> queryPageList(Map<String, Object> searchParams, PageRequest pageRequst);

	Boolean saveGjtShare(GjtCourseOwnership gjtCourseOwnership);

	Boolean updateGjtShare(GjtCourseOwnership gjtCourseOwnership);

	GjtCourseOwnership queryById(String id);

	public void delete(String id);

	public List<GjtCourseOwnership> findByOrgCode(String orgCode);
	
	public List<GjtCourseOwnership> findByOrgId(String orgId);
}
