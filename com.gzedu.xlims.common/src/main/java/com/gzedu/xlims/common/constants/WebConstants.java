package com.gzedu.xlims.common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * web 系统常用常量
 */
public class WebConstants {

	/** 当前登录用户 */
	public final static String CURRENT_USER = "current_user";
	public final static String STUDENT_INFO = "student_info";
	public final static String TEACH_CLASS = "teach_class";

	/** 当前登录用户 角色：教师 */
	public final static String CURRENT_USER_TEACHER = "current_user_teacher";

	public final static int EXPIRE = 7200;

	/** excel导入模板文件路径 */
	public final static String EXCEL_MODEL_URL = "/excel/model/";

	/** excel导入结果存放路径 */
	public final static String EXCEL_DOWNLOAD_URL = "/tmp/download/";

	public final static String SUCCESSFUL = "successful";
	public final static String MESSAGE = "message";

	/**
	 * 院校模式标识
	 */
	public final static String COLLEGE_MODEL_TAG = "college_model_tag";

	/**
	 * 国开实验学院的机构ID，院校模式暂用国开的课程
	 */
	public final static String GK_ORG_ID = "2f5bfcce71fa462b8e1f65bcd0f4c632";

	public final static int MAX_TEARM = 6;

	public final static Integer EXAM_WK_TYPE = 1;
	public final static Integer EXAM_BS_TYPE = 2;
	public final static Integer EXAM_JK_TYPE = 3;
	public final static Integer EXAM_XK_TYPE = 4;
	public final static Integer EXAM_WKS_TYPE = 5;
	public final static Integer EXAM_GK_TYPE = 6;
	public final static Integer EXAM_SK_TYPE = 7;
	public final static Integer EXAM_LW_TYPE = 8;
	public final static Integer EXAM_BG_TYPE = 9;
	public final static Integer EXAM_ZY_TYPE = 10;
	public final static Integer EXAM_LG_TYPE = 11;

	/** 未同步 */
	public final static Integer EXAM_SUBJECT_NEW = 0;
	/** 已同步 */
	public final static Integer EXAM_SUBJECT_SYNC = 1;
	/** 制作中 */
	public final static Integer EXAM_SUBJECT_MAKING = 2;
	/** 已发布 */
	public final static Integer EXAM_SUBJECT_PUBLISHED = 3;

	/** 个人考试预约上限(如果后续有多间学校且各自有不同限制, 则改成读缓存或者配置等其他方式) */
	public final static Integer EXAM_APPOINTMENT_LIMIT = 8;

	public final static Map<Integer, String> examTypeMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(1, "7");
			put(2, "8");
			put(3, "11");
			put(4, "13");
			put(5, "19");
			put(6, "17");
			put(7, "18");
			put(8, "14");
			put(9, "15");
			put(10, "20");
			put(11, "21");
		}
	};
}
