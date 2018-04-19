package com.gzedu.xlims.dao.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.exam.repository.GjtExamBatchApprovalRepository;
import com.gzedu.xlims.pojo.exam.GjtExamBatchApproval;

@Repository
public class GjtExamBatchApprovalDao {

	@Autowired
	private GjtExamBatchApprovalRepository gjtExamBatchApprovalRepository;
	
	public GjtExamBatchApproval save(GjtExamBatchApproval entity) {
		return gjtExamBatchApprovalRepository.save(entity);
	}

}
