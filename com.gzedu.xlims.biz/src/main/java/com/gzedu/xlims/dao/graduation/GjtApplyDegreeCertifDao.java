package com.gzedu.xlims.dao.graduation;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtApplyDegreeCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 毕业证申请
 * 
 *
 */
public interface GjtApplyDegreeCertifDao extends BaseDao<GjtApplyDegreeCertif, String> {

    @Query("select b from GjtApplyDegreeCertif b where b.gjtStudentInfo.studentId = ?1 and b.gjtGraduationPlan.id = ?2 ")
    public GjtApplyDegreeCertif queryApplyDegreeCertifByStudentIdAndPlanId(String studntId, String planId);

}
