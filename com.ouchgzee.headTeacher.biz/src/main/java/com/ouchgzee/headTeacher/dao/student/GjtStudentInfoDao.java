/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.student;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;

/**
 * 学员信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated
@Repository("bzrGjtStudentInfoDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtStudentInfoDao extends BaseDao<BzrGjtStudentInfo, String> {

	/**
	 * 根据学员登录账号获取学员信息
	 * 
	 * @param loginAccount
	 * @return
	 */
	@Query("SELECT t FROM BzrGjtStudentInfo t INNER JOIN t.gjtUserAccount ua WHERE t.isDeleted='N' AND ua.isDeleted='N' AND ua.loginAccount=:loginAccount")
	BzrGjtStudentInfo findByLoginAccount(@Param("loginAccount") String loginAccount);

	/**
	 * 获取学员姓名
	 * 
	 * @param studentId
	 * @return
	 */
	@Query("SELECT t.xm FROM BzrGjtStudentInfo t WHERE t.studentId=:studentId")
	String findXmById(@Param("studentId") String studentId);

	/**
	 * 查询多个学员的ID、姓名、手机号、EE号
	 *
	 * @param studentIds
	 * @return
	 */
	@Query("SELECT DISTINCT t.studentId,t.xm,t.sjh,t.eeno FROM BzrGjtStudentInfo t WHERE t.isDeleted='N' AND t.studentId IN :studentIds")
	List<Object[]> findManyStudentInfoByIds(@Param("studentIds") List<String> studentIds);

	@Query(value = "select   t.student_id,t.xm,t.sjh,t.eeno,a.appid from gjt_student_info t inner join gjt_school_info a on "
			+ "t.xx_id=a.id where t.is_deleted='N' and t.account_id in :userIds", nativeQuery = true)
	List<Object[]> findManyStudentByuserIds(@Param("userIds") List<String> userIds);

	/**
	 * 班主任确认学员入学
	 * 
	 * @param studentId
	 * @param bzrId
	 * @return
	 */
	@Modifying
	@Transactional(value = "transactionManagerBzr")
	@Query(value = "UPDATE gjt_student_info t SET t.IS_ENTERING_SCHOOL ='1' WHERE t.STUDENT_ID=:studentId AND exists(SELECT gcs.STUDENT_ID FROM gjt_class_info gci, gjt_class_student gcs WHERE gci.class_id = gcs.class_id and gci.is_deleted = 'N' and gcs.is_deleted = 'N' and gci.class_type = 'teach' and gci.bzr_id = :bzrId and gcs.student_id=t.student_id)", nativeQuery = true)
	int enteringSchool(@Param("studentId") String studentId, @Param("bzrId") String bzrId);

	@Modifying
	@Transactional(value = "transactionManagerBzr")
	@Query(value = "SELECT ATID,sfzh FROM GJT_STUDENT_INFO WHERE ACCOUNT_ID IN :ids AND IS_DELETED='N'", nativeQuery = true)
	List<Object[]> queryAtidByIds(@Param("ids") List<String> ids);
}
