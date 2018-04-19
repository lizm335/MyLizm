package com.gzedu.xlims.dao.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.thesis.GjtThesisDefencePlan;

public interface GjtThesisDefencePlanRepository extends JpaRepository<GjtThesisDefencePlan, String>, JpaSpecificationExecutor<GjtThesisDefencePlan> {

}
