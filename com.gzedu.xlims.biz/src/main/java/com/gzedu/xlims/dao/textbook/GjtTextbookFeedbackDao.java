package com.gzedu.xlims.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.textbook.repository.GjtTextbookFeedbackRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedback;

@Repository
public class GjtTextbookFeedbackDao {

	@Autowired
	private GjtTextbookFeedbackRepository gjtTextbookFeedbackRepository;
	
	public Page<GjtTextbookFeedback> findAll(Specification<GjtTextbookFeedback> spec, PageRequest pageRequst) {
		return gjtTextbookFeedbackRepository.findAll(spec, pageRequst);
	}
	
	public GjtTextbookFeedback save(GjtTextbookFeedback entity) {
		return gjtTextbookFeedbackRepository.save(entity);
	}
	
	public GjtTextbookFeedback findOne(String id) {
		return gjtTextbookFeedbackRepository.findOne(id);
	}
	
	public List<GjtTextbookFeedback> findByStatusAndOrgId(int status, String orgId) {
		return gjtTextbookFeedbackRepository.findByStatusAndOrgId(status, orgId);
	}

}
