package com.ouchgzee.headTeacher.service.textbook;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStockOperaBatch;

@Deprecated public interface BzrGjtTextbookStockOperaBatchService {
	
	public Page<BzrGjtTextbookStockOperaBatch> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public BzrGjtTextbookStockOperaBatch insert(BzrGjtTextbookStockOperaBatch entity);
	
	public BzrGjtTextbookStockOperaBatch findOne(String id);
	
	public void approval(String id, int operaType, String description, String userId);

}
