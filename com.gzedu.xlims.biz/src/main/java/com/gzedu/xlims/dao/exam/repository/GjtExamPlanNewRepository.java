package com.gzedu.xlims.dao.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;

public interface GjtExamPlanNewRepository extends BaseDao<GjtExamPlanNew, String> {

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "	update " + "			GJT_EXAM_PLAN_NEW " + "		set "
			+ "			IS_DELETED=1" + "		where " + "			EXAM_PLAN_ID=:id")
	public int deleteGjtExamPlanNew(@Param("id") String id);

	public GjtExamPlanNew queryByExamPlanId(String id);

	GjtExamPlanNew findByExamBatchCodeAndTypeAndExamSubjectNewKch(String examBatchCode, String type, String kch);

	GjtExamPlanNew findByExamBatchNewStudyYearIdAndTypeAndExamSubjectNewSubjectIdAndIsDeleted(String studyYearId,
			int type, String SubjectId, int isDeleted);

	GjtExamPlanNew findByExamBatchNewStudyYearIdAndTypeAndSubjectCodeAndIsDeleted(String studyYearId, int type,
			String SubjectCode, int isDeleted);


	List<GjtExamPlanNew> findByExamBatchCodeAndIsDeleted(String batchCode, int isDeleted);
	
	List<GjtExamPlanNew> findByIsDeletedAndExamNoIsNotNull(int isDeleted);

	GjtExamPlanNew findByExamBatchCodeAndExamNoAndTypeAndIsDeleted(String examBatchCode, String examNo, int type, int isDeleted);

	@Deprecated
	GjtExamPlanNew findByExamBatchCodeAndExamNoAndTypeAndGjtCourseListCourseIdAndIsDeleted(String examBatchCode, String examNo, int type, String courseId, int isDeleted);
	
	@Query("select p from GjtExamPlanNew p where p.isDeleted = 0 and p.xxId = ?1 and p.examBatchNew.gjtGrade.startDate <= sysdate and p.examBatchNew.gjtGrade.endDate >= sysdate")
	List<GjtExamPlanNew> findCurrentExamPlanList(String xxId);

	/**
	 * 查询修改的开考科目数
	 * @param examBatchNo
	 * @return HQL-返回Long<br>
	 *         SQL-返回BigDecimal<br>
	 */
	@Query("SELECT COUNT(*) FROM GjtExamPlanNew t WHERE t.isDeleted=0 AND t.updatedDt IS NOT NULL AND t.examBatchCode=:examBatchCode")
	Long countUpdateExamPlan(@Param("examBatchCode") String examBatchCode);

	/**
	 * 删除开考科目
	 * @param examBatchCode
	 */
	@Modifying
	@Transactional
	@Query("UPDATE GjtExamPlanNew t SET t.isDeleted=1,t.updatedDt=sysdate,t.updatedBy='自动生成开考科目-删除' WHERE t.isDeleted=0 AND t.examBatchCode=:examBatchCode")
	int deleteByExamBatchCode(@Param("examBatchCode") String examBatchCode);

}
