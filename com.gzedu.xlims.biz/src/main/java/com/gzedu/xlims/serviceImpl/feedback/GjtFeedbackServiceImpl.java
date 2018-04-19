package com.gzedu.xlims.serviceImpl.feedback;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.feedback.GjtFeedbackDao;
import com.gzedu.xlims.pojo.GjtFeedback;
import com.gzedu.xlims.service.feedback.GjtFeedbackService;

@Service
public class GjtFeedbackServiceImpl implements GjtFeedbackService {
	@Autowired
	private GjtFeedbackDao gjtFeedbackDao;

	@Override
	public Page<GjtFeedback> queryPageList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		Specification<GjtFeedback> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtFeedback.class);
		return gjtFeedbackDao.findAll(spec, page);
	}

	@Override
	public Boolean saveGjtFeedback(GjtFeedback gjtFeedback) {
		GjtFeedback save = gjtFeedbackDao.save(gjtFeedback);
		return save != null ? true : false;
	}

	@Override
	public long finAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtFeedback> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtFeedback.class);
		return gjtFeedbackDao.count(spec);
	}

	@Override
	public GjtFeedback queryById(String id) {
		return gjtFeedbackDao.findOne(id);
	}

	@Override
	public void updateDealResultById(String id) {
		gjtFeedbackDao.updateDealResultById(id);
	}

}