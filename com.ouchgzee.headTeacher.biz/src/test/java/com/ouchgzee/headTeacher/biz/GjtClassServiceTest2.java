/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.gzedu.xlims.common.Constants;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtGrade;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtClassServiceTest2 {

	@Autowired
	private BzrGjtClassService gjtClassService;

	@Autowired
	private BzrGjtStudentService gjtStudentService;

	@Test
	public void queryByClassPage() {
		List<BzrGjtGrade> gradeList = gjtStudentService.queryGradeBy("2f5bfcce71fa462b8e1f65bcd0f4c632");

		Map<String, Object> searchParams = new HashMap();
		// 班级状态 1-开启/0-关闭
		searchParams.put("EQ_isEnabled", Constants.BOOLEAN_1);
		// searchParams.put("LIKE_bjmc", "行政管理班");
		// searchParams.put("EQ_gjtGrade.gradeId",
		// "a3347e80d84c45afbb34260b978484d3");
		Page<BzrGjtClassInfo> page = gjtClassService.queryClassInfoByPage("a3dbdbe68e7f4a56bd0071aece07b0db", searchParams,
				new PageRequest(0, 10));

		for (BzrGjtGrade g : gradeList) {
			System.out.println(g.getGradeId() + "\t" + g.getXxId() + "\t" + g.getGradeName());
		}
		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
		System.out.println("ClassId\t班级人数\t班级名称\t年级");
		for (BzrGjtClassInfo info : page.getContent()) {
			System.out.println(info.getClassId() + "\t" + info.getColStudentNum() + "/" + info.getBjrn() + "\t"
					+ info.getBjmc() + "\t" + (info.getGjtGrade() == null ? null : info.getGjtGrade().getGradeName()));
		}
		Assert.notEmpty(page.getContent());
	}

	@Test
	public void updateCloseClass() {
		boolean flag = gjtClassService.updateCloseClass("308cd259ccca4fa5a9f42a6f88a5d66b",
				"4e1718d37f00000122d555b2d2a2682d");

		Assert.isTrue(flag);
	}

	@Test
	public void updateReopenClass() {
		boolean flag = gjtClassService.updateReopenClass("308cd259ccca4fa5a9f42a6f88a5d66b",
				"4e1718d37f00000122d555b2d2a2682d");

		Assert.isTrue(flag);
	}

	@Test
	public void getXueQing() {
		List<String> ids = new ArrayList<String>();
		ids.add("2694be635a1d4255a161b34351311277");
		ids.add("6324a5b33f9f4d038ef2a6588d455608");
		ids.add("02d242458d014377bf3f1505f98b3207");
		List<Object[]> list = gjtClassService.getStudentSpecialty("c9a5baca5b9c4bb7a23c9c1e7ac62a98", ids);
		for (Object[] objects : list) {
			System.out.println("第一：" + objects[0] + "第二：" + objects[1] + "第三：" + objects[2]);
		}
	}

	@Test
	public void getBanJi() {
		List<String> list = gjtClassService.queryTeacherClassStudent("0bf9e5e3e1e74196a723f785eed1fc2f");
		for (String objects : list) {
			System.out.println("第一：" + objects);
		}
	}

	public static void main(String[] args) {
		BigDecimal b1 = new BigDecimal(100);
		BigDecimal b2 = new BigDecimal(200);

		System.out.println(b1.subtract(b2).intValue());
	}
}
