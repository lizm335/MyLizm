package com.gzedu.xlims.dao.graduation;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationPlan;

/**
 * 毕业计划操作类<br/>
 * @author huangyifei
 */
public interface GjtGraduationPlanDao extends BaseDao<GjtGraduationPlan, String> {

    /**
     * 根据学期查找毕业计划
     * @param termId
     * @param xxId
     * @param isDeleted
     * @return
     */
    GjtGraduationPlan findByGjtGradeGradeIdAndXxIdAndIsDeleted(String termId, String xxId, String isDeleted);

    /**
     * 审核毕业计划
     * @param id
     * @param auditState
     * @param updatedBy
     * @param updatedDt
     */
    @Modifying
    @Query("UPDATE GjtGraduationPlan t SET t.auditState=:auditState,t.version=t.version+1,t.updatedBy=:updatedBy,t.updatedDt=:updatedDt WHERE t.id=:id")
    @Transactional
    int auditGraduationPlan(@Param("id") String id, @Param("auditState") BigDecimal auditState,
                        @Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);

	GjtGraduationPlan findById(String planId);

	GjtGraduationPlan findByTermId(String currentGradeId);

}
