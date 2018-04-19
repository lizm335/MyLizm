/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz;

import com.ouchgzee.headTeacher.dto.CountLoginDto;
import com.ouchgzee.headTeacher.service.BzrCacheService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 班级首页
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月23日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class ClassIndexTest {

	private final String STUDENT_ID = "799db19acc0b4d1f885466c227952872";

	private final String CLASS_ID = "59e3a89436a4449baa3e77a3c1f1eeb6";

	@Autowired
	private BzrGjtStudentService gjtStudentService;

	@Autowired
	private BzrCacheService cacheService;

	/**
	 * 班级学员考勤情况
	 */
	@Test
	public void countStudentClockingInSituationByClass() {
		Map clockingInSituation = gjtStudentService.countStudentClockingInSituationByClass(CLASS_ID, null);

		System.err.println(clockingInSituation);

		Assert.notEmpty(clockingInSituation);
	}

	/**
	 * 班级学员两周的考勤统计
	 */
	@Test
	public void countTwoWeeksClockInSituation() {
		List<CountLoginDto> loginSituation = gjtStudentService.countTwoWeeksClockInSituation(CLASS_ID);

		for(CountLoginDto info : loginSituation) {
			System.err.println(info.getDayName() + "\t" + info.getStudentLoginNum());
		}

		Assert.notEmpty(loginSituation);
	}

	/**
	 * 班级学员当年的考勤统计
	 */
	@Test
	public void countThisYearClockInSituation() {
		List<CountLoginDto> loginSituation = gjtStudentService.countThisYearClockInSituation(CLASS_ID);

		for(CountLoginDto info : loginSituation) {
			System.err.println(info.getDayName() + "\t" + info.getStudentLoginNum());
		}

		Assert.notEmpty(loginSituation);
	}

}
