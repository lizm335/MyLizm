/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.message;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.message.GjtMessageUser;

/**
 * 功能说明：用户信息
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月11日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtMessageUserService {
	Page<GjtMessageUser> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	List<GjtMessageUser> queryAll(Map<String, Object> searchParams);

	Page<Map> queryAllByMessageId(Map<String, Object> searchParams, PageRequest pageRequst);

	long queryAllCount(Map<String, Object> searchParams);

	public GjtMessageUser queryById(String id);

	public GjtMessageUser queryByUserIdAndMessageId(String userId, String messageId);

	// 查询发送总记录数
	List<Object[]> queryPutMessageIds(List<String> messageId);

	List<Object[]> findReadMessageIds(List<String> messageId);

	// 查询已读的总记录数
	List<Object[]> queryReadMessageIds(List<String> messageId);

	public void delete(String ids[]);

	public void delete(String id);

	public void insert(GjtMessageUser entity);

	public void insert(List<GjtMessageUser> list);

	public void save(List<String> userLists, String userName, String messageId);

	public void update(GjtMessageUser entity);

	/**
	 * 点赞数
	 * 
	 * @param messageIds
	 * @return
	 */
	List<Object[]> queryLikeMessageIds(List<String> messageIds);

	/**
	 * 反馈数
	 * 
	 * @param messageIds
	 * @return
	 */
	List<Object[]> queryTicklingMessageIds(List<String> messageIds);

	/**
	 * 评论数
	 * 
	 * @param messageIds
	 * @return
	 */
	List<Object[]> queryCommMessageIds(List<String> messageIds);

	public long updateConstraint(String messageId);

	public long settingsIsConstraint(String id, String isConstraint);

	public List<Map<String, Object>> findEntityByMessageId(String messageId);

	/**
	 * 统计每种反馈
	 * 
	 * @param messageId
	 * @return
	 */
	List<Map<String, Object>> queryTicktingList(String messageId);

	public List<Map<String, Object>> findTypeCount(String userId);

	/**
	 * 查询自己的未读的重要信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> findImportanceList(String userId);

}
