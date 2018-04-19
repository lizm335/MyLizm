/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.flow;

import java.util.Date;
import java.util.List;

import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 审核流程逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年02月11日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtFlowService extends BaseService<GjtFlowRecord> {
	
	/**
	 * 根据学员ID获取学籍资料审核记录
	 * @param studentId
	 * @return
	 */
	List<GjtFlowRecord> queryFlowRecordByStudentId(String studentId);
	
	/**
	 * 初始化学员的学籍资料审核记录
	 * @param studentId
	 * @return
	 */
	boolean initAuditSignupInfo(String studentId);

    /**
     * 班主任、招生办审核学籍资料
     * @param studentId
     * @param auditState
     * @param auditContent
     * @param operatorRole
     * @param operatorRealName
     * @return
     */
    ResultFeedback auditSingupInfo(String studentId, String auditState, String auditContent, int operatorRole, String operatorRealName);

    /**
     * 学籍科主任审核学籍资料
     * @param studentId
     * @param auditState
     * @param auditContent
     * @param operatorId
     * @param operatorRealName
     * @return
     */
    boolean auditSignupData(String studentId, String auditState, String auditContent, String operatorId, String operatorRealName);
}
