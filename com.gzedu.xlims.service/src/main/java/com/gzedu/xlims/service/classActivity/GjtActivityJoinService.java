/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.classActivity;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtActivityJoin;
import com.gzedu.xlims.pojo.dto.ActivityJoinDto;
import com.gzedu.xlims.pojo.dto.GjtActivityDto;

/**
 * 
 * 功能说明：活动参与接口
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
public interface GjtActivityJoinService {

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
	public List<ActivityJoinDto> getActivityStudentsInfo(String activityId, String auditStatus);

	public Page<ActivityJoinDto> getActivityStudentsInfoPage(String activityId, PageRequest pageRequest);

	// 获取学员报名的活动列表
	public Page<GjtActivityDto> getActivityByStudent(Map<String, Object> map, PageRequest pageRequest);

	/**
	 * 获取学员报名的活动ID
	 * 
	 * @param activityId
	 * @param auditStatus
	 * @return
	 */
	public List<GjtActivityJoin> getActivityIdByStudentId(String studentId);

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
	public void updateBatchAuditActivityBystudentId(String activityId, String auditStatus, String studentId);

	/**
	 * 审核不通过的活动
	 * 
	 * @param activityId
	 * @return
	 */
	public void updateBatchAuditActivitytoUnpass(String activityId, String auditStatus, String studentId);

	GjtActivityJoin queryById(String studentId, String activityId);

	Boolean insert(GjtActivityJoin entity);
}
