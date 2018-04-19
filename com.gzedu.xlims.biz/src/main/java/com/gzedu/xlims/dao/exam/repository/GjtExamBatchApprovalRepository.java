package com.gzedu.xlims.dao.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.exam.GjtExamBatchApproval;

public interface GjtExamBatchApprovalRepository extends JpaRepository<GjtExamBatchApproval, String>, JpaSpecificationExecutor<GjtExamBatchApproval> {

}
