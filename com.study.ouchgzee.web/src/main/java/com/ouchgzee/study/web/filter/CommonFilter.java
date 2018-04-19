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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.ResponseModel;
import com.gzedu.xlims.common.exception.CommonException;

//@Component("commonFilter")
public class CommonFilter implements Filter {
	
	private static final Log log = LogFactory.getLog(CommonFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest request = ((HttpServletRequest) req);
			String url = request.getServletPath();
			if(url.contains("/pcenter") || url.contains("/wx")) {
				// 如果请求html,不拦截response的write方法
				// 如果返回的是流则过滤掉
				if(url.contains("html") || url.endsWith("ToFile") || url.endsWith("expAdmissionTicket") || url.endsWith("downGradesExcel")) {
					chain.doFilter(request, response);
				} else {
					chain.doFilter(request, new FilterResponse((HttpServletResponse)response));
					try {
						ResponseModel rm = new ResponseModel(MessageCode.RESP_OK.getMsgCode(), 0, MessageCode.RESP_OK.getMessage());
						this.writeJson(response, rm);
					} catch (Exception e) {
						
					}
				}
			} else {
				chain.doFilter(request, response);
				
			}
		}catch (Exception e) {
			ResponseModel rm;
			if (e.getCause() != null) {
				Throwable cause = e.getCause();
				if (cause instanceof CommonException) {
					CommonException ce = (CommonException)cause;
					if (ce.getMessageCode() != null) {
						rm = new ResponseModel(ce.getMessageCode(), ce.getErrorMessage());
					} else {
						rm = new ResponseModel(ce.getBusCode(), ce.getErrorMessage());
					}
				} else {
					log.error(e.getMessage(), e);
					rm = new ResponseModel(MessageCode.SYSTEM_ERROR.getMsgCode(), 0, MessageCode.SYSTEM_ERROR.getMessage());
				}
			} else {
				if (e instanceof CommonException) {
					CommonException ce = (CommonException)e;
					if (ce.getMessageCode() != null) {
						rm = new ResponseModel(ce.getMessageCode(), ce.getErrorMessage());
					} else {
						rm = new ResponseModel(ce.getBusCode(), ce.getErrorMessage());
					}
				} else {
					log.error(e.getMessage(), e);
					rm = new ResponseModel(MessageCode.SYSTEM_ERROR.getMsgCode(), 0, MessageCode.SYSTEM_ERROR.getMessage());
				}
			}
			
			this.writeJson(response, rm);
		}
	}

	@Override
	public void destroy() {
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
			if( pw != null ){
				pw.close();
			}
		}
	}

}
