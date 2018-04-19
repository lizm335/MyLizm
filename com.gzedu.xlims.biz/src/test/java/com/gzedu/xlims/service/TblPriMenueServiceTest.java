/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File MenueDaoTest.java
 * @Date:2016年4月26日下午2:18:30
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.data.TblPriMenueData;
import com.gzedu.xlims.pojo.TblPriMenue;
import com.gzedu.xlims.service.menue.TblPriMenueService;

/**
 * 
 * 功能说明：菜单测试用例
 * 
 * @author liming
 * @Date 2016年5月4日
 * @version 2.5
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class TblPriMenueServiceTest {

	@Autowired
	private TblPriMenueService menueService;

	@Test
	public void queryAll() {
		TblPriMenue menue = new TblPriMenue();
		menue.setMenuLevel(new BigDecimal(1));
		// menue.setMenuName("教务管理");
		// menue.setIsLeaf(true);

		Page<TblPriMenue> page = menueService.queryAll(menue, new PageRequest(0, 100));
		System.out.println(page.getTotalElements());
		for (TblPriMenue tblPriMenue : page.getContent()) {
			System.out.println(tblPriMenue.getMenuName());
			for (TblPriMenue tblPriMenue2 : tblPriMenue.getChildMenueList()) {
				System.out.println("--" + tblPriMenue2.getMenuName());
			}
		}
	}

	@Test
	public void insertMenue() {
		TblPriMenue menue = TblPriMenueData.randomNewTblPriMenue();

		try {
			menueService.insertMenue(menue);
			System.out.println("保存成功" + menue.getMenuName());
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

}
