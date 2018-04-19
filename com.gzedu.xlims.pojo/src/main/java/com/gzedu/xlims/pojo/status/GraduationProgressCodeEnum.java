package com.gzedu.xlims.pojo.status;

/**
 * 毕业论文进度码
 * @author eenet09
 *
 */
public enum GraduationProgressCodeEnum {
	
	//论文进度码
	THESIS_APPLY("申请", "1001"),
	THESIS_STAY_OPEN("分配指导老师", "2001"),
	THESIS_SUBMIT_PROPOSE("上传开题报告", "3001"),
	THESIS_CONFIRM_PROPOSE("开题报告定稿", "3002"),
	THESIS_SUBMIT_THESIS("提交初稿", "4001"),
	THESIS_TEACHER_CONFIRM_THESIS("指导老师定稿", "4002"),
	THESIS_COLLEGE_CONFIRM_THESIS("学院定稿", "4003"),
	THESIS_REVIEW("初评", "5001"),
	THESIS_STAY_DEFENCE("答辩安排", "5002"),
	THESIS_DEFENCE("答辩", "5003"),
	THESIS_COMPLETED("完成", "0000"),
	
	//社会实践进度码
	PRACTICE_APPLY("申请", "1001"),
	PRACTICE_STAY_OPEN("分配指导老师", "2001"),
	PRACTICE_SUBMIT_PRACTICE("提交初稿", "4001"),
	PRACTICE_CONFIRM_PRACTICE("定稿", "4002"),
	PRACTICE_REVIEW("初评", "5001"),
	PRACTICE_COMPLETED("完成", "0000");
	
	private String name;
	
	private String code;
	
	private GraduationProgressCodeEnum(String name, String code) {
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
	
	/**
	 * 论文进度码
	 * @author eenet09
	 *
	 */
	public enum ThesisProgressCodeEnum {
		
		THESIS_APPLY("申请", "1001"),
		THESIS_STAY_OPEN("分配指导老师", "2001"),
		THESIS_SUBMIT_PROPOSE("上传开题报告", "3001"),
		THESIS_CONFIRM_PROPOSE("开题报告定稿", "3002"),
		THESIS_SUBMIT_THESIS("提交初稿", "4001"),
		THESIS_TEACHER_CONFIRM_THESIS("指导老师定稿", "4002"),
		THESIS_COLLEGE_CONFIRM_THESIS("学院定稿", "4003"),
		THESIS_REVIEW("初评", "5001"),
		THESIS_DEFENCE("答辩", "5002"),
		THESIS_COMPLETED("完成", "0000");
		
		private String name;
		
		private String code;
		
		private ThesisProgressCodeEnum(String name, String code) {
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
	
	/**
	 * 社会实践进度码
	 * @author eenet09
	 *
	 */
	public enum PracticeProgressCodeEnum {
		
		PRACTICE_APPLY("申请", "1001"),
		PRACTICE_STAY_OPEN("分配指导老师", "2001"),
		PRACTICE_SUBMIT_PRACTICE("提交初稿", "4001"),
		PRACTICE_CONFIRM_PRACTICE("定稿", "4002"),
		PRACTICE_REVIEW("初评", "5001"),
		PRACTICE_COMPLETED("完成", "0000");
		
		private String name;
		
		private String code;
		
		private PracticeProgressCodeEnum(String name, String code) {
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

}
