package com.ouchgzee.headTeacher.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStockOpera;

@Deprecated public interface BzrGjtTextbookStockOperaService {
	
	public BzrGjtTextbookStockOpera insert(BzrGjtTextbookStockOpera entity);
	
	public List<BzrGjtTextbookStockOpera> insert(List<BzrGjtTextbookStockOpera> entities);
	
	public Page<BzrGjtTextbookStockOpera> findAll(Map<String, Object> searchParams, PageRequest pageRequst);

}
