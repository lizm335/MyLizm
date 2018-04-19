/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.constants.shiro.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.gzedu.xlims.common.Md5Util;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 功能说明：
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月17日
 * @version 2.5
 *
 */
public class LoginFormAuthenticationFilter extends FormAuthenticationFilter{
    
   protected final Logger logger = Logger.getLogger(LoginFormAuthenticationFilter.class);


	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
									 ServletResponse response) throws Exception {
		boolean contextRelative = true;
		String successUrl = this.getSuccessUrl();
		if ("".equals(successUrl)) {
			successUrl = DEFAULT_SUCCESS_URL;
		}

		WebUtils.issueRedirect(request, response, successUrl, null, contextRelative);

		return false;
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        if (StringUtils.isNotEmpty(password)) {
			try {
				password = Md5Util.encrypt(getPassword(request));
			} catch (Exception e) {
			}
        }
        return createToken(username, password, request, response);
    }
}
