package com.ouchgzee.headTeacher.service.feedback;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrLcmsMutualSubject;

@Deprecated public interface BzrLcmsMutualSubjectService {

	public Page<BzrLcmsMutualSubject> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst);

	public long queryAllCount(Map<String, Object> searchParams);

	public BzrLcmsMutualSubject save(BzrLcmsMutualSubject item);

	public BzrLcmsMutualSubject queryById(String id);

	public boolean update(BzrLcmsMutualSubject item);

	public boolean delete(String id);

	// 更改常见问题状态
	public boolean updateIsComm(String id, String state);

	// 问题转回初始的地方
	public boolean updateForward(String id, String initialId);
}