package com.gzedu.xlims.dao.exam.repository;

import java.util.Date;
import java.util.List;

import com.gzedu.xlims.dao.base.BaseDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;

public interface GjtExamBatchNewRepository extends BaseDao<GjtExamBatchNew, String> {

	public GjtExamBatchNew findByExamBatchCodeAndXxId(String examBatchCode, String xxid);

	public GjtExamBatchNew findByExamBatchCodeAndIsDeleted(String examBatchCode, int isDeleted);
//	public GjtExamBatchNew findByExamBatchCodeAndIsDeleted(String examBatchCode, String isDeleted);

	public GjtExamBatchNew findByStudyYearIdAndXxIdAndIsDeleted(String studyYearId, String xxId, int isDeleted);
//	public GjtExamBatchNew findByStudyYearIdAndXxIdAndIsDeleted(String studyYearId, String xxId, String isDeleted);
	
	public GjtExamBatchNew findByGradeIdAndXxIdAndIsDeleted(String gradeId, String xxId, int isDeleted);
	
	public List<GjtExamBatchNew> findByPlanStatusAndXxIdAndIsDeletedAndRecordEndAfter(String planStatus, String xxId, int isDeleted, Date date);
	
	@Query("select b from GjtExamBatchNew b where b.isDeleted = 0 and b.xxId = ?1 and b.gjtGrade.startDate <= sysdate and b.gjtGrade.endDate >= sysdate")
	public List<GjtExamBatchNew> findCurrentExamBatchList(String xxId);
}
