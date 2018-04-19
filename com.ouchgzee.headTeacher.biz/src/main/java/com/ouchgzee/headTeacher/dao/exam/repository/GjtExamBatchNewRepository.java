package com.ouchgzee.headTeacher.dao.exam.repository;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Deprecated @Repository("bzrGjtExamBatchNewRepository") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtExamBatchNewRepository extends BaseDao<BzrGjtExamBatchNew, String> {

	public BzrGjtExamBatchNew findByExamBatchCodeAndXxId(String examBatchCode, String xxid);

	public BzrGjtExamBatchNew findByExamBatchCodeAndIsDeleted(String examBatchCode, int isDeleted);
//	public GjtExamBatchNew findByExamBatchCodeAndIsDeleted(String examBatchCode, String isDeleted);

	public BzrGjtExamBatchNew findByStudyYearIdAndXxIdAndIsDeleted(String studyYearId, String xxId, int isDeleted);
//	public GjtExamBatchNew findByStudyYearIdAndXxIdAndIsDeleted(String studyYearId, String xxId, String isDeleted);
	
	public BzrGjtExamBatchNew findByGradeIdAndXxIdAndIsDeleted(String gradeId, String xxId, int isDeleted);
	
	public List<BzrGjtExamBatchNew> findByPlanStatusAndXxIdAndIsDeletedAndRecordEndAfter(String planStatus, String xxId, int isDeleted, Date date);
	
	@Query("select b from BzrGjtExamBatchNew b where b.isDeleted = 0 and b.xxId = ?1 and b.gjtGrade.startDate <= sysdate and b.gjtGrade.endDate >= sysdate")
	public List<BzrGjtExamBatchNew> findCurrentExamBatchList(String xxId);
	
	@Query("select b from BzrGjtExamBatchNew b where b.isDeleted = 0 and b.planStatus = 3 and b.xxId = ?1 order by b.gjtGrade.startDate desc")
	public List<BzrGjtExamBatchNew> findExamBatchList(String xxId);
}
