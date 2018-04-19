/**
mappedBy = "priOperateInfo" * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File MenueDaoTest.java
 * @Date:2016年4月26日下午2:18:30
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.menue.TblPriMenueDao;
import com.gzedu.xlims.dao.menue.TblPriMenueSpecs;
import com.gzedu.xlims.data.TblPriMenueData;
import com.gzedu.xlims.pojo.TblPriMenue;

/**
 * 
 * 功能说明：菜单测试用例
 * 
 * @author liming
 * @Date 2016年5月4日
 * @version 2.5
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class TblPriMenueDaoTest {

	@Autowired
	private TblPriMenueDao menueDao;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Test
	public void crud() {
		Date now = new Date();
		TblPriMenue menue = TblPriMenueData.randomNewTblPriMenue();
		menue.setMenuId(UUID.randomUUID().toString());
		menue.setCreatedDt(now);

		String pId = "a9dc67e019fd48e6be7b2d0b32665c4d";
		TblPriMenue parentMenue = menueDao.findOne(pId);
		menue.setParentMenue(parentMenue);

		TblPriMenue newMenue = menueDao.save(menue);
		Assert.assertEquals(newMenue.getParentMenue().getMenuId(), pId);

		// menueDao.delete(menue.getMenuId());
	}

	@Test
	public void query() {
		List<TblPriMenue> list = menueDao.findAll();

		for (TblPriMenue tblPriMenue : list) {
			String space = "-";
			this.next(tblPriMenue, space);
		}
	}

	private void next(TblPriMenue tblPriMenue, String space) {
		List<TblPriMenue> childModelList = tblPriMenue.getChildMenueList();
		if (childModelList != null) {
			space += "-";
			for (TblPriMenue element : childModelList) {
				System.out.println(space + element.getMenuName());
				this.next(element, space);
			}
		}
	}

	@Test
	public void findByIsDeleted() {
		List<TblPriMenue> page = menueDao.findByIsDeleted("Y");
		Assert.assertEquals(8, page.size());
	}

	@Test
	public void findPrentObject() {
		TblPriMenue menue = menueDao.findOne("2cf3eddeadb1421ab70b0ee286e06e7a");
		System.out.println(menue.getMenuName() + "  " + menue.getParentMenue().getMenuName());
		Assert.assertNotNull(menue.getMenuName());
		Assert.assertNotNull(menue.getParentMenue().getMenuName());
	}

	@Test
	public void findChirenObject() {
		TblPriMenue menue = menueDao.findOne(TblPriMenueSpecs.isMenuId("74f523532bcc41dc870faae6f758c973"));
		System.out.println(menue.getMenuName());
		for (TblPriMenue ss : menue.getChildMenueList()) {
			System.out.println(ss.getMenuName());
		}
	}

	@Test
	public void isPrentMenuId() {
		menueDao.findAll(TblPriMenueSpecs.isPrentMenuId("6963931833d04d5fa0ecd506a90cafd4"));
	}
}
