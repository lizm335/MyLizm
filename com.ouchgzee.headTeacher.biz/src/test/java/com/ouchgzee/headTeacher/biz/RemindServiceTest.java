/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ouchgzee.headTeacher.dao.student.GjtStudentInfoDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageService;

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
public class RemindServiceTest {

	private final String STUDENT_ID = "c1a44be464504d9eb7403300c51f0068";

	private final String CLASS_ID = "ba91eb1a11154f1d8843cbf957eff4fc";

	@Autowired
	private BzrGjtMessageService gjtMessageService;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Test
	public void findManyStudentInfoByIds() {
		List<String> ids = new ArrayList();
		{
			ids.add("4e7ed08a7f00000122d555b2950dd47a");
			ids.add("4e7ed02a7f00000122d555b2d5bcb2da");
			ids.add("4e7ed02e7f00000122d555b20a63ce40");
		}
		List<Object[]> manyStudentInfoByIds = gjtStudentInfoDao.findManyStudentInfoByIds(ids);
		Assert.notEmpty(manyStudentInfoByIds);

		for (Object[] info : manyStudentInfoByIds) {
			System.err.print(info[0] + "\t");
			System.err.print(info[1] + "\t");
			System.err.print(info[2] + "\t");
			System.err.print(info[3] + "\t");
			System.err.println();
		}
	}

	/**
	 * EE提醒
	 */
	@Test
	@Transactional(value="transactionManagerBzr", readOnly = true)
	public void queryRemindMessageInfoByPage() {
		Map<String, Object> searchParams = new HashMap();
		searchParams.put("EQ_infoTool", "3"); // 发送工具 1-站内、2-邮件、3-短信、4-EE
												// 具体查看数据字典InfoTool
		searchParams.put("EQ_gjtUserAccount.id", "4b74b570119e40cf8b9e9e2c9afa1a32");

		Page<BzrGjtMessageInfo> page = gjtMessageService.queryRemindMessageInfoByPage(searchParams,
				new PageRequest(0, 10));

		System.err.println(page.getNumberOfElements());
		System.err.println(page.getTotalElements());
		System.err.println(page.getTotalPages());

		System.err.println("ID\t标题\t内容\t发送时间\t接收人\t操作");
		for (BzrGjtMessageInfo info : page.getContent()) {
			System.err.print(info.getMessageId() + "\t");
			System.err.print(info.getInfoContent() + "\t");
			System.err.print(info.getCreatedDt() + "\t");
			System.err.print(info.getMemo() + "\t");
			System.err.println();
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 新增提醒
	 */
	@Test
	public void insertRemind() {
		BzrGjtMessageInfo messageInfo = new BzrGjtMessageInfo();
		List<String> ids = new ArrayList();
		{
			ids.add("4e7ed08a7f00000122d555b2950dd47a");
			ids.add("4e7ed02a7f00000122d555b2d5bcb2da");
			ids.add("4e7ed02e7f00000122d555b20a63ce40");
		}
		messageInfo.setInfoContent("同学们好，上扣扣面试！");
		messageInfo.setGetUser(StringUtils.join(ids, ','));

		messageInfo.setGjtUserAccount(new BzrGjtUserAccount("4b74b570119e40cf8b9e9e2c9afa1a32"));
		messageInfo.setXxId("xxId");
		messageInfo.setCreatedBy("4b74b570119e40cf8b9e9e2c9afa1a32");
		boolean flag = gjtMessageService.insertRemind(messageInfo, "orgCode", true, true);

		Assert.isTrue(flag);
	}

}
