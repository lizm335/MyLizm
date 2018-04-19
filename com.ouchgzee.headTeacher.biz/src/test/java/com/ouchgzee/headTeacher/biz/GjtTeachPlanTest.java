/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz;

import com.ouchgzee.headTeacher.dao.study.GjtTeachPlanDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月18日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtTeachPlanTest {

	@Autowired
	private GjtTeachPlanDao gjtTeachPlanDao;

	@Test
	public void queryMapList() {
//		List<Map<String, Object>> mapList = gjtTeachPlanDao.findTachPlanClassByEmpId("z");
//		for (Map<String, Object> m: mapList) {
//			System.err.println(m);
//		}
//		Assert.notEmpty(mapList);
	}

	@Test
	public void queryObjects() {
		List<Object[]> list = gjtTeachPlanDao.findTachPlanClassByEmpId("z");
		for (Object[] objects : list) {
			System.err.println("Id：" + objects[0] + "\t\t\tClassId：" + objects[1]);
		}
		Assert.notEmpty(list);
	}

}
