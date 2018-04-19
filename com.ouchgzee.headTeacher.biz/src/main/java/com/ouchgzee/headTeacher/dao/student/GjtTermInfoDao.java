/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.student;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtTermInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学期信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtTermInfoDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtTermInfoDao extends BaseDao<BzrGjtTermInfo, String> {

    /**
     * 根据学员ID获取当前学期ID
     *
     * @param studentId
     * @return
     */
    @Query(value = "select * from (select t.term_id from gjt_term_info t inner join gjt_student_term b on t.term_id=b.term_id where t.is_deleted='N' and sysdate>=t.start_date and b.student_id=:studentId order by t.start_date desc) where rownum<=1", nativeQuery = true)
    String findByStudentCurrentTerm(@Param("studentId") String studentId);

    /**
     * 根据学员ID获取[总学期/已学习]的学期数
     *
     * @param studentId
     * @return HQL-返回Long<br>
     *         SQL-返回BigDecimal<br>
     */
    @Query(value = "SELECT COUNT(t.term_id),COUNT(CASE WHEN sysdate>=t.start_date THEN 1 ELSE NULL END) FROM gjt_term_info t INNER JOIN gjt_student_term b ON t.term_id=b.term_id WHERE t.is_deleted='N' AND b.student_id=:studentId", nativeQuery = true)
    Object[] countByStudentTerm(@Param("studentId") String studentId);

    /**
     * 根据班级id获取当前期的id
     * @param classId
     * @return
     */
    @Query(value = "SELECT t.TERM_ID FROM gjt_term_info t INNER JOIN gjt_student_term b ON t.term_id = b.term_id LEFT JOIN GJT_CLASS_STUDENT gcs ON gcs.STUDENT_ID = b.STUDENT_ID"+
            " AND gcs.IS_DELETED = 'N' WHERE t.is_deleted = 'N'  AND gcs.CLASS_ID = :classId AND SYSDATE >= t.start_date AND SYSDATE <= t.END_DATE AND rownum <= 1 ORDER BY t.start_date DESC", nativeQuery = true)
    String findByClassCurrentTerm(@Param("classId") String classId);


    /**
     * 根据班级id获取所有期
     * @param classId
     * @return
     */
    @Query(value = "SELECT DISTINCT t.TERM_ID,t.TERM_NAME FROM gjt_term_info t INNER JOIN gjt_student_term b ON t.term_id = b.term_id LEFT JOIN GJT_CLASS_STUDENT gcs ON gcs.STUDENT_ID = b.STUDENT_ID"+
            " AND gcs.IS_DELETED = 'N' WHERE t.is_deleted = 'N'  AND gcs.CLASS_ID = :classId", nativeQuery = true)
    List<Object[]> findByClassTerms(@Param("classId") String classId);
}
