/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.feedback;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeedback;

/**
 * 留言反馈信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月31日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtFeedbackDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtFeedbackDao extends BaseDao<BzrGjtFeedback, String> {

	/**
	 * 留言反馈信息转发给其他老师
	 * 
	 * @param id
	 * @param shareEmployeeId
	 * @return
	 */
	// @Modifying
	// @Query("UPDATE GjtFeedback t SET
	// t.gjtEmployeeInfo.employeeId=:shareEmployeeId WHERE t.id=:id")
	// @Transactional(value="transactionManagerBzr")
	// int shareTeacher(@Param("id") String id, @Param("shareEmployeeId") String
	// shareEmployeeId);

	/**
	 * 留言反馈标记为已处理
	 * 
	 * @param id
	 * @return
	 */
	@Modifying
	@Query("UPDATE BzrGjtFeedback t SET t.dealResult='Y' WHERE t.id=:id")
	@Transactional(value="transactionManagerBzr")
	int updateAnswer(@Param("id") String id);

	List<BzrGjtFeedback> findByPidOrderByCreatedDtAsc(String pId);

}
