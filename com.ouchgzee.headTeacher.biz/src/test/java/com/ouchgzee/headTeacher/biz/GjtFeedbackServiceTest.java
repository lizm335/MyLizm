package com.ouchgzee.headTeacher.biz;

import java.util.HashMap;
import java.util.Map;

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

import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dto.FeedbackSolvedDto;
import com.ouchgzee.headTeacher.dto.FeedbackUnsolvedDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeedback;
import com.ouchgzee.headTeacher.service.feedback.BzrGjtFeedbackService;

/**
 * 答问解疑 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtFeedbackServiceTest {

	private final String BZR_ID = "687ffde92b27491aa8bceeede0813425";

	@Autowired
	private BzrGjtFeedbackService gjtFeedbackService;

	/**
	 * 未解答的答疑列表页面
	 */
	@Test
	@Transactional(value="transactionManagerBzr", readOnly = true)
	public void queryUnsolvedFeedBack() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 已解答的条数
		System.err.println("已解答的条数:" + gjtFeedbackService.countSolvedFeedBack(map));
		// 未解答的条数
		System.err.println("未解答的条数:" + gjtFeedbackService.countUnsolvedFeedBack(map));

		Map<String, Object> searchParams = new HashMap();
		// searchParams.put("title", "");
		// searchParams.put("createdDtBegin", "2016-01-01");
		// searchParams.put("createdDtEnd", "2016-07-01");

		Page<FeedbackUnsolvedDto> page = gjtFeedbackService.queryUnsolvedFeedBackByBzrIdPage(searchParams,
				new PageRequest(0, 10));

		System.err.println(page.getNumberOfElements());
		System.err.println(page.getTotalElements());
		System.err.println(page.getTotalPages());

		System.err.println("ID\t标题\t学员姓名\t提出时间\t内容\t操作");
		for (FeedbackUnsolvedDto info : page.getContent()) {
			System.err.print(info.getId() + "\t");
			System.err.print(info.getTitle() + "\t");
			System.err.print(info.getStudentXm() + "\t");
			System.err.print(info.getCreatedDt() + "\t");
			System.err.print(info.getContent() + "\t");
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 已解答的答疑列表页面
	 */
	@Test
	@Transactional(value="transactionManagerBzr", readOnly = true)
	public void querySolvedFeedBack() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 已解答的条数
		System.err.println("已解答的条数:" + gjtFeedbackService.countSolvedFeedBack(map));
		// 未解答的条数
		System.err.println("未解答的条数:" + gjtFeedbackService.countUnsolvedFeedBack(map));

		Map<String, Object> searchParams = new HashMap();
		// searchParams.put("title", "");
		// searchParams.put("createdDtBegin", "2016-01-01");
		// searchParams.put("createdDtEnd", "2016-07-01");

		Page<FeedbackSolvedDto> page = gjtFeedbackService.querySolvedFeedBackByPage(searchParams,
				new PageRequest(0, 10, new Sort("t.created_dt")));

		System.err.println(page.getNumberOfElements());
		System.err.println(page.getTotalElements());
		System.err.println(page.getTotalPages());

		System.err.println("ID\t标题\t学员姓名\t提出时间\t内容\t回复时间\t回复内容\t操作");
		for (FeedbackSolvedDto info : page.getContent()) {
			System.err.print(info.getId() + "\t");
			System.err.print(info.getTitle() + "\t");
			System.err.print(info.getStudentXm() + "\t");
			System.err.print(info.getCreatedDt() + "\t");
			System.err.print(info.getContent() + "\t");
			System.err.print(info.getAnswerDealDt() + "\t");
			System.err.print(info.getAnswerEmployeeXm() + "老师答:" + info.getAnswerContent() + "\t");
			System.err.println();
		}
		Assert.notEmpty(page.getContent());
	}

	/**
	 * 回复
	 */
	@Test
	public void answer() {
		final String ID = "211f4abd654f48f281b866e7e99f4033";
		final String CONTENT = "good！棒棒哒！";

		BzrGjtFeedback feedback = new BzrGjtFeedback();
		feedback.setId(UUIDUtils.random());
		feedback.setPid(ID);
		feedback.setContent(CONTENT);
		feedback.setCreatedBy(BZR_ID);
		// feedback.setUserAgent(request.getHeader("User-Agent"));
		boolean flag = gjtFeedbackService.insert(feedback);
		Assert.isTrue(flag);
	}

	/**
	 * 转发辅导教师
	 */
	@Test
	public void shareTeacher() {
		boolean flag = gjtFeedbackService.shareTeacher("e204b9c30a164cd79e6ce9e2c047d226", BZR_ID, "");
		Assert.isTrue(flag);
	}

}
