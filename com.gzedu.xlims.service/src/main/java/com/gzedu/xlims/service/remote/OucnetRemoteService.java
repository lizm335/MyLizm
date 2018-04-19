package com.gzedu.xlims.service.remote;

import com.alibaba.fastjson.JSONObject;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 门户平台接口调用<br/>
 * http://www.oucnet.cn<br/>
 */
@Service
public class OucnetRemoteService {

	private static final Logger log = LoggerFactory.getLogger(OucnetRemoteService.class);

	private static final String HOST = AppConfig.getProperty("applicationPlatform.domain");
	private static final String IS_BINDING = AppConfig.getProperty("yunying.wx.isbinding");

//	// 测试使用
//	private static final String HOST = "http://test.oucnet.cn";
//	private static final String IS_BINDING = "/api/wechat/isbinding";

	/**
	 * 校验openId是否绑定门户的账号<br/>
	 * 查询是否已绑定 http://doc.oucnet.cn/index.php?s=/31&page_id=709<br/>
	 * @param openId
	 * @return
	 */
	public boolean checkBinding(String openId) {
		try {
			Map params = new HashMap();
			params.put("oid", openId);
			String result = HttpClientUtils.doHttpGet(HOST + IS_BINDING, params, 3000, "UTF-8");
			JSONObject jsonObj = JSONObject.parseObject(result);
			if(jsonObj.getIntValue("code") == 200) {
				log.info("the response is : " + result);
				JSONObject jsonData = jsonObj.getJSONObject("data");
				return StringUtils.equals(jsonData.getString("is_bind"), Constants.BOOLEAN_1);
			} else {
				log.error("the response error is : " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		OucnetRemoteService service = new OucnetRemoteService();
		System.out.println(service.checkBinding("o9I6xsywTcHsLo1mrC1AN9tj8U2g"));
	}

}
