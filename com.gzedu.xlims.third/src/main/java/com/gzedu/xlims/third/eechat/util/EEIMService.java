/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.third.eechat.data.EEIMGroup;
import com.gzedu.xlims.third.eechat.data.EEIMGroupReturnData;
import com.gzedu.xlims.third.eechat.data.EEIMMembersEENO;
import com.gzedu.xlims.third.eechat.data.EEIMUpdateInfo;
import com.gzedu.xlims.third.eechat.data.EEIMUpdateInfoReturnData;
import com.gzedu.xlims.third.eechat.data.EEIMUserNew;
import com.gzedu.xlims.third.eechat.data.EEIMcreateUserDataNew;
import com.gzedu.xlims.third.eechat.data.EEIMcreateUserReturnData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年8月19日
 * @version 2.5
 *
 */
@Service
public class EEIMService {

	private static final Log log = LogFactory.getLog(EEIMService.class);

	@Value("#{configProperties['eeChatInterface']}")
	private String EEIMSERVER;

	// B10单个增加新的EE成员
	public EEIMcreateUserReturnData singleRemoteB10(String appId, List<EEIMUserNew> studentInfos) {
		EEIMcreateUserDataNew data = new EEIMcreateUserDataNew(appId, studentInfos);
		JSONObject jsonObject = JSONObject.fromObject(data);
		String url = EEIMSERVER + "/contactGroup/management/createEEIMUser.do";
		String result = EEIMHttpClientUtil.doHttpPost(url, jsonObject.toString());
		String context = "请求地址：" + url + ";<br>参数：" + jsonObject.toString() + ";<br>返回：" + result;
		log.info(context);
		System.out.println(context);
		try {
			JSONObject resultObject = JSONObject.fromObject(result);
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("membersEENOlist", EEIMMembersEENO.class);
			EEIMcreateUserReturnData returnData = (EEIMcreateUserReturnData) JSONObject.toBean(resultObject,
					EEIMcreateUserReturnData.class, classMap);
			return returnData;
		} catch (Exception e) {
		}
		return new EEIMcreateUserReturnData();
	}

	// C5新增群组
	public EEIMGroupReturnData remoteC5(List<EEIMGroup> eeimGroups) {

		JSONArray jsonObject = JSONArray.fromObject(eeimGroups);
		String url =EEIMSERVER + "/contactGroup/management/createGroup.do";
		String result = EEIMHttpClientUtil.doHttpPost(url, jsonObject.toString());
		String context = "请求地址：" + url + ";<br>参数：" + jsonObject.toString() + ";<br>返回：" + result;
		log.info(context);
		System.out.println(context);
		try {
			JSONObject resultObject = JSONObject.fromObject(result);
			EEIMGroupReturnData returnData = (EEIMGroupReturnData) JSONObject.toBean(resultObject,
					EEIMGroupReturnData.class);
			return returnData;
		} catch (Exception e) {
		}
		return new EEIMGroupReturnData();
		// {"MESSAGE":"操作成功!","GROUP_ID":"09b7acd4ae4843f7b277614bda7b7a2e","USER_NUMS":"2000","GROUP_EEIM_NO":"21519555","STATUS":"1"}
	}
	
   //D2修改个人信息
	public EEIMUpdateInfoReturnData updateDataD2(List<EEIMUpdateInfo> studentInfos){
		JSONArray jsonObject = JSONArray.fromObject(studentInfos);
		String url =EEIMSERVER + "/personal/management/updateData.do";
		String result = EEIMHttpClientUtil.doHttpPost(url, jsonObject.toString());
		String context = "请求地址：" + url + ";<br>参数：" + jsonObject.toString() + ";<br>返回：" + result;
		log.info(context);
		try {
			JSONObject resultObject = JSONObject.fromObject(result);
			EEIMUpdateInfoReturnData returnData=(EEIMUpdateInfoReturnData) JSONObject.toBean(resultObject,
					EEIMUpdateInfoReturnData.class);
			return returnData;
		} catch (Exception e) {
		}
		return new EEIMUpdateInfoReturnData();		
	}
	//解散群组
	public String destroyGroup(Map<String, Object> parms){
		JSONObject jsonObject = JSONObject.fromObject(parms);
		String url =EEIMSERVER + "/contactGroup/management/destroyGroup.do";
		String result = EEIMHttpClientUtil.doHttpPostNew(url, jsonObject.toString());
		String context = "请求地址：" + url + ";<br>参数：" + jsonObject.toString() + ";<br>返回：" + result;
		log.info(context);
		String status=null;
		try {
			JSONObject resultObject = JSONObject.fromObject(result);
			status=ObjectUtils.toString(resultObject.getString("Status"));
		} catch (Exception e) {
			e.printStackTrace();
			log.info(status);
		}
		return status;
	}
}
