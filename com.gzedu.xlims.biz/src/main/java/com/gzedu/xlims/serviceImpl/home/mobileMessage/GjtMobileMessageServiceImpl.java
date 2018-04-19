/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.mobileMessage;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.home.mobileMessage.GjtMobileMessageDao;
import com.gzedu.xlims.pojo.mobileMessage.GjtMobileMessage;
import com.gzedu.xlims.service.home.mobileMessage.GjtMobileMessageService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月16日
 * @version 3.0
 *
 */

@Service
public class GjtMobileMessageServiceImpl implements GjtMobileMessageService {

	@Autowired
	GjtMobileMessageDao gjtMobileMessageDao;

	@Override
	public Page<GjtMobileMessage> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMobileMessage> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMobileMessage.class);

		PageRequest pageRequest = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		return gjtMobileMessageDao.findAll(spec, pageRequest);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMobileMessage> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtMobileMessage.class);
		return gjtMobileMessageDao.count(spec);
	}

	@Override
	public GjtMobileMessage save(GjtMobileMessage entity) {
		return gjtMobileMessageDao.save(entity);
	}

	@Override
	public GjtMobileMessage queryById(String id) {
		return gjtMobileMessageDao.findOne(id);
	}

	@Override
	public GjtMobileMessage update(GjtMobileMessage entity) {
		return gjtMobileMessageDao.save(entity);
	}

}
