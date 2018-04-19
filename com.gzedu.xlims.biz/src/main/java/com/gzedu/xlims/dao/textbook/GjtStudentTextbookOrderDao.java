package com.gzedu.xlims.dao.textbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.textbook.GjtStudentTextbookOrder;


public interface GjtStudentTextbookOrderDao extends BaseDao<GjtStudentTextbookOrder, String>, JpaRepository<GjtStudentTextbookOrder, String>,
		JpaSpecificationExecutor<GjtStudentTextbookOrder> {

	public GjtStudentTextbookOrder getByStudentIdAndGradeId(String studentId, String gradeId);
	
	public GjtStudentTextbookOrder getByStudentIdAndGradeIdAndIsDeleted(String studentId, String gradeId, String isDeleted);

}
