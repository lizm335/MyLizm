package com.gzedu.xlims.dao.systemManage;

import java.math.BigDecimal;

import com.gzedu.xlims.dao.base.BaseDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.TblPriLoginLog;

/**
 * 
 * 功能说明：登陆日志
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月5日
 * @version 2.5
 *
 */
public interface TblPriLoginLogDao extends BaseDao<TblPriLoginLog, String>{
	@Modifying
	@Transactional
	@Query("update TblPriLoginLog t set t.loginTime=:loginTime,t.updatedDt=sysdate where t.loginSession=:sessionId ")
	public int updateBySessionId(@Param("sessionId") String sessionId, @Param("loginTime") BigDecimal loginTime);

	@Modifying
	@Transactional
	@Query("update TblPriLoginLog t set t.loginSession=:newSession where t.loginSession=:sessionId ")
	public int updateNewSessionByOldSession(@Param("sessionId") String oldSessionId,
			@Param("newSession") String newSession);
}
