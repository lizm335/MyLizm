package com.ouchgzee.headTeacher.serviceImpl.feedback;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.ouchgzee.headTeacher.dao.feedback.LcmsMutualReplyDao;
import com.ouchgzee.headTeacher.pojo.BzrLcmsMutualReply;
import com.ouchgzee.headTeacher.service.feedback.BzrLcmsMutualReplyService;

@Deprecated @Service("bzrLcmsMutualReplyServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class LcmsMutualReplyServiceImpl implements BzrLcmsMutualReplyService {

	@Autowired
	LcmsMutualReplyDao lcmsMutualReplyDao;

	@Override
	public Page<BzrLcmsMutualReply> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		Specification<BzrLcmsMutualReply> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				BzrLcmsMutualReply.class);
		return lcmsMutualReplyDao.findAll(spec, page);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrLcmsMutualReply> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				BzrLcmsMutualReply.class);
		return lcmsMutualReplyDao.count(spec);
	}

	@Override
	public BzrLcmsMutualReply save(BzrLcmsMutualReply item) {
		return lcmsMutualReplyDao.save(item);
	}

	@Override
	public BzrLcmsMutualReply queryById(String id) {
		return lcmsMutualReplyDao.findOne(id);
	}

	@Override
	public boolean update(BzrLcmsMutualReply item) {
		BzrLcmsMutualReply entity = lcmsMutualReplyDao.save(item);
		return entity == null ? false : true;
	}

	@Override
	public boolean deleteBySubjectId(String id) {
		int i = lcmsMutualReplyDao.deleteBySubjectId(id);
		return i == 1 ? true : false;
	}

	@Override
	public BzrLcmsMutualReply queryPidBySubjectId(String pId) {
		BzrLcmsMutualReply item = null;
		List<BzrLcmsMutualReply> list = lcmsMutualReplyDao.findBySubjectIdAndParentIdIsNotNull(pId);
		if (list != null && list.size() > 0) {
			item = list.get(0);
		}
		return item;
	}

}