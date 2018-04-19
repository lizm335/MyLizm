/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.employee;

import com.ouchgzee.headTeacher.dto.CourseTeacherDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.base.BaseService;

import java.util.List;

/**
 * 班主任个人信息操作接口<br>
 * 功能说明：
 * 
 * @author 李建华 lijianhua@gzedu.net
 * @Date 2016年5月10日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtEmployeeInfoService extends BaseService<BzrGjtEmployeeInfo> {

	/**
	 * 根据登录账号获取教职工信息
	 *
	 * @param loginAccount
	 * @return
	 */
	BzrGjtEmployeeInfo queryByLoginAccount(String loginAccount);

	/**
	 * 根据教职工ID获取教学计划ID，班级ID
	 *
	 * @param employeeId
	 * @return
	 */
	List<Object[]> queryTachPlanClassByEmpId(String employeeId);

	/**
	 * 获取教学班下面的课程班的辅导老师信息
	 * @param classId
	 * @return
     */
	List<CourseTeacherDto> queryCourseTeacherByTeachClassId(String classId);

	/**
	 * 更新班主任个人信息
	 * 
	 * @param gjtEmployeeInfo
	 * @return
	 */
	boolean update(BzrGjtEmployeeInfo gjtEmployeeInfo);

	/**
	 * 修改账号密码
	 * 
	 * @param accountId
	 *            账号ID
	 * @param password
	 *            新密码
	 * @param oldPassword
	 *            原密码
	 * @param updatedBy
	 *            修改人
	 * @return 返回码 1-成功 0-失败 -1-原密码不正确
	 */
	int updatePassword(String accountId, String password, String oldPassword, String updatedBy);

	/**
	 * 更换头像
	 *
	 * @param employeeId
	 *            教职工ID
	 * @param photoUrl
	 *            教职工头像
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updatePhoto(String employeeId,String photoUrl, String updatedBy);

	public List<BzrGjtEmployeeInfo> findListByType(int type, int subType, String orgId);

}
