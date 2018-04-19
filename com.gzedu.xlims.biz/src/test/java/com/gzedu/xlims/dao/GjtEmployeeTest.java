
package com.gzedu.xlims.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSchoolInfoDao;
import com.gzedu.xlims.dao.organization.GjtStudyCenterDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtEmployeePosition;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtEmployeeTest {

	@Autowired
	GjtSchoolInfoDao gjtSchoolInfoDao;

	@Autowired
	GjtOrgDao gjtOrgDao;

	@Autowired
	GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	GjtStudyCenterDao gjtStudyCenterDao;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	// @Test
	// public void updaeEntity() {
	// GjtEmployeeInfo gtinfo =
	// gjtEmployeeInfoService.queryById("7d778b63b41a46fb86aa21fdc0f21aa8");
	// System.out.println(gtinfo.getEeno() + "--修改前姓名-" + gtinfo.getXm() +
	// "修改前手机号" + gtinfo.getSjh() + "修改前的状态"
	// + gtinfo.getIsEnabled());
	// gtinfo.setSjh("13926408888");
	// gtinfo.setXm("向东流");
	// gtinfo.setIsEnabled("0");
	// gtinfo.getGjtUserAccount().setPassword2("222222");
	// gjtEmployeeInfoService.updateEntity(gtinfo);
	//
	// GjtEmployeeInfo gtinfo2 =
	// gjtEmployeeInfoService.queryById("7d778b63b41a46fb86aa21fdc0f21aa8");
	// System.out.println(gtinfo2.getEeno() + "-修改后---" + gtinfo2.getXm() +
	// "修改后" + gtinfo2.getSjh() + "修改后"
	// + gtinfo2.getIsEnabled());
	// }
	//
	// @Test
	// public void updaePwd() {
	// try {
	// String pwd = "1111111";
	// boolean i =
	// gjtEmployeeInfoService.updatePwd("7d778b63b41a46fb86aa21fdc0f21aa8");
	// System.out.println("打印-----------------" + i);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// GjtEmployeeInfo entity =
	// gjtEmployeeInfoService.queryById("7d778b63b41a46fb86aa21fdc0f21aa8");
	// System.out.println(entity.getXm() + "---------" +
	// entity.getGjtUserAccount().getPassword() + "密码2="
	// + entity.getGjtUserAccount().getPassword2());
	//
	// }
	//
	// @Test
	// public void findAllPage() {
	// // 分页根据条件查询分页
	// GjtEmployeeInfo gjtEmployeeInfo = new GjtEmployeeInfo();
	// gjtEmployeeInfo.setZgh("1575573");
	// gjtEmployeeInfo.setXm("118460213012166");
	// Page<GjtEmployeeInfo> page =
	// gjtEmployeeInfoService.querySource(gjtEmployeeInfo, new PageRequest(0,
	// 10));
	// GjtEmployeeInfo gt = page.getContent().get(0);
	// System.out.println("打印职工号" + gt.getZgh() + "-----" + gt.getXm());
	// }

	// @org.junit.Test
	// public void testAtdd() {
	//
	// GjtSchoolInfo gjtSchoolInfo =
	// gjtSchoolInfoDao.findOne("14eb762198734d4983b5b1865ef2c899");
	// GjtOrg gjtorg = gjtOrgDao.findOne("2ed021d2f36b49938a249dccc56704a4");
	// GjtStudyCenter gjtStudyCenter =
	// gjtStudyCenterDao.findOne("2ed021d2f36b49938a249dccc56704a4");
	//
	// System.out.println("------------------------------------------添加-----------------------------------------");
	//
	// // 添加 用户帐号
	// GjtUserAccount gjtUserAccount = UserAccountData.getUserTeacher();
	// Boolean boolean1 = gjtUserAccountService.saveEntity(gjtUserAccount);
	// System.out.println("添加帐号=" + boolean1);
	//
	// // 添加 教职工信息
	// GjtEmployeeInfo gjtEmployeeInfo = TeacherData.getTeacher(gjtSchoolInfo,
	// gjtorg, gjtStudyCenter, gjtUserAccount);
	// Boolean boolean2 = gjtEmployeeInfoService.saveEntity(gjtEmployeeInfo);
	// System.out.println("添加职工信息=" + boolean2);
	//
	// System.out.println("--------------------------------查询-----------------------------------------------");
	//
	// GjtEmployeeInfo employeeInfo =
	// gjtEmployeeInfoService.queryById(gjtEmployeeInfo.getEmployeeId());
	// System.out.println("id=" + employeeInfo.getEmployeeId() + "姓名=" +
	// employeeInfo.getXm() + "院校ID="
	// + employeeInfo.getGjtSchoolInfo().getId() + "\n院校名=" +
	// employeeInfo.getGjtSchoolInfo().getXxmc() + "帐号="
	// + employeeInfo.getGjtUserAccount().getId() + "帐号名=" +
	// employeeInfo.getGjtUserAccount().getRealName()
	// + "学习机构名称=" + employeeInfo.getGjtOrg().getOrgName());
	//
	// System.out.println("----------------------------删除-------------------------------------------------");
	//
	// // 删除用户表
	// gjtUserAccountService.delete(gjtUserAccount.getId());
	// System.out.println("用户表已经删除");
	//
	// // 删除教职工表
	// gjtEmployeeInfoService.delete(gjtEmployeeInfo.getEmployeeId());
	// System.out.println("教师表已经删除");
	//
	// }
	//
	// @org.junit.Test
	// public void updateIsDelete() {
	// Boolean updateIsEnabled =
	// gjtEmployeeInfoService.updateIsEnabled("f4dd68ab5bd84852a4c7883e220981b9",
	// "0");
	// System.out.println(updateIsEnabled);
	//
	// gjtEmployeeInfoService.deleteById("f4dd68ab5bd84852a4c7883e220981b9");
	//
	// }

	/**
	 * 模拟登录
	 */
	@org.junit.Test
	@Transactional
	public void simulationLogin() {
		// <a href="?studentId=${info.studentId}" target="_blank">模拟登录</a>

		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById("7d89138095dd4288b04484bb61090d82");
		GjtUserAccount gjtUserAccount = employeeInfo.getGjtUserAccount();
		if (gjtUserAccount != null) {
			EncryptUtils enc = new EncryptUtils();
			String str = enc.encrypt(gjtUserAccount.getLoginAccount() + "," + employeeInfo.getEmployeeId() + ","
					+ DateUtils.getDate().getTime());
			String url = AppConfig.getProperty("pcenterEmployeeServer")
					+ AppConfig.getProperty("employee.sso.login.url");

			url += "?userInfo=" + str;
			System.err.println("\n" + url);
			// response.sendRedirect(url);
		}
	}

	/**
	 * 论文老师测试
	 */
	@org.junit.Test
	@Transactional
	public void employeePosition() {
		// 查询到一个教师信息
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById("120413c3c8130bfdc3e273f32b3c18f4");
		System.err.println(employeeInfo.getXm());

		// 获取教师的工作岗位
		if ((EmployeeTypeEnum.论文教师.getNum() + "").equals(employeeInfo.getEmployeeType())) {
			for (GjtEmployeePosition position : employeeInfo.getGjtEmployeePositionList()) {
				System.err.println(
						position.getId().getType() + "\t" + EmployeeTypeEnum.getName(position.getId().getType()));
			}
		}
	}

}
