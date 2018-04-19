package com.gzedu.xlims.service.feedback;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualReply;
import com.gzedu.xlims.pojo.LcmsMutualSubject;

public interface LcmsMutualReplyService {

	public Page<LcmsMutualReply> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst);

	public long queryAllCount(Map<String, Object> searchParams);

	public LcmsMutualReply save(LcmsMutualReply item);

	public LcmsMutualReply queryById(String id);

	public boolean update(LcmsMutualReply item);

	public boolean deleteBySubjectId(String id);

	public LcmsMutualReply queryByPid(String pid);

	public LcmsMutualReply queryPidBySubjectId(String pid);

	/**
	 * 学员追问接口
	 * 
	 * @param imgUrls
	 * @param pid
	 * @param user
	 * @param content
	 * @return
	 */
	public LcmsMutualReply save(String[] imgUrls, String pid, GjtUserAccount user, String content);

	/**
	 * 微信平台，老师提问，管理回答，这个是老师追问接口
	 * 
	 * @param imgUrls
	 * @param pid
	 * @param user
	 * @param content
	 * @return
	 */
	public LcmsMutualReply teachSave(String[] imgUrls, String pid, GjtUserAccount user, String content);

	/**
	 * 自己添加常见问题答疑回复（老师回复端）
	 * 
	 * @param imgUrls2
	 * @param subject
	 * @param user
	 * @param content2
	 * @param title
	 * @return
	 */
	public LcmsMutualReply saveOftenReply(String[] imgUrls2, LcmsMutualSubject subject, GjtUserAccount user,
			String content2, String title);

	/**
	 * 老师回复学员答疑
	 * 
	 * @param imgs
	 * @param pid
	 * @param entity
	 * @param user
	 * @param content
	 * @param is_comm
	 * @param oftenType
	 * @return
	 */
	public LcmsMutualReply teachReplyStudentSave(String[] imgs, String pid, LcmsMutualSubject entity,
			GjtUserAccount user, String content, String is_comm, String oftenType, String isCommendType);

	public LcmsMutualReply updateOftenReply(String replyId, String content2, String[] imgUrls2);
}