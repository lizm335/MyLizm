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
import com.gzedu.xlims.dao.home.GjtWorkOrderAssignPersonDao;
import com.gzedu.xlims.pojo.GjtWorkOrderAssignPerson;
import com.gzedu.xlims.service.home.GjtWorkOrderAssignPersonService;

/**
 * 
 * 功能说明：工单任务-指定任务人
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月21日
 * @version 2.5
 *
 */
@Service
public class GjtWorkOrderAssignPersonServiceImpl implements GjtWorkOrderAssignPersonService {

	@Autowired
	GjtWorkOrderAssignPersonDao gjtWorkOrderAssignPersonDao;

	@Override
	public Page<GjtWorkOrderAssignPerson> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		Specification<GjtWorkOrderAssignPerson> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtWorkOrderAssignPerson.class);
		return gjtWorkOrderAssignPersonDao.findAll(spec, page);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtWorkOrderAssignPerson> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtWorkOrderAssignPerson.class);
		return gjtWorkOrderAssignPersonDao.count(spec);
	}

	@Override
	public GjtWorkOrderAssignPerson save(GjtWorkOrderAssignPerson item) {
		return gjtWorkOrderAssignPersonDao.save(item);
	}

}
