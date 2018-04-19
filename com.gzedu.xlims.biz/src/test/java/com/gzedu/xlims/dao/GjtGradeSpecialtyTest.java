
package com.gzedu.xlims.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.organization.GjtSpecialtyDao;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtGradeSpecialtyTest {

	@Autowired
	GjtGradeSpecialtyService gjtGradeSpecialtyService;

	@Autowired
	GjtSpecialtyService specialtyService;

	@Autowired
	GjtSpecialtyDao specialtyDao;

	/**
	 * 模拟登录
	 */
	@org.junit.Test
	@Transactional
	public void simulationLogin() {
		PageRequest pageRequst = new PageRequest(0, 15);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Page<GjtGradeSpecialty> queryAll = gjtGradeSpecialtyService.queryAll("696c75a310b44a7f9dd61516ea84fbe3",
				searchParams, pageRequst);
		List<GjtGradeSpecialty> list = queryAll.getContent();
		for (GjtGradeSpecialty gjtGradeSpecialty : list) {
			System.err.println(
					gjtGradeSpecialty.getGjtGrade().getGradeName() + gjtGradeSpecialty.getGjtSpecialty().getZymc());
		}
		System.err.println(1111111);
	}

	@org.junit.Test
	@Transactional
	public void gradeSpecialty() {
		PageRequest pageRequst = new PageRequest(0, 15);
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("gjtGradeMap.gradeName", new SearchFilter("gjtGradeMap.gradeName", Operator.EQ, "bb"));
		filters.put("gjtSchoolInfo.id",
				new SearchFilter("gjtSchoolInfo.id", Operator.EQ, "696c75a310b44a7f9dd61516ea84fbe3"));
		Specification<GjtSpecialty> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSpecialty.class);
		List<GjtSpecialty> list = specialtyDao.findAll(spec);
		String greadeId = "e0878eaf9bde476c9faf8b726ed0ffd1";
		System.out.println("年级	专业名称");
		for (GjtSpecialty specialty : list) {
			// GjtGrade gjtGrade = specialty.getGjtGradeMap().get(greadeId);
			// System.err.print(gjtGrade.getGradeName() + "-");
			// System.err.print(specialty.getZymc());
			// System.err.println();
		}

		System.out.println(list.size());
	}
}
