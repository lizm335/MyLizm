/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.constants;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzedu.xlims.common.Constants;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import com.ouchgzee.headTeacher.pojo.BzrPriRoleOperate;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.model.BzrPriRoleOperateService;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月2日
 * @version 2.5
 *
 */
public class ShiroDbRealm extends AuthorizingRealm {

	@Autowired
	BzrGjtUserAccountService userAccountService;

	@Autowired
	BzrPriRoleOperateService priRoleOperateService;

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken tokens) throws AuthenticationException {
		/*
		 * SimpleAuthenticationInfo authenticationInfo = null;
		 * 
		 * String username = (String) authcToken.getPrincipal(); GjtUserAccount
		 * user = userAccountService.findByLoginAccount(username);
		 * 
		 * if (user != null) { //
		 * 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配 //
		 * 当验证都通过后，把用户信息放在session里 Session session =
		 * SecurityUtils.getSubject().getSession();
		 * session.setAttribute("userSession", user);
		 * session.setAttribute("userSessionId", user.getId());
		 * 
		 * return new SimpleAuthenticationInfo(user, // 用户名 user.getPassword(),
		 * // 密码 getName() // realm name
		 * 
		 * ); } return null;
		 */
		String username = (String) tokens.getPrincipal();
		BzrGjtUserAccount user = userAccountService.findByTeacherLogin(username);
		SimpleAuthenticationInfo authenticationInfo = null;
		if (user == null) {
			throw new UnknownAccountException(); // 找不到
		} else {
			if(Constants.BOOLEAN_0.equals(user.getIsEnabled())) {
				throw new UnknownAccountException(); // 停用账号
			}
			// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
			authenticationInfo = new SimpleAuthenticationInfo(user.getLoginAccount(), // 用户名
					user.getPassword(), // 密码
					getName() // realm name
			);
		}
		// Session session = SecurityUtils.getSubject().getSession();
		// session.setAttribute("user", user);
		// session.setAttribute("userId", user.getId());
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
			BzrGjtUserAccount user = userAccountService.queryByLoginAccount(loginName);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

			if (user.getPriRoleInfo() != null) {
				info.addRole(user.getPriRoleInfo().getRoleName());

				List<BzrPriModelInfo> models = user.getPriRoleInfo().getPriModelInfos();
				if (models != null && models.size() > 0) {
					for (BzrPriModelInfo model : models) {
						if (model.getIsLeaf()) {
							info.addStringPermission(model.getModelAddress());

							List<BzrPriRoleOperate> roleOperates = priRoleOperateService
									.findByRoleIdAndModelId(user.getPriRoleInfo().getRoleId(), model.getModelId());
							if (roleOperates != null && roleOperates.size() > 0) {
								for (BzrPriRoleOperate roleOperate : roleOperates) {
									info.addStringPermission(model.getModelAddress() + "$"
											+ roleOperate.getPriOperateInfo().getOperateCode());
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

}
