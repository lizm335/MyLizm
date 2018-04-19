/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.constants.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月17日
 * @version 2.5
 *
 */
public class LoginFormAuthenticationFilter extends FormAuthenticationFilter {

	protected static final Logger logger = Logger.getLogger(LoginFormAuthenticationFilter.class);
	
	private String AJAX_DISPATCHER_URL = "/loginForSessionTimeOut";//"/loginForNotLogged";

	@Autowired
	private GjtUserAccountService gjtUserAccountService;
	
	@Autowired
	private GjtClassStudentService gjtClassStudentService;
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		boolean contextRelative = true;
		String successUrl = this.getSuccessUrl();
		if ("".equals(successUrl)) {
			successUrl = DEFAULT_SUCCESS_URL;
		}
		String username = (String) token.getPrincipal();
		GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(username);
		if(user == null) {
			user = gjtUserAccountService.queryBySfzh(username);
			if(user == null) {
				throw new UnknownAccountException(); // 找不到
			}
		}
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute(WebConstants.CURRENT_USER, user);
		session.setAttribute("userId", user.getId());

		if (user.getUserType() != null && user.getUserType() == 1 && user.getGjtStudentInfo() != null) {
			GjtStudentInfo studentInfo = user.getGjtStudentInfo();
			session.setAttribute(WebConstants.STUDENT_INFO, studentInfo);

			GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(studentInfo.getStudentId());
			session.setAttribute(WebConstants.TEACH_CLASS, classInfo);
		}
		WebUtils.issueRedirect(request, response, successUrl, null, contextRelative);

		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		String requestUrl = WebUtils.toHttp(request).getRequestURI();
		logger.info("request url："+requestUrl.toString());
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		String vCode = (String) session.getAttribute("vCode");
		String rCode = httpRequest.getParameter("rCode");
		logger.info("----------------->>>>>>>>>>>>>>>vCode:"+vCode+",rCode:"+rCode);
		if(vCode != null && rCode != null && !vCode.equals(rCode)) {
			httpRequest.setAttribute("shiroLoginFailure", "randomCodeError");
			return true;
		}
		if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                	logger.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                	logger.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            String requestedWith = WebUtils.toHttp(request).getHeader("x-requested-with");
    		if(requestedWith != null) {
    			((HttpServletResponse)response).sendRedirect(AJAX_DISPATCHER_URL);
    			//request.getRequestDispatcher(AJAX_DISPATCHER_URL).forward(request, response); // WebUtils.issueRedirect(request, response, loginUrl);
    			return false;
    		} else {
    			if (logger.isTraceEnabled()) {
                	logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                            "Authentication url [" + getLoginUrl() + "]");
                }
    			saveRequestAndRedirectToLogin(request, response);
    			return false;
    		}
        }
		
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
