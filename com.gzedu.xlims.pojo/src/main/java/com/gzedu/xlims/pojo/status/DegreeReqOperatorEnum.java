package com.gzedu.xlims.pojo.status;

public enum DegreeReqOperatorEnum {
	EQ(1, "等于", "="), LG(2, "大于", ">"), LGE(3, "大于等于", ">="), LT(4, "小于", "<"), LTE(5, "小于等于", "<=");

	private String name;
	private int value;
	private String opertor;

	private DegreeReqOperatorEnum(int value, String name, String opertor) {
		this.name = name;
		this.value = value;
		this.opertor = opertor;
	}

	public String getName() {
		return this.name;
	}

	public int getValue() {
		return this.value;
	}

	public String getOpertor() {
		return this.opertor;
	}

	public static String getOpertorByValue(int value) {
		for (DegreeReqOperatorEnum e : DegreeReqOperatorEnum.values()) {
			if (e.getValue() == value) {
				return e.getOpertor();
			}
		}
		return null;
	}
}
