package com.gzedu.xlims.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.GjtSchoolRollTran;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月7日
 * @version 2.5
 */
public interface GjtSchoolRollTranDao extends JpaRepository<GjtSchoolRollTran,String>,JpaSpecificationExecutor<GjtSchoolRollTran>{
	
	@Query(value="SELECT T FROM GjtSchoolRollTran T WHERE T.isDeleted='N' AND T.transactionPartStatus=?1 AND T.studentId=?2 AND T.transactionType=?3 ORDER BY CREATED_DT DESC")
	List<GjtSchoolRollTran> findSchoolRollTran(int messageType, String studentId,int transactionType);

	List<GjtSchoolRollTran> findByStudentIdAndIsDeletedOrderByCreatedDtAsc(String studentId, String booleanNo);

	List<GjtSchoolRollTran> findByStudentIdAndTransactionTypeAndIsDeletedOrderByCreatedDtDesc(String studentId,int type, String booleanNo);
	
	@Query(value="SELECT T FROM GjtSchoolRollTran T WHERE T.isDeleted=?3  AND T.studentId=?1 AND T.transactionType=?2 order by T.createdDt DESC")
	List<GjtSchoolRollTran> queryGjtSchoolRollTran(String studentId, int transType, String booleanNo);
	
	@Query(value="SELECT T FROM GjtSchoolRollTran T WHERE T.transactionStatus<>13  AND T.studentId=?1 AND T.transactionType=?2 ")
	List<GjtSchoolRollTran> findByDropOutStudyRollTran(String studentId, int transactionType);

	List<GjtSchoolRollTran> findByStudentId(String studentId);
}
