package com.gzedu.xlims.service.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ApiOucSyncService {
	
	/**
	 * 同步学习网数据
	 * @param response
	 * @param request
	 * @return
	 */
	public Map addAPPDataSynch(Map formMap);
}
