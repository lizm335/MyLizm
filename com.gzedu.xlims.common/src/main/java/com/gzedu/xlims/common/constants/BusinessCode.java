package com.gzedu.xlims.common.constants;

/**
 * 业务码定义：<br>
 * 由7位数字组成，<br>
 * 第一位代表系统：1-教学教务管理后台，2-学支平台，3-学习空间<br>
 * 第二、三位代表模块，从01~99<br>
 * 第四、五位代表接口，从01~99<br>
 * 第六、七位代表每个接口里的错误识别码，从01~99<br>
 * 例如，3010101的解释如下：<br>
 * 3 - 代表了学习空间<br>
 * 01 - 代表这是论文写作模块<br>
 * 01 - 代表查询当前论文计划的接口<br>
 * 01 - 这个接口定义的错误识别码<br>
 * 这个错误码的意思是：调用“学习空间-论文写作模块-查询当前论文计划”接口时返回“当前学期没有论文计划”的错误。
 * 
 * @author lzj
 *
 */
public enum BusinessCode {
	
	/* 论文写作模块 start */
	STUDY_THESIS_PLAN_NOT_FOUND(3010101, "当前学期没有论文计划"),
	
	STUDY_THESIS_APPLY_NOT_FOUND(3010201, "找不到学员的论文申请"),
	
	STUDY_THESIS_APPLY_THESIS_NOT_FOUND(3010301, "论文计划Id有误"),
	STUDY_THESIS_APPLY_HAD_APPLY(3010302, "论文写作已申请"),
	STUDY_THESIS_APPLY_COURSE_NOT_FOUND(3010303, "没有这门课程"),
	STUDY_THESIS_APPLY_NOT_IN_TIME(3010304, "申请时间未到"),

	STUDY_THESIS_CANCEL_APPLY_NOT_FOUND(3010401, "找不到申请"),

	STUDY_THESIS_UPDATE_DEGREE_APPLY_NOT_FOUND(3010501, "找不到申请"),
	
	STUDY_THESIS_QUERY_EXAMPLE_THESIS_NOT_FOUND(3010601, "论文计划Id有误"),

	STUDY_THESIS_SUBMIT_PROPOSE_APPLY_NOT_FOUND(3010701, "找不到申请"),
	STUDY_THESIS_SUBMIT_PROPOSE_NO_OPERATION(3010702, "无此操作"),

	STUDY_THESIS_QUERY_GUIDERECORD_APPLY_NOT_FOUND(3010801, "找不到申请"),

	STUDY_THESIS_SUBMIT_THESIS_APPLY_NOT_FOUND(3010901, "找不到申请"),

	STUDY_THESIS_SUBMIT_EXPRESS_APPLY_NOT_FOUND(3011101, "找不到申请"),

	STUDY_THESIS_QUERY_LOGISTICS_ERROR(3011201, "查询物流信息异常"),

	STUDY_THESIS_QUERY_DEFENCEPLAN_APPLY_NOT_FOUND(3011301, "找不到申请"),

	STUDY_THESIS_QUERY_DEFENCEPLAN_NO_PLAN(3011302, "没有答辩安排"),

	STUDY_THESIS_SEND_THESIS_APPLY_NOT_FOUND(3011401, "找不到申请"),
	/* 论文写作模块 end */

	/* 社会实践写作模块 start */
	STUDY_PRACTICE_PLAN_NOT_FOUND(3020101, "当前学期没有实践计划"),
	
	STUDY_PRACTICE_APPLY_NOT_FOUND(3020201, "找不到学员的实践申请"),
	
	STUDY_PRACTICE_APPLY_PRACTICE_NOT_FOUND(3020301, "实践计划Id有误"),
	STUDY_PRACTICE_APPLY_HAD_APPLY(3020302, "实践写作已申请"),
	STUDY_PRACTICE_APPLY_COURSE_NOT_FOUND(3020303, "没有这门课程"),
	STUDY_PRACTICE_APPLY_NOT_IN_TIME(3020304, "申请时间未到"),

	STUDY_PRACTICE_CANCEL_APPLY_NOT_FOUND(3020401, "找不到申请"),

	STUDY_PRACTICE_SUBMIT_PRACTICE_APPLY_NOT_FOUND(3020501, "找不到申请"),

	STUDY_PRACTICE_QUERY_GUIDERECORD_APPLY_NOT_FOUND(3020601, "找不到申请"),

	STUDY_PRACTICE_SUBMIT_EXPRESS_APPLY_NOT_FOUND(3020801, "找不到申请"),

	STUDY_PRACTICE_SEND_PRACTICE_APPLY_NOT_FOUND(3021001, "找不到申请");
	/* 社会实践写作模块 end */
    
	private int busCode;
	
	private String message;

	BusinessCode(int busCode, String message) {
		this.busCode=busCode;
		this.message=message;
	}

	public int getBusCode() {
		return busCode;
	}

	public void setBusCode(int busCode) {
		this.busCode = busCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
