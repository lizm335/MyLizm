/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.pageConfig;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.dao.feedback.LcmsMutualReplyDao;
import com.gzedu.xlims.dao.feedback.LcmsMutualSubjectDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualReply;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.dto.LcmsMutualSubjectDto;
import com.gzedu.xlims.serviceImpl.feedback.GjtFeedbackServiceImpl;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月12日
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-config.xml" })
public class FeedbackServiceTest {
	@Autowired
	private GjtFeedbackServiceImpl gjtFeedbackServiceImpl;

	@Autowired
	LcmsMutualSubjectDao lcmsMutualSubjectDao;

	@Autowired
	LcmsMutualReplyDao lcmsMutualReplyDao;

	@Test
	public void queryPageInfo() {
		PageRequest pageRequst = new PageRequest(1, 2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("createUserId", "");
		map.put("title", "测试");
	}

	@Test
	public void insert() {

		LcmsMutualSubjectDto item = new LcmsMutualSubjectDto();
		item.setClass_id("b02640fa4cd642e68acf8aadf9ab587f");// 2017春季行政管理（本）ZSB01班
		item.setCreate_account_id("f0cdbe1fdd834af9b3f53a8fbe7a240b");
		item.setCreated_dt(DateUtils.getNowTime());
		item.setForward_account_id("84513b924882411bba50107e16e354d6");// 班主任gkyinsaiqiong
		item.setInitial_account_id("84513b924882411bba50107e16e354d6");// 初始
		item.setIs_commend("N");
		item.setIsdeleted("N");
		item.setReply_count(new BigDecimal("0"));
		item.setRes_path(
				"http://172.16.170.119:801/ouchgzee_com/platform/xlbzr_css/dist/img/images/waiting-for-open.png,http://172.16.170.119:801/ouchgzee_com/platform/xllms_css/dist/img/user2-160x160.jpg");
		item.setStudentId("0d01564245084da5ad23f21eee4b6156");
		item.setSubject_content("2017年6月17日 17:34:24测试张内容内容了空间里看将刻录机");
		item.setSubject_id("12345678");
		item.setSubject_title("2017年6月17日 17:35:01标题");
		item.setUser_type("stud");
		item.setStudentId("0d01564245084da5ad23f21eee4b6156");// 兼容旧的userId字段和createBy字段，取studentId

	}

	@Test
	public void save() {
		LcmsMutualSubject item = new LcmsMutualSubject();
		item.setClassId("b02640fa4cd642e68acf8aadf9ab587f");
		item.setCreatedDt(DateUtils.getNowTime());
		item.setSubjectId("12345678");
		lcmsMutualSubjectDao.save(item);
	}

	@Test
	public void saves() {
		LcmsMutualReply entity = new LcmsMutualReply();
		GjtUserAccount user = new GjtUserAccount("f0cdbe1fdd834af9b3f53a8fbe7a240b");
		entity.setReplyId("1236987");
		entity.setUserId("0d01564245084da5ad23f21eee4b6156");// 兼容旧的userId字段，取studentId
		entity.setUserType("stud");
		entity.setCreatedDt(DateUtils.getNowTime());
		entity.setSubjectId("12345678");
		lcmsMutualReplyDao.save(entity);
	}

}
