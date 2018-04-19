package com.gzedu.xlims.pojo.status;

/**
 * @Function: 状态枚举类型
 * @ClassName: EnumStatus 
 * @date: 2016年4月17日 下午10:06:22 
 *
 * @author  zhy
 * @version V2.5
 * @since   JDK 1.6
 */
public enum EnumStatus implements BaseEnum {
	ON("启用"), OFF("注销");

	private EnumStatus(String label) {
		this.label = label;
	}

	private String label;

	public String getLabel() {
		return this.label;
	}

}
