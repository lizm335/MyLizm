package com.gzedu.xlims.dao.exam.repository;

import java.util.List;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;

public interface GjtExamPointNewRepository extends BaseDao<GjtExamPointNew, String> {

	List<GjtExamPointNew> findByCodeAndIsDeleted(String code, String isDeleted);

	List<GjtExamPointNew> findByExamBatchCodeAndIsDeleted(String examBatchCode, String isDeleted);

	GjtExamPointNew findByExamBatchCodeAndExamPointIdAndIsDeleted(String examBatchCode, String examPointId, String isDeleted);

	GjtExamPointNew findByExamBatchCodeAndCodeAndIsDeleted(String examBatchCode, String code, String isDeleted);
}
