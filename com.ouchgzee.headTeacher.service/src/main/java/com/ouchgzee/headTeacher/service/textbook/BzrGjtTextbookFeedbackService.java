package com.ouchgzee.headTeacher.service.textbook;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookFeedback;

@Deprecated public interface BzrGjtTextbookFeedbackService {
	
	public Page<BzrGjtTextbookFeedback> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public BzrGjtTextbookFeedback insert(BzrGjtTextbookFeedback entity);
	
	public void update(BzrGjtTextbookFeedback entity);
	
	public BzrGjtTextbookFeedback findOne(String id);

}
