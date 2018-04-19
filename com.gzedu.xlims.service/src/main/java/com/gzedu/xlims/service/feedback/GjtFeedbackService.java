package com.gzedu.xlims.service.feedback;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtFeedback;

public interface GjtFeedbackService {
	Page<GjtFeedback> queryPageList(Map<String, Object> searchParams, PageRequest pageRequst);

	long finAllCount(Map<String, Object> searchParams);

	Boolean saveGjtFeedback(GjtFeedback gjtFeedback);

	GjtFeedback queryById(String id);

	void updateDealResultById(String id);

}