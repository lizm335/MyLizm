/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.signup;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtSignup;

/**
 * 功能说明：
 * 
 * @author 卢林林
 * @Date 2016年11月29日
 * @version 1.0
 *
 */
public interface GjtSignupDao extends BaseDao<GjtSignup, String> {
	
	/**
	 * 根据学员ID获取报读信息
	 * @param studentId
	 * @return
	 */
	@Query("SELECT t FROM GjtSignup t WHERE t.isDeleted='N' AND t.gjtStudentInfo.studentId=:studentId")
	GjtSignup querySignupByStudentId(@Param("studentId") String studentId);

    /**
     * 审核报读资料
     *
     * @param studentId
     * @param auditState
     * @param auditOpinion
     * @param updatedBy
     * @param updatedDt
     */
    @Modifying
    @Query("UPDATE GjtSignup t SET t.auditState=:auditState,t.auditOpinion=:auditOpinion,t.auditDate=:updatedDt,t.version=t.version+1,t.updatedBy=:updatedBy,t.updatedDt=:updatedDt WHERE t.gjtStudentInfo.studentId=:studentId AND t.isDeleted='N'")
    @Transactional
    int auditSignupData(@Param("studentId") String studentId, @Param("auditState") String auditState, @Param("auditOpinion") String auditOpinion,
                        @Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);

}
