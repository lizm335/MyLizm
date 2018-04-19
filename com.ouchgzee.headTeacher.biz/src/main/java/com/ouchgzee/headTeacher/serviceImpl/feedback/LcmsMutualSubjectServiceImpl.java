package com.ouchgzee.headTeacher.serviceImpl.feedback;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.ouchgzee.headTeacher.dao.feedback.LcmsMutualSubjectDao;
import com.ouchgzee.headTeacher.pojo.BzrLcmsMutualSubject;
import com.ouchgzee.headTeacher.service.feedback.BzrLcmsMutualSubjectService;

@Deprecated @Service("bzrLcmsMutualSubjectServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class LcmsMutualSubjectServiceImpl implements BzrLcmsMutualSubjectService {

	@Autowired
	LcmsMutualSubjectDao lcmsMutualSubjectDao;

	@Override
	public Page<BzrLcmsMutualSubject> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		Specification<BzrLcmsMutualSubject> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				BzrLcmsMutualSubject.class);
		return lcmsMutualSubjectDao.findAll(spec, page);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrLcmsMutualSubject> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				BzrLcmsMutualSubject.class);
		return lcmsMutualSubjectDao.count(spec);
	}

	@Override
	public BzrLcmsMutualSubject save(BzrLcmsMutualSubject item) {
		return lcmsMutualSubjectDao.save(item);
	}

	@Override
	public BzrLcmsMutualSubject queryById(String id) {
		return lcmsMutualSubjectDao.findOne(id);
	}

	@Override
	public boolean update(BzrLcmsMutualSubject item) {
		BzrLcmsMutualSubject entity = lcmsMutualSubjectDao.save(item);
		return entity == null ? false : true;
	}

	@Override
	public boolean updateIsComm(String id, String state) {
		int i = lcmsMutualSubjectDao.updateIsComm(id, state);
		return i == 1 ? true : false;
	}

	@Override
	public boolean delete(String id) {
		int i = lcmsMutualSubjectDao.updateIsDelete(id);
		return i == 1 ? true : false;
	}

	@Override
	public boolean updateForward(String id, String initialId) {
		int i = lcmsMutualSubjectDao.updateForward(id, initialId);
		return i == 1 ? true : false;
	}

}