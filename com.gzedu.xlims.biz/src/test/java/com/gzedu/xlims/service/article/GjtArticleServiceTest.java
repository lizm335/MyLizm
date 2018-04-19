/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.article;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.pojo.GjtArticle;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月8日
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtArticleServiceTest {
	@Autowired
	private GjtArticleService gjtArticleService;
	@Autowired
	private GjtSpecialtyService gjtSpecialtyService;
	@Autowired
	private CommonMapService commonMapService;

	// 根据条件查询数据源
	@Test
	public void queryPyccDates() {
		Map<String, String> infoMap = commonMapService.getPyccMap();
		System.out.println(infoMap.toString());
	}

	public void queryGradeDates() {
	}

	public void querySpecialtyMap() {
	}

	public void querySpecialtyName() {

		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_specialtyId", "9b54718072e74cbdb0f2d7add892df40");
		Page<GjtSpecialty> list = gjtSpecialtyService.queryPage(searchParams, pageRequst);
		for (GjtSpecialty gjtSpecialty : list) {
			System.out.println("querySpecialtyName start....");
			System.out.println("querySpecialtyName:" + gjtSpecialty.getZymc());
		}
	}

	public void queryPageList() {

		PageRequest pageRequst = new PageRequest(0, 15);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("LIKE_title", "教学计划");
		Page<GjtArticle> list = gjtArticleService.queryPageList(searchParams, pageRequst);
		for (GjtArticle gjtArticle : list) {
			System.out.println("id:" + gjtArticle.getId());
		}
	}
}
