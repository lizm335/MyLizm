package com.ouchgzee.headTeacher.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.textbook.repository.GjtTextbookStockRepository;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStock;

@Deprecated @Repository("bzrGjtTextbookStockDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookStockDao {

	@Autowired
	private GjtTextbookStockRepository gjtTextbookStockRepository;

	public Page<BzrGjtTextbookStock> findAll(Specification<BzrGjtTextbookStock> spec, PageRequest pageRequst) {
		return gjtTextbookStockRepository.findAll(spec, pageRequst);
	}

	public List<BzrGjtTextbookStock> findAll(Specification<BzrGjtTextbookStock> spec) {
		return gjtTextbookStockRepository.findAll(spec);
	}
	
	public BzrGjtTextbookStock findOne(Specification<BzrGjtTextbookStock> spec) {
		return gjtTextbookStockRepository.findOne(spec);
	}
	
	public BzrGjtTextbookStock findOne(String id) {
		return gjtTextbookStockRepository.findOne(id);
	}
	
	public BzrGjtTextbookStock save(BzrGjtTextbookStock entity) {
		return gjtTextbookStockRepository.save(entity);
	}
	
	public List<BzrGjtTextbookStock> save(List<BzrGjtTextbookStock> entities) {
		return gjtTextbookStockRepository.save(entities);
	}

}
