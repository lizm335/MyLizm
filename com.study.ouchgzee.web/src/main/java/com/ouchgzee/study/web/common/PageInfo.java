/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.common;

import org.springframework.data.domain.Page;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年3月3日
 * @version 2.5
 *
 */
public class PageInfo {
	int number;// 当前页
	int size;// 每页条数
	int totalPages;// 总页数
	boolean firstPage;// 是否第一页
	boolean lastPage;// 是否最后一页
	long totalElements;// 总记录数
	int numberOfElements;// 当前页条数
	Object content;// 返回的对象（list,map）

	public PageInfo() {

	}

	/**
	 * @param Page
	 * 
	 */
	public PageInfo(Page page, Object object) {
		super();
		this.number = page.getNumber();
		this.size = page.getSize();
		this.totalPages = page.getTotalPages();
		this.firstPage = page.isFirstPage();
		this.lastPage = page.isLastPage();
		this.totalElements = page.getTotalElements();
		this.numberOfElements = page.getNumberOfElements();
		this.content = object;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isFirstPage() {
		return firstPage;
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

}
