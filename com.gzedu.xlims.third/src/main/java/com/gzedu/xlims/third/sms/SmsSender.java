package com.gzedu.xlims.third.sms;

import java.util.Map;
import java.util.Set;

public interface SmsSender {
	
	/**
	 * 发送模板
	 * @param params
	 * @return
	 */
	String send(Map<String, Object> params);

}
