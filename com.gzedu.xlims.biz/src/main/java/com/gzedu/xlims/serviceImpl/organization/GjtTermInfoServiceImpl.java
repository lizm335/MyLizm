/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
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
import com.gzedu.xlims.dao.organization.GjtTermInfoDao;
import com.gzedu.xlims.pojo.GjtTermInfo;
import com.gzedu.xlims.service.organization.GjtTermInfoService;

/**
 * 
 * 功能说明：学期管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月28日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtTermInfoServiceImpl implements GjtTermInfoService {

	@Autowired
	private GjtTermInfoDao gjtTermInfoDao;

	@Override
	public Boolean saveEntity(GjtTermInfo entity) {
		GjtTermInfo save = gjtTermInfoDao.save(entity);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean updateEntity(GjtTermInfo id) {
		GjtTermInfo save = gjtTermInfoDao.save(id);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GjtTermInfo queryById(String id) {
		return gjtTermInfoDao.findOne(id);
	}

	@Override
	public List<GjtTermInfo> queryAll() {
		return gjtTermInfoDao.findAll();
	}

	/**
	 * 假删除
	 */
	@Override
	public Boolean deleteById(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtTermInfoDao.deleteById(id, "Y");
			}
		}
		return true;
	}

	/**
	 * 真删除
	 */
	@Override
	public void delete(String id) {
		gjtTermInfoDao.delete(id);
	}

	@Override
	public Page<GjtTermInfo> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("gjtSchoolInfo.id", new SearchFilter("gjtSchoolInfo.id", Operator.EQ, orgId));
		Specification<GjtTermInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTermInfo.class);
		return gjtTermInfoDao.findAll(spec, pageRequst);
	}
}
