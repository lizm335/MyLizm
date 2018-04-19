package com.gzedu.xlims.dao.practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.practice.GjtPracticeArrange;

public interface GjtPracticeArrangeRepository extends JpaRepository<GjtPracticeArrange, String>, JpaSpecificationExecutor<GjtPracticeArrange> {

}
