/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.vo;

import java.util.Map;

import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.status.DistributeStatusEnum;
import com.gzedu.xlims.pojo.status.TermType;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年3月29日
 * @version 3.0
 *
 */
public class TeachPlanTextbookVO {
	private int term;
	private String courseName;
	private String textbookName;
	private Integer textbookType;
	private Float price;
	private Integer status;

	/**
	 * @param plan
	 * @param textbook
	 * @param statusMap
	 */
	public TeachPlanTextbookVO(GjtTeachPlan plan, GjtTextbook textbook, Map<String, Integer> statusMap) {
		this.term = plan.getKkxq();
		this.courseName = plan.getGjtCourse().getKcmc();
		this.textbookName = textbook.getTextbookName();
		this.textbookType = textbook.getTextbookType();
		this.price = textbook.getPrice();
		this.status = 0;
		if (statusMap.get(textbook.getTextbookId()) != null)
			this.status = statusMap.get(textbook.getTextbookId());
	}

	public String getTermName() {
		return TermType.getName(String.valueOf(term));
	}

	public String getCourseName() {
		return courseName;
	}

	public String getTextbookName() {
		return textbookName;
	}

	public Integer getTextbookType() {
		return textbookType;
	}

	public Float getPrice() {
		return price;
	}

	public Integer getStatus() {
		return status;
	}

	public String getStatusDesc() {
		return DistributeStatusEnum.getName(status);
	}

}
