/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.data;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;

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
public class TeacherData {
	public static GjtEmployeeInfo getTeacher(GjtSchoolInfo gjtSchoolInfo, GjtOrg gjtorg, GjtStudyCenter gjtStudyCenter,
			GjtUserAccount gjtUserAccount) {
		GjtEmployeeInfo gjtEmployeeInfo = new GjtEmployeeInfo();
		String employeeId = UUIDUtils.random();
		gjtEmployeeInfo.setEmployeeId(employeeId);// id
		gjtEmployeeInfo.setYxsh("1");// 院系所部中心号
		gjtEmployeeInfo.setKsh("学历教学部"); // 地址
		gjtEmployeeInfo.setCym("*");// 曾用名
		gjtEmployeeInfo.setZgh("qwlrgzhangyoubei");// 职工号
		gjtEmployeeInfo.setXmpy("missLiu");// 姓名拼音
		gjtEmployeeInfo.setXm("刘老师");// 姓名
		gjtEmployeeInfo.setSfzh("441481199008087012");// 身份证号
		gjtEmployeeInfo.setCsrq("19891011");// 出生日期
		gjtEmployeeInfo.setXbm("2");// 性别码
		gjtEmployeeInfo.setMzm("01");// 名族码
		gjtEmployeeInfo.setJkzkm("1");// 健康状态
		gjtEmployeeInfo.setHyzkm("0");// 婚姻状态
		gjtEmployeeInfo.setJgm("gd");// 籍贯码
		gjtEmployeeInfo.setHkxzm("1");// 户口哦性质码
		gjtEmployeeInfo.setWhcdm("0");// 文化程度
		gjtEmployeeInfo.setBzlbm("11");// 编制类别码
		gjtEmployeeInfo.setJzglbm("10");// 教职工类别码
		gjtEmployeeInfo.setRkzkm("11");// 任课状况码
		gjtEmployeeInfo.setZp("http://baidu.com");// 头像地址
		gjtEmployeeInfo.setLxdh("020222222");// 联系电话
		gjtEmployeeInfo.setSjh("13926402022");// 手机号
		gjtEmployeeInfo.setDzxx("121@121.com");// 电子邮箱
		// gjtEmployeeInfo.setGjtSchoolInfo(gjtSchoolInfo); // 所在院校
		// gjtEmployeeInfo.setGjtOrg(gjtorg); // 学习机构
		// gjtEmployeeInfo.setGjtStudyCenter(gjtStudyCenter);// 学习中心
		gjtEmployeeInfo.setGjtUserAccount(gjtUserAccount);// 帐号表
		gjtEmployeeInfo.setEeno("1280800");// ee帐号，感觉多余在用户表也存在
		gjtEmployeeInfo.setOrgCode("000100010005");// 机构编码
		gjtEmployeeInfo.setEmployeeType(String.valueOf(EmployeeTypeEnum.班主任.getNum()));// 帐号类型
		gjtEmployeeInfo.setCreatedDt(DateUtils.getDate());// 创建日期
		gjtEmployeeInfo.setUpdatedDt(DateUtils.getDate());// 修改日期
		gjtEmployeeInfo.setIsDeleted("N");
		gjtEmployeeInfo.setIsEnabled(String.valueOf(1));
		gjtEmployeeInfo.setVersion(new BigDecimal(1));
		gjtEmployeeInfo.setSyncStatus("N");// 同步状态

		return gjtEmployeeInfo;
	}
}
