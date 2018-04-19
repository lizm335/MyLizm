/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.pojo.status.DistributeStatusEnum;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;

import net.sf.json.JSONObject;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年3月29日
 * @version 3.0
 *
 */
public class GjtTextbookDistributeVO {

	private String distributeId;
	private String orderCode;// 订单号
	private String waybillCode;// 运单号
	private String logisticsComp;// 物流公司名称
	private String logisticsCode;// 物流公司编号
	private Date distributionDate;// 配送时间
	private BigDecimal freight;// 运费
	private Date signDate;// 签收时间
	private Integer status;// 状态
	private boolean allowFeedback = false;
	private List<Object> textbookDistributionDetas;// 物品配送明细

	public GjtTextbookDistributeVO(GjtTextbookDistribute textbookDistribute) {
		this.distributeId = textbookDistribute.getDistributeId();
		this.orderCode = textbookDistribute.getOrderCode();
		this.waybillCode = textbookDistribute.getWaybillCode();
		if (StringUtils.isNoneBlank(textbookDistribute.getLogisticsComp())) {
			int i = textbookDistribute.getLogisticsComp().indexOf("-");
			if (i > 0) {
				this.logisticsComp = textbookDistribute.getLogisticsComp().substring(0, i);
				this.logisticsCode = textbookDistribute.getLogisticsComp().substring(i + 1);
			}

		}
		this.distributionDate = textbookDistribute.getDistributionDt();
		this.freight = textbookDistribute.getFreight();
		this.signDate = textbookDistribute.getSignDt();
		this.status = textbookDistribute.getStatus();
		if (CollectionUtils.isNotEmpty(textbookDistribute.getGjtTextbookDistributeDetails())) {
			textbookDistributionDetas = new ArrayList<Object>();
			for (GjtTextbookDistributeDetail detail : textbookDistribute.getGjtTextbookDistributeDetails()) {
				JSONObject obj = new JSONObject();
				obj.put("textbookId", detail.getGjtTextbook().getTextbookId());
				obj.put("textbookName", detail.getGjtTextbook().getTextbookName());
				obj.put("price", detail.getPrice());
				obj.put("quantity", detail.getQuantity());
				textbookDistributionDetas.add(obj);
			}
		}
		GjtTextbookPlan textbookPlan = textbookDistribute.getGjtTextbookPlan();
		if (textbookPlan != null) {
			Date startDate = textbookPlan.getOfeedbackSdate(), endDate = textbookPlan.getOfeedbackEdate();
			long current = System.currentTimeMillis();
			// 是否反馈时间范围内
			this.allowFeedback = current >= startDate.getTime() && current <= endDate.getTime();
		}

	}

	public String getDistributeId() {
		return distributeId;
	}

	public void setDistributeId(String distributeId) {
		this.distributeId = distributeId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getWaybillCode() {
		return waybillCode;
	}

	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}

	public String getLogisticsComp() {
		return logisticsComp;
	}

	public void setLogisticsComp(String logisticsComp) {
		this.logisticsComp = logisticsComp;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Object> getTextbookDistributionDetas() {
		return textbookDistributionDetas;
	}

	public void setTextbookDistributionDetas(List<Object> textbookDistributionDetas) {
		this.textbookDistributionDetas = textbookDistributionDetas;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	// 状态描述
	public String getStatusDesc() {
		return DistributeStatusEnum.getName(status);
	}

	public boolean isAllowFeedback() {
		return allowFeedback;
	}

	public void setAllowFeedback(boolean allowFeedback) {
		this.allowFeedback = allowFeedback;
	}

	public String getDistributionDt() {
		if (distributionDate != null) {
			return DateUtil.toString(distributionDate, "yyyy-MM-dd HH:mm:ss");
		}
		return null;
	}

	public String getSignDt() {
		if (signDate != null) {
			return DateUtil.toString(signDate, "yyyy-MM-dd HH:mm:ss");
		}
		return null;
	}

}
