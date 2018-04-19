/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.signup;

import java.util.Map;

import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * 学籍资料同步业务
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月21日
 * @version 3.0
 * @since JDK1.7
 */
public interface SignupDataAddService {

	/**
	 * 根据培养层次是否存在学籍，存在返回学员信息，同一学员只能报读一个专科/本科
	 * @param xxId
	 * @param sfzh
	 * @param pycc
     * @return
     */
	GjtStudentInfo queryExistsSignupDataByPycc(String xxId, String sfzh, String pycc);

	/**
	 * 报读缴费状态确认-删除预存学籍资料
	 * @param xxId
	 * @param sfzh
	 * @param pycc
	 * @param studentId
     * @return
     */
	boolean updateSignupDataByAwaitPayOrder(String xxId, String sfzh, String pycc, String studentId);

	/**
	 * 是否存在教学班，存在返回班级信息
	 * @param xxId
	 * @param className
     * @return
     */
	GjtClassInfo queryExistsClassName(String xxId, String className);

	/**
	 * 录入学籍
	 * @param entity
	 * @return successful-是否成功 type-错误类型 message-错误信息 obj-结果
     */
	Map<String, Object> addSignupData(GjtStudentInfo entity);

	/**
	 * 已缴费的学员生成账号、分配教学班
	 * @param studentId
	 * @return successful-是否成功 type-错误类型 message-错误信息 obj-结果
	 */
	Map<String, Object> addCreateUserAccountAndDivideIntoClasses(String studentId);
	
	/**
	 * 注销学籍，或删除未缴费学员
	 * @param studentId
	 * @param force 是否强制注销
	 * @return successful-是否成功 type-错误类型 message-错误信息 obj-结果
	 */
	Map<String, Object> revokedSignup(String studentId, boolean force);

	/**
	 * 报读成功且已缴费发送短信通知学员及班主任
	 * @param studentId
	 * @return
     */
	boolean signupDataToSms(final String studentId);

	/**
	 * 账号同步至运营平台
	 * @param studentId
	 * @return successful-是否成功 type-错误类型 message-错误信息 obj-结果
     */
	Map<String, Object> syncYunYingCenter(String studentId);

	/**
	 * 生成EE号
	 * @param studentId
	 * @return
	 */
	boolean createEENo(String studentId);

	/**
	 * 生成班主任EE号
	 * @param employeeId
	 * @return
	 */
	boolean createHeadTeacherEENo(String employeeId);

	/**
	 * 创建EE群（群以教学班ID为唯一标识），并加群成员，如果群不存在就创建再加群成员，如果群存在直接加群成员
	 * @param studentId
	 * @return
	 */
	boolean createGroupNoAndAddSingleStudent(String studentId);

	/**
	 * 创建EE群，并加入班级所有的成员，如果群不存在就创建再加群成员，如果群存在直接加群成员
	 * @param teachClassId
	 * @return
	 */
	boolean createGroupNoAndAddAllStudent(String teachClassId);
}
