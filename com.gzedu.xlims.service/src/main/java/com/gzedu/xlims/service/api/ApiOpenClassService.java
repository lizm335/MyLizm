package com.gzedu.xlims.service.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ApiOpenClassService {
	
	/***
	 * 初始化学员选课记录
	 * @param
	 * @return
	 */
	public Map initStudentChoose(Map formMap);
	
	/**
	 * 恢复旧的学习记录
	 * @param formMap
	 * @return
	 */
	public Map stuOldToNewRec(Map formMap);
	
	/***
	 * 初始化期课程记录
	 * @param
	 * @return
	 */
	public Map initTermCourse(Map formMap);
	
	/***
	 * 初始化调班数据
	 * @param
	 * @return
	 */
	public Map initCourseClass(Map formMap);
	
	/***
	 * 初始化课程班级的辅导老师和督导老师数据
	 * @param
	 * @return
	 */
	public Map initCourseTeacher(Map formMap);
	
	/***
	 * 检查同步期课程班级是否同步到学习平台
	 * @param
	 * @return
	 */
	public Map syncTermClass(Map formMap);
	
	/***
	 * 教学计划保存的时候初始化学员的选课信息
	 * @param
	 * @return
	 */
	public Map initPlanStuRec(Map formMap);
	
	/***
	 * 开课完成初始化学员的选课（点击开课流程的时候调用）
	 * @param
	 * @return
	 */
	public Map initTermcourseStuRec(Map formMap);
	
	/***
	 * 初始化课程数据
	 * @param
	 * @return
	 */
	public Map initCourseInfo(Map formMap);
	
	/***
	 * 定时调用
	 * 1、选课数据不足的补齐
	 * 2、删除多余的选课信息
	 * 3、删除开课为0、班级为0 的数据
	 * 4、学习平台的选课数据保持一致
	 */
	public Map initStudentRec(Map formMap);
}
