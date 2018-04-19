package com.ouchgzee.study.web.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.ResponseModel;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

@Component("apiFilter")
public class ApiFilter implements Filter {

	private static final Log log = LogFactory.getLog(ApiFilter.class);

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getServletPath();
		// 接口studentId放到请求体，如果之后的人有时间优化建议使用token机制
		if (url.startsWith("/api")) {
			GjtUserAccount user;
			String userId = request.getParameter("userId");
			userId = userId != null ? userId : ((HttpServletRequest) request).getHeader("userId");
			if (userId == null || "".equals(userId)) {
				log.info("userId is null");

				String studentId = request.getParameter("studentId");
				studentId = studentId != null ? studentId : ((HttpServletRequest) request).getHeader("studentId");
				if (studentId == null || "".equals(studentId)) {
					log.info("studentId is null");

					String atid = request.getParameter("atid");
					atid = atid != null ? atid : ((HttpServletRequest) request).getHeader("atid");
					if (atid == null || "".equals(atid)) {
						log.info("atid is null");
						this.throwCommonException(response, "userId and studentId and atid is null");
						return;
					} else {
						GjtStudentInfo studentInfo = gjtStudentInfoService.queryByAtid(atid);
						if (studentInfo == null) {
							this.throwCommonException(response, "can not found student by atid");
							return;
						}
						user = studentInfo.getGjtUserAccount();
					}
				} else {
					GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
					if (studentInfo == null) {
						this.throwCommonException(response, "can not found student by studentId");
						return;
					}
					user = studentInfo.getGjtUserAccount();
				}

			} else {
				user = gjtUserAccountService.findOne(userId);
			}

			if (user == null) {
				this.throwCommonException(response, "can not found user info");
				return;
			}

			req.getSession().setAttribute(WebConstants.CURRENT_USER, user);
			req.getSession().setAttribute("userId", user.getId());

			if (user.getUserType() != null && user.getUserType() == 1 && user.getGjtStudentInfo() != null) {
				GjtStudentInfo studentInfo = user.getGjtStudentInfo();
				req.getSession().setAttribute(WebConstants.STUDENT_INFO, studentInfo);
				GjtClassInfo classInfo = gjtClassStudentService
						.queryTeachClassInfoByStudetnId(studentInfo.getStudentId());
				req.getSession().setAttribute(WebConstants.TEACH_CLASS, classInfo);
			}

			url = "/pcenter" + url.substring(4);
			req.setAttribute("isApiForward", true);
			req.getRequestDispatcher(url).forward(request, new FilterResponse((HttpServletResponse) response));
		} else {
			chain.doFilter(request, new FilterResponse((HttpServletResponse) response));
		}
	}

	@Override
	public void destroy() {
	}

	private void throwCommonException(ServletResponse response, String errorMsg) {
		ResponseModel rm = new ResponseModel(MessageCode.TOKEN_INVALID.getMsgCode(), 0, errorMsg);
		this.writeJson(response, rm);
	}

	private void writeJson(ServletResponse response, ResponseModel rm) {
		PrintWriter pw = null;
		Gson gson = new GsonBuilder().serializeNulls().create();
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			pw = response.getWriter();
			pw.write(gson.toJson(rm));
			pw.flush();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

}
