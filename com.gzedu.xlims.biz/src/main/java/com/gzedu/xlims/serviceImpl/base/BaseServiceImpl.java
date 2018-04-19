/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.base;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 对象业务基础接口类<br>
 * 功能说明：实现基础的业务逻辑，如果不满足当前的业务，可重写方法或者新增方法
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年05月28日
 * @version 2.5
 * @since JDK 1.7
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	/**
	 * 获取基础数据库操作类
	 * @return
	 */
	protected abstract BaseDao<T, String> getBaseDao();

	@Override
	public Page<T> queryByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<T> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return getBaseDao().findAll(spec, pageRequest);
	}

	@Override
	public List<T> queryBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<T> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return getBaseDao().findAll(spec, sort);
	}

	@Override
	public T queryById(String id) {
		return getBaseDao().findOne(id);
	}

	@Override
	public Iterable<T> queryByIds(Iterable<String> ids) {
		return getBaseDao().findAll(ids);
	}

	@Override
	public long count(Map<String, Object> searchParams) {
		Criteria<T> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return getBaseDao().count(spec);
	}

	@Override
	public boolean insert(T entity) {
		T info = getBaseDao().save(entity);
		return info != null;
	}

	@Override
	public boolean delete(String id) {
		getBaseDao().delete(id);
		return true;
	}

	@Override
	public boolean delete(T entity) {
		getBaseDao().delete(entity);
		return true;
	}

	@Override
	public T save(T entity) {
		return getBaseDao().save(entity);
	}

}
