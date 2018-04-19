package com.gzedu.xlims.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOpera;

public interface GjtTextbookStockOperaService {
	
	public GjtTextbookStockOpera insert(GjtTextbookStockOpera entity);
	
	public List<GjtTextbookStockOpera> insert(List<GjtTextbookStockOpera> entities);
	
	public Page<GjtTextbookStockOpera> findAll(Map<String, Object> searchParams, PageRequest pageRequst);

}
