package com.gzedu.xlims.service.remote;

import com.gzedu.xlims.common.*;
import com.gzedu.xlims.common.gzedu.SignUtil;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtSyncLog;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EnrollRemoteService {

	private static final Logger log = LoggerFactory.getLogger(EnrollRemoteService.class);

	@Autowired
	private GjtSyncLogService gjtSyncLogService;
	
	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Value("#{configProperties['api.enroll.domain']}")
	private String ENROLL_API_SERVER;
	private static final String ENROLL_GET_SIMPLE_ORDER_INFO = "/orderapi/getSimpleOrderInfo.html";
	private static final String ENROLL_UPDATE_USERINFO = "/userapi/updateUserInfo";

	// 测试使用
//	private static final String ENROLL_API_SERVER = "http://eapi.tt.oucnet.com";
//	private static final String ENROLL_GET_SIMPLE_ORDER_INFO = "/orderapi/getSimpleOrderInfo.html";
//	private static final String ENROLL_UPDATE_USERINFO = "/userapi/updateUserInfo";

	/**
	 * 获取简单订单信息
	 * 接口编号  :  0001113211  获取简单订单信息
	 * @param orderSn 报读订单号
	 * @return
	 */
	public EnrollOrderInfo getSimpleOrderInfo(String orderSn) {
		try {
			Map params = new HashMap();
			params.put("order_sn", orderSn); // 报读订单号

			// 额外参数;签名
			long time = System.currentTimeMillis();
//        	params.put("school_code", Objects.toString("041", ""));	// 院校code，非必填
			params.put("sign", SignUtil.formatUrlMap(params, time));
			params.put("appid", SignUtil.APPID);// APPID不需要参与加密
			params.put("time", String.valueOf(time));

			String result = HttpClientUtils.doHttpPost(ENROLL_API_SERVER + ENROLL_GET_SIMPLE_ORDER_INFO, params, 6000, "UTF-8");

			log.info("获取简单订单信息 [url:" + ENROLL_API_SERVER + ENROLL_GET_SIMPLE_ORDER_INFO + ", params: " + params + "]， 返回值为：" + result);

			if (StringUtils.isNotEmpty(result)) {
				JSONObject json = JSONObject.fromObject(result);
				int status = json.getInt("status");
				if (status == 1) {
					JSONObject dataJson = json.getJSONObject("data");
					EnrollOrderInfo data = GsonUtils.toBean(dataJson.toString(), EnrollOrderInfo.class);
					return data;
				} else {
					log.error("招生平台接口发生异常！msg=" + json.get("msg"));
				}
			}
		} catch (Exception e) {
			log.error("招生平台接口发生异常！" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 更新学员信息
	 * 接口编号  :  00036789  接收发生信息变更的用户，主动获取用户信息更新
	 * @param studentId
	 * @return
     */
	public boolean syncUpdateUserInfo(String studentId) {
		GjtStudentInfo info = gjtStudentInfoService.queryById(studentId);
		Map<String, String> params = new HashMap<String, String>();
		try {
			// 更新学员信息 # 编号  :  00036789
			params.put("student_id", studentId); // 学员ID
			// 额外参数;签名
			long time = DateUtils.getDate().getTime();
			if(info.getGjtSchoolInfo() != null) {
				params.put("school_code", info.getGjtSchoolInfo().getGjtOrg().getCode());    // 院校code，非必填
			}
			params.put("sign", SignUtil.formatUrlMap(params, time));
			params.put("appid", SignUtil.APPID);// APPID不需要参与加密
			params.put("time", String.valueOf(time));
			
			String result = HttpClientUtils.doHttpPost(ENROLL_API_SERVER + ENROLL_UPDATE_USERINFO, params, 6000, Constants.CHARSET);
			Map<String, Object> resultMap = GsonUtils.toBean(result, Map.class);
			if(resultMap != null) {
				int code = (int) NumberUtils.toDouble(Objects.toString(resultMap.get("status"), ""));
				if (code == 1) {
					// 成功也记录下
					gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0006, GsonUtils.toJson(params), "success" + result));
					return true;
				} else {
					// 记录同步失败日志
					gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0006, GsonUtils.toJson(params), result));
				}
			} else {
				// 记录同步失败日志
				gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0006, GsonUtils.toJson(params), result));
			}
		} catch (Exception e) {
			String objJson = GsonUtils.toJson(params);
			log.error("syncUpdateUserInfo fail ======== params:" + objJson);
			// 记录同步失败日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0006, objJson, e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 测试
	 * @param args
     */
	public static void main(String[] args) {
		EnrollRemoteService service = new EnrollRemoteService();
//		boolean flag = service.syncUpdateUserInfo("688f459cbb404bd4a62cbc39ae29a799");
//		System.out.println(flag);

		EnrollOrderInfo info = service.getSimpleOrderInfo("20160822588716");
		System.out.println(JSONObject.fromObject(info).toString());
	}
	
}
