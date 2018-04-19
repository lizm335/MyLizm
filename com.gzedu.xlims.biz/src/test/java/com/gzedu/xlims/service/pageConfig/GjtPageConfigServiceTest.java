/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.pageConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.service.CommonMapService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月12日
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtPageConfigServiceTest {
	@Autowired
	private CommonMapService commonMapService;

	@Test
	public void queryPyccDates() {
		System.out.println(commonMapService.getXxmcDates("2f5bfcce71fa462b8e1f65bcd0f4c632")
				.get("2f5bfcce71fa462b8e1f65bcd0f4c632"));
	}
}
