/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.service.activity;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrGjtActivity;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 班级活动接口<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月12日
 * @version 1.0
 *
 */
@Deprecated public interface BzrGjtActivityService extends BaseService<BzrGjtActivity> {

	/**
	 * 分页根据条件查询班级的活动信息
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtActivity> queryActivityInfoPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 进行中的活动
	 *
	 * @param searchParams
	 * @return
	 */
	long countUnoverNum(Map<String, Object> searchParams);

	/**
	 * 已结束的活动
	 * 
	 * @param endTimeStr
	 * @return
	 */
	long countOverNum(String endTimeStr, String classId);

	/**
	 * 班级活动新增一条活动
	 * 
	 * @param gjtActivity
	 * @return 0:新增失败，1新增成功，-1参数有误
	 */
	public String addActivity(BzrGjtActivity gjtActivity);

	/**
	 * 班级活动修改一条活动
	 * 
	 * @param gjtActivity
	 * @return 0:修改失败，1修改成功，-1修改有误
	 */
	public String updateActivity(BzrGjtActivity gjtActivity);

	/**
	 * 班级活动删除一条活动
	 * 
	 * @param gjtActivity
	 * @return 0:删除失败，1删除成功，-1删除有误
	 */
	public String deleteActivity(BzrGjtActivity gjtActivity);

	/**
	 * 待审核活动报名人数 参数：无 返回的结果：活动数long
	 *
	 * @param searchParams
	 * @return 活动报名人数
	 */
	long countWaitActivityStudentNum(Map<String, Object> searchParams);

}
