/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.home;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.home.GjtWorkOrderDao;
import com.gzedu.xlims.pojo.GjtWorkOrder;
import com.gzedu.xlims.service.home.GjtWorkOrderService;

/**
 * 
 * 功能说明：工单管理
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月21日
 * @version 2.5
 *
 */
@Service
public class GjtWorkOrderServiceImpl implements GjtWorkOrderService {

	@Autowired
	GjtWorkOrderDao gjtWorkOrderDao;

	@Override
	public Page<GjtWorkOrder> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		Specification<GjtWorkOrder> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtWorkOrder.class);
		return gjtWorkOrderDao.findAll(spec, page);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtWorkOrder> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtWorkOrder.class);
		return gjtWorkOrderDao.count(spec);
	}

	@Override
	public GjtWorkOrder queryById(String id) {
		return gjtWorkOrderDao.findOne(id);
	}

	@Override
	public GjtWorkOrder save(GjtWorkOrder item) {
		return gjtWorkOrderDao.save(item);
	}

	@Override
	public GjtWorkOrder update(GjtWorkOrder item) {
		return gjtWorkOrderDao.save(item);
	}

	@Override
	public boolean delete(String id) {
		int i = gjtWorkOrderDao.updateIsDelete(id);
		return i == 1 ? true : false;
	}

	@Override
	public boolean updateIsState(String id, String isState) {
		int i = gjtWorkOrderDao.updateIsState(id, isState);
		return i == 1 ? true : false;
	}

}
