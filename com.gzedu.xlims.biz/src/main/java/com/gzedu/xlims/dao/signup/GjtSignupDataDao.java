/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.signup;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtSignupData;

/**
 * 学员信息操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月27日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtSignupDataDao extends BaseDao<GjtSignupData, String> {
	
	/**
	 * 根据学员ID获取指定证件资料
	 * @param studentId
	 * @return
	 */
//	@Query("SELECT t FROM GjtSignupData t WHERE t.studentId=:studentId AND t.fileType=:fileType")
	GjtSignupData findByStudentIdAndFileType(String studentId, String fileType);
	
	/**
	 * 根据学员ID获取所有证件资料
	 * @param studentId
	 * @return
	 */
//	@Query("SELECT t FROM GjtSignupData t WHERE t.studentId=:studentId AND t.fileType=:fileType")
	List<GjtSignupData> findByStudentId(String studentId);

    /**
     * 查询学员的证件资料
     * @param studentId
     * @return
     */
    @Query(value = "SELECT T.FILE_TYPE,NVL(T.URL_NEW,T.URL) FROM GJT_SIGNUP_DATA T WHERE T.STUDENT_ID=:studentId", nativeQuery = true)
    List<Object[]> querySignupDataByStudentId(@Param("studentId") String studentId);

    /**
     * 审核报读资料
     *
     * @param studentId
     * @param auditState
     * @param auditOpinion
     * @param updatedBy
     */
    @Modifying
    @Query("UPDATE GjtSignupData t SET t.auditState=:auditState,t.auditOpinion=:auditOpinion,t.auditDate=:updatedDt,t.auditor=:updatedBy WHERE t.studentId=:studentId")
    @Transactional
    int auditSignupData(@Param("studentId") String studentId, @Param("auditState") String auditState, @Param("auditOpinion") String auditOpinion,
                        @Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);

}
