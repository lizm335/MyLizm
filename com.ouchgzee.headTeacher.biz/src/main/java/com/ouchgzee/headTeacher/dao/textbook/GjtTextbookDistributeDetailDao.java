package com.ouchgzee.headTeacher.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.textbook.repository.GjtTextbookDistributeDetailRepository;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookDistributeDetail;

@Deprecated @Repository("bzrGjtTextbookDistributeDetailDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookDistributeDetailDao {

	@Autowired
	private GjtTextbookDistributeDetailRepository gjtTextbookDistributeDetailRepository;
	
	public BzrGjtTextbookDistributeDetail save(BzrGjtTextbookDistributeDetail entity) {
		return gjtTextbookDistributeDetailRepository.save(entity);
	}
	
	public List<BzrGjtTextbookDistributeDetail> save(List<BzrGjtTextbookDistributeDetail> entities) {
		return gjtTextbookDistributeDetailRepository.save(entities);
	}
	
	public Page<BzrGjtTextbookDistributeDetail> findAll(Specification<BzrGjtTextbookDistributeDetail> spec, PageRequest pageRequst) {
		return gjtTextbookDistributeDetailRepository.findAll(spec, pageRequst);
	}

}
