package com.gzedu.xlims.third.sms;

import java.util.Map;

public class SmsSenderManager {

	private Map<Integer, SmsSender> smsSenderMap;

	public String smsSend(SmsSenderType smsSenderType, Map<String, Object> params) {
		return smsSenderMap.get(smsSenderType.getCode()).send( params);
	}

	public Map<Integer, SmsSender> getSmsSenderMap() {
		return smsSenderMap;
	}

	public void setSmsSenderMap(Map<Integer, SmsSender> smsSenderMap) {
		this.smsSenderMap = smsSenderMap;
	}


}
