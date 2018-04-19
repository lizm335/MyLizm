package com.ouchgzee.headTeacher.dao.textbook.repository;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookOrderDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Deprecated @Repository("bzrGjtTextbookOrderDetailRepository") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtTextbookOrderDetailRepository extends BaseDao<BzrGjtTextbookOrderDetail, String> {

	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("delete from BzrGjtTextbookOrderDetail d where d.gjtTextbookArrange.arrangeId = ?1")
	public void deleteByArrangeId(String arrangeId);

	public List<BzrGjtTextbookOrderDetail> findByStudentIdAndTextbookIdAndStatus(String studentId, String textbookId,
																				 int status);

	public BzrGjtTextbookOrderDetail findByStudentIdAndTextbookIdAndCourseIdAndPlanId(String studentId, String textbookId,
																					  String courseId, String planId);

}
