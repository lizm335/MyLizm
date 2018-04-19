package com.ouchgzee.headTeacher.service.feedback;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrLcmsMutualReply;

@Deprecated public interface BzrLcmsMutualReplyService {

	public Page<BzrLcmsMutualReply> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst);

	public long queryAllCount(Map<String, Object> searchParams);

	public BzrLcmsMutualReply save(BzrLcmsMutualReply item);

	public BzrLcmsMutualReply queryById(String id);

	public BzrLcmsMutualReply queryPidBySubjectId(String pId);

	public boolean update(BzrLcmsMutualReply item);

	public boolean deleteBySubjectId(String id);
}