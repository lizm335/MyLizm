package com.ouchgzee.headTeacher.dao.textbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.textbook.repository.GjtTextbookFeedbackRepository;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookFeedback;

@Deprecated @Repository("bzrGjtTextbookFeedbackDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookFeedbackDao {

	@Autowired
	private GjtTextbookFeedbackRepository gjtTextbookFeedbackRepository;
	
	public Page<BzrGjtTextbookFeedback> findAll(Specification<BzrGjtTextbookFeedback> spec, PageRequest pageRequst) {
		return gjtTextbookFeedbackRepository.findAll(spec, pageRequst);
	}
	
	public BzrGjtTextbookFeedback save(BzrGjtTextbookFeedback entity) {
		return gjtTextbookFeedbackRepository.save(entity);
	}
	
	public BzrGjtTextbookFeedback findOne(String id) {
		return gjtTextbookFeedbackRepository.findOne(id);
	}

}
