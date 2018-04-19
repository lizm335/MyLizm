/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.article;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtArticle;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月8日
 * @version 1.0
 *
 */

public interface GjtArticleService extends BaseService<GjtArticle> {

	Page<GjtArticle> queryPageList(Map<String, Object> searchParams,
			PageRequest pageRequst);

	List<GjtArticle> queryGjtArticle(Map<String, Object> searchParams);

	/**
	 * @param gjtArticle
	 * @return
	 */
	Boolean saveArticle(GjtArticle gjtArticle);

	/**
	 * @param gjtArticle
	 * @return
	 */
	Boolean updateArticle(GjtArticle gjtArticle);

	/**
	 * @param id
	 * @return
	 */
	GjtArticle queryById(String id);
	
}
