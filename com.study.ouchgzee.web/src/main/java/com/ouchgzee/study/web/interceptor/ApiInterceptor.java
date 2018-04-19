package com.ouchgzee.study.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.ResponseModel;

/**
 * 解决使用路径 “/api/**”访问void返回值的方法时没有返回码的问题
 * @author eenet09
 *
 */
public class ApiInterceptor extends HandlerInterceptorAdapter {
	
	private static final Log log = LogFactory.getLog(ApiInterceptor.class);

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Object isApiForward = request.getAttribute("isApiForward");
		if (isApiForward != null)  {
			try {
				ResponseModel rm = new ResponseModel(MessageCode.RESP_OK.getMsgCode(), 0, MessageCode.RESP_OK.getMessage());
				this.writeJson(response, rm);
			} catch (Exception e) {
				
			}
		}
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
