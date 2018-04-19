package com.gzedu.xlims.third.weixin.send;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.third.weixin.IMessageSender;
import com.gzedu.xlims.third.weixin.MessageTemplate;

/**
 * 微信公众号消息发送<br/>
 */
public class MessageSenderService implements IMessageSender {

	private static Log logger = LogFactory.getLog(MessageSenderService.class);

	private Config config;

	@Override
	public boolean sendTemplete(String publicAccounts, String openid, String tplid,
			Map<String, Object> msgTemplateParams) {
		return false;
	}

	@Override
	public boolean sendCustomMsg(String publicAccounts, String openid, MessageTemplate msgTemplate,
			Object... msgTemplateParams) {
		if (StringUtils.isBlank(openid)) {
			return false;
		}
		Map params = new HashMap();
		params.put("myaction", "sendCustomMsg");
		params.put("uniacid", publicAccounts);
		// 消息内容，json格式
		Map msgdata = new HashMap();
		msgdata.put("touser", openid);
		msgdata.put("msgtype", "text");
		Map msgcontentdata = new HashMap();
		msgcontentdata.put("content", String.format(msgTemplate.getName(), msgTemplateParams));
		msgdata.put("text", msgcontentdata);
		String data = JSONObject.toJSONString(msgdata);
		params.put("data", data);
		String result = HttpClientUtils.doHttpPost(config.getUrl() + "/eeapi.php", params, 6000, Constants.CHARSET);

		JSONObject jsonObj = JSONObject.parseObject(result);
		if (jsonObj.getIntValue("status") == 200) {
			logger.info("sendCustomMsg: " + params + "," + result);
			return true;
		} else {
			logger.error("sendCustomMsg: " + params + ",error:" + result);
		}
		return false;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
