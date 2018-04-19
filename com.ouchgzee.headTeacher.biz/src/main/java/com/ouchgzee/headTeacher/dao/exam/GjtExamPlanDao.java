/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.exam;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtExamPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 期末考试安排操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月15日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtExamPlanDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtExamPlanDao extends BaseDao<BzrGjtExamPlan, String> {

    /**
     * 假删除学员的课程考试安排记录
     * @param studentId
     * @param courseId
     * @param updatedBy
     * @param updatedDt
     * @return
     */
    @Query("UPDATE BzrGjtExamPlan t SET t.isDeleted='Y',t.updatedBy=:updatedBy,t.updatedDt=:updatedDt WHERE t.studentId=:studentId AND t.examCourse=:examCourse")
    @Modifying
    @Transactional(value="transactionManagerBzr")
    int deletedExamPlan(@Param("studentId") String studentId, @Param("examCourse") String courseId,
                          @Param("updatedBy") String updatedBy, @Param("updatedDt") Date updatedDt);

}
