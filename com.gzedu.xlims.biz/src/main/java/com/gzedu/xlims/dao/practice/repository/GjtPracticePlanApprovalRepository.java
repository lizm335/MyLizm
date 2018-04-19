package com.gzedu.xlims.dao.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.practice.GjtPracticePlanApproval;

public interface GjtPracticePlanApprovalRepository extends JpaRepository<GjtPracticePlanApproval, String>, JpaSpecificationExecutor<GjtPracticePlanApproval> {

}
