package com.gzedu.xlims.pojo.comm;
/**
 * 审核类型
 * @author lyj
 * @time 2017年7月27日 
 * TODO
 */
public enum OperateType {
	
	GRANT_COURSE_PLAN_AUDIT(0,"授课计划审核"),
	GRANT_COURSE_SCORE_AUDIT(1,"授课成绩审核");
	
	private OperateType(int type, String name) {
		this.type = type;
		this.name = name;
	}
	private int type;
	
	private String name;
	
	public static OperateType getItem(int type) {
		for(OperateType t : OperateType.values()) {
			if(t.getType() == type) {
				return t;
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
