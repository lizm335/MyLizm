/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.constants.shiro.filter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;

import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月17日
 * @version 2.5
 *
 */
public class SysUserFilter extends PathMatchingFilter {

	@Resource
	private GjtUserAccountService gjtUserAccountService;

	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {

		String username = (String) SecurityUtils.getSubject().getPrincipal();
		request.setAttribute("user", gjtUserAccountService.queryByLoginAccount(username));
		return true;
	}
}
