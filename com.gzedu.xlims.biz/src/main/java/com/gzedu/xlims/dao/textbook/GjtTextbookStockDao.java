package com.gzedu.xlims.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.textbook.repository.GjtTextbookStockRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;

@Repository
public class GjtTextbookStockDao {

	@Autowired
	private GjtTextbookStockRepository gjtTextbookStockRepository;

	public Page<GjtTextbookStock> findAll(Specification<GjtTextbookStock> spec, PageRequest pageRequst) {
		return gjtTextbookStockRepository.findAll(spec, pageRequst);
	}

	public List<GjtTextbookStock> findAll(Specification<GjtTextbookStock> spec) {
		return gjtTextbookStockRepository.findAll(spec);
	}
	
	public GjtTextbookStock findOne(Specification<GjtTextbookStock> spec) {
		return gjtTextbookStockRepository.findOne(spec);
	}
	
	public GjtTextbookStock findOne(String id) {
		return gjtTextbookStockRepository.findOne(id);
	}
	
	public GjtTextbookStock save(GjtTextbookStock entity) {
		return gjtTextbookStockRepository.save(entity);
	}
	
	public List<GjtTextbookStock> save(List<GjtTextbookStock> entities) {
		return gjtTextbookStockRepository.save(entities);
	}
	
	public List<GjtTextbookStock> findNotEnoughStock(String orgId) {
		return gjtTextbookStockRepository.findNotEnoughStock(orgId);
	}

}
