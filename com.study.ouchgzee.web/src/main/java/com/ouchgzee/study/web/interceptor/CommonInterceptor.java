package com.ouchgzee.study.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

public class CommonInterceptor extends HandlerInterceptorAdapter {

	private static final Log log = LogFactory.getLog(CommonInterceptor.class);

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("excute CommonInterceptor preHandle");
		Object currentUser = request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (currentUser == null) { // 解决/api接口没有userId参数问题
			String userId = request.getParameter("userId");
			if (userId == null || "".equals(userId)) {
				throw new CommonException(MessageCode.TOKEN_INVALID);
			}

			GjtUserAccount user = gjtUserAccountService.findOne(userId);
			if (user == null) {
				throw new CommonException(MessageCode.TOKEN_INVALID);
			}

			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
			request.getSession().setAttribute("userId", user.getId());

			if (user.getUserType() != null && user.getUserType() == 1 && user.getGjtStudentInfo() != null) {
				GjtStudentInfo studentInfo = user.getGjtStudentInfo();
				request.getSession().setAttribute(WebConstants.STUDENT_INFO, studentInfo);
				GjtClassInfo classInfo = gjtClassStudentService
						.queryTeachClassInfoByStudetnId(studentInfo.getStudentId());
				request.getSession().setAttribute(WebConstants.TEACH_CLASS, classInfo);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		/*
		 * log.info(modelAndView); ServletOutputStream outputStream =
		 * response.getOutputStream(); String s = "";
		 */
	}

}
