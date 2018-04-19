/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.usermanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * 
 * 功能说明：教职工
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月27日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtEmployeeInfoService {

	public Page<GjtEmployeeInfo> queryAll(String schoolId, Map<String, Object> map, PageRequest pageRequest,
			Integer employeeType);

	public List<GjtEmployeeInfo> queryAll(Integer employeeType);

	List<GjtEmployeeInfo> queryAllNotClassEmployee(Integer employeeType, String classId, String xx_id);

	// 添加职员信息
	public Boolean saveEntity(GjtEmployeeInfo employeeInfo);

	/**
	 * 添加教职工
	 * 
	 * @param item
	 * @param gjtUserAccount
	 * @param employeeType
	 * @return
	 */
	GjtEmployeeInfo saveEntity(GjtEmployeeInfo item, GjtUserAccount gjtUserAccount, String employeeType);

	/**
	 * 添加教职工
	 * 
	 * @param item
	 * @param gjtUserAccount
	 * @param employeeType
	 * @param positions
	 * @return
	 */
	GjtEmployeeInfo saveEntity(GjtEmployeeInfo item, GjtUserAccount gjtUserAccount, String employeeType,
			String[] positions);

	// 修改职员信息
	public Boolean updateEntity(GjtEmployeeInfo employeeInfo);

	// 查询职员信息
	public GjtEmployeeInfo queryById(String id);

	public List<Map<String, String>> queryAllEmployeeByOrgId(String schoolId, Integer employeeType);

	// 重置密码
	public Boolean updatePwd(String id);

	// 删除（假）
	public Boolean deleteById(String[] id);

	public Boolean deleteById(String id);

	// 删除（真）
	public void delete(String id);

	// 是否启用停用
	public Boolean updateIsEnabled(String id, String str);

	public List<GjtEmployeeInfo> findListByType(int type, int subType, String orgId);

	public GjtEmployeeInfo findOneByAccountId(String accountId);

	/**
	 * 论文老师新增权限
	 * 
	 * @param accountId
	 * @param createdBy
	 * @return
	 */
	boolean updateLunwenTeacherAddPermissions(String accountId, String createdBy);

	/**
	 * 根据班级id 查询班级的班主任
	 * 
	 * @param classId
	 * @return
	 */
	Map<String, String> queryByClassId(String classId);

	/**
	 * 根据STUDENT_ID查询班主任信息
	 * 
	 * @param studentId
	 * @return
	 */
	public Map<String, Object> queryHeadTeacherInfo(String studentId);

	public GjtEmployeeInfo queryByAccountId(String accountId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月22日 下午7:13:03
	 * @param type
	 * @param orgId
	 * @return
	 */
	List<GjtEmployeeInfo> findListByType(String type, String orgId);

	void deletePositionByEmployeeId(String employeeId);

	void addPosition(String employeeId, String type);
}
