/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.message;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrGjtMessageInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageUser;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 消息业务接口<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月9日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtMessageService extends BaseService<BzrGjtMessageInfo> {

	Page<BzrGjtMessageInfo> findAll(Map<String, Object> searchParams, PageRequest pageRequst);

	long findAllCount(Map<String, Object> searchParams);

	/**
	 * 更新消息信息
	 * 
	 * @param messageInfo
	 * @return
	 */
	boolean update(BzrGjtMessageInfo messageInfo);

	/**
	 * 消息标记为已读
	 * 
	 * @param messageId
	 *            消息ID
	 * @param receiveUser
	 *            阅读人
	 * @return
	 */
	boolean updateMessageRead(String messageId, String receiveUser);

	/**
	 * 删除消息信息
	 *
	 * @param messageId
	 *            消息ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean delete(String messageId, String updatedBy);

	/**
	 * 批量删除消息信息
	 *
	 * @param messageIds
	 *            消息ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean delete(String[] messageIds, String updatedBy);

	/**
	 * 分页根据条件查询班主任的消息信息/发送人信息
	 * 
	 * @param messageInfo
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtMessageInfo> queryHeadTeacherMessageInfoByPage(BzrGjtMessageInfo messageInfo, PageRequest pageRequest);

	/**
	 * 根据条件统计班主任的消息信息
	 * 
	 * @param messageInfo
	 * @return
	 */
	long countHeadTeacherMessageInfo(BzrGjtMessageInfo messageInfo);

	/**
	 * 分页根据条件查询班主任发布的班级消息信息/阅读情况
	 * 
	 * @param messageInfo
	 * @param classId
	 * @param putUser
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtMessageInfo> queryClassMessageInfoByPage(BzrGjtMessageInfo messageInfo, String classId, String putUser,
														PageRequest pageRequest);

	/**
	 * 根据条件统计班主任发布的班级消息信息
	 * 
	 * @param messageInfo
	 * @param classId
	 * @param putUser
	 * @return
	 */
	long countClassMessageInfo(BzrGjtMessageInfo messageInfo, String classId, String putUser);

	/**
	 * 根据ID查询消息信息及阅读情况
	 * 
	 * @param messageId
	 * @return
	 */
	BzrGjtMessageInfo queryMessageReadSituationById(String messageId);

	/**
	 * 分页根据条件获取督促提醒信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtMessageInfo> queryRemindMessageInfoByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 添加督促提醒
	 * 
	 * @param messageInfo
	 * @param orgCode
	 * @param isSendEE
	 *            是否发送EE
	 * @param isSendSMS
	 *            是否发送短信
	 * @return
	 */
	boolean insertRemind(BzrGjtMessageInfo messageInfo, String orgCode, boolean isSendEE, boolean isSendSMS);

	boolean insertRemind(String teacherEENo, BzrGjtMessageInfo messageInfo, List<String> userIdList, boolean isSendEE,
						 boolean isSendSMS);

	/**
	 * 重新发送督促提醒
	 *
	 * @param messageId
	 *            消息ID
	 * @param updatedBy
	 *            修改人
	 * @param orgCode
	 * @param isSendEE
	 * @param isSendSMS
	 * @return
	 */
	boolean resend(String employeeId, String messageId, String updatedBy, String orgCode, boolean isSendEE,
			boolean isSendSMS);

	public boolean insert(BzrGjtMessageInfo messageInfo, List<BzrGjtMessageUser> gjtMessageUserList);
}
