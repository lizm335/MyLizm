/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.dao.organization.GjtYearDao;
import com.gzedu.xlims.pojo.GjtYear;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtYearService;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月7日
 * @version 3.0
 *
 */
@Service
public class GjtYearServiceImpl implements GjtYearService {

	@Autowired
	private GjtYearDao gjtYearDao;

	@Autowired
	private CommonMapService commonMapService;

	@Override
	public Page<GjtYear> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Map<String, String> map = commonMapService.getOrgMapByOrgId(orgId);
		filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.IN, map.keySet()));
		filters.put("isEnabled", new SearchFilter("isEnabled", Operator.EQ, 1));
		Specification<GjtYear> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtYear.class);
		return gjtYearDao.findAll(spec, pageRequest);
	}

	@Override
	public GjtYear queryById(String id) {
		return gjtYearDao.findOne(id);
	}

	@Override
	public void save(GjtYear gjtYear) {
		gjtYearDao.save(gjtYear);
	}

	@Override
	public void updateGjtYear(GjtYear gjtYear) {
		GjtYear item = gjtYearDao.findOne(gjtYear.getGradeId());
		item.setName(gjtYear.getName());
		item.setUpdatedBy(gjtYear.getUpdatedBy());
		item.setUpdatedDt(DateUtil.getDate());
		gjtYearDao.save(item);
	}

	@Override
	public List<GjtYear> findByOrgId(String orgId) {

		return gjtYearDao.findByOrgIdOrderByStartYearDesc(orgId);
	}

	@Override
	public List<GjtYear> findByOrgIdAndIsEnabled(String orgId, int isEnable) {
		return gjtYearDao.findByOrgIdAndIsEnabledOrderByStartYearAsc(orgId, isEnable);
	}

	@Override
	public List<GjtYear> findByExistsEnableGrade(String orgId) {
		return gjtYearDao.findByExistsEnableGrade(orgId);
	}

	@Override
	public GjtYear findOne(String gradeId) {
		return gjtYearDao.findOne(gradeId);
	}

}
