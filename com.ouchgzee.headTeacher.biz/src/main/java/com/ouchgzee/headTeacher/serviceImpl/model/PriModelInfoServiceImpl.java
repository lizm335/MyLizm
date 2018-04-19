package com.ouchgzee.headTeacher.serviceImpl.model;

import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.model.PriModelInfoDao;
import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import com.ouchgzee.headTeacher.service.model.BzrPriModelInfoService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Deprecated @Service("bzrPriModelInfoServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class PriModelInfoServiceImpl extends BaseServiceImpl<BzrPriModelInfo> implements BzrPriModelInfoService {
	
	@Autowired
	public PriModelInfoDao priModelInfoDao;

	@Override
	public List<BzrPriModelInfo> findAll() {
		return priModelInfoDao.findAll();
	}

	@Override
	public List<BzrPriModelInfo> queryBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<BzrPriModelInfo> spec = new Criteria();
		spec.addAll(Restrictions.parse(searchParams));
		return getBaseDao().findAll(spec, sort);
	}

	@Override
	protected BaseDao<BzrPriModelInfo, String> getBaseDao() {
		return this.priModelInfoDao;
	}
}
