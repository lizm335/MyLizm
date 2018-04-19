package com.ouchgzee.headTeacher.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStock;

@Deprecated public interface BzrGjtTextbookStockService {
	
	public Page<BzrGjtTextbookStock> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public List<BzrGjtTextbookStock> findAll(Map<String, Object> searchParams);
	
	public BzrGjtTextbookStock findOne(Map<String, Object> searchParams);
	
	public BzrGjtTextbookStock findOne(String id);
	
	public BzrGjtTextbookStock insert(BzrGjtTextbookStock entity);
	
	public void update(BzrGjtTextbookStock entity);
	
	public void update(List<BzrGjtTextbookStock> entities);

}
