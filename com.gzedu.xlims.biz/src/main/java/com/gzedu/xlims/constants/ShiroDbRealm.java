/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.constants;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriOperateInfo;
import com.gzedu.xlims.pojo.PriRoleOperate;
import com.gzedu.xlims.service.model.PriModelInfoService;
import com.gzedu.xlims.service.model.PriRoleOperateService;
import com.gzedu.xlims.service.systemManage.PriOperateInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

/**
 * 功能说明：个人中心登陆login，教务后台登陆login
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月2日
 * @version 2.5
 *
 */
public class ShiroDbRealm extends AuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(ShiroDbRealm.class);

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private PriRoleOperateService priRoleOperateService;

	@Autowired
	private PriModelInfoService priModelInfoService;

	@Autowired
	private PriOperateInfoService priOperateInfoService;

	private Integer userType;

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken tokens) throws AuthenticationException {
		String username = (String) tokens.getPrincipal();

		GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(username);
		SimpleAuthenticationInfo authenticationInfo = null;
		if (user == null) {
			user = gjtUserAccountService.queryBySfzh(username);// 学员的第二种情况
			if (user == null) {
				throw new UnknownAccountException(); // 找不到
			}
		} else if (!userType.equals(user.getUserType())) {
			throw new UnknownAccountException(); // 用户类型不匹配不能登陆
		} else if (!user.getIsEnabled()) {// 帐号被停用
			throw new DisabledAccountException();
		} else {
			if(!user.getIsEnabled()) {
				throw new UnknownAccountException(); // 停用账号
			}
			authenticationInfo = new SimpleAuthenticationInfo(username, // 用户名
					user.getPassword(), // 密码
					getName() // realm name
			);
		}

		return authenticationInfo;
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// log.info("------>>principals:" + principals);
		// GjtUserAccount ur = (GjtUserAccount)
		// principals.getPrimaryPrincipal();
		String loginName = SecurityUtils.getSubject().getPrincipal().toString();
		// log.info("------>>loginName:" + loginName);
		// Session session = SecurityUtils.getSubject().getSession();
		// GjtUserAccount user =
		// (GjtUserAccount)session.getAttribute(WebConstants.CURRENT_USER);
		if (loginName != null) {
			// if (user != null) {
			// GjtUserAccount user = gjtUserAccountService.findOne(ur.getId());
			GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(loginName);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

			if (user.getPriRoleInfo() != null) {
				info.addRole(user.getPriRoleInfo().getRoleName());

				if (user.getIsSuperMgr() != null && user.getIsSuperMgr()) {
					List<PriModelInfo> models = priModelInfoService.findAll();
					if (models != null && models.size() > 0) {
						for (PriModelInfo model : models) {
							if (model.getIsLeaf()) {
								info.addStringPermission(model.getModelAddress());

								List<PriOperateInfo> operateList = priOperateInfoService.queryAll();
								for (PriOperateInfo operate : operateList) {
									info.addStringPermission(model.getModelAddress() + "$" + operate.getOperateCode());
								}
							}
						}
					}
				} else {
					List<PriModelInfo> models = user.getPriRoleInfo().getPriModelInfos();
					if (models != null && models.size() > 0) {
						for (PriModelInfo model : models) {
							if (model.getIsLeaf()) {
								info.addStringPermission(model.getModelAddress());

								List<PriRoleOperate> roleOperates = priRoleOperateService
										.findByRoleIdAndModelId(user.getPriRoleInfo().getRoleId(), model.getModelId());
								if (roleOperates != null && roleOperates.size() > 0) {
									for (PriRoleOperate roleOperate : roleOperates) {
										info.addStringPermission(model.getModelAddress() + "$"
												+ roleOperate.getPriOperateInfo().getOperateCode());
									}
								}
							}
						}
					}
				}

			}

			return info;
		}

		return null;
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 更新用户信息缓存.
	 */
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	/**
	 * 清除用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	/**
	 * 清除用户信息缓存.
	 */
	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	/**
	 * 清空所有缓存
	 */
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	/**
	 * 清空所有认证缓存
	 */
	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

	/**
	 * 用户类型
	 * 
	 * @param userType
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}

}
