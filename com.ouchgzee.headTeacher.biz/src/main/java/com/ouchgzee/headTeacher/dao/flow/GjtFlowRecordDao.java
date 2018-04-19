/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.flow;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.flow.BzrGjtFlowRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 审核流程记录操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年02月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtFlowRecordDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtFlowRecordDao extends BaseDao<BzrGjtFlowRecord, String> {

    /**
     * 学籍科主任审核学籍资料
     * @param studentId
     * @param auditState
     * @param auditContent
     * @param auditOperator
     * @param auditDt
     */
    @Modifying
    @Query("UPDATE BzrGjtFlowRecord t SET t.auditState=:auditState,t.auditOperator=:auditOperator,t.auditDt=:auditDt,t.auditContent=:auditContent WHERE t.studentId=:studentId AND t.auditOperatorRole='4'")
    @Transactional(value="transactionManagerBzr")
    int auditSignupData(@Param("studentId") String studentId, @Param("auditState") BigDecimal auditState, @Param("auditContent") String auditContent,
                        @Param("auditOperator") String auditOperator, @Param("auditDt") Date auditDt);

}
