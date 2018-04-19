/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.gjtSignup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.signup.GjtSignupDao;
import com.gzedu.xlims.pojo.GjtSignup;
import com.gzedu.xlims.service.CommonMapService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月24日
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtSignupServiceTest {
	@Autowired
	private GjtSignupDao gjtSignupDao;
	@Autowired
	private CommonMapService commonMapService;

	@Test
	public void ListGjtSignupTest() {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_charge", "1");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtSignup> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSignup.class);
		// Sort sort = new Sort(Sort.Direction.DESC, "g.created_dt");
		Sort sort = null;
		List<GjtSignup> result = gjtSignupDao.findAll(spec, sort);
		// Map<String, String> pyccMap = commonMapService.getPyccMap();
		for (GjtSignup gjtSignup : result) {
			System.out.println("pycc:" + commonMapService.getSpecialtyDates(gjtSignup.getSignupSpecialtyId())
					.get(gjtSignup.getSignupSpecialtyId()));
		}
	}
}
