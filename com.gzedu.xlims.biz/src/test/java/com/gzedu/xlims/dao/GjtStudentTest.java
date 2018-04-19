
package com.gzedu.xlims.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSchoolInfoDao;
import com.gzedu.xlims.dao.organization.GjtStudyCenterDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK1.7
 *
 */

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月4日
 * @version 2.5
 * @since JDK1.7
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtStudentTest {

	@Autowired
	GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	GjtSchoolInfoDao gjtSchoolInfoDao;

	@Autowired
	GjtOrgDao gjtOrgDao;

	@Autowired
	GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	GjtStudyCenterDao gjtStudyCenterDao;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	// @Test
	// public void updaeEntity() {
	//
	// GjtStudentInfo gtinfo =
	// gjtStudentInfoDao.findOne("4e7ed0a37f00000122d555b27e522a6c");
	// System.out.println(gtinfo.getEeno() + "-----" + gtinfo.getXh() + "修改前手机号"
	// + gtinfo.getSjh() + "修改前的状态"
	// + gtinfo.getIsEnabled());
	// gtinfo.setSjh("sadfsdfsdf");
	// gtinfo.setIsEnabled("1");
	// gjtStudentInfoService.updateEntity(gtinfo);
	//
	// GjtStudentInfo gt2 =
	// gjtStudentInfoDao.findOne("4e7ed0a37f00000122d555b27e522a6c");
	// System.out.println(gt2.getEeno() + "----" + gt2.getXh() + "修改后" +
	// gt2.getSjh() + "修改后" + gt2.getIsEnabled());
	//
	// }
	//
	// @Test
	// public void updaePwd() {
	// try {
	// String pwd = "1111111";
	// int i =
	// gjtUserAccountService.updatePwd("48cd07f95d614da1bec20ae57ed6bd5c",
	// Md5Util.encode(pwd),
	// pwd + " 666666");
	// System.out.println("打印-----------------" + i);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// GjtStudentInfo entity =
	// gjtStudentInfoService.queryById("4e7ed0a37f00000122d555b27e522a6c");
	// System.out.println(entity.getXm() + "---------" +
	// entity.getGjtUserAccount().getPassword() + "密码2="
	// + entity.getGjtUserAccount().getPassword2());
	//
	// }

	// @Test
	// public void findAllPage() {
	// // 分页根据条件查询分页
	// GjtStudentInfo gjtStudentInfo = new GjtStudentInfo();
	// gjtStudentInfo.setEeno("1575573");
	// gjtStudentInfo.setXh("118460213012166");
	// Page<GjtStudentInfo> page =
	// gjtStudentInfoService.querySource(gjtStudentInfo, new PageRequest(0,
	// 10));
	// GjtStudentInfo gt = page.getContent().get(0);
	// System.out.println(gt.getEeno() + "-----" + gt.getXh() + "修改前手机号" +
	// gt.getSjh() + "修改前的状态" + gt.getIsEnabled());
	// }

	// @Test
	// public void testAtdd() {
	//
	// GjtSchoolInfo gjtSchoolInfo =
	// gjtSchoolInfoDao.findOne("14eb762198734d4983b5b1865ef2c899");
	// GjtOrg gjtorg = gjtOrgDao.findOne("2ed021d2f36b49938a249dccc56704a4");
	// GjtStudyCenter gjtStudyCenter =
	// gjtStudyCenterDao.findOne("2ed021d2f36b49938a249dccc56704a4");
	// GjtSpecialty gjtSpecialty =
	// gjtSpecialtyService.queryById("9b54718072e74cbdb0f2d7add892df40");
	//
	// System.out.println("------------------------------------------添加-----------------------------------------");
	//
	// // 添加 用户帐号
	// GjtUserAccount gjtUserAccount = UserAccountData.getUserAccount();
	// Boolean boolean1 = gjtUserAccountService.saveEntity(gjtUserAccount);
	// System.out.println("添加帐号=" + boolean1);
	//
	// // 添加 学生信息
	// GjtStudentInfo gjtStudentInfo = StudentData.getStudent(gjtSchoolInfo,
	// gjtorg, gjtStudyCenter, gjtUserAccount,
	// gjtSpecialty);
	// Boolean boolean2 = gjtStudentInfoService.saveEntity(gjtStudentInfo);
	// System.out.println("添加学生信息=" + boolean2);
	//
	// System.out.println("--------------------------------查询-----------------------------------------------");
	//
	// GjtStudentInfo student =
	// gjtStudentInfoService.queryById(gjtStudentInfo.getStudentId());
	// System.out.println("id=" + student.getStudentId() + "姓名=" +
	// student.getXm() + "院校ID="
	// + student.getGjtSchoolInfo().getId() + "\n院校名=" +
	// student.getGjtSchoolInfo().getXxmc() + "帐号="
	// + student.getGjtUserAccount().getId() + "帐号名=" +
	// student.getGjtUserAccount().getRealName() + "\n学习机构id="
	// + student.getGjtOrg().getId() + "学习机构名称=" +
	// student.getGjtOrg().getOrgName());
	//
	// System.out.println("----------------------------删除-------------------------------------------------");
	//
	// // 删除用户表
	// gjtUserAccountDao.delete(gjtUserAccount.getId());
	// System.out.println("用户表已经删除");
	//
	// // 删除学生表
	// gjtStudentInfoService.delete(gjtStudentInfo.getStudentId());
	// System.out.println("学生表已经删除");
	// }

	// /**
	// *
	// * 现在的是 单点登陆
	// */
	// @org.junit.Test
	// public void simulationLogin() {
	// // <a href="?studentId=${info.studentId}" target="_blank">模拟登录</a>
	//
	// GjtStudentInfo studentInfo =
	// gjtStudentInfoService.queryById("99ff74466afa4f979ef38ac403435bf4");
	//
	// SSOUtil sso = new SSOUtil();
	// String p = sso.getSignOnParam(studentInfo.getXh());
	// String url = AppConfig.getProperty("pcenterServer") +
	// AppConfig.getProperty("sso.login.url");
	// url += "?p=" + p + "&pdmn=pd&s=" + studentInfo.getStudentId();
	// System.err.println("\n" + url);
	// // response.sendRedirect(url);
	// }

	@Test
	public void query() {
		gjtStudentInfoDao.updateStudentGrade("fdcd18882b0b4efbbeef896d532b4dab", "147bc08239344744a368721ca3c36d6c");

	}
}
