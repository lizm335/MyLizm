/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.serviceManager;

import com.ouchgzee.headTeacher.pojo.BzrGjtServiceInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceRecord;
import com.ouchgzee.headTeacher.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

/**
 * 服务记录管理业务接口<br>
 * 功能说明：
 * 
 * @author 李建华 lijianhua@gzedu.net
 * @Date 2016年5月09日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtServiceManagerService extends BaseService<BzrGjtServiceInfo> {
	
	/**
	 * 根据班级id及其他查询条件分页查询服务主题信息
	 *
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtServiceInfo> queryServiceInfoByClassIdPage(String classId, Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 未结束的服务
	 * @param classId
	 * @return
	 */
	long countUnoverNum(String classId);

	/**
	 * 已结束的服务
	 * @param classId
	 * @return
	 */
	long countOverNum(String classId);

	/**
	 * 新增一条服务主题信息
	 * 
	 * @param paramGjtServiceInfo
	 * @param paramGjtServiceRecord
	 * @param paramStudentIds
	 * @param openUser 操作人
	 * @return 0:新增失败，1新增成功，-1参数有误
	 */
	String addServiceInfo(BzrGjtServiceInfo paramGjtServiceInfo, BzrGjtServiceRecord paramGjtServiceRecord, String paramStudentIds, String openUser);

	/**
	 * 在服务主题下新增一条服务记录
	 *
	 * @param gjtServiceRecord
	 * @param openUser 操作人
	 * @return 0:新增失败，1新增成功，-1参数有误
	 */
	String addServiceRecord(BzrGjtServiceRecord gjtServiceRecord, String openUser);

	/**
	 * 结束服务
	 *
	 * @param serviceid
	 * @param openUser 操作人
	 * @return
	 */
	boolean over(String serviceid, String openUser);
	
}
