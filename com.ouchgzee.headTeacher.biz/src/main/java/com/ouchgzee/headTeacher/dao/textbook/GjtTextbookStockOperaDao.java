package com.ouchgzee.headTeacher.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.textbook.repository.GjtTextbookStockOperaRepository;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStockOpera;

@Deprecated @Repository("bzrGjtTextbookStockOperaDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookStockOperaDao {

	@Autowired
	private GjtTextbookStockOperaRepository gjtTextbookStockOperaRepository;
	
	public BzrGjtTextbookStockOpera save(BzrGjtTextbookStockOpera entity) {
		return gjtTextbookStockOperaRepository.save(entity);
	}
	
	public List<BzrGjtTextbookStockOpera> save(List<BzrGjtTextbookStockOpera> entities) {
		return gjtTextbookStockOperaRepository.save(entities);
	}
	
	public Page<BzrGjtTextbookStockOpera> findAll(Specification<BzrGjtTextbookStockOpera> spec, PageRequest pageRequst) {
		return gjtTextbookStockOperaRepository.findAll(spec, pageRequst);
	}

}
