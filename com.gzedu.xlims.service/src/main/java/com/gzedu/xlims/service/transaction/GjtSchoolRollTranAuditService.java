package com.gzedu.xlims.service.transaction;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月9日
 * @version 2.5
 */
public interface GjtSchoolRollTranAuditService{
	/**
	 * 查询学员是否存在审核记录--学习空间接口
	 * @param messageType
	 * @param studentId
	 * @param transactionType
	 * @return
	 */
	List<GjtSchoolRollTransAudit> queryGjtSchoolRollTranAudit(int messageType, String studentId, int transactionType);

	List<Map<String, String>> querySchoolRollRransAuditList(String studentId,String transactionId);
	/**
	 * 根据ID查询学员的审核记录--教务后台
	 * @param transactionId
	 * @param studentId
	 * @param booleanNo
	 * @return
	 */
	List<GjtSchoolRollTransAudit> queryTransAuditInfo(String transactionId,String studentId, String booleanNo);
	/**
	 * 添加学习中心审核记录
	 * @param transactionId
	 * @param auditState
	 * @param auditContent
	 * @return
	 */
	boolean updateSchoolRollTranAudit(Map<String, Object> data);
	/**
	 * 添加学习科审核记录
	 * @param data
	 * @return
	 */
	boolean updateTransAudit(Map<String, Object> data);
	/**
	 * 更新审核记录(信息更正)
	 * @param messageType
	 * @param studentId
	 * @param i
	 * @param rollTran
	 */
	void initSchoolRollTranAudit(int messageType, String studentId, int tranType, GjtSchoolRollTran rollTran);
	/**
	 * 更新审核记录
	 * @param rollTran
	 * @param studentId
	 */
	void initAudit(GjtSchoolRollTran rollTran, String studentId);
	/**
	 * 更新退学审核记录--招生调用
	 * @param studentId
	 * @param transType
	 * @param operatorRoleName 
	 * @param transStatus 
	 * @param operatorRole 
	 * @param auditContent 
	 * @param transCost 
	 */
	boolean updateOutStudyRransAudit(String studentId, int transType, int transStatus, String operatorRoleName, 
			int operatorRole, String auditContent, GjtSchoolRollTransCost transCost);
	
	/**
	 * 退学审核流程
	 * @param data
	 * @return
	 */
	boolean insertOutStudyTransAudit(Map<String, Object> data);
	/**
	 * 招生平台更新退学申请状态
	 * @param gjtStudentInfo
	 * @param applyStatus
	 * @return
	 */
	int syncOutStudyRransAudit(GjtStudentInfo gjtStudentInfo, String applyStatus);

	List<GjtSchoolRollTransAudit> queryDropOutStudyRransAuditList(String transactionId, String studentId);
	
		
}
