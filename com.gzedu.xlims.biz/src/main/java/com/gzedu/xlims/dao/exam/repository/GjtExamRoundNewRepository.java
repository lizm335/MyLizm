package com.gzedu.xlims.dao.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.exam.GjtExamRoundNew;

public interface GjtExamRoundNewRepository
		extends JpaRepository<GjtExamRoundNew, String>, JpaSpecificationExecutor<GjtExamRoundNew> {

	List<GjtExamRoundNew> findByExamBatchCodeAndExamPointId(String examBatchCode, String examPointId);
}
