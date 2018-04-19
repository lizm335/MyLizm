package com.ouchgzee.headTeacher.serviceImpl.textbook;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookPlanDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookPlan;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookPlanService;

@Deprecated @Service("bzrGjtTextbookPlanServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookPlanServiceImpl implements BzrGjtTextbookPlanService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookPlanServiceImpl.class);
	
	@Autowired
	private GjtTextbookPlanDao gjtTextbookPlanDao;

	@Override
	public Page<BzrGjtTextbookPlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtTextbookPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtTextbookPlan.class);
		return gjtTextbookPlanDao.findAll(spec, pageRequst);
	}


	@Override
	public BzrGjtTextbookPlan findOne(String id) {
		return gjtTextbookPlanDao.findOne(id);
	}


}
