/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.dao.recruitmanage.GjtEnrollBatchDao;
import com.gzedu.xlims.pojo.GjtEnrollBatch;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.service.organization.GjtGradeService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月6日
 * @version 2.5
 * @since JDK1.7
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtEnrollBatchTest {
	@Autowired
	GjtEnrollBatchDao gjtEnrollBatchDao;

	@Autowired
	GjtGradeService gjtGradeService;

	//
	// @Test
	// public void queyALL() {
	// Map<String, SearchFilter> filters = new HashMap();
	// Specification<GjtChangeSpecialty> spec =
	// DynamicSpecifications.bySearchFilter(filters.values(),
	// GjtChangeSpecialty.class);
	// PageRequest pageable = new PageRequest(0, 15);
	// Page<GjtChangeSpecialty> page = gjtChangeSpecialtyDao.findAll(spec,
	// pageable);
	// List<GjtChangeSpecialty> list = page.getContent();
	// for (GjtChangeSpecialty gjtChangeSpecialty : list) {
	// System.out.println(gjtChangeSpecialty.getNowGjtSpecialty().getZymc() +
	// "----"
	// + gjtChangeSpecialty.getOldGjtSpecialty().getZymc());
	// }
	// }
	@Test
	public void queyALL() {
		GjtGrade gg = gjtGradeService.queryById("144711aac94a474bad19b1cd3fd039f8");
		List<GjtEnrollBatch> list = gjtEnrollBatchDao.findByGjtGrade(gg);
		System.err.println(gg.getGradeName());
		for (GjtEnrollBatch gjtEnrollBatch : list) {
			System.err.println(gjtEnrollBatch.getBatchName());
		}
	}
}
