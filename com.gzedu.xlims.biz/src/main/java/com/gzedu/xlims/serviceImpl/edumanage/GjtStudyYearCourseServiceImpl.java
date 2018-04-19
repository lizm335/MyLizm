/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.organization.GjtStudyYearCourseDao;
import com.gzedu.xlims.pojo.GjtStudyYearCourse;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearCourseService;

/**
 * 
 * 功能说明：学年度基础信息
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtStudyYearCourseServiceImpl implements GjtStudyYearCourseService {

	@Autowired
	CommonMapService commonMapService;

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager em;

	@Autowired
	GjtStudyYearCourseDao gjtStudyYearCourseDao;

	@Override
	public Page<GjtStudyYearCourse> queryAll(String orgId, Map<String, Object> map, PageRequest pageRequest) {
		Map<String, SearchFilter> filters = SearchFilter.parse(map);

		filters.put("studyYearInfo.xxId", new SearchFilter("studyYearInfo.xxId", Operator.EQ, orgId));

		Specification<GjtStudyYearCourse> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtStudyYearCourse.class);
		return gjtStudyYearCourseDao.findAll(spec, pageRequest);
	}

}
