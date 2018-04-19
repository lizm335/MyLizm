/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.signup;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.gzedu.xlims.pojo.GjtSignup;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月19日
 * @version 1.0
 *
 */
public interface GjtSignupService extends BaseService<GjtSignup> {

	/**
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtSignup> queryPageList(String xxId,Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 导出批量下载学生报读资料<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param searchParams
	 * @param sort
	 * @return Excel文件流
	 */
	HSSFWorkbook exportInfo(String xxId,Map<String, Object> searchParams, Sort sort);

	/**
	 * 获取学员的证件资料
	 * @param studentId
	 * @return
	 */
	Map<String, String> getSignupCopyData(String studentId);

	/**
	 * 审核报读资料
	 * @param studentId
	 * @param auditState
	 * @param auditOpinion
	 * @param updatedBy
     * @return
     */
	boolean auditSignupData(String studentId, String auditState, String auditOpinion, String updatedBy);
	/**
	 * 查询各状态的总数量
	 * @param xxId
	 * @param auditState
	 * @param searchParams 
	 * @return
	 */
	public long queryAuditStateTotalNum(String xxId,String auditState, Map<String, Object> searchParams);
	/**
	 * 根据学员ID查询学员报读信息的相关信息
	 */
	GjtSignup querySignupBySignupId(String xxId, String id);
	/**
	 * 根据条件统计学员报读资料统计
	 * @param id
	 * @param searchParams
	 * @return
	 */
	List<Map> querySignupNums(String id, Map<String, Object> searchParams);
	/**
	 * 根据条件统计学员报读缴费统计
	 * @param id
	 * @param searchParams
	 * @return
	 */
	List<Map> querySignupPayCostNum(String id, Map<String, Object> searchParams);
	
	/**
	 * 更新学员证件资料
	 * @param studentId
	 * @param fileType
	 * @param url
	 * @return
	 */
	boolean updateSignupCopyData(String studentId, String fileType, String url);

	/**
	 * 批量更新学员证件资料
	 * @param studentId
	 * @param signupCopyData
	 */
	boolean updateSignupCopyData(String studentId, Map<String, String> signupCopyData);

	GjtSignup querySignupByStudentId(String studentId);

	/**
	 * 更新各证件类型
	 * @param studentId
	 * @param signupSfzType
	 * @param zgxlRadioType
	 * @param signupByzType
	 * @param signupJzzType
     * @return
     */
	boolean updateEveryType(String studentId, String signupSfzType, String zgxlRadioType, String signupByzType, String signupJzzType);
}
