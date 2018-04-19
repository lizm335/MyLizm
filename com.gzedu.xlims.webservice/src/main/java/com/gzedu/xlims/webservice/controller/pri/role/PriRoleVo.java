package com.gzedu.xlims.webservice.controller.pri.role;

import com.gzedu.xlims.pojo.PriRoleInfo;

/**
 * 系统角色vo
 * @author lyj
 * @time 2017年5月17日 
 * TODO
 */
public class PriRoleVo {

	private String id;

	private String name;

	private String code;

	public PriRoleVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PriRoleVo(PriRoleInfo role) {
		super();
		this.id = role.getRoleId();
		this.name = role.getRoleName();
		this.code = role.getRoleCode();
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
