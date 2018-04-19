/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.temp;

/**
 * 功能说明：考场
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年11月18日
 * @version 2.5
 *
 */
public class Room {
	String id;
	String name;
	int number = 0;// 容量人数

	public Room(String id, String name, int number) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
