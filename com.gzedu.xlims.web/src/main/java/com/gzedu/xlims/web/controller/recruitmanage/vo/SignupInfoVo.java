package com.gzedu.xlims.web.controller.recruitmanage.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 报读资料统计VO
 * @author lulinlin
 *
 */
public class SignupInfoVo {
	
	String id;
	String name;
	int num;
	public SignupInfoVo(String id, String name, int num) {
		this.id = id;
		this.name = name;
		this.num = num;
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}	
}
