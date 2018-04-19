package com.ouchgzee.study.web.vo;

import java.util.Map;

import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.pojo.dto.AddressBookDto;

public class AddressBookVo {

	private static final String String = null;
	private String name = "";
	private String majorName = "";
	private String headImg = "";
	private String eeno = "";
	private String eeUrl = "";

	public AddressBookVo() {
	}

	public AddressBookVo(String name, String majorName, String headImg, String eeno) {
		super();
		this.name = name;
		this.majorName = majorName;
		this.headImg = headImg;
		this.eeno = eeno;
	}

	public AddressBookVo(String studetnId, AddressBookDto addressBookDto, String eeServer) {
		super();
		this.name = addressBookDto.getName();
		this.majorName = addressBookDto.getMajorName();
		this.headImg = addressBookDto.getHeadImg();
		this.eeno = addressBookDto.getEeno();
		this.eeUrl = eeServer + "/openChat.do?data=" + EncryptUtils
				.encrypt("{\"USER_ID\":\"" + studetnId + "\",\"TO_ID\":\"" + addressBookDto.getStudentId() + "\"}");// 旧的接口是传这样的参数
	}

	public AddressBookVo(String studetnId, Map map, String eeServer) {
		super();
		this.name = (String) map.get("XM");
		this.headImg = (String) map.get("ZP");
		this.eeno = (String) map.get("EENO");
		String empoyeeId = (String) map.get("EMPLOYEE_ID");
		this.eeUrl = eeServer + "/openChat.do?data="
				+ EncryptUtils.encrypt("{\"USER_ID\":\"" + studetnId + "\",\"TO_ID\":\"" + empoyeeId + "\"}");// 旧的接口是传这样的参数
	}

	public String getEeUrl() {
		return eeUrl;
	}

	public void setEeUrl(String eeUrl) {
		this.eeUrl = eeUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getEeno() {
		return eeno;
	}

	public void setEeno(String eeno) {
		this.eeno = eeno;
	}

}
