/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * 
 * 功能说明： 超级管理员的拦截器
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月27日
 * @version 2.5
 *
 */
public class AdminInterceptor extends HandlerInterceptorAdapter {
	private final Logger log = LoggerFactory.getLogger(AdminInterceptor.class);

	/*
	 * 利用正则映射到需要拦截的路径
	 * 
	 * private String mappingURL;
	 * 
	 * public void setMappingURL(String mappingURL) { this.mappingURL =
	 * mappingURL; }
	 */
	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		// log.info("requestUri:" + requestUri);
		// log.info("contextPath:" + contextPath);
		// log.info("url:" + url);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (user.getIsSuperMgr()) {// 如果是超级管理员，则url 追加/admin 前缀
			request.getRequestDispatcher("/admin" + url).forward(request, response);
			return false;
		}
		return true;
	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// log.info("==============执行顺序: 2、postHandle================");
		// if (modelAndView != null) { // 加入当前时间
		// modelAndView.addObject("var", "测试postHandle");
		// }
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// log.info("==============执行顺序: 3、afterCompletion================");
	}

}
