/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.vo;

import java.math.BigDecimal;

/**
 * 功能说明：
 * @author ouguohao@eenet.com
 * @Date 2018年2月1日
 * @version 3.0
 *
 */
public class TextbookVO {
	private String textbookName;
	private String cover;
	private String courseName;
	private BigDecimal price;

	public String getTextbookName() {
		return textbookName;
	}

	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
