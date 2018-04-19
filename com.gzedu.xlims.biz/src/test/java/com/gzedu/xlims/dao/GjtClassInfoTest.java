
package com.gzedu.xlims.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.organization.GjtClassInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtGradeSpecialtyPlan;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyPlanService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtClassInfoTest {

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtClassInfoDao gjtClassInfoDao;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	GjtGradeSpecialtyPlanService gjtGradeSpecialtyPlanService;

	@Test
	@Transactional
	public void quey() {
		// 创建一个课程班级
		// 课程，学年度，院校

		// 获得一个教学计划
		List<GjtGradeSpecialtyPlan> specialtyPlans = gjtGradeSpecialtyPlanService
				.queryGradeSpecialtyPlan("e0878eaf9bde476c9faf8b726ed0ffd1", "0fee3de6ee574e298b61fba21fbe6166");

	}
}
