package com.gzedu.xlims.service.transaction;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月7日
 * @version 2.5
 */
public interface GjtSchoolRollTranService{
	/**
	 * 新增异动申请记录
	 * @param studentInfo
	 * @param messageType 
	 * @param gjtStudentInfo
	 */
	boolean insertGjtSchoolRollTran(Map<String, Object> studentMap, GjtStudentInfo gjtStudentInfo);

	Page<GjtSchoolRollTran> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);
	/**
	 * 查询学员的审核记录
	 * @param messageType
	 * @param gjtStudentInfo
	 * @return
	 */
	List<Map<String, String>> querySchoolRollRransAuditList(String studentId,String transactionId);
	/**
	 * 查询学员异动的内容
	 * @param messageType
	 * @param studentId
	 * @return
	 */
	List<GjtSchoolRollTran> findSchoolRollTran(int messageType, String studentId,int transactionType);

	List<GjtSchoolRollTran> findByStudentIdAndIsDeletedOrderByCreatedDt(String studentId, String booleanNo);

	GjtSchoolRollTran findById(String transactionId);
	
	/**
	 * 学员的异动申请明细
	 * @param searchParams
	 * @return
	 */
	List<Map<String, Object>> querySchoolRollTranList(Map searchParams,String orgId);
	/**
	 * 下载学员的异动申请明细
	 * @param searchParams
	 * @return
	 */
	String downLoadTranactionList(List<Map<String, Object>> resultList,String fileName,String path);

	List<GjtSchoolRollTran> findByStudentIdAndTransactionTypeIsDeleted(String studentId,int type, String booleanNo);
	/**
	 * 重新提交异动申请(信息更正)
	 * @param gjtStudentInfo
	 * @param transactionType
	 * @param transactionPartStatus
	 */
	boolean againSubmitGjtSchoolRollTran(GjtStudentInfo gjtStudentInfo, int transactionType, int transactionPartStatus);
	/**
	 * 重新提交异动申请(转专业、休学、复学、退学、转学)
	 * @param gjtStudentInfo
	 * @param parseInt
	 * @return
	 */
	Boolean againSubmitGjtSchoolRollTranNew(GjtStudentInfo gjtStudentInfo, int transactionType);
	/**
	 * 更新异动申请表中的内容
	 * @param studentMap
	 * @param rollTran
	 */
	void updateGjtSchoolRollTran(Map<String, Object> studentMap, GjtSchoolRollTran rollTran);
	/**
	 * 判断当前时间是否在学期计划中的开学时间后三周（含第三周）之间
	 * @return
	 */
	Boolean getCurrentDate(GjtGrade currentGrade);
	
	boolean inertDropOutStudy(Map<String, Object> outStudyMap, GjtStudentInfo gjtStudentInfo);

	Page<GjtSchoolRollTran> queryRollTransAll(String id, int transType, Map<String, Object> searchParams,
			PageRequest pageRequst);
	/**
	 * 查询异动信息--招生接口
	 * @param id
	 * @param i
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<GjtSchoolRollTran> queryGjtSchoolRollTransAll(String id, int i, Map<String, Object> searchParams,
			PageRequest pageRequest);
	/**
	 * 退学申请记录推送至招生平台
	 * @param outStudyMap
	 * @return
	 */
	int syncDropOutStudy(Map<String, Object> outStudyMap,GjtStudentInfo gjtStudentInfo);
	/**
	 * 学员待确认应扣、应退金额
	 * @param transactionId
	 * @param gjtStudentInfo 
	 * @return
	 */
	int studentConfirmCost(String transactionId, GjtStudentInfo gjtStudentInfo);
	
	long queryStatusTotalNum(String xxId,int OperatorRole, int transactionStatus,String transactionType);
	/**
	 * 总计
	 * @param id
	 * @return
	 */
	long queryTotalNum(String xxId,int transactionType);

	Map<String, Object> queryStudentApplicationMsg(String studentId);
	/**
	 * 学员撤销退学申请
	 * @param transactionId
	 * @param gjtStudentInfo
	 * @return
	 */
	int studentRevocation(String transactionId, GjtStudentInfo gjtStudentInfo);
	
	
	/**
	 * 查询退学申请记录
	 * @param studentId
	 * @param parseInt
	 * @param string
	 * @return
	 */
	List<GjtSchoolRollTran> findByDropOutStudyRollTran(String studentId, int transactionType);
	/**
	 * 学员申请转专业
	 * @param changeSpecialtyMap
	 * @param gjtStudentInfo
	 * @return
	 */
	boolean insertChangeSpecialty(Map<String, Object> changeSpecialtyMap, GjtStudentInfo gjtStudentInfo);
	
	/**
	 * 查询学员的异动申请记录
	 * @param studentId
	 * @return
	 */
	List<GjtSchoolRollTran> findByStudentId(String studentId);
}
