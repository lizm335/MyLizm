/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.share;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.share.GjtShareDao;
import com.gzedu.xlims.pojo.GjtCourseOwnership;
import com.gzedu.xlims.service.share.GjtShareService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年12月28日
 * @version 1.0
 *
 */
@Service
public class GjtShareServiceImpl implements GjtShareService {
	@Autowired
	GjtShareDao gjtShareDao;

	@Override
	public Page<GjtCourseOwnership> queryPageList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtCourseOwnership> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtCourseOwnership.class);
		return gjtShareDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtCourseOwnership queryById(String id) {
		GjtCourseOwnership gjtArticle = gjtShareDao.findOne(id);
		return gjtArticle;
	}

	@Override
	public Boolean saveGjtShare(GjtCourseOwnership gjtCourseOwnership) {
		GjtCourseOwnership save = gjtShareDao.save(gjtCourseOwnership);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean updateGjtShare(GjtCourseOwnership gjtCourseOwnership) {
		GjtCourseOwnership save = gjtShareDao.save(gjtCourseOwnership);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void delete(String id) {
		gjtShareDao.delete(id);
	}

	@Override
	public List<GjtCourseOwnership> findByOrgCode(String orgCode) {
		return gjtShareDao.findByOrgCode(orgCode);
	}
	
	@Override
	public List<GjtCourseOwnership> findByOrgId(String orgId) {
		return gjtShareDao.findByOrgId(orgId);
	}

}
