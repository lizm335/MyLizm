/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.data.systemManage;

import java.math.BigDecimal;

import com.gzedu.xlims.pojo.GjtMenu;
import com.gzedu.xlims.pojo.GjtSchoolInfo;

public class GjtMenuData {

	public static GjtMenu setGjtMenu() {
		GjtMenu entity = new GjtMenu();

		entity.setName("设置测试菜单");
		entity.setOrderNo(new BigDecimal(1));
		entity.setUrl("www.baidu.com");
		entity.setVisible("1");
		entity.setNameEn("菜单英文名");

		GjtSchoolInfo gjtSchoolInfo = new GjtSchoolInfo();
		gjtSchoolInfo.setId("1");
		return entity;
	}
}
