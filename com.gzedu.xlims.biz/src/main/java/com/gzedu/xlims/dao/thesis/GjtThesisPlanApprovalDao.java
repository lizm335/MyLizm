package com.gzedu.xlims.dao.thesis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.thesis.repository.GjtThesisPlanApprovalRepository;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlanApproval;

@Repository
public class GjtThesisPlanApprovalDao {

	@Autowired
	private GjtThesisPlanApprovalRepository gjtThesisPlanApprovalRepository;

	public GjtThesisPlanApproval save(GjtThesisPlanApproval entity) {
		return gjtThesisPlanApprovalRepository.save(entity);
	}
	
}
