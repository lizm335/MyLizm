/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.data;

import java.math.BigDecimal;
import java.util.Date;

import com.gzedu.xlims.common.RandomData;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.TblPriMenue;
import com.gzedu.xlims.pojo.TblPriRole;
import com.gzedu.xlims.pojo.status.UserTypeEnum;

/**
 * 功能说明：
 * 
 * @author liming
 * @Date 2016年4月29日
 * @version 1.0
 *
 */
public class RandomTestData {

	public static GjtUserAccount GjtUserAccount() {
		GjtUserAccount userAccount = new GjtUserAccount();
		userAccount.setAllowBackLogin("1");
		userAccount.setCreatedBy("sdf");
		userAccount.setCreatedDt(new Date());
		userAccount.setCreateTime(new Date());
		userAccount.setCurrentLoginIp("123");
		userAccount.setCurrentLoginTime(new Date());
		userAccount.setDataScope("1");
		userAccount.setEeno("sdf");
		userAccount.setEmail("sdf");
		userAccount.setEndDate(new Date());
		userAccount.setGrantType("1");
		userAccount.setUserType(UserTypeEnum.学生.getNum());
		userAccount.setUpdatedDt(new Date());
		userAccount.setStatus("Y");
		userAccount.setRealName("cese");
		userAccount.setPassword("sdf");
		userAccount.setPassword2("fds");
		// userAccount.setGjtOrg(new GjtOrg(UUIDUtils.random()));
		userAccount.setNickName("cese");
//		userAccount.setOrgCode("sdfd");
		return userAccount;
	}

	public static TblPriRole TblPriRole() {
		TblPriRole tblPriRole = new TblPriRole();
		tblPriRole.setCreatedBy("sdf");
		tblPriRole.setCreatedDt(new Date());
		tblPriRole.setDescription("sdf");
		tblPriRole.setIsDeleted("N");
		tblPriRole.setOrgId("123");
		tblPriRole.setRemark("sdfsdf");
		tblPriRole.setRoleName("cese");
		tblPriRole.setStatus("1");
		tblPriRole.setVersion(new BigDecimal(1));
		return tblPriRole;
	}

	public static TblPriMenue TblPriMenue() {
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
