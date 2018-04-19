package com.gzedu.xlims.common.constants;

/**
 * 权限码
 * 
 * @author lzj
 *
 */
public enum PermittedConstants {
	
	CREATE("新增", "create"),
	DELETE("删除", "delete"),
	UPDATE("修改", "update"),
	VIEW("查看", "view"),
	IMPORT("导入", "import"),
	EXPORT("导出", "export"),
	SUMIT_APPROVAL("提交审核", "sumit-approval"),
	APPROVAL("审核", "approval");
	
	private String name;
	
	private String code;
	
	PermittedConstants(String name, String code) {
		this.name = name;
		this.code = code;
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
