package com.gzedu.xlims.service.textbook;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookOrder;

public interface GjtTextbookOrderService {
	
	public Page<Map<String, Object>> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public Map<String, Object> findNeedOrderNumAndPrice(String orderId);
	
	public GjtTextbookOrder insert(GjtTextbookOrder entity);

	public void update(GjtTextbookOrder entity);
	
	public GjtTextbookOrder findOne(String id);
	
	public GjtTextbookOrder findByPlanId(String planId);
	
	public void delete(GjtTextbookOrder entity);
	
	

}
