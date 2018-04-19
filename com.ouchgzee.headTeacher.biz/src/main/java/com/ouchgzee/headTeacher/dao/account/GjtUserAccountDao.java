/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.pojo.BzrPriRoleInfo;

/**
 * 账号信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtUserAccountDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtUserAccountDao extends BaseDao<BzrGjtUserAccount, String> {

	public List<BzrGjtUserAccount> findByPriRoleInfo(BzrPriRoleInfo role);

	/**
	 * 根据登录账号获取账号信息
	 * 
	 * @param loginAccount
	 * @return
	 */
	BzrGjtUserAccount findByLoginAccount(String loginAccount);

	/**
	 * 根据登录账号获取账号信息
	 *
	 * @param loginAccount
	 * @return
	 */
	@Query("SELECT t FROM BzrGjtUserAccount t WHERE t.isDeleted='N' AND t.userType=2 AND t.loginAccount=:loginAccount")
	BzrGjtUserAccount findByTeacherLoginAccount(@Param("loginAccount") String loginAccount);

	/**
	 * 更改账号密码
	 * 
	 * @param accountId
	 * @param updatedDt
	 * @param updatedBy
	 * @param password
	 * @param password2
	 * @return
	 */
	@Modifying
	@Query("UPDATE BzrGjtUserAccount u SET u.version=u.version+1,u.password=:password,u.password2=:password2,u.updatedBy=:updatedBy,u.updatedDt=:updatedDt WHERE u.id=:accountId")
	@Transactional(value="transactionManagerBzr")
	int updatePwd(@Param("accountId") String accountId, @Param("password") String password,
			@Param("password2") String password2, @Param("updatedBy") String updatedBy,
			@Param("updatedDt") Date updatedDt);

	/**
	 * 更改用户的角色
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 */
	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrGjtUserAccount g set g.priRoleInfo.roleId=?1 where g.id=?2")
	int updateUserRole(String roleId, String userId);

	/**
	 * 记录登陆次数，是否在线，最后登陆时间
	 * 
	 * @param id
	 * @param loginCount
	 * @return
	 */
	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrGjtUserAccount g set g.loginCount=:loginCount,g.isOnline='Y',g.lastLoginTime=sysdate,g.currentLoginTime=sysdate,"
			+ "currentLoginIp=:sessionId  where g.id=:userId ")
	public int updateLoginState(@Param("userId") String id, @Param("loginCount") BigDecimal loginCount,
			@Param("sessionId") String sessionId);

	/**
	 * 退出的时候更改在线状态
	 * 
	 * @param id
	 * @return
	 */
	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrGjtUserAccount g set g.isOnline='N'  where g.id=:userId ")
	public int updateQuitState(@Param("userId") String id);

	/**
	 * 关闭浏览器，重新登陆时的场景
	 * 
	 * @param id
	 * @param loginCount
	 * @param sessionId
	 * @return
	 */
	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrGjtUserAccount g set g.loginCount=:loginCount,g.lastLoginTime=sysdate,"
			+ "g.currentLoginIp=:sessionId  where g.id=:userId ")
	public int updateSessionId(@Param("userId") String userId, @Param("loginCount") BigDecimal loginCount,
			@Param("sessionId") String sessionId);

	@Query(value = "select CURRENT_LOGIN_IP from gjt_user_account where IS_ONLINE='Y' and USER_TYPE='2' ", nativeQuery = true)
	List<String> findSeesionId();

}
