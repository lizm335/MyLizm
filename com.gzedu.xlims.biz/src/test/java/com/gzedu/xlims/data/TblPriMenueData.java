/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.data;

import java.math.BigDecimal;
import java.util.Date;

import com.gzedu.xlims.common.RandomData;
import com.gzedu.xlims.pojo.TblPriMenue;

public class TblPriMenueData {
	public static TblPriMenue randomNewTblPriMenue() {
		TblPriMenue tblPriMenue = new TblPriMenue();
		tblPriMenue.setMenuCode("sdfsdf");
		tblPriMenue.setIsDeleted("N");
		tblPriMenue.setRemark(RandomData.randomName("Remark"));
		tblPriMenue.setCreatedBy("123");
		tblPriMenue.setDescription("sdf");
		tblPriMenue.setIsLeaf(false);
		tblPriMenue.setMenuCode("sfsdf");
		tblPriMenue.setMenuLevel(new BigDecimal(1));
		tblPriMenue.setMenuLink("sdfsdf");
		tblPriMenue.setMenuName("sdfsdf");
		tblPriMenue.setMenuOrd(new BigDecimal(2));
		tblPriMenue.setStatus("3");
		tblPriMenue.setVersion(new BigDecimal(1));
		tblPriMenue.setCreatedDt(new Date());
		return tblPriMenue;
	}
}
