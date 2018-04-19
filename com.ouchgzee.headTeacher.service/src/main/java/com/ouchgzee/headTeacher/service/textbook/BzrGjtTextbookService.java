package com.ouchgzee.headTeacher.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbook;

@Deprecated public interface BzrGjtTextbookService {
	
	public Page<BzrGjtTextbook> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public List<BzrGjtTextbook> findAll(Map<String, Object> searchParams);
	
	public BzrGjtTextbook findOne(Map<String, Object> searchParams);
	
	public BzrGjtTextbook findOne(String id);
	
	public BzrGjtTextbook findByCode(String textbookCode, String orgId);
	
	public BzrGjtTextbook insert(BzrGjtTextbook entity);
	
	public void update(BzrGjtTextbook entity);

}
