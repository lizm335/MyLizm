/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.flow;

import com.ouchgzee.headTeacher.pojo.flow.BzrGjtFlowRecord;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 审核流程逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年02月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtFlowService extends BaseService<BzrGjtFlowRecord> {

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
