package com.gzedu.xlims.dao.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.thesis.GjtThesisPlanApproval;

public interface GjtThesisPlanApprovalRepository extends JpaRepository<GjtThesisPlanApproval, String>, JpaSpecificationExecutor<GjtThesisPlanApproval> {

}
