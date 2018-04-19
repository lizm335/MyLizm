package com.gzedu.xlims.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;

public interface GjtTextbookStockService {
	
	public Page<GjtTextbookStock> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public List<GjtTextbookStock> findAll(Map<String, Object> searchParams);
	
	public GjtTextbookStock findOne(Map<String, Object> searchParams);
	
	public GjtTextbookStock findOne(String id);
	
	public GjtTextbookStock insert(GjtTextbookStock entity);
	
	public void update(GjtTextbookStock entity);
	
	public void update(List<GjtTextbookStock> entities);

}
