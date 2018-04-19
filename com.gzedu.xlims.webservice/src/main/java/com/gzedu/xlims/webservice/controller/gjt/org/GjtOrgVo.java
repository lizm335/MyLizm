package com.gzedu.xlims.webservice.controller.gjt.org;

import java.util.List;

import com.gzedu.xlims.pojo.GjtOrg;

public class GjtOrgVo {
	
	private String id;
	
	private String name;
	
	private String code;
	
	private String orgType;
	
	private String parentId;
	
	private List<GjtOrgVo> childs;
	
	public GjtOrgVo(GjtOrg org) {
		super();
		this.id = org.getId();
		this.name = org.getOrgName();
		this.code = org.getCode();
		this.orgType = org.getOrgType();
		this.parentId = org.getParentGjtOrg()!=null?org.getParentGjtOrg().getId():"";
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

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<GjtOrgVo> getChilds() {
		return childs;
	}

	public void setChilds(List<GjtOrgVo> childs) {
		this.childs = childs;
	}
	
	
}
