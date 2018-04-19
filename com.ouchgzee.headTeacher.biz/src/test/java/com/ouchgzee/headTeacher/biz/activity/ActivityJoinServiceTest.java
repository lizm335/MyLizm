/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.biz.activity;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ouchgzee.headTeacher.dao.activity.GjtActivityJoinDao;
import com.ouchgzee.headTeacher.daoImpl.BzrGjtActivityJoinDaoImpl;
import com.ouchgzee.headTeacher.dto.ActivityJoinDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtActivityJoin;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityJoinService;

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
public class ActivityJoinServiceTest {
	@Autowired
	private BzrGjtActivityJoinDaoImpl gjtActivityJoinImpl;
	@Autowired
	private GjtActivityJoinDao gjtActivityJoinDao;
	@Autowired
	private BzrGjtActivityJoinService gjtActivityJoinService;

	public void auditTest() {
		gjtActivityJoinImpl
				.batchAuditActivity("d90bb3204d9411e6e61d89ef7308d8c1", "0");
	}

	public void applyNumTest2() {
		System.out.println(gjtActivityJoinService
				.countApplyNum("d90bb3204d9411e6e61d89ef7308d8c1", "0"));
	}

	public void applyNumTest() {
		Specification<BzrGjtActivityJoin> spec = new Specification<BzrGjtActivityJoin>() {

			@Override
			public Predicate toPredicate(Root<BzrGjtActivityJoin> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				expressions.add(cb.equal(root.get("auditStatus"), "0"));
				expressions.add(cb.equal(root.get("id").get("activityId"),
						"d90bb3204d9411e6e61d89ef7308d8c1"));
				return predicate;
			}

		};
		System.out.println(gjtActivityJoinDao.count(spec));
	}

	@Test
	public void getActivityStudentsInfo() {
		List<ActivityJoinDto> list = gjtActivityJoinImpl
				.getActivityStudentsInfo("d90bb3204d9411e6e61d89ef7308d8c1",
						"1");
		for (ActivityJoinDto activityJoinDto : list) {
			System.out.println(activityJoinDto.getSex());
		}
	}
}
