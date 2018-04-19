package com.gzedu.xlims.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.textbook.repository.GjtTextbookStockOperaRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOpera;

@Repository
public class GjtTextbookStockOperaDao {

	@Autowired
	private GjtTextbookStockOperaRepository gjtTextbookStockOperaRepository;
	
	public GjtTextbookStockOpera save(GjtTextbookStockOpera entity) {
		return gjtTextbookStockOperaRepository.save(entity);
	}
	
	public List<GjtTextbookStockOpera> save(List<GjtTextbookStockOpera> entities) {
		return gjtTextbookStockOperaRepository.save(entities);
	}
	
	public Page<GjtTextbookStockOpera> findAll(Specification<GjtTextbookStockOpera> spec, PageRequest pageRequst) {
		return gjtTextbookStockOperaRepository.findAll(spec, pageRequst);
	}

}
