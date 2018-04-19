/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.pojo.BzrTblPriLoginLog;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 用户账号业务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月4日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated
public interface BzrGjtUserAccountService extends BaseService<BzrGjtUserAccount> {

	/**
	 * 根据ID查询账号信息
	 *
	 * @param accountId
	 * @return
	 */
	BzrGjtUserAccount findOne(String accountId);

	/**
	 * 根据班主任的登录账号获取账号信息
	 *
	 * @param username
	 * @return
	 */
	BzrGjtUserAccount findByTeacherLogin(String username);

	Page<BzrGjtUserAccount> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 查询帐号是否存在
	 * 
	 * @param loginAccount
	 * @return
	 */
	BzrGjtUserAccount queryByLoginAccount(String loginAccount);

	/**
	 * 用户首次登录平台时间
	 * 
	 * @param loginAccount
	 * @return
	 */
	Date getFirstLoginByLoginAccount(String loginAccount);

	void update(BzrGjtUserAccount entity);

	// 删除（真）
	void delete(List<String> ids);

	/**
	 * 分页根据条件查询账号登录日志信息
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrTblPriLoginLog> queryLoginLogByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 根据学员查他的全部考勤信息
	 * 
	 * @param searchParams
	 * @return
	 */
	List<BzrTblPriLoginLog> queryAllLoginLog(Map<String, Object> searchParams);

	// 登陆时记录在线,当前时间，最后登陆时间，和当前登陆sessionId
	boolean updateLoginState(String id, BigDecimal loginCount, String sessionId);

	// 退出的时候更改在线状态
	boolean updateQuitState(String id);

	// 更改sessionID和最后登陆时间还有登陆次数
	boolean updateSessionId(String userId, BigDecimal loginCount, String sessionId);

	/**
	 * 学员模拟登陆URL生产
	 * 
	 * @param studentId
	 * @param xh
	 * @return
	 */
	public String studentSimulation(String studentId, String xh);

	/**
	 * 学员模拟登陆新
	 * 
	 * @param appId
	 *            在 SSOUtil
	 * @param studentId
	 * @param xh
	 * @param type，t=1
	 *            跳转到PC学籍资料完善，t=2 跳转到PC学员个人详情，t=101 跳转到H5学籍资料完善
	 * @return
	 */
	public String studentSimulationNew(String appId, String studentId, String xh, String type);

}
