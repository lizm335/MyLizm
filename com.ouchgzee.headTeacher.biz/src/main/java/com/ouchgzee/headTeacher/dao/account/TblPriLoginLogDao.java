/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.account;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrTblPriLoginLog;

/**
 * 用户登入日志信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月4日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrTblPriLoginLogDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface TblPriLoginLogDao extends BaseDao<BzrTblPriLoginLog, String> {

	/**
	 * 用户首次登录平台时间
	 * 
	 * @param loginAccount
	 * @return
	 */
	@Query("SELECT MIN(t.createdDt) FROM BzrTblPriLoginLog t WHERE t.isDeleted='N' AND t.username=:loginAccount")
	Date findFirstLoginByLoginAccount(@Param("loginAccount") String loginAccount);

	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrTblPriLoginLog t set t.loginTime=:loginTime where t.loginSession=:sessionId ")
	public int updateBySessionId(@Param("sessionId") String sessionId, @Param("loginTime") BigDecimal loginTime);

	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrTblPriLoginLog t set t.loginSession=:newSession where t.loginSession=:sessionId ")
	public int updateNewSessionByOldSession(@Param("sessionId") String oldSessionId,
			@Param("newSession") String newSession);

}
