package com.gzedu.xlims.pojo.status;
/**
 * 功能说明：学籍异动状态
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年12月16日
 * @version 2.5
 */
public enum SchoolRollTransTypeEnum {
	待审核(0),通过(1),不通过(2),劝学中(3),劝学失败(4),劝学成功(5),待核算(6),已核算(7),待确认(8),已确认(9),
	待登记(10),已登记(11),退学成功 (12),撤销退学 (13),待缴费(14),已缴费(15),待上传(16),已上传(17);
	 private int value;
	 
	 SchoolRollTransTypeEnum(int value) {
	        this.value = value;
	    }
	 
	 public static String getName(int number) {
	        for (SchoolRollTransTypeEnum e : SchoolRollTransTypeEnum.values()) {
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
