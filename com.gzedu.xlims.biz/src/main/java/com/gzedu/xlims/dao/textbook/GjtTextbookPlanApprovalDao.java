package com.gzedu.xlims.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.textbook.repository.GjtTextbookPlanApprovalRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlanApproval;

@Repository
public class GjtTextbookPlanApprovalDao {

	@Autowired
	private GjtTextbookPlanApprovalRepository gjtTextbookPlanApprovalRepository;
	
	public GjtTextbookPlanApproval save(GjtTextbookPlanApproval entity) {
		return gjtTextbookPlanApprovalRepository.save(entity);
	}
	
	public List<GjtTextbookPlanApproval> findByPlanId(String planId) {
		return gjtTextbookPlanApprovalRepository.findByPlanId(planId);
	}

}
