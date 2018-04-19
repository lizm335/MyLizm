/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.usermanage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.GjtUserBehavior;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 
 * 功能说明：用户帐号管理接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtUserAccountService extends BaseService<GjtUserAccount> {

	GjtUserAccount findOne(String id);

	GjtUserAccount saveEntity(String xm, String loginAccount, String orgId, Integer userType);

	// 重置密码
	int updatePwd(String id, String mdPwd, String pwd2);

	// 启用停用
	int updateIsEnabled(String id, Integer isEnabled);

	// 查询帐号是否存在
	Boolean existsByLoginAccount(String loginAccount);

	// 删除（假）
	Boolean deleteById(String[] id);

	Boolean deleteById(String id);

	// 删除（真）
	void delete(List<String> id);

	void update(GjtUserAccount entity);

	// 根据登陆帐号查询对象
	GjtUserAccount queryByLoginAccount(String loginAccount);

	GjtUserAccount queryBySfzh(String sfzh);

	Page<Map<String, Object>> queryPage(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 查询工作统计，带分页
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryWorkStatisticsByPage(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	/**
	 * 查询工作统计
	 * 
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	List<Map<String, Object>> queryWorkStatisticsBy(String orgId, Map<String, Object> searchParams);

	/**
	 * COUNT工作统计
	 * 
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	long countWorkStatisticsBy(String orgId, Map<String, Object> searchParams);

	/**
	 * COUNT工作统计-答疑服务
	 * 
	 * @param accountId
	 * @return
	 */
	Map<String, Object> countWorkStatisticsMutualSubjectByAccountId(String accountId);

	/**
	 * COUNT工作统计-统计登录情况
	 * 
	 * @param accountId
	 * @return earliestLoginDt-最早一次登录 latestLogoutDt-最晚一次退出 totalLoginNum-总登录次数
	 *         totalLoginTime-总登录时长（单位：分） recentlyDt-最晚一次登录
	 */
	Map<String, Object> countProLoginLogByAccountId(String accountId, Map<String, Object> searchParams);

	/**
	 * 导出工作统计表
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param path
	 * @return
	 */
	String exportWorkStatistics(String orgId, Map searchParams, String path);

	/**
	 * 导出工作统计明细表
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param path
	 * @return
	 */
	String exportWorkStatisticsDetail(String orgId, Map searchParams, String path);

	/**
	 * 导出登录情况明细表
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param path
	 * @return
	 */
	String exportLoginSituation(String orgId, Map searchParams, String path);

	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月19日 下午5:38:19
	 * @param roleId
	 * @return
	 */
	Map<String, String> findUserByRoleId(String roleId, String orgId);

	// 登陆时记录在线,当前时间，最后登陆时间，和当前登陆sessionId
	boolean updateLoginState(String id, BigDecimal loginCount, String sessionId);

	// 退出的时候更改在线状态
	boolean updateQuitState(String id);

	// 更改sessionID和最后登陆时间还有登陆次数
	boolean updateSessionId(String userId, BigDecimal loginCount, String sessionId);

	// 根据角色查询用户
	List<String> findUserIdByRoleId(String roleId, String orgId);

	/**
	 * 学员模拟登陆URL生产
	 * 
	 * @param studentId
	 * @param xh
	 * @return
	 */
	String studentSimulation(String studentId, String xh);

	/**
	 * 辅导老师模拟登陆
	 * 
	 * @param employeeId
	 * @return
	 */
	String coachTeacherSimulation(String employeeId);

	/**
	 * 督导老师模拟登陆
	 * 
	 * @param teachPlanId
	 * @param classId
	 * @param employeeId
	 * @return
	 */
	String supervisorTeacherSimulation(String teachPlanId, String classId, String employeeId);

	/**
	 * 班主任模拟登陆
	 * 
	 * @param loginAccount
	 * @param employeeId
	 * @return
	 */
	String headTeacherSimulation(String loginAccount, String employeeId, String courseClassId);

	/******************************************* 用户行为 *******************************************/

	/**
	 * 分页根据条件查询用户行为信息
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<GjtUserBehavior> queryUserBehaviorByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 获取用户有行为的时间
	 * 
	 * @param userId
	 * @return
	 */
	List<String> queryAllDate(String userId);

	/**
	 * 添加用户行为
	 * 
	 * @param entity
	 * @return
	 */
	boolean saveUserBehavior(GjtUserBehavior entity);

	boolean updateQuitStateBySessionId(String sessionId);

	/**
	 * 根据微信OpenId获取后台管理员账号信息
	 * 
	 * @param wxOpenId
	 * @return
	 */
	GjtUserAccount queryAdminUserByWxOpenId(String wxOpenId);

	/**
	 * 根据sjh获取后台管理员账号信息
	 * 
	 * @param sjh
	 * @return
	 */
	GjtUserAccount queryAdminUserBySjh(String sjh);

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

	/**
	 * @param orgId
	 * @param roleIds
	 * @return
	 */
	List<Map<String, Object>> queryUserRoleByOrg(String orgId, List<String> roleIds);

}
