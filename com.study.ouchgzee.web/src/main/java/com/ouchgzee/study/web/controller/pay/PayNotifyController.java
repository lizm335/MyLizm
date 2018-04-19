/**
 * Copyright(c) 2013 版权归属广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.controller.pay;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gzedu.xlims.common.GsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.common.gzedu.SignUtil;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.exam.GjtExamCost;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.exam.GjtExamCostService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.service.textbook.GjtTextbookDistributeService;
import com.ouchgzee.study.web.common.BaseController;

/**
 * 支付通知控制器<br/>
 * 功能说明：<br/>
 * 		完成支付的同步通知和异步通知的业务处理。<br/>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年10月12日
 * @version 2.5
 */
@Controller
@RequestMapping("/pay")
public class PayNotifyController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(PayNotifyController.class);

	@Autowired
	private GjtRecResultService gjtRecResultService;

	@Autowired
	private GjtExamPlanNewService gjtExamPlanNewService;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtExamCostService gjtExamCostService;


	@Autowired
	private GjtTextbookDistributeService gjtTextbookDistributeService;

	/** ============================================ 补考费缴费 begin ============================================ **/

	/**
	 * 补考费缴费-同步通知<br/>
	 * 前端返回页面通知。<br/>
	 * @return
     * @throws IOException
     */
	@SysLog("补考费缴费-同步通知")
	@RequestMapping(value = "/examFeeReturn")
	public String examFeeReturn(ModelMap model, HttpServletResponse response, HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("utf-8");				//设置编码
		// 拼接请求的参数
		Map<String, String> args = new TreeMap<String, String>();
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			args.put(name, valueStr);
		}
		model.put("info", JSON.toJSONString(args));
		return "pay/exam_fee_return";
	}

	/**
	 * 补考费缴费-异步通知<br/>
	 * 异步通知结果 返回 success 字符串，招生系统便认为通知已成功，最大重发次数为12次(每隔5分钟)，超过12次后便不再通知调用方。br/>
	 * @return
	 * @throws IOException
	 */
	@SysLog("补考费缴费-异步通知")
	@RequestMapping(value = "/examFeeNotify")
	public void examFeeNotify(HttpServletResponse response, HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("utf-8");				//设置编码
		// 拼接请求的参数
		Map<String, String> args = new TreeMap<String, String>();
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			args.put(name, valueStr);
		}

		StringBuffer paramsJoin = new StringBuffer();
		for (Map.Entry<String, String> item : args.entrySet()) {
			if (org.apache.commons.lang.StringUtils.isNotEmpty(item.getKey())) {
				String key = item.getKey();
				String val = item.getValue();
				paramsJoin.append(key).append("=").append(val).append("&");
			}
		}
		paramsJoin.setLength(paramsJoin.length() - 1);
		log.info("支付异步通知 examFeeNotify：{}?{}", "http://test.study.tt.gzedu.com/pay/examFeeNotify", paramsJoin);
		log.error("支付异步通知 examFeeNotify：{}?{}", "http://test.study.tt.gzedu.com/pay/examFeeNotify", paramsJoin);
		// 校验签名
		String data = args.remove("data"); // 自己定义的参数
		String appid = args.remove("appid");
		long time = NumberUtils.toLong(args.remove("time"));
		String sign = args.remove("sign");
		String verifySign = SignUtil.formatUrlMap(args, time);
		if(org.apache.commons.lang3.StringUtils.equals(sign, verifySign)) {
			// 开始商家业务逻辑处理
			String paySn = args.get("pay_sn");
			String payStatus = args.get("pay_status");
			double price = NumberUtils.toDouble(args.get("price")); // 单位：元
			Date payDate = DateUtil.parseDate(args.get("pay_time"));

			// data格式：[{"examPlanId":"571f908e7f0000013d2e9ac0001395d2","recId":"4fd223107f000001d6adb42bf53972e3"}]
			List<Map> notifyDataList = null;
			if(StringUtils.isNotBlank(data)) {
				notifyDataList = GsonUtils.toBean(data, List.class);
			}
			// begin 循环处理
			for (Map notifyData : notifyDataList) {
				String recId = (String) notifyData.get("recId");
				String examPlanId = (String) notifyData.get("examPlanId");
				// 判断是否是已支付状态
				if(org.apache.commons.lang3.StringUtils.equals(payStatus, Constants.BOOLEAN_1)) {
					boolean flag = gjtRecResultService.updatePayState(recId, paySn);
					if (flag) {
						try {
							GjtRecResult recResult = gjtRecResultService.queryById(recId);
							Map<String, Object> searchParams = new HashMap<String, Object>();
							searchParams.put("EQ_studentId", recResult.getStudentId());
							searchParams.put("EQ_examPlanId", examPlanId);
							searchParams.put("EQ_paySn", paySn);
							List<GjtExamCost> examCostList = gjtExamCostService.queryBy(searchParams, null);
							if(examCostList == null || examCostList.size() == 0) {
								GjtExamCost examCost = new GjtExamCost();
								examCost.setCostId(UUIDUtils.random());
								examCost.setPaySn(paySn);
								examCost.setStudentId(recResult.getStudentId());
								examCost.setCourseId(recResult.getCourseId());
								GjtExamPlanNew examPlanNew = gjtExamPlanNewService.queryById(examPlanId);
								examCost.setGradeId(examPlanNew.getGradeId());
								examCost.setTeachPlanId(recResult.getTeachPlanId());
								examCost.setExamBatchCode(examPlanNew.getExamBatchCode());
								examCost.setExamPlanId(examPlanNew.getExamPlanId());
								examCost.setKsfs(examPlanNew.getType() + "");
								examCost.setMakeup("1");
								examCost.setPayStatus("0");
								examCost.setCourseCost(price + "");
								examCost.setCourseCode(gjtCourseService.queryById(recResult.getCourseId()).getKch());
								examCost.setPayDate(payDate);
								gjtExamCostService.insert(examCost);
							} else {
								for(GjtExamCost examCost : examCostList) {
									examCost.setPayStatus("0");
									examCost.setPayDate(payDate);
									examCost.setUpdatedDt(new Date());
									examCost.setUpdatedBy(recResult.getStudentId());
									gjtExamCostService.save(examCost);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						log.info("examFeeNotify ======== success{}", JSON.toJSONString(args));
					} else {
						log.error("examFeeNotify ======== update fail!{}", JSON.toJSONString(args));
					}
				} else {
					try {
						GjtRecResult recResult = gjtRecResultService.queryById(recId);
						Map<String, Object> searchParams = new HashMap<String, Object>();
						searchParams.put("EQ_studentId", recResult.getStudentId());
						searchParams.put("EQ_examPlanId", examPlanId);
						searchParams.put("EQ_paySn", paySn);
						List<GjtExamCost> examCostList = gjtExamCostService.queryBy(searchParams, null);
						if(examCostList == null || examCostList.size() == 0) {
							GjtExamCost examCost = new GjtExamCost();
							examCost.setCostId(UUIDUtils.random());
							examCost.setPaySn(paySn);
							examCost.setStudentId(recResult.getStudentId());
							examCost.setCourseId(recResult.getCourseId());
							GjtExamPlanNew examPlanNew = gjtExamPlanNewService.queryById(examPlanId);
							examCost.setGradeId(examPlanNew.getGradeId());
							examCost.setTeachPlanId(recResult.getTeachPlanId());
							examCost.setExamBatchCode(examPlanNew.getExamBatchCode());
							examCost.setExamPlanId(examPlanNew.getExamPlanId());
							examCost.setKsfs(examPlanNew.getType() + "");
							examCost.setMakeup("1");
							examCost.setPayStatus("1");
							examCost.setCourseCost(price + "");
							examCost.setCourseCode(gjtCourseService.queryById(recResult.getCourseId()).getKch());
							gjtExamCostService.insert(examCost);
						} else {
							for(GjtExamCost examCost : examCostList) {
								examCost.setPayStatus("1");
								examCost.setUpdatedDt(new Date());
								examCost.setUpdatedBy(recResult.getStudentId());
								gjtExamCostService.save(examCost);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					log.error("examFeeNotify ======== 等待支付... payStatus={}!{}", payStatus, JSON.toJSONString(args));
				}
			}
			// ./end 循环处理
			// 判断是否是已支付状态
			if(org.apache.commons.lang3.StringUtils.equals(payStatus, Constants.BOOLEAN_1)) {
				// 通知支付方处理完成
				super.outputHtml(response, "success");
			}
		} else {
			log.error("examFeeNotify ======== error {},{}", "verifySign[" + verifySign + "],verify sign fail!校验签名失败!", JSON.toJSONString(args));
		}
	}

	/** ============================================ 补考费缴费 end ============================================ **/

	/** ============================================ 教材费缴费 begin ============================================**/

	@SysLog("教材费缴费-同步通知")
	@RequestMapping(value = "/textbookFeeReturn")
	public String textbookFeeReturn(ModelMap model, HttpServletResponse response, HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("utf-8"); // 设置编码
		// 拼接请求的参数
		Map<String, String> args = new TreeMap<String, String>();
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			args.put(name, valueStr);
		}
		model.put("info", JSON.toJSONString(args));
		return "pay/textbook_fee_return";
	}

	@SysLog("教材费缴费-异步通知")
	@RequestMapping(value = "/textbookFeeNotify")
	public void textbookFeeNotify(HttpServletResponse response, HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("utf-8"); // 设置编码
		// 拼接请求的参数
		Map<String, String> args = new TreeMap<String, String>();
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			args.put(name, valueStr);
		}


		// 校验签名
		String data = args.remove("data");

		String appid = args.remove("appid");
		long time = NumberUtils.toLong(args.remove("time"));
		String sign = args.remove("sign");
		String verifySign = SignUtil.formatUrlMap(args, time);
		if (org.apache.commons.lang3.StringUtils.equals(sign, verifySign)) {
			// 开始商家业务逻辑处理
			String paySn = args.get("pay_sn");
			String payStatus = args.get("pay_status");
			BigDecimal price = NumberUtils.createBigDecimal(args.get("price")); // 单位：元
			Date payDate = DateUtil.parseDate(args.get("pay_time"));
			// 判断是否是已支付状态
			if (org.apache.commons.lang3.StringUtils.equals(payStatus, Constants.BOOLEAN_1)) {
				GjtTextbookDistribute distribute = gjtTextbookDistributeService.queryById(data);
				distribute.setStatus(1);
				distribute.setUpdatedBy("textbookFeeNotify");
				distribute.setPayPrice(price);
				gjtTextbookDistributeService.update(distribute);
				super.outputHtml(response, "success");
				log.info("textbookFeeNotify=======支付成功,订单号:{}", paySn);
			} else {
				log.error("textbookFeeNotify ======== 等待支付... payStatus={}!{}", payStatus, JSON.toJSONString(args));
			}
		} else {
			log.error("textbookFeeNotify ======== error {},{}", "verifySign[" + verifySign + "],verify sign fail!校验签名失败!", JSON.toJSONString(args));
		}
	}
	/*** ============================================ 教材费缴费 end ============================================**/

}
