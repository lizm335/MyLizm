/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.exam;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年9月26日
 * @version 2.5
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class ExamPointDaoTest {

	@Autowired
	GjtExamPointNewDao gjtExamPointNewDao;

	@org.junit.Test
	public void queryAll() {
	}

	@org.junit.Test
	public void testAdd() {

	}

	@org.junit.Test
	public void updateAdd() {

	}

}
