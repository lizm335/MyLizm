/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.classActivity;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtActivity;

/**
 * 
 * 功能说明：班级活动接口
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
public interface GjtActivityService {

	/**
	 * 分页根据条件查询班级的活动信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<GjtActivity> queryActivityInfoPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 进行中的活动
	 * 
	 * @param endTimeStr
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
	public String addActivity(GjtActivity gjtActivity);

	/**
	 * 班级活动修改一条活动
	 * 
	 * @param gjtActivity
	 * @return 0:修改失败，1修改成功，-1修改有误
	 */
	public String updateActivity(GjtActivity gjtActivity);

	/**
	 * 班级活动删除一条活动
	 * 
	 * @param gjtActivity
	 * @return 0:删除失败，1删除成功，-1删除有误
	 */
	public String deleteActivity(GjtActivity gjtActivity);

	/**
	 * 待审核活动数量 参数：无 返回的结果：活动数long
	 * 
	 * @return 活动数
	 */
	long countWaitActivityNum();

	GjtActivity queryById(String id);

	Boolean insert(GjtActivity entity);
}
