/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dao.message.GjtMessageBoxDao;
import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageService;

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
public class GjtMessageServiceTest2 {

	@Autowired
	private BzrGjtMessageService gjtMessageService;

	@Autowired
	private GjtMessageBoxDao gjtMessageBoxDao;

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Test
	public void queryByPage() {
		BzrGjtMessageInfo messageInfo = new BzrGjtMessageInfo();
		messageInfo.setXxId("2f5bfcce71fa462b8e1f65bcd0f4c632");

		Page<BzrGjtMessageInfo> page = gjtMessageService.queryHeadTeacherMessageInfoByPage(messageInfo,
				new PageRequest(0, 10));

		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		for (BzrGjtMessageInfo info : page.getContent()) {
			System.out.println(info.getInfoTheme() + "\t||||||||||||||"
					+ (info.getGjtUserAccount() == null ? null : info.getGjtUserAccount().getRealName()));
		}
		Assert.notEmpty(page.getContent());
	}

	@Test
	public void queryByClassPage() {
		BzrGjtMessageInfo messageInfo = new BzrGjtMessageInfo();
		messageInfo.setXxId("2f5bfcce71fa462b8e1f65bcd0f4c632");
		messageInfo.setInfoTheme("");
		messageInfo.setInfoType(null);
		messageInfo.setCondCreatedDtBegin("2013-01-01");
		messageInfo.setCondCreatedDtEnd("2016-06-01");

		Page<BzrGjtMessageInfo> page = gjtMessageService.queryClassMessageInfoByPage(messageInfo,
				"308cd259ccca4fa5a9f42a6f88a5d66b", null, new PageRequest(0, 10));

		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		System.out.println("MessageId\t\t\t\t接收总人数\t阅读人数\t\t发送人\t标题");
		for (BzrGjtMessageInfo info : page.getContent()) {
			System.out.println(info.getMessageId() + "\t" + info.getColReceiveUserNum() + "\t\t"
					+ info.getColReadUserNum() + "\t\t"
					+ (info.getGjtUserAccount() == null ? null : info.getGjtUserAccount().getRealName()) + "\t"
					+ info.getInfoTheme());
		}
		Assert.notEmpty(page.getContent());
	}

	@Test
	public void addMessageBjgg() {
		// 班级公告
		BzrGjtMessageInfo messageInfo = new BzrGjtMessageInfo();
		messageInfo.setMessageId(UUIDUtils.random());
		messageInfo.setInfoType("11");
		messageInfo.setInfoTool("1");
		messageInfo.setInfoTheme("六一儿童节快到了");
		messageInfo.setInfoContent("<div class='BlogContent'><p>Oracle 获取当前系统时间的几种方式:</p> "
				+ "<div><div class='syntaxhighlighter  java' id='highlighter_734214'><div class='toolbar'><span><a class='toolbar_item command_help help' href='#'>?</a></span></div><table cellspacing='0' cellpadding='0' border='0'><tbody><tr><td class='gutter'><div class='line number1 index0 alt2'>1</div><div class='line number2 index1 alt1'>2</div><div class='line number3 index2 alt2'>3</div><div class='line number4 index3 alt1'>4</div><div class='line number5 index4 alt2'>5</div><div class='line number6 index5 alt1'>6</div><div class='line number7 index6 alt2'>7</div><div class='line number8 index7 alt1'>8</div><div class='line number9 index8 alt2'>9</div><div class='line number10 index9 alt1'>10</div><div class='line number11 index10 alt2'>11</div><div class='line number12 index11 alt1'>12</div></td><td class='code'><div class='container'><div class='line number1 index0 alt2'><code class='java comments'>/*</code></div><div class='line number2 index1 alt1'><code class='java comments'>Oracle使用计算机操作系统的当前日期和时间。</code></div><div class='line number3 index2 alt2'><code class='java comments'>SYSDATE：可将Sysdate视为一个其结果为当前日期和时间的函数，在任何可以使用Oracle函数的地方都可以使用Sysdate。也可以将它视为每个表的一个隐藏的列或伪列。*/</code></div><div class='line number4 index3 alt1'><code class='java plain'>select&nbsp;sysdate&nbsp;from&nbsp;dual;--使用最多的</code></div><div class='line number5 index4 alt2'><code class='java comments'>/*</code></div><div class='line number6 index5 alt1'><code class='java comments'>CURRENT_DATE：报告会话的时区中的系统日期。注：可以设置自己的时区，以区别于数据库的时区。</code></div><div class='line number7 index6 alt2'><code class='java comments'>*/</code></div><div class='line number8 index7 alt1'><code class='java plain'>select&nbsp;current_date&nbsp;from&nbsp;dual;</code></div><div class='line number9 index8 alt2'><code class='java comments'>/*</code></div><div class='line number10 index9 alt1'><code class='java comments'>SYSTIMESTAMP：报告TIMESTAMP数据类型格式的系统日期。</code></div><div class='line number11 index10 alt2'><code class='java comments'>*/</code></div><div class='line number12 index11 alt1'><code class='java plain'>select&nbsp;SYSTIMESTAMP&nbsp;&nbsp;from&nbsp;dual;</code></div></div></td></tr></tbody></table></div></div> "
				+ "<p><br></p></div>");
		BzrGjtUserAccount gjtUserAccount = gjtUserAccountDao.findOne("c20a50cef1824bc38aaeff6d5b0c6d1c");
		messageInfo.setGjtUserAccount(gjtUserAccount);
		messageInfo.setXxId("2f5bfcce71fa462b8e1f65bcd0f4c632");
		messageInfo.setGetUserRole("1");
		messageInfo.setGetUserMethod("2");
		messageInfo.setGetUser("308cd259ccca4fa5a9f42a6f88a5d66b");
		messageInfo.setCreatedBy(messageInfo.getGjtUserAccount().getId());
		boolean flag = gjtMessageService.insert(messageInfo);

		Assert.isTrue(flag);
	}

	@Test
	public void addMessageKstz() {
		// 考试通知
		BzrGjtMessageInfo messageInfo = new BzrGjtMessageInfo();
		messageInfo.setMessageId(UUIDUtils.random());
		messageInfo.setInfoType("12");
		messageInfo.setInfoTool("1");
		messageInfo.setInfoTheme("明天上午模拟考试");
		messageInfo.setInfoContent("<div class='BlogContent'><p>Oracle 获取当前系统时间的几种方式:</p> "
				+ "<div><div class='syntaxhighlighter  java' id='highlighter_734214'><div class='toolbar'><span><a class='toolbar_item command_help help' href='#'>?</a></span></div><table cellspacing='0' cellpadding='0' border='0'><tbody><tr><td class='gutter'><div class='line number1 index0 alt2'>1</div><div class='line number2 index1 alt1'>2</div><div class='line number3 index2 alt2'>3</div><div class='line number4 index3 alt1'>4</div><div class='line number5 index4 alt2'>5</div><div class='line number6 index5 alt1'>6</div><div class='line number7 index6 alt2'>7</div><div class='line number8 index7 alt1'>8</div><div class='line number9 index8 alt2'>9</div><div class='line number10 index9 alt1'>10</div><div class='line number11 index10 alt2'>11</div><div class='line number12 index11 alt1'>12</div></td><td class='code'><div class='container'><div class='line number1 index0 alt2'><code class='java comments'>/*</code></div><div class='line number2 index1 alt1'><code class='java comments'>Oracle使用计算机操作系统的当前日期和时间。</code></div><div class='line number3 index2 alt2'><code class='java comments'>SYSDATE：可将Sysdate视为一个其结果为当前日期和时间的函数，在任何可以使用Oracle函数的地方都可以使用Sysdate。也可以将它视为每个表的一个隐藏的列或伪列。*/</code></div><div class='line number4 index3 alt1'><code class='java plain'>select&nbsp;sysdate&nbsp;from&nbsp;dual;--使用最多的</code></div><div class='line number5 index4 alt2'><code class='java comments'>/*</code></div><div class='line number6 index5 alt1'><code class='java comments'>CURRENT_DATE：报告会话的时区中的系统日期。注：可以设置自己的时区，以区别于数据库的时区。</code></div><div class='line number7 index6 alt2'><code class='java comments'>*/</code></div><div class='line number8 index7 alt1'><code class='java plain'>select&nbsp;current_date&nbsp;from&nbsp;dual;</code></div><div class='line number9 index8 alt2'><code class='java comments'>/*</code></div><div class='line number10 index9 alt1'><code class='java comments'>SYSTIMESTAMP：报告TIMESTAMP数据类型格式的系统日期。</code></div><div class='line number11 index10 alt2'><code class='java comments'>*/</code></div><div class='line number12 index11 alt1'><code class='java plain'>select&nbsp;SYSTIMESTAMP&nbsp;&nbsp;from&nbsp;dual;</code></div></div></td></tr></tbody></table></div></div> "
				+ "<p><br></p></div>");
		BzrGjtUserAccount gjtUserAccount = gjtUserAccountDao.findOne("c20a50cef1824bc38aaeff6d5b0c6d1c");
		messageInfo.setGjtUserAccount(gjtUserAccount);
		messageInfo.setXxId("2f5bfcce71fa462b8e1f65bcd0f4c632");
		messageInfo.setGetUserRole("1");
		messageInfo.setGetUserMethod("1");
		messageInfo.setGetUser("969300");
		messageInfo.setCreatedBy(messageInfo.getGjtUserAccount().getId());
		boolean flag = gjtMessageService.insert(messageInfo);

		Assert.isTrue(flag);
	}

	@Test
	public void updateRead() {
		boolean flag = gjtMessageService.updateMessageRead("ad32a972a4481c717eb3d39f9cc8bfc8", "969300");
		Assert.isTrue(flag);
	}

	@Test
	public void delete() {
		boolean flag = gjtMessageService.delete("ad32a972a4481c717eb3d39f9cc8bfc8", "4e1718d37f00000122d555b2d2a2682d");
		Assert.isTrue(flag);
	}

	@Test
	public void queryObjects() {
		Object[] counts = gjtMessageBoxDao.countReadSituation("2d6c241b970b4809a9b10d348b0c1a49");
		Long count1 = (Long) counts[0];
		Long count2 = (Long) counts[1];
		System.out.println("接收总人数：" + count1 + "\t阅读人数：" + count2);

		List<String> messageIdList = new ArrayList<String>();
		messageIdList.add("2d6c241b970b4809a9b10d348b0c1a49");
		messageIdList.add("ad32a972a4481c717eb3d39f9cc8bfc8");

		List<Object[]> list = gjtMessageBoxDao.countReadSituations(messageIdList);
		for (Object[] objects : list) {
			Long c1 = (Long) objects[1];
			Long c2 = (Long) objects[2];
			System.out.println("MessageId：" + objects[0] + "\t接收总人数：" + c1 + "\t阅读人数：" + c2);
		}
		Assert.notEmpty(messageIdList);
	}

}
