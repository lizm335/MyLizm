package com.gzedu.xlims.dao.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.practice.repository.GjtPracticePlanApprovalRepository;
import com.gzedu.xlims.pojo.practice.GjtPracticePlanApproval;

@Repository
public class GjtPracticePlanApprovalDao {
	
	@Autowired
	private GjtPracticePlanApprovalRepository gjtPracticePlanApprovalRepository;

	public GjtPracticePlanApproval save(GjtPracticePlanApproval entity) {
		return gjtPracticePlanApprovalRepository.save(entity);
	}

}
