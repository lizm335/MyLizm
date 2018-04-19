package com.gzedu.xlims.service.remote;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.gzedu.SignUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BillRemoteNewService {

	private static final Logger log = LoggerFactory.getLogger(BillRemoteNewService.class);

	private static final String PAY_NEWSERVER_DOMAIN = AppConfig.getProperty("pay.newServerDomain");
	private static final String PAY_GETORDERBILLINFO = AppConfig.getProperty("pay.getOrderBillInfo");
	private static final String PAY_GETORDERBILLSINFOFORINTERFACE = AppConfig.getProperty("pay.getOrderBillsInfoForInterface");
	private static final String PAY_GETORDERBYWHERE = AppConfig.getProperty("pay.getOrderByWhere");

	// 测试使用
//	private static final String PAY_NEWSERVER_DOMAIN = "http://eapi.tt.oucnet.com";
//	private static final String PAY_GETORDERBILLINFO = "/orderapi/getOrderBillInfo.html";
//	private static final String PAY_GETORDERBILLSINFOFORINTERFACE = "/orderapi/getOrderBillsInfoForInterface.html";
//	private static final String PAY_GETORDERBYWHERE = "/orderapi/getOrderByWhere";

	/**
	 * 获取缴费单信息
	 * 接口编号  :  0000003840  获取缴费单信息
	 * @param orderBillsSn
	 * @return
     */
	public OrderBillInfo getOrderBillInfo(String orderBillsSn) {
		try {
			Map params = new HashMap();
			params.put("order_bills_sn", orderBillsSn); // 缴费单号

			// 额外参数;签名
			long time = System.currentTimeMillis();
//        	params.put("school_code", Objects.toString("041", ""));	// 院校code，非必填
			params.put("sign", SignUtil.formatUrlMap(params, time));
			params.put("appid", SignUtil.APPID);// APPID不需要参与加密
			params.put("time", String.valueOf(time));

			String result = HttpClientUtils.doHttpPost(PAY_NEWSERVER_DOMAIN + PAY_GETORDERBILLINFO, params, 6000, "UTF-8");

			log.info("获取缴费单信息 [url:" + PAY_NEWSERVER_DOMAIN + PAY_GETORDERBILLINFO + ", params: " + params + "]， 返回值为：" + result);

			if (StringUtils.isNotEmpty(result)) {
				JSONObject json = JSONObject.fromObject(result);
				int status = json.getInt("status");
				if (status == 1) {
					JSONObject dataJson = json.getJSONObject("data");
					OrderBillInfo data = GsonUtils.toBean(dataJson.toString(), OrderBillInfo.class);
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
	 * 获取缴费单信息
	 * 接口编号  :  0000003988  获取接口创建缴费单信息
	 * @param paySn
	 * @param billsSnPrefix 缴费单前缀，不传默认是C
	 * @return
	 */
	public OrderBillInfo getOrderBillsInfoForInterface(String paySn, String billsSnPrefix) {
		try {
			Map params = new HashMap();
			params.put("order_bills_sn", paySn); // 缴费单号
			if(StringUtils.isNotBlank(billsSnPrefix)) {
				params.put("bills_sn_prefix", billsSnPrefix); // 缴费单前缀
			}

			// 额外参数;签名
			long time = System.currentTimeMillis();
//        	params.put("school_code", Objects.toString("041", ""));	// 院校code，非必填
			params.put("sign", SignUtil.formatUrlMap(params, time));
			params.put("appid", SignUtil.APPID);// APPID不需要参与加密
			params.put("time", String.valueOf(time));

			String result = HttpClientUtils.doHttpPost(PAY_NEWSERVER_DOMAIN + PAY_GETORDERBILLSINFOFORINTERFACE, params, 6000, "UTF-8");

			log.info("获取缴费单信息 [url:" + PAY_NEWSERVER_DOMAIN + PAY_GETORDERBILLSINFOFORINTERFACE + ", params: " + params + "]， 返回值为：" + result);

			if (StringUtils.isNotEmpty(result)) {
				JSONObject json = JSONObject.fromObject(result);
				int status = json.getInt("status");
				if (status == 1) {
					JSONObject dataJson = json.getJSONObject("data");
					OrderBillInfo data = GsonUtils.toBean(dataJson.toString(), OrderBillInfo.class);
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
	 * 根据身份证号，姓名，层次，专业唯一号获取最新订单信息
	 * 接口编号  :  0000005902  根据条件获取订单信息
	 * @param xm 学员名称
	 * @param sfzh 	身份证号
	 * @param pycc 专业层次 0-专科 2-本科
	 * @return orderSn
	 */
	@Deprecated // 这个接口不用
	public OrderInfo getOrderByWhere(String xm, String sfzh, String pycc) {
		try {
			Map params = new HashMap();
			params.put("user_name", xm);
			params.put("identity_card", sfzh);
			if("0".equals(pycc)) {
				pycc = "1";
			}
			params.put("edu_level", pycc); // 专业层次（1.专科，2.本科）

			// 额外参数;签名
			long time = System.currentTimeMillis();
//        	params.put("school_code", Objects.toString("041", ""));	// 院校code，非必填
			params.put("sign", SignUtil.formatUrlMap(params, time));
			params.put("appid", SignUtil.APPID);// APPID不需要参与加密
			params.put("time", String.valueOf(time));

			String result = HttpClientUtils.doHttpPost(PAY_NEWSERVER_DOMAIN + PAY_GETORDERBYWHERE, params, 6000, "UTF-8");

			log.info("根据条件获取订单信息 [url:" + PAY_NEWSERVER_DOMAIN + PAY_GETORDERBYWHERE + ", params: " + params + "]， 返回值为：" + result);

			if (StringUtils.isNotEmpty(result)) {
				JSONObject json = JSONObject.fromObject(result);
				int status = json.getInt("status");
				if (status == 1) {
					JSONObject dataJson = json.getJSONObject("data");
					OrderInfo data = GsonUtils.toBean(dataJson.toString(), OrderInfo.class);
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
	 * 测试
	 * @param args
     */
	public static void main(String[] args) {
		BillRemoteNewService service = new BillRemoteNewService();
//		OrderBillInfo orderBillInfo = service.getOrderBillsInfoForInterface("201710250000033", null);
//		System.out.println(orderBillInfo.getPay_amount());
//		System.out.println(new Date(orderBillInfo.getPay_time()*1000));
		OrderInfo orderInfo = service.getOrderByWhere("雷剑成", "441802199011293815", "0");
		System.out.println(orderInfo.getOrder_sn());
	}
	
}
