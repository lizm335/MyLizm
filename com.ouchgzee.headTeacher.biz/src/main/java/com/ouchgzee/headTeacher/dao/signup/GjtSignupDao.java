/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.signup;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtSignup;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 报名信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月21日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtSignupDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtSignupDao extends BaseDao<BzrGjtSignup, String> {

    /**
     * 根据学员ID获取报名信息
     * @param studentId
     * @return
     */
    @Query("SELECT T FROM BzrGjtSignup T WHERE T.isDeleted='N' AND T.gjtStudentInfo.studentId=:studentId")
    BzrGjtSignup findByStudentId(@Param("studentId") String studentId);

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
    @Query("UPDATE BzrGjtSignup t SET t.auditState=:auditState,t.auditOpinion=:auditOpinion,t.auditDate=:updatedDt,t.version=t.version+1,t.updatedBy=:updatedBy,t.updatedDt=:updatedDt WHERE t.gjtStudentInfo.studentId=:studentId AND t.isDeleted='N'")
    @Transactional(value="transactionManagerBzr")
    int auditSignupData(@Param("studentId") String studentId, @Param("auditState") String auditState, @Param("auditOpinion") String auditOpinion,
                        @Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);

    /**
     * 提交资料
     *
     * @param studentId
     * @param employeeId
     */
    @Modifying

    @Query(value = "UPDATE GJT_SIGNUP T SET T.AUDIT_STATE='3'" +
            " WHERE T.STUDENT_ID=:studentId AND (T.AUDIT_STATE='4' OR T.AUDIT_STATE='2')" +
            " AND EXISTS(SELECT Y.STUDENT_ID" +
            "                        FROM GJT_CLASS_INFO X," +
            "                          GJT_CLASS_STUDENT Y" +
            "                        WHERE X.CLASS_ID = Y.CLASS_ID" +
            "                          AND X.IS_DELETED = 'N'" +
            "                          AND Y.IS_DELETED = 'N'" +
            "                          AND X.CLASS_TYPE = 'teach'" +
            "                          AND X.BZR_ID = :employeeId" +
            "                          AND Y.STUDENT_ID=T.STUDENT_ID" +
            "           )",
            nativeQuery = true)
    @Transactional(value="transactionManagerBzr")
    int signupSibmit(@Param("studentId") String studentId, @Param("employeeId") String employeeId);
}
