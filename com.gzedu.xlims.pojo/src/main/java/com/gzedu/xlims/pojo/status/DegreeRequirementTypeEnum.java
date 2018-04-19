package com.gzedu.xlims.pojo.status;

/**
 * 学位条件类型
 * @author eenet09
 *
 */
public enum DegreeRequirementTypeEnum {
	
	COMPULSORY_AVG("必修课平均分", 1),
	OTHER_AVG("其他课程平均分", 2),
	DEGREE_SCORE("学位课程成绩", 3),
	ENGLISH_SCORE("学位英语要求",4),
	DESIGN_SCORE("毕业设计(论文)要求", 5),
	PAPER_FEE("学位论文指南学分费用",6);
	
	private String name;
	
	private int value;
	
	public static String getName(int value) {
		for (DegreeRequirementTypeEnum type : DegreeRequirementTypeEnum.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}

	private DegreeRequirementTypeEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
