package com.gzedu.xlims.pojo.status;

/**
 * 文档类型<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @CreateDate 2016年11月11日
 * @EditDate 2016年11月11日
 * @version 2.5
 */
public enum DocumentTypeEnum {

	// 文档类型
	DOCUMENT_TYPE01(1, "自我鉴定示例文档");

	private int code;

	private String name;

	private DocumentTypeEnum(int code, String name) {
		this.name = name;
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static DocumentTypeEnum getItem(int value) {
		for (DocumentTypeEnum item : values()) {
			if (item.getCode() == value)
				return item;
		}
		return null;
	}

}
