package com.gzedu.xlims.pojo.status;
/**
 * 功能说明：学籍异动类型
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月14日
 * @version 2.5
 */
public enum SchoolRollTransEnum {
	转专业(1),
	休学(2),
	复学(3),
	退学(4),
	信息更正(5),
	转学(6);
	
	 private int value;
	 
	 SchoolRollTransEnum(int value) {
	        this.value = value;
	    }
	 
	 public static String getName(int number) {
	        for (SchoolRollTransEnum e : SchoolRollTransEnum.values()) {
	            if (e.value == number) {
	                return e.name();
	            }
	        }
	        return "";
	    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}	 
}
