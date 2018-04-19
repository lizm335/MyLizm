package com.ouchgzee.headTeacher.service.remote;

import com.google.gson.Gson;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.gzedu.AnalyXmlUtil;
import com.gzedu.xlims.common.HttpClientUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Deprecated @Service("bzrBillRemoteService") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class BillRemoteService {

	private static final Log log = LogFactory.getLog(BillRemoteService.class);

	private static final String API_BILL_QUERY = "api.bill.query";
	private static final String API_BILL_QUERY_NEW = "api.bill.query.new";
	private static final String API_BILL_MERGE = "api.bill.pay";
	private static final String API_SUBSIDIES = "api.subsidies";

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
	@SuppressWarnings("unchecked")
	public List getBillList(String userAccount, String...billType) {
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
		
		Map params = new HashMap();
		params.put("formMap.CARDID", userAccount);
		if(sb.toString().length()>1) params.put("formMap.TYPE", sb.toString());

//		params.clear();
//		url = "http://eomp.oucnet.cn/interface/userOrderInterface/findUserOrderforXL.do?formMap.TYPE=XF&formMap.CARDID=440881199201083557";
		
		String rspXML = HttpClientUtils.doHttpPost(url, params, 9000, "UTF-8");
		
		log.info("用户订单费用缴纳明细[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);

		if (StringUtils.isNotBlank(rspXML) && !StringUtils.equals(rspXML, "error")) {
			try {
				Map rsp = AnalyXmlUtil.parserXmlUTF(rspXML);
				List list = (List) rsp.get("ORDER");
				
				if(list!=null) {
					for(Iterator itr = list.iterator(); itr.hasNext(); ) {
						Map order = (Map)itr.next();
						String payDate = ObjectUtils.toString(order.get("PAY_DATE")); // 支付日期
						order.put("payDate", StringUtils.equals(payDate, "null") ? "--" : payDate);
					}
					return list;
				}
			} catch (Exception e) {
				log.error("===> 用户订单费用缴纳明细[查询出错]\n" + rspXML);
				e.printStackTrace();
			}
		}
		return Collections.emptyList();
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
		
		Map params = new HashMap();
		params.put("formMap.CARDID", userAccount);
		if(sb.toString().length()>1) params.put("formMap.TYPE", sb.toString());
		
//		params.clear();
//		url = "http://eomp.oucnet.cn/interface/userOrderInterface/findUserOrderforXL.do?formMap.TYPE=XF&formMap.CARDID=440881199201083557";
		
		String rspXML = HttpClientUtils.doHttpPost(url, params, 9000, "UTF-8");
		
		log.info("用户订单费用缴纳明细[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		
		if (StringUtils.isNotBlank(rspXML) && !StringUtils.equals(rspXML, "error")) {
			try {
				Map rsp = AnalyXmlUtil.parserXmlUTF(rspXML);
				Map orderMap = (Map) rsp.get("ORDER");
				Map recordMap=null;
				String orderId =null;
				if(orderMap!=null){
					orderId= (String) orderMap.get("ORDER_ID");
					recordMap = (Map) orderMap.get("ORDER_PAY_RECORD_LIST");
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
					tempMap.put("ORDER_ID", orderId);
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
	
}
