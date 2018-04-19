/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.service.activity;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ouchgzee.headTeacher.dto.ActivityJoinDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtActivityJoin;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 活动参与接口<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月20日
 * @version 1.0
 *
 */
@Deprecated public interface BzrGjtActivityJoinService extends BaseService<BzrGjtActivityJoin> {

	/**
	 * 统计活动参加人数
	 * 
	 * @param id
	 * @param auditStatus
	 * @return
	 */
	long countApplyNum(final String id, final String auditStatus);

	/**
	 * 导出活动呢人员
	 * 
	 * @param activityId
	 * @return
	 */
	HSSFWorkbook exportActivityNumberToExcel(String activityId);

	/**
	 * 获取参与学生信息
	 * 
	 * @param activityId
	 * @return
	 */
	public List<ActivityJoinDto> getActivityStudentsInfo(String activityId,
			String auditStatus);

	/**
	 * 批量审核活动
	 * 
	 * @param activityId
	 * @return
	 */
	public void updateBatchAuditActivity(String activityId, String auditStatus);

	/**
	 * 单独审核学生的活动
	 * 
	 * @param activityId
	 * @return
	 */
	public void updateBatchAuditActivityBystudentId(String activityId,
			String auditStatus, String studentId);

	/**
	 * 审核不通过的活动
	 * 
	 * @param activityId
	 * @return
	 */
	public void updateBatchAuditActivitytoUnpass(String activityId,
			String auditStatus, String studentId);
}
