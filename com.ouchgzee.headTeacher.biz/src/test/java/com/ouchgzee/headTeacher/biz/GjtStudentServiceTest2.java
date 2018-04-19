/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.gzedu.SSOUtil;
import com.ouchgzee.headTeacher.daoImpl.StudentInfoDaoImpl;
import com.ouchgzee.headTeacher.dto.StudentClockingInDto;
import com.ouchgzee.headTeacher.dto.StudentDto;
import com.ouchgzee.headTeacher.dto.StudentPaymentDto;
import com.ouchgzee.headTeacher.dto.StudentStateDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtSpecialty;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.BzrTblPriLoginLog;
import com.ouchgzee.headTeacher.service.BzrCacheService;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;

/**
 * 
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtStudentServiceTest2 {

	private final String STUDENT_ID = "ebc806cd61fb4982b3e54407662ee08b";

	private final String CLASS_ID = "ba91eb1a11154f1d8843cbf957eff4fc";

	@Autowired
	private BzrGjtStudentService gjtStudentService;

	@Autowired
	private BzrGjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private BzrGjtUserAccountService gjtUserAccountService;

	@Autowired
	private BzrCacheService cacheService;

	@Autowired
	private StudentInfoDaoImpl studentInfoDao;

	@Test
	public void queryById() {
		BzrGjtStudentInfo studentInfo = gjtStudentService.queryById(STUDENT_ID);
		System.err.println(studentInfo.getGjtSignup().getSignupId());
		System.err.println(studentInfo.getGjtSignup().getCreatedDt());
	}

	/**
	 * 单表分页查询
	 */
	@Test
	public void manyCondition() {
		Map<String, Object> searchParams = new HashMap();
		searchParams.put("LIKE_xm", "李");

		Page<BzrGjtStudentInfo> page = gjtStudentService.queryByPage(searchParams, new PageRequest(1, 10));

		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		for (BzrGjtStudentInfo info : page.getContent()) {
			System.out
					.println(info.getStudentId() + "\t" + info.getXm() + "\t\t" + info.getCreatedDt().toLocaleString());

			System.out.println("AccountId：" + info.getGjtUserAccount().getId());
			// System.out.println("ClassId：" +
			// info.getGjtClassInfoList().get(0).getClassId());
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 学员信息列表页面
	 */
	@Test
	public void queryStudentInfoSpecialty() {
		BzrGjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByLoginAccount("bzr001");
		List<BzrGjtSpecialty> specialtyList = gjtStudentService.querySpecialtyBy(employeeInfo.getXxId());

		Map<String, Object> searchParams = new HashMap();
		searchParams.put("EQ_gjtUserAccount.loginAccount", "20160930");
		searchParams.put("LIKE_xm", "溢");
		searchParams.put("EQ_gjtSpecialty.specialtyId", "1de90679115344bcb24bc2dbc6fabaa9");

		Page<BzrGjtStudentInfo> page = gjtStudentService.queryStudentInfoSpecialtyByClassIdPage(CLASS_ID, searchParams,
				new PageRequest(0, 10));

		for (BzrGjtSpecialty sp : specialtyList) {
			System.err.println(sp.getSpecialtyId() + "\t" + sp.getZymc());
		}

		System.err.println(page.getNumberOfElements());
		System.err.println(page.getTotalElements());
		System.err.println(page.getTotalPages());

		System.err.println("ID\t学员账号\t学员姓名\t手机号码\t邮箱地址\t报读时间\t报读产品\t年级\t操作");
		for (BzrGjtStudentInfo info : page.getContent()) {
			System.err.print(info.getStudentId() + "\t");
			System.err.print(info.getGjtUserAccount().getLoginAccount() + "\t");
			System.err.print(info.getXm() + "\t");
			System.err.print(info.getSjh() + "\t");
			System.err.print(info.getDzxx() + "\t");
			System.err.print(info.getGjtSignup().getCreatedDt() + "\t");
			System.err.print(info.getGjtSpecialty().getZymc() + "\t");
			// System.err.print(info.getNj() + "\t");
			System.err.print(info.getGjtSignup().getGjtEnrollBatch().getGjtGrade().getGradeName() + "\t");
			System.err.println();
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 重置密码
	 */
	@Test
	public void resetPwd() {
		BzrGjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByLoginAccount("bzr001");

		Date begin = new Date();
		boolean flag = gjtStudentService.updateStudentResetPwd(STUDENT_ID, employeeInfo.getGjtUserAccount().getId());
		Date end = new Date();
		System.out.println("更改结果：" + flag + "\tDate:" + (end.getTime() - begin.getTime()));

		Assert.isTrue(flag);
	}

	/**
	 * 模拟登录
	 */
	@Test
	public void simulationLogin() {
		BzrGjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByLoginAccount("bzr001");

		// <a href="?studentId=${info.studentId}" target="_blank">模拟登录</a>

		BzrGjtStudentInfo studentInfo = gjtStudentService.queryById(STUDENT_ID);

		if ("696c75a310b44a7f9dd61516ea84fbe3".equals(employeeInfo.getXxId())) {
			// 广州电大
			/*
			 * SSOUtil sso = new SSOUtil(); String p =
			 * sso.encrypt(SSOUtil.GZDD_APP_ID, studentInfo.getXh()); String url
			 * = AppConfig.getProperty("gzddServer") +
			 * AppConfig.getProperty("sso.login.url"); url += "?p=" + p + "&s="
			 * + studentInfo.getStudentId(); System.err.println("\n" + url); //
			 * response.sendRedirect(url);
			 */ } else if ("2f5bfcce71fa462b8e1f65bcd0f4c632".equals(employeeInfo.getXxId())) { // 国开
			SSOUtil sso = new SSOUtil();
			String p = sso.getSignOnParam(studentInfo.getXh());
			String url = AppConfig.getProperty("pcenterServer") + AppConfig.getProperty("sso.login.url");
			url += "?p=" + p + "&pdmn=pd&s=" + studentInfo.getStudentId();
			System.err.println("\n" + url);
			// response.sendRedirect(url);
		} else {
			System.err.println("\n非国开和电大的班主任，无权限模拟登录");
		}
	}

	/**
	 * 模拟登录
	 */
	public static void main(String[] args) {
		SSOUtil sso = new SSOUtil();
		String p = sso.getSignOnParam("1444101207401");
		String url = AppConfig.getProperty("pcenterServer") + AppConfig.getProperty("sso.login.url");
		url += "?p=" + p + "&pdmn=pd";
		System.err.println("\n" + url);
	}

	@Test
	public void enteringSchool() {
		/*
		 * String[] students = new String[2]; students[0] =
		 * "c1a44be464504d9eb7403300c51f0068"; students[1] =
		 * "4e7ed0897f00000122d555b24732bedf"; boolean flag =
		 * gjtStudentService.updateStudentEnteringSchool(students,
		 * "a3dbdbe68e7f4a56bd0071aece07b0db");
		 * 
		 * Assert.isTrue(flag);
		 */
	}

	/**
	 * 导出学员信息功能
	 */
	@Test
	public void exportInfo() {
		String outputUrl = "C:\\Users\\Administrator\\Desktop\\StudentInfo_20160525.xls";

		Map<String, Object> searchParams = new HashMap();
		searchParams.put("EQ_gjtUserAccount.loginAccount", "20160930");
		searchParams.put("LIKE_xm", "溢");
		searchParams.put("EQ_gjtSpecialty.specialtyId", "1de90679115344bcb24bc2dbc6fabaa9");

		HSSFWorkbook workbook = gjtStudentService.exportStudentInfoSpecialtyToExcel(CLASS_ID, searchParams, null);
		OutputStream os = null;
		try {
			os = new FileOutputStream(outputUrl);
			workbook.write(os);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 学员状态列表页面
	 */
	@Test
	@Transactional(value = "transactionManagerBzr", readOnly = true)
	public void queryStudentState() {
		Map<String, Object> searchParams = new HashMap();
		// searchParams.put("loginAccount", "20160930");
		// searchParams.put("xm", "张");

		Page<StudentStateDto> page = gjtStudentService.queryStudentStateByClassIdPage(
				"59e3a89436a4449baa3e77a3c1f1eeb6", searchParams, new PageRequest(0, 10));

		System.err.println(page.getNumberOfElements());
		System.err.println(page.getTotalElements());
		System.err.println(page.getTotalPages());

		Map<String, String> xjztMap = cacheService
				.getCachedDictionaryMap(BzrCacheService.DictionaryKey.STUDENTNUMBERSTATUS);
		System.err.println("ID\t学员姓名\t报读时间\t报读产品\t年级\t学习状态\t资料完善\t缴费状态\t学籍状态\t毕业状态\t学位证书\t操作");
		for (StudentStateDto info : page.getContent()) {
			System.err.print(info.getStudentId() + "\t");
			System.err.print(info.getXm() + "\t");
			System.err.print(info.getSignupDt() + "\t");
			System.err.print(info.getZymc() + "\t");
			System.err.print(info.getGradeName() + "\t");
			System.err.print("0".equals(info.getLearningState()) ? "未激活" : "已激活" + "\t");
			System.err.print("1".equals(info.getDataState()) ? "已完善" : "待完善" + "\t");
			System.err.print("1".equals(info.getFeeStatus()) ? "已缴费" : "未缴费" + "\t");
			System.err.print(xjztMap.get(info.getXjzt()) + "\t");
			System.err.print(("1".equals(info.getReceiveStatus()) || "2".equals(info.getReceiveStatus())) ? "已毕业"
					: "未毕业" + "\t");
			System.err.print(info.getCertificateNum() + "\t");
			System.err.println();
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 导出学员状态信息
	 */
	@Test
	@Transactional(value = "transactionManagerBzr", readOnly = true)
	public void exportStudentState() {
		String outputUrl = "C:\\Users\\Administrator\\Desktop\\StudentState_20160530.xls";

		Map<String, Object> searchParams = new HashMap();
		// searchParams.put("loginAccount", "20160930");
		searchParams.put("xm", "溢");

		HSSFWorkbook workbook = gjtStudentService.exportStudentStateToExcel(CLASS_ID, searchParams, null);
		OutputStream os = null;
		try {
			os = new FileOutputStream(outputUrl);
			workbook.write(os);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 学员考勤列表页面
	 */
	@Test
	@Transactional(value = "transactionManagerBzr", readOnly = true)
	public void queryStudentClockingIn() {
		Map<String, Object> searchParams = new HashMap();
		// searchParams.put("loginAccount", "440583198708011642");
		// searchParams.put("xm", "王");
		searchParams.put("noLoginDays", 1);

		Page<StudentClockingInDto> page = gjtStudentService.queryStudentClockingInByClassIdPage(
				"54a100f4bbe14f75a30d6f9be9700238", searchParams, new PageRequest(0, 10));

		System.err.println(page.getNumberOfElements());
		System.err.println(page.getTotalElements());
		System.err.println(page.getTotalPages());

		System.err.println("ID\t学员姓名\t首次登录\t登录总次数\t登录总时长\t最后一次登录\t未登录天数\t操作");
		for (StudentClockingInDto info : page.getContent()) {
			System.err.print(info.getStudentId() + "\t");
			System.err.print(info.getXm() + "\t");
			System.err.print(info.getFirstLogin() + "\t");
			System.err.print(info.getCountLogin() + "\t");
			System.err.print(info.getTotalMinute() + "\t");
			System.err.print(info.getLastLogin() + "\t");
			System.err.print(info.getNoLoginDays() + "\t");
			System.err.println();
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 学员考勤详情
	 */
	@Test
	public void queryStudentClockingInDetail() {
		BzrGjtStudentInfo studentInfo = gjtStudentService.queryById("735e137bf95b4aa6af21ad2bee1917df");

		Map<String, Object> searchParams = new HashMap();
		searchParams.put("EQ_username", studentInfo.getGjtUserAccount().getLoginAccount());
		searchParams.put("GTE_createdDt", "2016-01-01");
		searchParams.put("LTE_createdDt", "2016-07-01");

		Page<BzrTblPriLoginLog> page = gjtUserAccountService.queryLoginLogByPage(searchParams, new PageRequest(0, 10));

		System.err.println(page.getNumberOfElements());
		System.err.println(page.getTotalElements());
		System.err.println(page.getTotalPages());

		System.err.println("序号\tIP地址\t所在区域\t学习终端\t浏览器\t登入平台时间\t登出平台时间\t登录时长\t操作");
		for (int i = 0; i < page.getContent().size(); i++) {
			BzrTblPriLoginLog info = page.getContent().get(i);
			System.err.print(i + 1 + "\t");
			System.err.print(info.getLoginIp() + "\t");
			System.err.print("" + "\t");
			System.err.print(info.getOs() + "\t");
			System.err.print(info.getBrowser() + "\t");
			System.err.print(info.getCreatedDt() + "\t");
			System.err.print(info.getUpdatedDt() + "\t");
			System.err.print(info.getLoginTime() + "\t");
			System.err.println();
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 导出学员考勤信息
	 */
	@Test
	@Transactional(value = "transactionManagerBzr", readOnly = true)
	public void exportStudentClockingIn() {
		String outputUrl = "C:\\Users\\Administrator\\Desktop\\StudentClockingIn_20160604.xls";

		Map<String, Object> searchParams = new HashMap();
		// searchParams.put("loginAccount", "440583198708011642");
		// searchParams.put("xm", "王");
		// searchParams.put("noLoginDays", 1);

		HSSFWorkbook workbook = gjtStudentService.exportStudentClockingInToExcel("54a100f4bbe14f75a30d6f9be9700238",
				searchParams, null);
		OutputStream os = null;
		try {
			os = new FileOutputStream(outputUrl);
			workbook.write(os);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 缴费管理列表页面
	 */
	@Test
	@Transactional(value = "transactionManagerBzr", readOnly = true)
	public void queryPaymentSituation() {
		Map<String, Object> searchParams = new HashMap();
		// searchParams.put("loginAccount", "20160930");
		// searchParams.put("xm", "*");

		Page<StudentPaymentDto> page = gjtStudentService.queryPaymentSituationByClassIdPage(
				"59e3a89436a4449baa3e77a3c1f1eeb6", searchParams, new PageRequest(0, 25));

		System.err.println(page.getNumberOfElements());
		System.err.println(page.getTotalElements());
		System.err.println(page.getTotalPages());

		System.err.println("ID\t学员姓名\t缴费类型\t优惠政策\t缴费金额\t当前应缴金额\t最迟缴费时间\t当前缴费状态\t学习状态\t操作");
		for (Iterator<StudentPaymentDto> iter = page.iterator(); iter.hasNext();) {
			StudentPaymentDto info = iter.next();

			System.err.print(info.getStudentId() + "\t");
			System.err.print(info.getXm() + "\t");
			System.err.print(info.getPaymentDetail().get("GKXL_PAYMENT_TPYE") + "\t");
			System.err.print(null + "\t");
			System.err.print(info.getPaymentDetail().get("ORDER_TOTAL_AMT") + "\t");
			System.err.print(null + "\t");
			System.err.print(null + "\t");
			System.err.print(null + "\t");
			System.err.print((info.getLearningState() == null || Constants.BOOLEAN_1.equals(info.getLearningState()))
					? "正常学习" : "停止学习" + "\t");
			{
				if (info.getLearningState() == null || Constants.BOOLEAN_1.equals(info.getLearningState())) {
					System.err
							.print("<a href='changeLearningState?studentId=${info.studentId}&learningState=0'>停止学习</a>"
									+ "\t");
				} else if (info.getOverdueCount() != null && info.getOverdueCount() < 3) {
					System.err.print("<a href='javascript:void(0);'>——</a>" + "\t");
				} else {
					System.err
							.print("<a href='changeLearningState?studentId=${info.studentId}&learningState=1'>恢复学习</a>"
									+ "\t");
				}
			}
			System.err.println();
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 导出缴费信息
	 */
	@Test
	@Transactional(value = "transactionManagerBzr", readOnly = true)
	public void exportPaymentSituation() {
		String outputUrl = "C:\\Users\\Administrator\\Desktop\\PaymentSituation_20160530.xls";

		Map<String, Object> searchParams = new HashMap();
		// searchParams.put("loginAccount", "20160930");
		// searchParams.put("xm", "*");

		HSSFWorkbook workbook = gjtStudentService.exportPaymentSituationToExcel("54a100f4bbe14f75a30d6f9be9700238",
				searchParams, null);
		OutputStream os = null;
		try {
			os = new FileOutputStream(outputUrl);
			workbook.write(os);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 缴费信息详情
	 */
	@Test
	@Transactional(value = "transactionManagerBzr", readOnly = true)
	public void queryPaymentSituationDetail() {
		String studentId = "93190338a91d4daea4af475254b45e11";
		BzrGjtStudentInfo info = gjtStudentService.queryById(studentId);

		Map paymentInfo = gjtStudentService.queryPaymentSituationByStudentId(studentId);

		System.err.print(info.getStudentId() + "\t");
		System.err.print(info.getZp() + "\t");
		System.err.print(info.getGjtUserAccount().getLoginAccount() + "\t");
		System.err.print(info.getXm() + "\t");
		System.err.print(info.getSjh() + "\t");
		System.err.print(info.getDzxx() + "\t");
		System.err.print(info.getGjtSpecialty().getZymc() + "\t");

		System.err.println();
		System.err.println("缴费类型：" + (String) paymentInfo.get("GKXL_PAYMENT_TPYE"));
		System.err.println("优惠补贴：" + (String) paymentInfo.get("?"));
		System.err.println("实收/当前应收/总金额：" + NumberUtils.toDouble(paymentInfo.get("ORDER_AMT") + "") + "/"
				+ NumberUtils.toDouble(paymentInfo.get("ORDER_TOTAL_AMT") + "") + "/?");

		if (paymentInfo.get("ORDER_PAY_RECORD_LIST") != null) {
			System.err.println("序号\t期数\t已缴金额/应缴金额\t缴费时间/最迟缴费时间\t状态");
			List<Map> orders = (List) ((Map) paymentInfo.get("ORDER_PAY_RECORD_LIST")).get("PAY_RECORD");
			for (int i = 0; i < orders.size(); i++) {
				Map childOd = orders.get(i);
				System.err.print(i + "\t");
				System.err.print(childOd.get("PAY_RECORD_TYPE_CODE") + "\t");
				System.err.print(
						"Y".equals(childOd.get("PAY_STATUS")) ? NumberUtils.toDouble(paymentInfo.get("REC_AMT") + "")
								: "--" + "/" + NumberUtils.toDouble(paymentInfo.get("REC_AMT") + "") + "\t");
				System.err.print(
						"Y".equals(childOd.get("PAY_STATUS")) ? "?" : "--" + "/" + childOd.get("REC_DATE") + "\t");
				if ("Y".equals(childOd.get("PAY_STATUS"))) {
					System.err.print("正常缴费\t");
				} else {
					try {
						if (new Date()
								.after(new SimpleDateFormat("yyyy-MM-dd").parse((String) childOd.get("REC_DATE")))) {
							System.err.print("欠费\t");
						} else {
							System.err.print("未开始\t");
						}
					} catch (ParseException e) {

					}
				}
				System.err.print(i + "\t");
			}
		}
		Assert.notEmpty(paymentInfo, "未查询到缴费信息");
	}

	/**
	 * 操作学员学习状态
	 */
	@Test
	public void changePaymentState() {
		String studentId = "3b4218352ed547f3b048933ed5090288";
		int learningState = 1;// 0-停止学习 1-恢复学习
		boolean flag = gjtStudentService.updateChangePaymentState(studentId, learningState);
		Assert.isTrue(flag);
	}

	@Test
	public void queryHql() {
		Page<StudentDto> page = studentInfoDao.findByPage("李",
				new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createdDt")));

		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		for (StudentDto info : page.getContent()) {
			System.out.println(info.getStudentId() + "\t" + info.getXm());
		}
		Assert.notEmpty(page.getContent());
	}

	@Test
	public void querySql() {
		Page<Map> page = studentInfoDao.findByPageSQLTest("李", new PageRequest(0, 10,
				new Sort(new Sort.Order(Sort.Direction.DESC, "created_dt"), new Sort.Order("xm"))));

		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		for (Iterator<Map> iter = page.iterator(); iter.hasNext();) {
			Map<String, Object> info = iter.next();
			System.err.println(info);
			System.out.print("{");
			for (Map.Entry<String, Object> col : info.entrySet()) {
				System.out.print(col.getKey() + ":" + col.getValue() + ", ");
			}
			System.out.println("}");
		}
		Assert.notEmpty(page.getContent());
	}

}
