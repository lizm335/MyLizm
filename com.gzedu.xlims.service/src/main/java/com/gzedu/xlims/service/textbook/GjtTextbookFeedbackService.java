package com.gzedu.xlims.service.textbook;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedback;

public interface GjtTextbookFeedbackService {
	
	public Page<GjtTextbookFeedback> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtTextbookFeedback insert(GjtTextbookFeedback entity);
	
	public void update(GjtTextbookFeedback entity);
	
	public GjtTextbookFeedback findOne(String id);

}
