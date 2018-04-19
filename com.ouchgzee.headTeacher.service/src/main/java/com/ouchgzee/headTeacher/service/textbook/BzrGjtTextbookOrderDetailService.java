package com.ouchgzee.headTeacher.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookOrderDetail;

@Deprecated public interface BzrGjtTextbookOrderDetailService {

	public Page<Map<String, Object>> findAllSummary(Map<String, Object> searchParams, PageRequest pageRequst);

	public Page<BzrGjtTextbookOrderDetail> findAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public List<BzrGjtTextbookOrderDetail> findAll(List<String> ids);

	public BzrGjtTextbookOrderDetail insert(BzrGjtTextbookOrderDetail entity);

	public void insert(List<BzrGjtTextbookOrderDetail> entities);

	public void update(BzrGjtTextbookOrderDetail entity);

	public void update(List<BzrGjtTextbookOrderDetail> entities);

	public BzrGjtTextbookOrderDetail findOne(String id);

	public void delete(BzrGjtTextbookOrderDetail entity);

	public void delete(List<BzrGjtTextbookOrderDetail> entities);

	public List<BzrGjtTextbookOrderDetail> findByStudentIdAndTextbookIdAndStatus(String studentId, String textbookId,
																				 int status);

	public BzrGjtTextbookOrderDetail findByStudentIdAndTextbookIdAndCourseIdAndPlanId(String studentId, String textbookId,
																					  String courseId, String planId);

}
