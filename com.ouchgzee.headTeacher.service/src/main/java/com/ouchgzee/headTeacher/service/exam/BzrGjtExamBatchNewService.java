package com.ouchgzee.headTeacher.service.exam;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;

@Deprecated public interface BzrGjtExamBatchNewService {

	public Page<BzrGjtExamBatchNew> queryAll(final String schoolId, Map<String, Object> searchParams,
											 PageRequest pageRequst);


	public BzrGjtExamBatchNew queryBy(String id);
	
	public BzrGjtExamBatchNew findCurrentExamBatch(String xxId);
	
	public BzrGjtExamBatchNew findLastExamBatch(String xxId);
	
	public BzrGjtExamBatchNew findByExamBatchCode(String examBatchCode);

}
