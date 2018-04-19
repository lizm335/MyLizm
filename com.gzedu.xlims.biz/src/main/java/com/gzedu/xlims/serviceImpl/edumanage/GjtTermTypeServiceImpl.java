package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.organization.GjtTermTypeDao;
import com.gzedu.xlims.pojo.GjtTermType;
import com.gzedu.xlims.service.edumanage.GjtTermTypeService;

/**
 * 
 * 功能说明：课程管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
@Service
public class GjtTermTypeServiceImpl implements GjtTermTypeService {
	@Autowired
	private GjtTermTypeDao gjtTermTypeDao;

	@Override
	public Page<GjtTermType> queryAll(final String schoolId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, schoolId));

		Specification<GjtTermType> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTermType.class);

		return gjtTermTypeDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtTermType queryBy(String id) {
		return gjtTermTypeDao.findOne(id);
	}

	@Override
	public void delete(Iterable<String> ids) {
		for (String id : ids) {
			gjtTermTypeDao.delete(id);
		}
	}

	@Override
	public void delete(String id) {
		gjtTermTypeDao.delete(id);
	}

	@Override
	public void insert(GjtTermType entity) {
		entity.setId(UUIDUtils.random());
		gjtTermTypeDao.save(entity);
	}

	@Override
	public void update(GjtTermType entity) {
		gjtTermTypeDao.save(entity);
	}

}
