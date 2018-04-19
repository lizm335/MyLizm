/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.exam;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtExamRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 考试预约流水记录操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月15日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtExamRecordDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtExamRecordDao extends BaseDao<BzrGjtExamRecord, String> {

    /**
     * 修改考试记录表中当前选课ID的所有记录，状态全部重置为0
     * @param recId
     * @return
     */
    @Query("UPDATE BzrGjtExamRecord t SET t.examState='0' WHERE t.recId=:recId")
    @Modifying
    @Transactional(value="transactionManagerBzr")
    int inactiveExamState(@Param("recId") String recId);

}
