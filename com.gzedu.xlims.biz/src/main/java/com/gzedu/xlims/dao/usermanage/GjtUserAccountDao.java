/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.usermanage;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;

/**
 * 
 * 功能说明：用户帐号管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtUserAccountDao extends BaseDao<GjtUserAccount, String> {

	public List<GjtUserAccount> findByPriRoleInfo(PriRoleInfo role);

	/**
	 * 重置密码
	 * @param id
	 * @param mdPwd
	 * @param pwd2
     * @return
     */
	@Modifying
	@Transactional
	@Query("update GjtUserAccount g set g.password=?2,g.password2=?3,g.updatedDt=sysdate where g.id=?1 ")
	public int updatePwd(String id, String mdPwd, String pwd2);

	/**
	 * 帐号停用启用
	 * 
	 * @param id
	 * @param isEnabled
	 * @return
	 */
	@Query("update  GjtUserAccount g  set  g.isEnabled=?2 where  g.id=?1")
	public int updateIsEnabled(String id, Integer isEnabled);

	/**
	 * 帐号校验
	 */
	public GjtUserAccount findByLoginAccount(String loginAccount);

	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtUserAccount g set g.isDeleted=?2 where g.id=?1 ")
	public int deleteById(String id, String str);

	@Modifying
	@Transactional
	@Query("update GjtUserAccount g set g.priRoleInfo.roleId=?1 where g.id=?2")
	public int updateUserRole(String roleId, String userId);

	@Modifying
	@Transactional
	@Query("update GjtUserAccount g set g.gjtOrg=?1 where g.id=?2")
	public int updateUserOrg(GjtOrg gjtOrg, String userId);

	/**
	 * 记录登陆次数，是否在线，最后登陆时间,当前登陆时间，已经当前登陆所用到的sessionId
	 * 
	 * @param id
	 * @param loginCount
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtUserAccount g set g.loginCount=:loginCount,g.isOnline='Y',g.lastLoginTime=sysdate,g.currentLoginTime=sysdate,"
			+ "currentLoginIp=:sessionId  where g.id=:userId ")
	public int updateLoginState(@Param("userId") String id, @Param("loginCount") BigDecimal loginCount,
			@Param("sessionId") String sessionId);

	/**
	 * 关闭浏览器，重新登陆时的场景
	 * 
	 * @param userId
	 * @param loginCount
	 * @param sessionId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtUserAccount g set g.loginCount=:loginCount,g.lastLoginTime=sysdate,"
			+ "g.currentLoginIp=:sessionId  where g.id=:userId ")
	public int updateSessionId(@Param("userId") String userId, @Param("loginCount") BigDecimal loginCount,
			@Param("sessionId") String sessionId);

	/**
	 * 退出的时候更改在线状态
	 * 
	 * @param id
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtUserAccount g set g.isOnline='N',g.endDate=sysdate  where g.id=:userId ")
	public int updateQuitState(@Param("userId") String id);
	
	/**
	 * 退出的时候更改在线状态
	 * 
	 * @param id
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtUserAccount g set g.isOnline='N',g.endDate=sysdate  where g.currentLoginIp=:sessionId ")
	public int updateQuitStateBySessionId(@Param("sessionId") String sessionId);

	@Query(value = "select distinct id from gjt_user_account u where u.role_id=:roleId and u.is_deleted='N' and is_enabled='1' and org_id in "
			+ "(select id from gjt_org where is_deleted='N' start with id = :orgId connect by prior id = perent_id)  ", nativeQuery = true)
	List<String> findUserIdByRoleId(@Param("roleId") String roleId, @Param("orgId") String orgId);

	@Query(value = "select CURRENT_LOGIN_IP from gjt_user_account where IS_ONLINE='Y'  and USER_TYPE=:type", nativeQuery = true)
	List<String> findSeesionId(@Param("type") String type);

	@Query(value = "select * from gjt_user_account where is_deleted='N' and "
			+ " id=(select account_id from gjt_student_info where is_deleted='N' and sfzh=?1) ", nativeQuery = true)
	public GjtUserAccount findBySfzh(String sfzh);
	
	/**
	 * 根据微信OpenId获取后台管理员账号信息
	 * @param wxOpenId
	 * @return
	 */
	@Query(value = "SELECT t from GjtUserAccount t where t.isDeleted='N' and t.userType=0 and t.wxOpenId=?1")
	GjtUserAccount queryAdminUserByWxOpenId(String wxOpenId);
	
	/**
	 * 根据sjh获取后台管理员账号信息
	 * @param sjh
	 * @return
	 */
	@Query(value = "SELECT t from GjtUserAccount t where t.isDeleted='N' and t.userType=0 and t.sjh=?1")
	GjtUserAccount queryAdminUserBySjh(String sjh);

}
