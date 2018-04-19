package com.gzedu.xlims.service.remote;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.gzedu.AnalyXmlUtil;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BillRemoteService {

	private static final Log log = LogFactory.getLog(BillRemoteService.class);

	private static final String API_BILL_QUERY = "api.bill.query";
	private static final String API_BILL_QUERY_NEW = "api.bill.query.new";
	private static final String API_BILL_MERGE = "api.bill.pay";
	private static final String API_SUBSIDIES = "api.subsidies";
	private static final String API_BILL_QUERY_BY_SFZH = "api.bill.query.by.sfzh";
	
	private static final String API_ORDER_DETAIL_URL = "api.order.detail.url";//http://eomp.oucnet.cn/interface/order/orderDetail.do
	private static final String API_ORDER_DETAIL_URL_TEST = "api.order.detail.url.test";//http://www.tt.96930.cn/interface/order/orderDetail.do

	private static final String API_POLICY_DETAIL_URL = "api.policy.detail.url"; //http://tg.969300.com/opi/roll/getPolicyDetail.do"
	private static final String API_POLICY_DETAIL_URL_TEST = "api.policy.detail.url.test"; //http://tg.tt.969300.com/opi/roll/getPolicyDetail.do

	private static final String URL_BILL_MERGE = AppConfig.getProperty(API_BILL_MERGE);

	public static Map PAY_TYPE = new HashMap();

	public static Map ORDER_STATUS = new HashMap();
	
	static {
		PAY_TYPE.put("1", "网上支付");
		PAY_TYPE.put("2", "货到付款");
		PAY_TYPE.put("3", "现场支付");
		PAY_TYPE.put("4", "银行转账");
		PAY_TYPE.put("1,2", "网上支付和货到付款");
		
		ORDER_STATUS.put("0", "未付款");
		ORDER_STATUS.put("1", "待发货");
		ORDER_STATUS.put("2", "已发货");
		ORDER_STATUS.put("3", "交易成功");
		ORDER_STATUS.put("4", "交易关闭");
		ORDER_STATUS.put("5", "退费订单");
		ORDER_STATUS.put("6", "部分退费");
		ORDER_STATUS.put("7", "已退费");
		ORDER_STATUS.put("8", "部分发货");
	}
	
	public static enum BillType {
		XF,     // 学费
		JC,     // 教材费  
		JDF,    // 鉴定费
		WLPX,   // 网络培训费  
		CS      // 测试费
	}
	

	//开学入学
	public Map<String,Object> getBill(String userAccount, String...billType) {
		String url = AppConfig.getProperty(API_BILL_QUERY);

		StringBuffer sb = new StringBuffer(100);
		boolean flag = false;
		for(String type : billType) {
			if(flag) {
				sb.append(",");
			}
			sb.append(type);
			flag = true;
		}
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("formMap.CARDID", userAccount);
		if(sb.toString().length()>1) params.put("formMap.TYPE", sb.toString());

//		params.clear();
//		url = "http://eomp.oucnet.cn/interface/userOrderInterface/findUserOrderforXL.do?formMap.TYPE=XF&formMap.CARDID=440881199201083557";
		
		String rspXML = HttpClientUtils.doHttpPost(url, params, 9000, "UTF-8");
		
		log.info("用户订单费用缴纳明细[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);

		if (StringUtils.isNotBlank(rspXML) && !StringUtils.equals(rspXML, "error")) {
			try {
				Map rsp = AnalyXmlUtil.parserXmlUTF(rspXML);
				List<Map<String,Object>> list = (List<Map<String, Object>>) rsp.get("ORDER");

				if(list!=null) {
					for(Iterator<Map<String, Object>> itr = list.iterator(); itr.hasNext(); ) {
						Map<String, Object> order = itr.next();
						String payDate = ObjectUtils.toString(order.get("PAY_DATE")); // 支付日期
						order.put("payDate", StringUtils.equals(payDate, "null") ? "--" : payDate);
					}
					//return list;
				}
				if (list != null && list.size() > 0) {
					return (Map<String, Object>) list.get(0);
				}
			} catch (Exception e) {
				log.error("===> 用户订单费用缴纳明细[查询出错]\n" + rspXML);
				e.printStackTrace();
			}
		}
		return Collections.emptyMap();

	}
	
	public List getBillListNew(String userAccount, String...billType) {
		String url = AppConfig.getProperty(API_BILL_QUERY_NEW);
		
		StringBuffer sb = new StringBuffer(100);
		boolean flag = false;
		for(String type : billType) {
			if(flag) {
				sb.append(",");
			}
			sb.append(type);
			flag = true;
		}
		
		Map<String,Object> params = new HashMap();
		params.put("formMap.CARDID", userAccount);
		if(sb.toString().length()>1) params.put("formMap.TYPE", sb.toString());
		
//		params.clear();
//		url = "http://eomp.oucnet.cn/interface/userOrderInterface/findUserOrderforXL.do?formMap.TYPE=XF&formMap.CARDID=440881199201083557";
		
		String rspXML = HttpClientUtils.doHttpPost(url, params, 9000, "UTF-8");
		
		log.info("用户订单费用缴纳明细[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		
		if (StringUtils.isNotBlank(rspXML) && !StringUtils.equals(rspXML, "error")) {
			try {
				Map<?,?> rsp = AnalyXmlUtil.parserXmlUTF(rspXML);
				Map<?,?> orderMap = (Map<?,?>) rsp.get("ORDER");
				Map<?,?> recordMap=null;
				String orderId =null;
				if(orderMap!=null){
					orderId= (String) orderMap.get("ORDER_SN");
					recordMap = (Map<?,?>) orderMap.get("ORDER_PAY_RECORD_LIST");
				}
				List list =null;
				if(recordMap!=null){
					list= (List) recordMap.get("PAY_RECORD");
				}
				if(list!=null) {
					for(Iterator itr = list.iterator(); itr.hasNext(); ) {
						Map order = (Map)itr.next();
						String payDate = ObjectUtils.toString(order.get("PAY_DATE")); // 支付日期
						order.put("payDate", StringUtils.equals(payDate, "null") ? "--" : payDate);
					}
					HashMap tempMap = new HashMap();
					tempMap.put("ORDER_SN", orderId);
					list.add(0, tempMap);
					return list;
				}
				
			} catch (Exception e) {
				log.error("===> 用户订单费用缴纳明细[查询出错]\n" + rspXML);
				e.printStackTrace();
			}
		}
		
		return Collections.emptyList();
	}
	
	public Map<String,Object> getOrderDetail(String orderSN) {
		Map<String,Object> params = Maps.newHashMap();
		params.put("formMap.ORDER_SN", orderSN);
		//String url = "http://www.tt.96930.cn/interface/order/orderDetail.do";
		String url = AppConfig.getProperty(API_ORDER_DETAIL_URL);
		String rspJSON = HttpClientUtils.doHttpPost(url, params, 9000, "gbk");
		if (StringUtils.isNotBlank(rspJSON) && !StringUtils.equals(rspJSON, "error")) {
			try {
				Map<String, Object> rsp = JsonUtils.toObject(rspJSON, HashMap.class);
				return rsp;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Maps.newHashMap();
	}
	
	public Map<String,Object> getPolicyDetail(String policyId) {
		Map<String,Object> params = Maps.newHashMap();
		params.put("formMap.POLICY_ID", policyId);

		params.put("formMap.RESULT_TYPE", "json");	
		//String url = "http://tg.tt.969300.com/opi/roll/getPolicyDetail.do";
		String url = AppConfig.getProperty(API_POLICY_DETAIL_URL);
		String rspJSON = HttpClientUtils.doHttpPost(url, params, 9000, "utf-8");
		if (StringUtils.isNotBlank(rspJSON) && !StringUtils.equals(rspJSON, "error")) {
			try {
				Map<String, Object> rsp = JsonUtils.toObject(rspJSON, HashMap.class);
				return rsp;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Maps.newHashMap();
	}
	
	//补贴发放
	public Map getSubsidyList(String userAccount) {
		String url = AppConfig.getProperty(API_SUBSIDIES);
		
		StringBuffer sb = new StringBuffer(100);
		
		Map params = new HashMap();
		params.put("formMap.Id_Card", userAccount);
		
		String rspXML = HttpClientUtils.doHttpPost(url, params, 3000, "GBK");
		
		log.info("用户订单费用缴纳明细[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		
		if (StringUtils.isNotBlank(rspXML) && !"error".equals(rspXML)) {
			String progress = "0";
			try {
				Gson gson = new Gson();
				Map rsp = gson.fromJson(rspXML, Map.class);
				return rsp;
				
			} catch (Exception e) {
				log.error("===> 用户订单费用缴纳明细[查询出错]\n" + rspXML);
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * 分期付款支付接口。
	 * 
	 * @param orderId 
	 * @param orderItemIds
	 * @return
	 */
	public boolean mergeBill(String orderId, List<String> orderItemIds) {
		
		String url = AppConfig.getProperty(API_BILL_MERGE);
		
		Map params = new HashMap();
		params.put("formMap.ORDER_ID", orderId);
		params.put("formMap.ORDER_PAY_RECORD_ID", StringUtils.join(orderItemIds, ","));

		String rspXML = HttpClientUtils.doHttpPost(url, params, 3000, "UTF-8");
		log.info("分期付款支付接口[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);

		boolean isSuccess = false;
		if (StringUtils.isNotBlank(rspXML) && !"error".equals(rspXML)) {
			try {
				Map rsp = AnalyXmlUtil.parserXmlUTF(rspXML);
				log.debug(rsp.toString());
				isSuccess = true;

			} catch (Exception e) {
				log.error("===> 分期付款支付接口[合并订单出错]\n" + rspXML);
				e.printStackTrace();
				isSuccess = false;
			}
			
		} else {
			isSuccess = false;
		}
		
		return isSuccess;
		
	}

	public List getBillListBySfzh(String userAccount) {
		String url = AppConfig.getProperty(API_BILL_QUERY_BY_SFZH);
		HashMap params = new HashMap();
		params.put("formMap.CARDID", userAccount);
		Map map = AnalyXmlUtil.parserXmlUTF(HttpClientUtils.doHttpPost(url, params, 9000, "UTF-8"));
		List billList = new ArrayList();
		try {
			if (map != null) {
				Object object = map.get("ORDER");
				if (object instanceof List) {
					List<Map> orderList = (List) object;
					for (Map orederDetail : orderList) {
						getOrderDetail(billList, orederDetail);
					}
				} else if (object instanceof Map) {
					getOrderDetail(billList, (Map) object);
				}
			}
		} catch (Exception e) {

		}
		return billList;
	}

	private void getOrderDetail(List billList, Map orederDetail) {
		String ORDER_ID = (String) orederDetail.get("ORDER_ID");
		String PAY_CHANNEL = (String) orederDetail.get("PAY_CHANNEL");
		Object object2 = orederDetail.get("PAY_RECORD");
		if (object2 instanceof List) {
			List<Map> payRecordList = (List) object2;
			for (Map payRecord : payRecordList) {
				payRecord.put("orderId", ORDER_ID);
				payRecord.put("PAY_CHANNEL", PAY_CHANNEL);
				billList.add(payRecord);
			}
		} else if (object2 instanceof Map) {
			Map payRecord = (Map) object2;
			payRecord.put("orderId", ORDER_ID);
			payRecord.put("PAY_CHANNEL", PAY_CHANNEL);
			billList.add(payRecord);
		}
	}
	
}
