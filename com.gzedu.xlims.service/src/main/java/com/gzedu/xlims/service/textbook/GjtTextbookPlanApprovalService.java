package com.gzedu.xlims.service.textbook;

import java.util.List;

import com.gzedu.xlims.pojo.textbook.GjtTextbookPlanApproval;

public interface GjtTextbookPlanApprovalService {
	
	public GjtTextbookPlanApproval insert(GjtTextbookPlanApproval entity);
	
	public List<GjtTextbookPlanApproval> findByPlanId(String planId);

}
