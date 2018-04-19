package com.ouchgzee.headTeacher.service.message;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrGjtMessageUser;

@Deprecated public interface BzrGjtMessageUserService {

	public BzrGjtMessageUser queryById(String id);

	public List<BzrGjtMessageUser> queryByMessageId(String messageId);

	// 查询信息发送的所有用户
	Page<Map> queryAllByMessageId(Map<String, Object> searchParams, PageRequest pageRequst);

	public void delete(String ids[]);

	public void delete(String id);

	public void insert(BzrGjtMessageUser entity);

	public void insert(List<BzrGjtMessageUser> list);

	public void save(Set<String> lists, String CreatedName, String messageId);

	public void update(BzrGjtMessageUser entity);

	public Page<BzrGjtMessageUser> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	long queryAllCount(Map<String, Object> searchParams);

	/**
	 * 标记为已读
	 * 
	 * @param messageId
	 * @param userId
	 * @return
	 */
	boolean updateRead(String messageId, String userId);

	// 查询发送总记录数
	List<Object[]> queryPutMessageIds(String... messageId);

	// 查询已读的总记录数
	List<Object[]> queryReadMessageIds(String... messageId);
}
