/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.data;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.StudentTypeEnum;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */
public class StudentData {
	public static GjtStudentInfo getStudent(GjtSchoolInfo gjtSchoolInfo, GjtOrg gjtorg, GjtStudyCenter gjtStudyCenter,
			GjtUserAccount gjtUserAccount, GjtSpecialty gjtSpecialty) {
		GjtStudentInfo gjtStudentInfo = new GjtStudentInfo();
		String studentId = UUIDUtils.random();
		gjtStudentInfo.setStudentId(studentId);// id
		gjtStudentInfo.setXm("卿心我心");// 姓名
		gjtStudentInfo.setArea("广播电视大学41号其实就是测试"); // 地址
		gjtStudentInfo.setGjtSchoolInfo(gjtSchoolInfo); // 所在院校
		// gjtStudentInfo.setGjtOrg(gjtorg); // 学习机构
		gjtStudentInfo.setGjtStudyCenter(gjtStudyCenter);// 学习中心
		gjtStudentInfo.setGjtUserAccount(gjtUserAccount);// 帐号表
		gjtStudentInfo.setXh("1184602130121246");// 学号
		gjtStudentInfo.setPycc("0");// 培养层次
		gjtStudentInfo.setSfzh("441481199008087012");// 身份证号
		gjtStudentInfo.setXbm("2");// 性别码
		gjtStudentInfo.setCsrq("19891011");// 生日
		gjtStudentInfo.setSjh("13926402022");// 手机号
		gjtStudentInfo.setEeno("1280800");// ee帐号，感觉多余在用户表也存在
		gjtStudentInfo.setGjtSpecialty(gjtSpecialty);// 主修科目

		gjtStudentInfo.setOrgCode("000100010005");// 机构编码
		gjtStudentInfo.setClassType("C");// 班级类型
		gjtStudentInfo.setForward("N");// 是否为跟读学员
		gjtStudentInfo.setIsFirstLogin("0");// 是否首次登陆？

		gjtStudentInfo.setUserType(String.valueOf(StudentTypeEnum.正式学员.getNum()));// 帐号类型
		gjtStudentInfo.setCreatedDt(DateUtils.getDate());// 创建日期
		gjtStudentInfo.setUpdatedDt(DateUtils.getDate());// 修改日期

		return gjtStudentInfo;
	}
}
