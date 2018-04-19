/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.common.AppConfig;

/**
 * 功能说明：
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class AppConfigTest {
	
	@org.junit.Test
	public void getConfig(){
		System.out.println(AppConfig.getProperty("cssconfig"));
		System.out.println(AppConfig.getProperty("imageconfig"));
	}

}
