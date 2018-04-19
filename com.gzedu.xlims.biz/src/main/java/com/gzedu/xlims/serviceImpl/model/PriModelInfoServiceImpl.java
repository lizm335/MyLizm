package com.gzedu.xlims.serviceImpl.model;

import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.model.PriModelInfoDao;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.service.model.PriModelInfoService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PriModelInfoServiceImpl extends BaseServiceImpl<PriModelInfo> implements PriModelInfoService {
	
	@Autowired
	public PriModelInfoDao priModelInfoDao;

	@Override
	public List<PriModelInfo> findAll() {
		return priModelInfoDao.findAll();
	}

	@Override
	public List<PriModelInfo> queryBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<PriModelInfo> spec = new Criteria();
		spec.addAll(Restrictions.parse(searchParams));
		return getBaseDao().findAll(spec, sort);
	}

	@Override
	protected BaseDao<PriModelInfo, String> getBaseDao() {
		return this.priModelInfoDao;
	}
}
