/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.pageConfig;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.pageConfig.GjtPageConfigDao;
import com.gzedu.xlims.pojo.GjtPageDef;
import com.gzedu.xlims.service.pageConfig.GjtPageConfigService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月12日
 * @version 1.0
 *
 */
@Service
public class GjtPageConfigServiceImpl implements GjtPageConfigService {

	@Autowired
	GjtPageConfigDao gjtPageConfigDao;

	@Override
	public Page<GjtPageDef> queryPageList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtPageDef> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtPageDef.class);
		return gjtPageConfigDao.findAll(spec, pageRequst);
	}

	@Override
	public Boolean savePageConfig(GjtPageDef gjtPageDef) {
		GjtPageDef save = gjtPageConfigDao.save(gjtPageDef);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean updatePageConfig(GjtPageDef gjtPageDef) {
		GjtPageDef save = gjtPageConfigDao.save(gjtPageDef);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GjtPageDef queryById(String id) {
		GjtPageDef gjtPageDef = gjtPageConfigDao.findOne(id);
		return gjtPageDef;
	}

}
