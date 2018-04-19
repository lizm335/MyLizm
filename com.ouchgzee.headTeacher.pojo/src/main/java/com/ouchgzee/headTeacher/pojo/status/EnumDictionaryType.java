package com.ouchgzee.headTeacher.pojo.status;


/**
 * @Function: 字典类型枚举类型 dir 目录，data 数据
 * @ClassName: EnumDictionaryType 
 * @date: 2016年4月17日 下午10:05:49 
 *
 * @author  zhy
 * @version V2.5
 * @since   JDK 1.6
 */
@Deprecated public enum EnumDictionaryType implements BaseEnum {
	DIR("目录"), DATA("数据");

	private String label;

	private EnumDictionaryType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
