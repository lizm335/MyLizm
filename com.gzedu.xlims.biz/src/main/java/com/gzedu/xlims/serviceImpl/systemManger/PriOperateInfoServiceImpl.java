/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.systemManger;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.model.PriOperateInfoDao;
import com.gzedu.xlims.pojo.PriOperateInfo;
import com.gzedu.xlims.service.systemManage.PriOperateInfoService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
@Service
public class PriOperateInfoServiceImpl extends BaseServiceImpl<PriOperateInfo> implements PriOperateInfoService {

	@Autowired
	PriOperateInfoDao priOperateInfoDao;

	@Override
	public Page<PriOperateInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		searchParams.put("EQ_isdeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<PriOperateInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				PriOperateInfo.class);
		return priOperateInfoDao.findAll(spec, pageRequst);
	}

	@Override
	public PriOperateInfo queryBy(String id) {
		return priOperateInfoDao.findOne(id);
	}

	@Override
	protected BaseDao<PriOperateInfo, String> getBaseDao() {
		return this.priOperateInfoDao;
	}

	@Override
	public boolean insert(PriOperateInfo entity) {
		entity.setOperateId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsdeleted("N");
		priOperateInfoDao.save(entity);
		return true;
	}

	@Override
	public boolean delete(String id) {
		priOperateInfoDao.delete(id);
		return true;
	}

	@Override
	public void delete(Iterable<String> ids) {
		//priOperateInfoDao.delete(priOperateInfoDao.findAll(ids));
		Iterable<PriOperateInfo> priOperateInfos = priOperateInfoDao.findAll(ids);
		if (priOperateInfos != null) {
			Iterator<PriOperateInfo> iterator = priOperateInfos.iterator();
			while (iterator.hasNext()) {
				PriOperateInfo priOperateInfo = iterator.next();
				priOperateInfo.setIsdeleted("Y");
			}
			
			priOperateInfoDao.save(priOperateInfos);
		}
	}

	@Override
	public boolean update(PriOperateInfo entity) {
		entity.setUpdatedDt(new Date());
		priOperateInfoDao.save(entity);
		return true;
	}

	@Override
	public List<PriOperateInfo> queryAll() {
		return priOperateInfoDao.findAll();
	}

	@Override
	public List<PriOperateInfo> queryAll(Iterable<String> ids) {
		return (List<PriOperateInfo>) priOperateInfoDao.findAll(ids);
	}

	@Override
	public List<PriOperateInfo> queryBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<PriOperateInfo> spec = new Criteria<PriOperateInfo>();
		spec.addAll(Restrictions.parse(searchParams));
		return getBaseDao().findAll(spec, sort);
	}

}
