/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.signup;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtSignupData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 学员信息操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月27日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtSignupDataDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtSignupDataDao extends BaseDao<BzrGjtSignupData, String> {

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
    @Query("UPDATE BzrGjtSignupData t SET t.auditState=:auditState,t.auditOpinion=:auditOpinion,t.auditDate=:updatedDt,t.auditor=:updatedBy WHERE t.studentId=:studentId")
    @Transactional(value="transactionManagerBzr")
    int auditSignupData(@Param("studentId") String studentId, @Param("auditState") String auditState, @Param("auditOpinion") String auditOpinion,
                        @Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);

    /**
     * 上传学员的证件资料
     * @param studentId
     * @param signupId
     * @param idcard
     * @param fileType
     * @param url
     * @return
     */
    @Modifying
    @Query(value = "MERGE INTO GJT_SIGNUP_DATA T " +
            " USING (SELECT :studentId AS STUDENT_ID,:fileType AS FILE_TYPE FROM DUAL) X" +
            " ON (X.STUDENT_ID=T.STUDENT_ID AND X.FILE_TYPE=T.FILE_TYPE)" +
            " WHEN MATCHED THEN UPDATE SET T.URL=:url" +
            " WHEN NOT MATCHED THEN INSERT(ID,SIGNUP_ID,STUDENT_ID,ID_NO,FILE_TYPE,URL,CONTENT_TYPE,AUDIT_STATE,SOURCE) " +
            "   VALUES ((SELECT SYS_GUID() FROM DUAL),:signupId,:studentId,:idcard,:fileType,:url,'application/octet-stream','0','sysadm')",
            nativeQuery = true)
    @Transactional(value="transactionManagerBzr")
    int updateSignupCopyData(@Param("studentId") String studentId, @Param("signupId") String signupId, @Param("idcard") String idcard,
                             @Param("fileType") String fileType, @Param("url") String url);
}
