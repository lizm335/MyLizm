package com.gzedu.xlims.service.textbook;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOperaBatch;

public interface GjtTextbookStockOperaBatchService {
	
	public Page<GjtTextbookStockOperaBatch> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtTextbookStockOperaBatch insert(GjtTextbookStockOperaBatch entity);
	
	public GjtTextbookStockOperaBatch findOne(String id);
	
	public void approval(String id, int operaType, String description, String userId);

}
