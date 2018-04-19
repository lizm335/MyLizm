package com.gzedu.xlims.serviceImpl.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JSONArray;
import com.gzedu.xlims.common.json.JSONObject;
import com.gzedu.xlims.common.wcf.org.tempuri.APPDataSynchService;
import com.gzedu.xlims.common.wcf.org.tempuri.IAPPDataSynchService;
import com.gzedu.xlims.service.api.ApiOucSyncService;

@Service
public class ApiOucSyncServiceImpl implements ApiOucSyncService {
	
	/**
	 * 同步学习网数据
	 * @param operatingUserName	操作人账号
	 * @param functionType	          同步业务方法
	 * @param synchDATA         同步业务数据
	 * @return
	 */
	public Map addAPPDataSynch(Map formMap) {
		Map resultMap = new HashMap();
		try {
			APPDataSynchService syncService = new APPDataSynchService();
			IAPPDataSynchService isyncService = syncService.getBasicHttpBindingIAPPDataSynchService();
			
			String operatingUserName = ObjectUtils.toString(formMap.get("operatingUserName")); // 操作人账号
			String functionType = ObjectUtils.toString(formMap.get("functionType"));		   // 同步业务方法
			
			// 同步业务数据
			String synchDATA = "";
			if(formMap.get("synchDATA") instanceof Map){
				JSONObject jsonObject = new JSONObject((Map)formMap.get("synchDATA"));
				synchDATA =jsonObject.toString();
			} else {
				JSONArray jsonArray = new JSONArray((List)formMap.get("synchDATA"));
				synchDATA =jsonArray.toString();
			}
			
			// 双方约定MD5加密字符 OUCHN_ONLINE@SPACE#2018
			String key = "c9f2effbcee602697c0dc1847a56aa8f";
			Holder<String> dataSynchResult = new Holder<String>();
			Holder<String> outMSG = new Holder<String>();
			isyncService.dataSynch(operatingUserName, functionType, key, synchDATA, dataSynchResult, outMSG);
			String returnStr = outMSG.value;
			
			if (EmptyUtils.isNotEmpty(returnStr)) {
				resultMap = (Map)JSON.parse(returnStr);
				String statusJson = ObjectUtils.toString(resultMap.get("status"));
				resultMap.putAll((Map)JSON.parse(statusJson));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	public static void main(String[] args) {
		Map formMap = new HashMap();
		Map teachMap = new HashMap();
		teachMap.put("OrgCode", "44133");
		teachMap.put("UserName", "lxjx005");
		teachMap.put("RealName", "蓝星教学管理员");
		teachMap.put("TeacherNO", "lxjx005");
		teachMap.put("Password", "jx@888888");
		teachMap.put("EMAIL", "lxjx005@gzedu.com");
		
		formMap.put("synchDATA", teachMap);
		formMap.put("operatingUserName", "");
		formMap.put("functionType", "SynchTeacher");
		ApiOucSyncServiceImpl api = new ApiOucSyncServiceImpl();
		Map jsonMsg = api.addAPPDataSynch(formMap);
		System.out.println(jsonMsg.toString());
	}
}
