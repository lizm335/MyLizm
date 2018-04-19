/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.organization.GjtChangeSpecialtyDao;
import com.gzedu.xlims.pojo.GjtChangeSpecialty;
import com.gzedu.xlims.service.organization.GjtChangeSpecialtyService;

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
public class GjtChangeSpecialtyTest {
	@Autowired
	GjtChangeSpecialtyService gjtChangeSpecialtyService;
	@Autowired
	GjtChangeSpecialtyDao gjtChangeSpecialtyDao;

	@Test
	public void queyALL() {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		Specification<GjtChangeSpecialty> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtChangeSpecialty.class);
		PageRequest pageable = new PageRequest(0, 15);
		Page<GjtChangeSpecialty> page = gjtChangeSpecialtyDao.findAll(spec, pageable);
		List<GjtChangeSpecialty> list = page.getContent();
		for (GjtChangeSpecialty gjtChangeSpecialty : list) {
			System.out.println(gjtChangeSpecialty.getNowGjtSpecialty().getZymc() + "----"
					+ gjtChangeSpecialty.getOldGjtSpecialty().getZymc());
		}
	}
}
