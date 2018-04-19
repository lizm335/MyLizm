/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.biz.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ouchgzee.headTeacher.daoImpl.BzrGjtActivityCommentDaoImpl;
import com.ouchgzee.headTeacher.dto.ActivityCommentDto;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityCommentService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月20日
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class ActivityCommentServiceTest {
	@Autowired
	private BzrGjtActivityCommentDaoImpl gjtActivityCommentImpl;
	@Autowired
	private BzrGjtActivityCommentService gjtActivityCommentService;

	@Test
	public void countComentsNum() {
		System.out.println(gjtActivityCommentService.countComentsNum("d90bb3204d9411e6e61d89ef7308d8c1"));
	}

	public void getActivityStudentsInfo() {
		PageRequest pageRequest = new PageRequest(0, 15);
		Page<ActivityCommentDto> list = gjtActivityCommentImpl
				.getActivityCommentInfo("d90bb3204d9411e6e61d89ef7308d8c1", pageRequest);
		for (ActivityCommentDto activityCommentDto : list) {
			System.out.println(activityCommentDto.getAvatar());
		}
	}

}
