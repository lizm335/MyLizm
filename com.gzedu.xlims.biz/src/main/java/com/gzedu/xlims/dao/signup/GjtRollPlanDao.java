/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.signup;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtRollPlan;

/**
 * 学籍计划操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月05日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtRollPlanDao extends BaseDao<GjtRollPlan, String> {

    /**
     * 审核学籍计划
     * @param id
     * @param auditState
     * @param auditContent
     * @param updatedBy
     * @param auditDt
     * @return
     */
    @Modifying
    @Query("UPDATE GjtRollPlan t SET t.auditState=:auditState,t.auditDt=:auditDt,t.auditContent=:auditContent,t.auditOperator=:auditOperator,t.updatedBy=:updatedBy,t.updatedDt=:auditDt WHERE t.id=:id")
    @Transactional
    int auditRollPlan(@Param("id") String id, @Param("auditState") BigDecimal auditState, @Param("auditContent") String auditContent,
                      @Param("auditOperator") String auditOperator, @Param("updatedBy") String updatedBy, @Param("auditDt") Date auditDt);

    /**
     * 根据审核状态获取学籍计划
     * @param auditState
     * @param xxId
     * @param isDeleted
     * @return
     */
    List<GjtRollPlan> findByAuditStateAndXxIdAndIsDeleted(BigDecimal auditState, String xxId, String isDeleted);

    /**
     * 获取当前的学籍计划
     * @param xxId
     * @return
     */
    @Query("select p from GjtRollPlan p where p.isDeleted = 'N' and p.xxId = ?1 and p.gjtGrade.startDate <= sysdate and p.gjtGrade.endDate >= sysdate")
	List<GjtRollPlan> findCurrentRollPlanList(String xxId);

    /**
     * 获取某年级的学籍计划
     * @param xxId
     * @param termId
     * @return
     */
    @Query("select p from GjtRollPlan p where p.isDeleted = 'N' and p.auditState='1' and p.xxId = ?1 and p.gjtGrade.gradeId = ?2")
    List<GjtRollPlan> findTermRollPlanList(String xxId, String termId);

}
