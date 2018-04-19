package com.gzedu.xlims.service.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ApiService {
	
	/***
	 * 缴费记录
	 * @param request
	 * @return
	 */
	public Map addFeesRecord(HttpServletRequest request);
}
