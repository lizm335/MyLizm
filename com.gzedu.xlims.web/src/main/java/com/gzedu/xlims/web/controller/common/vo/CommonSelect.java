/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.common.vo;

/**
 * 功能说明：下拉列表 id,name 赋值
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年10月8日
 * @version 2.5
 *
 */
public class CommonSelect {
	String id;
	String name;

	public CommonSelect(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
