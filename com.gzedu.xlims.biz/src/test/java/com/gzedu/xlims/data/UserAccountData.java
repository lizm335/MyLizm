/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.data;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.UserTypeEnum;

import java.math.BigDecimal;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */
public class UserAccountData {
	public static GjtUserAccount getUserAccount() {
		GjtUserAccount gjtUserAccount = new GjtUserAccount();
		String userId = UUIDUtils.random();
		gjtUserAccount.setId(userId); // id
		gjtUserAccount.setLoginAccount("144410140907686"); // 登陆帐号
		gjtUserAccount.setPassword(Md5Util.encode("888888")); // 密码
		gjtUserAccount.setPassword2("888888"); // 密码
		gjtUserAccount.setRealName("卿心我心"); // 姓名
		gjtUserAccount.setUserType(UserTypeEnum.学生.getNum());// 学生类型
		gjtUserAccount.setVersion(new BigDecimal(1));// 版本
		gjtUserAccount.setDataScope("0");// 查看数据范围
		gjtUserAccount.setIsEnabled(true);// 帐号是否启用
		gjtUserAccount.setEeno("1280800");// EE帐号
		gjtUserAccount.setIsDeleted("N");// 是否删除
		gjtUserAccount.setCreateTime(DateUtils.getDate());// 创建日期
		gjtUserAccount.setCreatedDt(DateUtils.getDate());// 创建时间
		gjtUserAccount.setUpdatedDt(DateUtils.getDate());// 修改时间
		return gjtUserAccount;
	}

	public static GjtUserAccount getUserTeacher() {
		GjtUserAccount gjtUserAccount = new GjtUserAccount();
		String userId = UUIDUtils.random();
		gjtUserAccount.setId(userId); // id
		gjtUserAccount.setLoginAccount("1444101489076865"); // 登陆帐号
		gjtUserAccount.setPassword(Md5Util.encode("888888")); // 密码
		gjtUserAccount.setPassword2("888888"); // 密码
		gjtUserAccount.setRealName("麻辣教师"); // 姓名
		gjtUserAccount.setUserType(UserTypeEnum.教师.getNum());// 学生类型
		gjtUserAccount.setVersion(new BigDecimal(1));// 版本
		gjtUserAccount.setDataScope("0");// 查看数据范围
		gjtUserAccount.setIsEnabled(true);// 帐号是否启用
		gjtUserAccount.setEeno("1280859");// EE帐号
		gjtUserAccount.setIsDeleted("N");// 是否删除
		gjtUserAccount.setCreateTime(DateUtils.getDate());// 创建日期
		gjtUserAccount.setCreatedDt(DateUtils.getDate());// 创建时间
		gjtUserAccount.setUpdatedDt(DateUtils.getDate());// 修改时间
		return gjtUserAccount;
	}
}
