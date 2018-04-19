package com.ouchgzee.headTeacher.serviceImpl.exam;

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
import com.ouchgzee.headTeacher.dao.exam.GjtExamBatchNewDao;
import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;
import com.ouchgzee.headTeacher.service.exam.BzrGjtExamBatchNewService;

@Deprecated @Service("bzrGjtExamBatchNewServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtExamBatchNewServiceImpl implements BzrGjtExamBatchNewService {

	@Autowired
	private GjtExamBatchNewDao gjtExamBatchNewDao;
	
	@Override
	public Page<BzrGjtExamBatchNew> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, schoolId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));
//		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<BzrGjtExamBatchNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				BzrGjtExamBatchNew.class);
		return gjtExamBatchNewDao.findAll(spec, pageRequst);
	}

	@Override
	public BzrGjtExamBatchNew queryBy(String id) {
		return gjtExamBatchNewDao.findOne(id);
	}

	@Override
	public BzrGjtExamBatchNew findCurrentExamBatch(String xxId) {
		List<BzrGjtExamBatchNew> examBatchList = gjtExamBatchNewDao.findCurrentExamBatchList(xxId);
		if (examBatchList != null && examBatchList.size() > 0) {
			return examBatchList.get(0);
		}
		
		return null;
	}

	@Override
	public BzrGjtExamBatchNew findLastExamBatch(String xxId) {
		List<BzrGjtExamBatchNew> examBatchList = gjtExamBatchNewDao.findExamBatchList(xxId);
		if (examBatchList != null && examBatchList.size() > 0) {
			return examBatchList.get(0);
		}
		
		return null;
	}

	@Override
	public BzrGjtExamBatchNew findByExamBatchCode(String examBatchCode) {
		return gjtExamBatchNewDao.findByExamBatchCodeAndIsDeleted(examBatchCode, 0);
	}

}
