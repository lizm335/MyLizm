/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz;

import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dao.invoice.GjtSignupFeeDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtBillApplyRecord;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeeInvoice;
import com.ouchgzee.headTeacher.pojo.BzrGjtSignupFee;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.status.IssueStatus;
import com.ouchgzee.headTeacher.service.invoice.BzrGjtInvoiceService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtInvoiceServiceTest2 {

	private final String CLASS_ID = "90580fd73cd2482e8abe96a0e0bf8a59";

	private final String studentId = "4e7ed08a7f00000122d555b2950dd47a";

	private final String invoiceId = "c674f9f8d44ca8e86b78aff4c9953456";

	@Autowired
	private GjtSignupFeeDao gjtSignupFeeDao;

	@Autowired
	private BzrGjtInvoiceService gjtInvoiceService;

	@Autowired
	private BzrGjtStudentService gjtStudentService;

	@Test
	public void sadd() {
		BzrGjtSignupFee gjtSignupFee = gjtSignupFeeDao.findOne("e27bac1d2f9ea009ee5a15c07585b7d7");

		BzrGjtBillApplyRecord gjtBillApplyRecord = new BzrGjtBillApplyRecord();
		gjtBillApplyRecord.setId(UUIDUtils.random());
		BzrGjtStudentInfo gjtStudentInfo = gjtStudentService.queryById(studentId);
		gjtBillApplyRecord.setGjtStudentInfo(gjtStudentInfo);
		gjtBillApplyRecord.setTitle("2016年学费");

		String invoiceCode = "112233445566";
		BzrGjtFeeInvoice feeInvoice = new BzrGjtFeeInvoice();
		feeInvoice.setInvoiceId(UUIDUtils.random());
		feeInvoice.setInvoiceType("1");
		feeInvoice.setInvoiceFee(gjtSignupFee.getFeeNum());
		feeInvoice.setInvoiceNum("20160505001");
		feeInvoice.setInvoiceCode(invoiceCode);
		feeInvoice.setReceiveWay("广州远程教育中心");
		feeInvoice.setPayWay("欧阳图书");
		feeInvoice.setGjtSignupFee(gjtSignupFee);
		feeInvoice.setInvoiceSerial("312628128");
		feeInvoice.setXxzxId("adba13982e654eb7813d746387ccd20e");
		feeInvoice.setXxId("14eb762198734d4983b5b1865ef2c899");
		feeInvoice.setMemo(null);
		feeInvoice.setCreatedBy("4e1718d37f00000122d555b2d2a2682d");
		feeInvoice.setGjtBillApplyRecord(gjtBillApplyRecord);
		feeInvoice.setIssueStatus(IssueStatus.WAIT.getValue() + "");
		feeInvoice.setIssueDt(null);

		// 交费费用信息同步更新
		gjtSignupFee.setInvoiceNo(invoiceCode);

		boolean flag = gjtInvoiceService.insert(feeInvoice);
		Assert.isTrue(flag);
	}

	@Test
	public void updateInvoiceIssue() {
		boolean flag = gjtInvoiceService.updateInvoiceIssue(invoiceId, "abc");
		Assert.isTrue(flag);
	}

	@Test
	public void queryById() {
		BzrGjtSignupFee gjtSignupFee = gjtSignupFeeDao.findOne("e27bac1d2f9ea009ee5a15c07585b7d7");
		Assert.notNull(gjtSignupFee, "没有找到对象！");

		BzrGjtFeeInvoice gjtFeeInvoice = gjtInvoiceService.queryById(invoiceId);
		Assert.notNull(gjtFeeInvoice);
	}

	/**
	 * 发票信息列表页面
	 */
	@Test
	public void queryFeeInvoice() {
		Map<String, Object> searchParams = new HashMap();
		searchParams.put("EQ_issueStatus", IssueStatus.WAIT.getValue());
		searchParams.put("LIKE_gjtBillApplyRecord.gjtStudentInfo.xm", "李");
		searchParams.put("EQ_gjtBillApplyRecord.gjtStudentInfo.gjtUserAccount.loginAccount", "118460213012137");

		Page<BzrGjtFeeInvoice> page = gjtInvoiceService.queryFeeInvoiceByClassIdPage(CLASS_ID, searchParams, new PageRequest(0, 10));

		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		for (BzrGjtFeeInvoice info : page.getContent()) {
			System.out.println((info.getGjtSignupFee() == null ? "NULL" : info.getGjtSignupFee().getFeeOrderNo()) + "\t"
					+ info.getInvoiceFee() + "元\t" + info.getGjtBillApplyRecord().getTitle());
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 导入发票信息功能
	 */
	@Test
	public void importInfo() {
		String url = "C:\\Users\\Administrator\\Desktop\\FeeInvoice.xls";
		Map<String, Object> result = gjtInvoiceService.updateBatchImportFeeInvoice(url,
				"4e1718d37f00000122d555b2d2a2682d");
		System.out.println("success:" + ((List<BzrGjtFeeInvoice>) result.get("succList")).size());
		System.out.println("fail:" + ((List<BzrGjtFeeInvoice>) result.get("failList")).size());

		Assert.isTrue(((List<BzrGjtFeeInvoice>) result.get("failList")).size() == 0);
	}

	/**
	 * 导出发票信息功能
	 */
	@Test
	public void exportInfo() {
		String outputUrl = "C:\\Users\\Administrator\\Desktop\\FeeInvoiceInfo_20160524.xls";

		Map<String, Object> searchParams = new HashMap();
		searchParams.put("EQ_issueStatus", IssueStatus.WAIT.getValue());
		searchParams.put("LIKE_gjtBillApplyRecord.gjtStudentInfo.xm", "李");
		searchParams.put("EQ_gjtBillApplyRecord.gjtStudentInfo.gjtUserAccount.loginAccount", "118460213012137");

		HSSFWorkbook workbook = gjtInvoiceService.exportFeeInvoiceToExcel(CLASS_ID, searchParams, null);
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

}
